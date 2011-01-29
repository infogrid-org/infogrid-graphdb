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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.security.aclbased.utils;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotRelatedException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.ArrayHelper;

/**
 * Utilities to check access rights. This class cannot be instantiated.
 */
public abstract class AclUtils
{
    /**
     * Private constructor to keep this abstract.
     */
    private AclUtils()
    {}
    
    /**
     * Evaluate whether a MeshObject has access of a certain type to another MeshObject.
     *
     * @param accessor the MeshObject wanting to access the accessed, if any
     * @param roleType the RoleType that indicates the type of access
     * @param accessed the MeshObject to be accessed
     * @return true if accessor has the type of access to accessed
     */
    public static boolean hasAccess(
            MeshObject accessor,
            RoleType   roleType,
            MeshObject accessed )
    {
        // let's hope this implementation is reasonably efficient
        MeshObject protectionDomain = accessed.traverse(
                AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getDestination(),
                false ).getSingleMember();
            // it's important that we don't consider equivalents here

        if( protectionDomain == null ) {
            // anybody can do whatever
            return true;
        }
        if( accessor == null ) {
            // anonymous caller with ProtectionDomain: deny
            return false;
        }
        try {
            RoleType [] found = protectionDomain.getRoleTypes( accessor );
            if( ArrayHelper.isIn( roleType, found, false )) {
                return true;
            } else {
                return false;
            }
        } catch( NotRelatedException ex ) {
            return false;
        }
    }

    /**
     * Evaluate whether a MeshObject is the owner of another MeshObject.
     *
     * @param potentialOwner the potential owner of the potentialOwned
     * @param potentialOwned the potentially owned by the potentialOwner
     * @return true if potentialOwner is an actual owner of potentialOwned
     */
    public static boolean isOwner(
            MeshObject potentialOwner,
            MeshObject potentialOwned )
    {
        MeshObjectSet owners = potentialOwned.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), false );
            // it's important that we don't consider equivalents here
        
        if( owners.contains( potentialOwner )) {
            return true;
        } else {
            return false;
        }
    }
}
