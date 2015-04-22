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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
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
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.modelbase.AttributableMeshTypeNotFoundException;
import org.infogrid.modelbase.EntityTypeNotFoundException;
import org.infogrid.modelbase.MeshTypeLifecycleEventListener;
import org.infogrid.modelbase.MeshTypeLifecycleManager;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelLoader;
import org.infogrid.modelbase.PropertyTypeNotFoundException;
import org.infogrid.modelbase.RelationshipTypeNotFoundException;
import org.infogrid.modelbase.SubjectAreaNotFoundException;
import org.infogrid.modelbase.WrongMeshTypeException;
import org.infogrid.modelbase.externalized.xml.XmlModelLoader;
import org.infogrid.module.Module;
import org.infogrid.module.ModuleActivationException;
import org.infogrid.module.ModuleClassLoader;
import org.infogrid.module.ModuleException;
import org.infogrid.module.ModuleMeta;
import org.infogrid.module.ModuleNotFoundException;
import org.infogrid.module.ModuleRegistry;
import org.infogrid.module.ModuleRequirement;
import org.infogrid.module.ModuleResolutionCandidateNotUniqueException;
import org.infogrid.module.ModuleResolutionException;
import org.infogrid.util.logging.Log;

/**
  * Implementation of ModelBase that holds its content in memory only.
  */
