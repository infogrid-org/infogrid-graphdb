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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.security;

import org.infogrid.mesh.MeshObject;

import org.infogrid.util.AbstractLocalizedException;

/**
 * This Exception is thrown if a caller attempted to associate a different identity
 * (e.g. super-user identity) with the current Thread, and was not authorized to do so.
 */
public class IdentityChangeException
        extends
            AbstractLocalizedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param t the Thread with which the identity was supposed to be associated
     * @param caller the caller attempting to change the identity
     */
    public IdentityChangeException(
            Thread     t,
            MeshObject caller )
    {
        theThread = t;
        theCaller = caller;
    }
    
    /**
     * Obtain the Thread with which the attempted identity change was supposed to be associated
     *
     * @return the Thread
     */
    public final Thread getThread()
    {
        return theThread;
    }

    /**
     * Obtain the caller that attempted to perform the operation and failed.
     *
     * @return the caller
     */
    public final MeshObject getCaller()
    {
        return theCaller;
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theThread, theCaller };
    }
    
    /**
     * The Thread.
     */
    protected final Thread theThread;
    
    /**
     * The current caller. This may be null for "anonymous".
     */
    protected MeshObject theCaller;
}
