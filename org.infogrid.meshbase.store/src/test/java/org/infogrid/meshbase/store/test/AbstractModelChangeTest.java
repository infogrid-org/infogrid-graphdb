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

package org.infogrid.meshbase.store.test;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.util.Locale;
import java.util.ResourceBundle;
import org.diet4j.core.ModuleRegistry;
import org.diet4j.core.ModuleRequirement;
import org.diet4j.inclasspath.InClasspathModuleRegistry;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.TypedMeshObjectFacadeImpl;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.a.AMeshBaseLifecycleManager;
import org.infogrid.mesh.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.store.IterableStoreMeshBase;
import org.infogrid.meshbase.store.StoreMeshBaseEntryMapper;
import org.infogrid.meshbase.store.StoreMeshBaseSwappingHashMap;
import org.infogrid.meshbase.transaction.TransactionAction;
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
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.m.MMeshTypeIdentifierFactory;
import org.infogrid.modelbase.m.MMeshTypeLifecycleManager;
import org.infogrid.modelbase.m.MMeshTypeSynonymDictionary;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.store.IterableStore;
import org.infogrid.store.sql.AbstractSqlStore;
import org.infogrid.store.sql.mysql.MysqlStore;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;
import org.infogrid.util.logging.log4j.Log4jLog;
import org.infogrid.util.logging.log4j.Log4jLogFactory;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Factors out common functionality of ModelChangeTests.
 */
