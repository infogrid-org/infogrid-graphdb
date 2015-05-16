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
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests a large number of traversals.
 */
public class MeshBaseTest17
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
        for( int i=BOTTOM_TEST_INDEX ; i<TOP_TEST_INDEX ; ++i ) {

            collectGarbage();

            log.info( "Running test " + i );

            Runtime rt = Runtime.getRuntime();
            log.debug( "Memory: max: " + rt.maxMemory() + ", total: " + rt.totalMemory() + ", free: " + rt.freeMemory() );

            MeshBase theMeshBase = MMeshBase.create(
                    theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                    theModelBase,
                    null,
                    rootContext );


            MeshObject [] objs = create( theMeshBase, i );
            relate(   theMeshBase, objs, i );
            traverse( theMeshBase, objs, i );

            log.debug( "Memory: max: " + rt.maxMemory() + ", total: " + rt.totalMemory() + ", free: " + rt.freeMemory() );

            theMeshBase.die();
        }
    }

    protected MeshObject [] create(
            MeshBase mb,
            int      index )
        throws
            Exception
    {
        MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idFact = mb.getMeshObjectIdentifierFactory();

        int n = 2<<index;
        MeshObject [] objs = new MeshObject[ n ];
        String PREFIX = "object-";

        //

        log.debug( "Creating " + n + " test objects" );

        Transaction tx = mb.createTransactionNow();

        long start = startClock();
        for( int i=0 ; i<n ; ++i ) {
            objs[i] = life.createMeshObject( idFact.fromExternalForm( PREFIX + String.valueOf( i )) );
        }

        tx.commitTransaction();

        long stop = getRelativeTime();

        log.info( "Took " + stop + " msec: " + ( stop * 1000L / n ) + " usec per object to create" );

        return objs;
    }

    protected void relate(
            MeshBase      mb,
            MeshObject [] objs,
            int           index )
        throws
            Exception
    {
        log.debug( "Relating " + objs.length + " test objects with " + RELATE_PREVIOUS );

        int nRelates = 0;
        Transaction tx = mb.createTransactionNow();

        long start = startClock();
        for( int i=0 ; i<objs.length ; ++i ) {
            for( int j=i-RELATE_PREVIOUS ; j<i ; ++j ) {
                if( j >= 0 ) {
                    objs[i].relate( objs[j] );
                    ++nRelates;
                }
            }
        }

        tx.commitTransaction();

        long stop = getRelativeTime();

        log.info( "Took " + stop + " msec: " + ( stop * 1000L / nRelates ) + " usec per relate" );
    }

    protected void traverse(
            MeshBase      mb,
            MeshObject [] objs,
            int           index )
        throws
            Exception
    {
        log.debug( "Traversing " + objs.length + " test objects with " + NUMBER_TRAVERSALS );

        int nTraversals = 0;

        long start = startClock();
        for( int i=0 ; i<objs.length ; ++i ) {
            MeshObjectSet current = objs[i].traverseToNeighborMeshObjects();
            ++nTraversals;
            for( int j=1 ; j<NUMBER_TRAVERSALS ; ++j ) {
                current = current.traverseToNeighborMeshObjects();
                ++nTraversals;
            }
        }

        long stop = getRelativeTime();

        log.info( "Took " + stop + " msec: " + ( stop * 1000L / nTraversals ) + " usec per traversal" );
    }

    /**
     * Number of tests to run.
     */
    public static final int BOTTOM_TEST_INDEX = 14;
    public static final int TOP_TEST_INDEX    = 15;

    /**
     * How many previous objects to relate to.
     */
    public static final int RELATE_PREVIOUS = 4;

    /**
     * How many traversals per object.
     */
    public static final int NUMBER_TRAVERSALS = 3;


    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest17.class );
}
