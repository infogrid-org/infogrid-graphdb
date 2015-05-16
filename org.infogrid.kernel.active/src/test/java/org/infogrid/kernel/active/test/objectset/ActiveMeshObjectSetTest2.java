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
import org.infogrid.mesh.set.MeshObjectSet;
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
  * Tests TraversalActiveMeshObjectSet contents and events, traversing across a sequence of RoleTypes.
  */
public class ActiveMeshObjectSetTest2
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

        /***** Build a MeshObject tree structure *****/
        /**
                           CPO_1 is at the root of the tree


                                    (AR2A)              (AR1A)
                             |----------------->CPO_4 ---------->CPO_7
                             |
                  (AR1A)     |
              |--------->CPO_2                          (AR1A)
              |              |                   |-------------->CPO_8
              |              |                   |
              |              |                   |
             CPO_1           |----------------->CPO_5
              |                     (AR2A)       |
              |                                  |-------------->CPO_9
              |                                         (AR1A)
              |--------->CPO_3 ---------------->CPO_6
                  (AR1A)            (AR2A)

        **/

        MeshObject theCPO_1  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_1" ));
        theCPO_1.setPropertyValue( typeX, StringValue.create( "CPO_1" ) );
        MeshObject theCPO_2  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_2" ) );
        theCPO_2.setPropertyValue( typeX, StringValue.create( "CPO_2" ) );
        MeshObject theCPO_3  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_3" ) );
        theCPO_3.setPropertyValue( typeX, StringValue.create( "CPO_3" ) );
        MeshObject theCPO_4  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_4" ) );
        theCPO_4.setPropertyValue( typeX, StringValue.create( "CPO_4" ) );
        MeshObject theCPO_5  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_5" ) );
        theCPO_5.setPropertyValue( typeX, StringValue.create( "CPO_5" ) );
        MeshObject theCPO_6  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_6" ) );
        theCPO_6.setPropertyValue( typeX, StringValue.create( "CPO_6" ) );
        MeshObject theCPO_7  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_7" ) );
        theCPO_7.setPropertyValue( typeX, StringValue.create( "CPO_7" ) );
        MeshObject theCPO_8  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_8" ) );
        theCPO_8.setPropertyValue( typeX, StringValue.create( "CPO_8" ) );
        MeshObject theCPO_9  = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_9" ) );
        theCPO_9.setPropertyValue( typeX, StringValue.create( "CPO_9" ) );
        MeshObject theCPO_10 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_10" ) );
        theCPO_10.setPropertyValue( typeX, StringValue.create( "CPO_10" ) );
        MeshObject theCPO_11 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_11" ) );
        theCPO_11.setPropertyValue( typeX, StringValue.create( "CPO_11" ) );
        MeshObject theCPO_12 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_12" ) );
        theCPO_12.setPropertyValue( typeX, StringValue.create( "CPO_12" ) );
        MeshObject theCPO_13 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_13" ) );
        theCPO_13.setPropertyValue( typeX, StringValue.create( "CPO_13" ) );
        MeshObject theCPO_14 = createMeshObject( life, typeAA, idFactory.fromExternalForm( "CPO_14" ) );
        theCPO_14.setPropertyValue( typeX, StringValue.create( "CPO_14" ) );

        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_2 );
        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_3 );
        theCPO_2.relateAndBless( typeAR2A.getSource(), theCPO_4 );
        theCPO_2.relateAndBless( typeAR2A.getSource(), theCPO_5 );
        theCPO_3.relateAndBless( typeAR2A.getSource(), theCPO_6 );
        theCPO_4.relateAndBless( typeAR1A.getSource(), theCPO_7 );
        theCPO_5.relateAndBless( typeAR1A.getSource(), theCPO_8 );
        theCPO_5.relateAndBless( typeAR1A.getSource(), theCPO_9 );

        tx.commitTransaction();

        //

        log.info( "Set up TraversalActiveMeshObjectSet and listeners" );

        TraversalActiveMeshObjectSet monitor = theMeshObjectSetFactory.createActiveMeshObjectSet(
                theCPO_1,
                SequentialCompoundTraversalSpecification.create( new RoleType[] {
                        typeAR1A.getSource(),
                        typeAR2A.getSource(),
                        typeAR1A.getSource()
                } ));

        ActiveMeshObjectSetTestListener listener1 = new ActiveMeshObjectSetTestListener( "Listener#1", monitor, log );

        //

        log.info( "check that we have the right content" );

        MeshObject [] otherside = monitor.getMeshObjects();

        checkMeshObjectSet( monitor, typeX, new String[] { "CPO_7", "CPO_8", "CPO_9" }, "wrong content in set" );
        // checkRootObjectSet( otherside, new String[] { "CPO_7", "CPO_8", "CPO_9" }, "wrong content in set" );

        //

        log.info( "creating relationships that should NOT trigger the monitors" );

        tx = theMeshBase.createTransactionAsap();

        /**
                                    (AR2A)              (AR1A)
                             |----------------->CPO_4 ---------->CPO_7
                             |
                  (AR1A)     |
              |--------->CPO_2                          (AR1A)
              |              |                   |-------------->CPO_8
              |              |                   |
              |              |                   |
             CPO_1           |----------------->CPO_5
              |                     (AR2A)       |
              |                                  |-------------->CPO_9
              |                                         (AR1A)
              |--------->CPO_3 ---------------->CPO_6
              |   (AR1A)     |      (AR2A)
              |              |
              |              |----------------->CPO_11
              |                      (AR2A)
              |
              |--------->CPO_10                 CPO_13--------------->CPO_14
                   (AR1A)                                  (AR1A)
        **/

        theCPO_1.relateAndBless( typeAR1A.getSource(), theCPO_10 );
        theCPO_3.relateAndBless( typeAR2A.getSource(), theCPO_11 );
        theCPO_13.relateAndBless( typeAR1A.getSource(), theCPO_14 );

        tx.commitTransaction();

        log.info( "Listener #1 reports: " + listener1.getAddCounter() );

        checkEquals( listener1.getAddCounter(), 0, "Listener 1 received incorrect number of add events" );
        listener1.reset();

        //

        log.info( "creating relationships that should trigger the monitors" );

        /**
                                    (AR2A)              (AR1A)
                             |----------------->CPO_4 ---------->CPO_7
                             |
                  (AR1A)     |
              |--------->CPO_2                          (AR1A)
              |              |                   |-------------->CPO_8
              |              |                   |
              |              |                   |
             CPO_1           |----------------->CPO_5
              |                     (AR2A)       |
              |                                  |-------------->CPO_9
              |                                         (AR1A)
              |--------->CPO_3 ---------------->CPO_6
              |   (AR1A)     |      (AR2A)
              |              |
              |              |----------------->CPO_11--------------->CPO_12
              |                      (AR2A)               (AR1A)
              |
              |--------->CPO_10---------------->CPO_13--------------->CPO_14
                  (AR1A)             (AR2A)               (AR1A) 
        **/

        tx = theMeshBase.createTransactionAsap();

        theCPO_11.relateAndBless( typeAR1A.getSource(), theCPO_12 );
        theCPO_10.relateAndBless( typeAR2A.getSource(), theCPO_13 );

        tx.commitTransaction();

        log.info( "Listener #1 theMeshBaseorts: " + listener1.getAddCounter() );

        checkEquals( listener1.getAddCounter(), 2, "Listener 1 received incorrect number of add events" );

        //

        log.info( "now removing one relationship" );

        /***** Perform a remove to test if the appropriate subMonitor listens to this remove *****/

        /**
                                    (AR2A)              (AR1A)
                             |----------------->CPO_4 ---------->CPO_7
                             |
                  (AR1A)     |
              |--------->CPO_2                          (AR1A)
              |              |                   |-------------->CPO_8
              |              |                   |
              |              |                   |
             CPO_1           |----------------->CPO_5
              |                     (AR2A)       |
              |                                  |-------------->CPO_9
              |                                         (AR1A)
              |--------->CPO_3 ---------------->CPO_6
              |   (AR1A)            (AR2A)
              |               
              |                                 CPO_11--------------->CPO_12
              |                                           (AR1A)
              |
              |--------->CPO_10---------------->CPO_13--------------->CPO_14
                  (AR1A)             (AR2A)               (AR1A)

        **/

        tx = theMeshBase.createTransactionAsap();
        
        theCPO_3.unrelate( theCPO_11 );
        
        tx.commitTransaction();

        log.info( "Listener #1 reports: " + listener1.getRemoveCounter() );

        checkEquals( listener1.getRemoveCounter(), 1, "Listener 1 received incorrect number of remove events" );

        //

        log.info( "now checking ActiveMeshObjectSet content" );

        checkMeshObjectSet( monitor, typeX, new String[] { "CPO_7", "CPO_8", "CPO_9", "CPO_14" }, "wrong content in set" );

        //

        log.info( "now checking ActiveMeshObjectSet.traverse( RoleType )" );

        MeshObjectSet resultOfTraverse = monitor.traverse( typeAR1A.getDestination() );
        checkMeshObjectSet( resultOfTraverse, typeX, new String[] { "CPO_4", "CPO_5", "CPO_13" }, "wrong content in set" );

        //

        log.info( "let the garbage collector do its work" );
        listener1 = null;
        monitor   = null;
        collectGarbage();

        //

        log.info( "make sure all monitors unsubscribe" );

        checkHasNoSubscribers( theCPO_1,  "CPO_1 still has subscribers" );
        checkHasNoSubscribers( theCPO_2,  "CPO_2 still has subscribers" );
        checkHasNoSubscribers( theCPO_3,  "CPO_3 still has subscribers" );
        checkHasNoSubscribers( theCPO_4,  "CPO_4 still has subscribers" );
        checkHasNoSubscribers( theCPO_5,  "CPO_5 still has subscribers" );
        checkHasNoSubscribers( theCPO_6,  "CPO_6 still has subscribers" );
        checkHasNoSubscribers( theCPO_7,  "CPO_7 still has subscribers" );
        checkHasNoSubscribers( theCPO_8,  "CPO_8 still has subscribers" );
        checkHasNoSubscribers( theCPO_9,  "CPO_9 still has subscribers" );
        checkHasNoSubscribers( theCPO_10, "CPO_10 still has subscribers" );
        checkHasNoSubscribers( theCPO_11, "CPO_11 still has subscribers" );
        checkHasNoSubscribers( theCPO_12, "CPO_12 still has subscribers" );
        checkHasNoSubscribers( theCPO_13, "CPO_13 still has subscribers" );
        checkHasNoSubscribers( theCPO_14, "CPO_14 still has subscribers" );
    }

    /**
     * Helper method to check whether a certain MeshObject has PropertyChangeListeners.
     * 
     * @param obj the MeshObject to check
     * @param msg message to print when check failed
     * @return true if check passed
     */
    protected boolean checkHasNoSubscribers(
            MeshObject obj,
            String     msg )
    {
        if( !obj.hasPropertyChangeListener() ) {
            return true;
        }

        reportError( msg, obj.propertyChangeListenersIterator() );
        return false;
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest2.class);
}

