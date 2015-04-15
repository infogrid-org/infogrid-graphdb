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

package org.infogrid.mesh.set.active.m;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.mesh.set.active.TraversalActiveMeshObjectSet;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.logging.Log;

/**
 * <p>This is an ActiveMeshObjectSet whose elements are obtained by repeatedly
 * traversing a TraversalSpecification. All MeshObjects reached by that repeated
 * traversal are members of this set. The start MeshObject is also a member of this set.</p>
 * 
 * <p>By listening to MeshObjectAddedEvents and MeshObjectRemovedEvents one can monitor this set.
 * If subscribing as a content PropertyChangeListener, PropertyChangeEvents are being
 * forwarded from all MeshObjects that in the set
 * (if PropertyTypes are specified, only from those PropertyTypes)</p>
 * 
 * <p>To instantiate, use factory methods.</p>
 * 
 * <p>This is how it works: All current members of the set have an associated
 * TraversalActiveMMeshObjectSet, which contains the objects reachable from the
 * respective member through the TraveralSpecification. We monitor those
 * TraversalActiveMeshObjectSets, and as we encounter changes, we increment/decrement
 * a counter that goes with each member of the set. Once that that counter is down
 * to zero, the respective object is not a member of this set any more because it
 * is reachable through 0 paths.</p>
 * 
 * <p>Note: This may potentially forward duplicate PropertyChangeEvents.</p>
 */

