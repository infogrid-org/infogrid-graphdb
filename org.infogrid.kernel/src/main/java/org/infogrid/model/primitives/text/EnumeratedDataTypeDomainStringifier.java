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

import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.Stringifier;
import org.infogrid.util.text.StringifierException;

/**
 * Stringifies the domain of EnumeratedDataTypes.
 */
public class EnumeratedDataTypeDomainStringifier
        extends
            AbstractStringifier<EnumeratedDataType>
{
    /**
     * Factory method.
     *
     * @param delegate the Stringifier responsible for stringifying the contained EnumeratedValues
     * @return the created EnumeratedValueStringifier
     */
    public static EnumeratedDataTypeDomainStringifier create(
            Stringifier<EnumeratedValue> delegate )
    {
        return new EnumeratedDataTypeDomainStringifier( delegate, null, null, null );
    }

    /**
     * Factory method.
     *
     * @param middle the string to insert in the middle
     * @param delegate the Stringifier responsible for stringifying the contained EnumeratedValues
     * @return the created EnumeratedValueStringifier
     */
    public static EnumeratedDataTypeDomainStringifier create(
            Stringifier<EnumeratedValue> delegate,
            String                       middle )
    {
        return new EnumeratedDataTypeDomainStringifier( delegate, null, middle, null );
    }

    /**
     * Factory method.
     *
     * @param start the string to insert at the beginning
     * @param middle the string to insert in the middle
     * @param end the string to append at the end
     * @param delegate the Stringifier responsible for stringifying the contained EnumeratedValues
     * @return the created EnumeratedValueStringifier
     */
    public static EnumeratedDataTypeDomainStringifier create(
            Stringifier<EnumeratedValue> delegate,
            String                       start,
            String                       middle,
            String                       end )
    {
        return new EnumeratedDataTypeDomainStringifier( delegate, start, middle, end );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param delegate the Stringifier responsible for stringifying the contained EnumeratedValues
     * @param start the string to insert at the beginning
     * @param middle the string to insert in the middle
     * @param end the string to append at the end
     */
    protected EnumeratedDataTypeDomainStringifier(
            Stringifier<EnumeratedValue> delegate,
            String                       start,
            String                       middle,
            String                       end )
    {
        theDelegate = delegate;
        theStart    = start;
        theMiddle   = middle;
        theEnd      = end;
    }

    /**
     * Obtain the start String, if any.
     *
     * @return the start String, if any
     */
    public String getStart()
    {
        return theStart;
    }

    /**
     * Obtain the middle String, if any.
     *
     * @return the middle String, if any
     */
    public String getMiddle()
    {
        return theMiddle;
    }

    /**
     * Obtain the end String, if any.
     *
     * @return the end String, if any
     */
    public String getEnd()
    {
        return theEnd;
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return the formatted String
     */
    public String format(
            String                         soFar,
            EnumeratedDataType             arg,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        EnumeratedValue [] values = arg.getDomain();
        StringBuilder      ret    = new StringBuilder();
        String             sep    = theStart;

        for( int i=0 ; i<values.length ; ++i ) {
            if( sep != null ) {
                ret.append( sep );
            }
            String childInput = theDelegate.format( soFar + ret.toString(), values[i], pars ); // presumably shorter, but we don't know
            if( childInput != null ) {
                ret.append( childInput );
            }
            sep = theMiddle;
        }
        if( theEnd != null ) {
            ret.append( theEnd );
        }
        return potentiallyShorten( ret.toString(), pars );
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
        return format( soFar, (EnumeratedDataType) arg, pars );
    }

    /**
     * The underlying Stringifier responsible for stringifying the contained EnumeratedValues.
     */
    protected Stringifier<EnumeratedValue> theDelegate;

    /**
     * The String to insert at the beginning. May be null.
     */
    protected String theStart;

    /**
     * The String to insert when joining two elements. May be null.
     */
    protected String theMiddle;

    /**
     * The String to append at the end. May be null.
     */
    protected String theEnd;
}
