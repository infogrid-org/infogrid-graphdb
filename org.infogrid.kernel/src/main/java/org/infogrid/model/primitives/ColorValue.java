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

import java.awt.Color;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * This is a color value for PropertyValue.
 * This used to go through java.awt.Color, but it turns out that in headless mode, that
 * class may not exist.
 */
public final class ColorValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param value the JDK's definition of Color
     * @return the created ColorValue
     * @throws IllegalArgumentException if null was provided
     */
    public static ColorValue create(
            Color value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new ColorValue( value.getRGB() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the JDK's definition of Color
     * @return the created ColorValue, or null
     * @throws IllegalArgumentException if null was provided
     */
    public static ColorValue createOrNull(
            Color value )
    {
        if( value == null ) {
            return null;
        }
        return new ColorValue( value.getRGB() );
    }

    /**
     * Factory method.
     *
     * @param value the value as RGB value
     * @return the created ColorValue
     */
    public static ColorValue create(
            int value )
    {
        return new ColorValue( value );
    }

    /**
     * Factory method.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return the created ColorValue
     */
    public static ColorValue create(
            int r,
            int g,
            int b )
    {
        return create( r, g, b, 255 );
    }

    /**
     * Factory method.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @return the created ColorValue
     */
    public static ColorValue create(
            int r,
            int g,
            int b,
            int a )
    {
        @SuppressWarnings("PointlessBitwiseExpression")
        ColorValue ret = new ColorValue(
                  (( a & 0xFF ) << 24 )
                | (( r & 0xFF ) << 16 )
                | (( g & 0xFF ) <<  8 )
                | (( b & 0xFF ) <<  0 ));

        return ret;
    }

    /**
     * Factory method.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @return the created ColorValue
     */
    public static ColorValue create(
            float r,
            float g,
            float b )
    {
        return create( r, g, b, 1.0f );
    }

    /**
     * Factory method.
     *
     * @param r the red component
     * @param g the green component
     * @param b the blue component
     * @param a the alpha component
     * @return the created ColorValue
     */
    public static ColorValue create(
            float r,
            float g,
            float b,
            float a )
    {
        return create(
                (int) (r*255+0.5),
                (int) (g*255+0.5),
                (int) (b*255+0.5));
    }

    /**
      * Initialize to specified value.
      *
      * @param value the JDK's definition of Color
      */
    private ColorValue(
            int value )
    {
        this.theValue = value;
    }

    /**
      * Convert back to the JDK's definition of this Color.
      *
      * @return the JDK's definition of this Color.
      */
    public Color value()
    {
        return new Color( theValue );
    }

    /**
     * Obtain the value of the alpha component.
     *
     * @return value of the alpha component
     */
    public int getAlpha()
    {
        return ( theValue >> 24 ) & 0xFF;
    }

    /**
      * Obtain value of the red component.
      *
      * @return value of the red component
      */
    public int getRed()
    {
        return ( theValue >> 16 ) & 0xFF;
    }

    /**
      * Obtain value of the blue component.
      *
      * @return value of the blue component
      */
    @SuppressWarnings("PointlessBitwiseExpression")
    public int getBlue()
    {
        return ( theValue >> 0 ) & 0xFF;
    }

    /**
      * Obtain value of the green component.
      *
      * @return value of the green component
      */
    public int getGreen()
    {
        return ( theValue >> 8 ) & 0xFF;
    }

    /**
      * Obtain as RGB value.
      *
      * @return the RGB value
      */
    public int getRGB()
    {
        return theValue;
    }

    /**
      * Determine equality of two objects.
      *
      * @param otherValue the object to test against
      * @return if true, the objects are equal
      */
    @Override
    public boolean equals(
            Object otherValue )
    {
        if( otherValue instanceof ColorValue ) {
            return theValue == ((ColorValue)otherValue).theValue;
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
        return theValue;
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
        buf.append( getClass().getName());
        buf.append( DataType.CREATE_STRING );
        buf.append( theValue );
        buf.append( DataType.CLOSE_PARENTHESIS_STRING );
        return buf.toString();
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
        buf.append( "(" );
        buf.append( getRed() );
        buf.append( ";" );
        buf.append( getGreen() );
        buf.append( ";" );
        buf.append( getBlue() );
        buf.append( ";" );
        buf.append( getAlpha() );
        buf.append( ")" );
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
        ColorValue realValue = (ColorValue) o;

        if( theValue == realValue.theValue ) {
            return 0;
        } else {
            return +2; // not comparable convention: +2
        }
    }

    /**
      * This attempts to parse a string and turn it into a integer value similarly
      * to Integer.parseInt().
      *
      * @param theString the string that shall be parsed
      * @return the created IntegerValue
      * @throws NumberFormatException thrown if theString does not follow the correct syntax
      */
    public static ColorValue parseColorValue(
            String theString )
        throws
            NumberFormatException
    {
        return ColorValue.create( Integer.parseInt( theString ));
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
        /* 3 */ getRed(),
        /* 4 */ getGreen(),
        /* 5 */ getBlue(),
        /* 6 */ getAlpha(),
        /* 7 */ getRGB() );
    }

    /**
      * The value encoded the same way as java.awt.Color.
      */
    protected int theValue;
}
