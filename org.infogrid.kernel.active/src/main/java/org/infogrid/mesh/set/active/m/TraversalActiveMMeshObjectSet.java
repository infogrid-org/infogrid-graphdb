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

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.mesh.set.active.TraversalActiveMeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
 * <p>The MeshObjects in this ActiveMeshObjectSet are obtained by traversing a
 *    TraversalSpecification from a given MeshObject or a given MeshObjectSet.</p>
 * 
 * <p>But instead of being a "snapshot" of the MeshObjects currently related to the
 *    MeshObject, this ActiveMeshObjectSet continues to expand and shrink as the relationships
 *    related to the MeshObjects change through continued modification of the MeshObject graph.</p>
 * 
 * <p>This may forward duplicate PropertyChangeEvents under some circumstances
 *    when the same MeshObject is being reached through multiple paths.</p>
 */
public abstract class TraversalActiveMMeshObjectSet
        extends
            AbstractActiveMMeshObjectSet
        implements
            TraversalActiveMeshObjectSet
{
    private static final Log log = Log.getLogInstance(TraversalActiveMMeshObjectSet.class); // our own, private logger

    /**
     * Private constructor, for subclasses only.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     */
    protected TraversalActiveMMeshObjectSet(
            MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * Obtain the MeshObjectSet from where we started the traversal.
     *
     * @return the MeshObjectSet from where we started this traversal
     */
    public abstract MeshObjectSet getStartOfTraversalSet();

    /**
     * Obtain the TraversalSpecification that was used to determine the content of this set.
     *
     * @return the TraversalSpecification that was used to determine the content of this set
     */
    public abstract TraversalSpecification getTraversalSpecification();

    /**
      * One of our child monitors tells us that it re-ordered -- ignore.
      *
      * @param theEvent ignored event
      */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent theEvent )
    {
        // noop
    }

    /**
     * This class is the superclass of all implementations that start their traversal from one
     * single MeshObject.
     */
    public static abstract class StartFromMeshObject
            extends
                TraversalActiveMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         */
        protected StartFromMeshObject(
                ActiveMeshObjectSetFactory factory,
                MeshObject                 start )
        {
            super( factory );

            startMeshObject = start;
        }

        /**
         * Obtain the single MeshObject from where we started the traversal.
         *
         * @return the MeshObject from where we started the traversal
         */
        public final MeshObject getStartOfTraversalMeshObject()
        {
            return startMeshObject;
        }

        /**
         * Obtain the MeshObjectSet from where we started the traversal.
         *
         * @return the MeshObjectSet from where we started this traversal
         */
        public final MeshObjectSet getStartOfTraversalSet()
        {
            if( startSet == null ) {
                startSet = theFactory.createSingleMemberImmutableMeshObjectSet( startMeshObject );
            }
            return startSet;
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "startMeshObject",
                        "startSet"
                    },
                    new Object[] {
                        startMeshObject,
                        startSet
                    } );
        }

        /**
         * The start MeshObject from where we started the traversal.
         */
        protected MeshObject startMeshObject;

        /**
         * The demand-allocated MeshObjectSet from where we started the traversal.
         */
        protected MeshObjectSet startSet;
    }

    /**
     * This class is the superclass of all implementations that start their traversal from
     * a MeshObjectSet.
     */
    public static abstract class StartFromMeshObjectSet
            extends
                TraversalActiveMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         */
        protected StartFromMeshObjectSet(
                ActiveMeshObjectSetFactory factory,
                MeshObjectSet              start )
        {
            super( factory );

            startSet = start;
        }

        /**
         * Obtain the MeshObjectSet from where we started the traversal.
         *
         * @return the MeshObjectSet from where we started this traversal
         */
        public final MeshObjectSet getStartOfTraversalSet()
        {
            return startSet;
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "startSet"
                    },
                    new Object[] {
                        startSet
                    } );
        }

        /**
         * The MeshObjectSet from which we started the traversal.
         */
        protected MeshObjectSet startSet;
    }

    /**
     * This is a somewhat degenerate TraversalActiveMMeshObjectSet: it contains the start object(s) as
     * its sole content. It's content never changes.
     */
    public static class ToSelfFromMeshObject
            extends
                StartFromMeshObject
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         */
        protected ToSelfFromMeshObject(
                ActiveMeshObjectSetFactory factory,
                MeshObject                 start )
        {
            super( factory, start );

            setInitialContent( new MeshObject[]{ start } );
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return null
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return StayRightHereTraversalSpecification.create();
        }
    }

    /**
     * This is a somewhat degenerate TraversalActiveMMeshObjectSet: it contains the start object(s) as
     * its sole content. It's content never changes.
     */
    public static class ToSelfFromMeshObjectSet
            extends
                StartFromMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         */
        protected ToSelfFromMeshObjectSet(
                ActiveMeshObjectSetFactory factory,
                MeshObjectSet              start )
        {
            super( factory, start );

            setInitialContent( start.getMeshObjects() ); // FIXME? If this is active, should we follow?
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return null
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return StayRightHereTraversalSpecification.create();
        }
    }

    /**
     * This is a TraversalActiveMMeshObjectSet implementation whose content is determined by a single
     * RoleType traversal from a single MeshObject.
     */
    public static class OneStepFromMeshObject
            extends
                StartFromMeshObject
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         * @param spec the one step in the traversal
         */
        protected OneStepFromMeshObject(
                ActiveMeshObjectSetFactory factory,
                MeshObject                 start,
                TraversalSpecification     spec )
        {
            super( factory, start );

            theSpec = spec;

            MeshObject [] initialContent = startMeshObject.traverse( theSpec ).getMeshObjects();

            startMeshObject.addWeakPropertyChangeListener( this );

            setInitialContent( initialContent );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
         * Callback from either our start MeshObject or a MeshObject in currentContent.
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
            if( !( event instanceof Change )) {
                return;
            }

            Change realEvent = (Change) event;
            realEvent.setResolver( theFactory.getMeshBase() );
            
            MeshObject sender = realEvent.getAffectedMeshObject();
            if( startMeshObject.equals( sender )) {
                // we are only interested in RolePlayerTable events
                if( event instanceof MeshObjectRoleChangeEvent ) {
                    MeshObjectRoleChangeEvent realRealEvent = (MeshObjectRoleChangeEvent) event;

                    MeshBase meshBase = startMeshObject.getMeshBase();
                   
                    // but only in the right RoleTypes
                    if( ! theSpec.isAffectedBy( meshBase, realRealEvent )) {
                        return;
                    }

                    if( realRealEvent instanceof MeshObjectRoleAddedEvent ) {
                        MeshObjectRoleAddedEvent realRealRealEvent = (MeshObjectRoleAddedEvent) realRealEvent;
                        MeshObject               added             = realRealRealEvent.getNeighborMeshObject();

                        potentiallyAdd( added );
                       
                    } else if( realRealEvent instanceof MeshObjectRoleRemovedEvent ) {
                        MeshObjectRoleRemovedEvent realRealRealEvent = (MeshObjectRoleRemovedEvent) realRealEvent;
                        MeshObject                 removed           = realRealRealEvent.getNeighborMeshObject();
                       
                        potentiallyRemove( removed );

                    } else {
                        log.error( "unexpected event type " + realEvent );
                    }
                }
            } else {
                // normal behavior
                super.propertyChange( event );
            }
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "start",
                        "spec",
                        "current"
                    },
                    new Object[] {
                        startMeshObject,
                        theSpec,
                        getMeshObjects()
                    });
        }

        /**
         * Obtain the TraversalSpecification we used to construct the content of this set.
         *
         * @return the TraversalSpecification we used to construct the content of this set
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return theSpec;
        }

        /**
         * The TraversalSpecification used to construct the content of this set.
         */
        protected TraversalSpecification theSpec;
    }

    /**
     * This is the implementation for a single-step filtering TraversalActiveMMeshObjectSet.
     */
    public static class SelectiveStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                ActiveMeshObjectSetListener
    {
        /**
         * Private constructor, use factory methods.
         * We don't do anything about the start selector, because if that one does not accept us,
         * the factory method will have prevented the creation of this instance and returned an empty set instead.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         * @param spec the TraversalSpecification to use
         * @param childSet the underlying MeshObjectSet from which we select
         */
        protected SelectiveStepFromMeshObject(
                ActiveMeshObjectSetFactory      factory,
                MeshObject                      start,
                SelectiveTraversalSpecification spec,
                ActiveMeshObjectSet             childSet )
        {
            super( factory, start );

            if( spec.getStartSelector() != null && !spec.getStartSelector().accepts( start )) {
                throw new IllegalArgumentException( "factory method should not have let this through" );
            }
            theTraversalSpecification = spec;
            theEndSelector            = theTraversalSpecification.getEndSelector();
            theChildSet               = childSet;

            theChildSet.addWeakContentPropertyChangeListener( this );
            theChildSet.addWeakActiveMeshObjectSetListener( this );

            MeshObject []  children = theChildSet.getMeshObjects();
            boolean [] take     = new boolean[ children.length ];
            int count = 0;

            for( int i=0 ; i<children.length ; ++i ) {
                if( theEndSelector.accepts( children[i] )) {
                    take[i] = true;
                    ++count;
                }
            }

            MeshObject [] initialContent = new MeshObject[ count ];
            count = 0;
            for( int i=0 ; i<children.length ; ++i ) {
                if( take[i] ) {
                    initialContent[ count++ ] = children[i];
                }
            }

            setInitialContent( initialContent );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return the TraversalSpecification that was used to determine the content of this set
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from either our start MeshObject or a MeshObject in currentContent.
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
            if( !( event instanceof Change )) {
                return;
            }

            Change realEvent = (Change) event;
            realEvent.setResolver( theFactory.getMeshBase() );

            MeshObject source = realEvent.getAffectedMeshObject();

            if( theEndSelector.accepts( source )) {
                if( contains( source )) {
                    super.propertyChange( event );
                } else {
                    certainlyAdd( source );
                }

            } else if( contains( source )) {
                certainlyRemove( source );
            }
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "meshObjectAdded", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject obj = event.getDeltaValue();

            if( theEndSelector.accepts( obj )) {
                certainlyAdd( obj );
            }
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "meshObjectRemoved", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject obj = event.getDeltaValue();

            if( theEndSelector.accepts( obj )) {
                certainlyRemove( obj );
            }
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "startMeshObject",
                        "startSet",
                        "traversalSpecification",
                        "endSelector",
                        "childSet"
                    },
                    new Object[] {
                        startMeshObject,
                        startSet,
                        theTraversalSpecification,
                        theEndSelector,
                        theChildSet
                    } );
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
         * The underlying set from which we select.
         */
        protected ActiveMeshObjectSet theChildSet;
    }

    /**
     * This implementation (minus a child factory method) collects results from child sets.
     * It also manages child sets if the start MeshObject start grows or shrinks
     */
    public static class Unifier
            extends
                StartFromMeshObjectSet
            implements
                ActiveMeshObjectSetListener
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObjectSet from which we started the traversal
         * @param spec the TraversalSpecification to use
         * @param startSelector the MeshObjectSelector to be applied to MeshObjects in the start MeshObjectSet, if any
         */
        protected Unifier(
                ActiveMeshObjectSetFactory factory,
                MeshObjectSet              start,
                TraversalSpecification     spec,
                MeshObjectSelector         startSelector )
        {
            super( factory, start );

            theTraversalSpecification = spec;
            theStartSelector          = startSelector;

            if( startSet instanceof ActiveMeshObjectSet ) {
                ((ActiveMeshObjectSet)startSet).addWeakActiveMeshObjectSetListener( this );
            }

            MeshObject []   startObjects   = startSet.getMeshObjects();
            MeshObject [][] initialContent = new MeshObject[ startObjects.length ][];

            theChildrenMap = new HashMap<MeshObject,TraversalActiveMeshObjectSet>( startSet.size() * 3 / 2 ); // fudge
            for( int i=0 ; i<startObjects.length ; ++i ) {
                TraversalActiveMeshObjectSet childSet = ((ActiveMeshObjectSetFactory)theFactory).createActiveMeshObjectSet(
                        startObjects[i],
                        theTraversalSpecification );

                MeshObject [] newContent = childSet.getMeshObjects();
                initialContent[i] = newContent;

                childSet.addWeakActiveMeshObjectSetListener( this );
                childSet.addWeakContentPropertyChangeListener( this );
                theChildrenMap.put( startObjects[i], childSet );
            }
            setInitialContent( initialContent );
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return the TraversalSpecification that was used to determine the content of this set
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Callback from either our start MeshObject or a MeshObject in currentContent.
         *
         * @param event the event
         */
        @Override
        public void propertyChange(
                PropertyChangeEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            super.propertyChange( event );
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject added = event.getDeltaValue();
            if( event.getSource() == startSet ) {
                // new start object added
                TraversalActiveMeshObjectSet childSet = ((ActiveMeshObjectSetFactory)theFactory).createActiveMeshObjectSet(
                        added,
                        theTraversalSpecification );

                childSet.addWeakActiveMeshObjectSetListener( this );
                childSet.addWeakContentPropertyChangeListener( this );
                theChildrenMap.put( added, childSet );

                MeshObject [] newContent = childSet.getMeshObjects();
                for( int i=0 ; i<newContent.length ; ++i ) {
                    potentiallyAdd( newContent[i] );
                }

            } else {
                // object added to child set
                potentiallyAdd( added );
            }
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject removed = event.getDeltaValue();
            if( event.getSource() == startSet ) {
                // start object removed
                TraversalActiveMMeshObjectSet childSet = (TraversalActiveMMeshObjectSet) theChildrenMap.remove( removed );
                childSet.removeContentPropertyChangeListener( this );
                childSet.removeActiveMeshObjectSetListener( this );

                MeshObject [] removeContent = childSet.getMeshObjects();
                for( int i=0 ; i<removeContent.length ; ++i ) {
                    potentiallyRemove( removeContent[i] );
                }

            } else {
                // object removed from child set
                potentiallyRemove( removed );
            }
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "startSet",
                        "traversalSpecification",
                        "childrenMap",
                        "startSelector"
                    },
                    new Object[] {
                        startSet,
                        theTraversalSpecification,
                        theChildrenMap,
                        theStartSelector
                    } );
        }

        /**
         * The TraversalSpecification used to construct the content of this set.
         */
        protected TraversalSpecification theTraversalSpecification;

        /**
         * The set of child monitors that use, one per member of the start set.
         * This maps from MeshObject (as member of the start set) to TraversalActiveMMeshObjectSet.
         */
        protected HashMap<MeshObject,TraversalActiveMeshObjectSet> theChildrenMap;

        /**
         * This determines whether or not a new member in the start set is
         * going to be accepted for normal functinality, or ignored. This may
         * be null, meaning we accept all new members.
         */
        protected MeshObjectSelector theStartSelector;
    }

    /**
     * This is the implementation for an AlternativeCompoundTraversalSpecification starting
     * with one MeshObject.
     */
    public static class ParallelStepFromMeshObject
            extends
                StartFromMeshObject
            implements
                ActiveMeshObjectSetListener
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         * @param spec the TraversalSpecification to use
         * @param startSelector the MeshObjectSelector to be applied to MeshObjects in the start MeshObjectSet, if any
         */
        protected ParallelStepFromMeshObject(
                ActiveMeshObjectSetFactory                 factory,
                MeshObject                                 start,
                AlternativeCompoundTraversalSpecification  spec,
                MeshObjectSelector                         startSelector ) // FIXME: is startSelector used?
        {
            super( factory, start );

            theTraversalSpecification = spec;

            theChildSpecifications = spec.getAlternatives();

            theChildSets = new TraversalActiveMeshObjectSet[ theChildSpecifications.length ];

            // determine child monitors, and maximum length of arrays
            int max = 0;
            for( int i=0 ; i<theChildSpecifications.length ; ++i ) {
                theChildSets[i] = ((ActiveMeshObjectSetFactory)theFactory).createActiveMeshObjectSet(
                        start,
                        theChildSpecifications[i] );

                theChildSets[i].addWeakContentPropertyChangeListener( this );
                theChildSets[i].addWeakActiveMeshObjectSetListener( this );

                max += theChildSets[i].size();
            }

            // allocate, but potentially shorten later
            MeshObject [] initialContent         = new MeshObject[ max ];
            int    [] initialContentCounters = new int[ max ];

            max = 0;
            for( int i=0 ; i<theChildSpecifications.length ; ++i ) {
                MeshObject [] addedContent = theChildSets[i].getMeshObjects();

                for( int j=0 ; j<addedContent.length ; ++j ) {
                    boolean found = false;
                    for( int k=0 ; k<max ; ++k ) {
                        if( addedContent[j] == initialContent[k] ) {
                            found = true;
                            ++initialContentCounters[k];
                            break;
                        }
                    }
                    if( !found ) {
                        initialContent[ max ] = addedContent[j];
                        initialContentCounters[ max ] = 0;
                        ++max;
                    }
                }
            }

            // do we have to shorten?
            if( max < initialContent.length ) {
                MeshObject [] newContent  = new MeshObject[ max ];
                int []    newCounters = new int[ max ];

                for( int i=0 ; i<max ; ++i ) {
                    newContent[i]  = initialContent[i];
                    newCounters[i] = initialContentCounters[i];
                }
                initialContent         = newContent;
                initialContentCounters = newCounters;
            }

            setInitialContent( initialContent, initialContentCounters );

            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "constructor" );
            }
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return the TraversalSpecification that was used to determine the content of this set
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "meshObjectAdded", event );
            }
            
            if( haveReceivedEventBefore( event )) {
                return;
            }

            potentiallyAdd( event.getDeltaValue() );
        }

        /**
         * Our underlying set notifies us of a change.
         *
         * @param event the event
         */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( log.isTraceEnabled() ) {
                log.traceMethodCallEntry( this, "meshObjectRemoved", event );
            }

            if( haveReceivedEventBefore( event )) {
                return;
            }

            potentiallyRemove( event.getDeltaValue());
        }

        /**
         * Dump this object.
         *
         * @param d the Dumper to dump to
         */
        @Override
        public void dump(
                Dumper d )
        {
            d.dump( this,
                    new String[] {
                        "startMeshObject",
                        "startSet",
                        "traversalSpecification",
                        "childSpecifications",
                        "childSets"
                    },
                    new Object[] {
                        startMeshObject,
                        startSet,
                        theTraversalSpecification,
                        theChildSpecifications,
                        theChildSets
                    } );
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected AlternativeCompoundTraversalSpecification theTraversalSpecification;

        /**
         * A convenience variable containing theTraversalSpecification contained in the
         * theTraversalSpecification.
         */
        protected TraversalSpecification [] theChildSpecifications;

        /**
         * The set of child monitors that we delegate our alternative TraversalSpecifications to.
         */
        protected TraversalActiveMeshObjectSet[] theChildSets;
    }

    /**
     * This TraversalActiveMMeshObjectSet is known to be always empty.
     */
    public static class EmptyFromMeshObject
            extends
                StartFromMeshObject
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param start the MeshObject from which we started the traversal
         * @param spec the TraversalSpecification to use
         */
        protected EmptyFromMeshObject(
                ActiveMeshObjectSetFactory factory,
                MeshObject                 start,
                TraversalSpecification     spec )
        {
            super( factory, start );

            theTraversalSpecification = spec;

            setInitialContent( new MeshObject[0] );
        }

        /**
         * Obtain the TraversalSpecification that was used to determine the content of this set.
         *
         * @return the TraversalSpecification that was used to determine the content of this set
         */
        public TraversalSpecification getTraversalSpecification()
        {
            return theTraversalSpecification;
        }

        /**
          * The TraversalSpecification that has been specified in the constructor.
          */
        protected TraversalSpecification theTraversalSpecification;
    }
}
