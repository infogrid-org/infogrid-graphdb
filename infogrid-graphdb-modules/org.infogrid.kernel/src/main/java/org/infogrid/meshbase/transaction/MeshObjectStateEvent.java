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
import org.infogrid.util.event.AbstractExternalizablePropertyChangeEvent;
import org.infogrid.util.event.SourceUnresolvedException;

/**
  * This indicates a change in the state of a MeshObject, such as
  * "the MeshObject died". Subclasses implement specific state changes.
  */
public abstract class MeshObjectStateEvent
        extends
            AbstractExternalizablePropertyChangeEvent<MeshObject, MeshObjectIdentifier, String, Void, MeshObjectStateEvent.MeshObjectState, String>
        implements
            Change<MeshObject,MeshObjectIdentifier,MeshObjectStateEvent.MeshObjectState,String>
{
    /**
     * Constructor.
     *
     * @param source the MeshObject that is the source of the event
     * @param sourceIdentifier the identifier representing the source MeshObject of the event
     * @param oldValue the old value of the MeshObjectState, prior to the event
     * @param newValue the new value of the MeshObjectState, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    protected MeshObjectStateEvent(
            MeshObject           source,
            MeshObjectIdentifier sourceIdentifier,
            MeshObjectState      oldValue,
            MeshObjectState      newValue,
            long                 timeEventOccurred,
            MeshBase             resolver )
    {
        super(  source,
                sourceIdentifier,
                MeshObject._MESH_OBJECT_STATE_PROPERTY,
                null,
                oldValue,
                oldValue.toString(),
                newValue, // delta = new
                newValue.toString(),
                newValue,
                newValue.toString(),
                timeEventOccurred );
        
        theResolver = resolver;
    }
    
    /**
     * Obtain the identifier of the MeshObject affected by this Change.
     *
     * @return the identifier of the MeshObject affected by this Change
     */
    public MeshObjectIdentifier getAffectedMeshObjectIdentifier()
    {
        return getSourceIdentifier();
    }

    /**
     * Obtain the MeshObject affected by this Change.
     *
     * @return obtain the MeshObject affected by this Change
     */
    public MeshObject getAffectedMeshObject()
    {
        return getSource();
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
    protected MeshObject resolveSource()
    {
        if( theResolver == null ) {
            throw new SourceUnresolvedException( this );
        }
        
        MeshObject ret = theResolver.findMeshObjectByIdentifier( getSourceIdentifier() );
        return ret;
    }

    /**
     * Resolve the property of the event.
     *
     * @return the property of the event
     */
    protected String resolveProperty()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * The resolver of identifiers carried by this event.
     */
    protected transient MeshBase theResolver;

    /**
     * Common super-interface for all MeshObjectStates.
     */
    public static interface MeshObjectState
    {}
}
