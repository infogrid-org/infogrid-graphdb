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
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the blessing of MeshObjects, unblessing, and Facade-based access. This does not test
 * relationships between MeshObjects.
 */
public class MeshBaseTest14
        extends
            AbstractSingleMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    @Test
    public void run()
        throws
            Exception
    {
        log.info( "Instantiating MeshObject" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();
        MeshObject obj = life.createMeshObject( );
            
        tx.commitTransaction();
        
        //
        
        log.info( "Blessing with C" );
        
        tx = theMeshBase.createTransactionNow();
        
        obj.bless( TestSubjectArea.C );
            
        tx.commitTransaction();

        //
        
        log.info( "Making sure re-blessing doesn't work" );
        
        tx = theMeshBase.createTransactionNow();

        try {
            obj.bless( TestSubjectArea.C );
            
            reportError( "Reblessing with C worked but should not" );

        } catch( Throwable t ) {
            log.debug( "Correctly received exception", t );
        }
            
        tx.commitTransaction();
        
        //
        
        log.info( "Testing down-blessing to D" );
        
        tx = theMeshBase.createTransactionNow();
        
        obj.bless( TestSubjectArea.D );
            
        tx.commitTransaction();
        
        //
        
        log.info( "Checking that it's only D now, not C" );
        
        super.checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.D }, "Wrong types" );
        
        //
        
        log.info( "Making sure up-blessing doesn't work" );
        
        tx = theMeshBase.createTransactionNow();

        try {
            obj.bless( TestSubjectArea.C );
            
            reportError( "Up-blessing with C worked but should not" );

        } catch( Throwable t ) {
            log.debug( "Correctly received exception", t );
        }
            
        tx.commitTransaction();
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest14.class);
}
