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

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.logging.Log;

/**
 * Reproduces a StoreMeshBase integrity problem found 2007-06-22.
 */
public class StoreMeshBaseTest5
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
        theSqlStore.initializeHard();

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "mb" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();

        //
        
        log.info( "Creating a few objects" );
        
        Transaction tx = mb.createTransactionNow();
        
        MeshObjectIdentifier extName = mb.getMeshObjectIdentifierFactory().fromExternalForm( "wsItEtOFGML7KyXCQ0slH6w+Jc9Tw5tY9+kc0TTlz8U=" );
        MeshObject obj = life.createMeshObject( extName, TestSubjectArea.AA );
        
        mb.getHomeObject().relateAndBless( TestSubjectArea.ARANY.getDestination(), obj );
        
        tx.commitTransaction();

        //
        
        log.info( "collecting garbage" );
        
        tx = null;
        obj = null;
        
        collectGarbage();
        
        //
        
        log.info( "checking everything's still there" );
        
        MeshObjectSet objs = mb.getHomeObject().traverseToNeighborMeshObjects();
        checkEquals( objs.size(), 1, "wrong number neighbors found" );

        RoleType [] rts = mb.getHomeObject().getRoleTypes( objs.getSingleMember() );
        checkEquals( rts.length, 1, "Wrong number of roles" );
        checkEquals( rts[0], TestSubjectArea.ARANY.getDestination(), "Wrong role" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        StoreMeshBaseTest5 test = null;
        try {
            if( args.length > 0 ) {
                System.err.println( "Synopsis: <no argument>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreMeshBaseTest5( args );
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
    public StoreMeshBaseTest5(
            String [] args )
        throws
            Exception
    {
        super( StoreMeshBaseTest5.class  );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest5.class);
}
