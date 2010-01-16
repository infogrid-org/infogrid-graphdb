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
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if an operation requires a relationship to
 * be blessed with a certain RoleType, but it is not.
 */
public class RoleTypeNotBlessedException
        extends
            NotBlessedException
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
     * @param type the MeshType of the missing blessing
     * @param typeIdentifier the MeshTypeIdentifier of the MeshType of the missing blessing
     * @param other the MeshObject identifying the other end of the relationship, if available
     * @param otherIdentifier the MeshObjectIdentifier for the MeshObject identifying the other end of the relationship
     */
    public RoleTypeNotBlessedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            RoleType             type,
            MeshTypeIdentifier   typeIdentifier,
            MeshObject           other,
            MeshObjectIdentifier otherIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, obj, identifier, type, typeIdentifier );
        
        theOther           = other;
        theOtherIdentifier = otherIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted
     * @param type the MeshType of the missing blessing
     * @param other the MeshObject identifying the other end of the relationship
     */
    public RoleTypeNotBlessedException(
            MeshObject           obj,
            RoleType             type,
            MeshObject           other )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                type,
                type.getIdentifier(),
                other,
                other.getIdentifier() );
    }

    /**
     * Obtain the RoleType of the missing blessing.
     * 
     * @return the RoleType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the RoleType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType that is not an RoleType
     */
    @Override
    public synchronized RoleType getType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        return (RoleType) super.getType();
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[]{
                    "meshObject",
                    "meshObjectIdentifier",
                    "meshType",
                    "meshTypeIdentifier",
                    "otherObject",
                    "otherObjectIdentifier",
                    "types"
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theType,
                    theTypeIdentifier,
                    theOther,
                    theOtherIdentifier,
                    MeshTypeUtils.meshTypeIdentifiersOrNull( theMeshObject )
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theTypeIdentifier, theOtherIdentifier };
    }

    /**
     * The other end of the relationship.
     */
    protected transient MeshObject theOther;

    /**
     * The identifier of the other end of the relationship.
     */
    protected transient MeshObjectIdentifier theOtherIdentifier;
}
