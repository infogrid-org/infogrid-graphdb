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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.RelatedAlreadyException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.util.logging.Log;

/**
 * Tests whether we can create more than one relationship between the same
 * two MeshObjects.
 */
public class MeshBaseTest13
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
        RelationshipType typeR  = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "R" );
        RelationshipType typeRR = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "RR" );
        RelationshipType typeS  = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "S" );

        //

        log.info( "Creating MeshBase" );

        MeshBase theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ), 
                theModelBase,
                null,
                rootContext );
        
        //

        log.info( "Setting up objects" );

        Transaction tx = theMeshBase.createTransactionAsap();

        MeshBaseLifecycleManager life1 = theMeshBase.getMeshBaseLifecycleManager();

        MeshObject aa = life1.createMeshObject( typeAA );
        MeshObject b  = life1.createMeshObject( typeB );

        aa.relateAndBless( typeR.getSource(), b );

        tx.commitTransaction();

        //

        log.info( "Trying to create illegal R relationship" );

        try {
            tx = theMeshBase.createTransactionAsap();

            aa.relateAndBless( typeRR.getSource(), b );

            reportError( "Should have thrown exception" );

        } catch( RelatedAlreadyException ex ) {
            // noop
        }
        tx.commitTransaction();

        //

        log.info( "Trying to bless with RR, should downcast" );

        tx = theMeshBase.createTransactionAsap();

        aa.blessRelationship( typeRR.getSource(), b );

        tx.commitTransaction();

        checkEquals( aa.getRoleTypes().length, 1, "wrong number of role types" );
        checkEquals( typeRR.getSource(), aa.getRoleTypes()[0], "wrong role type" );

        //

        log.info( "Trying to create illegal S relationship" );

        try {
            tx = theMeshBase.createTransactionAsap();

            aa.relateAndBless( typeS.getSource(), b );

            reportError( "Should have thrown exception" );

        } catch( RelatedAlreadyException ex ) {
            // noop
        }
        tx.commitTransaction();

        //

        log.info( "Trying to bless instead (S)" );

        tx = theMeshBase.createTransactionAsap();

        aa.blessRelationship( typeS.getSource(), b );

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
        MeshBaseTest13 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest13( args );
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
    public MeshBaseTest13(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest13.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest13.class );
}
