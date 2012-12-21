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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store;

import java.io.IOException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.IterableMeshBaseDifferencer;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.a.AMeshBaseLifecycleManager;
import org.infogrid.mesh.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.store.IterableStore;
import org.infogrid.store.Store;
import org.infogrid.store.util.IterableStoreBackedSwappingHashMap;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentationParseException;

/**
 * A StoreMeshBase that we can iterate over. For this to work, the underlying
 * Store must be an IterableStore.
 */
public class IterableStoreMeshBase
        extends
            StoreMeshBase
        implements
            IterableMeshBase
{
    private static final Log log = Log.getLogInstance( IterableStoreMeshBase.class ); // our own, private logger

    /**
     * Most convenient factory method.
     *
     * @param meshObjectStore the IterableStore in which to store the MeshObjects
     * @return the created IterableStoreMeshBase
     */
    public static IterableStoreMeshBase create(
            IterableStore meshObjectStore )
    {
        try {
            return create(
                    DefaultMeshBaseIdentifierFactory.create().fromExternalForm( "DefaultMeshBase" ),
                    MModelBase.create(),
                    null,
                    meshObjectStore,
                    SimpleContext.createRoot( "root context" ));
        } catch( StringRepresentationParseException ex ) {
            log.error( ex );
            return null;
        }
    }
    
    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param meshObjectStore the IterableStore in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created IterableStoreMeshBase
      */
    public static IterableStoreMeshBase create(
            MeshBaseIdentifier identifier,
            ModelBase          modelBase,
            AccessManager      accessMgr,
            IterableStore      meshObjectStore,
            Context            context )
    {
        ImmutableMMeshObjectSetFactory setFactory
                = ImmutableMMeshObjectSetFactory.create( MeshObject.class, MeshObjectIdentifier.class );

        IterableStoreMeshBase ret = IterableStoreMeshBase.create(
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
     * @param modelBase the ModelBase containing type information
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param meshObjectStore the IterableStore in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created IterableStoreMeshBase
     */
    public static IterableStoreMeshBase create(
            MeshBaseIdentifier   identifier,
            MeshObjectSetFactory setFactory,
            ModelBase            modelBase,
            AccessManager        accessMgr,
            IterableStore        meshObjectStore,
            Context              context )
    {
        StoreMeshBaseEntryMapper objectMapper = new StoreMeshBaseEntryMapper();
        
        IterableStoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = IterableStoreBackedSwappingHashMap.createWeak( objectMapper, meshObjectStore );

        MeshObjectIdentifierFactory identifierFactory = DefaultAMeshObjectIdentifierFactory.create();
        AMeshBaseLifecycleManager   life              = AMeshBaseLifecycleManager.create();

        IterableStoreMeshBase ret = new IterableStoreMeshBase(
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
     * @param meshObjectStore the IterableStore in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created IterableStoreMeshBase
     */
    public static IterableStoreMeshBase create(
            MeshBaseIdentifier          identifier,
            MeshObjectIdentifierFactory identifierFactory,
            MeshObjectSetFactory        setFactory,
            ModelBase                   modelBase,
            AccessManager               accessMgr,
            IterableStore               meshObjectStore,
            Context                     context )
    {
        StoreMeshBaseEntryMapper objectMapper = new StoreMeshBaseEntryMapper();
        
        IterableStoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = IterableStoreBackedSwappingHashMap.createWeak( objectMapper, meshObjectStore );

        AMeshBaseLifecycleManager life = AMeshBaseLifecycleManager.create();

        IterableStoreMeshBase ret = new IterableStoreMeshBase(
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
    protected IterableStoreMeshBase(
            MeshBaseIdentifier                                                  identifier,
            MeshObjectIdentifierFactory                                         identifierFactory,
            MeshObjectSetFactory                                                setFactory,
            ModelBase                                                           modelBase,
            AMeshBaseLifecycleManager                                           life,
            AccessManager                                                       accessMgr,
            IterableStoreBackedSwappingHashMap<MeshObjectIdentifier,MeshObject> cache,
            Context                                                             context )
    {
        super( identifier, identifierFactory, setFactory, modelBase, life, accessMgr, cache, context );
    }
    
    /**
     * Obtain an Iterator over all MeshObjects in the Store.
     *
     * @return the Iterator
     */
    public CursorIterator<MeshObject> iterator()
    {
        return getCachingMap().valuesIterator( MeshObjectIdentifier.class, MeshObject.class );
    }
    
    /**
     * Obtain an Iterator over all MeshObjects in the Store.
     *
     * @return the Iterator
     */
    final public CursorIterator<MeshObject> getIterator()
    {
        return iterator();
    }

    /**
     * Determine the number of MeshObjects in this MeshBase.
     *
     * @return the number of MeshObjects in this MeshBase
     */
    public int size()
    {
        try {
            return ((IterableStore) getCachingMap().getStore()).size();

        } catch( IOException ex ) {
            log.error( ex );
            return 0;
        }
    }

    /**
     * Determine the number of MeshObjects in this MeshBase. This redundant method
     * is provided to make life easier for JavaBeans-aware software.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #size()
     */
    public final int getSize()
    {
        return size();
    }

    /**
     * Factory method for a IterableMeshBaseDifferencer, with this IterableMeshBase
     * being the comparison base.
     *
     * @return the IterableMeshBaseDifferencer
     */
    public IterableMeshBaseDifferencer getDifferencer()
    {
        return new IterableMeshBaseDifferencer( this );
    }
}
