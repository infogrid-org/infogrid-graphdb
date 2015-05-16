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

package org.infogrid.modelbase.m;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.MeshTypeCreatedEvent;
import org.infogrid.modelbase.MeshTypeLifecycleEvent;
import org.infogrid.modelbase.MeshTypeLifecycleEventListener;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.util.logging.Log;

/**
  * This stores a working model, in memory. A working model is the set of SubjectAreas currently needed
  * by some application. In-memory implementation.
  */
public class MMeshTypeStore
{
    private static final Log  log              = Log.getLogInstance( MMeshTypeStore.class ); // our own, private logger
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Construct one.
      *
      * @param base the MModelBase to which we belong
      */
    MMeshTypeStore(
            MModelBase base )
    {
        this.theModelBase = base;
    }

    /**
      * Obtain an iterator that iterates over all MeshTypes contained in this MeshTypeCluster.
      *
      * @return an iterator that iterates over all MeshTypes contained in this MeshTypeCluster
      */
    public Iterator<MeshType> iterator()
    {
        return new ClusterIterator();
    }

    /**
     * Obtain an iterator that traverses over all Subject Areas contained in this MeshTypeCluster.
     *
     * @return an iterator that traverses over all Subject Areas contained in this MeshTypeCluster
     */
    Iterator<SubjectArea> subjectAreaIterator()
    {
        return theSubjectAreas.values().iterator();
    }

