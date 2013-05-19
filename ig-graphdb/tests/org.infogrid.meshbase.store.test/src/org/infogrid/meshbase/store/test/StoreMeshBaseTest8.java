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

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;

/**
 * Deleting and recreating within the same transaction.
 */
public class StoreMeshBaseTest8
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Override
    public void run()
        throws
            Exception
    {
        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        RecordingStoreListener listener = new RecordingStoreListener();
        theSqlStore.addDirectStoreListener( listener );

        //

        log.info( "Creating MeshBase" );

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idFact = mb.getMeshObjectIdentifierFactory();

        MeshObjectIdentifier oneId   = idFact.fromExternalForm( "one" );
        MeshObjectIdentifier twoId   = idFact.fromExternalForm( "two" );
        MeshObjectIdentifier threeId = idFact.fromExternalForm( "three" );
        
        //

        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        MeshObject one   = life.createMeshObject( oneId, TestSubjectArea.AA );
        MeshObject two   = life.createMeshObject( twoId, TestSubjectArea.AA );
        MeshObject three = life.createMeshObject( threeId, TestSubjectArea.AA );
        
        two.relate( three );
        
        tx.commitTransaction();

        one = null;
        two = null;
        three = null;

        listener.reset();
        mb.clearMemoryCache();
        collectGarbage();

        //
        
        log.info( "Modifying" );
        
        tx = mb.createTransactionNow();
        
        one   = mb.find( oneId );
        two   = mb.find( twoId );
        three = mb.find( threeId );
        
        life.deleteMeshObject( one );
        life.deleteMeshObject( three );
        
        MeshObject newOne = life.createMeshObject( oneId, TestSubjectArea.B );
        
        tx.commitTransaction();
        
        checkEquals( two.getNeighborMeshObjectIdentifiers().length, 0, "leftover neighbors" );
        checkCondition( !newOne.isBlessedBy( TestSubjectArea.AA ), "Still blessed by AA" );
        checkCondition( newOne.isBlessedBy( TestSubjectArea.B ), "Not blessed by B" );
        
        newOne = null;
        
        mb.clearMemoryCache();
        collectGarbage();

        newOne = mb.findMeshObjectByIdentifier( oneId );
        checkCondition( !newOne.isBlessedBy( TestSubjectArea.AA ), "After restore: still blessed by AA" );
        checkCondition( newOne.isBlessedBy( TestSubjectArea.B ), "After restore: not blessed by B" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreMeshBaseTest1 test = null;
        try {
            if( args.length < 1 ) {
                System.err.println( "Synopsis: <test size>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreMeshBaseTest1( args );
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
    public StoreMeshBaseTest8(
            String [] args )
        throws
            Exception
    {
        super( StoreMeshBaseTest8.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest8.class );
    

}
