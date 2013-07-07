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

package org.infogrid.authp;

import org.infogrid.lid.AbstractLidClientAuthenticationPipelineStage;
import org.infogrid.lid.LidInvalidIdentifierException;
import org.infogrid.lid.account.LidAccountManager;
import org.infogrid.lid.session.LidSessionManager;
import org.infogrid.lid.credential.LidCredentialType;
import org.infogrid.util.Identifier;
import org.infogrid.util.SimpleStringIdentifier;
import org.infogrid.util.logging.Log;

/**
 * Implements LidClientAuthenticationPipelineStage for Authp.
 */
public class AuthpClientAuthenticationPipelineStage
        extends
            AbstractLidClientAuthenticationPipelineStage
{
    private static final Log log = Log.getLogInstance( AuthpClientAuthenticationPipelineStage.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @param sessionMgr the session manager to use for client sessions
     * @param accountMgr the LidAccountManager through which to find LidAccounts
     * @param availableCredentialTypes the LidCredentialTypes known by this application
     * @return the created AuthpClientAuthenticationPipelineStage
     */
    public static AuthpClientAuthenticationPipelineStage create(
            LidSessionManager    sessionMgr,
            LidAccountManager    accountMgr,
            LidCredentialType [] availableCredentialTypes )
    {
        AuthpClientAuthenticationPipelineStage ret = new AuthpClientAuthenticationPipelineStage(
                sessionMgr,
                accountMgr,
                availableCredentialTypes );

        return ret;
    }

    /**
     * Constructor for subclasses only, use factory method.
     *
     * @param sessionMgr the session manager to use for client sessions
     * @param accountMgr the LidAccountManager through which to find LidAccounts
     * @param availableCredentialTypes the LidCredentialTypes known by this application
     */
    protected AuthpClientAuthenticationPipelineStage(
            LidSessionManager    sessionMgr,
            LidAccountManager    accountMgr,
            LidCredentialType [] availableCredentialTypes )
    {
        super( sessionMgr, accountMgr, availableCredentialTypes );
    }
    
    /**
     * Correct the entered clientIdentifier.
     * 
     * @param contextUri the absolute URI of the application's context
     * @param candidate what the user typed
     * @return the corrected clientIdentifier
     */
    protected Identifier correctIdentifier(
            String contextUri,
            String candidate )
    {
        if( candidate == null ) {
            return null;
        }
        candidate = candidate.trim();
        if( candidate.length() == 0 ) {
            return SimpleStringIdentifier.create( "" ); // don't return null
        }
        
        String contextWithSlash = contextUri + "/";

        if( candidate.startsWith( contextWithSlash )) {
            // fully-qualified URL
            return SimpleStringIdentifier.create( candidate );
        }
        if( candidate.indexOf( '/' ) < 0 ) {
            // local user ID
            return SimpleStringIdentifier.create( contextWithSlash + candidate );
        }
        // seems to be something else
        if( log.isInfoEnabled() ) {
            log.info( new LidInvalidIdentifierException( SimpleStringIdentifier.create( candidate )));
        }
        return null;
    }

    /**
     * Name of the HTTP Post parameter that contains the clientIdentifier.
     */
    public static final String IDENTIFIER_PAR = "org_infogrid_authp_user";
    
    /**
     * Name of the HTTP Post parameter that contains the credential.
     */
    public static final String CREDENTIAL_PAR = "org_infogrid_authp_pass";
}