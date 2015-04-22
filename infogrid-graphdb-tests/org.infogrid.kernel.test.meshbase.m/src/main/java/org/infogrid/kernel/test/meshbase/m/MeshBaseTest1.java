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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.util.logging.Log;

/**
 * Tests whether we can create MeshObjects, and whether their time created / updated times are right.
 */
public class MeshBaseTest1
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
        log.info( "Creating MeshBase" );

        long t1 = System.currentTimeMillis();

        MeshBase theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                rootContext );

        long t2 = System.currentTimeMillis();

        checkObject( theMeshBase.getHomeObject(), "No home object" );
        checkIdentity( theMeshBase.getHomeObject().getMeshBase(), theMeshBase, "Home object in wrong MeshBase" );
        checkInRange( theMeshBase.getHomeObject().getTimeCreated(), t1, t2, "Home object created at wrong time" );
        checkInRange( theMeshBase.getHomeObject().getTimeUpdated(), t1, t2, "Home object updated at wrong time" );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        //

        log.info( "Checking that transactions are required" );

        try {
            life.createMeshObject();
            reportError( "createMeshObject did not throw TransactionException" );
        } catch( TransactionException ex ) {
            // noop
        }

        
        //
        
        log.info( "Creating MeshObjects" );
        
        Transaction tx = theMeshBase.createTransactionNow();

        t1 = System.currentTimeMillis();

        MeshObject [] mesh = new MeshObject[ 1000 ];
        for( int i=0 ; i<mesh.length ; ++i ) {
            mesh[i] = life.createMeshObject();
        }
        t2 = System.currentTimeMillis();
        for( int i=0 ; i<mesh.length ; ++i ) {
            checkObject( mesh[i], "MeshObject " + i + "is null" );
            checkIdentity( mesh[i].getMeshBase(), theMeshBase, "MeshObject " + i + " in wrong MeshBase" );
            checkInRange(
                    mesh[i].getTimeCreated(),
                    ( i==0 ) ? t1 : mesh[i-1].getTimeCreated(),
                    ( i==mesh.length-1 ) ? t2 : mesh[i+1].getTimeCreated(),
                    "MeshObject " + i + " created at wrong time" );
            checkInRange(
                    mesh[i].getTimeUpdated(),
                    ( i==0 ) ? t1 : mesh[i-1].getTimeUpdated(),
                    ( i==mesh.length-1 ) ? t2 : mesh[i+1].getTimeUpdated(),
                    "MeshObject " + i + " updated at wrong time" );
        }
        tx.commitTransaction();
        
        //
        
        log.info( "Duplicate Identifiers not allowed" );
        
        tx = theMeshBase.createTransactionNow();
        
        try{
            life.createMeshObject( mesh[0].getIdentifier() );
            reportError( "createMeshObject with duplicate Identifier did not throw IdentifierNotUniqueException" );
        } catch( MeshObjectIdentifierNotUniqueException ex ) {
            // noop
        }
        tx.commitTransaction();
        
        //
        
        theMeshBase.die();
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest1( args );
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
    public MeshBaseTest1(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest1.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest1.class );
}
