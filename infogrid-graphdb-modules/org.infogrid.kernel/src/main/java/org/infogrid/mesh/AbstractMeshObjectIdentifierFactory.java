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

package org.infogrid.mesh;

import java.text.ParseException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.util.UniqueStringGenerator;
import org.infogrid.util.logging.Log;

/**
 * Factors out common features of MeshObjectIdentifierFactories.
 */
public abstract class AbstractMeshObjectIdentifierFactory
        implements
            MeshObjectIdentifierFactory
{
    private static final Log log = Log.getLogInstance( AbstractMeshObjectIdentifierFactory.class ); // our own, private logger

    /**
     * Constructor.
     *
     * @param generator the UniqueStringGenerator to use
     */
    protected AbstractMeshObjectIdentifierFactory(
            UniqueStringGenerator generator )
    {
        theUniqueStringGenerator = generator;
    }

    /**
     * Determine the MeshBase to which this MeshObjectIdentifierFactory belongs.
     *
     * @return the MeshBase
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

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
            IllegalStateException
    {
        if( mb == null ) {
            throw new NullPointerException();
        }
        if( theMeshBase != null ) {
            throw new IllegalStateException();
        }
        theMeshBase = mb;
    }

    /**
     * Create a unique MeshObjectIdentifier.
     *
     * @return the unique MeshObjectIdentifier
     */
    public MeshObjectIdentifier createMeshObjectIdentifier()
    {
        String id = theUniqueStringGenerator.createUniqueToken();

        try {
            MeshObjectIdentifier ret = fromExternalForm( id.toString() );

            return ret;

        } catch( ParseException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Create a unique MeshObjectIdentifier of a certain length for a MeshObject that can be used to create a MeshObject
     * with the associated MeshBaseLifecycleManager.
     *
     * @param length the desired length of the MeshObjectIdentifier
     * @return the created Identifier
     */
    public MeshObjectIdentifier createMeshObjectIdentifier(
            int length )
    {
        String id = theUniqueStringGenerator.createUniqueToken( length );

        try {
            MeshObjectIdentifier ret = fromExternalForm( id.toString() );

            return ret;

        } catch( ParseException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * The MeshBase to which this factory belongs.
     */
    protected MeshBase theMeshBase;

    /**
     * The generator of unique Strings.
     */
    protected UniqueStringGenerator theUniqueStringGenerator;
}
