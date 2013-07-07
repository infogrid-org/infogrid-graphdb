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

import org.infogrid.util.http.HTTP;

/**
 * Tests anonymous browse access to the application.
 */
public class AnonymousBrowseTest1
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
        String [][] noCookies         = {};
        String [][] safeUnsafeCookies = { { "org-infogrid-jee-security-safeunsafepostfilter-cookie", ".*"  } };
        
        log.info( "Testing general site setup" );

        checkHttpGet( "/",                                   "text/html",       ".*<script.*Front\\.jsp.*",     "200", safeUnsafeCookies, "Wrong anonymous front page" );
                // Going to the front URL creates a jsessionid cookie in Tomcat 5.5, even if cookies="false" is specified in context.xml
                // Tomcat 6 is fine. FIXME?)
        checkHttpGet( "/s/templates/authp/text/test.css",    "text/css",        ".*^table\\s+\\{.*",            "200", safeUnsafeCookies, "Wrong anonymous site CSS" );
        checkHttpGet( "/w/user-pass.js",                     "text/javascript", ".*document\\.writeln.*",       "200", safeUnsafeCookies, "Wrong anonymous widget JS" );
        checkHttpGet( "/w/user-pass.css",                    "text/css",        ".*div\\.org-infogrid-authp.*", "200", safeUnsafeCookies, "Wrong anonymous widget CSS" );
        
        //
        
        log.info( "Testing user front pages" );
        
        checkHttpGet( "/" + VALID_USER,   null, null, "30[0-9]", safeUnsafeCookies, "Wrong anonymous valid user page" );
        checkHttpGet( "/" + INVALID_USER, null, null, "30[0-9]", safeUnsafeCookies, "Wrong anonymous invalid user page" );
                // Sun, in its wisdom, does not let us look at the returned response when http status=404
        //
        
        log.info( "Testing Yadis files" );

        checkHttpGet( "/" + VALID_USER   + "?lid-meta=capabilities", "application/xrds\\+xml", ".*<XRDS.*<xrd:XRD.*<xrd:Service.*",  "200", noCookies, "Wrong anonymous valid user XRDS" );
        checkHttpGet( "/" + INVALID_USER + "?lid-meta=capabilities", null,                     null,                                 "404", safeUnsafeCookies, "Wrong anonymous invalid user XRDS" );

        //
        
        log.info( "Testing Yadis discovery (via Accept header)" );
        
        HTTP.Response r;

        r = HTTP.http_get( theApplicationUrl + "/" + VALID_USER, HTTP_GET_ACCEPT_HEADER );
        checkRegex(
                "200",
                r.getResponseCode(),
                "Wrong Accept-based XRDS response code for anonymous valid user" );
        checkRegex(
                "application/xrds\\+xml",
                r.getContentType(),
                "Wrong Accept-based XRDS content type for anonymous valid user" );
        checkRegex(
                ".*<XRDS.*<xrd:XRD.*<xrd:Service.*",
                DEFAULT_HTTP_REGEX_FLAGS,
                r.getContentAsString(),
                "Wrong Accept-based XRDS content for anonymous valid user" );

        r = HTTP.http_get( theApplicationUrl + "/" + INVALID_USER, HTTP_GET_ACCEPT_HEADER );
        checkRegex(
                "404",
                r.getResponseCode(),
                "Wrong Accept-based XRDS response code for anonymous invalid user" );
        
        //
        
        log.info( "Testing Yadis discovery (via X-XRDS header)" );

        r = HTTP.http_get( theApplicationUrl + "/" + VALID_USER, false );
        checkRegex(
                "http://[^\\?]+\\?lid-meta=capabilities",
                r.getSingleHttpHeaderField( "X-XRDS-Location"),
                "Wrong X-XRDS-Location header for anonymous valid user" );
        // we don't test HTTP EQUIV because we only need to do one
                
        r = HTTP.http_get( theApplicationUrl + "/" + INVALID_USER, false );
        checkEquals( r.getHttpHeaderField( "X-XRDS-Location" ), null, "Wrong X-XRDS-Location for anonymous invalid user" );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
             String [] args )
    {
        AnonymousBrowseTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new AnonymousBrowseTest1( args );
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
    public AnonymousBrowseTest1(
            String [] args )
        throws
            Exception
    {
        super( args );
    }

    /**
     * This is the default accept header for HTTP requests.
     */
    public static final String HTTP_GET_ACCEPT_HEADER = "application/xrds+xml,text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5";
}
