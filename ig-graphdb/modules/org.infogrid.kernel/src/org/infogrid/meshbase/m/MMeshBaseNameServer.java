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

package org.infogrid.meshbase.m;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshBaseNameServer;
import org.infogrid.util.Invocable;
import org.infogrid.util.MNameServer;
import org.infogrid.util.NameServer;
import org.infogrid.util.WritableNameServer;

/**
 * In-memory implementation of a NameServer for MeshBases.
 * 
 * @param <K> the MeshBaseIdentifier or subtype
 * @param <V> the MeshBase or subtype
 */
public class MMeshBaseNameServer<K extends MeshBaseIdentifier,V extends MeshBase>
        implements
            MeshBaseNameServer<K,V>,
            WritableNameServer<K,V>
{
    /**
     * Factory method.
     *
     * @return the created MMeshBaseNameServer
     * 
     * @param <K> the MeshBaseIdentifier or subtype
     * @param <V> the MeshBase or subtype
     */
    public static <K extends MeshBaseIdentifier,V extends MeshBase> MMeshBaseNameServer<K,V> create()
    {
        MNameServer<K,V> delegate = MNameServer.<K,V>create();
        
        MMeshBaseNameServer<K,V> ret = new MMeshBaseNameServer<K,V>(
                delegate );
        return ret;
    }
    
    /**
     * Factory method, specifiying a delegate.
     *
     * @param newDelegate the delegate
     * @return the created MMeshBaseNameServer
     * @param <K> the MeshBaseIdentifier or subtype
     * @param <V> the MeshBase or subtype
     */
    public static <K extends MeshBaseIdentifier,V extends MeshBase> MMeshBaseNameServer<K,V> create(
            NameServer<K,? extends V> newDelegate )
    {
        MNameServer<K,V> delegate = MNameServer.<K,V>create();
        delegate.addDelegate( newDelegate );
        
        MMeshBaseNameServer<K,V> ret = new MMeshBaseNameServer<K,V>(
                delegate );
        return ret;
    }
    
    /**
     * Constructor for subclasses only, use factory method.
     * 
     * @param delegate the underlying NameServer to which we delegate
     */
    protected MMeshBaseNameServer(
            WritableNameServer<K,V> delegate )
    {
        theDelegate = delegate;
    }

    /**
     * Obtain an already existing value for a provided key.
     * 
     * @param key the key for which we want to obtain a value
     * @return the already existing value, or null
     */
    public V get(
            K key )
    {
        return theDelegate.get( key );
    }

    /**
     * Obtain the keys for an existing value. This is the opposite operation
     * of {@link #get}. Depending on the implementation of this interface,
     * this operation may take a long time.
     * 
     * @param value the value whose keys need to be found
     * @return an Iterator over the keys
     */
    public Iterator<K> reverseGet(
            V value )
    {
        return theDelegate.reverseGet( value );
    }

    /**
     * Obtain a set of the currently known keys. This follows the pattern in
     * java.util.HashMap.
     *
     * @return a set of the currently known keys
     */
    public Set<K> keySet()
    {
        return theDelegate.keySet();
    }

    /**
     * Obtain a Collection of the currently known values. This follows the pattern in
     * java.util.HashMap.
     *
     * @return a set of the currently known values
     */
    public Collection<V> values()
    {
        return theDelegate.values();
    }

    /**
     * Determine whether the number of key-value pairs in this SmartFactory is zero.
     *
     * @return true if there are no key-value paris in this SmartFactory
     */
    public boolean isEmpty()
    {
        return theDelegate.isEmpty();
    }

    /**
     * The number of known key-value pairs in this SmartFactory at this time.
     *
     * @return the number of known key-value pairs
     */
    public int size()
    {
        return theDelegate.size();
    }

    /**
     * Add a new key and a new value.
     *
     * @param key the key
     * @param value the value for the key
     * @return the old value at this key, if any
     */
    public V put(
            K key,
            V value )
    {
        return theDelegate.put( key, value );
    }

    /**
     * Remove a key-value pair that was previously created. This does not affect
     * values that are currently still being constructed. The semantics of
     * &quot;remove&quot; for a SmartFactory imply &quot;deletion&quot; of the
     * object as well. Correspondingly, if the removed Object supports the
     * {@link org.infogrid.util.LiveDeadObject LiveDeadObject} object, this
     * implementation will invoke the <code>die()</code> method there.
     *
     * @param key the key of the key-value pair to be removed
     * @return the value of the key-value pair to be removed, if found
     */
    public V remove(
            K key )
    {
        return theDelegate.remove( key );
    }

    /**
     * Remove a key-value pair that was previously created. This does not affect
     * values that are currently still being constructed. The semantics of
     * &quot;remove&quot; for a SmartFactory imply &quot;deletion&quot; of the
     * object as well. The provided cleanupCode can be used to implement those
     * semantics, e.g. in order to invoke the die() method.
     *
     * @param key the key of the key-value pair to be removed
     * @param cleanupCode the cleanup code to run, if any
     * @return the value of the key-value pair to be removed, if found
     */
    public V remove(
            K                 key,
            Invocable<V,Void> cleanupCode )
    {
        return theDelegate.remove( key, cleanupCode );
    }
    
    /**
     * The underlying NameServer.
     */
    protected WritableNameServer<K,V> theDelegate;
}