    /**
      * Subscribe to events indicating the addition/removal/etc of MeshTypes.
      *
      * @param newListener the listener to be added
      * @see #removeMeshTypeLifecycleEventListener
      */
    public synchronized void addMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener newListener )
    {
        if( theLifecycleEventListeners == null ) {
            theLifecycleEventListeners = new ArrayList<MeshTypeLifecycleEventListener>();
        }
        theLifecycleEventListeners.add( newListener );
    }

    /**
      * Unsubscribe from events indicating the addition/removal/etc of MeshTypes.
      *
      * @param oldListener the listener to be removed
      * @see #addMeshTypeLifecycleEventListener
      */
    public synchronized void removeMeshTypeLifecycleEventListener(
            MeshTypeLifecycleEventListener oldListener )
    {
        theLifecycleEventListeners.remove( oldListener );
    }

    /**
      * Let MMeshTypeLifecycleManager add a SubjectArea.
      *
      * @param theObject the SubjectArea to be added
      */
    void addObject(
            SubjectArea theObject )
    {
        StringValue version = theObject.getVersionNumber();
        
        KeyInTable<String,String> theKey = new KeyInTable<String,String>(
                theObject.getName().value(),
                version != null ? version.value() : null );

        theSubjectAreas.put( theKey, theObject );
        allMeshTypes.put( theObject.getIdentifier(), theObject );

        notifyElementAdded( theObject );
    }

    /**
      * Let MMeshTypeLifecycleManager add an AttributableMeshType.
      *
      * @param theObject the AttributableMeshType to be added
      */
    void addObject(
            AttributableMeshType theObject )
    {
        SubjectArea sa = theObject.getSubjectArea();

        KeyInTable<SubjectArea,String> theKey = new KeyInTable<SubjectArea,String>(
                sa,
                theObject.getName().value() );

        theAttributableMeshTypes.put( theKey, theObject );
        allMeshTypes.put( theObject.getIdentifier(), theObject );

        notifyElementAdded( theObject );
    }

    /**
      * Let MMeshTypeLifecycleManager add a PropertyType.
      *
      * @param theObject the PropertyType to be added
      */
    void addObject(
            PropertyType theObject )
    {
        AttributableMeshType amo = theObject.getAttributableMeshType();

        KeyInTable<AttributableMeshType,String> theKey = new KeyInTable<AttributableMeshType,String>(
                amo,
                theObject.getName().value() );

        thePropertyTypes.put( theKey, theObject );
        allMeshTypes.put( theObject.getIdentifier(), theObject );

        notifyElementAdded( theObject );
    }

    /**
      * Let MMeshTypeLifecycleManager add a PropertyTypeGroup.
      *
      * @param theObject the PropertyTypeGroup to be added
      */
    void addObject(
            PropertyTypeGroup theObject )
    {
        allMeshTypes.put( theObject.getIdentifier(), theObject );
    }

    /**
      * Find a SubjectArea. No wildcards etc. are allowed.
      *
      * @param subjectAreaName the fully-qualified name of the SubjectArea
      * @param subjectAreaVersionNumber the version number of the SubjectArea
      * @return the found SubjectArea, or null
      */
    SubjectArea findSubjectArea(
            String subjectAreaName,
            String subjectAreaVersionNumber )
    {
        KeyInTable<String,String> theKey = new KeyInTable<String,String>(
                subjectAreaName,
                subjectAreaVersionNumber );

        return theSubjectAreas.get( theKey );
    }

    /**
      * Find an AttributableMeshType. No wildcards etc. are allowed.
      *
      * @param theSubjectArea the SubjectArea within which the search shall be performed
      * @param theAttributableMeshTypeName the programmatic name of the AttributableMeshType
      * @return the found AttributableMeshType, or null
      */
    AttributableMeshType findAttributableMeshType(
            SubjectArea theSubjectArea,
            String      theAttributableMeshTypeName )
    {
        if( theSubjectArea == null ) {
            throw new IllegalArgumentException( "null SubjectArea" );
        }
        if( theAttributableMeshTypeName == null ) {
            throw new IllegalArgumentException( "null AttributableMeshType name" );
        }
        KeyInTable<SubjectArea,String> theKey = new KeyInTable<SubjectArea,String>(
                theSubjectArea,
                theAttributableMeshTypeName );

        return theAttributableMeshTypes.get( theKey );
    }

    /**
      * Find a PropertyType. No wildcards etc. are allowed.
      *
      * @param theAttributableMeshType the AttributableMeshType within which the search shall be performed
      * @param thePropertyTypeName the programmatic name of the PropertyType
      * @return the found PropertyType, or null
      */
    PropertyType findPropertyType(
            AttributableMeshType theAttributableMeshType,
            String               thePropertyTypeName )
    {
        if( theAttributableMeshType == null ) {
            throw new IllegalArgumentException( "null AttributableMeshType" );
        }
        if( thePropertyTypeName == null ) {
            throw new IllegalArgumentException( "null PropertyType name" );
        }
        KeyInTable<AttributableMeshType,String> theKey = new KeyInTable<AttributableMeshType,String>(
                theAttributableMeshType,
                thePropertyTypeName );

        PropertyType ret = thePropertyTypes.get( theKey );
        if( ret != null ) {
            return ret;
        }

        AttributableMeshType [] theSupertypes = theAttributableMeshType.getDirectSupertypes();
        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            ret = findPropertyType( theSupertypes[i], thePropertyTypeName );
            if( ret != null ) {
                return ret;
            }
        }
        return null;
    }

    /**
     * Find a MeshType by its globally unique identifier.
     * 
     * @param identifier the Identifier of the MeshType to be found
     * @param doResolve if false, only look up the working model, do not attempt to extend the working model
     * @return the found MeshType, or null
     * @throws MeshTypeNotFoundException the MeshType could not be found
     * @throws IOException the model file could not be read
     */
    public MeshType findMeshTypeByIdentifier(
            MeshTypeIdentifier identifier,
            boolean            doResolve )
        throws
            MeshTypeNotFoundException,
            IOException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "findMeshTypeByIdentifier", identifier );
        }
        MeshType ret = allMeshTypes.get( identifier );
        if( ret != null || !doResolve ) {
            return ret;
        }

        // not found, need to attempt to load it. We attempt to parse the MeshObjectIdentifier to figure out which SA
        String identifierString = identifier.toExternalForm();
        if( identifierString == null ) {
            return null;
        }
        int slashStarts = identifierString.indexOf( '/' );
        
        String remainingIdentifierString;
        if( slashStarts >= 0 ) {
            remainingIdentifierString = identifierString.substring( 0, slashStarts );
        } else {
            remainingIdentifierString = identifierString;
        }

        int versionStarts = remainingIdentifierString.indexOf( "_v" );

        String saName;
        String saVersion;
        if( versionStarts >= 0 ) {
            saName    = remainingIdentifierString.substring( 0, versionStarts );
            saVersion = remainingIdentifierString.substring( versionStarts + "_v".length() );
        } else {
            saName    = remainingIdentifierString;
            saVersion = null;
        }

        // only load if we don't have that subject area already
        if( findSubjectArea( saName, saVersion ) != null ) {
            return null;
        }

        theModelBase.attemptToLoadSubjectArea( saName, saVersion );
        // may throw exception

        ret = allMeshTypes.get( identifier );
        return ret;
    }

    /**
      * Internal helper method that notifies MeshTypeLifecycleEventListeners of an
      * "added" event.
      *
      * @param theObject the MeshType that was added
      */
    protected void notifyElementAdded(
            MeshType theObject )
    {
        Iterator<MeshTypeLifecycleEventListener> iter;
        synchronized( this ) {
            if( theLifecycleEventListeners == null || theLifecycleEventListeners.isEmpty() ) {
                return;
            }

            iter = ( new ArrayList<MeshTypeLifecycleEventListener>( theLifecycleEventListeners )).iterator();
        }

        MeshTypeLifecycleEvent theEvent = new MeshTypeCreatedEvent(
                theModelBase,
                theObject );

        while( iter.hasNext() ) {
            MeshTypeLifecycleEventListener current = iter.next();
            current.objectCreated( theEvent );
        }
    }

    /**
     * The MModelBase that this MMeshTypeStore belongs to.
     */
    protected MModelBase theModelBase;

    /**
     * The SubjectAreas contained by this MMeshTypeStore.
     * key is { name, version }
     */
    protected HashMap<KeyInTable<String,String>,SubjectArea> theSubjectAreas
            = new HashMap<KeyInTable<String,String>,SubjectArea>( 20 ); // fudge

    /**
     * The AttributableMeshTypes contained by this MMeshTypeStore.
     * key is { subjectArea, name }
     */
    protected HashMap<KeyInTable<SubjectArea,String>,AttributableMeshType> theAttributableMeshTypes
            = new HashMap<KeyInTable<SubjectArea,String>,AttributableMeshType>();

    /**
     * The PropertyTypes contained by this MMeshTypeStore.
     * key is { amo, name }
     */
    protected HashMap<KeyInTable<AttributableMeshType,String>,PropertyType> thePropertyTypes
            = new HashMap<KeyInTable<AttributableMeshType,String>,PropertyType>();

    /**
     * An additional (and redundant) table that maps from the MeshType's
     * Identifier to the MeshType directly.
     */
    protected HashMap<MeshTypeIdentifier, MeshType> allMeshTypes
            = new HashMap<MeshTypeIdentifier,MeshType>();

    /**
      * The set of MeshTypeLifecycleEventListener.
      */
    protected transient ArrayList<MeshTypeLifecycleEventListener> theLifecycleEventListeners = null;

    /**
      * This implements the Iterator interface for this MMeshTypeStore.
      */
    protected class ClusterIterator
            implements Iterator<MeshType>
    {
        /**
          * Construct one.
          */
        ClusterIterator()
        {
            currentIter = theSubjectAreas.values().iterator();
            goNext();
        }

        /**
         * Tests if this Iterator can return more elements.
         *
         * @return if true, there are more elements
         */
        @Override
        public boolean hasNext()
        {
            return nextElement != null;
        }

        /**
         * Returns the next element of this Iterator.
         *
         * @return the next element
         */
        @Override
        public MeshType next()
        {
            MeshType ret = nextElement;
            goNext();
            return ret;
        }

        /**
         * The remove operation is unsupported.
         *
         * @throws UnsupportedOperationException always
         */
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

        /**
          * Internal advance to the next element.
          */
        final protected void goNext()
        {
            while( true ) {
                if( currentIter.hasNext() ) {
                    nextElement = currentIter.next();
                    return;
                }

                switch( state ) {
                    case 0:
                        state = 1;
                        currentIter = theAttributableMeshTypes.values().iterator();
                        break;
                    case 1:
                        state = 2;
                        currentIter = thePropertyTypes.values().iterator();
                        break;
                    case 2:
    //                    state = 3;
    //                    currentIter = theOtherCollectableMeshTypes.values().iterator();
    //                    break;
    //                case 3:
                        nextElement = null;
                        return;
                }
            }
        }

        /**
          * The state that we are in. The value 0 means we're returning SubjectAreas,
          * 1 AttributableMeshTypes, 2 PropertyTypes and 3 other CollectableMeshTypes
          */
        protected int state = 0;

        /**
          * The next element we will be returning.
          */
        protected MeshType nextElement;

        /**
          * The current Iterator that we are delegating to.
          */
        protected Iterator<? extends MeshType> currentIter;
    }

    /**
     * Helper class used as key in the hash tables.
     * 
     * @param <T> the first component of the key
     * @param <S> the second component of the key
     */
    public static class KeyInTable<T,S>
            implements
                Serializable
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
          * Create one with the two components, such as SubjectArea and AttributableMeshType.
          *
          * @param first the first component
          * @param second the second component
          */
        public KeyInTable(
                T first,
                S second )
        {
            if( first == null ) {
                throw new IllegalArgumentException();
            }
            this.theFirst  = first;
            this.theSecond = second;
        }

        /**
          * Delegate hash code to the member objects.
          *
          * @return the hash code
          */
        @Override
        public int hashCode()
        {
            int ret = theFirst.hashCode();
            if( theSecond != null ) {
                ret += theSecond.hashCode();
            }
            return ret;
        }

        /**
          * Determine equality, consistent with definition of hashCode.
          *
          * @param theOther object to test equality against
          * @return true if the two objects are equal
          * @see #hashCode
          */
        @Override
        public boolean equals(
                Object theOther )
        {
            if( theOther instanceof KeyInTable ) {
                @SuppressWarnings( "unchecked" )
                KeyInTable<T,S> realOther = (KeyInTable<T,S>) theOther;
                if( !theFirst.equals( realOther.theFirst )) {
                    return false;
                }
                if( theSecond == null ) {
                    return realOther.theSecond == null;
                } else {
                    return theSecond.equals( realOther.theSecond );
                }
            }
            return false;
        }

        /**
          * The first component.
          */
        protected T theFirst;

        /**
          * The second component.
          */
        protected S theSecond;
    }
}
