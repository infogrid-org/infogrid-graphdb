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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.event.AbstractExternalizablePropertyChangeEvent;
import org.infogrid.util.event.PropertyUnresolvedException;

/**
  * <p>This event indicates that one of a MeshObject's properties has changed its value.</p>
  */
public class MeshObjectPropertyChangeEvent
        extends
            AbstractExternalizablePropertyChangeEvent<MeshObject,MeshObjectIdentifier,PropertyType,MeshTypeIdentifier,PropertyValue,PropertyValue>
        implements
            Change<MeshObject,MeshObjectIdentifier,PropertyValue,PropertyValue>
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param source the MeshObject that is the source of the event
     * @param property an object representing the property of the event
     * @param oldValue the old value of the property, prior to the event
     * @param newValue the new value of the property, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectPropertyChangeEvent(
            MeshObject     source,
            PropertyType   property,
            PropertyValue  oldValue,
            PropertyValue  newValue,
            long           timeEventOccurred )
    {
        this(   source,
                source.getIdentifier(),
                property,
                property.getIdentifier(),
                oldValue,
                oldValue,
                newValue, // delta = new
                newValue,
                newValue,
                newValue,
                timeEventOccurred,
                source.getMeshBase() );
    }

    /**
     * Constructor for the case where we don't have an old value, only the new value.
     * This perhaps should trigger some exception if it is attempted to read old 
     * values later. (FIXME?)
     * 
     * @param sourceIdentifier the identifier of the MeshObject that is the source of the event
     * @param propertyIdentifier the identifier of an object representing the property of the event
     * @param newValue the new value of the property, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    public MeshObjectPropertyChangeEvent(
            MeshObjectIdentifier sourceIdentifier,
            MeshTypeIdentifier   propertyIdentifier,
            PropertyValue        newValue,
            long                 timeEventOccurred,
            MeshBase             resolver )
    {
        this(   null,
                sourceIdentifier,
                null,
                propertyIdentifier,
                null,
                null,
                null,
                null,
                newValue,
                newValue,
                timeEventOccurred,
                resolver );
    }
    
    /**
     * Constructor.
     *
     * @param sourceIdentifier the identifier of the MeshObject that is the source of the event
     * @param propertyIdentifier the identifier of an object representing the property of the event
     * @param oldValue the old value of the property, prior to the event
     * @param newValue the new value of the property, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    public MeshObjectPropertyChangeEvent(
            MeshObjectIdentifier sourceIdentifier,
            MeshTypeIdentifier   propertyIdentifier,
            PropertyValue        oldValue,
            PropertyValue        newValue,
            long                 timeEventOccurred,
            MeshBase             resolver )
    {
        this(  (MeshObject) null,
                sourceIdentifier,
                (PropertyType) null,
                propertyIdentifier,
                oldValue,
                oldValue,
                newValue, // delta = new
                newValue,
                newValue,
                newValue,
                timeEventOccurred,
                resolver );
    }

    /**
     * Constructor.
     * 
     * @param source the object that is the source of the event
     * @param sourceIdentifier the identifier representing the source of the event
     * @param property an object representing the property of the event
     * @param propertyIdentifier the identifier representing the property of the event
     * @param oldValue the old value of the property, prior to the event
     * @param oldValueIdentifier the identifier representing the old value of the property, prior to the event
     * @param deltaValue the value that changed
     * @param deltaValueIdentifier the identifier of the value that changed
     * @param newValue the new value of the property, after the event
     * @param newValueIdentifier the identifier representing the new value of the property, after the event
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     * @param resolver the MeshBase against which the MeshObjectIdentifiers are currently resolved, if any
     */
    protected MeshObjectPropertyChangeEvent(
            MeshObject           source,
            MeshObjectIdentifier sourceIdentifier,
            PropertyType         property,
            MeshTypeIdentifier   propertyIdentifier,
            PropertyValue        oldValue,
            PropertyValue        oldValueIdentifier,
            PropertyValue        deltaValue,
            PropertyValue        deltaValueIdentifier,
            PropertyValue        newValue,
            PropertyValue        newValueIdentifier,
            long                 timeEventOccurred,
            MeshBase             resolver )
    {
        super(  source,
                sourceIdentifier,
                property,
                propertyIdentifier,
                oldValue,
                oldValueIdentifier,
                deltaValue,
                deltaValueIdentifier,
                newValue,
                newValueIdentifier,
                timeEventOccurred );
        
        theResolver = resolver;
    }

    /**
     * Obtain the Identifier of the MeshObject affected by this Change.
     *
     * @return the Identifier of the MeshObject affected by this Change
     */
    public MeshObjectIdentifier getAffectedMeshObjectIdentifier()
    {
        return getSourceIdentifier();
    }

    /**
     * Obtain the MeshObject affected by this Change.
     *
     * @return obtain the MeshObject affected by this Change
     */
    public MeshObject getAffectedMeshObject()
    {
        return getSource();
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

            PropertyType  affectedProperty = getProperty();
            PropertyValue newValue         = getNewValue();
            long          updateTime       = getTimeEventOccurred();

            otherObject.setPropertyValue(
                    affectedProperty,
                    newValue,
                    updateTime );

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
     * <p>Assuming that this Change was applied to a MeshObject in this MeshBase before,
     *    unapply (undo) this Change.
     * <p>This method will attempt to create a Transaction if none is present on the
     * current Thread.</p>
     *
     * @param base the MeshBase in which to unapply the Change
     * @return the MeshObject to which the Change was unapplied
     * @throws CannotUnapplyChangeException thrown if the Change could not be unapplied
     * @throws TransactionException thrown if a Transaction didn't exist on this Thread and
     *         could not be created
     */
    public MeshObject unapplyFrom(
            MeshBase base )
        throws
            CannotUnapplyChangeException,
            TransactionException
    {
        setResolver( base );

        Transaction tx = null;

        try {
            tx = base.createTransactionNowIfNeeded();

            MeshObject otherObject = getSource();

            PropertyType  affectedProperty = getProperty();
            PropertyValue oldValue         = getOldValue();
            long          updateTime       = getTimeEventOccurred();

            otherObject.setPropertyValue(
                    affectedProperty,
                    oldValue,
                    updateTime );

            return otherObject;

        } catch( TransactionException ex ) {
            throw ex;

        } catch( Throwable ex ) {
            throw new CannotUnapplyChangeException.ExceptionOccurred( base, ex );

        } finally {
            if( tx != null ) {
                tx.commitTransaction();
            }
        }
    }

    /**
     * Set the MeshBase that can resolve the identifiers carried by this event.
     *
     * @param mb the MeshBase
     */
    public void setResolver(
            MeshBase mb )
    {
        if( theResolver != mb ) {
            theResolver = mb;
            clearCachedObjects();
        }
    }

    /**
     * Obtain the MeshBase that is currently set as resolver for the identifiers carried by this event.
     * 
     * @return the MeshBase, if any
     */
    public MeshBase getResolver()
    {
        return theResolver;
    }

    /**
     * Resolve the source of the event.
     *
     * @return the source of the event
     * @throws PropertyUnresolvedException thrown if the property cannot be resolved
     */
    protected MeshObject resolveSource()
        throws
            PropertyUnresolvedException
    {
        if( theResolver == null ) {
            throw new PropertyUnresolvedException( this );
        }
        
        MeshObject ret = theResolver.findMeshObjectByIdentifier( getSourceIdentifier() );
        return ret;
    }

    /**
     * Resolve the property of the event.
     *
     * @return the property of the event
     * @throws PropertyUnresolvedException thrown if the property cannot be resolved
     */
    protected PropertyType resolveProperty()
        throws
            PropertyUnresolvedException
    {
        if( theResolver == null ) {
            throw new PropertyUnresolvedException( this );
        }
        
        try {
            PropertyType ret = theResolver.getModelBase().findPropertyTypeByIdentifier( getPropertyIdentifier() );
            return ret;

        } catch( MeshTypeWithIdentifierNotFoundException ex ) {
            throw new PropertyUnresolvedException( this, ex );
        }
    }
    
    /**
     * Resolve the new value of the event.
     *
     * @return the new value of the event
     */
    protected PropertyValue resolveValue(
            PropertyValue vid )
    {
        return vid;
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
        if( !( other instanceof MeshObjectPropertyChangeEvent )) {
            return false;
        }
        MeshObjectPropertyChangeEvent realOther = (MeshObjectPropertyChangeEvent) other;

        if( !ArrayHelper.equals( getSourceIdentifier(), realOther.getSourceIdentifier() )) {
            return false;
        }
        if( !ArrayHelper.equals( getPropertyIdentifier(), realOther.getPropertyIdentifier())) {
            return false;
        }
        if( !ArrayHelper.equals( getNewValueIdentifier(), realOther.getNewValueIdentifier())) {
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
     * @return the hash code
     */
    @Override
    public int hashCode()
    {
        return getSourceIdentifier().hashCode();
    }

    /**
     * The resolver of identifiers carried by this event.
     */
    protected transient MeshBase theResolver;
}
