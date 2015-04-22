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

package org.infogrid.kernel.active.test.objectset;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.active.TraversalActiveMeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests TraversalActiveMeshObjectSet contents and events, traversing over a single RoleType.
 */
public class ActiveMeshObjectSetTest1
    extends
        AbstractActiveMeshObjectSetTest
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

        MeshBaseLifecycleManager    life      = theMeshBase.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idFactory = theMeshBase.getMeshObjectIdentifierFactory();
        
        MeshObject theCPO_1 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_1" ));
        theCPO_1.setPropertyValue( typeX, StringValue.create( "This is CPO_1." ));

        MeshObject theCPO_2 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_2" ));
        theCPO_2.setPropertyValue( typeX, StringValue.create( "This is CPO_2." ));

        MeshObject theCPO_3 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_3" ));
        theCPO_3.setPropertyValue( typeX, StringValue.create( "This is CPO_3." ));

        tx.commitTransaction();

        //
        
        log.info( "Set up TraversalActiveMeshObjectSet and listeners" );

        TraversalActiveMeshObjectSet monitor = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_1,
                typeAR1A.getSource() );

        ActiveMeshObjectSetTestListener listener1 = new ActiveMeshObjectSetTestListener( "Listener#1", monitor, log );
        ActiveMeshObjectSetTestListener listener2 = new ActiveMeshObjectSetTestListener( "Listener#2", monitor, log );

        //

        log.info( "Relating MeshObjects should trigger the TraversalActiveMeshObjectSets" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_2 );
        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_3 );

        tx.commitTransaction();

        //

        log.info( "testing whether we have the right set content" );

        checkEquals( monitor.size(), 2, "wrong set of objects" );

        //

        log.info( "testing whether the events occurred" );

        log.info( "Listener #1 reports: " + listener1.getAddCounter() );
        log.info( "Listener #2 reports: " + listener2.getAddCounter() );

        checkEquals( listener1.getAddCounter(), 2, "Listener 1 received incorrect number of add events" );
        checkEquals( listener2.getAddCounter(), 2, "Listener 2 received incorrect number of add events" );

        //

        log.info( "now removing one relationship" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.unrelate( theCPO_2 );

        tx.commitTransaction();

        //

        log.info( "testing whether the events occurred" );

        log.info( "Listener #1 reports: " + listener1.getRemoveCounter() );
        log.info( "Listener #2 reports: " + listener2.getRemoveCounter() );

        checkEquals( listener1.getRemoveCounter(), 1, "Listener 1 received incorrect number of remove events" );
        checkEquals( listener2.getRemoveCounter(), 1, "Listener 2 received incorrect number of remove events" );

        //

        log.info( "now removing the other relationship" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.unrelate( theCPO_3 );

        tx.commitTransaction();

        //

        log.info( "testing whether the events occurred" );

        log.info( "Listener #1 reports: " + listener1.getRemoveCounter() );
        log.info( "Listener #2 reports: " + listener2.getRemoveCounter() );

        checkEquals( listener1.getRemoveCounter(), 2, "Listener 1 received incorrect number of remove events" );
        checkEquals( listener2.getRemoveCounter(), 2, "Listener 2 received incorrect number of remove events" );

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        listener2 = null;
        monitor = null;
        collectGarbage();

        //

        log.info( "check that the monitor unsubscribed correctly" );

        checkCondition( !theCPO_1.hasPropertyChangeListener(), "CPO_1 still has subscribers" );
        checkCondition( !theCPO_2.hasPropertyChangeListener(), "CPO_2 still has subscribers" );
        checkCondition( !theCPO_3.hasPropertyChangeListener(), "CPO_3 still has subscribers" );
    }

    /**
      * The main program.
      *
      * @param args the command-line arguments
      */
    public static void main(
             String [] args )
    {
        ActiveMeshObjectSetTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ActiveMeshObjectSetTest1( args );
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
     * @param args the command-line arguments
     * @throws Exception all sorts of things may go wrong during a test
     */
    public ActiveMeshObjectSetTest1(
            String [] args )
        throws
            Exception
    {
        super( ActiveMeshObjectSetTest1.class  );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest1.class);
}
