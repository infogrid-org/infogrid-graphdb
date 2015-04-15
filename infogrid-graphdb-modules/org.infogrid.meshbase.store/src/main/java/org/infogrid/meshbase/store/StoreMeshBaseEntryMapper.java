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

package org.infogrid.meshbase.store;

import org.infogrid.mesh.externalized.ExternalizedMeshObjectEncoder;
import org.infogrid.mesh.externalized.xml.ExternalizedMeshObjectXmlEncoder;

/**
 * Helps to map MeshObjects in and out from and to Stores.
 */
public class StoreMeshBaseEntryMapper
        extends
            AbstractStoreMeshBaseEntryMapper
{

    /**
     * Constructor.
     */
    public StoreMeshBaseEntryMapper()
    {
        // noop
    }

    /**
     * Obtain the preferred encodingid of this StoreEntryMapper.
     * 
     * @return the preferred encodingid
     */
    public String getPreferredEncodingId()
    {
        return PREFERRED_ENCODING.getEncodingId();
    }

    /**
     * Obtain the ExternalizedMeshObjectEncoder with a certain id.
     * 
     * @param encoderId the encoderId
     * @return the ExternalizedMeshObjectEncoder, or null
     */
    protected ExternalizedMeshObjectEncoder getEncoderFor(
            String encoderId )
    {
        for( ExternalizedMeshObjectEncoder current : theEncoders ) {
            if( encoderId.equals( current.getEncodingId() )) {
                return current;
            }
        }
        return null;
    }

    /**
     * The set of encoders / decoders currently known.
     */
    protected static final ExternalizedMeshObjectEncoder[] theEncoders = {
            new ExternalizedMeshObjectXmlEncoder()
    };

    /**
     * Preferred encoding type.
     */
    protected static final ExternalizedMeshObjectEncoder PREFERRED_ENCODING = theEncoders[0];
}
