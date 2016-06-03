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

package org.infogrid.mesh.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectAccessException;

/**
 * This Exception indicates that a caller had insufficient permissions to perform
 * the requested operation. Other callers may have different permissions.
 */
public class CallerHasInsufficientPermissionsException
        extends
            NotPermittedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param obj the MeshObject whose attempted modification triggered this Exception
     * @param caller identifies the caller that did not have sufficient permissions and triggered this Exception.
     *               This caller may be null, indicating an anonymous caller.
     */
    public CallerHasInsufficientPermissionsException(
            MeshObject obj,
            MeshObject caller )
    {
        super( obj );

        theCaller           = caller;
        theCallerIdentifier = caller != null ? caller.getIdentifier() : null;
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param objId the MeshObjectIdentifier of the to-be-created MeshObject
     * @param caller identifies the caller that did not have sufficient permissions and triggered this Exception.
     *               This caller may be null, indicating an anonymous caller.
     */
    public CallerHasInsufficientPermissionsException(
            MeshBase             mb,
            MeshObjectIdentifier objId,
            MeshObject           caller )
    {
        super( mb, mb.getIdentifier(), null, objId );

        theCaller           = caller;
        theCallerIdentifier = caller != null ? caller.getIdentifier() : null;
    }

    /**
     * Obtain the caller who did not have sufficient permissions.
     *
     * @return the caller
     * @throws MeshObjectAccessException thrown if something went wrong accessing the MeshObject
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getCaller()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theCallerIdentifier == null ) {
            return null;
        }
        if( theCaller == null ) {
            theCaller = resolve( theCallerIdentifier );
        }
        return theCaller;
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object [] { theMeshObjectIdentifier, theCallerIdentifier };
    }

    /**
     * The caller that did not have sufficient permissions and triggered this Exception.
     */
    protected transient MeshObject theCaller;

    /**
     * Identifier of the caller that did not have sufficent permissions.
     */
    protected MeshObjectIdentifier theCallerIdentifier;
}
