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

package org.infogrid.model.primitives;

import org.infogrid.util.ResourceHelper;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This represents the multiplicity (aka cardinality) used for quantity
  * participation constraints on MetaRelationships.
  *
  * It also pre-defines a number of frequently-used MultiplicityValues that
  * can be used in order to save memory.
  */
public final class MultiplicityValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
      * Represents the "N" or "*" (open ended).
      * This is a very unusual value so we won't randomly do the wrong thing.
      */
    public static final int N = -1423;

    /**
     * This is a pre-defined 0-N multiplicity.
     */
    public static final MultiplicityValue ZERO_N = MultiplicityValue.create( 0, N );

    /**
     * This is a pre-defined 1-N multiplicity.
     */
    public static final MultiplicityValue ONE_N = MultiplicityValue.create( 1, N );

    /**
     * This is a pre-defined 0-1 multiplicity.
     */
    public static final MultiplicityValue ZERO_ONE = MultiplicityValue.create( 0, 1 );

    /**
     * This is a pre-defined 1-1 multiplicity.
     */
    public static final MultiplicityValue ONE_ONE = MultiplicityValue.create( 1, 1 );

    /**
     * Factory method.
     *
     * @param min the minimum number of participations (inclusive)
     * @param max the maximum number of participations (inclusive), may be N
     * @return the created MultiplicityValue
     * @throws IllegalArgumentException if incorrect/inconsistent values are being specified
     */
    public static MultiplicityValue create(
            int min,
            int max )
    {
        if(    ( min < 0 )
            || ( max <= 0 && max != N )
            || ( min > max && max != N ))
        {
            throw new InvalidMultiplicityException( min, max );
        }

        return new MultiplicityValue( min, max );
    }

    /**
      * Private constructor.
      *
      * @param min the minimum number of participations (inclusive)
      * @param max the maximum number of participations (inclusive), may be N
      */
    private MultiplicityValue(
            int min,
            int max )
    {
        this.minimum = min;
        this.maximum = max;
    }

    /**
      * Obtain the minimum number of participations.
      *
      * @return the minimum number of participations
      */
    public int getMinimum()
    {
        return minimum;
    }

    /**
      * Obtain the maximum number of participations.
      *
      * @return the maximum number of participations
      */
    public int getMaximum()
    {
        return maximum;
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
        if( otherValue instanceof MultiplicityValue ) {
            return (minimum == ((MultiplicityValue)otherValue).minimum)
                && (maximum == ((MultiplicityValue)otherValue).maximum);
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
        return minimum * maximum;
    }

    /**
     * Obtain the underlying value.
     *
     * @return the underlying value
     */
    public String value()
    {
        return toString();
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append( ( minimum == N ) ? N_SYMBOL : String.valueOf( minimum ) );
        buf.append( ".." );
        buf.append( ( maximum == N ) ? N_SYMBOL : String.valueOf( maximum ) );
        return buf.toString();
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
        String className = getClass().getName();
        StringBuilder buf = new StringBuilder( 32 );
        buf.append( className );
        buf.append( DataType.CREATE_STRING );
        
        if( minimum == N ) {
            buf.append( className );
            buf.append( ".N" );
        } else {
            buf.append( String.valueOf( minimum ));
        }
        buf.append( ", " );
        if( maximum == N ) {
            buf.append( className );
            buf.append( ".N" );
        } else {
            buf.append( String.valueOf( maximum ));
        }
        buf.append( DataType.CLOSE_PARENTHESIS_STRING );
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
        MultiplicityValue realOther = (MultiplicityValue) o;

        if( minimum == realOther.minimum && maximum == realOther.maximum ) {
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
        /* 3 */ minimum,
        /* 4 */ maximum,
        /* 5 */ ( minimum == N ) ? N_SYMBOL : String.valueOf( minimum ),
        /* 6 */ ( maximum == N ) ? N_SYMBOL : String.valueOf( maximum ));
    }

    /**
      * The value for the minimum.
      */
    protected int minimum;

    /**
      * The value for the maximum.
      */
    protected int maximum;
    
    /**
     * Symbol to use when displaying N.
     */
    public static final String N_SYMBOL = ResourceHelper.getInstance( MultiplicityValue.class ).getResourceStringOrDefault( 
            "NSymbol",
            "*" );
}
