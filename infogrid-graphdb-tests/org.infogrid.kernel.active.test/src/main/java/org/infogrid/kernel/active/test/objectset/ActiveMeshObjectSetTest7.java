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
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests TransitiveClosureTraversalActiveMeshObjectSet.
 */
public class ActiveMeshObjectSetTest7
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

        MeshObject theCPO_11 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_11" ) );
        theCPO_11.setPropertyValue( typeX, StringValue.create( "CPO_11" ) );
        MeshObject theCPO_12 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_12" ) );
        theCPO_12.setPropertyValue( typeX, StringValue.create( "CPO_12" ) );
        MeshObject theCPO_13 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_13" ) );
        theCPO_13.setPropertyValue( typeX, StringValue.create( "CPO_13" ) );
        MeshObject theCPO_21 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_21" ) );
        theCPO_21.setPropertyValue( typeX, StringValue.create( "CPO_21" ) );
        MeshObject theCPO_22 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_22" ) );
        theCPO_22.setPropertyValue( typeX, StringValue.create( "CPO_22" ) );
        MeshObject theCPO_23 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_23" ) );
        theCPO_23.setPropertyValue( typeX, StringValue.create( "CPO_23" ) );

        tx.commitTransaction();

        //

        log.info( "Set up TransitiveClosureTraversalActiveMeshObjectSet" );

        ActiveMeshObjectSet monitor = theMeshObjectSetFactory.createTransitiveClosureActiveMeshObjectSet(
                theCPO_11,
                typeAR1A.getSource() );

        ActiveMeshObjectSetTestListener listener1 = new ActiveMeshObjectSetTestListener(
                "Listener#1",
                monitor,
                log );

        //

        log.info( "check initial set" );

        checkMeshObjectSet(
                monitor,
                typeX,
                new String[] {
                        "CPO_11" },
                "just expecting the root object" );

        //

        log.info( "creating relationships from component 1" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_11.relateAndBless( typeAR1A.getSource(), theCPO_12 );
        theCPO_11.relateAndBless( typeAR1A.getSource(), theCPO_13 );

        tx.commitTransaction();

        checkEquals( listener1.getAddCounter(), 2, "listener 1 add events are wrong" );
        listener1.reset();

        checkMeshObjectSet(
                monitor,
                typeX,
                new String[] {
                        "CPO_11",
                        "CPO_12",
                        "CPO_13" },
                "expecting three objects" );

        //

        log.info( "adding a whole network" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_21.relateAndBless( typeAR1A.getSource(), theCPO_22 );
        theCPO_21.relateAndBless( typeAR1A.getSource(), theCPO_23 );
        theCPO_22.relateAndBless( typeAR1A.getSource(), theCPO_23 );
        theCPO_12.relateAndBless( typeAR1A.getSource(), theCPO_21 );

        tx.commitTransaction();

        checkEquals( listener1.getAddCounter(), 3, "listener 1 add events are wrong" );
        listener1.reset();

        //

        log.info( "check set" );

        checkMeshObjectSet(
                monitor,
                typeX,
                new String[] {
                        "CPO_11",
                        "CPO_12",
                        "CPO_13",
                        "CPO_21",
                        "CPO_22",
                        "CPO_23" },
                "wrong content for set" );

        //

        log.info( "remove one" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_11.unrelate( theCPO_13 );

        tx.commitTransaction();

        checkMeshObjectSet(
                monitor,
                typeX,
                new String[] {
                        "CPO_11",
                        "CPO_12",
                        "CPO_21",
                        "CPO_22",
                        "CPO_23" },
                "wrong content for set" );

        checkEquals( listener1.getRemoveCounter(), 1, "listener one should have gotten remove event" );
        listener1.reset();

        //

        log.info( "remove sub-net" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_12.unrelate( theCPO_21 );

        tx.commitTransaction();

        checkMeshObjectSet(
                monitor,
                typeX,
                new String[] {
                        "CPO_11",
                        "CPO_12" },
                "wrong content for set" );

        checkEquals( listener1.getRemoveCounter(), 3, "listener one should have gotten remove event" );
        listener1.reset();

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        monitor = null;
        collectGarbage();

        //

        log.info( "check that the active sets unsubscribed correctly" );

        checkCondition( !theCPO_11.hasPropertyChangeListener(), "CPO_11 still has subscribers" );
        checkCondition( !theCPO_12.hasPropertyChangeListener(), "CPO_12 still has subscribers" );
        checkCondition( !theCPO_13.hasPropertyChangeListener(), "CPO_13 still has subscribers" );
        checkCondition( !theCPO_21.hasPropertyChangeListener(), "CPO_21 still has subscribers" );
        checkCondition( !theCPO_22.hasPropertyChangeListener(), "CPO_22 still has subscribers" );
        checkCondition( !theCPO_23.hasPropertyChangeListener(), "CPO_23 still has subscribers" );
    }

    /**
     * The main program.
     *
     * @param args the command-line arguments
     */
    public static void main(
             String [] args )
    {
        ActiveMeshObjectSetTest7 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ActiveMeshObjectSetTest7( args );
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
    public ActiveMeshObjectSetTest7(
            String [] args )
        throws
            Exception
    {
        super( ActiveMeshObjectSetTest7.class  );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest7.class);
}
