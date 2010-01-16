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

package org.infogrid.modelbase;

import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.util.IdentifierFactory;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParseException;

/**
 * Factory for MeshTypeIdentifiers.
 */
public interface MeshTypeIdentifierFactory
        extends
            IdentifierFactory
{
    /**
     * Create a MeshTypeIdentifier from an external form.
     *
     * @param raw the external form
     * @return the created MeshTypeIdentifier
     */
    public MeshTypeIdentifier fromExternalForm(
            String raw );

    /**
     * Convert this StringRepresentation back to an Identifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param s the String to parse
     * @return the created MeshObjectIdentifier
     * @throws StringRepresentationParseException thrown if the String could not be successfully parsed
     */
    public MeshTypeIdentifier fromStringRepresentation(
            StringRepresentation representation,
            String               s )
        throws
            StringRepresentationParseException;
}
