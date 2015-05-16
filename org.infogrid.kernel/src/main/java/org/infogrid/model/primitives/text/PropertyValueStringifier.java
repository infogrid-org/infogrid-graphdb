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
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * A Stringifier to stringify PropertyValues into Strings. The reverse is currently NOT supported.
 */
public class PropertyValueStringifier
        extends
            AbstractStringifier<PropertyValue>
{
    /**
     * Factory method.
     *
     * @param rep the StringRepresentation to use
     * @return the created PropertyValueStringifier
     */
    public static PropertyValueStringifier create(
            StringRepresentation rep )
    {
        return new PropertyValueStringifier( rep );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param rep the StringRepresentation to use
     */
    protected PropertyValueStringifier(
            StringRepresentation rep )
    {
        theStringRepresentation = rep;
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String format(
            String                         soFar,
            PropertyValue                  arg,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String ret = PropertyValue.toStringRepresentationOrNull(
                arg,
                theStringRepresentation,
                pars );
        return ret;
    }

    /**
     * Format an Object using this Stringifier. This may be null.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @throws ClassCastException thrown if this Stringifier could not format the provided Object
     *         because the provided Object was not of a type supported by this Stringifier
     */
    public String attemptFormat(
            String                         soFar,
            Object                         arg,
            StringRepresentationParameters pars )
        throws
            StringifierException,
            ClassCastException
    {
        if( arg == null ) {
            return "null";
        } else {
            return format( soFar, (PropertyValue) arg, pars );
        }
    }

    /**
     * The StringRepresentation to use. This recursion should be handled better. (FIXME)
     */
    protected StringRepresentation theStringRepresentation;
}
