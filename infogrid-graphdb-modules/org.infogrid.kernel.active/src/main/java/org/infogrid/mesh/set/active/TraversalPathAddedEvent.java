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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set.active;

import org.infogrid.model.traversal.TraversalPath;

import org.infogrid.util.logging.Dumper;

/**
  * This event indicates that a TraversalPath instance was added to a TraversalPathSet.
  */
public class TraversalPathAddedEvent
        extends
            ActiveTraversalPathSetEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Construct one.
      *
      * @param _theSet the set to which a TraversalPath was added
      * @param _addedTraversalPath the TraversalPath that was added to the set
      */
    public TraversalPathAddedEvent(
            ActiveTraversalPathSet _theSet,
            TraversalPath          _addedTraversalPath )
    {
        super(_theSet, _addedTraversalPath );

        theAddedIndex = -1;
    }

    /**
     * Construct one, specifying the index of the TraversalPath that was added.
      * Given that only ordered set define the notion of an index into the set, this
      * shall only be used for ordered TraversalPathSets.
      *
      * @param _theSet the set to which a TraversalPath was added
      * @param _addedIndex the index of the TraversalPath that was added
      * @param _addedTraversalPath the TraversalPath that was added to the set
      */
    public TraversalPathAddedEvent(
            ActiveTraversalPathSet _theSet,
            int                    _addedIndex,
            TraversalPath          _addedTraversalPath )
    {
        super(_theSet, _addedTraversalPath );

        theAddedIndex = _addedIndex;
    }

    /**
     * Obtain the index of the TraversalPath in the set at which it was added.
     *
     * @return the index of the TraversalPath in th set at which it was added
     */
    public int getAddedTraversalPathIndex()
    {
        return theAddedIndex;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "added"
                },
                new Object[] {
                    theAddedIndex
                });
    }

    /**
      * The index of the added TraversalPath.
      */
    protected int theAddedIndex;
}
