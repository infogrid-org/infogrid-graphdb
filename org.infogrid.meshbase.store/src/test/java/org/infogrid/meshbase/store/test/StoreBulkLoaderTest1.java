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
import org.junit.Test;

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
    @Test
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

    // Our Logger
    private static Log log = Log.getLogInstance( StoreBulkLoaderTest1.class );
}
