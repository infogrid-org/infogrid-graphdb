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
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSet;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * This ActiveMeshObjectSet has the same content as a passed-in ActiveMeshObjectSet,
 * but in an order specified by a passed-in sorting algorithm. If the optional depth
 * parameter is given, the resulting set will only contain the top depth MeshObjects.
 */
public class OrderedActiveMMeshObjectSet
    extends
        AbstractActiveMMeshObjectSet
    implements
        OrderedActiveMeshObjectSet,  // the "active" implementation
        ActiveMeshObjectSetListener, // to listen to our underlying set
        PropertyChangeListener       // to listen to our underlying set
{
    private static final Log log = Log.getLogInstance( OrderedActiveMMeshObjectSet.class ); // our own, private logger

    /**
     * Private constructor, use factory methods.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param underlyingSet the set whose content we ordered
     * @param sorter the Sorter that we use to order the content of the set
     * @param max determines how many MeshObjects will be contained by this set. If the underlyingSet contains more,
     *        this set will only contain the first max MeshObjects according to the sorter.
     */
    protected OrderedActiveMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObjectSet        underlyingSet,
            MeshObjectSorter     sorter,
            int                  max )
    {
        super( factory );

        theUnderlyingSet = underlyingSet;
        theSorter        = sorter;
        theMaximum       = max;

        MeshObject [] org = theUnderlyingSet.getMeshObjects();
        fullContent       = theSorter.getOrderedInNew( org );

        if( theMaximum != UNLIMITED && theMaximum < fullContent.length ) {
            setInitialContent( ArrayHelper.copyIntoNewArray( fullContent, 0, theMaximum, MeshObject.class ));
        } else {
            setInitialContent( fullContent );
        }

        if( theUnderlyingSet instanceof ActiveMeshObjectSet ) {
            ((ActiveMeshObjectSet)theUnderlyingSet).addWeakActiveMeshObjectSetListener( this );
        }
        theUnderlyingSet.addWeakContentPropertyChangeListener( this );
            // we have to always subscribe to the property events because our order can depend on anything
    }

    /**
     * Obtain an MeshObject at a particular index.
     *
     * @param index the index into this set
     * @return the MeshObject at this location.
     * @throws ArrayIndexOutOfBoundsException thrown if an illegal index is given
     */
    public final MeshObject getMeshObject(
            int index )
    {
        return getMeshObjects()[ index ];
    }

    /**
     * Obtain the first MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the first MeshObject, if any
     */
    public MeshObject getFirstMeshObject()
    {
        if( currentContent != null && currentContent.length > 0 ) {
            return currentContent[0];
        } else {
            return null;
        }
    }
    
    /**
     * Obtain the last MeshObject in the OrderedMeshObjectSet, or null if the OrderedMeshObjectSet is empty.
     * 
     * @return the last MeshObject, if any
     */
    public MeshObject getLastMeshObject()
    {
        if( currentContent != null && currentContent.length > 0 ) {
            return currentContent[currentContent.length-1];
        } else {
            return null;
        }
    }

    /**
     * Determine the index of a certain MeshObject in this ordered set.
     *
     * @param candidate the MeshObject that we are looking for
     * @return the index of the found MeshObject, or -1 if not found
     */
    public final int findIndexOf(
            MeshObject candidate )
    {
        MeshObject [] meshObjects = getMeshObjects();

        for( int i=0 ; i<meshObjects.length ; ++i ) {
            if( meshObjects[i] == candidate ) {
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
     * Create a subset of this set by providing a MeshObjectSelector that will select the MeshObjects
     * to be selected for the subset. This method will return all matches in this set, keeping the
     * order of this OrderedMeshObjectSet.
     *
     * @param selector the criteria for selection
     * @return subset of this set
     */
    @Override
    public OrderedMeshObjectSet subset(
            MeshObjectSelector selector )
    {
        return theFactory.createOrderedImmutableMeshObjectSet( getMeshObjects(), selector );
    }

    /**
     * Callback from the underlying set.
     *
     * @param event the event indicating a change in the underlying set
     */
    public synchronized void meshObjectAdded(
            MeshObjectAddedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        MeshObject added    = event.getDeltaValue();
        int        insertAt = theSorter.getIndexOfNew( added, fullContent );

        fullContent = ArrayHelper.insert( fullContent, added, insertAt, MeshObject.class );

        if( theMaximum == UNLIMITED || fullContent.length <= theMaximum ) {
            certainlyAdd( added, insertAt );

        } else if( insertAt < theMaximum ) {
            // one needs to fall out
            certainlyRemove( theMaximum-1 );
            certainlyAdd( added, insertAt );
        }
    }

    /**
     * Callback from the underlying set.
     *
     * @param event the event indicating a change in the underlying set
     */
    public void meshObjectRemoved(
            MeshObjectRemovedEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        MeshObject removed = event.getDeltaValue();

        int index = -1;
        for( int i=0 ; i<fullContent.length ; ++i ) {
            if( removed == fullContent[i] ) {
                index = i;
                break;
            }
        }
        fullContent = ArrayHelper.remove( fullContent, index, MeshObject.class );

        if( theMaximum != UNLIMITED && index >= theMaximum ) {
            return;
        }

        certainlyRemove( index );
        if( theMaximum != UNLIMITED && theMaximum <= fullContent.length ) {
            certainlyAdd( fullContent[ theMaximum-1 ], theMaximum-1 );
        }
    }

    /**
     * Callback from the underlying set. This des not
     * matter to us as we have our own ordering.
     *
     * @param event the event indicating a change in the underlying set
     */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        // noop
    }

    /**
     * Callback from the underlying set. We sort again, and then potentially do updates.
     * Unfortunately, we have to reorder every time because we have no idea what we
     * are looking for. So use with care.
     *
     * @param event the event indicating a change in the underlying set
     */
    @Override
    public void propertyChange(
            PropertyChangeEvent event )
    {
        if( haveReceivedEventBefore( event )) {
            return;
        }

        // FIXME: This can probably be made more efficient by specifying under which circumstances
        // we want to do this as part of the constructor of this class

        if( theMaximum == UNLIMITED ) {
            potentiallyReorder( theSorter );
            super.propertyChange( event );

        } else {
            fullContent = theSorter.getOrderedInNew( fullContent );

            MeshObject [] content = getMeshObjects();
            for( int i=0 ; i<content.length ; ++i ) {
                if( !ArrayHelper.isIn( content[i], fullContent, 0, theMaximum, false )) {
                    certainlyRemove( content[i] );
                }
            }

            int min = Math.min( theMaximum, fullContent.length );
            for( int i=0 ; i<min ; ++i ) {
                if( i >= size() || fullContent[i] != getMeshObject( i )) {
                    certainlyAdd( fullContent[i], i );
                }
            }
        }
        this.fireSetReordered( new OrderedActiveMeshObjectSetReorderedEvent( this ));
    }

    /**
     * This fires a OrderedTraversalPathSetReorderedEvent to the registered listeners.
     *
     * @param event the event
     */
    protected void fireSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        if( log.isInfoEnabled() ) {
            log.info( this + ".fireSetReordered( " + event + " )" );
        }

        theSetListeners.fireEvent( event );
    }

    /**
     * The maximum number of elements in the set.
     */
    protected int theMaximum;

    /**
     * The set whose content we re-order. Keep a reference so this won't get garbage collected.
     */
    protected MeshObjectSet theUnderlyingSet;

    /**
     * The specification of how to order.
     */
    protected MeshObjectSorter theSorter;

    /**
     * The full content of the set if theMaximum is not UNLIMITED.
     */
    protected MeshObject [] fullContent;
}
