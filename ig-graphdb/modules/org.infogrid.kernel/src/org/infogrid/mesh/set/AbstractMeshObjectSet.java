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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.meshbase.transaction.AbstractMeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.FlexiblePropertyChangeListenerSet;
import org.infogrid.util.Identifier;
import org.infogrid.util.NotSingleMemberException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
  * <p>Collects functionality common to various types of MeshObjectSets.</p>
  *
  * <p>It has a private variable to hold the current content. It also holds an array of
  * counters that count how many times the corresponding content object has been added
  * to the content. For example, if a subclass of AbstractActiveMeshObjectSet defines its
  * behavior as being the unification of 3 other sets, and each of those 3 sets contains
  * object X, the counter for object X would be 3. This allows us to efficiently determine
  * whether a given event of an underlying set requires us to remove an object from the
  * current content, or not.</p>
  */
public abstract class AbstractMeshObjectSet
        implements
            MeshObjectSet,
            PropertyChangeListener, // for property callbacks from the members of our set
            CanBeDumped
{
    private static Log log = Log.getLogInstance( AbstractMeshObjectSet.class ); // our own, private logger

    /**
      * Constructor to be used by subclasses only.
      *
      * @param factory the MeshObjectSetFactory that created this MeshObjectSet
      */
    protected AbstractMeshObjectSet(
            MeshObjectSetFactory factory )
    {
        theFactory = factory;
    }

    /**
     * Set a name for this MeshObjectSet. Its only purpose is to help in debugging.
     * 
     * @param newValue the new value
     */
    public void setDebugName(
            String newValue )
    {
        theDebugName = newValue;
    }

    /**
     * Obtain a name for this MeshObjectSet. Its only purpose is to help in debugging.
     * 
     * @return the name
     */
    public String getDebugName()
    {
        return theDebugName;
    }

    /**
     * Obtain the MeshObjectSetFactory by which this MeshObjectSet was created.
     *
     * @return the MeshObjectSetFactory
     */
    public final MeshObjectSetFactory getFactory()
    {
        return theFactory;
    }

    /**
     * Shorthand to obtain the MeshBase to which this MeshObjectSet belongs.
     * 
     * @return the MeshBase
     */
    public final MeshBase getMeshBase()
    {
        return theFactory.getMeshBase();
    }

    /**
     * Get the Nth MeshObject contained in this set.
     * While the order of MeshObjects in a MeshObjectSet typically does not change,
     * no assumptions should be made about this. Compare OrderedMeshObjectSet.
     *
     * @param n the index parameter
     * @return the Nth MeshObject contained in this set.
     */
    public MeshObject get(
            int n )
    {
        MeshObject [] meshObjects = getMeshObjects();
        return meshObjects[n];
    }

    /**
     * Assuming that this set contains no more than one MeshObject, obtain this one MeshObject. This
     * method is often very convenient if it is known to the application programmer that
     * a certain set may only contain one member. Invoking this method if the set has more
     * than one member will throw an Exception.
     *
     * @return the one element of the set, or null if the set is empty
     * @throws NotSingleMemberException thrown if the set contains more than one element
     */
    public MeshObject getSingleMember()
        throws
            NotSingleMemberException
    {
        int size = getSize();
        switch( size ) {
            case 0:
                return null;

            case 1:
                return get( 0 );

            default:
                throw new NotSingleMemberException( "MeshObjectSet has wrong size", size );
        }
    }

    /**
     * Convenience method to return the content of this MeshObjectSet as an
     * array of the canonical Identifiers of the member MeshObjects.
     *
     * @return the array of IdentifierValues representing the Identifiers of the members
     *         of this MeshObjectSet
     */
    public MeshObjectIdentifier[] asIdentifiers()
    {
        return theFactory.asIdentifiers( getMeshObjects() );
    }

    /**
     * Obtain an Iterator over all members of this set.
     *
     * @return Iterator over all members of this set
     */
    public abstract CursorIterator<MeshObject> iterator();

    /**
     * For JavaBeans-aware applications, we also provide this method to obtain an Iterator over
     * all members of this set
     *
     * @return an Iterator iterating over the content of this set
     */
    public final CursorIterator<MeshObject> getIterator()
    {
        return iterator();
    }

    /**
     * Determine whether a certain MeshObject is contained in this set.
     *
     * @param testObject the MeshObject we are looking for
     * @return if true, the MeshObject was found in the set
     * @throws WrongMeshBaseException thrown if the testObject is contained in a different MeshBase than the MeshObjects in this set
     */
    public boolean contains(
            MeshObject testObject )
        throws
            WrongMeshBaseException
    {
        // using an Iterator is most effecient for potentially large sets that aren't all loaded at the same time
        for( MeshObject current : this ) {

            if( current.getMeshBase() != testObject.getMeshBase() ) {
                throw new WrongMeshBaseException( current.getMeshBase(), testObject.getMeshBase() );
            }
            if( current.equals( testObject )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this set contains all MeshObjects in a supposed subset.
     *
     * @param subset the supposed subset
     * @return true if this set contains all MeshObjects in the supposed subset
     * @throws WrongMeshBaseException thrown if a tested object is contained in a different MeshBase than the MeshObjects in this set
     */
    public boolean containsAll(
            MeshObjectSet subset )
        throws
            WrongMeshBaseException
    {
        if( getMeshBase() != subset.getMeshBase()) {
            throw new WrongMeshBaseException( getMeshBase(), subset.getMeshBase() );
        }

        for( MeshObject current2 : subset ) {
            boolean found = false;

            for( MeshObject current1 : this ) {
                if( current2.equals( current1 )) {
                    found = true;
                    break;
                }
            }
            if( !found ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether this set has the same content as another set.
     *
     * @param other the MeshObjectSet to compare to
     */
    public boolean hasSameContent(
            MeshObjectSet other )
    {
        if( getSize() != other.getSize() ) {
            return false;
        }
        // given that's the same size, if we can find each object, we are in business
        for( MeshObject current : this ) {
            if( !other.contains( current )) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether this set contains a MeshObject with this identifier.
     * 
     * @param identifier the identifier of the MeshObject to look for
     * @return true if this set contains the given MeshObject
     */
    public boolean contains(
            Identifier identifier )
    {
        // using an Iterator is most effecient for potentially large sets that aren't all loaded at the same time
        for( MeshObject current : this ) {

            if( current.getIdentifier().equals( identifier )) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Convenience method to to easily find a member of this set by providing a
     * MeshObjectSelector that will select the MeshObject to be found. This method will return
     * the match and THEN STOP. If you expect more than one match, do not use this method.
     *
     * @param selector the criteria for selection
     * @return the first found MeshObject, or null if none
     */
    public MeshObject find(
            MeshObjectSelector selector )
    {
        // using an Iterator is most effecient for potentially large sets that aren't all loaded at the same time
        for( MeshObject current : this ) {
            
            if( selector.accepts( current )) {
                return current;
            }
        }
        return null;
    }

    /**
     * Create a subset of this set by providing a MeshObjectSelector that will select the MeshObjects
     * to be selected for the subset. This method will return all matches in this set.
     *
     * @param selector the criteria for selection
     * @return subset of this set
     */
    public MeshObjectSet subset(
            MeshObjectSelector selector )
    {
        return theFactory.createImmutableMeshObjectSet( this, selector );
    }

    /**
     * Determine whether this set is empty.
     *
     * @return if true, the set is empty
     */
    public boolean isEmpty()
    {
        return size() == 0;
    }

    /**
     * Determine the number of elements in this set.
     *
     * @return the number of elements in this set
     */
    public abstract int size();

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
     * Convenience method to intersect two MeshObjectSets using this MeshObjectSet's MeshObjectSetFactory.
     *
     * @param otherSet the MeshObjectSet to intersect this MeshObjectSet with
     * @return the intersection
     */
    public MeshObjectSet intersect(
            MeshObjectSet otherSet )
    {
        MeshObjectSet ret = theFactory.createImmutableMeshObjectSetIntersection( this, otherSet );
        return ret;
    }

    /**
     * Convenience method to unify two MeshObjectSets using this MeshObjectSet's MeshObjectSetFactory.
     *
     * @param otherSet the MeshObjectSet to unify this MeshObjectSet with
     * @return the intersection
     */
    public MeshObjectSet unify(
            MeshObjectSet otherSet )
    {
        MeshObjectSet ret = theFactory.createImmutableMeshObjectSetUnification( this, otherSet );
        return ret;
    }

    /**
     * Convenience method to remove the members of a MeshObjectSet from this MeshObjectSet.
     *
     * @param otherSet the MeshObjectSet whose members shall be removed from this MeshObjectSet
     * @return a new MeshObjectSet without the removed members
     */
    public MeshObjectSet minus(
            MeshObjectSet otherSet )
    {
        MeshObjectSet ret = theFactory.createImmutableMeshObjectSetMinus( this, otherSet );
        return ret;
    }

   /**
     * Set the PropertyTypes whose change events we forward to content
     * PropertyChangeListeners.
     *
     * @param filter the PropertyTypes whose change events we forward to content PropertyChangeListeners
     * @see #getFilterPropertyTypes
     */
    public void setFilterPropertyTypes(
            PropertyType [] filter )
    {
        filterPropertyTypes = filter;
    }

    /**
     * Obtain the PropertyTypes whose PropertyChangeEvents we forward to our
     * content PropertyChangeListeners, if any.
     *
     * @return the PropertyTypes whose PropertyChangeEvents we forward to our
     * content PropertyChangeListeners, if any
     */
    public final PropertyType [] getFilterPropertyTypes()
    {
        return filterPropertyTypes;
    }

    /**
     * Set whether this set forward RolePlayerTableEvents to content
     * PropertyChangeListeners.
     *
     * @param newValue true if we forward RolePlayerTableEvents to content PropertyChangeListeners
     */
    public void setForwardingRolePlayerUpdateEvents(
            boolean newValue )
    {
        forwardRolePlayerChangeEvents = newValue;
    }

    /**
     * Determine whether this set forwards RolePlayerTableEvents.
     *
     * @return if true, this set forwards RolePlayerTableEvents
     */
    public final boolean isForwardingRolePlayerUpdateEvents()
    {
        return forwardRolePlayerChangeEvents;
    }

    /**
      * Implements the ActiveEntitySet method that adds PropertyChangeListeners.
      *
      * @param newListener the listener to add as a content PropertyChangeListener
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
      * Implements the ActiveEntitySet method that adds PropertyChangeListeners.
      *
      * @param newListener the listener to add as a content PropertyChangeListener
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
      * Implements the ActiveEntitySet method that adds PropertyChangeListeners.
      *
      * @param newListener the listener to add as a content PropertyChangeListener
      * @see #addWeakContentPropertyChangeListener
      * @see #addDirectContentPropertyChangeListener
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
     * Implements the ActiveEntitySet method that removes PropertyChangeListeners.
     *
     * @param oldListener the existing listener to remove as a content PropertyChangeListener
     * @see #addDirectContentPropertyChangeListener
     * @see #addWeakContentPropertyChangeListener
     * @see #addSoftContentPropertyChangeListener
     */
    public final synchronized void removeContentPropertyChangeListener(
            PropertyChangeListener oldListener )
    {
        thePropertyChangeListeners.remove( oldListener );
        if( thePropertyChangeListeners.isEmpty() ) {
            thePropertyChangeListeners = null;
            lostLastContentPropertyChangeListener();
        }
    }

    /**
     * Returns a MeshObjectSet which is the union of all MeshObjectSets obtained
     * by traversing this TraversalSpecification for each of the MeshObjects in this set. Note
     * that the semantics of MeshObjectSet do not allow duplicates and thus there
     * won't be any duplicates in this result. This is a convenience function.
     *
     * @param theTraversalSpecification specifies how to traverse
     * @return the set of Entities obtained through the traversal
     */
    public final MeshObjectSet traverse(
            TraversalSpecification theTraversalSpecification )
    {
        return traverse( theTraversalSpecification, true );
    }

    /**
     * Returns a MeshObjectSet which is the union of all MeshObjectSets obtained
     * by traversing this TraversalSpecification for each of the MeshObjects in this set.
     * Specify whether to consider equivalent MeshObjects as well.
     * Note that the semantics of MeshObjectSet do not allow duplicates and thus there
     * won't be any duplicates in this result. This is a convenience function.
     *
     * @param theTraversalSpecification specifies how to traverse
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the set of Entities obtained through the traversal
     */
    public MeshObjectSet traverse(
            TraversalSpecification theTraversalSpecification,
            boolean                considerEquivalents )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "traverse", theTraversalSpecification, considerEquivalents );
        }

        MeshObjectSet us = theFactory.createImmutableMeshObjectSet( getMeshObjects() );
        return us.traverse( theTraversalSpecification, considerEquivalents );
    }

    /**
     * Traverse to the neighbor Entities of the members of this set.
     *
     * @return the set of neighbor Entities
     */
    public final MeshObjectSet traverseToNeighborMeshObjects()
    {
        return traverseToNeighborMeshObjects( true );
    }

    /**
     * Traverse to the neighbor MeshObjects of the members of this set. Specify whether
     * to consider equivalent MeshObjects as well.
     *
     * @param considerEquivalents if true, all equivalent MeshObjects are considered as well
     * @return the set of neighbor Entities
     */
    public MeshObjectSet traverseToNeighborMeshObjects(
            boolean considerEquivalents )
    {
        MeshObject [] meshObjects = getMeshObjects();
        
        MeshObjectSet [] neighbors = new MeshObjectSet[ meshObjects.length ];

        for( int i=0 ; i<meshObjects.length ; ++i ) {
            neighbors[i] = meshObjects[i].traverseToNeighborMeshObjects( considerEquivalents );
        }

        return theFactory.createImmutableMeshObjectSetUnification( neighbors );
    }

    /**
     * Determine whether we have content PropertyChangeListeners at this time.
     *
     * @return if true, we do have content PropertyChangeListeners at this time
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
        // using an Iterator is most effecient for potentially large sets that aren't all loaded at the same time
        for( MeshObject current : this ) {
            current.addWeakPropertyChangeListener( this );
        }
    }

    /**
     * This is a hook that is invoked when the number of content PropertyChangeListeners
     * goes from one to zero. Subclasses can override this to do special cleanup
     * to stop forwarding PropertyChangeEvents.
     */
    protected void lostLastContentPropertyChangeListener()
    {
        for( MeshObject current : this ) {
            current.removePropertyChangeListener( this );
        }
    }

    /**
     * This method determines whether an incoming event is supposed to be re-broadcast.
     * It can be overridden by subclasses. This is a hook primarily for efficiency
     * reasons -- if we know that none of our listeners will be interested in that
     * event, let's not send it.
     *
     * @param candidateEvent the event that may or may not be re-broadcast
     * @return if true, event will be re-broadcast.
     */
    protected boolean filterEventOkay(
            EventObject candidateEvent )
    {
        if( candidateEvent instanceof MeshObjectPropertyChangeEvent ) {
            if( filterPropertyTypes == null ) {
                return true;
            }

            MeshObjectPropertyChangeEvent realEvent = (MeshObjectPropertyChangeEvent) candidateEvent;
            PropertyType                  ptToCheck = realEvent.getProperty();

            for( int i=0 ; i<filterPropertyTypes.length ; ++i ) {
                if( ptToCheck.equalsOrOverrides( filterPropertyTypes[i] )) {
                    return true;
                }
            }
        } else if( candidateEvent instanceof AbstractMeshObjectRoleChangeEvent ) {
            if( forwardRolePlayerChangeEvents ) {
                return true; 
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * (Re-)broadcast a property change event.
     *
     * @param theEvent the event to broadcast
     */
    protected final void firePropertyChange(
            PropertyChangeEvent theEvent )
    {
        // by assigning this to a local variable, we save a synchronized statement
        // Note that the WeakPropertyChangeListenerSet is synchronized itself
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
        // This double check avoids doing a synchronization when not necessary
        if( listeners.isEmpty() ) {
            synchronized( this ) {
                if( listeners.isEmpty() ) {
                    listeners = null;
                    lostLastContentPropertyChangeListener();
                }
            }
        }
    }

    /**
     * Check whether we have received this event before, and if so, return true.
     * This provides functionality to our subtypes that allows them to easily skip reacting
     * to the same event more than once (eg receiving it from multiple sources).
     *
     * @param event the event to check
     * @return if true, we have received this every same event instance before
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
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "meshObject"
                },
                new Object[] {
                    getMeshObjects()
                } );
    }

    /**
     * We are getting a PropertyChangeEvent from a MeshObject that is contained in this set.
     *
     * @param theEvent the event
     */
    public void propertyChange(
            PropertyChangeEvent theEvent )
    {
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;
        if( listeners != null ) {
            listeners.fireEvent( theEvent );
        }
    }

    /**
     * The traceMethodCallEntry name, if any.
     */
    protected String theDebugName;

    /**
     * The MeshObjectSetFactory by which this MeshObjectSet was created.
     */
    protected MeshObjectSetFactory theFactory;

    /**
     * List of current property change listeners, if any.
     */
    private FlexiblePropertyChangeListenerSet thePropertyChangeListeners = null;

    /**
     * The PropertyTypes (if any) whose PropertyChangeEvents we are forwarding. If
     * this is null, we forward all of them.
     */
    private PropertyType [] filterPropertyTypes;

    /**
     * If this is true, we forward RolePlayer events, otherwise we don't.
     */
    private boolean forwardRolePlayerChangeEvents;

    /**
     * Buffer for the last N events for filtering out duplicates. This is a ring buffer with
     * eventTrackerIndex pointing to the next insertion point
     */
    private EventObject [] eventTracker = new EventObject[4];
    
    /**
     * Pointer into the eventTracker array
     */
    private int eventTrackerIndex = 0;
}
