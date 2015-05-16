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

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;

/**
 * Thrown if an attempt is made to relate a MeshObject to itself.
 */
public class CannotRelateToItselfException
        extends
            RelatedAlreadyException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param meshObject the MeshObject
     * @param meshObjectIdentifier the MeshObjectIdentifier of the MeshObject
     */
    public CannotRelateToItselfException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           meshObject,
            MeshObjectIdentifier meshObjectIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, meshObject, meshObjectIdentifier, meshObject, meshObjectIdentifier );
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param meshObject the MeshObject
     */
    public CannotRelateToItselfException(
            MeshObject meshObject )
    {
        this(   meshObject.getMeshBase(),
                meshObject.getMeshBase().getIdentifier(),
                meshObject,
                meshObject.getIdentifier() );
    }
}
