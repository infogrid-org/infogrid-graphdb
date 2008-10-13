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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.jee.net.TEST;

import java.io.File;
import java.net.URL;
import org.infogrid.module.ModuleRequirement;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.testharness.AbstractTestGroup;
import org.infogrid.testharness.AbstractTestGroup.TestSpec;
import org.infogrid.testharness.tomcat.TomcatProxy;
import org.infogrid.testharness.tomcat.TomcatProxyConfiguredByProperties;

/**
 * Runs all tests in this package.
 */
public class AllTests
        extends
            AbstractTestGroup
{
    /**
     * Main program.
     *
     * @param args command-line arguments
     * @throws Exception all kinds of things may happen in tests
     */
    public static void main(
            String [] args )
        throws
            Exception
    {
        ModuleRequirement toTest = ModuleRequirement.create1( "org.infogrid.jee.net.TESTAPP" );
        TomcatProxy       tomcat = TomcatProxyConfiguredByProperties.create(
                new File( AbstractTest.fileSystemFileName( AllTests.class, "tomcat.properties" ))) ;

        URL appUrl = tomcat.deployModule( toTest );

        TestSpec [] tests = {
                new TestSpec( AllMeshObjectsTest1.class, appUrl.toExternalForm() )
        };

        runTests( tests );
    }
}
