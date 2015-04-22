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
 * Thrown if a certain subtype of MeshType was expected than was found.
 */
public class WrongMeshTypeException
        extends
            MeshTypeWithIdentifierNotFoundException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the MeshType to be found
     * @param foundType the type that was found
     */
    public WrongMeshTypeException(
            MeshTypeIdentifier        identifier,
            Class<? extends MeshType> foundType )
    {
        super( identifier );

        theFoundType = foundType;
    }
    
    /**
     * Obtain the type that was found. This will be an interface such as
     * <code>EntityType.class</code>, not a subclass such as <code>MEntityType.class</code>.
     * 
     * @return the found type
     */
    public Class<? extends MeshType> getFoundType()
    {
        return theFoundType;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theIdentifier, theFoundType };
    }

    /**
     * The type that was actually found.
     */
    protected transient Class<? extends MeshType> theFoundType;
}
