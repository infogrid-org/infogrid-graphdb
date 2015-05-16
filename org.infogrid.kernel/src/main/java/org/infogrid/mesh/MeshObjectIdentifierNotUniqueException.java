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
import org.infogrid.meshbase.MeshObjectAccessException;

/**
  * <p>This Exception is thrown if an attempt is made to create a new MeshObject with a
  *    MeshObjectIdentifier that is used already for a different MeshObject. This
  *    typically indicates a programming error by the application programmer.</p>
  */
public class MeshObjectIdentifierNotUniqueException
    extends
        MeshObjectGraphModificationException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param existing the MeshObject that exists already with the same MeshObjectIdentifier
     * @param existingIdentifier the MeshObjectIdentifier of the MeshObject that exists already with the same MeshObjectIdentifier
     */
    public MeshObjectIdentifierNotUniqueException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           existing,
            MeshObjectIdentifier existingIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier );

        theExisting           = existing;
        theExistingIdentifier = existingIdentifier;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject that exists already with the same MeshObjectIdentifier
     */
    public MeshObjectIdentifierNotUniqueException(
            MeshObject obj )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier() );
    }

    /**
     * Obtain the MeshObject that already existed with this MeshObjectIdentifier
     * 
     * @return the already-existing MeshObject
     * @throws MeshObjectAccessException thrown if the MeshObject could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    public synchronized MeshObject getExistingMeshObject()
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theExisting == null ) {
            theExisting = resolve( theExistingIdentifier );
        }
        return theExisting;
    }

    /**
     * Obtain the Identifier of the MeshObject that existed already with the same Identifier.
     * This value will always be available, even after serialization/deserialization.
     *
     * @return the Identifier of the MeshObject that existed already with the same Identifier.
     */
    public final MeshObjectIdentifier getExistingMeshObjectIdentifier()
    {
        return theExistingIdentifier;
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theExistingIdentifier };
    }

    /**
     * The already-existing MeshObject.
     */
    protected transient MeshObject theExisting;

    /**
     * The Identifier of the already-existing MeshObject.
     */
    protected MeshObjectIdentifier theExistingIdentifier;
}
