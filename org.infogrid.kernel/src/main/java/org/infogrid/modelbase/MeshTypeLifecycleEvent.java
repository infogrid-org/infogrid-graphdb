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
import org.infogrid.util.event.AbstractExternalizableEvent;
import org.infogrid.util.event.SourceUnresolvedException;

/**
  * <p>This is the abstract supertype for all events affecting the life cycle of
  * a MeshType.</p>
  *
  * <p>Note: this is a fairly infrequent occurrence, as the model
  * tends to be constant for relatively long times.</p>
  */
public abstract class MeshTypeLifecycleEvent
       extends
           AbstractExternalizableEvent<ModelBase,Void,MeshType,MeshTypeIdentifier>
{
    /**
      * Construct one.
      *
      * @param theSender the ModelBase in which the event occurred
      * @param theObject the MeshType whose lifecycle was affected
      */
    protected MeshTypeLifecycleEvent(
            ModelBase theSender,
            MeshType  theObject )
    {
        super( theSender, null, theObject, theObject.getIdentifier(), System.currentTimeMillis() );
    }

    /**
     * Enable subclass to resolve the source of the event.
     *
     * @return the source of the event
     * @throws SourceUnresolvedException thrown if this ExternalizableEvent was serialized/deserialized,
     *         and re-resolving the source failed
     */
    protected ModelBase resolveSource()
            throws
                SourceUnresolvedException
    {
        return ModelBaseSingleton.getSingleton();
    }
}
