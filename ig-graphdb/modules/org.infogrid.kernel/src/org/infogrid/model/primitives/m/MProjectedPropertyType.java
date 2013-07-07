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

import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.traversal.TraversalToPropertySpecification;

/**
  * A ProjectedPropertyType is a PropertyType whose value is automatically calculated rather
  * than set.
  * In-memory implementation.
  */
public final class MProjectedPropertyType
        extends
            MPropertyType
        implements
            ProjectedPropertyType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MProjectedPropertyType(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
     * Set the local TraversalToPropertySpecifications that we are watching. This shall only
     * be invoked during instantiation of a ProjectedPropertyType; never at run-time.
     *
     * @param newValue the new value of this property
     * @see #getInputProperties
     */
    public final void setInputPropertiesDuringParsing(
            TraversalToPropertySpecification [] newValue )
    {
        theInputPropertyInstances = newValue;
    }

    /**
     * Obtain the local TraversalToPropertySpecifications that we are watching.
     *
     * @return the local TraversalToPropertySpecifications that we are watching
     * @see #setInputPropertiesDuringParsing
     */
    @Override
    public final TraversalToPropertySpecification [] getInputProperties()
    {
        return theInputPropertyInstances;
    }

    /**
     * Set the projection code for this ProjectedPropertyType that is in-lined by the code generator.
     *
     * @param newValue the new value of this property
     * @see #getProjectionCode
     */
    public final void setProjectionCode(
            StringValue newValue )
    {
        this.theProjectionCode = newValue;
    }

    /**
     * Obtain the projection code for this ProjectedPropertyType that is in-lined by the code generator.
     *
     * @return the Java text containing the projection code for this ProjectedPropertyType
     * @see #setProjectionCode
     */
    @Override
    public final StringValue getProjectionCode()
    {
        return theProjectionCode;
    }

    /**
     * The TraversalToPropertySpecifications to our input PropertyTypes.
     */
    private TraversalToPropertySpecification [] theInputPropertyInstances;

    /**
     * The recalculate code. This is transient because we don't need to
     * carry this around beyond code generation.
     */
    private transient StringValue theProjectionCode;
}
