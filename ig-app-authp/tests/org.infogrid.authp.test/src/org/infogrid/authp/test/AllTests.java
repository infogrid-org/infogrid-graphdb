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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.authp.test;

import java.io.File;
import java.net.URL;
import org.infogrid.module.ModuleRequirement;
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
        if( args.length != 1 ) {
            throw new IllegalArgumentException( "Must specify Tomcat properties file name on command-line" );
        }
        String tomcatPropertiesFile = args[0];

        ModuleRequirement toTest = ModuleRequirement.create1( "org.infogrid.authp.testapp" );
        TomcatProxy       tomcat = TomcatProxyConfiguredByProperties.create( new File( tomcatPropertiesFile )) ;

        URL appUrl = tomcat.deployModule( toTest );

        TestSpec [] tests = {
                new TestSpec( AnonymousBrowseTest1.class,    appUrl.toExternalForm() ),
                new TestSpec( LoginNoAccountTest1.class,     appUrl.toExternalForm() ),
                new TestSpec( LoginWrongPasswordTest1.class, appUrl.toExternalForm() ),
                new TestSpec( LoginLogoutSessionTest1.class, appUrl.toExternalForm() ),
                new TestSpec( SessionExpiredTest1.class,     appUrl.toExternalForm() ),
                new TestSpec( OpenId1SsoTest1.class,         appUrl.toExternalForm() ), // invalid associations are not accepted
                new TestSpec( OpenId1SsoTest2.class,         appUrl.toExternalForm() ),
                new TestSpec( OpenId1SsoTest3.class,         appUrl.toExternalForm() ),
//                new TestSpec( OpenId1SsoTest4.class,         appUrl.toExternalForm() ),
//                new TestSpec( OpenId1SsoTest5.class,         appUrl.toExternalForm() ),

                // These tests still need to be written:
                // new TestSpec( OpenId2SsoTest1.class,         appUrl.toExternalForm() ),

                // new TestSpec( LidGpgSsoTest1.class,          appUrl.toExternalForm() ), // FIXME
        };

        runTests( tests );
    }
}

