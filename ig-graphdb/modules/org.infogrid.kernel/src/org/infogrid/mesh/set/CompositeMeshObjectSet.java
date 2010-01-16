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

package org.infogrid.mesh.set;

/**
 * A MeshObjectSet that is composed, in some fashion, from other MeshObjectSets.
 * This interface does not specify the way in which the MeshObjectSet is composed.
 */
public interface CompositeMeshObjectSet
        extends
            MeshObjectSet
{
    /**
     * Obtain the operands that were used to construct this set.
     *
     * @return the operands that were used to construct this set
     */
    public abstract MeshObjectSet [] getOperands();
}
