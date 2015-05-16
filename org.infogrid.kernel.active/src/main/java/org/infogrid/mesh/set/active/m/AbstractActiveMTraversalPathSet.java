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

package org.infogrid.mesh.set.active.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.TraversalPathSorter;

import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetEvent;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.mesh.set.m.AbstractMTraversalPathSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalPath;

import org.infogrid.util.FlexibleListenerSet;
import org.infogrid.util.logging.Log;

/**
 * Factors out functionality common to most in-memory ActiveTraversalPathSets.
 */
public abstract class AbstractActiveMTraversalPathSet
        extends
            AbstractMTraversalPathSet
        implements
            ActiveTraversalPathSet
{
    private static Log log = Log.getLogInstance( AbstractActiveMTraversalPathSet.class ); // our own, private logger

    /**
     * Private constructor for subtypes only.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     */
    protected AbstractActiveMTraversalPathSet(
             MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * This is invoked by our subclasses to potentially reorder the set according to
     * the specified sorter.
     *
     * @param theSorter the TraversalPathSorter according to which we are supposed to reorder
     */
    protected final void potentiallyReorder(
            TraversalPathSorter theSorter )
    {
        currentContent = theSorter.getOrderedInNew( currentContent );
    }

    /**
     * This is invoked by subclasses to add a TraversalPath to the set that is known
     * not to be there yet. The new TraversalPath will be appended at the end of the
     * set.
     *
     * @param newPath the TraversalPath to be added
     */
    protected final synchronized void certainlyAdd(
            TraversalPath newPath )
    {
        certainlyAdd( newPath, size() );
    }

    /**
     * This is invoked by subclasses to add a TraversalPath to the set that is known
     * not to be there yet. The new TraversalPath will be appended at a specific
     * index.
     *
     * @param newPath the TraversalPath to be added
     * @param where the index where the TraversalPath will be added
     */
    protected final synchronized void certainlyAdd(
            TraversalPath newPath,
            int           where )
    {
        MeshBase theMeshBase = theFactory.getMeshBase();

        if( theMeshBase != newPath.getMeshBase() ) {
            throw new IllegalArgumentException();
        }

        // is new
        TraversalPath [] newContent  = new TraversalPath[ currentContent.length+1 ];

        for( int i=0 ; i<where ; ++i ) {
            newContent[i]  = currentContent[i];
        }

        newContent[ where ] = newPath;

        for( int i=where ; i<currentContent.length ; ++i ) {
            newContent[i+1]  = currentContent[i];
        }

        currentContent = newContent;

        if( haveContentPropertyChangeListeners() ) {
            newPath.addWeakTraversalPathPropertyChangeListener( this );
        }

        fireTraversalPathAdded( newPath, where );
    }

    /**
     * This is invoked by subclasses to remove a TraversalPath at a certain index which
     * is known for certain to be there, and to be removed. This method exists primarily
     * for efficiency reasons.
     *
     * @param toRemove the TraversalPath to remove
     */
    protected final synchronized void certainlyRemove(
            TraversalPath toRemove )
    {
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( toRemove.equals( currentContent[i] )) {
                certainlyRemove( i );
                return;
            }
        }
        log.error( "certainly remove did not find: " + toRemove );
    }

    /**
     * This is invoked by subclasses to remove a TraversalPath at a certain index which
     * is known for certain to be there, and to be removed. This exists primarily
     * for efficiency reasons.
     *
     * @param indexToRemove the index of the TraversalPath to remove
     */
    protected final synchronized void certainlyRemove(
            int indexToRemove )
    {
        TraversalPath    oldPath    = currentContent[ indexToRemove ];
        TraversalPath [] newContent = new TraversalPath[ currentContent.length-1 ];

        for( int i=0 ; i<indexToRemove ; ++i ) {
            newContent[i] = currentContent[i];
        }

        for( int i=indexToRemove+1 ; i<currentContent.length ; ++i ) {
            newContent[i-1] = currentContent[i];
        }

        currentContent = newContent;

        // remove in any case, defensively
        oldPath.removeTraversalPathPropertyChangeListener( this );

        fireTraversalPathRemoved( oldPath, indexToRemove );
    }

    /**
     * This is invoked by subclasses to remove a TraversalPath that has two components.
     *
     * @param one the first component of the TraversalPath
     * @param two the second component of the TraversalPath
     */
    protected final synchronized void certainlyRemove(
            TraversalPath one,
            TraversalPath two )
    {
        // FIXME -- inefficient
        TraversalPath fullPath = TraversalPath.create( one, two );
        certainlyRemove( fullPath );
    }

    /**
     * This is invoked by subclasses to remove all TraversalPaths that start with this
     * base TraversalPath.
     *
     * @param basePath the base TraversalPath
     */
    protected final synchronized void certainlyRemoveAllStartingWith(
            TraversalPath basePath )
    {
        int index = 0;
        // note: currentContent changes during this loop
        while( index<currentContent.length ) {
            TraversalPath current = currentContent[index];
            if( current.startsWith( basePath )) {
                certainlyRemove( index );
            } else {
                ++index;
            }
        }
        // if I see this right, not finding anything is perfectly alright here
    }

    /**
     * Obtain the destination Entities as an ActiveMeshObjectSet. This is
     * similar to TraversalPathSet.getDestinationsAsSet(), except that we return an
     * active set, not a snapshot.
     *
     * @return the created ActiveMeshObjectSet containing the destination Entities of this set
     */
    public synchronized ActiveMeshObjectSet getDestinationsAsActiveSet()
    {
        if( theDestinationsSet == null ) {
            theDestinationsSet = new MyDestinationsSet( this );
        }
        return theDestinationsSet;
    }

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public void addDirectActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addDirectActiveTraversalPathSetListener", newListener );
        }
        theSetListeners.addDirect( newListener );
    }

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public void addSoftActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addSoftActiveTraversalPathSetListener", newListener );
        }
        theSetListeners.addSoft( newListener );
    }

    /**
      * Add a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param newListener the new listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #removeActiveTraversalPathSetListener
      */
    public void addWeakActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addWeakActiveTraversalPathSetListener", newListener );
        }
        theSetListeners.addWeak( newListener );
    }

    /**
      * Remove a listener to tell us when TraversalPaths are added to or removed from this ActiveTraversalPathSet.
      *
      * @param oldListener the to-be-removed listener
      * @see #addDirectActiveTraversalPathSetListener
      * @see #addSoftActiveTraversalPathSetListener
      * @see #addWeakActiveTraversalPathSetListener
      */
    public void removeActiveTraversalPathSetListener(
            ActiveTraversalPathSetListener oldListener)
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "removeActiveTraversalPathSetListener", oldListener );
        }
        theSetListeners.remove( oldListener );
    }

    /**
     * This fires a TraversalPathAddedEvent to the registered listeners.
     *
     * @param added the added TraversalPath
     * @param whereAdded the index at which the added TraversalPath was inserted
     */
    protected void fireTraversalPathAdded(
            TraversalPath added,
            int           whereAdded )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireTraversalPathAdded( " + added + ", " + whereAdded + " )" );
        }
        
        if( theDestinationsSet == null && theSetListeners.isEmpty() ) {
            return;
        }

        TraversalPathAddedEvent addEvt;
        if( whereAdded >= 0 ) {
            addEvt = new TraversalPathAddedEvent( this, whereAdded, added );
        } else {
            addEvt = new TraversalPathAddedEvent( this, added );
        }

        MyDestinationsSet set = theDestinationsSet;
        if( set != null ) {
            set.added( addEvt );
        }
        theSetListeners.fireEvent( addEvt );
    }

    /**
     * This fires a TraversalPathRemovedEvent to the registered listeners.
     *
     * @param removed the removed TraversalPath
     * @param whereRemoved the index at which the removed TraversalPath was located before it was removed
     */
    protected void fireTraversalPathRemoved(
            TraversalPath removed,
            int           whereRemoved )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireTraversalPathRemoved( " + removed + ", " + whereRemoved + " )" );
        }

        if( theDestinationsSet == null && theSetListeners.isEmpty() ) {
            return;
        }

        TraversalPathRemovedEvent removeEvt;
        if( whereRemoved >= 0 ) {
            removeEvt = new TraversalPathRemovedEvent( this, whereRemoved, removed );
        } else {
            removeEvt = new TraversalPathRemovedEvent( this, removed );
        }

        MyDestinationsSet set = theDestinationsSet;
        if( set != null ) {
            set.removed( removeEvt );
        }

        theSetListeners.fireEvent( removeEvt );
    }

    /**
     * This fires a OrderedTraversalPathSetReorderedEvent to the registered listeners.
     *
     * @param event the event
     */
    protected void fireSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireSetReordered( " + event + " )" );
        }

        theSetListeners.fireEvent( event );
    }

    /**
      * The set listeners registered with this set.
      */
    protected FlexibleListenerSet<ActiveTraversalPathSetListener,ActiveTraversalPathSetEvent,Object> theSetListeners
            = new FlexibleListenerSet<ActiveTraversalPathSetListener,ActiveTraversalPathSetEvent,Object>() {
                    protected void fireEventToListener(
                            ActiveTraversalPathSetListener listener,
                            ActiveTraversalPathSetEvent    event,
                            Object                         parameter )
                    {
                        if( event instanceof TraversalPathAddedEvent ) {
                            listener.traversalPathAdded( (TraversalPathAddedEvent) event );
                        } else if( event instanceof TraversalPathRemovedEvent ) {
                            listener.traversalPathRemoved( (TraversalPathRemovedEvent) event );
                        } else if( event instanceof OrderedTraversalPathSetReorderedEvent ) {
                            listener.orderedTraversalPathSetReordered( (OrderedTraversalPathSetReorderedEvent) event );
                        } else {
                            log.error( "unexpected event type " + event );
                        }
                    }
                };

    /**
     * The active set of destinations, allocated as needed.
     */
    protected MyDestinationsSet theDestinationsSet;

    /**
     * The ActiveMeshObjectSet containing the destinations of the ActiveTraversalPathSet.
     *
     * This is a static inner class in order to have the explicity this$0 pointer to facilitate debugging
     */
    protected static class MyDestinationsSet
        extends
            AbstractActiveMMeshObjectSet
    {
        /**
         * Construct one.
         *
         * @param set the underlying ActiveTraversalPathSet
         */
        public MyDestinationsSet(
                AbstractActiveMTraversalPathSet set )
        {
            super( set.getFactory() );

            theUnderlyingSet = set;

            // there may be duplicates
            TraversalPath [] paths   = theUnderlyingSet.getTraversalPaths();
            MeshObject    [] content = new MeshObject[ paths.length ];
            int           [] mult    = new int[ paths.length ];

            int count = 0;
            for( int i=0 ; i<paths.length ; ++i ) {
                MeshObject current = paths[i].getLastMeshObject();
                int found;
                for( found = 0; found<count ; ++found ) {
                    if( content[found] == current ) {
                        break;
                    }
                }
                if( found == count ) {
                    content[ count++ ] = current;
                }
                ++mult[found];
            }
            if( count < content.length ) {
                MeshObject [] oldContent = content;
                int []        oldMult    = mult;

                content = new MeshObject[ count ];
                mult    = new int[ count ];

                for( int i=0 ; i<count ; ++i ) {
                    content[i] = oldContent[i];
                    mult[i]    = oldMult[i];
                }
            }

            this.setInitialContent( content, mult );
        }

        /**
         * Callback indicating that a TraversalPath has been added.
         *
         * @param event the event
         */
        public void added(
                TraversalPathAddedEvent event )
        {
            super.potentiallyAdd( event.getDeltaValue().getLastMeshObject() );
        }

        /**
         * Callback indicating that a TraversalPath has been removed.
         *
         * @param event the event
         */
        public void removed(
                TraversalPathRemovedEvent event )
        {
            super.potentiallyRemove( event.getDeltaValue().getLastMeshObject() );
        }

        /**
         * the TraversalPathSet whose content we use
         */
        protected AbstractActiveMTraversalPathSet theUnderlyingSet;
    }
}
