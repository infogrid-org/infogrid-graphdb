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

package org.infogrid.meshbase.security.aclbased.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests that read/update/delete rights are enforced.
 */
public class AclbasedSecurityTest5
        extends
            AbstractAclbasedSecurityTest
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
        MeshObject actorMayDelete = life.createMeshObject();

        MeshObject owner    = life.createMeshObject();

        ThreadIdentityManager.setCaller( owner );
        MeshObject dataObject = life.createMeshObject();
        MeshObject domain     = life.createMeshObject( AclbasedSubjectArea.PROTECTIONDOMAIN );

        domain.relateAndBless( AclbasedSubjectArea.PROTECTIONDOMAIN_GOVERNS_MESHOBJECT.getSource(), dataObject );

        actorMayRead.relateAndBless(   AclbasedSubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN.getSource(), domain );
        actorMayUpdate.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN.getSource(), domain );
        actorMayDelete.relateAndBless( AclbasedSubjectArea.MESHOBJECT_HASDELETEACCESSTO_PROTECTIONDOMAIN.getSource(), domain );

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
        
        StringValue   orgValue   = StringValue.create( "owner value" );
        StringValue   wrongValue = StringValue.create( "WRONG value" );
        PropertyValue readValue  = null;
        //
        
        log.info( "Owner can do anything" );
        
        ThreadIdentityManager.setCaller( owner );
        tx = theMeshBase.createTransactionNow();
        
        dataObject.bless( TestSubjectArea.AA );
        dataObject.bless( TestSubjectArea.B );
        dataObject.setPropertyValue( TestSubjectArea.A_X, orgValue );
        dataObject.unbless( TestSubjectArea.B );

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();

        //
        
        log.info( "Testing reader" );
        
        ThreadIdentityManager.setCaller( actorMayRead );
        tx = theMeshBase.createTransactionNow();
        try {
            readValue = dataObject.getPropertyValue( TestSubjectArea.A_X );
            checkEquals( orgValue, readValue, "Wrong value read" );

        } catch( NotPermittedException ex ) {
            reportError( "Reader cannot read" );
        }
        
        try {
            dataObject.bless( TestSubjectArea.B );
            reportError( "Reader could bless" );

        } catch( NotPermittedException ex ) {
            // no op
        }

        try {
            dataObject.setPropertyValue( TestSubjectArea.A_X, wrongValue );
            reportError( "Reader could set value" );

        } catch( NotPermittedException ex ) {
            // no op
        }

        try {
            dataObject.unbless( TestSubjectArea.AA );
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
            readValue = dataObject.getPropertyValue( TestSubjectArea.A_X );
            reportError( "Updater can read" );
            
        } catch( NotPermittedException ex ) {
            // no op
        }
        
        try {
            dataObject.bless( TestSubjectArea.B );

        } catch( NotPermittedException ex ) {
            reportError( "Updater could not bless", ex );
        }

        try {
            dataObject.setPropertyValue( TestSubjectArea.A_X, wrongValue );
        } catch( NotPermittedException ex ) {
            reportError( "Updater could not set value", ex );
        }

        try {
            dataObject.unbless( TestSubjectArea.AA );
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
        
       log.info( "Testing deleter" );
        
        ThreadIdentityManager.setCaller( actorMayDelete );
        tx = theMeshBase.createTransactionNow();

        try {
            life.deleteMeshObject( dataObject );
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
            life.deleteMeshObject( dataObject );

        } catch( NotPermittedException ex ) {
            reportError( "Deleter could not delete", ex );
        }

        tx.commitTransaction();
        ThreadIdentityManager.unsetCaller();
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        AclbasedSecurityTest5 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new AclbasedSecurityTest5( args );
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
    public AclbasedSecurityTest5(
            String [] args )
        throws
            Exception
    {
        super( AclbasedSecurityTest4.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( AclbasedSecurityTest5.class );
}
