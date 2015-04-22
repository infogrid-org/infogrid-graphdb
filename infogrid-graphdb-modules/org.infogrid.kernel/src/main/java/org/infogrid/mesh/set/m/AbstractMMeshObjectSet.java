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

package org.infogrid.mesh.set.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.AbstractMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.util.ArrayCursorIterator;
import org.infogrid.util.CursorIterable;
import org.infogrid.util.CursorIterator;

/**
 * Abstract superclass for those MeshObjectSets that keep their content in memory.
 */
public abstract class AbstractMMeshObjectSet
        extends
            AbstractMeshObjectSet
        implements
            CursorIterable<MeshObject>
{
    /**
      * Constructor to be used by subclasses only.
      *
      * @param factory the MeshObjectSetFactory that created this MeshObjectSet
      */
    protected AbstractMMeshObjectSet(
            MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * Obtain content of the set.
     *
     * @return the content of the set.
     */
    public final MeshObject [] getMeshObjects()
    {
        return currentContent;
    }

    /**
     * Set the initial content of the set, for subclasses only.
     *
     * @param initialContent the initial content of the set, with count 1 each
     */
    protected void setInitialContent(
            MeshObject [] initialContent )
    {
        setInitialContent( initialContent, null );
    }

    /**
     * Set the initial content of the set, for subclasses only.
     *
     * @param initialContent the initial content of the set
     * @param initialCounters the counts for the initial content, in the same sequence
     * @throws IllegalStateException thrown if this MeshObjectSet already has an initial content
     * @throws IllegalArgumentException thrown if the initial content is null, or the array lenghts did not match
     * @throws WrongMeshBaseException thrown if the provided MeshObjects dot not all reside in the same MeshBase
     */
    protected void setInitialContent(
            MeshObject [] initialContent,
            int []        initialCounters )
        throws
            IllegalStateException,
            IllegalArgumentException,
            WrongMeshBaseException
    {
        if( currentContent != null ) {
            throw new IllegalStateException( "initialized already" );
        }
        if( initialContent == null ) {
            throw new IllegalArgumentException();
        }
        if( initialCounters != null && initialCounters.length != initialContent.length ) {
            throw new IllegalArgumentException();
        }

        MeshBase base = theFactory.getMeshBase();
        for( int i=0; i<initialContent.length; ++i ) {
            // do something smart even if setMeshBase has not been called yet
            if( base == null ) {
                base = initialContent[i].getMeshBase();
            } else if( base != initialContent[i].getMeshBase() ) {
                throw new WrongMeshBaseException( base, initialContent[i].getMeshBase() );
            }
        }

        currentContent = initialContent;
        if( initialCounters != null ) {
            currentContentCounters = initialCounters;
        } else {
            currentContentCounters = new int[ currentContent.length ]; // array members initialize to zero automatically
        }

        // if we have listeners, invoke gainedFirstContentPropertyChangeListener
        if( haveContentPropertyChangeListeners() ) {
            gainedFirstContentPropertyChangeListener();
        }
    }

    /**
     * Set the initial content of the set, for subclasses only.
     *
     * @param initialContent the initial content of the set, with the counters to be calculated
     * @throws IllegalStateException thrown if this MeshObjectSet already has an initial content
     * @throws IllegalArgumentException thrown if the initial content is null
     * @throws WrongMeshBaseException thrown if the provided MeshObjects dot not all reside in the same MeshBase
     */
    protected void setInitialContent(
            MeshObject [][] initialContent )
        throws
            IllegalStateException,
            IllegalArgumentException,
            WrongMeshBaseException
    {
        if( currentContent != null ) {
            throw new IllegalStateException( "initialized already" );
        }
        if( initialContent == null ) {
            throw new IllegalArgumentException();
        }

        // potentially over-allocate
        int count = 0;
        for( int i=0 ; i<initialContent.length ; ++i ) {
            count += initialContent[i].length;
        }

        MeshBase base          = theFactory.getMeshBase();
        currentContent         = new MeshObject[ count ];
        currentContentCounters = new int[ count ];
        count = 0;

        for( int i=0 ; i<initialContent.length ; ++i ) {
            for( int j=0 ; j<initialContent[i].length ; ++j ) {
                MeshObject candidate = initialContent[i][j];

                // do something smart even if setMeshBase has not been called yet
                if( base == null ) {
                    base = candidate.getMeshBase();
                } else if( base != candidate.getMeshBase() ) {
                    throw new WrongMeshBaseException( base, candidate.getMeshBase() );
                }

                boolean found = false;
                for( int k=0 ; k<count ; ++k ) {
                    if( currentContent[k] == candidate ) {
                        found = true;
                        ++currentContentCounters[k];
                        break;
                    }
                }
                if( !found ) {
                    currentContent[count++] = candidate;
                }
            }
        }

        // now shrink if over-allocated
        if( count < currentContent.length ) {
            MeshObject [] oldContent  = currentContent;
            int        [] oldCounters = currentContentCounters;

            currentContent         = new MeshObject[ count ];
            currentContentCounters = new int[ count ];

            for( int i=0 ; i<count ; ++i ) {
                currentContent[i]         = oldContent[i];
                currentContentCounters[i] = oldCounters[i];
            }
        }

        // if we have listeners, invoke gainedFirstContentPropertyChangeListener
        if( haveContentPropertyChangeListeners() ) {
            gainedFirstContentPropertyChangeListener();
        }
    }
    
    /**
     * Obtain an Iterator over all members of this set.
     *
     * @return Iterator over all members of this set
     */
    public CursorIterator<MeshObject> iterator()
    {
        return ArrayCursorIterator.<MeshObject>create( currentContent );
    }

    /**
     * Determine the number of elements in this set.
     *
     * @return the number of elements in this set
     */
    public int size()
    {
        return currentContent.length;
    }

    /**
     * This is invoked by our subclasses to potentially reorder the set according to
     * the specified MeshObjectSorter.
     *
     * @param theSorter the MeshObjectSorter according to which the current content is sorted
     */
    protected final void potentiallyReorder(
            MeshObjectSorter theSorter )
    {
        // FIXME: implementation not complete
        for( int i=0 ; i<currentContentCounters.length ; ++i ) {
            if( currentContentCounters[i] > 0 ) {
                throw new UnsupportedOperationException();
            }
        }
        currentContent = theSorter.getOrderedInNew( currentContent );
    }
    
    /**
     * The current content of this set.
     */
    protected MeshObject [] currentContent;

    /**
     * <p>This array has the same length as currentContent, and counts the number of times
     * the corresponding element in currentContent is "contained" in this set. This is to
     * make this set a real set, not something with duplicates.</p>
     *
     * <p>Note that a value of N in this array means that there are N duplicates, not that there
     * are N instances (there are N+1 instances).</p>
     */
    protected int [] currentContentCounters;
}
