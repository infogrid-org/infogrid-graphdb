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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.m;

import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.SubjectArea;

/**
  * A MeshType that is defined in a SubjectArea.
  * In-memory implementation.
  */
public abstract class MCollectableMeshType
        extends
            MMeshType
        implements
            CollectableMeshType
{
    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MCollectableMeshType(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
      * Set the SubjectArea property.
      *
      * @param newValue the new value for the property
      * @see #getSubjectArea
      */
    public final void setSubjectArea(
            SubjectArea newValue )
    {
        this.theSubjectArea = newValue;
    }

    /**
      * Obtain the value of the SubjectArea property.
      *
      * @return the value of the SubjectArea property
      * @see #setSubjectArea
      */
    public final SubjectArea getSubjectArea()
    {
        return theSubjectArea;
    }

    /**
      * The SubjectArea in which this CollectableMeshType is defined.
      */
    private SubjectArea theSubjectArea;
}
