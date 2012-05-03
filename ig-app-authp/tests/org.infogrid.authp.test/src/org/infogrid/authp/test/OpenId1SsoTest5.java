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
 * Tests dumb mode IdP side if there is a session with the IdP.
 */
public class OpenId1SsoTest5
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
        
        //
        
        log.info( "Logging in" );
        
        // ...
        
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
             String [] args )
    {
        OpenId1SsoTest5 test = null;
        try {
            if( args.length != 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }
            test = new OpenId1SsoTest5( args );
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
    public OpenId1SsoTest5(
            String [] args )
        throws
            Exception
    {
        super( args );
    }
}
