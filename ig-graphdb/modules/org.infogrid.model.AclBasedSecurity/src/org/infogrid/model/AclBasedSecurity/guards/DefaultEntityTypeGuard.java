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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.AclBasedSecurity.guards;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.CallerHasInsufficientPermissionsException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EntityTypeGuard;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;
import org.infogrid.util.logging.Log;

/**
 * This EntityTypeGuard applies the rules of the SecurityModel to Entity operations.
 */
public class DefaultEntityTypeGuard
        implements
            EntityTypeGuard
{
    private static final Log log = Log.getLogInstance( DefaultEntityTypeGuard.class );

    /**
     * Check whether the given caller is allowed to bless an existing MeshObject
     * with a given new MeshType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the EntityType
     * @param obj the MeshObject to be blessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBless(
            EntityType    type,
            MeshObject    obj,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        try {
            AclBasedSecurityGuardUtils.checkPermittedOperation(
                    obj,
                    caller,
                    AclBasedSecuritySubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getDestination() );

        } catch( NotPermittedException ex ) {
            throw ex; // gotta let this one through

        } catch( Exception ex ) {
            log.error( ex );
            throw new CallerHasInsufficientPermissionsException( obj, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to unbless an existing MeshObject
     * from a given new MeshType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the EntityType
     * @param obj the MeshObject to be unblessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedUnbless(
            EntityType    type,
            MeshObject    obj,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        try {
            AclBasedSecurityGuardUtils.checkPermittedOperation(
                    obj,
                    caller,
                    AclBasedSecuritySubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getDestination() );

        } catch( NotPermittedException ex ) {
            throw ex; // gotta let this one through

        } catch( Exception ex ) {
            log.error( ex );
            throw new CallerHasInsufficientPermissionsException( obj, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to determine whether or not a given MeshObject
     * is blessed by a given EntityType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the EntityType
     * @param obj the MeshObject to be checked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBlessedBy(
            EntityType    type,
            MeshObject    obj,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        try {
            AclBasedSecurityGuardUtils.checkPermittedOperation(
                    obj,
                    caller,
                    AclBasedSecuritySubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN.getDestination() );

        } catch( NotPermittedException ex ) {
            throw ex; // gotta let this one through

        } catch( Exception ex ) {
            log.error( ex );
            throw new CallerHasInsufficientPermissionsException( obj, caller );
        }
    }
}
