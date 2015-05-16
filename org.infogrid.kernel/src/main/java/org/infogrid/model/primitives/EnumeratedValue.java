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

package org.infogrid.model.primitives;

import java.util.Locale;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This represents an enumerated value for PropertyValues.
  *
  * This does not have a public constructor as only EnumeratedDataType gets to instantiate this.
  */
public final class EnumeratedValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method, to be used only by EnumeratedDataType. Use those factory methods and select
     * methods instead, do not instantiate EnumeratedValue using this factory method.
     *
     * @param type the EnumeratedDataType to which this EnumeratedValue belongs
     * @param value the programmatic value of this EnumeratedValue
     * @param userNameMap user-visible, internationalized names for this value
     * @param userDescriptionMap user-visible, internalizationalized descriptions for this value
     * @return the created EnumeratedValue
     */
    public static EnumeratedValue create(
            EnumeratedDataType type,
            String             value,
            L10PropertyValueMap userNameMap,
            L10PropertyValueMap userDescriptionMap )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new EnumeratedValue( type, value, userNameMap, userDescriptionMap );
    }

    /**
      * Private constructor.
      *
     * @param type the EnumeratedDataType to which this EnumeratedValue belongs
      * @param value the programmatic value of this EnumeratedValue
      * @param userNameMap user-visible, internationalized names for this value
      * @param userDescriptionMap user-visible, internalizationalized descriptions for this value
      */
    private EnumeratedValue(
            EnumeratedDataType  type,
            String              value,
            L10PropertyValueMap userNameMap,
            L10PropertyValueMap userDescriptionMap )
    {
        this.theDataType           = type;
        this.theValue              = value;
        this.theUserNameMap        = userNameMap;
        this.theUserDescriptionMap = userDescriptionMap;
    }

    /**
     * Obtain the EnumeratedDataType to which this EnumeratedValue belongs.
     *
     * @return the EnumeratedDataType
     */
    public EnumeratedDataType getDataType()
    {
        return theDataType;
    }

    /**
      * Obtain the programmatic string.
      *
      * @return the programmatic string
      */
    public String value()
    {
        return theValue;
    }

    /**
     * Obtain a user-visible, internationalized representation of this value.
     *
     * @return user-visible, internationalized representation of this value
     */
    public StringValue getUserVisibleName()
    {
        if( theUserName == null && theUserNameMap != null ) {
            theUserName = (StringValue) theUserNameMap.get( Locale.getDefault() );
        }
        if( theUserName == null ) {
            return StringValue.create( theValue );
        }
        return theUserName;
    }

    /**
     * Obtain the map of user-visible, internationalized representations of this value.
     *
     * @return map of user-visible, internationalized representations of this value
     */
    public L10PropertyValueMap getUserVisibleNameMap()
    {
        return theUserNameMap;
    }

    /**
     * Obtain a user-visible, internationalized description of this value.
     *
     * @return user-visible, internationalized description of this value
     */
    public BlobValue getUserVisibleDescription()
    {
        if( theUserDescription == null && theUserDescriptionMap != null ) {
            theUserDescription = (BlobValue) theUserDescriptionMap.get( Locale.getDefault() );
        }
        return theUserDescription;
    }

    /**
     * Obtain the map of user-visible, internationalized descriptions of this value.
     *
     * @return map of user-visible, internationalized descriptions of this value
     */
    public L10PropertyValueMap getUserVisibleDescriptionMap()
    {
        return theUserDescriptionMap;
    }

    /**
      * Determine equality of two objects.
      *
      * @param otherValue the object to test against
      * @return true if the objects are equal
      */
    @Override
    public boolean equals(
            Object otherValue )
    {
        if( otherValue instanceof EnumeratedValue ) {
            return this == otherValue;
        } else if( otherValue instanceof String ) {
            return theValue.equals( otherValue );
        } else {
            return false;
        }
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = 0;
        ret ^= theDataType != null ? theDataType.hashCode() : 0;
        ret ^= theValue.hashCode();

        return ret;
    }
    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        return "#" + theValue;
    }

    /**
     * Obtain a string which is the Java-language constructor expression reflecting this value.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @param typeVar  name of the variable containing the DatatType that goes with the to-be-created instance.
     * @return the Java-language constructor expression
     */
    public String getJavaConstructorString(
            String classLoaderVar,
            String typeVar )
    {
        return typeVar + ".select( \"" + theValue + "\" )";
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param   o the PropertyValue to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *      is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     */
    public int compareTo(
            PropertyValue o )
    {
        // typecast to ensure ClassCastException
        if( this == (EnumeratedValue) o ) {
            return 0;
        } else {
            return +2; // not comparable convention: +2
        }
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String  editVar   = (String) pars.get( StringRepresentationParameters.EDIT_VARIABLE );
        Integer editIndex = (Integer) pars.get( StringRepresentationParameters.EDIT_INDEX );

        if( editIndex == null ) {
            editIndex = 1;
        }

        return rep.formatEntry(
                getClass(),
                StringRepresentation.DEFAULT_ENTRY,
                pars,
        /* 0 */ this,
        /* 1 */ editVar,
        /* 2 */ editIndex,
        /* 3 */ theValue,
        /* 4 */ getUserVisibleName()        != null ? getUserVisibleName().value()        : null,
        /* 5 */ getUserVisibleDescription() != null ? getUserVisibleDescription().value() : null );
    }

    /**
     * The EnumeratedDataType to which this instance belongs.
     */
    protected EnumeratedDataType theDataType;
    
    /**
      * The real value.
      */
    protected String theValue;

    /**
     * The user-visible string in the current locale -- allocated as needed.
     */
    protected transient StringValue theUserName;

    /**
     * The user-visible description in the current locale -- allocated as needed.
     */
    protected transient BlobValue theUserDescription;

    /**
     * The user name localization map.
     */
    protected L10PropertyValueMap theUserNameMap;

    /**
     * The user description localization map.
     */
    protected L10PropertyValueMap theUserDescriptionMap;
}
