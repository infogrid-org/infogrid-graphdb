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

package org.infogrid.mesh.set.active;

import org.infogrid.mesh.set.MeshObjectSet;

/**
  * An ActiveMeshObjectSet is a dynamically growing or shrinking set of MeshObjects.
  * By subscribing, an
  * {@link org.infogrid.mesh.set.active.ActiveMeshObjectSetListener ActiveMeshObjectSetListener}
  * will be informed of additions to or removals from the set.
  *
  * <p>This type of MeshObjectSet is guaranteed to never contain any dead MeshObjects, the
  *    ActiveMeshObjectSet is automatically removing them.</p>
  *
  * <p>Specific subtypes have certain pre-defined behaviors that define under which
  *    circumstances MeshObjects are added and removed to/from the set.</p>
  */
public interface ActiveMeshObjectSet
        extends
            MeshObjectSet
{
    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @param newListener the listener to be added
      * @see #addSoftActiveMeshObjectSetListener
      * @see #addWeakActiveMeshObjectSetListener
      * @see #removeActiveMeshObjectSetListener
      */
    public void addDirectActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener );

    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @param newListener the listener to be added
      * @see #addDirectActiveMeshObjectSetListener
      * @see #addWeakActiveMeshObjectSetListener
      * @see #removeActiveMeshObjectSetListener
      */
    public void addSoftActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener );

    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @param newListener the listener to be added
      * @see #addDirectActiveMeshObjectSetListener
      * @see #addSoftActiveMeshObjectSetListener
      * @see #removeActiveMeshObjectSetListener
      */
    public void addWeakActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener );

    /**
      * Remove a listener for events indicating additions or removals to/from the set.
      *
      * @param oldListener the listener to be removed
      * @see #addDirectActiveMeshObjectSetListener
      * @see #addSoftActiveMeshObjectSetListener
      * @see #addWeakActiveMeshObjectSetListener
      */
    public void removeActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener oldListener );
}
