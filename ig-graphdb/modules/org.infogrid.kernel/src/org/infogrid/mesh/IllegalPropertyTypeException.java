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

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
  * This Exception is thrown when there is an attempt to access
  * a property that cannot exist on this MeshObject because the MeshObject
  * has not been blessed with a MeshType that provides this PropertyType.
  */
public class IllegalPropertyTypeException
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
     * @param obj the MeshObject that did not have the PropertyType, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject that did not have the PropertyType
     * @param pt the PropertyType that was illegal on this MeshObject, if available
     * @param typeIdentifier the MeshTypeIdentifier for the PropertyType that was illegal on this MeshObject
     */
    public IllegalPropertyTypeException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            PropertyType         pt,
            MeshTypeIdentifier   typeIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, obj, identifier );
        
        if( typeIdentifier == null ) {
            throw new IllegalArgumentException( "typeIdentifier must not be null" );
        }
        thePropertyType           = pt;
        thePropertyTypeIdentifier = typeIdentifier;
    }

   /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject that did not have the PropertyType, if available
     * @param pt the PropertyType that was illegal on this MeshObject, if available
     */
    public IllegalPropertyTypeException(
            MeshObject           obj,
            PropertyType         pt )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                pt,
                pt.getIdentifier() );
    }

    /**
     * Obtain the PropertyType that identified a non-existing Property.
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
                    "types"
                },
                new Object[] {
                    theResolvingMeshBase,
                    theMeshObject,
                    theMeshObjectIdentifier,
                    thePropertyType,
                    thePropertyTypeIdentifier,
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
        EntityType []         types   = theMeshObject.getTypes();
        MeshTypeIdentifier [] typeIds = new MeshTypeIdentifier[ types.length ];
        
        for( int i=0 ; i<types.length ; ++i ) {
            typeIds[i] = types[i].getIdentifier();
        }
        
        return new Object[] { theMeshObjectIdentifier, thePropertyTypeIdentifier, typeIds };
    }

    /**
     * The PropertyType that was illegal on the MeshObject.
     */
    protected transient PropertyType thePropertyType;
    
    /**
     * The identifier of the PropertyType that was illegal on the MeshObject.
     */
    protected MeshTypeIdentifier thePropertyTypeIdentifier;
}
