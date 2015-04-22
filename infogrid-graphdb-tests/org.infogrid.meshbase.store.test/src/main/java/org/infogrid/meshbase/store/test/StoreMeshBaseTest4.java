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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;

/**
 * Tests large amounts of data in a StoreMeshBase.
 */
public class StoreMeshBaseTest4
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
        final String PREFIX = "obj-";

        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        //

        log.info( "Creating MeshBase" );

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "storeMb" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idFact = mb.getMeshObjectIdentifierFactory();

        //
        
        log.info( "Collecting garbage and taking memory snapshot 1" );
        
        collectGarbage();

        Runtime rt = Runtime.getRuntime();

        long memFree1  = rt.freeMemory();
        long memMax1   = rt.maxMemory();
        long memTotal1 = rt.totalMemory();
        
        log.debug( "Memory free: " + memFree1 + ", max: " + memMax1 + ", total: " + memTotal1 + ", consumed: " + ( memTotal1 - memFree1 ) );

        //

        log.info( "Creating " + theTestSize + " objects, 100 objects per transaction" );
        
        Transaction tx = null;
        for( int i=0 ; i<theTestSize ; ++i ) {
            if( tx == null ) {
                tx = mb.createTransactionNow();
            }
            
            MeshObject created = life.createMeshObject( idFact.fromExternalForm( PREFIX + i ));
            
            if( i % 100 == 99 ) {
                tx.commitTransaction();
                tx = null;
            }
        }
        if( tx != null ) {
            tx.commitTransaction();
            tx = null;
        }
        
        //
        
        log.info( "Relating objects" );
        
        int quarter = theTestSize/4;
        for( int i=0 ; i<quarter ; ++i ) {
            MeshObjectIdentifier oneId   = idFact.fromExternalForm( PREFIX + i );
            MeshObjectIdentifier twoId   = idFact.fromExternalForm( PREFIX + ( i + quarter ));
            MeshObjectIdentifier threeId = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter ));
            MeshObjectIdentifier fourId  = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter + quarter ));

            MeshObject one   = mb.accessLocally( oneId );
            MeshObject two   = mb.accessLocally( twoId );
            MeshObject three = mb.accessLocally( threeId );
            MeshObject four  = mb.accessLocally( fourId );
            
            if( tx == null ) {
                tx = mb.createTransactionNow();
            }

            two.relate( three );
            four.relate( one );
            two.relate( four );

            if( i % 100 == 99 ) {
                tx.commitTransaction();
                tx = null;
            }
        }
        if( tx != null ) {
            tx.commitTransaction();
            tx = null;
        }
        
        //

        log.info( "Checking graph (1)" );

        for( int i=0 ; i<quarter ; ++i ) {
            MeshObjectIdentifier oneId   = idFact.fromExternalForm( PREFIX + i );
            MeshObjectIdentifier twoId   = idFact.fromExternalForm( PREFIX + ( i + quarter ));
            MeshObjectIdentifier threeId = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter ));
            MeshObjectIdentifier fourId  = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter + quarter ));

            MeshObject one   = mb.accessLocally( oneId );
            MeshObject two   = mb.accessLocally( twoId );
            MeshObject three = mb.accessLocally( threeId );
            MeshObject four  = mb.accessLocally( fourId );

            checkEqualsOutOfSequence(
                    one.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        four
                    },
                    "One has wrong relationships " + oneId );
            checkEqualsOutOfSequence(
                    two.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        three,
                        four
                    },
                    "One has wrong relationships " + twoId );
            checkEqualsOutOfSequence(
                    three.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        two
                    },
                    "One has wrong relationships " + threeId );
            checkEqualsOutOfSequence(
                    four.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        one,
                        two
                    },
                    "One has wrong relationships " + fourId );
        }

        //
        
        log.info( "Collecting garbage and taking memory snapshot 2" );
        
        collectGarbage();

        long memFree2  = rt.freeMemory();
        long memMax2   = rt.maxMemory();
        long memTotal2 = rt.totalMemory();
        
        log.debug( "Memory free: " + memFree2 + ", max: " + memMax2 + ", total: " + memTotal2 + ", consumed: " + ( memTotal2 - memFree2 ));
        
        //
        
        log.info( "Checking graph (2)" );
        
        for( int i=0 ; i<quarter ; ++i ) {
            MeshObjectIdentifier oneId   = idFact.fromExternalForm( PREFIX + i );
            MeshObjectIdentifier twoId   = idFact.fromExternalForm( PREFIX + ( i + quarter ));
            MeshObjectIdentifier threeId = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter ));
            MeshObjectIdentifier fourId  = idFact.fromExternalForm( PREFIX + ( i + quarter + quarter + quarter ));

            MeshObject one   = mb.accessLocally( oneId );
            MeshObject two   = mb.accessLocally( twoId );
            MeshObject three = mb.accessLocally( threeId );
            MeshObject four  = mb.accessLocally( fourId );

            checkEqualsOutOfSequence(
                    one.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        four
                    },
                    "One has wrong relationships " + oneId );
            checkEqualsOutOfSequence(
                    two.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        three,
                        four
                    },
                    "One has wrong relationships " + twoId );
            checkEqualsOutOfSequence(
                    three.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        two
                    },
                    "One has wrong relationships " + threeId );
            checkEqualsOutOfSequence(
                    four.traverseToNeighborMeshObjects().getMeshObjects(),
                    new MeshObject[] {
                        one,
                        two
                    },
                    "One has wrong relationships " + fourId );
        }

        //
        
        log.info( "Collecting garbage and taking memory snapshot 3" );
        
        collectGarbage();
        
        long memFree3  = rt.freeMemory();
        long memMax3   = rt.maxMemory();
        long memTotal3 = rt.totalMemory();
        
        log.debug( "Memory free: " + memFree3 + ", max: " + memMax3 + ", total: " + memTotal3 + ", consumed: " + ( memTotal3 - memFree3 ) );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreMeshBaseTest4 test = null;
        try {
            if( args.length < 1 ) {
                System.err.println( "Synopsis: [testsize as positive integer]" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreMeshBaseTest4( args );
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
    public StoreMeshBaseTest4(
            String [] args )
        throws
            Exception
    {
        super( StoreMeshBaseTest2.class );

        theTestSize = Integer.parseInt( args[0] );
    }

    /**
     * The number of MeshObjects to create for the test.
     */
    protected int theTestSize;

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest4.class);
}
