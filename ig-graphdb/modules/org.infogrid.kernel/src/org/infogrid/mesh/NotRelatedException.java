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
 * This Exception is thrown if two MeshObjects are to become unrelated, but are not
 * currently related.
 */
public class NotRelatedException
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
     * @param meshObject the first MeshObject that was unrelated, if available
     * @param meshObjectIdentifier the MeshObjectIdentifier for the first MeshObject that was unrelated
     * @param notNeighbor the MeshObject at the other end of the non-existing relationship, if available
     * @param notNeighborIdentifier the MeshObjectIdentifier for the MeshObject at the other end of the non-existing relationship, if available
     */
    public NotRelatedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           meshObject,
            MeshObjectIdentifier meshObjectIdentifier,
            MeshObject           notNeighbor,
            MeshObjectIdentifier notNeighborIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, meshObject, meshObjectIdentifier );
        
        theNotNeighbor                = notNeighbor;
        theNotNeighborIdentifier      = notNeighborIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param meshObject the first MeshObject that was unrelated, if available
     * @param notNeighbor the MeshObject at the other end of the non-existing relationship, if available
     */
    public NotRelatedException(
            MeshObject           meshObject,
            MeshObject           notNeighbor )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier(),
                notNeighbor,
                notNeighbor.getIdentifier() );
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param meshObject the first MeshObject that was unrelated, if available
     * @param notNeighborIdentifier identifier of the MeshObject at the other end of the non-existing relationship, if available
     */
    public NotRelatedException(
            MeshObject           meshObject,
            MeshObjectIdentifier notNeighborIdentifier )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier(),
                null,
                notNeighborIdentifier );
    }

    /**
     * Obtain the MeshObject at the other end of the relationship that did not exist.
     * 
     * @return the other MeshObject
     * @throws MeshObjectAccessException thrown if the MeshObject could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getNotNeighbor()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theNotNeighbor == null ) {
            theNotNeighbor = resolve( theNotNeighborIdentifier );
        }
        return theNotNeighbor;
    }

    /**
     * Obtain the MeshObjectIdentifier of the MeshObject at the other end of the relationship that did not exist.
     *
     * @return the MeshObjectIdentifier
     */
    public MeshObjectIdentifier getNotNeighborIdentifier()
    {
        return theNotNeighborIdentifier;
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
                    "theNotNeighbor",
                    "theNotNeighborIdentifier",
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theNotNeighbor,
                    theNotNeighborIdentifier
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theNotNeighborIdentifier };
    }

    /**
     * The MeshObject at the other end of the relationship for which we discovered a violation.
     */
    protected transient MeshObject theNotNeighbor;

    /**
     * The MeshObjectIdentifier of the MeshObject at the other end of the relationship for which we discovered a violation.
     */
    protected MeshObjectIdentifier theNotNeighborIdentifier;
}

