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

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.util.Identifier;
import org.infogrid.util.SubsettingCursorIterator;

/**
 * A MeshObjectSet that was constructed by subsetting another MeshObjectSet starting
 * from a start index to an end index.
 */
public class StartEndMeshObjectSubSet
        extends
            AbstractMeshObjectSet
{
    /**
     * Factory method.
     *
     * @param delegate the MeshObjectSet that is being subset
     * @param startIndex the start index (inclusive)
     * @param endIndex   the end index (exclusive)
     * @return StartEndMeshObjectSubSet the created sub-set
     */
    public static StartEndMeshObjectSubSet create(
            MeshObjectSet delegate,
            int           startIndex,
            int           endIndex )
    {
        return new StartEndMeshObjectSubSet( delegate, startIndex, endIndex );
    }

    /**
     * Constructor.
     *
     * @param delegate the MeshObjectSet that is being subset
     * @param startIndex the start index for the subset
     * @param endIndex the end index for the subset
     */
    protected StartEndMeshObjectSubSet(
            MeshObjectSet   delegate,
            int             startIndex,
            int             endIndex )
    {
        super( delegate.getFactory() );

        theDelegate   = delegate;
        theStartIndex = startIndex;
        theEndIndex   = endIndex;
    }
    
    /**
     * Obtain the underlying MeshObjectSet of which this is a subset.
     *
     * @return the underlying MeshObjectSet
     */
    public MeshObjectSet getDelegate()
    {
        return theDelegate;
    }

    /**
     * Obtain the start index of the underlying MeshObjectSet from where this subset was created.
     *
     * @return the start index (inclusive)
     */
    public int getStartIndex()
    {
        return theStartIndex;
    }
    
    /**
     * Obtain the end index of the underlying MeshObject through where this subset was created.
     *
     * @return the end index (exclusive)
     */
    public int getEndIndex()
    {
        return theEndIndex;
    }
    
    /**
      * Obtain the MeshObjects contained in this set.
      *
      * @return the MeshObjects contained in this set
      */
    public MeshObject[] getMeshObjects()
    {
        int max   = theDelegate.size();
        int start = ( theStartIndex < max ) ? theStartIndex : max;
        int end   = ( theEndIndex != -1 && theEndIndex < max ) ? theEndIndex : max;
        
        MeshObject [] ret = new MeshObject[ end - theStartIndex ];

        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = theDelegate.get( theStartIndex + i );
        }
        return ret;
    }

    /**
     * Get the Nth MeshObject contained in this set.
     *
     * @return the Nth MeshObject contained in this set.
     */
    @Override
    public MeshObject get(
            int n )
    {
        int delegateIndex = theStartIndex + n;
        
        MeshObject ret = theDelegate.get( delegateIndex ); // may throw
        return ret;
    }

    /**
     * Assuming that this set contains no more than one object, obtain this one object. This
     * method is often very convenient if it is known to the application programmer that
     * a certain set may only contain one member. Invoking this method if the set has more
     * than one member will throw an IllegalStateException.
     *
     * @return the one element of the set, or null if the set is empty
     * @throws IllegalStateException thrown if the set contains more than one element
     */
    @Override
    public MeshObject getSingleMember()
        throws
            IllegalStateException
    {
        int max   = theDelegate.size();
        int start = ( theStartIndex < max ) ? theStartIndex : max;
        int end   = ( theEndIndex != -1 && theEndIndex < max ) ? theEndIndex : max;
        
        if( start + 1 == end ) {
            MeshObject ret = theDelegate.get( start );
            return ret;
        } else {
            throw new IllegalStateException( "Set had " + (end-start) + " members, not one" );
        }
    }

    /**
     * Obtain an Iterator iterating over the content of this set.
     *
     * @return an Iterator iterating over the content of this set
     */
    public SubsettingCursorIterator<MeshObject> iterator()
    {
        MeshObject startObject = theDelegate.get( theStartIndex );
        MeshObject endObject   = theDelegate.get( theEndIndex );

        return SubsettingCursorIterator.<MeshObject>create( startObject, endObject, theDelegate.iterator(), MeshObject.class );
    }

    /**
     * Determine whether this set contains a certain MeshObject.
     *
     * @param testObject the MeshObject to look for
     * @return true if this set contains the given MeshObject
     * @throws WrongMeshBaseException thrown if the testObject is contained in a different MeshBase than the MeshObjects in this set
     */
    @Override
    public boolean contains(
            MeshObject testObject )
        throws
            WrongMeshBaseException
    {
        int max   = theDelegate.size();
        int start = ( theStartIndex < max ) ? theStartIndex : max;
        int end   = ( theEndIndex != -1 && theEndIndex < max ) ? theEndIndex : max;
        
        for( int i = start ; i<end ; ++i ) {
            MeshObject current = theDelegate.get( i );
            if( current.equals( testObject )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this set contains a MeshObject with this identifier.
     * 
     * @param identifier the identifier of the MeshObject to look for
     * @return true if this set contains the given MeshObject
     */
    @Override
    public boolean contains(
            Identifier identifier )
    {
        int max   = theDelegate.size();
        int start = ( theStartIndex < max ) ? theStartIndex : max;
        int end   = ( theEndIndex != -1 && theEndIndex < max ) ? theEndIndex : max;
        
        for( int i = start ; i<end ; ++i ) {
            MeshObject current = theDelegate.get( i );
            if( current.getIdentifier().equals( identifier )) {
                return true;
            }
            if( current.getIdentifier().equals( identifier )) {
                return true;
            }
        }
        return false;        
    }

    /**
     * Determine whether this set is empty.
     *
     * @return true if this set is empty
     */
    @Override
    public boolean isEmpty()
    {
        if( theStartIndex >= theEndIndex ) {
            return true;
        }
        int delegateSize = theDelegate.size();
        if( delegateSize < theStartIndex ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determine the number of members in this set.
     *
     * @return the number of members in this set
     */
    @Override
    public int size()
    {
        int delegateSize = theDelegate.size();
        int max = Math.min( theEndIndex, delegateSize );
        
        int ret = max - theStartIndex;
        return ret;
    }

    /**
     * The underlying MeshObjectSet.
     */
    protected MeshObjectSet theDelegate;
    
    /**
     * Start index.
     */
    protected int theStartIndex;
    
    /**
     * End index. This may be <code>-1</code>, indicating &quot;to the end&quot;. We cannot simply store
     * the count here, because the delegate set may change at runtime.
     */
    protected int theEndIndex;
}
