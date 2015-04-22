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

/**
 * A single step taken by an IterableSweeper.
 */
class SweepStep
        implements
            Runnable
{    
    /**
     * Constructor.
     *
     * @param sweeper the IterableSweeper to which this SweepStep belongs
     */
    SweepStep(
            Sweeper sweeper )
    {
        theSweeper = sweeper;
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
        Sweeper s = theSweeper;

        if( s == null ) {
            // sweeper is gone, so stop running
            return;
        }
        s.sweepNextLot();
    }

    /**
     * The Sweeper to which this SweepStep belongs.
     */
    protected Sweeper theSweeper;
}
