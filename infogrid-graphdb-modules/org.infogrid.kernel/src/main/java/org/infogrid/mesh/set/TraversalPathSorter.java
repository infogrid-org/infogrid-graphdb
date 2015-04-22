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

package org.infogrid.mesh.set;

import org.infogrid.model.traversal.TraversalPath;

/**
 * This interface is supported by objects that can sort an array of TraversalPaths.
 */
public interface TraversalPathSorter
{
    /**
     * Pass in an array of TraversalPath in arbitrary order, and return the same set
     * in order.
     *
     * @param unorderedSet the unordered set
     * @return this a convenience return value: it's the same array as was passed in
     */
    public abstract TraversalPath [] getOrderedInSame(
            TraversalPath [] unorderedSet );

    /**
     * Pass in an array of TraversalPath in arbitrary order, and return a new set
     * with the same content in order.
     *
     * @param unorderedSet the unordered set
     * @return the new array with the ordered content
     */
    public abstract TraversalPath [] getOrderedInNew(
            TraversalPath [] unorderedSet );

    /**
     * Obtain the index where a new TraversalPath would be inserted to keep the
     * passed-in array ordered according to this sorter.
     *
     * @param newMeshObject the new TraversalPath potentially to be inserted
     * @param orderedSet the set into which newMeshObject could potentially be inserted
     * @return the index where newMeshObject would be inserted
     */
    public abstract int getIndexOfNew(
            TraversalPath    newMeshObject,
            TraversalPath [] orderedSet );

    /**
     * Obtain the index of a an existing TraversalPath in the array.
     *
     * @param objectToLookFor the TraversalPath that we look for
     * @param orderedSet the set in which we look.
     * @return the index of objectToLookFor within orderedSet, or -1 if not found
     */
    public abstract int getIndexOf(
            TraversalPath    objectToLookFor,
            TraversalPath [] orderedSet );

    /**
     * Obtain the user visible-name for this sorter.
     *
     * @return the user-visible name
     */
    public abstract String getUserName();
}
