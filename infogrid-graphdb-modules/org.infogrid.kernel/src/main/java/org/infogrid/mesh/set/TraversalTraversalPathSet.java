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

import org.infogrid.model.traversal.TraversalSpecification;

/**
 * A TraversalPathSet that is created by having traversed a TraversalSpecification.
 */
public interface TraversalTraversalPathSet
        extends
            TraversalPathSet
{
    /**
     * Obtain the set of start MeshObjects. If this TraversalTraversalPathSet was created
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
