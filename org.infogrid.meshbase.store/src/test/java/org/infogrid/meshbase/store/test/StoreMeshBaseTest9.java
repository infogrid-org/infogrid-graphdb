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
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.mesh.RelatedAlreadyException;
import org.infogrid.mesh.RoleTypeBlessedAlreadyException;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Rollbacks,
 */
public class StoreMeshBaseTest9
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
        //
        
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();
        
        //

        log.info( "Creating MeshBase" );

        IterableStoreMeshBase mb = IterableStoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idFact = mb.getMeshObjectIdentifierFactory();

        MeshObjectIdentifier oneId   = idFact.fromExternalForm( "one" );
        MeshObjectIdentifier twoId   = idFact.fromExternalForm( "two" );
        MeshObjectIdentifier threeId = idFact.fromExternalForm( "three" );
        
        //

        log.info( "Creating MeshObjects" );
        
        Transaction tx = mb.createTransactionNow();

        MeshObject one   = life.createMeshObject( oneId, TestSubjectArea.AA );
        MeshObject two   = life.createMeshObject( twoId, TestSubjectArea.AA );
        MeshObject three = null;
        
        one.relateAndBless( TestSubjectArea.AR1A.getSource(), two );
        
        tx.commitTransaction();

        checkEquals( mb.size(), 3, "Wrong number of MeshObjects in MeshBase" );
        
        one = null;
        two = null;
        mb.clearMemoryCache();
        collectGarbage();

        checkEquals( mb.size(), 3, "Wrong number of MeshObjects in MeshBase" );

        //
        
        log.info( "Modifying (1)" );
        
        try {
            tx = mb.createTransactionNow();
        
            life.createMeshObject( threeId );
            life.createMeshObject( oneId, TestSubjectArea.B );
            
            reportError( "MeshObjectIdentifierNotUniqueException not thrown" );
            
        } catch( MeshObjectIdentifierNotUniqueException ex ) {
            // good
        } finally {
            tx.rollbackTransaction( null );
        }

        one = null;
        two = null;
        mb.clearMemoryCache();
        collectGarbage();
        
        one   = mb.findMeshObjectByIdentifier( oneId );
        two   = mb.findMeshObjectByIdentifier( twoId );
        three = mb.findMeshObjectByIdentifier( threeId );

        checkObject( one, "MeshObject one does not exist" );
        checkObject( two, "MeshObject two does not exist" );
        checkNotObject( three, "MeshObject three exists" );
        checkCondition( one.isBlessedBy( TestSubjectArea.AA ), "One not blessed with AA" );
        checkCondition( two.isBlessedBy( TestSubjectArea.AA ), "Two not blessed with AA" );
        checkCondition( !two.isBlessedBy( TestSubjectArea.B ), "Two blessed with B" );
        checkEquals( mb.size(), 3, "Wrong number of MeshObjects in MeshBase" );
        
        //
        
        log.info( "Modifying (2)" );
        
        try {
            tx = mb.createTransactionNow();
        
            life.createMeshObject( threeId );
            one.relate( two );
            
            reportError( "RelatedAlreadyException not thrown" );
            
        } catch( RelatedAlreadyException ex ) {
            // good
        } finally {
            tx.rollbackTransaction( null );
        }

        one = null;
        two = null;
        mb.clearMemoryCache();
        collectGarbage();
        
        one   = mb.findMeshObjectByIdentifier( oneId );
        two   = mb.findMeshObjectByIdentifier( twoId );
        three = mb.findMeshObjectByIdentifier( threeId );

        checkObject( one, "MeshObject one does not exist" );
        checkObject( two, "MeshObject two does not exist" );
        checkNotObject( three, "MeshObject three exists" );
        checkCondition( one.isBlessedBy( TestSubjectArea.AA ), "One not blessed with AA" );
        checkCondition( two.isBlessedBy( TestSubjectArea.AA ), "Two not blessed with AA" );
        checkCondition( !two.isBlessedBy( TestSubjectArea.B ), "Two blessed with B" );
        checkCondition( one.isRelated( TestSubjectArea.AR1A.getSource(), two ), "not related and blessed" );
        checkEquals( mb.size(), 3, "Wrong number of MeshObjects in MeshBase" );
    
        //
        
        log.info( "Modifying (3)" );
        
        try {
            tx = mb.createTransactionNow();
        
            life.createMeshObject( threeId );
            one.blessRelationship( TestSubjectArea.AR1A.getSource(), two );
            
            reportError( "RoleTypeBlessedAlreadyException not thrown" );
            
        } catch( RoleTypeBlessedAlreadyException ex ) {
            // good
        } finally {
            tx.rollbackTransaction( null );
        }

        checkObject( one, "MeshObject one does not exist" );
        checkObject( two, "MeshObject two does not exist" );
        checkNotObject( three, "MeshObject three exists" );
        checkCondition( one.isBlessedBy( TestSubjectArea.AA ), "One not blessed with AA" );
        checkCondition( two.isBlessedBy( TestSubjectArea.AA ), "Two not blessed with AA" );
        checkCondition( !two.isBlessedBy( TestSubjectArea.B ), "Two blessed with B" );
        checkCondition( one.isRelated( TestSubjectArea.AR1A.getSource(), two ), "not related and blessed" );
        checkEquals( mb.size(), 3, "Wrong number of MeshObjects in MeshBase" );
    
        one = null;
        two = null;
        mb.clearMemoryCache();
        collectGarbage();
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest9.class );
}
