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

import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;

import org.infogrid.model.traversal.TraversalPath;

/**
 * This in-memory ActiveTraversalPathSet unifies N ActiveTraversalPathSets.
 */
public class UnifyingActiveMTraversalPathSet
        extends
            AbstractActiveMTraversalPathSet
        implements
            ActiveTraversalPathSetListener
{
    /**
     * Private constructor, use factory method.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     * @param operands the ActiveTraversalPathSets to be unified
     */
    protected UnifyingActiveMTraversalPathSet(
            MeshObjectSetFactory      factory,
            ActiveTraversalPathSet [] operands )
    {
        super( factory );

        theOperands = operands;

        // count first
        int count = 0;
        for( int i=0 ; i<theOperands.length ; ++i ) {
            count += theOperands[i].getTraversalPaths().length;
        }

        TraversalPath [] content = new TraversalPath[ count ];
        for( int i=theOperands.length-1 ; i>=0 ; --i ) {
            TraversalPath [] current = theOperands[i].getTraversalPaths();

            for( int j=current.length-1 ; j>=0 ; --j ) {
                content[--count] = current[j];
            }

            theOperands[i].addWeakActiveTraversalPathSetListener( this );
        }
        setInitialContent( content );
    }

    /**
      * Indicates that a TraversalPath was added to this set.
      *
      * @param event the event
      */
    public void traversalPathAdded(
            TraversalPathAddedEvent event )
    {
        certainlyAdd( event.getDeltaValue() );
    }

    /**
      * Indicates that a TraversalPath was removed from this set.
      *
      * @param event the event
      */
    public void traversalPathRemoved(
            TraversalPathRemovedEvent event )
    {
        certainlyRemove( event.getDeltaValue() );
    }

    /**
     * We are told that the set was reordered -- ignore.
     *
     * @param event the event
     */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        // no op
    }

    /**
     * The operands that we unify. Keep references to prevent that they will
     * be garbage collected
     */
    protected ActiveTraversalPathSet [] theOperands;
}
