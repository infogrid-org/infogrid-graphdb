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
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.security.aclbased.AclbasedSubjectArea;
import org.infogrid.meshbase.security.aclbased.accessmanager.AclbasedAccessManager;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;

/**
 * Factors out common functionality of the various MeshBaseSecurityTests.
 */
public abstract class AbstractAclbasedSecurityTest
        extends
            AbstractTest
{
    /**
     * Constructor.
     * 
     * @param testClass the class to be tested
     * @throws MeshTypeNotFoundException thrown if a MeshType could not be found
     * @throws ParseException thrown if a URI String was misformed
     */
    public AbstractAclbasedSecurityTest(
            Class testClass )
        throws
            MeshTypeNotFoundException,
            ParseException
    {
        super( localFileName( testClass, "/ResourceHelper" ));

        theAccessManager = AclbasedAccessManager.create();
        
        MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();
        
        theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                theAccessManager,
                rootContext );

        life   = theMeshBase.getMeshBaseLifecycleManager();
        idFact = theMeshBase.getMeshObjectIdentifierFactory();
        
        rightsTypes = new RelationshipType[] {
                AclbasedSubjectArea.MESHOBJECT_HASREADACCESSTO_PROTECTIONDOMAIN,
                AclbasedSubjectArea.MESHOBJECT_HASUPDATEACCESSTO_PROTECTIONDOMAIN,
                AclbasedSubjectArea.MESHOBJECT_HASDELETEACCESSTO_PROTECTIONDOMAIN
        };
    }
    
    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        theMeshBase.die();
    }

    /**
     * The ModelBase.
     */
    protected ModelBase theModelBase = ModelBaseSingleton.getSingleton();

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
    protected RelationshipType [] rightsTypes;

    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );
}
