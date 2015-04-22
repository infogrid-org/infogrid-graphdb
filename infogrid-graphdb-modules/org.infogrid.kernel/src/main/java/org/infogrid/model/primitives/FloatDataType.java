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

import java.io.ObjectStreamException;
import java.text.ParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParseException;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is a float DataType for PropertyTypes
  * with explicit minimum and maximum values, which may be infinity. Minimum and
  * maximum are included in the domain. It can also carry a unit.
  *
  * FIXME: units not tested.
  */
public class FloatDataType
        extends
            DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class. It represents
      * a float data type with no domain restriction.
      */
    public static final FloatDataType theDefault
            = new FloatDataType(
                    FloatValue.create( Float.NEGATIVE_INFINITY ),
                    FloatValue.create( Float.POSITIVE_INFINITY ),
                    null,
                    null );

    /**
      * This is another default instance of this class. It represents
      * a float data type which can only be positive or zero.
      */
    public static final FloatDataType thePositiveDefault
            = new FloatDataType(
                    FloatValue.create( 0.f ),
                    FloatValue.create( Float.POSITIVE_INFINITY ),
                    null,
                    theDefault );

    /**
      * Construct one with minimum and maximum values (inclusive).
      * Use constants defined in java.lang.Float to represent infinity.
      *
      * @param min the smallest allowed value (may be negative infinity)
      * @param max the largest allowed value (may be infinity)
      * @param superType the DataType that we refine, if any
      * @return the created FloatDataType
      */
    public static FloatDataType create(
            FloatValue min,
            FloatValue max,
            DataType   superType )
    {
        return new FloatDataType( min, max, null, superType );
    }

     /**
      * Construct one with minimum and maximum values (inclusive) and a unit family.
      * This allows one to specify that a certain meta-attribute can only hold
      * values with a specific unit, eg a length.
      * Use constants defined in java.lang.Float to represent infinity.
      *
      * @param min the smallest allowed value (may be negative infinity)
      * @param max the largest allowed value (may be infinity)
      * @param u the UnitFamily, if any
      * @param superType the DataType that we refine, if any
      * @return the created FloatDataType
      */
    public static FloatDataType create(
            FloatValue min,
            FloatValue max,
            UnitFamily u,
            DataType   superType )
    {
        return new FloatDataType( min, max, u, superType );
    }

     /**
      * Private constructor, use factory method.
      *
      * @param min the smallest allowed value (may be negative infinity)
      * @param max the largest allowed value (may be infinity)
      * @param u the UnitFamily, if any
      * @param superType the DataType that we refine, if any
      */
    private FloatDataType(
            FloatValue min,
            FloatValue max,
            UnitFamily u,
            DataType   superType )
    {
        super( superType );

        this.theMin = min;
        this.theMax = max;

        this.theUnitFamily = u;

        if( theUnitFamily == null ) {
            if( theMin.getUnit() != null ) {
                throw new IllegalArgumentException( "Min value has unit, data type has none" );
            }
            if( theMax.getUnit() != null ) {
                throw new IllegalArgumentException( "Max value has unit, data type has none" );
            }
        } else {
            if( ! theMin.getUnit().getFamily().equals( theUnitFamily )) {
                throw new IllegalArgumentException( "Min value has wrong unit family" );
            }
            if( ! theMax.getUnit().getFamily().equals( theUnitFamily )) {
                throw new IllegalArgumentException( "Max value has wrong unit family" );
            }
        }
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return true if the two objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof FloatDataType ) {
            FloatDataType realOther = (FloatDataType) other;
            if(    ( ! theMin.equals( realOther.theMin ))
                || ( ! theMax.equals( realOther.theMax ))) {
                return false;
            }

            if( theUnitFamily == null ) {
                return( realOther.theUnitFamily == null );
            } else {
                return theUnitFamily.equals( realOther.theUnitFamily );
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
        if( theMin != null ) {
            ret ^= theMin.hashCode();
        }
        if( theMax != null ) {
            ret ^= theMax.hashCode();
        }
        if( theUnitFamily != null ) {
            ret ^= theUnitFamily.hashCode();
        }
        return ret;
    }
    /**
      * Test whether this DataType is a superset of or equals the argument
      * DataType. This is useful to test whether an assigment of
      * values is legal prior to attempting to do it.
      *
      * @param other the DataType we test against
      * @return if true, this DataType is a superset or equals the argument DataType
      */
    @Override
    public boolean isSupersetOfOrEquals(
            DataType other )
    {
        if( other instanceof FloatDataType ) {
            FloatDataType realOther = (FloatDataType) other;
            return    ( theMin.isSmallerOrEquals( realOther.theMin ))
                   && ( realOther.theMax.isSmallerOrEquals( theMax ))
                   && (    ( theUnitFamily == null && realOther.theUnitFamily == null )
                        || ( theUnitFamily != null && theUnitFamily.equals( realOther.theUnitFamily )));
        }
        return false;
    }

    /**
      * Determine whether a domain check shall be performed on
      * assignments. Default to false.
      *
      * @return if true, a domain check shall be performed prior to performing assignments
      */
    @Override
    public boolean getPerformDomainCheck()
    {
        return    ( theMin.theValue != Float.NEGATIVE_INFINITY )
               || ( theMax.theValue != Float.POSITIVE_INFINITY );
    }

    /**
     * Determine whether this PropertyValue conforms to the constraints of this instance of DataType.
     *
     * @param value the candidate PropertyValue
     * @return 0 if the candidate PropertyValue conforms to this type. Non-zero values depend
     *         on the DataType; generally constructed by analogy with the return value of strcmp.
     * @throws ClassCastException if this PropertyValue has the wrong type (e.g.
     *         the PropertyValue is a StringValue, and the DataType an IntegerDataType)
     */
    public int conforms(
            PropertyValue value )
        throws
            ClassCastException
    {
        FloatValue realValue = (FloatValue) value; // may throw

        if( theMin.theValue > realValue.value() ) {
            return -1;
        }
        if( theMax.theValue < realValue.value() ) {
            return +1;
        }
        return 0;
    }

    /**
      * Return a boolean expression in the Java language that uses
      * varName as an argument and that evaluates whether the content
      * of variable varName is assignable to a value of this data type.
      *
      * This is used primarily for code-generation purposes.
      * FIXME add support for units.
      *
      * @param varName the name of the variable containing the value
      * @return the boolean expression
      */
    @Override
    public String getJavaDomainCheckExpression(
            String varName )
    {
        if( theMin.theValue == Float.NEGATIVE_INFINITY ) {
            if( theMax.theValue == Float.POSITIVE_INFINITY ) {
                return "true";
            } else {
                return "( " + varName + ".value() <= " + String.valueOf( theMax.value() ) + " )";
            }
        } else {
            if( theMax.theValue == Float.POSITIVE_INFINITY ) {
                return "( " + varName + ".value() >= " + String.valueOf( theMin.value() ) + " )";
            } else {
                return "(    ( " + varName + ".value() >= " + String.valueOf( theMin.value() ) + " )"
                   +    " && ( " + varName + ".value() <= " + String.valueOf( theMax.value() ) + " ) )";
            }
        }
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class<FloatValue> getCorrespondingJavaClass()
    {
        return FloatValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public FloatValue getDefaultValue()
    {
        if( theUnitFamily == null ) {
            if( theMin.theValue <= 0.0 && theMax.theValue > 0.0 ) {
                return FloatValue.create( 0.0 );
            }
            return FloatValue.create( theMin.theValue );
        } else {
            // FIXME we should use a "primary unit" here, but we don't know the concept
            if( theMin.theValue <= 0.0 && theMax.theValue > 0.0 ) {
                return FloatValue.create( 0.0, theUnitFamily.getUnitsInFamily()[0] );
            }
            return FloatValue.create( theMin.theValue, theUnitFamily.getUnitsInFamily()[0] );
        }
    }

    /**
      * Obtain minimum allowed value.
      *
      * @return the smallest allowed value (may be negative infinity)
      */
    public FloatValue getMinimum()
    {
        return theMin;
    }

    /**
      * Obtain maximum allowed value.
      *
      * @return the largest allowed value (may be infinity)
      */
    public FloatValue getMaximum()
    {
        return theMax;
    }

    /**
      * Obtain the UnitFamily.
      *
      * @return the UnitFamily, if any
      */
    public UnitFamily getUnitFamily()
    {
        return theUnitFamily;
    }

    /**
     * Correctly deserialize a static instance.
     *
     * @return the static instance if appropriate
     * @throws ObjectStreamException thrown if reading from the stream failed
     */
    public Object readResolve()
        throws
            ObjectStreamException
    {
        if( this.equals( theDefault )) {
            return theDefault;
        } else if( this.equals( thePositiveDefault )) {
            return thePositiveDefault;
        } else {
            return this;
        }
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
        final String className = getClass().getName();

        if( this == theDefault ) {
            return className + DEFAULT_STRING;
        } else if( this == thePositiveDefault ) {
            return className + ".thePositiveDefault";
        } else {
            StringBuilder ret = new StringBuilder( className );
            ret.append( CREATE_STRING );

            if( theMin != null ) {
                ret.append( theMin.getJavaConstructorString( classLoaderVar, null ));
            } else {
                ret.append( NULL_STRING );
            }

            ret.append( COMMA_STRING );

            if( theMax != null ) {
                ret.append( theMax.getJavaConstructorString( classLoaderVar, null ));
            } else {
                ret.append( NULL_STRING );
            }

            ret.append( COMMA_STRING );

            if( theUnitFamily != null ) {
                ret.append( theUnitFamily.getJavaConstructorString( classLoaderVar ));
            } else {
                ret.append( NULL_STRING );
            }
            ret.append( COMMA_STRING );

            if( theSupertype != null ) {
                ret.append( theSupertype.getJavaConstructorString( classLoaderVar ));
            } else {
                ret.append( NULL_STRING );
            }

            ret.append( CLOSE_PARENTHESIS_STRING );
            return ret.toString();
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
        return rep.formatEntry(
                FloatValue.class,
                DEFAULT_ENTRY,
                pars,
                this,
                PropertyValue.toStringRepresentationOrNull( getDefaultValue(), rep, pars ), // all three presumably shorter, but we don't know
                PropertyValue.toStringRepresentationOrNull( theMin,            rep, pars ),
                PropertyValue.toStringRepresentationOrNull( theMax,            rep, pars ),
                theUnitFamily,
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public FloatValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException
    {
        s = s.trim();
        if( s.length() == 0 ) {
            return null;
        }

        try {
            Object [] found = representation.parseEntry( FloatValue.class, StringRepresentation.DEFAULT_ENTRY, pars, s, this );

            FloatValue ret;
            switch( found.length ) {
                case 4:
                case 5:
                    ret = FloatValue.create( (Number) found[3] );
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringRepresentationParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex.getFormatString(), ex );

        } catch( ParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, null, ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }

    /**
      * The value for the minimum.
      */
    protected FloatValue theMin;

    /**
      * The value for the maximum.
      */
    protected FloatValue theMax;

    /**
      * The unit family (if any).
      */
    protected UnitFamily theUnitFamily;
}
