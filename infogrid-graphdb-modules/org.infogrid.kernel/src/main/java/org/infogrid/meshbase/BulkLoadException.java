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

import org.infogrid.model.primitives.externalized.DecodingException;

/**
 * Thrown if something went wrong during bulk load.
 */
public class BulkLoadException
        extends
            DecodingException
{
    private static final long serialVersionUID = 1l; // helps with serialization

    /**
     * Constructor for subclasses only.
     *
     * @param cause the underlying cause for the BulkLoadException
     */
    public BulkLoadException(
            Throwable cause )
    {
        super( null, cause );
    }

    /**
     * Constructor.
     *
     * @param errorMessage the message
     * @param cause the underlying cause for the BulkLoadException
     */
    public BulkLoadException(
            String    errorMessage,
            Throwable cause )
    {
        super( errorMessage, cause );
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return null;
    }

    /**
     * Thrown if the MeshBase does not support bulk loading.
     */
    public static class NotSupported
            extends BulkLoadException
    {
        private static final long serialVersionUID = 1l; // helps with serialization

        /**
         * Constructor.
         */
        public NotSupported()
        {
            super( "This MeshBase does not support bulk load", null );
        }
    }
}
