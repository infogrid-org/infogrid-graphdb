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

package org.infogrid.kernel.active.test.traversalpathset;

import java.text.ParseException;
import org.infogrid.kernel.active.test.AllTests;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.mesh.set.active.m.ActiveMMeshObjectSetFactory;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;

/**
 * Factors out functionality common to ActiveTraversalPathSetTests.
 */
public abstract class AbstractActiveTraversalPathSetTest
        extends
            AbstractTest
{
    /**
     * Constructor.
     * 
     * @param testClass the Class containing the test
     * @throws MeshTypeNotFoundException a MeshType could not be found
     * @throws ParseException a MeshBaseIdentifier could not be created
     */
    protected AbstractActiveTraversalPathSetTest(
            Class<?> testClass )
        throws
            MeshTypeNotFoundException,
            ParseException
    {
        super( localFileName( AllTests.class, "/ResourceHelper" ) );

        typeAA   = theModelBase.findEntityType(       "org.infogrid.model.Test", null, "AA" );
        typeAR1A = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "AR1A" );
        typeAR2A = theModelBase.findRelationshipType( "org.infogrid.model.Test", null, "AR2A" );
        typeX    = theModelBase.findPropertyType( typeAA, "X" );
        
        MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();
        
        theMeshBase = MMeshBase.create( theMeshBaseIdentifierFactory.fromExternalForm( "testMeshBase" ), theModelBase, null, rootContext );

        theMeshObjectSetFactory = ActiveMMeshObjectSetFactory.create( MeshObject.class, MeshObjectIdentifier.class );
        theMeshObjectSetFactory.setMeshBase( theMeshBase );
    }

    /**
     * Create a MeshObject.
     * 
     * @param life the MeshBaseLifecycleManager to use
     * @param type the type with which to bless the MeshObject
     * @param identifier the MeshObjectIdentifier for the MeshObject
     * @return the created MeshObject
     * @throws Exception all sorts of things may go wrong in tests
     */
    protected MeshObject createMeshObject(
            MeshBaseLifecycleManager   life,
            EntityType                 type,
            MeshObjectIdentifier       identifier )
        throws
            Exception
    {
        MeshObject ret = life.createMeshObject( identifier );
        ret.bless( type );
        return ret;
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        theMeshBase = null;
    }

    /**
     * Check whether an (unordered) set of MeshObjects contains exactly the set of
     * MeshObjects whose properties (specified as second parameter) contains the
     * values that are given by the
     * third parameter. This is provided to make certain tests easy where we look
     * for the existence of certain objects in another set.
     *
     * @param set the set of MeshObject in which we look
     * @param propertyType the PropertyType in which we look
     * @param values the set of values for which we look in the set
     * @param msg a message to print to report an error
     * @return true if the test passed
     * @throws Exception all sorts of things may go wrong in tests
     */
    protected final boolean checkTraversalPathSet(
            TraversalPathSet set,
            PropertyType     propertyType,
            String     [][]  values,
            String           msg )
        throws
            Exception
    {
        // first make sure all MeshObjects are still alive
        for( TraversalPath currentPath : set.getTraversalPaths() ) {
            TraversalPath currentPath2 = currentPath;
            if( currentPath2.getFirstMeshObject().getIsDead() ) {
                reportError( "MeshObject is dead", currentPath2.getFirstMeshObject() );
            }
            currentPath2 = currentPath2.getNextSegment();
        }
        
        if( set.size() != values.length ) {            
            reportError( msg, set, values );
            return false;
        }
        return true;
    }

    /**
     * Dump the content of a TraversalPathSet to log.traceMethodCallEntry().
     *
     * @param set the TraversalPathSet whose content we want to dump
     * @param prefix a string to prepend
     * @param type indicates the property whose value shall be dumped
     * @param mylog the Log to dump to
     * @throws Exception all sorts of things may go wrong in tests
     */
    protected final void dumpTraversalPathSet(
            TraversalPathSet set,
            String           prefix,
            PropertyType     type,
            Log              mylog )
        throws
            Exception
    {
        if( mylog.isDebugEnabled() ) {
            StringBuilder buf = new StringBuilder( prefix );
            for( TraversalPath current : set ) {
                buf.append( "\n  " );
                TraversalPath p = current;
                do {
                    buf.append( " / " );
                    buf.append( p.getFirstMeshObject().getPropertyValue( type ) );
                    p = p.getNextSegment();
                } while( p != null );
            }
            mylog.debug( buf.toString() );
        }
    }
        
    /**
     * The ModelBase.
     */
    protected ModelBase theModelBase = ModelBaseSingleton.getSingleton();

    /**
     * Cached MeshType.
     */
    protected EntityType typeAA;

    /**
     * Cached MeshType.
     */
    protected RelationshipType typeAR1A;

    /**
     * Cached MeshType.
     */
    protected RelationshipType typeAR2A;

    /**
     * Cached MeshType.
     */
    protected PropertyType typeX;

    /**
     * The test MeshBase.
     */
    protected MeshBase theMeshBase;

    /**
     * The test ActiveMeshObjectSetFactory.
     */
    protected ActiveMMeshObjectSetFactory theMeshObjectSetFactory;

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );
}
