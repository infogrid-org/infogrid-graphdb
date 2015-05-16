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

package org.infogrid.mesh.externalized;

import java.io.Serializable;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * This implementation of ExternalizedMeshObject is fully initialized in the
 * factory method.
 */
public class SimpleExternalizedMeshObject
        extends
            AbstractExternalizedMeshObject
        implements
            CanBeDumped,
            Serializable
{
    private static final long serialVersionUID = 1L; // help with serialization

    /**
     * Factory method.
     * 
     * @param identifier the MeshObjectIdentifier of the MeshObject
     * @param typeNames the MeshTypeIdentifier identifying the EntityTypes with which the MeshObject is currently blessed
     * @param timeCreated the time the MeshObject was created
     * @param timeUpdated the time the MeshObject was last updated
     * @param timeRead the time the MeshObject was last read
     * @param timeExpires the time the MeshObject will expire
     * @param propertyTypes the current PropertyTypes of the MeshObject, in the same sequence as propertyValues
     * @param propertyValues the current values of the PropertyTypes, in the same sequence as propertyTypes
     * @param neighbors the MeshObjectIdentifiers of the directly related MeshObjects
     * @param roleTypes the MeshTypeIdentifiers of the RoleTypes applicable to the neighbors, in the same sequence
     * @param equivalents the MeshObjectIdentifiers of the current left and right equivalent MeshObject, if any
     * @return the created SimpleExternalizedMeshObject
     */
    public static SimpleExternalizedMeshObject create(
            MeshObjectIdentifier    identifier,
            MeshTypeIdentifier []   typeNames,
            long                    timeCreated,
            long                    timeUpdated,
            long                    timeRead,
            long                    timeExpires,
            MeshTypeIdentifier []   propertyTypes,
            PropertyValue []        propertyValues,
            MeshObjectIdentifier [] neighbors,
            MeshTypeIdentifier [][] roleTypes,
            MeshObjectIdentifier [] equivalents )
    {
        // do some sanity checking
        if( identifier == null ) {
            throw new IllegalArgumentException( "null Identifier" );
        }
        if( typeNames != null ) {
            for( MeshTypeIdentifier current : typeNames ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null typeName" );
                }
            }
        } else {
            typeNames = new MeshTypeIdentifier[0];
        }
        if( propertyTypes != null ) {
            for( MeshTypeIdentifier current : propertyTypes ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null PropertyType" );
                }
            }
        } else {
            propertyTypes = new MeshTypeIdentifier[0];
        }
        if( propertyValues == null ) {
            propertyValues = new PropertyValue[0];
        }
        if( neighbors != null ) {
            for( MeshObjectIdentifier current : neighbors ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null neighbor" );
                }
            }
        } else {
            neighbors = new MeshObjectIdentifier[0];
            roleTypes = new MeshTypeIdentifier[0][];
        }
        
        if( equivalents != null ) {
            for( MeshObjectIdentifier current : equivalents ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null equivalent" );
                }
            }
        } else {
            equivalents = new MeshObjectIdentifier[0];
        }
        
        SimpleExternalizedMeshObject ret = new SimpleExternalizedMeshObject(
                identifier,
                typeNames,
                timeCreated,
                timeUpdated,
                timeRead,
                timeExpires,
                propertyTypes,
                propertyValues,
                neighbors,
                roleTypes,
                equivalents );

        return ret;
    }
        
    /**
     * Construct one from externalized data.
     *
     * @param identifier the MeshObjectIdentifier of the MeshObject
     * @param typeNames the MeshTypeIdentifier identifying the EntityTypes with which the MeshObject is currently blessed
     * @param timeCreated the time the MeshObject was created
     * @param timeUpdated the time the MeshObject was last updated
     * @param timeRead the time the MeshObject was last read
     * @param timeExpires the time the MeshObject will expire
     * @param propertyTypes the current PropertyTypes of the MeshObject, in the same sequence as propertyValues
     * @param propertyValues the current values of the PropertyTypes, in the same sequence as propertyTypes
     * @param neighbors the MeshObjectIdentifiers of the directly related MeshObjects
     * @param roleTypes the MeshTypeIdentifiers of the RoleTypes applicable to the neighbors, in the same sequence
     * @param equivalents the MeshObjectIdentifiers of the current left and right equivalent MeshObject, if any
     */
    protected SimpleExternalizedMeshObject(
            MeshObjectIdentifier    identifier,
            MeshTypeIdentifier []   typeNames,
            long                    timeCreated,
            long                    timeUpdated,
            long                    timeRead,
            long                    timeExpires,
            MeshTypeIdentifier []   propertyTypes,
            PropertyValue  []       propertyValues,
            MeshObjectIdentifier[]  neighbors,
            MeshTypeIdentifier [][] roleTypes,
            MeshObjectIdentifier[]  equivalents )
    {
        super( identifier, timeCreated, timeUpdated, timeRead, timeExpires );

        // do some sanity checking
        if( identifier == null ) {
            throw new IllegalArgumentException( "null Identifier" );
        }
        if( typeNames != null ) {
            for( MeshTypeIdentifier current : typeNames ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null typeName" );
                }
            }
        } else {
            typeNames = new MeshTypeIdentifier[0];
        }
        if( propertyTypes != null ) {
            for( MeshTypeIdentifier current : propertyTypes ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null PropertyType" );
                }
            }
        } else {
            propertyTypes = new MeshTypeIdentifier[0];
        }
        if( propertyValues == null ) {
            propertyValues = new PropertyValue[0];
        }
        if( neighbors != null ) {
            for( MeshObjectIdentifier current : neighbors ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null neighbor" );
                }
            }
        } else {
            neighbors = new MeshObjectIdentifier[0];
            roleTypes = new MeshTypeIdentifier[0][];
        }
        
        if( equivalents != null ) {
            for( MeshObjectIdentifier current : equivalents ) {
                if( current == null ) {
                    throw new IllegalArgumentException( "null equivalent" );
                }
            }
        } else {
            equivalents = new MeshObjectIdentifier[0];
        }
        theIdentifier      = identifier;
        theTypeNames       = typeNames;
        thePropertyTypes   = propertyTypes;
        thePropertyValues  = propertyValues;
        theNeighbors       = neighbors;
        theRoleTypes       = roleTypes;
        theEquivalents     = equivalents;
    }

    /**
     * Obtain the MeshTypeIdentifiers of the MeshObject's EntityTypes.
     *
     * @return the MeshTypeIdentifiers of the MeshObject's EntityTypes
     */
    public final MeshTypeIdentifier [] getExternalTypeIdentifiers()
    {
        return theTypeNames;
    }

    /**
     * Obtain the MeshTypeIdentifier of the MeshObject's PropertyTpyes.
     *
     * @return the MeshTypeIdentifier of the MeshObject's PropertyTypes
     * @see #getPropertyValues()
     */
    public final MeshTypeIdentifier [] getPropertyTypes()
    {
        return thePropertyTypes;
    }

    /**
     * Obtain the PropertyValues of the MeshObject's properties, in the same sequence
     * as the PropertyTypes returned by getPropertyTypes.
     *
     * @return the PropertyValues of the MeshObject's properties
     * @see #getPropertyTypes()
     */
    public PropertyValue [] getPropertyValues()
    {
        return thePropertyValues;
    }

    /**
     * Obtain the MeshObjectIdentifier of the neighbors of this MeshObject.
     *
     * @return the MeshObjectIdentifier of the neighbors
     */
    public MeshObjectIdentifier [] getNeighbors()
    {
        return theNeighbors;
    }

    /**
     * Obtain the RoleTypes played by this MeshObject with respect to
     * a given neighbor.
     *
     * @param neighbor the neighbor
     * @return the RoleTypes
     */
    public MeshTypeIdentifier [] getRoleTypesFor(
            MeshObjectIdentifier neighbor )
    {
        for( int i=0 ; i<theNeighbors.length ; ++i ) {
            MeshObjectIdentifier current = theNeighbors[i];

            if( current.equals( neighbor )) {
                if( theRoleTypes[i] != null ) {
                    return theRoleTypes[i];
                } else {
                    return new MeshTypeIdentifier[0];
                }
            }
        }
        throw new IllegalArgumentException( "Not found" );
    }

    /**
     * Obtain the MeshObjectIdentifiers of the left and right MeshObjects that
     * participate in an equivalence set with this MeshObject.
     *
     * @return the MeshObjectIdentifier. May be null.
     */
    public MeshObjectIdentifier [] getEquivalents()
    {
        return theEquivalents;
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
                    "theIdentifier",
                    "theTypeNames",
                    "thePropertyTypes",
                    "thePropertyValues",
                    "theTimeCreated",
                    "theTimeUpdated",
                    "theTimeRead",
                    "theTimeExpires",
                    "theNeighbors",
                    "theRoleTypes",
                    "theEquivalents"
                },
                new Object[] {
                    theIdentifier,
                    theTypeNames,
                    thePropertyTypes,
                    thePropertyValues,
                    theTimeCreated,
                    theTimeUpdated,
                    theTimeRead,
                    theTimeExpires,
                    theNeighbors,
                    theRoleTypes,
                    theEquivalents
                });
    }

    /**
     * The identity of our EntityTypes.
     */
    protected MeshTypeIdentifier [] theTypeNames;

    /**
     * The identity of our PropertyTypes, in same sequence as thePropertyValues.
     */
    protected MeshTypeIdentifier [] thePropertyTypes;

    /**
     * The PropertyValues, in the same sequence as thePropertyTypes.
     */
    protected PropertyValue [] thePropertyValues;

    /**
     * The identity of our neighbors, in same sequence as theRoleTypes.
     */
    protected MeshObjectIdentifier[] theNeighbors;

    /**
     * The identity of the RoleTypes that we play with respect to our neighbors, in
     * same sequence as theNeighbors.
     */
    protected MeshTypeIdentifier [][] theRoleTypes;

    /**
     * The identities of the other members of the equivalence set in which this MeshObject participates
     */
    protected MeshObjectIdentifier[] theEquivalents;
}
