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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.myhealth.www;

import org.infogrid.util.AbstractLocalizedException;

/**
 * Indicates that Unnamed App could not connect to the specified database and will run with
 * an in-memory MeshBase instead.
 */
public class InMemoryOnlyException
    extends
        AbstractLocalizedException
{
    private static final long serialVersionUID = 1L; // helps with serialiazation

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        return null;
    }
}
