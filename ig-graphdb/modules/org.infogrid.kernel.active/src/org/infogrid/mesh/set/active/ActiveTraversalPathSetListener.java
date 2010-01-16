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

/**
 * This is the listener interface to obtain events from an ActiveTraversalPathSet.
 */
public interface ActiveTraversalPathSetListener
{
    /**
      * Indicates that a TraversalPath was added to this set.
      *
      * @param event the event
      */
    public void traversalPathAdded(
            TraversalPathAddedEvent event );

    /**
      * Indicates that a TraversalPath was removed from this set.
      *
      * @param event the event
      */
    public void traversalPathRemoved(
            TraversalPathRemovedEvent event );

    /**
      * Indicates that a TraversalPathSet was re-ordered. This only applies for
      * sets that are ordered.
      *
      * @param event the event
      */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event );
}
