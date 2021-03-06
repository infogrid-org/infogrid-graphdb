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

/**
 * This is data wanting to become a ProjectedPropertyType, during reading.
 */
public class ExternalizedProjectedPropertyType
        extends
            ExternalizedPropertyType
{
    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addTraversalToPropertySpecification(
            ExternalizedTraversalToPropertySpecification newValue ) 
    {
        if( inputTraversalToPropertySpecifications == null ) {
            inputTraversalToPropertySpecifications = new ArrayList<ExternalizedTraversalToPropertySpecification>();
        }
        inputTraversalToPropertySpecifications.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedTraversalToPropertySpecification> getTraversalToPropertySpecifications()
    {
        return inputTraversalToPropertySpecifications;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setRawProjectionCode(
            String newValue ) 
    {
        rawProjectionCode = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String getRawProjectionCode()
    {
        return rawProjectionCode;
    }
    
    /**
      * List of TraversalToPropertySpecification.
      */
    protected ArrayList<ExternalizedTraversalToPropertySpecification> inputTraversalToPropertySpecifications
            = new ArrayList<ExternalizedTraversalToPropertySpecification>();

    /**
     * ProjectionCode in raw form.
     */
    protected String rawProjectionCode = null;
}
