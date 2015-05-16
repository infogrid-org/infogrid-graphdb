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
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.util.AbstractLocalizedException;

/**
 * Superclass of all Exceptions that occur when unsuccessfully manipulating the
 * graph of MeshObjects, or individual MeshObjects. It also collects functionality
 * that allows subclasses to be more easily serialized and deserialized.
 */
public abstract class MeshObjectGraphModificationException
        extends
            AbstractLocalizedException
{
    /**
     * Constructor without a message or a cause.
     * 
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     */
    protected MeshObjectGraphModificationException(
            MeshBase           mb,
            MeshBaseIdentifier originatingMeshBaseIdentifier )
    {
        theResolvingMeshBase             = mb;
        theOriginatingMeshBaseIdentifier = originatingMeshBaseIdentifier;
    }

    /**
     * Constructor with a message but no cause.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param msg the message
     */
    public MeshObjectGraphModificationException(
            MeshBase           mb,
            MeshBaseIdentifier originatingMeshBaseIdentifier,
            String             msg )
    {
        super( msg );

        theResolvingMeshBase             = mb;
        theOriginatingMeshBaseIdentifier = originatingMeshBaseIdentifier;
    }

    /**
     * Constructor with no message but a cause.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param cause the Throwable that caused this Exception
     */
    public MeshObjectGraphModificationException(
            MeshBase           mb,
            MeshBaseIdentifier originatingMeshBaseIdentifier,
            Throwable          cause )
    {
        super( cause );

        theResolvingMeshBase             = mb;
        theOriginatingMeshBaseIdentifier = originatingMeshBaseIdentifier;
    }

    /**
     * Constructor with a message and a cause.
     *
     * @param mb the MeshBase in which this Exception was created
     * @param originatingMeshBaseIdentifier the MeshBaseIdentifier of the MeshBase in which this Exception was created
     * @param msg the message
     * @param cause the Exception that caused this Exception
     */
    public MeshObjectGraphModificationException(
            MeshBase           mb,
            MeshBaseIdentifier originatingMeshBaseIdentifier,
            String             msg,
            Throwable          cause )
    {
        super( msg, cause );

        theResolvingMeshBase             = mb;
        theOriginatingMeshBaseIdentifier = originatingMeshBaseIdentifier;
    }
    
    /**
     * Obtain the MeshBaseIdentifier of the MeshBase in which the problem
     * originated. This may or may not be the same as the resolving MeshBase.
     * 
     * @return the MeshBaseIdentifier
     */
    public final MeshBaseIdentifier getOriginatingMeshBaseIdentifier()
    {
        return theOriginatingMeshBaseIdentifier;
    }

    /**
     * Set a MeshBase that can be used to resolve the MeshObjects carried
     * by this Exception.
     * 
     * @param mb the MeshBase against which MeshObjectIdentifiers will be resolved
     */
    public void setResolvingMeshBase(
            MeshBase mb )
    {
        theResolvingMeshBase = mb;
    }
    
    /**
     * Obtain the resolving MeshBase, if one has been set.
     * 
     * @return the resolving MeshBase, if any
     */
    public MeshBase getResolvingMeshBase()
    {
        return theResolvingMeshBase;
    }

    /**
     * Resolve a MeshObjectIdentifier against the resolving MeshBase.
     * 
     * @param identifier the MeshObjectIdentifier to resolve
     * @return the resolved MeshObject
     * @throws MeshObjectAccessException thrown if something went wrong accessing the MeshObject
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    protected MeshObject resolve(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectAccessException,
            NotPermittedException,
            IllegalStateException
    {
        if( theResolvingMeshBase == null ) {
            throw new IllegalStateException( "Must setResolvingMeshBase() prior to resolving a MeshObjectIdentifier" );
        }
        
        MeshObject ret = theResolvingMeshBase.accessLocally( identifier );
        return ret;
    }

    /**
     * Resolve a MeshTypeIdentifier against the resolving MeshBase's ModelBase.
     * 
     * @param identifier the MeshTypeIdentifier to resolve
     * @return the resolved MeshType
     * @throws MeshTypeWithIdentifierNotFoundException thrown if the MeshType could not be found
     * @throws IllegalStateException thrown if no resolving MeshBase is available
     */
    protected MeshType resolve(
            MeshTypeIdentifier identifier )
        throws
            MeshTypeWithIdentifierNotFoundException,
            IllegalStateException
    {
        if( theResolvingMeshBase == null ) {
            throw new IllegalStateException( "Must setResolvingMeshBase() prior to resolving a MeshTypeIdentifier" );
        }
        
        MeshType ret = theResolvingMeshBase.getModelBase().findMeshTypeByIdentifier( identifier );
        return ret;
    }

    /**
     * The MeshBase used to resolve MeshObjectIdentifiers. This may or may not be the same
     * MeshBase as the one identified with theOriginatingMeshBaseIdentifier.
     */
    protected MeshBase theResolvingMeshBase;
    
    /**
     * The MeshBaseIdentifier of the MeshBase in which the Exception was created.
     */
    protected MeshBaseIdentifier theOriginatingMeshBaseIdentifier;
}
