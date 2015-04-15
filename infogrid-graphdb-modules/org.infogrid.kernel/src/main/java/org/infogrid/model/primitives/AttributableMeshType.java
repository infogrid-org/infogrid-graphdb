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

/**
  * <p>A MeshType that can have features, called PropertyTypes, and which participates in an
  *    inheritance hierarchy.</p>
  *
  * <p>A given AttributableMeshType has PropertyTypes that are defined locally (called local
  *    PropertyTypes) and those that are inherited from its supertype or supertypes (called
  *    inherited PropertyTypes). Generally, all PropertyTypes of the supertype or supertypes
  *    are always inherited to all subtypes.</p>
  *
  * <p>Further, inherited PropertyTypes can be locally overridden. This is useful, for example,
  *    to set different default values for a PropertyType in subtypes.</p>
  *
  * <p>PropertyTypes can be grouped in PropertyTypeGroups. PropertyTypeGroups also are
  *    held by AttributableMeshTypes.</p>
  *
  * <p>AttributableMeshTypes can also have one or more supertypes. By having more than one
  *    supertype, an AttributableMeshType can inherit multiple times.</p>
  *
  * <p>AttributableMeshTypes can be abstract or concrete (i.e. cannot be instantiated, or
  *    can be instantiated).</p>
  */
