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

import org.infogrid.util.ArrayHelper;

/**
 * Collection of utility methods for MeshObjects.
 */
public abstract class MeshObjectUtils
{
    /**
     * Private constructor, this class cannot be instantiated.
     */
    private MeshObjectUtils()
    {
        // noop
    }

    /**
     * Construct an array of MeshObjectIdentifiers from an array of MeshObjects.
     *
     * @param objs the MeshObjects
     * @return the MeshObjectIdentifiers of the MeshObjects
     */
    public static MeshObjectIdentifier [] meshObjectIdentifiers(
            MeshObject [] objs )
    {
        if( objs == null ) {
            return null;
        }
        MeshObjectIdentifier [] ret = new MeshObjectIdentifier[ objs.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = objs[i].getIdentifier();
        }
        return ret;
    }
}
