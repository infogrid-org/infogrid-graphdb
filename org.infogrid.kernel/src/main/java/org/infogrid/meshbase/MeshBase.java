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

package org.infogrid.meshbase;

import java.beans.PropertyChangeListener;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.sweeper.Sweeper;
import org.infogrid.meshbase.transaction.MeshObjectLifecycleListener;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionActiveAlreadyException;
import org.infogrid.meshbase.transaction.TransactionAsapTimeoutException;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.meshbase.transaction.TransactionListener;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.LiveDeadObject;
import org.infogrid.util.QuitListener;
import org.infogrid.util.context.ObjectInContext;
import org.infogrid.meshbase.transaction.TransactionAction;
import org.infogrid.meshbase.transaction.TransactionActionException;
import org.infogrid.util.HasIdentifier;
import org.infogrid.util.HasIdentifierFinder;
import org.infogrid.util.IsDeadException;
import org.infogrid.util.text.HasStringRepresentation;

/**
  * <p>MeshBase represents the place where MeshObjects live. MeshBases collect MeshObjects
  * for management purposes. A MeshBase may be persistent (i.e. retain the contained
  * MeshObjects after reboot), or it may be volatile; however, this distinction is not
  * visible by a client of a MeshBase other than the result of the
  * {@link #isPersistent method}.</p>
  *
  * <p>Many "interesting" operations on the MeshBase are delegated to
  * managers of various sorts that may or may not be supported by any
  * given MeshBase implementation.</p>
  *
  * <p>A MeshBase's home object is a single MeshObject in the MeshBase
  * that is "known" to the outside world. This might often be some sort of
  * "directory object", or an object which is a logical entry point into the
  * information held by the MeshBase. The home object cannot be deleted, but
  * it may be updated (e.g. blessed, properties set, related etc.)</p>
  * 
  * <p>All operations potentially modifying information held in a MeshBase must be performed
  *    within a {@link Transaction}. A good pattern to use for Transactions is this:
  * <pre>
  * Transaction tx = null;
  * try {
  *    tx = meshBase.createTransactionXXX(); // use one of the factory methods
  *    // code that is subject to the Transaction
  * } catch( ... ) {
  *    // catch code for whatever Exceptions need to be handled
  * } finally {
  *    if( tx != null ) {
  *        tx.commit();
  *    }
  * }
  * </pre>
  * <p>This code works with all Transaction factory methods, including the ones that do not
  * actually create a Transaction under some circumstances because the current Thread
  * has an open Transaction already.</p>
  */
