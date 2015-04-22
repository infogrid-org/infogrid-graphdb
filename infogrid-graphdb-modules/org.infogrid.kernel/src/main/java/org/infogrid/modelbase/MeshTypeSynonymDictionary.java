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

package org.infogrid.modelbase;

import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;

/**
 * This interface is supported by objects that know how to translate an identifier
 * other than the MeshType's primary identifier into the right MeshType. This is
 * useful if external namespace authorities identify concepts that are synonyms
 * with concepts defined in InfoGrid models, but at a different unique identifier.
 */
public interface MeshTypeSynonymDictionary
{
    /**
     * Obtain the ModelBase that this MeshTypeSynonymDictionary delegates to.
     *
     * @return the ModelBase
     */
    public ModelBase getModelBase();

    /**
     * Obtain the MeshType for this identifier.
     *
     * @param externalIdentifier the Identifier
     * @return the MeshType
     * @throws MeshTypeNotFoundException thrown if this Identifier was not known
     */
    public MeshType findMeshTypeByIdentifier(
            MeshTypeIdentifier externalIdentifier )
        throws
            MeshTypeNotFoundException;
}
