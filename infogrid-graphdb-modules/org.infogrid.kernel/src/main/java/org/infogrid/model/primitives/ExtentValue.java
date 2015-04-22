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

import java.awt.geom.Dimension2D;
import org.infogrid.util.DoubleDimension;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a graphical extent value for PropertyValues.
  */
public final class ExtentValue
        extends
            PropertyValue
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @param value the Dimension2D version of this value
     * @return the created ExtentValue
     */
    public static ExtentValue create(
            Dimension2D value )
    {
        if( value == null ) {
            throw new IllegalArgumentException( "null value" );
        }
        return new ExtentValue( value.getWidth(), value.getHeight() );
    }

    /**
     * Factory method, or return null if the argument is null.
     *
     * @param value the Dimension2D version of this value
     * @return the created ExtentValue, or null
     */
    public static ExtentValue createOrNull(
            Dimension2D value )
    {
        if( value == null ) {
            return null;
        }
        return new ExtentValue( value.getWidth(), value.getHeight() );
    }

    /**
     * Factory method.
     *
     * @param width the theWidth of the extent
     * @param height the theHeight of the extent
     * @return the created ExtentValue
     */
    public static ExtentValue create(
            double width,
            double height )
    {
        return new ExtentValue( width, height );
    }

    /**
      * Private constructor, use factory method.
      *
      * @param width the theWidth of the extent
      * @param height the theHeight of the extent
      */
    private ExtentValue(
            double width,
            double height )
    {
        this.theWidth  = width;
        this.theHeight = height;
    }

    /**
      * Obtain value.
      *
      * @return the value as Dimension2D
      */
    public Dimension2D value()
    {
        return new DoubleDimension( theWidth, theHeight    ); // FIXME as soon as Sun provides this
    }

    /**
      * Determine theWidth of the extent.
      *
      * @return the theWidth of the extent
      */
    public double getWidth()
    {
        return theWidth;
    }

    /**
      * Determine theHeight of the extent.
      *
      * @return the theHeight of the extent
      */
    public double getHeight()
    {
        return theHeight;
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
        if( otherValue instanceof ExtentValue ) {
            return (theHeight == ((ExtentValue)otherValue).theHeight)
                && (theWidth  == ((ExtentValue)otherValue).theWidth);
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
        return (int) ( theHeight * theWidth );
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        return "[" + theWidth + ";" + theHeight + "]";
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
        buf.append( getClass().getName());
        buf.append( DataType.CREATE_STRING );
        buf.append( theWidth  );
        buf.append( ", " );
        buf.append( theHeight  );
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
    public int compareTo(
            PropertyValue o )
    {
        ExtentValue realOther = (ExtentValue) o;

        if( theWidth < realOther.theWidth ) {
            if( theHeight < realOther.theHeight ) {
                return -1;
            } else {
                return +2; // not comparable convention: +2
            }
        } else if( theWidth == realOther.theWidth ) {
            if( theHeight == realOther.theHeight ) {
                return 0;
            } else {
               return +2; // not comparable convention: +2
            }
        } else {
            if( theHeight > realOther.theHeight ) {
                return +1;
            } else {
                return +2; // not comparable convention: +2
            }
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
        /* 3 */ theWidth,
        /* 4 */ theHeight );
    }

    /**
      * The actual value for the theWidth.
      */
    protected double theWidth;

    /**
      * The actual value for the theHeight.
      */
    protected double theHeight;
}
