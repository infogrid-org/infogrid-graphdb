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

package org.infogrid.kernel.active.test.traversalpathset;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.active.m.TraversalActiveMTraversalPathSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests a sequence of two TraversalActiveTraversalPathSets.
 */
public class ActiveTraversalPathSetTest2
    extends
        AbstractActiveTraversalPathSetTest
{
    /**
     * Run the test.
     * 
     * @throws Exception all sorts of things may go wrong in a test
     */
//    @Test
    public void run()
        throws
            Exception
    {
        log.info( "Setting up objects" );

        Transaction tx = theMeshBase.createTransactionAsap();

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        MeshObject root  = life.createMeshObject( typeAA );
        MeshObject id1   = life.createMeshObject( typeAA );
        MeshObject id2   = life.createMeshObject( typeAA );
        MeshObject id3   = life.createMeshObject( typeAA );
        MeshObject id4   = life.createMeshObject( typeAA );
        MeshObject id5   = life.createMeshObject( typeAA );
        MeshObject arg1  = life.createMeshObject( typeAA );
        MeshObject arg2  = life.createMeshObject( typeAA );
        MeshObject arg3  = life.createMeshObject( typeAA );
        // "arg4" is the same as arg3
        MeshObject arg41 = life.createMeshObject( typeAA );
        MeshObject arg5  = life.createMeshObject( typeAA );

        root.setPropertyValue( typeX, StringValue.create( "root" ));

        id1.setPropertyValue( typeX, StringValue.create( "idea 1" ));
        id2.setPropertyValue( typeX, StringValue.create( "idea 2" ));
        id3.setPropertyValue( typeX, StringValue.create( "idea 3" ));
        id4.setPropertyValue( typeX, StringValue.create( "idea 4" ));
        id5.setPropertyValue( typeX, StringValue.create( "idea 5" ));

        arg1.setPropertyValue( typeX,  StringValue.create( "argument 1" ));
        arg2.setPropertyValue( typeX,  StringValue.create( "argument 2" ));
        arg3.setPropertyValue( typeX,  StringValue.create( "argument 3" ));
        arg41.setPropertyValue( typeX, StringValue.create( "argument 41" ));
        arg5.setPropertyValue( typeX,  StringValue.create( "argument 5" ));

        id1.relateAndBless( typeAR1A.getSource(), root );
        id2.relateAndBless( typeAR1A.getSource(), root );
        arg1.relateAndBless( typeAR2A.getSource(), id1 );
        arg2.relateAndBless( typeAR2A.getSource(), id2 );
        
        MeshObject [] allMeshObjects = new MeshObject[] {
                root,
                id1,
                id2,
                id3,
                id4,
                id5,
                arg1,
                arg2,
                arg3,
                arg41,
                arg5
        };

        tx.commitTransaction();

        //

        log.info( "Set up TraversalActiveTraversalPathSets and listeners" );

        TraversalActiveMTraversalPathSet monitor1 = theMeshObjectSetFactory.createActiveTraversalPathSet(
                root,
                typeAR1A.getDestination() );

        ActiveTraversalPathSetTestListener listener1 = new ActiveTraversalPathSetTestListener( "Listener#1", monitor1, log );

        TraversalActiveMTraversalPathSet monitor2 = theMeshObjectSetFactory.createActiveTraversalPathSet(
                monitor1,
                typeAR2A.getDestination() );

        ActiveTraversalPathSetTestListener listener2 = new ActiveTraversalPathSetTestListener( "Listener#2", monitor2, log );


        //

        log.info( "check what's in the set initially" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        dumpTraversalPathSet( monitor2, "tp1", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" }
                },
                "incorrect initial set" );

        //

        log.info( "adding on the first level, top-down" );

        tx = theMeshBase.createTransactionNow();

        id3.relateAndBless( typeAR1A.getSource(), root );
        arg3.relateAndBless( typeAR2A.getSource(), id3 );

        tx.commitTransaction();

        //

        log.info( "check the set" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        dumpTraversalPathSet( monitor2, "tp2", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "adding on the first level, bottom-up" );

        tx = theMeshBase.createTransactionNow();

        arg3.relateAndBless( typeAR2A.getSource(), id4 );
        id4.relateAndBless( typeAR1A.getSource(), root );

        tx.commitTransaction();

        //

        log.info( "check the set" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        dumpTraversalPathSet( monitor2, "tp3", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "adding on second level only" );

        tx = theMeshBase.createTransactionNow();

        arg41.relateAndBless( typeAR2A.getSource(), id4 );

        tx.commitTransaction();

        //

        log.info( "check the set" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        dumpTraversalPathSet( monitor2, "tp4", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" },
                        { "idea 4", "argument 41" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "remove an object in the middle" );

        tx = theMeshBase.createTransactionNow();

        life.deleteMeshObject( id2 );

        tx.commitTransaction();

        //

        log.info( "check the set" );

        dumpTraversalPathSet( monitor2, "tp5", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" },
                        { "idea 4", "argument 41" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      0, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   1, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "remove two objects at the ends and add another" );

        tx = theMeshBase.createTransactionNow();

        life.deleteMeshObject( arg1 );
        life.deleteMeshObject( arg41 );

        id5.relateAndBless( typeAR1A.getSource(), root );
        arg5.relateAndBless( typeAR2A.getSource(), id5 );

        tx.commitTransaction();

        //

        log.info( "check the set" );

        dumpTraversalPathSet( monitor2, "tp6", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" },
                        { "idea 5", "argument 5" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   2, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "remove the shared object at the end" );

        tx = theMeshBase.createTransactionNow();

        life.deleteMeshObject( arg3 );

        tx.commitTransaction();

        //

        log.info( "check the set" );

        dumpTraversalPathSet( monitor2, "tp7", typeX, log );
        checkTraversalPathSet(
                monitor2,
                typeX,
                new String[][] {
                        { "idea 5", "argument 5" }
                },
                "incorrect set" );

        checkEquals( listener2.getAddCounter(),      0, "wrong number of add events" );
        checkEquals( listener2.getRemoveCounter(),   2, "wrong number of remove events" );
        checkEquals( listener2.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener2.getReorderCounter(),  0, "wrong number of reorder events" );
        listener2.reset();

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        listener2 = null;
        monitor1 = null;
        monitor2 = null;
        collectGarbage();

        //

        log.info( "check that the monitor unsubscribed correctly" );

        for( int i=0 ; i<allMeshObjects.length ; ++i ) {
            checkCondition( !allMeshObjects[i].hasPropertyChangeListener(), "object " + allMeshObjects[i].getIdentifier() + " still has subscribers" );
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveTraversalPathSetTest2.class  );
}
