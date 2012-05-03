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

package org.infogrid.authp.www;

import java.io.File;
import java.io.FileInputStream;
import org.infogrid.authp.AuthpAppInitializationFilter;
import org.infogrid.authp.AuthpClientAuthenticationPipelineStage;
import java.io.IOException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.infogrid.jee.templates.defaultapp.AppInitializationException;
import org.infogrid.lid.session.DefaultLidSessionManagementPipelineStage;
import org.infogrid.lid.account.LidAccountManager;
import org.infogrid.lid.account.ldap.LdapLidLocalAccountManager;
import org.infogrid.lid.account.ldap.LdapLidPasswordCredentialType;
import org.infogrid.lid.session.LidSessionManagementPipelineStage;
import org.infogrid.lid.session.LidSessionManager;
import org.infogrid.lid.credential.LidCredentialType;
import org.infogrid.lid.account.translate.TranslatingLidAccountManager;
import org.infogrid.lid.account.prepostfix.PrePostfixTranslatingAccountManager;
import org.infogrid.lid.account.translate.TranslatingLidCredentialType;
import org.infogrid.lid.session.store.StoreLidSessionManager;
import org.infogrid.store.Store;
import org.infogrid.store.sql.mysql.MysqlStore;
import org.infogrid.util.Identifier;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.SimpleStringIdentifierFactory;
import org.infogrid.util.context.Context;
import org.infogrid.util.http.SaneRequest;
import org.infogrid.util.naming.NamingReportingException;

/**
 * Initializes WwwAuthpApp.
 */
