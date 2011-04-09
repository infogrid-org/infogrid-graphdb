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

import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * Stringifies the domain of an EnumeratedValue's EnumeratedDataType, highlighting the EnumeratedValue.
 */
public class EnumeratedValueChoiceHtmlStringifier
        extends
            AbstractStringifier<EnumeratedValue>
{
    /**
     * Factory method.
     *
     * @param tag the HTML tag to use
     * @return the created EnumeratedValueStringifier
     */
    public static EnumeratedValueChoiceHtmlStringifier create(
            String tag )
    {
        return new EnumeratedValueChoiceHtmlStringifier( tag );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param tag the HTML tag to use
     */
    protected EnumeratedValueChoiceHtmlStringifier(
            String tag )
    {
        theTag = tag;
    }

    /**
     * Obtain the HTML tag.
     *
     * @return the HTML tag
     */
    public String getTag()
    {
        return theTag;
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
        EnumeratedValue [] values = arg.getDataType().getDomain();
        StringBuilder      ret    = new StringBuilder();

        for( int i=0 ; i<values.length ; ++i ) {
            String name        = values[i].value();
            String userVisible = values[i].getUserVisibleName().value();

            ret.append( "<" ).append( theTag );
            ret.append( " value=\"" ).append( name );

            if( arg == values[i] ) {
                ret.append( "\" selected=\"selected" );
            }
            ret.append( "\">" );
            ret.append( userVisible );
            ret.append( "</" ).append( theTag ).append( ">\n" );

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
        if( arg instanceof EnumeratedValue ) {
            return format( soFar, (EnumeratedValue) arg, pars );
        } else if( arg instanceof EnumeratedDataType ) {
            return format( soFar, ((EnumeratedDataType) arg).getDefaultValue(), pars );
        } else {
            throw new ClassCastException( "Cannot cast this object to a supported type: " + arg );
        }
    }

    /**
     * The HTML tag to use.
     */
    protected String theTag;
}
