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
import org.infogrid.model.primitives.CurrencyValue;
import org.infogrid.util.OneElementIterator;
import org.infogrid.util.ZeroElementCursorIterator;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierParseException;
import org.infogrid.util.text.StringifierParsingChoice;
import org.infogrid.util.text.StringifierUnformatFactory;
import org.infogrid.util.text.StringifierValueParsingChoice;

/**
 * Stringifies CurrencyValues.
 */
public class CurrencyValueStringifier
        extends
            AbstractStringifier<CurrencyValue>
{
    /**
     * Factory method.
     *
     * @return the created EnumeratedValueStringifier
     */
    public static CurrencyValueStringifier create()
    {
        return new CurrencyValueStringifier();
    }

    /**
     * Private constructor for subclasses only, use factory method.
     */
    protected CurrencyValueStringifier()
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
            CurrencyValue                  arg,
            StringRepresentationParameters pars )
    {
        String ret = arg.value();

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
        return format( soFar, (CurrencyValue) arg, pars );
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
    public CurrencyValue unformat(
            String                     rawString,
            StringifierUnformatFactory factory )
        throws
            StringifierParseException
    {
        CurrencyValue ret;
        try {
            ret = CurrencyValue.parseCurrencyValue( rawString );

        } catch( IllegalArgumentException ex ) {
            throw new StringifierParseException( this, null, ex );
        }
        return ret;
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
    public Iterator<StringifierParsingChoice<CurrencyValue>> parsingChoiceIterator(
            final String               rawString,
            final int                  startIndex,
            final int                  endIndex,
            final int                  max,
            final boolean              matchAll,
            StringifierUnformatFactory factory )
    {
        if( matchAll ) {
            try {
                CurrencyValue found = unformat( rawString, factory );
                return OneElementIterator.<StringifierParsingChoice<CurrencyValue>>create(
                        new StringifierValueParsingChoice<CurrencyValue>( startIndex, endIndex, found ));
            } catch( StringifierParseException ex ) {
                return ZeroElementCursorIterator.create();
            }
        } else {
            return new MyIterator( rawString, startIndex, endIndex, max, factory );
        }
    }

    /**
     * Iterator implementation for this CurrencyValueStringifier.
     */
    static class MyIterator
            implements
                Iterator<StringifierParsingChoice<CurrencyValue>>
    {
        /**
         * Constructor.
         */
        public MyIterator(
                String                     rawString,
                int                        startIndex,
                int                        endIndex,
                int                        max,
                StringifierUnformatFactory factory )
        {
            theRawString = rawString;
            theStartIndex = startIndex;
            theEndIndex   = endIndex;
            theMax        = max;
            theFactory    = factory;

            theCounter      = 0;
            theCurrentIndex = theStartIndex;

            goNext();
        }

        /**
         * Does the iterator have a next element?
         *
         * @return true if the iterator has a next element
         */
        public boolean hasNext()
        {
            if( theCounter >= theMax ) {
                return false;
            }
            if( theCurrentIndex >= theEndIndex ) {
                return false;
            }
            return true;
        }

        /**
         * Obtain the next element in the iteration.
         *
         * @return the next element
         */
        public StringifierParsingChoice<CurrencyValue> next()
        {
            StringifierParsingChoice<CurrencyValue> ret;
            if( theNext != null ) {
                ret = theNext;
                goNext();
            } else {
                ret = null;
            }
            return ret;
        }

        /**
         * Throws UnsupportedOperationException at all times.
         *
         * @throws UnsupportedOperationException at all times.
         */
        public void remove()
            throws
                UnsupportedOperationException
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Iterate one step forward.
         */
        protected void goNext()
        {
            if( theCounter >= theMax ) {
                return;
            }
            // try until one works
            while( theCurrentIndex < theEndIndex ) {
                try {
                    CurrencyValue found = CurrencyValue.parseCurrencyValue( theRawString.substring( theStartIndex, theCurrentIndex ) );
                    theNext             = new StringifierValueParsingChoice<CurrencyValue>( theStartIndex, theEndIndex, found );
                    ++theCounter;
                    return;

                } catch( IllegalArgumentException ex ) {
                    ++theCurrentIndex;
                    theNext = null;
                }
            }
        }

        /**
         * The factory.
         */
        protected StringifierUnformatFactory theFactory;

        /**
         * The String to be parsed.
         */
        protected String theRawString;

        /**
         * WHere to start parsing.
         */
        protected int theStartIndex;

        /**
         * Where to end parsing.
         */
        protected int theEndIndex;

        /**
         * Number of elements returned so far.
         */
        protected int theCounter;

        /**
         * Maximum number of elements to return.
         */
        protected int theMax;

        /**
         * The current parsing location.
         */
        protected int theCurrentIndex;

        /**
         * The next element to return.
         */
        protected StringifierParsingChoice<CurrencyValue> theNext;
    }
}
