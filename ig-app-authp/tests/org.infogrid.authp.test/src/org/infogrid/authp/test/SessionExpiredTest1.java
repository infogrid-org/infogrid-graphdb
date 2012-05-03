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
import org.infogrid.util.http.IncomingSaneCookie;
import org.infogrid.util.http.IncomingSimpleSaneCookie;

/**
 * Tests what happens if a session is expired.
 */
public class SessionExpiredTest1
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
        log.info( "Submitting valid password to front page" );

        Map<String,String> userPassMap = new HashMap<String,String>();
        userPassMap.put( "lid",            VALID_USER );           // valid user
        userPassMap.put( "lid-credential", VALID_USER + "passj" ); // valid credential
        userPassMap.put( "lid-credtype",   "simple-password" );
        
        HTTP.Response r = HTTP.http_post( theApplicationUrl + "/" , userPassMap, false );

        checkEquals( r.getResponseCode(), "302", "Wrong HTTP response code" );
        checkEquals( r.getLocation(), theApplicationUrl + "/" + VALID_USER, "Wrong redirect location" );
        checkCondition( r.getContentAsString().length() == 0, "Has content" );
        checkEquals( r.getCookies().size(), 3, "LID, session and CSRF cookies should be sent" );
        checkEquals( r.getCookie( constructCookieName( LID_COOKIE,         theApplicationUrl )).getValue(), theApplicationUrl + "/" + VALID_USER, "Wrong LID cookie" );
        checkObject( r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )).getValue(), "Wrong session cookie" );

        //
        
        log.info( "Trying to use session while canceling session" );

        IncomingSaneCookie lidCookie = IncomingSimpleSaneCookie.create(
                constructCookieName( LID_COOKIE, theApplicationUrl ),
                theApplicationUrl + "/" + VALID_USER );
        IncomingSaneCookie sessionCookie = IncomingSimpleSaneCookie.create(
                constructCookieName( LID_SESSION_COOKIE, theApplicationUrl ),
                r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )).getValue());
        
        Map<String,IncomingSaneCookie> cookies = new HashMap<String,IncomingSaneCookie>();
        cookies.put( lidCookie.getName(),     lidCookie );
        cookies.put( sessionCookie.getName(), sessionCookie );

        r = HTTP.http_get( theApplicationUrl + "/" + VALID_USER + "?lid-action=cancel-session", null, false, cookies );
        checkEquals( r.getResponseCode(), "302", "Wrong HTTP response code" );
        checkNotRegex( ".*<script.*",  DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Still Login form displayed" );
        checkNotRegex( ".*user-own.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Still accessing private content" );
        // checkEquals( r.getCookies().size(), 0, "No cookies should be sent" );
                // Going to the front URL creates a jsessionid cookie in Tomcat 5.5, even if cookies="false" is specified in context.xml
                // Tomcat 6 is fine. FIXME?)

        //
        
        log.info( "Trying to use expired session as if nothing had happened" );
        
        r = HTTP.http_get( theApplicationUrl + "/" + VALID_USER, null, false, cookies );
        checkEquals( r.getResponseCode(), "302", "Wrong HTTP response code" );
        checkNotRegex( ".*[Ee]rror.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Error message displayed" );
        checkNotRegex( ".*<script.*",  DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Login form displayed" );
        checkNotRegex( ".*user-own.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Still accessing private content" );
        // checkEquals( r.getCookies().size(), 0, "No cookies should be sent" );
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
        SessionExpiredTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new SessionExpiredTest1( args );
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
    public SessionExpiredTest1(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
