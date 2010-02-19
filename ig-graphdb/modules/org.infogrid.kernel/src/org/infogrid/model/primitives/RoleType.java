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

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.traversal.TraversalSpecification;


/**
  * <p>The role played by a EntityType in a RelationshipType. One can consider a RoleType to be the "end"
  *    of a RelationshipType.</p>
  *
  * <p>RelationshipType inheritance is modeled through RoleTypes. By allowing a RoleType to refine another
  *    RoleType, the RelationshipType is subtyped. Of course, there are constraints on the topology of
  *    such refinement relationships. Given that InfoGrid supports multiple inheritance for EntityTypes
  *    and for RelationshipTypes, any RoleType may refine N RoleTypes, which thereby identify the
  *    supertype RelationshipTypes. The top RoleType is a singleton and used by a unary RelationshipType.</p>
  *
  * <p>RoleTypes carry a multiplicity, which, counter-intuitively, is the opposite of what is commonly written
  *    next to the line in an entity-relationship or class-association diagram. For example, if this is the
  *    diagram:</p>
  * <pre>
  *    +-------+ 0..1        1..* +-------+
  *    |   A   |<---------------->|   B   |
  *    +-------+ (source)  (dest) +-------+
  * </pre>
  * <p>then the source RoleType carries the 1..*, while the destination RoleType carries the 0..1. While this is
  *    a little counter-intuitive diagrammatically, it makes a lot more sense semantically and programmatically.</p>
  */
