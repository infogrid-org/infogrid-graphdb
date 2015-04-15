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

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.util.event.AbstractExternalizableEvent;
import org.infogrid.util.event.ValueUnresolvedException;

/**
  * This is the abstract supertype for all events indicating
  * a lifecycle event in the life of a MeshObject.
  */
public abstract class AbstractMeshObjectLifecycleEvent
        extends
            AbstractExternalizableEvent<MeshBase,MeshBaseIdentifier,MeshObject,MeshObjectIdentifier>
        implements
            MeshObjectLifecycleEvent
{
    /**
     * Private constructor, use subclasses.
     *
     * @param source the MeshBase that is the source of the event
     * @param sourceIdentifier the MeshBaseIdentifier representing the source of the event
     * @param deltaValue the MeshObject whose lifecycle changed
     * @param deltaValueIdentifier the MeshObjectIdentifier whose lifecycle changed
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    protected AbstractMeshObjectLifecycleEvent(
            MeshBase             source,
            MeshBaseIdentifier   sourceIdentifier,
            MeshObject           deltaValue,
            MeshObjectIdentifier deltaValueIdentifier,
            long                 timeEventOccurred )
    {
        super( source, sourceIdentifier, deltaValue, deltaValueIdentifier, timeEventOccurred );
        
        theResolver = source;
    }

    /**
     * Obtain the identifier of the MeshObject affected by this Change.
     *
     * @return the identifier of the MeshObject affected by this Change
     */
    public MeshObjectIdentifier getAffectedMeshObjectIdentifier()
    {
        return getDeltaValueIdentifier();
    }

    /**
     * Obtain the MeshObject affected by this Change.
     *
     * @return obtain the MeshObject affected by this Change
     */
    public MeshObject getAffectedMeshObject()
    {
        return getDeltaValue();
    }
    
    /**
     * Set the MeshBase that can resolve the identifiers carried by this event.
     *
     * @param mb the MeshBase
     */
    public void setResolver(
            MeshBase mb )
    {
        if( theResolver != mb ) {
            theResolver = mb;
            clearCachedObjects();
        }
    }
    
    /**
     * Obtain the MeshBase that is currently set as resolver for the identifiers carried by this event.
     * 
     * @return the MeshBase, if any
     */
    public MeshBase getResolver()
    {
        return theResolver;
    }

    /**
     * Resolve the source of the event.
     *
     * @return the source of the event
     */
    protected MeshBase resolveSource()
    {
        throw new UnsupportedOperationException(); // Should this be implemented, and do what exactly?
    }

    /**
     * Resolve a value of the event.
     *
     * @param vid the value identifier
     * @return a value of the event
     */
    protected MeshObject resolveValue(
            MeshObjectIdentifier vid )
    {
        if( theResolver == null ) {
            throw new ValueUnresolvedException( this, vid );
        }
        MeshObject ret = theResolver.findMeshObjectByIdentifier( vid );
        return ret;
    }

    /**
     * The resolver of identifiers carried by this event.
     */
    protected transient MeshBase theResolver;
}
