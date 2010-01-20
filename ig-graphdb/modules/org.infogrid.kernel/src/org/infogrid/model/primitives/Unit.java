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

/**
  * <p>This represents a unit for a numerical value. It uses a UnitFamily and a
  * numerical prefix to distinguish, for example, centimeters from inches.
  * Currently, the set of units is hard-coded and cannot be changed by the
  * user or application programmer. Not sure whether we every will change this.</p>
  *
  * FIXME -- not tested.
  */
public class Unit
{
    /**
      * The pre-defined length unit family.
      */
    public static final UnitFamily theLengthUnitFamily = new UnitFamily(
            "length", 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 );

    /** Meter. */
    public static final Unit theMeterUnit      = new Unit( "meter",      "theMeterUnit",      "m",  1.0,       theLengthUnitFamily );
    /** Centimeter. */
    public static final Unit theCentimeterUnit = new Unit( "centimeter", "theCentimeterUnit", "cm", 0.01,      theLengthUnitFamily );
    /** Kilometer. */
    public static final Unit theKilometerUnit  = new Unit( "kilometer",  "theKilometerUnit",  "km", 1000.,     theLengthUnitFamily );
    /** Millimeter. */
    public static final Unit theMillimeterUnit = new Unit( "millimeter", "theMillimeterUnit", "mm", 0.001,     theLengthUnitFamily );
    /** Micron / micrometer. */
    public static final Unit theMicrometerUnit = new Unit( "micrometer", "theMicrometerUnit", "mm", 0.000001,  theLengthUnitFamily );
    /** Inch. */
    public static final Unit theInchUnit       = new Unit( "inch",       "theInchUnit",       "in", 0.0254,    theLengthUnitFamily );
    /** Foot. */
    public static final Unit theFootUnit       = new Unit( "foot",       "theFootUnit",       "ft", 12*0.0254, theLengthUnitFamily );
    /** Mile. */
    public static final Unit theMileUnit       = new Unit( "mile",       "theMileUnit",       "mi", 1609.344,  theLengthUnitFamily );

    /**
      * The pre-defined area unit family.
      */
    public static final UnitFamily theAreaUnitFamily = new UnitFamily(
            "area", 2, 0, 0, 0, 0, 0, 0, 0, 0, 0 );

    /** Square Meter. */
    public static final Unit theSquareMeterUnit      = new Unit( "square meter",      "theSquareMeterUnit",      "m\u00B2",  1.0,           theAreaUnitFamily );
    /** Square Centimeter. */
    public static final Unit theSquareCentimeterUnit = new Unit( "square centimeter", "theSquareCentimeterUnit", "cm\u00B2", 0.01*0.01,     theAreaUnitFamily );
    /** Square Millimeter. */
    public static final Unit theSquareMillimeterUnit = new Unit( "square millimeter", "theSquareMillimeterUnit", "mm\u00B2", 0.001*0.001,   theAreaUnitFamily );
    /** Square Micrometer / Square Micron. */
    public static final Unit theSquareMicrometerUnit = new Unit( "square micrometer", "theSquareMicrometerUnit", "um\u00B2", 1.0e-6*2.0e-6, theAreaUnitFamily );
    /** Square Inch. */
    public static final Unit theSquareInchUnit       = new Unit( "square inch",       "theSquareInchUnit",       "in\u00B2", 0.0254*0.0254, theAreaUnitFamily );

    /**
      * The pre-defined mass unit family.
      */
    public static final UnitFamily theMassUnitFamily = new UnitFamily(
            "mass", 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 );

    /** Kilogram. */
    public static final Unit theKilogramUnit = new Unit( "kilogram", "theKilogramUnit", "kg", 1.0,   theMassUnitFamily );
    /** Gram. */
    public static final Unit theGramUnit     = new Unit( "gram",     "theGramUnit",     "g",  0.001, theMassUnitFamily );

