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

package org.infogrid.meshbase.security.aclbased.test;

import java.text.ParseException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.diet4j.core.ModuleRegistry;
import org.diet4j.core.ModuleRequirement;
import org.diet4j.inclasspath.InClasspathModuleRegistry;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.security.aclbased.accessmanager.AclbasedAccessManager;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
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
 * Factors out common functionality of the various MeshBaseSecurityTests.
 */
public abstract class AbstractAclbasedSecurityTest
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
        ClassLoader    cl       = AbstractAclbasedSecurityTest.class.getClassLoader();
        ModuleRegistry registry = InClasspathModuleRegistry.instantiateOrGet( cl );

        registry.resolve( registry.determineSingleResolutionCandidate( ModuleRequirement.create( "org.infogrid", "org.infogrid.meshbase.security.aclbased" ))).activateRecursively();
        registry.resolve( registry.determineSingleResolutionCandidate( ModuleRequirement.create( "org.infogrid", "org.infogrid.model.Test" ))).activateRecursively();
        
        Log4jLog.configure( "org/infogrid/meshbase/security/aclbased/test/Log.properties", cl );
        Log.setLogFactory( new Log4jLogFactory());
        
        ResourceHelper.setApplicationResourceBundle( ResourceBundle.getBundle(
                "org/infogrid/meshbase/security/aclbased/test/ResourceHelper",
                Locale.getDefault(),
                cl ));

        theModelBase = ModelBaseSingleton.getSingleton();

        SubjectArea aclbasedSa = theModelBase.findSubjectArea( "org.infogrid.meshbase.security.aclbased" );

        rightsTypes = new RelationshipType[] {
                theModelBase.findRelationshipType( aclbasedSa, "MeshObject_HasReadAccessTo_ProtectionDomain" ),
                theModelBase.findRelationshipType( aclbasedSa, "MeshObject_HasUpdateAccessTo_ProtectionDomain" ),
                theModelBase.findRelationshipType( aclbasedSa, "MeshObject_HasDeleteAccessTo_ProtectionDomain" )
        };
    }

    /**
     * Setup.
     * 
     * @throws MeshTypeNotFoundException thrown if a MeshType could not be found
     * @throws ParseException thrown if a URI String was misformed
     */
    @Before
    public void setup()
        throws
            MeshTypeNotFoundException,
            ParseException
    {
        theAccessManager = AclbasedAccessManager.create();
        
        MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();
        
        theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                theAccessManager,
                rootContext );

        life   = theMeshBase.getMeshBaseLifecycleManager();
        idFact = theMeshBase.getMeshObjectIdentifierFactory();
    }

    /**
     * Clean up after the test.
     */
    @After
    public void cleanup()
    {
        theMeshBase.die();
    }

    /**
     * The ModelBase.
     */
    protected static ModelBase theModelBase;

    /**
     * The MeshBase for the test.
     */
    protected MeshBase theMeshBase;

    /**
     * The MeshBaseLifecycleManager that goes with the MeshBase.
     */
    protected MeshBaseLifecycleManager life;

    /**
     * The IdentifierFactory that goes with the MeshBase.
     */
    protected MeshObjectIdentifierFactory idFact;

    /**
     * Our AccessManager for the MeshBase.
     */
    protected AccessManager theAccessManager;
    
    /**
     * All concrete RelationshipTypes that indicate rights in a ProtectionDomain.
     */
    protected static RelationshipType [] rightsTypes;

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );
}
