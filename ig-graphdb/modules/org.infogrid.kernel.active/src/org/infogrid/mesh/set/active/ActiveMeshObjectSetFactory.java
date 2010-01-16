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

package org.infogrid.mesh.set.active;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * A factory for ACtiveMeshObjectSets.
 */
public interface ActiveMeshObjectSetFactory
    extends
        MeshObjectSetFactory
{
    /**
     * Factory method to create an empty MeshObjectSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * 
     * @return the empty MeshObjectSet
     */
    public ActiveMeshObjectSet obtainEmptyConstantActiveMeshObjectSet();

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
     * Factory method to construct a MeshObjectSet that contains those MeshObjects that are contained
     * in all of the provided MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet [] operands );

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public CompositeActiveMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector );

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @return the created MeshObjectSet
     */
    public OrderedActiveMeshObjectSet createActiveOrderedMeshObjectSet(
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
    public OrderedActiveMeshObjectSet createActiveOrderedMeshObjectSet(
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
    public ActiveMeshObjectSet createTransitiveClosureAktiveMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification );

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
}
