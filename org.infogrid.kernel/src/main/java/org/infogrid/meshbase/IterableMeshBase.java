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

package org.infogrid.meshbase;

import org.infogrid.mesh.MeshObject;
import org.infogrid.util.CursorIterable;
import org.infogrid.util.CursorIterator;

/**
 * A MeshBase that supports iterating over the MeshObjects it contains.
 * Not all MeshBases are IterableMeshBases, because their content may be distributed
 * widely, which makes iteration impractical. For those that are, this interface is provided.
 */
public interface IterableMeshBase
        extends
            MeshBase,
            CursorIterable<MeshObject>
{
    /**
     * Returns a CursorIterator over the content of this MeshBase.
     * 
     * @return a CursorIterator.
     */
    public CursorIterator<MeshObject> iterator();

    /**
     * Determine the number of MeshObjects in this MeshBase.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #getSize()
     */
    public abstract int size();

    /**
     * Determine the number of MeshObjects in this MeshBase. This redundant method
     * is provided to make life easier for JavaBeans-aware software.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #size()
     */
    public abstract int getSize();

    /**
     * Factory method for a IterableMeshBaseDifferencer, with this IterableMeshBase
     * being the comparison base.
     *
     * @return the IterableMeshBaseDifferencer
     */
    public abstract IterableMeshBaseDifferencer getDifferencer();
}
