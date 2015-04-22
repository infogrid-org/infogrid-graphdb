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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;

/**
 * This is a MeshObjectSet whose content is ordered according to some criteria.
 * Consequently, it provides methods that only are appropriate for OrderedMeshObjectSets.
 */
public interface OrderedMeshObjectSet
    extends
        MeshObjectSet
{
    /**
     * Obtain a MeshObject at a particular index.
     *
     * @param index the index specifying the MeshObject that we are looking for
     * @return the MeshObject at this index
     */
    public abstract MeshObject getMeshObject(
            int index );

    /**
     * Obtain the first MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the first MeshObject, if any
     */
    public abstract MeshObject getFirstMeshObject();
    
    /**
     * Obtain the last MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the last MeshObject, if any
     */
    public abstract MeshObject getLastMeshObject();
    
    /**
     * Determine the index of a certain MeshObject in this ordered set.
     * Generally, index == findIndexOf( getMeshObject( index )).
     *
     * @param candidate the MeshObject that we are looking for in this set
     * @return the index of the found MeshObject, or -1 if not found
     */
    public abstract int findIndexOf(
            MeshObject candidate );

    /**
     * Obtain the maximum number of elements in ths set.
     * 
     * @return the maximum number of elements in the set, or UNLIMITED
     */
    public abstract int getMaximumElements();

    /**
     * Create a subset of this set by providing a MeshObjectSelector that will select the MeshObjects
     * to be selected for the subset. This method will return all matches in this set, keeping the
     * order of this OrderedMeshObjectSet.
     *
     * @param selector the criteria for selection
     * @return subset of this set
     */
    public abstract OrderedMeshObjectSet subset(
            MeshObjectSelector selector );

    /**
     * Special code to specifiy "all levels" instead of a limited number.
     */
    public static final int UNLIMITED = -1;
}
