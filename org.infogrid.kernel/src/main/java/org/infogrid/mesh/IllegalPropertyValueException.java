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

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
  * This Exception is thrown when a PropertyValue with an incorrect DataType is
  * specified as the new value of a property, or when the new value of the
  * property is outside of its allowed domain.
  */
public class IllegalPropertyValueException
        extends
            IllegalOperationTypeException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param obj the MeshObject on which the violation was discovered, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject on which the violation was discovered
     * @param pt the PropertyType whose value was attempted to be set, if available
     * @param typeIdentifier the MeshTypeIdentifier for the PropertyType whose value was attempted to be set
     * @param illegalValue the value for the PropertyType that was illegal
     */
    public IllegalPropertyValueException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            PropertyType         pt,
            MeshTypeIdentifier   typeIdentifier,
            PropertyValue        illegalValue )
    {
        super( mb, originatingMeshBaseIdentifier, obj, identifier );
        
        if( typeIdentifier == null ) {
            throw new IllegalArgumentException( "typeIdentifier must not be null" );
        }
        thePropertyType           = pt;
        thePropertyTypeIdentifier = typeIdentifier;
        theIllegalValue           = illegalValue;
    }

    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject on which the violation was discovered, if available
     * @param pt the PropertyType whose value was attempted to be set, if available
     * @param illegalValue the value for the PropertyType that was illegal
     */
    public IllegalPropertyValueException(
            MeshObject           obj,
            PropertyType         pt,
            PropertyValue        illegalValue )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                pt,
                pt.getIdentifier(),
                illegalValue );
    }

    /**
     * Obtain the PropertyType whose value was attempted to be set
     * 
     * @return the PropertyType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the PropertyType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType which is not a PropertyType
     */
    public synchronized PropertyType getPropertyType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( thePropertyType == null ) {
            thePropertyType = (PropertyType) resolve( thePropertyTypeIdentifier );
        }
        return thePropertyType;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "resolvingMeshBase",
                    "meshObject",
                    "meshObjectIdentifier",
                    "propertyType",
                    "propertyTypeIdentifier",
                    "illegalValue",
                    "types"
                },
                new Object[] {
                    theResolvingMeshBase,
                    theMeshObject,
                    theMeshObjectIdentifier,
                    thePropertyType,
                    thePropertyTypeIdentifier,
                    theIllegalValue,
                    MeshTypeUtils.meshTypeIdentifiersOrNull( theMeshObject )
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, thePropertyTypeIdentifier, theIllegalValue };
    }

    /**
     * The PropertyType that was illegal on the MeshObject.
     */
    protected transient PropertyType thePropertyType;
    
    /**
     * The identifier of the PropertyType that was illegal on the MeshObject.
     */
    protected MeshTypeIdentifier thePropertyTypeIdentifier;
    
    /**
     * The illegal PropertyValue.
     */
    protected PropertyValue theIllegalValue;
}
