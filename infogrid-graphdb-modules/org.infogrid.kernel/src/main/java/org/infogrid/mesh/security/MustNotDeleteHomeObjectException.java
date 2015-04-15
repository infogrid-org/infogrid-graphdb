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

package org.infogrid.mesh.security;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;

/**
 * Thrown if an attempt is made to delete the home MeshObject of a MeshBase.
 */
public class MustNotDeleteHomeObjectException
        extends
            NotPermittedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param obj the MeshObject whose attempted modification triggered this Exception
     */
    public MustNotDeleteHomeObjectException(
            MeshObject obj )
    {
        super( obj );
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier };
    }
}
