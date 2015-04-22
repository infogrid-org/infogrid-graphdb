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

import org.infogrid.mesh.set.active.*;
import org.infogrid.mesh.set.TraversalPathSelector;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

import java.beans.PropertyChangeListener;
import java.util.Comparator;
import org.infogrid.mesh.set.MeshObjectSetFactory;

/**
 * <p>This is an active subset of another ActiveTraversalPathSet, as determined by the criteria
 * specified in the factory methods / constructors.</p>
 *
 * <p>If a TraversalPathSelector is specified, this set will contain exactly those elements of the
 * delegate set that pass muster of the TraversalPathSelector.
 * If a Comparator and a number N is specified, this set will contain the top-N elements
 * according to the Comparator's ordering.</p>
 *
 * <p>This class assumes that the criteria do not change their behavior over time.</p>
 */
public abstract class SelectiveActiveTraversalPathSet
        extends
            AbstractActiveMTraversalPathSet
        implements
            ActiveTraversalPathSetListener,
            PropertyChangeListener
{
    private static Log log = Log.getLogInstance( SelectiveActiveTraversalPathSet.class ); // our own, private logger

    /**
     * Private constructor, use factory method.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     * @param delegate the underlying ActiveTraversalPathSet from which we select
     */
    protected SelectiveActiveTraversalPathSet(
            MeshObjectSetFactory   factory,
            ActiveTraversalPathSet delegate )
    {
        super( factory );

        theDelegate = delegate;
    }

    /**
     * This indicates that the set was re-ordered. Only applies in case of
     * ordered sets.
     *
     * @param event the event
     */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        // noop
    }

    /**
     * The underlying set from where we take our content. Keep a reference so this
     * won't get garbage collected.
     */
    protected ActiveTraversalPathSet theDelegate;

    /**
     * This class implements the RootObjectSelector criteria.
     */
    static class SelectorCrit
            extends
                SelectiveActiveTraversalPathSet
    {
        /**
         * Private constructor, use factory method.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param delegate the underlying ActiveTraversalPathSet from which we select
         * @param selector the TraversalPathSelector determining which TraversalPaths we select of the underlying ActiveTraversalPathSet
         */
        protected SelectorCrit(
                MeshObjectSetFactory   factory,
                ActiveTraversalPathSet delegate,
                TraversalPathSelector  selector )
        {
            super( factory, delegate );

            theSelector = selector;

            theDelegate.addWeakActiveTraversalPathSetListener( this );
            theDelegate.addWeakContentPropertyChangeListener( this ); // FIXME? Too pessimistic?

            // determine initial content
            TraversalPath [] delegateContent = theDelegate.getTraversalPaths();
            TraversalPath [] ourContent      = new TraversalPath[ delegateContent.length ];

            int count = 0;
            for( int i=0 ; i<delegateContent.length ; ++i ) {
                if( theSelector.accepts( delegateContent[i] )) {
                    ourContent[count++] = delegateContent[i];
                }
            }
            if( count < ourContent.length ) {
                ourContent = ArrayHelper.copyIntoNewArray( ourContent, 0, count, TraversalPath.class );
            }
            super.setInitialContent( ourContent );
        }

        /**
          * Callback indicating that a TraversalPath was added to this set.
          *
          * @param event the event
          */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath path = event.getDeltaValue();

            if( theSelector.accepts( path )) {
                certainlyAdd( path );
            }
        }

        /**
          * Callback indicating that a TraversalPath was removed from this set.
          *
          * @param event the event
          */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath path = event.getDeltaValue();

            if( contains( path )) {
                certainlyRemove( path );
            }
        }

        /**
         * The TraversalPathSelector that determines which TraversalPaths to take from theDelegate.
         */
        protected TraversalPathSelector theSelector;
    }

    /**
     * This class implements the Comparator criteria.
     */
    static class ComparatorCrit
            extends
                SelectiveActiveTraversalPathSet
    {
        /**
         * Private constructor, use factory method.
         *
         * @param factory the MeshObjectSetFactory that created this TraversalPathSet
         * @param delegate the underlying ActiveTraversalPathSet from which we select
         * @param comp determines which are the top n TraversalPaths
         * @param number the maximum number of TraversalPaths in this set
         */
        protected ComparatorCrit(
                MeshObjectSetFactory      factory,
                ActiveTraversalPathSet    delegate,
                Comparator<TraversalPath> comp,
                int                       number )
        {
            super( factory, delegate );

            theComparator = comp;
            n             = number;

            theDelegate.addWeakActiveTraversalPathSetListener( this );
            theDelegate.addWeakContentPropertyChangeListener( this );

            // determine initial content
            TraversalPath [] delegateContent = theDelegate.getTraversalPaths();
            TraversalPath [] ourContent = constructContent( delegateContent, n, theComparator );

            super.setInitialContent( ourContent );
        }

        /**
          * Callback indicating that a TraversalPath was added to this set.
          *
          * @param event the event
          */
        public void traversalPathAdded(
                TraversalPathAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath path = event.getDeltaValue();

            TraversalPath [] ourContent = getTraversalPaths();
            TraversalPath removed = null;
            int addedAt = -1;

            // insert if necessary
            for( int i=0 ; i<ourContent.length ; ++i ) {
                if( theComparator.compare( path, ourContent[i] ) <= 0 ) {
                    // insert
                    if( ourContent.length < n ) {
                        ourContent = ArrayHelper.append( ourContent, (TraversalPath) null, TraversalPath.class );
                    } else {
                        removed = ourContent[ ourContent.length-1 ];
                    }

                    for( int j=ourContent.length-1 ; j>i ; --j ) {
                        ourContent[j] = ourContent[j-1];
                    }

                    ourContent[i] = path;
                    addedAt = i;
                    break;
                }
            }
            setInitialContent( ourContent );
            if( removed != null ) {
                super.fireTraversalPathRemoved( removed, ourContent.length-1 );
            }
            super.fireTraversalPathAdded( path, addedAt );
        }


        /**
          * Callback indicating that a TraversalPath was removed from this set.
          *
          * @param event the event
          */
        public void traversalPathRemoved(
                TraversalPathRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            TraversalPath obj = event.getDeltaValue();

            if( theDelegate.size() < n ) {
                certainlyRemove( obj );
            } else {
                // need to find who is next -- reassemble (fixme, inefficient)
                setInitialContent( constructContent( theDelegate.getTraversalPaths(), n, theComparator ));
            }
        }

        /**
         * Internal helper to construct the correct content for this set.
         *
         * @param delegateContent where we get the content from
         * @param number the number of TraversalPaths we want
         * @param comp determines which TraversalPaths to select
         * @return the top n=number TraversalPaths
         */
        protected static TraversalPath [] constructContent(
                TraversalPath []          delegateContent,
                int                       number,
                Comparator<TraversalPath> comp )
        {
            TraversalPath [] ret;

            if( delegateContent.length == 0 ) {
                ret = new TraversalPath[0];
            } else {
                ret = new TraversalPath[ delegateContent.length > number ? number : delegateContent.length ];

                int watermark = 0;
                ret[ watermark++ ] = delegateContent[0];

                for( int i=1 ; i<delegateContent.length ; ++i ) {
                    for( int j=0 ; j<watermark ; ++j ) {
                        if( comp.compare( delegateContent[i], ret[j] ) <= 0 ) {
                            // insert
                            if( watermark == ret.length ) {
                                --watermark; // prevent falling off the array
                            }
                            for( int k=watermark ; k>j; --k ) {
                                ret[k] = ret[k-1];
                            }
                            ++watermark;
                            ret[j] = delegateContent[i];
                            break;
                        }
                    }
                }
            }
            return ret;
        }

        /**
         * The Comparator which determines the ordering.
         */
        protected Comparator<TraversalPath> theComparator;

        /**
         * The number of TraversalPaths we want to have.
         */
        protected int n;
    }
}
