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
import org.infogrid.util.logging.Log;

/**
 * Tests that unrelating MeshObjects creates the right events.
 */
public class MeshBaseTest5
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
        log.info( "Creating MeshObjects" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();

        MeshObject a = life.createMeshObject();
        MeshObject b = life.createMeshObject();
        a.relate( b );

        tx.commitTransaction();

        //
        
        MyListener listenerA = new MyListener();
        MyListener listenerB = new MyListener();
        
        a.addWeakPropertyChangeListener( listenerA );
        b.addWeakPropertyChangeListener( listenerB );
        
        //
        
        tx = theMeshBase.createTransactionNow();
        
        a.unrelate( b );
        
        tx.commitTransaction();
        
        //
        
        if( !checkEquals( listenerA.theEvents.size(), 1, "Wrong number of events at object A" )) {
            log.debug( listenerA );
        }
        if( !checkEquals( listenerB.theEvents.size(), 1, "Wrong number of events at object B" )) {
            log.debug( listenerA );
        }
    }
    
    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest5 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest5( args );
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
    public MeshBaseTest5(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest5.class );

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
    private static Log log = Log.getLogInstance( MeshBaseTest5.class );    

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
         * @param event the event
         */
        public void propertyChange(
                PropertyChangeEvent event )
        {
            theEvents.add( event );
        }
        
        /**
         * Convert to String, for debugging.
         * 
         * @return String representation
         */
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

        /**
         * The received events.
         */
        protected ArrayList<PropertyChangeEvent> theEvents = new ArrayList<PropertyChangeEvent>();
    }
}
