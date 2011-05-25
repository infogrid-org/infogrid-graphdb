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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.modelbase.externalized;

import java.util.regex.Pattern;
import org.infogrid.model.primitives.StringDataType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.L10MapImpl;
import org.infogrid.util.L10StringMapImpl;

/**
 * This is data wanting to become a regular expression, during reading.
 */
public class ExternalizedRegex
{
    /**
     * Set property.
     *
     * @param newValue the new thePattern
     */
    public void setRegexString(
            String newValue )
    {
        thePattern = newValue;
    }

    /**
     * Get property.
     *
     * @return the thePattern
     */
    public String getRegexString()
    {
        return thePattern;
    }

    /**
     * Set default thePattern.
     *
     * @param newValue the new thePattern
     */
    public void setDefaultValue(
            String newValue )
    {
        theDefaultValue = newValue;
    }

    /**
     * Get the default thePattern.
     *
     * @return the thePattern
     */
    public String getDefaultValue()
    {
        return theDefaultValue;
    }

    /**
     * Add an error message in a particular locale.
     *
     * @param newLocale the locale
     * @param newValue the new error message
     */
    public void addRegexErrorString(
            String newLocale,
            String newValue )
    {
        if( theErrorMessages == null ) {
            theErrorMessages = L10StringMapImpl.create( null );
        }
        if( newLocale == null || newLocale.length() == 0 ) {
            theErrorMessages.setDefault( newValue );
        } else {
            theErrorMessages.put( newLocale, newValue );
        }
    }

    /**
     * Get property as a Pattern.
     *
     * @return the Pattern
     */
    public Pattern getAsPattern()
    {
        if( thePattern != null ) {
            Pattern ret = Pattern.compile( thePattern );
            return ret;
        } else {
            return null;
        }
    }

    /**
     * Get default thePattern as StringValue.
     *
     * @return the StringValue
     */
    public StringValue getDefaultAsStringValue()
    {
        if( theDefaultValue != null ) {
            return StringValue.create( theDefaultValue );
        }else {
            return null;
        }
    }

    /**
     * Get as StringDataType.
     *
     * @return the StringDataType
     */
    public StringDataType getAsStringDataType()
    {
        StringDataType ret;
        if( thePattern == null ) {
            if( theDefaultValue == null ) {
                ret = StringDataType.create();
            } else {
                ret = StringDataType.create( StringValue.create( theDefaultValue ));
            }
        } else {
            Pattern p = Pattern.compile( thePattern );

            if( theDefaultValue != null ) {
                ret = StringDataType.create( p, StringValue.create( theDefaultValue ), theErrorMessages );
            } else {
                throw new IllegalArgumentException( "Cannot create a StringDataType that has a regex but no default value" );
            }
        }
        return ret;
    }

    /**
      * The actual pattern.
      */
    protected String thePattern = null;

    /**
     * The default value.
     */
    protected String theDefaultValue;

    /**
     * Error messages, by locale.
     */
    protected L10MapImpl<String> theErrorMessages;
}
