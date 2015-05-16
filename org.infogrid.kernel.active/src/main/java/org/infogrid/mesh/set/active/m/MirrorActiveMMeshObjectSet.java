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

package org.infogrid.mesh.set.active.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;

import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * An ActiveMeshObjectSet that mirrors the content of another. This
 * is used primarily for debugging as it enables the developer to monitor
 * the content of an arbitrary ActiveMeshObjectSet by "inserting" this MirrorActiveMMeshObjectSet
 * between the original set and its listeners. Given its purpose, this class is not
 * instantiated using an ActiveMeshObjectFactory, but through a public constructor
 * that also makes subclassing easy.
 */
public class MirrorActiveMMeshObjectSet
    extends
        AbstractActiveMMeshObjectSet
    implements
        ActiveMeshObjectSetListener
{
    private static Log log = Log.getLogInstance( MirrorActiveMMeshObjectSet.class  ); // our own, private logger

    /**
     * Public constructor, so it can be subclassed easily (such as using an inner class), enabling
     * the developer to set what amounts to instance-specific breakpoints in standard debuggers.
     * 
     * @param mirroredSet the ActiveMeshObjectSet that this MirroredActiveMeshObjectSet mirrors
     */
    public MirrorActiveMMeshObjectSet(
            ActiveMeshObjectSet mirroredSet )
    {
        super( mirroredSet.getFactory() ); // FIXME? Different factory?

        MeshObject [] mirroredContent = mirroredSet.getMeshObjects();
        MeshObject [] copiedContent   = ArrayHelper.copyIntoNewArray( mirroredContent, MeshObject.class );

        setInitialContent( copiedContent );

        mirroredSet.addWeakActiveMeshObjectSetListener( this );

        theMirroredSet = mirroredSet;
    }

    /**
     * This can be overridden to have an easy location for breakpoints in the debugger.
     */
    protected void breakpointHook()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "breakpointHook" );
        }
    }

    /**
     * Allows the mirrored set to send a MeshObjectAddedEvent to this set.
     * 
     * @param event the event notifying us of a change in the mirrored set
     */
    public void meshObjectAdded(
            MeshObjectAddedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        MeshObject added = event.getDeltaValue();

        breakpointHook();
        certainlyAdd( added );
    }

    /**
     * Allows the mirrored set to send a MeshObjectRemovedEvent to this set.
     * 
     * @param event the event notifying us of a change in the mirrored set
     */
    public void meshObjectRemoved(
            MeshObjectRemovedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        MeshObject removed = event.getDeltaValue();

        breakpointHook();
        certainlyRemove( removed );
    }

    /**
     * Allows the mirrored set to send a OrderedActiveMeshObjectSetReorderedEvent to this set.
     *
     * @param event the event notifying us of a change in the mirrored set
     */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        log.warn( "not implemented" );
    }

    /**
     * The set that we mirror. We keep a reference so this won't get garbage collected.
     */
    protected ActiveMeshObjectSet theMirroredSet;
}
