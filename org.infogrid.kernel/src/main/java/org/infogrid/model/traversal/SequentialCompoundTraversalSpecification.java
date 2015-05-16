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
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;

import org.infogrid.util.ArrayHelper;

/**
 * This implements a compound TraversalSpecification as a sequence of TraversalSpecifications
 * that are being traversed in sequence.
 */
public class SequentialCompoundTraversalSpecification
        extends
            AbstractTraversalSpecification
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method to construct a SequentialCompoundTraversalSpecification
     * from a sequence of TraversalSpecifications.
     *
     * @param steps the sequence of TraversalSpecifications
     * @return the created SequentialCompoundTraversalSpecification
     */
    public static SequentialCompoundTraversalSpecification create(
            TraversalSpecification [] steps )
    {
        // we are paranoid, so we check
        if( steps == null || steps.length < 2 ) {
            throw new IllegalArgumentException( ArrayHelper.arrayToString( steps ) + " must be at least of length 2" );
        }

        return new SequentialCompoundTraversalSpecification( steps );
    }

    /**
     * Convenience factory method for a sequence of two TraversalSpecifications.
     *
     * @param step1 the first traversal
     * @param step2 the first traversal
     * @return the created SequentialCompoundTraversalSpecification
     */
    public static SequentialCompoundTraversalSpecification create(
            TraversalSpecification step1,
            TraversalSpecification step2 )
    {
        return create( new TraversalSpecification[] { step1, step2 } );
    }

    /**
     * Convenience factory method for a sequence of three TraversalSpecifications.
     *
     * @param step1 the first traversal
     * @param step2 the first traversal
     * @param step3 the first traversal
     * @return the created SequentialCompoundTraversalSpecification
     */
    public static SequentialCompoundTraversalSpecification create(
            TraversalSpecification step1,
            TraversalSpecification step2,
            TraversalSpecification step3 )
    {
        return create( new TraversalSpecification[] { step1, step2, step3 } );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param steps the sequence of TraversalSpecifications
     */
    private SequentialCompoundTraversalSpecification(
            TraversalSpecification [] steps )
    {
        for( int i=0 ; i<steps.length ; ++i ) {
            if( steps[i] == null ) {
                throw new NullPointerException( "Step " + i + " in SequentialCompoundTraversalSpecification is null" );
            }
        }
        theSteps = steps;
    }

    /**
     * Obtain the first step.
     *
     * @return the first step
     */
    public TraversalSpecification getFirstStep()
    {
        return theSteps[0];
    }

    /**
     * Obtain all steps.
     *
     * @return all steps
     */
    public TraversalSpecification [] getSteps()
    {
        return theSteps;
    }

    /**
     * Obtain the number of steps in this SequentialComopoundTraversalSpecification.
     *
     * @return the number of steps in this SequentialComopoundTraversalSpecification
     */
    public int getStepCount()
    {
        return theSteps.length;
    }

    /**
     * Obtain another TraversalSpecification that is just like this
     * but without the first step.
     *
     * @return the same TraversalSpecification as this one without the first step
     */
    public TraversalSpecification withoutFirstStep()
    {
        switch( theSteps.length ) {
            case 2:
                return theSteps[1];

            default:
                return new SequentialCompoundTraversalSpecification(
                        ArrayHelper.copyIntoNewArray(
                                theSteps,
                                1,
                                theSteps.length,
                                TraversalSpecification.class ));
        }
    }

    /**
     * Use this TraversalSpecification to traverse from the passed-in start MeshObject
     * to related MeshObjects.
     *
     * @param start the start MeshObject for the traversal
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the result of the traversal
     */
    public MeshObjectSet traverse(
            MeshObject start,
            boolean    considerEquivalents )
    {
        MeshObjectSet current = theSteps[0].traverse( start, considerEquivalents );
        for( int i=1 ; i<theSteps.length ; ++i ) {
                current = current.traverse( theSteps[i], considerEquivalents );
            }
        return current;
    }

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
      *        if false, only this MeshObject will be used as the start
      * @return the result of the traversal
      */
    public MeshObjectSet traverse(
            MeshObjectSet theSet,
            boolean       considerEquivalents )
    {
        MeshObjectSet current = theSet;

        for( int i=0 ; i<theSteps.length ; ++i ) {
            current = current.traverse( theSteps[i], considerEquivalents );
        }
        return current;
    }

    /**
     * Determine equality.
     *
     * @param other the object to test against
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof SequentialCompoundTraversalSpecification ) {
            SequentialCompoundTraversalSpecification realOther = (SequentialCompoundTraversalSpecification) other;

            if( theSteps.length != realOther.theSteps.length ) {
                return false;
            }

            for( int i=0 ; i<theSteps.length ; ++i ) {
                if( !theSteps[i].equals( realOther.theSteps[i] )) {
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
        for( TraversalSpecification current : theSteps ) {
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
        return getFirstStep().isAffectedBy( meshBase, theEvent );
    }

    /**
     * The sequence of steps to traverse.
     */
    protected TraversalSpecification [] theSteps;
}
