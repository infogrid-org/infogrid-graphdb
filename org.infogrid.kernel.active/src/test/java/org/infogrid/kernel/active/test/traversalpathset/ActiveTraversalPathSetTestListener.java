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

package org.infogrid.kernel.active.test.traversalpathset;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;
import org.infogrid.util.logging.Log;

/**
 * Receive and track callbacks in ActiveTraversalPathSetTests.
 */
class ActiveTraversalPathSetTestListener
        implements
            ActiveTraversalPathSetListener,
            PropertyChangeListener
{
    /**
     * Constructor.
     * 
     * @param listenerName name of the listener to distinguish listeners
     * @param set the ActiveTraversalPathSet to monitor
     * @param logger logger to log to
     */
    public ActiveTraversalPathSetTestListener(
            String                 listenerName,
            ActiveTraversalPathSet set,
            Log                    logger )
    {
        name   = listenerName;
        theSet = set;
        theSet.addWeakActiveTraversalPathSetListener( this );
        // theSet.addPropertyChangeListener( this );

        log = logger;

        reset();
    }

    /**
     * Obtain the number of addition events that we have received.
     * 
     * @return number of events
     */
    public int getAddCounter()
    {
        return addCounter;
    }

    /**
     * Obtain the number of deletion events that we have received.
     * 
     * @return number of events
     */
    public int getRemoveCounter()
    {
        return removeCounter;
    }

    /**
     * Obtain the number of reorder events that we have received.
     * 
     * @return number of events
     */
    public int getReorderCounter()
    {
        return reorderCounter;
    }

    /**
     * Obtain the number of PropertyChangeEvents that we have received.
     * 
     * @return number of events
     */
    public int getPropertyChangeCounter()
    {
        return propertyCounter;
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void traversalPathAdded(
            TraversalPathAddedEvent event )
    {
        ++addCounter;
        log.debug( name + ": The traversalPathAdded method was called: " + event );
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void traversalPathRemoved(
            TraversalPathRemovedEvent event )
    {
        ++removeCounter;
        log.debug( name + ": traversalPathRemoved method was called: " + event );
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        ++reorderCounter;
        log.debug( name + ": traversalPathSetReordered method was called: " + event );
    }

    /**
     * Callback.
     * 
     * @param event the event
     */
    public void propertyChange(
            PropertyChangeEvent event )
    {
        ++propertyCounter;
        log.debug( name + ": propertyChange was called: " + event );
    }

    /**
     * Reset all counters.
     */
    public void reset()
    {
        addCounter      = 0;
        removeCounter   = 0;
        propertyCounter = 0;
        reorderCounter  = 0;
    }

    /**
     * The name of the listener.
     */
    String name;

    /**
     * The set that we listen to.
     */
    ActiveTraversalPathSet theSet;

    /**
     * Counts additions.
     */
    private int addCounter;

    /**
     * Counts removals.
     */
    private int removeCounter;

    /**
     * Counts reorder events.
     */
    private int reorderCounter;

    /**
     * Counts PropertyChangeEvents.
     */
    private int propertyCounter;

    /**
     * The logger to log to.
     */
    private Log log;
}
