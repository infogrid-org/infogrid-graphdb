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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.Role;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.logging.Log;

/**
 * An implementation of AccessManager that checks the Guards defined in the model.
 */
public class GuardCheckingAccessManager
        implements
            AccessManager
{
    private static final Log log = Log.getLogInstance( GuardCheckingAccessManager.class ); // our own, private logger

    /**
     * Factory method.
     * 
     * @return the created GuardCheckingAccessManager
     */
    public static GuardCheckingAccessManager create()
    {
        return new GuardCheckingAccessManager();
    }

    /**
     * Constructor, for subclasses only.
     */
    protected GuardCheckingAccessManager()
    {
        // no op
    }

    /**
     * Assign the second MeshObject to be the owner of the first MeshObject. This
     * must only be called if the current Thread has an open Transaction.
     *
     * @param toBeOwned the MeshObject to be owned by the new owner
     * @param newOwner the MeshObject that is the new owner.
     * @throws TransactionException thrown if this is invoked outside of proper transaction boundaries
     */
    public void assignOwner(
            MeshObject toBeOwned,
            MeshObject newOwner )
        throws
            TransactionException
    {
        // no op
    }

    /**
     * Check whether it is permitted to semantically create a MeshObject with the provided
     * MeshObjectIdentifier.
     *
     * @param identifier the MeshObjectIdentifier
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedCreate(
            MeshObjectIdentifier identifier )
        throws
            NotPermittedException
    {
        // no op
    }

    /**
     * Check whether it is permitted to set a MeshObject's timeExpires to the given value.
     *
     * @param obj the MeshObject
     * @param newValue the proposed new value for timeExpires
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetTimeExpires(
            MeshObject obj,
            long       newValue )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            for( Role current : obj.getRoles( false ) ) {
                current.getRoleType().checkPermittedSetTimeExpires(
                        obj,
                        newValue,
                        caller );
            }
        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to set a MeshObject's given property to the given
     * value.
     *
     * @param obj the MeshObject
     * @param thePropertyType the PropertyType identifing the property to be modified
     * @param newValue the proposed new value for the property
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetProperty(
            MeshObject    obj,
            PropertyType  thePropertyType,
            PropertyValue newValue )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            thePropertyType.checkPermittedSetProperty(
                    obj,
                    newValue,
                    caller );

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to obtain a MeshObject's given property.
     *
     * @param obj the MeshObject
     * @param thePropertyType the PropertyType identifing the property to be read
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedGetProperty(
            MeshObject   obj,
            PropertyType thePropertyType )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            thePropertyType.checkPermittedGetProperty(
                    obj,
                    caller );

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to determine whether or not a MeshObject is blessed with
     * the given type.
     *
     * @param obj the MeshObject
     * @param type the EntityType whose blessing we wish to check
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBlessedBy(
            MeshObject obj,
            EntityType type )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            type.checkPermittedBlessedBy(
                    obj,
                    caller );

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to bless a MeshObject with the given EntityTypes.
     *
     * @param obj the MeshObject
     * @param types the EntityTypes with which to bless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            MeshObject    obj,
            EntityType [] types )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller      = ThreadIdentityManager.getCaller();
            MeshBase   theMeshBase = obj.getMeshBase();

            // we ask the new EntityTypes, and we ask the existing RoleTypes

            for( EntityType current : types ) {
                current.checkPermittedBless(
                        obj,
                        caller );
            }

            Role [] roles = obj.getRoles( false );
            for( Role current : roles ) {
                RoleType   roleType = current.getRoleType();
                MeshObject neighbor = theMeshBase.findMeshObjectByIdentifier( current.getNeighborIdentifier() );

                roleType.checkPermittedIncrementalBless(
                        obj,
                        types,
                        current.getNeighborIdentifier(),
                        neighbor,
                        caller );
            }
        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to unbless a MeshObject from the given EntityTypes.
     *
     * @param obj the MeshObject
     * @param types the EntityTypes from which to unbless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            MeshObject    obj,
            EntityType [] types )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller      = ThreadIdentityManager.getCaller();
            MeshBase   theMeshBase = obj.getMeshBase();

            for( EntityType current : types ) {
                current.checkPermittedUnbless( obj, caller );
            }

            Role [] roles = obj.getRoles( false );
            for( Role current : roles ) {
                RoleType   roleType = current.getRoleType();
                MeshObject neighbor = theMeshBase.findMeshObjectByIdentifier( current.getNeighborIdentifier() );

                roleType.checkPermittedIncrementalUnbless(
                        obj,
                        types,
                        current.getNeighborIdentifier(),
                        neighbor,
                        caller );
            }
        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to bless the relationship to the neighbor with the
     * provided RoleTypes.
     *
     * @param obj the MeshObject
     * @param thisEnds the RoleTypes to bless the relationship with
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            MeshObject           obj,
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            for( RoleType current : thisEnds ) {
                current.checkPermittedBless(
                        obj,
                        neighborIdentifier,
                        neighbor,
                        caller );

                if( neighbor != null ) {
                    RoleType inverseRoleType = current.getInverseRoleType();
                    inverseRoleType.checkPermittedBless(
                            neighbor,
                            obj.getIdentifier(),
                            obj,
                            caller );
                }
            }

            Role [] roles = obj.getRoles( false );
            for( Role current : roles ) {
                RoleType roleType = current.getRoleType();

                MeshObjectIdentifier neighborWithOpinionIdentifier = current.getNeighborIdentifier();
                MeshObject           neighborWithOpinion           = current.getNeighborIfAvailable();

                roleType.checkPermittedIncrementalBless(
                        obj,
                        neighborIdentifier,
                        neighbor,
                        thisEnds,
                        neighborWithOpinionIdentifier,
                        neighborWithOpinion,
                        caller );
            }

            if( neighbor != null ) {
                Role [] neighborRoles = neighbor.getRoles( false );
                RoleType [] otherEnds = new RoleType[ thisEnds.length ];
                for( int i=0 ; i<thisEnds.length ; ++i ) {
                    otherEnds[i] = thisEnds[i].getInverseRoleType();
                }
                for( Role current : neighborRoles ) {
                    RoleType roleType = current.getRoleType();

                    MeshObjectIdentifier neighborWithOpinionIdentifier = current.getNeighborIdentifier();
                    MeshObject           neighborWithOpinion           = current.getNeighborIfAvailable();

                    roleType.checkPermittedIncrementalBless(
                            neighbor,
                            obj.getIdentifier(),
                            obj,
                            otherEnds,
                            neighborWithOpinionIdentifier,
                            neighborWithOpinion,
                            caller );
                }
            }
        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to unbless the relationship to the neighbor from the
     * provided RoleTypes.
     *
     * @param obj the MeshObject
     * @param thisEnds the RoleTypes to unbless the relationship from
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            MeshObject           obj,
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            for( RoleType current : thisEnds ) {
                current.checkPermittedUnbless(
                        obj,
                        neighborIdentifier,
                        neighbor,
                        caller );

                if( neighbor != null ) {
                    RoleType inverse = current.getInverseRoleType();
                    inverse.checkPermittedUnbless(
                            neighbor,
                            obj.getIdentifier(),
                            obj,
                            caller );
                }
            }

            Role [] roles = obj.getRoles( false );
            for( Role current : roles ) {
                RoleType roleType = current.getRoleType();

                MeshObjectIdentifier neighborWithOpinionIdentifier = current.getNeighborIdentifier();
                MeshObject           neighborWithOpinion           = current.getNeighborIfAvailable();

                roleType.checkPermittedIncrementalUnbless(
                        obj,
                        neighborIdentifier,
                        neighbor,
                        thisEnds,
                        neighborWithOpinionIdentifier,
                        neighborWithOpinion,
                        caller );
            }

            if( neighbor != null ) {
                Role [] neighborRoles = neighbor.getRoles( false );
                RoleType [] otherEnds = new RoleType[ thisEnds.length ];
                for( int i=0 ; i<thisEnds.length ; ++i ) {
                    otherEnds[i] = thisEnds[i].getInverseRoleType();
                }
                for( Role current : neighborRoles ) {
                    RoleType roleType = current.getRoleType();

                    MeshObjectIdentifier neighborWithOpinionIdentifier = current.getNeighborIdentifier();
                    MeshObject           neighborWithOpinion           = current.getNeighborIfAvailable();

                    roleType.checkPermittedIncrementalUnbless(
                            neighbor,
                            obj.getIdentifier(),
                            obj,
                            otherEnds,
                            neighborWithOpinionIdentifier,
                            neighborWithOpinion,
                            caller );
                }
            }

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to traverse the given RoleType from this MeshObject to the
     * given MeshObject.
     *
     * @param obj the MeshObject
     * @param toTraverse the RoleType to traverse
     * @param neighborIdentifier identifier of the neighbor to which the traversal leads
     * @param neighbor neighbor to which this traversal leads
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedTraversal(
            MeshObject           obj,
            RoleType             toTraverse,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            MeshObject caller = ThreadIdentityManager.getCaller();

            toTraverse.checkPermittedTraversal( obj, neighborIdentifier, neighbor, caller );

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Check whether it is permitted to make one MeshObject equivalent to another.
     *
     * @param one the first MeshObject
     * @param twoIdentifier identifier of the second MeshObject
     * @param two the second MeshObject, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedAddAsEquivalent(
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two )
        throws
            NotPermittedException
    {
        // no op for now (FIXME?)
    }

    /**
     * Check whether it is permitted to remove a MeshObject from the equivalence set
     * it is currently a member of.
     *
     * @param obj the MeshObject to remove
     * @param roleTypesToAsk the RoleTypes to ask
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedRemoveAsEquivalent(
            MeshObject  obj,
            RoleType [] roleTypesToAsk )
        throws
            NotPermittedException
    {
        // no op for now
    }

   /**
     * Check whether it is permitted to delete this MeshObject. This checks both whether the
     * MeshObject itself may be deleted, and whether the relationships it participates in may
     * be deleted (which in turn depends on whether the relationships may be unblessed).
     *
     * @param obj the MeshObject
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedDelete(
            MeshObject obj )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();

            EntityType [] allTypes = obj.getTypes();

            checkPermittedUnbless( obj, allTypes );

            for( Role current : obj.getRoles( false ) ) {
                RoleType roleType = current.getRoleType();

                checkPermittedUnbless(
                        obj,
                        new RoleType[] { roleType },
                        current.getNeighborIdentifier(),
                        current.getNeighborIfAvailable() );

                try {
                    MeshObject currentNeighbor = current.getNeighbor();
                    RoleType   inverseRoleType = roleType.getInverseRoleType();

                    checkPermittedUnbless(
                            currentNeighbor,
                            new RoleType[] { inverseRoleType },
                            obj.getIdentifier(),
                            obj );

                } catch( MeshObjectAccessException ex ) {
                    log.info( ex );
                }
            }
        } finally {
            ThreadIdentityManager.sudone();
        }
    }
}