public interface RoleType
        extends
            CollectableMeshType,
            TraversalSpecification
{
    /**
      * Obtain the RelationshipType that this RoleType belongs to.
      *
      * @return the RelationshipType that this RoleType belongs to
      */
    public RelationshipType getRelationshipType();

    /**
      * Obtain the EntityType that plays this RoleType.
      *
      * @return the EntityType that this RoleType belongs to
      */
    public EntityType getEntityType();

    /**
     * Obtain the "other" RoleType of this RelationshipType. A RelationshipType has a source
     * and a destination RoleType. If this is applied on a source RoleType, this will obtain
     * the destination RoleType, and vice versa.
     *
     * @return the RoleType on the other end of the RelationshipType that this RoleType belongs to
     */
    public RoleType getInverseRoleType();

    /**
     * Obtain the EntityType on the other end of this RelationshipType. A RelationshipType has a source
     * and a destination RoleType. If this is applied on a source RoleType, this will obtain
     * the EntityType related to the destination RoleType, and vice versa.
     *
     * @return the EntityType on the other end of the RelationshipType that this RoleType belongs to
     */
    public EntityType getOtherEntityType();

    /**
     * Obtain the multiplicity of this RoleType. The multiplicity of a RoleType is the number of
     * EntityType instances that can be reached from a start EntityType instance by traversing
     * the RoleType. Multiplicities have a minimum and a maximum value. Note that multiplicities
     * on RoleType are the opposite numbers of what is written next to a line in an entity-relationship
     * (or class-association) diagram.
     *
     * @return the multiplicity of this RoleType
     */
    public MultiplicityValue getMultiplicity();

    /**
     * Obtain the RoleTypes that this RoleType refines directly.
     *
     * @return the RoleType that this RoleType refines directly. In most cases, this will be just one.
     */
    public RoleType [] getDirectSuperRoleTypes();

    /**
     * Determine whether this RoleType is a specialization of, or the same
     * as the passed-in TraversalSpecification. This is analogous to
     * AttributableMeshType.isSubtypeOfOrEquals().
     *
     * @param other the TraversalSpecification to compare against
     * @return true if this RoleType is a specialization of the passed-in TraversalSpecification
     */
    public boolean isSpecializationOfOrEquals(
            TraversalSpecification other );

    /**
     * Determine whether this RoleType is a source or destination RoleType.
     * FIXME: this should go away at some time.
     *
     * @return true if this is a source RoleType of a RelationshipType
     */
    public boolean isSource();

    /**
     * Determine whether this RoleType is the top singleton RoleType.
     *
     * @return true if this is the top singleton RoleType
     */
    public boolean isTopSingleton();

    /**
     * Obtain the set of RoleTypeGuards locally defined on this RoleType.
     *
     * @return the set of RoleTypeGuards locally defined on this RoleType
     */
    public RoleTypeGuard [] getLocalRoleTypeGuards();

    /**
     * Obtain the set of RoleTypeGuards defined on this RoleType either locally or by inheritance.
     *
     * @return the set of RoleTypeGuards defined on this RoleType either locally or by inheritance
     */
    public RoleTypeGuard [] getAllRoleTypeGuards();

    /**
     * Obtain the class names of the set of RoleTypeGuards on this RoleType.
     *
     * @return the class names of the set of RoleTypeGuards on this RoleType
     */
    public String [] getLocalRoleTypeGuardClassNames();

    /**
     * Check whether the given caller is allowed to change the expirationTime
     * on a given MeshObject.
     *
     * @param obj the MeshObject whose expirationTime property shall be changed
     * @param newValue the new value of the property
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedSetTimeExpires(
            MeshObject obj,
            long       newValue,
            MeshObject caller )
        throws
            NotPermittedException;

    /**
     * Check whether the given caller is allowed to bless an existing relationship from a given start
     * MeshObject to a given destination MeshObject with this new RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException;

    /**
     * Check whether the given caller is allowed to unbless an existing relationship from a given start
     * MeshObject to a given destination MeshObject from this RoleType.
     * This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param start the MeshObject from which the relationship starts
     * @param neighborIdentifier identifier of the MeshObject to which the relationship leads
     * @param neighbor MeshObject to which the relationship leads, if successfully resolved
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedUnbless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException;

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
    public void checkPermittedIncrementalBless(
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException;

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
    public void checkPermittedIncrementalUnbless(
            MeshObject           start,
            EntityType []        types,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException;
    
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
    public void checkPermittedIncrementalBless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          newTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException;

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
    public void checkPermittedIncrementalUnbless(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            RoleType []          newTypes,
            MeshObjectIdentifier neighborWithOpinionIdentifier,
            MeshObject           neighborWithOpinion,
            MeshObject           caller )
        throws
            NotPermittedException;
    
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
    public void checkPermittedTraversal(
            MeshObject           start,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor,
            MeshObject           caller )
        throws
            NotPermittedException;

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
    public void checkPermittedAddAsEquivalent(
            MeshObject           one,
            MeshObjectIdentifier twoIdentifier,
            MeshObject           two,
            MeshObject           caller )
        throws
            NotPermittedException;
    
    /**
     * Check whether the given caller is allowed to remove the MeshObject from its
     * equivalence set.
     * 
     * @param obj the MeshObject to be removed from its equivalence set
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedRemoveAsEquivalent(
            MeshObject  obj,
            MeshObject  caller )
        throws
            NotPermittedException;
    
    /**
      * Appended to the Identifier of a RelationshipType to create a "source" RoleType's Identifier.
      * Note: an application programmer should not depend on this; use provided methods instead to
      * query a RoleType.
      */
    public static final String SOURCE_POSTFIX = "-S";

    /**
      * Appended to the Identifier of a RelationshipType to create a "destination" RoleType's Identifier.
      * Note: an application programmer should not depend on this; use provided methods instead to
      * query a RoleType.
      */
    public static final String DESTINATION_POSTFIX = "-D";

    /**
      * Appended to the Identifier of the RelationshipType to create a "topSingleton" RoleType's Identifier.
      * Note: an application programmer should not depend on this; use provided methods instead to
      * query a RoleType.
      */
    public static final String TOP_SINGLETON_POSTFIX = "-T";

    /**
     * The special type of RoleType that is at the top of the RoleType refinement hierarchy.
     */
    public interface TopSingleton
            extends
                RoleType
    {
    }
}
