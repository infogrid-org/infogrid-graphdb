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
import javax.servlet.GenericServlet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.infogrid.jee.app.InfoGridWebApp;
import org.infogrid.jee.sane.SaneServletRequest;
import org.infogrid.jee.servlet.AbstractInfoGridServlet;
import org.infogrid.jee.templates.NoContentStructuredResponseTemplate;
import org.infogrid.jee.templates.StructuredResponse;
import org.infogrid.jee.templates.TextStructuredResponseSection;
import org.infogrid.jee.templates.utils.JeeTemplateUtils;
import org.infogrid.lid.LidClientAuthenticationPipelineStage;
import org.infogrid.lid.LidClientAuthenticationStatus;
import org.infogrid.lid.LidPipelineInstructions;
import org.infogrid.lid.credential.LidInvalidCredentialException;
import org.infogrid.lid.servlet.LidPipelineServlet;
import org.infogrid.util.HasIdentifier;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.http.SaneRequest;
import org.infogrid.util.http.SaneRequestUtils;
import org.infogrid.util.logging.Log;

/**
 * Serves the right content for this application.
 */
public class ContentServlet
        extends
            AbstractInfoGridServlet
{
    private static final long serialVersionUID = 1L; // helps with serialization
    private static final Log  log              = Log.getLogInstance( ContentServlet.class );

    /**
     * Constructor.
     */
    public ContentServlet()
    {
        // nothing right now
    }

    /**
     * Main servlet method.
     *
     * @param request the incoming request
     * @param response the outgoing response
     * @throws ServletException thrown if an error occurred
     * @throws IOException thrown if an I/O error occurred
     */
    public final void service(
            ServletRequest  request,
            ServletResponse response )
        throws
            ServletException,
            IOException
    {
        HttpServletRequest realRequest     = (HttpServletRequest) request;
        InfoGridWebApp     app             = getInfoGridWebApp();
        SaneRequest        lidRequest      = SaneServletRequest.create( realRequest );
        SaneRequest        originalRequest = lidRequest.getOriginalSaneRequest();

        StructuredResponse lidResponse = (StructuredResponse) request.getAttribute( StructuredResponse.STRUCTURED_RESPONSE_ATTRIBUTE_NAME );

        LidPipelineInstructions instructions = (LidPipelineInstructions) request.getAttribute(
                LidPipelineServlet.PROCESSING_PIPELINE_INSTRUCTIONS_ATTRIBUTE_NAME );

        LidClientAuthenticationStatus authenticationStatus = instructions.getClientAuthenticationStatus();
        HasIdentifier                 requestedResource    = instructions.getRequestedResource();
        boolean                       isSso                = instructions.getSsoInstructionsIter().hasNext();
        
        String  contextUriWithSlash = originalRequest.getAbsoluteContextUriWithSlash();
        boolean isFront             = originalRequest.getAbsoluteBaseUri().equals( contextUriWithSlash );
        boolean isMe                = requestedResource != null && requestedResource.getIdentifier().equals( authenticationStatus.getClientIdentifier() );

        // these are the basic actions we can take
        String  dispatcherName = null;
        String  location       = null;
        int     status         = HttpServletResponse.SC_OK;
        
        Boolean showLogin  = null;
        Boolean showLogout = null;
        Boolean showLinkToClientHome = null;
        Boolean showLinkToSiteHome   = null;
        String  lid = null;
        
        TextStructuredResponseSection messagesSection = lidResponse.obtainTextSection(
                StructuredResponse.HTML_MESSAGES_SECTION );

        LidInvalidCredentialException [] invalidEx = authenticationStatus.getInvalidCredentialExceptions();
        if( invalidEx != null ) {
            for( Throwable t : authenticationStatus.getInvalidCredentialExceptions() ) {
                lidResponse.reportProblem( t );
            }
        }

        if( authenticationStatus.getClientIdentifier() != null ) {
            lid = authenticationStatus.getClientIdentifier().toExternalForm();
        }

        if( authenticationStatus.isInvalidIdentity() ) {
            if( isFront ) {
                dispatcherName = "/s/front.jsp";
            } else {
                // not front
                dispatcherName = "/s/nodata.jsp";
            }
            showLogin = Boolean.TRUE;
            
            lidResponse.reportProblem( new InvalidAccountException( originalRequest, authenticationStatus.getClientIdentifier() ));

        } else if(    authenticationStatus.isAnonymous()
                   || authenticationStatus.isClaimedOnly()
                   || authenticationStatus.isExpiredSessionOnly()
                   || authenticationStatus.isCarryingExpiredCredential() )
        {
            // anonymous
            if(    authenticationStatus.clientWishesToCancelSession()
                || authenticationStatus.clientWishesToLogout() )
            {
                // just logged out
                messagesSection.appendContent( theLoggedOutMessage );
            }

            if( isFront ) {
                dispatcherName = "/s/front.jsp";
                showLogin      = Boolean.TRUE;

            } else if( isSso ) {
                // not front, but we want to do SSO
                dispatcherName = "/s/front.jsp";
                showLogin      = Boolean.TRUE;

            } else {
                // not front, not SSO
                location = contextUriWithSlash;
            }

        } else if( authenticationStatus.isCarryingInvalidCredential() ) {
            // at least one invalid credential -- cut off session
            showLogin           = Boolean.TRUE;
            showLinkToSiteHome  = Boolean.TRUE;
            dispatcherName      = "/s/nodata.jsp";

        } else if( authenticationStatus.isCarryingValidCredential() ) {
            // client just successfully authenticated

            request.setAttribute( LidClientAuthenticationPipelineStage.LID_PARAMETER_NAME, lid );

            if( isFront ) {
                showLinkToClientHome       = Boolean.TRUE;
                location                   = authenticationStatus.getClientIdentifier().toExternalForm();
                
            } else if( isMe ) {
                // my own page
                dispatcherName     = "/s/user-own.jsp";
                showLogout         = Boolean.TRUE;
                showLinkToSiteHome = Boolean.TRUE;
                
            } else if( requestedResource != null ) {
                // not front, not me, but exists
                dispatcherName       = "/s/user-authenticated.jsp";
                showLogout           = Boolean.TRUE;
                showLinkToClientHome = Boolean.TRUE;
                showLinkToSiteHome   = Boolean.TRUE;
                
            } else {
                // not found
                dispatcherName       = "/s/errors/404.jsp";
                showLogout           = Boolean.TRUE;
                showLinkToClientHome = Boolean.TRUE;
                showLinkToSiteHome   = Boolean.TRUE;
                status               = 404;
            }

        } else if( authenticationStatus.isValidSessionOnly() && !authenticationStatus.clientWishesToCancelSession() ) {
            // still valid session

            request.setAttribute( LidClientAuthenticationPipelineStage.LID_PARAMETER_NAME, lid );

            if( isFront ) {
                dispatcherName       = "/s/front.jsp";
                showLogout           = Boolean.TRUE;
                showLinkToClientHome = Boolean.TRUE;

            } else if( isMe ) {
                // my own page
                dispatcherName     = "/s/user-own.jsp";
                showLogout         = Boolean.TRUE;
                showLinkToSiteHome = Boolean.TRUE;

            } else if( requestedResource != null ) {
                // not front, not me, but exists
                dispatcherName       = "/s/user-authenticated.jsp";
                showLogout           = Boolean.TRUE;
                showLinkToClientHome = Boolean.TRUE;
                showLinkToSiteHome   = Boolean.TRUE;

            } else {
                // not found
                dispatcherName       = "/s/errors/404.jsp";
                showLogout           = Boolean.TRUE;
                showLinkToClientHome = Boolean.TRUE;
                showLinkToSiteHome   = Boolean.TRUE;
                status               = 404;
            }

        } else {
            log.error( "Not sure how we got here: " + authenticationStatus );
        }
        
        // now take the action
        
        request.setAttribute( SHOW_LOGIN_ATTRIBUTE_NAME,               showLogin );
        request.setAttribute( SHOW_LOGOUT_ATTRIBUTE_NAME,              showLogout );
        request.setAttribute( SHOW_LINK_TO_CLIENT_HOME_ATTRIBUTE_NAME, showLinkToClientHome );
        request.setAttribute( SHOW_LINK_TO_SITE_FRONT_ATTRIBUTE_NAME,  showLinkToSiteHome );
        
        if( dispatcherName != null ) {
            RequestDispatcher dispatcher = app.findLocalizedRequestDispatcher(
                    dispatcherName,
                    originalRequest.acceptLanguageIterator(),
                    getServletContext() );

            if( dispatcher != null ) {
                JeeTemplateUtils.runRequestDispatcher( dispatcher, originalRequest, lidResponse );

            } else {
                log.error( "Cannot find dispatcher " + dispatcherName );
            }
        } else {
            // dispatcher is null
            lidResponse.setRequestedTemplateName( NoContentStructuredResponseTemplate.NO_CONTENT_TEMPLATE_NAME );
        }

        TextStructuredResponseSection defaultSection = lidResponse.getDefaultTextSection();
        if( location != null ) {
            defaultSection.setHttpResponseCode( 302 );
            defaultSection.setLocation( location );

        } else if( defaultSection.getHttpResponseCode() < 0 ) {
            defaultSection.setHttpResponseCode( status );
        }
    }
    
    /**
     * Name of the request attribute, containing boolean, that indicates whether the login screen components shall be shown.
     */
    public static final String SHOW_LOGIN_ATTRIBUTE_NAME
            = SaneRequestUtils.classToAttributeName( ContentServlet.class, "show_login" );
    
    /**
     * Name of the request attribute, containing boolean, that indicates whether the logout screen components shall be shown.
     */
    public static final String SHOW_LOGOUT_ATTRIBUTE_NAME
            = SaneRequestUtils.classToAttributeName( ContentServlet.class, "show_logout" );
    
    /**
     * Name of the request attribute, containing boolean, that indicates whether the link to the client's
     * home page shall be shown.
     */
    public static final String SHOW_LINK_TO_CLIENT_HOME_ATTRIBUTE_NAME
            = SaneRequestUtils.classToAttributeName( ContentServlet.class, "link_to_client_home" );
    
    /**
     * Name of the request attribute, containing boolean, that indicates whether the link to the site's
     * front page shall be shown.
     */
    public static final String SHOW_LINK_TO_SITE_FRONT_ATTRIBUTE_NAME
            = SaneRequestUtils.classToAttributeName( ContentServlet.class, "link_to_site_front" );
    
    /**
     * Our ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( ContentServlet.class );

    /**
     * The "you have been logged out" message.
     */
    protected static final String theLoggedOutMessage = theResourceHelper.getResourceString( "LoggedOutMessage" );
}
