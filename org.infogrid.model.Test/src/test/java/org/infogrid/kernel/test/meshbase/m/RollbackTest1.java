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
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests whether rollbacks work.
 */
public class RollbackTest1
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
        final MeshObjectIdentifier wrong1Id = idFact.fromExternalForm( "wrong1" );
        final MeshObjectIdentifier wrong2Id = idFact.fromExternalForm( "wrong2" );

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

                    return null;
                }
        });
        
        checkEquals( theMeshBase.size(), 3, "Wrong initial number of MeshObjects" );

        final MeshObject fixed1 = theMeshBase.findMeshObjectByIdentifier( fixed1Id );
        final MeshObject fixed2 = theMeshBase.findMeshObjectByIdentifier( fixed2Id );

        PropertyValue fixed1Value = fixed1.getPropertyValue( TestSubjectArea.AA_Y );
        PropertyValue fixed2Value = fixed2.getPropertyValue( TestSubjectArea.AA_Y );
        
        //
        
        log.debug( "Creating failing Transaction that will automatically be rolled back." );
        
        theMeshBase.executeNow( new TransactionAction<Void>() {
                @Override
                public Void execute()
                        throws
                            Throwable
                {
                    MeshObject wrong1 = life.createMeshObject( wrong1Id, TestSubjectArea.AA );
                    MeshObject wrong2 = life.createMeshObject( wrong2Id, TestSubjectArea.AA );

                    fixed1.setPropertyValue( TestSubjectArea.AA_Y, FloatValue.create( 1.2 ));
                    
                    wrong1.relateAndBless( TestSubjectArea.AR1A.getSource(), wrong2 );
                    fixed1.relate( wrong1 );
                    wrong2.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed2 );
                    
                    wrong2.setPropertyValue( TestSubjectArea.AA_Y, FloatValue.create( 3.4 ));
                    
                    fixed1.relateAndBless( TestSubjectArea.AR1A.getSource(), fixed2 );
                    
                    throw new TransactionActionException.Rollback();
                }
                
                @Override
                public void preRollbackTransaction(
                        Transaction tx,
                        Throwable   causeForRollback )
                {
                    checkEquals( tx.getChangeSet().size(), 20, "Wrong number of changes" );
                }
        });
        
        //
        
        checkEquals( theMeshBase.size(), 3, "Wrong initial number of MeshObjects" );
        checkCondition( theMeshBase.findMeshObjectByIdentifier( wrong1Id ) == null, "Found wrong1Id" );
        checkCondition( theMeshBase.findMeshObjectByIdentifier( wrong2Id ) == null, "Found wrong2Id" );
        checkEquals( fixed1Value, fixed1.getPropertyValue( TestSubjectArea.AA_Y ), "Fixed1 has wrong property value" );
        checkEquals( fixed2Value, fixed2.getPropertyValue( TestSubjectArea.AA_Y ), "Fixed2 has wrong property value" );
        checkCondition( !fixed1.isRelated( fixed2 ), "fixed1 is still related to fixed2" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( RollbackTest1.class );
}
