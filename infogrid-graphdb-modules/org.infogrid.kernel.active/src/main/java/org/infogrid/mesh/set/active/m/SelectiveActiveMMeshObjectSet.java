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
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;

import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;

import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.infogrid.mesh.set.MeshObjectSetFactory;

/**
 * <p>This is an active subset of another ActiveMeshObjectSet, as determined by the criteria
 * specified in the factory methods / constructors.</p>
 * 
 * <p>If a MeshObjectSelector is specified, this set will contain exactly those elements of the
 * delegate set that pass muster of the MeshObjectSelector.</p>
 * 
 * <p>This class assumes that the criteria do not change their behavior over time.</p>
 */
public abstract class SelectiveActiveMMeshObjectSet
    extends
        AbstractActiveMMeshObjectSet
    implements
        ActiveMeshObjectSetListener,
        PropertyChangeListener
{
    private static Log log = Log.getLogInstance( SelectiveActiveMMeshObjectSet.class  ); // our own, private logger

   /**
     * Private constructor, use factory method.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param delegate the underlying set from whose content we select
     */
    protected SelectiveActiveMMeshObjectSet(
            MeshObjectSetFactory factory,
            ActiveMeshObjectSet  delegate )
    {
        super( factory );

        theDelegate = delegate;
    }

    /**
     * Callback from our underlying set.
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

        MeshObjectPropertyChangeEvent realEvent = (MeshObjectPropertyChangeEvent) event;
        realEvent.setResolver( theFactory.getMeshBase() );
        
        MeshObject sender = realEvent.getAffectedMeshObject();

        if( contains( sender )) {
            firePropertyChange( event );
        }
    }

    /**
     * Callback from our underlying set.
     *
     * @param event the event
     */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        // noop
    }

    /**
     * The underlying set from where we take our content. Keep a reference so this
     * won't get garbage collected.
     */
    protected ActiveMeshObjectSet theDelegate;

    /**
     * This class implements the MeshObjectSelector criteria.
     */
    static class SelectorCrit
            extends
                SelectiveActiveMMeshObjectSet
    {
        /**
         * Private constructor, use factory method.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param delegate the underlying set from whose content we select
         * @param selector the selection criteria
         */
        protected SelectorCrit(
                MeshObjectSetFactory factory,
                ActiveMeshObjectSet  delegate,
                MeshObjectSelector   selector )
        {
            super( factory, delegate );

            theSelector = selector;

            theDelegate.addWeakActiveMeshObjectSetListener( this );
            theDelegate.addWeakContentPropertyChangeListener( this );

            // determine initial content
            MeshObject [] delegateContent = theDelegate.getMeshObjects();
            MeshObject [] ourContent      = new MeshObject[ delegateContent.length ];

            int count = 0;
            for( int i=0 ; i<delegateContent.length ; ++i ) {
                if( theSelector.accepts( delegateContent[i] )) {
                    ourContent[count++] = delegateContent[i];
                }
            }
            if( count < ourContent.length ) {
                ourContent = ArrayHelper.copyIntoNewArray( ourContent, 0, count, MeshObject.class );
            }

            super.setInitialContent( ourContent );
        }

        /**
         * Callback from our underlying set.
         *
         * @param event the event
         */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject obj = event.getDeltaValue();

            if( theSelector.accepts( obj )) {
                certainlyAdd( obj );
            }
        }

        /**
         * Callback from our underlying set.
         *
         * @param event the event
         */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            MeshObject obj = event.getDeltaValue();

            if( contains( obj )) {
                certainlyRemove( obj );
            }
        }

        /**
         * The MeshObjectSelector that determines which Entities to take from theDelegate.
         */
        protected MeshObjectSelector theSelector;
    }
}
