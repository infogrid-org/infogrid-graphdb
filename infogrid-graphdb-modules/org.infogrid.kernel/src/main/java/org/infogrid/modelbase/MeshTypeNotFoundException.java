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

package org.infogrid.modelbase;

import org.infogrid.util.AbstractLocalizedException;

/**
 * This Exception is thrown if a type lookup was unsuccessful.
 * Sub-classes provide more detail.
 */
public abstract class MeshTypeNotFoundException
        extends
            AbstractLocalizedException
{
    /**
     * Protected constructor, use subclasses only.
     */
    protected MeshTypeNotFoundException()
    {
        super();
    }

    /**
     * Protected constructor, use subclasses only.
     *
     * @param cause the Throwable that caused this Exception
     */
    protected MeshTypeNotFoundException(
            Throwable cause )
    {
        super( cause );
    }

    /**
     * Protected constructor, use subclasses only.
     *
     * @param message the message
     */
    protected MeshTypeNotFoundException(
            String message )
    {
        super( message );
    }

    /**
     * Protected constructor, use subclasses only.
     *
     * @param message the message
     * @param cause the Throwable that caused this Exception
     */
    protected MeshTypeNotFoundException(
            String    message,
            Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Convert object into string representation, mostly for debugging.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
    {
        return getClass().getName() + ": ";
    }
}
