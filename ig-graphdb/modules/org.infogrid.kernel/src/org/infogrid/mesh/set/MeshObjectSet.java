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

import java.beans.PropertyChangeListener;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.CursorIterable;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.Identifier;
import org.infogrid.util.NotSingleMemberException;

/**
  * An unordered collection of MeshObject without duplicates (set semantics).
  */
public interface MeshObjectSet
        extends
            CursorIterable<MeshObject>
{
    /**
     * Set a name for this MeshObjectSet. Its only purpose is to help in debugging.
     * 
     * @param newValue the new value
     */
    public abstract void setDebugName(
            String newValue );

    /**
     * Obtain a name for this MeshObjectSet. Its only purpose is to help in debugging.
     * 
     * @return the name
     */
    public abstract String getDebugName();

    /**
     * Obtain the MeshObjectSetFactory by which this MeshObjectSet was created.
     *
     * @return the MeshObjectSetFactory
     */
    public abstract MeshObjectSetFactory getFactory();

    /**
     * Shorthand to obtain the MeshBase to which this MeshObjectSet belongs.
     * 
     * @return the MeshBase
     */
    public abstract MeshBase getMeshBase();

    /**
      * Obtain the MeshObjects contained in this set.
      *
      * @return the MeshObjects contained in this set
      */
    public abstract MeshObject[] getMeshObjects();

    /**
     * Get the Nth MeshObject contained in this set.
     * While the order of MeshObjects in a MeshObjectSet typically does not change,
     * no assumptions should be made about this. Compare
     * with {@link org.infogrid.mesh.set.OrderedMeshObjectSet OrderedMeshObjectSet}.
     *
     * @param n the index of the MeshObject to be returned
     * @return the Nth MeshObject contained in this set.
     */
    public abstract MeshObject get(
            int n );

    /**
     * Assuming that this set contains no more than one object, obtain this one object. This
     * method is often very convenient if it is known to the application programmer that
     * a certain set may only contain one member. Invoking this method if the set has more
     * than one member will throw an IllegalStateException.
     *
     * @return the one element of the set, or null if the set is empty
     * @throws NotSingleMemberException thrown if the set contains more than one element
     */
    public abstract MeshObject getSingleMember()
        throws
            NotSingleMemberException;

    /**
     * Convenience method to return the content of this MeshObjectSet as an
     * array of the canonical Identifiers of the member MeshObjects.
     *
     * @return the array of IdentifierValues representing the IdentifierValues of the members
     *         of this MeshObjectSet
     */
    public abstract MeshObjectIdentifier[] asIdentifiers();

    /**
     * Obtain an Iterator iterating over the content of this set.
     *
     * @return an Iterator iterating over the content of this set
     */
    public abstract CursorIterator<MeshObject> iterator();

    /**
     * For JavaBeans-aware applications, we also provide this method to obtain an Iterator
     * iterating over the content of this set.
     *
     * @return an Iterator iterating over the content of this set
     */
    public abstract CursorIterator<MeshObject> getIterator();

    /**
     * Determine whether this set contains a certain MeshObject.
     *
     * @param testObject the MeshObject to look for
     * @return true if this set contains the given MeshObject
     * @throws WrongMeshBaseException thrown if the testObject is contained in a different MeshBase than the MeshObjects in this set
     */
    public abstract boolean contains(
            MeshObject testObject )
        throws
            WrongMeshBaseException;

    /**
     * Determine whether this set contains a MeshObject with this Identifier.
     *
     * @param identifier the Identifier of the MeshObject to look for
     * @return true if this set contains the given MeshObject
     */
    public abstract boolean contains(
            Identifier identifier );

    /**
     * Determine whether this set contains all MeshObjects in a supposed subset.
     *
     * @param subset the supposed subset
     * @return true if this set contains all MeshObjects in the supposed subset
     * @throws WrongMeshBaseException thrown if a tested object is contained in a different MeshBase than the MeshObjects in this set
     */
    public abstract boolean containsAll(
            MeshObjectSet subset )
        throws
            WrongMeshBaseException;

    /**
     * Determine whether this set has the same content as another set.
     *
     * @param other the MeshObjectSet to compare to
     */
    public abstract boolean hasSameContent(
            MeshObjectSet other );

    /**
     * Convenience method to to easily find a member of this set by providing a
     * MeshObjectSelector that will select the MeshObject to be found. This method will return
     * the match and THEN STOP. If you expect more than one match, do not use this method.
     *
     * @param selector the criteria for selection
     * @return the first found MeshObject, or null if none
     */
    public abstract MeshObject find(
            MeshObjectSelector selector );

    /**
     * Determine whether this set is empty.
     *
     * @return true if this set is empty
     */
    public abstract boolean isEmpty();

    /**
     * Determine the number of members in this set.
     *
     * @return the number of members in this set
     */
    public abstract int size();

    /**
     * Determine the number of members in this set. Same as size(), for JavaBeans-aware software.
     *
     * @return the number of members in this set
     */
    public abstract int getSize();

    /**
      * Returns an MeshObjectSet which is the union of all MeshObjectSets obtained
      * by traversing this TraversalSpecification for each of the MeshObjects in this set. Note
      * that the semantics of MeshObjectSet do not allow duplicates and thus there
      * won't be any duplicates in this result. This is a convenience function.
      *
      * @param theTraversalSpecification specifies how to traverse
      * @return the set of MeshObjects obtained through the traversal
      */
    public abstract MeshObjectSet traverse(
            TraversalSpecification theTraversalSpecification );

    /**
     * Returns an MeshObjectSet which is the union of all MeshObjectSets obtained
     * by traversing this TraversalSpecification for each of the MeshObjects in this set.
     * Specify whether or not equivalent MeshObjects should be considered as well. Note
     * that the semantics of MeshObjectSet do not allow duplicates and thus there
     * won't be any duplicates in this result. This is a convenience function.
     *
     * @param theTraversalSpecification specifies how to traverse
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the set of MeshObjects obtained through the traversal
     */
    public abstract MeshObjectSet traverse(
            TraversalSpecification theTraversalSpecification,
            boolean                considerEquivalents );

    /**
     * Traverse to the neighbor MeshObjects of all the members of this set. This is
     * a convenience method.
     *
     * @return the set of neighbor MeshObjects
     */
    public abstract MeshObjectSet traverseToNeighborMeshObjects();

    /**
     * Traverse to the neighbor MeshObjects of the members of this set. Specify whether
     * to consider equivalent MeshObjects as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the set of neighbor Entities
     */
    public abstract MeshObjectSet traverseToNeighborMeshObjects(
            boolean considerEquivalents );

    /**
     * Set the PropertyTypes whose change events we forward to content
     * PropertyChangeListeners.
     *
     * @param filter the PropertyTypes whose change events we forward to content PropertyChangeListeners
     * @see #getFilterPropertyTypes
     */
    public void setFilterPropertyTypes(
            PropertyType [] filter );

    /**
     * Obtain the PropertyTypes whose change events we forward to content
     * PropertyChangeListeners. This may return null if none were specified.
     *
     * @return the PropertyTypes whose change events we forward to content PropertyChangeListeners
     * @see #setFilterPropertyTypes
     */
    public abstract PropertyType [] getFilterPropertyTypes();

    /**
     * Set whether this set forward RolePlayerTableEvents to content
     * PropertyChangeListeners.
     *
     * @param newValue true if we forward RolePlayerTableEvents to content PropertyChangeListeners
     */
    public abstract void setForwardingRolePlayerUpdateEvents(
            boolean newValue );

    /**
     * Determine whether this set forward RolePlayerTableEvents to content
     * PropertyChangeListeners.
     *
     * @return true if we forward RolePlayerTableEvents to content PropertyChangeListeners
     */
    public abstract boolean isForwardingRolePlayerUpdateEvents();

    /**
     * Adding a listener to this set through this method is equivalent to adding
     * the listener to all members of this set. Generally, this listener is
     * being notified whenever it would have been notified if it had been
     * a subscriber of each of the underlying MeshObjects directly. However,
     * for performance reasons, some classes implementing this interface may
     * provide capabilities to the user to filter events before re-broadcasting.
     * Refer to the documentation of subclasses to determine whether or not a
     * certain event will be re-broadcast. For example, some kinds of
     * set will only rebroadcast events that relate to certain PropertyTypes.
     *
     * @param newListener the to-be-added listener
     * @see #addSoftContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public abstract void addDirectContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Adding a listener to this set through this method is equivalent to adding
     * the listener to all members of this set. Generally, this listener is
     * being notified whenever it would have been notified if it had been
     * a subscriber of each of the underlying MeshObjects directly. However,
     * for performance reasons, some classes implementing this interface may
     * provide capabilities to the user to filter events before re-broadcasting.
     * Refer to the documentation of subclasses to determine whether or not a
     * certain event will be re-broadcast. For example, some kinds of
     * set will only rebroadcast events that relate to certain PropertyTypes.
     *
     * @param newListener the to-be-added listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public abstract void addWeakContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Adding a listener to this set through this method is equivalent to adding
     * the listener to all members of this set. Generally, this listener is
     * being notified whenever it would have been notified if it had been
     * a subscriber of each of the underlying MeshObjects directly. However,
     * for performance reasons, some classes implementing this interface may
     * provide capabilities to the user to filter events before re-broadcasting.
     * Refer to the documentation of subclasses to determine whether or not a
     * certain event will be re-broadcast. For example, some kinds of
     * set will only rebroadcast events that relate to certain PropertyTypes.
     *
     * @param newListener the to-be-added listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public abstract void addSoftContentPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
      * Remove a content PropertyChangeListener.
      *
      * @param oldListener the to-be-removed listener
      * @see #addDirectContentPropertyChangeListener
      * @see #addSoftContentPropertyChangeListener
      * @see #addWeakContentPropertyChangeListener
      */
    public abstract void removeContentPropertyChangeListener(
            PropertyChangeListener oldListener );
}
