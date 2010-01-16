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

package org.infogrid.modelbase;

import java.util.Iterator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyTypeOrGroup;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.SubjectArea;

/**
  * <p>This interface is implemented by classes that act as a repository of model-level concepts.
  * The concepts held in a ModelBase include EntityTypes, RelationshipTypes, PropertyTypes,
  * SubjectAreas and the like. A ModelBase is comparable to a database's data dictionary,
  * except that the ModelBase holds conceptual-level concepts, not data-base level ones.
  * For example, it holds EntityTypes and their inheritance relationships, rather than tables.</p>
  *
  * <p>ModelBases may contain their entire model from the time they are created or restored.
  * Others may demand-load models or model fragments as needed. This interface does not distinguish
  * implementation differences such as these ones. However, it does provide a method that
  * allows subclasses to distinguish those implementations.</p>
  */
public interface ModelBase
{
    /**
     * Obtain a MeshTypeIdentifierFactory appropriate for this ModelBase.
     *
     * @return the MeshTypeIdentifierFactory
     */
    public abstract MeshTypeIdentifierFactory getMeshTypeIdentifierFactory();

    /**
      * Find a SubjectArea by name and version number.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @return the found SubjectArea
      * @throws SubjectAreaNotFoundException thrown if the SubjectArea cannot be found
      */
    public abstract SubjectArea findSubjectArea(
            String subjectAreaName,
            String subjectAreaVersionNumber )
        throws
            SubjectAreaNotFoundException;

    /**
      * Find a EntityType by name and its containing SubjectArea.
      *
      * @param theSubjectArea the SubjectArea in which we are looking for the EntityType
      * @param entityTypeName the name of the EntityType
      * @return the found EntityType
      * @throws EntityTypeNotFoundException thrown if the EntityType cannot be found
      */
    public abstract EntityType findEntityType(
            SubjectArea theSubjectArea,
            String      entityTypeName )
        throws
            EntityTypeNotFoundException;

