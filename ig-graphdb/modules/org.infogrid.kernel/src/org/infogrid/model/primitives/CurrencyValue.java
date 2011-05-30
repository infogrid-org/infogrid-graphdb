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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a money value for PropertyValues.
  */
public final class CurrencyValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param wholes the wholes, e.g. dollars
     * @param fractions the fractions, e.g. cents
     * @param u the currency Unit for the value
     * @return the created FloatValue
     */
    public static CurrencyValue create(
            long                  wholes,
            int                   fractions,
            CurrencyDataType.Unit u )
    {
        return new CurrencyValue( wholes, fractions, u );
    }

    /**
     * Factory method.
     *
     * @param wholes the wholes, e.g. dollars
     * @param fractions the fractions, e.g. cents
     * @param u the currency Unit for the value
     * @return the created FloatValue
     */
    public static CurrencyValue create(
            long   wholes,
            int    fractions,
            String u )
    {
        return new CurrencyValue( wholes, fractions, CurrencyDataType.findUnitForCode( u ));
    }

    /**
     * Factory method.
     *
     * @param s amount and unit expressed as a String
     * @return the created CurrencyValue
     */
    public static CurrencyValue parseCurrencyValue(
            String s )
    {
        // We have two possible representations;

        String wholes;
        String fraction;
        String symbol;
        String code;

        Matcher m = AS_STRING_ISO.matcher( s );
        if( m.matches() ) {
            wholes   = m.group( 2 );
            fraction = m.group( 4 );

            if( fraction == null || fraction.length() == 0 ) {
                fraction = m.group( 5 );
            }
            code   = m.group( 6 );
            symbol = null;

        } else {
            m = AS_STRING_SYMBOL.matcher( s );
            if( m.matches() ) {
                symbol   = m.group( 1 );
                wholes   = m.group( 3 );
                fraction = m.group( 5 );

                if( fraction == null || fraction.length() == 0 ) {
                    fraction = m.group( 6 );
                }
                code = null;

            } else {
                throw new IllegalArgumentException( "Cannot parse CurrencyValue: " + s );
            }
        }

        CurrencyDataType.Unit u;
        
        if( code != null ) {
            u = CurrencyDataType.findUnitForCode( code );
            if( u == null ) {
                throw new IllegalArgumentException( "Cannot find a currency unit with ISO code " + code );
            }

        } else {
            u = CurrencyDataType.findUnitForSymbol( symbol );
            if( u == null ) {
                throw new IllegalArgumentException( "Cannot find a currency unit with symbol " + symbol );
            }
        }


        if( fraction != null ) {
            if( fraction.length() > u.getFractionPlaces() ) {
                throw new IllegalArgumentException( "Too many decimal places for " + u + ": " + fraction.length() );
            }
            for( int i=fraction.length() ; i<u.getFractionPlaces() ; ++i ) {
                fraction += "0";
            }
        }

        return new CurrencyValue(
                ( wholes   != null && wholes.length()   > 0 ) ? Long.parseLong(   wholes )   : 0L,
                ( fraction != null && fraction.length() > 0 ) ? Integer.parseInt( fraction ) : 0,
                u );
    }

    /**
      * Private constructor, use factory methods.
      *
      * @param wholes the whole units, e.g. dollars
      * @param fractions the fractional units, e.g. cents
      * @param u the currency Unit for the value
      */
    private CurrencyValue(
            long                  wholes,
            int                   fractions,
            CurrencyDataType.Unit u )
    {
        this.theWholes    = wholes;
        this.theFractions = fractions;
        this.theUnit      = u;
    }

    /**
      * Convert to String.
      *
      * @return the value as String
      */
    public String value()
    {
        return theUnit.format( theWholes, theFractions );
    }

    /**
     * Obtain the wholes, e.g. dollars.
     *
     * @return the wholes
     */
    public long getWholes()
    {
        return theWholes;
    }

    /**
     * Obtain the fractions, e.g. cents.
     *
     * @return the cents
     */
    public int getFractions()
    {
        return theFractions;
    }

    /**
      * Determine Unit, if any.
      *
      * @return the Unit, if any
      */
    public CurrencyDataType.Unit getUnit()
    {
        return theUnit;
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
        if( ! ( otherValue instanceof CurrencyValue )) {
            return false;
        }

        CurrencyValue realOtherValue = (CurrencyValue) otherValue;

        if( !theUnit.equals( realOtherValue.theUnit )) {
            return false;
        }
        return theWholes == realOtherValue.theWholes && theFractions == realOtherValue.theFractions;
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = (int) theWholes;
        ret ^= theFractions;
        ret ^= theUnit.hashCode();
        return ret;
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is smaller than, or the same as otherValue
      */
    public boolean isSmallerOrEquals(
            CurrencyValue otherValue )
    {
        if( !theUnit.equals( otherValue.theUnit )) {
            return false;
        }
        if( theWholes < otherValue.theWholes ) {
            return true;
        }
        if( theWholes > otherValue.theWholes ) {
            return false;
        }
        return theFractions <= otherValue.theFractions;
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is smaller than otherValue
      */
    public boolean isSmaller(
            CurrencyValue otherValue )
    {
        if( !theUnit.equals( otherValue.theUnit )) {
            return false;
        }
        if( theWholes < otherValue.theWholes ) {
            return true;
        }
        if( theWholes > otherValue.theWholes ) {
            return false;
        }
        return theFractions < otherValue.theFractions;
    }

    /**
      * Determine relationship between two values.
      *
      * @param otherValue the value to test against
      * @return returns true if this object is larger, or the same, as otherValue
      */
    public boolean isLargerOrEquals(
            CurrencyValue otherValue )
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
            CurrencyValue otherValue )
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
        return theUnit.format( theWholes, theFractions );
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
        StringBuilder buf = new StringBuilder( 128 );
        buf.append( getClass().getName() );
        buf.append( DataType.CREATE_STRING );
        buf.append( theWholes );
        buf.append( DataType.COMMA_STRING );
        buf.append( theFractions );
        buf.append( DataType.COMMA_STRING );
        buf.append( DataType.QUOTE_STRING );
        buf.append( theUnit.getIsoCode() );
        buf.append( DataType.QUOTE_STRING );

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
        CurrencyValue realOther = (CurrencyValue) o;

        if( !theUnit.equals( realOther.theUnit )) {
            return +2; // not comparable convention: +2
        }

        if( theWholes < realOther.theWholes ) {
            return -1;
        } else if( theWholes > realOther.theWholes ) {
            return +1;
        } else if( theFractions < realOther.theFractions ) {
            return -1;
        } else if( theFractions == realOther.theFractions ) {
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
        /* 3 */ theWholes,
        /* 4 */ theFractions,
        /* 5 */ theUnit );
    }

    /**
      * The actual value before the decimal point.
      */
    protected long theWholes;

    /**
     * The actual value after the decimal point.
     */
    protected int theFractions;

    /**
      * The Currency Unit, if any.
      */
    protected CurrencyDataType.Unit theUnit;

    /**
     * Pattern that expresses our String representation with a trailing ISO code.
     */
    public static final Pattern AS_STRING_ISO = Pattern.compile( "^\\s*((\\d+)(\\.(\\d*))?|\\.(\\d+))\\s*([A-Za-z]{3})\\s*$" );

    /**
     * Pattern that expresses our String representation with a leading currency symbol.
     */
    public static final Pattern AS_STRING_SYMBOL = Pattern.compile( "^\\s*([^\\s\\d]+)\\s*((\\d+)(\\.(\\d*))?|\\.(\\d+))\\s*$" );
}
