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

import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a boolean value for PropertyValues.
  */
public final class BooleanValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * This is a convenience function, given that this class only has a singleton instance.
     *
     * @param value the corresponding boolean for which we want to determine BooleanValue
     * @return the corresponding BooleanValue
     */
    public static BooleanValue create(
            boolean value )
    {
        if( value ) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * This is a convenience function, given that this class only has a singleton instance.
     *
     * @param value the corresponding boolean for which we want to determine BooleanValue
     * @return the corresponding BooleanValue
     */
    public static BooleanValue create(
            Boolean value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        if( value.booleanValue() ) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * This is a convenience function, given that this class only has a singleton instance.
     *
     * @param value the corresponding boolean for which we want to determine BooleanValue
     * @return the corresponding BooleanValue, or null
     */
    public static BooleanValue createOrNull(
            Boolean value )
    {
        if( value == null ) {
            return null;
        }

        if( value.booleanValue() ) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    /**
     * Private constructor as all instances of this class are pre-defined here.
     *
     * @param value the underlying boolean value
     */
    private BooleanValue(
            boolean value )
    {
        this.theValue = value;
    }

    /**
      * Convert to boolean.
      *
      * @return this value as boolean
      */
    public Boolean value()
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
        if( otherValue instanceof BooleanValue ) {
            return this == (BooleanValue) otherValue;
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
        return theValue ? 2 : 1; // make up something
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        if( theValue ) {
            return "-TRUE-";
        } else {
            return "-FALSE-";
        }
    }

    /**
      * Logical inversion.
      *
      * @return BooleanValue.TRUE if the value is BooleanValue.FALSE, and vice versa
      */
    public BooleanValue not()
    {
        if( theValue ) {
            return FALSE;
        } else {
            return TRUE;
        }
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
        String className = BooleanValue.class.getName();

        if( theValue ) {
            return className + ".TRUE";
        } else {
            return className + ".FALSE";
        }
    }

    /**
     * Special treatment for serialization in order to keep the singleton properties of TRUE and FALSE.
     *
     * @return the read object
     */
    public Object readResolve()
    {
        if( theValue ) {
            return TRUE;
        } else {
            return FALSE;
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
        String  editVar   = (String)  pars.get( StringRepresentationParameters.EDIT_VARIABLE );
        Integer editIndex = (Integer) pars.get( StringRepresentationParameters.EDIT_INDEX );

        if( editIndex == null ) {
            editIndex = 1;
        }

        String s;
        if( theValue ) {
            s = "True";
        } else {
            s = "False";
        }

        return rep.formatEntry(
                getClass(),
                s,
                pars,
        /* 0 */ this,
        /* 1 */ editVar,
        /* 2 */ editIndex );
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
        BooleanValue realValue = (BooleanValue) o;

        if( theValue ) {
            if( realValue.theValue ) {
                return 0;
            } else {
                return +1;
            }
        } else {
            if( realValue.theValue ) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
      * The actual value.
      */
    protected boolean theValue;

    /**
      * Pre-defined TRUE value.
      */
    public static final BooleanValue TRUE = new BooleanValue( true );

    /**
      * Pre-defined FALSE value.
      */
    public static final BooleanValue FALSE = new BooleanValue( false );
}
