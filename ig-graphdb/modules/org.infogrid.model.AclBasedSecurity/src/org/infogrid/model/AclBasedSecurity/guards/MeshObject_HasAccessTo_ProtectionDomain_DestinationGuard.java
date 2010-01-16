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
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.security.CallerHasInsufficientPermissionsException;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;
import org.infogrid.model.primitives.PermitAllRoleTypeGuard;
import org.infogrid.util.logging.Log;

/**
 * The RoleTypeGuard that guards the MeshObject_HasAccessTo_ProtectionDomain RelationshipType.
 */
public class MeshObject_HasAccessTo_ProtectionDomain_DestinationGuard
        extends
            PermitAllRoleTypeGuard
{
    private static final Log log = Log.getLogInstance( MeshObject_HasAccessTo_ProtectionDomain_DestinationGuard.class );

    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with a given new RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedBless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        myCheck( type, start, neighborIdentifier, neighbor, caller );
    }
    
    /**
     * Check whether the given caller is allowed to unbless an existing relationship from a given start
     * MeshObject to a given destination MeshObject from a given RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedUnbless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        myCheck( type, start, neighborIdentifier, neighbor, caller );
    }

    /**
     * The code for blessing and unblessing is the same.
     *
     * @param type the RoleType
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    protected void myCheck(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        try {
            MeshObjectSet startOwners = start.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );

            if( caller != null && startOwners.contains( caller )) {
                return;
            }

        } catch( Exception ex ) {
            log.error( ex );
        }
        throw new CallerHasInsufficientPermissionsException( start, caller );
    }
}
