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
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;

/**
 * This TraversalSpecification qualifies another TraversalSpecification by first taking
 * a subset of the start set as the point of departure for the qualified TraversalSpecification,
 * and then taking another subset of the result. Either (but not both) of the MeshObjectSelectors
 * is optional.
 */
public class SelectiveTraversalSpecification
        extends
            AbstractTraversalSpecification
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method to create one without the start selector.
     *
     * @param theQualified the TraversalSpecification that this SelectiveTraversalSpecification qualifies
     * @param theEndSelector determines which of the found MeshObjects of the qualified
     *        TraversalSpecification are returned
     * @return the created SelectiveTraversalSpecification
     * @throws IllegalArgumentException if one of the arguments is null
     */
    public static SelectiveTraversalSpecification create(
            TraversalSpecification theQualified,
            MeshObjectSelector     theEndSelector )
    {
        // we are paranoid, so we check
        if( theQualified == null ) {
            throw new IllegalArgumentException( "empty qualified TraversalSpecification" );
        }
        if( theEndSelector == null ) {
            throw new IllegalArgumentException( "end selector cannot be null" );
        }
        return new SelectiveTraversalSpecification( null, theQualified, theEndSelector );
    }

    /**
     * Factory method to create one without the start selector.
     *
     * @param theStartSelector determines with which of the start MeshObjects we start our traversal
     * @param theQualified the TraversalSpecification that this SelectiveTraversalSpecification qualifies
     * @param theEndSelector determines which of the found MeshObjects of the qualified
     *        TraversalSpecification are returned
     * @return the created SelectiveTraversalSpecification
     * @throws IllegalArgumentException if theQualified or either theStartSelector or theEndSelector are null
     */
    public static SelectiveTraversalSpecification create(
            MeshObjectSelector     theStartSelector,
            TraversalSpecification theQualified,
            MeshObjectSelector     theEndSelector )
    {
        // we are paranoid, so we check
        if( theQualified == null ) {
            throw new IllegalArgumentException( "empty qualified TraversalSpecification" );
        }
        if( theStartSelector == null && theEndSelector == null ) {
            throw new IllegalArgumentException( "both selectors cannot be null" );
        }
        return new SelectiveTraversalSpecification( theStartSelector, theQualified, theEndSelector );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param startSelector determines the subset of the start MeshObjects
     * @param qualified the TraversalSpecification from the start MeshObjectSet's subset
     * @param endSelector determines the subset of the destination MeshObjects
     */
    protected SelectiveTraversalSpecification(
            MeshObjectSelector     startSelector,
            TraversalSpecification qualified,
            MeshObjectSelector     endSelector )
    {
        theStartSelector = startSelector;
        theQualified     = qualified;
        theEndSelector   = endSelector;
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
        MeshObjectSetFactory setFactory = start.getMeshBase().getMeshObjectSetFactory();

        if( theStartSelector != null && ! theStartSelector.accepts( start )) {
            return setFactory.obtainEmptyImmutableMeshObjectSet();
        }

        MeshObjectSet s = theQualified.traverse( start, considerEquivalents );

        if( theEndSelector != null ) {
            return setFactory.createImmutableMeshObjectSet( s, theEndSelector );
        } else {
            return s;
        }
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
        MeshObjectSetFactory setFactory = theSet.getFactory();

        MeshObjectSet realStart;
        if( theStartSelector != null ) {
            realStart = setFactory.createImmutableMeshObjectSet( theSet, theStartSelector );
        } else {
            realStart = theSet;
        }
        MeshObjectSet s = theQualified.traverse( realStart, considerEquivalents );

        if( theEndSelector != null ) {
            return setFactory.createImmutableMeshObjectSet( s, theEndSelector );
        } else {
            return s;
        }
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare to
     * @return true if the two objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof SelectiveTraversalSpecification ) {
            SelectiveTraversalSpecification realOther = (SelectiveTraversalSpecification) other;

            if( ! theQualified.equals( realOther.theQualified )) {
                return false;
            }

            if( theStartSelector != null ) {
                if( ! theStartSelector.equals( realOther.theStartSelector )) {
                    return false;
                }
            } else if( realOther.theStartSelector != null ) {
                return false;
            }

            if( theEndSelector != null ) {
                if( ! theEndSelector.equals( realOther.theEndSelector )) {
                    return false;
                }
            } else if( realOther.theEndSelector != null ) {
                return false;
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
        int ret = theQualified.hashCode();
        if( theStartSelector != null ) {
            ret ^= theStartSelector.hashCode();
        }
        if( theEndSelector != null ) {
            ret ^= theEndSelector.hashCode();
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
        MeshObject source = theEvent.getAffectedMeshObject();
        if( theStartSelector != null ) {
            if( ! theStartSelector.accepts( source )) {
                return false;
            }
        }

        if( theEvent.isAdditionalRoleUpdate() ) {
            MeshObjectRoleAddedEvent realEvent = (MeshObjectRoleAddedEvent) theEvent;
            MeshObject               otherSide = realEvent.getNeighborMeshObject();

            if( !theEndSelector.accepts( otherSide )) {
                return false;
            }
        } else {
            MeshObjectRoleRemovedEvent realEvent = (MeshObjectRoleRemovedEvent) theEvent;
            MeshObject                 otherSide = realEvent.getNeighborMeshObject();

            if( !theEndSelector.accepts( otherSide )) {
                return false;
            }
        }
        return theQualified.isAffectedBy( meshBase, theEvent );
    }

    /**
     * Obtain the TraversalSpecification that we qualify.
     *
     * @return the TraversalSpecification that we qualify
     */
    public TraversalSpecification getQualifiedTraversalSpecification()
    {
        return theQualified;
    }

    /**
     * Obtain our start MeshObjectSelector, if any.
     *
     * @return the start MeshObjectSelector
     */
    public MeshObjectSelector getStartSelector()
    {
        return theStartSelector;
    }

    /**
     * Obtain our end MeshObjectSelector, if any.
     *
     * @return the end MeshObjectSelector
     */
    public MeshObjectSelector getEndSelector()
    {
        return theEndSelector;
    }

    /**
     * The TraversalSpecification that we qualify.
     */
    protected TraversalSpecification theQualified;

    /**
     * The selector that knows which of the start MeshObjects we should traverse from.
     */
    protected MeshObjectSelector theStartSelector;

    /**
     * The selector that knows which of the destination MeshObjects we should actually return.
     */
    protected MeshObjectSelector theEndSelector;
}
