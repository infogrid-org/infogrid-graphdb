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

package org.infogrid.meshbase.transaction;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.AbstractMeshBase;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * The default implementation of a Transaction for subclasses of AbstractMeshBase.
 */
public class DefaultTransaction
        extends
            Transaction
{
    protected static final Log log = Log.getLogInstance( DefaultTransaction.class ); // our own, private logger

    /**
     * Constructor. Transactions should only be created by the
     * corresponding MeshBase.
     * 
     * @param rep the Aidentifiersthat created this Transaction, and which is
     *            guarded by this Transaction
     */
    public DefaultTransaction(
            AbstractMeshBase rep )
    {
        super( rep );
    }

    /**
      * Commit a started Transaction.
      */
    @Override
    public synchronized void commitTransaction()
    {
        super.commitTransaction();
        ((AbstractMeshBase)theTransactable).transactionCommitted();
    }

    /**
     * Roll back all changes performed within this Transaction so far.
     *
     * @param thrown the Throwable that caused us to attempt to rollback the Transaction
     */
    @Override
    public synchronized void rollbackTransaction(
            Throwable thrown )
    {
        super.rollbackTransaction( thrown );
        ((AbstractMeshBase)theTransactable).transactionRolledback( thrown );
    }

    /**
     * This hook is invoked just prior to committing the Transaction. This allows subclasses to hook
     * in before the commit actually happens.
     */
    @Override
    protected void preCommitHook()
    {
        // we don't need to do a synchronized, our invoker does this.

        // this is a while loop, because these updates may add additional dirty MeshObjects

        if( theDirtyRecords != null ) {
            int attemptsLeft = MAX_RECALCULATE_ATTEMPTS;

            while( true ) {
                int                    numberDirty         = theDirtyRecords.size();
                ArrayList<DirtyRecord> currentDirtyRecords = theDirtyRecords;

                theDirtyRecords = null;

                Iterator<DirtyRecord> theIter = currentDirtyRecords.iterator();
                while( theIter.hasNext() ) {
                    DirtyRecord current = theIter.next();

                    if( current.theDirtyMeshObject.getIsDead()) {
                        continue;
                    }

                    current.theProjectionWatcher.update( current.theEvent );
                }

                if( theDirtyRecords == null ) {
                    break;
                }

                if( theDirtyRecords.size() < numberDirty ) {
                    attemptsLeft = MAX_RECALCULATE_ATTEMPTS;
                } else {
                    --attemptsLeft;
                    if( attemptsLeft == 0 ) {
                        log.error( "circular agent recalculation loop: " + ArrayHelper.collectionToString( theDirtyRecords ));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Memorize that an MeshObject is now "dirty" because of not having performed a
     * projected property update yet, and that it should be updated before this
     * Transaction is finished committing.
     *
     * @param newDirtyMeshObject the dirty MeshObject
     * @param howToUpdate code to invoke to update the projected property
     * @param event the event that caused the need for an update
     */
    public synchronized void addDirtyMeshObject(
            MeshObject               newDirtyMeshObject,
            ProjectedPropertyUpdater howToUpdate,
            EventObject              event )
    {
        if( theDirtyRecords == null ) {
            theDirtyRecords = new ArrayList<DirtyRecord>();
        }

        DirtyRecord newRecord = new DirtyRecord( newDirtyMeshObject, howToUpdate, event );

        if( !theDirtyRecords.contains( newRecord )) {
            theDirtyRecords.add( newRecord );
        }
    }


    /**
     * The set of MeshObjects (plus supporting info) which may now be dirty as a result
     * of this Transaction. Allocated as needed.
     */
    protected ArrayList<DirtyRecord> theDirtyRecords = null;

    /**
     * This class collects the MeshObject of which one ProjectedProperty needs to
     * be recalculated, plus supporting data.
     */
    protected static class DirtyRecord
    {
        /**
         * Construct one.
         *
         * @param dirtyMeshObject the dirty MeshObject
         * @param callback the ProjectedPropertyUpdater to invoke to update the Entity
         * @param event the event that caused the need for an update
         */
        public DirtyRecord(
                MeshObject               dirtyMeshObject,
                ProjectedPropertyUpdater callback,
                EventObject              event )
        {
            theDirtyMeshObject   = dirtyMeshObject;
            theProjectionWatcher = callback;
            theEvent             = event;
        }

        /**
         * Determine equality.
         *
         * @param other the Object to test against
         * @return true if this is equal to other
         */
        @Override
        public boolean equals(
                Object other )
        {
            if( other instanceof DirtyRecord ) {
                DirtyRecord realOther = (DirtyRecord) other;

                if( !theDirtyMeshObject.equals( realOther.theDirtyMeshObject )) {
                    return false;
                }
                if( !theProjectionWatcher.equals( realOther.theProjectionWatcher )) {
                    return false;
                }
                if( !theEvent.equals( realOther.theEvent )) {
                    return false;
                }
                return true;
            }
            return false;
        }

        /**
         * Hash code.
         * 
         * @return hash code
         */
        @Override
        public int hashCode()
        {
            // generated by NetBeans
            int hash = 7;
            hash = 29 * hash + (theDirtyMeshObject   != null ? theDirtyMeshObject.hashCode() : 0);
            hash = 29 * hash + (theProjectionWatcher != null ? theProjectionWatcher.hashCode() : 0);
            hash = 29 * hash + (theEvent             != null ? theEvent.hashCode() : 0);
            return hash;
        }

        /**
         * The dirty MeshObject.
         */
        MeshObject theDirtyMeshObject;
        
        /**
         * The code that will update the projected property.
         */
        ProjectedPropertyUpdater theProjectionWatcher;
        
        /**
         * The event that caused this DirtyRecord to come into existence.
         */
        EventObject theEvent;
    }
}
