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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store;

import java.lang.ref.Reference;
import java.util.HashSet;
import java.util.Set;
import org.infogrid.store.IterableStore;
import org.infogrid.store.IterableStoreCursor;
import org.infogrid.store.Store;
import org.infogrid.store.StoreEntryMapper;
import org.infogrid.store.util.IterableStoreBackedSwappingHashMap;
import org.infogrid.store.util.StoreBackedSwappingHashMap;
import org.infogrid.store.util.StoreBackedSwappingHashMapKeysIterator;
import org.infogrid.store.util.StoreBackedSwappingHashMapValuesIterator;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.logging.Log;


/**
 * Modifies StoredBasedSwappingHashMap to only write to storage upon commit of a Transaction.
 */
public class StoreMeshBaseSwappingHashMap<K,V>
    extends
        StoreBackedSwappingHashMap<K,V>
{
    private static final Log log = Log.getLogInstance( StoreMeshBaseSwappingHashMap.class ); // our own, private logger

    /**
     * Constructor.
     * 
     * @param mapper the <code>StoreEntryMapper</code> to use
     * @param store the underlying <code>Store</code>
     */
    public StoreMeshBaseSwappingHashMap(
            StoreEntryMapper<K,V> mapper,
            Store                 store )
    {
        super( DEFAULT_INITIAL_CAPACITY, mapper, store );
    }
    
    /**
     * Create the right kind of Reference.
     *
     * @param key the key
     * @param value the value
     * @return the Reference to the value
     */
    @Override
    protected Reference<V> createReference(
            K key,
            V value )
    {
        return new WeakEntryReference<K,V>( key, value, theQueue );
    }
    
    /**
     * Do not load anything that has been removed.
     *
     * @param key the key whose value should be loaded
     * @return the value that was loaded, or null if none.
     */
    @Override
    protected V loadValueFromStorage(
            Object key )
    {
        if( theRemoved.contains( key )) {
            return null;
        }
        return super.loadValueFromStorage( key );
    }

    /**
     * Don't do anything. Saving occurs only when a Transaction is committed.
     *
     * @param key the key whose value was updated
     * @param newValue the new value
     */
    @Override
    protected void saveValueToStorage(
            K key,
            V newValue )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "saveValueToStorage", key, newValue );
        }
    }
    
    /**
     * Save value to storage. Invoked by Transaction commit.
     *
     * @param key the key whose value was updated
     * @param newValue the new value
     */
    public void saveValueToStorageUponCommit(
            K key,
            V newValue )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "saveValueToStorageUponCommit", key, newValue );
        }
        super.saveValueToStorage( key, newValue );
        
        theRemoved.remove( key ); // happens if the object was created during the transaction.
                                  // And if not contained, nothing bad happens either.
    }
    
    /**
     * Don't do anything. Removing occurs only when a Transaction is committed.
     *
     * @param key the key whose value has been removed
     */
    @Override
    protected void removeValueFromStorage(
            Object key )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "removeValueFromStorage", key );
        }

        theDelegate.remove( key );
        theRemoved.add( key );
    }
    
    /**
     * Remove value from storage. Invoked by Transaction commit.
     *
     * @param key the key whose value has been removed
     */

    public void removeValueFromStorageUponCommit(
            Object key )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "removeValueFromStorageUponCommit", key );
        }
    
        super.removeValueFromStorage( key );
        
        theRemoved.remove( key );
    }
    
    /**
     * Returns a set view of the keys contained in this map.
     *
     * @return the Set of keys
     */
    @Override
    public Set<K> keySet()
    {
        cleanup();

        if( theKeySet == null ) {
            theKeySet = new IterableStoreBackedSwappingHashMap.MyKeySet<K,V>( (IterableStore) theStore, theMapper );
        }
        return theKeySet;
    }

    /**
     * Obtain a CursorIterator on the keys of this Map.
     *
     * @param keyArrayComponentType the class using which arrays of keys are allocated
     * @param valueArrayComponentType the class using which arrays of values are allocated
     * @return the CursorIterator
     */
    @Override
    public CursorIterator<K> keysIterator(
            Class<K> keyArrayComponentType,
            Class<V> valueArrayComponentType )
    {
        IterableStoreCursor delegate = ((IterableStore)theStore).iterator();
        
        CursorIterator<K> ret = new StoreBackedSwappingHashMapKeysIterator<K,V>( delegate, this, theMapper, keyArrayComponentType );
        return ret;
    }
    
    /**
     * Obtain a CursorIterator on the values of this Map.
     *
     * @param keyArrayComponentType the class using which arrays of keys are allocated
     * @param valueArrayComponentType the class using which arrays of values are allocated
     * @return the CursorIterator
     */
    @Override
    public CursorIterator<V> valuesIterator(
            Class<K> keyArrayComponentType,
            Class<V> valueArrayComponentType )
    {
        IterableStoreCursor delegate = ((IterableStore)theStore).iterator();
        
        CursorIterator<V> ret = new StoreBackedSwappingHashMapValuesIterator<K,V>( delegate, this, theMapper, keyArrayComponentType, valueArrayComponentType );
        return ret;
    }

    /**
     * The transaction is done. Whatever is still in theRemoved is a programming error.
     */
    public void transactionDone()
    {
        if( !theRemoved.isEmpty() ) {
            log.error( "theRemoved not empty", theRemoved );
        }
        theRemoved.clear();
    }

    /**
     * Set of keys in this IterableStoreBackedSwappingHashMap.
     */
    protected IterableStoreBackedSwappingHashMap.MyKeySet<K,V> theKeySet;
    
    /**
     * Keep track of MeshObjects that were removed during a Transaction, to avoid recreating them from the storage
     * although they were deleted during a transaction.
     */
    protected HashSet<Object> theRemoved = new HashSet<Object>();
}
