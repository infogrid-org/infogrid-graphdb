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

package org.infogrid.model.primitives.externalized;

import org.infogrid.model.primitives.PropertyValue;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Type supported by objects that know how to encode and decode PropertyValues.
 */
public interface PropertyValueEncoder
{
    /**
     * Serialize a PropertyValue to an OutputStream.
     * 
     * @param value the PropertyValue
     * @param out the OutputStream to which to append the PropertyValue
     * @throws EncodingException thrown if a problem occurred during encoding
     * @throws IOException thrown if an I/O error occurred
     */
    public void encodePropertyValue(
            PropertyValue value,
            OutputStream  out )
        throws
            EncodingException,
            IOException;
    
    /**
     * Serialize a PropertyValue to a StringBuilder.
     * 
     * @param value the PropertyValue
     * @param buf the StringBuilder to which to append the PropertyValue
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    public void appendPropertyValue(
            PropertyValue value,
            StringBuilder buf )
        throws
            EncodingException;

    /**
     * Deserialize a PropertyValue from a byte stream.
     * 
     * @param contentAsStream the byte [] stream in which the PropertyValue is encoded
     * @return return the just-instantiated PropertyValue
     * @throws DecodingException thrown if a problem occurred during decoding
     * @throws IOException thrown if an I/O error occurred
     */
    public PropertyValue decodePropertyValue(
            InputStream contentAsStream )
        throws
            DecodingException,
            IOException;

    /**
     * Obtain an encodingId that reflects this ExternalizedMeshObjectEncoder.
     * 
     * @return the encodingId.
     */
    public String getEncodingId();
}
