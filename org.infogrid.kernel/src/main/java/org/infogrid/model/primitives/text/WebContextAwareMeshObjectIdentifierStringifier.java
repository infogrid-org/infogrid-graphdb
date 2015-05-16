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

import java.text.ParseException;
import java.util.Iterator;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.a.AMeshObject;
import org.infogrid.mesh.text.MeshStringRepresentationParameters;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.util.Identifier;
import org.infogrid.util.OneElementIterator;
import org.infogrid.util.ZeroElementCursorIterator;
import org.infogrid.util.text.IdentifierStringifier;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierParseException;
import org.infogrid.util.text.StringifierParsingChoice;
import org.infogrid.util.text.StringifierUnformatFactory;
import org.infogrid.util.text.StringifierValueParsingChoice;

/**
 * A Stringifier that knows how to format Identifiers correctly as part of a URL
 * that includes a web context path already.
 */
public class WebContextAwareMeshObjectIdentifierStringifier
        extends
            IdentifierStringifier
{
    /**
     * Factory method.
     *
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     * @return the created WebContextAwareMeshObjectIdentifierStringifier
     */
    public static WebContextAwareMeshObjectIdentifierStringifier create(
            boolean assembleAsPartOfLongerId )
    {
        return new WebContextAwareMeshObjectIdentifierStringifier( true, null, null, assembleAsPartOfLongerId );
    }

    /**
     * Factory method.
     *
     * @param processColloquial if true, process the colloquial parameter (if given). If false, leave identifier as is.
     * @param prefix the prefix for the identifier, if any
     * @param postfix the postfix for the identifier, if any
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     * @return the created WebContextAwareMeshObjectIdentifierStringifier
     */
    public static WebContextAwareMeshObjectIdentifierStringifier create(
            boolean processColloquial,
            String  prefix,
            String  postfix,
            boolean assembleAsPartOfLongerId )
    {
        return new WebContextAwareMeshObjectIdentifierStringifier(
                processColloquial,
                prefix,
                postfix,
                assembleAsPartOfLongerId );
    }

    /**
     * Constructor. Use factory method.
     *
     * @param processColloquial if true, process the colloquial parameter (if given). If false, leave identifier as is.
     * @param prefix the prefix for the identifier, if any
     * @param postfix the postfix for the identifier, if any
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     */
    protected WebContextAwareMeshObjectIdentifierStringifier(
            boolean processColloquial,
            String  prefix,
            String  postfix,
            boolean assembleAsPartOfLongerId )
    {
        super( processColloquial, prefix, postfix );

        theAssembleAsPartOfLongerId = assembleAsPartOfLongerId;
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     */
    @Override
    public String format(
            String                         soFar,
            Identifier                     arg,
            StringRepresentationParameters pars )
    {
        if( arg == null ) {
            return formatToNull( soFar, pars );
        }

        MeshObjectIdentifier realIdentifier  = (MeshObjectIdentifier) arg;
        MeshBase             defaultMeshBase = null;
        String               contextPath     = "";

        if( pars != null ) {
            defaultMeshBase = (MeshBase) pars.get( MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY );

            if( defaultMeshBase != null ) {
                contextPath = defaultMeshBase.getIdentifier().toExternalForm();
            } else {
                contextPath = (String) pars.get( StringRepresentationParameters.WEB_ABSOLUTE_CONTEXT_KEY ); // which may or may not be null
                if( contextPath != null && !contextPath.endsWith( "/" )) {
                    contextPath += "/";
                }
            }
        }

        String ext = realIdentifier.toLocalExternalForm( contextPath, theAssembleAsPartOfLongerId );

        if( ext.length() == 0 ) {
            ext = AMeshObject.HOME_OBJECT_STRING; // FIXME, this looks like a hack
        }

        ext = potentiallyShorten( ext, pars );
        ext = escape( ext );

        String ret = processPrefixPostfix( ext );
        return ret;
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
        if( theAssembleAsPartOfLongerId ) {
            String ret = s.replace( "#", "%23" );
            return ret;
        } else {
            return s;
        }
    }

    /**
     * Parse out the Object in rawString that were inserted using this Stringifier.
     * This default implementation simply throws an UnsupportedOperationException.
     *
     * @param rawString the String to parse
     * @param factory the factory needed to create the parsed values, if any
     * @return the found Object
     * @throws StringifierParseException thrown if a parsing problem occurred
     */
    @Override
    public Identifier unformat(
            String                     rawString,
            StringifierUnformatFactory factory )
        throws
            StringifierParseException
    {
        MeshObjectIdentifierFactory realFactory   = (MeshObjectIdentifierFactory) factory;
        String                      contextString = realFactory.getMeshBase().getIdentifier().toExternalForm();
        String                      realRawString;

        if( rawString.startsWith( contextString )) {
            realRawString = rawString.substring( contextString.length() );
        } else {
            realRawString = rawString;
        }

        Identifier found;
        try {
            if( rawString.length() == 0 ) {
                found = null;
            } else if( realRawString.equals( AMeshObject.HOME_OBJECT_STRING )) {
                found = realFactory.getHomeMeshObjectIdentifier(); // FIXME this looks like a hack
            } else if( theProcessColloquial ) {
                found = realFactory.guessFromExternalForm( realRawString );
            } else {
                found = realFactory.fromExternalForm( realRawString );
            }
        } catch( ParseException ex ) {
            throw new StringifierParseException( this, rawString, ex );
        }
        return found;
    }


    /**
     * Obtain an iterator that iterates through all the choices that exist for this Stringifier to
     * parse the String. The iterator returns zero elements if the String could not be parsed
     * by this Stringifier.
     * This default implementation simply throws an UnsupportedOperationException.
     *
     * @param rawString the String to parse
     * @param startIndex the position at which to parse rawString
     * @param endIndex the position at which to end parsing rawString
     * @param max the maximum number of choices to be returned by the Iterator.
     * @param matchAll if true, only return those matches that match the entire String from startIndex to endIndex.
     *                 If false, return other matches that only match the beginning of the String.
     * @param factory the factory needed to create the parsed values, if any
     * @return the Iterator
     */
    @Override
    public Iterator<StringifierParsingChoice<Identifier>> parsingChoiceIterator(
            String                     rawString,
            int                        startIndex,
            int                        endIndex,
            int                        max,
            boolean                    matchAll,
            StringifierUnformatFactory factory )
    {
        MeshObjectIdentifierFactory realFactory   = (MeshObjectIdentifierFactory) factory;
        String                      contextString = realFactory.getMeshBase().getIdentifier().toExternalForm();
        String                      realRawString;
        boolean                     startsWithContext = false;

        if( rawString.startsWith( contextString )) {
            realRawString     = rawString.substring( contextString.length() );
            startsWithContext = true;
        } else {
            realRawString = rawString;
        }

        try {
            Identifier found;
            if( realRawString.length() == 0 ) {
                if( startsWithContext ) {
                    found = realFactory.getHomeMeshObjectIdentifier();
                } else {
                    found = null;
                }
            } else if( realRawString.equals( AMeshObject.HOME_OBJECT_STRING )) {
                found = realFactory.getHomeMeshObjectIdentifier(); // FIXME this looks like a hack
            } else if( theProcessColloquial ) {
                found = realFactory.guessFromExternalForm( realRawString );
            } else {
                found = realFactory.fromExternalForm( realRawString );
            }

            if( found != null ) {
                return OneElementIterator.<StringifierParsingChoice<Identifier>>create(
                        new StringifierValueParsingChoice<Identifier>( startIndex, endIndex, found ));
            } else {
                return ZeroElementCursorIterator.create();
            }

        } catch( ParseException ex ) {
            return ZeroElementCursorIterator.create();
        }
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
        if( theAssembleAsPartOfLongerId ) {
            String ret = s.replace( "%23", "#" );
            return ret;
        } else {
            return s;
        }
    }

    /**
     * If true, escape.
     */
    protected boolean theAssembleAsPartOfLongerId;
}
