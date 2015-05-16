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
import java.beans.PropertyChangeListener;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.mesh.set.TraversalPathSorter;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.ActiveTraversalPathSetListener;
import org.infogrid.mesh.set.active.OrderedActiveTraversalPathSet;
import org.infogrid.mesh.set.active.TraversalPathAddedEvent;
import org.infogrid.mesh.set.active.TraversalPathRemovedEvent;
import org.infogrid.mesh.set.active.OrderedTraversalPathSetReorderedEvent;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayHelper;

/**
 * <p>This ActiveTraversalSet has the same content as a passed-in ActiveActiveTraversalSet,
 * but in an order specified by a passed-in sorting algorithm.</p>
 *
 * <p>FIXME: We subscribe to PropertyChangeEvents from all members of all TraversalPaths,
 * in order to be able to sort on anything -- however, the way this is implemented
 * (one "sort" per event of the whole set), this is very inefficent.</p>
 */
public class OrderedActiveMTraversalPathSet
        extends
            AbstractActiveMTraversalPathSet
        implements
            OrderedActiveTraversalPathSet,
            ActiveTraversalPathSetListener,
            PropertyChangeListener
{
    /**
     * Construct one with the underlying set, and the specification of how to order.
     *
     * @param underlyingSet the ActiveTraversalPathSet from which we take our content
     * @param sorter the TraversalPathSorter determining the ordering sequence
     * @param max determines how many TraversalPaths will be contained by this set. If the underlyingSet contains more,
     *        this set will only contain the first max TraversalPaths according to the sorter.
     */
    protected OrderedActiveMTraversalPathSet(
            ActiveTraversalPathSet underlyingSet,
            TraversalPathSorter    sorter,
            int                    max )
    {
        super( underlyingSet.getFactory() );

        theUnderlyingSet = underlyingSet;
        theSorter        = sorter;
        theMaximum       = max;

        theUnderlyingSet.addWeakActiveTraversalPathSetListener( this );

        TraversalPath [] underlyingPaths = theUnderlyingSet.getTraversalPaths();

        if( theMaximum != UNLIMITED && theMaximum < underlyingPaths.length ) {
            setInitialContent( theSorter.getOrderedInNew(
                    ArrayHelper.copyIntoNewArray( underlyingPaths, 0, theMaximum, TraversalPath.class )));
        } else {
            setInitialContent( theSorter.getOrderedInNew( underlyingPaths ));
        }

    }

    /**
     * Obtain the nth TraversalPath in this set.
     *
     * @param index the index of the TraversalPath that we want to obtain
     * @return the TraversalPath at that index
     */
    public TraversalPath getTraversalPath(
            int index )
    {
        return super.getTraversalPaths()[ index ];
    }

    /**
     * Determine the index of a certain TraversalPath in this ordered set.
     * Generally, index == findIndexOf( TraversalPath( index )).
     *
     * @param candidate the TraversalPath that we are looking for in this set
     * @return the index of the found TraversalPath, or -1 if not found
     */
    public int findIndexOf(
            TraversalPath candidate )
    {
        TraversalPath [] paths = getTraversalPaths();

        for( int i=0 ; i<paths.length ; ++i ) {
            if( paths[i] == candidate ) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtain the maximum number of elements in ths set.
     * 
     * @return the maximum number of elements in the set, or UNLIMITED
     */
    public int getMaximumElements()
    {
        return theMaximum;
    }

    /**
     * Set a different TraversalPathSorter. This will most likely cause a traversalPathSetReordered event.
     *
     * @param newSorter the new TraversalPathSorter for this OrderedActiveTraversalPathSet
     */
    public void setSortOrder(
            TraversalPathSorter newSorter )
    {
        if( theSorter == newSorter ) {
            return;
        }

        theSorter = newSorter;

        super.potentiallyReorder( theSorter );

        fireSetReordered( new OrderedTraversalPathSetReorderedEvent( this ));
    }

    /**
     * Obtain the destinations of the contained TraversalPaths as a MeshObjectSet.
     * While the same MeshObject may be a destination of more than one contained
     * TraversalPath, the MeshObjectSet naturally only contains this MeshObject once.
     *
     * @return the destinations of the contained TraversalPaths as a MeshObjectSet
     */
    @Override
    public OrderedMeshObjectSet getDestinationsAsSet()
    {
        return (OrderedMeshObjectSet) super.getDestinationsAsSet();
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
    @Override
    public OrderedMeshObjectSet getStepAsSet(
            int index )
    {
        return (OrderedMeshObjectSet) super.getStepAsSet( index );
    }

    /**
     * Callback from our underlying set.
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
        added.addWeakTraversalPathPropertyChangeListener( this );

        TraversalPath [] newContent = theSorter.getOrderedInNew( theUnderlyingSet.getTraversalPaths() );
        TraversalPath [] oldContent = getTraversalPaths();

        int indexOfAdded = oldContent.length;
        for( int i=0 ; i<oldContent.length ; ++i ) {
            if( oldContent[i] != newContent[i] ) {
                // was inserted here
                indexOfAdded = i;
                break;
            }
        }
        certainlyAdd( added, indexOfAdded );
    }

    /**
     * Callback from our underlying set.
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
        removed.removeTraversalPathPropertyChangeListener( this );

        certainlyRemove( removed );
    }

    /**
     * We are told that the set was reordered -- ignore.
     *
     * @param event the event
     */
    public void orderedTraversalPathSetReordered(
            OrderedTraversalPathSetReorderedEvent event )
    {
        // noop
    }

    /**
     * Callback from one of the Entities that we subscribed to.
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
        
        if( theMaximum == UNLIMITED ) {
            potentiallyReorder( theSorter );

        } else {
            fullContent = theSorter.getOrderedInNew( fullContent );

            TraversalPath [] content = getTraversalPaths();
            for( int i=0 ; i<content.length ; ++i ) {
                if( !ArrayHelper.isIn( content[i], fullContent, 0, theMaximum, false )) {
                    certainlyRemove( content[i] );
                }
            }

            int min = Math.min( theMaximum, fullContent.length );
            for( int i=0 ; i<min ; ++i ) {
                if( i >= size() || fullContent[i] != getTraversalPath( i )) {
                    certainlyAdd( fullContent[i], i );
                }
            }
        }
        fireSetReordered( new OrderedTraversalPathSetReorderedEvent( this ));    
        super.propertyChange( event );
    }

    /**
     * The maximum number of elements in the set.
     */
    protected int theMaximum;

    /**
     * The set whose content we re-order. Keep a reference so this won't get garbage collected.
     */
    protected ActiveTraversalPathSet theUnderlyingSet;

    /**
     * The specification of how to order.
     */
    protected TraversalPathSorter theSorter;

    /**
     * The full content of the set if theMaximum is not UNLIMITED.
     */
    protected TraversalPath [] fullContent;
}