    /**
      * The pre-defined time unit family.
      */
    public static final UnitFamily theTimeUnitFamily = new UnitFamily(
            "time", 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 );

    /** Second. */
    public static final Unit theSecondUnit      = new Unit( "second",      "theSecondUnit",      "sec",  1.0,            theTimeUnitFamily );
    /** Millisecond. */
    public static final Unit theMillisecondUnit = new Unit( "millisecond", "theMillisecondUnit", "msec", 0.001,          theTimeUnitFamily );
    /** Microsecond. */
    public static final Unit theMicrosecondUnit = new Unit( "microsecond", "theMicrosecondUnit", "usec", 0.000001,       theTimeUnitFamily );
    /** Minute. */
    public static final Unit theMinuteUnit      = new Unit( "minute",      "theMinuteUnit",      "min",  60.,            theTimeUnitFamily );
    /** Hour. */
    public static final Unit theHourUnit        = new Unit( "hour",        "theHourUnit",        "h",    3600.,          theTimeUnitFamily );
    /** Day. */
    public static final Unit theDayUnit         = new Unit( "day",         "theDayUnit",         "d",    3600*24.,       theTimeUnitFamily );
    /** Week. */
    public static final Unit theWeekUnit        = new Unit( "week",        "theWeekUnit",        "w",    3600*24*7.,     theTimeUnitFamily );
    /** Month. */
    public static final Unit theMonthUnit       = new Unit( "month",       "theMonthUnit",       "m",    3600*24*30.,    theTimeUnitFamily );
    /** Year. */
    public static final Unit theYearUnit        = new Unit( "year",        "theYearUnit",        "yr",   3600*24*365.25, theTimeUnitFamily );

    /**
      * The pre-defined current unit family.
      */
    public static final UnitFamily theCurrentUnitFamily = new UnitFamily(
            "current", 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 );

    /** Ampere. */
    public static final Unit theAmpereUnit      = new Unit( "ampere",      "theAmpereUnit",      "A",  1.0,      theCurrentUnitFamily );
    /** Milliampere. */
    public static final Unit theMilliampereUnit = new Unit( "milliampere", "theMilliampereUnit", "mA", 0.001,    theCurrentUnitFamily );
    /** Microampere. */
    public static final Unit theMicroampereUnit = new Unit( "microampere", "theMicroampereUnit", "uA", 0.000001, theCurrentUnitFamily );
    /** Nanoampere. */
    public static final Unit theNanoampereUnit  = new Unit( "nanoampere",  "theNanoampereUnit",  "nA", 1.e-9,    theCurrentUnitFamily );

    /**
      * The pre-defined temperature unit family.
      */
    public static final UnitFamily theTemperatureUnitFamily = new UnitFamily(
            "temperature", 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 );

    /** Kelvin. */
    public static final Unit theKelvinUnit = new Unit( "kelvin", "theKelvinUnit", "K", 1.0, theTemperatureUnitFamily );
    // FIXME need to extend to support Celcius and Fahrenheit

    /**
      * The pre-defined speed unit family.
      */
    public static final UnitFamily theSpeedUnitFamily = new UnitFamily(
            "speed", 1, 0, -1, 0, 0, 0, 0, 0, 0, 0 );

    /** Meter Per Second. */
    public static final Unit theMeterPerSecondUnit   = new Unit( "meters per second",   "theMeterPerSecondUnit",   "m/sec", 1.0,            theSpeedUnitFamily );
    /** Kilometer Per Hour. */
    public static final Unit theKilometerPerHourUnit = new Unit( "kilometers per hour", "theKilometerPerHourUnit", "km/h",  1/3.6,          theSpeedUnitFamily );
    /** Mile Per Second. */
    public static final Unit theMilePerHourUnit      = new Unit( "miles per hour",      "theMilePerHourUnit",      "mi/h",  1/3.6*1.609344, theSpeedUnitFamily );

