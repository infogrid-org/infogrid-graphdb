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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set.active.m;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetEvent;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.mesh.set.m.AbstractMMeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.meshbase.transaction.MeshObjectStateEvent;
import org.infogrid.util.FlexibleListenerSet;
import org.infogrid.util.logging.Log;


/**
 * <p>Factors out common functionality in various in-memory ActiveMeshObjectSet implementations.</p>
 * 
 * <p>This implementation DOES NOT listen to PropertyChangeEvents from its content.
 * If a subclass needs to do that, it needs to do it itself.</p>
 */
public abstract class AbstractActiveMMeshObjectSet
    extends
        AbstractMMeshObjectSet
    implements
        ActiveMeshObjectSet,
        PropertyChangeListener // we can listen to dying MeshObjects
{
    private static Log log = Log.getLogInstance( AbstractActiveMMeshObjectSet.class  ); // our own, private logger

    /**
     * Constructor for subclasses only.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     */
    protected AbstractActiveMMeshObjectSet(
            MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * This is invoked by subclasses to potentially add a MeshObject to this set.
     * This method will check whether the MeshObject is indeed new, and if so,
     * add it to the set (otherwise, it will not add it). This method will also
     * fire a MeshObjectAddedEvent in case that is necessary
     * 
     * @param newMeshObject the new MeshObject to potentially add
     * @return true if it was indeed added
     */
    protected final synchronized boolean potentiallyAdd(
            MeshObject newMeshObject )
    {
        MeshBase theMeshBase = theFactory.getMeshBase();
        if( theMeshBase != newMeshObject.getMeshBase() ) {
            throw new WrongMeshBaseException( theMeshBase, newMeshObject.getMeshBase(), "potentiallyAdd cannot add MeshObject in different MeshBase" );
        }

        // linear search (FIXME? Not sure we can do ordering necessary for binary search)
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( newMeshObject == currentContent[i] ) {
                ++currentContentCounters[i];
                return false;
            }
        }

        certainlyAdd( newMeshObject );
        return true;
    }

    /**
     * This is invoked by subclasses to add a MeshObject to the set that is known
     * not to be there yet. This method exists in addition to potentiallyAdd()
     * for performance reasons.
     *
     * @param newMeshObject the new MeshObject to add
     */
    protected final synchronized void certainlyAdd(
            MeshObject newMeshObject )
    {
        certainlyAdd( newMeshObject, size() );
    }

    /**
     * This is invoked by subclasses to add a MeshObject to the set that is known
     * not to be there yet, and add it at a specific location. This method exists
     * in addition to potentiallyAdd() for performance reasons.
     *
     * @param newMeshObject the new MeshObject to add
     * @param where the index into the current content
     */
    protected final synchronized void certainlyAdd(
            MeshObject newMeshObject,
            int        where )
    {
        MeshBase theMeshBase = theFactory.getMeshBase();
        if( theMeshBase != newMeshObject.getMeshBase() ) {
            throw new WrongMeshBaseException( theMeshBase, newMeshObject.getMeshBase(), "certainlyAdd cannot add MeshObject in different MeshBase" );
        }

        // is new
        MeshObject [] newContent  = new MeshObject[ currentContent.length+1 ];
        int []        newCounters = new int[ newContent.length ];

        for( int i=0 ; i<where ; ++i ) {
            newContent[i]  = currentContent[i];
            newCounters[i] = currentContentCounters[i];
        }
        newContent[  where ] = newMeshObject;
        newCounters[ where ] = 0;

        for( int i=where ; i<currentContent.length ; ++i ) {
            newContent[i+1]  = currentContent[i];
            newCounters[i+1] = currentContentCounters[i];
        }

        currentContent         = newContent;
        currentContentCounters = newCounters;

        if( haveContentPropertyChangeListeners() ) {
            newMeshObject.addWeakPropertyChangeListener( this );
        }
        if( this instanceof OrderedMeshObjectSet ) {
            fireMeshObjectAdded( newMeshObject, where );
        } else {
            fireMeshObjectAdded( newMeshObject, -1 );
        }
    }

    /**
     * This is invoked by subclasses to potentially remove a MeshObject from this set.
     * This method will check whether this set contains this MeshObject for another
     * reason. If not, it will remove it from the set and fire events.
     *
     * @param oldMeshObject the MeshObject to potentially remove
     * @return true if it was indeed removed
     */
    protected final synchronized boolean potentiallyRemove(
            MeshObject oldMeshObject )
    {
        // linear search (FIXME? Not sure we can do ordering necessary for binary search)
        int foundIndex = -1;
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( oldMeshObject == currentContent[i] ) {
                foundIndex = i;
                --currentContentCounters[ foundIndex ];
                break;
            }
        }
        if( foundIndex < 0 ) {
            throw new IllegalArgumentException( "Entity not in set" );
        }

        if( currentContentCounters[ foundIndex ] >= 0 ) {
            return false;
        }

        certainlyRemove( foundIndex );

        return true;
    }

    /**
     * This is invoked by subclasses to remove a MeshObject which
     * is known for certain to be there, and to be removed. This exists primarily
     * for efficiency reasons.
     *
     * It is allowed to call this with an argument that is not actually contained
     * in this set at this time. However, if it is contained, it ignores the
     * content counter and removes it.
     *
     * @param toRemove the Entity to remove
     */
    protected final synchronized void certainlyRemove(
            MeshObject toRemove )
    {
        for( int i=0 ; i<currentContent.length ; ++i ) {
            if( toRemove == currentContent[i] ) {
                certainlyRemove( i );
                break;
            }
        }
    }

    /**
     * This is invoked by subclasses to remove a MeshObject at a certain index which
     * is known for certain to be there, and to be removed. This exists primarily
     * for efficiency reasons.
     *
     * @param indexToRemove index of the MeshObject to remove in current content
     */
    protected final synchronized void certainlyRemove(
            int indexToRemove )
    {
        MeshObject    oldMeshObject = currentContent[ indexToRemove ];
        MeshObject [] newContent    = new MeshObject[ currentContent.length-1 ];
        int []        newCounters   = new int[ newContent.length ];

        for( int i=0 ; i<indexToRemove ; ++i ) {
            newContent[i]  = currentContent[i];
            newCounters[i] = currentContentCounters[i];
        }
        for( int i=indexToRemove+1 ; i<currentContent.length ; ++i ) {
            newContent[i-1]  = currentContent[i];
            newCounters[i-1] = currentContentCounters[i];
        }

        currentContent         = newContent;
        currentContentCounters = newCounters;

        // we do this in any case, just to be defensive
        oldMeshObject.removePropertyChangeListener( this );

        if( this instanceof OrderedMeshObjectSet ) {
            fireMeshObjectRemoved( oldMeshObject, indexToRemove );
        } else {
            fireMeshObjectRemoved( oldMeshObject, -1 );
        }
    }

    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @param newListener the listener to be added
      * @see #addSoftActiveMeshObjectSetListener 
      * @see #addWeakActiveMeshObjectSetListener 
      * @see #removeActiveMeshObjectSetListener
      */
    public final synchronized void addDirectActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addDirectActiveMeshObjectSetListener", newListener );
        }

        theSetListeners.addDirect( newListener );
    }

    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @param newListener the listener to be added
      * @see #addDirectActiveMeshObjectSetListener 
      * @see #addWeakActiveMeshObjectSetListener 
      * @see #removeActiveMeshObjectSetListener
      */
    public final synchronized void addSoftActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addSoftActiveMeshObjectSetListener", newListener );
        }

        theSetListeners.addSoft( newListener );
    }

    /**
      * Add a listener for events indicating additions or removals to/from the set.
      *
      * @see #addDirectActiveMeshObjectSetListener 
      * @see #addWeakActiveMeshObjectSetListener 
      * @see #removeActiveMeshObjectSetListener
      */
    public final synchronized void addWeakActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener newListener )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "addWeakActiveMeshObjectSetListener", newListener );
        }

        theSetListeners.addWeak( newListener );
    }

    /**
      * Remove a listener for events indicating additions or removals to/from the set.
      *
      * @param oldListener the listener to be removed
      * @see #addDirectActiveMeshObjectSetListener 
      * @see #addSoftActiveMeshObjectSetListener 
      * @see #addWeakActiveMeshObjectSetListener 
      */
    public final synchronized void removeActiveMeshObjectSetListener(
            ActiveMeshObjectSetListener oldListener)
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "removeActiveMeshObjectSetListener", oldListener );
        }

        theSetListeners.remove( oldListener );
    }

    /**
     * This fires a MeshObjectAddedEvent to the registered ActiveMeshObjectSetListeners.
     * 
     * @param added the MeshObject that was added
     * @param whereAdded the index where in the current content it was added
     */
    protected final void fireMeshObjectAdded(
            MeshObject added,
            int        whereAdded )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireMeshObjectAdded( " + added + ", " + whereAdded + " )" );
        }

        if( theSetListeners.isEmpty() ) {
            return;
        }

        MeshObjectAddedEvent addEvt;
        if( whereAdded >= 0 ) {
            addEvt = new MeshObjectAddedEvent( this, added, whereAdded );
        } else {
            addEvt = new MeshObjectAddedEvent( this, added );
        }

        theSetListeners.fireEvent( addEvt );
    }

    /**
     * This fires a MeshObjectRemovedEvent to the registered ActiveMeshObjectSetListeners.
     * 
     * @param removed the MeshObject that was removed
     * @param whereRemoved the index in current content that the MeshObject held before being removed
     */
    protected final void fireMeshObjectRemoved(
            MeshObject removed,
            int        whereRemoved )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireMeshObjectRemoved( " + removed + ", " + whereRemoved + " )" );
        }

        if( theSetListeners.isEmpty() ) {
            return;
        }

        MeshObjectRemovedEvent removeEvt;
        if( whereRemoved >=0 ) {
            removeEvt = new MeshObjectRemovedEvent( this, removed, whereRemoved );
        } else {
            removeEvt = new MeshObjectRemovedEvent( this, removed );
        }

        theSetListeners.fireEvent( removeEvt );
    }

    /**
     * We are getting a PropertyChangeEvent from a MeshObject that is contained in this set.
     *
     * @param theEvent the event
     */
    @Override
    public void propertyChange(
            PropertyChangeEvent theEvent )
    {
        if( theEvent instanceof MeshObjectStateEvent ) {
            MeshObject sender = (MeshObject) theEvent.getSource();
            if( sender.getIsDead() ) {
                certainlyRemove( sender );
            }
        }
        super.propertyChange( theEvent );
    }

    /**
     * The set listeners registered with this set. Allocated all the time as it is highly
     * unlikely that we would have an ActiveMeshObjectSet without set listeners.
     */
    protected FlexibleListenerSet<ActiveMeshObjectSetListener,ActiveMeshObjectSetEvent,Object> theSetListeners
            = new FlexibleListenerSet<ActiveMeshObjectSetListener,ActiveMeshObjectSetEvent,Object>() {
                    protected void fireEventToListener(
                            ActiveMeshObjectSetListener listener,
                            ActiveMeshObjectSetEvent    event,
                            Object                      parameter )
                    {
                        if( event instanceof MeshObjectAddedEvent ) {
                            listener.meshObjectAdded( (MeshObjectAddedEvent) event );
                        } else if( event instanceof MeshObjectRemovedEvent ) {
                            listener.meshObjectRemoved( (MeshObjectRemovedEvent) event );
                        } else if( event instanceof OrderedActiveMeshObjectSetReorderedEvent ) {
                            listener.orderedMeshObjectSetReordered( (OrderedActiveMeshObjectSetReorderedEvent) event );
                        } else {
                            log.error( "unexpected event type: " + event );
                        }
                    }
            };
}
