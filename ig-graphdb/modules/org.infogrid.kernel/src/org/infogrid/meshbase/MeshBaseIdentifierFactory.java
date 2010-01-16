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

package org.infogrid.meshbase;

import org.infogrid.util.IdentifierFactory;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParseException;

/**
 * Factory for MeshBaseIdentifiers.
 */
public interface MeshBaseIdentifierFactory
        extends
             IdentifierFactory
{
    /**
     * Recreate a MeshBaseIdentifier from an external form. Be strict about syntax.
     *
     * @param raw the external form
     * @return the created MeshBaseIdentifier
     * @throws StringRepresentationParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier fromExternalForm(
            String raw )
        throws
            StringRepresentationParseException;

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
            StringRepresentationParseException;

    /**
     * Convert this StringRepresentation back to a MeshBaseIdentifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param s the String to parse
     * @return the created MeshBaseIdentifier
     * @throws StringRepresentationParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier fromStringRepresentation(
            StringRepresentation representation,
            String               s )
        throws
            StringRepresentationParseException;
}
