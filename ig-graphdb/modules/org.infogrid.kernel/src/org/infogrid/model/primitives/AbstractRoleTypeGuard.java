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

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;

/**
 * Subclasses know how to check operations on instances of RoleType. This abstract
 * class makes it easier to implement it.
 */
public abstract class AbstractRoleTypeGuard
        implements
            RoleTypeGuard
{
    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with a given new RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the MeshObject from which the relationship starts
     * @param destination the MeshObject to which the relationship leads
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBless(
            RoleType      type,
            MeshObject    start,
            MeshObject    destination,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }
    
    /**
     * Check whether the given caller is allowed to unbless an existing relationship from a given start
     * MeshObject to a given destination MeshObject from a given RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the MeshObject from which the relationship starts
     * @param destination the MeshObject to which the relationship leads
     * @param caller the MeshObject representing the caller
     * @param duringDelete if true, this is part of a delete operation
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedUnbless(
            RoleType      type,
            MeshObject    start,
            MeshObject    destination,
            MeshObject    caller,
            boolean       duringDelete )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }
}
