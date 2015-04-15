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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if a MeshObject cannot be unblessed because a RoleType requires this
 * MeshObject to continue being blessed by that MeshType.
 */
public class RoleTypeRequiresEntityTypeException
        extends
            IllegalOperationTypeException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject on which the illegal operation was attempted
     * @param entityType the EntityType of the attempted unblessing, if available
     * @param entityTypeIdentifier the MeshTypeIdentifier of the EntityType of the attempted unblessing
     * @param roleType the RoleType of that blocked the attempted unblessing
     * @param roleTypeIdentifier the MeshTypeIdentifier of the RoleType of that blocked the attempted unblessing
     * @param neighbor the MeshObject at the other end of the relationship, if available
     * @param neighborIdentifier the MeshObjectIdentifier of the MeshObject at the other end of the relationship
     */
    public RoleTypeRequiresEntityTypeException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            EntityType           entityType,
            MeshTypeIdentifier   entityTypeIdentifier,
            RoleType             roleType,
            MeshTypeIdentifier   roleTypeIdentifier,
            MeshObject           neighbor,
            MeshObjectIdentifier neighborIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, obj, identifier );

        theEntityType           = entityType;
        theEntityTypeIdentifier = entityTypeIdentifier;
        theRoleType             = roleType;
        theRoleTypeIdentifier   = roleTypeIdentifier;
        theNeighbor             = neighbor;
        theNeighborIdentifier   = neighborIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param entityType the EntityType of the attempted unblessing, if available
     * @param roleType the RoleType of that blocked the attempted unblessing
     * @param other the MeshObject at the other end of the relationship, if available
     */
    public RoleTypeRequiresEntityTypeException(
            MeshObject           obj,
            EntityType           entityType,
            RoleType             roleType,
            MeshObject           other )
    {
        this( obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                entityType,
                entityType.getIdentifier(),
                roleType,
                roleType.getIdentifier(),
                other,
                other.getIdentifier() );
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param entityType the EntityType of the attempted unblessing, if available
     * @param roleType the RoleType of that blocked the attempted unblessing
     * @param neighborIdentifier identifier of the MeshObject at the other end of the relationship, if available
     */
    public RoleTypeRequiresEntityTypeException(
            MeshObject           obj,
            EntityType           entityType,
            RoleType             roleType,
            MeshObjectIdentifier neighborIdentifier )
    {
        this( obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                entityType,
                entityType.getIdentifier(),
                roleType,
                roleType.getIdentifier(),
                null,
                neighborIdentifier );
    }

    /**
     * Obtain the EntityType that of the attempted unblessing.
     * 
     * @return the EntityType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the MeshType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType of an incorrect type
     */
    public synchronized EntityType getEntityType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( theEntityType == null ) {
            theEntityType = (EntityType) resolve( theEntityTypeIdentifier );
        }
        return theEntityType;
    }
    
    /**
     * Obtain the RoleType that blocked the unblessing of the MeshObject.
     * 
     * @return the RoleType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the MeshType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType of an incorrect type
     */
    public synchronized RoleType getRoleType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( theRoleType == null ) {
            theRoleType = (RoleType) resolve( theRoleTypeIdentifier );
        }
        return theRoleType;
    }
    
    /**
     * Obtain the MeshObject at the other end of the relationship.
     * 
     * @return the MeshObject
     * @throws MeshObjectAccessException thrown if something went wrong accessing the MeshObject
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getOtherMeshObject()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theNeighbor == null ) {
            theNeighbor = resolve( theNeighborIdentifier );
        }
        return theNeighbor;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[]{
                    "meshObject",
                    "meshObjectIdentifier",
                    "entityType",
                    "entityTypeIdentifier",
                    "roleType",
                    "roleTypeIdentifier",
                    "neighbor",
                    "neighborIdentifier",
                    "types"
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theEntityType,
                    theEntityTypeIdentifier,
                    theRoleType,
                    theRoleTypeIdentifier,
                    theNeighbor,
                    theNeighborIdentifier,
                    MeshTypeUtils.meshTypeIdentifiersOrNull( theMeshObject )
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theEntityTypeIdentifier, theRoleTypeIdentifier, theNeighborIdentifier };
    }

    /**
     * The EntityType whose unblessing was attempted.
     */
    protected transient EntityType theEntityType;
    
    /**
     * The identifier of the EntityType whose unblessing was attempted.
     */
    protected MeshTypeIdentifier theEntityTypeIdentifier;

    /**
     * The RoleType that blocked the unblessing.
     */
    protected transient RoleType theRoleType;
    
    /**
     * The identifier of the RoleType that blocked the unblessing.
     */
    protected MeshTypeIdentifier theRoleTypeIdentifier;

    /**
     * The MeshObject at the other end of the relationship that blocked the unblessing.
     */
    protected transient MeshObject theNeighbor;

    /**
     * The MeshObjectIdentifier of the MeshObject at the other end of the relationship that blocked the unblessing.
     */
    protected MeshObjectIdentifier theNeighborIdentifier;
}
