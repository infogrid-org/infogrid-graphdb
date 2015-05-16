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

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.MeshObjectCreatedEvent;
import org.infogrid.meshbase.transaction.MeshObjectDeletedEvent;
import org.infogrid.meshbase.transaction.MeshObjectLifecycleListener;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that the MeshBase creates the right lifecycle events.
 */
public class MeshBaseTest16
        extends
            AbstractSingleMeshBaseTest
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
        log.info( "Setting up listener" );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        TestListener listener = new TestListener();
        theMeshBase.addDirectMeshObjectLifecycleEventListener( listener );

        //
        
        log.info(  "Create a few objects" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject zero = life.createMeshObject();
        MeshObject one  = life.createMeshObject();
        MeshObject two  = life.createMeshObject();
        
        tx.commitTransaction();
        
        checkEquals( listener.theCreatedEvents.size(), 3, "Wrong created events" );
        checkEquals( listener.theDeletedEvents.size(), 0, "Wrong deleted events" );
        
        checkIdentity( zero, listener.theCreatedEvents.get( 0 ).getDeltaValue(), "Wrong zero object" );
        checkIdentity( one,  listener.theCreatedEvents.get( 1 ).getDeltaValue(), "Wrong one object" );
        checkIdentity( two,  listener.theCreatedEvents.get( 2 ).getDeltaValue(), "Wrong two object" );

        listener.clear();
        
        //
        
        log.info( "Delete some objects" );
        
        tx = theMeshBase.createTransactionNow();
        
        life.deleteMeshObjects( new MeshObject[] { zero, two } );
        
        tx.commitTransaction();
        
        checkEquals( listener.theCreatedEvents.size(), 0, "Wrong created events" );
        checkEquals( listener.theDeletedEvents.size(), 2, "Wrong deleted events" );
        
        checkIdentity( zero, listener.theDeletedEvents.get( 0 ).getDeltaValue(), "Wrong zero object" );
        checkIdentity( two,  listener.theDeletedEvents.get( 1 ).getDeltaValue(), "Wrong two object" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest16.class );
    
    /**
     * Test listener.
     */
    protected static class TestListener
            implements
                MeshObjectLifecycleListener
    {
        public void meshObjectCreated(
                MeshObjectCreatedEvent theEvent )
        {
            theCreatedEvents.add( theEvent );
        }

        public void meshObjectDeleted(
                MeshObjectDeletedEvent theEvent )
        {
            theDeletedEvents.add( theEvent );
        }

        /**
         * Clear collected events.
         */
        public void clear()
        {
            theCreatedEvents.clear();
            theDeletedEvents.clear();
        }
        
        /**
         * Stores received created events.
         */
        protected ArrayList<MeshObjectCreatedEvent> theCreatedEvents = new ArrayList<MeshObjectCreatedEvent>();
        
        /**
         * Stores received deleted events.
         */
        protected ArrayList<MeshObjectDeletedEvent> theDeletedEvents = new ArrayList<MeshObjectDeletedEvent>();        
    }
}
