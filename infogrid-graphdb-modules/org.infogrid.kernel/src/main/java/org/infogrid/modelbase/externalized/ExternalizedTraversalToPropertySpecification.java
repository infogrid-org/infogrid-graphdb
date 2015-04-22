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

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.util.StringHelper;

/**
 * This is data wanting to become a TraversalToPropertySpecification.
 */
public class ExternalizedTraversalToPropertySpecification
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setTraversalSpecification(
            ExternalizedTraversalSpecification newValue ) 
    {
        traversalSpecification = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ExternalizedTraversalSpecification getTraversalSpecification()
    {
        return traversalSpecification;
    }
    
    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addPropertyType(
            MeshTypeIdentifier newValue ) 
    {
        if( propertyTypes == null ) {
            propertyTypes = new ArrayList<MeshTypeIdentifier>();
        }
        propertyTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<MeshTypeIdentifier> getPropertyTypes()
    {
        return propertyTypes;
    }

    /**
     * Convert to String, for user error messages.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        return "Traverse " + traversalSpecification + " to PropertyType(s) " + StringHelper.join( propertyTypes.iterator() );
    }
    
    /**
     * The traversal specification.
     */
    protected ExternalizedTraversalSpecification traversalSpecification;

    /**
      * List of Identifiers of PropertyTypes qualifying the traversal specification.
      */
    protected ArrayList<MeshTypeIdentifier> propertyTypes = new ArrayList<MeshTypeIdentifier>();
}
