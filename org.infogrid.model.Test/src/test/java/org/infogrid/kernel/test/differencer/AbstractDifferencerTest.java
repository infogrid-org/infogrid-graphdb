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

package org.infogrid.kernel.test.differencer;

import java.util.Locale;
import java.util.ResourceBundle;
import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.ChangeSet;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.module.inclasspath.InClasspathModuleRegistry;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;
import org.infogrid.util.logging.log4j.Log4jLog;
import org.infogrid.util.logging.log4j.Log4jLogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Factors out common functionality for DifferencerTests.
 */
public abstract class AbstractDifferencerTest
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
        InClasspathModuleRegistry registry = InClasspathModuleRegistry.getSingleton();
        registry.resolve( registry.getModuleMetaFor( "org.infogrid.model.Test" )).activateRecursively();

        Log4jLog.configure( "org/infogrid/kernel/test/differencer/Log.properties", AbstractDifferencerTest.class.getClassLoader() );
        Log.setLogFactory( new Log4jLogFactory());
        
        ResourceHelper.setApplicationResourceBundle( ResourceBundle.getBundle(
                "org/infogrid/kernel/test/differencer/ResourceHelper",
                Locale.getDefault(),
                AbstractDifferencerTest.class.getClassLoader() ));

        theModelBase = ModelBaseSingleton.getSingleton();

        SubjectArea sa = theModelBase.findSubjectArea( "org.infogrid.model.Test", null );
        
        EntityType typeA = theModelBase.findEntityType( sa, "A" );
        typeAA   = theModelBase.findEntityType(       sa,     "AA" );
        typeB    = theModelBase.findEntityType(       sa,     "B" );
        typeC    = theModelBase.findEntityType(       sa,     "C" );
        typeR    = theModelBase.findRelationshipType( sa,     "R" );
        typeRR   = theModelBase.findRelationshipType( sa,     "RR" );
        typeAR1A = theModelBase.findRelationshipType( sa,     "AR1A" );
        typeX    = theModelBase.findPropertyType(     typeA,  "X" );
        typeY    = theModelBase.findPropertyType(     typeAA, "Y" );
        typeZ    = theModelBase.findPropertyType(     typeB,  "Z" );
        typeU    = theModelBase.findPropertyType(     typeB,  "U" );
    }

    /**
     * Set up.
     * 
     * @throws Exception all sorts of things may go wrong in a test
     */
    @Before
    public void setup()
        throws
            Exception
    {
        theMeshBase1 = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase1" ),
                ModelBaseSingleton.getSingleton(),
                null,
                rootContext );
        theMeshBase2 = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase2" ),
                ModelBaseSingleton.getSingleton(),
                null,
                rootContext );
    }
    
    /**
     * Helper method to create a MeshObject.
     * 
     * @param life the MeshBaseLifecycleManager to use
     * @param identifier the identifier for the MeshObject,
     * @param type the EntitType for the MeshObject
     * @param now the creation time
     * @return the created MeshObject
     * @throws IsAbstractException thrown if the EntityType is abstract
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     * @throws MeshObjectIdentifierNotUniqueException thrown if a MeshObject with this identifier existed already
     * @throws NotPermittedException thrown if the caller did not have the required permissions
     */
    protected MeshObject createMeshObject(
            MeshBaseLifecycleManager   life,
            MeshObjectIdentifier       identifier,
            EntityType                 type,
            long                       now )
        throws
            IsAbstractException,
            TransactionException,
            MeshObjectIdentifierNotUniqueException,
            NotPermittedException
    {
        MeshObject ret = life.createMeshObject( identifier, type, now, now, now, -1L );
        return ret;
    }

    /**
     * Helper method to create a MeshObject.
     * 
     * @param life the MeshBaseLifecycleManager to use
     * @param identifier the identifier for the MeshObject,
     * @param now the creation time
     * @return the created MeshObject
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     * @throws MeshObjectIdentifierNotUniqueException thrown if a MeshObject with this identifier existed already
     * @throws NotPermittedException thrown if the caller did not have the required permissions
     */
    protected MeshObject createMeshObject(
            MeshBaseLifecycleManager life,
            MeshObjectIdentifier          identifier,
            long                     now )
        throws
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = life.createMeshObject( identifier, now, now, now, -1L );
        return ret;
    }

    /**
     * Helper to print the contents of a MeshBase.
     * 
     * @param useThis the Log to print to
     * @param theMeshBase the MeshBase to print
     */
    protected void printMeshBase(
            Log              useThis,
            IterableMeshBase theMeshBase )
    {
        if( useThis.isDebugEnabled() ) {
            useThis.debug( "MeshBase content: " + theMeshBase.size() + " MeshObjects" );

            int i=0;
            for( MeshObject current : theMeshBase ) {
                useThis.debug( " " + i + ": " + current );
                ++i;
            }
        }
    }

    /**
     * Helper to print a ChangeSet.
     * 
     * @param useThis the Log to print to
     * @param theChangeSet the ChangeSet to print
     */
    protected void printChangeSet(
            Log       useThis,
            ChangeSet theChangeSet )
    {
        if( useThis.isDebugEnabled() ) {
            Change [] theChanges = theChangeSet.getChanges();

            useThis.debug( "found " + theChanges.length + " changes" );

            for( int i=0 ; i<theChanges.length ; ++i ) {
                Change current = theChanges[i];
                useThis.debug( " " + i + ": " + current );
            }
        }
    }

    /**
     * Clean up after the test.
     */
    @After
    public void cleanup()
    {
        theMeshBase2.die();
        theMeshBase1.die();
    }

    /**
     * The first MeshBase for the test.
     */
    protected IterableMeshBase theMeshBase1;

    /**
     * The second MeshBase for the test.
     */
    protected IterableMeshBase theMeshBase2;

    /**
     * The ModelBase.
     */
    protected static ModelBase theModelBase;

    /**
     * Buffered MeshTypes.
     */
    protected static EntityType       typeAA;
    protected static EntityType       typeB;
    protected static EntityType       typeC;
    protected static RelationshipType typeR;
    protected static RelationshipType typeRR;
    protected static RelationshipType typeAR1A;
    protected static PropertyType     typeX;
    protected static PropertyType     typeY;
    protected static PropertyType     typeZ;
    protected static PropertyType     typeU;

    /**
     * Factory for MeshBaseIdentifiers.
     */
    protected MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );
}
