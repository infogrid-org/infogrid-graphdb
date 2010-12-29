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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.util.StringHelper;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a string value for PropertyValues. StringValues are arbitary-length.
  */
public final class StringValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param value the value
     * @return the created StringValue
     * @throws IllegalArgumentException if null is given as argument
     */
    public static StringValue create(
            String value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new StringValue( value );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the value
     * @return the created StringValue, or null
     * @throws IllegalArgumentException if null is given as argument
     */
    public static StringValue createOrNull(
            String value )
    {
        if( value == null ) {
            return null;
        }
        return new StringValue( value );
    }

    /**
     * Factory method.
     *
     * @param value the value
     * @return the created StringValue
     * @throws IllegalArgumentException if null is given as argument
     */
    public static StringValue create(
            StringBuilder value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new StringValue( value.toString() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the value
     * @return the created StringValue, or null
     * @throws IllegalArgumentException if null is given as argument
     */
    public static StringValue createOrNull(
            StringBuilder value )
    {
    	if( value == null ) {
            return null;
        }
    	return new StringValue( value.toString() );
    }

    /**
     * Helper method to create an array of StringValues from Strings.
     *
     * @param raw the array of Strings, or null
     * @return the corresponding array of StringValue, or null
     */
    public static StringValue [] createMultiple(
            String [] raw )
    {
        if( raw == null ) {
            return null;
        }
        StringValue [] ret = new StringValue[ raw.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = StringValue.create( raw[i] );
        }
        return ret;
    }

    /**
      * Private constructor, use factory methods
      *
      * @param value the value
      */
    private StringValue(
            String value )
    {
        this.theValue = value;
    }

    /**
      * Obtain the value.
      *
      * @return the value
      */
    public String value()
    {
        return theValue;
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
        if( otherValue instanceof StringValue ) {
            return theValue.equals( ((StringValue) otherValue).theValue );
        }
        if( otherValue instanceof String ) {
            return theValue.equals( (String) otherValue );
        }
        return false;
    }

    /**
     * Determine hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return theValue.hashCode();
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder( theValue.length() + 2 );
        buf.append( "\'" );
        buf.append( theValue );
        buf.append( "\'" );
        return buf.toString();
    }

    /**
     * Obtain as String, mostly for JavaBeans-aware software.
     *
     * @return as String
     */
    public String getAsString()
    {
        return value();
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
        StringBuilder buf = new StringBuilder( theValue.length() + 30 ); // fudge
        buf.append( getClass().getName() );
        buf.append( DataType.CREATE_STRING );
        buf.append( "\"" );
        buf.append( StringHelper.stringToJavaString( theValue ));
        buf.append( "\" )" );
        return buf.toString();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param   o the PropertyValue to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     */
    public int compareTo(
            PropertyValue o )
    {
        StringValue realOther = (StringValue) o;

        return theValue.compareTo( realOther.theValue );
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String  editVar   = null;
        Integer editIndex = null;

        if( pars != null ) {
            editVar   = (String) pars.get( StringRepresentationParameters.EDIT_VARIABLE );
            editIndex = (Integer) pars.get( StringRepresentationParameters.EDIT_INDEX );
        }
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
        /* 3 */ theValue );
    }

    /**
      * The real value.
      */
    protected String theValue;
}
