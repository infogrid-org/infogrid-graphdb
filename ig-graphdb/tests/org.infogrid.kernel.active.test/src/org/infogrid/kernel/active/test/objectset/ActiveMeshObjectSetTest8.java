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

package org.infogrid.kernel.active.test.objectset;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.ByPropertyValueSorter;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetFactory;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSet;
import org.infogrid.mesh.set.active.m.ConstantActiveMMeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Tests OrderedActiveMeshObjectSet.
 */
public class ActiveMeshObjectSetTest8
    extends
        AbstractActiveMeshObjectSetTest
{
    /**
     * Tun the test.
     * 
     * @throws Exception all kinds of things may go wrong during a test
     */
    public void run()
        throws
            Exception
    {
        log.info( "Setting up objects" );

        Transaction tx = theMeshBase.createTransactionAsap();

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        MeshObject [] testData = new MeshObject[ 10 ];
        for( int i=0 ; i<testData.length ; ++i ) {
            testData[i] = createTestObject( life, typeAA, typeX, i );
        }
        tx.commitTransaction();

        int depth = 3;
        MeshObjectSorter theSorter = ByPropertyValueSorter.create( typeX );

        TestMeshObjectSet inputSet = new TestMeshObjectSet( theMeshObjectSetFactory, testData );
        dumpMeshObjectSet( inputSet, "TestData", typeX, log );

        OrderedActiveMeshObjectSet selectedSet = theMeshObjectSetFactory.createOrderedActiveMeshObjectSet(
                inputSet,
                theSorter,
                depth );
        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );

        ActiveMeshObjectSetTestListener theListener = new ActiveMeshObjectSetTestListener( "listener", selectedSet, log );

        //

        log.info( "check initial results" );

        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "1",
                    "2"
                },
                "wrong initial set" );

        //

        log.info( "remove an object outside of the depth" );

        inputSet.remove( testData[7] );

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "1",
                    "2"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    0, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), 0, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();

        //

        log.info( "remove an object within the depth" );

        inputSet.remove( testData[1] );

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "2",
                    "3"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    1, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), 1, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();

        //

        log.info( "adding an object outside the depth" );

        inputSet.add( testData[7] );

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "2",
                    "3"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    0, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), 0, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();

        //

        log.info( "adding an object within the depth" );

        inputSet.add( testData[1] );

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "1",
                    "2"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    1, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), 1, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();

        //

        log.info( "remove all objects except one" );

        for( int i=0 ; i<testData.length ; ++i ) {
            if( i == 5 ) {
                continue;
            }

            inputSet.remove( testData[i] );
            dumpMeshObjectSet( selectedSet, "After removing " + i + ", SelectedSet", typeX, log );
        }

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "5"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    testData.length-1-2, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), testData.length-1, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();

        //

        log.info( "put them back in" );
        int [] putBackSequence = { 0, 9, 4, 8, 7, 2, 3, 1, 6 };
        for( int i=0 ; i<putBackSequence.length ; ++i ) {
            MeshObject toAdd = testData[ putBackSequence[ i ]];
            inputSet.add( toAdd );
            dumpMeshObjectSet( selectedSet, "After adding " + i + ", SelectedSet", typeX, log );
        }

        dumpMeshObjectSet( selectedSet, "SelectedSet", typeX, log );
        checkMeshObjectSet(
                selectedSet,
                typeX,
                new String[] {
                    "0",
                    "1",
                    "2"
                },
                "wrong set" );

        checkEquals( theListener.getAddCounter(),    6, "wrong number of add events" );
        checkEquals( theListener.getRemoveCounter(), 4, "wrong number of remove events" );
        checkEquals( theListener.getPropertyChangesCounter(), 0, "wrong number of property change events" );
        checkEquals( theListener.getRoleChangesCounter(),     0, "wrong number of role events" );
        theListener.reset();
    }

    /**
     * Create one test object.
     * 
     * @param life the lifecycle manager to create the test MeshObject
     * @param eType the type of MeshObject
     * @param pType the PropertyType of the property to set
     * @param index the value of the property
     * @return the created MeshObject
     * @throws Exception all kinds of things may go wrong during a test
     */
    protected MeshObject createTestObject(
            MeshBaseLifecycleManager   life,
            EntityType                 eType,
            PropertyType               pType,
            int                        index )
        throws
            Exception
    {
        MeshObject ret = createMeshObject( life, eType, life.getMeshBase().getMeshObjectIdentifierFactory().fromExternalForm( "CPO" + index ) );
        ret.setPropertyValue( pType, StringValue.create( String.valueOf( index )) );

        return ret;
    }

    /**
     * The main program.
     *
     * @param args the command-line arguments
     */
    public static void main(
             String [] args )
    {
        ActiveMeshObjectSetTest8 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ActiveMeshObjectSetTest8( args );
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
     * @throws Exception all kinds of things may go wrong during a test
     */
    public ActiveMeshObjectSetTest8(
            String [] args )
        throws
            Exception
    {
        super( ActiveMeshObjectSetTest8.class  );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ActiveMeshObjectSetTest8.class);


    /**
     * The ActiveMeshObjectSet that contains our input data.
     */
    static class TestMeshObjectSet
        extends
            ConstantActiveMMeshObjectSet
    {
        /**
         * Constructor.
         * 
         * @param factory the ActiveMeshObjectSetFactory
         * @param data the data held by the set
         */
        public TestMeshObjectSet(
                ActiveMeshObjectSetFactory factory,
                MeshObject []              data )
        {
            super( factory, data );
        }

        /**
         * Add an object.
         * 
         * @param toAdd the object to add
         */
        public void add(
                MeshObject toAdd  )
        {
            super.certainlyAdd( toAdd );
        }

        /**
         * Remove an object.
         * 
         * @param toRemove the object to remove
         */
        public void remove(
                MeshObject toRemove )
        {
            super.certainlyRemove( toRemove );
        }
    }
}
