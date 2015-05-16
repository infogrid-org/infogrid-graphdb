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

import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is an integer value for PropertyValues. It can carry a unit.
  *
  * FIXME: units not tested.
  */
public final class IntegerValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param value the integer value
     * @return the created IntegerValue
     */
    public static IntegerValue create(
            long value )
    {
        return new IntegerValue( value, null );
    }

    /**
     * Factory method.
     *
     * @param value the integer value
     * @param u the unit for the integer value
     * @return the created IntegerValue
     */
    public static IntegerValue create(
            long  value,
            Unit  u )
    {
        return new IntegerValue( value, u );
    }

    /**
     * Factory method.
     *
     * @param value the integer value
     * @return the created IntegerValue
     * @throws IllegalArgumentException if null is given as argument
     */
    public static IntegerValue create(
            Number value )
        throws
            IllegalArgumentException
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new IntegerValue( value.longValue(), null );
    }

    /**
     * Factory method.
     *
     * @param value the integer value
     * @param u the unit for the integer value
     * @return the created IntegerValue
     * @throws IllegalArgumentException if null is given as argument for the value
     */
    public static IntegerValue create(
            Number value,
            Unit   u )
        throws
            IllegalArgumentException
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new IntegerValue( value.longValue(), u );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the integer value
     * @return the created IntegerValue
     */
    public static IntegerValue createOrNull(
            Number value )
    {
        if( value == null ) {
            return null;
        }
        return new IntegerValue( value.longValue(), null );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the integer value
     * @param u the unit for the integer value
     * @return the created IntegerValue
     */
    public static IntegerValue createOrNull(
            Number value,
            Unit   u )
    {
        if( value == null ) {
            return null;
        }
        return new IntegerValue( value.longValue(), u );
    }

    /**
      * Private constructor, use factory methods.
      *
      * @param value the integer value
      * @param u the unit for the integer value
      */
    private IntegerValue(
            long value,
            Unit u )
    {
        this.theValue = value;
        this.theUnit  = u;
    }

    /**
      * Convert value back to Long.
      *
      * @return the value as Long
      */
    public Long value()
    {
        return theValue;
    }

    /**
      * Convert value back to long.
      *
      * @return the value as long
      */
    public long longValue()
    {
        return theValue;
    }

    /**
     * Convenience method along the lines of Number.doubleValue.
     *
     * @return the value as double
     */
    public Double doubleValue()
    {
        return Double.valueOf( theValue );
    }

    /**
      * Obtain Unit, if any.
      *
      * @return the Unit, if any
      */
    public Unit getUnit()
    {
        return theUnit;
    }

    /**
      * Determine equality of two objects.
      *
      * @param otherValue the object to test against
      * @return true if the two objects are equal
      */
    @Override
    public boolean equals(
            Object otherValue )
    {
        if( ! ( otherValue instanceof IntegerValue )) {
            return false;
        }

        IntegerValue realOtherValue = (IntegerValue) otherValue;

        if( theUnit == null ) {
            if( realOtherValue.theUnit != null ) {
                return false;
            }
            return theValue == realOtherValue.theValue;

        } else {
            if( realOtherValue.theUnit == null ) {
                return false;
            }

            if( ! theUnit.getFamily().equals( realOtherValue.theUnit.getFamily() )) {
                return false;
            }

            return theValue * theUnit.getPrefix() == realOtherValue.theValue * realOtherValue.theUnit.getPrefix();
        }
    }

    /**
     * Calculate hash code.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        // auto-generated by NetBeans
        int hash = 7;
        hash = 41 * hash + (int) (theValue ^ ( theValue >>> 32 ));
        hash = 41 * hash + ( theUnit != null ? theUnit.hashCode() : 0 );
        return hash;
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is smaller than, or the same as otherValue
      */
    public boolean isSmallerOrEquals(
            IntegerValue otherValue )
    {
        if( theUnit == null ) {
            if( otherValue.theUnit != null ) {
                return false;
            }

            return theValue <= otherValue.theValue;
        } else {
            if( otherValue.theUnit == null ) {
                return false;
            }

            if( ! theUnit.getFamily().equals( otherValue.theUnit.getFamily() )) {
                return false;
            }

            return theValue * theUnit.getPrefix() <= otherValue.theValue * otherValue.theUnit.getPrefix();
        }
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is smaller than otherValue
      */
    public boolean isSmaller(
            IntegerValue otherValue )
    {
        if( theUnit == null ) {
            if( otherValue.theUnit != null ) {
                return false;
            }
            return theValue < otherValue.theValue;

        } else {
            if( otherValue.theUnit == null ) {
                return false;
            }

            if( ! theUnit.getFamily().equals( otherValue.theUnit.getFamily() )) {
                return false;
            }

            return theValue * theUnit.getPrefix() < otherValue.theValue * otherValue.theUnit.getPrefix();
        }
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is larger, or the same, as otherValue
      */
    public boolean isLargerOrEquals(
            IntegerValue otherValue )
    {
        return otherValue.isSmallerOrEquals( this );
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is larger than otherValue
      */
    public boolean isLarger(
            IntegerValue otherValue )
    {
        return otherValue.isSmaller( this );
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        if( theUnit != null ) {
            return String.valueOf( theValue ) + " " + theUnit.toString();
        } else {
            return String.valueOf( theValue );
        }
    }

    /**
      * This attempts to parse a string and turn it into a integer value similarly
      * to Integer.parseInt().
      *
      * FIXME: need to deal with unit
      *
      * @param theString the string that shall be parsed
      * @return the created IntegerValue
      * @throws NumberFormatException thrown if theString does not follow the correct syntax
      */
    public static IntegerValue parseIntegerValue(
            String theString )
        throws
            NumberFormatException
    {
        return IntegerValue.create( Long.parseLong( theString ));
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
        StringBuilder buf = new StringBuilder( 64 );
        buf.append( getClass().getName() );
        buf.append( ".create( " );
        buf.append( theValue );
        buf.append( "L" ); // be safe
        if( theUnit != null ) {
            buf.append( ", " );
            buf.append( theUnit.getJavaConstructorString() );
        }
        buf.append( " )" );
        return buf.toString();
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
        IntegerValue realOther = (IntegerValue) o;

        if( theValue < realOther.theValue ) {
            return -1;
        } else if( theValue == realOther.theValue ) {
            return 0;
        } else {
            return +1;
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

        return rep.formatEntry(
                getClass(),
                StringRepresentation.DEFAULT_ENTRY,
                pars,
        /* 0 */ this,
        /* 1 */ editVar,
        /* 2 */ editIndex,
        /* 3 */ theValue,
        /* 4 */ theUnit );
    }

    /**
      * The actual value.
      */
    protected long theValue;

    /**
      * The Unit, if any.
      */
    protected Unit theUnit;
}
