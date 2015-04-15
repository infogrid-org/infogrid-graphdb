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

package org.infogrid.model.util;

import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.util.logging.Log;


/**
 * This utility class makes it easy for the code generator to look up types.
 */
public abstract class TypeFinder
{
    private static final Log log = Log.getLogInstance( TypeFinder.class ); // our own, private logger

    /**
     * Find an EntityType by its unique identifierString. If needed, attempt to load additional
     * ModelModules to resolve the request. This uses String instead of MeshObjectIdentifier to
     * simplify the API for the code generator purpose.
     * 
     * @param identifierString identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     */
    public static EntityType findEntityType(
            String identifierString )
    {
        ModelBase mb = getModelBase();

        try {
            MeshTypeIdentifier identifier = mb.getMeshTypeIdentifierFactory().fromExternalForm( identifierString );
        
            EntityType ret = mb.findEntityTypeByIdentifier( identifier );

            return ret;

        } catch( Throwable ex ) {
            log.error( ex );
            return null;
        }
    }
    
    /**
     * Find a RelationshipType by its unique identifier. If needed, attempt to load additional
     * ModelModules to resolve the request. This uses String instead of MeshObjectIdentifier to
     * simplify the API for the code generator purpose.
     * 
     * 
     * @param identifierString Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     */
    public static RelationshipType findRelationshipType(
            String identifierString )
    {
        ModelBase mb = getModelBase();

        try {
            MeshTypeIdentifier identifier = mb.getMeshTypeIdentifierFactory().fromExternalForm( identifierString );
        
            RelationshipType ret = mb.findRelationshipTypeByIdentifier( identifier );

            return ret;

        } catch( Throwable ex ) {
            log.error( ex );
            return null;
        }
    }
    
    /**
     * Find a PropertyType by its unique identifier. If needed, attempt to load additional
     * ModelModules to resolve the request. This uses String instead of MeshObjectIdentifier to
     * simplify the API for the code generator purpose.
     * 
     * 
     * @param identifierString Identifier of the to-be-found MeshType
     * @return the found MeshType, or null if not found
     */
    public static PropertyType findPropertyType(
            String identifierString )
    {
        ModelBase mb = getModelBase();

        try {
            MeshTypeIdentifier identifier = mb.getMeshTypeIdentifierFactory().fromExternalForm( identifierString );
        
            PropertyType ret = mb.findPropertyTypeByIdentifier( identifier );

            return ret;

        } catch( Throwable ex ) {
            log.error( ex );
            return null;
        }
    }
    
    /**
     * Helper method to obtain the right ModelBase.
     *
     * @return the ModelBase
     */
    protected static ModelBase getModelBase()
    {
        return ModelBaseSingleton.getSingleton();
    }
}
