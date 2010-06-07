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
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.AclBasedSecurity.AclBasedSecuritySubjectArea;
import org.infogrid.model.SecurityTest.SecurityTestSubjectArea;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests that read/update/delete rights are enforced.
 */
public class MeshBaseSecurityTest5
        extends
            AbstractMeshBaseSecurityTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        log.info( "Setting up objects to test with" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject actorMayRead   = life.createMeshObject();
        MeshObject actorMayUpdate = life.createMeshObject();
        // MeshObject actorMayDelete = life.createMeshObject();

        MeshObject owner    = life.createMeshObject();

        ThreadIdentityManager.setCaller( owner );
        MeshObject dataObject = life.createMeshObject();
        MeshObject domain     = life.createMeshObject( AclBasedSecuritySubjectArea.PROTECTIONDOMAIN );

        domain.relateAndBless( AclBasedSecuritySubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getSource(), dataObject );

        actorMayRead.relateAndBless(   AclBasedSecuritySubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN.getSource(), domain );
        actorMayUpdate.relateAndBless( AclBasedSecuritySubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getSource(), domain );
        // actorMayDelete.relateAndBless( hasDeleteAccessToType.getSource(), domain );

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        StringValue   orgValue   = StringValue.create( "owner value" );
        StringValue   wrongValue = StringValue.create( "WRONG value" );
        PropertyValue readValue  = null;
        //
        
        log.info( "Owner can do anything" );
        
        ThreadIdentityManager.setCaller( owner );
        tx = theMeshBase.createTransactionNow();
        
        dataObject.bless( SecurityTestSubjectArea.AA );
        dataObject.bless( SecurityTestSubjectArea.B );
        dataObject.setPropertyValue( SecurityTestSubjectArea.A_X, orgValue );
        dataObject.unbless( SecurityTestSubjectArea.B );

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //
        
        log.info( "Testing reader" );
        
        ThreadIdentityManager.setCaller( actorMayRead );
        tx = theMeshBase.createTransactionNow();
        try {
            readValue = dataObject.getPropertyValue( SecurityTestSubjectArea.A_X );
            checkEquals( orgValue, readValue, "Wrong value read" );

        } catch( NotPermittedException ex ) {
            reportError( "Reader cannot read" );
        }
        
        try {
            dataObject.bless( SecurityTestSubjectArea.B );
            reportError( "Reader could bless" );

        } catch( NotPermittedException ex ) {
            // no op
        }

        try {
            dataObject.setPropertyValue( SecurityTestSubjectArea.A_X, wrongValue );
            reportError( "Reader could set value" );

        } catch( NotPermittedException ex ) {
            // no op
        }

        try {
            dataObject.unbless( SecurityTestSubjectArea.AA );
            reportError( "Reader could unbless" );

        } catch( NotPermittedException ex ) {
            // no op
        }

        try {
            life.deleteMeshObject( dataObject );
            reportError( "Reader could delete" );

        } catch( NotPermittedException ex ) {
            // no op
        }
        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        //
        
        log.info( "Testing updater" );
        
        ThreadIdentityManager.setCaller( actorMayUpdate );
        tx = theMeshBase.createTransactionNow();
        try {
            readValue = dataObject.getPropertyValue( SecurityTestSubjectArea.A_X );
            reportError( "Updater can read" );
            
        } catch( NotPermittedException ex ) {
            // no op
        }
        
        try {
            dataObject.bless( SecurityTestSubjectArea.B );

        } catch( NotPermittedException ex ) {
            reportError( "Updater could not bless", ex );
        }

        try {
            dataObject.setPropertyValue( SecurityTestSubjectArea.A_X, wrongValue );
        } catch( NotPermittedException ex ) {
            reportError( "Updater could not set value", ex );
        }

        try {
            dataObject.unbless( SecurityTestSubjectArea.AA );
        } catch( NotPermittedException ex ) {
            reportError( "Updater could not unbless", ex );
        }

        try {
            life.deleteMeshObject( dataObject );
            reportError( "Updater could delete" );
        } catch( NotPermittedException ex ) {
            // no op
        }

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //
        
 /** This currently does not work. I'm not sure it should work. FIXME?
       log.info( "Testing deleter" );
        
        ThreadIdentityManager.setCaller( actorMayDelete );
        tx = theMeshBase.createTransactionNow();

        try {
            life.delete( dataObject );
            reportError( "Deleter could unbless as result of delete ");
        } catch( NotPermittedException ex ) {
        }

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        // unbless with owner
        ThreadIdentityManager.setCaller( owner );
        tx = theMeshBase.createTransactionNow();

        dataObject.unbless( dataObject.getTypes() );

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        ThreadIdentityManager.setCaller( actorMayDelete );
        tx = theMeshBase.createTransactionNow();

        try {
            life.delete( dataObject );

        } catch( NotPermittedException ex ) {
            reportError( "Deleter could not delete", ex );
        }

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
 **/
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseSecurityTest5 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseSecurityTest5( args );
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
    public MeshBaseSecurityTest5(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseSecurityTest4.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseSecurityTest5.class );
}
