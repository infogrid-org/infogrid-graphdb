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

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * This Exception is thrown if two MeshObject are supposed to become related, but
 * are related already.
 */
public class RelatedAlreadyException
        extends
            IllegalOperationTypeException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param meshObject the first MeshObject that was related already, if available
     * @param meshObjectIdentifier the MeshObjectIdentifier for the first MeshObject that was related already
     * @param neighbor the MeshObject at the other end of the already-existing relationship, if available
     * @param neighborIdentifier the MeshObjectIdentifier for the MeshObject at the other end of the already-existing relationship, if available
     */
    public RelatedAlreadyException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           meshObject,
            MeshObjectIdentifier meshObjectIdentifier,
            MeshObject           neighbor,
            MeshObjectIdentifier neighborIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, meshObject, meshObjectIdentifier );
        
        theNeighbor                = neighbor;
        theNeighborIdentifier      = neighborIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param meshObject the first MeshObject that was related already, if available
     * @param neighbor the MeshObject at the other end of the already-existing relationship, if available
     */
    public RelatedAlreadyException(
            MeshObject           meshObject,
            MeshObject           neighbor )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier(),
                neighbor,
                neighbor.getIdentifier() );
    }

    /**
     * More convenient simple constructor.
     *
     * @param meshObject the first MeshObject that was related already, if available
     * @param neighborIdentifier the MeshObject at the other end of the already-existing relationship, if available
     */
    public RelatedAlreadyException(
            MeshObject           meshObject,
            MeshObjectIdentifier neighborIdentifier )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier(),
                null,
                neighborIdentifier );
    }

    /**
     * Obtain the MeshObject at the other end of the relationship that did not exist.
     * 
     * @return the other MeshObject
     * @throws MeshObjectAccessException thrown if the MeshObject could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getNeighbor()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theNeighbor == null ) {
            theNeighbor = resolve( theNeighborIdentifier );
        }
        return theNeighbor;
    }

    /**
     * Obtain the MeshObjectIdentifier of the MeshObject at the other end of the relationship that did not exist.
     *
     * @return the MeshObjectIdentifier
     */
    public MeshObjectIdentifier getNeighborIdentifier()
    {
        return theNeighborIdentifier;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "theMeshObject",
                    "theMeshObjectIdentifier",
                    "theNeighbor",
                    "theNeighborIdentifier",
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theNeighbor,
                    theNeighborIdentifier
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theNeighborIdentifier };
    }

    /**
     * The MeshObject at the other end of the relationship for which we discovered a violation.
     */
    protected transient MeshObject theNeighbor;

    /**
     * The MeshObjectIdentifier of the MeshObject at the other end of the relationship for which we discovered a violation.
     */
    protected MeshObjectIdentifier theNeighborIdentifier;
}
