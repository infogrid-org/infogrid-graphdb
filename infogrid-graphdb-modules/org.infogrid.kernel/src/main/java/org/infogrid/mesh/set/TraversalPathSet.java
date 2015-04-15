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

import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.CursorIterable;
import org.infogrid.util.CursorIterator;

import java.beans.PropertyChangeListener;

/**
 * A set of TraversalPaths. It is similar to
 * MeshObjectSet, but it contains TraversalPaths, not MeshObjects.
 */
public interface TraversalPathSet
        extends
            CursorIterable<TraversalPath>
{
    /**
     * Set a name for this TraversalPathSet. Its only purpose is to help in debugging.
     * 
     * @param newValue the new value
     */
    public abstract void setDebugName(
            String newValue );

    /**
     * Obtain a name for this TraversalPathSet. Its only purpose is to help in debugging.
     * 
     * @return the name
     */
    public abstract String getDebugName();

    /**
     * Obtain the MeshObjectSetFactory by which this TraversalPathSet was created.
     *
     * @return the MeshObjectSetFactory
     */
    public abstract MeshObjectSetFactory getFactory();

    /**
     * Shorthand to obtain the MeshBase to which this TraversalPathSet belongs.
     * 
     * @return the MeshBase
     */
    public abstract MeshBase getMeshBase();

    /**
      * Obtain the TraversalPaths contained in this TraversalPathSet.
      *
      * @return the TraversalPaths contained in this TraversalPathSet
      */
    public abstract TraversalPath [] getTraversalPaths();

    /**
     * Assuming that this set contains no more than one TraversalPath, obtain this one TraversalPath. This
     * method is often very convenient if it is known to the application programmer that
     * a certain set may only contain one member. Invoking this method if the set has more
     * than one member will throw an Exception.
     *
     * @return the one element of the set, or null if the set is empty
     * @throws IllegalStateException thrown if the set contains more than one element
     */
    public abstract TraversalPath getSingleMember()
        throws
            IllegalStateException;

    /**
     * Obtain an Iterator that iterates over this set.
     *
     * @return the Iterator that iterates over the content of this set
     */
    public abstract CursorIterator<TraversalPath> iterator();

    /**
     * For JavaBeans-aware applications, we also provide this method to obtain an Iterator
     * iterating over the content of this set.
     *
     * @return an Iterator iterating over the content of this set
     */
    public abstract CursorIterator<TraversalPath> getIterator();

    /**
     * Determine whether a certain TraversalPath is contained in this set.
     * This method uses equals() to determine whether the path is contained.
     *
     * @param testObject the test TraversalPath
     * @return true if testObject is contained in this set
     */
    public abstract boolean contains(
            TraversalPath testObject );

    /**
     * Determine whether this set is empty.
     *
     * @return true if this set is empty
     */
    public abstract boolean isEmpty();

    /**
     * Determine the number of members of this set.
     *
     * @return the number of members of this set
     */
    public abstract int size();

    /**
     * Determine the number of members in this set. Same as size(), for JavaBeans-aware software.
     *
     * @return the number of members in this set
     */
    public abstract int getSize();

    /**
     * Obtain the destinations of the contained TraversalPaths as a MeshObjectSet.
     * While the same MeshObject may be a destination of more than one contained
     * TraversalPath, the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @return the destinations of the contained TraversalPaths as a MeshObjectSet
     */
    public abstract MeshObjectSet getDestinationsAsSet();

    /**
     * Obtain the MeshObjects found at the given index in all the contained TraversalPaths,
     * and return them as a MeshObjectSet.
     * While the same MeshObject may be a step in more than one contained TraversalPath,
     * the MeshObjectSet naturally only contains this MeshObject once.
     * 
     * @param index the index from which we want to obtain the MeshObject
     * @return the MeshObjects found at the given index as a MeshObjectSet
     */
    public abstract MeshObjectSet getStepAsSet(
            int index );

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addSoftContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public void addDirectContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public void addWeakContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public void addSoftContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Remove a PropertyChangeListener from all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param oldListener the to-be-removed listeners
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     */
    public void removeContentPropertyChangeListener(
            PropertyChangeListener oldListener );
}
