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

import java.util.HashMap;
import java.util.Map;
import org.infogrid.util.http.HTTP;

/**
 * Tests that one cannot log into a non-existing account.
 */
public class LoginNoAccountTest1
        extends
            AbstractAuthpTest
{
    /**
     * Run the test.
     *
     * @throws Exception this code may throw any Exception
     */
    public void run()
            throws
                Exception
    {
        log.info( "Submitting invalid user id to front page" );
        
        Map<String,String> userPassMap = new HashMap<String,String>();
        userPassMap.put( "lid",            INVALID_USER );           // invalid user
        userPassMap.put( "lid-credential", INVALID_USER + "pass" );  // valid credential
        userPassMap.put( "lid-credtype",   "simple-password" );
        
        HTTP.Response r = HTTP.http_post( theApplicationUrl + "/" , userPassMap, false );

        checkEquals( r.getResponseCode(), "200", "Wrong HTTP response code" );
        checkRegex( ".*<script.*[Ee]rror.*" + INVALID_USER + ".*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Login form or error message not displayed" );
        // checkCookiesRegexes( r, noCookies, "No cookies should be sent" );
                // Going to the front URL creates a jsessionid cookie in Tomcat 5.5, even if cookies="false" is specified in context.xml
                // Tomcat 6 is fine. FIXME?)
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
             String [] args )
    {
        LoginNoAccountTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new LoginNoAccountTest1( args );
            test.run();

        } catch( Throwable ex ) {
            log.error( ex );
            ++errorCount;
        }
        if( test != null ) {
            test.cleanup();
        }

        if( errorCount == 0 ) {
            log.info( "PASS" );
        } else {
            log.error( "FAIL (" + errorCount + " errors)" );
        }

        System.exit( errorCount );
    }

    /**
     * Setup.
     *
     * @param args not used
     * @throws Exception any kind of exception
     */
    public LoginNoAccountTest1(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