public class WwwAuthpAppInitializationFilter
    extends
        AuthpAppInitializationFilter
{
    /**
     * Public constructor per servlet spec.
     */
    public WwwAuthpAppInitializationFilter()
    {
        // nothing
    }
    
    /**
     * Initialize the Filter.
     *
     * @param filterConfig the Filter configuration object
     * @throws ServletException thrown if misconfigured
     */
    @Override
    public void internalInit(
            FilterConfig filterConfig )
        throws
            ServletException
    {
        super.internalInit( filterConfig );

        String ldapPropertiesFileName = filterConfig.getInitParameter( LDAP_PROPERTIES_FILE_FILTER_PARAMETER_NAME );
        if( ldapPropertiesFileName == null ) {
            throw new ServletException( "Missing web.xml filter parameter " + LDAP_PROPERTIES_FILE_FILTER_PARAMETER_NAME + " for class " + getClass().getName() );
        }

        File ldapPropertiesFile = new File( ldapPropertiesFileName );

        if( !ldapPropertiesFile.exists() || !ldapPropertiesFile.isFile() || !ldapPropertiesFile.canRead() ) {
            throw new ServletException( "Cannot read LDAP properties file: " + ldapPropertiesFileName );
        }

        theLdapProperties = new Properties();
        try {
            theLdapProperties.load( new FileInputStream( ldapPropertiesFile ));
        } catch( IOException ex ) {
            throw new ServletException( "Cannot read LDAP properties file: " + ldapPropertiesFileName, ex );
        }
    }

    /**
     * Initialize the data sources.
     *
     * @throws NamingException thrown if a data source could not be found or accessed
     * @throws IOException thrown if an I/O problem occurred
     * @throws AppInitializationException thrown if the application could not be initialized
     */
    protected void initializeDataSources()
            throws
                NamingException,
                IOException,
                AppInitializationException
    {
        String         name = "java:comp/env/jdbc/authpdb";
        InitialContext ctx  = null;
        Throwable      toThrow = null;

        try {
            // Database access via JNDI
            ResourceHelper rh = ResourceHelper.getInstance( WwwAuthpAppInitializationFilter.class );

            ctx                      = new InitialContext();
            DataSource theDataSource = (DataSource) ctx.lookup( name );

            theOpenIdSmartAssociationStore = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "OpenIdSmartAssociationTable", "OpenIdSmartAssociation" ));
            theOpenIdDumbAssociationStore  = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "OpenIdDumbAssociationTable",  "OpenIdDumbAssociation" ));
            theNonceStore                  = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "LidNonceTable",               "LidNonce" ));
            theSessionStore                = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "LidSessionTable",             "LidSession" ));
            theAccountStore                = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "LidAccountTable",             "LidAccount" ));
            thePasswordStore               = MysqlStore.create( theDataSource, rh.getResourceStringOrDefault( "LidPasswordTable",            "LidSimplePassword" ));

            theOpenIdSmartAssociationStore.initializeIfNecessary();
            theOpenIdDumbAssociationStore.initializeIfNecessary();
            theNonceStore.initializeIfNecessary();
            theSessionStore.initializeIfNecessary();
            theAccountStore.initializeIfNecessary();
            thePasswordStore.initializeIfNecessary();

        } catch( NamingException ex ) {
            toThrow = new NamingReportingException( name, ctx, ex );
            throw (NamingReportingException) toThrow;

        } catch( IOException ex ) {
            toThrow = ex;
            throw (IOException) toThrow;

        } catch( Throwable ex ) {
            toThrow = new AppInitializationException( ex );
            throw (AppInitializationException) toThrow;

        } finally {
            if( toThrow != null ) {
                theOpenIdSmartAssociationStore = null;
                theOpenIdDumbAssociationStore  = null;
                theNonceStore                  = null;
                theSessionStore                = null;
                theAccountStore                = null;
                thePasswordStore               = null;
            }
        }
    }

    /**
     * Initialize the authentication pipeline.
     *
     * @param lidRequest the incoming request
     * @param appContext application Context
     * @throws NamingException thrown if a data source could not be found or accessed
     * @throws IOException thrown if an I/O problem occurred
     */
    protected void initializeAuthenticationPipeline(
            SaneRequest       lidRequest,
            Context           appContext )
        throws
            NamingException,
            IOException
    {
        SimpleStringIdentifierFactory idFact = SimpleStringIdentifierFactory.create();
        Identifier                    siteId = idFact.fromExternalForm( lidRequest.getOriginalSaneRequest().getAbsoluteContextUriWithSlash() );

        String [] attributeList = null;
        String attList = theLdapProperties.getProperty( LDAP_ATTRIBUTE_LIST, null );
        if( attList != null ) {
            attList = attList.trim();
        }
        if( attList != null && attList.length() > 0 ) {
            attributeList = attList.split( "," );
            for( int i=0 ; i<attributeList.length ; ++i ) {
                attributeList[i] = attributeList[i].trim();
            }
        } else {
             attributeList = null;
        }

        LidAccountManager lidPasswordManager = LdapLidLocalAccountManager.create(
                siteId,
                theLdapProperties,
                theLdapProperties.getProperty( LDAP_SEARCH_CONTEXT, null ),
                theLdapProperties.getProperty( LDAP_USER_ID_FILTER, null ),
                attributeList,
                idFact );

        TranslatingLidAccountManager accountManagerBridge = PrePostfixTranslatingAccountManager.create(
                siteId,
                siteId.toExternalForm(),
                lidPasswordManager );
        appContext.addContextObject( accountManagerBridge );

        LidCredentialType [] availableCredentialTypes = {
                TranslatingLidCredentialType.create(
                        accountManagerBridge,
                        LdapLidPasswordCredentialType.create( theLdapProperties, theLdapProperties.getProperty( IDENTIFIER_SUFFIX, null ) ))
        };

        // Session manager
        LidSessionManager lidSessionManager = StoreLidSessionManager.create( theSessionStore, accountManagerBridge );
        appContext.addContextObject( lidSessionManager );

        AuthpClientAuthenticationPipelineStage authStage = AuthpClientAuthenticationPipelineStage.create(
                lidSessionManager,
                accountManagerBridge,
                availableCredentialTypes );
        appContext.addContextObject( authStage );

        LidSessionManagementPipelineStage sessionMgmtStage = DefaultLidSessionManagementPipelineStage.create( lidSessionManager );
        appContext.addContextObject( sessionMgmtStage );

        // Lid
        //    MinimumLidProcessor minProc = MinimumLidProcessor.create( appContext );
        //    appContext.addContextObject( minProc );
    }

    /**
     * The Store for local accounts.
     */
    protected Store theAccountStore;

    /**
     * The Store for passwords.
     */
    protected Store thePasswordStore;

    /**
     * Name of the web.xml filter parameter that indicates from where to read the LDAP properties.
     */
    public static final String LDAP_PROPERTIES_FILE_FILTER_PARAMETER_NAME = "ldap-properties-file";

    /**
     * The  LDAP properties. Initialized when needed only (this is a filter)
     */
    private static Properties theLdapProperties;

    /**
     * Name of the property file parameter defining the Context in which to look for entries.
     */
    public static final String LDAP_SEARCH_CONTEXT = "org.infogrid.authp.va.ldap.Context";

    /**
     * Name of the property file parameter that defines the suffix to append to the identifier
     * for authentication, if any.
     */
    public static final String IDENTIFIER_SUFFIX = "org.infogrid.authp.va.ldap.IdentifierSuffix";

    /**
     * Name of the property file parameter defining the Filter to use to find an entry.
     */
    public static final String LDAP_USER_ID_FILTER = "org.infogrid.authp.va.ldap.UserIdFilter";

    /**
     * Name of the property file parameter defining the list of attributes to pull out of LDAP.
     */
    public static final String LDAP_ATTRIBUTE_LIST = "org.infogrid.authp.va.ldap.AttributeList";
}
