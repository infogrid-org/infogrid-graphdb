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

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests whether the EquivalenceSet works with the StoreMeshBase.
 */
public class StoreMeshBaseTest3
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Test
    public void run()
        throws
            Exception
    {
        EntityType typeAA = theModelBase.findEntityType( "org.infogrid.model.Test", null, "AA" );
        EntityType typeB  = theModelBase.findEntityType( "org.infogrid.model.Test", null, "B" );

        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        //

        log.info( "Creating MeshBase" );

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "mb" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();

        //
        
        MeshObjectIdentifier nameAA1 = mb.getMeshObjectIdentifierFactory().fromExternalForm( "AA1" );
        MeshObjectIdentifier nameAA2 = mb.getMeshObjectIdentifierFactory().fromExternalForm( "AA2" );
        MeshObjectIdentifier nameAA3 = mb.getMeshObjectIdentifierFactory().fromExternalForm( "AA3" );

        MeshObjectIdentifier nameB1  = mb.getMeshObjectIdentifierFactory().fromExternalForm( "B1" );
        MeshObjectIdentifier nameB2  = mb.getMeshObjectIdentifierFactory().fromExternalForm( "B2" );
        MeshObjectIdentifier nameB3  = mb.getMeshObjectIdentifierFactory().fromExternalForm( "B3" );
        
        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        MeshObject objAA1 = life.createMeshObject( nameAA1, typeAA );
        MeshObject objAA2 = life.createMeshObject( nameAA2, typeAA );
        MeshObject objAA3 = life.createMeshObject( nameAA3, typeAA );
        
        MeshObject objB1 = life.createMeshObject( nameB1, typeB );
        MeshObject objB2 = life.createMeshObject( nameB2, typeB );
        MeshObject objB3 = life.createMeshObject( nameB3, typeB );

        objAA1.addAsEquivalent( objAA3 );
        objAA1.addAsEquivalent( objB2 );

        objB1.addAsEquivalent( objAA2 );
        
        tx.commitTransaction();
        
        //
        
        log.info( "Clearing cache, and loading MeshObjects again" );
        
        objAA1 = null;
        objAA2 = null;
        objAA3 = null;
        objB1  = null;
        objB2  = null;
        objB3  = null;
        
        mb.clearMemoryCache();

        objAA1 = mb.findMeshObjectByIdentifier( nameAA1 );
        objAA2 = mb.findMeshObjectByIdentifier( nameAA2 );
        objAA3 = mb.findMeshObjectByIdentifier( nameAA3 );

        objB1  = mb.findMeshObjectByIdentifier( nameB1 );
        objB2  = mb.findMeshObjectByIdentifier( nameB2 );
        objB3  = mb.findMeshObjectByIdentifier( nameB3 );
        
        checkObject( objAA1, "objAA1 could not be restored" );
        checkObject( objAA2, "objAA2 could not be restored" );
        checkObject( objAA3, "objAA3 could not be restored" );

        checkObject( objB1,  "objB1 could not be restored" );
        checkObject( objB2,  "objB2 could not be restored" );

        MeshObjectSet set1 = objAA1.getEquivalents();
        MeshObjectSet set2 = objB1.getEquivalents();
        MeshObjectSet set3 = objB3.getEquivalents();
        
        checkEquals( set1.size(), 3, "Wrong elements in set1" );
        checkCondition( set1.contains( objAA1 ), "objAA1 not in set1" );
        checkCondition( set1.contains( objAA3 ), "objAA3 not in set1" );
        checkCondition( set1.contains( objB2 ),  "objB2 not in set1" );
        
        checkEquals( set2.size(), 2, "Wrong elements in set2" );
        checkCondition( set2.contains( objB1 ),  "objB1 not in set2" );
        checkCondition( set2.contains( objAA2 ), "objAA2 not in set2" );
        
        checkEquals( set3.size(), 1, "Wrong elements in set3" );
        checkCondition( set3.contains( objB3 ), "objB3 not in set3" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest3.class);
}
