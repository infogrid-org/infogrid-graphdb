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

package org.infogrid.mesh.externalized;

import org.infogrid.mesh.MeshObjectIdentifier;

import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyValue;

/**
 * Representation of a MeshObject that can be easily serialized and deserialized.
 */
public interface ExternalizedMeshObject
{
    /**
     * Obtain the MeshObjectIdentifier of the MeshObject.
     *
     * @return the MeshObjectIdentifier of the MeshObject
     */
    public abstract MeshObjectIdentifier getIdentifier();

    /**
     * Obtain the time the MeshObject was created.
     *
     * @return the time the MeshObject was created, in System.currentTimeMillis() format
     */
    public abstract long getTimeCreated();

    /**
     * Obtain the time the MeshObject was last updated.
     *
     * @return the time the MeshObject was last updated, in System.currentTimeMillis() format
     */
    public abstract long getTimeUpdated();

    /**
     * Obtain the time the MeshObject was last read.
     *
     * @return the time the MeshObject was last read, in System.currentTimeMillis() format
     */
    public abstract long getTimeRead();

    /**
     * Obtain the time when the MeshObject will expire.
     *
     * @return the time the MeshObject will expire, in System.currentTimeMillis() format, or -1 if never.
     */
    public abstract long getTimeExpires();

    /**
     * Obtain the MeshTypeIdentifiers of the EntityTypes with which the MeshObject has been blessed
     *
     * @return the MeshTypeIdentifiers of the EntityTypes
     */
    public abstract MeshTypeIdentifier [] getExternalTypeIdentifiers();

    /**
     * Obtain the MeshTypeIdentifiers of the MeshObject's PropertyTpyes.
     *
     * @return the MeshTypeIdentifiers of the MeshObject's PropertyTypes
     * @see #getPropertyValues()
     */
    public abstract MeshTypeIdentifier [] getPropertyTypes();

    /**
     * Obtain the PropertyValues of the MeshObject's properties, in the same sequence
     * as the PropertyTypes returned by getPropertyTypes.
     *
     * @return the PropertyValues of the MeshObject's properties
     * @see #getPropertyTypes()
     */
    public abstract PropertyValue [] getPropertyValues();

    /**
     * Obtain the MeshObjectIdentifiers of the neighbors of this MeshObject.
     *
     * @return the MeshObjectIdentifiers of the neighbors
     */
    public abstract MeshObjectIdentifier [] getNeighbors();

    /**
     * Obtain the MeshTypeIdentifiers of the RoleTypes played by this MeshObject with respect to
     * a given neighbor.
     *
     * @param neighbor the identifier of the neighbor
     * @return the MeshTypeIdentifiers of the RoleTypes
     */
    public abstract MeshTypeIdentifier [] getRoleTypesFor(
            MeshObjectIdentifier neighbor );

    /**
     * Obtain the MeshObjectIdentifiers of the MeshObjects that participate in an equivalence
     * set with this MeshObject.
     *
     * @return the MeshObjectIdentifier. May be null.
     */
    public abstract MeshObjectIdentifier [] getEquivalents();
}
