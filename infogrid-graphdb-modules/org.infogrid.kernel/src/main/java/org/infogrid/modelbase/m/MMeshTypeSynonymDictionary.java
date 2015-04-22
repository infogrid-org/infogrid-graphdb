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

package org.infogrid.modelbase.m;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.modelbase.MeshTypeIdentifierFactory;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.MeshTypeSynonymDictionary;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.logging.Log;

/**
 * An in-memory implementation of MeshTypeSynonymDictionary. This implementation
 * assumes that the ModelBase is set prior to invoking any of the find methods.
 */
public class MMeshTypeSynonymDictionary
        implements
            MeshTypeSynonymDictionary
{
    private static final Log log = Log.getLogInstance( MMeshTypeSynonymDictionary.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @return the created MMeshTypeSynonymDictionary
     */
    public static MMeshTypeSynonymDictionary create()
    {
        return new MMeshTypeSynonymDictionary();
    }

    /**
     * Private constructor, use factory method instead.
     */
    protected MMeshTypeSynonymDictionary()
    {
        // nothing
    }

    /**
     * Set the ModelBase that this MeshTypeSynonymDictionary delegates to.
     *
     * @param modelBase the ModelBase
     */
    public void setModelBase(
            ModelBase modelBase )
    {
        theModelBase = modelBase;
    }

    /**
     * Obtain the ModelBase that this MeshTypeSynonymDictionary delegates to.
     *
     * @return the ModelBase
     */
    @Override
    public ModelBase getModelBase()
    {
        return theModelBase;
    }

    /**
     * Obtain the MeshType for this identifier.
     *
     * @param externalIdentifier the Identifier
     * @return the MeshType
     * @throws MeshTypeNotFoundException thrown if this Identifier was not known
     */
    @Override
    public MeshType findMeshTypeByIdentifier(
            MeshTypeIdentifier externalIdentifier )
        throws
            MeshTypeNotFoundException
    {
        MeshTypeIdentifier internalIdentifier = theMappingTable.get( externalIdentifier );
        if( internalIdentifier == null ) {
            throw new MeshTypeWithIdentifierNotFoundException( externalIdentifier );
        }

        MeshType ret = theModelBase.findMeshTypeByIdentifier( internalIdentifier );
        return ret;
    }

    /**
     * Add a mapping to the internal table.
     *
     * @param externalIdentifier the identifier as known externally
     * @param internalIdentifier the canonical identifier
     */
    public void addToTable(
            MeshTypeIdentifier externalIdentifier,
            MeshTypeIdentifier internalIdentifier )
    {
        MeshTypeIdentifier found = theMappingTable.put( externalIdentifier, internalIdentifier );
        if( found != null ) {
            throw new IllegalArgumentException( "Redefining external identifier " + externalIdentifier + ", was " + found );
        }
    }

    /**
     * Load a property file containing external/internal mappings.
     *
     * @param inStream the incoming stream
     * @throws IOException thrown if a input/output problem occurred
     */
    public void loadMappings(
            InputStream inStream )
        throws
            IOException
    {
        Properties props = new Properties();
        props.load( inStream );

        MeshTypeIdentifierFactory factory = theModelBase.getMeshTypeIdentifierFactory();
        for( Object key : props.keySet() ) {
            Object value = props.get( key );

            MeshTypeIdentifier realKey   = factory.fromExternalForm( (String) key );
            MeshTypeIdentifier realValue = factory.fromExternalForm( (String) value );

            Object found = theMappingTable.put( realKey, realValue );
            if( found != null ) {
                log.warn( this + ".loadMappings(...): have pair already: " + key + " -> " + found );
            }
        }
    }

    /**
     * The internal table mapping external identifiers to canonical identifiers.
     */
    protected Map<MeshTypeIdentifier,MeshTypeIdentifier> theMappingTable
            = new HashMap<MeshTypeIdentifier,MeshTypeIdentifier>();

    /**
     * The ModelBase to delegate to.
     */
    protected ModelBase theModelBase;
}
