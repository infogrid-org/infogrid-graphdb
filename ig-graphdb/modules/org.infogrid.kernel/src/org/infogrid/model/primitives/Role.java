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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectAccessException;

/**
 * This pairs up a RoleType with the other side of a relationship that plays this RoleType.
 */
public abstract class Role
{
    /**
     * Factory method.
     *
     * @param start the start MeshObject
     * @param type the RoleType
     * @param neighbor the destination MeshObject
     * @return the created Role
     */
    public static Role create(
            MeshObject start,
            RoleType   type,
            MeshObject neighbor )
    {
        return new UsingMeshObject( start, type, neighbor );
    }

    /**
     * Factory method.
     *
     * @param start the start MeshObject
     * @param type the RoleType
     * @param neighborIdentifier identifier of the destination MeshObject
     * @return the created Role
     */
    public static Role create(
            MeshObject           start,
            RoleType             type,
            MeshObjectIdentifier neighborIdentifier )
    {
        return new UsingMeshObjectIdentifier( start, type, neighborIdentifier );
    }

    /**
     * Constructor.
     *
     * @param start the start MeshObject
     * @param type the RoleType
     */
    public Role(
            MeshObject start,
            RoleType   type )
    {
        theStart = start;
        theType  = type;
    }

    /**
     * Obtain the start MeshObject.
     *
     * @return the start MeshObject
     */
    public MeshObject getStart()
    {
        return theStart;
    }

    /**
     * Obtain the RoleType.
     *
     * @return the RoleType
     */
    public RoleType getRoleType()
    {
        return theType;
    }

    /**
     * Obtain the identifier of the destination MeshObject.
     *
     * @return the identifier of the destination MeshObject
     */
    public abstract MeshObjectIdentifier getNeighborIdentifier();

    /**
     * Obtain the destination MeshObject.
     *
     * @return the destination MeshObject
     * @throws MeshObjectAccessException thrown if the MeshObject could not be accessed
     * @throws NotPermittedException thrown if accessing the MeshObject was not permitted
     */
    public abstract MeshObject getNeighbor()
        throws
            MeshObjectAccessException,
            NotPermittedException;

    /**
     * Obtain the destination MeshObject if it is available. Do not attempt to resolve.
     *
     * @return the destination MeshObject
     */
    public abstract MeshObject getNeighborIfAvailable();

    /**
     * The start MeshObject.
     */
    protected MeshObject theStart;

    /**
     * The RoleType.
     */
    protected RoleType theType;

    /**
     * Implementation of Role that pairs a RoleType and a MeshObject.
     */
    private static class UsingMeshObject
            extends
                Role
    {
        /**
         * Constructor.
         *
         * @param start the start MeshObject
         * @param type the RoleType
         * @param neighbor the MeshObject
         */
        UsingMeshObject(
                MeshObject start,
                RoleType   type,
                MeshObject neighbor )
        {
            super( start, type );

            theNeighbor = neighbor;
        }

        /**
         * Obtain the identifier of the destination MeshObject.
         *
         * @return the identifier of the destination MeshObject
         */
        @Override
        public MeshObjectIdentifier getNeighborIdentifier()
        {
            return theNeighbor.getIdentifier();
        }

        /**
         * Obtain the destination MeshObject.
         *
         * @return the destination MeshObject
         */
        @Override
        public MeshObject getNeighbor()
        {
            return theNeighbor;
        }

        /**
         * Obtain the destination MeshObject if it is available. Do not attempt to resolve.
         *
         * @return the destination MeshObject
         */
        @Override
        public MeshObject getNeighborIfAvailable()
        {
            return theNeighbor;
        }

        /**
         * The other side.
         */
        protected MeshObject theNeighbor;
    }

    /**
     * Implementation of Role that pairs a RoleType and a MeshObjectIdentifier that
     * can be resolved when needed.
     */
    private static class UsingMeshObjectIdentifier
            extends
                Role
    {
        /**
         * Constructor.
         *
         * @param start the start MeshObject
         * @param type the RoleType
         * @param neighborIdentifier identifier of the  MeshObject
         */
        UsingMeshObjectIdentifier(
                MeshObject           start,
                RoleType             type,
                MeshObjectIdentifier neighborIdentifier )
        {
            super( start, type );

            theNeighborIdentifier = neighborIdentifier;
        }

        /**
         * Obtain the identifier of the destination MeshObject.
         *
         * @return the identifier of the destination MeshObject
         */
        @Override
        public MeshObjectIdentifier getNeighborIdentifier()
        {
            return theNeighborIdentifier;
        }

        /**
         * Obtain the destination MeshObject.
         *
         * @return the destination MeshObject
         * @throws MeshObjectAccessException thrown if the MeshObject could not be accessed
         * @throws NotPermittedException thrown if accessing the MeshObject was not permitted
         */
        @Override
        public MeshObject getNeighbor()
            throws
                MeshObjectAccessException,
                NotPermittedException
        {
            MeshBase   mb  = theStart.getMeshBase();
            MeshObject ret = mb.accessLocally( theNeighborIdentifier );

            return ret;
        }

        /**
         * Obtain the destination MeshObject if it is available. Do not attempt to resolve.
         *
         * @return the destination MeshObject
         */
        @Override
        public MeshObject getNeighborIfAvailable()
        {
            return null;
        }

        /**
         * Identifier of the other side.
         */
        protected MeshObjectIdentifier theNeighborIdentifier;
    }
}