    /**
      * The pre-defined power unit family.
      */
    public static final UnitFamily thePowerUnitFamily = new UnitFamily(
            "power", 2, 1, -3, 0, 0, 0, 0, 0, 0, 0 );

    /** Watt. */
    public static final Unit theWattUnit      = new Unit( "Watt",      "theWattUnit",      "W",  1.0,   thePowerUnitFamily );
    /** Milliwatt. */
    public static final Unit theMilliwattUnit = new Unit( "Milliwatt", "theMilliwattUnit", "mW", 0.001, thePowerUnitFamily );
    /** Kilowatt. */
    public static final Unit theKilowattUnit  = new Unit( "Kilowatt",  "theKilowattUnit",  "kW", 1000., thePowerUnitFamily );
    /** Megawatt. */
    public static final Unit theMegawattUnit  = new Unit( "Megawatt",  "theMegawattUnit",  "MW", 1.e6,  thePowerUnitFamily );

    /**
      * The pre-defined energy unit family.
      */
    public static final UnitFamily theEnergyUnitFamily = new UnitFamily(
            "energy", 2, 1, -2, 0, 0, 0, 0, 0, 0, 0 );

    /** Joule. */
    public static final Unit theJouleUnit = new Unit( "Joule", "theJouleUnit", "J", 1.0, theEnergyUnitFamily );

    /**
      * The pre-defined voltage unit family.
      */
    public static final UnitFamily theVoltageUnitFamily = new UnitFamily(
            "voltage", 2, 1, -3, -1, 0, 0, 0, 0, 0, 0 );

    /** Volt. */
    public static final Unit theVoltUnit      = new Unit( "Volt",      "theVoltUnit",      "V",  1.0,   theVoltageUnitFamily );
    /** Millivolt. */
    public static final Unit theMillivoltUnit = new Unit( "Millivolt", "theMillivoltUnit", "mV", 0.001, theVoltageUnitFamily );

    /**
      * The pre-defined resistance unit family.
      */
    public static final UnitFamily theResistanceUnitFamily = new UnitFamily(
            "resistance", 2, 1, -3, -2, 0, 0, 0, 0, 0, 0 );

    /** Ohm. */
    public static final Unit theOhmUnit      = new Unit( "Ohm",      "theOhmUnit",      "\u2126",  1.0,   theResistanceUnitFamily );
    /** Milliohm. */
    public static final Unit theMilliohmUnit = new Unit( "Milliohm", "theMilliohmUnit", "m\u2126", 0.001, theResistanceUnitFamily );
    /** Kiloohm. */
    public static final Unit theKiloohmUnit  = new Unit( "Kiloohm",  "theKiloohmUnit",  "k\u2126", 1000., theResistanceUnitFamily );
    /** Megaohm. */
    public static final Unit theMegaohmUnit  = new Unit( "Megaohm",  "theMegaohmUnit",  "M\u2126", 1.e6,  theResistanceUnitFamily );
    /** Gigaohm. */
    public static final Unit theGigaohmUnit  = new Unit( "Gigaohm",  "theGigaohmUnit",  "G\u2126", 1.e9,  theResistanceUnitFamily );

    /**
      * The pre-defined information unit family.
      */
    public static final UnitFamily theInformationUnitFamily = new UnitFamily(
            "information", 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 );

    /** Bit. */
    public static final Unit theBitUnit      = new Unit( "Bit",      "theBitUnit",      "b",  1.0,                        theInformationUnitFamily );
    /** Byte. */
    public static final Unit theByteUnit     = new Unit( "Byte",     "theByteUnit",     "B",  8.,                         theInformationUnitFamily );
    /** Kilobyte. */
    public static final Unit theKilobyteUnit = new Unit( "Kilobyte", "theKilobyteUnit", "KB", 8.*1024.,                   theInformationUnitFamily );
    /** Megabyte. */
    public static final Unit theMegabyteUnit = new Unit( "Megabyte", "theMegabyteUnit", "MB", 8.*1024.*1024.,             theInformationUnitFamily );
    /** Gigabyte. */
    public static final Unit theGigabyteUnit = new Unit( "Gigabyte", "theGigabyteUnit", "GB", 8.*1024.*1024.*1024.,       theInformationUnitFamily );
    /** Teraabyte. */
    public static final Unit theTerabyteUnit = new Unit( "Terabyte", "theTerabyteUnit", "TB", 8.*1024.*1024.*1024.*1024., theInformationUnitFamily );

