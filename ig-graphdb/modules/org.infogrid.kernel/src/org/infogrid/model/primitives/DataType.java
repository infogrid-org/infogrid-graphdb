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

import java.io.Serializable;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.IllegalPropertyValueException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.text.ModelPrimitivesStringRepresentationParameters;
import org.infogrid.util.text.HasStringRepresentation;
import org.infogrid.util.text.SimpleStringRepresentationParameters;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationDirectory;
import org.infogrid.util.text.StringRepresentationDirectorySingleton;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;
import org.infogrid.util.text.StringifierUnformatFactory;

/**
  * This represents a data type for properties. This is an abstract class;
  * a more suitable subclass shall be instantiated.
  * A DataType can refine another DataType and usually does.
  *
  * Note: This is different from a value for properties:
  * Instances of (a subtype of) DataType represent the notion of a type,
  * such as "a positive integer", while instances of (a subtype of)
  * PropertyValue represent a value, such as "7".
  */
public abstract class DataType
        implements
            HasStringRepresentation,
            StringifierUnformatFactory,
            Serializable
{
    /**
      * Constructor for subclasses only.
      *
      * @param supertype the DataType that we refine, if any
      */
    protected DataType(
            DataType supertype )
    {
        theSupertype = supertype;
    }

    /**
      * Obtain the name of the DataType.
      *
      * @return the name of the DataType
      */
    public final String getName()
    {
        return this.getClass().getName();
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return true if the two objects are equal
      */
    @Override
    public abstract boolean equals(
            Object other );

    /**
      * Test whether this DataType is a superset of or equals the argument
      * DataType. This is useful to test whether an assigment of
      * values is legal prior to attempting to do it. By default, this delegates
      * to the equals method.
      *
      * @param other the DataType we test against
      * @return if true, this DataType is a superset or equals the argument DataType
      */
    public boolean isSupersetOfOrEquals(
            DataType other )
    {
        return equals( other );
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
    public abstract int conforms(
            PropertyValue value )
        throws
            ClassCastException;

    /**
      * Determine whether a domain check shall be performed on
      * assignments. Default to false.
      *
      * @return if true, a domain check shall be performed prior to performing assignments
      */
    public boolean getPerformDomainCheck()
    {
        return false;
    }

    /**
      * Return a boolean expression in the Java language that uses
      * varName as an argument and that evaluates whether the content
      * of variable varName is assignable to a value of this data type.
      * For example, on an integer data type with minimum and maximum
      * values, one would call it with argument "theValue", and it could
      * return: "( theValue > 2 && theValue < 7 )", where "2" and "7" are
      * the values of the min and max attributes of the integer data type.
      *
      * This is used primarily for code-generation purposes.
      *
      * @param varName the name of the variable containing the value
      * @return the Java language  expression
      */
    public String getJavaDomainCheckExpression(
            String varName )
    {
        return "true";
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public abstract Class getCorrespondingJavaClass();

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
    public abstract String getJavaConstructorString(
            String classLoaderVar );

    /**
      * Instantiate this data type into a PropertyValue with a
      * reasonable default value.
      *
      * @return a PropertyValue with a reasonable default value that is an instance of this DataType
      */
    public final PropertyValue instantiate()
    {
        return getDefaultValue();
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public abstract PropertyValue getDefaultValue();

    /**
     * If this DataType is a refinement of another, find which DataType it refines.
     *
     * @return the refined DataType, if any
     */
    public final DataType getSupertype()
    {
        return theSupertype;
    }

    /**
     * Obtain the start part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars the parameters to use
     * @return String representation
     */
    public String toStringRepresentationLinkStart(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
    {
        return "";
    }

    /**
     * Obtain the end part of a String representation of this MeshBase that acts
     * as a link/hyperlink and can be shown to the user.
     * 
     * @param rep the StringRepresentation
     * @param pars the parameters to use
     * @return String representation
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
    {
        return "";
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     * 
     * @param representation the StringRepresentation in which the String s is given
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public abstract PropertyValue fromStringRepresentation(
            StringRepresentation representation,
            String               s,
            String               mimeType )
        throws
            PropertyValueParsingException;

    /**
     * Format a Property.
     *
     * @param owningMeshObject the MeshObject that owns this Property
     * @param propertyType the PropertyType of the Property
     * @param representation the representation scheme
     * @param pars collects parameters that may influence the String representation
     * @return the String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public String formatProperty(
            MeshObject                     owningMeshObject,
            PropertyType                   propertyType,
            StringRepresentation           representation,
            StringRepresentationParameters pars )
        throws
            StringifierException,
            IllegalPropertyTypeException,
            NotPermittedException
    {
        String  editVar    = null;
        Boolean allowNull  = null;

        PropertyValue currentValue = owningMeshObject.getPropertyValue( propertyType );
        PropertyValue defaultValue = propertyType.getDefaultValue();

        if( pars != null ) {
            if( currentValue == null ) {
                String nullString = (String) pars.get( StringRepresentationParameters.NULL_STRING );
                if( nullString != null ) {
                    return nullString;
                }
            }
            editVar   = (String) pars.get( StringRepresentationParameters.EDIT_VARIABLE );
            allowNull = (Boolean) pars.get( ModelPrimitivesStringRepresentationParameters.ALLOW_NULL );
            if( allowNull != null && allowNull.booleanValue() ) {
                allowNull = propertyType.getIsOptional().value();
            } // else if not allowNull from the parameters, don't care what the PropertyType says
        }
        if( allowNull == null ) {
            allowNull = propertyType.getIsOptional().value();
        }

        if( pars == null ) {
            pars = SimpleStringRepresentationParameters.create();
        }

        String entry;
        if( currentValue != null ) {
            entry = "Value";
        } else {
            entry = "Null";
        }

        if( defaultValue == null ) {
            defaultValue = getDefaultValue(); // the DataType's default, rather than the PropertyType's (which is null)
        }
        StringRepresentation           jsRep    = StringRepresentationDirectorySingleton.getSingleton().get( StringRepresentationDirectory.TEXT_JAVASCRIPT_NAME );
        StringRepresentationParameters realPars = pars.with( ModelPrimitivesStringRepresentationParameters.PROPERTY_TYPE, propertyType );

        String currentValueJsString = PropertyValue.toStringRepresentationOrNull( currentValue, jsRep, realPars );
        String defaultValueJsString = PropertyValue.toStringRepresentationOrNull( defaultValue, jsRep, realPars );

        String propertyHtml;
        if( currentValue != null ) {
            propertyHtml = currentValue.toStringRepresentation( representation, realPars );
        } else {
            propertyHtml = defaultValue.toStringRepresentation( representation, realPars );
        }

        String ret = representation.formatEntry(
                DataType.class,
                entry,
                realPars,
        /* 0 */ owningMeshObject,
        /* 1 */ propertyType,
        /* 2 */ currentValue,
        /* 3 */ currentValueJsString,
        /* 4 */ defaultValue,
        /* 5 */ defaultValueJsString,
        /* 6 */ propertyHtml,
        /* 7 */ allowNull,
        /* 8 */ propertyType.getIsReadOnly().value(),
        /* 9 */ editVar );

        return ret;
    }

    /**
     * The supertype of this DataType (if any).
     */
    protected DataType theSupertype;

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String CREATE_STRING = ".create( ";

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String COMMA_STRING = ", ";

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String NULL_STRING = "null";

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String QUOTE_STRING = "\"";

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String DEFAULT_STRING = ".theDefault";

    /**
     * String constant used in our subclasses in order to avoid using up more memory than necessary.
     */
    public static final String CLOSE_PARENTHESIS_STRING = " )";

    /**
     * The default entry in the resource files for a DataType, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_ENTRY = "Type";
}
