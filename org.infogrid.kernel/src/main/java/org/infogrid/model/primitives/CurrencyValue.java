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

import java.text.ParseException;
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
     * @param isPositive if true, create a positive amount
     * @param wholes the wholes, e.g. dollars
     * @param fractions the fractions, e.g. cents
     * @param u the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue create(
            boolean               isPositive,
            long                  wholes,
            int                   fractions,
            CurrencyDataType.Unit u )
    {
        long internal = fractions + u.getFractionMultiplier() * wholes;
        if( !isPositive ) {
            internal = -internal;
        }
        return new CurrencyValue( internal, u );
    }

    /**
     * Factory method.
     *
     * @param isPositive if true, create a positive amount
     * @param wholes the wholes, e.g. dollars
     * @param fractions the fractions, e.g. cents
     * @param u the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue create(
            boolean               isPositive,
            long                  wholes,
            int                   fractions,
            String                u )
    {
        return create( isPositive, wholes, fractions, CurrencyDataType.findUnitForCode( u ) );
    }

    /**
     * Factory method from a number.
     *
     * @param number the number
     * @param unit the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue create(
            double                number,
            CurrencyDataType.Unit unit )
    {
        long internal = (long) ( number * unit.getFractionMultiplier() + .5 );
        return new CurrencyValue( internal, unit );
    }
    
    /**
     * Factory method from a number.
     *
     * @param number the number
     * @param unit the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue create(
            double                number,
            String                unit )
    {
        CurrencyDataType.Unit u = CurrencyDataType.findUnitForCode( unit );
        
        long internal = (long) ( number * u.getFractionMultiplier() + .5 );
        return new CurrencyValue( internal, u );
    }
    
    /**
     * Factory method to create a zero amount.
     *
     * @param unit the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue createFree(
            CurrencyDataType.Unit unit )
    {
        return new CurrencyValue( 0, unit );
    }
    
    /**
     * Factory method to create a zero amount.
     *
     * @param unit the currency Unit for the value
     * @return the created CurrencyValue
     */
    public static CurrencyValue createFree(
            String                unit )
    {
        CurrencyDataType.Unit u = CurrencyDataType.findUnitForCode( unit );

        return new CurrencyValue( 0, u );
    }
    
    /**
     * Factory method.
     *
     * @param s amount and unit expressed as a String
     * @return the created CurrencyValue
     * @throws ParseException thrown if the string could not be parsed
     */
    public static CurrencyValue parseCurrencyValue(
            String s )
        throws
            ParseException
    {
        // We have two possible representations;

        String sign;
        String wholes;
        String fraction;
        String symbol;
        String code;

        Matcher m = AS_STRING_ISO.matcher( s );
        if( m.matches() ) {
            sign     = m.group( 1 );
            wholes   = m.group( 2 );
            fraction = m.group( 3 );
            if( fraction == null || fraction.length() == 0 ) {
                fraction = m.group( 4 );
            }
            
            code   = m.group( 5 );
            symbol = null;

        } else {
            m = AS_STRING_SYMBOL.matcher( s );
            if( m.matches() ) {
                sign     = m.group( 1 );
                symbol   = m.group( 2 );
                wholes   = m.group( 3 );
                fraction = m.group( 4 );

                if( fraction == null || fraction.length() == 0 ) {
                    fraction = m.group( 5 );
                }
                code = null;

            } else {
                throw new ParseException( "Cannot parse CurrencyValue: " + s, 0 );
            }
        }

        CurrencyDataType.Unit u;
        
        if( code != null ) {
            u = CurrencyDataType.findUnitForCode( code );
            if( u == null ) {
                throw new ParseException( "Cannot find a currency unit with ISO code " + code, 0 );
            }

        } else {
            u = CurrencyDataType.findUnitForSymbol( symbol );
            if( u == null ) {
                throw new ParseException( "Cannot find a currency unit with symbol " + symbol, 0 );
            }
        }


        if( fraction != null ) {
            if( fraction.length() > u.getFractionPlaces() ) {
                throw new ParseException( "Too many decimal places for " + u + ": " + fraction.length(), 0 );
            }
            for( int i=fraction.length() ; i<u.getFractionPlaces() ; ++i ) {
                fraction += "0";
            }
        }
        
        long realWholes   = 0L;
        int  realFraction = 0;
        
        if( wholes != null && wholes.length() > 0 ) {
            realWholes = Long.parseLong( wholes );
        }
        if( fraction != null && fraction.length() > 0 ) {
            realFraction = Integer.parseInt( fraction );
        }
        
        long internalValue = realFraction + u.getFractionMultiplier() * realWholes;
        if( "-".equals( sign )) {
            internalValue = -internalValue;
        }
        return new CurrencyValue( internalValue, u );
    }

    /**
      * Private constructor, use factory methods.
      *
      * @param internalValue the internal value, i.e. fractions
      * @param u the currency Unit for the value
      */
    private CurrencyValue(
            long                  internalValue,
            CurrencyDataType.Unit u )
    {
        this.theInternalValue = internalValue;
        this.theUnit          = u;
    }

    /**
      * Convert to String.
      *
      * @return the value as String
      */
    @Override
    public String value()
    {
        return theUnit.format( theInternalValue >= 0, getWholes(), getFractions() );
    }

    /**
     * Obtain the wholes, e.g. dollars.
     *
     * @return the wholes
     */
    public long getWholes()
    {
        long ret = Math.abs( theInternalValue / theUnit.getFractionMultiplier() );
        
        return ret;
    }

    /**
     * Obtain the fractions, e.g. cents.
     *
     * @return the cents
     */
    public int getFractions()
    {
        long ret = Math.abs( theInternalValue % theUnit.getFractionMultiplier());
        
        return (int) ret;
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
     * Obtain this CurrencyValue as a numeric fraction, without unit.
     * For example $1.50 would return 1.5.
     * 
     * @return the numeric fraction
     */
    public double getAsDouble()
    {
        double ret = (double)theInternalValue / theUnit.getFractionMultiplier();

        return ret;
    }

    /**
     * Determine whether this value is zero.
     *
     * @return true if the value is zero
     */
    public boolean isFree()
    {
        return theInternalValue == 0;
    }

    /**
     * Determine whether this value is positive.
     * 
     * @return true if the value is positive, and not negative or zero
     */
    public boolean isPositive()
    {
        return theInternalValue > 0;
    }
    
    /**
     * Determine whether this value is negative.
     * 
     * @return true if the value is negative, and not positive or zero
     */
    public boolean isNegative()
    {
        return theInternalValue < 0;
    }
    
    /**
     * Add two CurrencyValues.
     * 
     * @param other the other CurrencyValue
     * @return the sum of the two CurrencyValues
     * @throws IllegalArgumentException thrown if the two CurrencyValues do not have the same unit.
     */
    public CurrencyValue plus(
            CurrencyValue other )
        throws
            IllegalArgumentException
    {
        if( theUnit != other.theUnit ) {
            throw new IllegalArgumentException( "Incompatible units: " + theUnit + " and " + other.theUnit );
        }
        return new CurrencyValue( theInternalValue + other.theInternalValue, theUnit );
    }

    /**
     * Subtract a CurrencyValue from this CurrencyValue.
     *
     * @param other the other CurrencyValue
     * @return the difference of the two CurrencyValues
     * @throws IllegalArgumentException thrown if the two CurrencyValues do not have the same unit.
     */
    public CurrencyValue minus(
            CurrencyValue other )
        throws
            IllegalArgumentException
    {
        if( theUnit != other.theUnit ) {
            throw new IllegalArgumentException( "Incompatible units: " + theUnit + " and " + other.theUnit );
        }
        return new CurrencyValue( theInternalValue - other.theInternalValue, theUnit );
    }

    /**
     * Turn a CurrencyValue into a CurrencyValue with the same value but opposite sign.
     *
     * @return the inverted CurrencyValue
     */
    public CurrencyValue minus()
    {
        return new CurrencyValue( -theInternalValue, theUnit );
    }

    /**
     * Multiply a CurrencyValue by some factor, and round.
     * 
     * @param the factor
     * @return the multiplied CurrencyValue
     */
    public CurrencyValue times(
            double factor )
    {
        return new CurrencyValue( (long) ( theInternalValue * factor + .5d ), theUnit );
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
        return theInternalValue == realOtherValue.theInternalValue;
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = (int) theInternalValue;
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
        return theInternalValue <= otherValue.theInternalValue;
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
        return theInternalValue < otherValue.theInternalValue;
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
        if( !theUnit.equals( otherValue.theUnit )) {
            return false;
        }
        return theInternalValue >= otherValue.theInternalValue;
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
        if( !theUnit.equals( otherValue.theUnit )) {
            return false;
        }
        return theInternalValue > otherValue.theInternalValue;
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
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
    @Override
    public String getJavaConstructorString(
            String classLoaderVar,
            String typeVar )
    {
        StringBuilder buf = new StringBuilder( 128 );
        buf.append( getClass().getName() );
        buf.append( DataType.CREATE_STRING );
        buf.append( theInternalValue >= 0 );
        buf.append( DataType.COMMA_STRING );
        buf.append( getWholes() );
        buf.append( DataType.COMMA_STRING );
        buf.append( getFractions() );
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
     *      is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this Object.
     */
    @Override
    public int compareTo(
            PropertyValue o )
    {
        CurrencyValue realOther = (CurrencyValue) o;

        if( !theUnit.equals( realOther.theUnit )) {
            return +2; // not comparable convention: +2
        }
        long delta = theInternalValue - realOther.theInternalValue;
        
        if( delta > 0 ) {
            return 1;
        } else if( delta < 0 ) {
            return -1;
        } else {
            return 0;
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
    @Override
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
        /* 3 */ theInternalValue >= 0,
        /* 4 */ theInternalValue >= 0 ? "" : "-",
        /* 5 */ getWholes(),
        /* 6 */ getFractions(),
        /* 7 */ theUnit );
    }

    /**
     * The internal value, which is expressed entirely as fractions.
     */
    private long theInternalValue;

    /**
      * The Currency Unit, if any.
      */
    private CurrencyDataType.Unit theUnit;

    /**
     * Pattern that expresses our String representation with a trailing ISO code.
     */
    public static final Pattern AS_STRING_ISO = Pattern.compile( "^\\s*?(-)?(?:(\\d+)(?:\\.(\\d*))?|\\.(\\d+))\\s*([A-Za-z]{3})\\s*$" );

    /**
     * Pattern that expresses our String representation with a leading currency symbol.
     */
    public static final Pattern AS_STRING_SYMBOL = Pattern.compile( "^\\s*(-)?\\s*([^-\\.\\s\\d]+)\\s*(?:(\\d+)(?:\\.(\\d*))?|\\.(\\d+))\\s*$" );
}
