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

package org.infogrid.meshbase;

import org.infogrid.mesh.AbstractMeshObject;
import org.infogrid.mesh.EntityBlessedAlreadyException;
import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.externalized.ParserFriendlyExternalizedMeshObject;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.transaction.MeshObjectCreatedEvent;
import org.infogrid.meshbase.transaction.MeshObjectDeletedEvent;
import org.infogrid.meshbase.transaction.MeshObjectLifecycleEvent;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;

/**
 * Collects implementation methods common to various implementations of
 * {@link MeshBaseLifecycleManager}.
 */
public abstract class AbstractMeshBaseLifecycleManager
        implements
            MeshBaseLifecycleManager
{
    private static final Log log = Log.getLogInstance( AbstractMeshBaseLifecycleManager.class ); // our own, private logger

    /**
     * Constructor. The application developer should not call this or a subclass constructor; use
     * MeshBase.getMeshObjectLifecycleManager() instead.
     */
    protected AbstractMeshBaseLifecycleManager()
    {
        // no op
    }

    /**
     * Enable the AbstractMeshBase to tell this MeshBaseLifecycleManager that they are working together.
     * 
     * @param meshBase the AbstractMeshBase with which this MeshBaseLifecycleManager works
     */
    void setMeshBase(
            AbstractMeshBase meshBase )
    {
        if( theMeshBase != null ) {
            throw new IllegalStateException( "Have MeshBase already: " + theMeshBase );
        }
        theMeshBase = meshBase;
    }

    /**
     * Obtain the MeshBase that this MeshBaseLifecycleManager works on.
     * 
     * @return the MeshBase that this MMeshBaseLifecycleManagerworks on
     */
    @Override
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * <p>This is a convenience method to create a MeshObject with exactly one EntityType
     * and an automatically created MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     *
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     *
     * @param type the EntityType with which the MeshObject will be blessed
     * @return the created MeshObject
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            EntityType type )
        throws
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject();
        if( type != null ) {
            try {
                ret.bless( type );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }

    /**
     * <p>This is a convenience method to create a MeshObject with zero or more EntityTypes
     * and an automatically created MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     *
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     *
     * @param types the EntityTypes with which the MeshObject will be blessed
     * @return the created MeshObject
     * @throws IsAbstractException thrown if one or more EntityTypes are abstract and cannot be instantiated
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            EntityType [] types )
        throws
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject();

        if( types != null && types.length > 0 ) {
            try {
                ret.bless( types );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }

    /**
     * <p>Create a new MeshObject without a type
     * and a provided MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is to be created.</p>
     * 
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     * 
     * @param identifier the identifier of the to-be-created MeshObject. If this is null,
     *                        automatically create a suitable MeshObjectIdentifier.
     * @return the created MeshObject
     * @throws MeshObjectIdentifierNotUniqueException a MeshObject exists already in this MeshBase with the specified Identifier
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        long time = determineCreationTime();
        long autoExpires;
        
        if( DEFAULT_RELATIVE_TIME_EXPIRES > 0 ) {
            autoExpires = time + DEFAULT_RELATIVE_TIME_EXPIRES;
        } else {
            autoExpires = DEFAULT_RELATIVE_TIME_EXPIRES;
        }

        return createMeshObject( identifier, time, time, time, autoExpires );
    }

    /**
     * <p>This is a convenience method to create a MeshObject with exactly one EntityType
     * and a provided MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     * 
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     * 
     * @param identifier the identifier of the to-be-created MeshObject. If this is null,
     *                        automatically create a suitable MeshObjectIdentifier.
     * @param type the EntityType with which the MeshObject will be blessed
     * @return the created MeshObject
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws MeshObjectIdentifierNotUniqueException a MeshObject exists already in this MeshBase with the specified Identifier
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            MeshObjectIdentifier identifier,
            EntityType           type )
        throws
            IsAbstractException,
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject( identifier );
        if( type != null ) {
            try {
                ret.bless( type );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }

    /**
     * <p>This is a convenience method to create a MeshObject with zero or more EntityTypes
     * and a provided MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     * 
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     * 
     * @param identifier the identifier of the to-be-created MeshObject. If this is null,
     *                        automatically create a suitable MeshObjectIdentifier.
     * @param types the EntityTypes with which the MeshObject will be blessed
     * @return the created MeshObject
     * @throws IsAbstractException thrown if one or more EntityTypes are abstract and cannot be instantiated
     * @throws MeshObjectIdentifierNotUniqueException a MeshObject exists already in this MeshBase with the specified Identifier
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            MeshObjectIdentifier identifier,
            EntityType []        types )
        throws
            IsAbstractException,
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject( identifier );
        if( types != null && types.length > 0 ) {
            try {
                ret.bless( types );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }

    /**
     * <p>This is a convenience method to create a MeshObject with exactly one EntityType,
     * with provided time stamps
     * and a provided MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     * 
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     * 
     * @param identifier the identifier of the to-be-created MeshObject. If this is null,
     *                        automatically create a suitable MeshObjectIdentifier.
     * @param type the EntityType with which the MeshObject will be blessed
     * @param timeCreated the time when this MeshObject was semantically created, in System.currentTimeMillis() format
     * @param timeUpdated the time when this MeshObject was last updated, in System.currentTimeMillis() format
     * @param timeRead the time when this MeshObject was last read, in System.currentTimeMillis() format
     * @param timeExpires the time this MeshObject will expire, in System.currentTimeMillis() format
     * @return the created MeshObject
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws MeshObjectIdentifierNotUniqueException a MeshObject exists already in this MeshBase with the specified Identifier
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            MeshObjectIdentifier identifier,
            EntityType     type,
            long           timeCreated,
            long           timeUpdated,
            long           timeRead,
            long           timeExpires )
        throws
            IsAbstractException,
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject( identifier, timeCreated, timeUpdated, timeRead, timeExpires );
        
        if( type != null ) {
            try {
                ret.bless( type );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }

    /**
     * <p>This is a convenience method to create a MeshObject with zero or more EntityTypes,
     * with provided time stamps
     * and a provided MeshObjectIdentifier.
     * This call is a "semantic create" which means that a new, semantically distinct object
     * is created.</p>
     * 
     * <p>Before this operation can be successfully invoked, a Transaction must be active
     * on this Thread.>/p>
     * 
     * @param identifier the identifier of the to-be-created MeshObject. If this is null,
     *                        automatically create a suitable MeshObjectIdentifier.
     * @param types the EntityTypes with which the MeshObject will be blessed
     * @param timeCreated the time when this MeshObject was semantically created, in System.currentTimeMillis() format
     * @param timeUpdated the time when this MeshObject was last updated, in System.currentTimeMillis() format
     * @param timeRead the time when this MeshObject was last read, in System.currentTimeMillis() format
     * @param timeExpires the time this MeshObject will expire, in System.currentTimeMillis() format
     * @return the created MeshObject
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws MeshObjectIdentifierNotUniqueException a MeshObject exists already in this MeshBase with the specified Identifier
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public MeshObject createMeshObject(
            MeshObjectIdentifier identifier,
            EntityType []  types,
            long           timeCreated,
            long           timeUpdated,
            long           timeRead,
            long           timeExpires )
        throws
            IsAbstractException,
            MeshObjectIdentifierNotUniqueException,
            TransactionException,
            NotPermittedException
    {
        MeshObject ret = createMeshObject( identifier, timeCreated, timeUpdated, timeRead, timeExpires );
        
        if( types != null && types.length > 0 ) {
            try {
                ret.bless( types );

            } catch( EntityBlessedAlreadyException ex ) {
                log.error( ex );
            }
        }
        return ret;
    }
    
    /**
     * <p>Semantically delete a MeshObject.</p>
     * 
     * <p>This call is a "semantic delete", which means that an existing
     * MeshObject will go away in all its replicas. Due to time lag, the MeshObject
     * may still exist in certain replicas in other places for a while, but
     * the request to deleteMeshObjects all objects is in the queue and will get there
     * eventually.</p>
     * 
     * @param theObject the MeshObject to be semantically deleted
     * @throws TransactionException thrown if this method was invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    @Override
    public void deleteMeshObject(
            MeshObject theObject )
        throws
            TransactionException,
            NotPermittedException
    {
        deleteMeshObjects( new MeshObject[] { theObject } );
    }


    /**
     * Instantiate an ExternalizedMeshObject that is appropriate to capture the information held by
     * the subtype of MeshObject used by this MeshBase. This is factored out so it can easily be
     * overridden in subclasses.
     * 
     * @return the ParserFriendlyExternalizedMeshObject.
     */
    @Override
    public ParserFriendlyExternalizedMeshObject createParserFriendlyExternalizedMeshObject()
    {
        return new ParserFriendlyExternalizedMeshObject();
    }

    /**
     * Assign owner MeshObject to a newly created MeshObject. For subclasses only.
     *
     * @param obj the newly created MeshObject
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     */
    protected void assignOwner(
            MeshObject obj )
        throws
            TransactionException
    {
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr == null ) {
            return; // no AccessManager
        }

        MeshObject caller = ThreadIdentityManager.getCaller();
        if( caller == null ) {
            return; // no owner
        }

        try {
            accessMgr.assignOwner( obj, caller );
        } catch( NotPermittedException ex ) {
            log.error( ex );
        }
    }

    /**
     * Determine the current time for the purpose of assigning the right creation time to a
     * to-be-created MeshObject. This may be overridden in subclasses.
     *
     * @return the creation time to use, in System.currentTimeMillis() format
     */
    protected long determineCreationTime()
    {
        return System.currentTimeMillis();
    }

    /**
     * Overridable helper to create a MeshObjectCreatedEvent.
     *
     * @param createdObject the created MeshObject
     * @return the created event
     */
    protected MeshObjectCreatedEvent createCreatedEvent(
            MeshObject createdObject )
    {
        MeshObjectCreatedEvent ret = new MeshObjectCreatedEvent(
                theMeshBase,
                theMeshBase.getIdentifier(),
                createdObject,
                createdObject.getTimeCreated() );
        return ret;
    } 

    /**
     * Overridable helper to create a MeshObjectDeletedEvent.
     * 
     * @param deletedObject the deleted MeshObject
     * @param canonicalIdentifier the canonical MeshObjectIdentifier of the deleted MeshObject.
     *        Once a MeshObject has been deleted, its canonical MeshObjectIdentifier can no longer be determined
     * @param externalizedDeletedObject external form of the deleted MeshObject
     * @param timeEventOccurred the time when the event occurred
     * @return the created event
     */
    protected MeshObjectDeletedEvent createDeletedEvent(
            MeshObject             deletedObject,
            MeshObjectIdentifier   canonicalIdentifier,
            ExternalizedMeshObject externalizedDeletedObject,
            long                   timeEventOccurred )
    {
        MeshObjectDeletedEvent ret = new MeshObjectDeletedEvent(
                theMeshBase,
                theMeshBase.getIdentifier(),
                deletedObject,
                canonicalIdentifier,
                externalizedDeletedObject,
                timeEventOccurred );
        return ret;
    }

    /**
     * Helper method that allows our subclasses to access the internal storage without having to expose it publicly.
     * 
     * @param identifier the identifier of the MeshObject to look for
     * @return the found MeshObject, or none.
     */
    protected MeshObject findInStore(
            MeshObjectIdentifier identifier )
    {
        AbstractMeshBase realBase = (AbstractMeshBase) theMeshBase;
        MeshObject       ret      = realBase.theCache.get( identifier );
        return ret;
    }

    /**
     * Helper method that allows our subclasses to access the internal storage without having to expose it publicly.
     *
     * @param obj the MeshObject to put into the storage
     * @param event the MeshObjectLifeCycleEvent
     */
    protected void putIntoMeshBase(
            AbstractMeshObject       obj,
            MeshObjectLifecycleEvent event )
    {
        AbstractMeshBase realBase = (AbstractMeshBase) theMeshBase;

        realBase.theCache.putIgnorePrevious( obj.getIdentifier(), obj );
        
        if( event != null ) {
            realBase.getCurrentTransaction().addChange( event );
            realBase.notifyLifecycleEvent( event );
        }
    }

    /**
     * Helper method that allows our subclasses to access the internal storage without having to expose it publicly.
     * 
     * @param identifier the identifier of the MeshObject
     * @param event the MeshObjectLifeCycleEvent
     * @return the removed MeshObject, or null
     */
    protected MeshObject removeFromMeshBase(
            MeshObjectIdentifier     identifier,
            MeshObjectLifecycleEvent event )
    {
        AbstractMeshBase realBase = (AbstractMeshBase) theMeshBase;
        MeshObject       ret      = realBase.theCache.remove( identifier );
        
        if( event != null ) {
            realBase.getCurrentTransaction().addChange( event );
            realBase.notifyLifecycleEvent( event );
        }
        return ret;
    }

    /**
     * The MeshBase that we work on.
     */
    protected MeshBase theMeshBase;
    
    /**
     * Our ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( AbstractMeshBaseLifecycleManager.class );

    /**
     * The default time at which MeshObjects auto-expire, unless otherwise specified.
     * This is given as a relative time, from the current time. If -1 is given, it means
     * "never".
     */
    public static final long DEFAULT_RELATIVE_TIME_EXPIRES = theResourceHelper.getResourceLongOrDefault( 
            "DefaultRelativeTimeExpires",
            -1L );
}
