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

import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyTypeOrGroup;

/**
  * This is the implementation of something that is either a PropertyType or
  * a PropertyTypeGroup.
  * In-memory implementation.
  */
public class MPropertyTypeOrGroup
        extends
            MCollectableMeshType
        implements
            PropertyTypeOrGroup
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MPropertyTypeOrGroup(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
      * Set the AttributableMeshType on which this PropertyTypeOrGroup is defined.
      *
      * @param newValue the new value of the property
      * @see #getAttributableMeshType
      */
    public final void setAttributableMeshType(
            AttributableMeshType newValue )
    {
        this.theAttributableMeshType = newValue;
    }

    /**
      * Obtain the AttributableMeshType on which this PropertyTypeOrGroup is defined.
      *
      * @return the AttributableMeshType on which this PropertyTypeOrGroup is defined
      * @see #setAttributableMeshType
      */
    @Override
    public final AttributableMeshType getAttributableMeshType()
    {
        return theAttributableMeshType;
    }

    /**
     * Set the value of the SequenceNumber property.
     *
     * @param newValue the new value of the SequenceNumber property
     * @see #getSequenceNumber
     */
    public final void setSequenceNumber(
            FloatValue newValue )
    {
        theSequenceNumber = newValue;
    }

    /**
     * Obtain the value of the SequenceNumber property.
     *
     * @return the value of the SequenceNumber property
     * @see #setSequenceNumber
     */
    @Override
    public final FloatValue getSequenceNumber()
    {
        return theSequenceNumber;
    }

    /**
      * The AttributableMeshType that this PropertyTypeOrGroup is defined in.
      */
    private AttributableMeshType theAttributableMeshType;

    /**
     * The value of the SequenceNumber property.
     */
    private FloatValue theSequenceNumber;
}
