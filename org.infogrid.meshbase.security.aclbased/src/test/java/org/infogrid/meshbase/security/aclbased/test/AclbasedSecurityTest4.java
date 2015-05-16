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
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that only the owner of a ProtectionDomain may grant/revoke any form of rights to/from
 * a ProtectionDomain.
 */
public class AclbasedSecurityTest4
        extends
            AbstractAclbasedSecurityTest
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
        log.info( "Creating principals" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject thirdParty = life.createMeshObject();
        
        MeshObject owner    = life.createMeshObject();

        ThreadIdentityManager.setCaller( owner );
        MeshObject ownerProtectionDomain = life.createMeshObject( AclbasedSubjectArea.PROTECTIONDOMAIN );
        ThreadIdentityManager.unsetCaller();
        
        MeshObject attacker = life.createMeshObject();

        ThreadIdentityManager.setCaller( attacker );
        MeshObject attackerProtectionDomain = life.createMeshObject( AclbasedSubjectArea.PROTECTIONDOMAIN );
        ThreadIdentityManager.unsetCaller();

        tx.commitTransaction();
        
        ThreadIdentityManager.sudo();
        checkEquals(    owner.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getDestination() ).size(), 1, "wrong number objects owned by owner" );
        checkEquals( attacker.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getDestination() ).size(), 1, "wrong number objects owned by attacker" );
        ThreadIdentityManager.sudone();
        
        //
        
        log.info( "Attacker cannot add third party gaining rights in owner's ProtectionDomain" );

        tx = theMeshBase.createTransactionNow();

        ThreadIdentityManager.setCaller( attacker );
        thirdParty.relate( ownerProtectionDomain );
        for( RelationshipType right : rightsTypes ) {
            try {
                thirdParty.blessRelationship( right.getSource(), ownerProtectionDomain );
                
                reportError( "Attacker could add third party to owner's ProtectionDomain using RelationshipType ", right.getIdentifier() );
            } catch( NotPermittedException ex ) {
                // noop
            }
        }
        ThreadIdentityManager.unsetCaller();
        tx.commitTransaction();
        
        //
        
        log.info( "Owner can add third party gaining rights in owner's ProtectionDomain" );
        
        tx = theMeshBase.createTransactionNow();

        ThreadIdentityManager.setCaller( owner );
        for( RelationshipType right : rightsTypes ) {
            try {
                thirdParty.blessRelationship( right.getSource(), ownerProtectionDomain );
                
            } catch( NotPermittedException ex ) {
                reportError( "Owner could not add third party to owner's ProtectionDomain using RelationshipType ", right.getIdentifier() );
            }
        }
        ThreadIdentityManager.unsetCaller();
        tx.commitTransaction();
        
    }

    // Our Logger
    private static Log log = Log.getLogInstance( AclbasedSecurityTest4.class );
}
