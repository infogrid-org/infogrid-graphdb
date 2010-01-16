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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
  * This Exception is thrown if we try to bless a MeshObject with an EntityType or RelationshipType
  * that is declared as abstract.
  */
public class IsAbstractException
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
     * @param obj the MeshObject that could not be blessed, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject that could not be blessed
     * @param type the AttributableMeshType that is abstract, if available
     * @param typeIdentifier the MeshTypeIdentifier for the AttributableMeshType that is abstract
     */
    public IsAbstractException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            AttributableMeshType type,
            MeshTypeIdentifier   typeIdentifier )
    {
        super( mb, originatingMeshBaseIdentifier, obj, identifier );
        
        if( typeIdentifier == null ) {
            throw new IllegalArgumentException( "typeIdentifier must not be null" );
        }
        theType           = type;
        theTypeIdentifier = typeIdentifier;
    }
    
    /**
     * More convenient simple constructor for the most common case.
     *
     * @param obj the MeshObject that could not be blessed, if available
     * @param type the AttributableMeshType that is abstract, if available
     */
    public IsAbstractException(
            MeshObject           obj,
            AttributableMeshType type )
    {
        this(   obj.getMeshBase(),
                obj.getMeshBase().getIdentifier(),
                obj,
                obj.getIdentifier(),
                type,
                type.getIdentifier() );
    }

    /**
     * Obtain the AttributableMeshType that was abstract.
     * 
     * @return the AttributableMeshType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the AttributableMeshType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType which is not a AttributableMeshType
     */
    public synchronized AttributableMeshType getAttributableMeshType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( theType == null ) {
            theType = (AttributableMeshType) resolve( theTypeIdentifier );
        }
        return theType;
    }

    /**
      * Obtain the Identifier of the abstract AttributableMeshType.
      *
      * @return the Identifier of the abstract AttributableMeshType
      */
    public MeshTypeIdentifier getMeshTypeIdentifier()
    {
        return theTypeIdentifier;
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
                    "type",
                    "typeIdentifier"
                },
                new Object[] {
                    theResolvingMeshBase,
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theType,
                    theTypeIdentifier
                });
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theMeshObjectIdentifier, theTypeIdentifier };
    }

    /**
     * The AttributableMeshType that was abstract.
     */
    protected transient AttributableMeshType theType;
    
    /**
     * The identifier of the AttributableMeshType that was abstract.
     */
    protected MeshTypeIdentifier theTypeIdentifier;
}

