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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.util.ArrayList;
import org.infogrid.util.ArrayHelper;

/**
  * <p>This represents a family of "compatible" units, eg "cm" and "inch".
  * It does not represent any particular member of this family, but the
  * whole family. Any two compatible units are members of the same family.</p>
  *
  * <p>It follows the SI system, augmented by Bytes. The Exponent attributes
  * are integer values that represent the exponent of the base unit in the
  * composite, eg 2 for meterExponent when talking about an area.</p>
  *
  * <p>Note that temperature cannot be represented using this concept, as it
  * has a linear offset in various systems (F and C vs. K)</p>
  *
  * FIXME -- not tested
  */
public class UnitFamily
{
    /**
      * Private constructor. Use only by Unit.
      *
      * @param name the name of the UnitFamily
      * @param meterExponent exponent for the meter base unit
      * @param kilogramExponent exponent for the kilogram base unit
      * @param secondExponent exponent for the second base unit
      * @param ampereExponent exponent for the ampere base unit
      * @param kelvinExponent exponent for the kelvin base unit
      * @param moleExponent exponent for the mole base unit
      * @param candelaExponent exponent for the candela base unit
      * @param radianExponent exponent for the radian base unit
      * @param steradianExponent exponent for the steriadian base unit
      * @param byteExponent exponent for the byte base unit
      */
    UnitFamily(
            String name,
            int    meterExponent,
            int    kilogramExponent,
            int    secondExponent,
            int    ampereExponent,
            int    kelvinExponent,
            int    moleExponent,
            int    candelaExponent,
            int    radianExponent,
            int    steradianExponent,
            int    byteExponent )
    {
        theName              = name;

        theMeterExponent     = (short) meterExponent;
        theKilogramExponent  = (short) kilogramExponent;
        theSecondExponent    = (short) secondExponent;
        theAmpereExponent    = (short) ampereExponent;
        theKelvinExponent    = (short) kelvinExponent;
        theMoleExponent      = (short) moleExponent;
        theCandelaExponent   = (short) candelaExponent;
        theRadianExponent    = (short) radianExponent;
        theSteradianExponent = (short) steradianExponent;
        theByteExponent      = (short) byteExponent;
    }

    /**
      * Determine equality.
      *
      * @param other the object to compare against
      * @return true if this is the same UnitFamily
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof UnitFamily ) {
            UnitFamily realOther = (UnitFamily) other;

            if(    theName.equals( realOther.theName )
                && isCompatible( realOther ))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine hash code.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = 0;
        ret = ( ret << 1 ) ^ theMeterExponent;
        ret = ( ret << 1 ) ^ theKilogramExponent;
        ret = ( ret << 1 ) ^ theSecondExponent;
        ret = ( ret << 1 ) ^ theAmpereExponent;
        ret = ( ret << 1 ) ^ theKelvinExponent;
        ret = ( ret << 1 ) ^ theMoleExponent;
        ret = ( ret << 1 ) ^ theCandelaExponent;
        ret = ( ret << 1 ) ^ theRadianExponent;
        ret = ( ret << 1 ) ^ theSteradianExponent;
        ret = ( ret << 1 ) ^ theByteExponent;

        return ret;
    }

    /**
      * Find out whether two UnitFamilies are compatible. For example,
      * centimenters and inches are compatible because they both denote length.
      * Meter and square meters are not, because one denotes a length, the other
      * and area.
      *
      * @param realOther the UnitFamily to compare against
      * @return true if the two UnitFamilies are compatible
      */
    public boolean isCompatible(
            UnitFamily realOther )
    {
        if(    theMeterExponent     != realOther.theMeterExponent
            || theKilogramExponent  != realOther.theKilogramExponent
            || theSecondExponent    != realOther.theSecondExponent
            || theAmpereExponent    != realOther.theAmpereExponent
            || theKelvinExponent    != realOther.theKelvinExponent
            || theMoleExponent      != realOther.theMoleExponent
            || theCandelaExponent   != realOther.theCandelaExponent
            || theRadianExponent    != realOther.theRadianExponent
            || theSteradianExponent != realOther.theSteradianExponent
            || theByteExponent      != realOther.theByteExponent )
        {
            return false;
        } else {
            return true;
        }
    }

    /**
      * Obtain the name of this UnitFamily.
      *
      * @return the name of this UnitFamily
      */
    public String getName()
    {
        return theName;
    }

    /**
      * Obtain all the Units that are known to belong to this UnitFamily.
      *
      * @return all known Units that belong to this UnitFamily
      */
    public Unit [] getUnitsInFamily()
    {
        return ArrayHelper.copyIntoNewArray( theUnits, Unit.class );
    }

    /**
      * This allows a Unit to add itself to this UnitFamily.
      *
      * @param newUnit the Unit to be added to this UnitFamily
      */
    protected void addUnit(
            Unit newUnit )
    {
        theUnits.add( newUnit );
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
        StringBuilder ret = new StringBuilder( "new " );
        ret.append( getClass().getName() );
        ret.append( "( " );

        ret.append( DataType.QUOTE_STRING );
        ret.append( theName );
        ret.append( DataType.QUOTE_STRING );
        ret.append( DataType.COMMA_STRING );

        ret.append( theMeterExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theKilogramExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theSecondExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theAmpereExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theKelvinExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theMoleExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theCandelaExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theRadianExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theSteradianExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( theByteExponent );
        ret.append( DataType.COMMA_STRING );

        ret.append( " )" );
        return ret.toString();
    }

    /**
      * The name of this unit.
      */
    protected String theName;

    /**
      * The exponent for the meter.
      */
    protected short theMeterExponent;

    /**
      * The exponent for the kilogram.
      */
    protected short theKilogramExponent;

    /**
      * The exponent for the second.
      */
    protected short theSecondExponent;

    /**
      * The exponent for the ampere.
      */
    protected short theAmpereExponent;

    /**
      * The exponent for the kelvin.
      */
    protected short theKelvinExponent;

    /**
      * The exponent for the mole.
      */
    protected short theMoleExponent;

    /**
      * The exponent for the candela.
      */
    protected short theCandelaExponent;

    /**
      * The exponent for the radian.
      */
    protected short theRadianExponent;

    /**
      * The exponent for the steradian.
      */
    protected short theSteradianExponent;

    /**
      * The exponent for the byte.
      */
    protected short theByteExponent;

    /**
      * These are the Units that we know belong to this UnitFamily.
      */
    protected ArrayList<Unit> theUnits = new ArrayList<Unit>();
}
