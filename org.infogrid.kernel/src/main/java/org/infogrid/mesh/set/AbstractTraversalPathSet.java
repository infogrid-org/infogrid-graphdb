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

package org.infogrid.mesh.set;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayCursorIterator;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.FlexiblePropertyChangeListenerSet;

/**
 * This abstract class factors out some functionality common to most TraversalPathSets.
 */
public abstract class AbstractTraversalPathSet
        implements
            TraversalPathSet,
            PropertyChangeListener
{
    /**
     * Constructor.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     */
    protected AbstractTraversalPathSet(
             MeshObjectSetFactory factory )
    {
        theFactory = factory;
    }

    /**
     * Set a name for this TraversalPathSet. Its only purpose is to help in debugging.
     * 
     * @param newValue the new value
     */
    public void setDebugName(
            String newValue )
    {
        theDebugName = newValue;
    }

    /**
     * Obtain a name for this TraversalPathSet. Its only purpose is to help in debugging.
     * 
     * @return the name
     */
    public String getDebugName()
    {
        return theDebugName;
    }

    /**
     * Obtain the MeshObjectSetFactory by which this TraversalPathSet was created.
     *
     * @return the MeshObjectSetFactory
     */
    public final MeshObjectSetFactory getFactory()
    {
        return theFactory;
    }

    /**
     * Shorthand to obtain the MeshBase to which this TraversalPathSet belongs.
     * 
     * @return the MeshBase
     */
    public final MeshBase getMeshBase()
    {
        return theFactory.getMeshBase();
    }
    
    /**
     * Assuming that this set contains no more than one TraversalPath, obtain this one TraversalPath. This
     * method is often very convenient if it is known to the application programmer that
     * a certain set may only contain one member. Invoking this method if the set has more
     * than one member will throw an Exception.
     *
     * @return the one element of the set, or null if the set is empty
     * @throws IllegalStateException thrown if the set contains more than one element
     */
    public TraversalPath getSingleMember()
        throws
            IllegalStateException
    {
        TraversalPath [] currentContent = getTraversalPaths();
        switch( currentContent.length ) {
            case 0:
                return null;

            case 1:
                return currentContent[0];

            default:
                throw new IllegalStateException( "Set had " + currentContent.length + " members, not one" );
        }
    }

    /**
     * Obtain an Iterator that iterates over this set.
     *
     * @return the Iterator that iterates over the content of this set
     */
    public CursorIterator<TraversalPath> iterator()
    {
        return ArrayCursorIterator.<TraversalPath>create( getTraversalPaths() );
    }

    /**
     * For JavaBeans-aware applications, we also provide this method to obtain an Iterator
     * iterating over the content of this set.
     *
     * @return an Iterator iterating over the content of this set
     */
    public final CursorIterator<TraversalPath> getIterator()
    {
        return iterator();
    }

    /**
     * Determine whether a certain TraversalPath is contained in this set.
     * This method uses equals() to determine whether the path is contained.
     *
     * @param testObject the test TraversalPath
     * @return true if testObject is contained in this set
     */
    public boolean contains(
            TraversalPath testObject )
    {
        TraversalPath [] currentContent = getTraversalPaths();

        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( testObject.equals( currentContent[i] )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this set is empty.
     *
     * @return true if this set is empty
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Determine the number of members of this set.
     *
     * @return the number of members of this set
     */
    public int size()
    {
        return getTraversalPaths().length;
    }

    /**
     * Determine the number of members in this set. Same as size(), for JavaBeans-aware software.
     *
     * @return the number of members in this set
     */
    public final int getSize()
    {
        return size();
    }

    /**
     * Obtain the destinations of the contained TraversalPaths as a MeshObjectSet.
     * While the same MeshObject may be a destination of more than one contained
     * TraversalPath, the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @return the destinations of the contained TraversalPaths as a MeshObjectSet
     */
    public MeshObjectSet getDestinationsAsSet()
    {
        // there may be duplicates
        TraversalPath [] paths   = getTraversalPaths();
        MeshObject    [] content = new MeshObject[ paths.length ];
        int count = 0;

        for( int i=0 ; i<paths.length ; ++i ) {
            boolean found   = false;
            MeshObject  current = paths[i].getLastMeshObject();
            for( int j=0 ; j<i ; ++j ) {
                if( content[j] == current ) {
                    found = true;
                    break;
                }
            }
            if( found ) {
                continue;
            }
            content[ count++ ] = current;
        }
        if( count < content.length ) {
            content = ArrayHelper.copyIntoNewArray( content, 0, count, MeshObject.class );
        }
        return theFactory.createOrderedImmutableMeshObjectSet( content ); // must be ordered, so subclasses can simply typecast
    }

    /**
     * Obtain the MeshObjects found at the given index in all the contained TraversalPaths,
     * and return them as a MeshObjectSet.
     * While the same MeshObject may be a step in more than one contained TraversalPath,
     * the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @param index the index from which we want to obtain the MeshObject
     * @return the MeshObjects found at the given index as a MeshObjectSet
     */
    public MeshObjectSet getStepAsSet(
            int index )
    {
        // there may be duplicates
        TraversalPath [] paths   = getTraversalPaths();
        MeshObject    [] content = new MeshObject[ paths.length ];
        int count = 0;

        for( int i=0 ; i<paths.length ; ++i ) {
            boolean found   = false;
            MeshObject  current = paths[i].getMeshObjectAt( index );
            for( int j=0 ; j<i ; ++j ) {
                if( content[j] == current ) {
                    found = true;
                    break;
                }
            }
            if( found ) {
                continue;
            }
            content[ count++ ] = current;
        }
        if( count < content.length ) {
            content = ArrayHelper.copyIntoNewArray( content, 0, count, MeshObject.class );
        }
        return theFactory.createOrderedImmutableMeshObjectSet( content ); // must be ordered, so subclasses can simply typecast
    }

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addWeakContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public final synchronized void addDirectContentPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
            gainedFirstContentPropertyChangeListener();
        }

        thePropertyChangeListeners.addDirect( newListener );
    }

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public final synchronized void addWeakContentPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
            gainedFirstContentPropertyChangeListener();
        }

        thePropertyChangeListeners.addWeak( newListener );
    }

    /**
     * Add a PropertyChangeListener to all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param newListener the new listener
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #removeContentPropertyChangeListener
     */
    public final synchronized void addSoftContentPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
            gainedFirstContentPropertyChangeListener();
        }

        thePropertyChangeListeners.addSoft( newListener );
    }

    /**
     * Remove a PropertyChangeListener from all the MeshObjects in all the
     * TraversalPaths in this set.
     *
     * @param oldListener the to-be-removed listeners
     * @see #addDirectContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     */
    public final synchronized void removeContentPropertyChangeListener(
            PropertyChangeListener oldListener)
    {
        thePropertyChangeListeners.remove( oldListener );
        if( thePropertyChangeListeners.isEmpty() ) {
            thePropertyChangeListeners = null;
            lostLastContentPropertyChangeListener();
        }
    }

    /**
     * Determine whether we have content PropertyChangeListeners at this time.
     *
     * @return true if we have content PropertyChangeListeners at this time
     */
    public final boolean haveContentPropertyChangeListeners()
    {
        // assignment saves a synchronized statement
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;

        return listeners != null && !listeners.isEmpty();
    }

    /**
     * This is a hook that is invoked when the number of content PropertyChangeListeners
     * goes from zero to one. Subclasses can override this to do special initializations
     * to forward PropertyChangeEvents.
     */
    protected void gainedFirstContentPropertyChangeListener()
    {
        TraversalPath [] content = getTraversalPaths();

        for( int i=0 ; i<content.length ; ++i ) {
            content[i].addWeakTraversalPathPropertyChangeListener( this );
        }
    }

    /**
     * This is a hook that is invoked when the number of content PropertyChangeListeners
     * goes from one to zero. Subclasses can override this to do special cleanup
     * to stop forwarding PropertyChangeEvents.
     */
    protected void lostLastContentPropertyChangeListener()
    {
        TraversalPath [] content = getTraversalPaths();

        for( int i=0 ; i<content.length ; ++i ) {
            content[i].removeTraversalPathPropertyChangeListener( this );
        }
    }

    /**
     * A member of this set sent a PropertyChangeEvent -- rebroadcast.
     *
     * @param theEvent the event
     */
    public void propertyChange(
            PropertyChangeEvent theEvent )
    {
        if( haveReceivedEventBefore( theEvent )) {
            return;
        }

        if( filterEventOkay( theEvent )) {
            firePropertyChange( theEvent );
        }
    }

    /**
     * Fire a PropertyChangeEvent.
     *
     * @param theEvent the event
     */
    protected final void firePropertyChange(
            PropertyChangeEvent theEvent )
    {
        // by assigning this to a local variable, we save a synchronized statement
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;

        if( listeners == null || listeners.isEmpty() ) {
            return;
        }

        if( !filterEventOkay( theEvent )) {
            return;
        }

        listeners.fireEvent( theEvent );

        // the fireEvent() method has a cleanup as a side-effect. If we cleaned up all
        // listners, do necessary adjustments.
        synchronized( this ) {
            if( listeners.isEmpty() ) {
                listeners = null;
                lostLastContentPropertyChangeListener();
            }
        }
    }

    /**
     * This method determines whether an incoming event is supposed to be rebroadcast.
     * It can be overridden by subclasses. This is a hook primarily for efficiency
     * reasons -- if we know that none of our listeners will be interested in that
     * event, let's not send it.
     *
     * @param candidateEvent the event that we examine
     * @return if true, event will be rebroadcast.
     */
    protected boolean filterEventOkay(
            EventObject candidateEvent )
    {
        // FIXME
        return true;
    }

    /**
     * Check whether we have received this event before, and if so, return true.
     * This provides functionality to our subtypes that allows them to easily skip reacting
     * to the same event more than once (eg receiving it from multiple sources).
     *
     * @param event the event that we test
     * @return true if we have received this event before
     */
    protected boolean haveReceivedEventBefore(
            EventObject event )
    {
        for( int i=0 ; i<eventTracker.length ; ++i ) {
            if( eventTracker[i] == event ) {
                return true;
            }
        }

        // exceptionTracker[eventTrackerIndex] = new Exception();
        eventTracker[eventTrackerIndex++] = event;

        if( eventTrackerIndex >= eventTracker.length ) {
            eventTrackerIndex = 0;
        }

        return false;
    }

    /**
     * The debug name, if any.
     */
    protected String theDebugName;

    /**
     * The MeshObjectSetFactory by which this MeshObjectSet was created.
     */
    protected MeshObjectSetFactory theFactory;

    /**
     * List of current PropertyChangeListeners, if any.
     */
    protected FlexiblePropertyChangeListenerSet thePropertyChangeListeners = null;

    /**
     * Buffer the last N events for filtering out duplicates. This is a ring buffer with
     * eventTrackerIndex pointing to the next insertion point.
     */
    private EventObject [] eventTracker = new EventObject[4];
    
    /**
     * Pointer into the eventTracker buffer.
     */
    private int eventTrackerIndex = 0;
}
