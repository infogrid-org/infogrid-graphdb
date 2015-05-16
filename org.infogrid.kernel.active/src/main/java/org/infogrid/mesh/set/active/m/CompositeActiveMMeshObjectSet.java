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
import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetListener;
import org.infogrid.mesh.set.active.CompositeActiveMeshObjectSet;
import org.infogrid.mesh.set.active.MeshObjectAddedEvent;
import org.infogrid.mesh.set.active.MeshObjectRemovedEvent;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSetReorderedEvent;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.util.ArrayHelper;

/**
 * <p>This in-memory ActiveMeshObjectSet is calculated as a composition of other
 * MeshObjectSets. The type of composition can be specified by
 * choosing an appropriate factory method.</p>
 *
 * <p>Factory methods do not require operands to be ActiveMeshObjectSets as well. This allows
 *    the construction of composite sets even only some of the operands are active.
 * 
 * <p>The result will be an ActiveMeshObjectSet as well.</p>
 */
public abstract class CompositeActiveMMeshObjectSet
        extends
            AbstractActiveMMeshObjectSet
        implements
            CompositeActiveMeshObjectSet,
            ActiveMeshObjectSetListener
{
    /**
     * Private constructor, use factory methods.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param operands the sets from which this set is constructed
     */
    protected CompositeActiveMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObjectSet []     operands )
    {
        super( factory );

        theOperands = operands;

        for( int i=0 ; i<theOperands.length ; ++i ) {
            if( theOperands[i] instanceof ActiveMeshObjectSet ) {
                ((ActiveMeshObjectSet)theOperands[i]).addWeakActiveMeshObjectSetListener( this );
                // we only subscribe to property change events if we know we need them
            }
        }
    }

    /**
     * Obtain the set of operands for this set.
     *
     * @return the set of operands for this set
     */
    public final MeshObjectSet [] getOperands()
    {
        return theOperands;
    }

    /**
      * Callback from one of our operand sets, but we don't care about OrderedActiveMeshObjectSetReorderedEvent.
      *
      * @param event the event
      */
    public void orderedMeshObjectSetReordered(
            OrderedActiveMeshObjectSetReorderedEvent event )
    {
        // noop
    }

    /**
     * A PropertyChangeEvent was forwarded by one of our operands. We keep forwarding
     * if we contain the sender.
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
        if( !( event instanceof Change )) {
            return;
        }
        Change realEvent = (Change) event;
        realEvent.setResolver( theFactory.getMeshBase() );

        MeshObject sender = realEvent.getAffectedMeshObject();

        if( contains( sender )) {
            firePropertyChange( event );
        }
    }

    /**
     * This is a hook that is invoked when the number of content PropertyChangeListeners
     * goes from zero to one. Subclasses can override this to do special initializations
     * to forward PropertyChangeEvents.
     */
    @Override
    protected void gainedFirstContentPropertyChangeListener()
    {
        for( int i=0 ; i<theOperands.length ; ++i ) {
            theOperands[i].addWeakContentPropertyChangeListener( this );
        }
    }

    /**
     * This is a hook that is invoked when the number of content PropertyChangeListeners
     * goes from one to zero. Subclasses can override this to do special cleanup
     * to stop forwarding PropertyChangeEvents.
     */
    @Override
    protected void lostLastContentPropertyChangeListener()
    {
        for( int i=0 ; i<theOperands.length ; ++i ) {
            theOperands[i].removeContentPropertyChangeListener( this );
        }
    }

    /**
     * The operands of this set.
     */
    protected MeshObjectSet [] theOperands;

    /**
     * An ActiveMeshObjectSet that is constructed as a unification of the content of other MeshObjectSets.
     */
    public static class Unification
            extends
                CompositeActiveMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param operands the sets from which this set is constructed
         * @param selector determines which candidates are included
         */
        protected Unification(
                MeshObjectSetFactory factory,
                MeshObjectSet []     operands,
                MeshObjectSelector   selector )
        {
            super( factory, operands );

            // create currentContent
            ArrayList<MeshObject> temp = new ArrayList<MeshObject>( theOperands.length * 5 ); // fudge
            for( int i=0 ; i<theOperands.length ; ++i ) {
                MeshObject [] childEntities = theOperands[i].getMeshObjects();
                for( int j=0 ; j<childEntities.length ; ++j ) {
                    if( ( selector == null || selector.accepts( childEntities[j] )) && !temp.contains( childEntities[j] )) {
                        temp.add( childEntities[j] );
                    }
                }
            }
            setInitialContent( ArrayHelper.copyIntoNewArray( temp, MeshObject.class ));
        }

        /**
          * A MeshObject was added by any of our operands.
          *
          * @param event the event
          */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }
            potentiallyAdd( event.getDeltaValue() );
        }

        /**
          * A MeshObject was removed from any of our operands.
          *
          * @param event the event
          */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }
            potentiallyRemove( event.getDeltaValue() );
        }
    }

    /**
     * An ActiveMeshObjectSet that is constructed as an intersection of the content of other MeshObjectSets.
     */
    public static class Intersection
            extends
                CompositeActiveMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param operands the sets from which this set is constructed
         * @param selector determines which candidates are included
         */
        protected Intersection(
                MeshObjectSetFactory factory,
                MeshObjectSet []     operands,
                MeshObjectSelector   selector )
        {
            super( factory, operands );

            MeshObject [] initialContent = ArrayHelper.copyIntoNewArray(
                    theOperands[0].getMeshObjects(),
                    MeshObject.class ); // shorten later, this is max

            if( selector != null ) {
                for( int i=0 ; i<initialContent.length ; ++i ) {
                    if( !selector.accepts( initialContent[i] )) {
                        initialContent[i] = null;
                    }
                }
            }
            
            int takenOut = 0;
            for( int i=1; i<theOperands.length ; ++i ) {
                MeshObject [] thisContent = theOperands[i].getMeshObjects();
                for( int j=0 ; j<initialContent.length ; ++j ) {
                    MeshObject testObject = initialContent[j];
                    if( testObject == null ) {
                        continue; // was removed previously
                    }

                    if( !ArrayHelper.isIn( testObject, thisContent, false )) {
                        initialContent[j] = null;
                        ++takenOut;
                    }
                }
            }
            if( takenOut > 0 ) {
                MeshObject [] old = initialContent;
                initialContent    = new MeshObject[ old.length - takenOut ];
                for( int i=0, j=0 ; i<old.length ; ++i ) {
                    if( old[i] != null ) {
                        initialContent[j++] = old[i];
                    }
                }
            }

            setInitialContent( initialContent );
        }

        /**
          * A MeshObject was added by any of our operands.
          *
          * @param event the event
          */
        public void meshObjectAdded(
                MeshObjectAddedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            // check that it is contained everywhere, and if so, add
            MeshObject added = event.getDeltaValue();

            for( int i=0 ; i<theOperands.length ; ++i ) {
                if( !ArrayHelper.isIn( added, theOperands[i].getMeshObjects(), false )) {
                    return; // skip, not there
                }
            }
            certainlyAdd( added );
        }

        /**
          * A MeshObject was removed from any of our operands.
          *
          * @param event the event
          */
        public void meshObjectRemoved(
                MeshObjectRemovedEvent event )
        {
            if( haveReceivedEventBefore( event )) {
                return;
            }

            // if we contain this already, remove
            certainlyRemove( event.getDeltaValue() );
        }
    }
}
