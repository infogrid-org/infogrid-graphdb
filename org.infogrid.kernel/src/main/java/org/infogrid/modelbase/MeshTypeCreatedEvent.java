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
import org.infogrid.util.event.ValueUnresolvedException;

/**
  * This event indicates the creation of a MeshType in a ModelBase.
  */
public class MeshTypeCreatedEvent
        extends
            MeshTypeLifecycleEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Construct one.
      *
      * @param theSender the ModelBase in which the MeshType was created
      * @param theObject the MeshType that was created
      */
    public MeshTypeCreatedEvent(
            ModelBase theSender,
            MeshType  theObject )
    {
        super( theSender, theObject );
    }

    /**
     * Enable subclass to resolve a value of the event.
     *
     * @param vid the identifier of the value of the event
     * @return a value of the event
     * @throws ValueUnresolvedException thrown if this ExternalizableEvent was serialized/deserialized,
     *         and re-resolving the value failed
     */
    protected MeshType resolveValue(
            MeshTypeIdentifier vid )
       throws
           ValueUnresolvedException
    {
        try {
            MeshType ret = ModelBaseSingleton.getSingleton().findAttributableMeshTypeByIdentifier( vid );
            return ret;

        } catch( MeshTypeWithIdentifierNotFoundException ex ) {
            throw new ValueUnresolvedException( this, vid );
        }
    }
}
