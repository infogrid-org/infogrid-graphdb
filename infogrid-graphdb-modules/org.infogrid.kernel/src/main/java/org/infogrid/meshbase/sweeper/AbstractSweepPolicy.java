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
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.util.logging.Log;

/**
 * Factors out common behaviors of SweepPolicies.
 */
public abstract class AbstractSweepPolicy
        implements
            SweepPolicy
{
    private static final Log log = Log.getLogInstance( AbstractSweepPolicy.class ); // our own, private logger

    /**
     * Constructor.
     */
    protected AbstractSweepPolicy()
    {
        // noop
    }
    
    /**
     * Determine whether this candidate MeshObject should be swept, according
     * to this Sweeper.
     *
     * @param candidate the MeshObject that is a candidate for sweeping
     * @return true of the MeshObject should be swept
     */
    public abstract boolean shouldBeDeleted(
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
            MeshObject candidate )
    {
        if( shouldBeDeleted( candidate )) {
            deleteMeshObject( candidate );
            return null;

        } else {
            return candidate;
        }
    }

    /**
     * Invoked by a background sweep process, the Sweeper will delete the
     * MeshObject if this MeshObject should be swept.
     *
     * @param candidate the MeshObject that is a candidate for sweeping
     * @return true if the MeshObject was swept
     */
    public boolean potentiallyDelete(
            MeshObject candidate )
    {
        if( shouldBeDeleted( candidate )) {
            deleteMeshObject( candidate );
            return true;

        } else {
            return false;
        }
    }

    /**
     * Delete a MeshObject. This is a convenience method.
     *
     * @param toDelete the MeshObject to delete
     */
    protected void deleteMeshObject(
            MeshObject toDelete )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "deleteMeshObject", toDelete.getIdentifier() );
        }

        MeshBase base = toDelete.getMeshBase();

        Transaction tx = null;
        try {
            ThreadIdentityManager.sudo();
            
            tx = base.createTransactionAsapIfNeeded();
            
            base.getMeshBaseLifecycleManager().deleteMeshObject( toDelete );

        } catch( TransactionException ex ) {
            log.error( ex );
            
        } catch( NotPermittedException ex ) {
            log.error( ex );
            
        } finally {
            if( tx != null ) {
                tx.commitTransaction();
            }
            ThreadIdentityManager.sudone();
        }
    }
}
