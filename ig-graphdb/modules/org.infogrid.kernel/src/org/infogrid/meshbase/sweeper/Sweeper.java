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

import java.util.concurrent.ScheduledExecutorService;
import org.infogrid.meshbase.MeshBase;

/**
 * Responsible for sweeping the content of a MeshBase according to its
 * own preferences. For examples, a Sweeper may decide to remove all
 * MeshObjects that have not been read in one year.
 */
public interface Sweeper
{
    /**
     * Determine the MeshBase on which this Sweeper works.
     *
     * @return the MeshBase
     */
    public MeshBase getMeshBase();

    /**
     * Set the SweepPolicy.
     * 
     * @param newValue the new SweepPolicy
     */
    public void setSweepPolicy(
            SweepPolicy newValue );
    
    /**
     * Get the SweepPolicy.
     * 
     * @return the SweepPolicy
     */
    public SweepPolicy getSweepPolicy();

    /**
     * Continually sweep this IterableMeshBase in the background, according to
     * the configured Sweeper.
     *
     * @param scheduleVia the ScheduledExecutorService to use for scheduling
     * @throws NullPointerException thrown if no Sweeper has been set
     */
    public void startBackgroundSweeping(
            ScheduledExecutorService scheduleVia )
        throws
            NullPointerException;

    /**
     * Stop the background sweeping.
     */
    public void stopBackgroundSweeping();

    /**
     * Perform a sweep on every single MeshObject in this InterableMeshBase.
     * This may take a long time; using background sweeping is almost always
     * a better alternative.
     */
    public void sweepAllNow();

    /**
     * Invoked by the SweepStep, schedule the next SweepStep.
     */
    public void scheduleSweepStep();
}
