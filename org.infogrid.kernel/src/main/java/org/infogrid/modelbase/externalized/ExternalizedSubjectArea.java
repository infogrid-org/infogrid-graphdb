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

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.model.primitives.StringValue;

/**
 * This is data wanting to become a SubjectArea, during reading.
 */
public class ExternalizedSubjectArea
    extends
        ExternalizedMeshType
{
    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addEntityType(
            ExternalizedEntityType newValue ) 
    {
        entityTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedEntityType> getEntityTypes()
    {
        return entityTypes;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addRelationshipType(
            ExternalizedRelationshipType newValue ) 
    {
        relationshipTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedRelationshipType> getRelationshipTypes()
    {
        return relationshipTypes;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addSubjectAreaDependency(
            ExternalizedSubjectAreaDependency newValue ) 
    {
        subjectAreaDependencies.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedSubjectAreaDependency> getSubjectAreaDependencies()
    {
        return subjectAreaDependencies;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addModuleRequirement(
            ExternalizedModuleRequirement newValue ) 
    {
        moduleRequirements.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<ExternalizedModuleRequirement> getModuleRequirements()
    {
        return moduleRequirements;
    }

    /**
     * List of ExternalizedEntityTypes, as they are being read in.
     */
    protected ArrayList<ExternalizedEntityType> entityTypes = new ArrayList<ExternalizedEntityType>();

    /**
     * List of ExternalizedRelationshipTypes, as they are being read in.
     */
    protected ArrayList<ExternalizedRelationshipType> relationshipTypes = new ArrayList<ExternalizedRelationshipType>();

    /**
     * List of SubjectAreas that we are dependent on, as they are being read in.
     */
    protected ArrayList<ExternalizedSubjectAreaDependency> subjectAreaDependencies = new ArrayList<ExternalizedSubjectAreaDependency>();
    
    /**
     * List of Modules, beyond the SubjectAreas, that we are dependent on, as they are being read in.
     */
    protected ArrayList<ExternalizedModuleRequirement> moduleRequirements = new ArrayList<ExternalizedModuleRequirement>();
}
