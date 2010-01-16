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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;

import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.EntityType;

/**
 * A simple implementation of MeshObjectSelector that accepts all MeshObjects of
 * a certain type (or subtype).
 */
public class ByTypeMeshObjectSelector
        implements
            MeshObjectSelector
{
    /**
     * Factory method.
     *
     * @param filterType the type whose instances we accept, including subtypes
     * @return the created ByTypeMeshObjectSelector
     */
    public static ByTypeMeshObjectSelector create(
            EntityType filterType )
    {
        return new ByTypeMeshObjectSelector( filterType, true );
    }

    /**
     * Factory method.
     *
     * @param filterType the type whose instances we accept
     * @param subtypeAllowed  if true, we also accept instances of a subtype
     * @return the created ByTypeMeshObjectSelector
     */
    public static ByTypeMeshObjectSelector create(
            EntityType filterType,
            boolean    subtypeAllowed )
    {
        return new ByTypeMeshObjectSelector( filterType, subtypeAllowed );
    }

    /**
     * Construct one with the type whose instances we accept.
     *
     * @param filterType the type whose instances we accept
     * @param subtypeAllowed  if true, we also accept instances of a subtype
     */
    protected ByTypeMeshObjectSelector(
            EntityType filterType,
            boolean    subtypeAllowed )
    {
        theFilterType     = filterType;
        theSubtypeAllowed = subtypeAllowed;
    }

    /**
     * Determine whether this MeshObject shall be selected.
     *
     * @param candidate MeshObject to test
     * @return true if this MeshObject is an instance of the specified type
     */
    public boolean accepts(
            MeshObject candidate )
    {
        if( candidate == null ) {
            throw new IllegalArgumentException();
        }

        AttributableMeshType [] candidateTypes = candidate.getTypes();

        if( theSubtypeAllowed ) {
            for( int i=0 ; i<candidateTypes.length ; ++i ) {
                if( candidateTypes[i].isSubtypeOfOrEquals( theFilterType )) {
                    return true;
                }
            }
        } else {
            for( int i=0 ; i<candidateTypes.length ; ++i ) {
                if( candidateTypes[i].equals( theFilterType )) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtain the EntityType for whose subtypes we filter.
     *
     * @return the EntityType for whose subtypes we filter
     */
    public EntityType getFilterType()
    {
        return theFilterType;
    }

    /**
     * Determine whether subtypes are allowed.
     *
     * @return are subtypes allowed
     */
    public boolean isSubtypesAllowed()
    {
        return theSubtypeAllowed;
    }

    /**
     * The EntityType for whose instances we accept.
     */
    protected EntityType theFilterType;

    /**
     * Do we allow a subtype instance.
     */
    protected boolean theSubtypeAllowed;
}
