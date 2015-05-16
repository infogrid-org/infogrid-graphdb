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

import org.infogrid.mesh.MeshObject;

/**
 * This interface is a type that can sort an array of MeshObjects.
 */
public interface MeshObjectSorter
{
    /**
     * Pass in an array of MeshObjects in arbitrary order. Return the same set
     * in order.
     *
     * @param unorderedSet the passed-in unordered set, is modified to be ordered
     * @return this a convenience return value: it's the same array as was passed in
     * @see #getOrderedInNew
     */
    public abstract MeshObject [] getOrderedInSame(
            MeshObject [] unorderedSet );

    /**
     * Pass in an array of MeshObject in arbitrary order. Return a new set
     * with the same content in order.
     *
     * @param unorderedSet the passed-in unordered set, is not modified
     * @return the new array with the ordered content
     * @see #getOrderedInSame
     */
    public abstract MeshObject [] getOrderedInNew(
            MeshObject [] unorderedSet );

    /**
     * Obtain the index where a new MeshObject would be inserted to keep the
     * passed-in array ordered according to this sorter. This does not actually
     * insert; it only asks a hypothetical question.
     *
     * @param newMeshObject the MeshObject potentially to be inserted
     * @param orderedSet the set to potentially insert into
     * @return the index of newMeshObject if it was inserted into orderedSet. This may be any
     *         number from 0 to orderedSet.length.
     */
    public abstract int getIndexOfNew(
            MeshObject    newMeshObject,
            MeshObject [] orderedSet );

    /**
     * Obtain the index of a MeshObject that's currently held in the array.
     * Return -1 if the object was not found in the set.
     *
     * @param objectToLookFor the object whose index we are looking for
     * @param orderedSet the ordered set in which we look
     * @return the index of objectToLookFor within orderedSet, or -1 if not found
     */
    public abstract int getIndexOf(
            MeshObject    objectToLookFor,
            MeshObject [] orderedSet );

    /**
     * Obtain the user visible name for this sorter in the current locale.
     *
     * @return the user-visible name for this sorted in the current locale
     */
    public abstract String getUserName();
}
