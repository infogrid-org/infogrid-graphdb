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

package org.infogrid.model.traversal;

import org.infogrid.model.primitives.AttributableMeshType;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * An Iterator that returns the supertypes of an AttributableMeshType
 * in breadth-first order. It will return duplicates in case of multiple inheritance;
 * to avoid that, chain this Iterator with an Iterator that filters out duplicates.
 */
public class BreadthFirstSupertypeIterator
        implements
            Iterator<AttributableMeshType>
{
    /**
     * Constructor.
     *
     * @param startObject the AttributableMeshType whose supertypes we'd like to find
     */
    public BreadthFirstSupertypeIterator(
            AttributableMeshType startObject )
    {
        AttributableMeshType [] theSupertypes = startObject.getDirectSupertypes();

        if( theSupertypes.length > 0 ) {

            currentLayer = new ArrayList<AttributableMeshType>( theSupertypes.length );
            for( int i=0 ; i<theSupertypes.length ; ++i ) {
                currentLayer.add( theSupertypes[i] );
            }

            currentIter = currentLayer.iterator();
            nextObject  = currentIter.next();
        }
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements.
     *
     * @return <tt>true</tt> if the iteration has more elements
     */
    public boolean hasNext()
    {
        if( nextObject != null ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    public AttributableMeshType next()
    {
        AttributableMeshType ret = nextObject;
        if( currentIter.hasNext() ) {
            nextObject = currentIter.next();

        } else {
            currentIter = currentLayer.iterator();
            ArrayList<AttributableMeshType> newLayer = new ArrayList<AttributableMeshType>();
            while( currentIter.hasNext() ) {

                AttributableMeshType [] s = currentIter.next().getDirectSupertypes();
                for( int i=0 ; i<s.length ; ++i ) {
                    newLayer.add( s[i] );
                }
            }
            currentLayer = newLayer;
            if( currentLayer.isEmpty() ) {
                currentLayer = null;
                currentIter  = null;
                nextObject   = null;

            } else {
                currentIter = currentLayer.iterator();
                if( currentIter.hasNext() ) {
                    nextObject = currentIter.next();
                } else {
                    nextObject = null;
                }
            }
        }
        return ret;
    }

    /**
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).
     *
     * @throws UnsupportedOperationException always thrown, not a supported operation
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * The set of current super- (or super-super- etc) AttributableMeshType.
     */
    protected ArrayList<AttributableMeshType> currentLayer;

    /**
     * The current delegate Iterator.
     */
    protected Iterator<AttributableMeshType> currentIter;

    /**
     * The next AttributableMeshType to return. We leave this as Object in order to
     * avoid doing unnecessary (performance-wise) typecasts.
     */
    protected AttributableMeshType nextObject;
}
