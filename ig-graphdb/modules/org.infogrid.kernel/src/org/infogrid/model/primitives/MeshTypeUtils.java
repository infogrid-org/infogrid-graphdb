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

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;

/**
 * Collection of utility methods for MeshTypes.
 */
public abstract class MeshTypeUtils
{
    /**
     * Private constructor, this class cannot be instantiated.
     */
    private MeshTypeUtils()
    {
        // noop
    }

    /**
     * Construct an array of MeshTypeIdentifiers from an array of MeshTypes.
     *
     * @param types the MeshTypeIdentifiers
     * @return the MeshTypeIdentifiers of the MeshTypes
     */
    public static MeshTypeIdentifier [] meshTypeIdentifiersOrNull(
            MeshType [] types )
    {
        if( types == null ) {
            return null;
        }
        MeshTypeIdentifier [] ret = new MeshTypeIdentifier[ types.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = types[i].getIdentifier();
        }
        return ret;
    }

    /**
     * Construct an array of MeshTypeIdentifiers from the MeshTypes by which
     * a MeshObject is blessed.
     *
     * @param obj the MeshObject
     * @return the MeshTypeIdentifiers of the MeshTypes of the MeshObject
     */
    public static MeshTypeIdentifier [] meshTypeIdentifiersOrNull(
            MeshObject obj )
    {
        if( obj == null ) {
            return null;
        }
        MeshType           [] types = obj.getTypes();
        MeshTypeIdentifier [] ret = new MeshTypeIdentifier[ types.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = types[i].getIdentifier();
        }
        return ret;
    }
}
