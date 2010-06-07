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

package org.infogrid.kernel.test.meshbase.m.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;
import org.infogrid.util.logging.Log;

/**
 * Tests that MeshObjects automatically have owners.
 */
public class MeshBaseSecurityTest1
        extends
            AbstractMeshBaseSecurityTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
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
        
        MeshObject owner1 = obj1.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember();
        MeshObject owner2 = obj2.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() ).getSingleMember();
        checkIdentity( owner1, caller, "Not the right owner" );
        checkIdentity( owner2, caller, "Not the right owner" );
        
        //
        
        log.info( "Set anonymous owner and try again" );
        
        ThreadIdentityManager.unsetCaller();
        
        tx = theMeshBase.createTransactionNow();
        
        MeshObject obj3 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 3" ) );
        MeshObject obj4 = life.createMeshObject( theMeshBase.getMeshObjectIdentifierFactory().fromExternalForm( "object 4" ) );
        
        tx.commitTransaction();
        
        MeshObjectSet ownerSet3 = obj3.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
        MeshObjectSet ownerSet4 = obj4.traverse( AclBasedSecuritySubjectArea.MESHOBJECT_HASOWNER_MESHOBJECT.getSource() );
        checkCondition( ownerSet3.isEmpty(), "Has an owner, but should not" );
        checkCondition( ownerSet4.isEmpty(), "Has an owner, but should not" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseSecurityTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseSecurityTest1( args );
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
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public MeshBaseSecurityTest1(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseSecurityTest1.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseSecurityTest1.class );
}
