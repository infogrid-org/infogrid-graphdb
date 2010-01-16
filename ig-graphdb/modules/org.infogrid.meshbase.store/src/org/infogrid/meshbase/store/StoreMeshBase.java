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

package org.infogrid.meshbase.store;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.a.AMeshBase;
import org.infogrid.meshbase.a.AMeshBaseLifecycleManager;
import org.infogrid.meshbase.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.store.Store;
import org.infogrid.store.util.StoreBackedSwappingHashMap;
import org.infogrid.util.CachingMap;
import org.infogrid.util.context.Context;
import org.infogrid.util.logging.Log;

/**
  * A MeshBase that delegates persistence to an external Store. All of its intelligence
  * resides either in the factory method, or in the AMeshBase implementation.
  */
public class StoreMeshBase
        extends
            AMeshBase
{
    private static final Log log = Log.getLogInstance( StoreMeshBase.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param modelBase the ModelBase with the type definitions we use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param meshObjectStore the Store in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created StoreMeshBase
      */
    public static StoreMeshBase create(
            MeshBaseIdentifier identifier,
            ModelBase          modelBase,
            AccessManager      accessMgr,
            Store              meshObjectStore,
            Context            context )
    {
        ImmutableMMeshObjectSetFactory setFactory = ImmutableMMeshObjectSetFactory.create( MeshObject.class, MeshObjectIdentifier.class );

        StoreMeshBase ret = StoreMeshBase.create(
                identifier,
                setFactory,
                modelBase,
                accessMgr,
                meshObjectStore,
                context );

        return ret;
    }

    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param setFactory the factory for MeshObjectSets appropriate for this MeshBase
     * @param modelBase the ModelBase with the type definitions we use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param meshObjectStore the Store in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created StoreMeshBase
     */
    public static StoreMeshBase create(
            MeshBaseIdentifier   identifier,
            MeshObjectSetFactory setFactory,
            ModelBase            modelBase,
            AccessManager        accessMgr,
            Store                meshObjectStore,
            Context              context )
    {
        StoreMeshBaseEntryMapper objectMapper = new StoreMeshBaseEntryMapper();
        
        StoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = StoreBackedSwappingHashMap.createWeak( objectMapper, meshObjectStore );

        MeshObjectIdentifierFactory identifierFactory = DefaultAMeshObjectIdentifierFactory.create();
        AMeshBaseLifecycleManager   life              = AMeshBaseLifecycleManager.create();

        StoreMeshBase ret = new StoreMeshBase(
                identifier,
                identifierFactory,
                setFactory,
                modelBase,
                life,
                accessMgr,
                objectStorage,
                context );

        setFactory.setMeshBase( ret );
        objectMapper.setMeshBase( ret );
        ret.initializeHomeObject();
        
        if( log.isDebugEnabled() ) {
            log.debug( "created " + ret );
        }
        return ret;
    }

    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param identifierFactory the factory for MeshObjectIdentifiers appropriate for this MeshBase
     * @param setFactory the factory for MeshObjectSets appropriate for this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param meshObjectStore the Store in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created StoreMeshBase
     */
    public static StoreMeshBase create(
            MeshBaseIdentifier          identifier,
            MeshObjectIdentifierFactory identifierFactory,
            MeshObjectSetFactory        setFactory,
            ModelBase                   modelBase,
            AccessManager               accessMgr,
            Store                       meshObjectStore,
            Context                     context )
    {
        StoreMeshBaseEntryMapper objectMapper = new StoreMeshBaseEntryMapper();
        
        StoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = StoreBackedSwappingHashMap.createWeak( objectMapper, meshObjectStore );

        AMeshBaseLifecycleManager life = AMeshBaseLifecycleManager.create();

        StoreMeshBase ret = new StoreMeshBase(
                identifier,
                identifierFactory,
                setFactory,
                modelBase,
                life,
                accessMgr,
                objectStorage,
                context );

        objectMapper.setMeshBase( ret );
        ret.initializeHomeObject();
        
        if( log.isDebugEnabled() ) {
            log.debug( "created " + ret );
        }
        return ret;
    }

    /**
     * Constructor.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param identifierFactory the factory for MeshObjectIdentifiers appropriate for this MeshBase
     * @param setFactory the factory for MeshObjectSets appropriate for this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param life the MeshBaseLifecycleManager to use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param cache the CachingMap that holds the MeshObjects in this MeshBase
     * @param context the Context in which this MeshBase runs
     */
    protected StoreMeshBase(
            MeshBaseIdentifier                          identifier,
            MeshObjectIdentifierFactory                 identifierFactory,
            MeshObjectSetFactory                        setFactory,
            ModelBase                                   modelBase,
            AMeshBaseLifecycleManager                   life,
            AccessManager                               accessMgr,
            CachingMap<MeshObjectIdentifier,MeshObject> cache,
            Context                                     context )
    {
        super( identifier, identifierFactory, setFactory, modelBase, life, accessMgr, cache, context );
    }

    /**
     * Helper method to cast the cache to the right subtype of CachingMap.
     * 
     * @return the cache
     */
    protected StoreBackedSwappingHashMap<MeshObjectIdentifier, MeshObject> getCachingMap()
    {
        return (StoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject>) theCache;
    }
}
