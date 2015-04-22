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

import java.util.HashMap;
import java.util.Map;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;

/**
 * Convenience superclass for most of the other ExternalizedXXX. Roughly resembles MeshType.
 */
public class ExternalizedMeshType
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIdentifier(
            MeshTypeIdentifier newValue ) 
    {
        theIdentifier = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public MeshTypeIdentifier getIdentifier()
    {
        return theIdentifier;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setName(
            StringValue newValue ) 
    {
        theName = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public StringValue getName()
    {
        return theName;
    }

    /**
     * Add to property.
     *
     * @param key the locale
     * @param newValue the new value in this locale
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
     * @param newValue the new value in this locale
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
     * Set property.
     *
     * @param newValue the new value
     */
    public void setGenerateInterface(
            BooleanValue newValue ) 
    {
        generateInterface = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getGenerateInterface()
    {
        return generateInterface;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setGenerateImplementation(
            BooleanValue newValue ) 
    {
        generateImplementation = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getGenerateImplementation()
    {
        return generateImplementation;
    }

    /**
     * Convert to String, for user error messages.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        final String PREFIX = "org.infogrid.modelbase.externalized.Externalized";
        
        String name = getClass().getName();
        if( name.startsWith( PREFIX )) {
            name = name.substring( PREFIX.length() );
        }
        
        return name + ": " + theIdentifier;
    }

    /**
     * Identifier, if specified.
     */
    protected MeshTypeIdentifier theIdentifier = null;

    /**
      * Name.
      */
    protected StringValue theName = null;

    /**
     * The set of localized user names, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userNames = null;

    /**
     * The set of localized user descriptions, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userDescriptions = null;

    /**
     * DoGenerateInterface, with default.
     */
    protected BooleanValue generateInterface = BooleanValue.TRUE;

    /**
     * DoGenerateImplementation, with default.
     */
    protected BooleanValue generateImplementation = BooleanValue.TRUE;
}
