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
import org.infogrid.model.primitives.PropertyValue;

/**
 * This is data wanting to become an EnumeratedValue, during reading.
 */
public class ExternalizedEnum
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setValue(
            String newValue ) 
    {
        value = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String getValue()
    {
        return value;
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
     * Convert to String, for user error messages.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        return "Enumerated value: " + value;
    }
    
    /**
      * The actual value.
      */
    protected String value = null;

    /**
     * The set of localized user names, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userNames = null;

    /**
     * The set of localized user descriptions, keyed by locale, if any.
     */
    protected HashMap<String,PropertyValue> userDescriptions = null;
}
