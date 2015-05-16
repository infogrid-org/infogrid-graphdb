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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;

/**
 * Selects any MeshObject contained in a set of allowed MeshObjects identified
 * by their identifiers.
 *
 * FIXME this could be more efficient.
 */
public class RightInstanceMeshObjectSelector
        extends
            AbstractMeshObjectSelector
{
    /**
     * Factory method.
     *
     * @param single a single allowed MeshObject
     * @return the created RightInstanceMeshObjectSelector
     */
    public static RightInstanceMeshObjectSelector create(
            MeshObject single )
    {
        return new RightInstanceMeshObjectSelector( new MeshObjectIdentifier[] { single.getIdentifier() } );
    }

    /**
     * Factory method.
     *
     * @param singleId identifier of a single allowed MeshObject
     * @return the created RightInstanceMeshObjectSelector
     */
    public static RightInstanceMeshObjectSelector create(
            MeshObjectIdentifier singleId )
    {
        return new RightInstanceMeshObjectSelector( new MeshObjectIdentifier[] { singleId } );
    }

    /**
     * Factory method.
     *
     * @param allowed the allowed MeshObjects
     * @return the created RightInstanceMeshObjectSelector
     */
    public static RightInstanceMeshObjectSelector create(
            MeshObject [] allowed )
    {
        MeshObjectIdentifier [] allowedIds = new MeshObjectIdentifier[ allowed.length ];
        for( int i=0 ; i<allowed.length ; ++i ) {
            allowedIds[i] = allowed[i].getIdentifier();
        }
        return new RightInstanceMeshObjectSelector( allowedIds );
    }

    /**
     * Factory method.
     *
     * @param allowedIds identifiers of the allowed MeshObjects
     * @return the created RightInstanceMeshObjectSelector
     */
    public static RightInstanceMeshObjectSelector create(
            MeshObjectIdentifier [] allowedIds )
    {
        return new RightInstanceMeshObjectSelector( allowedIds );
    }

    /**
     * Factory method.
     *
     * @param allowed the allowed MeshObjects
     * @return the created RightInstanceMeshObjectSelector
     */
    public static RightInstanceMeshObjectSelector create(
            MeshObjectSet allowed )
    {
        return new RightInstanceMeshObjectSelector( allowed.asIdentifiers() );
    }

    /**
     * Private onstructor, use factory method.
     *
     * @param ids MeshObjectIdentifiers of the allowed MeshObjects
     */
    protected RightInstanceMeshObjectSelector(
            MeshObjectIdentifier [] ids )
    {
        theAllowedIds = ids;
    }

    /**
      * Returns true if this MeshObject shall be selected.
      *
      * @param candidate the MeshObject that shall be tested
      * @return true of this MeshObject shall be selected
      */
    public boolean accepts(
            MeshObject candidate )
    {
        MeshObjectIdentifier id = candidate.getIdentifier();
        for( int i=0 ; i<theAllowedIds.length ; ++i ) {
            if( id.equals( theAllowedIds[i] )) {
                return true;
            }
        }
        return false;
    }

    /**
     * The allowed identifiers.
     */
    protected MeshObjectIdentifier [] theAllowedIds;
}
