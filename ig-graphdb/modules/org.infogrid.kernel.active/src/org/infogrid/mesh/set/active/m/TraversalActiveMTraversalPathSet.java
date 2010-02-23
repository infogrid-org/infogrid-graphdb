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
import java.util.HashMap;
import java.util.Iterator;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.mesh.set.active.TraversalActiveTraversalPathSet;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
  * <p>The TraversalPaths in this ActiveTraversalPathSet are obtained by traversing a
  * TraversalSpecification from a given MeshObject. But instead of being a "snapshot"
  * of the result, this active set continues to expand and shrink as the relationships
  * and their types in which this MeshObject participates change through actions by
  * other parts of the program.
  * By listening to the appropriate events, one can monitor this set.</p>
  *
  * <p>To instantiate, use static factory methods on the respective subclasses.</p>
  */
public abstract class TraversalActiveMTraversalPathSet
        extends
            AbstractActiveMTraversalPathSet
        implements
            TraversalActiveTraversalPathSet
{
    private static final Log log = Log.getLogInstance( TraversalActiveMTraversalPathSet.class  ); // our own, private logger

    /**
     * Private constructor, use factory.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     */
    protected TraversalActiveMTraversalPathSet(
            MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * Intermediate abstract superclass for all implementations that start with a MeshObject.
     */
    protected static abstract class StartFromMeshObject
        extends
            TraversalActiveMTraversalPathSet
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         */
        protected StartFromMeshObject(
                MeshObjectSetFactory factory,
                MeshObject           start )
        {
            super( factory );

            startOfTraversal = start;

            if( startOfTraversal == null ) {
                throw new IllegalArgumentException( "no start given for constructor" );
            }
        }

        /**
          * Obtain the MeshObject from which we started the traversal.
          *
          * @return the MeshObject from which we started the traversal
          */
        public MeshObject getStartOfTraversal()
        {
            return startOfTraversal;
        }

        /**
          * Obtain the MeshObjectSet from which we started the traversal.
          *
          * @return the MeshObjectSet from which we started the traversal
          */
        public MeshObjectSet getStartOfTraversalSet()
        {
            return theFactory.createSingleMemberImmutableMeshObjectSet( startOfTraversal );
        }

        /**
          * The MeshObject from which we started the traversal.
          */
        protected MeshObject startOfTraversal;
    }

    /**
     * Intermediate abstract superclass for all implementations that start with a MeshObjectSet.
     */
    protected static abstract class StartFromMeshObjectSet
        extends
            TraversalActiveMTraversalPathSet
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param startSet the MeshObjectSet from which we start the traversal
         */
        public StartFromMeshObjectSet(
                MeshObjectSetFactory factory,
                MeshObjectSet        startSet )
        {
            super( factory );

            startOfTraversalSet = startSet;

            if( startOfTraversalSet == null ) {
                throw new IllegalArgumentException( "no root given for constructor" );
            }
        }

        /**
          * Obtain the MeshObjectSet from which we started the traversal.
          *
          * @return the MeshObjectSet from which we started the traversal
          */
        public MeshObjectSet getStartOfTraversalSet()
        {
            return startOfTraversalSet;
        }

        /**
          * The MeshObjectSet from which we started the traversal. Keep a reference to
          * prevent that this is being garbage collected.
          */
        protected MeshObjectSet startOfTraversalSet;
    }

    /**
     * Intermediate abstract superclass for all implementations that start with a TraversalPathSet.
     */
    protected static abstract class StartFromTraversalPathSet
        extends
            TraversalActiveMTraversalPathSet
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param startSet the TraversalPathSet from which we start the traversal
         */
        public StartFromTraversalPathSet(
                MeshObjectSetFactory factory,
                TraversalPathSet     startSet )
        {
            super( factory );

            startOfTraversalSet = startSet;

            if( startOfTraversalSet == null ) {
                throw new IllegalArgumentException( "no root given for constructor" );
            }
        }

        /**
          * Obtain the TraversalPathSet from which we started the traversal.
          *
          * @return the TraversalPathSet from which we started the traversal
          */
        public MeshObjectSet getStartOfTraversalSet()
        {
            return startOfTraversalSet.getDestinationsAsSet();
        }

        /**
          * Obtain the TraversalPathSet from which we started the traversal.
          *
          * @return the TraversalPathSet from which we started the traversal
          */
        public TraversalPathSet getStartOfTraversalTraversalPathSet()
        {
            return startOfTraversalSet;
        }

        /**
          * The TraversalPathSet from which we started the traversal. Keep a reference to
          * prevent that this is being garbage collected.
          */
        protected TraversalPathSet startOfTraversalSet;
    }

    /**
     * This is the implementation for a set that only contains itself.
     */
    protected static class ToSelfFromMeshObject
            extends
                StartFromMeshObject
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         */
        protected ToSelfFromMeshObject(
                MeshObjectSetFactory   factory,
                MeshObject             start )
        {
            super( factory, start );
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return StayRightHereTraversalSpecification.create();
        }
    }

    /**
     * This is the implementation for a set that knows it is always going to be empty.
     */
    protected static class MeshObjectEmpty
            extends
                StartFromMeshObject
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected MeshObjectEmpty(
                MeshObjectSetFactory   factory,
                MeshObject             start,
                TraversalSpecification spec )
        {
            super( factory, start );

            theTraversalSpecification = spec;

            setInitialContent( new TraversalPath[0] );
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Our TraversalSpecification.
         */
        protected TraversalSpecification theTraversalSpecification;
    }

    /**
     * This implements a single TraversalSpecification step from a MeshObject.
     */
    protected static class OneStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                PropertyChangeListener, // so we can listen to the appropriate events
                CanBeDumped
    {
        /**
         * Private constructor, use factory.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected OneStepFromMeshObject(
                MeshObjectSetFactory   factory,
                MeshObject             start,
                TraversalSpecification spec )
        {
            super( factory, start );
            
            theSpec = spec;

            MeshObject [] currentObjects = startOfTraversal.traverse( theSpec ).getMeshObjects();

            TraversalPath [] content = new TraversalPath[ currentObjects.length ];
            for( int i=0 ; i<currentObjects.length ; ++i ) {
                content[i] = TraversalPath.create( theSpec, currentObjects[i] );
            }
            setInitialContent( content );

            startOfTraversal.addWeakPropertyChangeListener( this );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
         * Callback from our start MeshObject.
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

            if( getTraversalPaths() == null ) {
                return; // ignore events as long as we are not initialized
            }

            if( startOfTraversal == null ) {
                return; // ignore residual events that are still sent after we are dead.
                        // I spent a long time debugging this, and it turns out that
                        // a MeshObject's firePropertyChange method may indirectly
                        // cause a die() to be invoked on this object, but then still
                        // send a propertyChangeEvent to it afterwards, because the listener
                        // list has been cloned before firing the event. So this is fine.
            }

            Change realEvent = (Change) event;
            
            realEvent.setResolver( theFactory.getMeshBase() );

            MeshObject sender = realEvent.getAffectedMeshObject( );

            if( startOfTraversal.equals( sender )) {
                // we are only interested in RolePlayerTable events
                if( event instanceof MeshObjectRoleChangeEvent ) {
                    MeshObjectRoleChangeEvent realRealEvent = (MeshObjectRoleChangeEvent) event;
                    MeshBase                  meshBase      = startOfTraversal.getMeshBase();

                    // but only in the right MetaRoles
                    if( ! theSpec.isAffectedBy( meshBase, realRealEvent )) {
                        return;
                    }

                    if( realRealEvent instanceof MeshObjectRoleAddedEvent ) {
                        MeshObjectRoleAddedEvent realRealRealEvent = (MeshObjectRoleAddedEvent) realRealEvent;

                        MeshObject added = realRealRealEvent.getNeighborMeshObject();

                        // add to set
                        TraversalPath addedPath = TraversalPath.create( theSpec, added );
                        certainlyAdd( addedPath );

                    } else if( realRealEvent instanceof MeshObjectRoleRemovedEvent ) {
                        MeshObjectRoleRemovedEvent realRealRealEvent = (MeshObjectRoleRemovedEvent) realRealEvent;

                        MeshObject removed = realRealRealEvent.getNeighborMeshObject();

                        TraversalPath [] content = getTraversalPaths();

                        // remove from set
                        TraversalPath removedPath  = null;
                        int           removedIndex = -1;
                        for( int i=0 ; i<content.length ; ++i ) {
                            if( content[i].getFirstMeshObjectIdentifier().equals( removed.getIdentifier() )) {
                                removedPath  = content[i];
                                removedIndex = i;
                                break;
                            }
                        }
                        log.assertLog( removedPath, "path not found" );

                        certainlyRemove( removedIndex );
                    } else {
                        log.error( "Unknown event type " + realEvent );
                    }
                }
            } else {
                firePropertyChange( event );
            }
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theSpec;
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
                        "startOfTraversal",
                        "spec",
                        "current"
                    },
                    new Object[] {
                        startOfTraversal,
                        theSpec,
                        getTraversalPaths()
                    });
        }

        /**
         * The TraversalSpecification that we traverse.
         */
        protected TraversalSpecification theSpec;
    }

    /**
     * This is the implementation for a single SelectiveTraversalSpecification.
     */
    protected static class SelectiveStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                ActiveTraversalPathSetListener
    {
        /**
         * Private constructor for subclasses only.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected SelectiveStepFromMeshObject(
                MeshObjectSetFactory            factory,
                MeshObject                      start,
                SelectiveTraversalSpecification spec )
        {
            super( factory, start );

            theTraversalSpecification = spec;
            theEndSelector            = theTraversalSpecification.getEndSelector();

            childMonitor = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet( start, spec.getQualifiedTraversalSpecification() );
            childMonitor.addWeakActiveTraversalPathSetListener( this );

            TraversalPath [] children = childMonitor.getTraversalPaths();
            boolean       [] take     = new boolean[ children.length ];
            int count = 0;

            for( int i=0 ; i<children.length ; ++i ) {
                if( theEndSelector.accepts( children[i].getLastMeshObject() )) {
                    take[i] = true;
                    ++count;
                }
            }

            TraversalPath [] content = new TraversalPath[ count ];
            count = 0;
            for( int i=0 ; i<children.length ; ++i ) {
                if( take[i] ) {
                    content[ count++ ] = children[i];
                }
            }
            setInitialContent( content );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from our child monitor.
         *
         * @param event the event
         */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalSetAdded", event );
            }
            
            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            TraversalPath candidate = event.getDeltaValue();

            if( theEndSelector.accepts( candidate.getLastMeshObject() )) {
                certainlyAdd( candidate );
            }
        }

        /**
         * Callback from our child monitor.
         *
         * @param event the event
         */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalPathRemoved", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            // find in currentContent and remove
            TraversalPath [] content = getTraversalPaths();
            TraversalPath    removed = event.getDeltaValue();

            for( int i=0 ; i<content.length ; ++i ) {
                if( content[i] == removed ) {
                    certainlyRemove( i );
                    break;
                }
            }
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedTraversalPathSetReordered(
                OrderedTraversalPathSetReorderedEvent event )
        {
            log.warn( "unexpected call: " + this + ", event: " + event );
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected SelectiveTraversalSpecification theTraversalSpecification;

        /**
         * Convenience variable containing theTraversalSpecification.getEndSelector().
         */
        protected MeshObjectSelector theEndSelector;

        /**
         * The child TraversalActiveTraversalPathSet to which we delegate the traversal part of
         * the SelectiveTraversalSpecification.
         */
        protected TraversalActiveTraversalPathSet childMonitor;
    }

    /**
     * This is the implementation for a multiple-step monitor. It instantiates separate
     * monitors for each of its steps.
     */
    protected static class MultiStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                ActiveTraversalPathSetListener,
                PropertyChangeListener, // so we can listen to the appropriate events
                CanBeDumped
    {
        /**
         * Private constructor, use factory.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected MultiStepFromMeshObject(
                MeshObjectSetFactory                     factory,
                MeshObject                               start,
                SequentialCompoundTraversalSpecification spec )
        {
            super( factory, start );

            theTraversalSpecification = spec;
            theRemainingSpecification = spec.withoutFirstStep();

            MeshObject [] children = start.traverse(
                   theTraversalSpecification.getFirstStep() ).getMeshObjects();

            childMonitors = new TraversalActiveTraversalPathSet[ children.length ];

            for( int i=0 ; i<children.length ; ++i ) {
                childMonitors[i] = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet(
                        children[i],
                        theRemainingSpecification );
                childMonitors[i].addWeakActiveTraversalPathSetListener( this );
            }

            start.addWeakPropertyChangeListener( this );

            // count first
            int count = 0;
            for( int i=0 ; i<childMonitors.length ; ++i ) {
                count += childMonitors[i].size();
            }

            TraversalPath [] content = new TraversalPath[ count ];
            count = 0;
            for( int i=0 ; i<childMonitors.length ; ++i ) {
                TraversalPath [] childPaths = childMonitors[i].getTraversalPaths();

                for( int j=0 ; j<childPaths.length ; ++j ) {
                    content[ count++ ] = TraversalPath.create(
                            theTraversalSpecification.getFirstStep(),
                            children[i],
                            childPaths[j] );
                }
            }
            setInitialContent( content );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from the rootOfTraversal.
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

            if( startOfTraversal == null ) {
                return; // ignore residual events that are still sent after we are dead.
                        // I spent a long time debugging this, and it turns out that
                        // a MeshObject's firePropertyChange method may indirectly
                        // cause a die() to be invoked on this object, but then still
                        // send a propertyChangeEvent to it afterwards, because the listener
                        // list has been cloned before firing the event. So this is fine.
            }

            Change realEvent = (Change) event;
            realEvent.setResolver( theFactory.getMeshBase() );

            MeshObject sender = realEvent.getAffectedMeshObject();

            if( startOfTraversal.equals( sender )) {
                // we are only interested in RolePlayerTable events
                if( event instanceof MeshObjectRoleChangeEvent ) {
                    MeshObjectRoleChangeEvent realRealEvent = (MeshObjectRoleChangeEvent) event;
                    MeshBase                  meshBase      = startOfTraversal.getMeshBase();

                    // but only in the right MetaRoles
                    if( ! theTraversalSpecification.getFirstStep().isAffectedBy( meshBase, realRealEvent )) {
                        return;
                    }

                    if( realRealEvent instanceof MeshObjectRoleAddedEvent ) {
                        MeshObjectRoleAddedEvent realRealRealEvent = (MeshObjectRoleAddedEvent) realRealEvent;

                        MeshObject added = realRealRealEvent.getNeighborMeshObject();

                        // create child monitor and add to set
                        TraversalActiveTraversalPathSet addedMonitor = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet(
                                added,
                                theRemainingSpecification );
                        addedMonitor.addWeakActiveTraversalPathSetListener( this );
                        childMonitors = ArrayHelper.append( childMonitors, addedMonitor, TraversalActiveTraversalPathSet.class  );

                        // update currentContent, we don't have to watch for duplicates
                        TraversalPath [] addedContent = addedMonitor.getTraversalPaths();

                        for( int i=0 ; i<addedContent.length ; ++i ) {
                            certainlyAdd( TraversalPath.create(
                                    theTraversalSpecification.getFirstStep(),
                                    added,
                                    addedContent[i] ));
                        }
                    } else if( realRealEvent instanceof MeshObjectRoleRemovedEvent ) {
                        MeshObjectRoleRemovedEvent realRealRealEvent = (MeshObjectRoleRemovedEvent) realRealEvent;

                        MeshObject removed = realRealRealEvent.getNeighborMeshObject();
                        
                        // find monitor that goes with this
                        TraversalActiveTraversalPathSet monitorToRemove = null;
                        for( int i=0 ; i<childMonitors.length ; ++i ) {
                            if( removed == ((StartFromMeshObject)childMonitors[i]).startOfTraversal ) {
                                monitorToRemove = childMonitors[i];
                                break;
                            }
                        }
                        log.assertLog( monitorToRemove, "could not find child monitor" );

                        monitorToRemove.removeActiveTraversalPathSetListener( this );
                        childMonitors = ArrayHelper.remove( childMonitors, monitorToRemove, true, TraversalActiveTraversalPathSet.class  );

                        // update currentContent, we don't need to watch for duplicates
                        TraversalPath [] removedChildContent = monitorToRemove.getTraversalPaths();
                        TraversalPath [] content             = getTraversalPaths();

                        for( int i=0 ; i<content.length ; ++i ) {
                            boolean found = false;
                            for( int j=0 ; j<removedChildContent.length ; ++j ) {
                                if( content[i].getNextSegment() == removedChildContent[j] ) {
                                    certainlyRemove( content[i] ); // use object not index to be careful
                                    break;
                                }
                            }
                        }
                    } else {
                        log.error( "unknown event " + realEvent );
                    }
                }
            } else {
                super.firePropertyChange( event );
            }
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalSetAdded", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            TraversalSpecification travPrefix       = theTraversalSpecification.getFirstStep();
            MeshObject             meshObjectPrefix = ((StartFromMeshObject) event.getSource()).startOfTraversal;

            TraversalPath newMember = TraversalPath.create(
                    travPrefix,
                    meshObjectPrefix,
                    event.getDeltaValue() );
            certainlyAdd( newMember );
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalPathRemoved", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            TraversalPath [] content = getTraversalPaths();

            // find in currentContent and remove
            for( int i=0 ; i<content.length ; ++i ) {
                if( content[i].getNextSegment() == event.getDeltaValue() ) {
                    certainlyRemove( i );
                    break;
                }
            }
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedTraversalPathSetReordered(
                OrderedTraversalPathSetReorderedEvent event )
        {
            log.warn( "unexpected call: " + this + ", event: " + event );
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
                        "rootOfTraversal",
                        "theTraversalSpecification",
                        "current",
                        "childMon"
                    },
                    new Object[] {
                        startOfTraversal,
                        theTraversalSpecification,
                        getTraversalPaths(),
                        childMonitors
                    });
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected SequentialCompoundTraversalSpecification theTraversalSpecification;

        /**
         * A convenience variable containing theTraversalSpecification without
         * the first element.
         */
        protected TraversalSpecification theRemainingSpecification;

        /**
         * The set of child monitors that we delegate monitoring the remaining N-1 component TraversalSpecifications to.
         */
        protected TraversalActiveTraversalPathSet [] childMonitors;
    }

    /**
     * This is the implementation for a monitor that's an aggregate from multiple
     * child monitors.
     *
     * FIXME? We make the vastly simplifying (but realistic?) assumption that our child monitors will
     * not produce duplicates
     */
    protected static class ParallelStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                ActiveTraversalPathSetListener
    {
        /**
         * Private constructor, use factory.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param start the MeshObject from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected ParallelStepFromMeshObject(
                MeshObjectSetFactory                      factory,
                MeshObject                                start,
                AlternativeCompoundTraversalSpecification spec )
        {
            super( factory, start );

            theTraversalSpecification = spec;
            theChildSpecifications    = spec.getAlternatives();

            childMonitors = new TraversalActiveTraversalPathSet[ theChildSpecifications.length ];

            for( int i=0 ; i<theChildSpecifications.length ; ++i ) {
                childMonitors[i] = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet(
                        startOfTraversal,
                        theChildSpecifications[i] );
                childMonitors[i].addWeakActiveTraversalPathSetListener( this );
            }

            // count first
            int count = 0;
            for( int i=0 ; i<childMonitors.length ; ++i ) {
                count += childMonitors[i].size();
            }

            TraversalPath [] content = new TraversalPath[ count ];
            count = 0;
            for( int i=0 ; i<childMonitors.length ; ++i ) {
                TraversalPath [] childPaths = childMonitors[i].getTraversalPaths();

                for( int j=0 ; j<childPaths.length ; ++j ) {
                    content[ count++ ] = childPaths[j];
                }
            }

            setInitialContent( content );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalSetAdded", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            TraversalPath newMember = event.getDeltaValue();
            certainlyAdd( newMember );
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "traversalPathRemoved", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            if( getTraversalPaths() == null ) {
                return; // we are not initialized yet -- we can get callbacks as part of our children being constructed
            }

            TraversalPath removed = event.getDeltaValue();

            certainlyRemove( removed );
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedTraversalPathSetReordered(
                OrderedTraversalPathSetReorderedEvent event )
        {
            log.warn( "unexpected call: " + this + ", event: " + event );
        }

        /**
         * Convert to String, for debugging only.
         *
         * @return string representation of this object
         */
        @Override
        public String toString()
        {
            return "<"
                    + super.toString()
                    + "{ root: "
                    + startOfTraversal
                    + ", roles: "
                    + theTraversalSpecification
                    + ", current: "
                    + ArrayHelper.arrayToString( getTraversalPaths() )
                    + ", childMon: "
                    + ArrayHelper.arrayToString( childMonitors )
                    + "}>";
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected AlternativeCompoundTraversalSpecification theTraversalSpecification;

        /**
         * A convenience variable containing the TraversalSpecifications contained in the
         * theTraversalSpecification.
         */
        protected TraversalSpecification [] theChildSpecifications;

        /**
         * The set of child monitors that we delegate monitoring the remaining N-1 components of the TraversalSpecification to.
         */
        protected TraversalActiveTraversalPathSet [] childMonitors;
    }

    /**
     * This implementation (minus a child factory method) collects results from child sets.
     * It also manages child sets if the start set grows or shrinks.
     */
    public static class MeshObjectUnifier
            extends
                StartFromMeshObjectSet
            implements
                ActiveTraversalPathSetListener,
                ActiveMeshObjectSetListener // so we notice when the start set changes
    {
        /**
         * Private constructor, use factory.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param startSet the MeshObjectSet from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected MeshObjectUnifier(
                MeshObjectSetFactory   factory,
                MeshObjectSet          startSet,
                TraversalSpecification spec )
        {
            super( factory, startSet );

            theTraversalSpecification = spec;

            if( startSet instanceof ActiveMeshObjectSet ) {
                ((ActiveMeshObjectSet)startSet).addWeakActiveMeshObjectSetListener( this );
            }

            MeshObject []      startObjects   = startSet.getMeshObjects();
            TraversalPath [][] initialContent = new TraversalPath[ startObjects.length ][];
            int count = 0;

            theChildrenMap = new HashMap<MeshObject,TraversalActiveTraversalPathSet>( startSet.size() * 3 / 2 ); // fudge
            for( int i=0 ; i<startObjects.length ; ++i ) {
                TraversalActiveTraversalPathSet childSet   = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet( startObjects[i], theTraversalSpecification );
                TraversalPath []                newContent = childSet.getTraversalPaths();

                initialContent[i] = newContent;
                count += newContent.length;

                childSet.addWeakActiveTraversalPathSetListener( this );
                theChildrenMap.put( startObjects[i], childSet );
            }

            TraversalPath [] content = new TraversalPath[ count ];
            for( int i=initialContent.length-1 ; i>=0 ; --i ) {
                for( int j=initialContent[i].length-1 ; j>=0 ; --j ) {
                    content[--count] = initialContent[i][j];
                }
            }
            setInitialContent( content );
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath added = event.getDeltaValue();

            certainlyAdd( added );
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath removed = event.getDeltaValue();

            certainlyRemove( removed );
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedTraversalPathSetReordered(
                OrderedTraversalPathSetReorderedEvent event )
        {
            log.warn( "not implemented" );
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            // new start object added
            MeshObject added = event.getDeltaValue();

            TraversalActiveTraversalPathSet childSet = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet(
                    added,
                    theTraversalSpecification );

            childSet.addWeakActiveTraversalPathSetListener( this );
            theChildrenMap.put( added, childSet );

            TraversalPath [] newContent = childSet.getTraversalPaths();
            for( int i=0 ; i<newContent.length ; ++i ) {
                certainlyAdd( newContent[i] );
            }
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            // start object removed
            MeshObject removed = event.getDeltaValue();

            TraversalActiveTraversalPathSet childSet = theChildrenMap.remove( removed );
            childSet.removeActiveTraversalPathSetListener( this );

            TraversalPath [] removeContent = childSet.getTraversalPaths();

            for( int i=0 ; i<removeContent.length ; ++i ) {
                certainlyRemove( removeContent[i] );
            }
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedMeshObjectSetReordered(
                OrderedActiveMeshObjectSetReorderedEvent event )
        {
            // noop
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected TraversalSpecification theTraversalSpecification;

        /**
         * The set of child monitors that we use, one per member of the start set.
         * This maps from MeshObject (as member of the start set) to TraversalActiveTraversalPathSet.
         */
        protected HashMap<MeshObject,TraversalActiveTraversalPathSet> theChildrenMap;
    }

    /**
     * This implementation (minus a child factory method) collects results from child sets.
     * It also manages child sets if the start set grows or shrinks.
     */
    public static class PathUnifier
            extends
                StartFromTraversalPathSet
            implements
                ActiveTraversalPathSetListener
    {
        /**
         * Private constructor, use factory.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param startSet the MeshObjectSet from which we start the traversal
         * @param spec the TraversalSpecification to be used
         */
        protected PathUnifier(
                MeshObjectSetFactory   factory,
                TraversalPathSet       startSet,
                TraversalSpecification spec )
        {
            super( factory, startSet );

            theTraversalSpecification = spec;

            if( startSet instanceof ActiveTraversalPathSet ) {
                ((ActiveTraversalPathSet)startSet).addWeakActiveTraversalPathSetListener( this );
            }

            theUnderlyingSet = startSet;

            TraversalPath []   startPaths     = startSet.getTraversalPaths();
            TraversalPath [][] initialContent = new TraversalPath[ startPaths.length ][];
            int count = 0;

            theReverseChildrenMap = new HashMap<TraversalPathSet,TraversalPath>( startPaths.length * 3 / 2 );

            for( int i=0 ; i<startPaths.length ; ++i ) {
                MeshObject pivotMeshObject = startPaths[i].getLastMeshObject();

                TraversalActiveTraversalPathSet childSet   = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet( pivotMeshObject, theTraversalSpecification );
                TraversalPath []                newContent = childSet.getTraversalPaths();

                initialContent[i] = new TraversalPath[ newContent.length ];

                for( int j=0 ; j<newContent.length ; ++j ) {
                    initialContent[i][j] = TraversalPath.create( startPaths[i], newContent[j] );
                }

                count += newContent.length;

                childSet.addWeakActiveTraversalPathSetListener( this );

                theReverseChildrenMap.put( childSet, startPaths[i] );
            }

            TraversalPath [] content = new TraversalPath[ count ];
            for( int i=initialContent.length-1 ; i>=0 ; --i )
            {
                for( int j=initialContent[i].length-1 ; j>=0 ; --j ) {
                    content[--count] = initialContent[i][j];
                }
            }
            setInitialContent( content );
        }

        /**
          * Obtain the TraversalSpecification that has been specified in the constructor.
          *
          * @return the TraversalSpecification that has been specified in the constructor
          */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            ActiveTraversalPathSet source = event.getSource();
            TraversalPath          added  = event.getDeltaValue();

            if( theUnderlyingSet == source ) {
                MeshObject pivotMeshObject = added.getLastMeshObject();

                TraversalActiveTraversalPathSet childSet   = ((ActiveMeshObjectSetFactory)theFactory).createActiveTraversalPathSet( pivotMeshObject, theTraversalSpecification );
                TraversalPath []                newContent = childSet.getTraversalPaths();

                childSet.addWeakActiveTraversalPathSetListener( this );
                theReverseChildrenMap.put( childSet, added );

                for( int i=0 ; i<newContent.length ; ++i ) {
                    certainlyAdd( TraversalPath.create( added, newContent[i] ));
                }
            } else {
                TraversalPath basePath = theReverseChildrenMap.get( source );

                certainlyAdd( TraversalPath.create( basePath, added ));
            }
        }

        /**
         * Callback from a child monitor.
         *
         * @param event the event
         */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            ActiveTraversalPathSet source  = event.getSource();
            TraversalPath          removed = event.getDeltaValue();

            if( theUnderlyingSet == source ) {
                TraversalPath basePath;
                TraversalPathSet removedSet;

                Iterator<TraversalPathSet> iter = theReverseChildrenMap.keySet().iterator();
                while( iter.hasNext() ) {
                    TraversalPathSet key   = iter.next();
                    TraversalPath    value = theReverseChildrenMap.get( key );

                    if( value.equals( removed )) {
                        removedSet = key;
                        basePath   = value;

                        theReverseChildrenMap.remove( key );

                        certainlyRemoveAllStartingWith( basePath );

                        break;
                    }
                }
            } else {
                TraversalPath basePath = theReverseChildrenMap.get( source );
                if( basePath != null ) {
                    certainlyRemove( basePath, removed );
                }
                // FIXME? This if here looks suspicious -- but if we don't have it here, ActiveTraversalPathSetTest2
                // will produce errors upon delete. I suspect that the "certainlyRemoveAllStartingWith()" just
                // about 10 lines above from here did our job already, but I'm not definite.
            }
        }

        /**
         * Callback from our child monitor. Ignored for now -- FIXME?
         *
         * @param event the event
         */
        public void orderedTraversalPathSetReordered(
                OrderedTraversalPathSetReorderedEvent event )
        {
            log.warn( "not implemented" );
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected TraversalSpecification theTraversalSpecification;

        /**
         * The set that we are watching.
         */
        protected TraversalPathSet theUnderlyingSet;

        /**
         * This maps from the child TraversalPathSet to the base TraversalPath by which
         * we reached it.
         */
        protected HashMap<TraversalPathSet,TraversalPath> theReverseChildrenMap;
    }
}
