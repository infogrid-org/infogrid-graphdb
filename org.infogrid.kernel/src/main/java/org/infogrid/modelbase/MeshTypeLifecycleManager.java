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

package org.infogrid.modelbase;

import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.L10PropertyValueMap;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.TraversalToPropertySpecification;

/**
  * <p>A life cycle manager for MeshTypes created and stored in a ModelBase.</p>
  * <p>At this point, we do not support the obsoletion of MeshTypes. Likely, we never will.</p>
  */
public interface MeshTypeLifecycleManager
{
    /**
     * Create an EntityType.
     * 
     * @param identifier globally unique identifier of this EntityType
     * @param theName programmatic name of this EntityType
     * @param theUserNames internalionalized names of this EntityType
     * @param theUserDescriptions internationalized descriptions of this EntityType
     * @param theIcon an icon that can be shown to the user that represents this EntityType
     * @param theSubjectArea the SubjectArea in which this EntityType is defined
     * @param supertypes the zero or more or AttributableMeshTypes that are the direct supertypes of this EntityType
     * @param synonyms the alternate MeshTypeIdentifiers identifying this EntityType
     * @param inheritingOverrideCode code, if any, that shall be in-lined into implementation code of this EntityType and all of its subtypes
     * @param localEntityTypeGuardClassNames the class names of the set of EntityTypeGuards locally defined on this EntityType
     * @param declaredMethods the methods declared on this EntityType
     * @param implementedMethods the methods implemented by this EntityType
     * @param additionalInterfaces additional interfaces declared by the generated code
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
     * @param mayBeUsedAsForwardReference if BooleanValue.TRUE, this EntityType may be used as a ForwardReference
     * @param isSignificant if BooleanValue.TRUE, this EntityType is significant and not just mechanistic
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created EntityType
     */
    public EntityType createEntityType(
            MeshTypeIdentifier      identifier,
            StringValue             theName,
            L10PropertyValueMap     theUserNames,
            L10PropertyValueMap     theUserDescriptions,
            BlobValue               theIcon,
            SubjectArea             theSubjectArea,
            AttributableMeshType [] supertypes,
            MeshTypeIdentifier []   synonyms,
            StringValue             inheritingOverrideCode,
            String []               localEntityTypeGuardClassNames,
            StringValue []          declaredMethods,
            StringValue []          implementedMethods,
            String []               additionalInterfaces,
            BooleanValue            isAbstract,
            BooleanValue            mayBeUsedAsForwardReference,
            BooleanValue            isSignificant,
            BooleanValue            doGenerateInterfaceCode,
            BooleanValue            doGenerateImplementationCode );

    /**
     * Create a binary RelationshipType.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceMultiplicity the maximum number of Entities that can participate as the destination
     *        of an instance of this RelationshipType for a given source Entity
     * @param destinationMultiplicity the maximum number of Entities that can particpate as the source
     *        of an instance of this RelationshipType for a given destination Entity
     * @param source the source EntityType of this RelationshipType
     * @param destination the destination EntityType of this RelationshipType
     * @param sourceSuperRoleTypes the RoleTypes that are refined by the source RoleTypes of this RelationshipType
     * @param destinationSuperRoleTypes the RoleTypes that are refined by the destination RoleTypes of this RelationshipType
     * @param sourceRoleConstraintClasses class names of the set of RoleConstraints to enforce at the source end
     *        of all instances of this RelationshipType
     * @param destinationRoleConstraintClasses class names of the set of RoleConstraints to enforce at the destination end
     *        of all instances of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this RelationshipType cannot be instantiated and a non-abstract subtype must be instantiated instead
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created RelationshipType
     */
    public RelationshipType createRelationshipType(
            MeshTypeIdentifier  identifier,
            StringValue         theName, // full name
            L10PropertyValueMap theUserNames,
            L10PropertyValueMap theUserDescriptions,
            SubjectArea         theSubjectArea,
            MultiplicityValue   sourceMultiplicity,
            MultiplicityValue   destinationMultiplicity,
            EntityType          source,
            EntityType          destination,
            RoleType []         sourceSuperRoleTypes,
            RoleType []         destinationSuperRoleTypes,
            String []           sourceRoleConstraintClasses,
            String []           destinationRoleConstraintClasses,
            BooleanValue        isAbstract,
            BooleanValue        doGenerateInterfaceCode,
            BooleanValue        doGenerateImplementationCode );

