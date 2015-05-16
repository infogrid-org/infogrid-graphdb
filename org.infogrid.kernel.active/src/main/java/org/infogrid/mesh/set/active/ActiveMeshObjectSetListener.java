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

package org.infogrid.mesh.set.active;

/**
  * This interface is supported by objects that listen to changes in an ActiveMeshObjectSet.
  */
public interface ActiveMeshObjectSetListener
{
    /**
     * Allows an ActiveMeshObjectSet to send a MeshObjectAddedEvent to this listener.
     * 
     * @param event the event
     * @see #meshObjectRemoved
     */
    public void meshObjectAdded(
            MeshObjectAddedEvent event );

    /**
     * Allows an ActiveMeshObjectSet to send a MeshObjectRemovedEvent to this listener.
     * 
     * @param event the event
     * @see #meshObjectAdded
     */
    public void meshObjectRemoved(
            MeshObjectRemovedEvent event );

    /**
      * Allows an OrderedActiveMeshObjectSet to send a OrderedActiveMeshObjectSetReorderedEvent to this
      * listener. This method will currently only be invoked by OrderedActiveMeshObjectSets.
      *
      * @param event the event
      */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event );
}
