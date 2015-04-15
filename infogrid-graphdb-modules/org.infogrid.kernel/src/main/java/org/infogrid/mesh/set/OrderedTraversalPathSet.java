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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import org.infogrid.model.traversal.TraversalPath;

/**
 * A TraversalPathSet whose content is ordered according to some criteria.
 * Consequently, it provides methods that only are appropriate for OrderedTraversalPathSets.
 */
public interface OrderedTraversalPathSet
    extends
        TraversalPathSet
{
    /**
     * Obtain a TraversalPath at a particular index.
     *
     * @param index the index specifying the TraversalPath that we are looking for
     * @return the TraversalPath at this index
     */
    public abstract TraversalPath getTraversalPath(
            int index );

    /**
     * Determine the index of a certain TraversalPath in this ordered set.
     * Generally, index == findIndexOf( TraversalPath( index )).
     *
     * @param candidate the TraversalPath that we are looking for in this set
     * @return the index of the found TraversalPath, or -1 if not found
     */
    public abstract int findIndexOf(
            TraversalPath candidate );

    /**
     * Obtain the destinations of the contained TraversalPaths as a MeshObjectSet.
     * While the same MeshObject may be a destination of more than one contained
     * TraversalPath, the MeshObjectSet naturally only contains this MeshObject once.
     * Given that this TraversalPathSet is ordered, the returned MeshObjectSet is ordered, too.
     *
     * @return the destinations of the contained TraversalPaths as a MeshObjectSet
     */
    public abstract OrderedMeshObjectSet getDestinationsAsSet();

    /**
     * Obtain the MeshObjects found at the given index in all the contained TraversalPaths,
     * and return them as a MeshObjectSet.
     * While the same MeshObject may be a step in more than one contained TraversalPath,
     * the MeshObjectSet naturally only contains this MeshObject once.
     * Given that this TraversalPathSet is ordered, the returned MeshObjectSet is ordered, too.
     *
     * @param index the index from which we want to obtain the MeshObject
     * @return the MeshObjects found at the given index as a MeshObjectSet
     */
    public abstract OrderedMeshObjectSet getStepAsSet(
            int index );

    /**
     * Obtain the maximum number of elements in ths set.
     * 
     * @return the maximum number of elements in the set, or UNLIMITED
     */
    public abstract int getMaximumElements();

    /**
     * Special code to specifiy "all levels" instead of a limited number.
     */
    public static final int UNLIMITED = -1;
}