    /**
     * Create a binary RelationshipType. This is a convenience method for those RelationshipTypes that
     * refine exactly one RoleType as source and one RoleType as destination, i.e. those RelationshipType
     * that use single (relationship) inheritance.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceMultiplicity the maximum number of Entities that can participate as the destination
     *        of an instance of this RelationshipType for a given source Entity
     * @param destinationMultiplicity the maximum number of Entities that can particpate as the source
     *        of an instance of this RelationshipType for a given destination Entity
     * @param source the source EntityType of this RelationshipType
     * @param destination the destination EntityType of this RelationshipType
     * @param sourceSuperRoleType the RoleType that is refined by the source RoleType of this RelationshipType
     * @param destinationSuperRoleType the RoleType that is refined by the destination RoleType of this RelationshipType
     * @param sourceRoleConstraintClasses class names of the set of RoleConstraints to enforce at the source end
     *        of all instances of this RelationshipType
     * @param destinationRoleConstraintClasses class names of the set of RoleConstraints to enforce at the destination end
     *        of all instances of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this RelationshipType cannot be instantiated and a non-abstract subtype must be instantiated instead
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created RelationshipType
     */
    public RelationshipType createRelationshipType(
            MeshTypeIdentifier        identifier,
            StringValue               theName, // full name
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            SubjectArea               theSubjectArea,
            MultiplicityValue         sourceMultiplicity,
            MultiplicityValue         destinationMultiplicity,
            EntityType                source,
            EntityType                destination,
            RoleType                  sourceSuperRoleType,
            RoleType                  destinationSuperRoleType,
            String []                 sourceRoleConstraintClasses,
            String []                 destinationRoleConstraintClasses,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode );

    /**
     * Create a unary RelationshipType.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceDestMultiplicity the maximum number of Entities that can participate in one
     *        instance of this RelationshipType
     * @param sourceDest the EntityType attached to this RelationshipType
     * @param sourceDestSuperRoleTypes the RoleTypes that are refined by the RoleTypes of this RelationshipType
     * @param sourceDestRoleConstraintClasses class names of the set of RoleConstraints to be enforced at each end
     *        of each instance of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this RelationshipType cannot be instantiated and a non-abstract subtype must be instantiated instead
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created RelationshipType
     */
    public RelationshipType createRelationshipType(
            MeshTypeIdentifier        identifier,
            StringValue               theName,
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            SubjectArea               theSubjectArea,
            MultiplicityValue         sourceDestMultiplicity,
            EntityType                sourceDest,
            RoleType []               sourceDestSuperRoleTypes,
            String []                 sourceDestRoleConstraintClasses,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode );

    /**
     * Create a unary RelationshipType. This is a convenience method for those unary RelationshipType that
     * refine exactly one RoleType, i.e. those unary RelationshipTypes that use single (relationship) inheritance.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceDestMultiplicity the maximum number of Entities that can participate in one
     *        instance of this RelationshipType
     * @param sourceDest the EntityType attached to this RelationshipType
     * @param sourceDestSuperRoleType the RoleType that is refined by the RoleType of this RelationshipType
     * @param sourceDestRoleConstraintClasses class names of the set of RoleConstraints to be enforced at each end
     *        of each instance of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this RelationshipType cannot be instantiated and a non-abstract subtype must be instantiated instead
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created RelationshipType
     */
    public RelationshipType createRelationshipType(
            MeshTypeIdentifier        identifier,
            StringValue               theName,
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            SubjectArea               theSubjectArea,
            MultiplicityValue         sourceDestMultiplicity,
            EntityType                sourceDest,
            RoleType                  sourceDestSuperRoleType,
            String []                 sourceDestRoleConstraintClasses,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode );

    /**
     * Create a PropertyType.
     * 
     * @param identifier globally unique identifier of this PropertyType
     * @param theName programmatic name of this PropertyType
     * @param theUserNames internalionalized names of this PropertyType
     * @param theUserDescriptions internationalized descriptions of this PropertyType
     * @param theParent the parent AttributableMeshType in which this PropertyType is defined
     * @param theSubjectArea the SubjectArea in which this PropertyType is defined
     * @param theDataType the data type for this PropertyType
     * @param theDefaultValue the value with which instances of this PropertyType are initialized. This must
     *                        be non-null for non-optional PropertyType
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this PropertyType
     * @param theLocalPropertyTypeGuardClassNames the class names of the set of PropertyTypeGuards locally defined on this PropertyType
     * @param isOptional if true, this PropertyType can have value null; if false, this PropertyType cannot have the null value
     * @param isReadOnly if true, this PropertyType can only be read, not written
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @param theSequenceNumber determines the sequence of display in things like property sheets etc.
     * @return the newly created PropertyType
     */
    public PropertyType createPropertyType(
            MeshTypeIdentifier        identifier,
            StringValue               theName,
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            AttributableMeshType      theParent,
            SubjectArea               theSubjectArea,
            DataType                  theDataType,
            PropertyValue             theDefaultValue,
            StringValue               theDefaultValueCode,
            String []                 theLocalPropertyTypeGuardClassNames,
            BooleanValue              isOptional,
            BooleanValue              isReadOnly,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode,
            FloatValue                theSequenceNumber );

