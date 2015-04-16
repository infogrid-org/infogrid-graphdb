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
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RoleType;

/**
 * Makes it easy to create an AccessManager by delegating to one or more other AccessManagers and
 * potentially overriding some methods. Access is granted only if all other AccessManagers grant
 * access.
 */
public class DelegatingAccessManager
        implements
            AccessManager
{
    /**
     * Factory method.
     *
     * @param singleDelegate a single AccessManager to delegate to
     * @return the created DelegatingAccessManager
     */
    public static DelegatingAccessManager create(
            AccessManager singleDelegate )
    {
        return new DelegatingAccessManager( new AccessManager[] { singleDelegate } );
    }

    /**
     * Factory method.
     *
     * @param delegates the AccessManagers to delegate to
     * @return the created DelegatingAccessManager
     */
    public static DelegatingAccessManager create(
            AccessManager [] delegates )
    {
        return new DelegatingAccessManager( delegates );
    }

    /**
     * Constructor.
     *
     * @param delegates the AccessManagers to delegate to
     */
    protected DelegatingAccessManager(
            AccessManager [] delegates )
    {
        theDelegates = delegates;
    }

    /**
     * Assign the second MeshObject to be the owner of the first MeshObject. This
     * must only be called if the current Thread has an open Transaction.
     *
     * @param toBeOwned the MeshObject to be owned by the new owner
     * @param newOwner the MeshObject that is the new owner
     * @throws NotPermittedException thrown if it is not permitted
     * @throws TransactionException thrown if this is invoked outside of proper transaction boundaries
     */
    public void assignOwner(
            MeshObject toBeOwned,
            MeshObject newOwner )
        throws
            NotPermittedException,
            TransactionException
    {
        for( AccessManager current : theDelegates ) {
            current.assignOwner( toBeOwned, newOwner );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedCreate( identifier );
        }
    }

    /**
     * Check whether it is permitted to set a MeshObject's auto-delete time to the given value.
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedSetTimeExpires( obj, newValue );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedSetProperty( obj, thePropertyType, newValue );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedGetProperty( obj, thePropertyType );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedBlessedBy( obj, type );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedBless( obj, types );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedUnbless( obj, types );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedBless( obj, thisEnds, neighborIdentifier, neighbor );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedUnbless( obj, thisEnds, neighborIdentifier, neighbor );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedTraversal( obj, toTraverse, neighborIdentifier, neighbor );
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedAddAsEquivalent( one, twoIdentifier, two );
        }
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedRemoveAsEquivalent( obj, roleTypesToAsk );
        }
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
        for( AccessManager current : theDelegates ) {
            current.checkPermittedDelete( obj );
        }
    }

    /**
     * The AccessManagers to delegate to.
     */
    protected AccessManager [] theDelegates;
}
