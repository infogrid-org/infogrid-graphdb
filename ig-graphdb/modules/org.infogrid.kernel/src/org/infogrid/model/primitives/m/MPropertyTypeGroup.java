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
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
  * A PropertyTypeGroup is a group of PropertyTypes, defined by the same AttributableMeshObjectType
  * (or its ancestors in the inheritance hierarchy) that logically belong together.
  * In-memory implementation.
  */
public final class MPropertyTypeGroup
        extends
            MPropertyTypeOrGroup
        implements
            PropertyTypeGroup,
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MPropertyTypeGroup(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
     * Obtain the PropertyTypes that are members of this PropertyTypeGroup.
     *
     * @return the PropertyTypes that are members of this PropertyTypeGroup
     * @see #setContainedPropertyTypes
     */
    @Override
    public final PropertyType [] getContainedPropertyTypes()
    {
        return thePropertyTypes;
    }

    /**
     * Set the PropertyTypes that are members of this PropertyTypeGroup.
     *
     * @param pts the PropertyTypes that are members of this PropertyTypeGroup.
     * @see #getContainedPropertyTypes
     */
    public final void setContainedPropertyTypes(
            PropertyType [] pts )
    {
        thePropertyTypes = pts;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "id",
                    "name"
                },
                new Object[] {
                    getIdentifier().toExternalForm(),
                    getName().value()
                });
    }

    /**
     * The member PropertyTypes of this PropertyTypeGroup.
     */
    private PropertyType [] thePropertyTypes;
}
