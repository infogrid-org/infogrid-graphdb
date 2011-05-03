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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase;

import java.text.ParseException;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.util.IdentifierFactory;
import org.infogrid.util.text.StringRepresentation;

/**
 * Factory for MeshObjectIdentifiers.
 */
public interface MeshObjectIdentifierFactory
        extends
             IdentifierFactory
{
    /**
     * Determine the MeshBase to which this MeshObjectIdentifierFactory belongs.
     *
     * @return the MeshBase
     */
    public abstract MeshBase getMeshBase();

    /**
     * Set the MeshBase to which this MeshObjectIdentifierFactory belongs.
     * This is invoked by the MeshBase's constructor and does not need to invoked
     * by the application programmer. It can only be invoked once; subsequent
     * invocations throw an IllegalStateException.
     *
     * @param mb the MeshBase
     * @throws IllegalStateException thrown if this call is performed more than one on the same instance
     */
    public void setMeshBase(
            MeshBase mb )
        throws
            IllegalStateException;

    /**
     * Determine the MeshObjectIdentifier of the Home MeshObject of this MeshBase.
     *
     * @return the Identifier
     */
    public abstract MeshObjectIdentifier getHomeMeshObjectIdentifier();

    /**
     * Create a unique MeshObjectIdentifier for a MeshObject that can be used to create a MeshObject
     * with the associated MeshBaseLifecycleManager.
     *
     * @return the created Identifier
     */
    public abstract MeshObjectIdentifier createMeshObjectIdentifier();

    /**
     * Create a unique MeshObjectIdentifier of a certain length for a MeshObject that can be used to create a MeshObject
     * with the associated MeshBaseLifecycleManager.
     *
     * @param length the desired length of the MeshObjectIdentifier
     * @return the created Identifier
     */
    public abstract MeshObjectIdentifier createMeshObjectIdentifier(
            int length );

    /**
     * Recreate a MeshObjectIdentifier from an external form.
     *
     * @param raw the external form
     * @return the created MeshObjectIdentifier
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshObjectIdentifier fromExternalForm(
            String raw )
        throws
            ParseException;

    /**
     * Recreate a MeshObjectIdentifier from an external form. Be lenient about syntax and
     * attempt to interpret what the user meant when entering an invalid or incomplete
     * raw String.
     *
     * @param raw the external form
     * @return the created MeshObjectIdentifier
     * @throws ParseException thrown if the String could not be successfully parsed
     */
    public MeshObjectIdentifier guessFromExternalForm(
            String raw )
        throws
            ParseException;

    /**
     * Convert this StringRepresentation back to a MeshObjectIdentifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param s the String to parse
     * @return the created MeshObjectIdentifier
     * @throws ParseException thrown if the String could not be successfully parsed
     */
    public MeshObjectIdentifier fromStringRepresentation(
            StringRepresentation representation,
            String               s )
        throws
            ParseException;
}
