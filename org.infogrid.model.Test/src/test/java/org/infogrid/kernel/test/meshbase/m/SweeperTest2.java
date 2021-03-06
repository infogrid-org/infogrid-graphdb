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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.sweeper.DefaultIterableSweeper;
import org.infogrid.meshbase.sweeper.ExpiresSweepPolicy;
import org.infogrid.meshbase.sweeper.Sweeper;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests whether MeshObjects respect their expires property. Compare with StoreMeshBaseTest4.
 */
public class SweeperTest2
        extends
            AbstractMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    @Test
    public void run()
        throws
            Exception
    {
        IterableMeshBase theMeshBase = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase"), theModelBase, null, rootContext );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Sweeper theSweeper = DefaultIterableSweeper.create( theMeshBase, ExpiresSweepPolicy.create());
        theMeshBase.setSweeper( theSweeper );

        //
        
        log.info( "Create a few MeshObjects" );
        
        long now = System.currentTimeMillis();

        MeshObjectIdentifier extName1 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj1" );
        MeshObjectIdentifier extName2 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj2" );
        MeshObjectIdentifier extName3 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj3" );

        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1Never = life.createMeshObject( extName1, now, now, now, -1L );
        MeshObject obj2Sec  = life.createMeshObject( extName2, now, now, now, now + 1000L );
        MeshObject obj3Sec  = life.createMeshObject( extName3, now, now, now, now + 2000L );
        
        obj1Never.relate( obj2Sec );

        obj1Never = obj2Sec = obj3Sec = null;
        
        tx.commitTransaction();

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
        
        log.info( "Waiting 2 seconds and checking that another is gone" );
        
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

    // Our Logger
    private static Log log = Log.getLogInstance( SweeperTest2.class );
}
