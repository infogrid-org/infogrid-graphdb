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
import org.infogrid.mesh.set.active.TraversalActiveTraversalPathSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.logging.Log;

/**
 * Tests the TraversalActiveTraversalPathSet over a sequence of two RoleTypes.
 */
public class ActiveTraversalPathSetTest1
    extends
        AbstractActiveTraversalPathSetTest
{
    /**
     * Run the test.
     * 
     * @throws Exception all sorts of things may go wrong in a test
     */
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
        MeshObject arg1  = life.createMeshObject( typeAA );
        MeshObject arg2  = life.createMeshObject( typeAA );
        MeshObject arg3  = life.createMeshObject( typeAA );
        MeshObject arg4  = arg3;
        MeshObject arg41 = life.createMeshObject( typeAA );

        root.setPropertyValue(  typeX, StringValue.create( "root" ));
        id1.setPropertyValue(   typeX, StringValue.create( "idea 1" ));
        id2.setPropertyValue(   typeX, StringValue.create( "idea 2" ));
        id3.setPropertyValue(   typeX, StringValue.create( "idea 3" ));
        id4.setPropertyValue(   typeX, StringValue.create( "idea 4" ));
        arg1.setPropertyValue(  typeX, StringValue.create( "argument 1" ));
        arg2.setPropertyValue(  typeX, StringValue.create( "argument 2" ));
        arg3.setPropertyValue(  typeX, StringValue.create( "argument 3" ));
        arg41.setPropertyValue( typeX, StringValue.create( "argument 41" ));

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
                arg1,
                arg2,
                arg3
        };

        tx.commitTransaction();

        //

        log.info( "Set up TraversalActiveTraversalPathSet and listeners" );

        TraversalSpecification spec = SequentialCompoundTraversalSpecification.create(
                        new RoleType[] {
                                typeAR1A.getDestination(),
                                typeAR2A.getDestination()
                        } );

        TraversalActiveTraversalPathSet monitor1 = theMeshObjectSetFactory.createActiveTraversalPathSet(
                root,
                spec );

        ActiveTraversalPathSetTestListener listener1 = new ActiveTraversalPathSetTestListener( "Listener#1", monitor1, log );

        //

        log.info( "check what's in the set initially" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        checkTraversalPathSet(
                monitor1,
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

        checkTraversalPathSet(
                monitor1,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" }
                },
                "incorrect set" );

        checkEquals( listener1.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener1.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener1.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener1.getReorderCounter(),  0, "wrong number of reorder events" );
        listener1.reset();

        //

        log.info( "adding on the first level, bottom-up" );

        tx = theMeshBase.createTransactionNow();

        arg4.relateAndBless( typeAR2A.getSource(), id4 );
        id4.relateAndBless( typeAR1A.getSource(), root );

        tx.commitTransaction();

        //

        log.info( "check the set" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        checkTraversalPathSet(
                monitor1,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" }
                },
                "incorrect set" );

        checkEquals( listener1.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener1.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener1.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener1.getReorderCounter(),  0, "wrong number of reorder events" );
        listener1.reset();

        //

        log.info( "adding on second level only" );

        tx = theMeshBase.createTransactionNow();

        arg41.relateAndBless( typeAR2A.getSource(), id4 );

        tx.commitTransaction();

        //

        log.info( "check the set" ); // FIXME: this does not test that we indeed found the right set, just that the size of the set is right

        checkTraversalPathSet(
                monitor1,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 2", "argument 2" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" },
                        { "idea 4", "argument 41" }
                },
                "incorrect set" );

        checkEquals( listener1.getAddCounter(),      1, "wrong number of add events" );
        checkEquals( listener1.getRemoveCounter(),   0, "wrong number of remove events" );
        checkEquals( listener1.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener1.getReorderCounter(),  0, "wrong number of reorder events" );
        listener1.reset();

        //

        log.info( "remove an object in the middle" );

        tx = theMeshBase.createTransactionNow();

        life.deleteMeshObject( id2 );

        tx.commitTransaction();

        //

        log.info( "check the set" );

        checkTraversalPathSet(
                monitor1,
                typeX,
                new String[][] {
                        { "idea 1", "argument 1" },
                        { "idea 3", "argument 3" },
                        { "idea 4", "argument 3" },
                        { "idea 4", "argument 41" }
                },
                "incorrect set" );

        checkEquals( listener1.getAddCounter(),      0, "wrong number of add events" );
        checkEquals( listener1.getRemoveCounter(),   1, "wrong number of remove events" );
        checkEquals( listener1.getPropertyChangeCounter(), 0, "wrong number of property change events" );
        checkEquals( listener1.getReorderCounter(),  0, "wrong number of reorder events" );
        listener1.reset();

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        monitor1 = null;
        collectGarbage();

        //

        log.info( "check that the monitor unsubscribed correctly" );

        for( int i=0 ; i<allMeshObjects.length ; ++i ) {
            checkCondition(
                    !allMeshObjects[i].hasPropertyChangeListener(),
                    "object " + allMeshObjects[i].getIdentifier() + " still has subscribers" );
        }
    }

    /**
      * The main program.
      *
      * @param args the command-line arguments
      */
    public static void main(
             String [] args )
    {
        ActiveTraversalPathSetTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ActiveTraversalPathSetTest1( args );
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
     * @throws Exception all sorts of things may go wrong during a test
     */
    public ActiveTraversalPathSetTest1(
            String [] args )
        throws
            Exception
    {
        super( ActiveTraversalPathSetTest1.class  );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveTraversalPathSetTest1.class  );
}
