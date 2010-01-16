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

import java.util.Iterator;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.text.StringStringifier;
import org.infogrid.util.text.StringifierParseException;
import org.infogrid.util.text.StringifierParsingChoice;
import org.infogrid.util.text.StringifierUnformatFactory;

/**
 * A Stringifier to stringify Strings into Java syntax. The reverse is currently NOT supported.
 * (FIXME. But: beware of code injection attacks)
 */
public class JavaStringStringifier
        extends
            StringStringifier
{
    /**
     * Factory method.
     *
     * @return the created JavaStringStringifier
     */
    public static JavaStringStringifier create()
    {
        return new JavaStringStringifier();
    }

    /**
     * Private constructor for subclasses only, use factory method.
     */
    protected JavaStringStringifier()
    {
        // no op
    }

    /**
     * Overridable method to possibly escape a String first.
     *
     * @param s the String to be escaped
     * @return the escaped String
     */
    @Override
    protected String escape(
            String s )
    {
        StringBuilder sb = new StringBuilder( s.length() * 5 / 4 ); // fudge
        sb.append(  "\"" );
        StringValue.encodeAsJavaString( s, sb );
        sb.append(  "\"" );
        return sb.toString();
    }

    /**
     * Overridable method to possibly unescape a String first.
     *
     * @param s the String to be unescaped
     * @return the unescaped String
     */
     @Override
     protected String unescape(
            String s )
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Parse out the Object in rawString that were inserted using this Stringifier.
     *
     * @param rawString the String to parse
     * @return the found Object
     * @throws StringifierParseException thrown if a parsing problem occurred
     */
    @Override
    public String unformat(
            String                     rawString,
            StringifierUnformatFactory factory )
        throws
            StringifierParseException
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Obtain an iterator that iterates through all the choices that exist for this Stringifier to
     * parse the String. The iterator returns zero elements if the String could not be parsed
     * by this Stringifier.
     *
     * @param rawString the String to parse
     * @param startIndex the position at which to parse rawString
     * @param endIndex the position at which to end parsing rawString
     * @param max the maximum number of choices to be returned by the Iterator.
     * @param matchAll if true, only return those matches that match the entire String from startIndex to endIndex.
     *                 If false, return other matches that only match the beginning of the String.
     * @return the Iterator
     */
    @Override
    public Iterator<StringifierParsingChoice<String>> parsingChoiceIterator(
            String                     rawString,
            int                        startIndex,
            int                        endIndex,
            int                        max,
            boolean                    matchAll,
            StringifierUnformatFactory factory )
    {
        throw new UnsupportedOperationException();
    }
}