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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.model.traversal.TraversalSpecification;

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
     * Given that it is empty, we might as well return an OrderedMeshObjectSet.
     * 
     * @return the empty MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet obtainEmptyImmutableMeshObjectSet();

    /**
     * Factory method to construct a MeshObjectSet with the single specified member.
     * Given that it has only one member, we might as well return an OrderedMeshObjectSet.
     * 
     * @param member the content of the set
     * @return the created MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createSingleMemberImmutableMeshObjectSet(
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
     * @param operands the sets to intersect
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to intersect
     * @param selector the MeshObjectSelector to use, if any
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );

    /**
     * Convenience factory method to construct an intersection of two MeshObjectSets.
     * 
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Convenience factory method to construct an intersection of two MeshObjectSets.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObject    two );

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
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     *
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @return the created CompositeImmutableMeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObject    two );

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
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification.
     *
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification );

    /**
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a TraversalSpecification.
     *
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObjectSet          startSet,
            TraversalSpecification specification );

    /**
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification, and repeating that process.
     *
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ImmutableMeshObjectSet createTransitiveClosureImmutableMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification );

    /**
     * Factory method to create an empty TraversalPathSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * Given that it is empty, we might as well return an OrderedTraversalPathSet.
     * 
     * @return the empty TraversalPathSet
     */
    public OrderedImmutableTraversalPathSet obtainEmptyImmutableTraversalPathSet();
    
    /**
     * Factory method to create an ImmutableTraversalPathSet.
     * Given that it has only one member, it might as well return an ordered TraversalPathSet.
     * 
     * @param singleMember the single member of the ImmutableTraversalPathSet
     * @return return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createSingleMemberImmutableTraversalPathSet(
            TraversalPath singleMember );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param content the content for the ImmutableTraversalPathSet
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            TraversalPath [] content );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     * This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet. The TraversalSpecification is passed in.
     *
     * @param spec the traversed TraversalSpecification
     * @param set used to construct the content for the ImmutableTraversalPathSet
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            TraversalSpecification spec,
            MeshObjectSet          set );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param start the MeshObject from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            MeshObject             start,
            TraversalSpecification spec );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param startSet the MeshObjectSet from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            MeshObjectSet          startSet,
            TraversalSpecification spec );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param startSet the TraversalPathSet from whose destination MeshObject we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableTraversalPathSet createImmutableTraversalPathSet(
            TraversalPathSet       startSet,
            TraversalSpecification spec );

    /**
     * Factory method to create an OrderedImmutableTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @return the created OrderedImmutableTraversalPathSet
     */
    public OrderedImmutableTraversalPathSet createOrderedImmutableTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter );

    /**
     * Factory method to create an OrderedImmutableTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @param max the maximum number of TraversalPaths that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max TraversalPaths according to the sorter.
     * @return the created OrderedImmutableTraversalPathSet
     */
    public OrderedImmutableTraversalPathSet createOrderedImmutableTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter,
            int                 max );

    /**
     * Convenience method to return an array of MeshObjects as an
     * array of the canonical Identifiers of the member MeshObjects.
     *
     * @param array the MeshObjects 
     * @return the array of IdentifierValues representing the Identifiers
     */
    public MeshObjectIdentifier [] asIdentifiers(
            MeshObject [] array );
}
