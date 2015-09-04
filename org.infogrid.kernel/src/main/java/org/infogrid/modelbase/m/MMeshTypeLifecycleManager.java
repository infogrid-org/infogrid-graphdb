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

package org.infogrid.modelbase.m;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.L10PropertyValueMap;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.m.MAttributableMeshType;
import org.infogrid.model.primitives.m.MEntityType;
import org.infogrid.model.primitives.m.MProjectedPropertyType;
import org.infogrid.model.primitives.m.MPropertyType;
import org.infogrid.model.primitives.m.MPropertyTypeGroup;
import org.infogrid.model.primitives.m.MRelationshipType;
import org.infogrid.model.primitives.m.MRoleType;
import org.infogrid.model.primitives.m.MSubjectArea;
import org.infogrid.model.traversal.TraversalToPropertySpecification;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.modelbase.MeshTypeLifecycleManager;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ProjectedPropertyTypePatcher;
import org.infogrid.util.ArrayHelper;

/**
 * The MeshTypeLifecycleManager implementation for the in-memory implementation
 * of ModelBase.
 */
public class MMeshTypeLifecycleManager
        implements
            MeshTypeLifecycleManager
{
    /**
     * Factory method.
     *
     * @return the created MMeshTypeLifecycleManager
     */
    public static MMeshTypeLifecycleManager create()
    {
        return new MMeshTypeLifecycleManager();
    }

    /**
      * Constructor.
      */
    protected MMeshTypeLifecycleManager()
    {
        // nothing
    }

    /**
      * Set the ModelBase that this MeshTypeLifecycleManager belongs to.
      *
      * @param mb the ModelBase that this MeshTypeLifecycleManager belongs to
      */
    public void setModelBase(
            MModelBase mb )
    {
        this.theModelBase = mb;
    }

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
     * @param localEntityTypeGuardClassNames the class names of the set of EntityTypeGuards locally defined on this EntityType.
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
            BooleanValue            doGenerateImplementationCode )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( supertypes == null ) {
            supertypes = new EntityType[0];
        }
        if( synonyms == null ) {
            synonyms = new MeshTypeIdentifier[0];
        }
        if( isAbstract == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have null IsAbstract" );
        }
        if( isSignificant == null ) {
            throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have null IsSignificant" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }
        MSubjectArea             realSubjectArea = (MSubjectArea) theSubjectArea;
        MAttributableMeshType [] realSupertypes  = copyIntoNewArray( supertypes, MAttributableMeshType.class );

        MEntityType ret = new MEntityType( identifier );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        if( theIcon != null ) {
            ret.setIcon( theIcon );
        }
        realSubjectArea.addCollectableMeshType( ret );
        ret.setSubjectArea( realSubjectArea );

        ret.setDirectSupertypes( realSupertypes );

        if( supertypes != null ) {
            for( int i=0 ; i<supertypes.length ; ++i ) {
                if( supertypes[i] == null ) {
                    throw new IllegalArgumentException( "To-be-created EntityType with Identifier " + identifier + " cannot have null supertype" );
                }
                realSupertypes[i].addDirectSubtype( ret );
            }
        }
        ret.setSynonyms( synonyms );

        ret.setInheritingOverrideCode( inheritingOverrideCode );
        ret.setLocalEntityTypeGuardClassNames( localEntityTypeGuardClassNames );
        ret.setIsAbstract( isAbstract );
        ret.setMayBeUsedAsForwardReference( mayBeUsedAsForwardReference );
        ret.setIsSignificant( isSignificant );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );

        ret.setDeclaredMethods(      declaredMethods );
        ret.setImplementedMethods(   implementedMethods );
        ret.setAdditionalInterfaces( additionalInterfaces );
        
        theModelBase.theCluster.addObject( ret );

        for( MeshTypeIdentifier current : synonyms ) {
            theModelBase.theSynonymDictionary.addToTable( current, identifier );
        }

        return ret;
    }

    /**
     * Create a binary RelationshipType.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceMultiplicity the maximum number of RootEntities that can participate as the destination of this RelationshipType for a given source RootEntity
     * @param destinationMultiplicity the maximum number of RootEntities that can particpate as the source of this RelationshipType for a given destination RootEntity
     * @param source the source EntityType of this RelationshipType
     * @param destination the destination EntityType of this RelationshipType
     * @param sourceSuperRoleTypes the RoleTypes that are refined by the source RoleType of this RelationshipType
     * @param destinationSuperRoleTypes the RoleTypes that are refined by the destination RoleType of this RelationshipType
     * @param sourceRoleConstraintClassNames the set of RoleConstraint class names to enforce at the source end of all instances of this RelationshipType
     * @param destinationRoleConstraintClassNames the set of RoleConstraints class names to enforce at the destination end of all instances of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
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
            RoleType []               sourceSuperRoleTypes,
            RoleType []               destinationSuperRoleTypes,
            String []                 sourceRoleConstraintClassNames,
            String []                 destinationRoleConstraintClassNames,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( sourceMultiplicity == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SourceMultiplicity" );
        }
        if( destinationMultiplicity == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null DestinationMultiplicity" );
        }
//        if( source == null ) {
//            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null Source" );
//        }
//        if( destination == null ) {
//            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null Destination" );
//        }
        if( sourceSuperRoleTypes == null ) {
            sourceSuperRoleTypes = new RoleType[0];
        }
        if( destinationSuperRoleTypes == null ) {
            destinationSuperRoleTypes = new RoleType[0];
        }
        if( sourceSuperRoleTypes.length != destinationSuperRoleTypes.length ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have source super-RoleTypes and destination super-RoleTypes of different lengths" );
        }
// FIXME?
        // they need to override the same relationship types
        for( int i=0 ; i<sourceSuperRoleTypes.length ; ++i ) {
            if( sourceSuperRoleTypes[i].getRelationshipType() != destinationSuperRoleTypes[i].getRelationshipType() ) {
                throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot override RoleTypes from different RelationshipTypes simultaneously" );
            }
        }
        if( isAbstract == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null IsAbstract" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }
        MEntityType  realSource                    = (MEntityType) source;
        MEntityType  realDestination               = (MEntityType) destination;
        MSubjectArea realSubjectArea               = (MSubjectArea) theSubjectArea;
        MRoleType [] realSourceSuperRoleTypes      = copyIntoNewArray( sourceSuperRoleTypes,      MRoleType.class );
        MRoleType [] realDestinationSuperRoleTypes = copyIntoNewArray( destinationSuperRoleTypes, MRoleType.class );

        MRelationshipType ret = new MRelationshipType(
                identifier,
                theName,
                realSource,
                realDestination,
                sourceMultiplicity,
                destinationMultiplicity,
                sourceRoleConstraintClassNames,
                destinationRoleConstraintClassNames );

        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        realSubjectArea.addCollectableMeshType( ret );
        ret.setSubjectArea( realSubjectArea );

        MRoleType sourceRole      = (MRoleType) ret.getSource();
        MRoleType destinationRole = (MRoleType) ret.getDestination();

        realSubjectArea.addCollectableMeshType( sourceRole );
        realSubjectArea.addCollectableMeshType( destinationRole );

        // we have to add it both in the roles and in the relationship
        sourceRole.setDirectSuperRoleTypes( realSourceSuperRoleTypes );
        destinationRole.setDirectSuperRoleTypes( realDestinationSuperRoleTypes );

        ArrayList<MRelationshipType> supertypes = new ArrayList<MRelationshipType>(
                sourceSuperRoleTypes.length + destinationSuperRoleTypes.length );

        if( sourceSuperRoleTypes != null ) {
            for( int i=0 ; i<sourceSuperRoleTypes.length ; ++i ) {
                if( sourceSuperRoleTypes[i] == null ) {
                    throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null source super-RoleType" );
                }
                MRelationshipType superRelationshipType = (MRelationshipType) sourceSuperRoleTypes[i].getRelationshipType();

                // FIXME? we dont do this right now
                // superRelationshipType.addDirectSubRoleType( sourceRole );
                superRelationshipType.addDirectSubtype( ret );

                if( !supertypes.contains( superRelationshipType )) {
                    supertypes.add( superRelationshipType );
                }
            }
        }

        if( destinationSuperRoleTypes != null ) {
            for( int i=0 ; i<destinationSuperRoleTypes.length ; ++i ) {
                if( destinationSuperRoleTypes[i] == null ) {
                    throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null destination super-RoleType" );
                }
                MRelationshipType superRelationshipType = (MRelationshipType) destinationSuperRoleTypes[i].getRelationshipType();

                // FIXME? we dont do this right now
                // superRelationshipType.addDirectSubRoleType( sourceRole );
                superRelationshipType.addDirectSubtype( ret );

                if( ! supertypes.contains( superRelationshipType )) {
                    supertypes.add( superRelationshipType );
                }
            }
        }
        ret.setDirectSupertypes( copyIntoNewArray( supertypes, MRelationshipType.class ) );

        ret.setIsAbstract( isAbstract );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

    /**
     * Create a binary RelationshipType. This is a convenience method for those RelationshipTypes that
     * refine exactly one RoleType as source and one RoleType as destination, i.e. those RelationshipTypes
     * that use single inheritance.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceMultiplicity the maximum number of RootEntities that can participate as the destination of this RelationshipType for a given source RootEntity
     * @param destinationMultiplicity the maximum number of RootEntities that can particpate as the source of this RelationshipType for a given destination RootEntity
     * @param source the source EntityType of this RelationshipType
     * @param destination the destination EntityType of this RelationshipType
     * @param sourceSuperRoleType the RoleType that is refined by the source RoleType of this RelationshipType
     * @param sourceRoleConstraintClassNames the set of RoleConstraint class names to enforce at the source end of all instances of this RelationshipType
     * @param destinationRoleConstraintClassNames the set of RoleConstraints class names to enforce at the destination end of all instances of this RelationshipType
     * @param destinationSuperRoleType the RoleType that is refined by the destination RoleType of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
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
            String []                 sourceRoleConstraintClassNames,
            String []                 destinationRoleConstraintClassNames,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
    {
        return createRelationshipType(
                identifier,
                theName,
                theUserNames,
                theUserDescriptions,
                theSubjectArea,
                sourceMultiplicity,
                destinationMultiplicity,
                source,
                destination,
                sourceSuperRoleType      != null ? new RoleType[] { sourceSuperRoleType      } : new RoleType[0],
                destinationSuperRoleType != null ? new RoleType[] { destinationSuperRoleType } : new RoleType[0],
                sourceRoleConstraintClassNames,
                destinationRoleConstraintClassNames,
                isAbstract,
                doGenerateInterfaceCode,
                doGenerateImplementationCode );
    }

    /**
     * Create a unary RelationshipType.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceDestMultiplicity the maximum number of RootEntities that can participate for this RelationshipType instance
     * @param sourceDest the EntityType attached to this RelationshipType
     * @param sourceDestSuperRoleType the RoleType that is refined by the RoleType of this RelationshipType
     * @param sourceDestRoleConstraintClassNames the set of RoleConstraint class names to be enforced at each end of each instance of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
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
            String []                 sourceDestRoleConstraintClassNames,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
     {
         return createRelationshipType(
                 identifier,
                 theName,
                 theUserNames,
                 theUserDescriptions,
                 theSubjectArea,
                 sourceDestMultiplicity,
                 sourceDest,
                 new RoleType[] { sourceDestSuperRoleType },
                 sourceDestRoleConstraintClassNames,
                 isAbstract,
                 doGenerateInterfaceCode,
                 doGenerateImplementationCode );
     }

     /**
     * Create the top-most RelationshipType in the inheritance hierarchy.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceDestMultiplicity the maximum number of RootEntities that can participate for this RelationshipType instance
     * @param sourceDest the EntityType attached to this RelationshipType
     * @param rootObject the EntityType representing RootObject, the very top of the inheritance hierarchy
     * @param sourceDestRoleConstraintClassNames the RoleConstraints class names to be enforced at each end of each instance of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
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
            MultiplicityValue         sourceDestMultiplicity,
            EntityType                sourceDest,
            EntityType                rootObject,
            String []                 sourceDestRoleConstraintClassNames,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( sourceDestMultiplicity == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SourceAndDestinationMultiplicity" );
        }
        if( sourceDest == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SourceAndDestination" );
        }
        if( rootObject == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null RootObject" );
        }
        if( isAbstract == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null IsAbstract" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }
        
        MEntityType  realSourceAndDestination = (MEntityType) sourceDest;
        MEntityType  realRootObject           = (MEntityType) rootObject;
        MSubjectArea realSubjectArea          = (MSubjectArea) theSubjectArea;

        MRelationshipType ret = new MRelationshipType(
                identifier,
                realSourceAndDestination,
                sourceDestMultiplicity,
                sourceDestRoleConstraintClassNames );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        realSubjectArea.addCollectableMeshType( ret );
        ret.setSubjectArea( realSubjectArea );

        ret.setDirectSupertypes( new MAttributableMeshType[] { realRootObject } );

        ret.setIsAbstract( isAbstract );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

    /**
     * Create a unary RelationshipType. This is a convenience method for those unary RelationshipTypes that
     * refine exactly one RoleType , i.e. those unary RelationshipTypes that use single inheritance.
     * 
     * @param identifier globally unique identifier of this RelationshipType
     * @param theName programmatic name of this RelationshipType
     * @param theUserNames internalionalized names of this RelationshipType
     * @param theUserDescriptions internationalized descriptions of this RelationshipType
     * @param theSubjectArea the SubjectArea in which this RelationshipType is defined
     * @param sourceDestMultiplicity the maximum number of RootEntities that can participate for this RelationshipType instance
     * @param sourceDest the EntityType attached to this RelationshipType
     * @param sourceDestSuperRoleTypes the RoleTypes that are refined by the RoleType of this RelationshipType
     * @param sourceDestRoleConstraintClassNames the set of RoleConstraint class names to be enforced at each end of each instance of this RelationshipType
     * @param isAbstract if BooleanValue.TRUE, this EntityType cannot be instantiated and a non-abstract subtype must be instantiated instead
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
            MultiplicityValue         sourceDestMultiplicity,
            EntityType                sourceDest,
            RoleType []               sourceDestSuperRoleTypes,
            String []                 sourceDestRoleConstraintClassNames,
            BooleanValue              isAbstract,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( sourceDestMultiplicity == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SourceAndDestinationMultiplicity" );
        }
// FIXME?
//        if( sourceDest == null ) {
//            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null SourceAndDestination" );
//        }
//        if( sourceDestSuperRoleTypes == null || sourceDestSuperRoleTypes.length == 0 ) {
//            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null super RoleTypes" );
//        }
        if( isAbstract == null ) {
            throw new IllegalArgumentException( "To-be-created RelationshipType with Identifier " + identifier + " cannot have null IsAbstract" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        MEntityType  realSourceAndDestination = (MEntityType) sourceDest;
        MRoleType [] realSuperRoles           = copyIntoNewArray( sourceDestSuperRoleTypes, MRoleType.class );
        MSubjectArea realSubjectArea          = (MSubjectArea) theSubjectArea;

        MRelationshipType ret = new MRelationshipType(
                identifier,
                realSourceAndDestination,
                sourceDestMultiplicity,
                sourceDestRoleConstraintClassNames );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );
        ((MRoleType)ret.getSource()).setDirectSuperRoleTypes( realSuperRoles );

        realSubjectArea.addCollectableMeshType( ret );
        ret.setSubjectArea( realSubjectArea );

        MRelationshipType [] directSupertypes = new MRelationshipType[ realSuperRoles.length ];
        for( int i=0 ; i<directSupertypes.length ; ++i ) {
            directSupertypes[i] = (MRelationshipType) realSuperRoles[i].getRelationshipType();
        }
        ret.setDirectSupertypes( directSupertypes );

        ret.setIsAbstract( isAbstract );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

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
     * @param theDefaultValue the value with which instances of this PropertyType are initialized. This must be non-null for non-optional PropertyTypes
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
            FloatValue                theSequenceNumber )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theParent == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null parent" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( theDataType == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null DataType" );
        }
        if( theDefaultValue != null && theDefaultValueCode != null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have both DefaultValue and DefaultValueCode" );
        }
        if( isOptional == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null IsOptional" );
        }
        if( isReadOnly == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null IsReadOnly" );
        }
        if( theSequenceNumber == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + identifier + " cannot have null SequenceNumber" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        if( theParent.findPropertyTypeByName( theName.value() ) != null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier has a PropertyType with Name " + theName + " already" );
        }
        MAttributableMeshType realParent      = (MAttributableMeshType) theParent;
        MSubjectArea            realSubjectArea = (MSubjectArea) theSubjectArea;

        MPropertyType ret = new MPropertyType( identifier );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );
        ret.setDataType( theDataType );

        // FIXME: we should check whether default value and data type are compatible
        if( theDefaultValue != null ) {
            ret.setDefaultValue( theDefaultValue );
        }
        if( theDefaultValueCode != null ) {
            ret.setDefaultValueCode( theDefaultValueCode );
        }
        ret.setIsOptional( isOptional );

        if( ! isOptional.value() && theDefaultValue == null && theDefaultValueCode == null ) {
            throw new IllegalArgumentException(
                    "Default value must be given if PropertyType is mandatory: PropertyType "
                    + ret
                    + " on AttributableMeshType "
                    + theParent );
        }

        ret.setLocalPropertyTypeGuardClassNames( theLocalPropertyTypeGuardClassNames );
        ret.setIsReadOnly( isReadOnly );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );
        ret.setSequenceNumber( theSequenceNumber );

        realParent.addPropertyType( ret );

        ret.setAttributableMeshType( realParent );
        ret.setSubjectArea( realSubjectArea );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

    /**
     * Create a PropertyType that overrides another PropertyType.
     * 
     * @param toOverride set of PropertyTypes to override. All PropertyType given must have the same ancestor PropertyType,
     *        and each PropertyType must be declared in a different supertype of our parent AttributableMeshType that uses
     *        multiple inheritance. For single inheritance, the length of this array must be 1.
     * @param overriddenIdentifier the unique identifier of this overridden PropertyType
     * @param theUserDescriptions internationalized descriptions of this PropertyType
     * @param theParent the parent AttributableMeshType in which this PropertyType is defined
     * @param theSubjectArea the SubjectArea in which this PropertyType is defined
     * @param theDataType the data type for this PropertyType
     * @param theDefaultValue the value with which instances of this PropertyType are initialized. This must be non-null for non-optional PropertyTypes
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this PropertyType
     * @param theLocalPropertyTypeGuardClassNames the class names of the set of PropertyTypeGuards locally defined on this PropertyType
     * @param isOptional if true, this PropertyType can have value null; if false, this PropertyType cannot have the null value
     * @param isReadOnly if true, this PropertyType can only be read, not written
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created PropertyType
     * @throws InheritanceConflictException thrown if the overridden PropertyTypes are wrong
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
            InheritanceConflictException
    {
        if( overriddenIdentifier == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType cannot have null overridden Identifier" );
        }
        if( theParent == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have null parent" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have null SubjectArea" );
        }
        if( theDataType == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have null DataType" );
        }
        if( theDefaultValue != null && theDefaultValueCode != null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have both DefaultValue and DefaultValueCode" );
        }
        if( isOptional == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have null IsOptional" );
        }
        if( isReadOnly == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier " + overriddenIdentifier + " cannot have null IsReadOnly" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( overriddenIdentifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + overriddenIdentifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        if( toOverride == null || toOverride.length == 0 ) {
            throw new IllegalArgumentException( "Cannot override empty PropertyType list" );
        }
        MAttributableMeshType realParent      = (MAttributableMeshType) theParent;
        MSubjectArea          realSubjectArea = (MSubjectArea) theSubjectArea;
        MPropertyType []      realOverride    = copyIntoNewArray( toOverride, MPropertyType.class );

        MPropertyType ret = new MPropertyType( overriddenIdentifier );

        ret.setOverride( realOverride );
        ret.setName( realOverride[0].getName() ); // use any
        ret.setUserVisibleNameMap( realOverride[0].getUserVisibleNameMap() ); // use any
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        for( int i=0 ; i<realOverride.length ; ++i ) {
            if( ! realOverride[i].getDataType().isSupersetOfOrEquals( theDataType )) {
                throw new IllegalArgumentException(
                        "Cannot override Dataype "
                        + realOverride[i].getDataType()
                        + " with a different DataType or a larger domain: "
                        + theDataType
                        + ", AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }
            if( ! realOverride[i].getIsReadOnly().value() && isReadOnly.value() ) {
                throw new IllegalArgumentException(
                        "Cannot override PropertyType from read-write to read-only, AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }

            if( !realOverride[i].getIsOptional().value() && isOptional.value() ) {
                throw new IllegalArgumentException(
                        "Cannot make mandatory PropertyType optional in subtype, AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }

            if( ! realOverride[i].getIsReadOnly().value() && isReadOnly.value() ) {
                throw new IllegalArgumentException(
                        "Cannot override PropertyType from read-write to read-only, AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }

            int distance = theParent.getDistanceToSupertype( realOverride[i].getAttributableMeshType() );
            if( distance <= 0 ) {
                throw new IllegalArgumentException(
                        "Cannot override PropertyType "
                        + realOverride[i]
                        + " of non-supertype in "
                        + realParent
                        + ", distance is "
                        + distance );
            }
        }

        ret.setDataType( theDataType );

        // FIXME: we should check whether default value and data type are compatible
        if( theDefaultValue != null ) {
            ret.setDefaultValue( theDefaultValue );
        }
        if( theDefaultValueCode != null ) {
            ret.setDefaultValueCode( theDefaultValueCode );
        }
        ret.setIsOptional( isOptional );
        if( ! isOptional.value() && theDefaultValue == null && theDefaultValueCode == null ) {
            throw new IllegalArgumentException(
                    "Default value must be given if PropertyType is mandatory: PropertyType "
                    + ret
                    + " on AttributableMeshType "
                    + realParent );
        }

        ret.setIsReadOnly( isReadOnly );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );
        ret.setSequenceNumber( realOverride[0].getSequenceNumber() ); // use any, all the same

        realParent.addOverridingPropertyType( ret );

        ret.setAttributableMeshType( realParent );
        ret.setSubjectArea( realSubjectArea );

        // This overridden PropertyType gets added to the cluster under the name of its
        // "parent" AMO -- not the name of the AMO of the overridden MA.
        // Not sure that this meets all requirements under all circumstances?!?
        //
        // It has to be in the cluster, otherwise we can't find it, and so we would get the properties
        // of the overridden PropertyTypes if we were looking for it.

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

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
     * @param theDefaultValue the value with which instances of this PropertyType are initialized.
     *        This must be non-null for non-optional PropertyTypes. Given that this is a ProjectedPropertyType,
     *        this value is only used until the projection code is run for the first time.
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this PropertyType.
     *        Given that this is a ProjectedPropertyType, this value is only used until the projection code is run for the first time.
     * @param theInputProperties the set of PropertyTypes in particular RootEntities that needs to be monitored in order to
     *        determine when to re-run the projection code. The RootEntities are determined through the TraversalSpecifications
     *        referenced by the theInputProperties.
     * @param theProjectionCode a StringValue with a text mime type that contains the projection code for determining the value of this ProjectdPropertyType
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
            FloatValue                          theSequenceNumber )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theParent == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have null parent" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have null SubjectArea" );
        }
        if( theDataType == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have null DataType" );
        }
        if( theDefaultValue != null && theDefaultValueCode != null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have both DefaultValue and DefaultValueCode" );
        }
        if( theSequenceNumber == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + identifier + " cannot have null SequenceNumber" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        if( theParent.findPropertyTypeByName( theName.value() ) != null ) {
            throw new IllegalArgumentException( "To-be-created PropertyType with Identifier has a PropertyType with Name " + theName + " already" );
        }
        MAttributableMeshType realParent      = (MAttributableMeshType) theParent;
        MSubjectArea          realSubjectArea = (MSubjectArea) theSubjectArea;

        MProjectedPropertyType ret = new MProjectedPropertyType( identifier );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );
        ret.setDataType( theDataType );

        // FIXME: we should check whether default value and data type are compatible
        if( theDefaultValue != null ) {
            ret.setDefaultValue( theDefaultValue );
        }
        if( theDefaultValueCode != null ) {
            ret.setDefaultValueCode( theDefaultValueCode );
        }
        if( theProjectionCode != null ) {
            ret.setProjectionCode( theProjectionCode );
        }
        ret.setInputPropertiesDuringParsing( theInputProperties );

        ret.setIsOptional( BooleanValue.TRUE );
        ret.setIsReadOnly( BooleanValue.TRUE );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );
        ret.setSequenceNumber( theSequenceNumber );

        realParent.addPropertyType( ret );

        ret.setAttributableMeshType( realParent );
        ret.setSubjectArea( realSubjectArea );

        theModelBase.theCluster.addObject( ret );

        return new MProjectedPropertyTypePatcher( ret );
    }

    /**
     * Create a ProjectedPropertyType that overrides another PropertyType.
     * 
     * @param toOverride set of PropertyTypes to override. All PropertyType given must have the same ancestor PropertyType,
     *        and each PropertyType must be declared in a different supertype of our parent AttributableMeshType that uses
     *        multiple inheritance. For single inheritance, the length of this array must be 1.
     * @param overriddenIdentifier the unique identifier of this overridden PropertyType
     * @param theUserDescriptions internationalized descriptions of this ProjectedPropertyType
     * @param theParent the parent AttributableMeshType in which this ProjectedPropertyType is defined
     * @param theSubjectArea the SubjectArea in which this ProjectedPropertyType is defined
     * @param theDataType the data type for this ProjectedPropertyType
     * @param theDefaultValue the value with which instances of this PropertyType are initialized.
     *        This must be non-null for non-optional PropertyTypes. Given that this is a ProjectedPropertyType,
     *        this value is only used until the projection code is run for the first time.
     * @param theDefaultValueCode if this is given, this is code whose result is the default value for this PropertyType.
     *        Given that this is a ProjectedPropertyType, this value is only used until the projection code is run for the first time.
     * @param theInputProperties the set of PropertyTypes in particular RootEntities that needs to be monitored in order to
     *        determine when to re-run the projection code. The RootEntities are determined through the TraversalSpecifications
     *        referenced by the theInputProperties.
     * @param theProjectionCode a StringValue with a text mime type that contains the projection code for determining the value of this ProjectdPropertyType
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @return the newly created ProjectedPropertyType
     * @throws InheritanceConflictException thrown if the overridden PropertyTypes are wrong
     */
    public ProjectedPropertyTypePatcher createOverridingProjectedPropertyType(
            PropertyType []                     toOverride,
            MeshTypeIdentifier                  overriddenIdentifier,
            L10PropertyValueMap                 theUserDescriptions,
            AttributableMeshType                theParent,
            SubjectArea                         theSubjectArea,
            DataType                            theDataType,
            PropertyValue                       theDefaultValue,     // used only to initialize with something meaningful
            StringValue                         theDefaultValueCode, // used only to initialize with something meaningful
            TraversalToPropertySpecification [] theInputProperties,
            StringValue                         theProjectionCode,
            BooleanValue                        doGenerateInterfaceCode,
            BooleanValue                        doGenerateImplementationCode )
        throws
            InheritanceConflictException
    {
        if( overriddenIdentifier == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType cannot have null overridden Identifier" );
        }
        if( theParent == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + overriddenIdentifier + " cannot have null parent" );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + overriddenIdentifier + " cannot have null SubjectArea" );
        }
        if( theDataType == null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + overriddenIdentifier + " cannot have null DataType" );
        }
        if( theDefaultValue != null && theDefaultValueCode != null ) {
            throw new IllegalArgumentException( "To-be-created ProjectedPropertyType with Identifier " + overriddenIdentifier + " cannot have both DefaultValue and DefaultValueCode" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( overriddenIdentifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + overriddenIdentifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        if( toOverride == null || toOverride.length == 0 ) {
            throw new IllegalArgumentException( "Cannot override empty PropertyType list" );
        }
        
        MAttributableMeshType realParent      = (MAttributableMeshType) theParent;
        MSubjectArea          realSubjectArea = (MSubjectArea) theSubjectArea;
        MPropertyType []      realOverride    = copyIntoNewArray( toOverride, MPropertyType.class );

        MProjectedPropertyType ret = new MProjectedPropertyType( overriddenIdentifier );

        ret.setOverride( realOverride );
        ret.setName( realOverride[0].getName() ); // take any
        ret.setUserVisibleNameMap( realOverride[0].getUserVisibleNameMap() ); // use any
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        if( theProjectionCode != null ) {
            ret.setProjectionCode( theProjectionCode );
        }
        ret.setInputPropertiesDuringParsing( theInputProperties );

        for( int i=0 ; i<realOverride.length ; ++i ) {
            if( !realOverride[i].getDataType().isSupersetOfOrEquals( theDataType )) {
                throw new IllegalArgumentException(
                        "Cannot override DataType "
                        + realOverride[i].getDataType()
                        + " with different DataType or larger domain: "
                        + theDataType
                        + ", AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }

            if( !realOverride[i].getIsReadOnly().value() ) {
                throw new IllegalArgumentException(
                        "Cannot create overriding ProjectedPropertyType for read-write parent PropertyType, AttributableMeshType: "
                        + realParent
                        + ", PropertyType: "
                        + realOverride[i] );
            }

            int distance = realParent.getDistanceToSupertype( realOverride[i].getAttributableMeshType() );
            if( distance <= 0 ) {
                throw new IllegalArgumentException(
                        "Cannot override PropertyType "
                        + realOverride[i]
                        + " of non-supertype in "
                        + realParent
                        + ", distance is "
                        + distance );
            }
        }

        ret.setDataType( theDataType );

        // FIXME: we should check whether default value and data type are compatible
        if( theDefaultValue != null ) {
            ret.setDefaultValue( theDefaultValue );
        }
        if( theDefaultValueCode != null ) {
            ret.setDefaultValueCode( theDefaultValueCode );
        }
        
        ret.setIsOptional( BooleanValue.TRUE );
        ret.setIsReadOnly( BooleanValue.TRUE );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );
        ret.setSequenceNumber( realOverride[0].getSequenceNumber() ); // use any, all the same

        realParent.addOverridingPropertyType( ret );

        ret.setAttributableMeshType( realParent );
        ret.setSubjectArea( realSubjectArea );

        // This overridden PropertyTypes gets added to the cluster under the name of its
        // "parent" AttributableMeshTypes -- not the name of the AttributableMeshTypes of the overridden
        // PropertyType.
        // Not sure that this meets all requirements under all circumstances?!?
        //
        // It has to be in the cluster, otherwise we can't find it, and so we would get the properties
        // of the overridden PropertyType if we were looking for it.

        theModelBase.theCluster.addObject( ret );

        return new MProjectedPropertyTypePatcher( ret );
    }

    /**
     * Create a PropertyTypeGroup.
     * 
     * @param identifier globally unique identifier of this PropertyTypeGroup
     * @param theName programmatic name of this PropertyTypeGroup
     * @param theUserNames internalionalized names of this PropertyTypeGroup
     * @param theUserDescriptions internationalized descriptions of this PropertyTypeGroup
     * @param theParent the parent AttributableMeshType in which this PropertyTypeGroup is defined
     * @param theSubjectArea the SubjectArea in which this PropertyTypeGroup is defined
     * @param theMembers the member PropertyTypes of this PropertyTypeGroup. All PropertyType must be defined in the parent
     *        AttributableMeshType, or any of its supertypes.
     * @param doGenerateInterfaceCode if BooleanValue.TRUE, instructs code generator to generate interface code
     * @param doGenerateImplementationCode if BooleanValue.TRUE, instructs code generator to generate implementation code
     * @param theSequenceNumber determines the sequence of display in things like property sheets etc.
     * @return the newly created PropertyTypeGroup
     */
    public PropertyTypeGroup createPropertyTypeGroup(
            MeshTypeIdentifier        identifier,
            StringValue               theName,
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            AttributableMeshType      theParent,
            SubjectArea               theSubjectArea,
            PropertyType []           theMembers,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode,
            FloatValue                theSequenceNumber )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup with Identifier " + identifier + " cannot have null Name" );
        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theParent == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup with Identifier " + identifier + " cannot have null parent" );
        }
        if( theMembers == null || theMembers.length < 2 ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup with Identifier " + identifier + " cannot have less than 2 Members" );
        }
        if( theSequenceNumber == null ) {
            throw new IllegalArgumentException( "To-be-created PropertyTypeGroup with Identifier " + identifier + " cannot have null SequenceNumber" );
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }
        
        MAttributableMeshType realParent      = (MAttributableMeshType) theParent;
        MSubjectArea          realSubjectArea = (MSubjectArea) theSubjectArea;

        MPropertyTypeGroup ret = new MPropertyTypeGroup( identifier );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );

        realParent.addPropertyTypeGroup( ret );

        ret.setAttributableMeshType( realParent );
        ret.setContainedPropertyTypes( theMembers );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );
        ret.setSequenceNumber( theSequenceNumber );

        ret.setSubjectArea( realSubjectArea );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

    /**
     * Create a SubjectArea.
     * 
     * @param identifier globally unique identifier of this SubjectArea
     * @param theName programmatic name of this SubjectArea
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
            L10PropertyValueMap       theUserNames,
            L10PropertyValueMap       theUserDescriptions,
            SubjectArea []            theSubjectAreaDependencies,
            ClassLoader               theClassLoader,
            BooleanValue              doGenerateInterfaceCode,
            BooleanValue              doGenerateImplementationCode )
    {
        if( identifier == null ) {
            throw new IllegalArgumentException( "To-be-created SubjectArea cannot have null Identifier" );
        }
        if( theName == null ) {
            throw new IllegalArgumentException( "To-be-created SubjectArea with Identifier " + identifier + " cannot have null Name" );
        }
//        if( theVersion == null ) {
//            throw new IllegalArgumentException( "To-be-created SubjectArea with Identifier " + identifier + " cannot have null Version" );
//        }
        if( theUserNames == null || theUserNames.getDefault() == null ) {
            throw new IllegalArgumentException( "To-be-created SubjectArea with Identifier " + identifier + " cannot have empty UserNames" );
        }
        if( theSubjectAreaDependencies == null ) {
            theSubjectAreaDependencies = new SubjectArea[0];
        }
        try {
            MeshType already = theModelBase.findLoadedMeshTypeByIdentifier( identifier );
            if( already != null ) {
                throw new IllegalArgumentException( "A MeshType with Identifier " + identifier + " exists already: " + already );
            }
        } catch( MeshTypeNotFoundException ex ) {
            // great
        }

        MSubjectArea ret = new MSubjectArea( identifier );

        ret.setName( theName );
        ret.setUserVisibleNameMap( theUserNames );
        ret.setUserVisibleDescriptionMap( theUserDescriptions );
        ret.setSubjectAreaDependencies( theSubjectAreaDependencies );
        ret.setClassLoader( theClassLoader );
        ret.setDoGenerateInterfaceCode( doGenerateInterfaceCode );
        ret.setDoGenerateImplementationCode( doGenerateImplementationCode );

        theModelBase.theCluster.addObject( ret );

        return ret;
    }

    /**
     * Helper method to copy into an array.
     *
     * @param oldArray the old array
     * @param arrayComponentType the type of the new array
     * @return the new array with the old content
     * @param <T> type parameter
     */
    @SuppressWarnings(value={"unchecked"})
    protected <T> T [] copyIntoNewArray(
            Object [] oldArray,
            Class<T>  arrayComponentType )
    {
        T [] ret = ArrayHelper.createArray( arrayComponentType, oldArray.length );
        for( int i=0 ; i<oldArray.length ; ++i ) {
            ret[i] = (T) oldArray[i];
        }
        return ret;
    }

    /**
     * Helper method to copy into an array.
     *
     * @param oldCollection the old Collection
     * @param arrayComponentType the type of the new array
     * @return the new array with the old content
     * @param <T> type parameter
     */
    @SuppressWarnings(value={"unchecked"})
    protected <T> T [] copyIntoNewArray(
            Collection<?> oldCollection,
            Class<T>      arrayComponentType )
    {
        T [] ret = ArrayHelper.createArray( arrayComponentType, oldCollection.size() );
        Iterator<?> iter = oldCollection.iterator();
        int         max  = oldCollection.size();
        for( int i=0 ; i<max ; ++i ) {
            ret[i] = (T) iter.next();
        }
        return ret;
    }

    /**
      * The MModelBase that this belongs to.
      */
    protected MModelBase theModelBase;
}