public abstract class AbstractModelChangeTest
        extends
            AbstractTest
{
    /**
     * Initialize Module Framework, and initialize statics.
     * 
     * @throws Exception all sorts of things may go wrong in tests
     */
    @BeforeClass
    public static void initialize()
        throws
            Exception
    {
        ClassLoader    cl       = AbstractModelChangeTest.class.getClassLoader();
        ModuleRegistry registry = InClasspathModuleRegistry.instantiateOrGet( cl );

        registry.resolve( registry.determineSingleResolutionCandidate( ModuleRequirement.create( "org.infogrid", "org.infogrid.meshbase.store" ))).activateRecursively();
        
        Log4jLog.configure( "org/infogrid/meshbase/store/test/Log.properties", cl );
        Log.setLogFactory( new Log4jLogFactory());
        
        ResourceHelper.setApplicationResourceBundle( ResourceBundle.getBundle(
                "org/infogrid/meshbase/store/test/ResourceHelper",
                Locale.getDefault(),
                cl ));
    }

    /**
     * Setup.
     */
    @Before
    public void setup()
        throws
            Exception
    {
        theDataSource = new MysqlDataSource();
        theDataSource.setDatabaseName( test_DATABASE_NAME );

        theSqlStore = MysqlStore.create( theDataSource, test_TABLE_NAME );
        theSqlStore.deleteAll();
    }

    /**
     * Initialize the Model and populate the MeshBase.
     *
     * @param mb the MeshBase
     */
    protected void initializeModelAndMeshBase(
            MeshBase mb )
    {
        MeshTypeIdentifierFactory typeIdFact = theModelBase.getMeshTypeIdentifierFactory();
        MeshTypeLifecycleManager  typeLife   = theModelBase.getMeshTypeLifecycleManager();

        final SubjectArea sa = typeLife.createSubjectArea(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model" ),
                StringValue.create( "org.infogrid.meshbase.store.test.model" ),
                null,
                L10PropertyValueMapImpl.create( StringValue.create( "test.model") ),
                null, null, null,
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

        final EntityType ent3 = typeLife.createEntityType(
                typeIdFact.fromExternalForm( "org.infogrid.meshbase.store.test.model/Ent3" ),
                StringValue.create( "Ent3" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Ent3") ),
                null, null,
                sa,
                new EntityType[] { ent2 },
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


        final MeshObjectIdentifierFactory idFact = mb.getMeshObjectIdentifierFactory();
        final MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();

        mb.executeNow( new TransactionAction<Void>() {
            public Void execute()
                throws
                    Throwable
            {
                MeshObject a = life.createMeshObject( idFact.fromExternalForm( "#a" ), ent3 );
                MeshObject b = life.createMeshObject( idFact.fromExternalForm( "#b" ), ent4 );

                a.relateAndBless( rel3_4.getSource(), b);

                a.setPropertyValue( ent1_prop1, StringValue.create( "Test value" ));
                return null;
            }
        });
    }

    /**
     * Create a test StoreMeshBase.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param meshObjectStore the Store in which to store the MeshObjects
     * @param context the Context in which this MeshBase runs
     * @return the created StoreMeshBase
     */
    protected TestingStoreMeshBase createTestStoreMeshBase(
            MeshBaseIdentifier identifier,
            ModelBase          modelBase,
            IterableStore      meshObjectStore,
            Context            context )
    {
        StoreMeshBaseEntryMapper objectMapper = new StoreMeshBaseEntryMapper();

        StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> objectStorage
                = new StoreMeshBaseSwappingHashMap<>( objectMapper, meshObjectStore );

        MeshObjectIdentifierFactory identifierFactory = DefaultAMeshObjectIdentifierFactory.create();
        AMeshBaseLifecycleManager   life              = new AMeshBaseLifecycleManager() {
                @Override
                public Class<? extends TypedMeshObjectFacadeImpl> getImplementationClass(
                        EntityType theObjectType )
                {
                    return null;
                }
        };

        ImmutableMMeshObjectSetFactory setFactory = ImmutableMMeshObjectSetFactory.create( MeshObject.class, MeshObjectIdentifier.class );

        TestingStoreMeshBase ret = new TestingStoreMeshBase(
                identifier,
                identifierFactory,
                setFactory,
                modelBase,
                life,
                null,
                objectStorage,
                context ) {
        };

        setFactory.setMeshBase( ret );
        objectMapper.setMeshBase( ret );
        ret.initializeHomeObject();

        return ret;
    }

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );

    /**
     * The ModelBase.
     */
    protected MModelBase theModelBase = new TestingModelBase( "Original" );

    /**
     * The Database connection.
     */
    protected MysqlDataSource theDataSource;

    /**
     * The AbstractSqlStore to be tested.
     */
    protected AbstractSqlStore theSqlStore;

    /**
     * Factory for MeshBaseIdentifiers.
     */
    protected MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();

    /**
     * The name of the database that we use to store test data.
     */
    public static final String test_DATABASE_NAME = "test";

    /**
     * The name of the table that we use to store test data.
     */
    public static final String test_TABLE_NAME = "SqlStoreMeshBaseTest";

    /**
     * Our StoreMeshBase testing class.
     */
    public static class TestingStoreMeshBase
            extends
                IterableStoreMeshBase
    {
        public TestingStoreMeshBase(
                MeshBaseIdentifier                                            identifier,
                MeshObjectIdentifierFactory                                   identifierFactory,
                MeshObjectSetFactory                                          setFactory,
                ModelBase                                                     modelBase,
                AMeshBaseLifecycleManager                                     life,
                AccessManager                                                 accessMgr,
                StoreMeshBaseSwappingHashMap<MeshObjectIdentifier,MeshObject> cache,
                Context                                                       context )
        {
            super( identifier, identifierFactory, setFactory, modelBase, life, accessMgr, cache, context );
        }

        @Override
        public MeshObject initializeHomeObject()
        {
            return super.initializeHomeObject();
        }

    }

    /**
     * Name a ModelBase.
     */
    public static class TestingModelBase
            extends
                MModelBase
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        public TestingModelBase(
                String name )
        {
            super(  MMeshTypeLifecycleManager.create(),
                    MMeshTypeIdentifierFactory.create(),
                    MMeshTypeSynonymDictionary.create());
            
            theName = name;
        }

        /**
         * Convert to String, for debugging.
         *
         * @return String
         */
        @Override
        public String toString()
        {
            return super.toString() + ": " + theName;
        }

        /**
         * Name of the ModelBase, for debugging.
         */
        protected String theName;
    }
}
