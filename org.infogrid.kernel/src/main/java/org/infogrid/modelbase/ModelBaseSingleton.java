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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.primitives.UnknownEnumeratedValueException;
import org.infogrid.modelbase.externalized.xml.XmlModelLoader;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.util.logging.Log;

/**
 * This class helps us to find the singleton instance of (an implementation of)
 * ModelBase. It also provides convenience methods for the code generator.
 */
public abstract class ModelBaseSingleton
{
    private static final Log log = Log.getLogInstance( ModelBaseSingleton.class ); // our own, private logger
    
    /**
     * Obtain the singleton instance.
     *
     * @return the singleton instance
     */
    public static synchronized ModelBase getSingleton()
    {
        if( theSingleton == null ) {
            theSingleton = MModelBase.create();
        }
        return theSingleton;
    }
    
    /**
     * Load a model from this stream.
     *
     * @param xmlResource the name of the XML resource, for error messages
     * @param stream the InputStream from which to load
     * @param classLoader the ClassLoader to use
     * @return the SubjectAreas that were loaded
     * @throws MeshTypeNotFoundException thrown if a required MeshType was not found (e.g. when a subtype was looking for its non-existing supertype)
     * @throws InheritanceConflictException thrown if the specified PropertyType overriding was inconsistent with the model
     * @throws IOException thrown if an I/O error occurred during reading
     * @throws UnknownEnumeratedValueException thrown if an invalid EnumeratedValue was specified
     */
    public static SubjectArea [] loadModel(
            String         xmlResource,
            InputStream    stream,
            ClassLoader    classLoader )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException,
            UnknownEnumeratedValueException
    {
        return loadModel( xmlResource, stream, classLoader, null );
    }
    
    /**
     * Load a model from this stream.
     *
     * @param xmlResource the name of the XML resource, for error messages
     * @param stream the InputStream from which to load
     * @param classLoader the ClassLoader to use
     * @param now the time the current run was started
     * @return the SubjectAreas that were loaded
     * @throws MeshTypeNotFoundException thrown if a required MeshType was not found (e.g. when a subtype was looking for its non-existing supertype)
     * @throws InheritanceConflictException thrown if the specified PropertyType overriding was inconsistent with the model
     * @throws IOException thrown if an I/O error occurred during reading
     * @throws UnknownEnumeratedValueException thrown if an invalid EnumeratedValue was specified
     */
    public static SubjectArea [] loadModel(
            String         xmlResource,
            InputStream    stream,
            ClassLoader    classLoader,
            TimeStampValue now )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException,
            UnknownEnumeratedValueException
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        if( now == null ) {
            now = TimeStampValue.now();
        }

        ModelLoader theLoader = new XmlModelLoader(
                            theSingleton,
                            stream,
                            classLoader, // FIXME?
                            classLoader,
                            xmlResource + ": " );
        SubjectArea [] ret = theLoader.loadAndCheckModel( theSingleton.getMeshTypeLifecycleManager(), now );
        return ret;
    }

    /**
     * Helper method to find a SubjectArea in the ModelBase.
     * 
     * @param identifierAsString the Identifier of the SubjectArea, as String for convenience
     * @return the SubjectArea
     */
    public static SubjectArea findSubjectArea(
            String identifierAsString )
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        MeshTypeIdentifier identifier = theSingleton.getMeshTypeIdentifierFactory().fromExternalForm( identifierAsString );
        
        try {
            SubjectArea ret = theSingleton.findSubjectAreaByIdentifier( identifier );
            return ret;

        } catch( MeshTypeNotFoundException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Helper method to find an EntityType in the ModelBase.
     * 
     * @param identifierAsString the Identifier of the EntityType, as String for convenience
     * @return the EntityType
     */
    public static EntityType findEntityType(
            String identifierAsString )
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        MeshTypeIdentifier identifier = theSingleton.getMeshTypeIdentifierFactory().fromExternalForm( identifierAsString );
        
        try {
            EntityType ret = theSingleton.findEntityTypeByIdentifier( identifier );
            return ret;

        } catch( MeshTypeNotFoundException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Helper method to find a RelationshipType in the ModelBase.
     * 
     * @param identifierAsString the Identifier of the RelationshipType, as String for convenience
     * @return the RelationshipType
     */
    public static RelationshipType findRelationshipType(
            String identifierAsString )
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        MeshTypeIdentifier identifier = theSingleton.getMeshTypeIdentifierFactory().fromExternalForm( identifierAsString );
        
        try {
            RelationshipType ret = theSingleton.findRelationshipTypeByIdentifier( identifier );
            return ret;

        } catch( MeshTypeNotFoundException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Helper method to find a PropertyType in the ModelBase.
     * 
     * @param identifierAsString the Identifier of the PropertyType, as String for convenience
     * @return the PropertyType
     */
    public static PropertyType findPropertyType(
            String identifierAsString )
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        MeshTypeIdentifier identifier = theSingleton.getMeshTypeIdentifierFactory().fromExternalForm( identifierAsString );
        
        try {
            PropertyType ret = theSingleton.findPropertyTypeByIdentifier( identifier );
            return ret;

        } catch( MeshTypeNotFoundException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Helper method to find a RoleType in the ModelBase.
     * 
     * @param identifierAsString the Identifier of the RoleType, as String for convenience
     * @return the RoleType
     */
    public static RoleType findRoleType(
            String identifierAsString )
    {
        if( theSingleton == null ) {
            throw new IllegalStateException( "no singleton ModelBase has been set" );
        }
        MeshTypeIdentifier identifier = theSingleton.getMeshTypeIdentifierFactory().fromExternalForm( identifierAsString );
        
        try {
            RoleType ret = theSingleton.findRoleTypeByIdentifier( identifier );
            return ret;

        } catch( MeshTypeNotFoundException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * The singleton instance of this class.
     */
    private static ModelBase theSingleton = MModelBase.create();
}
