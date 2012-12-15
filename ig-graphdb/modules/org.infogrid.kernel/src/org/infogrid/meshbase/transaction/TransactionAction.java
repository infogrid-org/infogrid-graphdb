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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;

/**
 * An action that is performed within Transaction boundaries.
 *
 * @param <T> the return type of the action
 */
public abstract class TransactionAction<T>
{
    /**
     * Constructor.
     */
    public TransactionAction()
    {
        theAllOrNothing = false;
    }

    /**
     * Constructor.
     *
     * @param allOrNothing if true, rollback the entire transaction upon an Exception; if false, abort at
     *        the location of the Exception
     */
    public TransactionAction(
            boolean allOrNothing )
    {
        theAllOrNothing = allOrNothing;
    }

    /**
     * Obtain the allOrNothing property.
     * 
     * @return the property
     */
    public final boolean getAllOrNothing()
    {
        return theAllOrNothing;
    }

    /**
     * Set the MeshBase on which this TransactionAction executes. This should not be invoked
     * by the application developer.
     *
     * @param meshBase the MeshBase
     */
    public void setMeshBase(
            MeshBase meshBase )
    {
        mb     = meshBase;
        life   = meshBase.getMeshBaseLifecycleManager();
        idFact = meshBase.getMeshObjectIdentifierFactory();
    }

    /**
     * Obtain the MeshBase on which this TransactionAction executes.
     * 
     * @return the MeshBase
     */
    public MeshBase getMeshBase()
    {
        return mb;
    }

    /**
     * Set the current Transaction, or reset the current Transaction to null. This should not be invoked
     * by the application developer.
     *
     * @param currentTx the current Transaction
     */
    public void setTransaction(
            Transaction currentTx )
    {
        tx = currentTx;
    }

    /**
     * Obtain the current Transaction.
     * 
     * @return the current Transaction
     */
    public Transaction getTransaction()
    {
        return tx;
    }

    /**
     * Execute the action. This will be invoked within valid Transaction
     * boundaries.
     *
     * @return a return object, if any
     * @throws Throwable this declaration makes it easy to implement this method
     */
    public abstract T execute()
        throws
            Throwable;

    /**
     * Overridable callback executed just prior to attempting to perform a commit.
     * 
     * @param tx the Transaction
     */
    public void preCommitTransaction(
            Transaction tx )
    {
        // no op
    }
    
    /**
     * Overridable callback executed just after having been successful performing a commit.
     * 
     * @param tx the Transaction
     */
    public void postCommitTransaction(
            Transaction tx )
    {
        // no op
    }

    /**
     * Overridable callback executed just prior to attempting to perform a rollback.
     * 
     * @param tx the Transaction
     * @param causeForRollback the Throwable that was the cause for this rollback
     */
    public void preRollbackTransaction(
            Transaction tx,
            Throwable   causeForRollback )
    {
        // no op
    }
    
    /**
     * Overridable callback executed just after having been successful performing a rollback.
     * 
     * @param tx the Transaction
     * @param causeForRollback the Throwable that was the cause for this rollback
     */
    public void postRollbackTransaction(
            Transaction tx,
            Throwable   causeForRollback )
    {
        // no op
    }
    
    /**
     * If true, rollback the entire transaction upon an Exception; if false, abort at the location of the Exception.
     */
    protected boolean theAllOrNothing;

    /**
     * The MeshBase on which this TransactionAction is to be performed.
     * This is held here to make writing code in anonymous subclasses less verbose.
     */
    protected MeshBase mb;

    /**
     * The MeshBaseLifecycleManager corresponding to this MeshBase.
     * This is held here to make writing code in anonymous subclasses less verbose.
     */
    protected MeshBaseLifecycleManager life;

    /**
     * The MeshObjectIdentifierFactory corresponding to this MeshBase.
     * This is held here to make writing code in anonymous subclasses less verbose.
     */
    protected MeshObjectIdentifierFactory idFact;

    /**
     * The current Transaction.
     */
    protected Transaction tx;
}
