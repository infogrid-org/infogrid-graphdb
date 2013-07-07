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
 * Tests dumb mode if there is no current session with the IdP.
 */
public class OpenId1SsoTest3
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

        String assoc_handle = "made-up-invalid-handle";

        HTTP.Response r;

        for( String user : new String[] { INVALID_USER, VALID_USER } ) {
            log.info( "checkid-authentication produces false for user " + user + " (no current session)" );
            postPars.clear();
            responses.clear();
        
            StringBuilder url = new StringBuilder();
            url.append( theApplicationUrl ).append( "/" );
            
            postPars.put( "openid.mode", "check_authentication" );
            postPars.put( "openid.identity", theApplicationUrl + "/"  + user );
            postPars.put( "openid.assoc_handle", assoc_handle );
            postPars.put( "openid.return_to", EXAMPLE_SITE_URL + "return_to" );
            postPars.put( "openid.trust_root", EXAMPLE_SITE_URL );
            postPars.put( "openid.signed", "mode,identity,assoc_handle,return_to,trust_root,signed" );
            postPars.put( "openid.sig", "invalid" );

            r = HTTP.http_post( url.toString(), postPars, false );

            log.debug( "HTTP POST returned " + r );
            
            checkEquals( r.getResponseCode(), "200", "Wrong HTTP response code" );
            checkEquals( r.getContentType(),  "text/plain", "Wrong content type" );
            checkNotObject( r.getLocation(),  "Unexpected HTTP redirect location" );
            
            responses = parseKeyValueList( r );
            checkEquals( responses.size(), 2, "Wrong number of response parameters" );
            checkEquals( responses.get( "openid.mode" ), "id_res", "wrong value for openid.mode" );
            checkEquals( responses.get( "is_valid" ),    "false", " wrong value for is_valid" );
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
        OpenId1SsoTest3 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new OpenId1SsoTest3( args );
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
    public OpenId1SsoTest3(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
