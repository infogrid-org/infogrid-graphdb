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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.security;

import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
 * Collects functionality common to many AccessManager implementations.
 */
public abstract class AbstractAccessManager
        implements
             AccessManager,
             CanBeDumped
{
    private static final Log log = Log.getLogInstance( AbstractAccessManager.class ); // our own, private logger

    /**
     * Determine the identity of the caller. This may return null, indicating that
     * the caller is anonymous.
     *
     * @return the identity of the caller, or null.
     * @see #setCaller
     * @see #unsetCaller
     */
    public final MeshObject getCaller()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "getCaller(" );
        }
        synchronized( theCallersOnThreads ) {
            MeshObject ret = theCallersOnThreads.get( Thread.currentThread() );
            return ret;
        }
    }

    /**
     * Determine whether the current Thread has super user privileges.
     *
     * @return true if the current Thread has super user privileges.
     */
    public final boolean isSu()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "isSu" );
        }
        Integer level;
        synchronized( theSuThreads ) {
            level = theSuThreads.get( Thread.currentThread() );
        }
        
        if( level == null ) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * <p>Set the identity of the caller on this Thread. This will unset any previous
     * identity set on this Thread.</p>
     * <p>The implementation on this level allows anybody to change the identity associated
     * with the current Thread; subclasses may implement this differently.</p>
     *
     * @param caller the caller, or null if anonymous
     * @return the previously set caller, if any
     * @throws IdentityChangeException thrown if the current caller was not authorized to perform this operation
     * @see #getCaller
     * @see #unsetCaller
     */
    public MeshObject setCaller(
            MeshObject caller )
        throws
            IdentityChangeException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "setCaller", caller );
        }
        synchronized( theCallersOnThreads ) {
            MeshObject ret = theCallersOnThreads.put( Thread.currentThread(), caller );
            return ret;
        }
    }

    /**
     * <p>Unset the identity of the caller on this Thread. This is called when the caller
     * is done, for example.</p>
     * <p>The implementation on this level allows anybody to change the identity associated
     * with the current Thread; subclasses may implement this differently.</p>
     *
     * @return the previously set caller, if any
     * @throws IdentityChangeException thrown if the current caller was not authorized to perform this operation
     * @see #getCaller
     * @see #setCaller
     */
    public MeshObject unsetCaller()
        throws
            IdentityChangeException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "callerDoneOnThread" );
        }
        synchronized( theCallersOnThreads ) {
            MeshObject ret = theCallersOnThreads.remove( Thread.currentThread() );
            return ret;
        }
    }

    /**
     * <p>Make the current Thread have super-user rights. This is very similar to common
     * operating-systems calls.</p>
     * <p>The implementation on this level allows anybody to change the identity associated
     * with the current Thread; subclasses may implement this differently.</p>
     * 
     * @throws IdentityChangeException thrown if the current caller was not authorized to perform this operation
     */
    public void sudo()
        throws
            IdentityChangeException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "sudo" );
        }
        Thread t = Thread.currentThread();

        synchronized( theSuThreads ) {
            Integer level = theSuThreads.get( t );
            if( level == null ) {
                level = new Integer( 1 );
            } else {
                level = new Integer( level.intValue() + 1 );
            }
            theSuThreads.put( t, level );
        }
    }

    /**
     * Release super-user rights from the current Thread. If the current Thread does not
     * have super-user rights, nothing happens.
     */
    public void sudone()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "sudone" );
        }
        Thread t = Thread.currentThread();

        synchronized( theSuThreads ) {
            Integer level = theSuThreads.get( t );
            int     l     = level.intValue() - 1;
            
            if( l > 0 ) {
                theSuThreads.put( t, new Integer( l ));
            } else {
                theSuThreads.remove( t );
            }
        }
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String [] {
                        "callersOnThreads",
                        "suThreads"
                },
                new Object [] {
                        theCallersOnThreads, // ArrayHelper.mapToString( theCallersOnThreads )
                        theSuThreads         // ArrayHelper.mapToString( theSuThreads )
                });
    }

    /**
     * The identities of the callers in the various threads.
     */
    protected final HashMap<Thread,MeshObject> theCallersOnThreads = new HashMap<Thread,MeshObject>();
    
    /**
     * The threads that currently are su'd.
     */
    protected final HashMap<Thread,Integer> theSuThreads = new HashMap<Thread,Integer>();
}
