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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.io.ObjectStreamException;
import java.text.ParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParseException;
import org.infogrid.util.text.StringifierException;

/**
  * This represents the DataType responsible for multiplicities.
  */
public class MultiplicityDataType
        extends DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class.
      */
    public static final MultiplicityDataType theDefault = new MultiplicityDataType();

    /**
     * Factory method. Always returns the same instance.
     *
     * @return the default instance of this class
     */
    public static MultiplicityDataType create()
    {
        return theDefault;
    }

    /**
      * Private constructor, there is no reason to instatiate this more than once.
      */
    private MultiplicityDataType()
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
        if( other instanceof MultiplicityDataType ) {
            return true;
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
        return MultiplicityDataType.class.hashCode();
    }

    /**
     * Determine whether this PropertyValue conforms to the constraints of this instance of DataType.
     *
     * @param value the candidate PropertyValue
     * @return 0 if the candidate PropertyValue conforms to this type. Non-zero values depend
     *         on the DataType; generally constructed by analogy with the return value of strcmp.
     * @throws ClassCastException if this PropertyValue has the wrong type (e.g.
     *         the PropertyValue is a StringValue, and the DataType an IntegerDataType)
     */
    public int conforms(
            PropertyValue value )
        throws
            ClassCastException
    {
        MultiplicityValue realValue = (MultiplicityValue) value; // may throw

        return 0;
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class<MultiplicityValue> getCorrespondingJavaClass()
    {
        return MultiplicityValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public MultiplicityValue getDefaultValue()
    {
        return MultiplicityValue.ZERO_N;
    }

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
        return rep.formatEntry(
                MultiplicityValue.class,
                DEFAULT_ENTRY,
                pars,
                this,
                PropertyValue.toStringRepresentationOrNull( MultiplicityValue.ZERO_N, rep, pars ), // presumably shorter, but we don't know
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public MultiplicityValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException
    {
        try {
            Object [] found = representation.parseEntry( MultiplicityValue.class, StringRepresentation.DEFAULT_ENTRY, pars, s, this );

            int min;
            int max;
            MultiplicityValue ret;
            switch( found.length ) {
                case 1:
                case 2:
                case 3:
                    ret = (MultiplicityValue) found[0];
                    break;

                case 5:
                    min = ( (Number)found[3] ).intValue();
                    max = ( (Number)found[4] ).intValue();
                    ret = MultiplicityValue.create( min, max );
                    break;

                case 7:
                    if( MultiplicityValue.N_SYMBOL.equals( found[5] )) {
                        min = MultiplicityValue.N;
                    } else {
                        min = Integer.parseInt( (String)found[5] );
                    }
                    if( MultiplicityValue.N_SYMBOL.equals( found[6] )) {
                        max = MultiplicityValue.N;
                    } else {
                        max = Integer.parseInt( (String)found[6] );
                    }
                    ret = MultiplicityValue.create( min, max );
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringRepresentationParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex.getFormatString(), ex );

        } catch( ParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, null, ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }
}
