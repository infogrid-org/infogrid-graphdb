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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.util.ArrayHelper;

/**
 * Implementation of a CompoundTraversalSpecification that traverses several
 * other TraversalSpecifications in parallel. In other words, the result of a traversal,
 * using this AlternativeCompoundTraversalSpecification from a certain MeshObject, is
 * the unification of all results of traversing from the same MeshObject using the
 * component TraversalSpecifications.
 */
public class AlternativeCompoundTraversalSpecification
        extends
            AbstractTraversalSpecification
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param alt the components of this AlternativeCompoundTraversalSpecification
     * @return the created AlternativeCompoundTraversalSpecification
     */
    public static AlternativeCompoundTraversalSpecification create(
            TraversalSpecification [] alt )
    {
        // we are paranoid, so we check
        if( alt == null || alt.length < 2 ) {
            throw new IllegalArgumentException( ArrayHelper.arrayToString( alt ) + " must be at least of length 2" );
        }

        for( int i=0 ; i<alt.length ; ++i ) {
            if( alt[i] == null ) {
                throw new IllegalArgumentException( "Alternative TraversalSpecification is null: " + ArrayHelper.arrayToString( alt ));
            }
        }
        return new AlternativeCompoundTraversalSpecification( alt );
    }

    /**
     * Convenience factory method.
     *
     * @param alt1 the first traversal alternative
     * @param alt2 the second traversal alternative
     * @return the created AlternativeCompoundTraversalSpecification
     */
    public static AlternativeCompoundTraversalSpecification create(
            TraversalSpecification alt1,
            TraversalSpecification alt2 )
    {
        return create( new TraversalSpecification[] { alt1, alt2 } );
    }

    /**
     * Convenience factory method.
     *
     * @param alt1 the first traversal alternative
     * @param alt2 the second traversal alternative
     * @param alt3 the third traversal alternative
     * @return the created AlternativeCompoundTraversalSpecification
     */
    public static AlternativeCompoundTraversalSpecification create(
            TraversalSpecification alt1,
            TraversalSpecification alt2,
            TraversalSpecification alt3 )
    {
        return create( new TraversalSpecification[] { alt1, alt2, alt3 } );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param alt the components of this AlternativeCompoundTraversalSpecification
     */
    private AlternativeCompoundTraversalSpecification(
            TraversalSpecification [] alt )
    {
        theAlternatives = alt;
    }

    /**
     * Obtain the component alternatives.
     *
     * @return the component alternatives
     */
    public TraversalSpecification [] getAlternatives()
    {
        return theAlternatives;
    }

    /**
     * Use this AlternativeCompoundTraversalSpecification to traverse from the passed-in MeshObject
     * to related MeshObjects.
     *
     * @param start the start MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the reached MeshObjectSet
     */
    public MeshObjectSet traverse(
            MeshObject start,
            boolean    considerEquivalents )
    {
        MeshObjectSetFactory setFactory = start.getMeshBase().getMeshObjectSetFactory();

        MeshObjectSet [] sets = new MeshObjectSet[ theAlternatives.length ];
        for( int i=0 ; i<sets.length ; ++i ) {
            sets[i] = theAlternatives[i].traverse( start, considerEquivalents );
        }
        return setFactory.createImmutableMeshObjectSetUnification( sets );
    }

    /**
     * Use this AlternativeCompoundTraversalSpecification to traverse from the passed-in MeshObjectSet
     * to related MeshObjects.
     *
     * @param theSet the start MeshObjectSet
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the reached MeshObjectSet
     */
    public MeshObjectSet traverse(
            MeshObjectSet theSet,
            boolean       considerEquivalents )
    {
        MeshObjectSetFactory setFactory = theSet.getFactory();

        MeshObjectSet [] sets = new MeshObjectSet[ theAlternatives.length ];
        for( int i=0 ; i<sets.length ; ++i ) {
            sets[i] = theAlternatives[i].traverse( theSet, considerEquivalents );
        }
        return setFactory.createImmutableMeshObjectSetUnification( sets );
    }

    /**
     * Determine equality.
     *
     * @param other the object to compare against
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof AlternativeCompoundTraversalSpecification ) {

            AlternativeCompoundTraversalSpecification realOther = (AlternativeCompoundTraversalSpecification) other;

            if( theAlternatives.length != realOther.theAlternatives.length ) {
                return false;
            }

            for( int i=0 ; i<theAlternatives.length ; ++i ) {                
                if( !theAlternatives[i].equals( realOther.theAlternatives[i] )) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Hash code.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = 0;
        for( TraversalSpecification current : theAlternatives ) {
            ret ^= current.hashCode();
        }
        return ret;
    }

    /**
     * Determine whether a given event, with a source of from where we traverse the
     * TraversalSpecification, may affect the result of the traversal.
     *
     * @param theEvent the event that we consider
     * @return true if this event may affect the result of traversing from the MeshObject
     *         that sent this event
     */
    public boolean isAffectedBy(
            MeshBase                  meshBase,
            MeshObjectRoleChangeEvent theEvent )
    {
        for( int i=0 ; i<theAlternatives.length ; ++i ) {

            if( theAlternatives[i].isAffectedBy( meshBase, theEvent )) {
                return true;
            }
        }
        return false;
    }

    /**
     * The alternatives to traverse.
     */
    protected TraversalSpecification [] theAlternatives;
}
