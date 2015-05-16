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

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * This Exception is thrown if two MeshObject are supposed to become equivalent, but
 * are equivalent already.
 */
public class EquivalentAlreadyException
        extends
            IllegalOperationTypeException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Construct one.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param meshObject the first MeshObject that was equivalent already, if available
     * @param meshObjectIdentifier the MeshObjectIdentifier for the first MeshObject that was equivalent already
     * @param other the second MeshObject that was equivalent already, if available
     * @param otherIdentifier the MeshObjectIdentifier for the second MeshObject that was equivalent already
     */
    public EquivalentAlreadyException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           meshObject,
            MeshObjectIdentifier meshObjectIdentifier,
            MeshObject           other,
            MeshObjectIdentifier otherIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, meshObject, meshObjectIdentifier );
        
        theOther                = other;
        theOtherIdentifier      = otherIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param meshObject the first MeshObject that was equivalent already, if available
     * @param other the second MeshObject that was equivalent already, if available
     */
    public EquivalentAlreadyException(
            MeshObject           meshObject,
            MeshObject           other )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier(),
                other,
                other.getIdentifier() );
    }

    /**
     * Obtain the MeshObject that was equivalent already.
     * 
     * @return the other MeshObject
     * @throws MeshObjectAccessException thrown if the MeshObject could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getOtherMeshObject()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theOther == null ) {
            theOther = resolve( theOtherIdentifier );
        }
        return theOther;
    }

    /**
     * Obtain the MeshObjectIdentifier of the MeshObject that was equivalent already.
     *
     * @return the MeshObjectIdentifier
     */
    public MeshObjectIdentifier getOtherMeshObjectIdentifier()
    {
        return theOtherIdentifier;
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
                    "theOther",
                    "theOtherIdentifier",
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theOther,
                    theOtherIdentifier
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theOtherIdentifier };
    }

    /**
     * The second MeshObject that was equivalent already.
     */
    protected transient MeshObject theOther;

    /**
     * The MeshObjectIdentifier of the second MeshObject that was equivalent already.
     */
    protected MeshObjectIdentifier theOtherIdentifier;
}

