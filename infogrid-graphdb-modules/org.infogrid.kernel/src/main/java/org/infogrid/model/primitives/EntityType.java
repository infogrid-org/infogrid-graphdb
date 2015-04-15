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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;

/**
  * <p>A MeshType that does not depend on the existence of others, that can participate in
  * inheritance relationships, and may be attributed. This is often called Class (or MetaClass)
  * or Entity in the literature.</p>
  *
  * <p>An EntityType may be significant or not, in which case it is a mechanistic construct.</p>
  */
public interface EntityType
        extends
            AttributableMeshType
{
    /**
     * Obtain the identifiers by which this EntityType is also known.
     *
     * @return the identifiers by which this EntityType is also known
     */
    public MeshTypeIdentifier [] getSynonyms();

    /**
      * Obtain the value of the IsSignificant property.
      *
      * @return the value of the IsSignificant property
      */
    public BooleanValue getIsSignificant();

    /**
     * Obtain all local RoleTypes in which this EntityType participates in the
     * working model. Local RoleTypes are those which identify this EntityType
     * as the source or destination of a RelationshipType, rather than one of its
     * supertypes.
     *
     * By definition (see definition of "working model"), not all RoleTypes of an
     * EntityType may be known at any point in time; only the ones
     * currently known in the working model are returned.
     *
     * @return the local RoleTypes in which this EntityType participates in the working model
     */
    public RoleType [] getLocalRoleTypes();

    /**
     * Obtain all RoleTypes in which this EntityType participates in the
     * working model. This includes all RoleTypes which identify this EntityType,
     * or one of its supertypes as the source or destination of a RelationshipType.
     *
     * By definition (see definition of "working model"), not all RoleTypes of an
     * EntityType may be known at any point in time; only the ones
     * currently known in the working model are returned.
     *
     * @return all RoleTypes in which this EntityType or one of its supertypes
     *         participates in the working model
     */
    public RoleType [] getAllRoleTypes();

    /**
      * Obtain all RoleTypes in which this EntityType participates in the
      * working model, and that belong to RelationshipTypes which are not abstract.
      * This includes all RoleTypes which identify this EntityType, or one of its supertypes
      * as the source or destination of a RelationshipType.
      *
     * By definition (see definition of "working model"), not all RoleTypes of an
     * EntityType may be known at any point in time; only the ones
     * currently known in the working model are returned.
      *
      * @return all RoleTypes in which this EntityType or one of its supertypes
      *         participates in the working model,
      *         and which belong to RelationshipTypes which are not abstract
      */
    public RoleType [] getAllConcreteRoleTypes();

    /**
      * Determine the set of RelationshipType that are subtypes of the specified RelationshipType,
      * that have this EntityType or one of its supertypes as a source or destination,
      * and that are "closests" in the inheritance hierarchy.
      *
      * @param supertypeRelationshipType the supertype RelationshipType
      * @return the set of RelationshipType
      */
    public RelationshipType [] obtainMostConcreteSubtypeRelated(
            RelationshipType supertypeRelationshipType );

    /**
     * Obtain code that the code generator inserts into the generated code for this
     * EntityType, and all of its subtypes.
     *
     * @return text in Java that will be in-lined into the generated code
     */
    public StringValue getInheritingOverrideCode();

    /**
     * Obtain the set of method declarations that this EntityType makes in addition.
     *
     * @return Java method declarations
     */
    public StringValue [] getDeclaredMethods();

    /**
     * Obtain the set of method implementations that this EntityType makes in addition.
     *
     * @return Java method implementations
     */
    public StringValue [] getImplementedMethods();

    /**
     * Obtain the set of interfaces that this EntityType declares in addition.
     *
     * @return Java interface names
     */
    public String [] getAdditionalInterfaces();

    /**
     * Obtain the set of EntityTypeGuards locally defined on this EntityType.
     *
     * @return the set of EntityTypeGuards locally defined on this EntityType
     */
    public EntityTypeGuard [] getLocalEntityTypeGuards();

    /**
     * Obtain the set of EntityTypeGuards defined on this EntityType either locally or by inheritance.
     *
     * @return the set of EntityTypeGuards defined on this EntityType either locally or by inheritance
     */
    public EntityTypeGuard [] getAllEntityTypeGuards();

    /**
     * Obtain the class names of the set of EntityTypeGuards on this EntityType.
     *
     * @return the class names of the set of EntityTypeGuards on this EntityType
     */
    public String [] getLocalEntityTypeGuardClassNames();

    /**
     * Determine whether this EntityType may be used as a ForwardReference.
     *
     * @return true if this may be used as a ForwardReference
     */
    public BooleanValue getMayBeUsedAsForwardReference();
    
    /**
     * Check whether the given caller is allowed to bless an existing MeshObject
     * with this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be blessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBless(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException;

    /**
     * Check whether the given caller is allowed to unbless an existing MeshObject
     * from this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be unblessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedUnbless(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException;

    /**
     * Check whether the given caller is allowed to determine whether a given MeshObject
     * has been blessed with this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be checked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedBlessedBy(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException;
}