    /**
      * Shortcut for finding a EntityType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param entityTypeName the name of the EntityType
      * @return the found EntityType
      * @throws MeshTypeNotFoundException thrown if the SubjectArea or the EntityType cannot be found
      */
    public abstract EntityType findEntityType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String entityTypeName )
        throws
            MeshTypeNotFoundException;

    /**
      * Find a RelationshipType by name and its containing SubjectArea.
      *
      * @param theSubjectArea the SubjectArea in which we are looking for the RelationshipType
      * @param relationshipTypeName the name of the RelationshipType
      * @return the found RelationshipType
      * @throws RelationshipTypeNotFoundException thrown if the RelationshipType cannot be found
      */
    public abstract RelationshipType findRelationshipType(
            SubjectArea theSubjectArea,
            String      relationshipTypeName )
        throws
            RelationshipTypeNotFoundException;

    /**
      * Shortcut for finding a RelationshipType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param relationshipTypeName the name of the RelationshipType
      * @return the found RelationshipType
      * @throws MeshTypeNotFoundException thrown if the SubjecArea or the RelationshipType cannot be found
      */
    public abstract RelationshipType findRelationshipType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String relationshipTypeName )
        throws
            MeshTypeNotFoundException;

    /**
      * Find a PropertyType by name and its AttributableMeshType (EntityType or RelationshipType).
      *
      * @param theAttributableMeshType the AttributableMeshType within which we are looking for the PropertyType
      * @param thePropertyTypeName the name of the PropertyType
      * @return the found PropertyType
      * @throws PropertyTypeNotFoundException thrown if the PropertyType cannot be found
      */
    public abstract PropertyType findPropertyType(
            AttributableMeshType theAttributableMeshType,
            String               thePropertyTypeName )
        throws
            PropertyTypeNotFoundException;

    /**
      * Shortcut for finding a PropertyType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param theAttributableMeshOTypeName the name of the owning EntityType or RelationshipType
      * @param thePropertyTypeName the name of the PropertyType
      * @return the found PropertyType
      * @throws MeshTypeNotFoundException thrown if the SubjectArea, AttributableMeshObject or PropertyType cannot be found
      */
    public abstract PropertyType findPropertyType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String theAttributableMeshOTypeName,
            String thePropertyTypeName )
        throws
            MeshTypeNotFoundException;

    /**
     * Find a MeshType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the MeshType could not be found
     */
    public abstract MeshType findMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a MeshType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a MeshType with this Identifier cannot be found
     */
    public abstract MeshType findLoadedMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a SubjectArea by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found SubjectArea
     * @return the found SubjectArea, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a SubjectArea with this Identifier cannot be found
     */
    public abstract SubjectArea findSubjectAreaByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a CollectableMeshType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found CollectableMeshType
     * @return the found CollectableMeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a CollectableMeshType with this Identifier cannot be found
     */
    public abstract CollectableMeshType findCollectableMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find an AttributableMeshType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found AttributableMeshType
     * @return the found AttributableMeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if an AttributableMeshType with this Identifier cannot be found
     */
    public abstract AttributableMeshType findAttributableMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find an EntityType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found EntityType
     * @return the found EntityType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if an EntityType with this Identifier cannot be found
     */
    public abstract EntityType findEntityTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a RelationshipType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found RelationshipType
     * @return the found RelationshipType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a RelationshipType with this Identifier cannot be found
     */
    public abstract RelationshipType findRelationshipTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a PropertyTypeOrGroup by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found PropertyTypeOrGroup
     * @return the found PropertyTypeOrGroup, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a PropertyTypeOrGroup with this Identifier cannot be found
     */
    public abstract PropertyTypeOrGroup findPropertyTypeOrGroupByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a PropertyType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found PropertyType
     * @return the found PropertyType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a PropertyType with this Identifier cannot be found
     */
    public abstract PropertyType findPropertyTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a ProjectedPropertyType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found ProjectedPropertyType
     * @return the found ProjectedPropertyType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a ProjectedPropertyType with this Identifier cannot be found
     */
    public abstract ProjectedPropertyType findProjectedPropertyTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a PropertyTypeGroup by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found PropertyTypeGroup
     * @return the found PropertyTypeGroup, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a PropertyTypeGroup with this Identifier cannot be found
     */
    public abstract PropertyTypeGroup findPropertyTypeGroupByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
     * Find a RoleType by its unique identifier.
     * 
     * 
     * @param identifier Identifier of the to-be-found RoleType
     * @return the found RoleType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException thrown if a RoleType with this Identifier cannot be found
     */
    public abstract RoleType findRoleTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException;

    /**
      * Obtain the manager for MeshType lifecycles. This manager cannot be
      * re-assigned as it is specific to the implementation of the ModelBase.
      *
      * @return the MeshTypeLifecycleManager for this ModelBase
      */
    public abstract MeshTypeLifecycleManager getMeshTypeLifecycleManager();

    /**
      * Obtain an Iterator to iterate over all MeshTypes contained in this ModelBase.
      *
      * @return the Iterator
      */
    public Iterator<MeshType> iterator();

    /**
      * Obtain an Iterator to iterate over all SubjectAreas.
      *
      * @return the Iterator
      */
    public Iterator<SubjectArea> subjectAreaIterator();

    /**
     * Check the correctness and integrity of this EntityType. There is usually no need
     * to call this for the application developer.
     *
     * @param candidate the EntityType that shall be checked
     * @throws IllegalArgumentException thrown if the check fails
     */
    public void checkEntityType(
            EntityType candidate )
        throws
            IllegalArgumentException;

    /**
     * Check the correctness and integrity of this RelationshipType. There is usually no need
     * to call this for the application developer.
     *
     * @param candidate the RelationshipType that shall be checked
     * @throws IllegalArgumentException thrown if the check fails
     */
    public void checkRelationshipType(
            RelationshipType candidate )
        throws
            IllegalArgumentException;

    /**
     * Check the correctness and integrity of this PropertyType. There is usually no need
     * to call this for the application developer.
     *
     * @param candidate the PropertyType that shall be checked
     * @throws IllegalArgumentException thrown if the check fails
     */
    public void checkPropertyType(
            PropertyType candidate )
        throws
            IllegalArgumentException;

    /**
     * Check the correctness and integrity of this PropertyTypeGroup. There is usually no need
     * to call this for the application developer.
     *
     * @param candidate the PropertyTypeGroup that shall be checked
     * @throws IllegalArgumentException thrown if the check fails
     */
    public void checkPropertyTypeGroup(
            PropertyTypeGroup candidate )
        throws
            IllegalArgumentException;

    /**
     * Check the correctness and integrity of this SubjectArea. There is usually no need
     * to call this for the application developer.
     *
     * @param candidate the SubjectArea that shall be checked
     * @throws IllegalArgumentException thrown if the check fails
     */
    public void checkSubjectArea(
            SubjectArea candidate )
        throws
            IllegalArgumentException;

    /**
      * Subscribe to events indicating the addition/removal/etc
      * of MeshTypes in this ModelBase.
      *
      * @param newListener the listener to be added
      * @see #removeMeshTypeLifecycleEventListener
      */
    public void addMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener newListener );

    /**
      * Unsubscribe from events indicating the addition/removal/etc
      * of MeshTypes in this ModelBase.
      *
      * @param oldListener the listener to be removed
      * @see #addMeshTypeLifecycleEventListener
      */
    public void removeMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener oldListener );

    /**
     * Obtain a MeshTypeSynonymDictionary.
     *
     * @return the MeshTypeSynonyDirectory.
     */
    public MeshTypeSynonymDictionary getSynonymDictionary();

    /**
      * We are told we are not needed any more. Clean up and release all resources.
      */
    public void die();
}
