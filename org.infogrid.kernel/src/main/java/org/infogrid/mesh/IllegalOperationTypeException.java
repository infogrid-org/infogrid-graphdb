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

/**
  * This Exception is thrown when an operation is attempted
  * that is illegal on this MeshObject.
  */
public abstract class IllegalOperationTypeException
        extends
            MeshObjectGraphModificationException
{
    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject on which the illegal operation was attempted
     */
    protected IllegalOperationTypeException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier )
    {
        super( mb, originatingMeshBaseIdentifier );

        if( identifier == null ) {
            throw new IllegalArgumentException( "identifier must not be null" );
        }
        theMeshObject           = obj;
        theMeshObjectIdentifier = identifier;
    }

    /**
     * Obtain the MeshObject on which the illegal operation was attempted.
     * 
     * @return the MeshObject
     * @throws MeshObjectAccessException thrown if something went wrong accessing the MeshObject
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getMeshObject()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theMeshObject == null ) {
            theMeshObject = resolve( theMeshObjectIdentifier );
        }
        return theMeshObject;
    }

    /**
     * The MeshObject on which the illegal operation was attempted.
     */
    protected transient MeshObject theMeshObject;
    
    /**
     * The MeshObjectIdentifier of the MeshObject on which the illegal operation was attempted.
     */
    protected MeshObjectIdentifier theMeshObjectIdentifier;
}
