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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * <p>Tests the "common neighbors" functionality with RoleTypes.</p>
 * <pre>
 * obj1(AA) -R- obj2(B) -R- objCenter(AA) -R- obj4(B) -R- obj3(AA)
 *                             | (RR)
 *                           obj6(B)
 *                             | (R)
 *                           obj5(AA)
 * </pre>
 */
public class MeshBaseTest12
        extends
            AbstractMeshBaseTest
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
        MeshBase                 theMeshBase = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ), theModelBase, null, rootContext );
        MeshBaseLifecycleManager life        = theMeshBase.getMeshBaseLifecycleManager();

        //
        
        log.info( "Looking up MeshTypes" );

        SubjectArea      sa     = theModelBase.findSubjectArea( "org.infogrid.model.Test", null );
        EntityType       typeAA = theModelBase.findEntityType( sa, "AA" );
        EntityType       typeB  = theModelBase.findEntityType( sa, "B" );
        RelationshipType typeR  = theModelBase.findRelationshipType( sa, "R" );
        RelationshipType typeRR = theModelBase.findRelationshipType( sa, "RR" );

        //
        
        log.info( "Create a few MeshObjects" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1 = life.createMeshObject( typeAA );
        MeshObject obj2 = life.createMeshObject( typeB );
        MeshObject obj3 = life.createMeshObject( typeAA );
        MeshObject obj4 = life.createMeshObject( typeB );
        MeshObject obj5 = life.createMeshObject( typeAA );
        MeshObject obj6 = life.createMeshObject( typeB );
        MeshObject objCenter = life.createMeshObject( typeAA );

        obj1.relateAndBless( typeR.getSource(),      obj2      );
        obj2.relateAndBless( typeR.getDestination(), objCenter );

        obj3.relateAndBless( typeR.getSource(),      obj4 );
        obj4.relateAndBless( typeR.getDestination(), objCenter );

        obj5.relateAndBless( typeR.getSource(),       obj6 );
        obj6.relateAndBless( typeRR.getDestination(), objCenter );

        tx.commitTransaction();

        //
        
        log.info( "Checking we got it right" );
        
        checkEquals( obj1.traverseToNeighborMeshObjects().size(), 1, "obj1 neighbors are wrong" );
        checkEquals( obj2.traverseToNeighborMeshObjects().size(), 2, "obj2 neighbors are wrong" );
        checkEquals( obj3.traverseToNeighborMeshObjects().size(), 1, "obj3 neighbors are wrong" );
        checkEquals( obj4.traverseToNeighborMeshObjects().size(), 2, "obj4 neighbors are wrong" );
        checkEquals( obj5.traverseToNeighborMeshObjects().size(), 1, "obj5 neighbors are wrong" );
        checkEquals( obj6.traverseToNeighborMeshObjects().size(), 2, "obj6 neighbors are wrong" );
        checkEquals( objCenter.traverseToNeighborMeshObjects().size(), 3, "objCenter neighbors are wrong" );

        //

        log.info( "Creating and checking set2a" );

        MeshObjectSet set2a = theMeshBase.findCommonNeighbors( obj1, typeR.getSource(), objCenter, typeR.getSource() );
        checkEquals( set2a.size(), 1, "Wrong size of set2a" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        set2a.getMeshObjects(),
                        new MeshObject[] { obj2 },
                        false ),
                "set2a has wrong content" );

        //
        
        log.info( "Creating and checking set2b" );

        MeshObjectSet set2b = theMeshBase.findCommonNeighbors( obj1, typeR.getDestination(), objCenter, typeR.getSource() );
        checkEquals( set2b.size(), 0, "Wrong size of set2b" );

        //
        
        log.info( "Creating and checking set34" );

        MeshObjectSet set34 = theMeshBase.findCommonNeighbors( obj3, obj4 );
        checkEquals( set34.size(), 0, "Wrong size of set34" );
        
        //
        
        log.info( "Creating and checking set4" );

        MeshObjectSet set4 = theMeshBase.findCommonNeighbors( obj3, objCenter );
        checkEquals( set4.size(), 1, "Wrong size of set4" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        set4.getMeshObjects(),
                        new MeshObject[] { obj4 },
                        false ),
                "set4 has wrong content" );

        //
        
        log.info( "Creating and checking setCenterA" );

        MeshObjectSet setCenterA = theMeshBase.findCommonNeighbors( obj2, obj4 );
        checkEquals( setCenterA.size(), 1, "Wrong size of setCenterA" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        setCenterA.getMeshObjects(),
                        new MeshObject[] { objCenter },
                        false ),
                "setCenterA has wrong content" );

        //
        
        log.info( "Creating and checking setCenterB" );

        MeshObjectSet setCenterB = theMeshBase.findCommonNeighbors( new MeshObject[] { obj2, obj4, obj6 } );
        checkEquals( setCenterB.size(), 1, "Wrong size of setCenterB" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        setCenterB.getMeshObjects(),
                        new MeshObject[] { objCenter },
                        false ),
                "setCenterB has wrong content" );

        //
        
        log.info( "Creating and checking setCenterC" );

        MeshObjectSet setCenterC = theMeshBase.findCommonNeighbors( new MeshObject[] { obj2, obj4, obj5 } );
        checkEquals( setCenterC.size(), 0, "Wrong size of setCenterC" );
        
        //
        
        theMeshBase.die();
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest12 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest12( args );
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
     * @throws Exception all sorts of things may go wrong during a test
     */
    public MeshBaseTest12(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest12.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest12.class );
}
