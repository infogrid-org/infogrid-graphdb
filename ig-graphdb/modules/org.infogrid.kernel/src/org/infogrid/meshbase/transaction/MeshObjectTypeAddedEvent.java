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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

import java.util.Map;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.IllegalPropertyValueException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * Event that indicates a MeshObject was blessed with one or more EntityTypes.
 */
public class MeshObjectTypeAddedEvent
        extends
            AbstractMeshObjectTypeChangeEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization
    private static final Log  log              = Log.getLogInstance( MeshObjectTypeAddedEvent.class ); // our own, private logger

    /**
     * Constructor.
     *
     * @param source the MeshObject whose type changed
     * @param oldValues the old set of EntityTypes, prior to the event
     * @param deltaValues the EntityTypes that were added
     * @param newValues the new set of EntityTypes, after the event
     * @param initialProperties if given, set the newly created properties to these values. This is used for undoing MeshObjectTypeRemovedEvents
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectTypeAddedEvent(
            MeshObject                      source,
            EntityType []                   oldValues,
            EntityType []                   deltaValues,
            EntityType []                   newValues,
            Map<PropertyType,PropertyValue> initialProperties,
            long                            timeEventOccurred )
    {
        super(  source,
                source.getIdentifier(),
                oldValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( oldValues ),
                deltaValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( deltaValues ),
                newValues,
                MeshTypeUtils.meshTypeIdentifiersOrNull( newValues ),
                timeEventOccurred,
                source.getMeshBase() );
        
        theInitialProperties = initialProperties;
    }

    /**
     * Constructor for the case where we don't have old and new values, only the delta.
     * This perhaps should trigger some exception if it is attempted to read old or
     * new values later. (FIXME?)
     * 
     * @param sourceIdentifier the identifier for the MeshObject whose type changed
     * @param deltaValues the EntityTypes that were added
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    public MeshObjectTypeAddedEvent(
            MeshObjectIdentifier  sourceIdentifier,
            MeshTypeIdentifier [] deltaValues,
            long                  timeEventOccurred,
            MeshBase              resolver )
    {
        super(  null,
                sourceIdentifier,
                null,
                null,
                null,
                deltaValues,
                null,
                null,
                timeEventOccurred,
                resolver );        
    }

    /**
     * Constructor.
     *
     * @param sourceIdentifier the identifier of the MeshObject whose type changed
     * @param oldValueIdentifiers the identifiers of the old set of EntityTypes, prior to the event
     * @param deltaValueIdentifiers the identifiers of the EntityTypes that were added
     * @param newValueIdentifiers the identifiers of the new set of EntityTypes, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    public MeshObjectTypeAddedEvent(
            MeshObjectIdentifier  sourceIdentifier,
            MeshTypeIdentifier [] oldValueIdentifiers,
            MeshTypeIdentifier [] deltaValueIdentifiers,
            MeshTypeIdentifier [] newValueIdentifiers,
            long                  timeEventOccurred,
            MeshBase              resolver )
    {
        super(  null,
                sourceIdentifier,
                null,
                oldValueIdentifiers,
                null,
                deltaValueIdentifiers,
                null,
                newValueIdentifiers,
                timeEventOccurred,
                resolver );
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
        MeshObject  otherObject = null;
        try {
            tx = base.createTransactionNowIfNeeded();

            otherObject = getSource();

            EntityType [] types = getDeltaValue();
            otherObject.bless( types );
            
            if( theInitialProperties != null ) {
                for( Map.Entry<PropertyType,PropertyValue> current : theInitialProperties.entrySet() ) {
                    otherObject.setPropertyValue( current.getKey(), current.getValue() );
                }
            }
            
            return otherObject;

        } catch( TransactionException ex ) {
            throw ex;

        } catch( IllegalPropertyTypeException ex ) {
            log.error( ex );
            return otherObject;

        } catch( IllegalPropertyValueException ex ) {
            log.error( ex );
            return otherObject;

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
    public MeshObjectTypeRemovedEvent inverse()
    {
        return new MeshObjectTypeRemovedEvent(
                getSource(),
                getNewValue(),
                getDeltaValue(),
                getOldValue(),
                theInitialProperties,
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
        if( !( candidate instanceof MeshObjectTypeRemovedEvent )) {
            return false;
        }
        if( !getAffectedMeshObjectIdentifier().equals( candidate.getAffectedMeshObjectIdentifier())) {
            return false;
        }
        MeshObjectTypeRemovedEvent realCandidate = (MeshObjectTypeRemovedEvent) candidate;

        if( !ArrayHelper.hasSameContentOutOfOrder( getDeltaValueIdentifier(), realCandidate.getDeltaValueIdentifier(), true )) {
            return false;
        }
        // FIXME? compare theInitialProperties

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
        if( !( other instanceof MeshObjectTypeAddedEvent )) {
            return false;
        }
        MeshObjectTypeAddedEvent realOther = (MeshObjectTypeAddedEvent) other;

        if( !super.getSourceIdentifier().equals( realOther.getSourceIdentifier() )) {
            return false;
        }        
        if( !ArrayHelper.hasSameContentOutOfOrder( getDeltaValueIdentifier(), realOther.getDeltaValueIdentifier(), true )) {
            return false;
        }
        if( getTimeEventOccurred() != realOther.getTimeEventOccurred() ) {
            return false;
        }
        // FIXME? compare theInitialProperties
        
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

    /**
     * The properties to set, if any.
     */
    protected Map<PropertyType,PropertyValue> theInitialProperties;
}
