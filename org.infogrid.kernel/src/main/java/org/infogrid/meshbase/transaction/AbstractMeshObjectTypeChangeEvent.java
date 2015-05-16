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
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.event.AbstractExternalizablePropertyChangeEvent;
import org.infogrid.util.event.SourceUnresolvedException;
import org.infogrid.util.event.ValueUnresolvedException;

/**
 * This event indicates that a MeshObject has changed its type, i.e. by supporting
 * one more or one less EntityType.
 */
public abstract class AbstractMeshObjectTypeChangeEvent
        extends
            AbstractExternalizablePropertyChangeEvent<MeshObject,MeshObjectIdentifier,String,String,EntityType[],MeshTypeIdentifier[]>
        implements
            MeshObjectTypeChangeEvent
{
    /**
     * Constructor.
     *
     * @param source the MeshObject that is the source of the event
     * @param sourceIdentifier the identifier representing the source MeshObject of the event
     * @param oldValues the old set of EntityTypes, prior to the event
     * @param oldValueIdentifiers the identifiers representing the old set of EntityTypes, prior to the event
     * @param deltaValues the EntityTypes that changed
     * @param deltaValueIdentifiers the identifiers of the EntityTypes that changed
     * @param newValues the new set of EntityTypes, after the event
     * @param newValueIdentifiers the identifiers representing the new set of EntityTypes, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    protected AbstractMeshObjectTypeChangeEvent(
            MeshObject           source,
            MeshObjectIdentifier sourceIdentifier,
            EntityType []        oldValues,
            MeshTypeIdentifier[] oldValueIdentifiers,
            EntityType []        deltaValues,
            MeshTypeIdentifier[] deltaValueIdentifiers,
            EntityType []        newValues,
            MeshTypeIdentifier[] newValueIdentifiers,
            long                 timeEventOccurred,
            MeshBase             resolver )
    {
        super(  source,
                sourceIdentifier,
                MeshObject._MESH_OBJECT_TYPES_PROPERTY,
                MeshObject._MESH_OBJECT_TYPES_PROPERTY,
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( oldValues ),
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( oldValueIdentifiers ),
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( deltaValues ),
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( deltaValueIdentifiers ),
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( newValues ),
                AbstractMeshObjectNeighborChangeEvent.checkNoNullArrayMembers( newValueIdentifiers ),
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
     * Obtain the identifiers of the EntityTypes involved in this Change.
     *
     * @return the identifiers of the EntityTypes involved in this Change.
     */
    public final MeshTypeIdentifier [] getEntityTypeIdentifiers()
    {
        return getDeltaValueIdentifier();
    }

    /**
     * Obtain the EntityTypes involved in this Change.
     *
     * @return obtain the EntityTypes involved in this Change.
     */
    public EntityType [] getEntityTypes()
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
        return getPropertyIdentifier();
    }
    
    /**
     * Resolve a value of the event.
     *
     * @param vid the value identifier
     * @return a value of the event
     */
    protected EntityType [] resolveValue(
            MeshTypeIdentifier [] vid )
    {
        if( theResolver == null ) {
            throw new SourceUnresolvedException( this );
        }
        if( vid == null || vid.length == 0 ) {
            return new EntityType[0];
        }

        ModelBase         modelBase = theResolver.getModelBase();
        EntityType []     ret       = new EntityType[ vid.length ];
        
        for( int i=0 ; i<ret.length ; ++i ) {
            try {
                ret[i] = modelBase.findEntityTypeByIdentifier( vid[i] );

            } catch( MeshTypeWithIdentifierNotFoundException ex ) {
                throw new ValueUnresolvedException( this, ex );
            }
        }
        return ret;
    }

    /**
     * The resolver of identifiers carried by this event.
     */
    protected transient MeshBase theResolver;
}
