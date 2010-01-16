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

package org.infogrid.meshbase.sweeper;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.Sweeper;

import org.infogrid.util.CursorIterator;
import org.infogrid.util.ResourceHelper;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A single step taken by the background Sweeper
 */
public class SweepStep
        implements
            Runnable
{    
    /**
     * Factory method for the first step.
     *
     * @param base the IterableMeshBase on which this SweepStep runs
     * @return the created SweepStep
     */
    public static SweepStep create(
            IterableMeshBase base )
    {
        long delay = theResourceHelper.getResourceLongOrDefault( "Delay", 1000L );
        return new SweepStep( base, null, delay );
    }

    /**
     * Factory method for each subsequent step.
     *
     * @return the created SweepStep
     */
    public SweepStep nextStep()
    {
        SweepStep ret = new SweepStep( theMeshBase, theIterator, theDelay );
        
        return ret;
    }
    
    /**
     * Constructor.
     *
     * @param base the IterableMeshBase on which this SweepStep runs
     * @param iter the current position in the IterableMeshBase
     */
    protected SweepStep(
            IterableMeshBase           base,
            CursorIterator<MeshObject> iter,
            long                       delay )
    {
        theMeshBase = base;
        theIterator = iter;
        theDelay    = delay;
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
        theMeshBase = null;
    }

    /**
     * Run this step.
     */
    public void run()
    {
        IterableMeshBase meshBase = theMeshBase;
        
        if( meshBase == null ) {
            // was canceled
            return;
        }
        
        Sweeper sweep = meshBase.getSweeper();
        if( sweep == null ) {
            // sweeper is gone, so stop running
            return;
        }
        
        if( theIterator == null || !theIterator.hasNext() ) {
            theIterator = theMeshBase.iterator();
        }

        MeshObject current = theIterator.next();
        
        sweep.potentiallyDelete( current );
        
        meshBase.scheduleSweepStep();
    }
    
    /**
     * The IterableMeshBase on which to run this SweepStep.
     */
    protected IterableMeshBase theMeshBase;
    
    /**
     * The Iterator reflecting the current state of the iteration over all MeshObjects.
     */
    protected CursorIterator<MeshObject> theIterator;
    
    /**
     * Our ResourceHelper.
     */
    protected static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( SweepStep.class );
    
    /**
     * The delay between invocations. This is an instance variable.
     */
    protected long theDelay;
    
}
