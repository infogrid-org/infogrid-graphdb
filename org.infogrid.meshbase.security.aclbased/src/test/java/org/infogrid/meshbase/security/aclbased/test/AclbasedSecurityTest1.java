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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that MeshObjects automatically have owners.
 */
public class AclbasedSecurityTest1
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
        log.info( "First we are creating ourselves a caller object and put it in place." );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject caller = life.createMeshObject();
        
        tx.commitTransaction();
        
        ThreadIdentityManager.setCaller( caller );
        
        checkIdentity( caller, ThreadIdentityManager.getCaller(), "Not the same caller" );
        
        //
        
        log.info( "Newly created objects have an owner" );
        
        tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 1" ) );
        MeshObject obj2 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 2" ) );
        
        tx.commitTransaction();
        
        MeshObject owner1 = obj1.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember();
        MeshObject owner2 = obj2.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember();
        checkIdentity( owner1, caller, "Not the right owner" );
        checkIdentity( owner2, caller, "Not the right owner" );
        
        //
        
        log.info( "Set anonymous owner and try again" );
        
        ThreadIdentityManager.unsetCaller();
        
        tx = theMeshBase.createTransactionNow();
        
        MeshObject obj3 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 3" ) );
        MeshObject obj4 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 4" ) );
        
        tx.commitTransaction();
        
        MeshObjectSet ownerSet3 = obj3.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
        MeshObjectSet ownerSet4 = obj4.traverse( AclbasedSubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
        checkCondition( ownerSet3.isEmpty(), "Has an owner, but should not" );
        checkCondition( ownerSet4.isEmpty(), "Has an owner, but should not" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( AclbasedSecurityTest1.class );
}
