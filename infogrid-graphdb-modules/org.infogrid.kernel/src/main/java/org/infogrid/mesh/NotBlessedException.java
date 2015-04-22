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
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.MeshTypeUtils;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * This Exception is thrown if an operation requires a MeshObject or a relationship to
 * be blessed with a certain type, but it is not.
 */
public abstract class NotBlessedException
        extends
            IllegalOperationTypeException
        implements
            CanBeDumped
{
    /**
     * Constructor.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param obj the MeshObject on which the illegal operation was attempted, if available
     * @param identifier the MeshObjectIdentifier for the MeshObject on which the illegal operation was attempted
     * @param type the MeshType that was missing
     * @param typeIdentifier the MeshTypeIdentifier of the MeshType that was missing
     */
    protected NotBlessedException(
            MeshBase             mb,
            MeshBaseIdentifier   originatingMeshBaseIdentifier,
            MeshObject           obj,
            MeshObjectIdentifier identifier,
            MeshType             type,
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
     * Obtain the MeshType of that was missing.
     * 
     * @return the MeshType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the MeshType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     * @throws ClassCastException thrown if the type identifier identified a MeshType of an incorrect type
     */
    public synchronized MeshType getType()
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( theType == null ) {
            theType = resolve( theTypeIdentifier );
        }
        return theType;
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
                new String[]{
                    "meshObject",
                    "meshObjectIdentifier",
                    "meshType",
                    "meshTypeIdentifier",
                    "types"
                },
                new Object[] {
                    theMeshObject,
                    theMeshObjectIdentifier,
                    theType,
                    theTypeIdentifier,
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
        return new Object[] { theMeshObjectIdentifier, theTypeIdentifier };
    }

    /**
     * The MeshType of the missing blessing.
     */
    protected transient MeshType theType;
    
    /**
     * The identifier of the MeshType of the missing blessing.
     */
    protected MeshTypeIdentifier theTypeIdentifier;
}
