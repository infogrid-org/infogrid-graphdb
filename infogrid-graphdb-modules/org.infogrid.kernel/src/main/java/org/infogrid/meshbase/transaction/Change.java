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

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.event.ExternalizableEvent;

/**
 * This interface represents the concept of a Change that may occur to a
 * {@link org.infogrid.mesh.MeshObject} during a {@link Transaction}. Any instance of Change
 * represents an "elemental" change, there are no composites.
 * 
 * @param <S> the type of the event source
 * @param <SID> the type of the identifier of the event source
 * @param <V> the type of the value
 * @param <VID> the type of the identifier of the value
 */
public interface Change<S,SID,V,VID>
        extends
            ExternalizableEvent<S,SID,V,VID>
{
    /**
     * Set the MeshBase against which all carried information shall be resolved.
     *
     * @param resolver the MeshBase.
     */
    public void setResolver(
            MeshBase resolver );

    /**
     * Obtain the MeshBase that is currently set as resolver for the identifiers carried by this event.
     * 
     * @return the MeshBase, if any
     */
    public MeshBase getResolver();

    /**
     * Obtain the identifier of the MeshObject affected by this Change.
     *
     * @return the identifier of the MeshObject affected by this Change
     */
    public abstract MeshObjectIdentifier getAffectedMeshObjectIdentifier();

    /**
     * Obtain the MeshObject affected by this Change.
     *
     * @return obtain the MeshObject affected by this Change
     */
    public abstract MeshObject getAffectedMeshObject();

    /**
     * <p>Apply this Change to a MeshObject in this MeshBase. This method
     *    is intended to make it easy to reproduce Changes that were made in
     *    one MeshBase to MeshObjects in another MeshBase.</p>
     *
     * <p>This method will attempt to create a Transaction if none is present on the
     * current Thread.</p>
     *
     * @param base the MeshBase in which to apply the Change
     * @return the MeshObject to which the Change was applied
     * @throws CannotApplyChangeException thrown if the Change could not be applied, e.g because
     *         the affected MeshObject did not exist in MeshBase base
     * @throws TransactionException thrown if a Transaction didn't exist on this Thread and
     *         could not be created
     */
    public abstract MeshObject applyTo(
            MeshBase base )
        throws
            CannotApplyChangeException,
            TransactionException;

    /**
     * <p>Create a Change that undoes this Change.</p>
     *
     * @return the inverse Change, or null if no inverse Change could be constructed.
     */
    public abstract Change inverse();

    /**
     * Determine whether a given Change is the inverse of this Change.
     *
     * @param candidate the candidate Change
     * @return true if the candidate Change is the inverse of this Change
     */
    public abstract boolean isInverse(
            Change candidate );
}
