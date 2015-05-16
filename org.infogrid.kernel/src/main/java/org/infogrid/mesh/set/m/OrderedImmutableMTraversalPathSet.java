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

package org.infogrid.mesh.set.m;

import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.OrderedImmutableTraversalPathSet;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.model.traversal.TraversalPath;

/**
 * This TraversalPathSet has the same content as a passed-in TraversalPathSet,
 * but in an order specified by a passed-in sorting algorithm. This set is
 * immutable. It is kept in memory.
 */
public class OrderedImmutableMTraversalPathSet
    extends
        ImmutableMTraversalPathSet
    implements
        OrderedImmutableTraversalPathSet
{
    /**
     * Private constructor, use factory method. Note that the content must
     * have been limited to the maximum allowed number by the caller of the constructor.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param orderedContent the content of this set in order
     * @param max the maximum number of elements in the set
     */
    protected OrderedImmutableMTraversalPathSet(
            MeshObjectSetFactory factory,
            TraversalPath []     orderedContent,
            int                  max )
    {
        super( factory, orderedContent );
        
        theMaximum = max;
    }

    /**
     * Obtain a TraversalPath at a particular index.
     *
     * @param index the index specifying the TraversalPath that we are looking for
     * @return the TraversalPath at this index
     */
    public TraversalPath getTraversalPath(
            int index )
    {
        return currentContent[ index ];
    }

    /**
     * Determine the index of a certain TraversalPath in this ordered set.
     * Generally, index == findIndexOf( TraversalPath( index )).
     *
     * @param candidate the TraversalPath that we are looking for in this set
     * @return the index of the found TraversalPath, or -1 if not found
     */
    public int findIndexOf(
            TraversalPath candidate )
    {
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( candidate == currentContent[i] ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtain the destinations of the contained TraversalPaths as a MeshObjectSet.
     * While the same MeshObject may be a destination of more than one contained
     * TraversalPath, the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @return the destinations of the contained TraversalPaths as a MeshObjectSet
     */
    @Override
    public OrderedMeshObjectSet getDestinationsAsSet()
    {
        return (OrderedMeshObjectSet) super.getDestinationsAsSet();
    }

    /**
     * Obtain the MeshObjects found at the given index in all the contained TraversalPaths,
     * and return them as a MeshObjectSet.
     * While the same MeshObject may be a step in more than one contained TraversalPath,
     * the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @param index the index from which we want to obtain the MeshObject
     * @return the MeshObjects found at the given index as a MeshObjectSet
     */
    @Override
    public OrderedMeshObjectSet getStepAsSet(
            int index )
    {
        return (OrderedMeshObjectSet) super.getStepAsSet( index );
    }

    /**
     * Obtain the maximum number of elements in ths set.
     * 
     * @return the maximum number of elements in the set, or UNLIMITED
     */
    public int getMaximumElements()
    {
        return theMaximum;
    }
    
    /**
     * The maximum number of elements in the set.
     */
    protected int theMaximum;
}
