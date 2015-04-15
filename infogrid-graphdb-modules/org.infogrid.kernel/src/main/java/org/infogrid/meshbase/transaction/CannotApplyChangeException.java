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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if a Change could not be applied. Inner classes provide
 * more detail.
 */
public abstract class CannotApplyChangeException
        extends
            Exception
        implements
            CanBeDumped
{
    /**
     * Constructor.
     *
     * @param mb the MeshBase to which the Change could not be applied
     * @param cause the cause for this Exception
     */
    protected CannotApplyChangeException(
            MeshBase  mb,
            Throwable cause )
    {
        super( null, cause );
        
        theMeshBase           = mb;
        theMeshBaseIdentifier = mb.getIdentifier();
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "meshBaseIdentifier"
                },
                new Object[] {
                    theMeshBaseIdentifier
                } );
    }

    /**
     * The MeshBase to which the Change could not be applied.
     */
    protected transient MeshBase theMeshBase;
    
    /**
     * The identifier of the MeshBase to which the Change could not be applied.
     */
    protected MeshBaseIdentifier theMeshBaseIdentifier;
    
    /**
     * This subclass indicates that an unexpected Exception occurred during the
     * operation.
     */
    public static class ExceptionOccurred
            extends
                CannotApplyChangeException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param mb the MeshBase to which the Change could not be applied
         * @param cause the cause for this Exception
         */
        public ExceptionOccurred(
                MeshBase  mb,
                Throwable cause )
        {
            super( mb, cause );
        }
//
//        /**
//         * Obtain resource parameters for the internationalization.
//         *
//         * @return the resource parameters
//         */
//        public Object [] getLocalizationParameters()
//        {
//            return new Object[] { theMeshBase, theMeshBaseIdentifier };
//        }
    }
    
    /**
     * This subclass indicates that the MeshObject to which the Change was supposed
     * to be applied could not be found.
     */
    public static class MeshObjectNotFound
            extends
                CannotApplyChangeException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         * 
         * @param objectIdentifier the Identifier of the MeshObject that could not be found.
         * @param mb the MeshBase in which the MeshObject could not be found
         */
        public MeshObjectNotFound(
                MeshObjectIdentifier objectIdentifier,
                MeshBase             mb )
        {
            super( mb, null );
            
            theObjectIdentifier = objectIdentifier;
        }

//        /**
//         * Obtain resource parameters for the internationalization.
//         *
//         * @return the resource parameters
//         */
//        public Object [] getLocalizationParameters()
//        {
//            return new Object[] { theMeshBase, theMeshBaseIdentifier, theObjectIdentifier };
//        }
//
        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "meshBaseIdentifier",
                        "objectIdentifier"
                    },
                    new Object[] {
                        theMeshBaseIdentifier,
                        theObjectIdentifier
                    } );
        }

        /**
         * The Identifier of the MeshObject that could not be found.
         */
        protected MeshObjectIdentifier theObjectIdentifier;
    }
}
