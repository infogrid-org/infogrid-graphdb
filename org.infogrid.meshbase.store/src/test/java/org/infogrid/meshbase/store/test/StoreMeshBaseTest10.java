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
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Default values are not re-set upon restore.
 */
public class StoreMeshBaseTest10
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
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
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

        MeshObjectIdentifier ID1 = idFact.fromExternalForm( "object1" );
        MeshObjectIdentifier ID2 = idFact.fromExternalForm( "object2" );
        
        //

        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        MeshObject obj1 = life.createMeshObject( ID1, TestSubjectArea.AA );
        MeshObject obj2 = life.createMeshObject( ID2, TestSubjectArea.AA );
        
        obj2.setPropertyValue( TestSubjectArea.AA_Y, FloatValue.create( 1.11 ));
        
        tx.commitTransaction();

        checkEquals( obj1.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create( 12.34 ), "obj1 has wrong value" );
        checkEquals( obj2.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create(  1.11 ), "obj2 has wrong value" );

        //
        
        log.info( "Clearing cache, and loading MeshObjects again" );
        
        obj1 = null;
        obj2 = null;
        
        mb.clearMemoryCache();
        
        obj1 = mb.findMeshObjectByIdentifier( ID1 );
        obj2 = mb.findMeshObjectByIdentifier( ID2 );
        
        //
        
        log.info( "Checking property values" );
        
        checkEquals( obj1.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create( 12.34 ), "obj1 has wrong value" );
        checkEquals( obj2.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create(  1.11 ), "obj2 has wrong value" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest10.class );
}
