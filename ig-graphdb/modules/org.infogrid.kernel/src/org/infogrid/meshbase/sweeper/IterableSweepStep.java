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
import java.util.concurrent.TimeUnit;
import org.infogrid.mesh.MeshObject;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.ResourceHelper;

/**
 * A single step taken by an IterableSweeper.
 */
class IterableSweepStep
        implements
            Runnable
{    
    /**
     * Factory method for the first step.
     *
     * @param sweeper the IterableSweeper to which this IterableSweepStep belongs
     * @return the created IterableSweepStep
     */
    public static IterableSweepStep create(
            IterableSweeper sweeper )
    {
        long delay = theResourceHelper.getResourceLongOrDefault( "Delay", 1000L );
        return new IterableSweepStep( sweeper, null, delay );
    }

    /**
     * Constructor.
     *
     * @param sweeper the IterableSweeper to which this IterableSweepStep belongs
     * @param iter the current position in the IterableMeshBase
     * @param delay the delay between steps
     */
    protected IterableSweepStep(
            IterableSweeper            sweeper,
            CursorIterator<MeshObject> iter,
            long                       delay )
    {
        theSweeper  = sweeper;
        theIterator = iter;
        theDelay    = delay;
    }
    
    /**
     * Factory method for each subsequent step.
     *
     * @return the created IterableSweepStep
     */
    public IterableSweepStep nextStep()
    {
        IterableSweepStep ret = new IterableSweepStep( theSweeper, theIterator, theDelay );

        return ret;
    }

    /**
     *
     */
    public void scheduleVia(
            ScheduledExecutorService exec )
    {
        exec.schedule( this, theDelay, TimeUnit.MILLISECONDS );
    }

    /**
     * Cancel this step.
     */
    public void cancel()
    {
        theSweeper = null;
    }

    /**
     * Run this step.
     */
    public void run()
    {
        if( theSweeper == null ) {
            // sweeper is gone, so stop running
            return;
        }
        
        if( theIterator == null || !theIterator.hasNext() ) {
            theIterator = theSweeper.getMeshBase().iterator();
        }

        MeshObject  current = theIterator.next();
        SweepPolicy policy  = theSweeper.getSweepPolicy();
        
        policy.potentiallyDelete( current );
        theSweeper.scheduleSweepStep();
    }

    /**
     * The Sweeper to which this IterableSweepStep belongs.
     */
    protected IterableSweeper theSweeper;

    /**
     * The Iterator reflecting the current state of the iteration over all MeshObjects.
     */
    protected CursorIterator<MeshObject> theIterator;
    
    /**
     * Our ResourceHelper.
     */
    protected static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( IterableSweepStep.class );
    
    /**
     * The delay between invocations. This is an instance variable.
     */
    protected long theDelay;
}