public interface MeshBase
        extends
            ObjectInContext,
            QuitListener,
            LiveDeadObject,
            HasIdentifier,
            HasIdentifierFinder,
            HasStringRepresentation
{    
    /**
     * Obtain the MeshBaseIdentifier that identifies this MeshBase.
     * 
     * @return the MeshBaseIdentifier
     */
    public abstract MeshBaseIdentifier getIdentifier();

    /**
      * Obtain the MeshBase's home object. The home object is
      * the only well-known object in a MeshBase. It is guaranteed to exist and
      * cannot be deleted.
      *
      * @return the MeshObject that is this MeshBase's home object
      */
    public abstract MeshObject getHomeObject();

    /**
     * <p>Find a MeshObject in this MeshBase by its identifier. Unlike
     * the {@link #accessLocally accessLocally} methods, this method purely considers MeshObjects in the
     * MeshBase, and does not attempt to obtain them if they are not in the MeshBase yet.</p>
     * <p>If not found, returns <code>null</code>.</p>
     * 
     * @param identifier the identifier of the MeshObject that shall be found
     * @return the found MeshObject, or null if not found
     * @see #findMeshObjectByIdentifierOrThrow
     */
    public abstract MeshObject findMeshObjectByIdentifier(
            MeshObjectIdentifier identifier );

    /**
     * <p>Find a set of MeshObjects in this MeshBase by their identifiers. Unlike
     *    the {@link #accessLocally accessLocally} methods, this method purely considers MeshObjects in the
     *    MeshBase, and does not attempt to obtain them if they are not in the MeshBase yet.</p>
     * <p>If one or more of the MeshObjects could not be found, returns <code>null</code> at
     *    the respective index in the returned array.</p>
     * 
     * @param identifiers the identifiers of the MeshObjects that shall be found
     * @return the found MeshObjects, which may contain null values for MeshObjects that were not found
     */
    public abstract MeshObject [] findMeshObjectsByIdentifier(
            MeshObjectIdentifier [] identifiers );

    /**
     * <p>Find a MeshObject in this MeshBase by its identifier. Unlike
     * the {@link #accessLocally accessLocally} methods, this method purely considers MeshObjects in the
     * MeshBase, and does not attempt to obtain them if they are not in the MeshBase yet.</p>
     * <p>If not found, throws {@link MeshObjectsNotFoundException MeshObjectsNotFoundException}.</p>
     * 
     * @param identifier the identifier of the MeshObject that shall be found
     * @return the found MeshObject, or null if not found
     * @throws MeshObjectsNotFoundException if the MeshObject was not found
     */
    public abstract MeshObject findMeshObjectByIdentifierOrThrow(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectsNotFoundException;

    /**
     * <p>Find a set of MeshObjects in this MeshBase by their identifiers. Unlike
     *    the {@link #accessLocally accessLocally} method, this method purely considers MeshObjects in the
     *    MeshBase, and does not attempt to obtain them if they are not in the MeshBase yet.</p>
     * <p>If one or more of the MeshObjects could not be found, throws
     *    {@link MeshObjectsNotFoundException MeshObjectsNotFoundException}.</p>
     * 
     * @param identifiers the identifiers of the MeshObjects that shall be found
     * @return the found MeshObjects, which may contain null values for MeshObjects that were not found
     * @throws MeshObjectsNotFoundException if one or more of the MeshObjects were not found
     */
    public abstract MeshObject [] findMeshObjectsByIdentifierOrThrow(
            MeshObjectIdentifier [] identifiers )
        throws
            MeshObjectsNotFoundException;

    /**
     * Obtain a MeshObject whose unique identifier is known.
     * 
     * @param identifier the identifier property of the MeshObject
     * @return the locally found MeshObject, or null if not found locally
     * @throws MeshObjectAccessException thrown if something went wrong accessing one or more MeshObjects
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract MeshObject accessLocally(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectAccessException,
            NotPermittedException;

    /**
     * Obtain N locally available MeshObjects whose unique identifiers are known.
     * 
     * @param identifiers the identifier properties of the MeshObjects
     * @return array of the same length as identifiers, with the locally found MeshObjects filled
     *         in at the same positions. If one or more of the MeshObjects were not found, the respective
     *         location in the array will be null.
     * @throws MeshObjectAccessException thrown if something went wrong accessing one or more MeshObjects
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public abstract MeshObject [] accessLocally(
            MeshObjectIdentifier [] identifiers )
        throws
            MeshObjectAccessException,
            NotPermittedException;

   /**
     * <p>Obtain a manager for MeshObject lifecycles.</p>
     * 
     * @return a MeshBaseLifecycleManager that works on this MeshBase
     */
    public abstract MeshBaseLifecycleManager getMeshBaseLifecycleManager();

    /**
     * Obtain the ModelBase that contains the type descriptions
     * for the content of this MeshBase.
     *
     * @return the MeshBase that contains the type descriptions
     */
    public abstract ModelBase getModelBase();

    /**
      * Determine whether this is a persistent MeshBase.
      * A MeshBase is persistent if the information stored in it last longer
      * than the lifetime of the virtual machine running this MeshBase.
      *
      * @return true if this is a persistent MeshBase.
      */
    public abstract boolean isPersistent();

    /**
     * Obtain the AccessManager associated with this MeshBase, if any.
     * The AccessManager controls access to the MeshObjects in this MeshBase.
     *
     * @return the AccessManager, if any
     */
    public abstract AccessManager getAccessManager();

    /**
     * Set a Sweeper for this MeshBase.
     *
     * @param newSweeper the new Sweeper
     */
    public abstract void setSweeper(
            Sweeper newSweeper );
    
    /**
     * Obtain the currently set Sweeper for this MeshBase, if any.
     *
     * @return the Sweeper, if any
     */
    public abstract Sweeper getSweeper();
    
    /**
     * Obtain a factory for MeshObjectIdentifiers that is appropriate for this MeshBase.
     *
     * @return the factory for MeshObjectIdentifiers
     */
    public abstract MeshObjectIdentifierFactory getMeshObjectIdentifierFactory();

    /**
     * Obtain the factory for MeshObjectSets.
     * 
     * @return the factory for MeshObjectSets
     * @see #setMeshObjectSetFactory
     */
    public abstract MeshObjectSetFactory getMeshObjectSetFactory();

    /**
     * Set a new factory for MeshObjectSets.
     * 
     * @param newValue the new factory
     * @see #getMeshObjectSetFactory
     */
    public abstract void setMeshObjectSetFactory(
            MeshObjectSetFactory newValue );

    /**
     * Create a new Transaction as soon as possible. This means the calling Thread may be suspended
     * for some amount of time before it can start, or it may time out.
     *
     * @return the created and started Transaction
     * @throws TransactionAsapTimeoutException a Transaction timeout has occurred
     */
    public abstract Transaction createTransactionAsap()
        throws
            TransactionAsapTimeoutException;

    /**
     * Create a new Transaction now, or throw an Exception if this is not possible at this very
     * moment. The calling Thread will not be suspended.
     *
     * @return the created and started Transaction
     * @throws TransactionActiveAlreadyException a Transaction was active already
     */
    public abstract Transaction createTransactionNow()
        throws
            TransactionActiveAlreadyException;

    /**
     * Create a new Transaction as soon as possible, but only if we are not currently on a Thread
     * that has already opened a Transaction. If we are on such a Thread, return null,
     * indicating that the operation can go ahead and there is no need to commit a
     * potential sub-Transaction. Otherwise behave like createTransactionAsap().
     *
     * @return the created and started Transaction, or null if one is already open on this Thread
     * @throws TransactionAsapTimeoutException a Transaction timeout has occurred
     */
    public abstract Transaction createTransactionAsapIfNeeded()
        throws
            TransactionAsapTimeoutException;

    /**
     * Create a Transaction now, but only if we are not currently on a Thread
     * that has already opened a Transaction. If we are on such a Thread, return null,
     * indicating that the operation can go ahead and there is no need to commit a
     * potential sub-Transaction. Otherwise behave like createTransactionNow().
     *
     * @return the created and started Transaction, or null if one is already open on this Thread
     * @throws TransactionActiveAlreadyException a Transaction was active already
     */
    public abstract Transaction createTransactionNowIfNeeded()
        throws
            TransactionActiveAlreadyException;

    /**
      * Obtain the currently active Transaction (if any).
      *
      * @return the currently active Transaction, or null if there is none
      */
    public abstract Transaction getCurrentTransaction();

    /**
     * Helper to check that we are on a Transaction, and it is the right Transaction.
     *
     * @return the current Transaction
     * @throws TransactionException throw if this has been invoked outside of proper Transaction boundaries
     */
    public abstract Transaction checkTransaction()
        throws
            TransactionException;

    /**
     * Perform this TransactionAction within an automatically generated Transaction
     * immediately. Evaluate any thrown TransactionActionException, and retry if requested.
     *
     * @param act the TransactionAction
     * @return a TransactionAction-specific return value
     * @throws TransactionActionException a problem occurred when executing the TransactionAction
     * @param <T> the type of return value
     */
    public abstract <T> T executeNow(
            TransactionAction<T> act )
        throws
            TransactionActionException;

    /**
     * Perform this TransactionAction within an automatically generated Transaction
     * as soon as possible. Evaluate any thrown TransactionActionException, and retry if requested.
     *
     * @param act the TransactionAction
     * @return a TransactionAction-specific return value
     * @throws TransactionActionException a problem occurred when executing the TransactionAction
     * @param <T> the type of return value
     */
    public abstract <T> T executeAsap(
            TransactionAction<T> act )
        throws
            TransactionActionException;

    /**
     * Clear the in-memory cache, if this MeshBase has one. This method only makes any sense
     * if the MeshBase is persistent. Any MeshBase may implement this as a no op.
     * This must only be invoked if no clients hold references to MeshObjects in the cache.
     */
    public abstract void clearMemoryCache();

    /**
     * Determine the set of MeshObjects that are neighbors of both of the passed-in MeshObjects.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param one the first MeshObject
     * @param two the second MeshObject
     * @return the set of MeshObjects that are neighbors of both MeshObject one and MeshObject two
     */
    public abstract MeshObjectSet findCommonNeighbors(
            MeshObject one,
            MeshObject two );

    /**
     * Determine the set of MeshObjects that are neighbors of all of the passed-in MeshObjects.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param all the MeshObjects whose common neighbors we seek.
     * @return the set of MeshObjects that are neighbors of all MeshObjects
     */
    public abstract MeshObjectSet findCommonNeighbors(
            MeshObject [] all );
    
    /**
     * Determine the set of MeshObjects that are neighbors of both of the passed-in MeshObjects
     * while playing particular RoleTypes.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param one the first MeshObject
     * @param oneType the RoleType to be played by the first MeshObject with the to-be-found MeshObjects
     * @param two the second MeshObject
     * @param twoType the RoleType to be played by the second MeshObject with the to-be-found MeshObjects
     * @return the set of MeshObjects that are neighbors of both MeshObject one and MeshObject two
     */
    public abstract MeshObjectSet findCommonNeighbors(
            MeshObject one,
            RoleType   oneType,
            MeshObject two,
            RoleType   twoType );

    /**
     * Determine the set of MeshObjects that are neighbors of all of the passed-in MeshObjects
     * while playing particular RoleTypes.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param all the MeshObjects whose common neighbors we seek.
     * @param allTypes the RoleTypes to be played by the MeshObject at the same position in the array
     *        with the to-be-found MeshObjects
     * @return the set of MeshObjects that are neighbors of all MeshObjects
     */
    public abstract MeshObjectSet findCommonNeighbors(
            MeshObject [] all,
            RoleType []   allTypes );

    /**
     * Add a PropertyChangeListener to this MeshBase, without using a Reference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addDirectPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener to this MeshBase, using a WeakReference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addWeakPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Add a PropertyChangeListener to this MeshBase, using a SoftReference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public abstract void addSoftPropertyChangeListener(
            PropertyChangeListener newListener );

    /**
     * Remove a PropertyChangeListener from this MeshBase.
     *
     * @param oldListener the to-be-removed PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #addDirectPropertyChangeListener
     */
    public abstract void removePropertyChangeListener(
            PropertyChangeListener oldListener );

    /**
      * Add a TransactionListener to this MeshBase, without using a Reference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addWeakTransactionListener
      * @see #addSoftTransactionListener
      * @see #removeTransactionListener
      */
    public abstract void addDirectTransactionListener(
            TransactionListener newListener );

    /**
      * Add a TransactionListener to this MeshBase, using a WeakReference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addDirectTransactionListener
      * @see #addSoftTransactionListener
      * @see #removeTransactionListener
      */
    public abstract void addWeakTransactionListener(
            TransactionListener newListener );

    /**
      * Add a TransactionListener to this MeshBase, using a SoftReference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addWeakTransactionListener
      * @see #addDirectTransactionListener
      * @see #removeTransactionListener
      */
    public abstract void addSoftTransactionListener(
            TransactionListener newListener );

    /**
      * Remove a TransactionListener from this MeshBase.
      *
      * @param oldListener the to-be-removed TransactionListener
      * @see #addWeakTransactionListener
      * @see #addSoftTransactionListener
      * @see #addDirectTransactionListener
      */
    public abstract void removeTransactionListener(
            TransactionListener oldListener );

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, without using a Reference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public abstract void addDirectMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener );

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, using a WeakReference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public abstract void addWeakMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener );

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, using a SoftReference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public abstract void addSoftMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener );

    /**
     * Unsubscribe from events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase.
     * 
     * @param oldListener the to-be-removed MMeshObjectLifecycleListener@see #addMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     */
    public abstract void removeMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener oldListener );

    /**
     * Tell this MeshBase that we don't need it any more.
     *
     * @param isPermanent if true, this MeshBase will go away permanmently; if false,
     *         it may come alive again some time later
     * @throws IsDeadException thrown if this object is dead already
     */
    public void die(
             boolean isPermanent )
         throws
             IsDeadException;
}
