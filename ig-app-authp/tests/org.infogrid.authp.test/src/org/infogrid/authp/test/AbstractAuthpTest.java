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
import org.infogrid.testharness.tomcat.AbstractTomcatTest;
import org.infogrid.util.http.HTTP;
import org.infogrid.util.logging.Log;

/**
 * Common functionality for tests that involve accessing an application running on Tomcat.
 */
public abstract class AbstractAuthpTest
        extends
            AbstractTomcatTest
{
    /**
     * Constructor for subclasses only, use factory method.
     * 
     * @param args not used
     * @throws Exception tests may all kinds of Exceptions
     */
    protected AbstractAuthpTest(
            String [] args )
        throws
            Exception
    {
        super( args[0], "org.infogrid.authp.test.ResourceHelper" );

        log = Log.getLogInstance( getClass() );
    }

    /**
     * Parse an OpenID response into a name-value list.
     * 
     * @param r the received HTTP response
     * @return the name-value list as a Map
     */
    protected Map<String,String> parseKeyValueList(
            HTTP.Response r )
    {
        HashMap<String,String> ret = new HashMap<String,String>();
        
        String [] lines = r.getContentAsString().split( "\n" );
        for( String current : lines ) {
            int colon = current.indexOf( ':' );
            if( colon <=0 || colon == current.length()-1 ) {
                log.error( "Cannot find colon in middle of " + current );
            }
            String key   = current.substring(  0, colon );
            String value = current.substring( colon+1 );
            
            ret.put( key, value );
        }
        return ret;
    }

    /**
     * Parse out the arguments from a URL.
     * 
     * @param url the URL
     * @return the name-value list as a Map
     */
    protected Map<String,String> parseUrlArgs(
            String url )
    {
        HashMap<String,String> ret = new HashMap<String,String>();

        int question = url.indexOf( '?' );
        if( question > 0 ) {
            String [] pairs = url.substring( question+1 ).split( "&" );
            for( String current : pairs ) {
                int equals = current.indexOf( "=" );
                String name;
                String value;
                if( equals >= 0 ) {
                    name = current.substring( 0, equals );
                    value = current.substring( equals+1 );
                    
                    ret.put( HTTP.decodeUrlArgument( name ), HTTP.decodeUrlArgument( value ));
                }
            }
        }
        
        return ret;
    }

    /**
     * Construct the real-aware name of a cookie.
     *
     * @param name name of the cookie
     * @param realm realm
     * @return real name
     */
    protected String constructCookieName(
            String name,
            String realm )
    {
        return name;
    }

    // Our Logger
    protected static Log log;

    /**
     * User id of a known valid user.
     */
    public static final String VALID_USER = "JValidUser";
    
    /**
     * User if of a non-existing user.
     */
    public static final String INVALID_USER = "InvalidUser";
    
    /**
     * Name of the login form's user name field.
     */
    public static final String USER_NAME_FIELD = "lid";
    
    /**
     * Name of the login form's credential field.
     */
    public static final String CREDENTIAL_FIELD = "lid-credential";
    
    /**
     * Name of the LID identifier cookie.
     */
    public static final String LID_COOKIE = "org-netmesh-lid-lid";
    
    /**
     * Name of the LID session cookie.
     */
    public static final String LID_SESSION_COOKIE = "org-netmesh-lid-session";
}
 
