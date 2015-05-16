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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

import java.util.HashMap;

/**
 * A ThreadIdentityManager manages caller identities associated with Threads.
 */
public abstract class ThreadIdentityManager
{
    private static final Log log = Log.getLogInstance( ThreadIdentityManager.class ); // our own, private logger
    
    /**
     * Determine the identity of the caller. This may return null, indicating that
     * the caller is anonymous.
     *
     * @return the identity of the caller, or null.
     */
    public static MeshObject getCaller()
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".getCaller()" );
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
    public static boolean isSu()
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".isSu()" );
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
     * Set the identity of the caller on this Thread. This will unset any previous
     * identity set on this Thread. Generally, the sequence of invocation should be:
     * <pre>
     * try {
     *     identifyCallerOnThread( theCaller );
     *     performWork();
     * } finally {
     *     callerDoneOnThread();
     * }
     * </pre>
     *
     * @param caller the caller, or null if anonymous
     * @return the previously set caller, if any
     * @see #unsetCaller
     */
    public static MeshObject setCaller(
            MeshObject caller )
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".setCaller( " + caller + " )" );
        }
        synchronized( theCallersOnThreads ) {
            MeshObject ret = theCallersOnThreads.put( Thread.currentThread(), caller );
            return ret;
        }
    }

    /**
     * Unset the identity of the caller on this Thread. This is called when the caller
     * is done.
     *
     * @return the previously set caller, if any
     * @see #setCaller
     */
    public static MeshObject unsetCaller()
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".callerDoneOnThread()" );
        }
        synchronized( theCallersOnThreads ) {
            MeshObject ret = theCallersOnThreads.remove( Thread.currentThread() );
            return ret;
        }
    }

    /**
     * Make the current Thread have super user rights.
     */
    public static void sudo()
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".sudo()" );
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
     * Release super user rights from the current Thread.
     */
    public static void sudone()
    {
        if( log.isDebugEnabled() ) {
            log.debug( local() + ".sudone()" );
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
     * Execute this action as super user.
     *
     * @param r the Runnable containing the action
     */
    public static void suExec(
            Runnable r )
    {
        try {
            sudo();

            r.run();

        } finally {
            sudone();
        }
    }

    /**
     * The equivalent of toString() for this set of static vars.
     * 
     * @return the String form
     */
    protected static String local()
    {
        StringBuilder buf = new StringBuilder();
        buf.append( ThreadIdentityManager.class.getName() );
        buf.append( "{ theCallersOnThreads: " );
        buf.append( ArrayHelper.mapToString( theCallersOnThreads ) );
        buf.append( ", theSuThreads: " );
        buf.append( ArrayHelper.mapToString( theSuThreads ) );
        buf.append( " }" );
        return buf.toString();
    }

    /**
     * The identities of the callers in the various threads.
     *
     * FIXME? It's not clear that this is the right place where to maintain this.
     */
    protected static final HashMap<Thread,MeshObject> theCallersOnThreads = new HashMap<Thread,MeshObject>();
    
    /**
     * The threads that currently are su'd. The value of the HashMap counts the number of
     * su invocations on that Thread.
     */
    protected static final HashMap<Thread,Integer> theSuThreads = new HashMap<Thread,Integer>();
}
