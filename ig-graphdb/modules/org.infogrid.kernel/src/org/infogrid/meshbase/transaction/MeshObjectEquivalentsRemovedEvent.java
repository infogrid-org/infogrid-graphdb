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
import org.infogrid.mesh.MeshObjectUtils;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.ArrayHelper;

/**
  * <p>This event indicates that a MeshObject has lost one or more equivalent MeshObjects.</p>
  */
public class MeshObjectEquivalentsRemovedEvent
        extends
            AbstractMeshObjectEquivalentsChangeEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param source the MeshObject that is the source of the event
     * @param oldValues the old values of the equivalents, prior to the event
     * @param deltaValues the equivalents that changed
     * @param newValues the new value of the equivalents, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectEquivalentsRemovedEvent(
            MeshObject    source,
            MeshObject [] oldValues,
            MeshObject [] deltaValues,
            MeshObject [] newValues,
            long          timeEventOccurred )
    {
        this(   source,
                source.getIdentifier(),
                oldValues,
                MeshObjectUtils.meshObjectIdentifiers( oldValues ),
                deltaValues,
                MeshObjectUtils.meshObjectIdentifiers( deltaValues ),
                newValues,
                MeshObjectUtils.meshObjectIdentifiers( newValues ),
                timeEventOccurred,
                source.getMeshBase() );
    }

    /**
     * Constructor.
     * 
     * @param source the MeshObject that is the source of the event
     * @param oldValues the old values of the equivalents, prior to the event
     * @param deltaValues the equivalents that changed
     * @param newValues the new value of the equivalents, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectEquivalentsRemovedEvent(
            MeshObject              source,
            MeshObjectIdentifier [] oldValues,
            MeshObjectIdentifier [] deltaValues,
            MeshObjectIdentifier [] newValues,
            long                    timeEventOccurred )
    {
        this(   source,
                source.getIdentifier(),
                null,
                oldValues,
                null,
                deltaValues,
                null,
                newValues,
                timeEventOccurred,
                source.getMeshBase() );
    }

    /**
     * Main constructor.
     * 
     * @param source the MeshObject that is the source of the event
     * @param sourceIdentifier the identifier representing the source MeshObject of the event
     * @param oldValues the old values of the equivalents, prior to the event
     * @param oldValueIdentifiers the identifiers representing the old values of the equivalents, prior to the event
     * @param deltaValues the equivalents that changed
     * @param deltaValueIdentifiers the identifiers of the equivalents that changed
     * @param newValues the new value of the equivalents, after the event
     * @param newValueIdentifiers the identifiers representing the new values of the equivalents, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    protected MeshObjectEquivalentsRemovedEvent(
            MeshObject              source,
            MeshObjectIdentifier    sourceIdentifier,
            MeshObject []           oldValues,
            MeshObjectIdentifier [] oldValueIdentifiers,
            MeshObject []           deltaValues,
            MeshObjectIdentifier [] deltaValueIdentifiers,
            MeshObject []           newValues,
            MeshObjectIdentifier [] newValueIdentifiers,
            long                    timeEventOccurred,
            MeshBase                resolver )
    {
        super(  source,
                sourceIdentifier,
                oldValues,
                oldValueIdentifiers,
                deltaValues,
                deltaValueIdentifiers,
                newValues,
                newValueIdentifiers,
                timeEventOccurred,
                resolver );
    }
    
    /**
     * Determine whether this is an addition or a removal of an equivalent.
     *
     * @return true if this is an addition
     */
    public boolean isAdditionalEquivalentsUpdate()
    {
        return false;
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

            MeshObject s = getSource();

            s.removeAsEquivalent();

            return s;

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
    public MeshObjectEquivalentsAddedEvent inverse()
    {
        return new MeshObjectEquivalentsAddedEvent(
                getSource(),
                getNewValue(),
                getDeltaValue(),
                getOldValue(),
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
        if( !( other instanceof MeshObjectEquivalentsRemovedEvent )) {
            return false;
        }
        MeshObjectEquivalentsRemovedEvent realOther = (MeshObjectEquivalentsRemovedEvent) other;

        if( !getSourceIdentifier().equals( realOther.getSourceIdentifier() )) {
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
