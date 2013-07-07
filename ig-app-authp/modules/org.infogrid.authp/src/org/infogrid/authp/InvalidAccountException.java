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

import org.infogrid.util.AbstractLocalizedException;
import org.infogrid.util.Identifier;
import org.infogrid.util.SimpleStringIdentifier;
import org.infogrid.util.http.SaneRequest;

/**
 * Thrown if an unknown username was entered.
 */
public class InvalidAccountException
        extends
            AbstractLocalizedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param request the incoming request
     * @param invalidIdentifier the invalid Identifier
     */
    public InvalidAccountException(
            SaneRequest request,
            Identifier  invalidIdentifier )
    {
        this( request, invalidIdentifier, null );
    }

    /**
     * Constructor.
     *
     * @param request the incoming request
     * @param invalidIdentifier the invalid Identifier
     * @param cause the underlying cause, if any
     */
    public InvalidAccountException(
            SaneRequest request,
            Identifier  invalidIdentifier,
            Throwable   cause )
    {
        super( cause );

        theRequest           = request;
        theInvalidIdentifier = invalidIdentifier;
    }

    /**
     * Obtain the incoming request.
     *
     * @return the incoming request
     */
    public SaneRequest getRequest()
    {
        return theRequest;
    }

    /**
     * Obtain the invalid invalidIdentifier.
     *
     * @return the invalidIdentifier
     */
    public Identifier getIdentifier()
    {
        return theInvalidIdentifier;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        Identifier shortIdentifier = null;
        if( theInvalidIdentifier.toExternalForm().startsWith( theRequest.getAbsoluteContextUriWithSlash() )) {
            shortIdentifier = SimpleStringIdentifier.create(
                    theInvalidIdentifier.toExternalForm().substring(
                            theRequest.getAbsoluteContextUriWithSlash().length() ));
        } else {
            shortIdentifier = theInvalidIdentifier;
        }
        return new Object[] { theInvalidIdentifier, shortIdentifier };
    }

    /**
     * The incoming request.
     */
    protected SaneRequest theRequest;

    /**
     * The invalid invalidIdentifier.
     */
    protected Identifier theInvalidIdentifier;
}