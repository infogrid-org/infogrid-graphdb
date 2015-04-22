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

import java.io.InputStream;
import java.util.Iterator;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.externalized.xml.BulkExternalizedMeshObjectXmlEncoder;
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.util.logging.Log;

/**
 * Tests bulk loading.
 */
public class StoreBulkLoaderTest1
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
        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        //

        log.info( "Creating MeshBase" );

        IterableStoreMeshBase mb = IterableStoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                ModelBaseSingleton.getSingleton(),
                null,
                theSqlStore,
                rootContext );

        InputStream inStream = StoreBulkLoaderTest1.class.getResourceAsStream( "StoreBulkLoaderTest1.xml" );

        Transaction tx = mb.createTransactionNow();
        
        BulkExternalizedMeshObjectXmlEncoder theParser = new BulkExternalizedMeshObjectXmlEncoder();
        
        Iterator<? extends ExternalizedMeshObject> iter = theParser.bulkLoad(
                inStream,
                mb );

        while( iter.hasNext() ) {
            mb.getMeshBaseLifecycleManager().loadExternalizedMeshObject( iter.next() );
        }
        
        tx.commitTransaction();
        
        sleepFor( 2000L );
        collectGarbage();
        
        //
        
        checkEquals( mb.size(), 5+1, "Wrong number of MeshObjects found" ); // don't forget home object
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreBulkLoaderTest1 test = null;
        try {
            if( args.length < 1 ) {
                System.err.println( "Synopsis: <test size>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreBulkLoaderTest1( args );
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
    public StoreBulkLoaderTest1(
            String [] args )
        throws
            Exception
    {
        super( StoreBulkLoaderTest1.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreBulkLoaderTest1.class );
}
