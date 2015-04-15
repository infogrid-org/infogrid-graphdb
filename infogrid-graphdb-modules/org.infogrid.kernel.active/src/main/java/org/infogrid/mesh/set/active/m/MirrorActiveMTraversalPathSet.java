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

package org.infogrid.mesh.set.active.m;

import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * This ActiveTraversalPathSet mirrors the exact content of another. It is used
 * primarily for debugging. For example, it provides a convenient means to add break points.
 */
public class MirrorActiveMTraversalPathSet
        extends
            AbstractActiveMTraversalPathSet
        implements
            ActiveTraversalPathSetListener
{
    private static Log log = Log.getLogInstance( MirrorActiveMTraversalPathSet.class  ); // our own, private logger

    /**
     * Public constructor, so we can instantiate it directly, or we
     * can easily subclass is using an inner class.
     *
     * @param mirroredSet the ActiveTraversalPathSet that this set is going to mirror
     */
    public MirrorActiveMTraversalPathSet(
            ActiveTraversalPathSet mirroredSet )
    {
        super(  mirroredSet.getFactory() ); // FIXME? different factory?

        TraversalPath [] mirroredContent = mirroredSet.getTraversalPaths();
        TraversalPath [] copiedContent   = ArrayHelper.copyIntoNewArray( mirroredContent, TraversalPath.class );

        setInitialContent( copiedContent );

        mirroredSet.addWeakActiveTraversalPathSetListener( this );

        theMirroredSet = mirroredSet;
    }

    /**
     * Override this method in subclasses to have a convenient place where to put break points
     * in the debugger.
     */
    protected void breakpointHook()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "breakpointHook" );
        }
    }

    /**
      * Indicates that a Traversalpath was added to this set.
      *
      * @param event the event
      */
    public void traversalPathAdded(
            TraversalPathAddedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        TraversalPath added = event.getDeltaValue();

        breakpointHook();

        certainlyAdd( added );
    }

    /**
      * Indicates that a Traversalpath was removed from this set.
      *
      * @param event the event
      */
    public void traversalPathRemoved(
            TraversalPathRemovedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        TraversalPath removed = event.getDeltaValue();

        breakpointHook();

        certainlyRemove( removed );
    }

    /**
     * This indicates that the set was re-ordered. Only applies in case of
     * ordered sets.
     *
     * @param event the event
     */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        log.warn( "unsupported operation" );
    }

    /**
     * The set that we mirror. Keep a reference to prevent garbage collection.
     */
    protected ActiveTraversalPathSet theMirroredSet;
}
