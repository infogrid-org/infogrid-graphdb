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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.L10PropertyValueMapImpl;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringDataType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.MeshTypeIdentifierFactory;
import org.infogrid.modelbase.MeshTypeLifecycleManager;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.util.logging.Log;

/**
 * Models are not supposed to change. But sometimes, they do, such as during development.
 * Tests that a supertype can be added without bad consequences.
 */
public class ModelChangeTest4
    extends
        AbstractModelChangeTest
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
        log.info( "Creating and populating MB1" );

        IterableStoreMeshBase mb1 = createTestStoreMeshBase(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                theSqlStore,
                rootContext );

        initializeModelAndMeshBase( mb1 );

        mb1.die();
        mb1 = null;

        //

        log.info( "Creating changed ModelBase" );

        MModelBase newModelBase = new TestingModelBase( "modified" );

        MeshTypeIdentifierFactory typeIdFact = newModelBase.getMeshTypeIdentifierFactory();
        MeshTypeLifecycleManager  typeLife   = newModelBase.getMeshTypeLifecycleManager();

        final SubjectArea sa = typeLife.createSubjectArea(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model" ),
                StringValue.create( "org.infogrid.meshbase.store.test.model" ),
                null,
                L10PropertyValueMapImpl.create( StringValue.create( "test.model") ),
                null, null, null, null,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final EntityType ent1 = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent1" ),
                StringValue.create( "Ent1" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent1") ),
                null, null,
                sa,
                null, null, null, null, null, null, null,
                BooleanValue.TRUE, // abstract
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final PropertyType ent1_prop1 = typeLife.createPropertyType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent1_Prop1" ),
                StringValue.create( "Ent1_Prop1" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent1_Prop1") ),
                null,
                ent1,
                sa,
                StringDataType.theDefault,
                null, null, null,
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                FloatValue.create( 1. ) );

        final EntityType ent2 = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent2" ),
                StringValue.create( "Ent2" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent2") ),
                null, null,
                sa,
                new EntityType[] { ent1 },
                null, null, null, null, null, null,
                BooleanValue.TRUE, // abstract
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final EntityType entNew = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/EntNew" ),
                StringValue.create( "EntNew" ),
                L10PropertyValueMapImpl.create( StringValue.create( "EntNew") ),
                null, null,
                sa,
                null, null, null, null, null, null, null,
                BooleanValue.FALSE, // not abstract
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final PropertyType entNew_prop1 = typeLife.createPropertyType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/EntNew_Prop1" ),
                StringValue.create( "EntNew_Prop1" ),
                L10PropertyValueMapImpl.create( StringValue.create( "EntNew_Prop1") ),
                null,
                ent1,
                sa,
                StringDataType.theDefault,
                StringValue.create( "Default value of EntNew_Prop1" ),
                null, null,
                BooleanValue.FALSE, // mandatory
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                FloatValue.create( 2. ) );

        final EntityType ent3 = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent3" ),
                StringValue.create( "Ent3" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent3") ),
                null, null,
                sa,
                new EntityType[] { ent2, entNew },
                null, null, null, null, null, null,
                BooleanValue.FALSE, // not abstract
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final EntityType ent4 = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent4" ),
                StringValue.create( "Ent4" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent4") ),
                null, null,
                sa,
                null, null, null, null, null, null, null,
                BooleanValue.FALSE, // not abstract
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        final RelationshipType rel3_4 = typeLife.createRelationshipType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Rel3_4" ),
                StringValue.create( "Rel3_4" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Rel3_4") ),
                null,
                sa,
                MultiplicityValue.ZERO_N,
                MultiplicityValue.ZERO_N,
                ent3,
                ent4,
                (RoleType[]) null, null, null, null,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        //

        log.info( "Creating mb2" );

        IterableStoreMeshBase mb2 = createTestStoreMeshBase(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                newModelBase,
                theSqlStore,
                rootContext );

        // Read all elements
        log.info( "Traversing mb2" );

        boolean found = false;
        for( MeshObject current : mb2 ) {
            if( log.isDebugEnabled() ) {
                log.debug( "Found", current );
            }
            if( current.getIdentifier().toExternalForm().equals( "#a" )) {
                checkEquals( current.getPropertyValue( entNew_prop1 ), entNew_prop1.getDefaultValue(), "wrong value" );
                found = true;
            }
        }
        if( !found ) {
            reportError( "Could not find #a" );
        }

        mb2.die();
        mb2 = null;
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        ModelChangeTest4 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ModelChangeTest4( args );
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
     * @throws Exception anything can go wrong in a test
     */
    public ModelChangeTest4(
            String [] args )
        throws
            Exception
    {
        super( ModelChangeTest4.class );
    }

    /**
     * The number of MeshObjects to create for the test.
     */
    protected int theTestSize;

    // Our Logger
    private static Log log = Log.getLogInstance( ModelChangeTest4.class );
}
