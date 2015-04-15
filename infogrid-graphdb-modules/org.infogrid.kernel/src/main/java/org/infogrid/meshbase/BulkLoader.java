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

package org.infogrid.meshbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;

/**
 * Interface implemented by those objects that know how to load bulk information into a MeshBase.
 */
public interface BulkLoader
{
    /**
     * Bulk-load data into this MeshBase.
     *
     * @param inStream the Stream from which to read the data
     * @param mb the MeshBase on whose behalf the loading is performed
     * @return the iterator over the loaded ExternalizedMeshObjects
     * @throws IOException thrown if an I/O error occurred
     * @throws BulkLoadException thrown if a loading exception occurred, for the details check the cause
     */
    public Iterator<? extends ExternalizedMeshObject> bulkLoad(
            InputStream inStream,
            MeshBase    mb )
        throws
            IOException,
            BulkLoadException;
}
