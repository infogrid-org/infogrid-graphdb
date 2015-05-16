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

package org.infogrid.kernel.test.mesh.externalized;

import java.util.Locale;
import java.util.ResourceBundle;
import org.infogrid.module.inclasspath.InClasspathModuleRegistry;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.logging.log4j.Log4jLog;
import org.infogrid.util.logging.log4j.Log4jLogFactory;
import org.junit.BeforeClass;

/**
 * Factors out common functionality of SerializerTests.
 */
public abstract class AbstractSerializerTest
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
        registry.resolve( registry.getModuleMetaFor( "org.infogrid.kernel" )).activateRecursively();
        
        Log4jLog.configure( "org/infogrid/kernel/test/mesh/externalized/Log.properties", AbstractSerializerTest.class.getClassLoader() );
        Log.setLogFactory( new Log4jLogFactory());
        
        ResourceHelper.setApplicationResourceBundle( ResourceBundle.getBundle(
                "org/infogrid/kernel/test/mesh/externalized/ResourceHelper",
                Locale.getDefault(),
                AbstractSerializerTest.class.getClassLoader() ));
    }
}
