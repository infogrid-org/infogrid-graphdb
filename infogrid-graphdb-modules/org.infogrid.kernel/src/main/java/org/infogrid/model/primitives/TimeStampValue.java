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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.infogrid.model.primitives.text.ModelPrimitivesStringRepresentationParameters;
import org.infogrid.util.DateTimeUtil;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a time stamp value for PropertyValues.
  */
public final class TimeStampValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 2L; // helps with serialization

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
    public static TimeStampValue create(
            int   year,
            int   month,
            int   day,
            int   hour,
            int   minute,
            float second )
    {
        Calendar cal = Calendar.getInstance( DateTimeUtil.UTC );
        int      sec = (int) second;

        cal.set(
                year,
                month-1, // counts from 0
                day,
                hour,
                minute,
                sec );
        cal.set( Calendar.MILLISECOND, (int) ( ( second-sec ) * 1000 ));

        return create( cal );
    }

    /**
     * Factory method.
     *
     * @param value time in the format returned by System.currentTimeMillis()
     * @return the created TimePeriodValue
     */
    public static TimeStampValue create(
            long value )
    {
        return new TimeStampValue( value );
    }

    /**
     * Factory method.
     *
     * @param value value as a Calendar
     * @return the created TimeStampValue
     */
    public static TimeStampValue create(
            Calendar value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }

        return new TimeStampValue( value.getTimeInMillis() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value value as a Calendar
     * @return the created TimeStampValue, or null
     */
    public static TimeStampValue createOrNull(
                Calendar value )
    {
        if( value == null ) {
            return null;
        }

        return create( value );
    }

    /**
     * Factory method.
     *
     * @param value value as a Date
     * @return the created TimeStampValue
     */
    public static TimeStampValue create(
            Date value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }

        return new TimeStampValue( value.getTime() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value value as a Date
     * @return the created TimeStampValue, or null
     */
    public static TimeStampValue createOrNull(
            Date value )
    {
        if( value == null ) {
            return null;
        }

        return create( value );
    }

    /**
     * Factory method.
     *
     * @param rfc3339String value as an RFC 3339 String
     * @return the created TimeStampValue
     * @throws ParseException thrown if a syntax error occurred
     */
    public static TimeStampValue createFromRfc3339(
            String rfc3339String )
        throws
            ParseException
    {
        if( rfc3339String == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        Date d = DateTimeUtil.rfc3339ToDate( rfc3339String );
        return new TimeStampValue( d.getTime() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param rfc3339String value as an RFC 3339 String
     * @return the created TimeStampValue, or null
     * @throws ParseException thrown if a syntax error occurred
     */
    public static TimeStampValue createFromRfc3339OrNull(
            String rfc3339String )
        throws
            ParseException
    {
        if( rfc3339String == null ) {
            return null;
        }

        return createFromRfc3339( rfc3339String );
    }

    /**
     * Factory method.
     *
     * @param w3cString value as a W3C String
     * @return the created TimeStampValue
     * @throws ParseException thrown if a syntax error occurred
     */
    public static TimeStampValue createFromW3c(
            String w3cString )
        throws
            ParseException
    {
        if( w3cString == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        Date d = DateTimeUtil.w3cToDate( w3cString );
        return new TimeStampValue( d.getTime() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param w3cString value as a W3C String
     * @return the created TimeStampValue, or null
     * @throws ParseException thrown if a syntax error occurred
     */
    public static TimeStampValue createFromW3cOrNull(
            String w3cString )
        throws
            ParseException
    {
        if( w3cString == null ) {
            return null;
        }

        return createFromW3c( w3cString );
    }

    /**
      * Private constructor, use factory method.
      *
     * @param value time in the format returned by System.currentTimeMillis()
      */
    private TimeStampValue(
            long value )
    {
        theValue = value;
    }

    /**
     * Obtain this as a java.util.Date.
     *
     * @return this value as java.util.Date
     */
    public Date getAsDate()
    {
        return new Date( theValue );
    }

    /**
     * Obtain this as a number of milliseconds in System.currentTimeMillis() format.
     *
     * @return this value in System.currentTimeMillis() format
     */
    public long getAsMillis()
    {
        return theValue;
    }

    /**
     * Obtain this as a Calendar in UTC format.
     * 
     * @return Calendar
     */
    public Calendar getAsUtcCalendar()
    {
        return getAsCalendar( null );
    }

    /**
     * Obtain this as a Calendar in a specified TimeZone.
     *
     * @param tz the TimeZone
     * @return Calendar
     */
    public Calendar getAsCalendar(
            TimeZone tz )
    {
        if( tz == null ) {
            tz = DateTimeUtil.UTC;
        }
        Calendar cal = Calendar.getInstance( tz );
        cal.setTimeInMillis( theValue );

        return cal;
    }

    /**
     * Obtain this as an RFC 3339 String.
     *
     * @return the String
     */
    public String getAsRfc3339String()
    {
        return DateTimeUtil.dateToRfc3339( getAsDate() );
    }

    /**
     * Obtain the underlying value.
     *
     * @return the underlying value
     */
    public String value()
    {
        return getAsRfc3339String();
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
        if( otherValue instanceof TimeStampValue ) {
            TimeStampValue realOtherValue = (TimeStampValue) otherValue;
            
            return theValue == realOtherValue.theValue;
        }
        return false;
    }

    /**
     * Compare two TimeStampValues and return the earlier one of the two.
     *
     * @param one first TimeStampValue
     * @param two second TimeStampValue
     * @return the earlier TimeStampValue
     */
    public static TimeStampValue earlierOf(
            TimeStampValue one,
            TimeStampValue two )
    {
        int comparison = compare( one, two );
        if( comparison <= 0 ) {
            return one;
        } else {
            return two;
        }
    }

    /**
     * Compare two TimeStampValues and return the later one of the two.
     *
     * @param one first TimeStampValue
     * @param two second TimeStampValue
     * @return the later TimeStampValue
     */
    public static TimeStampValue laterOf(
            TimeStampValue one,
            TimeStampValue two )
    {
        int comparison = compare( one, two );
        if( comparison > 0 ) {
            return one;
        } else {
            return two;
        }
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return (int) theValue;
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        Calendar cal = getAsUtcCalendar();

        String ret = String.format( "%1$tY-%1$tm-%1$tdZ%1$tH:%1$tM:%1$tS.%1$tL", cal );

        return ret;
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
        buf.append( ".create( " );
        buf.append( theValue );
        buf.append( "L )" ); // make sure it's a long not an int
        return buf.toString();
    }

    /**
     * This creates an instance initialized with the current time.
     *
     * @return the current time
     */
    public static TimeStampValue now()
    {
        return nowWithOffset( 0 );
    }

    /**
     * This creates an instance initialized with the current time plus an offset
     *
     * @param offset the time offset, in milliseconds
     * @return the time with offset
     */
    public static TimeStampValue nowWithOffset(
            long offset )
    {
        if( offset < Integer.MIN_VALUE || offset > Integer.MAX_VALUE ) {
            throw new IllegalArgumentException( "offset out of range: " + offset );
            // it would be so great if the JDK had consistent APIs
        }

        return new TimeStampValue( System.currentTimeMillis() + offset );
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
        TimeStampValue realOther = (TimeStampValue) o;

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
            TimeStampValue valueOne,
            TimeStampValue valueTwo )
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
                long delta = valueOne.theValue - valueTwo.theValue;

                if( delta > Integer.MAX_VALUE || delta < Integer.MIN_VALUE ) {
                    delta /= 1000L; // stupid API
                }
                return (int) delta;
            }
        }
    }

    /**
     * Determine whether this TimeStampValue is in the future.
     *
     * @return true if it is in the future
     */
    public boolean isInFuture()
    {
        return compare( this, now() ) > 0;
    }

    /**
     * Determine whether this TimeStampValue is in the past.
     *
     * @return true if it is in the future
     */
    public boolean isInPast()
    {
        return compare( this, now() ) < 0;
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

        Object tz;
        if( pars != null ) {
            tz = pars.get( ModelPrimitivesStringRepresentationParameters.TIME_ZONE );
        } else {
            tz = null;
        }
        if( tz == null ) {
            tz = DateTimeUtil.UTC;
        }
        
        Calendar cal = getAsCalendar( (TimeZone) tz );

        int   year  = cal.get( Calendar.YEAR );
        int   month = cal.get( Calendar.MONTH ) + 1; // Calendar counts from 0
        int   day   = cal.get( Calendar.DAY_OF_MONTH ); // Calendar counts from 1
        int   hour  = cal.get( Calendar.HOUR_OF_DAY );
        int   min   = cal.get( Calendar.MINUTE );
        float sec   = cal.get( Calendar.SECOND ) + (0.001f * cal.get( Calendar.MILLISECOND ));

        int millis = cal.get( Calendar.MILLISECOND );

        return rep.formatEntry(
                getClass(),
                StringRepresentation.DEFAULT_ENTRY,
                pars,
       /*  0 */ this,
       /*  1 */ editVar,
       /*  2 */ editIndex,
       /*  3 */ year,
       /*  4 */ month,
       /*  5 */ day,
       /*  6 */ hour,
       /*  7 */ min,
       /*  8 */ sec,
       /*  9 */ (int) sec,
       /* 10 */ millis,
       /* 11 */ ((TimeZone)tz).getID(),
       /* 12 */ ((TimeZone)tz).getDisplayName() );
    }

    /**
     * The time, in System.currentTimeMillis() format.
     */
    private long theValue;
}
