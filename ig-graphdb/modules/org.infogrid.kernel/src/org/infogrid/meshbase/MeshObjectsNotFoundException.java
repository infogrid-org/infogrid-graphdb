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

package org.infogrid.meshbase;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;

/**
 * Thrown if one or more MeshObjects could not be found.
 */
public class MeshObjectsNotFoundException
        extends
            MeshObjectAccessException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param mb the MeshBase that threw this Exception
     * @param identifier the identifier of the MeshObject that was not found
     */
    public MeshObjectsNotFoundException(
            MeshBase             mb,
            MeshObjectIdentifier identifier )
    {
        this( mb, null, new MeshObjectIdentifier[] { identifier } );
    }

    /**
     * Constructor.
     * 
     * @param mb the MeshBase that threw this Exception
     * @param identifiers the identifiers of the MeshObjects some of which were not found
     * @param partialResult the subset of MeshObjects that were found, in the same sequence and position as identifiers.
     *        MeshObjects that were not found are null in this array.
     */
    public MeshObjectsNotFoundException(
            MeshBase                mb,
            MeshObject []           partialResult,
            MeshObjectIdentifier [] identifiers )
    {
        super( mb, mb.getIdentifier(), partialResult, identifiers, null );
    }
}
