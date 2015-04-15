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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;

/**
 * Abstract base class for different TraversalSpecification implementations.
 */
public abstract class AbstractTraversalSpecification
        implements
            TraversalSpecification
{
    /**
     * Use this TraversalSpecification to traverse from the passed-in start MeshObject
     * to related MeshObjects. This method is defined on TraversalSpecification, so
     * different implementations of TraversalSpecification can implement different ways
     * of doing this.
     *
     * @param start the start MeshObject for the traversal
     * @return the result of the traversal
     */
    public final MeshObjectSet traverse(
            MeshObject start )
    {
        return traverse( start, true );
    }

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects. This method is defined on TraversalSpecification, so
      * different implementations of TraversalSpecification can implement different ways
      * of doing this.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @return the result of the traversal
      */
    public final MeshObjectSet traverse(
            MeshObjectSet theSet )
    {
        return traverse( theSet, true );
    }
}
