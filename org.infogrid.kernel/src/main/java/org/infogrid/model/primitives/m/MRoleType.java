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

package org.infogrid.model.primitives.m;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.RoleTypeGuard;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;

/**
  * The RoleType played by an EntityType in a RelationshipType (between another EntityType and itself).
  * In-memory implementation.
  */
public abstract class MRoleType
        extends
            MMeshType
        implements
            RoleType
{
    private static final Log log = Log.getLogInstance( MRoleType.class ); // our own, private logger

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     * @param relationship the RelationshipType to which this RoleType will belong
     * @param entity the EntityType related to this RoleType
     * @param multiplicity the multiplicity of this RoleType
     * @param localRoleTypeGuardClassNames the class names for the local RoleTypeGuards to be enforced
     */
    protected MRoleType(
            MeshTypeIdentifier identifier,
            MRelationshipType  relationship,
            MEntityType        entity,
            MultiplicityValue  multiplicity,
            String         []  localRoleTypeGuardClassNames )
    {
        super( identifier );

        theRelationshipType = relationship;
        theEntityType       = entity;
        theMultiplicity     = multiplicity;

        theLocalRoleTypeGuardClassNames = localRoleTypeGuardClassNames;
        theLocalRoleTypeGuards          = null;

        if( theEntityType != null ) {
            theEntityType.addLocalRoleType( this );
        }

        setDoGenerateInterfaceCode(      BooleanValue.FALSE );
        setDoGenerateImplementationCode( BooleanValue.FALSE );
    }

    /**
      * Obtain the SubjectArea in which this CollectableMeshType is defined.
      *
      * @return the SubjectArea in which this CollectableMeshType is defined
      */
    @Override
    public final SubjectArea getSubjectArea()
    {
        return theRelationshipType.getSubjectArea();
    }

    /**
      * Obtain the RelationshipType that this RoleType belongs to.
      *
      * @return the RelationshipType that this RoleType belongs to
      */
    @Override
    public final RelationshipType getRelationshipType()
    {
        return theRelationshipType;
    }

    /**
      * Obtain the EntityType that plays this RoleType.
      *
      * @return the EntityType that this RoleType belongs to
      */
    @Override
    public final EntityType getEntityType()
    {
        return theEntityType;
    }

    /**
     * Obtain the "other" RoleType of this RelationshipType. A RelationshipType has a source
     * and a destination RoleType. If this is applied on a source RoleType, this will obtain
     * the destination RoleType, and vice versa. For unary RelationshipTypes, this returns this.
     *
     * @return the RoleType on the other end of the RelationshipType that this RoleType belongs to
     */
    @Override
    public final RoleType getInverseRoleType()
    {
        if( theRelationshipType.getSource() == this ) {
            return theRelationshipType.getDestination();
        } else {
            return theRelationshipType.getSource();
        }
    }

    /**
     * Obtain the EntityType on the other end of this RelationshipType. A RelationshipType has a source
     * and a destination RoleType. If this is applied on a source RoleType, this will obtain
     * the EntityType related to the destination RoleType, and vice versa.
     *
     * @return the EntityType on the other end of the RelationshipType that this RoleType belongs to
     */
    @Override
    public EntityType getOtherEntityType()
    {
        return getInverseRoleType().getEntityType();
    }

    /**
     * Obtain the multiplicity of this RoleType. The multiplicity of a RoleType is the number of
     * entities that can be reached from a start entity by traversing
     * the RoleType. Multiplicities have a minimum and a maximum value. Note that multiplicities
     * on RoleTypes are the opposite numbers of what is typically written next to a line in an
     * entity-relationship (or class-association) diagram.
     *
     * @return the multiplicity of this RoleType
     */
    @Override
    public final MultiplicityValue getMultiplicity()
    {
        return theMultiplicity;
    }

    /**
     * Set the RoleTypes that we are refining directly.
     *
     * @param newValue the set of RoleTypes that we are refining directly
     * @see #getDirectSuperRoleTypes
     */
    public final void setDirectSuperRoleTypes(
            MRoleType [] newValue )
    {
        theDirectSuperRoleTypes = newValue;
    }

    /**
     * Obtain the RoleTypes that this RoleType refines directly.
     *
     * @return the RoleTypes that this RoleType refines directly. In most cases, this will be just one.
     * @see #setDirectSuperRoleTypes
     */
    @Override
    public final RoleType [] getDirectSuperRoleTypes()
    {
        return theDirectSuperRoleTypes;
    }

    /**
     * Determine whether this RoleType is a specialization, or the same
     * of the passed-in TraversalSpecification. This is analogous to
     * AttributableMeshType.isSubtypeOfOrEquals().
     *
     * @param other the TraversalSpecification to compare against
     * @return true if this RoleType is a specialization of the passed-in TraversalSpecification
     */
    @Override
    public final boolean isSpecializationOfOrEquals(
            TraversalSpecification other )
    {
        if( other instanceof RoleType ) {
            if( this == other ) {
                return true;
            }

            for( int i=0 ; i<theDirectSuperRoleTypes.length ; ++i ) {
                if( theDirectSuperRoleTypes[i].isSpecializationOfOrEquals( other )) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Use this TraversalSpecification to traverse from the passed-in start MeshObject
     * to related MeshObjects. This method is defined on TraversalSpecification, so
     * different implementations of TraversalSpecification can implement different ways
     * of doing this.
     *
     * @param start the start MeshObject for the traversal
     * @return the result of the traversal
     */
    @Override
    public final MeshObjectSet traverse(
            MeshObject start )
    {
        return start.traverse( this );
    }

    /**
     * Use this TraversalSpecification to traverse from the passed-in start MeshObject
     * to related MeshObjects. This method is defined on TraversalSpecification, so
     * different implementations of TraversalSpecification can implement different ways
     * of doing this.
     *
     * @param start the start MeshObject for the traversal
     * @return the result of the traversal
     */
    @Override
    public final MeshObjectSet traverse(
            MeshObject start,
            boolean    considerEquivalents )
    {
        return start.traverse( this, considerEquivalents );
    }

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects. This method is defined on TraversalSpecification, so
      * different implementations of TraversalSpecification can implement different ways
      * of doing this.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @return the result of the traversal
      */
    @Override
    public final MeshObjectSet traverse(
            MeshObjectSet theSet )
    {
        return theSet.traverse( this );
    }

    /**
      * Use this TraversalSpecification to traverse from the passed-in start MeshObjectSet
      * to related MeshObjects. This method is defined on TraversalSpecification, so
      * different implementations of TraversalSpecification can implement different ways
      * of doing this.
      *
      * @param theSet the start MeshObjectSet for the traversal
      * @return the result of the traversal
      */
    @Override
    public final MeshObjectSet traverse(
            MeshObjectSet theSet,
            boolean       considerEquivalents )
    {
        return theSet.traverse( this, considerEquivalents );
    }

    /**
     * Determine whether a given event, with a source of from where we traverse the
     * TraversalSpecification, may affect the result of the traversal.
     *
     * @param toResolveAgainst the MeshBase to resolve the event against
     * @param theEvent the event that we consider
     * @return true if this event may affect the result of traversing from the Entity
     *         that sent this event
     */
    @Override
    public final boolean isAffectedBy(
            MeshBase                  toResolveAgainst,
            MeshObjectRoleChangeEvent theEvent )
    {
        RoleType [] firedRoles = theEvent.getDeltaValue();

        for( RoleType current : firedRoles ) {
            if( current.isSpecializationOfOrEquals( this )) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtain a string identifying this TraversalSpecification that can be shown to the user.
     *
     * @return s user-visible string representing this object
     */
    public String getUserVisibleString()
    {
        StringValue almost = getUserVisibleName();
        if( almost != null ) {
            return almost.value();
        } else {
            return null;
        }
    }

    /**
     * Obtain the set of RoleConstraints locally defined on this RoleType.
     *
     * @return the set of RoleConstraints locally defined on this RoleType
     */
    @Override
    public synchronized RoleTypeGuard [] getLocalRoleTypeGuards()
    {
        if( theLocalRoleTypeGuards == null ) {
            
            ClassLoader loader      = theRelationshipType.getSubjectArea().getClassLoader();
            theLocalRoleTypeGuards = new RoleTypeGuard[ theLocalRoleTypeGuardClassNames.length ];

            for( int i=0 ; i<theLocalRoleTypeGuards.length ; ++i ) {
                try {
                    Class<?> clazz = Class.forName( theLocalRoleTypeGuardClassNames[i], true, loader );
                    theLocalRoleTypeGuards[i] = (RoleTypeGuard) clazz.newInstance();
                
                } catch( ClassNotFoundException ex ) {
                    log.error( ex );
                } catch( IllegalAccessException ex ) {
                    log.error( ex );
                } catch( InstantiationException ex ) {
                    log.error( ex.getCause() );
                }
            }
        }
        return theLocalRoleTypeGuards;
    }

    /**
     * Obtain the set of RoleTypeGuards applicable on this RoleType. These include
     * both the locally defined RoleTypeGuards and the inherited ones.
     *
     * @return the set of applicable RoleTypeGuards
     */
    @Override
    public synchronized RoleTypeGuard [] getAllRoleTypeGuards()
    {
        if( theAllRoleTypeGuards == null ) {
            try {
                ArrayList<RoleTypeGuard> almostRet = internalGetAllRoleTypeGuards(); // can throw InheritanceConflictException

                theAllRoleTypeGuards = ArrayHelper.copyIntoNewArray(
                        almostRet,
                        RoleTypeGuard.class );
            } finally {
                if( theAllRoleTypeGuards == null ) {
                    theAllRoleTypeGuards = new RoleTypeGuard[0];
                }
            }
        }
        return theAllRoleTypeGuards;
    }

    /**
     * Obtain transitive closure of RoleTypeGuards.
     *
     * @return an ArrayList containing a transitive closure of RoleTypeGuards
     * @throws InheritanceConflictException thrown if an internal inheritance conflict was detected
     */
    protected final ArrayList<RoleTypeGuard> internalGetAllRoleTypeGuards()
            throws
                InheritanceConflictException
    {
        ArrayList<RoleTypeGuard> ret = new ArrayList<RoleTypeGuard>( 20 ); // fudge factor
        for( RoleTypeGuard current : getLocalRoleTypeGuards() ) {
            ret.add( current );
        }

        for( int i=0 ; i<theDirectSuperRoleTypes.length ; ++i ) {
            RoleTypeGuard [] inherited = theDirectSuperRoleTypes[i].getAllRoleTypeGuards();

            for( int j=0 ; j<inherited.length ; ++j ) {
                
                if( !ret.contains( inherited[j] )) {
                    ret.add( inherited[j] );
                }
            }
        }

        return ret;
    }
    
    /**
     * Obtain the class names of the set of RoleTypeGuards on this RoleType.
     *
     * @return the class names of the set of RoleTypeGuards on this RoleType
     */
    @Override
    public String [] getLocalRoleTypeGuardClassNames()
    {
        return theLocalRoleTypeGuardClassNames;
    }

    /**
     * Check whether the given caller is allowed to change the time of auto-delete property
     * on a given MeshObject.
     *
     * @param obj the MeshObject whose auto-delete property shall be changed
     * @param newValue the new value of the property
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedSetTimeExpires(
            MeshObject obj,
            long       newValue,
            MeshObject caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedSetTimeAutoDeletes(
                    this,
                    obj,
                    newValue,
                    caller );
        }
    }

    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with a given new RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedBless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedBless(
                    this,
                    start,
                    neighborIdentifier,
                    neighbor,
                    caller );
        }
    }

    /**
     * Check whether the given caller is allowed to unbless an existing relationship from a given start
     * MeshObject to a given destination MeshObject from a given RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedUnbless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedUnbless(
                    this,
                    start,
                    neighborIdentifier,
                    neighbor,
                    caller );
        }
    }

    /**
     * Check whether the given caller is allowed to bless the given start MeshObject with
     * the given additional EntityTypes, in the opinion of a Role (identified by this
     * RoleType and neighborWithOpinion) currently also played by the start MeshObject.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject to be blessed
     * @param types the EntityTypes by which the start MeshObject should be blessed
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedIncrementalBless(
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedIncrementalBless(
                    this,
                    start,
                    types,
                    neighborWithOpinionIdentifier,
                    neighborWithOpinion,
                    caller );
        }
    }

    /**
     * Check whether the given caller is allowed to unbless the given start MeshObject from
     * the given EntityTypes, in the opinion of a Role (identified by this
     * RoleType and neighborWithOpinion) currently also played by the start MeshObject.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject to be blessed
     * @param types the EntityTypes by which the start MeshObject should be unblessed
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedIncrementalUnbless(
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedIncrementalUnbless(
                    this,
                    start,
                    types,
                    neighborWithOpinionIdentifier,
                    neighborWithOpinion,
                    caller );
        }
    }
    
    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with a given new RoleType, in the opinion of
     * another Role (identified as this RoleType and neighborWithOpinion).
     *
     * @param start the MeshObject to be blessed
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param newTypes the RoleType by which the start MeshObject's relationship to the newDestination should be blessed
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedIncrementalBless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          newTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedIncrementalBless(
                    this,
                    start,
                    neighborIdentifier,
                    neighbor,
                    newTypes,
                    neighborWithOpinionIdentifier,
                    neighborWithOpinion,
                    caller );
        }
    }
    
    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with a given new RoleType, in the opinion of
     * another Role (identified as this RoleType and neighborWithOpinion).
     *
     * @param start the MeshObject to be blessed
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param newTypes the RoleType by which the start MeshObject's relationship to the newDestination should be unblessed
     * @param neighborWithOpinionIdentifier identifier of the neighbor MeshObject whose opionion is being asked
     * @param neighborWithOpinion neighbor MeshObject whose opionion is being asked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this
     */
    @Override
    public void checkPermittedIncrementalUnbless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          newTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedIncrementalUnbless(
                    this,
                    start,
                    neighborIdentifier,
                    neighbor,
                    newTypes,
                    neighborWithOpinionIdentifier,
                    neighborWithOpinion,
                    caller );
        }
    }
    
    /**
     * Check whether the given caller is allowed to traverse this RoleType from a given start
     * MeshObject to a given destination MeshObject.
     *
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedTraversal(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedTraversal(
                    this,
                    start,
                    neighborIdentifier,
                    neighbor,
                    caller );
        }
    }

    /**
     * Check whether the given caller is allowed to make one and two members of the same
     * equivalence set.
     * 
     * @param one the first MeshObject
     * @param twoIdentifier identifier of the second MeshObject
     * @param two the second MeshObject, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedAddAsEquivalent(
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two,
            MeshObject           caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedAddAsEquivalent(
                    this,
                    one,
                    twoIdentifier,
                    two,
                    caller );
        }        
    }
    
    /**
     * Check whether the given caller is allowed to remove the MeshObject from its
     * equivalence set.
     * 
     * @param obj the MeshObject
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedRemoveAsEquivalent(
            MeshObject  obj,
            MeshObject  caller )
        throws
            NotPermittedException
    {
        for( RoleTypeGuard current : getAllRoleTypeGuards() ) {
            current.checkPermittedRemoveAsEquivalent(
                    this,
                    obj,
                    caller );
        }        
    }

    /**
     * Convert to String form, for debugging.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        StringBuilder almostRet = new StringBuilder( "<" );
        almostRet.append( super.toString() );
        almostRet.append( "{ " );
        if( this instanceof TopSingleton ) {
            almostRet.append( "top for " );
        } else if( isSource() ) {
            almostRet.append( "source of " );
        } else {
            almostRet.append( "dest of " );
        }
        almostRet.append( theRelationshipType.getIdentifier() );
        almostRet.append( " }>" );
        return almostRet.toString();
    }

    /**
      * The RelationshipType that this RoleType belongs to.
      */
    private MRelationshipType theRelationshipType;

    /**
      * The EntityType associated with this MetaRole.
      */
    private MEntityType theEntityType;

    /**
      * The value of the source multiplicity.
      */
    private MultiplicityValue theMultiplicity;

    /**
      * The RoleTypes that this RoleType refines directly.
      */
    private MRoleType [] theDirectSuperRoleTypes = new MRoleType[0];

    /**
     * The set of RoleTypeGroups on this RoleType (not a supertype), expressed as the set of class names.
     */
    private String [] theLocalRoleTypeGuardClassNames;

    /**
     * The set of RoleTypeGroups on this RoleType (not a supertype). This is allocated when needed.
     */
    private RoleTypeGuard [] theLocalRoleTypeGuards;

    /**
     * The set of RoleTypeGroups on this RoleType (including its supertypes). This is allocated when needed.
     */
    private RoleTypeGuard [] theAllRoleTypeGuards;

    /**
     * Our ResourceHelper.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( MRoleType.class );

    /**
     * This represents a source RoleType.
     */
    static class Source
            extends
                MRoleType
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
          * Constructor.
          *
          * @param relationship the RelationshipType to which this RoleType will belong
          * @param entity the EntityType related to this RoleType
          * @param multiplicity the multiplicity of this RoleType
          * @param constraintClassNames Class names of the RoleConstraints to be enforced
          */
        Source(
                MRelationshipType  relationship,
                MEntityType        entity,
                MultiplicityValue  multiplicity,
                String []          constraintClassNames )
        {
            super(  relationship.getIdentifier().createDerivedMeshTypeIdentifier( SOURCE_POSTFIX ),
                    relationship,
                    entity,
                    multiplicity,
                    constraintClassNames );

            setName( StringValue.create( relationship.getName().value() + SOURCE_POSTFIX ));
        }

        /**
         * Obtain a localized, user-visible name of this MeshObjectType in the current locale.
         * We override this to obtain a reasonable default.
         *
         * @return value of the UserVisibleName property
         */
        @Override
        public final StringValue getUserVisibleName()
        {
            StringValue ret = super.getUserVisibleName();
            if( ret == null ) {
                ret = StringValue.create( theUserVisiblePrefix + getRelationshipType().getUserVisibleName().value() );
                theUserName = ret;
            }
            return ret;
        }

        /**
         * Determine whether this RoleType is a source or destination RoleType.
         *
         * @return true if this is a source RoleType of a RelationshipType
         */
        @Override
        public final boolean isSource()
        {
            return true;
        }

        /**
         * Determine whether this RoleType is the top singleton RoleType.
         *
         * @return always returns false
         */
        @Override
        public final boolean isTopSingleton()
        {
            return false;
        }

        /**
         * The prefix in front of the name to construct our UserVisibleName.
         */
        private static final String theUserVisiblePrefix = theResourceHelper.getResourceString( "ForwardArrow" );
    }

    /**
     * This represents a destination RoleType.
     */
    static class Destination
            extends
                MRoleType
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
          * Constructor.
          *
          * @param relationship the RelationshipType to which this RoleType will belong
          * @param entity the EntityType related to this RoleType
          * @param multiplicity the multiplicity of this RoleType
          * @param constraintClassNames Class names of the RoleConstraints to be enforced
          */
        Destination(
                MRelationshipType  relationship,
                MEntityType        entity,
                MultiplicityValue  multiplicity,
                String []          constraintClassNames )
        {
            super(  relationship.getIdentifier().createDerivedMeshTypeIdentifier( DESTINATION_POSTFIX ),
                    relationship,
                    entity,
                    multiplicity,
                    constraintClassNames );

            setName( StringValue.create( relationship.getName().value() + DESTINATION_POSTFIX ));
        }

        /**
         * Obtain a localized, user-visible name of this MeshObjectType in the current locale.
         * We override this to obtain a reasonable default.
         *
         * @return value of the UserVisibleName property
         */
        @Override
        public final StringValue getUserVisibleName()
        {
            StringValue ret = super.getUserVisibleName();
            if( ret == null ) {
                ret = StringValue.create( theUserVisiblePrefix + getRelationshipType().getUserVisibleName().value() );
                theUserName = ret;
            }
            return ret;
        }

        /**
         * Determine whether this RoleType is a source or destination RoleType.
         *
         * @return true if this is a source RoleType of a RelationshipType
         */
        @Override
        public final boolean isSource()
        {
            return false;
        }

        /**
         * Determine whether this RoleType is the top singleton RoleType.
         *
         * @return always returns false
         */
        @Override
        public final boolean isTopSingleton()
        {
            return false;
        }

        /**
         * The prefix in front of the name to construct our UserVisibleName.
         */
        private static final String theUserVisiblePrefix = theResourceHelper.getResourceString( "BackwardArrow" );
    }

    /**
     * This represents a singleton MetaRole.
     */
    static class TopSingleton
            extends
                MRoleType
            implements
                RoleType.TopSingleton
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
          * Constructor.
          *
          * @param relationship the RelationshipType to which this RoleType will belong
          * @param entity the EntityType related to this RoleType
          * @param multiplicity the multiplicity of this RoleType
          * @param constraintClassNames Class names of the RoleConstraints to be enforced
          */
        TopSingleton(
                MRelationshipType  relationship,
                MEntityType        entity,
                MultiplicityValue  multiplicity,
                String []          constraintClassNames )
        {
            super(  relationship.getIdentifier().createDerivedMeshTypeIdentifier( TOP_SINGLETON_POSTFIX ),
                    relationship,
                    entity,
                    multiplicity,
                    constraintClassNames );
        }

        /**
         * Obtain the EntityType on the other end of this RelationshipType. This does not apply here.
         *
         * @return never returns
         * @throws UnsupportedOperationException always thrown
         */
        @Override
        public final EntityType getOtherEntityType()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Obtain a localized, user-visible name of this MeshObjectType in the current locale.
         * We override this to obtain a reasonable default.
         *
         * @return value of the UserVisibleName property
         */
        @Override
        public final StringValue getUserVisibleName()
        {
            StringValue ret = super.getUserVisibleName();
            if( ret == null ) {
                ret = StringValue.create( theUserVisiblePrefix + getRelationshipType().getUserVisibleName().value() );
                theUserName = ret;
            }
            return ret;
        }

        /**
         * Determine whether this RoleType is a source or destination RoleType.
         *
         * @return never
         * @throws UnsupportedOperationException always thrown
         */
        @Override
        public final boolean isSource()
        {
            throw new UnsupportedOperationException();
        }

        /**
         * Determine whether this RoleType is the top singleton RoleType.
         *
         * @return always returns true
         */
        @Override
        public final boolean isTopSingleton()
        {
            return true;
        }

        /**
         * The prefix in front of the name to construct our UserVisibleName.
         */
        private static final String theUserVisiblePrefix = theResourceHelper.getResourceString( "TopArrow" );
    }
}
