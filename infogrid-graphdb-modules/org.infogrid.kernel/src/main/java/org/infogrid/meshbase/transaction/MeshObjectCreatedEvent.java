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

import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.event.ExternalizableEvent;
import org.infogrid.util.event.OtherUnresolvedException;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
 * This event indicates that a new MeshObject was created. If the MeshObject was created
 * and blessed at the same time, only this event (and no PropertyChangeEvents reflecting the blessing)
 * will be sent.
 */
public class MeshObjectCreatedEvent
        extends
            AbstractMeshObjectLifecycleEvent
{
    private static final long serialVersionUID = 1l; // helps with serialization
    private static final Log  log              = Log.getLogInstance( MeshObjectCreatedEvent.class ); // our own, private logger

    /**
     * Constructor.
     *
     * @param source the MeshBase that is the source of the event
     * @param sourceIdentifier the MeshBaseIdentifier representing the source of the event
     * @param createdObject the MeshObject that was created
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectCreatedEvent(
            MeshBase           source,
            MeshBaseIdentifier sourceIdentifier,
            MeshObject         createdObject,
            long               timeEventOccurred )
    {
        super(  source,
                sourceIdentifier,
                createdObject,
                createdObject.getIdentifier(),
                timeEventOccurred );
        
        theExternalizedMeshObject = createdObject.asExternalized();
    }

    /**
     * Constructor.
     *
     * @param source the MeshBase that is the source of the event
     * @param sourceIdentifier the MeshBaseIdentifier representing the source of the event
     * @param createdObject the MeshObject that was created
     * @param timeEventOccurred the time at which the event occurred, in <code>System.currentTimeMillis</code> format
     */
    public MeshObjectCreatedEvent(
            MeshBase               source,
            MeshBaseIdentifier     sourceIdentifier,
            ExternalizedMeshObject createdObject,
            long                   timeEventOccurred )
    {
        super(  source,
                sourceIdentifier,
                null,
                createdObject.getIdentifier(),
                timeEventOccurred );
        
        theExternalizedMeshObject = createdObject;
    }

    /**
     * Obtain the ExternalizedMeshObject that captures the newly created MeshObject.
     * 
     * @return the ExternalizedMeshObject
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
            theEntityTypes = resolveEntityTypes( this, theExternalizedMeshObject, theResolver );
        }
        return theEntityTypes;
    }

    /**
     * Resolve the EntityTypes.
     *
     * @param event the event on whose behalf the method is executed
     * @param externalized the ExternalizedMeshObject containing the externalized EntityType references in question
     * @param resolver the resolver to use
     * @return the resolved EntityTypes
     */
    protected static EntityType [] resolveEntityTypes(
            ExternalizableEvent    event,
            ExternalizedMeshObject externalized,
            MeshBase               resolver )
    {
        MeshTypeIdentifier [] typeNames = externalized.getExternalTypeIdentifiers();
        if( typeNames == null || typeNames.length == 0 ) {
            return new EntityType[0];
        }
        EntityType [] ret = new EntityType[ typeNames.length ];
        if( resolver == null ) {
            throw new OtherUnresolvedException( event );
        }

        ModelBase modelBase = resolver.getModelBase();
        for( int i=0 ; i<typeNames.length ; ++i ) {
            try {
                ret[i] = modelBase.findEntityTypeByIdentifier( typeNames[i] );

            } catch( MeshTypeWithIdentifierNotFoundException ex ) {
                throw new OtherUnresolvedException( event, ex );
            }
        }
        return ret;
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
        Throwable   t = null;

        ModelBase modelBase = base.getModelBase();

        resolveEntityTypes( this, theExternalizedMeshObject, theResolver );

        try {
            tx = base.createTransactionNowIfNeeded();

            MeshObject newObject = instantiateMeshObject( base );

            for( int i=0 ; i<theExternalizedMeshObject.getPropertyTypes().length ; ++i ) {
                try {
                    PropertyType  propertyType  = modelBase.findPropertyTypeByIdentifier( theExternalizedMeshObject.getPropertyTypes()[i] );
                    PropertyValue propertyValue = theExternalizedMeshObject.getPropertyValues()[i];

                    newObject.setPropertyValue( propertyType, propertyValue );

                } catch( Throwable ex ) {
                    if( t == null ) {
                        t = ex;
                    } else {
                        log.warn( "Second or later Exception", ex );
                    }
                }
            }
            return newObject;

        } catch( TransactionException ex ) {
            throw ex;

        } catch( Throwable ex ) {
            if( t == null ) {
                t = ex;
            } else {
                log.warn( "Second or later Exception", ex );
            }
                
        } finally {
            if( tx != null ) {
                tx.commitTransaction();
            }
        }
        if( t != null ) {
            throw new CannotApplyChangeException.ExceptionOccurred( base, t );
        }
        return null; // I don't think this can happen, but let's make the compiler happy.
    }

    /**
     * Overridable helper method to actually instantiate the MeshObject.
     *
     * @param base the MeshBase in which to apply the Change
     * @return the MeshObject to which the Change was applied
     * @throws CannotApplyChangeException thrown if the Change could not be applied, e.g because
     *         the affected MeshObject did not exist in MeshBase base
     * @throws IsAbstractException thrown if at least one of the EntityTypes was abstract
     * @throws MeshObjectIdentifierNotUniqueException thrown if a MeshObject with this identifier exists already
     * @throws NotPermittedException thrown if the caller did not have sufficient permissions for this operation
     * @throws TransactionException thrown if a Transaction didn't exist on this Thread and
     *         could not be created
     */
    protected MeshObject instantiateMeshObject(
            MeshBase base )
        throws
            CannotApplyChangeException,
            IsAbstractException,
            MeshObjectIdentifierNotUniqueException,
            NotPermittedException,
            TransactionException
    {
        MeshObject ret = base.getMeshBaseLifecycleManager().createMeshObject(
                        getDeltaValueIdentifier(),
                        getEntityTypes(),
                        theExternalizedMeshObject.getTimeCreated(),
                        theExternalizedMeshObject.getTimeUpdated(),
                        theExternalizedMeshObject.getTimeRead(),
                        theExternalizedMeshObject.getTimeExpires() );

        return ret;
    }

    /**
     * <p>Create a Change that undoes this Change.</p>
     *
     * @return the inverse Change, or null if no inverse Change could be constructed.
     */
    public MeshObjectDeletedEvent inverse()
    {
        return new MeshObjectDeletedEvent(
                getSource(),
                getSourceIdentifier(),
                getDeltaValue(),
                getDeltaValueIdentifier(),
                theExternalizedMeshObject,
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
        if( !( candidate instanceof MeshObjectDeletedEvent )) {
            return false;
        }
        if( !getAffectedMeshObjectIdentifier().equals( candidate.getAffectedMeshObjectIdentifier())) {
            return false;
        }
        return true;
    }

    /**
     * Clear cached objects to force a re-resolve.
     */
    @Override
    protected void clearCachedObjects()
    {
        theEntityTypes = null;

        super.clearCachedObjects();
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare against
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( !( other instanceof MeshObjectCreatedEvent )) {
            return false;
        }
        MeshObjectCreatedEvent realOther = (MeshObjectCreatedEvent) other;

        if( !getDeltaValueIdentifier().equals( realOther.getDeltaValueIdentifier() )) {
            return false;
        }
        if( !theExternalizedMeshObject.equals( realOther.theExternalizedMeshObject )) {
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
        return theExternalizedMeshObject.hashCode();
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "getSourceIdentifier()",
                    "getNewValueIdentifier()",
                    "theExternalizedMeshObject",
                    "getTimeOccured()"
                },
                new Object[] {
                    getSourceIdentifier(),
                    getDeltaValueIdentifier(),
                    theExternalizedMeshObject,
                    getTimeEventOccurred()
                });
    }

    /**
     * Externalized version.
     */
    protected ExternalizedMeshObject theExternalizedMeshObject;
    
    /**
     * The EntityTypes, once resolved.
     */
    protected transient EntityType [] theEntityTypes;
}
