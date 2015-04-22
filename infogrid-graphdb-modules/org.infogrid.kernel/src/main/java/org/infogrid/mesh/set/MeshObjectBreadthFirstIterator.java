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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.logging.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>This is a breadth-first iterator over a a graph of MeshObjects, returning the
 * MeshObjects that it finds by traversing a certain TraversalSpecification. It starts with
 * a MeshObject, finds all MeshObjects related to that start MeshObject, and
 * returns those. After that, it returns all MeshObjects that are "two steps"
 * removed from the original MeshObject, and so forth, until we reach all the
 * MeshObjects that are reachable.</p>
 *
 * <p><b>WARNING:</b> if you use this iterator with a very large upper limit, you may be replicating
 * all of the world's data locally onto your machine!!!!</p>
 */
public class MeshObjectBreadthFirstIterator
        implements Iterator<MeshObject>
{
    private static final Log log = Log.getLogInstance( MeshObjectBreadthFirstIterator.class ); // our own, private logger

    /**
     * Factory method for a MeshObjectBreadthFirstIterator that will traverse to all neighbor MeshObjects
     * from the start MeshObject, then all the neighbors' neighbors and so forth.
     * 
     * @param start where to start the traversal
     * @param max the maximum number of "steps" we want to traverse
     * @return the created MeshObjectBreadthFirstIterator
     */
    public static MeshObjectBreadthFirstIterator create(
            MeshObject start,
            int        max )
    {
        return new MeshObjectBreadthFirstIterator( start, null, max );
    }

    /**
     * Factory method for a MeshObjectBreadthFirstIterator that will traverse the specified TraversalSpecification
     * from the start MeshObject, then from the reached MeshObjects, and so forth.
     * 
     * @param start where to start the traversal
     * @param toTraverse the TraversalSpecification to traverse
     * @param max the maximum number of "steps" we want to traverse
     * @return the created MeshObjectBreadthFirstIterator
     */
    public static MeshObjectBreadthFirstIterator create(
            MeshObject             start,
            TraversalSpecification toTraverse,
            int                    max )
    {
        return new MeshObjectBreadthFirstIterator( start, toTraverse, max );
    }

    /**
     * Constructor.
     *
     * @param start where to start the traversal
     * @param toTraverse the TraversalSpecification to traverse
     * @param max the maximum number of "steps" we want to traverse
     */
    protected MeshObjectBreadthFirstIterator(
            MeshObject             start,
            TraversalSpecification toTraverse,
            int                    max )
    {
        startMeshObject = start;
        maximum         = max;

        if( startMeshObject == null ) {
            throw new IllegalArgumentException( "cannot start with null MeshObject" );
        }
        if( maximum < 1 ) {
            throw new IllegalArgumentException( "maximum must be at least 1" );
        }

        theTraversalSpecification = toTraverse;

        outerIndex = 0;

        ArrayList<MeshObject> nextStepArrayList = new ArrayList<MeshObject>();
        nextStepArrayList.add( startMeshObject );
        foundAlready.add( outerIndex, nextStepArrayList );
        innerIter = nextStepArrayList.iterator();

        goNext();

        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "constructor" );
        }
    }

    /**
     * Determine whether there are more objects to be returned by this Iterator.
     *
     * @return true if there are more objects to be returned by this Iterator
     */
    public boolean hasNext()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "hasNext()" );
        }

        return nextToReturn != null;
    }

    /**
     * Obtain the next MeshObject returned by this Iterator and iterate forward.
     *
     * @return the next MeshObject returned by this Iterator
     */
    public MeshObject next()
    {
        StringBuilder deb = null;
        if( log.isDebugEnabled() ) {
            deb = new StringBuilder( this + ".next()" );
        }

        MeshObject ret = nextToReturn;
        if( ret == null ) {
            return ret;
        }

        goNext();

        if( log.isDebugEnabled() ) {
            log.debug( deb + " returns " + ret );
        }

        return ret;
    }

    /**
     * Internal "go next" method. This method makes the next step.
     */
    protected void goNext()
    {
        if( innerIter == null ) {
            // we are at the very end

            if( log.isDebugEnabled() ) {
                log.debug( "goNext() in innerIter == null section" );
            }

            return;

        } else if( innerIter.hasNext() ) {
            // we are in the middle of the innerIter

            if( log.isDebugEnabled() ) {
                log.debug( "goNext() in innerIter.hasNext() section" );
            }

            nextToReturn = innerIter.next();

        } else {
            // need to take the next step
            if( log.isDebugEnabled() ) {
                log.debug( "goNext() in else section" );
            }

            // need to do the next step here
            ArrayList<MeshObject> nextStepArrayList = new ArrayList<MeshObject>();

            Iterator<MeshObject> nextStepIter = foundAlready.get( outerIndex ).iterator();
            outerIndex++;

            while( nextStepIter.hasNext() ) {
                MeshObject    current = nextStepIter.next();
                MeshObjectSet nextStepSet;

                if( theTraversalSpecification != null ) {
                    nextStepSet = current.traverse( theTraversalSpecification );
                } else {
                    nextStepSet = current.traverseToNeighborMeshObjects();
                }

                Iterator<MeshObject> candidateIter = nextStepSet.iterator();
                while( candidateIter.hasNext() ) {
                    MeshObject candidate = candidateIter.next();
                    if( ! haveAlready( candidate, nextStepArrayList )) {
                        nextStepArrayList.add( candidate );
                    }
                }
            }
            foundAlready.add( outerIndex, nextStepArrayList );

            if( outerIndex < maximum ) {
                innerIter = nextStepArrayList.iterator();
                if( innerIter.hasNext() ) {
                    nextToReturn = innerIter.next();
                } else {
                    nextToReturn = null;
                    innerIter    = null; // end
                }

            } else {
                nextToReturn = null;
                innerIter    = null; // end
            }
        }
    }

