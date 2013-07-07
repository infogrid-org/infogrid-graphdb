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
 * Tests logging in and out, and maintenance of a session.
 */
public class LoginLogoutSessionTest1
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
        userPassMap.put( "lid-credential", VALID_USER + "passj" ); // valid credential, needs j at the end
        userPassMap.put( "lid-credtype",   "simple-password" );
        
        HTTP.Response r = HTTP.http_post( theApplicationUrl + "/" , userPassMap, false );

        checkEquals( r.getResponseCode(), "302", "Wrong HTTP response code" );
        checkEquals( r.getLocation(), theApplicationUrl + "/" + VALID_USER, "Wrong redirect location" );
        checkCondition( r.getContentAsString().length() == 0, "Has content" );
        checkEquals( r.getCookies().size(), 3, "LID, session and CSRF cookies should be sent" );
        checkEquals( r.getCookie( constructCookieName( LID_COOKIE, theApplicationUrl )).getValue(), theApplicationUrl + "/" + VALID_USER, "Wrong LID cookie" );
        checkObject( r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )).getValue(), "Wrong session cookie" );

        //
        
        log.info( "Accessing private info" );
        
        IncomingSaneCookie lidCookie = IncomingSimpleSaneCookie.create(
                constructCookieName( LID_COOKIE, theApplicationUrl ),
                theApplicationUrl + "/" + VALID_USER );
        IncomingSaneCookie sessionCookie = IncomingSimpleSaneCookie.create(
                constructCookieName( LID_SESSION_COOKIE, theApplicationUrl ),
                r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )).getValue());
        
        Map<String,IncomingSaneCookie> cookies = new HashMap<String,IncomingSaneCookie>();
        cookies.put( lidCookie.getName(),     lidCookie );
        cookies.put( sessionCookie.getName(), sessionCookie );

        log.debug( "LidCookie",     lidCookie );
        log.debug( "SessionCookie", sessionCookie );

        r = HTTP.http_get( theApplicationUrl + "/" + VALID_USER, null, false, cookies );
        checkEquals( r.getResponseCode(), "200", "Wrong HTTP response code" );
        checkNotRegex( ".*[Ee]rror.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Error message displayed" );
        checkNotRegex( ".*<script.*",  DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Login form displayed" );
        checkRegex(    ".*user-own.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Cannot access private content" );
        // checkEquals( r.getCookies().size(), 0, "No cookies should be sent" );
                // Going to the front URL creates a jsessionid cookie in Tomcat 5.5, even if cookies="false" is specified in context.xml
                // Tomcat 6 is fine. FIXME?)
        checkNotObject( r.getCookie( constructCookieName( LID_COOKIE,         theApplicationUrl )), "No LID cookie should be set here" );
        checkNotObject( r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )), "No session cookie should be set here" );

        //
        
        log.info( "Logging out" );
        
        r = HTTP.http_get( theApplicationUrl + "/?lid=", null, false, cookies );
        checkEquals( r.getResponseCode(), "200", "Wrong HTTP response code" );
        checkRegex( ".*<script.*logged out.*", DEFAULT_HTTP_REGEX_FLAGS, r.getContentAsString(), "Login form or logout message not displayed" );
        // checkEquals( r.getCookies().size(), 2, "LID and session cookies should be sent" );
                // Going to the front URL creates a jsessionid cookie in Tomcat 5.5, even if cookies="false" is specified in context.xml
                // Tomcat 6 is fine. FIXME?)
        checkCondition( r.getCookie( constructCookieName( LID_COOKIE,         theApplicationUrl )).getIsRemovedOrExpired(), "Wrong LID cookie" );
        checkCondition( r.getCookie( constructCookieName( LID_SESSION_COOKIE, theApplicationUrl )).getIsRemovedOrExpired(), "Wrong session cookie" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
             String [] args )
    {
        LoginLogoutSessionTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new LoginLogoutSessionTest1( args );
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
    public LoginLogoutSessionTest1(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
