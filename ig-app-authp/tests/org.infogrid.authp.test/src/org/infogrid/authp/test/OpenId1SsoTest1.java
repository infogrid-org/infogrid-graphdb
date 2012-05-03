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
 * Tests that we don't accept invalid associations.
 */
public class OpenId1SsoTest1
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

        String assoc_handle = "made-up-invalid-handle";
        
        
        for( String user : new String[] { INVALID_USER, VALID_USER } ) {
            log.info( "checkid-immediate needs to produce 500 for user " + user + " (invalid association)" );

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

            checkEquals( r.getResponseCode(), "500", "Wrong HTTP response code" );
        }

        //
        
        for( String user : new String[] { INVALID_USER, VALID_USER } ) {
            log.info( "checkid-setup needs to produce 500 for user " + user + " (invalid association)" );
        
            StringBuilder url = new StringBuilder();
            url.append( theApplicationUrl ).append( "/" );
            url.append( "?" ).append( "openid.mode" ).append( "=" ).append( "checkid_setup" );
            url.append( "&" ).append( "openid.identity" ).append( "=" ).append( theApplicationUrl + "/"  + user );
            url.append( "&" ).append( "openid.assoc_handle" ).append( "=" ).append( assoc_handle );
            url.append( "&" ).append( "openid.return_to" ).append( "=" ).append( EXAMPLE_SITE_URL + "return_to" );
            url.append( "&" ).append( "openid.trust_root" ).append( "=" ).append( EXAMPLE_SITE_URL );

            r = HTTP.http_get( url.toString(), false );

            checkEquals( r.getResponseCode(), "500", "Wrong HTTP response code" );
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
        OpenId1SsoTest1 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new OpenId1SsoTest1( args );
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
    public OpenId1SsoTest1(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
