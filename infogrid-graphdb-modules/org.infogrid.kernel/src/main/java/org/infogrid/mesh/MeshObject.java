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

package org.infogrid.mesh;

import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Map;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.Role;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.util.HasIdentifier;
import org.infogrid.util.IsDeadException;
import org.infogrid.util.text.HasStringRepresentation;

/**
 * <p>The core abstraction in InfoGrid.</p>
 * 
 * <p>Each MeshObject is identified by a {@link MeshObjectIdentifier}, which
 * is globally unique. Each MeshObject is managed by exactly one
 * {@link org.infogrid.meshbase.MeshBase}.</p>
 * 
 * <p>Each MeshObject may be blessed by one or more {@link org.infogrid.model.primitives.EntityType}s,
 * which may define properties for the MeshObject (by means of
 * {@link org.infogrid.model.primitives.PropertyType}).</p>
 * 
 * <p>Each MeshObject can stand on its own; pairs of MeshObjects may enter into relationships,
 * each of which may be blessed by one or more {@link org.infogrid.model.primitives.RelationshipType}s.</p>
 * 
 * <p>Properties of MeshObjects may be read, and relationships may be traversed outside
 * of {@link org.infogrid.meshbase.transaction.Transaction} boundaries; for updates, Transactions
 * are needed. (See {@link org.infogrid.meshbase.MeshBase#createTransactionNow} and similar
 * methods)</p>
 * 
 * <p>A MeshObject may be part of an equivalence set. In InfoGrid, an equivalence set of
 * MeshObjects collects one or more MeshObjects and considers them semantically equivalent.
 * When traversing relationships, all relationships of the MeshObjects in the equivalence
 * set are considered as if they were relationships of the same MeshObject.</p>
 * 
 * <p>A caller may or may not have the appropriate authorization to perform any of the
 * operations that may throw NotPermittedException, in which this exception is thrown.
 * Other operations may silently return only a subset of the information they could return
 * to some callers. The interface does not make any statements on this level about
 * the exact behavior, other than stating that the experienced behavior may be different
 * for different callers.</p>
 */
