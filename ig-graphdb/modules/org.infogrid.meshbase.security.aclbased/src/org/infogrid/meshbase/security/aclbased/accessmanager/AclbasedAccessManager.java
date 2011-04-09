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

package org.infogrid.meshbase.security.aclbased.accessmanager;

import org.infogrid.mesh.EntityNotBlessedException;
import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.NotRelatedException;
import org.infogrid.mesh.RelatedAlreadyException;
import org.infogrid.mesh.security.CallerHasInsufficientPermissionsException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * A simple implementation of AccessManager using Access Control List. This simple
 * implementation should only be used in environments where arbitrary clients cannot
 * start calling arbitrary APIs.
 */
public class AclbasedAccessManager
        implements
            AccessManager
{
    private static final Log log = Log.getLogInstance( AclbasedAccessManager.class ); // our own, private logger
    
    /**
     * Factory method.
     *
     * @return the created SimpleAccessManager
     */
    public static AclbasedAccessManager create()
    {
        return new AclbasedAccessManager();
    }

    /**
     * Constructor for subclasses only, use factory method.
     */
    protected AclbasedAccessManager()
    {
        // noop
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
        try {
            ThreadIdentityManager.sudo();
            toBeOwned.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), newOwner );

        } catch( EntityNotBlessedException ex ) {
            log.error( ex );

        } catch( RelatedAlreadyException ex ) {
            log.error( ex );

        } catch( IsAbstractException ex ) {
            log.error( ex );

        } catch( NotPermittedException ex ) {
            log.error( ex );

        } finally {
            ThreadIdentityManager.sudone();
        }
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
        // always allowed -- FIXME?
    }

    /**
     * Helper method to determine whether a particular relationship exists.
     *
     * @param obj the MeshObject
     * @param rel the RoleType
     * @throws NotPermittedException thrown if it is not permitted
     */
    protected void checkRelationshipToCaller(
            MeshObject obj,
            RoleType   rel )
        throws
            NotPermittedException
    {
        if( ThreadIdentityManager.isSu() ) {
            return; // root many do anything
        }

        try {
            ThreadIdentityManager.sudo();
            MeshObject    caller = ThreadIdentityManager.getCaller();
            MeshObjectSet owners = obj.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), false );

            // if we don't have an owner, it's free for all
            if( owners.isEmpty() ) {
                return;
            }

            // if we don't have a protection domain, it's free for all
            MeshObject protectionDomain = obj.traverse( AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getDestination(), false ).getSingleMember();
            if( protectionDomain == null ) {
                return;
            }

            // with protection domain, but anonymous: sorry
            if( caller == null ) {
                throw new CallerHasInsufficientPermissionsException( obj, caller );
            }

            // owner always may
            if( owners.contains( caller )) {
                return;
            }

            // if caller has the right right in the protection domain
            if( caller.isRelated( rel, protectionDomain )) {
                return;
            }
            throw new CallerHasInsufficientPermissionsException( obj, caller );

        } finally {
            ThreadIdentityManager.sudone();
        }
    }

    /**
     * Helper method to determine whether a relationship may be blessed or unblessed.
     *
     * @param obj the MeshObject
     * @param thisEnds the RoleTypes to bless the relationship with or unbless from
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    protected void checkPermittedBlessUnblessRelationship(
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

        boolean case1 = ArrayHelper.isIn( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), thisEnds, false );
        if( !case1 ) {
            for( int i=0 ; i<thisEnds.length ; ++i ) {
                if( thisEnds[i].isSpecializationOfOrEquals( AclbasedSubjectArea.MESHOBJECT_HASACCESSTO_PROTECTIONDOMAIN.getDestination() )) {
                    case1 = true;
                    break;
                }
            }
        }
        if( case1 ) {
            MeshObject caller = ThreadIdentityManager.getCaller();

            try {
                ThreadIdentityManager.sudo();
                MeshObjectSet owners = obj.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
                if( !owners.isEmpty() && !owners.contains( caller )) {
                    throw new CallerHasInsufficientPermissionsException( obj, caller );
                }
            } finally {
                ThreadIdentityManager.sudone();
            }
        } else if( ArrayHelper.isIn( AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getSource(), thisEnds, false )) {
            MeshObject caller = ThreadIdentityManager.getCaller();

            try {
                ThreadIdentityManager.sudo();
                MeshObjectSet objOwners = obj.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
                if( !objOwners.isEmpty() && !objOwners.contains( caller )) {
                    throw new CallerHasInsufficientPermissionsException( obj, caller );
                }
                MeshObjectSet neighborOwners = neighbor.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
                if( !neighborOwners.isEmpty() && !neighborOwners.contains( caller )) {
                    throw new CallerHasInsufficientPermissionsException( neighbor, caller );
                }

            } finally {
                ThreadIdentityManager.sudone();
            }
        }
    }

    /**
     * Helper method to determine whether it is permitted to read a MeshObject.
     *
     * @param obj the MeshObject
     * @throws NotPermittedException thrown if it is not permitted
     */
    protected void checkPermittedRead(
            MeshObject obj )
        throws
            NotPermittedException
    {
        checkRelationshipToCaller( obj, AclbasedSubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN.getSource() );
    }

    /**
     * Helper method to determine whether it is permitted to update a MeshObject.
     *
     * @param obj the MeshObject
     * @throws NotPermittedException thrown if it is not permitted
     */
    protected void checkPermittedUpdate(
            MeshObject obj )
        throws
            NotPermittedException
    {
        checkRelationshipToCaller( obj, AclbasedSubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getSource() );
    }

    /**
     * Check whether it is permitted to set a MeshObject's auto-delete time to the given value.
     * Subclasses may override this.
     *
     * @param obj the MeshObject
     * @param newValue the proposed new value for the auto-delete time
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetTimeExpires(
            MeshObject obj,
            long       newValue )
        throws
            NotPermittedException
    {
        checkPermittedUpdate( obj );
    }

    /**
     * Check whether it is permitted to set a MeshObject's given property to the given
     * value. Subclasses may override this.
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
        checkPermittedUpdate( obj );
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
        checkPermittedRead( obj );
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
        checkPermittedRead( obj );
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
        checkPermittedUpdate( obj );
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
        checkPermittedUpdate( obj );
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
        checkPermittedBlessUnblessRelationship( obj, thisEnds, neighborIdentifier, neighbor );
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
        checkPermittedBlessUnblessRelationship( obj, thisEnds, neighborIdentifier, neighbor );
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
        checkPermittedRead( obj );
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
        checkPermittedUpdate( one );
        checkPermittedUpdate( two );
    }

    /**
     * Check whether it is permitted to remove a MeshObject from the equivalence set
     * it is currently a member of.
     * Subclasses may override this.
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
        checkPermittedUpdate( obj );
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
        checkRelationshipToCaller( obj, AclbasedSubjectArea.MESHOBJECT_HASDELETEACCESSTO_PROTECTIONDOMAIN.getSource() );

        EntityType [] types;

        try {
            ThreadIdentityManager.sudo();
            types = obj.getTypes();
        } finally {
            ThreadIdentityManager.sudone();
        }
        if( types.length > 0 ) {
            checkPermittedUnbless( obj, types );
        }

        for( MeshObject neighbor : obj.traverseToNeighborMeshObjects() ) {
            try {
                RoleType [] roleTypes = obj.getRoleTypes( neighbor );

                // strip out all that deal with this SubjectArea
                RoleType [] roleTypes2 = new RoleType[roleTypes.length];
                int index=0;
                for( int i=0 ; i<roleTypes.length ; ++i ) {
                    if( roleTypes[i].getSubjectArea() != AclbasedSubjectArea._SA ) {
                        roleTypes2[index++] = roleTypes[i];
                    }
                }
                if( index > 0 ) {
                    if( index < roleTypes2.length ) {
                        roleTypes2 = ArrayHelper.copyIntoNewArray( roleTypes2, 0, index, RoleType.class );
                    }
                    checkPermittedUnbless( obj, roleTypes2, neighbor.getIdentifier(), neighbor );
                }
            } catch( NotRelatedException ex ) {
                log.error( ex );
            }
        }
    }
}
