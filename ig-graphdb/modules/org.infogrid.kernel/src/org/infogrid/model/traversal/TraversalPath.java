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

package org.infogrid.model.traversal;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectsNotFoundException;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;
import org.infogrid.meshbase.transaction.MeshObjectStateEvent;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.FlexiblePropertyChangeListenerSet;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
 * <p>Represents a path of traversal from an (implied) start MeshObject,
 * to another or the same MeshObject, via a sequence of zero or more steps. One
 * step is the combination of a TraversalSpecification, and the resulting
 * MeshObject.</p>
 *
 * <p>TraversalPaths are a handy structure to capture a "path" through an MeshObject
 * graph, similar to a path in a file system, but more powerful. For one, one can
 * traverse different types of TraversalSpecification, not just one as in case of
 * the file system.</p>
 *
 * <p>A TraversalPath is immutable. However, one can add a special type of
 * PropertyChangeListener to it, which will be notified of all PropertyChangeEvents
 * of MeshObjects on the TraversalPath. It uses a special subclass of
 * PropertyChangeEvent, TraversalPathPropertyChangeEvent, to indicate which
 * TraversalPath the changed MeshObject was on.</p>
 *
 * <p>TraversalPath is implemented as a recursive data structure.</p>
 */
public class TraversalPath
        implements
            PropertyChangeListener,
            CanBeDumped
{
    private static final Log log = Log.getLogInstance( TraversalPath.class ); // our own, private logger

    /**
     * Factory method for only one step on the TraversalPath.
     *
     * @param traversedSpec the TraversalSpecification that we traversed
     * @param reachedMeshObject the MeshObject that was reached by doing the traversal
     * @return the created TraversalPath
     */
    public static TraversalPath create(
            TraversalSpecification traversedSpec,
            MeshObject             reachedMeshObject )
    {
        return create( traversedSpec, reachedMeshObject, null );
    }

    /**
     * Factory method by prepending a step to an already-existing TraversalPath.
     *
     * @param traversedSpec the TraversalSpecification that we traversed
     * @param reachedMeshObject the MeshObject that was reached by doing the traversal
     * @param remainder the TraversalPath to which we prepend this step
     * @return the created TraversalPath
     * @throws WrongMeshBaseException thrown if a TraversalPath is prepended to a TraversalPath with MeshObjects in different MeshBases
     */
    public static TraversalPath create(
            TraversalSpecification traversedSpec,
            MeshObject             reachedMeshObject,
            TraversalPath          remainder )
        throws
            WrongMeshBaseException
    {
        int size;

        if( remainder != null ) {
            if( reachedMeshObject.getMeshBase() != remainder.getMeshBase() ) {
                throw new WrongMeshBaseException( reachedMeshObject.getMeshBase(), remainder.getMeshBase() );
            }
            size = remainder.getSize() + 1;
        } else {
            size = 1;
        }
        TraversalPath ret = new TraversalPath(
                traversedSpec,
                reachedMeshObject,
                remainder,
                size,
                reachedMeshObject.getMeshBase() );
        
        return ret;
    }

    /**
     * Factory method to construct one as a concatenation of two other TraversalPaths.
     *
     * @param one the first TraversalPath
     * @param two the second TraversalPath
     * @return the created TraversalPath
     * @throws WrongMeshBaseException thrown if a TraversalPath is prepended to a TraversalPath with MeshObjects in different MeshBases
     */
    public static TraversalPath create(
            TraversalPath one,
            TraversalPath two )
        throws
            WrongMeshBaseException
    {
        if( one == null || two == null ) {
            log.error( "argument is null" );
        }
        if( one.getMeshBase() != two.getMeshBase() ) {
            throw new WrongMeshBaseException( one.getMeshBase(), two.getMeshBase() );
        }

        TraversalPath remainder = recursiveCreate( one.theRemainder, two );

        TraversalPath ret = new TraversalPath(
                one.theTraversedSpec,
                one.theReached,
                remainder,
                one.theSize + two.theSize,
                one.getMeshBase() );

        return ret;
    }

    /**
     * Factory method to construct one by concatenating another step to an existing TraversalPath.
     *
     * @param one the first TraversalPath
     * @param spec the TraversalSpecification traversed from the last MeshObject of the first TraversalPath
     * @param reached the MeshObject reached in the process
     * @return the created TraversalPath
     * @throws WrongMeshBaseException thrown if a TraversalPath is prepended to a TraversalPath with MeshObjects in different MeshBases
     */
    public static TraversalPath create(
            TraversalPath          one,
            TraversalSpecification spec,
            MeshObject             reached )
        throws
            WrongMeshBaseException
    {
        return create( one, TraversalPath.create( spec, reached ));
    }

    /**
     * Factory method to construct one from equal-length arrays of TraversalSpecification and reached MeshObjects.
     *
     * @param steps the steps taken, in sequence
     * @param reached the MeshObject reached after each step
     * @return the created TraversalPath
     */
    public static TraversalPath create(
            TraversalSpecification [] steps,
            MeshObject []             reached )
    {
        if( steps.length != reached.length ) {
            throw new IllegalArgumentException( "Steps: " + steps.length + " vs. reached: " + reached.length );
        }
        TraversalPath ret = null;
        for( int i=steps.length-1 ; i>=0 ; --i ) {
            ret = create( steps[i], reached[i], ret );
        }
        return ret;
    }

    /**
     * Little helper method to recursively create.
     *
     * @param here the TraversalPath that is the first part
     * @param remainder the remainder TraversalPath
     * @return the created TraversalPath
     */
    private static TraversalPath recursiveCreate(
            TraversalPath here,
            TraversalPath remainder )
    {
        if( here == null ) {
            return remainder;
        } else {
            TraversalPath nextStep = recursiveCreate( here.theRemainder, remainder );
            int           size     = ( nextStep != null ) ? ( nextStep.theSize+1 ) : 1;
            return new TraversalPath(
                    here.theTraversedSpec,
                    here.theReached,
                    nextStep,
                    size,
                    here.getMeshBase() );
        }
    }

    /**
     * Constructor.
     *
     * @param traversedSpec the TraversalSpecification that we traversed
     * @param reachedMeshObject the MeshObject that was reached by doing the traversal
     * @param remainder the remaining TraversalPath (recursive structure)
     * @param size the number of steps in this TraversalPath
     * @param mb the MeshBase against which to resolve the MeshObject
     */
    protected TraversalPath(
            TraversalSpecification traversedSpec,
            MeshObject             reachedMeshObject,
            TraversalPath          remainder,
            int                    size,
            MeshBase               mb )
    {
        theTraversedSpec     = traversedSpec;
        theReached           = reachedMeshObject;
        theReachedIdentifier = reachedMeshObject.getIdentifier();
        theRemainder         = remainder;
        theSize              = size;
        theMeshBase          = mb;
    }

    /**
     * Set the traceMethodCallEntry name. This is to be used for debugging only.
     *
     * @param newValue the new value
     */
    public void setDebugName(
            String newValue )
    {
        theDebugName = newValue;
    }

    /**
     * Obtain the traceMethodCallEntry name. This is to be used for debugging only.
     *
     * @return the name
     */
    public String getDebugName()
    {
        return theDebugName;
    }

    /**
     * Set the MeshBase against which this TraversalPath shall be resolved.
     * 
     * @param newValue the new value
     */
    public void setMeshBase(
            MeshBase newValue )
    {
        if( newValue == null || newValue != theMeshBase ) {
            theMeshBase = newValue;
            
            theReached = null;
            theRemainder.setMeshBase( newValue );
        }
    }

    /**
     * Obtain the MeshBase to which this TraversalPath belongs.
     *
     * @return the MeshBase
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * Determine the number of steps in this TraversalPath.
     *
     * @return the number of steps in this TraversalPath
     */
    public int getSize()
    {
        return theSize;
    }

    /**
     * Obtain the TraversalSpecification at a certain position in this TraversalPath.
     *
     * @param index the index from which we want to obtain the TraversalSpecification
     * @return the TraversalSpecification at this index
     * @throws ArrayIndexOutOfBoundsException if index is out of range
     */
    public TraversalSpecification getTraversalSpecificationAt(
            int index )
    {
        if( index >= theSize ) {
            throw new ArrayIndexOutOfBoundsException( index );
        }
        if( index == 0 ) {
            return theTraversedSpec;
        }
        return theRemainder.getTraversalSpecificationAt( index-1 );
    }

    /**
     * Obtain the TraversalSpecifications traversed along the entire TraversalPath in sequence.
     *
     * @return the TraversalSpecifications in sequence
     */
    public TraversalSpecification [] getTraversalSpecificationsInSequence()
    {
        TraversalSpecification [] ret     = new TraversalSpecification[ theSize ];
        TraversalPath             current = this;
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = current.theTraversedSpec;
            current = current.theRemainder;
        }
        return ret;
    }

    /**
     * Obtain the TraversalSpecification traversed along the entire
     * TraversalPath. If this TraversalPath is longer than one step,
     * the returned TraversalSpecification will be a SequentialCompoundTraversalSpecification.
     *
     * @return the TraversalSpecification
     */
    public TraversalSpecification getTraversalSpecification()
    {
        if( theSize == 1 ) {
            return theTraversedSpec;
        } else {
            SequentialCompoundTraversalSpecification ret
                    = SequentialCompoundTraversalSpecification.create(
                            getTraversalSpecificationsInSequence() );
            return ret;
        }
    }

    /**
     * Obtain the MeshObject at a certain position in this TraversalPath.
     *
     * @param index the index from which we want to obtain the MeshObject
     * @return the MeshObject at this index
     * @throws ArrayIndexOutOfBoundsException if index is out of range
     */
    public MeshObject getMeshObjectAt(
            int index )
    {
        if( index >= theSize ) {
            throw new ArrayIndexOutOfBoundsException( index );
        }
        if( index == 0 ) {
            return theReached;
        }
        return theRemainder.getMeshObjectAt( index-1 );
    }

    /**
     * This convenience method returns all MeshObjects in this TraversalPath in sequence.
     *
     * @return an array of size getSize(), which contains the MeshObject on the TraversalPath
     * @throws MeshObjectsNotFoundException thrown if the MeshObject could not be resolved
     */
    public MeshObject [] getMeshObjectsInSequence()
        throws
            MeshObjectsNotFoundException
    {
        MeshObject []                   ret = new MeshObject[ getSize() ];
        ArrayList<MeshObjectIdentifier> notFound = null;

        TraversalPath current = this;
        for( int i=0 ; i<ret.length ; ++i ) {
            try {
                ret[i]  = current.getFirstMeshObject();
                current = current.getNextSegment();

            } catch( MeshObjectsNotFoundException ex ) {
                if( notFound == null ) {
                    notFound = new ArrayList<MeshObjectIdentifier>();
                }
                notFound.add( current.getFirstMeshObjectIdentifier() );
            }
        }
        if( notFound != null ) {
            throw new MeshObjectsNotFoundException(
                    theMeshBase,
                    ret, // partialResult
                    ArrayHelper.copyIntoNewArray( notFound, MeshObjectIdentifier.class ));
        }
        return ret;
    }

    /**
     * This convenience method returns the identifiers of all MeshObjects in this TraversalPath
     * in sequence.
     *
     * @return an array of size getSize(), which contains the MeshObjectIdentifiers of the MeshObjects on the TraversalPath
     */
    public MeshObjectIdentifier [] getMeshObjectIdentifiersInSequence()
    {
        MeshObjectIdentifier [] ret = new MeshObjectIdentifier[ getSize() ];
        TraversalPath           current = this;

        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i]  = current.getFirstMeshObjectIdentifier();
            current = current.getNextSegment();
        }
        return ret;
    }

    /**
     * Obtain the first MeshObject on this TraversalPath.
     *
     * @return the first MeshObject on this TraversalPath
     * @throws MeshObjectsNotFoundException thrown if the MeshObject could not be resolved
     */
    public synchronized MeshObject getFirstMeshObject()
        throws
            MeshObjectsNotFoundException
    {
        if( theReached == null ) {
            theReached = resolveMeshObject( theReachedIdentifier );
        }
        return theReached;
    }

    /**
     * Obtain the identifier of the first MeshObject on this TraversalPath.
     *
     * @return the identifier of the first MeshObject on this TraversalPath
     */
    public MeshObjectIdentifier getFirstMeshObjectIdentifier()
    {
        return theReachedIdentifier;
    }

    /**
     * Obtain the last MeshObject on this TraversalPath.
     *
     * @return the last MeshObject on this TraversalPath
     */
    public MeshObject getLastMeshObject()
    {
        if( theRemainder != null ) {
            return theRemainder.getLastMeshObject();
        } else {
            return theReached;
        }
    }

    /**
     * Obtain the next segment in this TraversalPath.
     *
     * @return obtain the next segment in this TraversalPath, if there is one
     */
    public TraversalPath getNextSegment()
    {
        return theRemainder;
    }

    /**
     * Resolve a MeshObject
     *
     * @param identifier the MeshObjectIdentifier to resolve
     * @return the resolved MeshObject
     * @throws MeshObjectsNotFoundException thrown if the MeshObject could not be resolved
     */
    protected MeshObject resolveMeshObject(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectsNotFoundException
    {
        if( theMeshBase == null ) {
            throw new NullPointerException();
        }
        
        MeshObject ret = theMeshBase.findMeshObjectByIdentifierOrThrow( identifier );
        return ret;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare against
     * @return true if the objects are equal
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof TraversalPath ) {

            TraversalPath realOther = (TraversalPath) other;

            if( theRemainder == null && realOther.theRemainder != null ) {
                return false;
            }
            if( theRemainder != null && realOther.theRemainder == null ) {
                return false;
            }

            if( theTraversedSpec == null ) {
                if( realOther.theTraversedSpec != null ) {
                    return false;
                }
            } else if( !theTraversedSpec.equals( realOther.theTraversedSpec )) {
                return false;
            }

            if( !theReached.equals( realOther.theReached )) {
                return false;
            }

            if( theRemainder != null ) {
                return theRemainder.equals( realOther.theRemainder );
            }
            return true;
        }
        return false;
    }

    /**
     * Obtain the hash code, which is a combination of of the reached MeshObjects' hash codes.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        if( theTraversedSpec != null ) {
            return theReached.hashCode() ^ theTraversedSpec.hashCode(); // good enough
        } else {
            return theReached.hashCode(); // good enough
        }
    }

    /**
     * Determine whether this TraversalPath starts with all of the TraversalPath elements
     * of the passed-in test path.
     *
     * @param testPath the TraversalPath to test against
     * @return true if testPath is fully contained in this TraversalPath at the beginning
     */
    public boolean startsWith(
            TraversalPath testPath )
    {
        if( testPath == null ) {
            return true;
        }

        if( theTraversedSpec == null ) {
            if( testPath.theTraversedSpec != null ) {
                return false;
            }
        } else if( !theTraversedSpec.equals( testPath.theTraversedSpec )) {
            return false;
        }

        if( !theReached.equals( testPath.theReached )) {
            return false;
        }

        if( theRemainder != null ) {
            return theRemainder.startsWith( testPath.theRemainder );
        } else {
            return testPath.theRemainder == null;
        }
    }

    /**
     * Add a PropertyChangeListener listening to PropertyChangeEvents of Entities
     * on the TraversalPath.
     *
     * @param newListener the listener to add
     * @see #addWeakTraversalPathPropertyChangeListener
     * @see #addSoftTraversalPathPropertyChangeListener
     * @see #removeTraversalPathPropertyChangeListener
     */
    public synchronized void addDirectTraversalPathPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( theListeners == null ) {
            theListeners = new FlexiblePropertyChangeListenerSet();
        }
        theListeners.addDirect( newListener );

        if( theListeners.size() == 1 ) {
            // subscribe
            TraversalPath current = this;
            while( current != null ) {
                try {
                    current.getFirstMeshObject().addDirectPropertyChangeListener( this );
                    current = current.getNextSegment();

                } catch( MeshObjectsNotFoundException ex ) {
                    log.error( ex ); // this is probably the best we can do
                }
            }
        }
    }

    /**
     * Add a PropertyChangeListener listening to PropertyChangeEvents of Entities
     * on the TraversalPath.
     *
     * @param newListener the listener to add
     * @see #addDirectTraversalPathPropertyChangeListener
     * @see #addSoftTraversalPathPropertyChangeListener
     * @see #removeTraversalPathPropertyChangeListener
     */
    public synchronized void addWeakTraversalPathPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( theListeners == null ) {
            theListeners = new FlexiblePropertyChangeListenerSet();
        }
        theListeners.addWeak( newListener );

        if( theListeners.size() == 1 ) {
            // subscribe
            TraversalPath current = this;
            while( current != null ) {
                try {
                    current.getFirstMeshObject().addWeakPropertyChangeListener( this );
                    current = current.getNextSegment();
                    
                } catch( MeshObjectsNotFoundException ex ) {
                    log.error( ex ); // this is probably the best we can do
                }
            }
        }
    }

    /**
     * Add a PropertyChangeListener listening to PropertyChangeEvents of Entities
     * on the TraversalPath.
     *
     * @param newListener the listener to add
     * @see #addDirectTraversalPathPropertyChangeListener
     * @see #addWeakTraversalPathPropertyChangeListener
     * @see #removeTraversalPathPropertyChangeListener
     */
    public synchronized void addSoftTraversalPathPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        if( theListeners == null ) {
            theListeners = new FlexiblePropertyChangeListenerSet();
        }
        theListeners.addSoft( newListener );

        if( theListeners.size() == 1 ) {
            // subscribe
            TraversalPath current = this;
            while( current != null ) {
                try {
                    current.getFirstMeshObject().addSoftPropertyChangeListener( this );
                    current = current.getNextSegment();

                } catch( MeshObjectsNotFoundException ex ) {
                    log.error( ex ); // this is probably the best we can do
                }
            }
        }
    }

    /**
     * Remove a PropertyChangeListener listening to PropertyChangeEvents of Entities
     * on the TraversalPath.
     *
     * @param oldListener the listener to remove
     * @see #addDirectTraversalPathPropertyChangeListener
     * @see #addWeakTraversalPathPropertyChangeListener
     * @see #addSoftTraversalPathPropertyChangeListener
     */
    public synchronized void removeTraversalPathPropertyChangeListener(
            PropertyChangeListener oldListener )
    {
        if( theListeners == null ) {
            return; // defensive programming
        }

        theListeners.remove( oldListener );

        if( theListeners.isEmpty() ) {
            // unsubscribe
            theListeners = null;

            TraversalPath current = this;
            while( current != null ) {
                try {
                    current.getFirstMeshObject().removePropertyChangeListener( this );
                    current = current.getNextSegment();

                } catch( MeshObjectsNotFoundException ex ) {
                    log.error( ex ); // this is probably the best we can do
                }
            }
        }
    }

    /**
     * Callback from one of the MeshObjects in this set -- forward property change event.
     *
     * @param theEvent the event
     */
    public void propertyChange(
            PropertyChangeEvent theEvent )
    {
        // we don't need to check that we have subscribers -- we only get this when we have subscribers

        PropertyChangeEvent delegatedEvent;
        if( theEvent instanceof MeshObjectPropertyChangeEvent ) {
            delegatedEvent = new TraversalPathDelegatedPropertyChangeEvent(
                    this,
                    (MeshObjectPropertyChangeEvent) theEvent );

        } else if( theEvent instanceof MeshObjectRoleAddedEvent ) {
            MeshObjectRoleAddedEvent realEvent = (MeshObjectRoleAddedEvent) theEvent;
            delegatedEvent = new TraversalPathDelegatedRoleChangeEvent.Added( this, realEvent );

        } else if( theEvent instanceof MeshObjectRoleRemovedEvent ) {
            MeshObjectRoleRemovedEvent realEvent = (MeshObjectRoleRemovedEvent) theEvent;
            delegatedEvent = new TraversalPathDelegatedRoleChangeEvent.Removed( this, realEvent );

        } else if( theEvent instanceof MeshObjectStateEvent ) {
            return;

        } else {
            log.error( "unexpected subtype of PropertyChangeEvent: " + theEvent );
            return;
        }

        FlexiblePropertyChangeListenerSet listeners = theListeners;
        if( listeners != null ) {
            listeners.fireEvent( delegatedEvent );
        }
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "debugName",
                    "traversedSpec",
                    "theReachedIdentifier",
                    "remainder"
                },
                new Object[] {
                    theDebugName,
                    theTraversedSpec,
                    theReachedIdentifier,
                    theRemainder
                });
    }

    /**
     * Our name (if any). For debugging only.
     */
    protected String theDebugName;

    /**
     * The first TraversalSpecification that we traversed.
     */
    protected TraversalSpecification theTraversedSpec;

    /**
     * The MeshBase against which to resolve the MeshObject.
     */
    protected transient MeshBase theMeshBase;

    /**
     * The MeshObject that we reached after we traversed the TraversalSpecification.
     */
    protected transient MeshObject theReached;

    /**
     * The identifier of the MeshObject that we reached after we traversed the TraversalSpecification.
     */
    protected MeshObjectIdentifier theReachedIdentifier;

    /**
     * The remainder (aka second part) of this TraversalPath.
     */
    protected TraversalPath theRemainder;

    /**
     * The length of this TraversalPath. We store this to avoid the frequent, recursive
     * calculations.
     */
    protected int theSize;

    /**
     * Our listeners, if any.
     */
    protected transient FlexiblePropertyChangeListenerSet theListeners;
}
