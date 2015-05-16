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

package org.infogrid.model.primitives;

/**
  * A MeshType that has a source and a destination, that can participate in
  * inheritance relationships, and may be attributed. This is often called Association
  * (or MetaAssociation), Relationship or Relation in the literature.
  */
public interface RelationshipType
        extends
            AttributableMeshType
{
    /**
      * Obtain the source RoleType of this RelationshipType.
      *
      * @return the source RoleType of this RelationshipType
      */
    public RoleType getSource();

    /**
      * Obtain the destination RoleType of this RelationshipType.
      *
      * @return the destination RoleType of this RelationshipType
      */
    public RoleType getDestination();
}
