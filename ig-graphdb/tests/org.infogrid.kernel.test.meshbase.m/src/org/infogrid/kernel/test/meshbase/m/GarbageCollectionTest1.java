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

import java.lang.ref.WeakReference;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.util.logging.Log;

/**
 * Tests whether MeshBases are deallocated when not needed any more.
 */
public class GarbageCollectionTest1
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
        EntityType         typeAA = theModelBase.findEntityType( "org.infogrid.model.Test", null, "AA" );
        EntityType         typeB  = theModelBase.findEntityType( "org.infogrid.model.Test", null, "B" );
        RelationshipType   typeR  = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "R" );
        PropertyType       typeY  = theModelBase.findPropertyType( typeAA, "Y" );
        PropertyType       typeZ  = theModelBase.findPropertyType( typeB,  "Z" );
        EnumeratedDataType zType  = (EnumeratedDataType) typeZ.getDataType();

        //
        
        log.info( "Creating MeshBases" );

        MeshBase mb1 = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase1" ), theModelBase, null, rootContext );
        MeshBase mb2 = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase2" ), theModelBase, null, rootContext );
        MeshBase mb3 = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase3" ), theModelBase, null, rootContext );
        
        //
        
        log.info( "Creating WeakReferences" );

        WeakReference<MeshBase> ref1 = new WeakReference<MeshBase>( mb1 );
        WeakReference<MeshBase> ref2 = new WeakReference<MeshBase>( mb2 );
        WeakReference<MeshBase> ref3 = new WeakReference<MeshBase>( mb3 );

        checkEquals( ref1.get(), mb1, "Weak reference to first MeshBase lost" );
        checkEquals( ref2.get(), mb2, "Weak reference to second MeshBase lost" );
        checkEquals( ref3.get(), mb3, "Weak reference to third MeshBase lost" );

        //

        log.info( "Removing reference to third MeshBase, waiting, and checking" );

        mb3 = null;

        collectGarbage();

        checkEquals( ref3.get(), null, "Third MeshBase still exists");

        //

        log.info( "Setting up some data in first MeshBase" );

        MeshObject obj1_A;

        // we put this into a block so the local vars can go away
        {
            Transaction tx = mb1.createTransactionAsap();

            MeshBaseLifecycleManager life1 = mb1.getMeshBaseLifecycleManager();

            obj1_A = life1.createMeshObject( typeAA );
            obj1_A.setPropertyValue( typeY, FloatValue.create( 12.34 ) );

            MeshObject obj2_A = life1.createMeshObject( typeB );
            obj2_A.setPropertyValue( typeZ, zType.select( "Value1" ) );

            MeshObject obj3_A = life1.createMeshObject( typeB );
            obj3_A.setPropertyValue( typeZ, zType.select( "Value3" ) );

            obj1_A.relateAndBless( typeR.getSource(), obj2_A );
            obj1_A.relateAndBless( typeR.getSource(), obj3_A );

            tx.commitTransaction();

            tx     = null;
            life1  = null;

            obj2_A = null;
            obj3_A = null;
        }

        //

        if( log.isDebugEnabled() ) {
            Thread[] theThreads = new Thread[100]; // wild guess, there's no good API ...
            Thread.enumerate(theThreads);

            for( int i=0 ; theThreads[i] != null ; ++i ) {
                log.debug( "Thread " + i + ": " + theThreads[i] );
            }
        }

        log.info( "Checking that everything is still fine" );

        checkEquals( ref1.get(), mb1, "Weak reference to first repository lost" );
        checkEquals( ref2.get(), mb2, "Weak reference to second repository lost" );
        checkEquals( ref3.get(), null, "Weak reference to third repository has re-appeared" );

        obj1_A = null;

        //

        log.info( "Setting all local references to null" );

        mb1 = null;
        mb2 = null;
        mb3 = null;

        collectGarbage();
        Thread.sleep( 10000L );
        collectGarbage();

        if( log.isDebugEnabled() ) {
            Thread[] theThreads = new Thread[100]; // wild guess, there's no good API ...
            Thread.enumerate(theThreads);

            for( int i=0 ; theThreads[i] != null ; ++i ) {
                log.debug( "Thread " + i + ": " + theThreads[i] );
            }
        }

        checkEquals( ref1.get(), null, "Weak reference to first MeshBase is still there" );
        checkEquals( ref2.get(), null, "Weak reference to second MeshBase is still there" );
        checkEquals( ref3.get(), null, "Weak reference to third MeshBase has re-appeared" );

    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        GarbageCollectionTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new GarbageCollectionTest1( args );
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
    public GarbageCollectionTest1(
            String [] args )
        throws
            Exception
    {
        super( GarbageCollectionTest1.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( GarbageCollectionTest1.class );
}

