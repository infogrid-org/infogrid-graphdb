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

import org.infogrid.model.primitives.PropertyType;
import org.infogrid.util.ArrayHelper;

/**
 * <p>This specifies the value of one or more specific properties at a destination
 *    MeshObject that is obtained by traversing a TraversalSpecification. In spite of the
 *    somewhat similar name, this is NOT a TraversalSpecification, as we don't arrive at a
 *    set of MeshObjects when we use it, but at the values of certain of their properties.</p>
 *
 * <p>A TraversalToPropertySpecification consists of a TraversalSpecification, and
 *    a set of PropertyTypes. We find the properties
 *    by first traversing the TraversalSpecification, and the selecting the specified properties
 *    at the reached MeshObject (or MeshObjects). The TraversalSpecification is optional, indicating
 *    properties at the current MeshObject.</p>
 *
 * <p>Just like a TraversalSpecification, this class does not specify the start MeshObject as
 *    it is the specification for a traversal, not the result of an actual traversal that took
 *    place from a certain start MeshObject.
 */
public class TraversalToPropertySpecification
{
    /**
     * Factory method. This will create a TraversalToPropertySpecification that
     * leads to a single property after traversing a TraversalSpecification.
     *
     * @param traversalSpec the TraversalSpecification to be traversed first
     * @param destinationPropertyType the PropertyType that indicates the destination property
     * @return the created TraversalToPropertySpecification
     */
    public static TraversalToPropertySpecification create(
            TraversalSpecification traversalSpec,
            PropertyType           destinationPropertyType )
    {
        return new TraversalToPropertySpecification(
                traversalSpec,
                new PropertyType[] { destinationPropertyType } );
    }

    /**
     * Factory method. This will create a TraversalToPropertySpecification that
     * leads to a single property of the start MeshObject.
     *
     * @param destinationPropertyType the PropertyType that indicates the destination property
     * @return the created TraversalToPropertySpecification
     */
    public static TraversalToPropertySpecification create(
            PropertyType destinationPropertyType )
    {
        return new TraversalToPropertySpecification(
                null,
                new PropertyType[] { destinationPropertyType } );
    }

    /**
     * Factory method. This will create a TraversalToPropertySpecification that
     * leads to several properties after traversing a TraversalSpecification.
     *
     * @param traversalSpec the TraversalSpecification to be traversed first
     * @param destinationPropertyTypes the PropertyTypes that indicate the destination properties
     * @return the created TraversalToPropertySpecification
     */
    public static TraversalToPropertySpecification create(
            TraversalSpecification traversalSpec,
            PropertyType []        destinationPropertyTypes )
    {
        return new TraversalToPropertySpecification(
                traversalSpec,
                destinationPropertyTypes );
    }

    /**
     * Factory method. This will create a TraversalToPropertySpecification that
     * leads to several properties of the start MeshObject.
     *
     * @param destinationPropertyTypes the PropertyTypes that indicate the destination properties
     * @return the created TraversalToPropertySpecification
     */
    public static TraversalToPropertySpecification create(
            PropertyType [] destinationPropertyTypes )
    {
        return new TraversalToPropertySpecification(
                null,
                destinationPropertyTypes );
    }

    /**
     * Private constructor, use factory methods.
     *
     * @param traversalSpec the TraversalSpecification to be traversed first
     * @param destinationPropertyTypes the PropertyTypes that indicate the destination properties
     */
    protected TraversalToPropertySpecification(
            TraversalSpecification traversalSpec,
            PropertyType []        destinationPropertyTypes )
    {
        theTraversalSpecification   = traversalSpec;
        theDestinationPropertyTypes = destinationPropertyTypes;
    }

    /**
     * Obtain the TraversalSpecification (if any).
     *
     * @return the TraversalSpecification (if any)
     */
    public TraversalSpecification getTraversalSpecification()
    {
        return theTraversalSpecification;
    }

    /**
     * Obtain the PropertyTypes at the destination.
     *
     * @return the PropertyTypes at the destination
     */
    public PropertyType [] getPropertyTypes()
    {
        return theDestinationPropertyTypes;
    }

    /**
     * Determine equality. Two TraversalToPropertySpecification
     * are equal if their components are equal.
     *
     * @param other the Object to compare against
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof TraversalToPropertySpecification ) {

            TraversalToPropertySpecification realOther = (TraversalToPropertySpecification) other;

            if( theTraversalSpecification == null ) {
                if( realOther.theTraversalSpecification != null ) {
                    return false;
                }
            } else if( ! theTraversalSpecification.equals( realOther.theTraversalSpecification )) {
                return false;
            }

            if( theDestinationPropertyTypes.length != realOther.theDestinationPropertyTypes.length ) {
                return false;
            }

            return ArrayHelper.hasSameContentOutOfOrder( theDestinationPropertyTypes, realOther.theDestinationPropertyTypes, true );
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
        int ret = 17; // random
        if( theTraversalSpecification != null ) {
            ret ^= theTraversalSpecification.hashCode();
        }
        for( PropertyType current : theDestinationPropertyTypes ) {
            ret ^= current.hashCode();
        }
        return ret;
    }

    /**
     * Our TraversalSpecification (if any).
     */
    protected TraversalSpecification theTraversalSpecification;

    /**
     * The PropertyTypes at the destination end.
     */
    protected PropertyType [] theDestinationPropertyTypes;
}
