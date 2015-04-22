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

package org.infogrid.meshbase;

import java.text.ParseException;
import org.infogrid.util.InvalidObjectNumberFoundParseException;
import org.infogrid.util.InvalidObjectTypeFoundParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParseException;

/**
 * The default implementation of MeshBaseIdentifierFactory.
 */
public class DefaultMeshBaseIdentifierFactory
        implements
            MeshBaseIdentifierFactory
{
    /**
     * Factory method.
     *
     * @return the created DefaultMeshBaseIdentifierFactory
     */
    public static DefaultMeshBaseIdentifierFactory create()
    {
        DefaultMeshBaseIdentifierFactory ret = new DefaultMeshBaseIdentifierFactory();
        return ret;
    }

    /**
     * Constructor for subclasses only, use factory method.
     */
    protected DefaultMeshBaseIdentifierFactory()
    {
        // nothing right now
    }

    /**
     * Recreate a MeshBaseIdentifier from an external form.
     *
     * @param raw the external form
     * @return the created MeshBaseIdentifier
     * @throws StringRepresentationParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier fromExternalForm(
            String raw )
        throws
            StringRepresentationParseException
    {
        MeshBaseIdentifier ret = new MeshBaseIdentifier( this, raw, raw );
        return ret;
    }

    /**
     * Recreate a MeshBaseIdentifier from an external form. Be lenient about syntax and
     * attempt to interpret what the user meant when entering an invalid or incomplete
     * raw String.
     *
     * @param raw the external form
     * @return the created MeshBaseIdentifier
     * @throws StringRepresentationParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier guessFromExternalForm(
            String raw )
        throws
            StringRepresentationParseException
    {
        // On this level, we treat all Strings as uninterpreted. Thus this matches to fromExternalForm.
        return fromExternalForm( raw );
    }

    /**
     * Convert this StringRepresentation back to a MeshBaseIdentifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String to parse
     * @return the created MeshBaseIdentifier
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s )
        throws
            ParseException
    {
        try {
            Object [] found = representation.parseEntry( MeshBaseIdentifier.class, StringRepresentation.DEFAULT_ENTRY, pars, s, this );

            MeshBaseIdentifier ret;
            switch( found.length ) {
                case 1:
                    ret = (MeshBaseIdentifier) found[0];
                    break;

                default:
                    throw new InvalidObjectNumberFoundParseException( s, 1, found );
            }

            return ret;

        // pass-through ParseException

        } catch( ClassCastException ex ) {
            throw new InvalidObjectTypeFoundParseException( s, MeshBaseIdentifier.class, null, ex );
        }
    }
}
