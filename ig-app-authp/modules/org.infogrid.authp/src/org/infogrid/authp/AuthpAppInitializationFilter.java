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

import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.infogrid.jee.JeeFormatter;
import org.infogrid.jee.app.InfoGridWebApp;
import org.infogrid.jee.sane.SaneServletRequest;
import org.infogrid.jee.templates.DefaultStructuredResponseTemplateFactory;
import org.infogrid.jee.templates.StructuredResponseTemplateFactory;
import org.infogrid.jee.templates.defaultapp.AbstractAppInitializationFilter;
import org.infogrid.jee.templates.defaultapp.AppInitializationException;
import org.infogrid.lid.nonce.LidNonceManager;
import org.infogrid.lid.nonce.store.StoreLidNonceManager;
import org.infogrid.lid.openid.OpenIdIdpSideAssociationManager;
import org.infogrid.lid.openid.OpenIdSsoPipelineStage;
import org.infogrid.lid.openid.store.StoreOpenIdIdpSideAssociationManager;
import org.infogrid.lid.yadis.DefaultYadisPipelineStage;
import org.infogrid.store.Store;
import org.infogrid.util.context.Context;
import org.infogrid.util.http.SaneRequest;
import org.infogrid.util.text.StringRepresentationDirectory;
import org.infogrid.util.text.StringRepresentationDirectorySingleton;

/**
 * Initializes application-level functionality.
 */
public abstract class AuthpAppInitializationFilter
        extends
            AbstractAppInitializationFilter
{
    /**
     * Constructor for subclasses only, use factory method.
     */
    public AuthpAppInitializationFilter()
    {
        // nothing right now
    }

    /**
     * <p>Perform initialization.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @throws Throwable something bad happened that cannot be fixed by re-invoking this method
     */
    protected void initialize(
            ServletRequest  request,
            ServletResponse response )
        throws
            Throwable
    {
        HttpServletRequest realRequest = (HttpServletRequest) request;
        SaneRequest        lidRequest  = SaneServletRequest.create( realRequest );
        
        InfoGridWebApp app        = getInfoGridWebApp();
        Context        appContext = app.getApplicationContext();
        
        initializeDataSources();

        // Nonce manager
        LidNonceManager lidNonceManager = StoreLidNonceManager.create( theNonceStore );
        appContext.addContextObject( lidNonceManager );

        // LID
        initializeAuthenticationPipeline( lidRequest, appContext );

        // Yadis
        DefaultYadisPipelineStage yadisProc = DefaultYadisPipelineStage.create( appContext );
        appContext.addContextObject( yadisProc );

        // OpenID
        OpenIdIdpSideAssociationManager smartAssocMgr
                = StoreOpenIdIdpSideAssociationManager.createSmart( theOpenIdSmartAssociationStore );
        appContext.addContextObject( smartAssocMgr );

        OpenIdIdpSideAssociationManager dumbAssocMgr
                = StoreOpenIdIdpSideAssociationManager.createDumb( theOpenIdDumbAssociationStore );
        appContext.addContextObject( dumbAssocMgr );

        OpenIdSsoPipelineStage ssoProc
                = OpenIdSsoPipelineStage.create( smartAssocMgr, dumbAssocMgr, appContext );
        appContext.addContextObject( ssoProc );

        // StructuredResponseTemplateFactory
        StructuredResponseTemplateFactory tmplFactory = DefaultStructuredResponseTemplateFactory.create( getInfoGridWebApp() );
        appContext.addContextObject( tmplFactory );

        StringRepresentationDirectory srepdir = StringRepresentationDirectorySingleton.getSingleton();
        appContext.addContextObject( srepdir );

        JeeFormatter formatter = JeeFormatter.create( srepdir );
        appContext.addContextObject( formatter );
    }

    /**
     * Initialize the data sources.
     * 
     * @throws NamingException thrown if a data source could not be found or accessed
     * @throws IOException thrown if an I/O problem occurred
     * @throws AppInitializationException thrown if the application could not be initialized
     */
    protected abstract void initializeDataSources()
            throws
                NamingException,
                IOException,
                AppInitializationException;

    /**
     * Initialize the authentication pipeline.
     *
     * @param lidRequest the incoming request
     * @param appContext application Context
     * @throws NamingException thrown if a data source could not be found or accessed
     * @throws IOException thrown if an I/O problem occurred
     */
    protected abstract void initializeAuthenticationPipeline(
            SaneRequest lidRequest,
            Context     appContext )
        throws
            NamingException,
            IOException;

    /**
     * The Store for smart OpenID Associations. This must be set by a subclass.
     */
    protected Store theOpenIdSmartAssociationStore;

    /**
     * The Store for dumb OpenID Associations. This must be set by a subclass.
     */
    protected Store theOpenIdDumbAssociationStore;

    /**
     * The Store for nonces. This must be set by a subclass.
     */
    protected Store theNonceStore;

    /**
     * The Store for sessions. This must be set by a subclass.
     */
    protected Store theSessionStore;
}
