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
 * <p>This exception is thrown if an operation is attempted on a MeshObject
 * that the caller was not permitted to perform. More specific subclasses
 * are defined in the <code>org.infogrid.mesh.security</code> package.</p>
 */
public abstract class NotPermittedException
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
    protected NotPermittedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier )
    {
        super( mb, originatingMeshBaseIdentifier );

        theMeshObject           = obj;
        theMeshObjectIdentifier = identifier;
    }
    
    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject on which the illegal operation was attempted
     * @param cause the underlying cause
     */
    protected NotPermittedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            Throwable            cause )
    {
        super( mb, originatingMeshBaseIdentifier, cause );
        
        theMeshObject           = obj;
        theMeshObjectIdentifier = identifier;
    }
    
    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted
     */
    protected NotPermittedException(
            MeshObject obj )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier() );
    }
    
    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted
     * @param cause the underlying cause
     */
    protected NotPermittedException(
            MeshObject obj,
            Throwable  cause )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                cause );
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
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theOriginatingMeshBaseIdentifier, theMeshObjectIdentifier };
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
