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

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.ArrayHelper;

/**
 * <p>This event indicates that a relationship between the MeshObject and
 * another MeshObject was blessed.</p>
 */
public class MeshObjectRoleAddedEvent
        extends
            AbstractMeshObjectRoleChangeEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param source the MeshObject that is the source of the event
     * @param oldValues the old values of the RoleType, prior to the event
     * @param deltaValues the RoleTypes that changed
     * @param newValues the new values of the RoleType, after the event
     * @param neighborIdentifier identifier of the MeshObject that identifies the other end of the affected relationship
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectRoleAddedEvent(
            MeshObject           source,
            RoleType []          oldValues,
            RoleType []          deltaValues,
            RoleType []          newValues,
            MeshObjectIdentifier neighborIdentifier,
            long                 timeEventOccurred )
    {
        this(   source,
                source.getIdentifier(),
                oldValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( oldValues ),
                deltaValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( deltaValues ),
                newValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( newValues ),
                null,
                neighborIdentifier,
                timeEventOccurred,
                source.getMeshBase() );
    }

    /**
     * Constructor.
     * 
     * @param source the MeshObject that is the source of the event
     * @param oldValues the old values of the RoleType, prior to the event
     * @param deltaValues the RoleTypes that changed
     * @param newValues the new values of the RoleType, after the event
     * @param neighbor the MeshObject that identifies the other end of the affected relationship
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectRoleAddedEvent(
            MeshObject        source,
            RoleType []       oldValues,
            RoleType []       deltaValues,
            RoleType []       newValues,
            MeshObject        neighbor,
            long              timeEventOccurred )
    {
        this(   source,
                source.getIdentifier(),
                oldValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( oldValues ),
                deltaValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( deltaValues ),
                newValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( newValues ),
                neighbor,
                neighbor.getIdentifier(),
                timeEventOccurred,
                source.getMeshBase() );
    }

    /**
     * Constructor.
     * 
     * @param sourceIdentifier the identifier representing the source MeshObject of the event
     * @param oldValueIdentifiers the identifier representing the old values of the RoleType, prior to the event
     * @param deltaValueIdentifiers the identifiers of the RoleTypes that changed
     * @param newValueIdentifiers the identifier representing the new values of the RoleType, after the event
     * @param neighborIdentifier the identifier representing the MeshObject that identifies the other end of the affected relationship
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    public MeshObjectRoleAddedEvent(
            MeshObjectIdentifier  sourceIdentifier,
            MeshTypeIdentifier [] oldValueIdentifiers,
            MeshTypeIdentifier [] deltaValueIdentifiers,
            MeshTypeIdentifier [] newValueIdentifiers,
            MeshObjectIdentifier  neighborIdentifier,
            long                  timeEventOccurred,
            MeshBase              resolver )
    {
        this(   null,
                sourceIdentifier,
                null,
                oldValueIdentifiers,
                null,
                deltaValueIdentifiers,
                null,
                newValueIdentifiers,
                null,
                neighborIdentifier,
                timeEventOccurred,
                resolver );
    }

    /**
     * Pass-through constructor for subclasses.
     * 
     * @param source the MeshObject that is the source of the event
     * @param sourceIdentifier the identifier representing the source MeshObject of the event
     * @param oldValues the old values of the RoleType, prior to the event
     * @param oldValueIdentifiers the identifier representing the old values of the RoleType, prior to the event
     * @param deltaValues the RoleTypes that changed
     * @param deltaValueIdentifiers the identifiers of the RoleTypes that changed
     * @param newValues the new values of the RoleType, after the event
     * @param newValueIdentifiers the identifier representing the new values of the RoleType, after the event
     * @param neighbor the MeshObject that identifies the other end of the affected relationship
     * @param neighborIdentifier the identifier representing the MeshObject that identifies the other end of the affected relationship
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    protected MeshObjectRoleAddedEvent(
            MeshObject            source,
            MeshObjectIdentifier  sourceIdentifier,
            RoleType []           oldValues,
            MeshTypeIdentifier [] oldValueIdentifiers,
            RoleType []           deltaValues,
            MeshTypeIdentifier [] deltaValueIdentifiers,
            RoleType []           newValues,
            MeshTypeIdentifier [] newValueIdentifiers,
            MeshObject            neighbor,
            MeshObjectIdentifier  neighborIdentifier,
            long                  timeEventOccurred,
            MeshBase              resolver )
    {
        super(  source,
                sourceIdentifier,
                oldValues,
                oldValueIdentifiers,
                deltaValues,
                deltaValueIdentifiers,
                newValues,
                newValueIdentifiers,
                neighbor,
                neighborIdentifier,
                timeEventOccurred,
                resolver );
    }
    
    /**
     * Determine whether this is an addition or a removal of a RoleType.
     *
     * @return true if this is an addition
     */
    public boolean isAdditionalRoleUpdate()
    {
        return true;
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

            MeshObject  otherObject        = getSource();
            MeshObject  relatedOtherObject = getNeighborMeshObject();
            RoleType [] roleTypes          = getDeltaValue();

            otherObject.blessRelationship( roleTypes, relatedOtherObject );

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
    public MeshObjectRoleRemovedEvent inverse()
    {
        return new MeshObjectRoleRemovedEvent(
                getSource(),
                getNewValue(),
                getDeltaValue(),
                getOldValue(),
                getNeighborMeshObject(),
                getTimeEventOccurred() );
    }

    /**
     * Determine whether a given Change is the inverse of this Change.
     *
     * @param candidate the candidate Change
     * @return true if the candidate Change is the inverse of this Change
     */
    public boolean isInverse(
            Change candidate )
    {
        if( !( candidate instanceof MeshObjectRoleRemovedEvent )) {
            return false;
        }
        if( !getAffectedMeshObjectIdentifier().equals( candidate.getAffectedMeshObjectIdentifier())) {
            return false;
        }
        if( !getDeltaValueIdentifier().equals( candidate.getDeltaValueIdentifier())) {
            return false;
        }
        MeshObjectRoleRemovedEvent realCandidate = (MeshObjectRoleRemovedEvent) candidate;

        if( !getNeighborMeshObjectIdentifier().equals( realCandidate.getNeighborMeshObjectIdentifier())) {
            return false;
        }
        return true;
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
        if( !( other instanceof MeshObjectRoleAddedEvent )) {
            return false;
        }
        MeshObjectRoleAddedEvent realOther = (MeshObjectRoleAddedEvent) other;
        
        if( !getSourceIdentifier().equals( realOther.getSourceIdentifier() )) {
            return false;
        }
        if( !theNeighborIdentifier.equals( realOther.theNeighborIdentifier )) {
            return false;
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getDeltaValueIdentifier(), realOther.getDeltaValueIdentifier(), true )) {
            return false;
        }
        if( getTimeEventOccurred() != realOther.getTimeEventOccurred() ) {
            return false;
        }
        return true;
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return getSourceIdentifier().hashCode();
    }
}
