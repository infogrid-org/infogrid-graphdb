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

import java.io.ObjectStreamException;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;
/**
  * This is a boolean DataType.
  */
public final class BooleanDataType
        extends DataType
{
    private static final Log  log              = Log.getLogInstance( BooleanDataType.class ); // our own, private logger
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class.
      */
    public static final BooleanDataType theDefault = new BooleanDataType();

    /**
     * Factory method. Always returns the same instance.
     *
     * @return the default instance of this class
     */
    public static BooleanDataType create()
    {
        return theDefault;
    }

    /**
      * Private constructor, there is no reason to instatiate this more than once.
      */
    private BooleanDataType()
    {
        super( null );
    }

    /**
      * Test for equality.
      *
      * @param other the object to test against
      * @return true if objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof BooleanDataType ) {
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
        return super.hashCode();
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
        BooleanValue realValue = (BooleanValue) value; // may throw

        return 0;
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class<BooleanValue> getCorrespondingJavaClass()
    {
        return BooleanValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public BooleanValue getDefaultValue()
    {
        return BooleanValue.FALSE;
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
     * Obtain a PropertyValue that corresponds to this DataType, based on the String representation
     * of the PropertyValue.
     *
     * @param s the String representation
     * @return the PropertyValue
     */
    public BooleanValue fromString(
            String s )
    {
        if( "true".equalsIgnoreCase( s )) {
            return BooleanValue.TRUE;
        } else if( "yes".equalsIgnoreCase( s ) ) {
            return BooleanValue.TRUE;
        } else {
            return BooleanValue.FALSE;
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
        return rep.formatEntry(
                BooleanValue.class,
                DEFAULT_ENTRY,
                pars,
                this,
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
    public PropertyValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException
    {
        try {
            String compareTo = representation.formatEntry( BooleanValue.class, "True", StringRepresentationParameters.EMPTY );
            if( compareTo.equalsIgnoreCase( s )) {
                return BooleanValue.TRUE;
            }
        } catch( StringifierException ex ) {
            log.error( ex );
        }

        try {
            String compareTo = representation.formatEntry( BooleanValue.class, "False", StringRepresentationParameters.EMPTY );
            if( compareTo.equalsIgnoreCase( s )) {
                return BooleanValue.FALSE;
            }
        } catch( StringifierException ex ) {
            log.error( ex );
        }

        try {
            String compareTo = representation.formatEntry( BooleanValue.class, "True", StringRepresentationParameters.EMPTY );
            if( compareTo.equalsIgnoreCase( s )) {
                return BooleanValue.TRUE;
            }
        } catch( StringifierException ex ) {
            log.error( ex );
        }

        try {
            String compareTo = representation.formatEntry( BooleanValue.class, "False", StringRepresentationParameters.EMPTY );
            if( compareTo.equalsIgnoreCase( s )) {
                return BooleanValue.FALSE;
            }
        } catch( StringifierException ex ) {
            log.error( ex );
        }
        throw new PropertyValueParsingException( this, representation, s );
    }
}
