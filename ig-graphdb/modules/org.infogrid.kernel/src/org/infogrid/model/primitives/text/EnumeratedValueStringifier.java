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

package org.infogrid.model.primitives.text;

import java.util.Iterator;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.util.OneElementIterator;
import org.infogrid.util.ZeroElementCursorIterator;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierParseException;
import org.infogrid.util.text.StringifierParsingChoice;
import org.infogrid.util.text.StringifierUnformatFactory;
import org.infogrid.util.text.StringifierValueParsingChoice;

/**
 * Stringifies EnumeratedValues.
 */
public class EnumeratedValueStringifier
        extends
            AbstractStringifier<EnumeratedValue>
{
    /**
     * Factory method.
     *
     * @return the created EnumeratedValueStringifier
     * @param emitUserVisible if false, emit computable name; if true, emit user-visible name.
     */
    public static EnumeratedValueStringifier create(
            boolean emitUserVisible )
    {
        return new EnumeratedValueStringifier( emitUserVisible );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param emitUserVisible if false, emit computable name; if true, emit user-visible name.
     */
    protected EnumeratedValueStringifier(
            boolean emitUserVisible )
    {
        theEmitUserVisible = emitUserVisible;
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
            EnumeratedValue                arg,
            StringRepresentationParameters pars )
    {
        String ret;
        if( theEmitUserVisible ) {
            ret = arg.getUserVisibleName().value();
        } else {
            ret = arg.value();
        }
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
        return format( soFar, (EnumeratedValue) arg, pars );
    }

    /**
     * Parse out the Object in rawString that were inserted using this Stringifier.
     *
     * @param rawString the String to parse
     * @param factory the factory needed to create the parsed values, if any
     * @return the found Object
     * @throws StringifierParseException thrown if a parsing problem occurred
     */
    @Override
    public EnumeratedValue unformat(
            String                     rawString,
            StringifierUnformatFactory factory )
        throws
            StringifierParseException
    {
        EnumeratedDataType type = (EnumeratedDataType) factory;

        for( EnumeratedValue candidate : type.getDomain() ) {
            String candidateName = candidate.value();

            if( candidateName.equals( rawString )) {
                return candidate;
            }
        }
        return null;
    }

    /**
     * Obtain an iterator that iterates through all the choices that exist for this Stringifier to
     * parse the String.
     *
     * @param rawString the String to parse
     * @param startIndex the position at which to parse rawString
     * @param endIndex the position at which to end parsing rawString
     * @param max the maximum number of choices returned by the Iterator.
     * @param matchAll if true, only return those matches that match the entire String from startIndex to endIndex.
     *                 If false, return other matches that only match the beginning of the String.
     * @param factory the factory needed to create the parsed values, if any
     * @return the Iterator
     */
    @Override
    public Iterator<StringifierParsingChoice<EnumeratedValue>> parsingChoiceIterator(
            final String               rawString,
            final int                  startIndex,
            final int                  endIndex,
            final int                  max,
            final boolean              matchAll,
            StringifierUnformatFactory factory )
    {
        EnumeratedDataType type = (EnumeratedDataType) factory;

        for( EnumeratedValue candidate : type.getDomain() ) {
            String candidateName = candidate.value();

            if(    candidateName.length() == endIndex - startIndex
                && candidateName.regionMatches( 0, rawString, startIndex, endIndex - startIndex ))
            {
                return OneElementIterator.<StringifierParsingChoice<EnumeratedValue>>create(
                        new StringifierValueParsingChoice<EnumeratedValue>( startIndex, endIndex, candidate ));
            }
        }
        return ZeroElementCursorIterator.create();
    }

    /**
     * If false, emit computable name. If true, emit user-visible name.
     */
    protected boolean theEmitUserVisible;
}
