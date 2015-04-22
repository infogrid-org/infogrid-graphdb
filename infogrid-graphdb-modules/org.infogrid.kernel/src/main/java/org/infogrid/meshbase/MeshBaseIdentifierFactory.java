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
import org.infogrid.util.IdentifierFactory;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;

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
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier fromExternalForm(
            String raw )
        throws
            ParseException;

    /**
     * Recreate a MeshBaseIdentifier from an external form. Be lenient about syntax and
     * attempt to interpret what the user meant when entering an invalid or incomplete
     * raw String.
     *
     * @param raw the external form
     * @return the created MeshBaseIdentifier
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshBaseIdentifier guessFromExternalForm(
            String raw )
        throws
            ParseException;

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
            ParseException;
}
