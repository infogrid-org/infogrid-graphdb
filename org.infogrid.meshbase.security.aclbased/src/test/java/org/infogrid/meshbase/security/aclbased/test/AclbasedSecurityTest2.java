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

package org.infogrid.meshbase.security.aclbased.test;

import java.io.FileOutputStream;
import java.io.PrintStream;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that only owners can assign new owners to MeshObjects.
 */
public class AclbasedSecurityTest2
        extends
            AbstractAclbasedSecurityTest
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
        log.info( "Setup" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject owner1    = life.createMeshObject();
        MeshObject owner2    = life.createMeshObject();
        MeshObject attacker1 = life.createMeshObject();
        MeshObject attacker2 = life.createMeshObject();

        ThreadIdentityManager.setCaller( owner1 );
        
        MeshObject data = life.createMeshObject();
        
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        ThreadIdentityManager.sudo();
        checkIdentity( owner1, data.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember(), "Not the same owner1" );
        ThreadIdentityManager.sudone();
        
        
        //
        
        log.info( "Attacker cannot create new owner" );
        
        ThreadIdentityManager.setCaller( attacker1 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), attacker1 );
            
            reportError( "Attacker could add itself as an owner" );
        } catch( NotPermittedException ex ) {
            // noop
        }
        
        try {
            data.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), attacker2 );
            
            reportError( "Attacker could add second attacker as an owner" );
        } catch( NotPermittedException ex ) {
            // noop
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //

        log.info( "Attacker cannot remove rightful owner" );

        ThreadIdentityManager.setCaller( attacker1 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.unblessRelationship( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner1 );

            reportError( "Attacker could remove rightful owner" );
        } catch( NotPermittedException ex ) {
            // noop
        }

        try {
            data.unblessRelationship( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner1 );

            reportError( "Attacker could remove rightful owner" );
        } catch( NotPermittedException ex ) {
            // noop
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //

        log.info( "Owner can add second owner" );
        
        ThreadIdentityManager.setCaller( owner1 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner2 );
            
        } catch( NotPermittedException ex ) {
            reportError( "Owner could not add a second owner" );
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //
        
        log.info( "Owner can remove itself as owner" );
        
        ThreadIdentityManager.setCaller( owner1 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.unblessRelationship( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner1 );
            
        } catch( NotPermittedException ex ) {
            reportError( "Owner could not remove itself as owner" );
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Ex-owner is just like attacker now" );
        
        ThreadIdentityManager.setCaller( owner1 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.blessRelationship( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner1 );
            
            reportError( "Ex-owner could add itself as an owner" );
        } catch( NotPermittedException ex ) {
            // noop
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //
        
        log.info( "Second Owner can add back in first owner" );
        
        ThreadIdentityManager.setCaller( owner2 );
        tx = theMeshBase.createTransactionNow();

        try {
            data.blessRelationship( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource(), owner1 );
            
        } catch( NotPermittedException ex ) {
            reportError( "Second owner could not add ex-owner" );
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Make sure there are two owners" );

        ThreadIdentityManager.sudo();
        checkEquals( data.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).size(), 2, "wrong number of owners" );
        ThreadIdentityManager.sudone();
    }

    // Our Logger
    private static Log log = Log.getLogInstance( AclbasedSecurityTest2.class );
}
