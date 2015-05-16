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

import java.lang.ref.WeakReference;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Test that we do the right number of writes and reads.
 */
public class StoreMeshBaseTest1
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Test
    public void run()
        throws
            Exception
    {
        EntityType   typeAA = theModelBase.findEntityType(   "org.infogrid.model.Test", null, "AA" );
        PropertyType ptX    = theModelBase.findPropertyType( "org.infogrid.model.Test", null, "A",  "X" );

        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        RecordingStoreListener listener = new RecordingStoreListener();
        theSqlStore.addDirectStoreListener( listener );

        //

        log.info( "Creating MeshBase" );

        super.startClock();

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();

        checkEquals( listener.thePuts.size(),       1, "Wrong number of puts" );
        checkEquals( listener.theUpdates.size(),    0, "Wrong number of updates" );
        checkEquals( listener.theGets.size(),       0, "Wrong number of gets" );
        checkEquals( listener.theFailedGets.size(), 2, "Wrong number of failedGets" ); // twice for a non-existing home object
        checkEquals( listener.theDeletes.size(),    0, "Wrong number of deletes" );
        listener.reset();

        //

        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        MeshObject []           mesh  = new MeshObject[ theTestSize ];
        MeshObjectIdentifier [] names = new MeshObjectIdentifier[ theTestSize ];
        
        WeakReference [] refs = new WeakReference<?>[ theTestSize ];
        
        for( int i=0 ; i<mesh.length ; ++i ) {
            mesh[i]  = life.createMeshObject();
            names[i] = mesh[i].getIdentifier();
            refs[i]  = new WeakReference<MeshObject>( mesh[i] );
            
            if( i % 3 == 1 ) {
                mesh[i].bless( typeAA );
            } else if( i % 3 == 2 ) {
                mesh[i].bless( typeAA );
                mesh[i].setPropertyValue( ptX, StringValue.create( "Testing ... " + i ));
            }
        }

        tx.commitTransaction();
        tx = null;

        checkEquals( listener.thePuts.size(),       theTestSize, "Wrong number of puts" );
        checkEquals( listener.theUpdates.size(),    0,           "Wrong number of updates" );
        checkEquals( listener.theGets.size(),       0,           "Wrong number of gets" );
        checkEquals( listener.theFailedGets.size(), theTestSize, "Wrong number of failedGets" );
        checkEquals( listener.theDeletes.size(),    0,           "Wrong number of deletes" );
        listener.reset();

        //
        
        log.info( "Clearing cache, and loading MeshObjects again" );
        
        mesh = new MeshObject[ names.length ]; // forget old references
        mb.clearMemoryCache();
        collectGarbage();

        for( int i=0 ; i<refs.length ; ++i ) {
            checkCondition( refs[i].get() == null, "MeshObject " + i + " still found" );
        }
        
        for( int i=0 ; i<names.length ; ++i ) {
            mesh[i] = mb.findMeshObjectByIdentifier( names[i] );
            
            checkObject( mesh[i], "Could not retrieve MeshObject with Identifier " + names[i] );
        }

        checkEquals( listener.thePuts.size(),       0,            "Wrong number of puts" );
        checkEquals( listener.theUpdates.size(),    0,            "Wrong number of updates" );
        checkEquals( listener.theGets.size(),       names.length, "Wrong number of gets" );
        checkEquals( listener.theFailedGets.size(), 0,            "Wrong number of failedGets" );
        checkEquals( listener.theDeletes.size(),    0,            "Wrong number of deletes" );
        listener.reset();
    }

    /**
     * The number of MeshObjects to create for the test.
     */
    protected int theTestSize = 1000;

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest1.class );
    

}
