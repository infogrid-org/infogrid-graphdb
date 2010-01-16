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
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * <p>Tests the "common neighbors" functionality.</p>
 * <pre>
 * obj1 - obj2 - objCenter - obj4 - obj3
 *                  |
 *                obj8
 *                  |
 *                obj7 - obj9
 *                  |
 *                obj6
 *                  |
 *                obj5
 * </pre>
 */
public class MeshBaseTest11
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
        
        log.info( "Create a few MeshObjects" );
        
        Transaction tx = theMeshBase.createTransactionNow();
        
        MeshObject obj1 = life.createMeshObject();
        MeshObject obj2 = life.createMeshObject();
        MeshObject obj3 = life.createMeshObject();
        MeshObject obj4 = life.createMeshObject();
        MeshObject obj5 = life.createMeshObject();
        MeshObject obj6 = life.createMeshObject();
        MeshObject obj7 = life.createMeshObject();
        MeshObject obj8 = life.createMeshObject();
        MeshObject obj9 = life.createMeshObject();
        MeshObject objCenter = life.createMeshObject();

        obj1.relate( obj2 );
        obj2.relate( objCenter );

        obj3.relate( obj4 );
        obj4.relate( objCenter );

        obj5.relate( obj6 );
        obj6.relate( obj7 );
        obj7.relate( obj8 );
        obj8.relate( objCenter );

        obj9.relate( obj7 );

        tx.commitTransaction();

        //
        
        log.info( "Checking we got it right" );
        
        checkEquals( obj1.traverseToNeighborMeshObjects().size(), 1, "obj1 neighbors are wrong" );
        checkEquals( obj2.traverseToNeighborMeshObjects().size(), 2, "obj2 neighbors are wrong" );
        checkEquals( obj3.traverseToNeighborMeshObjects().size(), 1, "obj3 neighbors are wrong" );
        checkEquals( obj4.traverseToNeighborMeshObjects().size(), 2, "obj4 neighbors are wrong" );
        checkEquals( obj5.traverseToNeighborMeshObjects().size(), 1, "obj5 neighbors are wrong" );
        checkEquals( obj6.traverseToNeighborMeshObjects().size(), 2, "obj6 neighbors are wrong" );
        checkEquals( obj7.traverseToNeighborMeshObjects().size(), 3, "obj7 neighbors are wrong" );
        checkEquals( obj8.traverseToNeighborMeshObjects().size(), 2, "obj8 neighbors are wrong" );
        checkEquals( obj9.traverseToNeighborMeshObjects().size(), 1, "obj9 neighbors are wrong" );
        checkEquals( objCenter.traverseToNeighborMeshObjects().size(), 3, "objCenter neighbors are wrong" );

        //

        MeshObjectSet set12 = theMeshBase.findCommonNeighbors( obj1, obj2 );
        checkEquals( set12.size(), 0, "Wrong size of set12" );
        
        MeshObjectSet set2 = theMeshBase.findCommonNeighbors( obj1, objCenter );
        checkEquals( set2.size(), 1, "Wrong size of set2" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        set2.getMeshObjects(),
                        new MeshObject[] { obj2 },
                        false ),
                "set2 has wrong content" );

        MeshObjectSet set34 = theMeshBase.findCommonNeighbors( obj3, obj4 );
        checkEquals( set34.size(), 0, "Wrong size of set34" );
        
        MeshObjectSet set4 = theMeshBase.findCommonNeighbors( obj3, objCenter );
        checkEquals( set4.size(), 1, "Wrong size of set4" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        set4.getMeshObjects(),
                        new MeshObject[] { obj4 },
                        false ),
                "set4 has wrong content" );
        
        MeshObjectSet setCenterA = theMeshBase.findCommonNeighbors( obj2, obj4 );
        checkEquals( setCenterA.size(), 1, "Wrong size of setCenterA" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        setCenterA.getMeshObjects(),
                        new MeshObject[] { objCenter },
                        false ),
                "setCenterA has wrong content" );

        MeshObjectSet setCenterB = theMeshBase.findCommonNeighbors( new MeshObject[] { obj2, obj4, obj8 } );
        checkEquals( setCenterB.size(), 1, "Wrong size of setCenterB" );
        checkCondition(
                ArrayHelper.hasSameContentOutOfOrder(
                        setCenterB.getMeshObjects(),
                        new MeshObject[] { objCenter },
                        false ),
                "setCenterB has wrong content" );

        MeshObjectSet setCenterC = theMeshBase.findCommonNeighbors( new MeshObject[] { obj2, obj4, obj7 } );
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
        MeshBaseTest11 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest11( args );
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
    public MeshBaseTest11(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest11.class );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest11.class );
}
