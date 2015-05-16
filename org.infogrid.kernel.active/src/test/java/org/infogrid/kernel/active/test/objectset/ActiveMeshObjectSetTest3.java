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
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests sending of PropertyChangeEvents by the TraversalActiveMeshObjectSet, traversing across a single RoleType.
 */
public class ActiveMeshObjectSetTest3
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

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        MeshObject theCPO_1 = life.createMeshObject( typeAA );
        theCPO_1.setPropertyValue( typeX, StringValue.create( "CPO_1" ) );
        MeshObject theCPO_2 = life.createMeshObject( typeAA );
        theCPO_2.setPropertyValue( typeX, StringValue.create( "CPO_2" ) );
        MeshObject theCPO_3 = life.createMeshObject( typeAA );
        theCPO_3.setPropertyValue( typeX, StringValue.create( "CPO_3" ) );

        tx.commitTransaction();

        //

        log.info( "Set up ActiveMeshObjectSets and listeners" );

        TraversalActiveMeshObjectSet monitor1 = null;

        monitor1 = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_1,
                typeAR1A.getSource() );

        ActiveMeshObjectSetTestListener listener1 = new ActiveMeshObjectSetTestListener( "Listener#1", monitor1, log );

        //

        log.info( "create new relationship and change property on root object" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_2 );
        theCPO_1.setPropertyValue( typeX, StringValue.create( "CPO_1-modified-1" ));

        tx.commitTransaction();

        checkEquals( listener1.getPropertyChangesCounter(), 0, "Listener 1 received incorrect number of property change events" );
        checkEquals( listener1.getRoleChangesCounter(), 1, "Listener 1 received incorrect number of role change events" );
        listener1.reset();

        //

        log.info( "now setting name of an object that should trigger property events in one" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_2.setPropertyValue( typeX, StringValue.create( "CPO_2-modified-3" ));

        tx.commitTransaction();

        checkEquals( listener1.getPropertyChangesCounter(), 1, "Listener 1 received incorrect number of property change events" );
        checkEquals( listener1.getRoleChangesCounter(), 0, "Listener 1 received incorrect number of role change events" );
        listener1.reset();

        //

        log.info( "create another new relationship and change property on root object" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_2.relateAndBless( typeAR2A.getSource(), theCPO_3 );
        theCPO_1.setPropertyValue( typeX, StringValue.create( "CPO_1-modified-2" ));

        tx.commitTransaction();

        checkEquals( listener1.getPropertyChangesCounter(), 0, "Listener 1 received incorrect number of property change events" );
        checkEquals( listener1.getRoleChangesCounter(), 1, "Listener 1 received incorrect number of role change events" );
        listener1.reset();


        //

        log.info( "now setting name of an object that should not trigger any events here" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_3.setPropertyValue( typeX, StringValue.create( "CPO_3-modified-4" ));

        tx.commitTransaction();

        checkEquals( listener1.getPropertyChangesCounter(), 0, "Listener 1 received incorrect number of property change events" );
        checkEquals( listener1.getRoleChangesCounter(), 0, "Listener 1 received incorrect number of role change events" );
        listener1.reset();

        //

        log.info( "breaking the chain, and now nothing should happen any more" );

        tx = theMeshBase.createTransactionAsap();

        theCPO_1.unrelate( theCPO_2 );

        theCPO_2.setPropertyValue( typeX, StringValue.create( "CPO_2-modified-5" ));
        theCPO_3.setPropertyValue( typeX, StringValue.create( "CPO_3-modified-6" ));

        tx.commitTransaction();

        checkEquals( listener1.getPropertyChangesCounter(), 0, "Listener 1 received incorrect number of property change events" );
        checkEquals( listener1.getRoleChangesCounter(), 0, "Listener 1 received incorrect number of role change events" );
        listener1.reset();

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        monitor1  = null;
        collectGarbage();

        //

        log.info( "check that the monitor unsubscribed correctly" );

        checkCondition( !theCPO_1.hasPropertyChangeListener(), "CPO_1 still has subscribers" );
        checkCondition( !theCPO_2.hasPropertyChangeListener(), "CPO_2 still has subscribers" );
        checkCondition( !theCPO_3.hasPropertyChangeListener(), "CPO_3 still has subscribers" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest3.class);
}
