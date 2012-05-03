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

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.infogrid.crypto.diffiehellman.DiffieHellmanEndpoint;
import org.infogrid.lid.openid.CryptUtils;
import org.infogrid.util.Base64;
import org.infogrid.util.http.HTTP;

/**
 * Factors out common behaviors of OpenId1Tests.
 */
public abstract class AbstractOpenId1SsoTest
        extends
            AbstractAuthpTest
{
    /**
     * Establish a smart association.
     *
     * @return the established association
     * @throws Exception this code may throw any Exception
     */
    protected MyAssociation establishSmartAssociation()
            throws
                Exception
    {
        Map<String,String> postPars  = new HashMap<String,String>();
        Map<String,String> responses = new HashMap<String,String>();
        HTTP.Response r;
        
        DiffieHellmanEndpoint dhLocal = DiffieHellmanEndpoint.create( DEFAULT_P, DEFAULT_G );
        
        //

        log.info( "Negotiating DH secret" );
        
        postPars.clear();
        responses.clear();
        postPars.put( "openid.mode", "associate" );
        // openid.assoc_type -> default (HMAC-SHA1)
        postPars.put( "openid.session_type", "DH-SHA1" );
        // openid.dh_modulus -> default (long number)
        // openid.dh_gen -> default (2)
        postPars.put( "openid.dh_consumer_public", Base64.base64encode( dhLocal.getPublicKey().toByteArray() ));
        
        r = HTTP.http_post( theApplicationUrl + "/" , postPars, false );
        
        checkEquals( r.getResponseCode(), "200",        "Wrong HTTP response code" );
        checkEquals( r.getContentType(),  "text/plain", "Wrong HTTP response MIME type" );
        responses = parseKeyValueList( r );
        
        checkEquals( responses.get( "assoc_type" ),   "HMAC-SHA1", "Wrong returned assoc_type" );
        checkObject( responses.get( "assoc_handle" ), "No returned assoc_handle" );
        checkObject( responses.get( "expires_in" ),   "No returned expires_in" );
        checkRegex( "[0-9]+", responses.get( "expires_in" ), "Wrong returned format of expires_in" );
        checkEquals( responses.get( "session_type" ), "DH-SHA1", "Unexpected returned session_type" );
        checkRegex( "[a-zA-Z0-9\\+/=]+", responses.get( "dh_server_public" ), "No returned dh_server_public" );
        checkObject( responses.get( "enc_mac_key" ), "Unexpected returned enc_mac_key" );
        checkNotObject( responses.get( "mac_key" ), "No returned mac_key" );
        
        String assoc_handle            = responses.get(  "assoc_handle" );
        String enc_mac_key_string      = responses.get( "enc_mac_key" );
        String dh_server_public_string = responses.get( "dh_server_public" );
        
        byte []    enc_mac_key      = Base64.base64decode( enc_mac_key_string );
        BigInteger dh_server_public = new BigInteger( Base64.base64decode( dh_server_public_string ));
        BigInteger dhSecret         = dhLocal.computeSharedSecret( dh_server_public );
        byte []    dhSecretSha1     = CryptUtils.calculateSha1( dhSecret );
        byte []    sharedSecret     = CryptUtils.do160BitXor( enc_mac_key, dhSecretSha1 );

        MyAssociation assoc = new MyAssociation( assoc_handle, sharedSecret );
        return assoc;
    }

    /**
     * Setup.
     *
     * @param args not used
     * @throws Exception any kind of exception
     */
    public AbstractOpenId1SsoTest(
            String [] args )
        throws
            Exception
    {
        super( args );
    }

    /**
     * The default value for the Diffie-Helloman p parameter in OpenID.
     */
    public static final BigInteger DEFAULT_P = new BigInteger(
            "155172898181473697471232257763715539915724801966915404479707795314057629378541917580651227423698188993727816152646631438561595825688188889951272158842675419950341258706556549803580104870537681476726513255747040765857479291291572334510643245094715007229621094194349783925984760375594985848253359305585439638443" );

    /**
     * The default value for the Diffie-Helloman g parameter in OpenID.
     */
    public static final BigInteger DEFAULT_G = BigInteger.valueOf( 2 );
    
    /**
     * Helper class to capture information about an association.
     */
    protected static class MyAssociation
    {
        /**
         * Constructor.
         * 
         * @param handle the association's handle
         * @param secret the association's secret
         */
        public MyAssociation(
                String handle,
                byte [] secret )
        {
            theHandle = handle;
            theSecret = secret;
        }
        
        /**
         * Obtain the association handle.
         * 
         * @return the association handloe
         */
        public String getHandle()
        {
            return theHandle;
        }
        
        /**
         * Obtain the association secret.
         * 
         * @return the association secret
         */
        public byte [] getSecret()
        {
            return theSecret;
        }
        /**
         * The association handle.
         */
        protected String theHandle;
        
        /**
         * The association secret, decoded.
         */
        protected byte [] theSecret;
    }
}
