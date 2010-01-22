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

import java.io.ObjectStreamException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationContext;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParseException;
import org.infogrid.util.text.StringifierException;

/**
  * This is a time period DataType for PropertyTypes. A time period is a "delta" value in time.
  */
public final class TimePeriodDataType
        extends
            DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class.
      */
    public static final TimePeriodDataType theDefault = new TimePeriodDataType();

    /**
     * Factory method. Always returns the same instance.
     *
     * @return the default instance of this class
     */
    public static TimePeriodDataType create()
    {
        return theDefault;
    }

    /**
      * Private constructor, there is no reason to instatiate this more than once.
      */
    private TimePeriodDataType()
    {
        super( null );
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return true if the two objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof TimePeriodDataType ) {
            return true;
        }
        return false;
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return TimePeriodDataType.class.hashCode();
    }

    /**
     * Determine whether this PropertyValue conforms to this DataType.
     *
     * @param value the candidate PropertyValue
     * @return true if the candidate PropertyValue conforms to this type
     */
    public boolean conforms(
            PropertyValue value )
    {
        if( value instanceof TimePeriodValue ) {
            return true;
        }
        return false;
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class getCorrespondingJavaClass()
    {
        return TimePeriodValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public TimePeriodValue getDefaultValue()
    {
        return theDefaultValue;
    }

    /**
     * The default value.
     */
    private static final TimePeriodValue theDefaultValue = TimePeriodValue.create( (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, (float) 0 );

    /**
     * Correctly deserialize a static instance.
     *
     * @return the static instance if appropriate
     * @throws ObjectStreamException thrown if reading from the stream failed
     */
    public Object readResolve()
        throws
            ObjectStreamException
    {
        if( this.equals( theDefault )) {
            return theDefault;
        } else {
            return this;
        }
    }

    /**
     * Obtain a value expression in the Java language that invokes the constructor
     * of factory method of the underlying concrete class, thereby creating or
     * reusing an instance of the underlying concrete class that is identical
     * to the instance on which this method was invoked.
     *
     * This is used primarily for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @return the Java language expression
     */
    public String getJavaConstructorString(
            String classLoaderVar )
    {
        final String className = getClass().getName();

        return className + DEFAULT_STRING;
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @param pars collects parameters that may influence the String representation
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationContext    context,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return rep.formatEntry(
                TimePeriodValue.class,
                DEFAULT_ENTRY,
                pars,
                PropertyValue.toStringRepresentation( theDefaultValue, rep, context, pars ), // presumably shorter, but we don't know
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public TimePeriodValue fromStringRepresentation(
            StringRepresentation representation,
            String               s,
            String               mimeType )
        throws
            PropertyValueParsingException
    {
        try {
            Object [] found = representation.parseEntry( TimePeriodValue.class, TimePeriodValue.DEFAULT_ENTRY, s, this );

            TimePeriodValue ret;

            switch( found.length ) {
                case 10:
                    ret = TimePeriodValue.create(
                            ((Number) found[4]).shortValue(),   // year
                            ((Number) found[5]).shortValue(),   // month
                            ((Number) found[6]).shortValue(),   // day
                            ((Number) found[7]).shortValue(),   // hour
                            ((Number) found[8]).shortValue(),   // minute
                            ((Number) found[9]).floatValue());  // second
                    break;

                case 11:
                    ret = TimePeriodValue.create(
                            ((Number) found[4]).shortValue(),   // year
                            ((Number) found[5]).shortValue(),   // month
                            ((Number) found[6]).shortValue(),   // day
                            ((Number) found[7]).shortValue(),   // hour
                            ((Number) found[8]).shortValue(),   // minute
                            ((Number) found[10]).floatValue());  // second
                    break;

                case 12:
                    ret = TimePeriodValue.create(
                            ((Number) found[4]).shortValue(),   // year
                            ((Number) found[5]).shortValue(),   // month
                            ((Number) found[6]).shortValue(),   // day
                            ((Number) found[7]).shortValue(),   // hour
                            ((Number) found[8]).shortValue(),   // minute
                            ((Number) found[10]).floatValue() + .001f * (Integer) found[11] );  // second
                    break;

                case 13:
                    ret = TimePeriodValue.create(
                            ((Number) found[4]).shortValue(),   // year
                            ((Number) found[5]).shortValue(),   // month
                            ((Number) found[6]).shortValue(),   // day
                            ((Number) found[7]).shortValue(),   // hour
                            ((Number) found[8]).shortValue(),   // minute
                            ((Number) found[10]).floatValue() + .001f * Float.parseFloat( (String) found[12] ) );  // second
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringRepresentationParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex.getFormatString(), ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }
}