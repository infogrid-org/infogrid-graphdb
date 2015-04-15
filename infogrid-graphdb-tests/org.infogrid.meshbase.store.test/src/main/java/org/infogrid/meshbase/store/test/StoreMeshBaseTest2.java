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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Creates MeshObjects in the Sql implementation of the StoreMeshBase, removes them from
 * cache and transparently re-reads them.
 */
public class StoreMeshBaseTest2
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        EntityType [] abctypes = { TestSubjectArea.AA, TestSubjectArea.B };

        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        //

        log.info( "Creating MeshBase" );

        super.startClock();
        long t1 = System.currentTimeMillis();

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        long t2 = System.currentTimeMillis();

        checkObject( mb.getHomeObject(), "No home object" );
        checkIdentity( mb.getHomeObject().getMeshBase(), mb, "Home object in wrong MeshBase" );
        checkInRange( mb.getHomeObject().getTimeCreated(), t1, t2, "Home object created at wrong time" );
        checkInRange( mb.getHomeObject().getTimeUpdated(), t1, t2, "Home object updated at wrong time" );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();

        //

        log.info( "Checking that transactions are required" );

        try {
            life.createMeshObject();            
            reportError( "createMeshObject did not throw TransactionException" );
        } catch( TransactionException ex ) {
            
        }
        
        //
        
        final String PROP_VALUE_PREFIX = "This is <b>important&trade;</b>&#33; MeshObject "; // make sure HTML is in it
        EnumeratedValue [] ptZdomain = TestSubjectArea.B_Z_type.getDomain();

        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        t1 = System.currentTimeMillis();

        MeshObject []     mesh  = new MeshObject[ theTestSize ];
        MeshObjectIdentifier [] names = new MeshObjectIdentifier[ theTestSize ];
        for( int i=0 ; i<mesh.length ; ++i ) {
            mesh[i] = life.createMeshObject();
            mesh[i].bless( abctypes[ i % abctypes.length ] );
            
            if( i>0 && i<mesh.length*3/4 ) {
                mesh[i].relate( mesh[i-1] );
            }
            if( i>0 && i<mesh.length/2 ) {
                if( i % 2 == 0 ) {
                    mesh[i].blessRelationship( TestSubjectArea.R.getSource(), mesh[i-1] );
                } else {
                    mesh[i].blessRelationship( TestSubjectArea.R.getDestination(), mesh[i-1] );
                }
            }
            if( i/2 % 2 == 0 ) {
                if( mesh[i].isBlessedBy( TestSubjectArea.AA )) {
                    mesh[i].setPropertyValue( TestSubjectArea.A_X,  StringValue.create( PROP_VALUE_PREFIX + i ));
                    mesh[i].setPropertyValue( TestSubjectArea.A_XX, TestSubjectArea.A_XX_type.createBlobValue( PROP_VALUE_PREFIX + i, BlobValue.TEXT_PLAIN_MIME_TYPE ));
                } else {
                    mesh[i].setPropertyValue( TestSubjectArea.B_Z, ptZdomain[ i % ptZdomain.length ] );
                }
            }
            
            names[i] = mesh[i].getIdentifier();
        }
        t2 = System.currentTimeMillis();
        for( int i=0 ; i<mesh.length ; ++i ) {
            checkObject( mesh[i], "MeshObject " + i + "is null" );
            checkIdentity( mesh[i].getMeshBase(), mb, "MeshObject " + i + " in wrong MeshBase" );
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
        
        double duration = super.getRelativeTime()/1000.0;
        
        log.info( "Test (writing) using " + mesh.length + " objects took " + duration + " seconds at " + ( duration / mesh.length * 1000.0 ) + " msec/object." );

        //
        
        log.info( "Clearing cache, and loading MeshObjects again" );
        
        mesh = new MeshObject[ names.length ]; // forget old references
        mb.clearMemoryCache();

        super.startClock();

        for( int i=0 ; i<names.length ; ++i ) {
            
            if( log.isDebugEnabled() ) {
                log.debug( "Looking for object " + names[i] );
            }
            mesh[i] = mb.findMeshObjectByIdentifier( names[i] );
            
            checkObject( mesh[i], "Could not retrieve MeshObject with Identifier " + names[i] );
            if( mesh[0] != null ) {
                // it always should be, but if it isn't and the test failed, we might still want to continue the test
                checkEquals( mesh[i].getTypes().length, 1, "not the right number of MeshTypes" );
                checkEquals( mesh[i].getTypes()[0], abctypes[ i % abctypes.length ], "not the right MeshType" );
            }
            if( i/2 % 2 == 0 ) {
                if( mesh[i].isBlessedBy( TestSubjectArea.AA )) {
                    checkEquals( mesh[i].getPropertyValue( TestSubjectArea.A_X ),  PROP_VALUE_PREFIX + i, "Wrong ptX value" );
                    checkEquals( mesh[i].getPropertyValue( TestSubjectArea.A_XX ), TestSubjectArea.A_XX_type.createBlobValue( PROP_VALUE_PREFIX + i, BlobValue.TEXT_PLAIN_MIME_TYPE ), "Wrong ptX value" );
                } else {
                    checkEquals( mesh[i].getPropertyValue( TestSubjectArea.B_Z ), ptZdomain[ i % ptZdomain.length ], "Wrong ptZ value" );
                }
            }
        }

        duration = super.getRelativeTime()/1000.0;
        
        log.info( "Test (reading) using " + mesh.length + " objects took " + duration + " seconds at " + ( duration / mesh.length * 1000.0 ) + " msec/object." );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreMeshBaseTest2 test = null;
        try {
            if( args.length < 1 ) {
                System.err.println( "Synopsis: [testsize as positive integer]" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreMeshBaseTest2( args );
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
     * @throws Exception anything can go wrong in a test
     */
    public StoreMeshBaseTest2(
            String [] args )
        throws
            Exception
    {
        super( StoreMeshBaseTest2.class );

        theTestSize = Integer.parseInt( args[0] );
    }

    /**
     * The number of MeshObjects to create for the test.
     */
    protected int theTestSize;

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest2.class);
}
