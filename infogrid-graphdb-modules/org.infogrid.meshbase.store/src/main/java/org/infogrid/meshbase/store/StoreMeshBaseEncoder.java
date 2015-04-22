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

package org.infogrid.meshbase.store;

import java.io.InputStream;
import org.infogrid.mesh.a.AMeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.externalized.DecodingException;
import org.infogrid.model.primitives.externalized.EncodingException;

/**
 * This interface is supported by classes that know how to serialize and deserialize
 * MeshObjects.
 */
public interface StoreMeshBaseEncoder
{
    /**
     * Serialize a MeshObject into a sequence of bytes.
     *
     * @param obj the input MeshObject
     * @return the byte stream
     * @throws EncodingException thrown if an encoding problem occurred
     */
    public byte [] encode(
            AMeshObject obj )
        throws
            EncodingException;

    /**
     * Deserialize a MeshObject from a byte stream.
     *
     * @param contentAsStream the byte [] stream in which the MeshObject is encoded
     * @param mb the MeshBase on whose behalf the decoding is performed
     * @return return the just-instantiated MeshObject, as convenience
     * @throws DecodingException thrown if a decoding problem occurred
     */
    public AMeshObject decode(
            InputStream contentAsStream,
            MeshBase    mb )
        throws
            DecodingException;
}
