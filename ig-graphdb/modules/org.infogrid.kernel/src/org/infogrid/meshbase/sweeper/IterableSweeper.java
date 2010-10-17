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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.sweeper;

import org.infogrid.meshbase.IterableMeshBase;

/**
 * A Sweeper over an IterableMeshBase. This is the most-frequent case.
 */
public interface IterableSweeper
        extends
            Sweeper
{
    /**
     * Determine the IterableMeshBase on which this Sweeper works.
     *
     * @return the IterableMeshBase
     */
    public IterableMeshBase getMeshBase();
}