public interface MeshObject
        extends
            HasIdentifier,
            HasStringRepresentation
{
    /**
     * Obtain the globally unique identifier of this MeshObject.
     *
     * @return the globally unique identifier of this MeshObject
     */
    public abstract MeshObjectIdentifier getIdentifier();

    /**
     * Obtain the MeshBase that contains this MeshObject. This is immutable for the
     * lifetime of this instance.
     *
     * @return the MeshBase that contains this MeshObject.
     */
    public abstract MeshBase getMeshBase();
 
    /**
     * Obtain the time of creation of this MeshObject. This is immutable for the
     * lifetime of the MeshObject.
     *
     * @return the time this MeshObject was created in <code>System.currentTimeMillis()</code> format
     */
    public abstract long getTimeCreated();

    /**
     * Obtain the time of last update of this MeshObject. This changes automatically
     * every time the MeshObject is changed.
     *
     * @return the time this MeshObject was last updated in <code>System.currentTimeMillis()</code> format
     */
    public abstract long getTimeUpdated();

    /**
     * Obtain the time of the last reading operation of this MeshObject. This changes automatically
     * every time the MeshObject is read.
     *
     * @return the time this MeshObject was last read in <code>System.currentTimeMillis()</code> format
     */
    public abstract long getTimeRead();

    /**
     * Set the time when this MeshObject expires. If -1, it never does.
     *
     * @param newValue the new value, in <code>System.currentTimeMillis()</code> format
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract void setTimeExpires(
            long newValue )
        throws
            NotPermittedException;

    /**
     * Obtain the time when this MeshObject expires. If this returns -1, it never does.
     * This may return a time before the present, indicating that the MeshObject is stale
     * and needs to be refreshed in some fashion. Being stale is different from being dead
     * (see {@link #getIsDead}).
     *
     * @return the time at which this MeshObject expires, in <code>System.currentTimeMillis()</code> format
     */
    public abstract long getTimeExpires();

    /**
     * Determine whether this MeshObject is dead and should not be used any further.
     *
     * @return true if the MeshObject is dead
     */
    public abstract boolean getIsDead();

    /**
     * Throw an IsDeadException if this MeshObject is dead and should not be used any further.
     * Does nothing as long as this MeshObject is alive.
     *
     * @throws IsDeadException thrown if this MeshObject is dead already
     */
    public abstract void checkAlive()
        throws
            IsDeadException;

    /**
     * Determine whether this MeshObject is the home object of its MeshBase.
     * 
     * @return true if it is the home object
     */
    public abstract boolean isHomeObject();

// --

    /**
     * Obtain the value of a Property.
     *
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @return the current value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #setPropertyValue
     */
    public abstract PropertyValue getPropertyValue(
            PropertyType thePropertyType )
        throws
            IllegalPropertyTypeException,
            NotPermittedException;

    /**
     * Obtain all PropertyValues for the PropertyTypes provided, in the same sequence as the provided
     * PropertyTypes. If a PropertyType does not exist on this MeshObject, or if access to one of the
     * PropertyTypes is not permitted, this will throw an exception. This is a convenience method.
     *
     * @param thePropertyTypes the PropertyTypes
     * @return the PropertyValues, in the same sequence as PropertyTypes
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract PropertyValue [] getPropertyValues(
            PropertyType [] thePropertyTypes )
        throws
            IllegalPropertyTypeException,
            NotPermittedException;

    /**
     * Set the value of a Property.
     * 
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @param newValue the new value of the Property
     * @return old value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #getPropertyValue
     */
    public abstract PropertyValue setPropertyValue(
            PropertyType  thePropertyType,
            PropertyValue newValue )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Set the value of a Property, and specify a time when that change happened. The caller must
     * have the appropriate rights to invoke this; typical callers do not have the rights because this
     * call is mostly intended for system-internal purposes.
     * 
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @param newValue the new value of the Property
     * @param timeUpdated the time at which this change occurred
     * @return old value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #getPropertyValue
     */
    public abstract PropertyValue setPropertyValue(
            PropertyType  thePropertyType,
            PropertyValue newValue,
            long          timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Set the value of several Properties at the same time. The PropertyTypes identifying the Properties
     * and their new PropertyValues are given in the same sequence. This method sets either all values, or none.
     *
     * @param thePropertyTypes the PropertyTypes that identify the correct Properties of this MeshObject
     * @param thePropertyValues the new values of the Properties
     * @return old values of the Properties
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public abstract PropertyValue [] setPropertyValues(
            PropertyType []  thePropertyTypes,
            PropertyValue [] thePropertyValues )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Set the value of several Properties at the same time. The PropertyTypes identifying the Properties
     * and their new PropertyValues are given as paids in the Map. This method sets either all values, or none.

     * @param newValues Map of PropertyType to PropertyValue
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public abstract void setPropertyValues(
            Map<PropertyType,PropertyValue> newValues )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Set the value of several Properties, given their PropertyTypes and PropertyValues, in the same sequence,
     * and specify a time when that change happened. This method sets either all values, or none.
     *
     * @param thePropertyTypes the PropertyTypes whose values we want to set
     * @param thePropertyValues the new values for the PropertyTypes for this MeshObject
     * @param timeUpdated the time at which this change occurred
     * @return old value of the Properties
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public abstract PropertyValue [] setPropertyValues(
            PropertyType []  thePropertyTypes,
            PropertyValue [] thePropertyValues,
            long             timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Set the value of several Properties, given their PropertyTypes and PropertyValues,
     * and specify a time when that change happened. This method sets either all values, or none.
     *
     * @param newValues Map of PropertyType to PropertyValue
     * @param timeUpdated the time at which this change occurred
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public abstract void setPropertyValues(
            Map<PropertyType,PropertyValue> newValues,
            long                            timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException;

    /**
     * Obtain the set of all PropertyTypes currently used with this MeshObject.
     *
     * @return the set of all PropertyTypes
     */
    public abstract PropertyType [] getAllPropertyTypes();

    /**
     * <p>This is a convenience method to obtain the value of a property by providing the
     * name of the property. If such a property could be found, this call returns its value. If not,
     * it throws an MeshTypeNotFoundException.</p>
     *
     * <p>Warning: sometimes, a MeshObject may carry two Properties with the same (short) name.
     * It is undefined which of those PropertyValues this call will return.</p>
     *
     * @param propertyName the name of the property
     * @return the PropertyValue
     * @throws MeshTypeNotFoundException thrown if a Property by this name could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract PropertyValue getPropertyValueByName(
            String propertyName )
        throws
            MeshTypeNotFoundException,
            NotPermittedException;

    /**
     * Traverse from this MeshObject to all directly related MeshObjects. Directly
     * related MeshObjects are those MeshObjects that are participating in a
     * relationship with this MeshObject.
     *
     * @return the set of MeshObjects that are directly related to this MeshObject
     */
    public abstract MeshObjectSet traverseToNeighborMeshObjects();

    /**
     * Traverse from this MeshObject to all directly related MeshObjects. Directly
     * related MeshObjects are those MeshObjects that are participating in a
     * relationship with this MeshObject. Specify whether to consider equivalents
     * as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the set of MeshObjects that are directly related to this MeshObject
     */
    public abstract MeshObjectSet traverseToNeighborMeshObjects(
            boolean considerEquivalents );

    /**
     * Obtain the MeshObjectIdentifiers of the neighbors of this MeshObject. This is sometimes a
     * more efficient operation than to traverse to the neighbors and determine the
     * MeshObjectIdentifiers from there.
     *
     * @return the MeshObjectIdentifiers of the neighbors, if any
     */
    public abstract MeshObjectIdentifier [] getNeighborMeshObjectIdentifiers();
    
    /**
     * Relate this MeshObject to another MeshObject. This does not bless the relationship.
     *
     * @param newNeighbor the MeshObject to relate to
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #unrelate
     * @see #relateAndBless
     */
    public abstract void relate(
            MeshObject newNeighbor )
        throws
            RelatedAlreadyException,
            TransactionException;

    /**
     * Unrelate this MeshObject from another MeshObject. This will also remove all blessings from the relationship.
     *
     * @param neighbor the MeshObject to unrelate from
     * @throws NotRelatedException thrown if this MeshObject is not related to the otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     */
    public abstract void unrelate(
            MeshObject neighbor )
        throws
            NotRelatedException,
            TransactionException,
            NotPermittedException;

    /**
     * Determine whether this MeshObject is related to another MeshObject.
     *
     * @param otherObject the MeshObject to which this MeshObject may be related
     * @return true if this MeshObject is currently related to otherObject
     */
    public abstract boolean isRelated(
            MeshObject otherObject );

    /**
     * Determine whether this MeshObject is related to another MeshObject whose MeshObjectIdentifier is given.
     *
     * @param otherObjectIdentifier the MeshObjectIdentifier of the MeshObject to which this MeshObject may be related
     * @return true if this MeshObject is currently related to otherObject
     */
    public abstract boolean isRelated(
            MeshObjectIdentifier otherObjectIdentifier );

// --

    /**
     * Make this MeshObject support the provided EntityType. (The name of this method comes from Perl's bless method.)
     *
     *
     * 
     * @param type the new EntityType to be supported by this MeshObject
     * @throws EntityBlessedAlreadyException thrown if this MeshObject is blessed already with this EntityType
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract void bless(
            EntityType type )
        throws
            EntityBlessedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;

    /**
     * Make this MeshObject support the provided one or more EntityTypes. As a result, the
     * MeshObject will either be blessed with all of the EntityTypes, or none.
     *  (The name of this method comes from Perl's bless method.)
     * 
     * @param types the new EntityTypes to be supported by this MeshObject
     * @throws EntityBlessedAlreadyException thrown if this MeshObject is blessed already with at least one of these EntityTypes
     * @throws IsAbstractException thrown if at least one of the EntityTypes is abstract and cannot be instantiated
     * @throws TransactionException thrown if invoked outside of proper transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract void bless(
            EntityType [] types )
        throws
            EntityBlessedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;

    /**
     * Makes this MeshObject stop supporting the provided EntityType. This may fail with an
     * RoleTypeRequiresEntityTypeException because the RoleType of a relationship in which
     * this MeshObject participates requires this MeshObject to have the EntityType
     * that is supposed to be unblessed. To avoid this, unbless the relevant relationship(s)
     * first.
     * 
     * @param type the EntityType that the MeshObject will stop supporting
     * @throws RoleTypeRequiresEntityTypeException thrown if this MeshObject plays a role that requires the MeshObject to remain being blessed with this EntityType
     * @throws EntityNotBlessedException thrown if this MeshObject does not currently support this EntityType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unbless(
            EntityType type )
        throws
            RoleTypeRequiresEntityTypeException,
            EntityNotBlessedException,
            TransactionException,
            NotPermittedException;

    /**
     * Makes this MeshObject stop supporting all of the provided EntityTypes. As a result,
     * the MeshObject will either be unblessed from all of the EntityTypes, or none. 
     * This may fail with an
     * RoleTypeRequiresEntityTypeException because the RoleType of a relationship in which
     * this MeshObject participates requires this MeshObject to have the EntityType
     * that is supposed to be unblessed. To avoid this, unbless the relevant relationship(s) first.
     * 
     * @param types the EntityTypes that the MeshObject will stop supporting
     * @throws RoleTypeRequiresEntityTypeException thrown if this MeshObject plays one or more roles that requires the MeshObject to remain being blessed with at least one of the EntityTypes
     * @throws EntityNotBlessedException thrown if this MeshObject does not support at least one of the given EntityTypes
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unbless(
            EntityType [] types )
        throws
            RoleTypeRequiresEntityTypeException,
            EntityNotBlessedException,
            TransactionException,
            NotPermittedException;

    /**
     * Obtain the EntityTypes that this MeshObject is currently blessed with.
     *
     * @return the EntityTypes
     */
    public abstract EntityType [] getTypes();

    /**
     * Determine whether this MeshObject currently supports this EntityType.
     * By default, this returns true even if the MeshObject is blessed by a
     * subtype of the provided EntityType instead of the EntityType directly.
     * 
     * @param type the EntityType to look for
     * @return true if this MeshObject supports this MeshType or a subtype
     */
    public abstract boolean isBlessedBy(
            EntityType type );

    /**
     * Determine whether this MeshObject currently supports this EntityType.
     * Specify whether or not subtypes of the provided EntityType should be considered.
     * 
     * @param type the EntityType to look for
     * @param considerSubtypes if true, return true even if only a subtype matches
     * @return true if this MeshObject supports this MeshType
     */
    public abstract boolean isBlessedBy(
            EntityType type,
            boolean    considerSubtypes );

    /**
     * Determine the specific subtypes of the provided EntityType with which this
     * MeshObject has been blessed. If this MeshObject has not been blessed with a
     * subtype of the provided EntityType, return a zero-length array. If this MeshObject
     * has not been blessed with the provided EntityType either, throw an
     * EntityNotBlessedException.
     *
     * @param type the EntityType
     * @return the subtypes, if any
     * @throws EntityNotBlessedException thrown if the MeshObject is not blessed by the EntityType
     * @see #determineSingleBlessedSubtype
     */
    public abstract EntityType [] determineBlessedSubtypes(
            EntityType type )
        throws
            EntityNotBlessedException;

    /**
     * Convenience method to determine the single subtype of the provided EntityType with
     * which this MeshObject has been blessed. If this MeshObject has not been blessed with a
     * subtype of the provided EntityType, return <code>null</code>. If it has
     * been blessed with more than one subtype, throw an IllegalStateException. If this MeshObject
     * has not been blessed with the provided EntityType either, throw an
     * EntityNotBlessedException.
     *
     * @param type the EntityType
     * @return the subtype, if any
     * @throws EntityNotBlessedException thrown if the MeshObject is not blessed by the EntityType
     * @throws IllegalStateException thrown if the MeshObject is blessed by more than one subtype
     * @see #determineBlessedSubtypes
     */
    public abstract EntityType determineSingleBlessedSubtype(
            EntityType type )
        throws
            EntityNotBlessedException,
            IllegalStateException;

    /**
     * If the provided TypedMeshObjectFacade is a facade of this instance, get the EntityType
     * that corresponds to this TypedMeshObjectFacade.
     * 
     * @param obj the TypedMeshObjectFacade
     * @return the EntityType that corresponds to this TypedMeshObjectFacade
     * @throws IllegalArgumentException thrown if the TypedMeshObjectFacade is not a facade of this MeshObject
     */
    public abstract EntityType getTypeFor(
            TypedMeshObjectFacade obj )
        throws
            IllegalArgumentException;

    /**
     * Obtain an instance of (a subclass of) TypedMeshObjectFacade that provides the type-safe interface
     * to this MeshObject for a particular EntityType. Throw NotBlessedException
     * if this MeshObject does not current support this EntityType.
     * 
     * @param type the EntityType
     * @return the TypedMeshObjectFacade for this MeshObject
     * @throws NotBlessedException thrown if this MeshObject does not currently support this EntityType
     */
    public abstract TypedMeshObjectFacade getTypedFacadeFor(
            EntityType type )
        throws
            EntityNotBlessedException;

    /**
     * Make a relationship of this MeshObject to another MeshObject support the provided RoleType.
     * (The name of this method comes from Perl's bless method.)
     * 
     * @param thisEnd the RoleType of the RelationshipType that is instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject whose relationship to this MeshObject shall be blessed
     * @throws RoleTypeBlessedAlreadyException thrown if the relationship to the other MeshObject is blessed
     *         already with this RoleType
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws IsAbstractException thrown if the RoleType belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #relateAndBless
     * @see #unrelate
     */
    public void blessRelationship(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            RoleTypeBlessedAlreadyException,
            EntityNotBlessedException,
            NotRelatedException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;
    
    /**
     * Make a relationship of this MeshObject to another MeshObject support the provided RoleTypes.
     * As a result, this relationship will support either all RoleTypes or none.
     * (The name of this method comes from Perl's bless method.)
     * 
     * @param thisEnd the RoleTypes of the RelationshipTypes that are instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject whose relationship to this MeshObject shall be blessed
     * @throws RoleTypeBlessedAlreadyException thrown if the relationship to the other MeshObject is blessed
     *         already with one ore more of the given RoleTypes
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws IsAbstractException thrown if one of the RoleTypes belong to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #relateAndBless
     * @see #unrelate
     */
    public void blessRelationship(
            RoleType [] thisEnd,
            MeshObject  neighbor )
        throws
            RoleTypeBlessedAlreadyException,
            EntityNotBlessedException,
            NotRelatedException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;
    
    /**
     * Convenience method to relate this MeshObject to another MeshObject, and bless the new relationship
     * with the provided RoleType.
     * 
     * @param thisEnd the RoleType of the RelationshipType that is instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject to which a relationship is to be created and blessed
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws IsAbstractException thrown if the provided RoleType belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #blessRelationship
     * @see #unrelate
     */
    public abstract void relateAndBless(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            EntityNotBlessedException,
            RelatedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;

    /**
     * Convenience method to relate this MeshObject to another MeshObject, and bless the new relationship
     * with all of the provided RoleTypes. As a result, this relationship will support either all RoleTypes or none.
     * 
     * @param thisEnd the RoleTypes of the RelationshipTypes that are to be instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject to which a relationship is to be created and blessed
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws IsAbstractException thrown if one of the provided RoleTypes belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #blessRelationship
     * @see #unrelate
     */
    public abstract void relateAndBless(
            RoleType [] thisEnd,
            MeshObject  neighbor )
        throws
            EntityNotBlessedException,
            RelatedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException;

    /**
     * Make a relationship of this MeshObject to another MeshObject stop supporting the provided RoleType.
     * 
     * @param thisEnd the RoleType of the RelationshipType at the end that this MeshObject is attached to, and that shall be removed
     * @param neighbor the other MeshObject whose relationship to this MeshObject shall be unblessed
     * @throws RoleTypeNotBlessedException thrown if the relationship to the other MeshObject does not support the RoleType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unblessRelationship(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            RoleTypeNotBlessedException,
            NotRelatedException,
            TransactionException,
            NotPermittedException;

    /**
     * Make a relationship of this MeshObject to another MeshObject stop supporting the provided RoleTypes.
     * As a result, either all RoleTypes will be unblessed or none.
     * 
     * @param thisEnd the RoleTypes of the RelationshipTypes at the end that this MeshObject is attached to, and that shall be removed
     * @param neighbor the other MeshObject whose relationship to this MeshObject shall be unblessed
     * @throws RoleTypeNotBlessedException thrown if the relationship to the other MeshObject does not support at least one of the RoleTypes
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unblessRelationship(
            RoleType [] thisEnd,
            MeshObject  neighbor )
        throws
            RoleTypeNotBlessedException,
            NotRelatedException,
            TransactionException,
            NotPermittedException;

    /**
      * Traverse a TraversalSpecification from this MeshObject to obtain a set of MeshObjects.
      * This will consider all MeshObjects equivalent to this one as the start MeshObject.
      *
      * @param theTraverseSpec the TraversalSpecification to traverse
      * @return the set of MeshObjects found as a result of the traversal
      */
    public abstract MeshObjectSet traverse(
            TraversalSpecification theTraverseSpec );

    /**
      * Traverse a TraversalSpecification from this MeshObject to obtain a set of MeshObjects.
      * Specify whether relationships of equivalent MeshObjects should be considered as well.
      *
      * @param theTraverseSpec the TraversalSpecification to traverse
      * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
      *        if false, only this MeshObject will be used as the start
      * @return the set of MeshObjects found as a result of the traversal
      */
    public abstract MeshObjectSet traverse(
            TraversalSpecification theTraverseSpec,
            boolean                considerEquivalents );

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in. This will return only one
     * instance of the same RoleType object, even if the MeshObject participates in this RoleType
     * multiple times with different other MeshObjects.
     * 
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public abstract RoleType [] getRoleTypes();

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in. This will return only one
     * instance of the same RoleType object, even if the MeshObject participates in this RoleType
     * multiple times with different other MeshObjects. Specify whether equivalent MeshObjects
     * should be considered as well.
     * 
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public abstract RoleType [] getRoleTypes(
            boolean considerEquivalents );
    
    /**
     * Obtain the MeshTypeIdentifiers of the RoleTypes that this MeshObject plays with a
     * given neighbor MeshObject identified by its MeshObjectIdentifier.
     * 
     * @param neighborIdentifier the MeshObjectIdentifier of the neighbor MeshObject
     * @return the identifiers of the RoleTypes
     * @throws NotRelatedException thrown if the specified MeshObject is not actually a neighbor
     */
    public abstract MeshTypeIdentifier [] getRoleTypeIdentifiers(
            MeshObjectIdentifier neighborIdentifier )
        throws
            NotRelatedException;
    
    /**
     * Obtain the MeshTypeIdentifiers of the RoleTypes that this MeshObject plays with a
     * given neighbor MeshObject identified by its MeshObjectIdentifier.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the neighbor MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the identifiers of the RoleTypes
     * @throws NotRelatedException thrown if the specified MeshObject is not actually a neighbor
     */
    public abstract MeshTypeIdentifier [] getRoleTypeIdentifiers(
            MeshObjectIdentifier neighborIdentifier,
            boolean              considerEquivalents )
        throws
            NotRelatedException;

    /**
     * Obtain the Roles that this MeshObject currently participates in.
     *
     * @return the Roles that this MeshObject currently participates in.
     */
    public abstract Role [] getRoles();
    
    /**
     * Obtain the Roles that this MeshObject currently participates in.
     * Specify whether relationships of equivalent MeshObjects
     * should be considered as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     *        if false, only this MeshObject will be used as the start
     * @return the Roles that this MeshObject currently participates in.
     */
    public abstract Role [] getRoles(
            boolean considerEquivalents );
    
    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * specified other MeshObject.
     *
     * @param neighbor the other MeshObject
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and the neighbor MeshObject are not related
     */
    public abstract RoleType [] getRoleTypes(
            MeshObject neighbor )
        throws
            NotRelatedException;

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * MeshObject with the specified MeshObjectIdentifier.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the other MeshObject
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and the neighbor MeshObject are not related
     */
    public abstract RoleType [] getRoleTypes(
            MeshObjectIdentifier neighborIdentifier )
        throws
            NotRelatedException;

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * specified other MeshObject.
     * Specify whether relationships of equivalent MeshObjects should be considered
     * as well.
     *
     * @param neighbor the other MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and the neighbor MeshObject are not related
     */
    public abstract RoleType [] getRoleTypes(
            MeshObject neighbor,
            boolean    considerEquivalents )
        throws
            NotRelatedException;

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * MeshObject with the specified MeshObjectIdentifier.
     * Specify whether relationships of equivalent MeshObjects should be considered
     * as well.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the other MeshObject
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well;
     *        if false, only this MeshObject will be used as the start
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and the neighbor MeshObject are not related
     */
    public abstract RoleType [] getRoleTypes(
            MeshObjectIdentifier neighborIdentifier,
            boolean              considerEquivalents )
        throws
            NotRelatedException;

    /**
     * Determine whether this MeshObject's relationship to the other MeshObject is blessed
     * with a given RoleType. Also returns false if the two MeshObjects are not related.
     *
     * @param thisEnd the RoleTypes of the RelationshipTypes at the end that this MeshObject is attached to
     * @param neighbor the other MeshObject
     * @return true if this MeshObject has a relationship to the other MeshObject  and it is blessed with the given RoleType
     */
    public abstract boolean isRelated(
            RoleType   thisEnd,
            MeshObject neighbor );

    /**
     * Determine whether this MeshObject is related to another MeshObject whose MeshObjectIdentifier is given is blessed
     * with a given RoleType. Also returns false if the two MeshObjects are not related.
     *
     * @param thisEnd the RoleTypes of the RelationshipTypes at the end that this MeshObject is attached to
     * @param neighborIdentifier the MeshObjectIdentifier of the other MeshObject
     * @return true if this MeshObject is currently related to otherObject
     */
    public abstract boolean isRelated(
            RoleType             thisEnd,
            MeshObjectIdentifier neighborIdentifier );

    /**
     * Add another MeshObject as an equivalent. All MeshObjects that are already equivalent
     * to this MeshObject, and all MeshObjects that are already equivalent to the newly
     * added MeshObject, are now equivalent.
     *
     * @param equiv the new equivalent
     * @throws EquivalentAlreadyException thrown if the provided MeshObject is already an equivalent of this MeshObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void addAsEquivalent(
            MeshObject equiv )
        throws
            EquivalentAlreadyException,
            TransactionException,
            NotPermittedException;
    
    /**
     * Obtain the set of MeshObjects, including this one, that are equivalent.
     * This always returns at least this MeshObject.
     *
     * @return the set of MeshObjects that are equivalent
     */
    public MeshObjectSet getEquivalents();
    
    /**
     * Obtain the Identifiers of the equivalent MeshObjects. This is sometimes more efficient than
     * traversing to the equivalents, and determining the MeshObjectIdentifiers.
     *
     * @return the MeshObjectIdentifiers of the equivalents
     */
    public MeshObjectIdentifier [] getEquivalentMeshObjectIdentifiers();

    /**
     * Remove this MeshObject as an equivalent from the set of equivalents. If this MeshObject
     * is not currently equivalent to any other MeshObject, this does nothing.
     *
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void removeAsEquivalent()
        throws
            TransactionException,
            NotPermittedException;

    /**
     * Add a PropertyChangeListener.
     * This listener is added directly to the listener list, which prevents the
     * listener from being garbage-collected before this Object is being garbage-collected.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addDirectPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener.
     * This listener is added to the listener list using a <code>java.lang.ref.SoftReference</code>,
     * which allows the listener to be garbage-collected before this Object is being garbage-collected
     * according to the semantics of Java references.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addWeakPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener.
     * This listener is added to the listener list using a <code>java.lang.ref.WeakReference</code>,
     * which allows the listener to be garbage-collected before this Object is being garbage-collected
     * according to the semantics of Java references.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addSoftPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Remove a PropertyChangeListener.
     *
     * @param oldListener the to-be-removed PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     */
    public abstract void removePropertyChangeListener(
            PropertyChangeListener oldListener );

    /**
     * Determine whether there is at least one currently subscribed PropertyChangeListener.
     *
     * @return true if there is at least one currently subscribed PropertyChangeListener.
     */
    public abstract boolean hasPropertyChangeListener();

    /**
     * This method returns an Iterator over the currently subscribed PropertyChangeListeners.
     *
     * @return the Iterator over the currently subscribed PropertyChangeListeners
     */
    public abstract Iterator<PropertyChangeListener> propertyChangeListenersIterator();

    /**
     * Obtain the same MeshObject as ExternalizedMeshObject so it can be easily serialized.
     * 
     * @return this MeshObject as ExternalizedMeshObject
     */
    public abstract ExternalizedMeshObject asExternalized();

    /**
     * Obtain a String that renders this instance suitable for showing to a user.
     * Specify the prioritized sequence of EntityTypes that have a chance of defining
     * that user-visible String. For example, if a MeshObject is blessed with EntityTypes
     * A, B and D, of which B and D define a user-visible String other than null,
     * and if this method is invoked with EntityTypes A, C, D, B, this method will
     * return the result of D's get_UserVisibleString() method.
     * For programming simplicity, this method may be invoked with EntityTypes that the
     * current MeshObject is not currently blessed with. If no user-visible String can be
     * determined from the specified EntityTypes (or if none are specified), this method
     * will return a generic default, such as the String form of the MeshObjectIdentifier.
     *
     * @param types the EntityTypes to be consulted, in sequence, until a non-null result is found
     * @return the user-visible String representing this instance
     */
    public String getUserVisibleString(
            EntityType [] types );

    /**
     * Obtain a String that renders this instance suitable for showing to a user.
     *
     * @return the user-visible String representing this instance
     */
    public String getUserVisibleString();

    /**
     * The name of a pseudo-property that indicates the state of the object, such as
     * "alive" or "dead". This pseudo-property changes when the MeshObject dies.
     */
    public static final String _MESH_OBJECT_STATE_PROPERTY = "_MeshObjectState";

    /**
     * The name of a pseudo-property that indicates the MeshTypes of this MeshObject.
     * This pseudo-property changes when the MeshObject is blessed or unblessed.
     */
    public static final String _MESH_OBJECT_TYPES_PROPERTY = "_MeshObjectTypes";
    
    /**
     * The name of a pseudo-property that indicates the RoleTypes in which this MeshObject
     * participates. This pseudo-property changes when the MeshObject's roles in
     * a relationship change.
     */
    public static final String _MESH_OBJECT_ROLES_PROPERTY = "_MeshObjectRoles";
    
    /**
     * The name of a pseudo-property that indicates that current set of neighbor MeshTypes.
     */
    public static final String _MESH_OBJECT_NEIGHBOR_PROPERTY = "_MeshObjectNeighbors";
    
    /**
     * The name of a pseudo-property that indicates the current set of equivalents.
     */
    public static final String _MESH_OBJECT_EQUIVALENTS_PROPERTY = "_MeshObjectEquivalents";
}
