//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.comm.pingpong.m;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import org.infogrid.comm.MessageEndpointIsDeadException;
import org.infogrid.comm.MessageSendException;
import org.infogrid.comm.pingpong.PingPongMessageEndpoint;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;

/**
 * In-memory implementation of PingPongMessageEndpoint. This implementation supports
 * the on-disk storage of a snapshot of this PingPongMessageEndpoint, and its recovery
 * later, with a special factory method for that purpose.
 * 
 * @param <T> the message type
 */
public class MPingPongMessageEndpoint<T>
        extends
            PingPongMessageEndpoint<T>
{
    private static final Log log = Log.getLogInstance( MPingPongMessageEndpoint.class ); // our own, private logger

    /**
     * Factory method.
     * 
     * @param exec the ScheduledExecutorService to schedule timed tasks
     * @return the created MPingPongMessageEndpoint
     * @param <T> the message type
     */
    public static <T> MPingPongMessageEndpoint<T> create(
            ScheduledExecutorService exec )
    {
        String name            = "MPingPongMessageEndpoint";
        long   deltaRespond    = theResourceHelper.getResourceLongOrDefault(   "DeltaRespond",   1000L );
        long   deltaResend     = theResourceHelper.getResourceLongOrDefault(   "DeltaResend",     500L );
        long   deltaRecover    = theResourceHelper.getResourceLongOrDefault(   "DeltaRecover",   5000L );
        double randomVariation = theResourceHelper.getResourceDoubleOrDefault( "RandomVariation", 0.02 ); // 2%

        List<T> messagesToBeSent = new ArrayList<T>();

        // it is advantageous if the recover time is larger than 4 times the respond time: that way, a
        // second RPC call can be successfully completed before returning to the parent RPC caller.
        
        MPingPongMessageEndpoint<T> ret = new MPingPongMessageEndpoint<T>(
                name,
                deltaRespond,
                deltaResend,
                deltaRecover,
                randomVariation,
                exec,
                -1,
                -1,
                null,
                messagesToBeSent );
        return ret;
    }

    /**
     * Factory method.
     *
     * @param name the name of the PingPongMessageEndpoint (for debugging only)
     * @param deltaRespond the number of milliseconds until this PingPongMessageEndpoint returns the token
     * @param deltaResend  the number of milliseconds until this PingPongMessageEndpoint resends the token if sending the token failed
     * @param deltaRecover the number of milliseconds until this PingPongMessageEndpoint decides that the token
     *                     was not received by the partner PingPongMessageEndpoint, and resends
     * @param randomVariation the random component to add to the various times
     * @param exec the ScheduledExecutorService to schedule timed tasks
     * @return the created MPingPongMessageEndpoint
     * @param <T> the message type
     */
    public static <T> MPingPongMessageEndpoint<T> create(
            String                   name,
            long                     deltaRespond,
            long                     deltaResend,
            long                     deltaRecover,
            double                   randomVariation,
            ScheduledExecutorService exec )
    {
        List<T> messagesToBeSent = new ArrayList<T>();

        MPingPongMessageEndpoint<T> ret = new MPingPongMessageEndpoint<T>(
                name,
                deltaRespond,
                deltaResend,
                deltaRecover,
                randomVariation,
                exec,
                -1,
                -1,
                null,
                messagesToBeSent );
        return ret;
    }

    /**
     * Factory method.
     *
     * @param name the name of the PingPongMessageEndpoint (for debugging only)
     * @param deltaRespond the number of milliseconds until this PingPongMessageEndpoint returns the token
     * @param deltaResend  the number of milliseconds until this PingPongMessageEndpoint resends the token if sending the token failed
     * @param deltaRecover the number of milliseconds until this PingPongMessageEndpoint decides that the token
     *                     was not received by the partner PingPongMessageEndpoint, and resends
     * @param randomVariation the random component to add to the various times
     * @param exec the ScheduledExecutorService to schedule timed tasks
     * @param lastSentToken the last token sent in a previous instantiation of this MessageEndpoint
     * @param lastReceivedToken the last token received in a previous instantiation of this MessageEndpoint
     * @param messagesSentLast the last set of Messages sent in a previous instantiation of this MessageEndpoint
     * @param messagesToBeSent the Messages to be sent from a previous instantiation of this MessageEndpoint
     * @return the created MPingPongMessageEndpoint
     * @param <T> the message type
     */
    public static <T> MPingPongMessageEndpoint<T> restore(
            String                   name,
            long                     deltaRespond,
            long                     deltaResend,
            long                     deltaRecover,
            double                   randomVariation,
            ScheduledExecutorService exec,
            long                     lastSentToken,
            long                     lastReceivedToken,
            List<T>                  messagesSentLast,
            List<T>                  messagesToBeSent )
    {
        MPingPongMessageEndpoint<T> ret = new MPingPongMessageEndpoint<T>(
                name,
                deltaRespond,
                deltaResend,
                deltaRecover,
                randomVariation,
                exec,
                lastSentToken,
                lastReceivedToken,
                messagesSentLast,
                messagesToBeSent );
        return ret;
    }

    /**
     * Constructor.
     *
     * @param name the name of the PingPongMessageEndpoint (for debugging only)
     * @param deltaRespond the number of milliseconds until this PingPongMessageEndpoint returns the token
     * @param deltaResend  the number of milliseconds until this PingPongMessageEndpoint resends the token if sending the token failed
     * @param deltaRecover the number of milliseconds until this PingPongMessageEndpoint decides that the token
     *                     was not received by the partner PingPongMessageEndpoint, and resends
     * @param randomVariation the random component to add to the various times
     * @param exec the ScheduledExecutorService to schedule timed tasks
     * @param lastSentToken the last token sent in a previous instantiation of this MessageEndpoint
     * @param lastReceivedToken the last token received in a previous instantiation of this MessageEndpoint
     * @param messagesSentLast the last set of Messages sent in a previous instantiation of this MessageEndpoint
     * @param messagesToBeSent outgoing message queue (may or may not be empty)
     */
    protected MPingPongMessageEndpoint(
            String                   name,
            long                     deltaRespond,
            long                     deltaResend,
            long                     deltaRecover,
            double                   randomVariation,
            ScheduledExecutorService exec,
            long                     lastSentToken,
            long                     lastReceivedToken,
            List<T>                  messagesSentLast,
            List<T>                  messagesToBeSent )
    {
        super(  name,
                deltaRespond,
                deltaResend, 
                deltaRecover,
                randomVariation,
                exec,
                lastSentToken,
                lastReceivedToken,
                messagesSentLast,
                messagesToBeSent );
    }

    /**
     * Set the partner endpoint.
     *
     * @param partner the partner
     */
    public void setPartnerAndInitiateCommunications(
            MPingPongMessageEndpoint<T> partner )
    {
        if( thePartner != null ) {
            throw new IllegalStateException();
        }
        if( thePartner == this ) {
            throw new IllegalArgumentException( "Cannot communicate with myself" );
        }
        thePartner = partner;

        thePartner.thePartner = this; // point back to us
        
        startCommunicating();
    }
    
    /**
     * Send a message via the next ping or pong.
     *
     * @param msg the Message to send.
     */
    @Override
    public void enqueueMessageForSend(
            T msg )
    {
        if( isGracefullyDead ) {
            throw new IllegalStateException( this + " is dead" );
        }
        super.enqueueMessageForSend( msg );
    }

    /**
     * Do the message send.
     *
     * @param token the token of the message
     * @param content the content to send.
     * @throws MessageSendException thrown if the message could not be sent
     */
    protected void sendMessage(
            long    token,
            List<T> content )
        throws
            MessageSendException
    {
        MPingPongMessageEndpoint<T> partner = thePartner;
        if( partner != null ) {
            partner.incomingMessage( token, content );
        } else {
            throw new MessageSendException( content, "No partner MPingPongMessageEndpoint has been set" );
        }
    }

    /**
     * Invoked by subclasses to provide the content of a received message.
     *
     * @param token the integer representing the token
     * @param content the content of a received message
     * @throws MessageEndpointIsDeadException thrown if the MessageEndpoint is dead
     * @throws MessageSendException thrown if the message could not be sent
     */
    @Override
    protected void incomingMessage(
            long    token,
            List<T> content )
        throws
            MessageEndpointIsDeadException,
            MessageSendException
    {
        if( isGracefullyDead ) {
            throw new MessageEndpointIsDeadException();
        } else {

            try {
                super.incomingMessage( token, content );

            } catch( RejectedExecutionException ex ) {
                throw new MessageEndpointIsDeadException( ex );
            }
        }
    }

    /**
     * Obtain the messages that were sent most recently.
     *
     * @return the List
     */
    @SuppressWarnings(value={"unchecked"})
    public synchronized List<T> messagesLastSent()
    {
        ArrayList<T> ret = new ArrayList<T>( theMessagesSentLast != null ? theMessagesSentLast.size() : 1 );

        if( theMessagesSentLast != null && !theMessagesSentLast.isEmpty() ) {
            ret.addAll( theMessagesSentLast );
        }
        return ret;
    }

    /**
     * Attempt to send the outgoing messages, but stop receiving incoming messages.
     */
    public void gracefulDie()
    {
        isGracefullyDead = true;
        
        if( theFuture != null ) {
            if( log.isDebugEnabled() ) {
                log.debug( this + " canceling future" );
            }
            theFuture.cancel( false );
            theFuture = null;
        }
    }

    /**
     * The partner PingPongMessageEndpoint.
     */
    protected MPingPongMessageEndpoint<T> thePartner;
    
    /**
     * If this is true, the MessageEndpoint is dead, but still attempts to send queued
     * messages.
     */
    protected boolean isGracefullyDead = false;

    /**
     * Our ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( MPingPongMessageEndpoint.class );
}