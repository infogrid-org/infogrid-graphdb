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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.traversal;

import org.infogrid.util.AbstractLocalizedException;

/**
 * Thrown if a TraversalSpecification could not be translated. This may be overridden by subclasses.
 */
public class TraversalTranslatorException
    extends
        AbstractLocalizedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     */
    public TraversalTranslatorException()
    {
    }

    /**
     * Constructor with a message.
     *
     * @param msg the message
     */
    public TraversalTranslatorException(
            String msg )
    {
        super( msg );
    }

    /**
     * Constructor with no message but a cause.
     *
     * @param cause the Throwable that caused this Exception
     */
    public TraversalTranslatorException(
            Throwable cause )
    {
        super( cause );
    }

    /**
     * Constructor with a message and a cause.
     *
     * @param msg the message
     * @param cause the Exception that caused this Exception
     */
    public TraversalTranslatorException(
            String    msg,
            Throwable cause )
    {
        super( msg, cause );
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[0];
    }
}
