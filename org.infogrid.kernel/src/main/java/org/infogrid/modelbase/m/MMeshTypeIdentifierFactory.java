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

package org.infogrid.modelbase.m;

import java.text.ParseException;
import org.infogrid.modelbase.MeshTypeIdentifierFactory;
import org.infogrid.util.InvalidObjectNumberFoundParseException;
import org.infogrid.util.InvalidObjectTypeFoundParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * Factory for creating MeshTypeIdentifiers appropriate for the MModelBase
 * implementation of ModelBase.
 */
public class MMeshTypeIdentifierFactory
        implements
            MeshTypeIdentifierFactory
{
    /**
     * Factory method.
     *
     * @return the created MMeshTypeIdentifierFactory
     */
    public static MMeshTypeIdentifierFactory create()
    {
        return new MMeshTypeIdentifierFactory();
    }

    /**
     * Constructor.
     */
    protected MMeshTypeIdentifierFactory()
    {
        // no op
    }
 
    /**
     * Create a MeshTypeIdentifier from an external form.
     *
     * @param raw the external form
     * @return the created MeshTypeIdentifier
     */
    @Override
    public MMeshTypeIdentifier fromExternalForm(
            String raw )
    {
        return MMeshTypeIdentifier.create( raw );
    }

    /**
     * Create a MeshTypeIdentifier from an external form. Be lenient about syntax and
     * attempt to interpret what the user meant when entering an invalid or incomplete
     * raw String.
     *
     * @param raw the external form
     * @return the Identifier
     */
    @Override
    public MMeshTypeIdentifier guessFromExternalForm(
            String raw )
    {
        return fromExternalForm( raw );
    }

    /**
     * Convert this StringRepresentation back to an Identifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String to parse
     * @return the created MMeshTypeIdentifier
     * @throws ParseException thrown if the String could not be successfully parsed
     */
    @Override
    public MMeshTypeIdentifier fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s )
        throws
            ParseException
    {
        try {
            Object [] found = representation.parseEntry( MMeshTypeIdentifier.class, MMeshTypeIdentifier.DEFAULT_ENTRY, pars, s, this );

            MMeshTypeIdentifier ret;
            switch( found.length ) {
                case 2:
                    ret = (MMeshTypeIdentifier) found[1];
                    break;

                default:
                    throw new InvalidObjectNumberFoundParseException( s, 2, found );
            }

            return ret;

        // pass-through StringRepresentationParseException

        } catch( ClassCastException ex ) {
            throw new InvalidObjectTypeFoundParseException( s, MMeshTypeIdentifier.class, null, ex );
        }
    }
}
