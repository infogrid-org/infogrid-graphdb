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

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.event.SourceUnresolvedException;

/**
 * Abstraction for neighbor and role changes.
 */
public interface MeshObjectRelationshipEvent
{
    /**
     * Set the MeshBase against which all carried information shall be resolved.
     *
     * @param resolver the MeshBase.
     */
    public void setResolver(
            MeshBase resolver );

    /**
     * Obtain the MeshBase that is currently set as resolver for the identifiers carried by this event.
     * 
     * @return the MeshBase, if any
     */
    public MeshBase getResolver();

    /**
     * Obtain the source of the event.
     * 
     * @return the source of the event
     * @throws SourceUnresolvedException thrown if this ExternalizableEvent was serialized/deserialized,
     *         and re-resolving the value failed
     */
    public MeshObject getSource()
        throws
            SourceUnresolvedException;
    
    /**
     * Obtain the source identifier of the event.
     *
     * @return the source identifier
     */
    public MeshObjectIdentifier getSourceIdentifier();

    /**
     * Obtain the neighbor that changed.
     *
     * @return the neighbor MeshObject
     */
    public MeshObject getNeighborMeshObject();
    
    /**
     * Obtain the Identifier of the neighbor that changed.
     *
     * @return the Identifier of the neighbor MeshObject
     */
    public MeshObjectIdentifier getNeighborMeshObjectIdentifier();
}
