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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.test.meshbase.m;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.B;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests the firing of PropertyChangeEvents upon the changing properties, and blessing of MeshObjects.
 */
public class MeshBaseTest3
        extends
            AbstractMeshBaseTest
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

        EntityType   typeB = theModelBase.findEntityType(   "org.infogrid.model.Test", null, "B" );
        PropertyType ptU   = theModelBase.findPropertyType( "org.infogrid.model.Test", null, "B",  "U" );

        //
        
        log.info( "Creating MeshObject" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();

        MeshObject obj = life.createMeshObject();
        
        tx.commitTransaction();
        long timeUpdated = obj.getTimeUpdated();
        sleepFor( 1l ); // make sure CPU doesn't run too fast

        //
        
        log.info( "Subscribing to events" );

        checkCondition( !obj.hasPropertyChangeListener(), "has PropertyChangeListener already" );
        
        MyListener listener = new MyListener();
        obj.addWeakPropertyChangeListener( listener );
        
        checkCondition( obj.hasPropertyChangeListener(), "doesn't have PropertyChangeListener" );
        checkEquals( timeUpdated, obj.getTimeUpdated(), "should not have changed timeUpdated" );

        //
        
        log.info( "Changing property" );
        
        tx = theMeshBase.createTransactionNow();
        try {
            obj.setPropertyValue( ptU, StringValue.create( "test 1" ));
            reportError( "Succeeded in changing non-existing property" );
        } catch( IllegalPropertyTypeException ex ) {
            // no op
        }
        tx.commitTransaction();

        checkEquals( listener.theEvents.size(), 0, "wrong number of events received" );
        checkEquals( timeUpdated, obj.getTimeUpdated(), "should not have changed timeUpdated" );
        listener.theEvents.clear();
        
        timeUpdated = obj.getTimeUpdated();
        sleepFor( 1l ); // make sure CPU doesn't run too fast

        //
        
        log.info( "Blessing" );
        
        tx = theMeshBase.createTransactionNow();
        obj.bless( typeB );
        tx.commitTransaction();

        checkEquals( listener.theEvents.size(), 1, "wrong number of events received" ); // bless event. Default value set event does not create event.
        checkEqualsOutOfSequence( new Object[] { typeB }, (Object []) listener.theEvents.get( 0 ).getNewValue(), "wrong new value" );
        checkNotEquals( timeUpdated, obj.getTimeUpdated(), "should have changed timeUpdated" );
        listener.theEvents.clear();

        timeUpdated = obj.getTimeUpdated();
        sleepFor( 1l ); // make sure CPU doesn't run too fast

        //
        
        log.info( "Changing property again" );
        
        tx = theMeshBase.createTransactionNow();
        B b = (B) obj.getTypedFacadeFor( typeB );
        
        b.setU( StringValue.create( "test 2" ));
        tx.commitTransaction();

        checkEquals( listener.theEvents.size(), 1, "wrong number of events received" );
        checkEquals( "test 2", ((StringValue)listener.theEvents.get( 0 ).getNewValue()).value(), "wrong new value" );
        checkNotEquals( timeUpdated, obj.getTimeUpdated(), "should have changed timeUpdated" );
        listener.theEvents.clear();

        timeUpdated = obj.getTimeUpdated();
        sleepFor( 1l ); // make sure CPU doesn't run too fast

        //
        
        log.info( "Unblessing" );
        
        tx = theMeshBase.createTransactionNow();
        obj.unbless( typeB );
        tx.commitTransaction();

        checkEquals( listener.theEvents.size(), 1, "wrong number of events received" );
        checkEqualsOutOfSequence( new Object[] { typeB }, (Object []) listener.theEvents.get( 0 ).getOldValue(), "wrong old value" );
        checkNotEquals( timeUpdated, obj.getTimeUpdated(), "should have changed timeUpdated" );
        listener.theEvents.clear();

        timeUpdated = obj.getTimeUpdated();
        sleepFor( 1l ); // make sure CPU doesn't run too fast

        //
        
        log.info( "Unsubscribing from events" );
        
        obj.removePropertyChangeListener( listener );
        checkCondition( !obj.hasPropertyChangeListener(), "still has PropertyChangeListener" );
        checkEquals( timeUpdated, obj.getTimeUpdated(), "should not have changed timeUpdated" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest3 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest3( args );
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
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public MeshBaseTest3(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest3.class );

        theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                rootContext );
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        theMeshBase.die();
    }

    /**
     * The MeshBase for the test.
     */
    protected MeshBase theMeshBase;

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest3.class);

    /**
     * Listener class.
     */
    public static class MyListener
            implements
                PropertyChangeListener
    {
        /**
         * Event callback.
         * 
         * @param event the vent
         */
        public void propertyChange(
                PropertyChangeEvent event )
        {
            theEvents.add( event );
        }
        
        /**
         * The received events.
         */
        protected ArrayList<PropertyChangeEvent> theEvents = new ArrayList<PropertyChangeEvent>();
    }
}
