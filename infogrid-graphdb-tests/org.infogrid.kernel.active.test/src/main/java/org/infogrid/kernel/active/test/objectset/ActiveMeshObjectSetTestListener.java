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

package org.infogrid.kernel.active.test.objectset;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.meshbase.transaction.MeshObjectNeighborChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.util.logging.Log;

/**
 * Receive and track callbacks in ActiveMeshObjectSetTests.
 */
public class ActiveMeshObjectSetTestListener
        implements
            ActiveMeshObjectSetListener,
            PropertyChangeListener
{
    /**
     * @param listenerName name of the listener to distinguish listeners
     * @param set the ActiveMeshObjectSet to monitor
     * @param logger logger to log to
     */
    public ActiveMeshObjectSetTestListener(
            String              listenerName,
            ActiveMeshObjectSet set,
            Log                 logger )
    {
        name   = listenerName;
        theSet = set;

        theSet.addWeakActiveMeshObjectSetListener( this );
        theSet.addWeakContentPropertyChangeListener( this );

        log = logger;

        reset();
    }

    /**
     * Obtain the number of addition events that we have received.
     * 
     * @return number of events
     */
    public int getAddCounter()
    {
        return addedEvents.size();
    }

    /**
     * Obtain the number of deletion events that we have received.
     * 
     * @return number of events
     */
    public int getRemoveCounter()
    {
        return removedEvents.size();
    }

    /**
     * Obtain the number of PropertyChangeEvents that we have received.
     * 
     * @return number of events
     */
    public int getPropertyChangesCounter()
    {
        return propertyChangeEvents.size();
    }

    /**
     * Obtain the number of RoleChangeEvents that we have received.
     * 
     * @return number of events
     */
    public int getRoleChangesCounter()
    {
        return roleChangeEvents.size();
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void meshObjectAdded(
            MeshObjectAddedEvent event )
    {
        addedEvents.add( event );
        log.info(
                name
                + ": The meshObjectAdded method was called: "
//                + event
//                + ", added name: "
                + event.getDeltaValue() );
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void meshObjectRemoved(
            MeshObjectRemovedEvent event )
    {
        removedEvents.add( event );
        log.info(
                name
                + ": the meshObjectRemoved method was called: "
//                + event
//                + ", removed name: "
                + event.getDeltaValue() );
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void propertyChange(
            PropertyChangeEvent event )
    {
        if( event instanceof MeshObjectPropertyChangeEvent ) {
            propertyChangeEvents.add( (MeshObjectPropertyChangeEvent) event );
        } else if( event instanceof MeshObjectRoleChangeEvent ) {
            roleChangeEvents.add( (MeshObjectRoleChangeEvent) event );
        } else if( event instanceof MeshObjectNeighborChangeEvent ) {
            // noop
        } else {
            log.error( "unexpected event: " + event );
        }

        log.info(name + ": propertyChange was called: " + event );
    }

    /**
     * ActiveMeshObjectSetListener interface method.
     * 
     * @param event the event
     */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        log.info( name + ": orderedMeshObjectSetReordered was called: " + event );
    }

    /**
     * Reset all counters.
     */
    public void reset()
    {
        addedEvents          = new ArrayList<MeshObjectAddedEvent>();
        removedEvents        = new ArrayList<MeshObjectRemovedEvent>();
        propertyChangeEvents = new ArrayList<MeshObjectPropertyChangeEvent>();
        roleChangeEvents     = new ArrayList<MeshObjectRoleChangeEvent>();
    }

    /**
     * The name of the listener.
     */
    String name;

    /**
     * The set that we listen to.
     */
    ActiveMeshObjectSet theSet;

    /**
     * Stores received MeshObjectAddedEvents.
     */
    private ArrayList<MeshObjectAddedEvent> addedEvents;

    /**
     * Stores received MeshObjectRemovedEvents.
     */
    private ArrayList<MeshObjectRemovedEvent> removedEvents;

    /**
     * Stores received MeshObjectPropertyChangeEvent.
     */
    private ArrayList<MeshObjectPropertyChangeEvent> propertyChangeEvents;

    /**
     * Stores received MeshObjectRoleChangeEvent.
     */
    private ArrayList<MeshObjectRoleChangeEvent> roleChangeEvents;

    /**
     * The logger to log to.
     */
    private Log log;
}