    /**
     * All unit families that we are aware of.
     */
    protected static final UnitFamily [] allUnitFamilies = {
            theLengthUnitFamily,
            theAreaUnitFamily,
            theMassUnitFamily,
            theTimeUnitFamily,
            theCurrentUnitFamily,
            theTemperatureUnitFamily,
            theSpeedUnitFamily,
            thePowerUnitFamily,
            theEnergyUnitFamily,
            theVoltageUnitFamily,
            theResistanceUnitFamily,
            theInformationUnitFamily
    };

    /**
      * Private constructor. This keeps the set of constants defined in this file a closed list.
      *
      * @param name the name of the Unit
      * @param programmingTextName the name of the static variable in this file declaring the Unit
      * @param symbol the generally-accepted symbol for this Unit
      * @param prefix the numeric prefix for the UnitFamily
      * @param family the UnitFamily that this Unit belongs to
      */
    private Unit(
            String     name,
            String     programmingTextName,
            String     symbol,
            double     prefix,
            UnitFamily family )
    {
        theName                = name;
        theProgrammingTextName = programmingTextName;
        theSymbol              = symbol;
        thePrefix              = prefix;
        theFamily              = family;

        theFamily.addUnit( this );
    }

    /**
      * Determine equality.
      *
      * @param other the object to compare against
      * @return true if the other object is a Unit with the same name and the same UnitFamily
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof Unit ) {
            Unit realOther = (Unit) other;

            if(    thePrefix == realOther.thePrefix
                && theName.equals( realOther.theName )
                && theFamily.equals( realOther.theFamily ))
            {
                return true;
            }
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
        return ((int) thePrefix ) ^ theName.hashCode() ^ theFamily.hashCode();
    }

    /**
      * Obtaine the name of this Unit.
      *
      * @return the name of this Unit
      */
    public String getName()
    {
        return theName;
    }

    /**
      * Obtain the generally-accepted symbol to be displayed for this Unit.
      *
      * @return the generally-accepted symbol to be displayed for this Unit
      */
    public String getSymbol()
    {
        return theSymbol;
    }

    /**
      * Obtain the numeric prefix of this Unit.
      *
      * @return the numeric prefix of this Unit
      */
    public double getPrefix()
    {
        return thePrefix;
    }

    /**
      * Obtain the UnitFamily that this Unit belongs to.
      *
      * @return the UnitFamily that this Unit belongs to
      */
    public UnitFamily getFamily()
    {
        return theFamily;
    }

    /**
      * Return in a string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        return getSymbol();
    }

    /**
     * Obtain a string which is the Java-language constructor statement reflecting
     * this value. This is mostly important for code generation.
     *
     * @return the Java constructor string
     */
    public String getJavaConstructorString()
    {
        StringBuilder buf = new StringBuilder( theProgrammingTextName.length() + 32 );
        buf.append( getClass().getName() );
        buf.append( "." );
        buf.append( theProgrammingTextName );
        return buf.toString();
    }

    /**
      * The numerical prefix for this Unit with respect to the UnitFamily.
      */
    protected double thePrefix;

    /**
      * The name of this Unit.
      */
    protected String theName;

    /**
      * The name of the static variable in this file which represents this Unit.
      */
    protected String theProgrammingTextName;

    /**
      * The symbol that is used to display the Unit.
      */
    protected String theSymbol;

    /**
      * The UnitFamily that this Unit belongs to.
      */
    protected UnitFamily theFamily;
}
