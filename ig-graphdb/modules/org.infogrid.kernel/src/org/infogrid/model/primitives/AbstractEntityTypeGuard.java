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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;

/**
 * Subclasses know how to check operations on MeshObjects. This abstract
 * class makes it easier to implement it.
 */
public abstract class AbstractEntityTypeGuard
        implements
            EntityTypeGuard
{
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
        // noop, but you can override
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
        // noop, but you can override
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
        // noop, but you can override
    }
}
