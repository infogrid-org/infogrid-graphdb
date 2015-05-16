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

package org.infogrid.meshbase.sweeper;

import org.infogrid.mesh.MeshObject;

/**
 * Defines the policy for sweeping the content of a MeshBase. For examples,
 * a SweepPolicy may be to delete all MeshObjects that have not been read in one year.
 */
public interface SweepPolicy
{
    /**
     * Determine whether this candidate MeshObject should be swept, according
     * to this Sweeper.
     *
     * @param candidate the MeshObject that is a candidate for sweeping
     * @return true of the MeshObject should be swept
     */
    public boolean shouldBeDeleted(
            MeshObject candidate );

    /**
     * Enable a MeshBase to filter this candidate MeshObject. If the filter lets
     * pass the MeshObject, this MeshObject will returned to a MeshBase client.
     * If the filter returns null, this is what the MeshBase client will obtain;
     * simultaneously, the Sweeper may delete the MeshObject
     *
     * @param candidate the candidate MeshObject
     * @return the MeshObject, or null if filtered
     */
    public MeshObject potentiallyFilter(
            MeshObject candidate );

    /**
     * Invoked by a background sweep process, the Sweeper will delete the
     * MeshObject if this MeshObject should be swept.
     *
     * @param candidate the MeshObject that is a candidate for sweeping
     * @return true if the MeshObject was swept
     */
    public boolean potentiallyDelete(
            MeshObject candidate );
}
