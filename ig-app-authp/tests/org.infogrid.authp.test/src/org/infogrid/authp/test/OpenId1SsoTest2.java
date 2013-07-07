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
 * Tests OpenID V1 SSO on the IdP side if there is no current user session with the IdP.
 */
public class OpenId1SsoTest2
        extends
            AbstractOpenId1SsoTest
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
        String EXAMPLE_SITE_URL = "http://example.com/";
        
        Map<String,String> postPars  = new HashMap<String,String>();
        Map<String,String> responses = new HashMap<String,String>();

        HTTP.Response r;

        MyAssociation assoc = super.establishSmartAssociation();
        String assoc_handle = assoc.getHandle();

        for( String user : new String[] { INVALID_USER, VALID_USER } ) {
            log.info( "checkid-immediate produces false for user " + user + " (no current session)" );

            postPars.clear();
            responses.clear();

            StringBuilder url = new StringBuilder();
            url.append( theApplicationUrl ).append( "/" );
            url.append( "?" ).append( "openid.mode" ).append( "=" ).append( "checkid_immediate" );
            url.append( "&" ).append( "openid.identity" ).append( "=" ).append( theApplicationUrl + "/"  + user );
            url.append( "&" ).append( "openid.assoc_handle" ).append( "=" ).append( assoc_handle );
            url.append( "&" ).append( "openid.return_to" ).append( "=" ).append( EXAMPLE_SITE_URL + "return_to" );
            url.append( "&" ).append( "openid.trust_root" ).append( "=" ).append( EXAMPLE_SITE_URL );

            r = HTTP.http_get( url.toString(), false );

            checkRegex( "30[26]", r.getResponseCode(), "Wrong HTTP response code" );
            checkObject( r.getLocation(),              "No HTTP redirect location" );

            responses = parseUrlArgs( r.getLocation() );
            checkEquals( responses.get( "openid.mode" ), "cancel", "Wrong mode" );
            checkObject( responses.get( "openid.user_setup_url"), "openid.user_setup_url not returned on false assertion" );
            checkEquals( responses.size(), 2, "Wrong number of response parameters returned" );
        }

        //
        
        for( String user : new String[] { INVALID_USER, VALID_USER } ) {
            log.info( "checkid-setup produces false for user " + user + " (no current session)" );
        
            StringBuilder url = new StringBuilder();
            url.append( theApplicationUrl ).append( "/" ).append( user );
            url.append( "?" ).append( "openid.mode" ).append( "=" ).append( "checkid_setup" );
            url.append( "&" ).append( "openid.identity" ).append( "=" ).append( theApplicationUrl + "/"  + user );
            url.append( "&" ).append( "openid.assoc_handle" ).append( "=" ).append( assoc_handle );
            url.append( "&" ).append( "openid.return_to" ).append( "=" ).append( EXAMPLE_SITE_URL + "return_to" );
            url.append( "&" ).append( "openid.trust_root" ).append( "=" ).append( EXAMPLE_SITE_URL );

            r = HTTP.http_get( url.toString(), false );

            checkEquals( r.getResponseCode(), "200", "Wrong HTTP response code" );
            checkNotObject( r.getLocation(),         "Unexpected HTTP redirect location" );
            checkRegex(
                    ".*<script.*",
                    DEFAULT_HTTP_REGEX_FLAGS,
                    r.getContentAsString(),
                    "Login form not displayed" );
        }
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
             String [] args )
    {
        OpenId1SsoTest2 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new OpenId1SsoTest2( args );
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
    public OpenId1SsoTest2(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
