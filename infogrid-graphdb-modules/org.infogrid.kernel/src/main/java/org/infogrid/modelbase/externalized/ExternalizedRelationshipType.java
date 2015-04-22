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

/**
 * This is data wanting to become a RelationshipType, during reading.
 */
public class ExternalizedRelationshipType
        extends
            ExternalizedAttributableMeshType
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setSource(
            ExternalizedRoleType newValue ) 
    {
        source = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ExternalizedRoleType getSource()
    {
        return source;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setDestination(
            ExternalizedRoleType newValue ) 
    {
        destination = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ExternalizedRoleType getDestination()
    {
        return destination;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setSourceDestination(
            ExternalizedRoleType newValue ) 
    {
        sourceDestination = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ExternalizedRoleType getSourceDestination()
    {
        return sourceDestination;
    }

    /**
      * This describes the source RoleType.
      */
    protected ExternalizedRoleType source;

    /**
      * This describes the source RoleType.
      */
    protected ExternalizedRoleType destination;

    /**
     * If this is a unary RelationshipType (but only then), we use this instaead of src and/or dest.
     */
    protected ExternalizedRoleType sourceDestination;
}
