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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;

/**
 * Classes supporting this type know how to instantiate MeshObjectSets of various
 * kinds.
 */
public interface MeshObjectSetFactory
{
    /**
     * Set the MeshBase on whose behalf this factory works. This must only be invoked
     * once for an instance.
     * 
     * @param newValue the new value
     */
    public void setMeshBase(
            MeshBase newValue );

    /**
     * Obtain the MeshBase on which this MeshObjectFactory operates.
     * 
     * @return the MeshBase
     */
    public MeshBase getMeshBase();

    /**
     * Factory method to create an empty MeshObjectSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * 
     * @return the empty MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet obtainEmptyImmutableMeshObjectSet();

    /**
     * Factory method to construct a MeshObjectSet with the single specified member.
     * 
     * @param member the content of the set
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createSingleMemberImmutableMeshObjectSet(
            MeshObject member );

    /**
     * Factory method to construct a MeshObjectSet with the specified members.
     * 
     * @param members the content of the set
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject [] members );

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject []      candidates,
            MeshObjectSelector selector );

    /**
     * Factory method to construct a MeshObjectSet with the members of another
     * MeshObjectSet, as long as they are selected by the MeshObjectSelector.
     * 
     * @param input the input MeshObjectSet
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObjectSet      input,
            MeshObjectSelector selector );
    
    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets, as long as they are selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector the MeshObjectSelector to use, if any
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );
    
    /**
     * Convenience factory method to construct a unification of two MeshObjectSets.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Convenience factory method to construct a unification of a MeshObjectSet and
     * a second single-element MeshObjectSet.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObject    two );

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects that are contained
     * in all of the provided MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector the MeshObjectSelector to use, if any
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );

    /**
     * Convenience factory method to construct an intersection of two MeshObjectSets.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     * 
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @return the created CompositeImmutableMeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet, as long
     * as they are also selected by the MeshObjectSelector.
     * 
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @param selector the MeshObjectSelector to use, if any
     * @return the created CompositeImmutableMeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet      one,
            MeshObjectSet      two,
            MeshObjectSelector selector );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param contentInOrder the content of the OrderedMeshObjectSet, in order
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObject [] contentInOrder );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     *
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @param max the maximum number of MeshObjects that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max MeshObjects according to the sorter.
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter,
            int              max );

    /**
     * Factory method.
     *
     * @param content the content for the ImmutableMTraversalPathSet
     * @return the created ImmutableMTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            TraversalPath [] content );

    /**
     * Factory method. This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet.
     *
     * @param set used to construct the content for the ImmutableMTraversalPathSet
     * @return the created ImmutableMTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            MeshObjectSet set );

    /**
     * Convenience method to return an array of MeshObjects as an
     * array of the canonical Identifiers of the member MeshObjects.
     *
     * @param array the MeshObjects 
     * @return the array of IdentifierValues representing the Identifiers
     */
    public MeshObjectIdentifier[] asIdentifiers(
            MeshObject [] array );
}
