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
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * A MeshObjectSelector that accepts all MeshObjects that have at least N
 * and less than M related MeshObjects via a certain TraversalSpecification.
 */
public class ByRelatedMeshObjectSelector
        extends
            AbstractMeshObjectSelector
{
    /**
     * Factory method: at least 1 related MeshObject of any type.
     *
     * @param traversal the TraversalSpecification
     * @return the created ByRelatedMeshObjectSelector
     */
    public static ByRelatedMeshObjectSelector createOneOrMore(
            TraversalSpecification traversal )
    {
        return new ByRelatedMeshObjectSelector( traversal, null, 1, Integer.MAX_VALUE );
    }

    /**
     * Factory method: at least 1 related MeshObject that meets the delegate MeshObjectSelector
     *
     * @param traversal the TraversalSpecification
     * @param delegate the delegate MeshObjectSelector
     * @return the created ByRelatedMeshObjectSelector
     */
    public static ByRelatedMeshObjectSelector createOneOrMore(
            TraversalSpecification traversal,
            MeshObjectSelector     delegate )
    {
        return new ByRelatedMeshObjectSelector( traversal, delegate, 1, Integer.MAX_VALUE );
    }

    /**
     * Factory method: related to the given MeshObject.
     *
     * @param traversal the TraversalSpecification
     * @param related the MeshObject to be related to.
     * @return the created ByRelatedMeshObjectSelector
     */
    public static ByRelatedMeshObjectSelector createOneOrMore(
            TraversalSpecification traversal,
            MeshObject             related )
    {
        MeshObjectSelector delegate = RightInstanceMeshObjectSelector.create( related );
        return new ByRelatedMeshObjectSelector( traversal, delegate, 1, Integer.MAX_VALUE );
    }

    /**
     * Factory method: related to the MeshObject with the given Identifier.
     *
     * @param traversal the TraversalSpecification
     * @param relatedIdentifier identifier of the MeshObject to be related to.
     * @return the created ByRelatedMeshObjectSelector
     */
    public static ByRelatedMeshObjectSelector createOneOrMore(
            TraversalSpecification traversal,
            MeshObjectIdentifier   relatedIdentifier )
    {
        MeshObjectSelector delegate = RightInstanceMeshObjectSelector.create( relatedIdentifier );
        return new ByRelatedMeshObjectSelector( traversal, delegate, 1, Integer.MAX_VALUE );
    }

    /**
     * Factory method.
     *
     * @param traversal the TraversalSpecification
     * @param delegate the delegate MeshObjectSelector
     * @param atLeast the minimum number of MeshObjects we need to find
     * @param lessThan the upper ceiling of MeshObjects we need to find (not inclusive)
     * @return the created ByRelatedMeshObjectSelector
     */
    public static ByRelatedMeshObjectSelector create(
            TraversalSpecification traversal,
            MeshObjectSelector     delegate,
            int                    atLeast,
            int                    lessThan )
    {
        return new ByRelatedMeshObjectSelector( traversal, delegate, atLeast, lessThan );
    }

    /**
     * Constructor.
     *
     * @param traversal the TraversalSpecification
     * @param delegate the delegate MeshObjectSelector
     * @param atLeast the minimum number of MeshObjects we need to find
     * @param lessThan the upper ceiling of MeshObjects wwe need to find (not inclusive)
     */
    protected ByRelatedMeshObjectSelector(
            TraversalSpecification traversal,
            MeshObjectSelector     delegate,
            int                    atLeast,
            int                    lessThan )
    {
        theTraversalSpecification = traversal;
        theDelegate               = delegate;
        theAtLeast                = atLeast;
        theLessThan               = lessThan;
    }

    /**
     * Determine whether this MeshObject shall be selected.
     *
     * @param candidate MeshObject to test
     * @return true if this MeshObject is an instance of the specified type
     */
    public boolean accepts(
            MeshObject candidate )
    {
        if( candidate == null ) {
            throw new IllegalArgumentException();
        }

        MeshObjectSet found = candidate.traverse( theTraversalSpecification );
        
        if( theDelegate != null ) {
            found = found.getFactory().createImmutableMeshObjectSet( found, theDelegate );
        }
        int size = found.size();
        
        if( size < theAtLeast ) {
            return false;
        }
        if( size >= theLessThan ) {
            return false;
        }
        return true;
    }

    /**
     * Obtain the TraversalSpecification.
     *
     * @return the TraversalSpecification
     */
    public TraversalSpecification getTraversalSpecification()
    {
        return theTraversalSpecification;
    }

    /**
     * Obtain the delegate MeshObjectSelector, if any.
     *
     * @return the delegate MeshObjectSelector
     */
    public MeshObjectSelector getDelegate()
    {
        return theDelegate;
    }

    /**
     * Determine the lower limit.
     *
     * @return the lower limit
     */
    public int getAtLeast()
    {
        return theAtLeast;
    }

    /**
     * Determine the upper limit.
     *
     * @return the upper limit
     */
    public int getLessThan()
    {
        return theLessThan;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare against
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof ByRelatedMeshObjectSelector ) {
            ByRelatedMeshObjectSelector realOther = (ByRelatedMeshObjectSelector) other;

            if( theAtLeast != realOther.theAtLeast ) {
                return false;
            }
            if( theLessThan != realOther.theLessThan ) {
                return false;
            }
            if( theDelegate == null ) {
                if( realOther.theDelegate != null ) {
                    return false;
                }
            } else if( !theDelegate.equals( realOther.theDelegate )) {
                return false;
            }
            if( theTraversalSpecification == null ) {
                if( realOther.theTraversalSpecification != null ) {
                    return false;
                }
            } else if( !theTraversalSpecification.equals( realOther.theTraversalSpecification )) {
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * Hash code.
     *
     * @return hash code.
     */
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 61 * hash + (this.theTraversalSpecification != null ? this.theTraversalSpecification.hashCode() : 0);
        hash = 61 * hash + (this.theDelegate != null ? this.theDelegate.hashCode() : 0);
        hash = 61 * hash + this.theAtLeast;
        hash = 61 * hash + this.theLessThan;
        return hash;
    }

    /**
     * The TraversalSpecification.
     */
    protected TraversalSpecification theTraversalSpecification;

    /**
     * The delegate MeshObjectSelector.
     */
    protected MeshObjectSelector theDelegate;

    /**
     * Lower limit, inclusive.
     */
    protected int theAtLeast;
    
    /**
     * Upper limit, exclusive.
     */
    protected int theLessThan;
}
