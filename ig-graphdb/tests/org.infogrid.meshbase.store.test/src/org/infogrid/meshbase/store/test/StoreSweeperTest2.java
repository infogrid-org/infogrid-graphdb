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

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.meshbase.sweeper.Sweeper;
import org.infogrid.meshbase.sweeper.DefaultIterableSweeper;
import org.infogrid.meshbase.sweeper.ExpiresSweepPolicy;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;

/**
 * Tests whether auto-deletion works. Compare with SweeperTest2.
 */
public class StoreSweeperTest2
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        theSqlStore.initializeHard();

        IterableStoreMeshBase theMeshBase  = IterableStoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MB" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        Sweeper theSweeper = DefaultIterableSweeper.create( theMeshBase, ExpiresSweepPolicy.create());
        theMeshBase.setSweeper( theSweeper );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        //
        
        log.info( "Create a few MeshObjects" );
        
        long now  = System.currentTimeMillis();

        MeshObjectIdentifier extName1 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj1" );
        MeshObjectIdentifier extName2 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj2" );
        MeshObjectIdentifier extName3 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj3" );

        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1Never = life.createMeshObject( extName1, now, now, now, -1L );
        MeshObject obj2Sec   = life.createMeshObject( extName2, now, now, now, now + 1000L );
        MeshObject obj3Sec   = life.createMeshObject( extName3, now, now, now, now + 2000L );
        
        obj1Never.relate( obj2Sec );

        obj1Never = obj2Sec = obj3Sec = null;

        tx.commitTransaction();

        theMeshBase.clearMemoryCache();

        //
        
        log.info( "Checking they are all still there" );

        obj1Never = theMeshBase.findMeshObjectByIdentifier( extName1 );
        obj2Sec   = theMeshBase.findMeshObjectByIdentifier( extName2 );
        obj3Sec   = theMeshBase.findMeshObjectByIdentifier( extName3 );
        
        checkCondition( obj1Never != null, "objNever is dead" );
        checkCondition( obj2Sec   != null, "obj2Sec is dead" );
        checkCondition( obj3Sec   != null, "obj3Sec is dead" );
        checkEquals( obj1Never.traverseToNeighborMeshObjects().size(), 1, "objNever has wrong number of neighbors" );

        obj1Never = obj2Sec = obj3Sec = null;

        //
        
        log.info( "Waiting 1 second and checking that one is gone" );
        
        sleepFor( 1001L );

        now = System.currentTimeMillis();

        obj1Never = theMeshBase.findMeshObjectByIdentifier( extName1 );
        obj2Sec   = theMeshBase.findMeshObjectByIdentifier( extName2 );
        obj3Sec   = theMeshBase.findMeshObjectByIdentifier( extName3 );

        checkCondition( obj1Never != null, "objNever is dead: " + now + " vs. " + obj1Never );
        checkCondition( obj2Sec   == null, "obj2Sec is still alive: " + now + " vs. " + obj2Sec );
        checkCondition( obj3Sec   != null, "obj3Sec is dead: " + now + " vs. " + obj3Sec );
        checkEquals( obj1Never.traverseToNeighborMeshObjects().size(), 0, "objNever has wrong number of neighbors" );
        
        obj1Never = obj2Sec = obj3Sec = null;

        //
        
        log.info( "Waiting 2 second and checking that another is gone" );
        
        sleepFor( 1001L );

        now = System.currentTimeMillis();

        obj1Never = theMeshBase.findMeshObjectByIdentifier( extName1 );
        obj2Sec = theMeshBase.findMeshObjectByIdentifier( extName2 );
        obj3Sec = theMeshBase.findMeshObjectByIdentifier( extName3 );

        checkCondition( obj1Never != null, "objNever is dead: " + now + " vs. " + obj1Never );
        checkCondition( obj2Sec   == null, "obj2Sec is still alive: " + now + " vs. " + obj2Sec );
        checkCondition( obj3Sec   == null, "obj3Sec is still alive: " + now + " vs. " + obj3Sec );
        checkEquals( obj1Never.traverseToNeighborMeshObjects().size(), 0, "objNever has wrong number of neighbors" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreSweeperTest2 test = null;
        try {
            if( args.length > 0 ) {
                System.err.println( "Synopsis: <no argument>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreSweeperTest2( args );
            test.run();

        } catch( Throwable ex ) {
            log.error( ex );
            System.exit(1);
        }
        if( test != null ) {
            test.cleanup();
        }
        if( errorCount == 0 ) {
            log.info( "PASS" );
        } else {
            log.info( "FAIL (" + errorCount + " errors)" );
        }
        System.exit( errorCount );
    }

    /**
     * Constructor.
     *
     * @param args command-line arguments
     * @throws Exception anything can go wrong in a test
     */
    public StoreSweeperTest2(
            String [] args )
        throws
            Exception
    {
        super( StoreSweeperTest2.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreSweeperTest2.class);
}
