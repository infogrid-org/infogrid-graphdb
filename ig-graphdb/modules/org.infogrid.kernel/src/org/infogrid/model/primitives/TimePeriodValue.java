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

import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a time period value for PropertyValues.
  */
public final class TimePeriodValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param year the number of years
     * @param month the number of months
     * @param day the number of days
     * @param hour the number of hours
     * @param minute the number of minutes
     * @param second the snumber of seconds, plus fractions
     * @return the created TimePeriodValue
     */
    public static TimePeriodValue create(
            short year,
            short month,
            short day,
            short hour,
            short minute,
            float second )
    {
        TimePeriodValue ret = new TimePeriodValue( year, month, day, hour, minute, second );
        return ret;
    }

    /**
     * Factory method.
     *
     * @param millis the number of milliseconds
     * @return the created TimePeriodValue
     */
    public static TimePeriodValue create(
            long millis )
    {
        long minutes = millis / 60000;
        long hours   = minutes / 60;
        long days    = hours / 24;
        long years   = days / 365;
        long months  = ( days - years * 365) / 12;
        
        days = days - years * 365 - 30 * months;
        
        float seconds = (millis - minutes * 60000) / 1000.f;

        minutes = minutes % 60;
        hours   = hours % 60;
        
        TimePeriodValue ret = new TimePeriodValue( (short) years, (short) months, (short) days, (short) hours, (short) minutes, seconds );
        return ret;
    }

    /**
      * Private constructor, use factory method.
      *
      * @param year the number of years
      * @param month the number of months
      * @param day the number of days
      * @param hour the number of hours
      * @param minute the number of minutes
      * @param second the snumber of seconds, plus fractions
      */
    private TimePeriodValue(
            short year,
            short month,
            short day,
            short hour,
            short minute,
            float second )
    {
        if( month < 0 ) {
            throw new InvalidTimePeriodValueException.Month( year, month, day, hour, minute, second );
        }
        if( day < 0 ) {
            throw new InvalidTimePeriodValueException.Day( year, month, day, hour, minute, second );
        }
        if( hour < 0 ) {
            throw new InvalidTimePeriodValueException.Hour( year, month, day, hour, minute, second );
        }
        if( minute < 0 ) {
            throw new InvalidTimePeriodValueException.Minute( year, month, day, hour, minute, second );
        }
        if( second < 0. ) {
            throw new InvalidTimePeriodValueException.Second( year, month, day, hour, minute, second );
        }

        this.theYear   = year;
        this.theMonth  = month;
        this.theDay    = day;
        this.theHour   = hour;
        this.theMinute = minute;
        this.theSecond = second;
    }

    /**
      * Determine year value.
      *
      * @return the number of years
      */
    public short getYear()
    {
        return theYear;
    }

    /**
      * Determine month value.
      *
      * @return the number of months
      */
    public short getMonth()
    {
        return theMonth;
    }

    /**
      * Determine day value.
      *
      * @return the number of days
      */
    public short getDay()
    {
        return theDay;
    }

    /**
      * Determine hour value.
      *
      * @return the number of hours
      */
    public short getHour()
    {
        return theHour;
    }

    /**
      * Determine minute value.
      *
      * @return the number of minutes
      */
    public short getMinute()
    {
        return theMinute;
    }

    /**
      * Determine second value.
      *
      * @return the number of seconds plus fractions
      */
    public float getSecond()
    {
        return theSecond;
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
     * Obtain the underlying value as number of milliseconds.
     *
     * @return the milliseconds
     */
    public long valueAsMillis()
    {
        long ret = theYear * 365 + theMonth * 12 + theDay;
        ret = ret * 24 + theHour;
        ret = ret * 60 + theMinute;
        ret = ret * 60000 + (long)( theSecond * 1000 );
        return ret;
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
        if( otherValue instanceof TimePeriodValue ) {
            return (theYear   == ((TimePeriodValue)otherValue).theYear)
                && (theMonth  == ((TimePeriodValue)otherValue).theMonth)
                && (theDay    == ((TimePeriodValue)otherValue).theDay)
                && (theHour   == ((TimePeriodValue)otherValue).theHour)
                && (theMinute == ((TimePeriodValue)otherValue).theMinute)
                && (theSecond == ((TimePeriodValue)otherValue).theSecond);
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
        int ret = 0;
        ret = (ret << 1) ^ theYear;
        ret = (ret << 1) ^ theMonth;
        ret = (ret << 1) ^ theDay;
        ret = (ret << 1) ^ theHour;
        ret = (ret << 1) ^ theMinute;
        ret = (ret << 1) ^ (int) theSecond;
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
        StringBuilder buf = new StringBuilder( 256 );
        buf.append( theYear );
        buf.append( "/" );
        buf.append( theMonth );
        buf.append( "/" );
        buf.append( theDay );
        buf.append( " " );
        buf.append( theHour );
        buf.append( ":" );
        buf.append( theMinute );
        buf.append( ":" );
        buf.append( theSecond );
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
        StringBuilder buf = new StringBuilder( 256 );
        buf.append( getClass().getName() );
        buf.append( DataType.CREATE_STRING );
        buf.append( "(short) " );
        buf.append( theYear );
        buf.append( ", (short) " );
        buf.append( theMonth );
        buf.append( ", (short) " );
        buf.append( theDay );
        buf.append( ", (short) " );
        buf.append( theHour );
        buf.append( ", (short) " );
        buf.append( theMinute );
        buf.append( ", (float) " );
        buf.append( theSecond );
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
        TimePeriodValue realOther = (TimePeriodValue) o;

        return compare( this, realOther );
    }

    /**
     * Compare two instances of this class according to the rules of java.util.Comparator.
     *
     * @param valueOne the first value
     * @param valueTwo the second value
     * @return 0 if both are the same, -1 if the first is smaller than the second, 1 if the second is smaller that the first
     */
    public static int compare(
            TimePeriodValue valueOne,
            TimePeriodValue valueTwo )
    {
        if( valueOne == null ) {
            if( valueTwo == null ) {
                return 0;
            } else {
                return +1;
            }
        } else {
            if( valueTwo == null ) {
                return -1;
            } else {
                int delta;
                delta = valueOne.theYear - valueTwo.theYear;
                if( delta != 0 ) {
                    return delta;
                }
                delta = valueOne.theMonth - valueTwo.theMonth;
                if( delta != 0 ) {
                    return delta;
                }
                delta = valueOne.theDay - valueTwo.theDay;
                if( delta != 0 ) {
                    return delta;
                }
                delta = valueOne.theHour - valueTwo.theHour;
                if( delta != 0 ) {
                    return delta;
                }
                delta = valueOne.theMinute - valueTwo.theMinute;
                if( delta != 0 ) {
                    return delta;
                }

                if( valueOne.theSecond < valueTwo.theSecond ) {
                    return -1;
                } else if( valueOne.theSecond == valueTwo.theSecond ) {
                    return 0;
                } else {
                    return +1;
                }
            }
        }
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

        int millis = ((int) ( theSecond * 1000 )) % 1000;

        return rep.formatEntry(
                getClass(),
                StringRepresentation.DEFAULT_ENTRY,
                pars,
       /*  0 */ this,
       /*  1 */ editVar,
       /*  2 */ editIndex,
       /*  3 */ theYear,
       /*  4 */ theMonth,
       /*  5 */ theDay,
       /*  6 */ theHour,
       /*  7 */ theMinute,
       /*  8 */ theSecond,
       /*  9 */ (int) theSecond,
       /* 10 */ millis );
    }
    
    /**
      * The real year value.
      */
    protected short theYear;

    /**
      * The real month value.
      */
    protected short theMonth;

    /**
      * The real day value.
      */
    protected short theDay;

    /**
      * The real hour value.
      */
    protected short theHour;

    /**
      * The real minute value.
      */
    protected short theMinute;

    /**
      * The real second value.
      */
    protected float theSecond;
}
