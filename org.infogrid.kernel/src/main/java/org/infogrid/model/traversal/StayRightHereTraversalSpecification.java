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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;

/**
 * A degenerate TraversalSpecification that does not perform any traversal of any kind.
 */
public class StayRightHereTraversalSpecification
        extends
            AbstractTraversalSpecification
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method always returns the same singleton instance.
     * 
     * @return the singleton instance
     */
    public static StayRightHereTraversalSpecification create()
    {
        return SINGLETON;
    }

    /**
     * Private constructor, use factory method.
     */
    private StayRightHereTraversalSpecification()
    {
        // do nothing
    }

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
    public MeshObjectSet traverse(
            MeshObject start,
            boolean    considerEquivalents )
    {
        MeshObjectSet ret;
        if( considerEquivalents ) {
            ret = start.getEquivalents();

        } else {
            MeshObjectSetFactory setFactory = start.getMeshBase().getMeshObjectSetFactory();
            ret = setFactory.createSingleMemberImmutableMeshObjectSet( start );
        }
        return ret;
    }

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
    public MeshObjectSet traverse(
            MeshObjectSet theSet,
            boolean       considerEquivalents )
    {
        MeshObjectSet ret;
        if( considerEquivalents ) {
            MeshObjectSetFactory setFactory = theSet.getMeshBase().getMeshObjectSetFactory();

            ret = setFactory.obtainEmptyImmutableMeshObjectSet();
            for( MeshObject current : theSet ) {
                MeshObjectSet equivalents = current.getEquivalents();
                ret = setFactory.createImmutableMeshObjectSetUnification( ret, equivalents );
            }
        } else {
            ret = theSet;
        }
        return ret;
    }

    /**
     * Determine whether a given event, with a source of from where we traverse the
     * TraversalSpecification, may affect the result of the traversal.
     *
     * @param meshBase the MeshBase in which we consider the event
     * @param theEvent the event that we consider
     * @return true if this event may affect the result of traversing from the MeshObject
     *         that sent this event
     */
    public boolean isAffectedBy(
            MeshBase                  meshBase,
            MeshObjectRoleChangeEvent theEvent )
    {
        return false;
    }

    /**
     * All implementations of this interface need an equals method.
     *
     * @param other the Object to compare against
     * @return true if the two objects are equal
     */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(
            Object other )
    {
        return this == other; // singleton instance
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * The singleton instance of this class.
     */
    private static final StayRightHereTraversalSpecification SINGLETON = new StayRightHereTraversalSpecification();
}
