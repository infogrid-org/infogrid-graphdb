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
import org.infogrid.util.StringTooShortParseException;
import org.infogrid.util.InvalidCharacterParseException;
import org.infogrid.util.ResourceHelper;
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
        checkLocalId( raw );

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
        String [] entriesToTry1 = {
                DefaultAMeshObjectIdentifier.DEFAULT_MESH_BASE_HOME_ENTRY,
                DefaultAMeshObjectIdentifier.NON_DEFAULT_MESH_BASE_HOME_ENTRY };

        Throwable firstException = null;

        for( String entry : entriesToTry1 ) {

            try {
                representation.parseEntry( DefaultAMeshObjectIdentifier.class, entry, s, this );
                return fromExternalForm( "" );

            } catch( ParseException ex ) {
                // that wasn't it ...
                if( firstException == null ) {
                    firstException = ex;
                }
            }
        }

        String [] entriesToTry2 = {
                DefaultAMeshObjectIdentifier.DEFAULT_MESH_BASE_ENTRY,
                DefaultAMeshObjectIdentifier.NON_DEFAULT_MESH_BASE_ENTRY };

        for( String entry : entriesToTry2 ) {
            Object [] found = null;
            try {
                found = representation.parseEntry( DefaultAMeshObjectIdentifier.class, entry, s, this );

            } catch( ParseException ex ) {
                // that wasn't it ...
                if( firstException == null ) {
                    firstException = ex;
                }

            } catch( ClassCastException ex ) {
                // that wasn't it ...
                if( firstException == null ) {
                    firstException = ex;
                }
            }

            if( found != null && found.length == 1 ) {
                DefaultAMeshObjectIdentifier ret = fromExternalForm( (String) found[0] ); // may throw
                return ret;
            }

        }
        throw new StringRepresentationParseException( s, null, firstException );
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
     * Check whether the proposed localId for a MeshObjectIdentifier is valid.
     * Subclasses can override this.
     *
     * @param localId the local ID
     * @throws ParseException thrown if the localId is not valid
     */
    protected void checkLocalId(
            String localId )
        throws
            ParseException
    {
        if( localId == null || localId.length() == 0 ) {
            return;
        }
        for( int i=0 ; i<DISALLOWED_LOCAL_ID_STRINGS.length ; ++i ) {
            if( localId.indexOf( DISALLOWED_LOCAL_ID_STRINGS[i] ) >= 0 ) {
                throw new InvalidCharacterParseException( localId, null, localId.indexOf( DISALLOWED_LOCAL_ID_STRINGS[i] ), DISALLOWED_LOCAL_ID_STRINGS[i] );
            }
        }
        if( MINIMUM_LOCAL_ID_LENGTH > 0 && localId.length() < MINIMUM_LOCAL_ID_LENGTH ) {
            throw new StringTooShortParseException( localId, null, MINIMUM_LOCAL_ID_LENGTH );
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
    public final static int MINIMUM_LOCAL_ID_LENGTH = theResourceHelper.getResourceIntegerOrDefault( "MinimumLocalIdLength", 4 );

    /**
     * The disallowed character strings in a local id.
     */
    public final static String [] DISALLOWED_LOCAL_ID_STRINGS = theResourceHelper.getResourceStringArrayOrDefault(
            "DisallowedLocalIdString",
            new String [] { "." } );
}
