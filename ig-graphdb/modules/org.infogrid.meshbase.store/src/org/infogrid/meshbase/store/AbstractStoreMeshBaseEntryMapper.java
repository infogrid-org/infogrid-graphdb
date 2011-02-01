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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.externalized.ExternalizedMeshObjectEncoder;
import org.infogrid.meshbase.a.AMeshBase;
import org.infogrid.model.primitives.externalized.DecodingException;
import org.infogrid.model.primitives.externalized.EncodingException;
import org.infogrid.store.StoreEntryMapper;
import org.infogrid.store.StoreValue;
import org.infogrid.store.StoreValueDecodingException;
import org.infogrid.store.StoreValueEncodingException;
import org.infogrid.util.logging.Log;

/**
 * Factors out functionality common to StoreMeshBaseEntryMappers.
 */
public abstract class AbstractStoreMeshBaseEntryMapper
        implements
            StoreEntryMapper<MeshObjectIdentifier,MeshObject>
{
    private static final Log log = Log.getLogInstance( AbstractStoreMeshBaseEntryMapper.class ); // our own, private logger

    /**
     * Empty constructor, mostly to enable a breakpoint to be set.
     */
    protected AbstractStoreMeshBaseEntryMapper()
    {
        // nothing
    }

    /**
     * Set the MeshBase to which this StoreEntryMapper belongs.
     *
     * @param mb the MeshBase
     */
    public void setMeshBase(
            AMeshBase mb )
    {
        theMeshBase = mb;
    }

    /**
     * Map a key to a String value that can be used for the Store.
     *
     * @param key the key object
     * @return the corresponding String value
     */
    public String keyToString(
            MeshObjectIdentifier key )
    {
        String ret = key.toExternalForm();
        return ret;
    }
    
    /**
     * Map to a key a String value that can be used for the Store.
     *
     * @param key the key in String form
     * @return the corresponding key
     * @throws ParseException thrown if a parsing error occurred
     */
    public MeshObjectIdentifier stringToKey(
            String key )
        throws
            ParseException
    {
        MeshObjectIdentifier ret = theMeshBase.getMeshObjectIdentifierFactory().guessFromExternalForm( key );
        return ret;
    }

    /**
     * Obtain the time a value was created.
     *
     * @param value the time a value was created.
     * @return the time created, in System.currentTimeMillis() format
     */
    public long getTimeCreated(
            MeshObject value )
    {
        return value.getTimeCreated();
    }

    /**
     * Obtain the time a value was last updated.
     *
     * @param value the time a value was last updated.
     * @return the time updated, in System.currentTimeMillis() format
     */
    public long getTimeUpdated(
            MeshObject value )
    {
        return value.getTimeUpdated();
    }

    /**
     * Obtain the time a value was last read.
     *
     * @param value the time a value was last read.
     * @return the time read, in System.currentTimeMillis() format
     */
    public long getTimeRead(
            MeshObject value )
    {
        return value.getTimeRead();
    }

    /**
     * Obtain the time a value will expire.
     *
     * @param value the time a value will expire.
     * @return the time will auto-delete, in System.currentTimeMillis() format
     */
    public long getTimeExpires(
            MeshObject value )
    {
        return value.getTimeExpires();
    }

    /**
     * Obtain the ExternalizedMeshObjectEncoder with a certain id.
     * 
     * @param encoderId the encoderId
     * @return the ExternalizedMeshObjectEncoder, or null
     */
    protected abstract ExternalizedMeshObjectEncoder getEncoderFor(
            String encoderId );

    /**
     * Map a StoreValue to an AMeshObject.
     *
     * @param key the key to the StoreValue
     * @param value the StoreValue
     * @return the AMeshObject
     * @throws StoreValueDecodingException thrown if the StoreValue could not be decoded into an AMeshObject
     */
    public MeshObject decodeValue(
            MeshObjectIdentifier key,
            StoreValue           value )
        throws
            StoreValueDecodingException
    {
        String encodingId = value.getEncodingId();

        ExternalizedMeshObjectEncoder encoder = getEncoderFor( encodingId );
        if( encoder == null ) {
            log.error( "Unknown encoding ID: " + value.getEncodingId() );
            return null;
        }

        InputStream stream = value.getDataAsStream();
        
        try {
            ExternalizedMeshObject externalized = encoder.decodeExternalizedMeshObject(
                    stream,
                    theMeshBase );

            MeshObject ret = theMeshBase.getMeshBaseLifecycleManager().recreateMeshObject( externalized );

            return ret;
            
        } catch( DecodingException ex ) {
            throw new StoreValueDecodingException( ex );

        } catch( IOException ex ) {
            throw new StoreValueDecodingException( ex );
        }
    }

    /**
     * Obtain the value as a byte array.
     *
     * @param value the value
     * @return the byte array
     * @throws StoreValueEncodingException thrown if the MeshObject could not be encoded
     */
    public byte [] asBytes(
            MeshObject value )
        throws
            StoreValueEncodingException
    {
        ExternalizedMeshObjectEncoder encoder = getEncoderFor( getPreferredEncodingId() );
        
        try {
            ExternalizedMeshObject externalValue = value.asExternalized();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            encoder.encodeExternalizedMeshObject( externalValue, out );
            
            return out.toByteArray();

        } catch( EncodingException ex ) {
            throw new StoreValueEncodingException( ex );

        } catch( IOException ex ) {
            throw new StoreValueEncodingException( ex );
        }
    }

    /**
     * The MeshBase that this StoreMeshBaseMapper belongs to.
     */
    protected AMeshBase theMeshBase; 
}