// ----

    /**
     * We do not support the remove operation.
     *
     * @throws UnsupportedOperationException always thrown
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * This internal helper tells us whether we have returned a certain MeshObject before.
     *
     * @param candidate the MeshObject to test
     * @param currentList the most recent inner list
     * @return true if we have returned candidate already
     */
    protected boolean haveAlready(
            MeshObject            candidate,
            ArrayList<MeshObject> currentList )
    {
        if( candidate == startMeshObject ) {
            return true;
        }

        for( int i=0 ; i<outerIndex ; ++i ) {
            ArrayList<MeshObject> listHere = foundAlready.get( i );
            if( listHere.contains( candidate )) {
                return true;
            }
        }
        return currentList.contains( candidate );
    }

    /**
     * The start MeshObject.
     */
    protected MeshObject startMeshObject;

    /**
     * The set of all MeshObjects that we have returned already. It is organized as
     * <code>ArrayList&lt;ArrayList&lt;MeshObject&gt;&gt;</code>. The outer ArrayList is
     * indexed by the number of
     * steps a MeshObject is away from the start MeshObject. The inner ArrayList contains
     * all the MeshObjects with the same number of steps away from the start MeshObject.
     */
    protected ArrayList<ArrayList<MeshObject>> foundAlready = new ArrayList<ArrayList<MeshObject>>();

    /**
     * The iterator that iterates over the "outer" foundAlready list.
     * We need to use an integer index here as a real iterator would fail every time
     * we add more to the foundAlrady list. If this is -1, it indicates that we have
     * not started iterating yet.
     *
     * Index 0 indicates the first set of objects reached from the startIndex,
     * which has a distance of 1 (not zero!).
     */
    protected int outerIndex = -1;

    /**
     * The iterator that iterates over the "inner" foundAlready list.
     */
    protected Iterator<MeshObject> innerIter;

    /**
     * The next MeshObject to return.
     */
    protected MeshObject nextToReturn;

    /**
     * The TraversalSpecification that we traverse.
     */
    protected TraversalSpecification theTraversalSpecification;

    /**
     * The maximum number of steps that we take from the start Entity to return Entities.
     */
    protected int maximum;
}
