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

package org.infogrid.mesh.deref;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;

/**
 * A common superclass for DerefProperty and DerefPropertyGroup, similar to
 * PropertyTypeOrGroup / PropertyType / PropertyTypeGroup.
 */
public abstract class DerefPropertyTypeOrGroup
{
    /**
     * Constructor only for subtypes.
     *
     * @param mo the MeshObject of this DerefPropertyTypeOrGroup
     */
    protected DerefPropertyTypeOrGroup(
            MeshObject mo )
    {
        theMeshObject = mo;
    }

    /**
      * Obtain the MeshObject in this DerefPropertyTypeOrGroup.
      *
      * @return the MeshObject in this DerefPropertyTypeOrGroup
      */
    public final MeshObject getMeshObject()
    {
        return theMeshObject;
    }

    /**
      * Obtain the MeshBase which we use to create Transactions. This
      * is a convenience function as it simply obtains the MeshObject's
      * containing MeshBase.
      *
      * @return the MeshBase on which to create Transactions
      */
    public final MeshBase getMeshBase()
    {
        return theMeshObject.getMeshBase();
    }

    /**
      * The MeshObject in this DerefPropertyTypeOrGroup.
      */
    protected MeshObject theMeshObject;
}
