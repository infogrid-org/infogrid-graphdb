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
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.util.logging.Log;

/**
 * This event indicates that a MeshObject was semantically deleted.
 */
public class MeshObjectDeletedEvent
        extends
            AbstractMeshObjectLifecycleEvent
{
    private static final long serialVersionUID = 1l; // helps with serialization
    private static final Log  log              = Log.getLogInstance( MeshObjectDeletedEvent.class ); // our own, private logger

    /**
     * Constructor.
     * 
     * @param source the MeshBase that is the source of the event
     * @param sourceIdentifier the MeshBaseIdentifier representing the source of the event
     * @param deltaValue the MeshObject whose lifecycle changed
     * @param deltaValueIdentifier the MeshObjectIdentifier whose lifecycle changed
     * @param externalized the deleted MeshObject in externalized form as it was just prior to deletion
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectDeletedEvent(
            MeshBase               source,
            MeshBaseIdentifier     sourceIdentifier,
            MeshObject             deltaValue,
            MeshObjectIdentifier   deltaValueIdentifier,
            ExternalizedMeshObject externalized,
            long                   timeEventOccurred )
    {
        super(  source,
                sourceIdentifier,
                deltaValue,
                deltaValueIdentifier,
                timeEventOccurred );
        
        theExternalizedMeshObject = externalized;
    }

    /**
     * Obtain an externalized representation of the MeshObject as it was just before it
     * was deleted.
     * 
     * @return externalized form of the deleted MeshObject
     */
    public ExternalizedMeshObject getExternalizedMeshObject()
    {
        return theExternalizedMeshObject;
    }

    /**
     * Obtain the EntityTypes with which the created MeshObject was blessed upon creation.
     *
     * @return the MeshTypes
     */
    public synchronized EntityType [] getEntityTypes()
    {
        if( theEntityTypes == null ) {
            theEntityTypes = MeshObjectCreatedEvent.resolveEntityTypes( this, theExternalizedMeshObject, theResolver );
        }
        return theEntityTypes;
    }

    /**
     * <p>Apply this Change to a MeshObject in this MeshBase. This method
     *    is intended to make it easy to reproduce Changes that were made in
     *    one MeshBase to MeshObjects in another MeshBase.</p>
     *
     * <p>This method will attempt to create a Transaction if none is present on the
     *    current Thread.</p>
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

            MeshObject otherObject = getDeltaValue();

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
    public MeshObjectCreatedEvent inverse()
    {
        return new MeshObjectCreatedEvent(
                getSource(),
                getSourceIdentifier(),
                theExternalizedMeshObject,
                getTimeEventOccurred() );
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
        if( !( other instanceof MeshObjectDeletedEvent )) {
            return false;
        }
        MeshObjectDeletedEvent realOther = (MeshObjectDeletedEvent) other;
        
        if( !getDeltaValueIdentifier().equals( realOther.getDeltaValueIdentifier() )) {
            return false;
        }
        if( getTimeEventOccurred() != realOther.getTimeEventOccurred() ) {
            return false;
        }
        return true;
    }

    /**
     * Determine hash code.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return getAffectedMeshObjectIdentifier().hashCode();
    }
    
    /**
     * The deleted MeshObject in externalized form, as it was just before the deletion.
     */
    protected ExternalizedMeshObject theExternalizedMeshObject;

    /**
     * The EntityTypes, once resolved.
     */
    protected transient EntityType [] theEntityTypes;
}
