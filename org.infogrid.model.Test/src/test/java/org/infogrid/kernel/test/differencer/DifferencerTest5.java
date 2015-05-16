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

package org.infogrid.kernel.test.differencer;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.IterableMeshBaseDifferencer;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.ChangeSet;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests creation of blessed relationships.
 */
public class DifferencerTest5
        extends
            AbstractDifferencerTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test
     */
    @Test
    public void run()
        throws
            Exception
    {
        long now = 1234L;

        //

        log.info( "Creating MeshObjects in MeshBase1" );
        
        MeshBaseLifecycleManager life1 = theMeshBase1.getMeshBaseLifecycleManager();

        Transaction tx1 = theMeshBase1.createTransactionNow();

        MeshObject a4_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a4" ), typeAA, now );
        MeshObject a5_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a5" ), typeAA, now );

        // no relationships
        
        tx1.commitTransaction();

        //
        
        log.info( "Creating MeshObjects in MeshBase1" );
        
        MeshBaseLifecycleManager life2 = theMeshBase2.getMeshBaseLifecycleManager();

        Transaction tx2 = theMeshBase2.createTransactionNow();

        MeshObject a4_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a4" ), typeAA, now );
        MeshObject a5_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a5" ), typeAA, now );

        a4_mb2.relateAndBless( typeAR1A.getSource(), a5_mb2 ); // DIFFERENCE: blessed relationship
        
        tx2.commitTransaction();

        //

        log.info( "now differencing" );

        IterableMeshBaseDifferencer diff = new IterableMeshBaseDifferencer( theMeshBase1 );

        ChangeSet theChangeSet = diff.determineChangeSet( theMeshBase2, false );

        printChangeSet( log, theChangeSet );
        
        checkEquals( theChangeSet.size(), 4, "Not the right number of changes" );
        
        //

        log.info( "now applying changes" );

        Transaction tx = diff.getBaselineMeshBase().createTransactionNow();
        diff.applyChangeSet( theChangeSet );
        tx.commitTransaction();

        //

        ChangeSet theEmptyChangeSet = diff.determineChangeSet( theMeshBase2 );

        checkEquals( theEmptyChangeSet.size(), 0, "Change set not empty after applying differences" );

        if( theEmptyChangeSet.size() > 0 ) {
            log.debug( "ChangeSet is " + theEmptyChangeSet );
        } else {
            log.debug( "ChangeSet is empty" );
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( DifferencerTest5.class );
}
