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
 * RoleTypeGuard that guarts the MeshObject_HasOwner_MeshObject RelationshipType.
 */
public class MeshObject_HasOwner_MeshObject_SourceGuard
        extends
            PermitAllRoleTypeGuard
{
    private static final Log log = Log.getLogInstance( MeshObject_HasOwner_MeshObject_SourceGuard.class );

    /**
     * Check whether the given caller is allowed to change the time of auto-delete property
     * on a given MeshObject.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param obj the MeshObject whose auto-delete property shall be changed
     * @param newValue the new value of the property
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedSetTimeAutoDeletes(
            RoleType   type,
            MeshObject obj,
            long       newValue,
            MeshObject caller )
        throws
            NotPermittedException
    {
        myCheck( obj, caller );
    }

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
        myCheck( start, caller );
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
        myCheck( start, caller );
    }

    /**
     * Check whether the given caller is allowed to make one and two members of the same
     * equivalence set.
     *
     * @param type the RoleType
     * @param one the first MeshObject
     * @param twoIdentifier identifier of the second MeshObject
     * @param two the second MeshObject, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedAddAsEquivalent(
            RoleType             type,
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        myCheck( one, caller );

        if( two != null ) {
            myCheck( two, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to remove the MeshObject from its
     * equivalence set.
     * 
     * @param type the RoleType
     * @param obj the MeshObject
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedRemoveAsEquivalent(
            RoleType      type,
            MeshObject    obj,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        myCheck( obj, caller );
        // FIXME? Do we need to check for permission of the other members of the equivalence set, too?
    }

    /**
     * The code for blessing and unblessing is the same.
     *
     * @param start the MeshObject from which the relationship starts
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    protected void myCheck(
            MeshObject           start,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        try {
            MeshObjectSet startOwners = start.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );

            if( startOwners.isEmpty() ) { // FIXME? This means anybody can grab an orphaned object
                return;
            }
            if( caller != null && startOwners.contains( caller )) {
                return;
            }

        } catch( Exception ex ) {
            log.error( ex );
        }
        throw new CallerHasInsufficientPermissionsException( start, caller );
    }
}
