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

import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;

/**
 * Factors out common features of tests in this package.
 */
public abstract class AbstractMeshBaseTest
        extends
            AbstractTest
{
    /**
     * Constructor.
     * 
     * @param testClass the class to be tested
     */
    public AbstractMeshBaseTest(
            Class<? extends AbstractTest> testClass )
    {
        super( localFileName( testClass, "/ResourceHelper" ));
    }
    
    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );

    /**
     * The ModelBase to use.
     */
    protected ModelBase theModelBase = ModelBaseSingleton.getSingleton();
    
    /**
     * The MeshBaseIdentifierFactory to use.
     */
    protected MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();
}
