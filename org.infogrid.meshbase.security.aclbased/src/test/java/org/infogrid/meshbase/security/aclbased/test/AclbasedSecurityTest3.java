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
 * Tests that MeshObjects can only be added/removed to/from ProtectionDomains by the owner
 * of both the ProtectionDomain and the MeshObject.
 */
public class AclbasedSecurityTest3
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
        
        MeshObject owner = life.createMeshObject( idFact.fromExternalForm( "#owner" ));

        ThreadIdentityManager.setCaller( owner );

        MeshObject ownerProtectionDomain = life.createMeshObject( idFact.fromExternalForm( "#owner-protectiondomain" ), AclbasedSubjectArea.PROTECTIONDOMAIN );
        MeshObject ownerData             = life.createMeshObject( idFact.fromExternalForm( "#owner-data" ) );

        ThreadIdentityManager.unsetCaller();
        
        MeshObject attacker = life.createMeshObject( idFact.fromExternalForm( "#attacker" ));

        ThreadIdentityManager.setCaller( attacker );

        MeshObject attackerProtectionDomain = life.createMeshObject( idFact.fromExternalForm( "#attacker-protectiondomain" ), AclbasedSubjectArea.PROTECTIONDOMAIN );
        MeshObject attackerData             = life.createMeshObject( idFact.fromExternalForm( "#attacker-data" ) );

        ThreadIdentityManager.unsetCaller();

        tx.commitTransaction();
        
        ThreadIdentityManager.sudo();
        checkEquals(    owner.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getDestination() ).size(), 2, "wrong number objects owned by owner" );
        checkEquals( attacker.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getDestination() ).size(), 2, "wrong number objects owned by attacker" );
        ThreadIdentityManager.sudone();

        //
        
        log.info( "Attacker attempting to put owner's data into his own ProtectionDomain" );
        
        ThreadIdentityManager.setCaller( attacker );
        tx = theMeshBase.createTransactionNow();
        
        try {
            ownerData.relateAndBless( AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getDestination(), attackerProtectionDomain );
            reportError( "Attacker succeeded putting owner's data into attacker's ProtectionDomain" );

        } catch( NotPermittedException ex ) {
            // no op
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Owner putting owner's data into his own ProtectionDomain" );
        
        ThreadIdentityManager.setCaller( owner );
        tx = theMeshBase.createTransactionNow();
        
        try {
            ownerData.relateAndBless( AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getDestination(), ownerProtectionDomain );

        } catch( NotPermittedException ex ) {
            reportError( "Owner unable putting owner's data into owner's ProtectionDomain" );
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Attacker attempting to remove owner's ProtectionDomain from owner's data" );
        
        ThreadIdentityManager.setCaller( attacker );
        tx = theMeshBase.createTransactionNow();
        
        try {
            ownerData.unrelate( ownerProtectionDomain ); // This also checks that we don't accidentally unrelate
            reportError( "Attacker succeeded removing owner's data from owner's ProtectionDomain" );

        } catch( NotPermittedException ex ) {
            // no op
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Owner should be able to remove owner's ProtectionDomain from owner's data" );
        
        ThreadIdentityManager.setCaller( owner );
        tx = theMeshBase.createTransactionNow();
        
        try {
            ownerData.unrelate( ownerProtectionDomain ); // This also checks that we don't accidentally unrelate

        } catch( NotPermittedException ex ) {
            reportError( "Owner was unable to remove owner's ProtectionDomain from owner's data" );
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
    }

    // Our Logger
    private static Log log = Log.getLogInstance( AclbasedSecurityTest3.class );
}