    /**
     * Create a PropertyType that overrides another PropertyType or set of PropertyTypes.
     * 
     * @param toOverride set of PropertyTypes to override. All PropertyTypes given must have the same ancestor PropertyType,
     *        and each PropertyType must be declared in a different supertype of our parent AttributableMeshType that uses
     *        multiple inheritance. For single inheritance, the length of this array must be 1.
     * @param overriddenIdentifier the unique identifier of this overridden PropertyType
     * @param theUserDescriptions internationalized descriptions of this PropertyType
     * @param theParent the parent AttributableMeshType in which this PropertyType is defined
     * @param theSubjectArea the SubjectArea in which this PropertyType is defined
     * @param theDataType the data type for this PropertyType
     * @param theDefaultValue the value with which instances of this PropertyType are initialized. This must
     *                        be non-null for non-optional PropertyType
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this PropertyType
     * @param theLocalPropertyTypeGuardClassNames the class names of the set of PropertyTypeGuards locally defined on this PropertyType
     * @param isOptional if true, this PropertyType can have value null; if false, this PropertyType cannot have the null value
     * @param isReadOnly if true, this PropertyType can only be read, not written
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created PropertyType
     * @throws InheritanceConflictException thrown if the overridden PropertyType are wrong
     */
    public PropertyType createOverridingPropertyType(
            PropertyType []           toOverride,
            MeshTypeIdentifier        overriddenIdentifier,
            L10PropertyValueMap       theUserDescriptions,
            AttributableMeshType      theParent,
            SubjectArea               theSubjectArea,
            DataType                  theDataType,
            PropertyValue             theDefaultValue,
            StringValue               theDefaultValueCode,
            String []                 theLocalPropertyTypeGuardClassNames,
            BooleanValue              isOptional,
            BooleanValue              isReadOnly,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
        throws
            InheritanceConflictException;

    /**
     * Create a ProjectedPropertyType.
     * 
     * @param identifier globally unique identifier of this ProjectedPropertyType
     * @param theName programmatic name of this ProjectedPropertyType
     * @param theUserNames internalionalized names of this ProjectedPropertyType
     * @param theUserDescriptions internationalized descriptions of this ProjectedPropertyType
     * @param theParent the parent AttributableMeshType in which this ProjectedPropertyType is defined
     * @param theSubjectArea the SubjectArea in which this ProjectedPropertyType is defined
     * @param theDataType the data type for this ProjectedPropertyType
     * @param theDefaultValue the value with which instances of this ProjectedPropertyType are initialized.
     *        This must be non-null for non-optional ProjectedPropertyType. Given that this is a ProjectedPropertyType,
     *        this value is only used until the projection code is run for the first time.
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this ProjectedPropertyType.
     *        Given that this is a ProjectedPropertyType, this value is only used until the projection code is run
     *        for the first time.
     * @param theInputProperties the path to the set of properties in particular Entities that needs to
     *        be monitored in order to determine when to re-run the projection code in instances. The Entities are
     *        determined through the TraversalSpecifications referenced by the theInputProperties.
     * @param theProjectionCode a StringValue with a text mime type that contains the projection code for determining
     *        the value of this ProjectedPropertyType in instances
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @param theSequenceNumber determines the sequence of display in things like property sheets etc.
     * @return the newly created ProjectedPropertyType
     */
    public ProjectedPropertyTypePatcher createProjectedPropertyType(
            MeshTypeIdentifier                  identifier,
            StringValue                         theName,
            L10PropertyValueMap                 theUserNames,
            L10PropertyValueMap                 theUserDescriptions,
            AttributableMeshType                theParent,
            SubjectArea                         theSubjectArea,
            DataType                            theDataType,
            PropertyValue                       theDefaultValue,     // used only to initialize with something meaningful
            StringValue                         theDefaultValueCode, // used only to initialize with something meaningful
            TraversalToPropertySpecification [] theInputProperties,
            StringValue                         theProjectionCode,
            BooleanValue                        doGenerateInterfaceCode,
            BooleanValue                        doGenerateImplementationCode,
            FloatValue                          theSequenceNumber );

    /**
     * Create a ProjectedPropertyType that overrides another PropertyType or set of PropertyTypes.
     * 
     * @param toOverride set of PropertyTypes to override. All PropertyTypes given must have the same ancestor PropertyType,
     *        and each PropertyType must be declared in a different supertype of our parent AttributableMeshType that uses
     *        multiple inheritance. For single inheritance, the length of this array must be 1.
     * @param overriddenIdentifier the unique identifier of this overridden PropertyType
     * @param theUserDescriptions internationalized descriptions of this ProjectedPropertyType
     * @param theParent the parent AttributableMeshType in which this ProjectedPropertyType is defined
     * @param theSubjectArea the SubjectArea in which this AttributableMeshType is defined
     * @param theDataType the data type for this ProjectedPropertyType
     * @param theDefaultValue the value with which instances of this ProjectedPropertyType are initialized.
     *        This must be non-null for non-optional ProjectedPropertyTypes. Given that this is a ProjectedPropertyType,
     *        this value is only used until the projection code is run for the first time.
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this ProjectedPropertyType.
     *        Given that this is a ProjectedPropertyType, this value is only used until the projection code is run
     *        for the first time.
     * @param theInputProperties the path to the set of properties in particular Entities that needs to
     *        be monitored in order to determine when to re-run the projection code in instances. The Entities are
     *        determined through the TraversalSpecifications referenced by the theInputProperties.
     * @param theProjectionCode a StringValue with a text mime type that contains the projection code for determining
     *        the value of this ProjectedPropertyType in instances
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created ProjectedPropertyType
     * @throws InheritanceConflictException thrown if the overriding ProjectedPropertyType cannot be created because the
     *        consistency constraints in the model were not met
     */
    public ProjectedPropertyTypePatcher createOverridingProjectedPropertyType(
            PropertyType []                     toOverride,
            MeshTypeIdentifier                  overriddenIdentifier,
            L10PropertyValueMap                 theUserDescriptions,
            AttributableMeshType                theParent,
            SubjectArea                         theSubjectArea,
            DataType                            theDataType,
            PropertyValue                       theDefaultValue,     // used only to initialize with something meaningful
            StringValue                         theDefaultValueCode, // used only to instantiate with something meaningful
            TraversalToPropertySpecification [] theInputProperties,
            StringValue                         theProjectionCode,
            BooleanValue                        doGenerateInterfaceCode,
            BooleanValue                        doGenerateImplementationCode )
        throws
            InheritanceConflictException;

    /**
     * Create a PropertyTypeGroup.
     * 
     * @param identifier globally unique identifier of this PropertyTypeGroup
     * @param theName programmatic name of this PropertyTypeGroup
     * @param theUserNames internalionalized names of this PropertyTypeGroup
     * @param theUserDescriptions internationalized descriptions of this PropertyTypeGroup
     * @param theParent the parent AttributableMeshType in which this PropertyTypeGroup is defined
     * @param theSubjectArea the SubjectArea in which this PropertyTypeGroup is defined
     * @param theMembers the member PropertyTypes of this PropertyTypeGroup. All PropertyTypes must be defined in the parent
     *        AttributableMeshType, or any of its supertypes.
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @param theSequenceNumber determines the sequence of display in things like property sheets etc.
     * @return the newly created PropertyTypeGroup
     */
    public PropertyTypeGroup createPropertyTypeGroup(
            MeshTypeIdentifier   identifier,
            StringValue          theName,
            L10PropertyValueMap  theUserNames,
            L10PropertyValueMap  theUserDescriptions,
            AttributableMeshType theParent,
            SubjectArea          theSubjectArea,
            PropertyType []      theMembers,
            BooleanValue         doGenerateInterfaceCode,
            BooleanValue         doGenerateImplementationCode,
            FloatValue           theSequenceNumber );

    /**
     * Create a SubjectArea.
     * 
     * @param identifier globally unique identifier of this SubjectArea
     * @param theName programmatic name of this SubjectArea
     * @param theVersion the version of this SubjectArea
     * @param theUserNames internalionalized names of this SubjectArea
     * @param theUserDescriptions internationalized descriptions of this SubjectArea
     * @param theSubjectAreaDependencies the set of SubjectAreas on whose content members of this SubjectArea depend
     * @param theClassLoader the ClassLoader to use to load resources needed in this SubjectArea
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created SubjectArea
     */
    public SubjectArea createSubjectArea(
            MeshTypeIdentifier        identifier,
            StringValue               theName,
            StringValue               theVersion,
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            SubjectArea []            theSubjectAreaDependencies,
            ClassLoader               theClassLoader,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode );
}
