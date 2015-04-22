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

package org.infogrid.model.traversal;

import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;

/**
 * A MeshObjectPropertyChangeEvent that has been forwarded by a TraversalPath and
 * that is annotated with the TraversalPath.
 */
public class TraversalPathDelegatedPropertyChangeEvent
        extends
            MeshObjectPropertyChangeEvent
        implements
            TraversalPathDelegatedEvent
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor, specifying the original event that is being forwarded.
     *
     * @param path the TraversalPath that forwarded this event
     * @param org the original event
     */
    public TraversalPathDelegatedPropertyChangeEvent(
            TraversalPath                 path,
            MeshObjectPropertyChangeEvent org )
    {
        super(  org.getSource(),
                org.getProperty(),
                org.getOldValue(),
                org.getDeltaValue(),
                org.getTimeEventOccurred() );

        thePath = path;
    }

    /**
     * Obtain the TraversalPath that forwarded this event.
     *
     * @return the TraversalPath that forwarded this event
     */
    public TraversalPath getTraversalPath()
    {
        return thePath;
    }

    /**
     * The TraversalPath that forwarded this event.
     */
    protected TraversalPath thePath;
}
