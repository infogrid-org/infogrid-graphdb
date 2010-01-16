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

package org.infogrid.mesh.set.active;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.event.AbstractExternalizableEvent;
import org.infogrid.util.event.SourceUnresolvedException;
import org.infogrid.util.event.ValueUnresolvedException;

/**
 * An event related to a TraversalPathSet.
 */
public abstract class ActiveTraversalPathSetEvent
        extends
            AbstractExternalizableEvent<ActiveTraversalPathSet,ActiveTraversalPathSet,TraversalPath,TraversalPath>
{
    /**
     * Constructor.
     *
     * @param source the source of the event
     * @param delta the affected TraversalPath
     */
    protected ActiveTraversalPathSetEvent(
            ActiveTraversalPathSet source,
            TraversalPath          delta )
    {
        super(  source,
                source,
                delta,
                delta,
                System.currentTimeMillis() );
    }

    /**
     * Set the resolving MeshBase.
     * 
     * @param newValue the new value
     */
    public void setMeshBase(
            MeshBase newValue )
    {
        theMeshBase = newValue;
    }

    /**
     * Obtain the resolving MeshBase, if any.
     * 
     * @return the MeshBase
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }
    
    /**
     * Enable subclass to resolve the source of the event.
     *
     * @return the source of the event
     * @throws SourceUnresolvedException thrown if this ExternalizableEvent was serialized/deserialized,
     *         and re-resolving the source failed
     */
    protected ActiveTraversalPathSet resolveSource()
            throws
                SourceUnresolvedException
    {
        throw new UnsupportedOperationException(); // FIXME for now
    }
    
    /**
     * Enable subclass to resolve a value of the event.
     *
     * @param vid the identifier of the value of the event
     * @return a value of the event
     * @throws ValueUnresolvedException thrown if this ExternalizableEvent was serialized/deserialized,
     *         and re-resolving the value failed
     */
    protected TraversalPath resolveValue(
            TraversalPath vid )
       throws
           ValueUnresolvedException
    {
        return vid;     
    }
    
    /**
     * The resolving MeshBase, if any.
     */
    protected transient MeshBase theMeshBase;
}
