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
// Copyright 1998-2016 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.security;

import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.util.logging.Log;
import org.infogrid.util.Pair;

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
        synchronized( theCallersOnThreads ) {
            Pair<MeshObject,String[]> ret = theCallersOnThreads.get( Thread.currentThread() );
            if( ret == null ) {
                return null;
            }
            return ret.getName();
        }
    }

    /**
     * Determine the groups of the caller. This may return null, indicating that
     * the caller is anonymous. If the caller is known, a (possibly empty) array will
     * be returned.
     *
     * @return the groups of the caller, or null.
     */
    public static String [] getCallerGroups()
    {
        synchronized( theCallersOnThreads ) {
            Pair<MeshObject,String[]> ret = theCallersOnThreads.get( Thread.currentThread() );
            if( ret == null ) {
                return null;
            }
            return ret.getValue();
        }
    }

    /**
     * Determine both caller and its groups at the same time. This may return null,
     * indicating that the caller is anonymous.
     *
     * @return Pair of caller and groups
     */
    public static Pair<MeshObject,String[]> getCallerAndGroups()
    {
        synchronized( theCallersOnThreads ) {
            Pair<MeshObject,String[]> ret = theCallersOnThreads.get( Thread.currentThread() );
            return ret;
        }
    }

    /**
     * Determine whether the caller is a member of the specified group.
     *
     * @param group the group
     * @return true if the caller is a member of the specified group
     */
    public static boolean isCallerGroupMemberOf(
            String group )
    {
        Pair<MeshObject,String[]> ret = getCallerAndGroups();
        if( ret == null ) {
            return false;
        }
        String [] groups = ret.getValue();
        if( groups == null ) {
            return false;
        }
        for( String g : groups ) {
            if( group.equals( group )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the current Thread has super user privileges.
     *
     * @return true if the current Thread has super user privileges.
     */
    public static boolean isSu()
    {
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
     * oldCaller = ThreadIdentityManager.getCaller();
     * try {
     *     ThreadIdentityManager.setCaller( newCaller );
     *     performWorkAsNewCaller();
     * } finally {
     *     ThreadIdentityManager.setCaller( oldCaller );
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
        return setCaller( caller, EMPTY_GROUPS );
    }

    /**
     * Set the identity of the caller on this Thread. This will unset any previous
     * identity set on this Thread. Generally, the sequence of invocation should be:
     * <pre>
     * oldCaller = ThreadIdentityManager.getCaller();
     * try {
     *     ThreadIdentityManager.setCaller( newCaller );
     *     performWorkAsNewCaller();
     * } finally {
     *     ThreadIdentityManager.setCaller( oldCaller );
     * }
     * </pre>
     *
     * @param caller the caller, or null if anonymous
     * @param callerGroups an array of groups the caller is part of
     * @return the previously set caller, if any
     * @see #unsetCaller
     */
    public static MeshObject setCaller(
            MeshObject caller,
            String []  callerGroups )
    {
        Pair<MeshObject,String[]> ret;
        if( caller != null ) {
            if( callerGroups == null ) {
                callerGroups = EMPTY_GROUPS;
            }
            synchronized( theCallersOnThreads ) {
                ret = theCallersOnThreads.put(
                        Thread.currentThread(),
                        new Pair<>( caller, callerGroups ) );
            }
        } else {
            synchronized( theCallersOnThreads ) {
                ret = theCallersOnThreads.remove(
                        Thread.currentThread());
            }
        }
        if( ret != null ) {
            return ret.getName();
        } else {
            return null;
        }
    }

    /**
     * Set the identity of the caller on this Thread. This will unset any previous
     * identity set on this Thread. Generally, the sequence of invocation should be:
     * <pre>
     * oldCaller = ThreadIdentityManager.getCaller();
     * try {
     *     ThreadIdentityManager.setCaller( newCaller );
     *     performWorkAsNewCaller();
     * } finally {
     *     ThreadIdentityManager.setCaller( oldCaller );
     * }
     * </pre>
     *
     * @param callerPair the caller, and the caller's groups, in one swoop
     * @return the previously set caller, if any
     * @see #unsetCaller
     */
    public static MeshObject setCaller(
            Pair<MeshObject,String []> callerPair )
    {
        Pair<MeshObject,String[]> ret;
        if( callerPair != null ) {
            if( callerPair.getName() == null ) {
                throw new NullPointerException();
            }
            synchronized( theCallersOnThreads ) {
                ret = theCallersOnThreads.put(
                        Thread.currentThread(),
                        callerPair );
            }
        } else {
            synchronized( theCallersOnThreads ) {
                ret = theCallersOnThreads.remove(
                        Thread.currentThread());
            }
        }
        if( ret != null ) {
            return ret.getName();
        } else {
            return null;
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
        Pair<MeshObject,String[]> ret;
        synchronized( theCallersOnThreads ) {
            ret = theCallersOnThreads.remove( Thread.currentThread() );
        }
        if( ret != null ) {
            return ret.getName();
        } else {
            return null;
        }
    }

    /**
     * Make the current Thread have super user rights.
     */
    public static void sudo()
    {
        Thread t = Thread.currentThread();

        synchronized( theSuThreads ) {
            Integer level = theSuThreads.get( t );
            if( level == null ) {
                level = 1;
            } else {
                ++level;
            }
            theSuThreads.put( t, level );
        }
    }

    /**
     * Release super user rights from the current Thread.
     */
    public static void sudone()
    {
        Thread t = Thread.currentThread();

        synchronized( theSuThreads ) {
            Integer level = theSuThreads.get( t );
            int     l     = level - 1;

            if( l > 0 ) {
                theSuThreads.put( t, l );
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
     * The identities of the callers in the various threads.
     */
    protected static final HashMap<Thread,Pair<MeshObject,String[]>> theCallersOnThreads = new HashMap<>();

    /**
     * The threads that currently are su'd. The value of the HashMap counts the number of
     * su invocations on that Thread.
     */
    protected static final HashMap<Thread,Integer> theSuThreads = new HashMap<>();

    /**
     * For efficiency, use the same empty array every time.
     */
    public static final String [] EMPTY_GROUPS = new String[0];
}
