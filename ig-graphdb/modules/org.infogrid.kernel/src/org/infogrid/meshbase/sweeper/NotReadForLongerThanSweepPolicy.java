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

import org.infogrid.mesh.MeshObject;

/**
 * A SweepPolicy that sweeps those MeshObjects that have not been read for long
 * than a configurable duration.
 */
public class NotReadForLongerThanSweepPolicy
        extends
            AbstractSweepPolicy
{
    /**
     * Factory method.
     *
     * @param duration the duration, in milliseconds
     * @return the created NotReadForMoreThanSweeper
     */
    public static NotReadForLongerThanSweepPolicy create(
            long duration )
    {
        return new NotReadForLongerThanSweepPolicy( duration );
    }

    /**
     * Constructor.
     *
     * @param duration the duration, in milliseconds
     */
    protected NotReadForLongerThanSweepPolicy(
            long duration )
    {
        theDuration = duration;
    }
    
    /**
     * Determines whether or not a given MeshObject should be swept.
     *
     * @param candidate the MeshObject that is a candidate for sweeping
     * @return true if the MeshObject should be swept.
     */
    public boolean shouldBeDeleted(
            MeshObject candidate )
    {
        long now   = System.currentTimeMillis();
        long read  = candidate.getTimeRead();
        
        if( read < 0 ) {
            read = candidate.getTimeCreated();
        }
        long delta = now - read;
        
        if( delta < theDuration ) {
            return false;
        }

        return true;
    }

    /**
     * The duration.
     */
    protected long theDuration;
}
