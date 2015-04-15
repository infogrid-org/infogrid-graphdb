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

package org.infogrid.modelbase.externalized;

import org.infogrid.model.primitives.MeshTypeIdentifier;

/**
 * This is data wanting to become a MeshObjectSelector, during reading.
 *
 * Concrete subclasses are inner classes.
 */
public abstract class ExternalizedMeshObjectSelector
{
    /**
     * This is a ByTypeMeshObjectSelector.
     */
    public static class ByType
        extends
            ExternalizedMeshObjectSelector
    {
        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
            if( subtypesAllowed ) {
               return "Type MeshObjectSelector: " + identifier + " (subtypes allowed)";
            } else {
               return "Type MeshObjectSelector: " + identifier + " (subtypes not allowed)";
            }
        }
        
        /**
         * Identifier of the type.
         */
        public MeshTypeIdentifier identifier;

        /**
         * Are subtypes allowed?
         */
        public boolean subtypesAllowed;
    }
}
