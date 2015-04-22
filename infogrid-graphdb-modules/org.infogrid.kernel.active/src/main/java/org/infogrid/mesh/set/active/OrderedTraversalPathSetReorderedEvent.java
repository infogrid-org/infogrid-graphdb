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
  * This indicates that a TraversalPathSet was reordered.
  *
  * FIXME? This does not indicate how the ordering was changed.
  */
public class OrderedTraversalPathSetReorderedEvent
        extends
            ActiveTraversalPathSetEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Construct one.
      *
      * @param _theSet the set that changed
      */
    public OrderedTraversalPathSetReorderedEvent(
            ActiveTraversalPathSet _theSet )
    {
        super( _theSet, null );
    }
}
