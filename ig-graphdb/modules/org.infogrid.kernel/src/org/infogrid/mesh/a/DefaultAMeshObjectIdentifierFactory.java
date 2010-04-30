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

package org.infogrid.mesh.a;

import java.text.ParseException;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.AbstractMeshObjectIdentifierFactory;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.StringTooShortParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParseException;

/**
 * Default implementation of MeshObjectIdentifierFactory for the A implementation.
 */
public class DefaultAMeshObjectIdentifierFactory
        extends
            AbstractMeshObjectIdentifierFactory
{
    /**
     * Factory method.
     *
     * @return the created DefaultAMeshObjectIdentifierFactory
     */
    public static DefaultAMeshObjectIdentifierFactory create()
    {
        DefaultAMeshObjectIdentifierFactory ret = new DefaultAMeshObjectIdentifierFactory();
        return ret;
    }

    /**
     * Constructor.
     */
    protected DefaultAMeshObjectIdentifierFactory()
    {
        // no op
    }

    /**
     * Create an identifier for a MeshObject at held locally at this MeshBase.
     *
     * @param raw the raw String
     * @throws ParseException thrown if a parsing error occurred
     */
    public DefaultAMeshObjectIdentifier fromExternalForm(
            String raw )
        throws
            ParseException
    {
        checkRawId( raw );

        return DefaultAMeshObjectIdentifier.create( this, raw, raw );
    }

    /**
     * Convert this StringRepresentation back to an Identifier.
     *
     * @param representation the StringRepresentation in which this String is represented
     * @param s the String to parse
     * @return the created MeshObjectIdentifier
     * @throws ParseException thrown if a parsing error occurred
     */
    public DefaultAMeshObjectIdentifier fromStringRepresentation(
            StringRepresentation representation,
            String               s )
        throws
            ParseException
    {
        Object [] found = representation.parseEntry( DefaultAMeshObjectIdentifier.class, StringRepresentation.DEFAULT_ENTRY, s, this );
        return (DefaultAMeshObjectIdentifier) found[0];
    }
    
    /**
     * Recreate a MeshObjectIdentifier from an external form. Be lenient about syntax and
     * attempt to interpret what the user meant when entering an invalid or incomplete
     * raw String.
     *
     * @param raw the external form
     * @return the created MeshObjectIdentifier
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshObjectIdentifier guessFromExternalForm(
            String raw )
        throws
            ParseException
    {
        // on this level, everything is opaque
        return DefaultAMeshObjectIdentifier.create( this, raw, raw );
    }

    /**
     * Determine the Identifier of the Home Object.
     *
     * @return the Identifier
     */
    public DefaultAMeshObjectIdentifier getHomeMeshObjectIdentifier()
    {
        return HOME_OBJECT;
    }

    /**
     * Check whether the proposed String for a MeshObjectIdentifier is valid.
     * Subclasses can override this.
     *
     * @param raw the String
     * @throws ParseException thrown if the String is not valid
     */
    protected void checkRawId(
            String raw )
        throws
            ParseException
    {
        if( raw == null || raw.length() == 0 ) {
            return;
        }

        if( MINIMUM_ID_LENGTH > 0 && raw.length() < MINIMUM_ID_LENGTH ) {
            throw new StringTooShortParseException( raw, null, MINIMUM_ID_LENGTH );
        }
    }

    /**
     * The Home Object's identifier. Subclass to avoid having to make the constructor public.
     */
    public final DefaultAMeshObjectIdentifier HOME_OBJECT = new DefaultAMeshObjectIdentifier( this, null, null ) {};

    /**
     * Our ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( DefaultAMeshObjectIdentifierFactory.class );

    /**
     * The minimum length for a local id.
     */
    public final static int MINIMUM_ID_LENGTH = theResourceHelper.getResourceIntegerOrDefault( "MinimumIdLength", 4 );

}
