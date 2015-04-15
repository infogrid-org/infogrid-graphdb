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
import org.infogrid.mesh.MeshObjectIdentifier;
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
    public void checkPermittedSetTimeAutoDeletes(
            RoleType   type,
            MeshObject obj,
            long       newValue,
            MeshObject caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
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
    public void checkPermittedBless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
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
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedUnbless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }

    /**
     * Check whether the given caller is allowed to bless the given start MeshObject with the additional
     * given EntityTypes, in the opinion of another Role (represented as the given RoleType and the
     * given otherSide) in which the start MeshObject participates in.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the start MeshObject
     * @param types the EntityTypes with which to additionally bless the start MeshObject
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedIncrementalBless(
            RoleType             type,
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }

    /**
     * Check whether the given caller is allowed to unbless the given start MeshObject from the
     * given EntityTypes, in the opinion of another Role (represented as the given RoleType and the
     * given otherSide) in which the start MeshObject participates in.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the start MeshObject
     * @param types the EntityTypes from which to unbless the start MeshObject
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedIncrementalUnbless(
            RoleType             type,
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }

    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given
     * start MeshObject to a given newDestination MeshObject with the given additional newTypes,
     * in the opinion of another Role (represented as the given RoleType and the given
     * otherSide).
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the start MeshObject
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param newTypes the new RoleTypes with which to bless the relationship
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedIncrementalBless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          newTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }

    /**
     * Check wether the given caller is allowed to unbless an existing relationship from a given
     * start MeshObject to a given newDestination MeshObject from the given oldTypes,
     * in the opinion of another Role (represented as the given RoleType and the given
     * otherSide).
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the start MeshObject
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param oldTypes the old RoleTypes from which to unbless the relationship
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedIncrementalUnbless(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          oldTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }

    /**
     * Check whether the given caller is allowed to traverse a given RoleType from a given
     * start MeshObject to a given destination MeshObject.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param type the RoleType
     * @param start the start MeshObject
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */    
    public void checkPermittedTraversal(
            RoleType             type,
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
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
    public void checkPermittedAddAsEquivalent(
            RoleType             type,
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
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
    public void checkPermittedRemoveAsEquivalent(
            RoleType      type,
            MeshObject    obj,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        // noop, but you can override
    }
}
