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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.io.Serializable;
import org.infogrid.util.text.HasStringRepresentation;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This is the abstract supertype for values of all supported DataTypes for
  * PropertyValues.
  *
  * This also implements Comparable. In addition to the conventions about return values from
  * the Comparable.compareTo method, subclasses of PropertyValues return 2 if the two
  * comparison operands are not comparable.
  */
public abstract class PropertyValue
        implements
            HasStringRepresentation,
            Serializable,
            Comparable<PropertyValue>
{
    /**
     * Obtain a string which is the Java-language constructor expression reflecting this value.
     * For example, a StringValue with value "my foo" would return
     *     new StringValue( "my foo" )
     * as the result.
     * This is mainly for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @param typeVar  name of the variable containing the DataType that goes with the to-be-created instance.
     * @return the Java-language constructor expression
     */
    public abstract String getJavaConstructorString(
            String classLoaderVar,
            String typeVar );

    /**
     * Obtain the underlying value. This is implemented with more specific subtypes in subclasses.
     *
     * @return the underlying value
     */
    public abstract Object value();

    /**
     * Obtain the start part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkStart(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return "";
    }

    /**
     * Obtain the end part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     * 
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return "";
    }
    
    /**
     * Convert a PropertyValue to its String representation, using the representation scheme.
     * Return null if the PropertyValue is null.
     *
     * @param v the PropertyValue to convert
     * @param representation the representation scheme
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public static String toStringRepresentationOrNull(
            PropertyValue                  v,
            StringRepresentation           representation,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        if( v != null ) {
            return v.toStringRepresentation( representation, pars );
        } else {
            return nullToStringRepresentation( representation, pars );
        }
    }

    /**
     * Convert a null PropertyValue to its String representation, using the representation scheme.
     *
     * @param representation the representation scheme
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public static String nullToStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return representation.formatEntry( PropertyValue.class, "Null", pars );
    }

    /**
     * This is a convenience comparison method in case one of the arguments may be null.
     *
     * @param one the first PropertyValue or null to be compared
     * @param two the second PropertyValue or null to be compared
     * @return 0 if the two values are equal, +1/-1 if the second is smaller/larger than the first,
     *         +2/-2 if one of them is null
     * @throws ClassCastException if the PropertyValue are of a different type
     */
    public static int compare(
            PropertyValue one,
            PropertyValue two )
        throws
            ClassCastException
    {
        if( one == null ) {
            if( two == null ) {
                return 0;
            } else {
                return -2;
            }
        } else {
            if( two == null ) {
                return +2;
            } else {
                int temp = one.compareTo( two );
                if( temp > 0 ) {
                    return +1;
                } else if( temp == 0 ) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    }
}
