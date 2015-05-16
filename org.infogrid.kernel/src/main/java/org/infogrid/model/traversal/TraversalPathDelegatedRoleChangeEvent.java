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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.AbstractMeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.CannotApplyChangeException;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;
import org.infogrid.meshbase.transaction.TransactionException;

/**
 * This is a RoleChangeEvent, forwarded by a TraversalPath and
 * annotated with the TraversalPath.
 */
public abstract class TraversalPathDelegatedRoleChangeEvent
        extends
            AbstractMeshObjectRoleChangeEvent
        implements
            TraversalPathDelegatedEvent
{
    /**
     * Construct with the original event that we are forwarding.
     *
     * @param path the TraversalPath that forwarded this event
     * @param org the original MeshObjectRolePlayerUpdateEvent
     */
    protected TraversalPathDelegatedRoleChangeEvent(
            TraversalPath                     path,
            AbstractMeshObjectRoleChangeEvent org )
    {
        super(  org.getSource(),
                org.getSource().getIdentifier(),
                org.getOldValue(),
                org.getOldValueIdentifier(),
                org.getDeltaValue(),
                org.getDeltaValueIdentifier(),
                org.getNewValue(),
                org.getNewValueIdentifier(),
                org.getNeighborMeshObject(),
                org.getNeighborMeshObjectIdentifier(),
                org.getTimeEventOccurred(),
                org.getResolver() );
        
        thePath          = path;
        theOriginalEvent = org;
    }

    /**
     * Obtain the TraversalPath that forwarded this event.
     *
     * @return the TraversalPath that forwarded this event
     */
    public TraversalPath getTraversalPath()
    {
        return thePath;
    }

    /**
     * Obtain the underlying original AbstractMeshObjectRoleChangeEvent.
     * 
     * @return the underlying original MAbstractMeshObjectRoleChangeEvent
     */
    public AbstractMeshObjectRoleChangeEvent getOriginalEvent()
    {
        return theOriginalEvent;
    }

    /**
     * Apply this Change to a MeshObject in this MeshBase. This method
     * is intended to make it easy to reproduce Changes that were made in
     * one MeshBase to MeshObjects in another MeshBase.
     *
     * This method will attempt to create a Transaction if none is present on the
     * current Thread.
     *
     * @param base the other MeshBase in which to apply the change
     * @throws CannotApplyChangeException thrown if the Change could not be applied, e.g because
     *         the affected MeshObject did not exist in the other MeshBase
     * @throws TransactionException thrown if a Transaction didn't exist on this Thread and could not be created
     */
    public MeshObject applyTo(
            MeshBase base )
        throws
            CannotApplyChangeException,
            TransactionException
    {
        MeshObject ret = theOriginalEvent.applyTo( base );
        return ret;
    }

    /**
     * Determine equality.
     * 
     * @param other the Object to compare with
     * @return true if the Objects are equal
     */
    @Override
    public abstract boolean equals(
            Object other );

    /**
     * Hash code.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int ret = 0;
        if( thePath != null ) {
            ret ^= thePath.hashCode();
        }
        if( theOriginalEvent != null ) {
            ret ^= theOriginalEvent.hashCode();
        }
        return ret;
    }

    /**
     * The TraversalPath that forwarded this event.
     */
    protected TraversalPath thePath;

    /**
     * The underlying original AbstractMeshObjectRoleChangeEvent.
     */
    protected AbstractMeshObjectRoleChangeEvent theOriginalEvent;

    /**
     * This indicates a TraversalPathDelegatedRoleChangeEvent in which the MeshObject plays one
     * more role.
     */
    public static class Added
            extends
                TraversalPathDelegatedRoleChangeEvent
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         * 
         * @param path the TraversalPath that forwarded this event
         * @param org the original MeshObjectRolePlayerUpdateEvent
         */
        Added(  TraversalPath            path,
                MeshObjectRoleAddedEvent org )
        {
            super( path, org );
        }

        /**
         * Determine whether this is an addition or a removal.
         *
         * @return always returns true
         */
        public boolean isAdditionalRoleUpdate()
        {
            return true;
        }

        /**
         * <p>Create a Change that undoes this Change.</p>
         *
         * @return the inverse Change, or null if no inverse Change could be constructed.
         */
        public Removed inverse()
        {
            return new Removed(
                    thePath,
                    ((MeshObjectRoleAddedEvent)theOriginalEvent).inverse() );
        }

        /**
         * Determine whether a given Change is the inverse of this Change.
         *
         * @param candidate the candidate Change
         * @return true if the candidate Change is the inverse of this Change
         */
        public boolean isInverse(
                Change candidate )
        {
            if( !( candidate instanceof Removed )) {
                return false;
            }
            Removed realCandidate = (Removed) candidate;
            if( !thePath.equals( realCandidate.thePath )) {
                return false;
            }
            if( !theOriginalEvent.isInverse( realCandidate.theOriginalEvent )) {
                return false;
            }
            return true;
        }

        /**
         * Determine equality.
         * 
         * @param other the Object to compare with
         * @return true if the Objects are equal
         */
        @Override
        public boolean equals(
                Object other )
        {
            if( !( other instanceof Added )) {
                return false;
            }
            Added realOther = (Added) other;
            
            if( thePath != null ) {
                if( !thePath.equals( realOther.thePath )) {
                    return false;
                }
            } else if( realOther.thePath != null ) {
                return false;
            }
            
            if( theOriginalEvent != null ) {
                if( !theOriginalEvent.equals( realOther.theOriginalEvent )) {
                    return false;
                }
            } else if( realOther.theOriginalEvent != null ) {
                return false;
            }
            return true;
        }

        /**
         * Hash code.
         * 
         * @return hash code
         */
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }
    }
    
    /**
     * This indicates a TraversalPathDelegatedRoleChangeEvent in which the MeshObject plays one
     * role less.
     */
    public static class Removed
            extends
                TraversalPathDelegatedRoleChangeEvent
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         * 
         * @param path the TraversalPath that forwarded this event
         * @param org the original MeshObjectRolePlayerUpdateEvent
         */
        Removed(
                TraversalPath              path,
                MeshObjectRoleRemovedEvent org )
        {
            super( path, org );
        }

        /**
         * Determine whether this is an addition or a removal.
         *
         * @return always returns false
         */
        public boolean isAdditionalRoleUpdate()
        {
            return false;
        }

        /**
         * <p>Create a Change that undoes this Change.</p>
         *
         * @return the inverse Change, or null if no inverse Change could be constructed.
         */
        public Added inverse()
        {
            return new Added(
                    thePath,
                    ((MeshObjectRoleRemovedEvent)theOriginalEvent).inverse() );
        }

        /**
         * Determine whether a given Change is the inverse of this Change.
         *
         * @param candidate the candidate Change
         * @return true if the candidate Change is the inverse of this Change
         */
        public boolean isInverse(
                Change candidate )
        {
            if( !( candidate instanceof Added )) {
                return false;
            }
            Added realCandidate = (Added) candidate;
            if( !thePath.equals( realCandidate.thePath )) {
                return false;
            }
            if( !theOriginalEvent.isInverse( realCandidate.theOriginalEvent )) {
                return false;
            }
            return true;
        }

        /**
         * Determine equality.
         * 
         * @param other the Object to compare with
         * @return true if the Objects are equal
         */
        @Override
        public boolean equals(
                Object other )
        {
            if( !( other instanceof Removed )) {
                return false;
            }
            Removed realOther = (Removed) other;
            
            if( thePath != null ) {
                if( !thePath.equals( realOther.thePath )) {
                    return false;
                }
            } else if( realOther.thePath != null ) {
                return false;
            }
            
            if( theOriginalEvent != null ) {
                if( !theOriginalEvent.equals( realOther.theOriginalEvent )) {
                    return false;
                }
            } else if( realOther.theOriginalEvent != null ) {
                return false;
            }
            return true;
        }

        /**
         * Hash code.
         * 
         * @return hash code
         */
        @Override
        public int hashCode()
        {
            return super.hashCode();
        }
    }
}
