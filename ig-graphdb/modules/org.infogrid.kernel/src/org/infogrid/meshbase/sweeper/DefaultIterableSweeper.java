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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.sweeper;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.ResourceHelper;

/**
 * Default implementation of Sweeper for IterableMeshBases.
 */
public class DefaultIterableSweeper
        implements
            IterableSweeper
{
    /**
     * Factory method if the Sweeper is only supposed to be invoked manually.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @return the created DefaultIterableSweeper
     */
    public static DefaultIterableSweeper create(
            IterableMeshBase         mb,
            SweepPolicy              policy )
    {
        return new DefaultIterableSweeper(
                mb,
                policy,
                null,
                DEFAULT_LOTSIZE,
                DEFAULT_WAITBETWEENLOTS );
    }

    /**
     * Factory method.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @param scheduler the scheduler to use, if any
     * @return the created DefaultIterableSweeper
     */
    public static DefaultIterableSweeper create(
            IterableMeshBase         mb,
            SweepPolicy              policy,
            ScheduledExecutorService scheduler )
    {
        return new DefaultIterableSweeper(
                mb,
                policy,
                scheduler,
                DEFAULT_LOTSIZE,
                DEFAULT_WAITBETWEENLOTS );
    }

    /**
     * Factory method.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @param scheduler the scheduler to use, if any
     * @param lotSize the number of MeshObjects to consider at a time
     * @param waitBetweenLots the time, in milliseconds, between examining subsequent lots
     * @return the created DefaultIterableSweeper
     */
    public static DefaultIterableSweeper create(
            IterableMeshBase         mb,
            SweepPolicy              policy,
            ScheduledExecutorService scheduler,
            int                      lotSize,
            long                     waitBetweenLots )
    {
        return new DefaultIterableSweeper(
                mb,
                policy,
                scheduler,
                lotSize,
                waitBetweenLots );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @param scheduler the scheduler to use, if any
     * @param lotSize the number of MeshObjects to consider at a time
     * @param waitBetweenLots the time, in milliseconds, between examining subsequent lots
     */
    protected DefaultIterableSweeper(
            IterableMeshBase         mb,
            SweepPolicy              policy,
            ScheduledExecutorService scheduler,
            int                      lotSize,
            long                     waitBetweenLots )
    {
        theMeshBase        = mb;
        thePolicy          = policy;
        theScheduler       = scheduler;
        theLotSize         = lotSize;
        theWaitBetweenLots = waitBetweenLots;
    }

    /**
     * Set the SweepPolicy.
     *
     * @param newValue the new SweepPolicy
     */
    public void setSweepPolicy(
            SweepPolicy newValue )
    {
        thePolicy = newValue;
    }

    /**
     * Get the SweepPolicy.
     *
     * @return the SweepPolicy
     */
    public SweepPolicy getSweepPolicy()
    {
        return thePolicy;
    }

    /**
     * Determine the IterableMeshBase on which this Sweeper works.
     *
     * @return the IterableMeshBase
     */
    public IterableMeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * Continually sweep this IterableMeshBase in the background, according to
     * the configured Sweeper.
     *
     * @param scheduleVia the ScheduledExecutorService to use for scheduling
     * @throws NullPointerException thrown if no Sweeper has been set
     */
    public void startBackgroundSweeping(
            ScheduledExecutorService scheduleVia )
        throws
            NullPointerException
    {
        theScheduler = scheduleVia;

        scheduleSweepStep();
    }

    /**
     * Stop the background sweeping.
     */
    public void stopBackgroundSweeping()
    {
        SweepStep nextStep = theNextSweepStep;
        if( nextStep == null ) {
            return;
        }
        synchronized( nextStep ) {
            nextStep.cancel();
            theNextSweepStep = null;
        }
    }

    /**
     * Perform a sweep on every single MeshObject in this IterableMeshBase.
     * This may take a long time; using background sweeping is almost always
     * a better alternative.
     */
    public synchronized void sweepAllNow()
    {
        boolean doReschedule = cancelSweepStep();

        theIterator = theMeshBase.iterator(); // reset
        while( theIterator.hasNext() ) {
            sweepObject( theIterator.next() );
        }

        if( doReschedule ) {
            scheduleSweepStep();
        }
    }

    /**
     * Perform a sweep on the next lot in this IterableMeshBase.
     */
    public void sweepNextLot()
    {
        boolean doReschedule = cancelSweepStep();

        if( theIterator == null ) {
            theIterator = theMeshBase.iterator();
        }
        for( int i=0 ; i<theLotSize && theIterator.hasNext() ; ++i ) {
            sweepObject( theIterator.next() );
        }

        if( doReschedule ) {
            scheduleSweepStep();
        }
    }

    /**
     * Perform a sweep on this MeshObject. This method
     * may be overridden by subclasses.
     */
    protected void sweepObject(
            MeshObject current )
    {
        thePolicy.potentiallyDelete( current );
    }

    /**
     * Cancel the next SweepStep, if there is one.
     *
     * @return true if there was one.
     */
    boolean cancelSweepStep()
    {
        SweepStep step = theNextSweepStep;
        if( step != null ) {
            step.cancel();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Schedule the next SweepStep.
     */
    void scheduleSweepStep()
    {
        theNextSweepStep = new SweepStep( this );

        theScheduler.schedule( theNextSweepStep, theWaitBetweenLots, TimeUnit.MILLISECONDS );
    }

    /**
     * The IterableMeshBase on which this Sweeper works.
     */
    protected IterableMeshBase theMeshBase;

    /**
     * The policy in effect.
     */
    protected SweepPolicy thePolicy;

    /**
     * The Scheduler for the Sweeper, if any.
     */
    protected ScheduledExecutorService theScheduler;

    /**
     * The number of MeshObjects to consider at a time.
     */
    protected int theLotSize;

    /**
     * The time, in milliseconds, between examining subsequent lots.
     */
    protected long theWaitBetweenLots;

    /**
     * The iterator capturing the current location of the Sweeper in the IterableMeshBase.
     */
    protected CursorIterator<MeshObject> theIterator = null;

    /**
     * The next background Sweep task, if any.
     */
    SweepStep theNextSweepStep;

    /**
     * The ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( DefaultIterableSweeper.class );

    /**
     * The default number of MeshObjects to consider at a time.
     */
    protected static final int DEFAULT_LOTSIZE = theResourceHelper.getResourceIntegerOrDefault( "DefaultLotsize", 100 );

    /**
     * The default time, in milliseconds, between examining subsequent lots.
     */
    protected static final long DEFAULT_WAITBETWEENLOTS = theResourceHelper.getResourceLongOrDefault( "DefaultWaitBetweenLots", 10000L );
}
