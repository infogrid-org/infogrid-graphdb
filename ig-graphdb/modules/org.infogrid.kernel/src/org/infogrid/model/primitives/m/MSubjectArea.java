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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.m;

import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.module.ModuleRequirement;
import org.infogrid.util.ArrayHelper;

/**
  * The packaging construct for Models.
  * In-memory implementation.
  */
public final class MSubjectArea
        extends
            MMeshType
        implements
            SubjectArea
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MSubjectArea(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
      * Set the VersionNumber property.
      *
      * @param newValue the new value of the property
      * @see #getVersionNumber
      */
    public final void setVersionNumber(
            StringValue newValue )
    {
        this.theVersionNumber = newValue;
    }

    /**
      * Obtain the VersionNumber property.
      *
      * @return the version number property
      * @see #setVersionNumber
      */
    @Override
    public final StringValue getVersionNumber()
    {
        return theVersionNumber;
    }

    /**
     * Obtain the set of SubjectAreas that this SubjectArea depends on.
     *
     * @return the set of SubjectAreas that this SubjectArea depends on
     * @see #setSubjectAreaDependencies
     */
    @Override
    public final SubjectArea [] getSubjectAreaDependencies()
    {
        return theSubjectAreaDependencies;
    }

    /**
     * Set the set of SubjectAreas that this SubjectArea depends on.
     *
     * @param newValue the set of SubjectAreas that this SubjectArea depends on
     * @see #getSubjectAreaDependencies
     */
    public final void setSubjectAreaDependencies(
            SubjectArea [] newValue )
    {
        theSubjectAreaDependencies = newValue;
    }

    /**
     * Obtain the set of Modules that this SubjectArea depends on.
     *
     * @return the set of Modules that this SubjectArea depends on
     * @see #setModuleRequirements
     */
    @Override
    public final ModuleRequirement [] getModuleRequirements()
    {
        return theModuleRequirements;
    }

    /**
     * Set the set of Modules that this SubjectArea depends on.
     *
     * @param newValue the set of Modules that this SubjectArea depends on
     * @see #getModuleRequirements
     */
    public final void setModuleRequirements(
            ModuleRequirement [] newValue )
    {
        theModuleRequirements = newValue;
    }

    /**
      * Obtain the CollectableMeshType collected by this SubjectArea. This
      * is the method by which the "content" of this SubjectArea can be determined.
      *
      * @return the CollectableMeshTypes in this SubjectArea
      */
    @Override
    public final CollectableMeshType [] getCollectableMeshTypes()
    {
        return theCollectableMeshTypes;
    }

    /**
     * Convenience method to obtain the EntityTypes collected by this SubjectArea.
     *
     * @return the EntityTypes in this SubjectArea.
     */
    @Override
    public EntityType [] getEntityTypes()
    {
        int count = 0;
        for( int i=theCollectableMeshTypes.length-1 ; i>=0 ; --i ) {
            if( theCollectableMeshTypes[i] instanceof EntityType ) {
                ++count;
            }
        }
        EntityType [] ret = new EntityType[ count ];
        for( int i=theCollectableMeshTypes.length-1 ; i>=0 ; --i ) {
            if( theCollectableMeshTypes[i] instanceof EntityType ) {
                ret[--count] = (EntityType) theCollectableMeshTypes[i];
            }
        }
        return ret;
    }
    
    /**
     * Convenience method to obtain the RelationshipType collected by this SubjectArea.
     *
     * @return the RelationshipType in this SubjectArea.
     */
    @Override
    public RelationshipType [] getRelationshipTypes()
    {
        int count = 0;
        for( int i=theCollectableMeshTypes.length-1 ; i>=0 ; --i ) {
            if( theCollectableMeshTypes[i] instanceof RelationshipType ) {
                ++count;
            }
        }
        RelationshipType [] ret = new RelationshipType[ count ];
        for( int i=theCollectableMeshTypes.length-1 ; i>=0 ; --i ) {
            if( theCollectableMeshTypes[i] instanceof RelationshipType ) {
                ret[--count] = (RelationshipType) theCollectableMeshTypes[i];
            }
        }
        return ret;
    }
    
    /**
      * Obtain the canonical name of this SubjectArea including version number.
      *
      * @return the canonical name of this SubjectArea
      */
    @Override
    public final StringValue getCanonicalName()
    {
        StringBuilder buf = new StringBuilder( 48 );
        buf.append( getName().value() );
        buf.append( ".V" );
        if( theVersionNumber != null ) {
            buf.append( theVersionNumber.value() );
        }
        return StringValue.create( buf.toString() );
    }

    /**
     * Set the ClassLoader that can load the appropriate Java classes to instantiate
     * the AttributableMeshTypes in this SubjectArea.
     *
     * @param newValue the new value of this property
     * @see #getClassLoader
     */
    public final void setClassLoader(
            ClassLoader newValue )
    {
        theClassLoader = newValue;
    }

    /**
     * Obtain a ClassLoader that can load the appropriate Java classes to instantiate
     * the AttributableMeshTypes in this SubjectArea.
     *
     * @return the ClassLoader for this SubjectArea
     * @see #setClassLoader
     */
    @Override
    public final ClassLoader getClassLoader()
    {
        return theClassLoader;
    }


    /**
     * Find a CollectableMeshType in this SubjectArea by name.
     *
     * @param name the name of the CollectableMeshType
     * @return the found CollectableMeshType, or null if not found
     */
    @Override
    public CollectableMeshType findCollectableMeshTypeByName(
            StringValue name )
    {
        // FIXME efficiency
        for( int i=0 ; i<theCollectableMeshTypes.length ; ++i ) {
            if( name.equals( theCollectableMeshTypes[i].getName() )) {
                return theCollectableMeshTypes[i];
            }
        }
        return null;
    }

    /**
     * Convert to String form, for debugging.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        return getCanonicalName().value();
    }

    /**
      * Add a CollectableMeshType to this SubjectArea.
      *
      * @param cmo the CollectableMeshType to add to this SubjectArea
      */
    public final void addCollectableMeshType(
            CollectableMeshType cmo )
    {
        for( int i=0 ; i<theCollectableMeshTypes.length ; ++i ) {
            if( theCollectableMeshTypes[i].equals( cmo )) {
                return;
            }
        }

        theCollectableMeshTypes = ArrayHelper.append(
                theCollectableMeshTypes,
                cmo,
                CollectableMeshType.class );
    }

    /**
      * Value of the VersionNumber property.
      */
    private StringValue theVersionNumber;

    /**
      * Contained CollectableMeshTypes.
      */
    private CollectableMeshType [] theCollectableMeshTypes = new CollectableMeshType[0];

    /**
     * The SubjectAreas that this SubjectArea depends on.
     */
    private SubjectArea [] theSubjectAreaDependencies = new SubjectArea[0];

    /**
     * The Modules that this SubjectArea depends on.
     */
    private ModuleRequirement [] theModuleRequirements = new ModuleRequirement[0];

    /**
     * The ClassLoader that we use in order to load the AttributableMeshTypes in this SubjectArea.
     */
    private transient ClassLoader theClassLoader;
}
