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
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGuard;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;
import org.infogrid.util.logging.Log;

/**
 * This PropertyTypeGuard applies the rules of the SecurityModel to Property access.
 */
public class DefaultPropertyTypeGuard
        implements
            PropertyTypeGuard
{
    private static final Log log = Log.getLogInstance( DefaultPropertyTypeGuard.class );

    /**
     * Check whether the given caller is allowed to set a given MeshObject's
     * given property to a given PropertyValue.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject whose property is supposed to be set
     * @param type identifies the property of the MeshObject
     * @param newValue the new PropertyValue for the MeshObject
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedSetProperty(
            MeshObject    subject,
            PropertyType  type,
            PropertyValue newValue,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        try {
            AclBasedSecurityGuardUtils.checkPermittedOperation(
                    subject,
                    caller,
                    AclBasedSecuritySubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getDestination() );

        } catch( NotPermittedException ex ) {
            throw ex; // gotta let this one through

        } catch( Exception ex ) {
            log.error( ex );
            throw new CallerHasInsufficientPermissionsException( subject, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to read a given MeshObject's
     * given property to a given PropertyValue.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject whose property is supposed to be read
     * @param type identifies the property of the MeshObject
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedGetProperty(
            MeshObject    subject,
            PropertyType  type,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        try {
            AclBasedSecurityGuardUtils.checkPermittedOperation(
                    subject,
                    caller,
                    AclBasedSecuritySubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN.getDestination() );

        } catch( NotPermittedException ex ) {
            throw ex; // gotta let this one through

        } catch( Exception ex ) {
            log.error( ex );
            throw new CallerHasInsufficientPermissionsException( subject, caller );
        }
    }
}
