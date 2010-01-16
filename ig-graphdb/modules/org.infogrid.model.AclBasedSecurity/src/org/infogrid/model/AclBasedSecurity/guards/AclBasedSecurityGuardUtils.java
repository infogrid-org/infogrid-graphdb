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

import org.infogrid.model.primitives.*;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.security.CallerHasInsufficientPermissionsException;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;

/**
 * Factors out common code of the various DefaultXXXGuard classes here.
 */
public abstract class AclBasedSecurityGuardUtils
{
    /**
     * Private constructor to prevent instantiation.
     */
    private AclBasedSecurityGuardUtils()
    {}

    /**
     * Helper method to determine whether the caller may update this MeshObject.
     *
     * @param obj the MeshObject to be updated
     * @param caller the MeshObject representing the caller
     * @param accessToRoleType the RoleType to traverse from the ProtectionDomain to the MeshObjects allowed
     *            to perform the operation
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    protected static void checkPermittedOperation(
            MeshObject obj,
            MeshObject caller,
            RoleType   accessToRoleType )
        throws
            NotPermittedException
    {
        MeshObject owner = obj.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember();
        if( owner == null ) {
            return; // anonymous object, anybody may update
        } else if( owner.equals( caller )) {
            return; // owner itself
        }

        // neither anonymous object, nor owner is calling
        
        MeshObject protectionDomain = obj.traverse( AclBasedSecuritySubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getDestination() ).getSingleMember();
        if( protectionDomain == null ) {
            // there is an owner, but no ProtectionDomain: not allowed
            throw new CallerHasInsufficientPermissionsException( obj, caller );

        } else {
            MeshObjectSet allowedUpdaters = protectionDomain.traverse( accessToRoleType );
            if( allowedUpdaters.contains( caller )) {
                return; // explicitly allowed
            } else {
                throw new CallerHasInsufficientPermissionsException( obj, caller );
            }
        }
    }
}
