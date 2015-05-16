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

package org.infogrid.mesh.set.active;

import org.infogrid.mesh.MeshObject;
import org.infogrid.util.logging.Dumper;

/**
  * This indicates that a MeshObject was removed from an ActiveMeshObjectSet.
  */
public class MeshObjectRemovedEvent
        extends
            ActiveMeshObjectSetEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Constructor.
      *
      * @param _theSet the set that changed
      * @param _theRemovedMeshObject the MeshObject that was removed from the set
      */
    public MeshObjectRemovedEvent(
            ActiveMeshObjectSet _theSet,
            MeshObject          _theRemovedMeshObject )
    {
        super(_theSet, _theRemovedMeshObject );

        theIndexOfRemoved = -1;
    }

    /**
      * Constructor.
      *
      * @param _theSet the set that changed,
      * @param _theRemovedMeshObject the Entity that was removed from the set
      * @param _indexOfRemoved the index in the set from which the Entity was removed
      */
    public MeshObjectRemovedEvent(
            ActiveMeshObjectSet _theSet,
            MeshObject          _theRemovedMeshObject,
            int                 _indexOfRemoved )
    {
        super(_theSet, _theRemovedMeshObject );

        theIndexOfRemoved = _indexOfRemoved;
    }

    /**
     * Obtain the index at which the removed MeshObject used to live before it was
     * removed from the set. This applies only to ordered sets.
     *
     * @return the index at which the removed MeshObject used to live before it was removed
     * @throws IllegalStateException thrown if applied on an unordered set
     */
    public int getIndexOfRemoved()
    {
        if( theIndexOfRemoved == -1 ) {
            throw new IllegalStateException( "unordered sets do not have indices" );
        }
        return theIndexOfRemoved;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "indexOfRemoved"
                },
                new Object[] {
                    theIndexOfRemoved
                });
    }

    /**
     * The index from which the MeshObject was removed.
     */
    protected int theIndexOfRemoved;
}
