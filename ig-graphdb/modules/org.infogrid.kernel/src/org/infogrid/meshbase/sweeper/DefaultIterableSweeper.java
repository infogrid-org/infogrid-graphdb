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
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.IterableMeshBase;

/**
 * Default implementation of Sweeper for IterableMeshBases.
 */
public class DefaultIterableSweeper
        implements
            IterableSweeper
{
    /**
     * Factory method if the Sweeper is only supposed to be invoked manually.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @return the created DefaultIterableSweeper
     */
    public static DefaultIterableSweeper create(
            IterableMeshBase         mb,
            SweepPolicy              policy )
    {
        return new DefaultIterableSweeper( mb, policy, null );
    }

    /**
     * Factory method.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     * @param scheduler the scheduler to use, if any
     * @return the created DefaultIterableSweeper
     */
    public static DefaultIterableSweeper create(
            IterableMeshBase         mb,
            SweepPolicy              policy,
            ScheduledExecutorService scheduler )
    {
        return new DefaultIterableSweeper( mb, policy, scheduler );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param mb the IterableMeshBase on which this Sweeper works
     * @param policy the SweepPolicy to use
     */
    protected DefaultIterableSweeper(
            IterableMeshBase         mb,
            SweepPolicy              policy,
            ScheduledExecutorService scheduler )
    {
        theMeshBase  = mb;
        thePolicy    = policy;
        theScheduler = scheduler;
    }

    /**
     * Set the SweepPolicy.
     *
     * @param newValue the new SweepPolicy
     */
    public void setSweepPolicy(
            SweepPolicy newValue )
    {
        thePolicy = newValue;
    }

    /**
     * Get the SweepPolicy.
     *
     * @return the SweepPolicy
     */
    public SweepPolicy getSweepPolicy()
    {
        return thePolicy;
    }

    /**
     * Determine the IterableMeshBase on which this Sweeper works.
     *
     * @return the IterableMeshBase
     */
    public IterableMeshBase getMeshBase()
    {
        return theMeshBase;
    }

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
            NullPointerException
    {
        theScheduler = scheduleVia;

        scheduleSweepStep();
    }

    /**
     * Stop the background sweeping.
     */
    public void stopBackgroundSweeping()
    {
        IterableSweepStep nextStep = theNextSweepStep;
        if( nextStep == null ) {
            return;
        }
        synchronized( nextStep ) {
            nextStep.cancel();
            theNextSweepStep = null;
        }
    }

    /**
     * Perform a sweep on every single MeshObject in this InterableMeshBase.
     * This may take a long time; using background sweeping is almost always
     * a better alternative.
     */
    public synchronized void sweepAllNow()
    {
        for( MeshObject candidate : theMeshBase ) {
            thePolicy.potentiallyDelete( candidate );
        }
    }

    /**
     * Invoked by the IterableSweepStep, schedule the next IterableSweepStep.
     */
    public void scheduleSweepStep()
    {
        if( theNextSweepStep != null ) {
            theNextSweepStep = theNextSweepStep.nextStep();
        } else {
            theNextSweepStep = IterableSweepStep.create( this );
        }
        theNextSweepStep.scheduleVia( theScheduler );
    }

    /**
     * The IterableMeshBase on which this Sweeper works.
     */
    protected IterableMeshBase theMeshBase;

    /**
     * The policy in effect.
     */
    protected SweepPolicy thePolicy;

    /**
     * The Scheduler for the Sweeper, if any.
     */
    protected ScheduledExecutorService theScheduler;

    /**
     * The next background Sweep task, if any.
     */
    IterableSweepStep theNextSweepStep;
}
