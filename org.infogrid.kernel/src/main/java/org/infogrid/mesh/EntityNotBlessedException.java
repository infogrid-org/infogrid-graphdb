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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;

/**
 * Thrown if an operation requires a MeshObject to
 * be blessed with a certain EntityType, but it is not.
 */
public class EntityNotBlessedException
        extends
            NotBlessedException
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
     */
    public EntityNotBlessedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            EntityType           type,
            MeshTypeIdentifier   typeIdentifier )
    {
        super(  mb,
                originatingMeshBaseIdentifier,
                obj,
                identifier,
                type,
                typeIdentifier );
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param type the MeshType of the already-existing blessing
     */
    public EntityNotBlessedException(
            MeshObject           obj,
            EntityType           type )
    {
        super(  obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                type,
                type.getIdentifier() );
    }

    /**
     * Obtain the EntityType of the missing blessing.
     * 
     * @return the MeshType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the EntityType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType that is not an EntityType
     */
    @Override
    public synchronized EntityType getType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        return (EntityType) super.getType();
    }
}
