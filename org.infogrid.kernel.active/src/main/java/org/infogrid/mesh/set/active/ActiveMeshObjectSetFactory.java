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

package org.infogrid.mesh.set.active;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.mesh.set.TraversalPathSorter;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * A factory for ACtiveMeshObjectSets.
 */
public interface ActiveMeshObjectSetFactory
    extends
        MeshObjectSetFactory
{
    /**
     * Factory method to create an empty MeshObjectSet.
     * This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * Given that it is empty, it might as well return an ordered ActiveMeshObjectSet.
     * 
     * @return the empty MeshObjectSet
     */
    public OrderedActiveMeshObjectSet obtainEmptyConstantActiveMeshObjectSet();

    /**
     * Factory method to create a MeshObjectSet with a single specified member.
     * This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * Given that it has only one member, it might as well return an ordered ActiveMeshObjectSet.
     *
     * @param member the content of the set
     * @return the created MeshObjectSet
     */
    public OrderedActiveMeshObjectSet obtainSingleMemberConstantActiveMeshObjectSet(
            MeshObject member );

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @return the created MeshObjectSet
     */
    public ActiveMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObject []      candidates );

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ActiveMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObject []      candidates,
            MeshObjectSelector selector );

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     *
     * @param input the input MeshObjectSet
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ActiveMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObjectSet      input,
            MeshObjectSelector selector );

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets, as long as they are selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );
    
    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     *
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     *
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObject    two );

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects that are contained
     * in all of the provided MeshObjectSets.
     * 
     * @param operands the sets to intersect
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to intersect
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObject two );

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     *
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @return the created CompositeActiveMeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetMinus(
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
     * @return the created CompositeActiveMeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetMinus(
            MeshObjectSet      one,
            MeshObjectSet      two,
            MeshObjectSelector selector );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     *
     * @param contentInOrder the content of the OrderedMeshObjectSet, in order
     * @return the created OrderedActiveMeshObjectSet
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObject [] contentInOrder );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @return the created MeshObjectSet
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @param max the maximum number of MeshObjects that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max MeshObjects according to the sorter.
     * @return the created MeshObjectSet
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter,
            int              max );

   /**
     * Factory method to construct a TraversalActiveMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public TraversalActiveMeshObjectSet createActiveMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification );

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a TraversalSpecification.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public TraversalActiveMeshObjectSet createActiveMeshObjectSet(
            MeshObjectSet          startSet,
            TraversalSpecification specification );

    /**
     * Factory method to construct a TraversalActiveMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification, and repeating that process.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ActiveMeshObjectSet createTransitiveClosureActiveMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification );

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     * Given that it has only one member, it might as well return an ordered TraversalPathSet.
     *
     * @param singleMember the single member of the ImmutableTraversalPathSet
     * @return return the created ImmutableTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createSingleMemberConstantActiveTraversalPathSet(
            TraversalPath singleMember );

    /**
     * Factory method to create an ActiveTraversalPathSet.
     *
     * @param content the content for the ActiveTraversalPathSet
     * @return the created ActiveTraversalPathSet
     */
    public ActiveTraversalPathSet createActiveTraversalPathSet(
            TraversalPath [] content );

    /**
     * Factory method to create an ActiveTraversalPathSet.
     * This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet. The TraversalSpecification is passed in.
     *
     * @param spec the traversed TraversalSpecification
     * @param set used to construct the content for the ActiveTraversalPathSet
     * @return the created ActiveTraversalPathSet
     */
    public ActiveTraversalPathSet createActiveTraversalPathSet(
            TraversalSpecification spec,
            MeshObjectSet          set );

    /**
     * Factory method.
     *
     * @param start the MeshObject from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveTraversalPathSet createActiveTraversalPathSet(
            MeshObject             start,
            TraversalSpecification spec );

    /**
     * Factory method.
     *
     * @param startSet the MeshObjectSet from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveTraversalPathSet createActiveTraversalPathSet(
            MeshObjectSet          startSet,
            TraversalSpecification spec );

    /**
     * Factory method.
     *
     * @param startSet the TraversalPathSet from whose destination MeshObject we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveTraversalPathSet createActiveTraversalPathSet(
            TraversalPathSet       startSet,
            TraversalSpecification spec );

    /**
     * Factory method to create an OrderedActiveTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @return the created OrderedActiveTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createOrderedActiveTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter );

    /**
     * Factory method to create an OrderedActiveTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @param max the maximum number of TraversalPaths that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max TraversalPaths according to the sorter.
     * @return the created OrderedActiveTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createOrderedActiveTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter,
            int                 max );
}
