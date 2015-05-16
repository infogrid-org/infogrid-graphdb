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

import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * An ActiveMeshObjectSet that is created by having traversed a TraversalSpecification.
 * Currently, no equivalent exists for ImmutableMeshObjectSets: the overhead does not
 * seem to justify the API, as there seem to be few circumstances where these methods would actually
 * be invoked on ImmutableMeshObjectSets; it's different for ActiveMeshObjectSets.
 */
public interface TraversalActiveMeshObjectSet
        extends
            ActiveMeshObjectSet
{
    /**
     * Obtain the set of start MeshObjects. If this TraversalMeshObjectSet was created
     * by traversing from a single MeshObject, this will return a single-element set.
     *
     * @return the start NeshObjects
     */
    public abstract MeshObjectSet getStartOfTraversalSet();

    /**
     * Obtain the TraversalSpecification that was traversed.
     *
     * @return the TraversalSpecification
     */
    public abstract TraversalSpecification getTraversalSpecification();
}

