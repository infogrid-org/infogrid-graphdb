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

package org.infogrid.modelbase.m;

import org.infogrid.modelbase.MeshTypeIdentifierFactory;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParseException;

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
    public MMeshTypeIdentifier fromExternalForm(
            String raw )
    {
        return MMeshTypeIdentifier.create( raw );
    }

    /**
     * Convert this StringRepresentation back to an Identifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param s the String to parse
     * @return the created MMeshTypeIdentifier
     * @throws StringRepresentationParseException thrown if the String could not be successfully parsed
     */
    public MMeshTypeIdentifier fromStringRepresentation(
            StringRepresentation representation,
            String               s )
        throws
            StringRepresentationParseException
    {
        try {
            Object [] found = representation.parseEntry( MMeshTypeIdentifier.class, MMeshTypeIdentifier.DEFAULT_ENTRY, s, this );

            MMeshTypeIdentifier ret;
            switch( found.length ) {
                case 2:
                    ret = fromExternalForm( (String) found[1] );
                    break;

                default:
                    throw new StringRepresentationParseException( s, null, null );
            }

            return ret;

        // pass-through StringRepresentationParseException

        } catch( ClassCastException ex ) {
            throw new StringRepresentationParseException( s, null, ex );
        }
    }
}
