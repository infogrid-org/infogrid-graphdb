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

package org.infogrid.kernel.test.modelbase;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.modelbase.SubjectAreaNotFoundException;
import org.infogrid.modelbase.externalized.xml.XmlModelExporter;
import org.infogrid.module.inclasspath.InClasspathModuleRegistry;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.logging.log4j.Log4jLog;
import org.infogrid.util.logging.log4j.Log4jLogFactory;
import org.junit.After;
import org.junit.BeforeClass;

/**
 * Factors out common behaviors of ModelBaseTests.
 */
public abstract class AbstractModelBaseTest
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

        Log4jLog.configure( "org/infogrid/kernel/test/modelbase/Log.properties", AbstractModelBaseTest.class.getClassLoader() );
        Log.setLogFactory( new Log4jLogFactory());
        
        ResourceHelper.setApplicationResourceBundle( ResourceBundle.getBundle(
                "org/infogrid/kernel/test/modelbase/ResourceHelper",
                Locale.getDefault(),
                AbstractModelBaseTest.class.getClassLoader() ));
    }

    /**
     * Populate the ModelBase.
     */
    protected void populateModelBase()
        throws
            SubjectAreaNotFoundException,
            IOException
    {
        String [] theSubjectAreas = { "org.infogrid.model.Test" };
        String [] theSubjectAreaVersions = { null };

        for( int i=0 ; i<theSubjectAreas.length ; ++i ) {
            theModelBase.findSubjectArea( theSubjectAreas[i], theSubjectAreaVersions[i] );
        }

        //

        if( getLog().isDebugEnabled() ) {
            XmlModelExporter theExporter = new XmlModelExporter();
            theExporter.exportToXML( theModelBase, System.err );
        }
    }

    /**
     * Clean up after the test.
     */
    @After
    public void cleanup()
    {
        theModelBase.die();
    }

    /**
     * The ModelBase to test.
     */
    protected ModelBase theModelBase = ModelBaseSingleton.getSingleton();
}
