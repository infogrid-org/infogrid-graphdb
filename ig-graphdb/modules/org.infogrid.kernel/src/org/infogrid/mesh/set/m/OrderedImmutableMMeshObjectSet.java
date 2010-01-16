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

package org.infogrid.mesh.set.m;

import org.infogrid.mesh.MeshObject;

import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.OrderedImmutableMeshObjectSet;

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
