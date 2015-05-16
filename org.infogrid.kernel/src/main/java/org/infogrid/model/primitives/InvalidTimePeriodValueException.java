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

/**
 * Thrown if the specified TimePeriodValue is illegal.
 */
public abstract class InvalidTimePeriodValueException
        extends
            InvalidPropertyValueException
{
    /**
     * Constructor.
     *
     * @param year the year
     * @param month the month
     * @param day the day
     * @param hour the hour
     * @param minute the minute
     * @param second the second
     */
    protected InvalidTimePeriodValueException(
            short year,
            short month,
            short day,
            short hour,
            short minute,
            float second )
    {
        this.theYear   = year;
        this.theMonth  = month;
        this.theDay    = day;
        this.theHour   = hour;
        this.theMinute = minute;
        this.theSecond = second;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */    
    public Object [] getLocalizationParameters()
    {
        return new Object[] {
                theYear,
                theMonth,
                theDay,
                theHour,
                theMinute,
                theSecond,
                (int) theSecond,
                ((int) ( theSecond * 1000 )) % 1000
        };
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
    
    /**
     * The Year was out of range.
     */
    public static class Year
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Year(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
    
    /**
     * The Month was out of range.
     */
    public static class Month
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Month(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
    
    /**
     * The Day was out of range.
     */
    public static class Day
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Day(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
    
    /**
     * The Hour was out of range.
     */
    public static class Hour
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Hour(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
    
    /**
     * The Minute was out of range.
     */
    public static class Minute
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Minute(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
    
    /**
     * The Second was out of range.
     */
    public static class Second
            extends
                InvalidTimePeriodValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param year the year
         * @param month the month
         * @param day the day
         * @param hour the hour
         * @param minute the minute
         * @param second the second
         */
        protected Second(
                short year,
                short month,
                short day,
                short hour,
                short minute,
                float second )
        {
            super( year, month, day, hour, minute, second );
        }
    }
}
