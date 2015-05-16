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

package org.infogrid.model.primitives.text;

import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * A Stringifier to stringify PropertyValues into Java syntax. The reverse is currently NOT supported.
 * (FIXME. But: beware of code injection attacks)
 */
public class JavaPropertyValueStringifier
        extends
            AbstractStringifier<PropertyValue>
{
    /**
     * Factory method.
     *
     * @return the created JavaPropertyValueStringifier
     */
    public static JavaPropertyValueStringifier create()
    {
        return new JavaPropertyValueStringifier();
    }

    /**
     * Private constructor for subclasses only, use factory method.
     */
    protected JavaPropertyValueStringifier()
    {
        // no op
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     */
    public String format(
            String                         soFar,
            PropertyValue                  arg,
            StringRepresentationParameters pars )
    {
        String ret = arg.getJavaConstructorString( "loader", "type" );
        return ret;
    }

    /**
     * Format an Object using this Stringifier. This may be null.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     * @throws ClassCastException thrown if this Stringifier could not format the provided Object
     *         because the provided Object was not of a type supported by this Stringifier
     */
    public String attemptFormat(
            String                         soFar,
            Object                         arg,
            StringRepresentationParameters pars )
        throws
            ClassCastException
    {
        if( arg == null ) {
            return "null";
        } else {
            return format( soFar, (PropertyValue) arg, pars );
        }
    }
}
