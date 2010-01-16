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

package org.infogrid.meshbase;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.util.AbstractLocalizedException;
import org.infogrid.util.PartialResultException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if something went wrong when trying to access a MeshObject. The underlying
 * cause may indicate what went wrong.
 */
public class MeshObjectAccessException
        extends
            AbstractLocalizedException
        implements
            PartialResultException<MeshObject[]>,
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param mbIdentifier the MeshBaseIdentifier in which the Exception occurred
     * @param partialResult a partial result, if any, available at the time the Exception occurred
     * @param failedIdentifiers the MeshObjectIdentifiers 
     * @param cause the underlying cause for the Exception
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshBaseIdentifier      mbIdentifier,
            MeshObject []           partialResult,
            MeshObjectIdentifier [] failedIdentifiers,
            Throwable               cause )
    {
        super( null, cause ); // avoid construction of default message

        theMeshBase           = mb;
        theMeshBaseIdentifier = mbIdentifier;
        thePartialResult      = partialResult;
        theFailedIdentifiers  = failedIdentifiers;
    }

    /**
     * Obtain the MeshBase in which the Exception occurred.
     *
     * @return the MeshBase in which the Exception occurred
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * Determine whether a partial result is available.
     *
     * @return true if a partial result is available
     */
    public boolean isPartialResultAvailable()
    {
        return thePartialResult != null;
    }

    /**
     * Obtain the partial result, if any.
     *
     * @return the partial result, if any
     */
    public MeshObject [] getBestEffortResult()
    {
        return thePartialResult;
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
                    "cause"
                },
                new Object[] {
                    theMeshBase.getIdentifier(),
                    getCause()
                });
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
        return new Object[] { theFailedIdentifiers }; // single-element array
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
     * The partial result (if any).
     */
    protected transient MeshObject [] thePartialResult;

    /**
     * The identifiers of the MeshObjects whose access failed.
     */
    protected MeshObjectIdentifier [] theFailedIdentifiers;
}
