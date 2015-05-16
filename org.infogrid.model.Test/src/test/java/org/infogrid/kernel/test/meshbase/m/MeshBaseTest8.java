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

import org.infogrid.mesh.EquivalentAlreadyException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests the equivalence mechanism. We create two chains of equivalences, make sure they work, then
 * make the equivalent, and remove some objects.
 */
public class MeshBaseTest8
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
        Transaction tx;

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        log.info( "Creating objects" );
        tx = theMeshBase.createTransactionNow();
        
        MeshObject obj11 = life.createMeshObject();
        MeshObject obj12 = life.createMeshObject();
        MeshObject obj13 = life.createMeshObject();
        MeshObject obj14 = life.createMeshObject();
        MeshObject obj21 = life.createMeshObject();
        MeshObject obj22 = life.createMeshObject();
        MeshObject obj23 = life.createMeshObject();
        
        tx.commitTransaction();
        
        //
        
        log.info( "Transactions are required" );
        
        try {
            obj11.addAsEquivalent( obj12 );
            reportError( "Should require transaction" );

        } catch( TransactionException ex ) {
            // noop
        }
        
        //
        
        log.info( "Making two MeshObjects equivalent" );
        
        tx = theMeshBase.createTransactionNow();
        
        obj12.addAsEquivalent( obj13 );

        tx.commitTransaction();
        
        MeshObjectSet equivalents1 = obj12.getEquivalents();
        
        checkEquals( equivalents1.size(), 2, "wrong number of equivalents" );
        checkCondition( equivalents1.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents1.contains( obj13 ), "obj13 not in the set" );

        //
        
        log.info( "Adding two more" );
        
        tx = theMeshBase.createTransactionNow();
        
        obj12.addAsEquivalent( obj11 );
        obj12.addAsEquivalent( obj14 );
        
        // doing it again should throw exception
        try {
            obj11.addAsEquivalent( obj12 );
            reportError( "Should have thrown Exception" );
            
        } catch( EquivalentAlreadyException ex ) {
            // noop
        }

        tx.commitTransaction();
        
        equivalents1 = obj12.getEquivalents();
        
        checkEquals( equivalents1.size(), 4, "wrong number of equivalents" );
        checkCondition( equivalents1.contains( obj11 ), "obj12 not in the set" );
        checkCondition( equivalents1.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents1.contains( obj13 ), "obj13 not in the set" );
        checkCondition( equivalents1.contains( obj14 ), "obj12 not in the set" );
        
        //
        
        log.info( "Creating a second set" );
        
        tx = theMeshBase.createTransactionNow();
        
        obj22.addAsEquivalent( obj21 );
        obj22.addAsEquivalent( obj23 );
        
        tx.commitTransaction();
        
        MeshObjectSet equivalents2 = obj21.getEquivalents();
        
        checkEquals( equivalents2.size(), 3, "wrong number of equivalents" );
        checkCondition( equivalents2.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents2.contains( obj22 ), "obj22 not in the set" );
        checkCondition( equivalents2.contains( obj23 ), "obj22 not in the set" );
        
        //
        
        log.info( "Merging the set" );
        
        tx = theMeshBase.createTransactionNow();

        obj12.addAsEquivalent( obj22 );
        
        tx.commitTransaction();
        
        MeshObjectSet equivalents3 = obj13.getEquivalents();
        
        checkEquals( equivalents3.size(), 7, "wrong number of equivalents" );
        checkCondition( equivalents3.contains( obj11 ), "obj11 not in the set" );
        checkCondition( equivalents3.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents3.contains( obj13 ), "obj13 not in the set" );
        checkCondition( equivalents3.contains( obj14 ), "obj14 not in the set" );
        checkCondition( equivalents3.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents3.contains( obj22 ), "obj22 not in the set" );
        checkCondition( equivalents3.contains( obj23 ), "obj23 not in the set" );
        
        //
        
        log.info( "Removing one item" );
        
        tx = theMeshBase.createTransactionNow();

        obj13.removeAsEquivalent(); // obj22 );
        
        tx.commitTransaction();
        
        MeshObjectSet equivalents4 = obj11.getEquivalents();
        
        checkEquals( equivalents4.size(), 6, "wrong number of equivalents" );
        checkCondition( equivalents4.contains( obj11 ), "obj11 not in the set" );
        checkCondition( equivalents4.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents4.contains( obj14 ), "obj14 not in the set" );
        checkCondition( equivalents4.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents4.contains( obj22 ), "obj22 not in the set" );
        checkCondition( equivalents4.contains( obj23 ), "obj23 not in the set" );

        MeshObjectSet equivalents5 = obj13.getEquivalents();
        
        checkEquals( equivalents5.size(), 1, "wrong number of equivalents" );
        checkCondition( equivalents5.contains( obj13 ), "obj13 not in the set" );
        
        //
        
        log.info( "Removing left item" );
        
        tx = theMeshBase.createTransactionNow();

        obj11.removeAsEquivalent(); //  obj22 );
        
        tx.commitTransaction();
        
        MeshObjectSet equivalents6 = obj12.getEquivalents();
        
        checkEquals( equivalents6.size(), 5, "wrong number of equivalents" );
        checkCondition( equivalents6.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents6.contains( obj14 ), "obj14 not in the set" );
        checkCondition( equivalents6.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents6.contains( obj22 ), "obj22 not in the set" );
        checkCondition( equivalents6.contains( obj23 ), "obj23 not in the set" );

        MeshObjectSet equivalents7 = obj11.getEquivalents();
        
        checkEquals( equivalents7.size(), 1, "wrong number of equivalents" );
        checkCondition( equivalents7.contains( obj11 ), "obj11 not in the set" );
        
        //
        
        log.info( "Removing right item" );
        
        tx = theMeshBase.createTransactionNow();

        obj23.removeAsEquivalent(); //  obj22 );
        
        tx.commitTransaction();
        
        MeshObjectSet equivalents8 = obj22.getEquivalents();
        
        checkEquals( equivalents8.size(), 4, "wrong number of equivalents" );
        checkCondition( equivalents8.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents8.contains( obj14 ), "obj14 not in the set" );
        checkCondition( equivalents8.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents8.contains( obj22 ), "obj22 not in the set" );

        MeshObjectSet equivalents9 = obj23.getEquivalents();
        
        checkEquals( equivalents9.size(), 1, "wrong number of equivalents" );
        checkCondition( equivalents9.contains( obj23 ), "obj23 not in the set" );
        
        // 
        
        log.info( "Deleting an object" );
        
        tx = theMeshBase.createTransactionNow();

        life.deleteMeshObject( obj14 );
        
        tx.commitTransaction();

        equivalents8 = obj21.getEquivalents();
        
        checkEquals( equivalents8.size(), 3, "wrong number of equivalents" );
        checkCondition( equivalents8.contains( obj12 ), "obj12 not in the set" );
        checkCondition( equivalents8.contains( obj21 ), "obj21 not in the set" );
        checkCondition( equivalents8.contains( obj22 ), "obj22 not in the set" );        
        
        // we can't look for what obj14 thinks because it is dead already
    }
    
    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest8.class );    
}