public interface AttributableMeshType
        extends
            CollectableMeshType
{
    /**
      * Obtain the locally defined PropertyTypes for this AttributableMeshType.
      * This does not return locally overridden PropertyTypes.
      *
      * @return the locally defined PropertyTypes for this AttributableMeshType
      */
    public PropertyType [] getLocalPropertyTypes();

    /**
     * Obtain the inherited, but locally overridden PropertyTypes.
     *
     * @return the inherited, but locally overridden PropertyTypes
     */
    public PropertyType [] getOverridingLocalPropertyTypes();

    /**
      * Obtain the inherited PropertyTypes for this AttributableMeshType.
      *
      * @return the inherited PropertyTypes for this AttributableMeshType
      */
    public PropertyType [] getInheritedPropertyTypes();

    /**
      * Obtain the transitive closure of all local and inherited PropertyTypes of this
      * AttributableMeshType and its supertypes without
      * duplicates. This will return locally overridden PropertyTypes and skip those in
      * supertypes that have been overridden.
      *
      * @return all local and inherited PropertyTypes
      */
    public PropertyType [] getAllPropertyTypes();

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypes of this
     * AttributableMeshTypes and its supertypes that are
     * ProjectedPropertyTypes without duplicates. This will return locally overridden
     * PropertyTypes and skip those in supertypes that have been overridden.
     *
     * @return all local and inherited PropertyTypes that are ProjectedPropertyTypes
     */
    public ProjectedPropertyType [] getAllProjectedPropertyTypes();

    /**
     * Obtain the locally defined PropertyTypeGroups for this AttributableMeshType.
     *
     * @return the locally defined PropertyTypeGroups for this AttributableMeshType
     */
    public PropertyTypeGroup [] getLocalPropertyTypeGroups();

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypeGroups of
     * this AttributableMeshType and all of its supertypes without duplicates.
     *
     * @return all local and inherited PropertyTypeGroups
     */
    public PropertyTypeGroup [] getAllPropertyTypeGroups();

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypeOrGroups
     * of this AttributableMeshType and all of its supertypes without duplicates.
     * Do not include those PropertyTypes that are part of returned PropertyTypeGroups.
     * Return them ordered by their SequenceNumber property.
     *
     * @return all local and inherited PropertyTypeOrGroups in sequence
     */
    public PropertyTypeOrGroup [] getAllPropertyTypeOrGroupsInSequence();

    /**
     * Obtain the direct supertypes for this AttributableMeshType.
     *
     * @return the direct supertypes of this AttributableMeshType
     */
    public AttributableMeshType [] getDirectSupertypes();

    /**
     * Obtain the transitive closure of all supertypes.
     *
     * @return transitive closure of all supertypes of this AttributableMeshType
     */
    public AttributableMeshType [] getAllSupertypes();

    /**
     * Obtain the paths to this AttributableMeshType down the inheritance hierarchy
     * from its ultimate root AttributableMeshType(s).
     * If this AttributableMeshType does not inherit from another, this will
     * return an empty set. If it (or a supertype) employs multiple inheritance,
     * this will return a set with more than one member. Each members of the
     * set at the steps taken from the root of the inheritance hierarchy down to
     * here (but not including this AttributableMeshType).
     *
     * @return the paths from the root(s) of the inheritance hierarchy
     */
    public AttributableMeshType [][] getPathsFromInheritanceRoots();
    
    /**
     * Obtain the direct subtypes for this AttributableMeshType in the working model.
     *
     * By definition (see definition of "working model"), not all subtypes of the
     * AttributableMeshType may be known at any point in time; only the ones
     * currently known in the working model ones are returned.
     *
     * @return the direct subtypes of this AttributableMeshType
     */
    public AttributableMeshType [] getDirectSubtypes();

    /**
     * Obtain the transitive closure of all subtypes of this AttributableMeshType
     * in the working model.
     *
     * By definition (see definition of "working model"), not all subtypes of the
     * AttributableMeshType may be known at any point in time; only the ones
     * currently known in the working model are returned.
     *
     * @return the transitive closure of all known subtypes of this AttributableMeshType
     */
    public AttributableMeshType [] getAllSubtypes();

    /**
     * Obtain the transitive closure of all subtypes of this AttributableMeshType
     * in the working model, and return those that are instantiable (as opposed
     * to those that are abstract).
     *
     * By definition (see definition of "working model"), not all subtypes of the
     * AttributableMeshType may be known at any point in time; only the ones
     * currently known in the working model are returned.
     *
     * @return the transitive closure of all known subtypes of this AttributableMeshType
     */
    public AttributableMeshType [] getAllConcreteSubtypes();

    /**
      * Determine whether the passed-in AttributableMeshType equals or is
      * a supertype of this AttributableMeshType.
      *
      * @param other the AttributableMeshType to test against
      * @return true of the passed-in AttributableMeshType equals or is a supertype of this
      *         AttributableMeshType
      */
    public boolean equalsOrIsSupertype(
            AttributableMeshType other );

    /**
      * Determine the distance from this AttributableMeshType to the
      * passed-in (supertype) AttributableMeshType in number of "inheritance steps".
      *
      * @param other the AttributableMeshType to test against
      * @return the number of steps between this AttributableMeshType and other. -1 if
      *         there is no sub-typing relationships between the two
      */
    public int getDistanceToSupertype(
            AttributableMeshType other );

    /**
      * Determine the distance from this AttributableMeshType to the
      * passed-in (supertype) AttributableMeshType in number of "inheritance steps".
      *
      * This method works on the name of the (class representing instances of the)
      * supertype, rather than its AttributableMeshType.
      *
      * @param className the class name corresponding to the AttributableMeshType to test against
      * @return the number of steps between this AttributableMeshType and other. -1 if
      *         there is no sub-typing relationships between the two
      */
    public int getDistanceToSupertype(
            String className );

    /**
     * Determine whether this AttributableMeshType is a subtype of the passed-in
     * AttributableMeshType. This also returns true of they are the same.
     *
     * @param other the AttributableMeshType to test against
     * @return true if this AttributableMeshType is a subtype of other
     * @see #isSubtypeOfDoesNotEqual
     */
    public boolean isSubtypeOfOrEquals(
            AttributableMeshType other );

    /**
     * Determine whether this AttributableMeshType is a subtype of the passed-in
     * AttributableMeshType. This returns fakse if they are the same.
     *
     * @param other the AttributableMeshType to test against
     * @return true if this AttributableMeshType is a subtype of other
     * @see #isSubtypeOfOrEquals
     */
    public boolean isSubtypeOfDoesNotEqual(
            AttributableMeshType other );

    /**
     * Determine whether the passed-in MeshObject is an instance of this AttributableMeshType
     * or one of its subtypes. This is modeled after a similar method on java.lang.Class.
     *
     * @param candidate MeshObject the MeshObject to test against
     * @return true if candidate is an instance of this AttributableMeshType, or one of its subtypes
     */
    public boolean isInstance(
            MeshObject candidate );

    /**
      * Obtain the value of the IsAbstract property.
      *
      * @return the value of the IsAbstract property
      */
    public BooleanValue getIsAbstract();

    /**
      * Find one of this AttributableMeshType's local or inherited PropertyTypes by its Name property.
      * This will return the most concrete overridden PropertyType.
      *
      * @param name value of the Name property of the PropertyType
      * @return the found PropertyType, or null if not found.
      */
    public PropertyType findPropertyTypeByName(
            String name );

    /**
     * Obtain a ClassLoader that can load the appropriate Java classes to instantiate this
     * AttributableMeshType.
     *
     * @return the ClassLoader that can load the appropriate Java classes to instantiate this
     *         AttributableMeshType
     */
    public ClassLoader getClassLoader();
}
