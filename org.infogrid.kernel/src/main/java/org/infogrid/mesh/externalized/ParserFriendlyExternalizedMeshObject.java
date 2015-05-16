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

import java.util.ArrayList;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.logging.Dumper;

/**
 * A temporary buffer for a to-be-deserialized MeshObject.
 */
public class ParserFriendlyExternalizedMeshObject
        extends
            AbstractExternalizedMeshObject
        implements
            ExternalizedMeshObject
{
    /**
     * Set the MeshObjectIdentifier of the MeshObject.
     * 
     * @param newValue the new value
     */
    public void setIdentifier(
            MeshObjectIdentifier newValue )
    {
        theIdentifier = newValue;
    }

    /**
     * Set the TimeCreated.
     *
     * @param newValue the new value
     */
    public void setTimeCreated(
            long newValue )
    {
        theTimeCreated = newValue;
    }
    
    /**
     * Set the TimeUpdated.
     *
     * @param newValue the new value
     */
    public void setTimeUpdated(
            long newValue )
    {
        theTimeUpdated = newValue;
    }
    
    /**
     * Set the TimeRead.
     *
     * @param newValue the new value
     */
    public void setTimeRead(
            long newValue )
    {
        theTimeRead = newValue;
    }
    
    /**
     * Set the TimeExpires.
     *
     * @param newValue the new value
     */
    public void setTimeExpires(
            long newValue )
    {
        theTimeExpires = newValue;
    }
    
    /**
     * Add a MeshTypeIdentifier for the MeshType that the MeshObject is blessed with.
     * 
     * @param identifier the MeshType
     */
    public void addMeshType(
            MeshTypeIdentifier identifier )
    {
        theMeshTypes.add( identifier );
    }

    /**
     * Get the MeshTypeIdentifiers of the MeshTypes that the MeshObject is blessed with.
     *
     * @return the MeshTypeIdentifiers
     */
    public MeshTypeIdentifier [] getExternalTypeIdentifiers()
    {
        MeshTypeIdentifier [] ret = theMeshTypes.toArray( new MeshTypeIdentifier[ theMeshTypes.size() ]);
        return ret;
    }

    /**
     * Obtain the identifiers of the neighbors of this MeshObject.
     * 
     * @return the dentifiers of the neighbors
     */
    public MeshObjectIdentifier[] getNeighbors()
    {
        MeshObjectIdentifier [] ret = new MeshObjectIdentifier[ theRelationships.size() ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = theRelationships.get( i ).getNeighborIdentifier();
        }
        return ret;
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
        for( int i=0 ; i<theRelationships.size() ; ++i ) {
            Relationship current = theRelationships.get( i );

            if( current.getNeighborIdentifier().equals( neighbor )) {
                return current.getTypes();
            }
        }
        throw new IllegalArgumentException( "Not found" );
    }

    /**
     * Add an equivalent, using its MeshObjectIdentifier.
     * 
     * @param identifier the MeshObjectIdentifier of an equivalent
     */
    public void addEquivalent(
            MeshObjectIdentifier identifier )
    {
        theEquivalents.add( identifier );
    }

    /**
     * Get the MeshObject's equivalents, if any.
     *
     * @return the equivalents' MeshObjectIdentifiers.
     */
    public MeshObjectIdentifier[] getEquivalents()
    {
        MeshObjectIdentifier [] ret = theEquivalents.toArray( new MeshObjectIdentifier[ theEquivalents.size() ]);
        return ret;
    }

    /**
     * Add a PropertyType, using its MeshTypeIdentifier.
     * 
     * @param identifier the PropertyType's identifier
     */
    public void addPropertyType(
            MeshTypeIdentifier identifier )
    {
        thePropertyTypes.add( identifier );
    }

    /**
     * Get the PropertyTypes' MeshTypeIdentifiers.
     *
     * @return the PropertyTypes' identifiers.
     */
    public MeshTypeIdentifier [] getPropertyTypes()
    {
        MeshTypeIdentifier [] ret = thePropertyTypes.toArray( new MeshTypeIdentifier[ thePropertyTypes.size() ]);
        return ret;
    }

    /**
     * Add a PropertyValue.
     *
     * @param newValue the PropertyValue.
     */
    public void addPropertyValue(
            PropertyValue newValue )
    {
        thePropertyValues.add( newValue );
    }

    /**
     * Get the PropertyValues.
     *
     * @return the PropertyValues
     */
    public PropertyValue [] getPropertyValues()
    {
        PropertyValue [] ret = thePropertyValues.toArray( new PropertyValue[ thePropertyValues.size() ]);
        return ret;
    }

    /**
     * Add a relationship.
     *
     * @param rel the other relationship
     */
    public void addRelationship(
            Relationship rel )
    {
        theRelationships.add( rel );
    }

    /**
     * Get the relationships.
     *
     * @return the relationships
     */
    public Relationship [] getRelationships()
    {
        Relationship [] ret = theRelationships.toArray( new Relationship[ theRelationships.size() ]);
        return ret;
    }

    /**
     * Set the PropertyValue that is currently being parsed.
     *
     * @param newPropertyValue the PropertyValue
     */
    public void setCurrentPropertyValue(
            PropertyValue newPropertyValue )
    {
        theCurrentPropertyValue = newPropertyValue;
    }

    /**
     * Obtain the PropertyValue that is currently being parsed.
     *
     * @return the PropertyValue
     */
    public PropertyValue getCurrentPropertyValue()
    {
        return theCurrentPropertyValue;
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
                    "theMeshTypes",
                    "thePropertyTypes",
                    "thePropertyValues",
                    "theTimeCreated",
                    "theTimeUpdated",
                    "theTimeRead",
                    "theTimeExpires",
                    "theRelationships",
                    "theEquivalents"
                },
                new Object[] {
                    theIdentifier,
                    theMeshTypes,
                    thePropertyTypes,
                    thePropertyValues,
                    theTimeCreated,
                    theTimeUpdated,
                    theTimeRead,
                    theTimeExpires,
                    theRelationships,
                    theEquivalents
                });
    }

    /**
     * The MeshTypeIdentifiers of the MeshTypes.
     */
    protected ArrayList<MeshTypeIdentifier> theMeshTypes = new ArrayList<MeshTypeIdentifier>();
    
    /**
     * The MeshObjectIdentifiers of the equivalents.
     */
    protected ArrayList<MeshObjectIdentifier> theEquivalents = new ArrayList<MeshObjectIdentifier>();
    
    /**
     * The MeshTypeIdentifiers of the PropertyTypes.
     */
    protected ArrayList<MeshTypeIdentifier> thePropertyTypes = new ArrayList<MeshTypeIdentifier>();
    
    /**
     * The PropertyValues that go with the PropertyTypes.
     */
    protected ArrayList<PropertyValue> thePropertyValues = new ArrayList<PropertyValue>();

    /**
     * The relationships in which this ExternalizedMeshObject participates.
     */
    protected ArrayList<Relationship> theRelationships = new ArrayList<Relationship>();
    
    /**
     * The PropertyValue that is currently being parsed.
     */
    protected PropertyValue theCurrentPropertyValue;

    /**
     * Represents something that has types, i.e. as a MeshType or a relationship.
     * Note that XprisoMessageXmlEncoder may instantiate this directly, so it can't be abstract.
     */
    public static class HasTypes
    {
        /**
         * Constructor.
         * 
         * @param identifier the MeshObjectIdentifier
         * @param timeUpdated the time it was last updated
         */
        public HasTypes(
                MeshObjectIdentifier identifier,
                long                 timeUpdated )
        {
            theIdentifier  = identifier;
            theTimeUpdated = timeUpdated;
        }

        /**
         * Obtain the MeshObjectIdentifier of the MeshObject at this end.
         * 
         * @return the MeshObjectIdentifier of the MeshObject at this end
         */
        public MeshObjectIdentifier getIdentifier()
        {
            return theIdentifier;
        }
        
        /**
         * Get the identifiers of the types at this end, i.e. EntityTypes or RoleTypes.
         *
         * @return the identifiers of the types
         */
        public MeshTypeIdentifier [] getTypes()
        {
            MeshTypeIdentifier [] ret = theTypes.toArray( new MeshTypeIdentifier[ theTypes.size() ]);
            return ret;
        }

        /**
         * Add a type by identifier
         * 
         * @param identifier the identifier
         */
        public void addType(
                MeshTypeIdentifier identifier )
        {
            theTypes.add( identifier );
        }

        /**
         * Obtain the time at which it was last updated.
         *
         * @return the time at which it was last updated, in System.currentTimeMillis() format.
         */
        public long getTimeUpdated()
        {
            return theTimeUpdated;
        }

        /**
         * The MeshObjectIdentifier of the MeshObject on this side.
         */
        protected MeshObjectIdentifier theIdentifier;

        /**
         * The identifiers of the types.
         */
        protected ArrayList<MeshTypeIdentifier> theTypes = new ArrayList<MeshTypeIdentifier>();

        /**
         * The time at which it was last updated.
         */
        protected long theTimeUpdated;
    }

    /**
     * Represents something that has RoleTypes, i.e. a relationship.
     * Note that XprisoMessageXmlEncoder may instantiate this directly, so it can't be abstract.
     */
    public static class HasRoleTypes
            extends
                HasTypes
    {
        /**
         * Constructor.
         * 
         * @param identifier the MeshObjectIdentifier on this side of the relationship
         * @param neighborIdentifier the MeshObjectIdentifier on the other side of the relationship
         * @param timeUpdated the time it was last updated
         */
        public HasRoleTypes(
                MeshObjectIdentifier identifier,
                MeshObjectIdentifier neighborIdentifier,
                long                 timeUpdated )
        {
            super( identifier, timeUpdated );

            theNeighborIdentifier = neighborIdentifier;
        }

        /**
         * Obtain the MeshObjectIdentifier of the MeshObject at the other end.
         * 
         * @return the HasMeshObjectIdentifierypes of the MeshObject at the other end
         */
        public MeshObjectIdentifier getNeighborIdentifier()
        {
            return theNeighborIdentifier;
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
                        "theTypes",
                        "theNeighborIdentifier"
                    },
                    new Object[] {
                        theIdentifier,
                        theTypes,
                        theNeighborIdentifier
                    });
        }

        /**
         * The MeshObjectIdentifier of the MeshObject on the other side.
         */
        protected MeshObjectIdentifier theNeighborIdentifier;
    }

    /**
     * A relationship.
     */
    public static class Relationship
            extends
                HasRoleTypes
    {
        /**
         * Constructor.
         *
         * @param identifier the MeshObjectIdentifier on this side of the relationship
         * @param neighborIdentifier the MeshObjectIdentifier on the other side of the relationship
         * @param timeUpdated the time it was last updated
         */
        public Relationship(
                MeshObjectIdentifier identifier,
                MeshObjectIdentifier neighborIdentifier,
                long                 timeUpdated )
        {
            super( identifier, neighborIdentifier, timeUpdated );
        }
    }

    /**
     * Represents something that has Properties, i.e. a MeshObject.
     */
    public static class HasProperties
    {
        /**
         * Constructor.
         * 
         * @param identifier the MeshObjectIdentifier
         * @param propertyTypeName the identifier of the PropertyType
         * @param timeUpdated the time it was last updated
         */
        public HasProperties(
                MeshObjectIdentifier identifier,
                MeshTypeIdentifier   propertyTypeName,
                long                 timeUpdated )
        {
            theIdentifier       = identifier;
            thePropertyTypeName = propertyTypeName;
            theTimeUpdated      = timeUpdated;
        }
        
        /**
         * Obtain the MeshObjectIdentifier.
         * 
         * @return the MeshObjectIdentifier
         */
        public MeshObjectIdentifier getIdentifier()
        {
            return theIdentifier;
        }
        
        /**
         * Obtain the identifier of the PropertyType.
         * 
         * @return the identifier of the PropertyType.
         */
        public MeshTypeIdentifier getPropertyTypeName()
        {
            return thePropertyTypeName;
        }
        
        /**
         * Obtain the time at which it was last updated.
         *
         * @return the time at which it was last updated, in System.currentTimeMillis() format.
         */
        public long getTimeUpdated()
        {
            return theTimeUpdated;
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
                        "thePropertyTypeName"
                    },
                    new Object[] {
                        theIdentifier,
                        thePropertyTypeName
                    });
        }

        /**
         * The Identifier of the affected MeshObject.
         */
        protected MeshObjectIdentifier theIdentifier;

        /**
         * The PropertyType's identifier.
         */
        protected MeshTypeIdentifier thePropertyTypeName;

        /**
         * The time at which it was last updated.
         */
        protected long theTimeUpdated;
    }
}
