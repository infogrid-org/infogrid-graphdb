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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.OrderedImmutableMeshObjectSet;
import org.infogrid.mesh.set.OrderedMeshObjectSet;

/**
 * This MeshObjectSet has the same content as a passed-in MeshObjectSet,
 * but in an order specified by a passed-in sorting algorithm. This set is
 * immutable. It is kept in memory.
 */
public class OrderedImmutableMMeshObjectSet
    extends
        ImmutableMMeshObjectSet
    implements
        OrderedImmutableMeshObjectSet
{
    /**
     * Private constructor, use factory method. Note that the content must
     * have been limited to the maximum allowed number by the caller of the constructor.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param orderedContent the content of this set in order
     * @param max the maximum number of elements in the set
     */
    OrderedImmutableMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObject []        orderedContent,
            int                  max )
    {
        super( factory, orderedContent );
        
        theMaximum = max;
    }

    /**
     * Obtain a specific element in the set.
     *
     * @param index the index of the requested element
     * @return the found MeshObject at this index
     * @throws ArrayIndexOutOfBoundsException thrown if the index is out of bounds
     */
    public MeshObject getMeshObject(
            int index )
    {
        return currentContent[ index ];
    }

    /**
     * Obtain the first MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the first MeshObject, if any
     */
    public MeshObject getFirstMeshObject()
    {
        if( currentContent != null && currentContent.length > 0 ) {
            return currentContent[0];
        } else {
            return null;
        }
    }
    
    /**
     * Obtain the last MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the last MeshObject, if any
     */
    public MeshObject getLastMeshObject()
    {
        if( currentContent != null && currentContent.length > 0 ) {
            return currentContent[currentContent.length-1];
        } else {
            return null;
        }
    }

    /**
     * Determine the index of a certain MeshObject in this ordered set.
     * Because we know nothing about how we are ordered, we simple search linearly.
     *
     * @param candidate the MeshObject we look for
     * @return index of the found MeshObject, or -1 if not found
     */
    public int findIndexOf(
            MeshObject candidate )
    {
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( candidate == currentContent[i] ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Create a subset of this set by providing a MeshObjectSelector that will select the MeshObjects
     * to be selected for the subset. This method will return all matches in this set, keeping the
     * order of this OrderedMeshObjectSet.
     *
     * @param selector the criteria for selection
     * @return subset of this set
     */
    @Override
    public OrderedMeshObjectSet subset(
            MeshObjectSelector selector )
    {
        return theFactory.createOrderedImmutableMeshObjectSet( getMeshObjects(), selector );
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
