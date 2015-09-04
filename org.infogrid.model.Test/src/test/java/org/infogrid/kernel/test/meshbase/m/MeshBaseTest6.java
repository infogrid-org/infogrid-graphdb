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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.util.logging.Log;

/**
 * This tests that unblessing relationships creates the right events.
 */
public class MeshBaseTest6
        extends
            AbstractSingleMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public void run()
        throws
            Exception
    {

        log.info( "Looking up MeshTypes" );
        
        EntityType       typeAA = theModelBase.findEntityType( "org.infogrid.model.Test", "AA" );
        EntityType       typeB  = theModelBase.findEntityType( "org.infogrid.model.Test", "B" );
        RelationshipType typeR  = theModelBase.findRelationshipType( "org.infogrid.model.Test", "R" );

        //
        
        log.info( "Creating MeshObjects" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();

        MeshObject a = life.createMeshObject( typeAA );
        MeshObject b = life.createMeshObject( typeB );
        a.relateAndBless( typeR.getSource(), b );

        tx.commitTransaction();

        //
        
        MyListener listenerA = new MyListener();
        MyListener listenerB = new MyListener();
        
        a.addWeakPropertyChangeListener( listenerA );
        b.addWeakPropertyChangeListener( listenerB );
        
        //
        
        tx = theMeshBase.createTransactionNow();
        
        a.unblessRelationship( typeR.getSource(), b );
        
        tx.commitTransaction();
        
        //
        
        checkEquals( listenerA.theEvents.size(), 1, "Wrong number of events at object A" );
        checkEquals( listenerB.theEvents.size(), 1, "Wrong number of events at object B" );
    }
    
    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest6.class );    

    /**
     * Listener class.
     */
    public static class MyListener
            implements
                PropertyChangeListener
    {
        public void propertyChange(
                PropertyChangeEvent event )
        {
            theEvents.add( event );
        }
     
        @Override
        public String toString()
        {
            StringBuilder buf = new StringBuilder();
            buf.append( "MyListener:" );
            for( PropertyChangeEvent e : theEvents ) {
                buf.append( "\n" );
                buf.append( e );
            }
            return buf.toString();
        }

        protected ArrayList<PropertyChangeEvent> theEvents = new ArrayList<PropertyChangeEvent>();
    }
}
