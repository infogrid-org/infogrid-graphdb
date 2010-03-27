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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.Sweeper;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.sweeper.NotReadForLongerThanSweeper;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;

/**
 * Tests the NotReadForLongerThanSweeper.
 */
public class SweeperTest1
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

        MeshBase theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase"),
                theModelBase,
                null,
                rootContext );
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Sweeper theSweeper = NotReadForLongerThanSweeper.create( 1000L );
        theMeshBase.setSweeper( theSweeper );
        
        //
        
        log.info( "Creating some objects" );
        
        long now = System.currentTimeMillis();
        
        MeshObjectIdentifier extName1 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj1" );
        MeshObjectIdentifier extName2 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj2" );
        MeshObjectIdentifier extName3 = theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "obj3" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1 = life.createMeshObject( extName1, TestSubjectArea.AA, now, now, now, -1L );
        MeshObject obj2 = life.createMeshObject( extName2, TestSubjectArea.AA, now, now, now, -1L );
        MeshObject obj3 = life.createMeshObject( extName3, TestSubjectArea.AA, now, now, now, -1L );
        
        tx.commitTransaction();

        //
        
        log.info( "Waiting for a bit, then touching some of the objects" );
        
        Thread.sleep( 4000L );
        
        Object foo = obj1.getRoles();
        foo = obj3.getPropertyValue( TestSubjectArea.A_X );
        
        obj1 = null;
        obj2 = null;
        obj3 = null;
        
        //
        
        log.info( "Checking that obj2 is gone" );
        
        obj1 = theMeshBase.findMeshObjectByIdentifier( extName1 );
        obj2 = theMeshBase.findMeshObjectByIdentifier( extName2 );
        obj3 = theMeshBase.findMeshObjectByIdentifier( extName3 );
        
        checkObject( obj1, "obj1 not here" );
        checkCondition( obj2 == null, "obj2 found" );
        checkObject( obj3, "obj3 not here" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        SweeperTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new SweeperTest1( args );
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
    public SweeperTest1(
            String [] args )
        throws
            Exception
    {
        super( SweeperTest1.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( SweeperTest1.class );
}
