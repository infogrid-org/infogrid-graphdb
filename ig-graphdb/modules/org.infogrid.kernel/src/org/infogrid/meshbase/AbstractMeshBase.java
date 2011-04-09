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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase;

import org.infogrid.meshbase.sweeper.Sweeper;
import java.beans.PropertyChangeListener;
import org.infogrid.mesh.AbstractMeshObject;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectGraphModificationException;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.MeshObjectIdentifierNotUniqueException;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.mesh.security.ThreadIdentityManager;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.text.MeshStringRepresentationParameters;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.transaction.DefaultTransaction;
import org.infogrid.meshbase.transaction.IllegalTransactionThreadException;
import org.infogrid.meshbase.transaction.MeshObjectCreatedEvent;
import org.infogrid.meshbase.transaction.MeshObjectDeletedEvent;
import org.infogrid.meshbase.transaction.MeshObjectLifecycleEvent;
import org.infogrid.meshbase.transaction.MeshObjectLifecycleListener;
import org.infogrid.meshbase.transaction.NotWithinTransactionBoundariesException;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionAction;
import org.infogrid.meshbase.transaction.TransactionActionException;
import org.infogrid.meshbase.transaction.TransactionActiveAlreadyException;
import org.infogrid.meshbase.transaction.TransactionAsapTimeoutException;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.meshbase.transaction.TransactionListener;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.AbstractFactory;
import org.infogrid.util.AbstractLiveDeadObject;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.CachingMap;
import org.infogrid.util.CannotFindHasIdentifierException;
import org.infogrid.util.Factory;
import org.infogrid.util.FactoryException;
import org.infogrid.util.FlexibleListenerSet;
import org.infogrid.util.FlexiblePropertyChangeListenerSet;
import org.infogrid.util.Identifier;
import org.infogrid.util.IsDeadException;
import org.infogrid.util.QuitManager;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.context.Context;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * This abstract, partial implementation of MeshBase provides
 * functionality that is common to all (or at least most) implementations of MeshBase.
 */
