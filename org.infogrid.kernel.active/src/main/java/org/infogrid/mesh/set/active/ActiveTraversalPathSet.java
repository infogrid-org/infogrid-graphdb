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

import org.infogrid.mesh.set.TraversalPathSet;

/**
 * This is a set of TraversalPaths that can change by itself. It is similar to
 * ActiveMeshObjectSet, but it contains TraversalPaths, not MeshObjects.
 */
public interface ActiveTraversalPathSet
        extends
            TraversalPathSet
{
    /**
     * Obtain the destination Entities as an ActiveMeshObjectSet. This is
     * similar to TraversalPathSet.getDestinationsAsSet(), except that we return an
     * active set, not a snapshot.
     *
     * @return the created ActiveMeshObjectSet containing the destination Entities of this set
     */
    public abstract ActiveMeshObjectSet getDestinationsAsActiveSet();

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public abstract void addDirectActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener );

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public abstract void addSoftActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener );

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public abstract void addWeakActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener );

    /**
      * Remove a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param oldListener the to-be-removed listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      */
    public abstract void removeActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener oldListener );
}
