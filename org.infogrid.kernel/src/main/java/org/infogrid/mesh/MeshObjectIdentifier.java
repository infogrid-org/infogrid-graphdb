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

package org.infogrid.mesh;

import org.infogrid.util.Identifier;

/**
 * <p>Represents the identity of a {@link org.infogrid.mesh.MeshObject MeshObject}.
 *    A MeshObject's MeshObjectIdentifier does not change during the lifetime of the
 *    MeshObject.</p>
 * <p>Different MeshBases may implement MeshObjectIdentifier differently. Use
 *    a MeshObjectIdentifierFactory to instantiate objects supporting this interface.</p>
 */
public interface MeshObjectIdentifier
        extends
            Identifier
{
    /**
     * Obtain the external form of the MeshObjectIdentifier relative to some path.
     *
     * @param relativePath the relative path
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     * @return the local external form
     */
    public abstract String toLocalExternalForm(
            String  relativePath,
            boolean assembleAsPartOfLongerId );

    /**
     * To save memory, this constant is allocated here and used wherever appropriate.
     */
    public static final MeshObjectIdentifier[] EMPTY_ARRAY = {};
}
