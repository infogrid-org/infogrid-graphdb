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

package org.infogrid.meshbase.transaction;

/**
  * This interface is implemented by all Objects that wish to listen for events indicating
  * a lifecycle event in the life of a MeshObject.
  */
public interface MeshObjectLifecycleListener
{
    /**
     * A MeshObject has been created.
     * This is a "semantic create".
     * 
     * @param theEvent the AbstractMeshObjectLifecycleEvent
     */
    public void meshObjectCreated(
            MeshObjectCreatedEvent theEvent );

    /**
     * A MeshObject has been deleted. This indicates a "semantic delete".
     * 
     * @param theEvent the MAbstractMeshObjectLifecycleEvent
     */
    public void meshObjectDeleted(
            MeshObjectDeletedEvent theEvent );
}