public class MModelBase
        implements
            ModelBase
{
    private static final Log  log              = Log.getLogInstance(MModelBase.class); // our own, private logger
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Factory method.
     *
     * @return the created MModelBase
     */
    public static ModelBase create()
    {
        MMeshTypeLifecycleManager  lifecycleManager          = MMeshTypeLifecycleManager.create();
        MMeshTypeIdentifierFactory meshTypeIdentifierFactory = MMeshTypeIdentifierFactory.create();
        MMeshTypeSynonymDictionary synonymDictionary         = MMeshTypeSynonymDictionary.create();

        return new MModelBase( lifecycleManager, meshTypeIdentifierFactory, synonymDictionary );
    }

    /**
     * Factory method.
     *
     * @param lifecycleManager allows the creation of MeshTypes
     * @param meshTypeIdentifierFactory the factory for MeshTypeIdentifiers to use
     * @param synonymDictionary the dictionary for MeshTypeIdentifier synonyms to use
     * @return the created MModelBase
     */
    public static MModelBase create(
            MMeshTypeLifecycleManager  lifecycleManager,
            MMeshTypeIdentifierFactory meshTypeIdentifierFactory,
            MMeshTypeSynonymDictionary synonymDictionary )
    {
        return new MModelBase( lifecycleManager, meshTypeIdentifierFactory, synonymDictionary );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param lifecycleManager allows the creation of MeshTypes
     * @param meshTypeIdentifierFactory the factory for MeshTypeIdentifiers to use
     * @param synonymDictionary the dictionary for MeshTypeIdentifier synonyms to use
     */
    protected MModelBase(
            MMeshTypeLifecycleManager  lifecycleManager,
            MMeshTypeIdentifierFactory meshTypeIdentifierFactory,
            MMeshTypeSynonymDictionary synonymDictionary )
    {
        theLifecycleManager          = lifecycleManager;
        theMeshTypeIdentifierFactory = meshTypeIdentifierFactory;
        theSynonymDictionary         = synonymDictionary;

        theLifecycleManager.setModelBase( this );
        theSynonymDictionary.setModelBase( this );

        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "constructor" );
        }
    }

    /**
      * Alternate way of constructing a MModelBase by deserializing one.
      *
      * @param theRawStream where to read from
      * @return the deserialized ModelBase
      * @throws ClassNotFoundException a class could not be found that was needed to deserialize this ModelBase
      * @throws IOException an error occurred while reading
      * @throws StreamCorruptedException the input stream was corrupted
      */
    public static ModelBase createFromSerialized(
            InputStream theRawStream )
        throws
            ClassNotFoundException,
            IOException,
            StreamCorruptedException
    {
        if( log.isDebugEnabled() ) {
            log.debug( "MModelBase.createFromSerialized( " + theRawStream + " )" );
        }
        ObjectInputStream theObjectStream = new ObjectInputStream( theRawStream );

        MModelBase ret = (MModelBase) theObjectStream.readObject();

        theObjectStream.close();

        return ret;
    }

    /**
     * Obtain a MeshTypeIdentifierFactory appropriate for this ModelBase.
     *
     * @return the MeshTypeIdentifierFactory
     */
    @Override
    public MMeshTypeIdentifierFactory getMeshTypeIdentifierFactory()
    {
        return theMeshTypeIdentifierFactory;
    }

    /**
      * Obtain the manager for MeshType lifecycles. This manager cannot be
      * re-assigned as it is specific to the implementation of the ModelBase.
      *
      * @return the MeshTypeLifecycleManager for this ModelBase
      */
    @Override
    public MeshTypeLifecycleManager getMeshTypeLifecycleManager()
    {
        return theLifecycleManager;
    }

    /**
      * Obtain an iterator to iterate over all MeshTypes contained in this ModelBase.
      *
      * @return the iterator
      */
    @Override
    public Iterator<MeshType> iterator()
    {
        return theCluster.iterator();
    }

    /**
      * Obtain an iterator to iterate over all SubjectAreas.
      *
      * @return the iterator
      */
    @Override
    public Iterator<SubjectArea> subjectAreaIterator()
    {
        return theCluster.subjectAreaIterator();
    }

    /**
      * Subscribe to events indicating the addition/removal/etc
      * of MeshTypes in this ModelBase.
      *
      * @param newListener the listener to be added
      * @see #removeMeshTypeLifecycleEventListener
      */
    @Override
    public void addMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener newListener )
    {
        theCluster.addMeshTypeLifecycleEventListener( newListener );
    }

    /**
      * Unsubscribe from events indicating the addition/removal/etc
      * of MeshTypes in this ModelBase.
      *
      * @param oldListener the listener to be removed
      * @see #removeMeshTypeLifecycleEventListener
      */
    @Override
    public void removeMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener oldListener )
    {
        theCluster.removeMeshTypeLifecycleEventListener( oldListener );
    }

    /**
      * Find a SubjectArea by name and version number.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @return the found SubjectArea
      * @throws SubjectAreaNotFoundException thrown if the SubjectArea cannot be found
      */
    @Override
    public SubjectArea findSubjectArea(
            String subjectAreaName,
            String subjectAreaVersionNumber )
        throws
            SubjectAreaNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findSubjectArea", subjectAreaName, subjectAreaVersionNumber );
        }

        SubjectArea ret = theCluster.findSubjectArea( subjectAreaName, subjectAreaVersionNumber );
        if( ret == null ) {
            try {
                ret = attemptToLoadSubjectArea( subjectAreaName, subjectAreaVersionNumber );

                if( ret == null ) {
                    throw new SubjectAreaNotFoundException( subjectAreaName, subjectAreaVersionNumber );
                }
            } catch( IOException ex ) {
                throw new SubjectAreaNotFoundException( subjectAreaName, subjectAreaVersionNumber, ex );
            }
        }
        return ret;
    }

    /**
      * Find an EntityType by name and its containing SubjectArea.
      *
      * @param theSubjectArea the SubjectArea in which we are looking for the EntityType
      * @param theEntityTypeName the name of the EntityType
      * @return the found EntityType
      * @throws EntityTypeNotFoundException thrown if the EntityType cannot be found
      */
    @Override
    public EntityType findEntityType(
            SubjectArea theSubjectArea,
            String      theEntityTypeName )
        throws
            EntityTypeNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findEntityType", theSubjectArea, theEntityTypeName );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "SubjectArea cannot be null" );
        }
        AttributableMeshType ret = theCluster.findAttributableMeshType( theSubjectArea, theEntityTypeName );
        if( ret == null ) {
            throw new EntityTypeNotFoundException( theSubjectArea, theEntityTypeName );
        }
        if( ret instanceof EntityType ) {
            return (EntityType) ret;
        }
        throw new EntityTypeNotFoundException( theSubjectArea, theEntityTypeName );
    }

    /**
      * Shortcut for finding an EntityType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param theEntityTypeName the name of the EntityType
      * @return the found EntityType
      * @throws MeshTypeNotFoundException thrown if the EntityType cannot be found
      */
    @Override
    public EntityType findEntityType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String theEntityTypeName )
        throws
            MeshTypeNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findEntityType", subjectAreaName, subjectAreaVersionNumber, theEntityTypeName );
        }
        SubjectArea theSa = findSubjectArea( subjectAreaName, subjectAreaVersionNumber );
        return findEntityType( theSa, theEntityTypeName );
    }

    /**
      * Find a RelationshipType by name and its containing SubjectArea.
      *
      * @param theSubjectArea the SubjectArea in which we are looking for the RelationshipType
      * @param theRelationshipTypeName the name of the RelationshipType
      * @return the found RelationshipType
      * @throws RelationshipTypeNotFoundException thrown if the RelationshipType cannot be found
      */
    @Override
    public RelationshipType findRelationshipType(
            SubjectArea theSubjectArea,
            String      theRelationshipTypeName )
        throws
            RelationshipTypeNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findRelationshipType", theSubjectArea, theRelationshipTypeName );
        }
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "SubjectArea cannot be null" );
        }
        AttributableMeshType ret = theCluster.findAttributableMeshType( theSubjectArea, theRelationshipTypeName );
        if( ret == null ) {
            throw new RelationshipTypeNotFoundException( theSubjectArea, theRelationshipTypeName );
        }
        if( ret instanceof RelationshipType ) {
            return (RelationshipType) ret;
        }

        throw new RelationshipTypeNotFoundException( theSubjectArea, theRelationshipTypeName );
    }

    /**
      * Shortcut for finding a RelationshipType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param theRelationshipTypeName the name of the RelationshipType
      * @return the found RelationshipType
      * @throws MeshTypeNotFoundException thrown if the RelationshipType cannot be found
      */
    @Override
    public RelationshipType findRelationshipType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String theRelationshipTypeName )
        throws
            MeshTypeNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findRelationshipType", subjectAreaName, subjectAreaVersionNumber, theRelationshipTypeName );
        }
        SubjectArea theSa = findSubjectArea( subjectAreaName, subjectAreaVersionNumber );
        return findRelationshipType( theSa, theRelationshipTypeName );
    }

    /**
      * Find a PropertyType by name and its AttributableMeshType (EntityType or RelationshipType).
      *
      * @param theAttributableMeshType the AttributableMeshType within which we are looking for the PropertyType
      * @param thePropertyTypeName the name of the PropertyType
      * @return the found PropertyType
      * @throws PropertyTypeNotFoundException thrown if the PropertyType cannot be found
      */
    @Override
    public PropertyType findPropertyType(
            AttributableMeshType theAttributableMeshType,
            String               thePropertyTypeName )
        throws
            PropertyTypeNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findPropertyType", theAttributableMeshType, thePropertyTypeName );
        }
        if( theAttributableMeshType == null ) {
            throw new IllegalArgumentException( "AttributableMeshType cannot be null" );
        }
        PropertyType ret = theCluster.findPropertyType( theAttributableMeshType, thePropertyTypeName );
        if( ret == null ) {
            throw new PropertyTypeNotFoundException( theAttributableMeshType, thePropertyTypeName );
        }
        return ret;
    }

    /**
      * Shortcut for finding a PropertyType directly.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @param theAttributableMeshType the name of the owning EntityType or RelationshipType
      * @param thePropertyTypeName the name of the PropertyType
      * @return the found PropertyType
      * @throws MeshTypeNotFoundException thrown if the PropertyType cannot be found
      */
    @Override
    public PropertyType findPropertyType(
            String subjectAreaName,
            String subjectAreaVersionNumber,
            String theAttributableMeshType,
            String thePropertyTypeName )
        throws
            MeshTypeNotFoundException
    {
        SubjectArea sa = findSubjectArea( subjectAreaName, subjectAreaVersionNumber ); // may throw exception

        AttributableMeshType amo = theCluster.findAttributableMeshType( sa, theAttributableMeshType );
        if( amo == null ) {
            throw new AttributableMeshTypeNotFoundException( sa, theAttributableMeshType );
        }
        return findPropertyType( amo, thePropertyTypeName );
    }

    /**
     * Find a MeshType by its unique identifier. If needed, attempt to load additional
     * models to resolve the request.
     * 
     * 
     * @param identifier Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if the MeshType could not be found
     */
    @Override
    public MeshType findMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        return findMeshTypeByIdentifierInternal( identifier, true );
    }
    
    /**
     * Find a MeshType by its unique identifier. This method does not attempt to load additional
     * models to resolve the request.
     * 
     * @param identifier Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     */
    @Override
    public MeshType findLoadedMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        return findMeshTypeByIdentifierInternal( identifier, false );
    }

    /**
     * Find a MeshType by its unique identifier. Specify whether to attempt to load additional
     * models to resolve the request.
     * 
     * @param identifier Identifier of the to-be-found MeshType
     * @param doResolve if true, attempt to load
     * @return the found MeshType
     * @throws MeshTypeWithIdentifierNotFoundException if the MeshType could not be found
     */
    protected MeshType findMeshTypeByIdentifierInternal(
            MeshTypeIdentifier identifier,
            boolean            doResolve )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findMeshTypeByIdentifierInternal", identifier, doResolve );
        }
        if( identifier == null ) {
            throw new NullPointerException( "Null Identifier given" );
        }

        MeshType ret = null;
        try {
            ret = theCluster.findMeshTypeByIdentifier( identifier, doResolve );
        } catch( MeshTypeNotFoundException ex ) {
            // do nothing
        } catch( IOException ex ) {
            // do nothing
        }
        if( ret != null ) {
            return ret;
        }

        // Now look for RoleTypes
        String roleIdString = identifier.toExternalForm();

        if( roleIdString.endsWith( RoleType.SOURCE_POSTFIX )) {
            try {
                ret = theCluster.findMeshTypeByIdentifier(
                        theMeshTypeIdentifierFactory.fromExternalForm( 
                                roleIdString.substring( 0, roleIdString.length() - RoleType.SOURCE_POSTFIX.length())),
                        doResolve );

                if( ret instanceof RelationshipType ) {
                    ret = ((RelationshipType)ret).getSource();
                } else {
                    ret = null;
                }
            } catch( MeshTypeNotFoundException ex ) {
                // do nothing
            } catch( IOException ex ) {
                // do nothing
            }
        }
        if( ret != null ) {
            return ret;
        }

        if( roleIdString.endsWith( RoleType.DESTINATION_POSTFIX )) {
            try {
                String relIdentifierString = identifier.toExternalForm();
                ret = theCluster.findMeshTypeByIdentifier(
                        theMeshTypeIdentifierFactory.fromExternalForm( 
                                relIdentifierString.substring( 0, relIdentifierString.length() - RoleType.DESTINATION_POSTFIX.length())),
                        doResolve );

                if( ret instanceof RelationshipType ) {
                    ret = ((RelationshipType)ret).getDestination();
                } else {
                    ret = null;
                }
            } catch( MeshTypeNotFoundException ex ) {
                // do nothing
            } catch( IOException ex ) {
                // do nothing
            }
        }
        if( ret != null ) {
            return ret;
        }

        if( roleIdString.endsWith( RoleType.TOP_SINGLETON_POSTFIX )) {
            try {
                String relIdentifierString = identifier.toExternalForm();
                ret = theCluster.findMeshTypeByIdentifier(
                        theMeshTypeIdentifierFactory.fromExternalForm( 
                                relIdentifierString.substring( 0, relIdentifierString.length() - RoleType.TOP_SINGLETON_POSTFIX.length())),
                        doResolve );

                if( ret instanceof RelationshipType ) {
                    ret = ((RelationshipType)ret).getSource();
                } else {
                    ret = null;
                }
            } catch( MeshTypeNotFoundException ex ) {
                // do nothing
            } catch( IOException ex ) {
                // do nothing
            }
        }
        if( ret != null ) {
            return ret;
        }

        throw new MeshTypeWithIdentifierNotFoundException( identifier );
        // this may call us back to load a subject area we have not loaded yet
    }


    /**
     * This internal method is called when there is an attempt to access a SubjectArea
     * which is not present in the working model. This method will attempt to load it, and returns
     * true if it was successful. This must only be called if this SubjectArea has not been loaded before.
     *
     * @param saName fully-qualified name of the SubjectArea to be loaded
     * @param saVersion version number of the SubjectArea to be loaded
     * @return the found SubjectArea
     * @throws SubjectAreaNotFoundException thrown if the SubjectArea was not found
     * @throws IOException thrown if the file could not be read
     */
    public SubjectArea attemptToLoadSubjectArea(
            String saName,
            String saVersion )
        throws
            SubjectAreaNotFoundException,
            IOException
    {
        SubjectArea ret;
        ClassLoader cl = getClass().getClassLoader();
        if( cl instanceof ModuleClassLoader && ((ModuleClassLoader)cl).getModuleRegistry() != null ) {
            try {
               ret = attemptToLoadSubjectAreaWithModuleRegistry( ((ModuleClassLoader)cl).getModuleRegistry(), saName, saVersion );

            } catch( ModuleException ex ) {
                throw new SubjectAreaNotFoundException( saName, saVersion, ex );
            }
        } else {
            try {
                ret = attemptToLoadSubjectAreaWithoutModuleRegistry( saName, saVersion );

            } catch( SubjectAreaNotFoundException ex ) {
                throw ex;

            } catch( MeshTypeNotFoundException ex ) {
                throw new SubjectAreaNotFoundException( saName, saVersion, ex );
            }
        }
        return ret;
    }

    /**
     * This internal method is called when there is an attempt to access a SubjectArea
     * which is not present in the working model. This method will attempt to load it without the
     * Module Framework. It returns true if it was successful. This must only be called
     * if this SubjectArea has not been loaded before.
     *
     * @param saName fully-qualified name of the SubjectArea to be loaded
     * @param saVersion version number of the SubjectArea to be loaded
     * @return the found SubjectArea
     * @throws MeshTypeNotFoundException thrown if the MeshType was not found
     * @throws IOException thrown if the file could not be read
     */
    protected SubjectArea attemptToLoadSubjectAreaWithoutModuleRegistry(
            String saName,
            String saVersion )
        throws
            MeshTypeNotFoundException,
            IOException
    {
        StringBuilder path = new StringBuilder();
        path.append( "infogrid-models/" );
        path.append( saName );
        path.append( ".V" );
        if( saVersion != null ) {
            path.append( saVersion );
        }
        path.append( ".xml" );

        String realPath = path.toString();

        ClassLoader cl     = getClass().getClassLoader();
        InputStream stream = cl.getResourceAsStream( realPath );

        if( stream == null ) {
            return null;
        }

        ModelLoader theLoader = new XmlModelLoader(
                            this,
                            stream,
                            cl, // FIXME?
                            cl,
                            realPath + ": " );
        SubjectArea [] ret = theLoader.loadAndCheckModel( getMeshTypeLifecycleManager(), TimeStampValue.now() );
        return ret[0];
    }

    /**
     * This internal method is called when there is an attempt to access a SubjectArea
     * which is not present in the working model. This method will attempt to load it using the
     * Module Framework. It returns true if it was successful. This must only be called
     * if this SubjectArea has not been loaded before.
     *
     * @param registry the ModuleRegistry to use
     * @param saName fully-qualified name of the SubjectArea to be loaded
     * @param saVersion version number of the SubjectArea to be loaded
     * @return the found SubjectArea
     * @throws ModuleNotFoundException thrown if the ModelModule was not found
     * @throws ModuleResolutionException thrown if the found ModelModule's dependencies could not be resolved
     * @throws ModuleActivationException thrown if the found ModelModule could not be activated
     * @throws ModuleResolutionCandidateNotUniqueException thrown if a dependency could not be uniquely resolved
     */
    protected SubjectArea attemptToLoadSubjectAreaWithModuleRegistry(
            ModuleRegistry registry,
            String         saName,
            String         saVersion )
        throws
            ModuleNotFoundException,
            ModuleResolutionException,
            ModuleActivationException,
            ModuleResolutionCandidateNotUniqueException
    {
        ModuleRequirement saRequirement = ModuleRequirement.create1( saName, saVersion, true );
        ModuleMeta        saCandidate   = registry.determineSingleResolutionCandidate( saRequirement );

        Module saModule = registry.resolve( saCandidate, true );

        saModule.activateRecursively();

        return theCluster.findSubjectArea( saName, saVersion );
    }

    /**
     * Find a SubjectArea by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found SubjectArea
     * @return the found SubjectArea, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a SubjectArea with this Identifier cannot be found
     */
    @Override
    public SubjectArea findSubjectAreaByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, SubjectArea.class );
    }

    /**
     * Find a CollectableMeshType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found CollectableMeshType
     * @return the found CollectableMeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a CollectableMeshType with this Identifier cannot be found
     */
    @Override
    public CollectableMeshType findCollectableMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, CollectableMeshType.class );
    }

    /**
     * Find an AttributableMeshType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found AttributableMeshType
     * @return the found AttributableMeshType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if an AttributableMeshType with this Identifier cannot be found
     */
    @Override
    public AttributableMeshType findAttributableMeshTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, AttributableMeshType.class );
    }

    /**
     * Find an EntityType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found EntityType
     * @return the found EntityType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if an EntityType with this Identifier cannot be found
     */
    @Override
    public EntityType findEntityTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, EntityType.class );
    }

    /**
     * Find a RelationshipType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found RelationshipType
     * @return the found RelationshipType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a RelationshipType with this Identifier cannot be found
     */
    @Override
    public RelationshipType findRelationshipTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, RelationshipType.class );
    }

    /**
     * Find a PropertyTypeOrGroup by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found PropertyTypeOrGroup
     * @return the found PropertyTypeOrGroup, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a PropertyTypeOrGroup with this Identifier cannot be found
     */
    @Override
    public PropertyTypeOrGroup findPropertyTypeOrGroupByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, PropertyTypeOrGroup.class );
    }

    /**
     * Find a PropertyType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found PropertyType
     * @return the found PropertyType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a PropertyType with this Identifier cannot be found
     */
    @Override
    public PropertyType findPropertyTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, PropertyType.class );
    }

    /**
     * Find a ProjectedPropertyType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found ProjectedPropertyType
     * @return the found ProjectedPropertyType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a ProjectedPropertyType with this Identifier cannot be found
     */
    @Override
    public ProjectedPropertyType findProjectedPropertyTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, ProjectedPropertyType.class );
    }

    /**
     * Find a PropertyTypeGroup by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found PropertyTypeGroup
     * @return the found PropertyTypeGroup, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a PropertyTypeGroup with this Identifier cannot be found
     */
    @Override
    public PropertyTypeGroup findPropertyTypeGroupByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, PropertyTypeGroup.class );
    }

    /**
     * Find a RoleType by its unique identifier.
     * 
     * @param identifier Identifier of the to-be-found RoleType
     * @return the found RoleType, or null if not found
     * @throws MeshTypeWithIdentifierNotFoundException if a RoleType with this Identifier cannot be found
     */
    @Override
    public RoleType findRoleTypeByIdentifier(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        MeshType ret = findMeshTypeByIdentifier( identifier );
        return checkType( identifier, ret, RoleType.class );
    }

    /**
     * Obtain a MeshTypeSynonymDictionary.
     *
     * @return the MeshTypeSynonyDirectory.
     */
    @Override
    public MMeshTypeSynonymDictionary getSynonymDictionary()
    {
        return theSynonymDictionary;
    }

    /**
     * Helper method to check whether the right subtype of MeshType is being returned. If not, throw exception.
     * 
     * @param identifier the Identifier of the found type
     * @param type the MeshType
     * @param clazz the Class of MeshType expected
     * @return the subtype of MeshType corresponding to clazz
     * @throws MeshTypeWithIdentifierNotFoundException if type is not an instance of clazz
     * @param <T> the type parameter
     */    
    @SuppressWarnings(value={"unchecked"})
    protected <T> T checkType(
            MeshTypeIdentifier identifier,
            MeshType           type,
            Class<T>           clazz )
        throws
            MeshTypeWithIdentifierNotFoundException
    {
        if( clazz.isInstance( type )) {
            return (T) type;
        } else {
            Class<? extends MeshType> foundType = null;

            if( type instanceof EntityType ) {
                foundType = EntityType.class;
            } else if( type instanceof RelationshipType ) {
                foundType = RelationshipType.class;
            } else if( type instanceof SubjectArea ) {
                foundType = SubjectArea.class;
            } else if( type instanceof PropertyTypeGroup ) {
                foundType = PropertyTypeGroup.class;
            } else if( type instanceof ProjectedPropertyType ) {
                foundType = ProjectedPropertyType.class;
            } else if( type instanceof PropertyType ) {
                foundType = PropertyType.class;
            } else {
                log.error( "unexpected type: " + type );
            }
            throw new WrongMeshTypeException( identifier, foundType );
        }
    }

    /**
     * Check the integrity of a EntityType.
     *
     * @param candidate the candidate EntityType to check
     * @throws IllegalArgumentException if the EntityType is not valid
     */
    @Override
    public void checkEntityType(
            EntityType candidate )
        throws
            IllegalArgumentException
    {
        if( candidate.getIdentifier() == null ) {
            throw new IllegalArgumentException( "EntityType has no Identifier: " + candidate );
        }
        if( candidate.getName() == null ) {
            throw new IllegalArgumentException( "EntityType has no Name: " + candidate );
        }
        if( candidate.getUserVisibleName() == null ) {
            throw new IllegalArgumentException( "EntityType has no UserName: " + candidate );
        }
        if( candidate.getSubjectArea() == null ) {
            throw new IllegalArgumentException( "EntityType has no SubjectArea: " + candidate );
        }
        if( candidate.getIsAbstract() == null ) {
            throw new IllegalArgumentException( "EntityType has no IsAbstract: " + candidate );
        }
        if( candidate.getDirectSubtypes() == null ) {
            throw new IllegalArgumentException( "EntityType has no direct subtypes: " + candidate );
        }
        if( candidate.getDirectSupertypes() == null ) {
            throw new IllegalArgumentException( "EntityType has no direct supertypes: " + candidate );
        }
        if( candidate.getLocalPropertyTypeGroups() == null ) {
            throw new IllegalArgumentException( "EntityType has no local PropertyTypeGroups: " + candidate );
        }
        if( candidate.getLocalPropertyTypes() == null ) {
            throw new IllegalArgumentException( "EntityType has no local PropertyTypes: " + candidate );
        }
        if( candidate.getOverridingLocalPropertyTypes() == null ) {
            throw new IllegalArgumentException( "EntityType has no overriding PropertyTypes: " + candidate );
        }
        if( candidate.getIsSignificant() == null ) {
            throw new IllegalArgumentException( "EntityType has no IsSignificant: " + candidate );
        }
        if( candidate.getLocalRoleTypes() == null ) {
            throw new IllegalArgumentException( "EntityType has no local RoleTypes: " + candidate );
        }
        // check that getAllPropertyTypes does not produce an exception -- if it does, something is wrong
        candidate.getAllPropertyTypes();
    }

    /**
     * Check the integrity of a RelationshipType.
     *
     * @param candidate the candidate RelationshipType to check
     * @throws IllegalArgumentException if the RelationshipType is not valid
     */
    @Override
    public void checkRelationshipType(
            RelationshipType candidate )
        throws
            IllegalArgumentException
    {
        if( candidate.getIdentifier() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no Identifier: " + candidate );
        }
        if( candidate.getName() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no Name: " + candidate );
        }
        if( candidate.getUserVisibleName() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no UserName: " + candidate );
        }
        if( candidate.getSubjectArea() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no SubjectArea: " + candidate );
        }
        if( candidate.getIsAbstract() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no IsAbstract: " + candidate );
        }
        if( candidate.getDirectSubtypes() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no direct subtypes: " + candidate );
        }
        if( candidate.getDirectSupertypes() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no direct supertypes: " + candidate );
        }
        if( candidate.getLocalPropertyTypeGroups() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no local PropertyTypeGroups: " + candidate );
        }
        if( candidate.getLocalPropertyTypes() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no local PropertyTypes: " + candidate );
        }
        if( candidate.getOverridingLocalPropertyTypes() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no overriding PropertyTypes: " + candidate );
        }
        if( candidate.getSource() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no Source: " + candidate );
        }
        if( candidate.getDestination() == null ) {
            throw new IllegalArgumentException( "RelationshipType has no Destination: " + candidate );
        }
        // check that getAllPropertyTypes does not produce an exception -- if it does, something is wrong
        candidate.getAllPropertyTypes();
    }

    /**
     * Check the integrity of a PropertyType.
     *
     * @param candidate the candidate PropertyType to check
     * @throws IllegalArgumentException if the PropertyType is not valid
     */
    @Override
    public void checkPropertyType(
            PropertyType candidate )
        throws
            IllegalArgumentException
    {
        if( candidate.getIdentifier() == null ) {
            throw new IllegalArgumentException( "PropertyType has no Identifier: " + candidate );
        }
        if( candidate.getName() == null ) {
            throw new IllegalArgumentException( "PropertyType has no Name: " + candidate );
        }
        if( candidate.getUserVisibleName() == null ) {
            throw new IllegalArgumentException( "PropertyType has no UserName: " + candidate );
        }
        if( candidate.getSubjectArea() == null ) {
            throw new IllegalArgumentException( "PropertyType has no SubjectArea: " + candidate );
        }
        if( candidate.getDataType() == null ) {
            throw new IllegalArgumentException( "PropertyType has no DataType: " + candidate );
        }
        if( candidate.getIsOptional() == null ) {
            throw new IllegalArgumentException( "PropertyType has no IsOptional: " + candidate );
        }
        if( candidate.getIsReadOnly() == null ) {
            throw new IllegalArgumentException( "PropertyType has no IsReadOnly: " + candidate );
        }
        if( candidate.getAttributableMeshType() == null ) {
            throw new IllegalArgumentException( "PropertyType has no AttributableMeshType: " + candidate );
        }
        if( candidate.getSequenceNumber() == null ) {
            throw new IllegalArgumentException( "PropertyType has no SequenceNumber: " + candidate );
        }
        // FIXME: check datatype wrt overridden PropertyTypes and default value
    }

    /**
     * Check the integrity of a PropertyTypeGroup.
     *
     * @param candidate the candidate PropertyTypeGroup to check
     * @throws IllegalArgumentException if the PropertyTypeGroup is not valid
     */
    @Override
    public void checkPropertyTypeGroup(
            PropertyTypeGroup candidate )
        throws
            IllegalArgumentException
    {
        if( candidate.getIdentifier() == null ) {
            throw new IllegalArgumentException( "PropertyTypeGroup has no Identifier: " + candidate );
        }
        if( candidate.getName() == null ) {
            throw new IllegalArgumentException( "PropertyTypeGroup has no Name: " + candidate );
        }
        if( candidate.getUserVisibleName() == null ) {
            throw new IllegalArgumentException( "PropertyTypeGroup has no UserName: " + candidate );
        }
        if( candidate.getContainedPropertyTypes() == null ) {
            throw new IllegalArgumentException( "PropertyTypeGroup contains no PropertyTypes: " + candidate );
        }
        if( candidate.getSequenceNumber() == null ) {
            throw new IllegalArgumentException( "PropertyTypeGroup has no SequenceNumber: " + candidate );
        }
    }

    /**
     * Check the integrity of a SubjectArea.
     *
     * @param candidate the candidate SubjectArea to check
     * @throws IllegalArgumentException if the SubjectArea is not valid
     */
    @Override
    public void checkSubjectArea(
            SubjectArea candidate )
        throws
            IllegalArgumentException
    {
        if( candidate.getIdentifier() == null ) {
            throw new IllegalArgumentException( "SubjectArea has no Identifier: " + candidate );
        }
        if( candidate.getName() == null ) {
            throw new IllegalArgumentException( "SubjectArea has no Name: " + candidate );
        }
        if( candidate.getUserVisibleName() == null ) {
            throw new IllegalArgumentException( "SubjectArea has no UserName: " + candidate );
        }
        if( candidate.getCollectableMeshTypes() == null ) {
            throw new IllegalArgumentException( "SubjectArea has no CollectableMeshTypes: " + candidate );
        }
    }

    /**
      * We are told we are not needed any more. Clean up and release all resources.
      */
    @Override
    public void die()
    {
    }

    /**
      * The life cycle manager -- allocated smartly.
      */
    protected transient MMeshTypeLifecycleManager theLifecycleManager;

    /**
     * The identifier factory.
     */
    protected MMeshTypeIdentifierFactory theMeshTypeIdentifierFactory;

    /**
      * The actual storage.
      */
    protected MMeshTypeStore theCluster = new MMeshTypeStore( this );

    /**
     * Dictionary of synonyms.
     */
    protected MMeshTypeSynonymDictionary theSynonymDictionary;
}
