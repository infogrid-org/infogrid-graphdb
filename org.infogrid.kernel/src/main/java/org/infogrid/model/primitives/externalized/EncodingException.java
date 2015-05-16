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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.externalized;

/**
 * Thrown if encoding of a MeshObject failed.
 */
public class EncodingException
        extends
            Exception
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param message the message
     */
    public EncodingException(
            String message )
    {
        super( message );
    }

    /**
     * Constructor.
     *
     * @param cause the underlying cause
     */
    public EncodingException(
            Throwable cause )
    {
        super( cause );
    }

    /**
     * Constructor.
     *
     * @param message the message
     * @param cause the underlying cause
     */
    public EncodingException(
            String    message,
            Throwable cause )
    {
        super( message, cause );
    }
}
