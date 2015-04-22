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

package org.infogrid.meshbase.store.test;

import java.util.ArrayList;
import org.infogrid.store.Store;
import org.infogrid.store.StoreListener;
import org.infogrid.store.StoreValue;

/**
 * StoreListener that records the events. Useful for several of the tests.
 */
public class RecordingStoreListener
        implements
            StoreListener
{
    /**
     * A put operation was performed. This indicates either a
     * <code>Store.put</code> or a <code>Store.putOrUpdate</code> operation
     * in which an actual <code>put</code> was performed.
     *
     * @param store the Store that emitted this event
     * @param value the StoreValue that was put
     */
    @Override
    public void putPerformed(
            Store      store,
            StoreValue value )
    {
        thePuts.add( value.getKey() );
    }

    /**
     * An update operation was performed. This indicates either a
     * <code>Store.update</code> or a <code>Store.putOrUpdate</code> operation
     * in which an actual <code>update</code> was performed.
     *
     * @param store the Store that emitted this event
     * @param value the StoreValue that was updated
     */
    @Override
    public void updatePerformed(
            Store      store,
            StoreValue value )
    {
        theUpdates.add( value.getKey() );
    }

    /**
     * A get operation was performed.
     *
     * @param store the Store that emitted this event
     * @param value the StoreValue that was obtained
     */
    @Override
    public void getPerformed(
            Store      store,
            StoreValue value )
    {
        theGets.add( value.getKey() );
    }

    /**
     * A get operation was attempted but not value could be found.
     *
     * @param store the Store that emitted this event
     * @param key the key that was attempted
     */
    @Override
    public void getFailed(
            Store  store,
            String key )
    {
        theFailedGets.add( key );
    }

    /**
     * A delete operation was performed.
     *
     * @param store the Store that emitted this event
     * @param key the key with which the data element was stored
     */
    @Override
    public void deletePerformed(
            Store  store,
            String key )
    {
        theDeletes.add( key );
    }

    /**
     * A delete-all operation was performed.
     *
     * @param store the Store that emitted this event
     * @param prefix if given, indicates the prefix of all keys that were deleted. If null, indicates &quot;all&quot;.
     */
    @Override
    public void deleteAllPerformed(
            Store  store,
            String prefix )
    {
        theAllDeletes.add( prefix );
    }

    /**
     * Reset the listener.
     */
    public void reset()
    {
        thePuts.clear();
        theUpdates.clear();
        theGets.clear();
        theFailedGets.clear();
        theDeletes.clear();
        theAllDeletes.clear();
    }

    protected ArrayList<String> thePuts       = new ArrayList<String>();
    protected ArrayList<String> theUpdates    = new ArrayList<String>();
    protected ArrayList<String> theGets       = new ArrayList<String>();
    protected ArrayList<String> theFailedGets = new ArrayList<String>();
    protected ArrayList<String> theDeletes    = new ArrayList<String>();
    protected ArrayList<String> theAllDeletes = new ArrayList<String>();
}