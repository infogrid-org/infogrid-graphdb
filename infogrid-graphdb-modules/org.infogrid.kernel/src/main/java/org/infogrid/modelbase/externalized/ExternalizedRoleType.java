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
import java.util.HashMap;
import java.util.Map;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.ArrayHelper;

/**
 * This is data wanting to become a RoleType, during reading.
 */
public class ExternalizedRoleType
{
    /**
     * Constructor.
     * 
     * @param rel the ExternalizedRelationshipType to which this ExternalizedRoleType belongs
     * @param source if true, this is the source ExternalizedRoleType
     * @param dest if true, this is the destination ExternalizedRoleType
     */
    public ExternalizedRoleType(
            ExternalizedRelationshipType rel,
            boolean                      source,
            boolean                      dest )
    {
        theRelationship = rel;
        isSource        = source;
        isDestination   = dest;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setEntityType(
            MeshTypeIdentifier newValue ) 
    {
        entityType = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public MeshTypeIdentifier getEntityType()
    {
        return entityType;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setMultiplicity(
            MultiplicityValue newValue ) 
    {
        multiplicity = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public MultiplicityValue getMultiplicity()
    {
        return multiplicity;
    }

    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addRefines(
            String newValue ) 
    {
        if( refines == null ) {
            refines = new ArrayList<String>();
        }
        refines.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<String> getRefines()
    {
        return refines;
    }

    /**
     * Add to property.
     *
     * @param key the locale
     * @param newValue the new value in the given locale
     */
    public void addUserName(
            String        key,
            PropertyValue newValue ) 
    {
        if( userNames == null ) {
            userNames = new HashMap<String,PropertyValue>();
        }
        userNames.put( key, newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public Map<String,PropertyValue> getUserNames()
    {
        return userNames;
    }

    /**
     * Add to property.
     *
     * @param key the locale
     * @param newValue the new value in the given locale
     */
    public void addUserDescription(
            String        key,
            PropertyValue newValue ) 
    {
        if( userDescriptions == null ) {
            userDescriptions = new HashMap<String,PropertyValue>();
        }
        userDescriptions.put( key, newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public Map<String,PropertyValue> getUserDescriptions()
    {
        return userDescriptions;
    }

    /**
     * Add a RoleTypeGuard class.
     *
     * @param newValue the new value
     */
    public void addRoleTypeGuardClassName(
            String newValue )
    {
        theRoleTypeGuardClassNames.add( newValue );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getRoleTypeGuardClassNames()
    {
        return ArrayHelper.copyIntoNewArray( theRoleTypeGuardClassNames, String.class );
    }

    /**
     * Convert to String, for user error messages.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        if( isSource ) {
            return "Source RoleType of RelationshipType: " + theRelationship.getIdentifier().toExternalForm();
        } else if( isDestination ) {
            return "Destination RoleType of RelationshipType: " + theRelationship.getIdentifier().toExternalForm();
        } else {
            return "RoleType of RelationshipType: " + theRelationship.getIdentifier().toExternalForm();
        }
    }

    /**
     * The RelationshipType to which this RoleType belongs.
     */
    protected ExternalizedRelationshipType theRelationship;
    
    /**
     * True if this is the source RoleType.
     */
    protected boolean isSource;
    
    /**
     * True if this is the destination RoleType.
     */
    protected boolean isDestination;
    
    /**
     * MeshObjectIdentifier identifying the EntityType playing this RoleType.
     */
    protected MeshTypeIdentifier entityType = null;

    /**
     * Multiplicity on this RoleType.
     */
    protected MultiplicityValue multiplicity = null;

    /**
     * String representation of the "supertype" RoleTypes.
     */
    protected ArrayList<String> refines = new ArrayList<String>();

    /**
     * The set of localized user names, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userNames = null;

    /**
     * The set of localized user descriptions, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userDescriptions = null;
    
    /**
     * The RoleTypeGuart class names.
     */
    protected ArrayList<String> theRoleTypeGuardClassNames = new ArrayList<String>();
}
