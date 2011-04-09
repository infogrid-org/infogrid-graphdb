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

package org.infogrid.mesh.a;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.infogrid.mesh.AbstractMeshObject;
import org.infogrid.mesh.CannotRelateToItselfException;
import org.infogrid.mesh.EntityNotBlessedException;
import org.infogrid.mesh.EquivalentAlreadyException;
import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.NotRelatedException;
import org.infogrid.mesh.RelatedAlreadyException;
import org.infogrid.mesh.RoleTypeBlessedAlreadyException;
import org.infogrid.mesh.RoleTypeNotBlessedException;
import org.infogrid.mesh.TypeInitializer;
import org.infogrid.mesh.TypedMeshObjectFacade;
import org.infogrid.mesh.externalized.SimpleExternalizedMeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.text.MeshStringRepresentationParameters;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.meshbase.a.AMeshBase;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.Role;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * One particular implementation of MeshObject.
 */
public class AMeshObject
        extends
            AbstractMeshObject
{
    private static final Log log = Log.getLogInstance( AMeshObject.class ); // our own, private logger

    /**
     * Constructor for regular instantiation.
     * 
     * @param identifier the MeshObjectIdentifier of the MeshObject
     * @param meshBase the MeshBase that this MeshObject belongs to
     * @param created the time this MeshObject was created
     * @param updated the time this MeshObject was last updated
     * @param read the time this MeshObject was last read
     * @param expires the time this MeshObject will expire
     */
    public AMeshObject(
            MeshObjectIdentifier identifier,
            AMeshBase            meshBase,
            long                 created,
            long                 updated,
            long                 read,
            long                 expires )
    {
        super( identifier, meshBase, created, updated, read, expires );
    }

    /**
     * Constructor for re-instantiation from external storage.
     * 
     * @param identifier the MeshObjectIdentifier of the MeshObject
     * @param meshBase the MeshBase that this MeshObject belongs to
     * @param created the time this MeshObject was created
     * @param updated the time this MeshObject was last updated
     * @param read the time this MeshObject was last read
     * @param expires the time this MeshObject will expire
     * @param properties the properties with their values of the MeshObject, if any
     * @param meshTypes the MeshTypes and facdes of the MeshObject, if any
     * @param equivalents either an array of length 2, or null. If given, contains the left and right equivalence pointers.
     * @param neighborIdentifiers the current neighbors of the MeshObject, given as Identifiers
     * @param neighborRoleTypes the RoleTypes of the relationships with the various neighbors, in sequence
     */
    public AMeshObject(
            MeshObjectIdentifier                identifier,
            AMeshBase                           meshBase,
            long                                created,
            long                                updated,
            long                                read,
            long                                expires,
            HashMap<PropertyType,PropertyValue> properties,
            EntityType []                       meshTypes,
            MeshObjectIdentifier []             equivalents,
            MeshObjectIdentifier []             neighborIdentifiers,
            RoleType [][]                       neighborRoleTypes )
    {
        super( identifier, meshBase, created, updated, read, expires );
        
        theProperties          = properties;
        theNeighborIdentifiers = neighborIdentifiers;
        theNeighborRoleTypes   = neighborRoleTypes;
       
        if( equivalents != null && equivalents.length != 2 ) {
            throw new IllegalArgumentException( "Equivalents must be of length 2" );
        }
        theEquivalenceSetPointers = equivalents;
        
        if( meshTypes != null && meshTypes.length > 0 ) {
            theMeshTypes = createMeshTypes();
            for( int i=0 ; i<meshTypes.length ; ++i ) {
                theMeshTypes.put( meshTypes[i], null );
            }
        }
    }

    /**
     * Update the lastUpdated property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     *
     * @param timeUpdated the time to set to. -1 means "don't update" and 0 means "current time".
     * @param lastTimeUpdated the time this MeshObject was updated last before
     */
    protected void updateLastUpdated(
            long timeUpdated,
            long lastTimeUpdated )
    {
        AMeshBase realBase = (AMeshBase) theMeshBase;
        theTimeUpdated = realBase.calculateLastUpdated( timeUpdated, lastTimeUpdated );
    }

    /**
     * Update the lastRead property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     *
     * @param timeRead the time to set to. -1 means "don't update" and 0 means "current time".
     * @param lastTimeRead the time this MeshObject was read last before
     */
    protected void updateLastRead(
            long timeRead,
            long lastTimeRead )
    {
        AMeshBase realBase = (AMeshBase) theMeshBase;
        theTimeRead = realBase.calculateLastRead( timeRead, lastTimeRead );
    }
    
    /**
     * Traverse from this MeshObject to all directly related MeshObjects. Directly
     * related MeshObjects are those MeshObjects that are participating in a
     * relationship with this MeshObject. Specify whether to consider equivalents
     * as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the set of MeshObjects that are directly related to this MeshObject
     */
    public MeshObjectSet traverseToNeighborMeshObjects(
            boolean considerEquivalents )
    {
        checkAlive();

        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }

        AMeshObjectNeighborManager nMgr                = getNeighborManager();
        AMeshBase                  realBase            = (AMeshBase) theMeshBase;
        MeshObjectIdentifier [][]  neighborIdentifiers = createMeshObjectIdentifierArrayArray( starts.length );

        int n=0;
        for( int s=0 ; s<starts.length ; ++s ) {
            AMeshObject current = (AMeshObject) starts[s];
            neighborIdentifiers[s] = nMgr.getNeighborIdentifiers( current );
            if( neighborIdentifiers[s] != null ) {
                n += neighborIdentifiers[s].length;
            }
        }

        MeshObjectSet ret;

        if( n == 0 ) {
            ret = realBase.getMeshObjectSetFactory().obtainEmptyImmutableMeshObjectSet();
            
        } else {
            AMeshObject [] almost = new AMeshObject[ n ];
            
            int max = 0;
            for( int s=0 ; s<neighborIdentifiers.length ; ++s ) {
                if( neighborIdentifiers[s] == null ) {
                    continue;
                }
                MeshObject [] add = findRelatedMeshObjects( theMeshBase, neighborIdentifiers[s] );

                for( int i=0 ; i<add.length ; ++i ) {
                    if( add[i] == null ) {
                        // This happens when a MeshObject auto-expires. FIXME: This might introduce more problems
                        // than it solves (e.g. it masks genuine errors)
                        if( log.isDebugEnabled() ) {
                            log.debug( "Could not find related object " + neighborIdentifiers[s][i] );
                        }

                    } else if( !ArrayHelper.isIn( add[i], almost, 0, max, false )) {
                        almost[ max++ ] = (AMeshObject) add[i];
                    }
                }
            }
            if( max < n ) {
                almost = ArrayHelper.copyIntoNewArray( almost, 0, max, AMeshObject.class );
            }

            ret = realBase.getMeshObjectSetFactory().createImmutableMeshObjectSet( almost );
        }
        updateLastRead();

        return ret;
    }

    /**
     * Find neighbor MeshObjects of this MeshObject that are known by their
     * MeshObjectIdentifiers.
     * We pass in the MeshBase to use because this may be invoked when a MeshObject's member
     * variable has been zero'd out already.
     *
     * @param mb the MeshBase to use
     * @param neighborIdentifiers the MeshObjectIdentifiers of the MeshObjects we are looking for
     * @return the MeshObjects that we found
     */
    protected MeshObject [] findRelatedMeshObjects(
            MeshBase                mb,
            MeshObjectIdentifier [] neighborIdentifiers )
    {
        MeshObject [] ret = mb.findMeshObjectsByIdentifier( neighborIdentifiers );
        return ret;
    }

    /**
     * Find a single neighbor MeshObject of this MeshObject that is known by its
     * MeshObjectIdentifier.
     * We pass in the MeshBase to use because this may be invoked when a MeshObject's member
     * variable has been zero'd out already.
     * This internal helper method may be overridden by subclasses.
     *
     * @param mb the MeshBase to use
     * @param neighborIdentifier the MeshObjectIdentifier of the MeshObject we are looking for
     * @return the MeshObject that we found
     */
    protected MeshObject findRelatedMeshObject(
            MeshBase             mb,
            MeshObjectIdentifier neighborIdentifier )
    {
        MeshObject [] ret = findRelatedMeshObjects( mb, new MeshObjectIdentifier[] { neighborIdentifier } );
        return ret[0];
    }

    /**
     * Obtain the MeshObjectIdentifier of the neighbors of this MeshObject. This is sometimes a
     * more efficient operation than to traverse to the neighbors and determine the
     * MeshObjectIdentifiers from there.
     *
     * @return the MeshObjectIdentifier of the neighbors, if any
     */
    @Override
    public MeshObjectIdentifier[] getNeighborMeshObjectIdentifiers()
    {
        checkAlive();

        // That avoids a synchronized
        MeshObjectIdentifier [] neighborIdentifiers = theNeighborIdentifiers;
        if( neighborIdentifiers == null ) {
            return null;
        }
        MeshObjectIdentifier[] ret = createMeshObjectIdentifierArray( neighborIdentifiers.length );
        System.arraycopy( neighborIdentifiers, 0, ret, 0, neighborIdentifiers.length );

        return ret;
    }

    /**
     * Relate this MeshObject to another MeshObject. This does not bless the relationship.
     *
     * @param newNeighbor the MeshObject to relate to
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #unrelate
     * @see #relateAndBless
     */
    public void relate(
            MeshObject newNeighbor )
        throws
            RelatedAlreadyException,
            TransactionException
    {
        internalRelate( newNeighbor.getIdentifier(), true, false );
    }
    
    /**
     * Internal helper to implement a method. While on this level, it does not appear that factoring out
     * this method makes any sense, subclasses may appreciate it.
     *
     * @param neighborIdentifier identifier of the MeshObject to relate to
     * @param isMaster true if this is the master
     * @param forgiving true if the problems should be tolerated to the maximum extent possible
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    protected void internalRelate(
            MeshObjectIdentifier neighborIdentifier,
            boolean              isMaster,
            boolean              forgiving )
        throws
            RelatedAlreadyException,
            TransactionException
    {
        if( neighborIdentifier == null ) {
            throw new NullPointerException( "neighborIdentifier is null" );
        }
        MeshObject neighbor = theMeshBase.findMeshObjectByIdentifier( neighborIdentifier );
                // if we have it here, deal with it. If we don't, don't.

        checkAlive();
        if( neighbor != null ) {
            neighbor.checkAlive();
        }

        if( neighbor == this ) {
            throw new CannotRelateToItselfException( this );
        }
        if( neighbor != null && getMeshBase() != neighbor.getMeshBase() ) {
            throw new WrongMeshBaseException( getMeshBase(), neighbor.getMeshBase() );
        }

        MeshObjectIdentifier here         = getIdentifier();
        AMeshObject          realNeighbor = (AMeshObject) neighbor;

        MeshObjectIdentifier [] oldNeighborIdentifiers         = createMeshObjectIdentifierArray( 0 );
        MeshObjectIdentifier [] oldNeighborNeighborIdentifiers = createMeshObjectIdentifierArray( 0 );

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        Object neighborSyncObject = neighbor != null ? neighbor : neighborIdentifier;
                // if there is no neighbor, we don't really need to sync, but Java is inflexible and so we just pick something

        synchronized( this ) {
            synchronized( neighborSyncObject ) {
                
                checkTransaction();
                
                boolean hereAlready  = false;
                boolean thereAlready = false;

                // do what can throw exceptions first
                if( nMgr.hasNeighbors( this )) {
                    for( MeshObjectIdentifier hereNeighborIdentifier : nMgr.getNeighborIdentifiers( this )) {
                        if( neighborIdentifier.equals( hereNeighborIdentifier )) {
                            if( forgiving ) {
                                hereAlready = true;
                            } else {
                                throw new RelatedAlreadyException( this, neighborIdentifier );
                            }
                        }
                    }
                }
                oldNeighborIdentifiers = nMgr.getNeighborIdentifiers( this );

                if( realNeighbor != null && nMgr.hasNeighbors( realNeighbor )) {
                    for( MeshObjectIdentifier thereNeighborIdentifier : nMgr.getNeighborIdentifiers( realNeighbor )) {
                        if( here.equals( thereNeighborIdentifier )) {
                            if( forgiving ) {
                                thereAlready = true;
                            } else {
                                throw new RelatedAlreadyException( realNeighbor, this );
                            }
                        }
                    }
                    oldNeighborNeighborIdentifiers = nMgr.getNeighborIdentifiers( realNeighbor );
                }

                if( !hereAlready ) {
                    nMgr.appendNeighbor( this, neighborIdentifier, null );
                }
                if( realNeighbor != null && !thereAlready ) {
                    nMgr.appendNeighbor( realNeighbor, here, null );
                }
                if( !hereAlready ) {
                    fireNeighborAdded( null, oldNeighborIdentifiers, neighborIdentifier, nMgr.getNeighborIdentifiers( this ), theMeshBase );
                }
                if( realNeighbor != null && !thereAlready ) {
                    realNeighbor.fireNeighborAdded( null, oldNeighborNeighborIdentifiers, here, nMgr.getNeighborIdentifiers( realNeighbor ), theMeshBase );
                }
            }
        }
        updateLastUpdated();
    }

    /**
     * Unrelate this MeshObject from another MeshObject. This will also remove all blessings from the relationship.
     *
     * @param neighbor the MeshObject to unrelate from
     * @throws NotRelatedException thrown if this MeshObject is not already related to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     */
    public void unrelate(
            MeshObject neighbor )
        throws
            NotRelatedException,
            TransactionException,
            NotPermittedException
    {
        internalUnrelate( neighbor.getIdentifier(), theMeshBase, true, true, 0L );
    }
    
    /**
     * Internal helper to unrelate that also works for already-dead MeshObjects.
     *
     * @param neighborIdentifier identifier of the MeshObject to unrelate from
     * @param mb the MeshBase that this MeshObject does or used to belong to
     * @param isMaster true if this is the master replica
     * @param performChecks if true, perform the AccessManager checks; if false, skip them
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws NotRelatedException thrown if this MeshObject is not already related to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the operation is not permitted
     */
    protected void internalUnrelate(
            MeshObjectIdentifier neighborIdentifier,
            MeshBase             mb,
            boolean              isMaster,
            boolean              performChecks,
            long                 timeUpdated )
        throws
            NotRelatedException,
            TransactionException,
            NotPermittedException
    {
        if( neighborIdentifier == null ) {
            throw new NullPointerException( "neighborIdentifier is null" );
        }
        MeshObject neighbor = mb.findMeshObjectByIdentifier( neighborIdentifier );
                // if we have it here, deal with it. If we don't, don't.

        checkAlive();
        if( neighbor != null ) {
            neighbor.checkAlive();
        }

        if( neighbor == this ) {
            throw new NotRelatedException( this, this );
        }
        if( neighbor != null && theMeshBase != neighbor.getMeshBase() ) {
            throw new WrongMeshBaseException( getMeshBase(), neighbor.getMeshBase() );
        }

        MeshObjectIdentifier here         = getIdentifier();
        AMeshObject          realNeighbor = (AMeshObject) neighbor;

        MeshObjectIdentifier [] oldNeighborIdentifiers         = {};
        MeshObjectIdentifier [] oldNeighborNeighborIdentifiers = {};
        RoleType [] roleTypes         = null;
        RoleType [] neighborRoleTypes = null;

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        Object neighborSyncObject = neighbor != null ? neighbor : neighborIdentifier;
                // if there is no neighbor, we don't really need to sync, but Java is inflexible and so we just pick something

        synchronized( this ) {
            synchronized( neighborSyncObject ) {

                internalCheckTransaction( theMeshBase );

                if( !nMgr.hasNeighbors( this )) {
                    throw new NotRelatedException( this, neighborIdentifier );
                }
                if( realNeighbor != null && !nMgr.hasNeighbors( realNeighbor )) {
                    log.error( "found neighbor here, but not here in neighbor: " + this + " vs. " + realNeighbor );
                    throw new NotRelatedException( realNeighbor, this );
                }

                oldNeighborIdentifiers = nMgr.getNeighborIdentifiers( this );
                roleTypes              = nMgr.getRoleTypesFor(        this, neighborIdentifier ); // will throw NotRelatedException

                if( realNeighbor != null ) {
                    oldNeighborNeighborIdentifiers = nMgr.getNeighborIdentifiers( realNeighbor );
                    neighborRoleTypes              = nMgr.getRoleTypesFor(        realNeighbor, here ); // will throw NotRelatedException
                }

                // check that the RoleTypes with the other side let us
                if( performChecks && roleTypes != null ) {
                    checkPermittedUnbless( roleTypes, neighborIdentifier, realNeighbor );
                }
                if( performChecks && realNeighbor != null && neighborRoleTypes != null ) {
                    realNeighbor.checkPermittedUnbless( neighborRoleTypes, here, this );
                }

                // first remove all the RoleTypes
                if( roleTypes != null ) {
                    fireTypesRemoved(
                            roleTypes,
                            roleTypes,
                            new RoleType[0],
                            neighborIdentifier,
                            theMeshBase );
                }
                if( realNeighbor != null && neighborRoleTypes != null ) {
                    realNeighbor.fireTypesRemoved(
                            neighborRoleTypes,
                            neighborRoleTypes,
                            new RoleType[0],
                            here,
                            theMeshBase );
                }

                nMgr.removeNeighbor( this, neighborIdentifier );
                if( realNeighbor != null ) {
                    nMgr.removeNeighbor( realNeighbor, here );
                }

                fireNeighborRemoved( oldNeighborIdentifiers, neighborIdentifier, nMgr.getNeighborIdentifiers( this ), theMeshBase );
                if( realNeighbor != null ) {
                    realNeighbor.fireNeighborRemoved( oldNeighborNeighborIdentifiers, here, nMgr.getNeighborIdentifiers( realNeighbor ), theMeshBase );
                }
            }
        }
        updateLastUpdated( timeUpdated, theTimeUpdated );
    }

    /**
     * Determine whether this MeshObject is related to another MeshObject.
     *
     * @param otherObject the MeshObject to which this MeshObject may be related
     * @return true if this MeshObject is currently related to otherObject
     */
    public boolean isRelated(
            MeshObject otherObject )
    {
        checkAlive();
        otherObject.checkAlive();
        updateLastRead();

        if( theMeshBase != otherObject.getMeshBase() ) {
            throw new IllegalArgumentException( "Cannot relate MeshObjects held in different MeshBases" );
        }

        AMeshObjectNeighborManager nMgr = getNeighborManager();
        boolean ret = nMgr.isRelated( this, otherObject.getIdentifier() );

        return ret;
    }

    /**
     * Make a relationship of this MeshObject to another MeshObject support the provided RoleTypes.
     * As a result, this relationship will support either all RoleTypes or none.
     * 
     * @param thisEnds the RoleTypes of the RelationshipTypes that are instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject whose relationship to this MeshObject shall be blessed
     * @throws RoleTypeBlessedAlreadyException thrown if the relationship to the other MeshObject is blessed
     *         already with one ore more of the given RoleTypes
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws IsAbstractException thrown if one of the RoleTypes belong to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #relateAndBless
     * @see #unrelate
     */
    public void blessRelationship(
            RoleType [] thisEnds,
            MeshObject  neighbor )
        throws
            RoleTypeBlessedAlreadyException,
            EntityNotBlessedException,
            NotRelatedException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        internalBless( thisEnds, neighbor.getIdentifier(), true, false, 0L );
    }
    
    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param roleTypesToAdd the RoleTypes of the RelationshipTypes that are instantiated at the end that this MeshObject is attached to
     * @param neighborIdentifier the MeshObject whose relationship to this MeshObject shall be blessed
     * @param isMaster if true, this is the master replica
     * @param forgiving if true, attempt to ignore errors
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws RoleTypeBlessedAlreadyException thrown if the relationship to the other MeshObject is blessed
     *         already with one ore more of the given RoleTypes
     * @throws EntityNotBlessedException thrown if the source or destination MeshObject was not blessed with a required EntityType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws IsAbstractException thrown if one of the RoleTypes belong to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected void internalBless(
            RoleType []          roleTypesToAdd,
            MeshObjectIdentifier neighborIdentifier,
            boolean              isMaster,
            boolean              forgiving,
            long                 timeUpdated )
        throws
            RoleTypeBlessedAlreadyException,
            EntityNotBlessedException,
            NotRelatedException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        if( neighborIdentifier == null ) {
            throw new NullPointerException( "neighborIdentifier is null" );
        }
        MeshObject neighbor;
        if( forgiving ) {
            neighbor = theMeshBase.findMeshObjectByIdentifier( neighborIdentifier );
                // if we have it here, deal with it. If we don't, don't.
        } else {
            neighbor = findRelatedMeshObject( theMeshBase, neighborIdentifier );
            if( neighbor == null ) {
                log.error( "Cannot find from " + this + " related object " + neighborIdentifier );
                return;
            }
        }

        checkAlive();
        if( neighbor != null ) {
            neighbor.checkAlive();
        }

        if( roleTypesToAdd.length == 0 ) {
            return;
        }

        RoleType [] neighborRoleTypesToAdd = new RoleType[ roleTypesToAdd.length ];

        for( int i=0 ; i<roleTypesToAdd.length ; ++i ) {
            RoleType toAdd = roleTypesToAdd[i];
            if( toAdd == null ) {
                throw new IllegalArgumentException( "null RoleType" );
            }
            if( toAdd.getRelationshipType().getIsAbstract().value() ) {
                throw new IsAbstractException( this, toAdd.getRelationshipType() );
            }
            neighborRoleTypesToAdd[i] = roleTypesToAdd[i].getInverseRoleType();
        }
        if( neighbor != null && getMeshBase() != neighbor.getMeshBase() ) {
            throw new WrongMeshBaseException( getMeshBase(), neighbor.getMeshBase() );
        }

        MeshObjectIdentifier here         = getIdentifier();
        AMeshObject          realNeighbor = (AMeshObject) neighbor;

        RoleType [] roleTypes         = null;
        RoleType [] neighborRoleTypes = null;

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        Object neighborSyncObject = neighbor != null ? neighbor : neighborIdentifier;
                // if there is no neighbor, we don't really need to sync, but Java is inflexible and so we just pick something

        synchronized( this ) {
            synchronized( neighborSyncObject ) {

                checkTransaction();

                for( int i=0 ; i<roleTypesToAdd.length ; ++i ) {
                    RoleType   otherEnd             = roleTypesToAdd[i].getInverseRoleType();
                    EntityType requiredType         = roleTypesToAdd[i].getEntityType();
                    EntityType requiredNeighborType = otherEnd.getEntityType();
                    
                    if( requiredType != null ) {
                        boolean found = false;
                        if( theMeshTypes != null ) {
                            for( AttributableMeshType amt : theMeshTypes.keySet() ) {
                                if( amt.equalsOrIsSupertype( requiredType )) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if( !found ) {
                            throw new EntityNotBlessedException( this, requiredType );
                        }
                    }
                    if( realNeighbor != null && requiredNeighborType != null ) {
                        boolean found = false;
                        if( realNeighbor.theMeshTypes != null ) {
                            for( AttributableMeshType amt : realNeighbor.theMeshTypes.keySet() ) {
                                if( amt.equalsOrIsSupertype( requiredNeighborType )) {
                                    found = true;
                                    break;
                                }
                            }
                        }
                        if( !found ) {
                            throw new EntityNotBlessedException( realNeighbor, requiredNeighborType );
                        }
                    }
                }

                if( !nMgr.hasNeighbors( this )) {
                    throw new NotRelatedException( this, neighborIdentifier );
                }
                if( realNeighbor != null && !nMgr.hasNeighbors( realNeighbor )) {
                    log.error( "found neighbor here, but not here in neighbor: " + this + " vs. " + neighborIdentifier );
                    throw new NotRelatedException( realNeighbor, this );
                }

                roleTypes = nMgr.getRoleTypesFor( this, neighborIdentifier ); // will throw NotRelatedException
                if( realNeighbor != null ) {
                    neighborRoleTypes = nMgr.getRoleTypesFor( realNeighbor, here ); // will throw NotRelatedException
                }

                boolean hereAlready  = false;
                boolean thereAlready = false;
                
                for( RoleType toAdd : roleTypesToAdd ) {
                    RoleType neighborToAdd = toAdd.getInverseRoleType();

                    // make sure we do everything that might throw an exception first, and do assignments later
                    if( roleTypes != null ) {
                        for( int i=0 ; i<roleTypes.length ; ++i ) {
                            if( roleTypes[i].isSpecializationOfOrEquals( toAdd )) {
                                if( forgiving ) {
                                    hereAlready = true;
                                } else {
                                    throw new RoleTypeBlessedAlreadyException( this, toAdd, neighborIdentifier );
                                }

                            } else if( toAdd.isSpecializationOfOrEquals( roleTypes[i] )) {
                                roleTypes[i] = toAdd;
                                break;
                            }
                        }
                    }
                                
                    if( realNeighbor != null && neighborRoleTypes != null ) {
                        for( int i=0 ; i<neighborRoleTypes.length ; ++i ) {
                            if( neighborRoleTypes[i].isSpecializationOfOrEquals( neighborToAdd )) {
                                if( forgiving ) {
                                    thereAlready = true;
                                } else {
                                    throw new RoleTypeBlessedAlreadyException( realNeighbor, neighborToAdd, this );
                                }
                    
                            } else if( neighborToAdd.isSpecializationOfOrEquals( neighborRoleTypes[i] )) {
                                neighborRoleTypes[i] = neighborToAdd;
                                return;
                            }
                        }
                    }
                }

                checkPermittedBless( roleTypesToAdd, neighborIdentifier, neighbor ); // implementation does everything else
                realNeighbor.checkPermittedBless( neighborRoleTypesToAdd, here, this ); // implementation does everything else

                for( RoleType thisEnd : roleTypesToAdd ) {
                    RoleType otherEnd = thisEnd.getInverseRoleType();

                    if( !hereAlready ) {
                        nMgr.appendRoleType( this, neighborIdentifier, thisEnd );
                    }
                    if( realNeighbor != null && !thereAlready ) {
                        nMgr.appendRoleType( realNeighbor, theIdentifier, otherEnd );
                    }
                }

                if( !hereAlready ) {
                    fireTypesAdded(
                            roleTypes,
                            roleTypesToAdd,
                            nMgr.getRoleTypesFor( this, neighborIdentifier ),
                            neighborIdentifier,
                            theMeshBase );
                }
                if( realNeighbor != null && !thereAlready ) {
                    realNeighbor.fireTypesAdded(
                            neighborRoleTypes,
                            neighborRoleTypesToAdd,
                            nMgr.getRoleTypesFor( realNeighbor, here ),
                            here,
                            theMeshBase );
                }
            }
        }
        updateLastUpdated( timeUpdated, theTimeUpdated );
    }

    /**
     * Make a relationship of this MeshObject to another MeshObject stop supporting the provided RoleType.
     * 
     * @param thisEnds the RoleType of the RelationshipType at the end that this MeshObject is attached to, and that shall be removed
     * @param otherObject the other MeshObject whose relationship to this MeshObject shall be unblessed
     * @throws RoleTypeNotBlessedException thrown if the relationship to the other MeshObject does not support the RoleType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unblessRelationship(
            RoleType [] thisEnds,
            MeshObject  otherObject )
        throws
            RoleTypeNotBlessedException,
            NotRelatedException,
            TransactionException,
            NotPermittedException
    {
        internalUnbless( thisEnds, otherObject.getIdentifier(), true, 0L );
    }

    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param roleTypesToRemoveHere the RoleType of the RelationshipType at the end that this MeshObject is attached to, and that shall be removed
     * @param neighborIdentifier identifier of the other MeshObject whose relationship to this MeshObject shall be unblessed
     * @param strict if true, be strict; if false, tolerate errors
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws RoleTypeNotBlessedException thrown if the relationship to the other MeshObject does not support the RoleType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected void internalUnbless(
            RoleType []          roleTypesToRemoveHere,
            MeshObjectIdentifier neighborIdentifier,
            boolean              strict,
            long                 timeUpdated )
        throws
            RoleTypeNotBlessedException,
            NotRelatedException,
            TransactionException,
            NotPermittedException
    {
        checkAlive();

        MeshObject neighbor = theMeshBase.findMeshObjectByIdentifier( neighborIdentifier );
        if( neighbor != null ) {
            neighbor.checkAlive();
        }

        if( roleTypesToRemoveHere.length == 0 ) {
            return;
        }
        for( RoleType thisEnd : roleTypesToRemoveHere ) {
            if( thisEnd == null ) {
                throw new IllegalArgumentException( "null RoleType" );
            }
        }

        AMeshObject realNeighbor = (AMeshObject) neighbor;

        Object neighborSyncObject; // this is a hack, but then, Java does not make this easy
        if( realNeighbor != null ) {
            neighborSyncObject = realNeighbor;
        } else {
            neighborSyncObject = new Object();
        }

        AMeshObjectNeighborManager nMgr = getNeighborManager();
        synchronized( this ) {
            synchronized( neighborSyncObject ) {

                checkTransaction();

                if( !nMgr.hasNeighbors( this )) {
                    throw new NotRelatedException( theMeshBase, theMeshBase.getIdentifier(), this, getIdentifier(), realNeighbor, neighborIdentifier );
                }
                if( realNeighbor != null && !nMgr.hasNeighbors( realNeighbor )) {
                    log.error( "found otherObject here, but not here in otherObject: " + this + " vs. " + realNeighbor );
                    throw new NotRelatedException( realNeighbor, this );
                }

//                MeshObjectIdentifier [] neighborIdentifiers      = nMgr.getNeighborIdentifiers( this );
//                MeshObjectIdentifier [] otherNeighborIdentifiers = realNeighbor != null ? nMgr.getNeighborIdentifiers( realNeighbor ) : null;
//
                RoleType [] oldRoleTypesHere  = realNeighbor != null ? nMgr.getRoleTypesFor( this, realNeighbor.getIdentifier() ) : null; // will throw NotRelatedException
                RoleType [] oldRoleTypesThere = realNeighbor != null ? nMgr.getRoleTypesFor( realNeighbor, theIdentifier )        : null; // will throw NotRelatedException

                RoleType [] roleTypesToRemoveThere = new RoleType[ roleTypesToRemoveHere.length ];
                for( int i=0 ; i<roleTypesToRemoveHere.length ; ++i ) {
                    roleTypesToRemoveThere[i] = roleTypesToRemoveHere[i].getInverseRoleType();
                }

                if( strict ) {
                    for( RoleType thisEnd : roleTypesToRemoveHere ) {
                        RoleType otherEnd = thisEnd.getInverseRoleType();

                        if( oldRoleTypesHere == null || !ArrayHelper.isIn( thisEnd, oldRoleTypesHere, false )) {
                            throw new RoleTypeNotBlessedException( this, thisEnd, realNeighbor );
                        }
                        if( realNeighbor != null ) {
                            if( oldRoleTypesThere == null || !ArrayHelper.isIn( otherEnd, oldRoleTypesThere, false )) {
                                throw new RoleTypeNotBlessedException( realNeighbor, otherEnd, this );
                            }
                        }
                    }

                    this.checkPermittedUnbless( roleTypesToRemoveHere, neighborIdentifier, neighbor );
                    if( realNeighbor != null ) {
                        realNeighbor.checkPermittedUnbless( roleTypesToRemoveThere, getIdentifier(), this );
                    }
                }

//                for( MeshObjectIdentifier hereNeighborIdentifier : neighborIdentifiers ) {
//                    if( neighborIdentifier.equals( hereNeighborIdentifier )) {
//                        continue;
//                    }
//                    RoleType [] hereNeighborRoleTypes = nMgr.getRoleTypesFor( this, hereNeighborIdentifier );
//                    if( hereNeighborRoleTypes == null || hereNeighborRoleTypes.length == 0 ) {
//                        continue;
//                    }
//                    MeshObject realOtherSide = findRelatedMeshObject( theMeshBase, hereNeighborIdentifier );
//                    checkPermittedUnbless(
//                            roleTypesToRemoveHere,
//                            neighborIdentifier,
//                            hereNeighborRoleTypes,
//                            realOtherSide.getIdentifier() );
//                }
//
//                if( realNeighbor != null ) {
//                    for( MeshObjectIdentifier otherNeighborIdentifier : otherNeighborIdentifiers ) {
//                        if( theIdentifier.equals( otherNeighborIdentifier )) {
//                            continue;
//                        }
//                        RoleType [] otherNeighborRoleTypes = nMgr.getRoleTypesFor( realNeighbor, otherNeighborIdentifier );
//                        if( otherNeighborRoleTypes == null || otherNeighborRoleTypes.length == 0 ) {
//                            continue;
//                        }
//                        MeshObject realOtherSide = findRelatedMeshObject( theMeshBase, otherNeighborIdentifier );
//                        checkPermittedUnbless(
//                                roleTypesToRemoveThere,
//                                otherNeighborIdentifier,
//                                otherNeighborRoleTypes,
//                                realOtherSide.getIdentifier() );
//                    }
//                }

                if( strict ) {
                    for( RoleType thisEnd : roleTypesToRemoveHere ) {
                        RoleType otherEnd = thisEnd.getInverseRoleType();

                        nMgr.removeRoleType( this, neighborIdentifier, thisEnd );
                        if( realNeighbor != null ) {
                            nMgr.removeRoleType( realNeighbor, theIdentifier, otherEnd );
                        }
                    }
                } else {
                    // same thing, but ignoring exceptions
                    for( RoleType thisEnd : roleTypesToRemoveHere ) {
                        RoleType otherEnd = thisEnd.getInverseRoleType();

                        try {
                            nMgr.removeRoleType( this, neighborIdentifier, thisEnd );
                        } catch( Throwable t ) {
                            if( log.isDebugEnabled() ) {
                                log.debug( t );
                            }
                        }
                        if( realNeighbor != null ) {
                            try {
                                nMgr.removeRoleType( realNeighbor, theIdentifier, otherEnd );
                            } catch( Throwable t ) {
                                if( log.isDebugEnabled() ) {
                                    log.debug( t );
                                }
                            }
                        }
                    }
                }

                RoleType [] newRoleTypesHere = nMgr.getRoleTypesFor( this, neighborIdentifier );
                fireTypesRemoved( oldRoleTypesHere, roleTypesToRemoveHere, newRoleTypesHere, neighborIdentifier, theMeshBase );

                if( realNeighbor != null ) {
                    RoleType [] newRoleTypesThere = nMgr.getRoleTypesFor( realNeighbor, theIdentifier );
                    realNeighbor.fireTypesRemoved( oldRoleTypesThere, roleTypesToRemoveThere, newRoleTypesThere, getIdentifier(), theMeshBase );
                }
            }
        }
        updateLastUpdated( timeUpdated, theTimeUpdated );
    }

    /**
      * Traverse a TraversalSpecification from this MeshObject to obtain a set of MeshObjects.
      * Specify whether relationships of equivalent MeshObjects should be considered as well.
      *
      * @param theTraverseSpec the TraversalSpecification to traverse
      * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
      *        if false, only this MeshObject will be used as the start
      * @return the set of MeshObjects found as a result of the traversal
      */
    public MeshObjectSet traverse(
            TraversalSpecification theTraverseSpec,
            boolean                considerEquivalents )
    {
        checkAlive();

        if( !( theTraverseSpec instanceof RoleType )) {
            return theTraverseSpec.traverse( this, considerEquivalents );
        }
        RoleType type = (RoleType) theTraverseSpec;

        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }
        
        AMeshObjectNeighborManager nMgr = getNeighborManager();

        AMeshBase                 realBase            = (AMeshBase) theMeshBase;
        MeshObjectIdentifier [][] neighborIdentifiers = createMeshObjectIdentifierArrayArray( starts.length );
        RoleType [][][]           roleTypes           = new RoleType[ starts.length ][][];

        int n = 0;
        for( int s=0 ; s<starts.length ; ++s ) {
            AMeshObject current = (AMeshObject) starts[s];
            synchronized( current ) {
                neighborIdentifiers[s] = nMgr.getNeighborIdentifiers( current );
                roleTypes[s]           = nMgr.getRoleTypes( current );
            }
            if( neighborIdentifiers[s] != null ) {
                n += neighborIdentifiers[s].length;
            }
        }

        MeshObjectSet ret;
        if( n == 0 ) {
            ret = realBase.getMeshObjectSetFactory().obtainEmptyImmutableMeshObjectSet();
        } else {
            MeshObjectIdentifier [] almost = createMeshObjectIdentifierArray( n );
            int                     max    = 0;

            // it's more efficient to first assemble all possible neighbors, and then subset based on permissions
            for( int s=0 ; s<neighborIdentifiers.length ; ++s ) {
                for( int i=0 ; i<neighborIdentifiers[s].length ; ++i ) {
                    if( roleTypes[s][i] != null ) {
                        for( int j=0 ; j<roleTypes[s][i].length ; ++j ) {
                            if( roleTypes[s][i][j].isSpecializationOfOrEquals( type ) ) {
                                if( !ArrayHelper.isIn( neighborIdentifiers[s][i], almost, 0, max, true )) {
                                    almost[max++] = neighborIdentifiers[s][i];
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if( max < almost.length ) {
                almost = ArrayHelper.copyIntoNewArray( almost, 0, max, MeshObjectIdentifier.class );
            }
            MeshObject [] almostRet  = findRelatedMeshObjects( theMeshBase, almost );
            MeshObject [] almostRet2 = new MeshObject[ almostRet.length ];

            int index = 0;
            for( MeshObject current : almostRet ) {
                try {
                    checkPermittedTraversal( type, current.getIdentifier(), current );
                    almostRet2[ index++ ] = current;
                } catch( NotPermittedException ex ) {
                    log.info( this, current, ex );
                } catch( Throwable t ) {
                    log.error( this, current, t );
                }
            }
            if( index < almostRet2.length ) {
                almostRet2 = ArrayHelper.copyIntoNewArray( almostRet2, 0, index, MeshObject.class );
            }
            ret = realBase.getMeshObjectSetFactory().createImmutableMeshObjectSet( almostRet2 );
        }

        updateLastRead();
        return ret;
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in. This will return only one
     * instance of the same RoleType object, even if the MeshObject participates in this RoleType
     * multiple times with different other MeshObjects. Specify whether equivalent MeshObjects
     * should be considered as well.
     * 
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public RoleType [] getRoleTypes(
            boolean considerEquivalents )
    {
        checkAlive();

        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }
        
        AMeshObjectNeighborManager nMgr = getNeighborManager();

        MeshObjectIdentifier [][] neighborIdentifiers = createMeshObjectIdentifierArrayArray( starts.length );
        RoleType [][][]           roleTypes           = new RoleType[ starts.length ][][];

        int n=0;
        for( int s=0 ; s<starts.length ; ++s ) {
            AMeshObject current = (AMeshObject) starts[s];
            synchronized( current ) {
                neighborIdentifiers[s] = nMgr.getNeighborIdentifiers( current );
                roleTypes[s]           = nMgr.getRoleTypes( current );
            }
            if( neighborIdentifiers[s] != null ) {
                n += neighborIdentifiers[s].length;
            }
        }

        RoleType [] ret = new RoleType[ n ];
        int         max = 0;

        for( int s=0 ; s<starts.length ; ++s ) {
            if( neighborIdentifiers[s] == null ) {
                continue;
            }
            MeshObject [] realOtherSides = findRelatedMeshObjects( theMeshBase, neighborIdentifiers[s] );

            if( roleTypes[s] != null ) {
                for( int i=0 ; i<roleTypes[s].length ; ++i ) {
                    if( roleTypes[s][i] != null ) {
                        for( int j=0 ; j<roleTypes[s][i].length ; ++j ) {
                            try {
                                checkPermittedTraversal(
                                        roleTypes[s][i][j],
                                        realOtherSides[i].getIdentifier(),
                                        realOtherSides[i] );

                                if( !ArrayHelper.isIn( roleTypes[s][i][j], ret, 0, max, true )) {
                                    ret[ max++ ] = roleTypes[s][i][j];
                                }
                            } catch( NotPermittedException ex ) {
                                log.info( ex );
                            }
                        }
                    }
                }
            }
        }
        if( max < n ) {
            ret = ArrayHelper.copyIntoNewArray( ret, 0, max, RoleType.class );
        }
        updateLastRead();

        return ret;
    }

    /**
     * Obtain the MeshTypeIdentifiers of the RoleTypes that this MeshObject plays with a
     * given neighbor MeshObject identified by its MeshObjectIdentifier.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the neighbor MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the identifiers of the RoleTypes
     * @throws NotRelatedException thrown if the specified MeshObject is not actually a neighbor
     */
    public MeshTypeIdentifier [] getRoleTypeIdentifiers(
            MeshObjectIdentifier neighborIdentifier,
            boolean              considerEquivalents )
        throws
            NotRelatedException
    {
        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        ArrayList<MeshTypeIdentifier> almost = new ArrayList<MeshTypeIdentifier>();
        boolean found = false;
        for( MeshObject start : starts ) {
            AMeshObject realStart = (AMeshObject) start;
            if( nMgr.hasNeighbors( realStart )) {
                for( MeshObjectIdentifier realStartNeighborIdentifier : nMgr.getNeighborIdentifiers( realStart )) {
                    if( neighborIdentifier.equals( realStartNeighborIdentifier )) {
                        found = true;
                        RoleType [] realStartNeighborRoleTypes = nMgr.getRoleTypesFor( realStart, realStartNeighborIdentifier );
                        if( realStartNeighborRoleTypes != null ) {
                            for( int j=0 ; j<realStartNeighborRoleTypes.length ; ++j ) {
                                if( !almost.contains( realStartNeighborRoleTypes[j].getIdentifier() )) {
                                    almost.add( realStartNeighborRoleTypes[j].getIdentifier() );
                                }
                            }
                        }
                    }
                }
            }
        }
        if( found ) {
            MeshTypeIdentifier [] ret = ArrayHelper.copyIntoNewArray( almost, MeshTypeIdentifier.class );
            return ret;
        } else {
            throw new NotRelatedException( theMeshBase, theMeshBase.getIdentifier(), this, theIdentifier, null, neighborIdentifier );
        }
    }
    
    /**
     * Obtain the Roles that this MeshObject currently participates in.
     * Specify whether relationships of equivalent MeshObjects
     * should be considered as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     *        if false, only this MeshObject will be used as the start
     * @return the Roles that this MeshObject currently participates in.
     */
    public Role [] getRoles(
            boolean considerEquivalents )
    {
        checkAlive();

        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        MeshObjectIdentifier [][] neighborIdentifiers = createMeshObjectIdentifierArrayArray( starts.length );
        RoleType [][][]           roleTypes           = new RoleType[ starts.length ][][];

        int n=0;
        for( int s=0 ; s<starts.length ; ++s ) {
            AMeshObject current = (AMeshObject) starts[s];
            synchronized( current ) {
                neighborIdentifiers[s] = nMgr.getNeighborIdentifiers( current );
                roleTypes[s]           = nMgr.getRoleTypes( current );
            }
            if( roleTypes[s] != null ) {
                for( int i=0 ; i<roleTypes[s].length ; ++i ) {
                    if( roleTypes[s][i] != null ) {
                        n += roleTypes[s][i].length;
                    }
                }
            }
        }

        Role [] ret = new Role[ n ];
        int     max = 0;

        for( int s=0 ; s<starts.length ; ++s ) {
            if( neighborIdentifiers[s] != null ) {
                for( int i=0 ; i<roleTypes[s].length ; ++i ) {
                    if( roleTypes[s][i] != null ) {
                        for( int j=0 ; j<roleTypes[s][i].length ; ++j ) {
                            try {
                                MeshObject thisNeighbor = theMeshBase.findMeshObjectByIdentifier( neighborIdentifiers[s][i] );

                                checkPermittedTraversal(
                                        roleTypes[s][i][j],
                                        neighborIdentifiers[s][i],
                                        thisNeighbor );
                                if( thisNeighbor != null ) {
                                    ret[ max++ ] = Role.create( this, roleTypes[s][i][j], thisNeighbor );
                                } else {
                                    ret[ max++ ] = Role.create( this, roleTypes[s][i][j], neighborIdentifiers[s][i] );
                                }
                            } catch( NotPermittedException ex ) {
                                log.info( ex );
                            }
                        }
                    }
                }
            }
        }
        if( max < n ) {
            ret = ArrayHelper.copyIntoNewArray( ret, 0, max, Role.class );
        }
        updateLastRead();

        return ret;
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * specified other MeshObject.
     * Specify whether relationships of equivalent MeshObjects should be considered
     * as well.
     *
     * @param neighbor the other MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public RoleType [] getRoleTypes(
            MeshObject neighbor,
            boolean    considerEquivalents )
    {
        return getRoleTypes( neighbor.getIdentifier(), considerEquivalents );
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * specified other MeshObject.
     * Specify whether relationships of equivalent MeshObjects should be considered
     * as well.
     *
     * @param neighborIdentifier identifier the other MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public RoleType [] getRoleTypes(
            MeshObjectIdentifier neighborIdentifier,
            boolean              considerEquivalents )
    {
        if( neighborIdentifier == null ) {
            throw new NullPointerException( "neighborIdentifier is null" );
        }
        MeshObject neighbor = theMeshBase.findMeshObjectByIdentifier( neighborIdentifier );
                // if we have it here, deal with it. If we don't, don't.

        checkAlive();
        if( neighbor != null ) {
            neighbor.checkAlive();
        }

        if( neighbor != null && theMeshBase != neighbor.getMeshBase() ) {
            throw new WrongMeshBaseException( getMeshBase(), neighbor.getMeshBase() );
        }
        
        MeshObject [] starts;
        if( considerEquivalents ) {
            starts = getEquivalents().getMeshObjects();
        } else {
            starts = new MeshObject[] { this };
        }
        
        AMeshObjectNeighborManager nMgr = getNeighborManager();

        MeshObjectIdentifier [][] neighborIdentifiers = createMeshObjectIdentifierArrayArray( starts.length );
        RoleType [][][]           roleTypes           = new RoleType[ starts.length ][][];

        int n=0;
        for( int s=0 ; s<starts.length ; ++s ) {
            AMeshObject current = (AMeshObject) starts[s];
            synchronized( current ) {
                neighborIdentifiers[s] = nMgr.getNeighborIdentifiers( current );
                roleTypes[s]  = nMgr.getRoleTypes( current );
            }
            if( roleTypes[s] != null ) {
                for( int i=0 ; i<roleTypes[s].length ; ++i ) {
                    if( roleTypes[s][i] != null ) {
                        n += roleTypes[s][i].length;
                    }
                }
            }
        }

        RoleType [] ret;
        if( n==0 ) {
            ret = new RoleType[0];

        } else {
            int max = 0;
            ret     = new RoleType[ n ];

            for( int s=0 ; s<starts.length ; ++s ) {

                if( neighborIdentifiers[s] == null ) {
                    continue;
                }

                for( int i=0 ; i<neighborIdentifiers[s].length ; ++i ) {
                    if( neighborIdentifier.equals( neighborIdentifiers[s][i] )) {
                        if( roleTypes[s][i] != null ) {
                            for( int j=0 ; j<roleTypes[s][i].length ; ++j ) {
                                try {
                                    checkPermittedTraversal(
                                            roleTypes[s][i][j],
                                            neighborIdentifier,
                                            neighbor );

                                    ret[max++] = roleTypes[s][i][j];
                                } catch( NotPermittedException ex ) {
                                    log.info( ex );
                                }
                            }
                        }
                    }
                }
            }
            if( max < ret.length ) {
                ret = ArrayHelper.copyIntoNewArray( ret, 0, max, RoleType.class );
            }
        }
        updateLastRead();

        return ret;
    }

    /**
     * Add another MeshObject as an equivalent. All MeshObjects that are already equivalent
     * to this MeshObject, and all MeshObjects that are already equivalent to the newly
     * added MeshObject, are now equivalent.
     *
     * @param equiv the new equivalent
     * @throws EquivalentAlreadyException thrown if the provided MeshObject is already an equivalent of this MeshObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void addAsEquivalent(
            MeshObject equiv )
        throws
            EquivalentAlreadyException,
            TransactionException,
            NotPermittedException
    {
        internalAddAsEquivalent( equiv.getIdentifier(), true, 0L );
    }

    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param equivIdentifier identifier of the new equivalent
     * @param isMaster is true, this is the master replica
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws EquivalentAlreadyException thrown if the provided MeshObject is already an equivalent of this MeshObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected synchronized void internalAddAsEquivalent(
            MeshObjectIdentifier equivIdentifier,
            boolean              isMaster,
            long                 timeUpdated )
        throws
            EquivalentAlreadyException,
            TransactionException,
            NotPermittedException
    {
        if( equivIdentifier == null ) {
            throw new NullPointerException( "equivIdentifier is null" );
        }
        MeshObject equiv = theMeshBase.findMeshObjectByIdentifier( equivIdentifier );
                // if we have it here, deal with it. If we don't, don't.

        checkAlive();
        if( equiv != null ) {
            equiv.checkAlive();
        }

        checkTransaction();

        if( this == equiv ) {
            throw new EquivalentAlreadyException( this, equiv );
        }
        
        if( equiv != null && theMeshBase != equiv.getMeshBase() ) {
            throw new WrongMeshBaseException( getMeshBase(), equiv.getMeshBase() );
        }

        AMeshObject temp = this;
        while( ( temp = temp.getLeftEquivalentObject( theMeshBase ) ) != null ) {
            if( temp == equiv ) {
                throw new EquivalentAlreadyException( this, equiv );
            }
        }
        temp = this;
        while( ( temp = temp.getRightEquivalentObject( theMeshBase ) ) != null ) {
            if( temp == equiv ) {
                throw new EquivalentAlreadyException( this, equiv );
            }
        }

        checkPermittedAddAsEquivalent( equivIdentifier, equiv );

        // now insert, being mindful that we might be joining to chains here
        
        AMeshObject leftMostHere = this;
        while( ( temp = leftMostHere.getLeftEquivalentObject( theMeshBase )) != null ) {
            leftMostHere = temp;
        }

        AMeshObject rightMostThere = (AMeshObject) equiv;
        while( ( temp = rightMostThere.getRightEquivalentObject( theMeshBase ) ) != null ) {
            rightMostThere = temp;
        }

        if( rightMostThere.theEquivalenceSetPointers == null ) {
            rightMostThere.theEquivalenceSetPointers = createMeshObjectIdentifierArray( 2 );
        }
        rightMostThere.theEquivalenceSetPointers[1] = leftMostHere.getIdentifier();

        if( leftMostHere.theEquivalenceSetPointers == null ) {
            leftMostHere.theEquivalenceSetPointers = createMeshObjectIdentifierArray( 2 );
        }
        leftMostHere.theEquivalenceSetPointers[0] = rightMostThere.getIdentifier();
        
        updateLastUpdated( timeUpdated, theTimeUpdated );
    }
    
    /**
     * Obtain the set of MeshObjects, including this one, that are equivalent.
     * This always returns at least this MeshObject.
     *
     * @return the set of MeshObjects that are equivalent
     */
    public synchronized MeshObjectSet getEquivalents()
    {
        checkAlive();

        AMeshBase realBase = (AMeshBase) theMeshBase;
        
        if( theEquivalenceSetPointers == null ) {
            return realBase.getMeshObjectSetFactory().createSingleMemberImmutableMeshObjectSet( this );
        }

        ArrayList<MeshObject> toTheLeft  = new ArrayList<MeshObject>();
        ArrayList<MeshObject> toTheRight = new ArrayList<MeshObject>();

        AMeshObject current = this;
        while( ( current = current.getLeftEquivalentObject( theMeshBase ) ) != null ) {
            toTheLeft.add( current );
        }
        current = this;
        toTheRight.add( current );
        while( ( current = current.getRightEquivalentObject( theMeshBase ) ) != null ) {
            toTheRight.add( current );
        }
        
        // we revert the direction of the toTheLeft, in order to make debugging easier
        ArrayList<MeshObject> allEquivalents = new ArrayList<MeshObject>( toTheLeft.size() );
        for( int i=toTheLeft.size()-1 ; i>=0 ; --i ) {
            allEquivalents.add( toTheLeft.get( i ));
        }
        for( MeshObject loop : toTheRight ) {
            allEquivalents.add( loop );
        }
        
        MeshObjectSet ret = realBase.getMeshObjectSetFactory().createImmutableMeshObjectSet(
                ArrayHelper.copyIntoNewArray( allEquivalents, MeshObject.class ));
        
        updateLastRead();
        
        return ret;
    }
    
    /**
     * Remove this MeshObject as an equivalent from the set of equivalents. If this MeshObject
     * is not currently equivalent to any other MeshObject, this does nothing.
     *
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void removeAsEquivalent()
        throws
            TransactionException,
            NotPermittedException
    {
        internalRemoveAsEquivalent( true, 0L );
    }
    
    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param isMaster if true, this is the master replica
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected void internalRemoveAsEquivalent(
            boolean isMaster,
            long    timeUpdated )
        throws
            TransactionException,
            NotPermittedException
    {
        checkAlive();
        checkTransaction();
        
        checkPermittedRemoveAsEquivalent();
        
        AMeshObject theLeft  = getLeftEquivalentObject( theMeshBase );
        AMeshObject theRight = getRightEquivalentObject( theMeshBase );

        if( theLeft != null ) {
            theLeft.theEquivalenceSetPointers[1] = ( theRight != null ) ? theRight.getIdentifier() : null;
        }
        if( theRight != null ) {
            theRight.theEquivalenceSetPointers[0] = ( theLeft != null ) ? theLeft.getIdentifier() : null;
        }
        
        theEquivalenceSetPointers = null;

        updateLastUpdated( timeUpdated, theTimeUpdated );
    }

    /**
     * For clients that know we are an AMeshObject, we can also return our internal representation.
     * Please do not modify the content of this array, bad things may happen.
     *
     * @return the MeshObjectIdentifiers of the left and right equivalent MeshObject. Either may be null. The
     *         return value may be null, too.
     */
    public MeshObjectIdentifier[] getInternalEquivalentList()
    {
        return theEquivalenceSetPointers;
    }

    /**
     * Delete this MeshObject. This must only be invoked by our MeshObjectLifecycleManager
     * and thus is defined down here, not higher up in the inheritance hierarchy.
     * 
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     */
    public void delete()
        throws
            TransactionException
    {
        if( theMeshTypes != null ) {
            Set<EntityType> keySet = theMeshTypes.keySet();

            for( EntityType current : keySet ) {
                TypeInitializer init = createTypeInitializer( current );
                init.cascadingDelete();
            }
        }

        internalDelete( true, 0L );
    }

    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param isMaster true if this is the master replica
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     */
    protected void internalDelete(
            boolean isMaster,
            long    timeUpdated )
        throws
            TransactionException
    {
        if( theMeshBase == null ) {
            // this is a loop, do nothing
            return;
        }
        MeshBase oldMeshBase = theMeshBase;

        AMeshObject theLeft  = getLeftEquivalentObject( oldMeshBase );
        AMeshObject theRight = getRightEquivalentObject( oldMeshBase );
        if( theLeft != null ) {
            if( theRight != null ) {
                // we are in the middle
                theLeft.theEquivalenceSetPointers[1]  = theRight.theIdentifier;
                theRight.theEquivalenceSetPointers[0] = theLeft.theIdentifier;
            } else {
                if( theLeft.theEquivalenceSetPointers[0] == null ) {
                    theLeft.theEquivalenceSetPointers = null; // don't need them any more
                } else {
                    theLeft.theEquivalenceSetPointers[1] = null;
                }
            }
        } else if( theRight != null ) {
            if( theRight.theEquivalenceSetPointers[1] == null ) {
                theRight.theEquivalenceSetPointers = null; // don't need them any more
            } else {
                theRight.theEquivalenceSetPointers[0] = null;
            }
        }

        AMeshObjectNeighborManager nMgr = getNeighborManager();

        if( nMgr.hasNeighbors( this ) ) {
            // we got to copy this array, otherwise we change it under our own feet
            MeshObjectIdentifier [] neighborIdentifiers = nMgr.getNeighborIdentifiers( this );

            for( int i=0 ; i<neighborIdentifiers.length ; ++i ) {
                if( neighborIdentifiers[i] != null ) {
                    try {
                        internalUnrelate( neighborIdentifiers[i], oldMeshBase, isMaster, false, timeUpdated );
                    } catch( NotRelatedException ex ) {
                        // that's fine, ignore
                    } catch( NotPermittedException ex ) {
                        log.error( ex );
                    }
                } else {
                    // that's fine, ignore -- may happen when a Probe drops both source and
                    // destination at the same time
                }
            }
        }

        MeshObjectIdentifier canonicalMeshObjectName = getIdentifier();
        
        theMeshBase = null; // this needs to happen rather late so the other code still works

        fireDeleted( oldMeshBase, canonicalMeshObjectName, System.currentTimeMillis() );
    }

    /**
     * Internal helper to obtain the left equivalent MeshObject, if any.
     * We pass in the MeshBase to use because this may be invoked when a MeshObject's member
     * variable has been zero'd out already.
     *
     * @param mb the MeshBase to use
     * @return the left equivalent MeshObject, if any
     */
    protected AMeshObject getLeftEquivalentObject(
            MeshBase mb )
    {
        if( theEquivalenceSetPointers == null ) {
            return null;
        }
        if( theEquivalenceSetPointers[0] == null ) {
            return null;
        }
        MeshObject ret = findRelatedMeshObject( mb, theEquivalenceSetPointers[0] );
        return (AMeshObject) ret;
    }

    /**
     * Internal helper to obtain the right equivalent MeshObject, if any.
     * We pass in the MeshBase to use because this may be invoked when a MeshObject's member
     * variable has been zero'd out already.
     *
     * @param mb the MeshBase to use
     * @return the left equivalent MeshObject, if any
     */
    protected AMeshObject getRightEquivalentObject(
            MeshBase mb )
    {
        if( theEquivalenceSetPointers == null ) {
            return null;
        }
        if( theEquivalenceSetPointers[1] == null ) {
            return null;
        }
        MeshObject ret = findRelatedMeshObject( mb, theEquivalenceSetPointers[1] );
        return (AMeshObject) ret;
    }

    /**
     * Obtain the same MeshObject as ExternalizedMeshObject so it can be easily serialized.
     * 
     * @return this MeshObject as ExternalizedMeshObject
     */
    public SimpleExternalizedMeshObject asExternalized()
    {
        AMeshObjectNeighborManager nMgr = getNeighborManager();

        MeshTypeIdentifier [] types;
        if( theMeshTypes != null && theMeshTypes.size() > 0 ) {
            types = new MeshTypeIdentifier[ theMeshTypes.size() ];

            int i=0;
            for( EntityType current : theMeshTypes.keySet() ) {
                types[i++] = current.getIdentifier();
            }
        } else {
            types = null;
        }
        
        MeshTypeIdentifier [] propertyTypes;
        PropertyValue      [] propertyValues;
        if( theProperties != null && theProperties.size() > 0 ) {
            propertyTypes  = new MeshTypeIdentifier[ theProperties.size() ];
            propertyValues = new PropertyValue[ propertyTypes.length ];

            int i=0;
            for( PropertyType current : theProperties.keySet() ) {
                propertyTypes[i]  = current.getIdentifier();
                propertyValues[i] = theProperties.get( current );
                ++i;
            }
        } else {
            propertyTypes  = null;
            propertyValues = null;
        }
        
        MeshTypeIdentifier [][] roleTypeIdentifiers;
        if( nMgr.hasNeighbors( this )) {
            RoleType [][] roleTypes = nMgr.getRoleTypes( this );
            roleTypeIdentifiers = new MeshTypeIdentifier[ roleTypes.length][];
            for( int i=0 ; i<roleTypes.length ; ++i ) {
                if( roleTypes[i] != null && roleTypes[i].length > 0 ) {
                    roleTypeIdentifiers[i] = new MeshTypeIdentifier[ roleTypes[i].length ];
                    for( int j=0 ; j<roleTypes[i].length ; ++j ) {
                        roleTypeIdentifiers[i][j] = roleTypes[i][j].getIdentifier();
                    }
                }
            }
        } else {
            roleTypeIdentifiers = null;
        }
        
        MeshObjectIdentifier [] equivalents;
        if( theEquivalenceSetPointers == null ) {
            equivalents = null;
        } else {
            int count = 0;
            if( theEquivalenceSetPointers[0] != null ) {
                ++count;
            }
            if( theEquivalenceSetPointers[1] != null ) {
                ++count;
            }
            equivalents = createMeshObjectIdentifierArray( count );
            if( theEquivalenceSetPointers[0] != null ) {
                equivalents[0] = theEquivalenceSetPointers[0];
                if( theEquivalenceSetPointers[1] != null ) {
                    equivalents[1] = theEquivalenceSetPointers[1];
                }
            } else if( theEquivalenceSetPointers[1] != null ) {
                equivalents[0] = theEquivalenceSetPointers[1];
            }
        }
        
        SimpleExternalizedMeshObject ret = SimpleExternalizedMeshObject.create(
                getIdentifier(),
                types,
                theTimeCreated,
                theTimeUpdated,
                theTimeRead,
                theTimeExpires,
                propertyTypes,
                propertyValues,
                nMgr.getNeighborIdentifiers( this ),
                roleTypeIdentifiers,
                equivalents );

        return ret;
    }

    /**
     * Obtain a String that renders this instance suitable for showing to a user.
     *
     * @param types the EntityTypes to be consulted, in sequence, until a non-null result is found
     * @return the user-visible String representing this instance, or null if none could be found
     */
    public String getUserVisibleString(
            EntityType [] types )
    {
        String ret = null;
        if( types != null && types.length > 0 ) {
            synchronized( this ) {
                for( EntityType current : types ) {
                    WeakReference<TypedMeshObjectFacade> ref    = theMeshTypes.get( current );
                    TypedMeshObjectFacade                facade = ( ref != null ) ? ref.get() : null;

                    if( facade == null ) {
                        facade = theMeshBase.getMeshBaseLifecycleManager().createTypedMeshObjectFacade( this, current );
                        theMeshTypes.put( current, new WeakReference<TypedMeshObjectFacade>( facade ));
                    }

                    ret = facade.get_UserVisibleString();
                    if( ret != null ) {
                        break;
                    }
                }
            }
        }

        return ret;
    }

    /**
     * Obtain a String that renders this instance suitable for showing to a user.
     *
     * @return the user-visible String representing this instance
     */
    public String getUserVisibleString()
    {
        // if it has a name, return that
        try {
            StringValue name = (StringValue) getPropertyValueByName( "Name" );
            if( name != null && name.value().length() > 0 ) {
                return name.value();
            }
        } catch( MeshTypeNotFoundException ex ) {
            // ignore
        } catch( NotPermittedException ex ) {
            // ignore
        } catch( ClassCastException ex ) {
            // ignore
        }

        String ret = getUserVisibleString( getTypes() ); // that makes a random sequence, but that's the best we can do
        return ret;
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return String representation
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String userVisible = getUserVisibleString();
        String ret;

        if( userVisible != null ) {
            ret = rep.formatEntry(
                    getClass(), // dispatch to the right subtype
                    StringRepresentation.DEFAULT_ENTRY,
                    pars,
            /* 0 */ this,
            /* 1 */ getMeshBase(),
            /* 2 */ userVisible );

        } else {
            ret = theIdentifier.toStringRepresentation( rep, pars );
        }
        return ret;
    }

    /**
     * Obtain the start part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkStart(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String     contextPath         = null;
        String     additionalArguments = null;
        String     target              = null;
        String     title               = null;

        if( pars != null ) {
            contextPath         = (String) pars.get( StringRepresentationParameters.WEB_RELATIVE_CONTEXT_KEY );
            target              = (String) pars.get( StringRepresentationParameters.LINK_TARGET_KEY );
            title               = (String) pars.get( StringRepresentationParameters.LINK_TITLE_KEY );
            additionalArguments = (String) pars.get( StringRepresentationParameters.HTML_URL_ADDITIONAL_ARGUMENTS );
        }

        boolean isDefaultMeshBase = true;
        if( pars != null ) {
            isDefaultMeshBase = getMeshBase().equals( pars.get( MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY ));
        }
        boolean isHomeObject = this == getMeshBase().getHomeObject();

        String key;
        if( isDefaultMeshBase ) {
            if( isHomeObject ) {
                key = DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY;
            } else {
                key = DEFAULT_MESH_BASE_LINK_START_ENTRY;
            }
        } else {
            if( isHomeObject ) {
                key = NON_DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY;
            } else {
                key = NON_DEFAULT_MESH_BASE_LINK_START_ENTRY;
            }
        }
        if( target == null ) {
            target = "_self";
        }

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                key,
                pars,
        /* 0 */ this,
        /* 1 */ getIdentifier(),
        /* 2 */ contextPath,
        /* 3 */ getMeshBase(),
        /* 4 */ additionalArguments,
        /* 5 */ target,
        /* 6 */ title );

        return ret;
    }

    /**
     * Obtain the end part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String contextPath = null;

        if( pars != null ) {
            contextPath = (String) pars.get( StringRepresentationParameters.WEB_RELATIVE_CONTEXT_KEY );
        }

        boolean isDefaultMeshBase = true;
        if( pars != null ) {
            isDefaultMeshBase = getMeshBase().equals( pars.get( MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY ));
        }
        boolean isHomeObject = this == getMeshBase().getHomeObject();

        String key;
        if( isDefaultMeshBase ) {
            if( isHomeObject ) {
                key = DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY;
            } else {
                key = DEFAULT_MESH_BASE_LINK_END_ENTRY;
            }
        } else {
            if( isHomeObject ) {
                key = NON_DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY;
            } else {
                key = NON_DEFAULT_MESH_BASE_LINK_END_ENTRY;
            }
        }

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                key,
                pars,
        /* 0 */ this,
        /* 1 */ getIdentifier(),
        /* 2 */ contextPath,
        /* 3 */ getMeshBase() );

        return ret;
    }

    /**
     * Find a manager for the MeshObject's neighbors.
     *
     * @return the AMeshObjectNeighborManager
     */
    public AMeshObjectNeighborManager getNeighborManager()
    {
        return AMeshObjectNeighborManager.SINGLETON;
    }

    /**
     * Helper method to create an array of MeshObjectIdentifier.
     * This may be overridden in subclasses.
     *
     * @param n the desired size of the array
     * @return the array
     */
    protected MeshObjectIdentifier [] createMeshObjectIdentifierArray(
            int n )
    {
        return new MeshObjectIdentifier[ n ];
    }

    /**
     * Helper method to create an array of array of MeshObjectIdentifier.
     * This may be overridden in subclasses.
     *
     * @param n the desired size of the array
     * @return the array
     */
    protected MeshObjectIdentifier [][] createMeshObjectIdentifierArrayArray(
            int n )
    {
        return new MeshObjectIdentifier[ n ][];
    }

    /**
     * The set of MeshObjecs to which this MeshObject is directly related. This is
     * expressed as a set of MeshObjectIdentifiers in order to not prevent garbage collection.
     */
    protected MeshObjectIdentifier [] theNeighborIdentifiers;

    /**
     * The set of sets of RoleTypes that goes with theOtherSides.
     */
    protected RoleType [][] theNeighborRoleTypes;
    
    /**
     * The left and right MeshObject in the equivalence set. This member is either null,
     * or MeshObjectIdentifier[2], which may have one or two entries, the first representing
     * the "left" side in the doubly-linked list, the second representing the "right"
     * side.
     */
    protected MeshObjectIdentifier [] theEquivalenceSetPointers;

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_START_ENTRY = "DefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY = "DefaultMeshBaseHomeLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_END_ENTRY = "DefaultMeshBaseLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY = "DefaultMeshBaseHomeLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_START_ENTRY = "NonDefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY = "NonDefaultMeshBaseHomeLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_END_ENTRY = "NonDefaultMeshBaseLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY = "NonDefaultMeshBaseHomeLinkEndString";
}
