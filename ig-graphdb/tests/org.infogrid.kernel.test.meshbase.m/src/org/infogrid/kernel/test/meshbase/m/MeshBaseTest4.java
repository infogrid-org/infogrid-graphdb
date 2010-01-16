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
import org.infogrid.mesh.NotBlessedException;
import org.infogrid.mesh.RoleTypeRequiresEntityTypeException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;

/**
 * Tests that we can create and delete relationships between MeshObjects.
 */
public class MeshBaseTest4
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
        //
        
        log.info( "Creating and relating MeshObjects" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();

        MeshObject a = life.createMeshObject( TestSubjectArea.AA );
        MeshObject b = life.createMeshObject( TestSubjectArea.B );
        a.relate( b );

        tx.commitTransaction();

        //
        
        log.info( "blessing the relationship" );

        tx = theMeshBase.createTransactionNow();

        a.blessRelationship( TestSubjectArea.RR.getSource(), b );

        tx.commitTransaction();

        //
        
        log.info( "attempting to bless the wrong relationship" );

        tx = theMeshBase.createTransactionNow();

        try {
            a.blessRelationship( TestSubjectArea.AR1A.getSource(), b );
            
            reportError( "Was able to bless relationship between wrongly blessed objects" );

        } catch( NotBlessedException ex ) {
            // okay
        }

        tx.commitTransaction();

        //

        log.info( "Trying a traversal" );
        
        MeshObjectSet other = a.traverse( TestSubjectArea.RR.getSource() );
        
        checkEquals( 1, other.size(), "did not reach destination" );
        checkEquals( b, other.getMeshObjects()[0], "wrong destination" );

        log.info( "Trying reverse traversal" );
        
        MeshObjectSet first = b.traverse( TestSubjectArea.RR.getDestination() );
        checkEquals( 1, first.size(), "did not reach source" );

        //

        log.info( "Trying a supertype traversal" );
        
        other = a.traverse( TestSubjectArea.R.getSource() );
        
        checkEquals( 1, other.size(), "did not reach destination" );
        checkEquals( b, other.getMeshObjects()[0], "wrong destination" );

        log.info( "Trying reverse supertype traversal" );
        
        first = b.traverse( TestSubjectArea.R.getDestination() );
        checkEquals( 1, first.size(), "did not reach source" );

        //
        
        log.info( "attempting to unbless object" );

        tx = theMeshBase.createTransactionNow();

        try {
            b.unbless( TestSubjectArea.B );
            reportError( "Was able to unbless in spite of attached relationship role requirements" );

        } catch( RoleTypeRequiresEntityTypeException ex ) {
            // no op
        }
        tx.commitTransaction();

        //
        
        log.info( "attempting to unbless again after relationship unblessed" );
        
        tx = theMeshBase.createTransactionNow();

        a.unblessRelationship( TestSubjectArea.RR.getSource(), b );
        b.unbless( TestSubjectArea.B );

        tx.commitTransaction();
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest4 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest4( args );
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
    public MeshBaseTest4(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest4.class );

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
    private static Log log = Log.getLogInstance( MeshBaseTest4.class);
}
