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
import org.infogrid.mesh.set.active.TraversalActiveMeshObjectSet;
import org.infogrid.mesh.set.active.m.CompositeActiveMMeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests CompositeActiveMeshObjectSet as a union of two TraversalActiveMeshObjectSets.
 */
public class ActiveMeshObjectSetTest6
    extends
        AbstractActiveMeshObjectSetTest
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

        log.info( "Set up two TraversalActiveMeshObjectSets, composite set and listener" );

        TraversalActiveMeshObjectSet monitor1 = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_11,
                typeAR1A.getSource() );
        TraversalActiveMeshObjectSet monitor2 = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_21,
                typeAR1A.getSource() );
        CompositeActiveMMeshObjectSet composite = theMeshObjectSetFactory.createActiveMeshObjectSetUnification(
                new ActiveMeshObjectSet [] {
                        monitor1,
                        monitor2 } );

        ActiveMeshObjectSetTestListener listener1    = new ActiveMeshObjectSetTestListener( "Listener#1",        monitor1,  log );
        ActiveMeshObjectSetTestListener listener2    = new ActiveMeshObjectSetTestListener( "Listener#2",        monitor2,  log );
        ActiveMeshObjectSetTestListener compListener = new ActiveMeshObjectSetTestListener( "CompositeListener", composite, log );

        //

        log.info( "check initial set" );

        checkMeshObjectSet( composite, typeX, new String[0], "not empty" );

        //

        log.info( "creating relationships from component 1" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_11.relateAndBless( typeAR1A.getSource(), theCPO_12 );
        theCPO_11.relateAndBless( typeAR1A.getSource(), theCPO_13 );

        tx.commitTransaction();

        checkEquals( listener1.getAddCounter(),    2, "listener 1 add events are wrong" );
        checkEquals( compListener.getAddCounter(), 2, "composite listener add events are wrong" );
        listener1.reset();
        compListener.reset();

        //

        log.info( "creating relationships from component 2" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_21.relateAndBless( typeAR1A.getSource(), theCPO_22 );
        theCPO_21.relateAndBless( typeAR1A.getSource(), theCPO_23 );

        tx.commitTransaction();

        checkEquals( listener2.getAddCounter(),    2, "listener 2 add events are wrong" );
        checkEquals( compListener.getAddCounter(), 2, "composite listener add events are wrong" );
        listener1.reset();
        compListener.reset();

        //

        log.info( "check set" );

        checkMeshObjectSet( 
                composite,
                typeX,
                new String[] {
                        "CPO_12",
                        "CPO_13",
                        "CPO_22",
                        "CPO_23" },
                "wrong content for composite set" );

        //

        log.info( "remove one" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_11.unrelate( theCPO_12 );

        tx.commitTransaction();

        checkMeshObjectSet(
                composite,
                typeX,
                new String[] {
                        "CPO_13",
                        "CPO_22",
                        "CPO_23" },
                "wrong content for composite set" );

        checkEquals( listener1.getRemoveCounter(),    1, "listener one should have gotten remove event" );
        checkEquals( compListener.getRemoveCounter(), 1, "comp listener should have gotten remove event" );
        listener1.reset();
        compListener.reset();

        //

        log.info( "remove another" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_21.unrelate( theCPO_23 );

        tx.commitTransaction();

        checkMeshObjectSet(
                composite,
                typeX,
                new String[] {
                        "CPO_13",
                        "CPO_22" },
                "wrong content for composite set" );

        checkEquals( listener2.getRemoveCounter(),    1, "listener two should have gotten remove event" );
        checkEquals( compListener.getRemoveCounter(), 1, "comp listener should have gotten remove event" );
        listener2.reset();
        compListener.reset();

        //

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        listener2 = null;
        compListener = null;
        composite = null;
        monitor1  = null;
        monitor2  = null;
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

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest6.class);
}
