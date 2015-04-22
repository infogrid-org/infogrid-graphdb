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
import org.infogrid.model.primitives.BooleanValue;

/**
 * This is data wanting to become an AttributableMeshType, during reading.
 */
public abstract class ExternalizedAttributableMeshType
    extends
        ExternalizedMeshType
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIsAbstract(
            BooleanValue newValue ) 
    {
        isAbstract = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getIsAbstract()
    {
        return isAbstract;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addPropertyType(
            ExternalizedPropertyType newValue ) 
    {
        propertyTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedPropertyType> getPropertyTypes()
    {
        return propertyTypes;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addProjectedPropertyType(
            ExternalizedPropertyType newValue ) 
    {
        projectedPropertyTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedPropertyType> getProjectedPropertyTypes()
    {
        return projectedPropertyTypes;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addPropertyTypeGroup(
            ExternalizedPropertyTypeGroup newValue ) 
    {
        propertyTypeGroups.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedPropertyTypeGroup> getPropertyTypeGroups()
    {
        return propertyTypeGroups;
    }

    /**
     * IsAbstract.
     */
    protected BooleanValue isAbstract = BooleanValue.FALSE;

    /**
     * List of ExternalizedPropertyType.
     */
    protected ArrayList<ExternalizedPropertyType> propertyTypes
            = new ArrayList<ExternalizedPropertyType>();

    /**
     * List of ExternalizedProjectedPropertyType.
     */
    protected ArrayList<ExternalizedPropertyType> projectedPropertyTypes
            = new ArrayList<ExternalizedPropertyType>();

    /**
     * List of ExternalizedPropertyTypeGroups.
     */
    protected ArrayList<ExternalizedPropertyTypeGroup> propertyTypeGroups
            = new ArrayList<ExternalizedPropertyTypeGroup>();
}