public abstract class AbstractMeshBase
        extends
            AbstractLiveDeadObject
        implements
            MeshBase,
            CanBeDumped
{
    private static final Log log = Log.getLogInstance(AbstractMeshBase.class); // our own, private logger

    /**
     * Constructor for subclasses only. This does not initialize content.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param identifierFactory the factory for MeshObjectIdentifiers appropriate for this MeshBase
     * @param setFactory the factory for MeshObjectSets appropriate for this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param life the MeshBaseLifecycleManager to use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param cache the CachingMap that holds the MeshObjects in this MeshBase
     * @param context the Context in which this MeshBase runs
     */
    protected AbstractMeshBase(
            MeshBaseIdentifier                          identifier,
            MeshObjectIdentifierFactory                 identifierFactory,
            MeshObjectSetFactory                        setFactory,
            ModelBase                                   modelBase,
            AbstractMeshBaseLifecycleManager            life,
            AccessManager                               accessMgr,
            CachingMap<MeshObjectIdentifier,MeshObject> cache,
            Context                                     context )
    {
        if( identifier == null ) {
            throw new NullPointerException();
        }
        if( identifierFactory == null ) {
            throw new NullPointerException();
        }
        if( setFactory == null ) {
            throw new NullPointerException();
        }
        if( modelBase == null ) {
            throw new NullPointerException();
        }
        if( life == null ) {
            throw new NullPointerException();
        }
        // accessMgr may be null
        if( cache == null ) {
            throw new NullPointerException();
        }
        if( context == null ) {
            throw new NullPointerException();
        }

        this.theMeshBaseIdentifier          = identifier;
        this.theMeshObjectIdentifierFactory = identifierFactory;
        this.theMeshObjectSetFactory        = setFactory;
        this.theModelBase                   = modelBase;
        this.theMeshBaseLifecycleManager    = life;
        this.theAccessManager               = accessMgr;
        this.theCache                       = cache;
        this.theContext                     = context;

        this.theMeshObjectIdentifierFactory.setMeshBase( this ); // may throw IllegalStateException
        this.theMeshBaseLifecycleManager.setMeshBase(    this );

        QuitManager qm = getContext().findContextObject( QuitManager.class );
        if( qm != null ) {
            qm.addWeakQuitListener( this );
        }
    }

    /**
     * Enable subclasses to initialize the MeshBase's Home Object after the constructor has
     * returned. This will use the current time for timeCreated and timeUpdated of the newly
     * created home object.
     *
     * @return the Home Object
     */
    protected MeshObject initializeHomeObject()
    {
        long now = System.currentTimeMillis();

        return initializeHomeObject( now, now, -1L );
    }

    /**
     * Enable subclasses to initialize the MeshBase's Home Object after the constructor has
     * returned.
     *
     * @param timeCreated the time the created Home Object was (semantically) created
     * @param timeUpdated the time the created Home Object was last updated
     * @param timeRead    the time the created Home Object was last read
     * @return the Home Object
     */
    protected MeshObject initializeHomeObject(
            long          timeCreated,
            long          timeUpdated,
            long          timeRead )
    {
        MeshObjectIdentifier homeObjectIdentifier = theMeshObjectIdentifierFactory.getHomeMeshObjectIdentifier();

        MeshObject homeObject = this.findMeshObjectByIdentifier( homeObjectIdentifier );
        if( homeObject == null ) {
            Transaction tx = null;
            
            try {
                ThreadIdentityManager.sudo();

                tx = createTransactionNowIfNeeded();

                homeObject = this.getMeshBaseLifecycleManager().createMeshObject(
                        homeObjectIdentifier,
                        timeCreated,
                        timeUpdated,
                        timeRead,
                        -1L ); // home objects never auto-expire

                if( log.isDebugEnabled() ) {
                    log.debug( this + ": created home object " + homeObject );
                }

            } catch( MeshObjectIdentifierNotUniqueException ex ) {
                log.error( ex ); // should not happen, the outside if should find HomeObject if it exists

            } catch( TransactionException ex ) {
                log.error( ex );

            } catch( NotPermittedException ex ) {
                log.error( ex );

            } finally {
                if( tx != null ) {
                    tx.commitTransaction();
                }
                ThreadIdentityManager.sudone();
            }
        }
        return homeObject;
    }

    /**
     * Obtain the Context in which this <tt>ObjectInContext</tt> runs.
     *
     * @return the Context in which this <tt>ObjectInContext</tt> runs.
     */
    public final Context getContext()
    {
        return theContext;
    }

    /**
     * Obtain the MeshBaseIdentifier that identifies this MeshBase.
     * 
     * @return the MeshBaseIdentifier
     */
    public MeshBaseIdentifier getIdentifier()
    {
        return theMeshBaseIdentifier;
    }

    /**
     * Determine whether this object is being identified with the provided Identifier.
     * This is a useful method for those objects of type HasIdentifier that may listen
     * to multiple names.
     *
     * @param toTest the Identifier to test against
     * @return true if this HasIdentifier is being identified by the provided Identifier
     */
    public boolean isIdentifiedBy(
            Identifier toTest )
    {
        return theMeshBaseIdentifier.equals( toTest );
    }

    /**
      * Obtain the MeshBase's home object. The home object is
      * the only well-known object in a MeshBase, but it is guaranteed to exist and cannot
      * be deleted.
      *
      * @return the MeshObject that is this MeshBase's home object
      */
    public MeshObject getHomeObject()
    {
        // this is not subject to the sweeper

        MeshObject theHomeObject = theCache.get( theMeshObjectIdentifierFactory.getHomeMeshObjectIdentifier() );

        return theHomeObject;
    }
    
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
    public MeshObject findMeshObjectByIdentifier(
            MeshObjectIdentifier identifier )
    {
        if( identifier == null ) {
            throw new NullPointerException();
        }
        MeshObject ret = theCache.get( identifier );

        Sweeper s = theSweeper;
        if( ret != null && s != null ) {
            ret = s.getSweepPolicy().potentiallyFilter( ret );
        }
        return ret;
    }

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
    public MeshObject [] findMeshObjectsByIdentifier(
            MeshObjectIdentifier[] identifiers )
    {
        MeshObject [] ret = new MeshObject[ identifiers.length ];
        for( int i=0 ; i<identifiers.length ; ++i ) {
            ret[i] = findMeshObjectByIdentifier( identifiers[i] );
        }
        return ret;
    }

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
    public MeshObject findMeshObjectByIdentifierOrThrow(
            MeshObjectIdentifier identifier )
        throws
            MeshObjectsNotFoundException
    {
        MeshObject ret = findMeshObjectByIdentifier( identifier );
        if( ret == null ) {
            throw new MeshObjectsNotFoundException( this, identifier );
        }
        return ret;
    }

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
    public MeshObject [] findMeshObjectsByIdentifierOrThrow(
            MeshObjectIdentifier [] identifiers )
        throws
            MeshObjectsNotFoundException
    {
        MeshObject []           ret      = new MeshObject[ identifiers.length ];
        MeshObjectIdentifier [] notFound = null; // allocated when needed
        int                     count    = 0;
        
        for( int i=0 ; i<identifiers.length ; ++i ) {
            ret[i] = findMeshObjectByIdentifier( identifiers[i] );
            if( ret[i] == null ) {
                if( notFound == null ) {
                    notFound = new MeshObjectIdentifier[ identifiers.length ];
                }
                notFound[ count++ ] = identifiers[i];
            }
        }
        if( count == 0 ) {
            return ret;
        }
        if( count < notFound.length ) {
            notFound = ArrayHelper.copyIntoNewArray( notFound, 0, count, MeshObjectIdentifier.class );
        }
        throw new MeshObjectsNotFoundException( this, notFound );
    }

    /**
     * Find a MeshObject from its Identifier.
     *
     * @param identifier the Identifier
     * @return the found HasIdentifier
     * @throws CannotFindHasIdentifierException thrown if the MeshObject cannot be found
     */
    public MeshObject find(
            Identifier identifier )
        throws
            CannotFindHasIdentifierException
    {
        try {
            MeshObject ret = accessLocally( (MeshObjectIdentifier) identifier );

            if( ret == null ) {
                throw new CannotFindHasIdentifierException(  identifier );
            }

            return ret;

        } catch( MeshObjectAccessException ex ) {
            throw new CannotFindHasIdentifierException( identifier, ex );

        } catch( NotPermittedException ex ) {
            throw new CannotFindHasIdentifierException( identifier, ex );
        }
    }

    /**
     * Obtain a MeshObject whose unique identifier is known. This is the default implementation,
     * it may be overridden by subclasses.
     *
     * @param nameOfLocalObject the identifier property of the MeshObject
     * @return the locally found MeshObject, or null if not found locally
     * @throws MeshObjectAccessException thrown if something went wrong accessing the MeshObject
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public MeshObject accessLocally(
            MeshObjectIdentifier nameOfLocalObject )
        throws
            MeshObjectAccessException,
            NotPermittedException
    {
        return findMeshObjectByIdentifier( nameOfLocalObject );
    }

    /**
     * Obtain N locally available MeshObjects whose unique identifiers are known.
     * This is the default implementation, it may be overridden by subclasses.
     * 
     * @param identifiers the identifier properties of the MeshObjects
     * @return array of the same length as identifiers, with the locally found MeshObjects filled
     *         in at the same positions. If one or more of the MeshObjects were not found, the respective
     *         location in the array will be null.
     * @throws MeshObjectAccessException thrown if something went wrong accessing one or more MeshObjects
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public MeshObject [] accessLocally(
            MeshObjectIdentifier[] identifiers )
        throws
            MeshObjectAccessException,
            NotPermittedException
    {
        MeshObject [] ret = new MeshObject[ identifiers.length ];
        for( int i=0 ; i<identifiers.length ; ++i ) {
            ret[i] = findMeshObjectByIdentifier( identifiers[i] );
        }
        return ret;
    }

    /**
      * <p>This announces that a quit is imminent. Note that because of
      * potential communication between various Objects that receive this
      * notification in arbitrary order, the Object still needs to be
      * functional after it has received this message and react to other
      * object's requests (if any).</p>
      *
      * <p>This overridable default implementation does nothing.</p>
      */
    public void prepareForQuit()
    {
    }

    /**
     * Tell this object that we don't need it any more. By default, we assume this
     * initiates a non-permanent die().
     *
     * @throws IsDeadException if called on an object that is dead already
     */
    public void die()
        throws
            IsDeadException
    {
        die( false );
    }

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
             IsDeadException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "die", isPermanent );
        }

        // let current transaction finish for no more than 5 seconds
        int count = 25;
        while( count> 0 && theCurrentTransaction != null ) {
            try {
                Thread.sleep( asapRetryInterval );
                --count;

            } catch( InterruptedException ex ) {
                // ignore
            }
        }

        // let's die even if the transaction is not done yet

        makeDead();
         
        internalDie( isPermanent );

        theLifecycleEventListeners = null;
        theTransactionListeners    = null;
        theSweeper                 = null;
        // let's not set the cache to null, we want to know that it is collected at the same time

        QuitManager qm = getContext().findContextObject( QuitManager.class );

        if( qm != null ) {
            qm.removeQuitListener( this ); // in case we are told to die by someone else than QuitManager
        }
    }

    /**
      * This overridable method is invoked during die() and lets subclasses of AbstractMeshBase
      * clean up on their own. This implementation does nothing.
      * 
      * @param isPermanent if true, this MeshBase will go away permanmently; if false, it may come alive again some time later
      */
     protected void internalDie(
             boolean isPermanent )
     {
         // no op
     }

    /**
     * <p>Obtain a manager for MeshObject lifecycles.</p>
     * 
     * @return a MeshBaseLifecycleManager that works on this MeshBase
     */
    public MeshBaseLifecycleManager getMeshBaseLifecycleManager()
    {
        return theMeshBaseLifecycleManager;
    }

    /**
      * Obtain the ModelBase that contains the type descriptions
      * for the content of this MeshBase.
      *
      * @return the MeshBase that contains the type descriptions
      */
    public final ModelBase getModelBase()
    {
        return theModelBase;
    }

    /**
      * Determine whether this is a persistent MeshBase.
      * A MeshBase is persistent if the information stored in it last longer
      * than the lifetime of the virtual machine running this MeshBase.
      *
      * @return true if this is a persistent MeshBase.
      */
    public boolean isPersistent()
    {
        return theCache.isPersistent();
    }

    /**
     * Obtain the AccessManager associated with this MeshBase, if any.
     * The AccessManager controls access to the MeshObjects in this MeshBase.
     *
     * @return the AccessManager, if any
     */
    public AccessManager getAccessManager()
    {
        return theAccessManager;
    }
    
    /**
     * Set a Sweeper for this MeshBase.
     *
     * @param newSweeper the new Sweeper
     */
    public void setSweeper(
            Sweeper newSweeper )
    {
        theSweeper = newSweeper;
    }
    
    /**
     * Obtain the currently set Sweeper for this MeshBase, if any.
     *
     * @return the Sweeper, if any
     */
    public Sweeper getSweeper()
    {
        return theSweeper;
    }

    /**
     * Obtain a factory for MeshObjectIdentifiers that is appropriate for this MeshBase.
     *
     * @return the factory for MeshObjectIdentifiers
     */
    public MeshObjectIdentifierFactory getMeshObjectIdentifierFactory()
    {
        return theMeshObjectIdentifierFactory;
    }

    /**
     * Obtain the factory for MeshObjectSets.
     * 
     * @return the factory for MeshObjectSets
     * @see #setMeshObjectSetFactory
     */
    public MeshObjectSetFactory getMeshObjectSetFactory()
    {
        return theMeshObjectSetFactory;
    }

    /**
     * Set a new factory for MeshObjectSets.
     * 
     * @param newValue the new factory
     * @see #getMeshObjectSetFactory
     */
    public void setMeshObjectSetFactory(
            MeshObjectSetFactory newValue )
    {
        theMeshObjectSetFactory = newValue;
    }

    /**
     * Create a new Transaction as soon as possible. This means the calling Thread may be suspended
     * for some amount of time before it can start, or it may time out.
     *
     * @return the created and started Transaction
     * @throws TransactionAsapTimeoutException a Transaction timeout has occurred
     */
    public final Transaction createTransactionAsap()
        throws
            TransactionAsapTimeoutException
    {
        if( log.isTraceEnabled()) {
            log.traceMethodCallEntry( this, "createTransactionAsap" );
        }

        Transaction existingTransaction = null;
        Transaction ret                 = null;
        int counter = nTries;

        while( ret == null && counter -- > 0 ) {
            synchronized( this ) {
                existingTransaction = theCurrentTransaction;
                if( theCurrentTransaction == null ) {
                    theCurrentTransaction = createNewTransaction();
                    ret = theCurrentTransaction;
                }
            }
            if( ret == null ) {
                try {
                    Thread.sleep( asapRetryInterval );
                } catch( InterruptedException ex ) {
                }
            }
        }
        if( ret == null ) {
            throw new TransactionAsapTimeoutException( this, existingTransaction );
        }

        fireTransactionStartedEvent( ret );

        return ret;
    }

    /**
     * Create a new Transaction now, or throw an Exception if this is not possible at this very
     * moment. The calling Thread will not be suspended.
     *
     * @return the created and started Transaction
     * @throws TransactionActiveAlreadyException a Transaction was active already
     */
    public final Transaction createTransactionNow()
        throws
            TransactionActiveAlreadyException
    {
        if( log.isTraceEnabled()) {
            log.traceMethodCallEntry( this, "createTransactionNow" );
        }

        Transaction ret;
        synchronized( this ) {
            if( theCurrentTransaction == null ) {
                theCurrentTransaction = createNewTransaction();
                ret = theCurrentTransaction;
            } else {
                throw new TransactionActiveAlreadyException( this, theCurrentTransaction );
            }
        }

        fireTransactionStartedEvent( ret );

        return ret;
    }

    /**
     * Create a new Transaction as soon as possible, but only if we are not currently on a Thread
     * that has already opened a Transaction. If we are on such a Thread, return null,
     * indicating that the operation can go ahead and there is no need to commit a
     * potential sub-Transaction. Otherwise behave like createTransactionAsap().
     *
     * @return the created and started Transaction, or null if one is already open on this Thread
     * @throws TransactionAsapTimeoutException a Transaction timeout has occurred
     */
    public final Transaction createTransactionAsapIfNeeded()
        throws
            TransactionAsapTimeoutException
    {
        if( log.isTraceEnabled()) {
            log.traceMethodCallEntry( this, "createTransactionAsapIfNeeded" );
        }

        Transaction existingTransaction = null;
        Transaction ret                 = null;
        int counter = nTries;

        while( ret == null && counter -- > 0 ) {
            synchronized( this ) {
                existingTransaction = theCurrentTransaction;
                if( theCurrentTransaction == null ) {
                    theCurrentTransaction = createNewTransaction();
                    ret = theCurrentTransaction;
                } else {
                    try {

                        theCurrentTransaction.checkThreadIsAllowed();
                        return null;

                    } catch( IllegalTransactionThreadException ex ) {
                        // do nothing
                    }
                }
            }
            if( ret == null ) {
                try {
                    Thread.sleep( asapRetryInterval );
                } catch( InterruptedException ex ) {
                }
            }
        }
        if( ret == null ) {
            throw new TransactionAsapTimeoutException( this, existingTransaction );
        }

        fireTransactionStartedEvent( ret );

        return ret;
    }

    /**
     * Create a Transaction now, but only if we are not currently on a Thread
     * that has already opened a Transaction. If we are on such a Thread, return null,
     * indicating that the operation can go ahead and there is no need to commit a
     * potential sub-Transaction. Otherwise behave like createTransactionNow().
     *
     * @return the created and started Transaction, or null if one is already open on this Thread
     * @throws TransactionActiveAlreadyException a Transaction was active already
     */
    public final Transaction createTransactionNowIfNeeded()
        throws
            TransactionActiveAlreadyException
    {
        if( log.isTraceEnabled()) {
            log.traceMethodCallEntry( this, "createTransactionNowIfNeeded" );
        }
        
        Transaction ret;
        synchronized( this ) {
            if( theCurrentTransaction == null ) {
                theCurrentTransaction = createNewTransaction();
                ret = theCurrentTransaction;
            } else {
                try {
                    theCurrentTransaction.checkThreadIsAllowed();

                    ret = null;

                } catch( IllegalTransactionThreadException ex ) {
                    throw new TransactionActiveAlreadyException( this, theCurrentTransaction );
                }
            }
        }
        fireTransactionStartedEvent( ret );

        return ret;
    }

    /**
     * Factory method to create a new Transaction. This overridable implementation
     * creates an DefaultTransaction.
     * 
     * @return the newly created Transaction
     */
    protected Transaction createNewTransaction()
    {
        return new DefaultTransaction( this );
    }

    /**
      * Obtain the currently active Transaction (if any).
      *
      * @return the currently active Transaction, or null if there is none
      */
    public final Transaction getCurrentTransaction()
    {
        return theCurrentTransaction;
    }

    /**
     * Check whether a Transaction is active is on this Thread, and whether it is
     * the right Transaction.
     *
     * @return the current Transaction
     * @throws TransactionException thrown if there was no valid Transaction
     */
    public synchronized Transaction checkTransaction()
        throws
            TransactionException
    {
        if( theCurrentTransaction == null ) {
            throw new NotWithinTransactionBoundariesException( this );
        }
        theCurrentTransaction.checkThreadIsAllowed();
        return theCurrentTransaction;
    }

    /**
     * Perform this TransactionAction within an automatically generated Transaction
     * immediately. Evaluate any thrown TransactionActionException, and retry if requested.
     *
     * @param act the TransactionAction
     * @return a TransactionAction-specific return value
     * @throws TransactionActionException a problem occurred when executing the TransactionAction
     * @param <T> the type of return value
     */
    public <T> T executeNow(
            TransactionAction<T> act )
        throws
            TransactionActionException
    {
        Factory<Void,Transaction,Void> txFactory = new AbstractFactory<Void,Transaction,Void>() {
                public Transaction obtainFor(
                        Void key,
                        Void argument )
                    throws
                        FactoryException
                {
                    try {
                        return createTransactionNowIfNeeded();
                    } catch( TransactionException ex ) {
                        throw new FactoryException( this, ex );
                    }
                }
        };

        T ret = executeTransactionAction( txFactory, act );
        return ret;
    }

    /**
     * Perform this TransactionAction within an automatically generated Transaction
     * as soon as possible. Evaluate any thrown TransactionActionException, and retry if requested.
     *
     * @param act the TransactionAction
     * @return a TransactionAction-specific return value
     * @throws TransactionActionException a problem occurred when executing the TransactionAction
     * @param <T> the type of return value
     */
    public <T> T executeAsap(
            TransactionAction<T> act )
        throws
            TransactionActionException
    {
        Factory<Void,Transaction,Void> txFactory = new AbstractFactory<Void,Transaction,Void>() {
                public Transaction obtainFor(
                        Void key,
                        Void argument )
                    throws
                        FactoryException
                {
                    try {
                        return createTransactionAsapIfNeeded();

                    } catch( TransactionException ex ) {
                        throw new FactoryException( this, ex );
                    }
                }
        };

        T ret = executeTransactionAction( txFactory, act );
        return ret;
    }

    /**
     * Helper method to execute a TransactionAction.
     *
     * @param txFactory create the Transaction when needed
     * @param act the TransactionAction
     * @return a TransactionAction-specific return value
     * @throws TransactionActionException a problem occurred when executing the TransactionAction
     * @param <T> the type of return value
     */
    protected <T> T executeTransactionAction(
            Factory<Void,Transaction,Void> txFactory,
            TransactionAction<T>           act )
        throws
            TransactionActionException
    {
        T         ret         = null;
        Throwable firstThrown = null;

        act.setMeshBase( this );

        for( int counter = 0 ; counter < MAX_COMMIT_RETRIES ; ++counter ) {

            Transaction tx     = null;
            Throwable   thrown = null;
            try {
                tx = txFactory.obtainFor( null, null );

                act.setTransaction( tx );
                ret = act.execute();
                act.setTransaction( null );

                tx.commitTransaction();
                tx = null;

                return ret;

            } catch( FactoryException ex ) {
                thrown = ex;
                log.error( ex );
                return null;

            } catch( TransactionActionException.Rollback ex ) {
                return null;

            } catch( TransactionActionException.Retry ex ) {
                thrown = ex;
                // do nothing, stay in the loop

            } catch( TransactionException ex ) {
                thrown = ex;
                // do nothing, stay in the loop

            } catch( TransactionActionException.Error ex ) {
                throw ex;

            } catch( TransactionActionException ex ) {
                log.error( "This should not be possible", ex );
                throw new RuntimeException( ex );

            } catch( MeshObjectGraphModificationException ex ) {
                thrown = ex;
                if( act.getAllOrNothing() ) {
                    log.error( "Rolling back Transaction", ex );
                    return null; // rollback
                } else {
                    // else stay in the loop
                    log.warn( "Attempting to retry Transaction", ex );
                }

            } catch( Throwable ex ) {
                thrown = ex;
                log.error( ex );
                if( act.getAllOrNothing() ) {
                    log.error( "Rolling back Transaction", ex );
                    return null; // rollback
                } else {
                    // else stay in the loop
                    log.warn( "Attempting to retry Transaction", ex );
                }

            } finally {
                act.setTransaction( null );
                if( tx != null ) {
                    tx.rollbackTransaction( thrown );
                }
            }
            if( firstThrown == null ) {
                firstThrown = thrown;
            }
        }
        if( firstThrown == null ) {
            return null;

        } else if( firstThrown instanceof TransactionException ) {
            throw new TransactionActionException.FailedToCreateTransaction( (TransactionException) firstThrown );

        } else if( firstThrown instanceof TransactionActionException ) {
            throw (TransactionActionException) firstThrown;

        } else {
            throw new TransactionActionException.Error( firstThrown );
        }
    }

    /**
     * Clear the in-memory cache, if this MeshBase has any. This method only makes any sense
     * if the MeshBase is persistent. Any class may implement this as a no op.
     * This must only be invoked if no clients hold references to MeshObjects in the cache.
     */
    public synchronized void clearMemoryCache()
    {
        theCache.clearLocalCache();
    }

    /**
     * Determine the set of MeshObjects that are neighbors of both of the passed-in MeshObjects.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param one the first MeshObject
     * @param two the second MeshObject
     * @return the set of MeshObjects that are neighbors of both MeshObject one and MeshObject two
     */
    public MeshObjectSet findCommonNeighbors(
            MeshObject one,
            MeshObject two )
    {
        return findCommonNeighbors( new MeshObject[] { one, two } );
    }

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
    public MeshObjectSet findCommonNeighbors(
            MeshObject one,
            RoleType   oneType,
            MeshObject two,
            RoleType   twoType )
    {
        return findCommonNeighbors( new MeshObject[] { one, two }, new RoleType[] { oneType, twoType } );
    }

    /**
     * Determine the set of MeshObjects that are neighbors of all of the passed-in MeshObjects.
     * This is a convenience method that can have substantial performance benefits, depending on
     * the underlying implementation of MeshObject.
     *
     * @param all the MeshObjects whose common neighbors we seek.
     * @return the set of MeshObjects that are neighbors of all MeshObjects
     */
    public MeshObjectSet findCommonNeighbors(
            MeshObject [] all )
    {
        return findCommonNeighbors( all, new RoleType[ all.length ]);
    }

    /**
     * Tell the MeshBase that this AMeshObject needs to be saved into persistent
     * storage (if applicable per AMeshBase implementation).
     * 
     * @param obj the AbstractMeshObject to be saved
     */
    public void flushMeshObject(
            AbstractMeshObject obj )
    {
        theCache.putIgnorePrevious( obj.getIdentifier(), obj );
    }

    /**
     * Add a PropertyChangeListener to this MeshBase, without using a Reference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public final synchronized void addDirectPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        instantiatePropertyChangeListenersIfNeeded();

        thePropertyChangeListeners.addDirect( newListener );
    }

    /**
     * Add a PropertyChangeListener to this MeshBase, using a WeakReference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public final synchronized void addWeakPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        instantiatePropertyChangeListenersIfNeeded();

        thePropertyChangeListeners.addWeak( newListener );
    }

    /**
     * Add a PropertyChangeListener to this MeshBase, using a SoftReference.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public final synchronized void addSoftPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        instantiatePropertyChangeListenersIfNeeded();

        thePropertyChangeListeners.addSoft( newListener );
    }

    /**
     * Remove a PropertyChangeListener from this MeshBase.
     *
     * @param oldListener the to-be-removed PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #addDirectPropertyChangeListener
     */
    public final synchronized void removePropertyChangeListener(
            PropertyChangeListener oldListener )
    {
        thePropertyChangeListeners.remove( oldListener );
        if( thePropertyChangeListeners.isEmpty() ) {
            thePropertyChangeListeners = null;
        }
    }

    /**
      * Add a TransactionListener to this MeshBase, without using a Reference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addWeakTransactionListener
      * @see #addSoftTransactionListener
      * @see #removeTransactionListener
      */
    public final synchronized void addDirectTransactionListener(
            TransactionListener newListener )
    {
        instantiateTransactionListenersIfNeeded();
        
        theTransactionListeners.addDirect( newListener );
    }

    /**
      * Add a TransactionListener to this MeshBase, using a WeakReference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addDirectTransactionListener
      * @see #addSoftTransactionListener
      * @see #removeTransactionListener
      */
    public final synchronized void addWeakTransactionListener(
            TransactionListener newListener )
    {
        instantiateTransactionListenersIfNeeded();
        
        theTransactionListeners.addWeak( newListener );
    }

    /**
      * Add a TransactionListener to this MeshBase, using a SoftReference.
      *
      * @param newListener the to-be-added TransactionListener
      * @see #addWeakTransactionListener
      * @see #addDirectTransactionListener
      * @see #removeTransactionListener
      */
    public final synchronized void addSoftTransactionListener(
            TransactionListener newListener )
    {
        instantiateTransactionListenersIfNeeded();
        
        theTransactionListeners.addSoft( newListener );
    }

    /**
      * Remove a TransactionListener from this MeshBase.
      *
      * @param oldListener the to-be-removed TransactionListener
      * @see #addWeakTransactionListener
      * @see #addSoftTransactionListener
      * @see #addDirectTransactionListener
      */
    public final synchronized void removeTransactionListener(
            TransactionListener oldListener )
    {
        theTransactionListeners.remove( oldListener );
        if( theTransactionListeners.isEmpty() ) {
            theTransactionListeners = null;
        }
    }

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, without using a Reference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public final synchronized void addDirectMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener )
    {
        instantiateLifecycleEventListenersIfNeeded();

        theLifecycleEventListeners.addDirect( newListener );
    }

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, using a WeakReference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public final synchronized void addWeakMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener )
    {
        instantiateLifecycleEventListenersIfNeeded();

        theLifecycleEventListeners.addWeak( newListener );
    }

    /**
     * Subscribe to events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase, using a SoftReference.
     * 
     * @param newListener the to-be-added MMeshObjectLifecycleListener@see #removeMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     * @see #removeMeshObjectLifecycleEventListener
     */
    public final synchronized void addSoftMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener newListener )
    {
        instantiateLifecycleEventListenersIfNeeded();

        theLifecycleEventListeners.addSoft( newListener );
    }

    /**
     * Unsubscribe from events indicating the addition/removal/etc
     * of MeshObjects to/from this MeshBase.
     * 
     * @param oldListener the to-be-removed MMeshObjectLifecycleListener@see #addMeshObjectLifecycleEventListener
     * @see #addWeakMeshObjectLifecycleEventListener
     * @see #addSoftMeshObjectLifecycleEventListener
     * @see #addDirectMeshObjectLifecycleEventListener
     */
    public final synchronized void removeMeshObjectLifecycleEventListener(
            MeshObjectLifecycleListener oldListener )
    {
        theLifecycleEventListeners.remove( oldListener );
        if( theLifecycleEventListeners.isEmpty() ) {
            theLifecycleEventListeners = null;
        }
    }

    /**
     * Internal helper to instantiate thePropertyChangeListeners if needed.
     */
    protected void instantiatePropertyChangeListenersIfNeeded()
    {
        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
        }
    }

    /**
     * Internal helper to instantiate theTransactionListeners if needed.
     */
    protected void instantiateTransactionListenersIfNeeded()
    {
        if( theTransactionListeners == null ) {
            theTransactionListeners = new FlexibleListenerSet<TransactionListener,Transaction,Integer>()
            {
                public void fireEventToListener(
                        TransactionListener listener,
                        Transaction         tx,
                        Integer             flag )
                {
                    if( flag.intValue() == 0 ) {
                        listener.transactionStarted( tx );
                    } else if( flag.intValue() == 1 ) {
                        listener.transactionCommitted( tx );
                    } else {
                        listener.transactionRolledback( tx );
                    }
                }
            };
        }
    }

    /**
     * Internal helper to instantiate theLifecycleEventListeners if needed.
     */
    protected void instantiateLifecycleEventListenersIfNeeded()
    {
        if( theLifecycleEventListeners == null ) {
            theLifecycleEventListeners = new FlexibleListenerSet<MeshObjectLifecycleListener,MeshObjectLifecycleEvent,Integer>()
            {
                public void fireEventToListener(
                        MeshObjectLifecycleListener listener,
                        MeshObjectLifecycleEvent    event,
                        Integer                     flag )
                {
                    if( event instanceof MeshObjectCreatedEvent ) {
                        listener.meshObjectCreated( (MeshObjectCreatedEvent) event );
                    } else if( event instanceof MeshObjectDeletedEvent ) {
                        listener.meshObjectDeleted( (MeshObjectDeletedEvent) event );
                    } else {
                        log.error( "Unexpected event: " + event );
                    }
                }
            };
        }
    }

    /**
      * Indicate that a Transaction on this MeshBase has been committed.
      * This is only called by a Transaction itself, and not the application programmer.
      */
    public final void transactionCommitted()
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "transactionCommitted" );
        }

        Transaction oldTransaction;
        synchronized( this ) {
            oldTransaction = theCurrentTransaction;

            log.assertLog( oldTransaction, "cannot commit empty transaction" );

            theCurrentTransaction = null;
        }

        transactionCommittedHook( oldTransaction );

        fireTransactionCommittedEvent( oldTransaction );
    }

    /**
     * Indicate that a Transaction on this MeshBase has been rolled back.
     * This is only called by a Transaction itself, and not the application programmer.
     *
     * @param thrown the Throwable that caused us to attempt to rollback the Transaction
     */
    public final void transactionRolledback(
            Throwable thrown )
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "transactionRolledback", thrown );
        }

        Transaction oldTransaction;
        synchronized( this ) {
            oldTransaction = theCurrentTransaction;

            log.assertLog( oldTransaction, "cannot roll back empty transaction" );

            theCurrentTransaction = null;
        }

        transactionRolledbackHook( oldTransaction );

        fireTransactionRolledbackEvent( oldTransaction );
    }

    /**
     * This method may be overridden by subclasses to perform suitable actions when a
     * Transaction was committed.
     *
     * @param tx Transaction the Transaction that was committed
     */
    protected void transactionCommittedHook(
            Transaction tx )
    {
        // noop
    }

    /**
     * This method may be overridden by subclasses to perform suitable actions when a
     * Transaction was rolled back.
     *
     * @param tx Transaction the Transaction that was rolled back
     */
    protected void transactionRolledbackHook(
            Transaction tx )
    {
        // noop
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return String representation
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        boolean isDefaultMeshBase = equals( pars.get( MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY ));

        String key;
        if( isDefaultMeshBase ) {
            key = DEFAULT_MESH_BASE_ENTRY;
        } else {
            key = NON_DEFAULT_MESH_BASE_ENTRY;
        }

        String ret = rep.formatEntry(
                getClass(),
                key,
                pars,
        /* 0 */ this );

        return ret;        
    }

    /**
     * Obtain the start part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkStart(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        boolean isDefaultMeshBase   = equals( pars.get(  MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY ));
        String  contextPath         = (String) pars.get( StringRepresentationParameters.WEB_RELATIVE_CONTEXT_KEY );
        String  target              = (String) pars.get( StringRepresentationParameters.LINK_TARGET_KEY );
        String  title               = (String) pars.get( StringRepresentationParameters.LINK_TITLE_KEY );
        String  additionalArguments = (String) pars.get( StringRepresentationParameters.HTML_URL_ADDITIONAL_ARGUMENTS );

        String key;
        if( isDefaultMeshBase ) {
            key = DEFAULT_MESH_BASE_LINK_START_ENTRY;
        } else {
            key = NON_DEFAULT_MESH_BASE_LINK_START_ENTRY;
        }
        if( target == null ) {
            target = "_self";
        }

        String ret = rep.formatEntry(
                getClass(),
                key,
                pars,
        /* 0 */ this,
        /* 1 */ contextPath,
        /* 2 */ additionalArguments,
        /* 3 */ target,
        /* 4 */ title );

        return ret;
    }

    /**
     * Obtain the end part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     * 
     * @param rep the StringRepresentation
     * @param pars the parameters to use
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        boolean isDefaultMeshBase = true;
        String  contextPath       = null;

        if( pars != null ) {
            isDefaultMeshBase = equals( pars.get( MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY ));
            contextPath       = (String) pars.get( StringRepresentationParameters.WEB_RELATIVE_CONTEXT_KEY );
        }

        String key;
        if( isDefaultMeshBase ) {
            key = DEFAULT_MESH_BASE_LINK_END_ENTRY;
        } else {
            key = NON_DEFAULT_MESH_BASE_LINK_END_ENTRY;
        }

        String ret = rep.formatEntry(
                getClass(),
                key,
                pars,
        /* 0 */ this,
        /* 1 */ contextPath );

        return ret;        
    }

    /**
      * Notify TransactionListeners that a Transaction has been started.
      * This shall not be invoked by the application programmer.
      *
      * @param theTransaction the Transaction that has been started
      */
    public final void fireTransactionStartedEvent(
            Transaction theTransaction )
    {
        FlexibleListenerSet<TransactionListener,Transaction,Integer> listeners = theTransactionListeners;
        if( listeners != null ) {
            listeners.fireEvent( theTransaction, 0 );
        }
    }

    /**
      * Notify TransactionListeners that a Transaction has been committed.
      * This shall not be invoked by the application programmer.
      *
      * @param theTransaction the Transaction that has been committed
      */
    public void fireTransactionCommittedEvent(
            Transaction theTransaction )
    {
        FlexibleListenerSet<TransactionListener,Transaction,Integer> listeners = theTransactionListeners;
        if( listeners != null ) {
            listeners.fireEvent( theTransaction, 1 );
        }
    }

    /**
      * Notify TransactionListeners that a Transaction has been rolled back.
      * This shall not be invoked by the application programmer.
      *
      * @param theTransaction the Transaction that has been rolled back
      */
    public void fireTransactionRolledbackEvent(
            Transaction theTransaction )
    {
        FlexibleListenerSet<TransactionListener,Transaction,Integer> listeners = theTransactionListeners;
        if( listeners != null ) {
            listeners.fireEvent( theTransaction, 2 );
        }
    }

    /**
     * Fire a property change event.
     *
     * @param source the sending object
     * @param propertyName name of the property that changed
     * @param oldValue the old value of the property
     * @param newValue the new value of the property
     */
    protected void firePropertyChange(
            Object source,
            String propertyName,
            Object oldValue,
            Object newValue )
    {
        // let's be thread-safe here
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;

        if( listeners != null ) {
            listeners.firePropertyChange( source, propertyName, oldValue, newValue );
        }
    }

    /**
     * Notify MeshObjectLifecycleListeners that a MeshObjectLifecycleEvent occurred.
     * This shall not be invoked by the application programmer.
     * 
     * @param theEvent the MAbstractMeshObjectLifecycleEvent
     */
    public final void notifyLifecycleEvent(
            MeshObjectLifecycleEvent theEvent )
    {
        FlexibleListenerSet<MeshObjectLifecycleListener,MeshObjectLifecycleEvent,Integer> listeners = theLifecycleEventListeners;
        
        if( listeners != null ) {
            listeners.fireEvent( theEvent );
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
                    "theMeshBaseIdentifier"
                },
                new Object[] {
                    theMeshBaseIdentifier
                });
    }

    /**
     * The identifier of this MeshBase.
     */
    protected MeshBaseIdentifier theMeshBaseIdentifier;

    /**
      * The ModelBase containing the MeshTypes used by this MeshBase.
      */
    protected ModelBase theModelBase;

    /**
     * The lifecycle manager.
     */
    protected AbstractMeshBaseLifecycleManager theMeshBaseLifecycleManager;

    /**
     * The AccessManager that enforces access control, if any.
     */
    protected AccessManager theAccessManager;

    /**
     * The Sweeper that sweeps the content of the MeshBase, if any.
     */
    protected Sweeper theSweeper;
    
    /**
     * The factory for MeshObjectIdentifiers.
     */
    protected MeshObjectIdentifierFactory theMeshObjectIdentifierFactory;
    
    /**
     * The factory for MeshObjectSets.
     */
    protected MeshObjectSetFactory theMeshObjectSetFactory;

    /**
     * The internal cache of MeshObjects. This may be implemented in a variety of ways,
     * e.g. in-memory (only), on-disk (only), or any combination.
     */
    protected CachingMap<MeshObjectIdentifier,MeshObject> theCache;

    /**
      * The PropertyChangeListeners (if any).
      */
    private FlexiblePropertyChangeListenerSet thePropertyChangeListeners = null;

    /**
      * The TransactionListeners (if any).
      */
    private FlexibleListenerSet<TransactionListener,Transaction,Integer> theTransactionListeners = null;

    /**
      * The MeshObjectLifecycleEventListeners (if any).
      */
    private FlexibleListenerSet<MeshObjectLifecycleListener, MeshObjectLifecycleEvent, Integer> theLifecycleEventListeners = null;

    /**
      * The current Transaction, if any.
      */
    private Transaction theCurrentTransaction;

    /**
      * The Context in which we run.
      */
    private Context theContext;

    /**
      * The number of tries before "asap" Transactions time out.
      */
    private int nTries = theResourceHelper.getResourceIntegerOrDefault(
            "NTriesForAsapTransactions",
            10 );

    /**
      * The interval in milliseconds before an "asap" Transaction is retried.
      */
    private int asapRetryInterval = theResourceHelper.getResourceIntegerOrDefault(
            "AsapTransactionRetryInterval",
            200 );

    /**
     * Our way to find our resources.
     */
    private static final ResourceHelper theResourceHelper = ResourceHelper.getInstance( AbstractMeshBase.class );

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_ENTRY = "DefaultMeshBaseString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_START_ENTRY = "DefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_END_ENTRY = "DefaultMeshBaseLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_ENTRY = "NonDefaultMeshBaseString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_START_ENTRY = "NonDefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_END_ENTRY = "NonDefaultMeshBaseLinkEndString";

    /**
     * The maximum number of retries for a Transaction.
     */
    public static final int MAX_COMMIT_RETRIES = theResourceHelper.getResourceIntegerOrDefault( "MaxCommitRetries", 5 );
}
