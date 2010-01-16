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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseIdentifier;

/**
  * A superclass for all {@link Transaction}-related Exceptions.
  */
public abstract class TransactionException
        extends
            Exception
{
    /**
     * Constructor.
     *
     * @param mb the MeshBase that raised this TransactionException
     * @param tx the Transaction that raised this TransactionException
     */
    protected TransactionException(
            MeshBase    mb,
            Transaction tx )
    {
        theTransactable = mb;
        theIdentifier   = mb.getIdentifier();
        theTransaction  = tx;
    }

    /**
     * Obtain the MeshBase that raised this TransactionException.
     *
     * @return the MeshBase that raised this TransactionException
     */
    public MeshBase getTransactable()
    {
        return theTransactable;
    }

    /**
     * Obtain the MeshBaseIdentifier of the MeshBase that raised this TransactionException.
     * 
     * @return the MeshBaseIdentifier of the MeshBase that raised this TransactionException
     */
    public MeshBaseIdentifier getTransactableIdentifier()
    {
        return theIdentifier;
    }

    /**
     * Obtain the Transaction that raised this TransactionException.
     *
     * @return the Transaction that raised this TransactionException
     */
    public Transaction getTransaction()
    {
        return theTransaction;
    }

    /**
     * Convert this into a string, for debugging.
     *
     * @return this instance as a string
     */
    @Override
    public String toString()
    {
        return super.toString() + " -- MB: " + theIdentifier + ", TX: " + theTransaction;
    }

    /**
     * The MeshBase the raised this TransactionException.
     */
    protected transient MeshBase theTransactable;

    /**
     * The identifier of the MeshBase that raised this TransactionException.
     */
    protected MeshBaseIdentifier theIdentifier;

    /**
     * The Transaction that raised this TransactionException.
     */
    protected transient Transaction theTransaction;
}
