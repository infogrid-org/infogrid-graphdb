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

package org.infogrid.mesh.externalized;

import org.infogrid.mesh.MeshObjectIdentifier;

import org.infogrid.util.ArrayHelper;

/**
 * Factors out functionality common to ExternalizedMeshObject implementations.
 */
public abstract class AbstractExternalizedMeshObject
        implements
            ExternalizedMeshObject
{
    /**
     * Constructor for subclasses only. Subclass sets all properties.
     */
    protected AbstractExternalizedMeshObject()
    {
        // noop
    }

    /**
     * Constructor for subclasses only. Set all local properties.
     * 
     * @param identifier the MeshObjectIdentifier of the MeshObject
     * @param timeCreated the time the MeshObject was created
     * @param timeUpdated the time the MeshObject was last updated
     * @param timeRead the time the MeshObject was last read
     * @param timeExpires the time the MeshObject will expire
     */
    protected AbstractExternalizedMeshObject(
            MeshObjectIdentifier identifier,
            long                 timeCreated,
            long                 timeUpdated,
            long                 timeRead,
            long                 timeExpires )
    {
        theIdentifier  = identifier;
        theTimeCreated = timeCreated;
        theTimeUpdated = timeUpdated;
        theTimeRead    = timeRead;
        theTimeExpires = timeExpires;
    }

    /**
     * Obtain the MeshObjectIdentifier of the MeshObject.
     *
     * @return the MeshObjectIdentifier of the MeshObject
     */
    public MeshObjectIdentifier getIdentifier()
    {
        return theIdentifier;
    }

    /**
     * Obtain the time the MeshObject was created.
     *
     * @return the time the MeshObject was created, in System.currentTimeMillis() format
     */
    public long getTimeCreated()
    {
        return theTimeCreated;
    }

    /**
     * Obtain the time the MeshObject was last updated.
     *
     * @return the time the MeshObject was last updated, in System.currentTimeMillis() format
     */
    public long getTimeUpdated()
    {
        return theTimeUpdated;
    }

    /**
     * Obtain the time the MeshObject was last read.
     *
     * @return the time the MeshObject was last read, in System.currentTimeMillis() format
     */
    public long getTimeRead()
    {
        return theTimeRead;
    }

    /**
     * Obtain the time when the MeshObject will expire.
     *
     * @return the time the MeshObject will expire, in System.currentTimeMillis() format, or -1 if never.
     */
    public long getTimeExpires()
    {
        return theTimeExpires;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to test against
     * @return true if the two Objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( !( other instanceof ExternalizedMeshObject )) {
            return false;
        }
        
        ExternalizedMeshObject realOther = (ExternalizedMeshObject) other;

        if( !theIdentifier.equals( realOther.getIdentifier() )) {
            return false;
        }
        if( theTimeCreated != realOther.getTimeCreated() ) {
            return false;
        }
        if( theTimeUpdated != realOther.getTimeUpdated() ) {
            return false;
        }
        if( theTimeRead != realOther.getTimeRead() ) {
            return false;
        }
        if( theTimeExpires != realOther.getTimeExpires() ) {
            return false;
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getExternalTypeIdentifiers(), realOther.getExternalTypeIdentifiers(), true )) {
            return false;
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getPropertyTypes(), realOther.getPropertyTypes(), true )) {
            return false;
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getPropertyValues(), realOther.getPropertyValues(), true )) {
            return false;
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getNeighbors(), realOther.getNeighbors(), true )) {
            return false;
        }
        for( MeshObjectIdentifier neighbor : getNeighbors() ) {
            if( !ArrayHelper.hasSameContentOutOfOrder( getRoleTypesFor( neighbor ), realOther.getRoleTypesFor( neighbor ), true )) {
                return false;
            }
        }
        if( !ArrayHelper.hasSameContentOutOfOrder( getEquivalents(), realOther.getEquivalents(), true )) {
            return false;
        }

        return true;
    }
    
    /**
     * Determine HashCode.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return theIdentifier.hashCode();
    }

    /**
     * The MeshObjectIdentifier of this MeshObject.
     */
    protected MeshObjectIdentifier theIdentifier;

    /**
     * Our creation time in System.currentTimeMillis() format.
     */
    protected long theTimeCreated = -1L;

    /**
     * Our time of last update in System.currentTimeMillis() format.
     */
    protected long theTimeUpdated = -1L;

    /**
     * Our time of last read in System.currentTimeMillis() format.
     */
    protected long theTimeRead = -1L;
    
    /**
     * Our time of expiration in System.currentTimeMillis() format.
     */
    protected long theTimeExpires = -1L;
}
