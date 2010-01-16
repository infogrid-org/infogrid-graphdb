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

import java.io.Serializable;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;

/**
 * <p>This interface is supported by all classes that can be
 * used to specify how to traverse from one MeshObject to
 * another (or a set of others).</p>
 *
 * <p>For example, the method MeshObject.traverse takes a TraversalSpecification
 * as an argument. The most basic TraversalSpecification is a RoleType, but
 * more complex ones are pre-defined within InfoGrid, and additional ones can
 * be created by the developer.</p>
 *
 * <p>In order to support polymorphism on TraversalSpecification, the
 *    MeshObject.traverse method then typically invokes TraversalSpecification.traverse.
 */
public interface TraversalSpecification
        extends
            Serializable
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
    public abstract MeshObjectSet traverse(
            MeshObject start );

    /**
     * Use this TraversalSpecification to traverse from the passed-in start MeshObject
     * to related MeshObjects. This method is defined on TraversalSpecification, so
     * different implementations of TraversalSpecification can implement different ways
     * of doing this. Specify whether relationships of equivalent MeshObjects
     * should be considered as well.
     *
     * @param start the start MeshObject for the traversal
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the result of the traversal
     */
    public abstract MeshObjectSet traverse(
            MeshObject start,
            boolean    considerEquivalents );

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects. This method is defined on TraversalSpecification, so
      * different implementations of TraversalSpecification can implement different ways
      * of doing this.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @return the result of the traversal
      */
    public abstract MeshObjectSet traverse(
            MeshObjectSet theSet );

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects. This method is defined on TraversalSpecification, so
      * different implementations of TraversalSpecification can implement different ways
      * of doing this. Specify whether relationships of equivalent MeshObjects
      * should be considered as well.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
      * @return the result of the traversal
      */
    public abstract MeshObjectSet traverse(
            MeshObjectSet theSet,
            boolean       considerEquivalents );

    /**
     * Determine whether a given event, with a source of from where we traverse the
     * TraversalSpecification, may affect the result of the traversal.
     *
     * @param meshBase the MeshBase in which we consider the event
     * @param theEvent the event that we consider
     * @return true if this event may affect the result of traversing from the MeshObject
     *         that sent this event
     */
    public abstract boolean isAffectedBy(
            MeshBase                  meshBase,
            MeshObjectRoleChangeEvent theEvent );

    /**
     * All implementations of this interface need an equals method.
     *
     * @param other the Object to compare against
     * @return true if the two objects are equal
     */
    @Override
    public abstract boolean equals(
            Object other );
}
