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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionAction;
import org.infogrid.meshbase.transaction.TransactionActionException;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests whether rollbacks work.
 */
public class RollbackTest2
        extends
            AbstractMeshBaseTest
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
        IterableMeshBase            theMeshBase = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase"), theModelBase, null, rootContext );
        MeshObjectIdentifierFactory idFact      = theMeshBase.getMeshObjectIdentifierFactory();

        final MeshObjectIdentifier fixed1Id = idFact.fromExternalForm( "fixed1" );
        final MeshObjectIdentifier fixed2Id = idFact.fromExternalForm( "fixed2" );
        final MeshObjectIdentifier fixed3Id = idFact.fromExternalForm( "fixed3" );
        final MeshObjectIdentifier fixed4Id = idFact.fromExternalForm( "fixed4" );

        //
        
        log.debug( "Creating MeshObjectGraph that won't be rolled back" );
        
        theMeshBase.executeNow( new TransactionAction<Void>() {
                @Override
                public Void execute()
                        throws
                            Throwable
                {
                    MeshObject fixed1 = life.createMeshObject( fixed1Id, TestSubjectArea.AA );
                    MeshObject fixed2 = life.createMeshObject( fixed2Id, TestSubjectArea.AA );
                    MeshObject fixed3 = life.createMeshObject( fixed3Id, TestSubjectArea.AA );
                    MeshObject fixed4 = life.createMeshObject( fixed4Id, TestSubjectArea.AA );

                    fixed1.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed2 );
                    fixed2.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed3 );
                    fixed3.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed4 );
                    fixed4.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed1 );
                    
                    return null;
                }
        });
        
        checkEquals( theMeshBase.size(), 5, "Wrong initial number of MeshObjects" );

        final MeshObject fixed1 = theMeshBase.findMeshObjectByIdentifier( fixed1Id );
        final MeshObject fixed2 = theMeshBase.findMeshObjectByIdentifier( fixed2Id );
        final MeshObject fixed3 = theMeshBase.findMeshObjectByIdentifier( fixed3Id );
        final MeshObject fixed4 = theMeshBase.findMeshObjectByIdentifier( fixed4Id );

        //
        
        log.debug( "Creating failing Transaction that will automatically be rolled back." );
        
        theMeshBase.executeNow( new TransactionAction<Void>() {
                @Override
                public Void execute()
                        throws
                            Throwable
                {
                    life.deleteMeshObject( fixed4 );
                    
                    fixed1.unrelate( fixed2 );
                    
                    fixed2.unblessRelationship( TestSubjectArea.AR1A.getSource(), fixed3 );
                    
                    throw new TransactionActionException.Rollback();
                }
                
                @Override
                public void preRollbackTransaction(
                        Transaction tx,
                        Throwable   causeForRollback )
                {
                    checkEquals( tx.getChangeSet().size(), 15, "Wrong number of changes" );
                }
        });
        
        //
        
        final MeshObject fixed4New = theMeshBase.findMeshObjectByIdentifier( fixed4Id );
        // fixed4 is now dead and cannot currently be revived (FIXME?)

        checkEquals( theMeshBase.size(), 5, "Wrong initial number of MeshObjects" );
        checkObject( theMeshBase.findMeshObjectByIdentifier( fixed1Id ), "fixed1Id not found" );
        checkObject( theMeshBase.findMeshObjectByIdentifier( fixed2Id ), "fixed2Id not found" );
        checkObject( theMeshBase.findMeshObjectByIdentifier( fixed3Id ), "fixed3Id not found" );
        checkObject( theMeshBase.findMeshObjectByIdentifier( fixed4Id ), "fixed4Id not found" );
        checkCondition( fixed1.isRelated( fixed2 ), "fixed1 is not related to fixed2" );
        checkCondition( fixed2.isRelated( fixed3 ), "fixed2 is not related to fixed3" );
        checkCondition( fixed3.isRelated( fixed4New ), "fixed3 is not related to fixed4" );
        checkCondition( fixed4New.isRelated( fixed1 ), "fixed4 is not related to fixed1" );
        checkEquals( fixed1.getRoleTypes( fixed2 ).length, 1, "Not blessed: fixed1 to fixed2");
        checkEquals( fixed2.getRoleTypes( fixed3 ).length, 1, "Not blessed: fixed2 to fixed3");
        checkEquals( fixed3.getRoleTypes( fixed4New ).length, 1, "Not blessed: fixed3 to fixed4");
        checkEquals( fixed4New.getRoleTypes( fixed1 ).length, 1, "Not blessed: fixed4 to fixed1");
    }

    // Our Logger
    private static Log log = Log.getLogInstance( RollbackTest2.class );
}
