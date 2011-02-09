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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;

/**
 * This indicates that a MeshObject became dead, such as by being deleted.
 */
public class MeshObjectBecameDeadStateEvent
        extends
            MeshObjectStateEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor. This must be invoked with both the MeshObject and the canonical MeshObjectIdentifier,
     * because it is not possible to construct the canonical MeshObjectIdentifier after the MeshObject is dead.
     * 
     * @param theMeshObject the MeshObject whose state changed
     * @param canonicalIdentifier the canonical MeshObjectIdentifier of the MeshObject that became dead
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectBecameDeadStateEvent(
            MeshObject           theMeshObject,
            MeshObjectIdentifier canonicalIdentifier,
            long                 timeEventOccurred )
    {
        super(  theMeshObject,
                canonicalIdentifier,
                Value.ALIVE,
                Value.DEAD,
                timeEventOccurred,
                null ); // FIXME? Is null the right value here?
    }

    /**
     * Resolve a value of the event.
     *
     * @param vid the valid identifier
     * @return a value of the event
     */
    protected MeshObjectState resolveValue(
            String vid )
    {
        if( Value.ALIVE.toString().equals( vid )) {
            return Value.ALIVE;
        } else if( Value.DEAD.toString().equals( vid )) {
            return Value.DEAD;
        } else {
            throw new IllegalArgumentException( "Do not know value " + vid );
        }
    }

    /**
     * The values for the MeshObjectState.
     */
    public static enum Value
            implements
                MeshObjectState
    {
        ALIVE,
        DEAD;
    }

    /**
     * <p>Apply this Change to a MeshObject in this MeshBase. This method
     *    is intended to make it easy to reproduce Changes that were made in
     *    one MeshBase to MeshObjects in another MeshBase.</p>
     *
     * <p>This method will attempt to create a Transaction if none is present on the
     * current Thread.</p>
     *
     * @param base the MeshBase in which to apply the Change
     * @return the MeshObject to which the Change was applied
     * @throws CannotApplyChangeException thrown if the Change could not be applied, e.g because
     *         the affected MeshObject did not exist in MeshBase base
     * @throws TransactionException thrown if a Transaction didn't exist on this Thread and
     *         could not be created
     */
    public MeshObject applyTo(
            MeshBase base )
        throws
            CannotApplyChangeException,
            TransactionException
    {
        setResolver( base );

        Transaction tx = null;
        try {
            tx = base.createTransactionNowIfNeeded();

            MeshObject otherObject = getSource();

            base.getMeshBaseLifecycleManager().deleteMeshObject( otherObject );

            return otherObject;
            
        } catch( TransactionException ex ) {
            throw ex;

        } catch( Throwable ex ) {
            throw new CannotApplyChangeException.ExceptionOccurred( base, ex );
            
        } finally {
            if( tx != null ) {
                tx.commitTransaction();
            }
        }
    }
    
    /**
     * <p>Create a Change that undoes this Change.</p>
     *
     * @return the inverse Change, or null if no inverse Change could be constructed.
     */
    public Change inverse()
    {
        return null;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare with
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( !( other instanceof MeshObjectBecameDeadStateEvent )) {
            return false;
        }
        MeshObjectBecameDeadStateEvent realOther = (MeshObjectBecameDeadStateEvent) other;
        
        if( !getSourceIdentifier().equals( realOther.getSourceIdentifier() )) {
            return false;
        }
        if( !getDeltaValueIdentifier().equals( realOther.getDeltaValueIdentifier() )) {
            return false;
        }
        if( getTimeEventOccurred() != realOther.getTimeEventOccurred() ) {
            return false;
        }
        return true;
    }
}
