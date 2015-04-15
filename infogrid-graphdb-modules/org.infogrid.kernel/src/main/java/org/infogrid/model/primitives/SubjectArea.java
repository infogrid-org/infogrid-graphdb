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

package org.infogrid.model.primitives;

/**
  * SubjectAreas are the packaging construct for the model. They are similar
  * to the concept of a package in UML, or in Java. SubjectAreas have names
  * using the reverse DNS name convention (same as in Java), and version numbers.
  * Any SubjectArea depends on zero or more SubjectAreas.
  */
public interface SubjectArea
        extends
            MeshType
{
    /**
      * Obtain the VersionNumber property.
      *
      * @return the version number property
      */
    public StringValue getVersionNumber();

    /**
     * Obtain the set of SubjectAreas that this SubjectArea depends on.
     *
     * @return the set of SubjectAreas that this SubjectArea depends on
     */
    public SubjectArea [] getSubjectAreaDependencies();

    /**
      * Obtain the CollectableMeshTypes collected by this SubjectArea. This
      * is the method by which the "content" of this SubjectArea can be determined.
      *
      * @return the CollectableMeshTypes in this SubjectArea
      */
    public CollectableMeshType [] getCollectableMeshTypes();

    /**
     * Convenience method to obtain the EntityTypes collected by this SubjectArea.
     *
     * @return the EntityTypes in this SubjectArea.
     */
    public EntityType [] getEntityTypes();
    
    /**
     * Convenience method to obtain the RelationshipType collected by this SubjectArea.
     *
     * @return the RelationshipType in this SubjectArea.
     */
    public RelationshipType [] getRelationshipTypes();
    
    /**
      * Obtain the canonical name of this SubjectArea including version number.
      *
      * @return the canonical name of this SubjectArea
      */
    public StringValue getCanonicalName();

    /**
     * Obtain a ClassLoader that can load the appropriate Java classes to instantiate
     * the AttributableMeshTypes in this SubjectArea.
     *
     * @return the ClassLoader for this SubjectArea
     */
    public ClassLoader getClassLoader();

    /**
     * Find a CollectableMeshType in this SubjectArea by name.
     *
     * @param name the name of the CollectableMeshType
     * @return the found CollectableMeshType, or null if not found
     */
    public CollectableMeshType findCollectableMeshTypeByName(
            StringValue name );
}
