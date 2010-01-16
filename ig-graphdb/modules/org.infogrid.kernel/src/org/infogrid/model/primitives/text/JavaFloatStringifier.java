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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.text;

import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * A Stringifier to stringify floats into Java syntax. The reverse is currently NOT supported.
 * (FIXME. But: beware of code injection attacks)
 */
public class JavaFloatStringifier
        extends
            AbstractStringifier<Number>
{
    /**
     * Factory method.
     *
     * @return the created JavaFloatStringifier
     */
    public static JavaFloatStringifier create()
    {
        return new JavaFloatStringifier();
    }

    /**
     * Private constructor for subclasses only, use factory method.
     */
    protected JavaFloatStringifier()
    {
        // no op
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
     * @return the formatted String
     */
    public String format(
            String                         soFar,
            Number                         arg,
            StringRepresentationParameters pars )
    {
        // ignore maxLength, it makes no sense here
        return String.valueOf( arg.floatValue() );
    }

    /**
     * Format an Object using this Stringifier. This may be null.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
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
        return format( soFar, (Number) arg, pars );
    }
}