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
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that TraversalActiveMeshObjectSet has set semantics (any object only contained once),
 * even if multiple traversal paths lead to the same object.
 */
public class ActiveMeshObjectSetTest5
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

        MeshObject theCPO_1 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_1" ) );
        theCPO_1.setPropertyValue( typeX, StringValue.create( "CPO_1" ));
        MeshObject theCPO_2 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_2" ) );
        theCPO_2.setPropertyValue( typeX, StringValue.create( "CPO_2" ));
        MeshObject theCPO_3 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_3" ) );
        theCPO_3.setPropertyValue( typeX, StringValue.create( "CPO_3" ));
        MeshObject theCPO_4 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_4" ) );
        theCPO_4.setPropertyValue( typeX, StringValue.create( "CPO_4" ));

        tx.commitTransaction();

        //

        log.info( "Set up TraversalActiveMeshObjectSet and listener" );

        TraversalActiveMeshObjectSet monitor = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_1,
                SequentialCompoundTraversalSpecification.create(
                        new RoleType[] {
                                typeAR1A.getSource(),
                                typeAR1A.getSource()
                } ));

        ActiveMeshObjectSetTestListener listener1 = new ActiveMeshObjectSetTestListener( "Listener#1", monitor, log );

        //

        log.info( "creating relationships that should NOT trigger the listener" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_2.relateAndBless( typeAR1A.getSource(), theCPO_4 );
        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_3 );

        tx.commitTransaction();

        checkEquals( listener1.getAddCounter(), 0, "should not have gotten event" );
        listener1.reset();

        //

        log.info( "creating relationships that should trigger the listener" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_2 );
        theCPO_3.relateAndBless( typeAR1A.getSource(), theCPO_4 );

        tx.commitTransaction();

        checkEquals( listener1.getAddCounter(), 1, "should have gotten exactly one event" );
        listener1.reset();

        //

        log.info( "testing whether we have the right content" );

        checkMeshObjectSet( monitor, typeX, new String[] { "CPO_4" }, "wrong content in set" );

        //

        log.info( "now removing one (redundant) relationship" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.unrelate( theCPO_2 );

        tx.commitTransaction();

        checkMeshObjectSet( monitor, typeX, new String[] { "CPO_4" }, "wrong content in set" );

        checkEquals( listener1.getRemoveCounter(), 0, "should have gotten no event" );
        listener1.reset();

        //

        log.info( "now removing the other (non-redundant) relationship" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.unrelate( theCPO_3 );

        tx.commitTransaction();

        checkMeshObjectSet( monitor, typeX, new String[] {}, "wrong content in set" );

        checkEquals( listener1.getRemoveCounter(), 1, "should have gotten one event" );
        listener1.reset();

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        monitor   = null;
        collectGarbage();

        //

        log.info( "check that the monitor unsubscribed correctly" );

        checkCondition( !theCPO_1.hasPropertyChangeListener(), "CPO_1 still has subscribers" );
        checkCondition( !theCPO_2.hasPropertyChangeListener(), "CPO_2 still has subscribers" );
        checkCondition( !theCPO_3.hasPropertyChangeListener(), "CPO_3 still has subscribers" );
        checkCondition( !theCPO_4.hasPropertyChangeListener(), "CPO_4 still has subscribers" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest5.class);
}
