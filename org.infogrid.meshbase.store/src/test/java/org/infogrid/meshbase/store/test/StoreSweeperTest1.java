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
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.meshbase.sweeper.Sweeper;
import org.infogrid.meshbase.sweeper.DefaultIterableSweeper;
import org.infogrid.meshbase.sweeper.NotReadForLongerThanSweepPolicy;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests the NotReadForLongerThanSweepPolicy.
 */
public class StoreSweeperTest1
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
        theSqlStore.initializeHard();

        IterableStoreMeshBase theMeshBase  = IterableStoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "meshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        Sweeper theSweeper = DefaultIterableSweeper.create( theMeshBase, NotReadForLongerThanSweepPolicy.create( 1000L ));
        theMeshBase.setSweeper( theSweeper );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        //
        
        log.info( "Creating some objects" );
        
        long now = System.currentTimeMillis();
        
        MeshObjectIdentifier extName1 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj1" );
        MeshObjectIdentifier extName2 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj2" );
        MeshObjectIdentifier extName3 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj3" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1 = life.createMeshObject( extName1, TestSubjectArea.AA, now, now, now, -1L );
        MeshObject obj2 = life.createMeshObject( extName2, TestSubjectArea.AA, now, now, now, -1L );
        MeshObject obj3 = life.createMeshObject( extName3, TestSubjectArea.AA, now, now, now, -1L );
        
        tx.commitTransaction();

        //
        
        log.info( "Waiting for a bit, then touching some of the objects" );
        
        Thread.sleep( 4000L );
        
        Object foo = obj1.getRoles();
        foo = obj3.getPropertyValue( TestSubjectArea.A_X );
        
        obj1 = null;
        obj2 = null;
        obj3 = null;
        
        //
        
        log.info( "Checking that obj2 is gone" );
        
        obj1 = theMeshBase.findMeshObjectByIdentifier( extName1 );
        obj2 = theMeshBase.findMeshObjectByIdentifier( extName2 );
        obj3 = theMeshBase.findMeshObjectByIdentifier( extName3 );
        
        checkObject( obj1, "obj1 not here" );
        checkCondition( obj2 == null, "obj2 found" );
        checkObject( obj3, "obj3 not here" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreSweeperTest1.class );
}
