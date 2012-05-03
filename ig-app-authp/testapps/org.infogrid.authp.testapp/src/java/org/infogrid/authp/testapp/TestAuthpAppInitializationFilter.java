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

package org.infogrid.authp.testapp;

import org.infogrid.authp.AuthpAppInitializationFilter;
import org.infogrid.authp.AuthpClientAuthenticationPipelineStage;
import java.io.IOException;
import javax.naming.NamingException;
import org.infogrid.lid.session.DefaultLidSessionManagementPipelineStage;
import org.infogrid.lid.account.LidAccountManager;
import org.infogrid.lid.session.LidSessionManagementPipelineStage;
import org.infogrid.lid.session.LidSessionManager;
import org.infogrid.lid.credential.LidCredentialType;
import org.infogrid.lid.account.prepostfix.PrePostfixTranslatingAccountManager;
import org.infogrid.lid.account.regex.RegexLidAccountManager;
import org.infogrid.lid.account.regex.RegexLidPasswordCredentialType;
import org.infogrid.lid.session.store.StoreLidSessionManager;
import org.infogrid.store.m.MStore;
import org.infogrid.util.Identifier;
import org.infogrid.util.SimpleStringIdentifierFactory;
import org.infogrid.util.context.Context;
import org.infogrid.util.http.SaneRequest;

/**
 * Initializes TestAuthpApp.
 */
public class TestAuthpAppInitializationFilter
    extends
        AuthpAppInitializationFilter
{
    /**
     * Public constructor per servlet spec.
     */
    public TestAuthpAppInitializationFilter()
    {
        // nothing
    }

    /**
     * Initialize the data sources.
     *
     * @throws NamingException thrown if a data source could not be found or accessed
     * @throws IOException thrown if an I/O problem occurred
     */
    protected void initializeDataSources()
            throws
                NamingException,
                IOException
    {
        theOpenIdSmartAssociationStore = MStore.create();
        theOpenIdDumbAssociationStore  = MStore.create();
        theNonceStore                  = MStore.create();
        theSessionStore                = MStore.create();

        // the initialization calls are no ops, but for consistency they should be here
        theOpenIdSmartAssociationStore.initializeIfNecessary();
        theOpenIdDumbAssociationStore.initializeIfNecessary();
        theNonceStore.initializeIfNecessary();
        theSessionStore.initializeIfNecessary();
    }

    /**
     * Initialize the authentication pipeline.
     *
     * @param lidRequest the incoming request
     * @param appContext application Context
     */
    protected void initializeAuthenticationPipeline(
            SaneRequest       lidRequest,
            Context           appContext )
    {
        SimpleStringIdentifierFactory idFact = SimpleStringIdentifierFactory.create();
        Identifier                    siteId = idFact.fromExternalForm( lidRequest.getOriginalSaneRequest().getAbsoluteContextUriWithSlash() );

        RegexLidPasswordCredentialType credType = RegexLidPasswordCredentialType.create( ".*j" );

        LidAccountManager lidPasswordManager = RegexLidAccountManager.create(
                siteId,
                "J.*",
                null,
                idFact );
        LidAccountManager accountManagerBridge = PrePostfixTranslatingAccountManager.create(
                siteId,
                siteId.toExternalForm(),
                lidPasswordManager );
        appContext.addContextObject( accountManagerBridge );

        // Session manager
        LidSessionManager lidSessionManager = StoreLidSessionManager.create( theSessionStore, accountManagerBridge );
        appContext.addContextObject( lidSessionManager );

        AuthpClientAuthenticationPipelineStage authStage = AuthpClientAuthenticationPipelineStage.create(
                lidSessionManager,
                accountManagerBridge,
                new LidCredentialType[] {
                        credType
                });
        appContext.addContextObject( authStage );

        LidSessionManagementPipelineStage sessionMgmtStage = DefaultLidSessionManagementPipelineStage.create( lidSessionManager );
        appContext.addContextObject( sessionMgmtStage );

        // Lid
        //    MinimumLidProcessor minProc = MinimumLidProcessor.create( appContext );
        //    appContext.addContextObject( minProc );
    }
}
