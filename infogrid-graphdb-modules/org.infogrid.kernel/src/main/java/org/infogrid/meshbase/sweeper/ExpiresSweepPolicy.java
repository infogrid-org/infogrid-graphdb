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
 * Default SweepPolicy that sweeps all MeshObjects that have exceeded their expires
 * property.
 */
public class ExpiresSweepPolicy
        extends
            AbstractSweepPolicy
{
    /**
     * Factory method.
     * 
     * @return the created ExpiresSweepPolicy
     */
    public static ExpiresSweepPolicy create()
    {
        return new ExpiresSweepPolicy();
    }

    /**
     * Constructor.
     */
    protected ExpiresSweepPolicy()
    {
        // no op
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
        long auto = candidate.getTimeExpires();
        if( auto < 0 ) {
            return false;
        }

        long now = System.currentTimeMillis();
        long delta = now - auto;
        
        if( delta < 0 ) {
            return false;
        }
        return true;
    }
}
