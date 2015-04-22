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
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.util.logging.Log;

/**
 * Tests traversal in the context of equivalence sets.
 */
public class MeshBaseTest9
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
        EntityType       typeAA = theModelBase.findEntityType( "org.infogrid.model.Test", null, "AA" );
        EntityType       typeB  = theModelBase.findEntityType( "org.infogrid.model.Test", null, "B" );
        RelationshipType typeRR = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "RR" );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();

        MeshObject a = life.createMeshObject( typeAA );
        MeshObject b1 = life.createMeshObject( typeB );
        MeshObject b2 = life.createMeshObject( typeAA );
        MeshObject c = life.createMeshObject( typeB );

        a.relateAndBless(  typeRR.getSource(), b1 );
        a.relateAndBless(  typeRR.getSource(), c );
        b2.relateAndBless( typeRR.getSource(), c );

        b1.addAsEquivalent( b2 );

        tx.commitTransaction();

        //

        log.info( "Trying traversal from A" );
        
        MeshObjectSet fromA = a.traverse( typeRR.getSource() );
        
        checkEquals( 2, fromA.size(), "did not reach all destinations" );
        checkEqualsOutOfSequence( new Object[] { b1, c }, fromA.getMeshObjects(), "wrong destinations from A" );

        //
        
        log.info( "Trying traversal from C" );
        
        MeshObjectSet fromC = c.traverse( typeRR.getDestination() );
        
        checkEquals( 2, fromC.size(), "did not reach all destinations" );
        checkEqualsOutOfSequence( new Object[] { b2, a }, fromC.getMeshObjects(), "wrong destinations from C" );

        //
        
        log.info( "Trying traversal from B1" );
        
        MeshObjectSet fromB1_source = b1.traverse( typeRR.getSource() );
        
        checkEquals( 1, fromB1_source.size(), "did not reach all destinations" );
        checkEqualsOutOfSequence( new Object[] { c }, fromB1_source.getMeshObjects(), "wrong destinations from B1 (source)" );

        MeshObjectSet fromB1_dest = b1.traverse( typeRR.getDestination() );
        
        checkEquals( 1, fromB1_dest.size(), "did not reach all destinations" );
        checkEqualsOutOfSequence( new Object[] { a }, fromB1_dest.getMeshObjects(), "wrong destinations from B1 (dest)" );
        
        MeshObjectSet fromB1_aa = b1.traverseToNeighborMeshObjects();
        
        checkEquals( 2, fromB1_aa.size(), "did not reach all neighbors" );
        checkEqualsOutOfSequence( new Object[] { a, c }, fromB1_aa.getMeshObjects(), "wrong destinations from B1 (all)" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest9 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest9( args );
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
    public MeshBaseTest9(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest9.class );

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
    private static Log log = Log.getLogInstance( MeshBaseTest9.class);
}
