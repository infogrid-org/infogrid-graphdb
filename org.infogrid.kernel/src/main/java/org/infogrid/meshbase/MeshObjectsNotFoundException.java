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

package org.infogrid.meshbase;

import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.util.AbstractLocalizedException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if one or more MeshObjects could not be found.
 */
public class MeshObjectsNotFoundException
        extends
            AbstractLocalizedException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param mb the MeshBase that threw this Exception
     * @param identifier the identifier of the MeshObject that was not found
     */
    public MeshObjectsNotFoundException(
            MeshBase             mb,
            MeshObjectIdentifier identifier )
    {
        this( mb, new MeshObjectIdentifier[] { identifier } );
    }

    /**
     * Constructor.
     * 
     * @param mb the MeshBase that threw this Exception
     * @param identifiers the identifiers of the MeshObjects some of which were not found
     */
    public MeshObjectsNotFoundException(
            MeshBase                mb,
            MeshObjectIdentifier [] identifiers )
    {
        theMeshBase             = mb;
        theMeshBaseIdentifier   = mb.getIdentifier();
        theAttemptedIdentifiers = identifiers;
    }

    /**
     * Determine the identifiers of the MeshObjects that could not be found.
     *
     * @return the identifiers
     */
    public MeshObjectIdentifier [] getMeshObjectIdentifiers()
    {
        return theAttemptedIdentifiers;
    }

    /**
     * Allow subclasses to override which key to use in the Resource file for the string representation.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationParameter()
    {
        if( theAttemptedIdentifiers.length == 1 ) {
            return SINGULAR_STRING_REPRESENTATION_KEY;
        } else {
            return PLURAL_STRING_REPRESENTATION_KEY;
        }

    }
    /**
     * Obtain the parameters with which the internationalized string
     * will be parameterized.
     *
     * @return the parameters with which the internationalized string will be parameterized
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] {
                theMeshBase,
                theMeshBaseIdentifier,
                theAttemptedIdentifiers
        };
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
                new String [] {
                    "mb",
                    "attempted"
                },
                new Object[] {
                    theMeshBaseIdentifier,
                    theAttemptedIdentifiers
                });
    }

    /**
     * The MeshBase in which this Exception occurred.
     */
    protected transient MeshBase theMeshBase;

    /**
     * The identifier of the MeshBase in which this Exception occurred.
     */
    protected MeshBaseIdentifier theMeshBaseIdentifier;

    /**
     * The identifiers of the MeshObjects that were attempted to be accessed.
     */
    protected MeshObjectIdentifier [] theAttemptedIdentifiers;

    /**
     * Key into the ResourceHelper that helps with stringifying the singular case.
     */
    public static final String SINGULAR_STRING_REPRESENTATION_KEY = "SingularString";

    /**
     * Key into the ResourceHelper that helps with stringifying the plural case.
     */
    public static final String PLURAL_STRING_REPRESENTATION_KEY = "PluralString";
}