public class TransitiveClosureTraversalActiveMMeshObjectSet
    extends
        AbstractActiveMMeshObjectSet
    implements
        PropertyChangeListener,     // so we can listen to the appropriate events from our contained MeshObjects
        ActiveMeshObjectSetListener // so we can listen to the appropriate events from the TraversalActiveMeshObjectSets of our contained MeshObjects
{
    private static final Log log = Log.getLogInstance(TransitiveClosureTraversalActiveMMeshObjectSet.class); // our own, private logger

    /**
     * Constructor.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param root the root MeshObject from where we attempt to traverse
     * @param spec the TraversalSpecification to use
     */
    protected TransitiveClosureTraversalActiveMMeshObjectSet(
            MeshObjectSetFactory   factory,
            MeshObject             root,
            TraversalSpecification spec )
    {
        super( factory );

        rootOfTraversal      = root;
        traversalSpec        = spec;

        synchronized( root.getMeshBase() ) {
            // this may take a while, we better protect ourselves
            addNewNode( 0, root, null, null );
        }

        // initialize content
        Set<MeshObject> theKeys    = theMap.keySet();
        MeshObject []   newContent = new MeshObject[ theKeys.size() ];
        theKeys.toArray( newContent );

        setInitialContent( newContent );
    }

    /**
     * Add a new node.
     *
     * @param numberSteps the number of steps we have traversed
     * @param newNode the new node
     * @param from reached from where
     * @param addNewMeshObjectsHere the ArrayList to add the new MeshObjects to
     */
    protected void addNewNode(
            int                   numberSteps,
            MeshObject            newNode,
            MeshObject            from,
            ArrayList<MeshObject> addNewMeshObjectsHere )
    {
        TraversalActiveMeshObjectSet newSet = ((ActiveMeshObjectSetFactory)theFactory).createActiveMeshObjectSet(
                newNode,
                traversalSpec );

        newNode.addWeakPropertyChangeListener( this );

        Memento newMemento = new Memento( newSet, numberSteps, from );

        Memento error = theMap.put( newNode, newMemento );
        if( error != null ) {
            log.error( "object already in HashSet " + newNode );
        }

        newSet.addWeakActiveMeshObjectSetListener( this );

        if( addNewMeshObjectsHere != null ) {
            addNewMeshObjectsHere.add( newNode );
        }
        int nextNumberSteps = numberSteps+1;

        Iterator<MeshObject> theIter = newSet.iterator();
        while( theIter.hasNext() ) {
            MeshObject current       = theIter.next();
            Memento    existsAlready = theMap.get( current );

            if( existsAlready == null ) {
                addNewNode( nextNumberSteps, current, newNode, addNewMeshObjectsHere );
            } else {
                existsAlready.addReachableFrom( newNode, nextNumberSteps );
            }
        }
    }

    /**
     * This indicates that oldObject has one less paths leading to it, which means
     * that it may have to be removed from this set, and all of its dependends need to
     * be checked similarly.
     *
     * @param oldObject the MeshObject that has one less path leading to it
     * @param from from where we reached this node
     * @param addRemovedMeshObjectsHere the ArrayList to which we add the removed MeshObject
     */
    protected void removeOrAdjust(
            MeshObject            oldObject,
            MeshObject            from,
            ArrayList<MeshObject> addRemovedMeshObjectsHere )
    {
        Memento m = theMap.get( oldObject );

        if( m.getReachedFrom().size() == 1 ) {
            // remove it and objects reached from there

            theMap.remove( oldObject );
            oldObject.removePropertyChangeListener( this );

            Iterator<MeshObject> theIter = m.getReaches().iterator();
            while( theIter.hasNext() ) {
                MeshObject current = theIter.next();

                removeOrAdjust( current, oldObject, addRemovedMeshObjectsHere );
            }
            addRemovedMeshObjectsHere.add( oldObject );

        } else {
            // adjust # steps
            m.removeReachableFrom( from );

            Iterator<MeshObject> theIter = m.getReachedFrom().iterator();
            int closest = Integer.MAX_VALUE;
            while( theIter.hasNext() ) {
                MeshObject current     = theIter.next();
                Memento    fromMemento = theMap.get( current );
                if( fromMemento != null ) {
                    // may be null, in case of chain removals
                    if( fromMemento.getNumberSteps() < closest ) {
                        closest = fromMemento.getNumberSteps();
                    }
                }
            }
            m.adjustNumberStepsTo( closest );
        }
    }

    /**
     * Obtain the MeshObject that is the start object for this TraversalActiveMMeshObjectSet.
     * 
     * @return the MeshObject that is the start object for this TTraversalActiveMeshObjectSet
     */
    public MeshObject getRootOfTraversal()
    {
        return rootOfTraversal;
    }

    /**
      * Obtain the TraversalSpecification that has been specified in the constructor.
      *
      * @return the TraversalSpecification that has been specified in the constructor
      */
    public TraversalSpecification getTraversalSpecification()
    {
        return traversalSpec;
    }

    /**
     * Callback from an object in the set.
     *
     * @param event the event
     */
    @Override
    public void propertyChange(
            PropertyChangeEvent event )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "propertyChange", event );
        }
        
        if( haveReceivedEventBefore( event )) {
            return;
        }

        if( event instanceof MeshObjectPropertyChangeEvent ) {
            if( filterEventOkay( event )) {
                firePropertyChange( event );
            }

        } else if( event instanceof MeshObjectRoleChangeEvent ) {

        }
    }

    /**
     * Callback from one of the contained TraversalActiveMeshObjectSets.
     *
     * @param event the event
     */
    public synchronized void meshObjectAdded(
            MeshObjectAddedEvent event )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "meshObjectAdded", event );
        }

        if( haveReceivedEventBefore( event )) {
            return;
        }

        TraversalActiveMMeshObjectSet.StartFromMeshObject sourceSet
                = (TraversalActiveMMeshObjectSet.StartFromMeshObject) event.getSource();
        MeshObject from = sourceSet.getStartOfTraversalMeshObject();

        MeshObject added       = event.getDeltaValue();
        Memento    m           = theMap.get( added );
        Memento    fromMemento = theMap.get( from );
        if( m == null ) {
            // new object for this set
            ArrayList<MeshObject> addedMeshObjects = new ArrayList<MeshObject>();

            addNewNode( fromMemento.getNumberSteps()+1, added, from, addedMeshObjects );

            Iterator<MeshObject> theIter = addedMeshObjects.iterator();
            while( theIter.hasNext() ) {
                MeshObject current = theIter.next();
                super.certainlyAdd( current );
            }
        } else {
            m.addReachableFrom( from, fromMemento.getNumberSteps()+1 );
        }
    }

    /**
     * Callback from one of the contained TraversalActiveMeshObjectSets.
     *
     * @param event the event
     */
    public synchronized void meshObjectRemoved(
            MeshObjectRemovedEvent event )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "meshObjectRemoved", event );
        }

        if( haveReceivedEventBefore( event )) {
            return;
        }

        TraversalActiveMMeshObjectSet.StartFromMeshObject sourceSet
                = (TraversalActiveMMeshObjectSet.StartFromMeshObject) event.getSource();
        MeshObject from = sourceSet.getStartOfTraversalMeshObject();

        MeshObject removed = event.getDeltaValue();

        ArrayList<MeshObject> removedMeshObjects = new ArrayList<MeshObject>();

        removeOrAdjust( removed, from, removedMeshObjects );

        Iterator<MeshObject> theIter = removedMeshObjects.iterator();
        while( theIter.hasNext() ) {
            MeshObject current = theIter.next();
            super.certainlyRemove( current );
        }
    }

    /**
     * Callback from one of the contained TraversalActiveMeshObjectSets -- ignore.
     *
     * @param event the event
     */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        // noop
    }

    /**
      * The MeshObject from which we started the traversal.
      */
    protected MeshObject rootOfTraversal;

    /**
     * The TraversalSpecification.
     */
    protected TraversalSpecification traversalSpec;

    /**
     * This map has the current content of the set as keys, and Mementos as values.
     */
    protected HashMap<MeshObject,Memento> theMap = new HashMap<MeshObject,Memento>();

    /**
     * This utility class collects all the info we need to memorize about any MeshObject
     * in this set.
     */
    protected class Memento
    {
        /**
         * Construct one. As this starts with one MeshObject, there is only one "from" parameter.
         *
         * @param reachableSet the set of MeshObjects reachable from from
         * @param number the number of steps
         * @param from the MeshObject from which we reached the reachableSet
         */
        public Memento(
                TraversalActiveMeshObjectSet reachableSet,
                int                          number,
                MeshObject                   from )
        {
            theReachableSet = reachableSet;
            numberSteps     = number;

            if( from != null ) {
                theReachedFrom.add( from );
            }
        }

        /**
         * Obtain the best-known number of steps from the root node to this node.
         *
         * @return the number of steps
         */
        public int getNumberSteps()
        {
            return numberSteps;
        }

        /**
         * We have learned of another way how to reach this.
         *
         * @param from the MeshObject from which this could also be reached
         * @param nSteps the number of steps it took
         */
        public void addReachableFrom(
                MeshObject from,
                int    nSteps )
        {
            if( theReachedFrom.contains( from )) {
                log.error( "have member already: " + from );
            }
            theReachedFrom.add( from );
            if( nSteps < numberSteps ) {
                numberSteps = nSteps;
            }
        }

        /**
         * One path to reach this has gone away.
         *
         * @param from the MeshObject from which this is not reachable any more
         */
        public void removeReachableFrom(
                MeshObject from )
        {
            boolean ok = theReachedFrom.remove( from );
            log.assertLog( ok, "element not in list" );
        }

        /**
         * Something changed in the graph, and as a result, we need to adjust our number steps.
         *
         * @param nSteps the number of steps is now this
         */
        public void adjustNumberStepsTo(
                int nSteps )
        {
            numberSteps = nSteps;
        }

        /**
         * Obtain a MeshObjectSet with the MeshObjects that are reachable from here.
         *
         * @return the MeshObjectSet with the MeshObjects that are reachable from here
         */
        public MeshObjectSet getReaches()
        {
            return theReachableSet;
        }


        /**
         * Obtain a Collection with the MeshObjects through which this one is reachable.
         *
         * @return the Collection with the MeshObjects through which this one is reachable
         */
        public Collection<MeshObject> getReachedFrom()
        {
            return theReachedFrom;
        }

        /**
         * The TraversalActiveMMeshObjectSet that goes with any particular member of the
         * TransitiveClosureTraversalActiveMMeshObjectSet. It specifies which other
         * members of the TransitiveClosureTraversalActiveMMeshObjectSet we can reach.
         */
        private TraversalActiveMeshObjectSet theReachableSet;

        /**
         * The best-known number of steps from the root to this node.
         */
        private int numberSteps;

        /**
         * The set of MeshObjects that can reach this MeshObject.
         */
        private ArrayList<MeshObject> theReachedFrom = new ArrayList<MeshObject>( 5 );
    }
}
