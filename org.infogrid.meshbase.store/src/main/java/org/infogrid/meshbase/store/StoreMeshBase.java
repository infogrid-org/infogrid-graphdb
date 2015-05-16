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

package org.infogrid.meshbase.store;

import java.util.HashMap;
import java.util.Map;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.a.AMeshBase;
import org.infogrid.meshbase.a.AMeshBaseLifecycleManager;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.transaction.AbstractMeshObjectNeighborChangeEvent;
import org.infogrid.meshbase.transaction.AbstractMeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.AbstractMeshObjectTypeChangeEvent;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.MeshObjectBecameDeadStateEvent;
import org.infogrid.meshbase.transaction.MeshObjectCreatedEvent;
import org.infogrid.meshbase.transaction.MeshObjectDeletedEvent;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.store.Store;
import org.infogrid.store.util.StoreBackedSwappingHashMap;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentationParseException;

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
     * Most convenient factory method.
     *
     * @param meshObjectStore the Store in which to store the MeshObjects
     * @return the created StoreMeshBase
     */
    public static StoreMeshBase create(
            Store meshObjectStore )
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
        
        StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = new StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject>( objectMapper, meshObjectStore );

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
        
        StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = new StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject>( objectMapper, meshObjectStore );

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
            MeshBaseIdentifier                                            identifier,
            MeshObjectIdentifierFactory                                   identifierFactory,
            MeshObjectSetFactory                                          setFactory,
            ModelBase                                                     modelBase,
            AMeshBaseLifecycleManager                                     life,
            AccessManager                                                 accessMgr,
            StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> cache,
            Context                                                       context )
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
    
    /**
     * Update the cache when Transactions are committed.
     *
     * @param tx Transaction the Transaction that was committed
     */
    @Override
    protected void transactionCommittedHook(
            Transaction tx )
    {
        super.transactionCommittedHook( tx );
        
        Map<MeshObjectIdentifier,MeshObject>                          toWrite = determineObjectsToWriteFromTransaction( tx );
        StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> map     = (StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject>) theCache;
        
        for( Map.Entry<MeshObjectIdentifier,MeshObject> current : toWrite.entrySet() ) {
            if( current.getValue() != null ) {
                map.saveValueToStorageUponCommit( current.getKey(), current.getValue() );
            } else {
                map.removeValueFromStorageUponCommit( current.getKey() );
            }
        }
        map.transactionDone();
    }
    
    /**
     * Write changes made during a Transaction to the Store. This is factored out as a static, so NetStoreMeshBase can also
     * use it without replicating code.
     * 
     * @param tx Transaction the Transaction that was committed
     * @param map the storage for the MeshObjects
     */
    public static Map<MeshObjectIdentifier,MeshObject> determineObjectsToWriteFromTransaction(
            Transaction tx )
    {
        Change [] theChanges = tx.getChangeSet().getChanges();

        HashMap<MeshObjectIdentifier,MeshObject> ret = new HashMap<MeshObjectIdentifier,MeshObject>( theChanges.length );

        // we go backwards, that way we don't forget to store MeshObjects that were deleted and recreated within the
        // same Transaction
        for( int i=theChanges.length-1 ; i>=0 ; --i ) {
            
            Change               currentChange = theChanges[i];
            MeshObjectIdentifier affectedName  = currentChange.getAffectedMeshObjectIdentifier();

            if( ret.containsKey( affectedName )) {
                continue;
            }
            MeshObject affected = currentChange.getAffectedMeshObject();
            if( affected == null ) {
                log.error( "Cannot find affected MeshObject " + affectedName );

            } else if( affected.getIsDead() ) {
                // need to check for isDead first, otherwise we might update instead of delete, for example
                ret.put( affectedName, null );

            } else if( currentChange instanceof MeshObjectDeletedEvent ) {
                ret.put( affectedName, null );

            } else if( currentChange instanceof MeshObjectPropertyChangeEvent ) {
                ret.put( affectedName, affected );

            } else if( currentChange instanceof AbstractMeshObjectNeighborChangeEvent ) { // either Added or Removed
                ret.put( affectedName, affected );

            } else if( currentChange instanceof AbstractMeshObjectTypeChangeEvent ) { // either Added or Removed
                ret.put( affectedName, affected );

            } else if( currentChange instanceof AbstractMeshObjectRoleChangeEvent ) { // either Added or Removed
                ret.put( affectedName, affected );

            } else if( currentChange instanceof MeshObjectCreatedEvent ) {
                ret.put( affectedName, affected );

            } else if( currentChange instanceof MeshObjectBecameDeadStateEvent ) {
                // noop, we catch this through MeshObjectLifecycleEvent.Deleted
 
            } else {
                log.error( "Unknown change: " + currentChange );
            }
        }
        return ret;
    }
}
