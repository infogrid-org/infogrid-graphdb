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

package org.infogrid.meshbase.transaction;

import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.CreateWhenNeeded;

/**
 * Utility that makes it easy to only start a transaction if it turns out one is ndded.
 */
public class OnDemandTransaction
        extends
            CreateWhenNeeded<Transaction>
{
    /**
     * Constructor.
     *
     * @param mb the MeshBase on which the Transaction is created
     */
    public OnDemandTransaction(
            MeshBase mb )
    {
        theMeshBase = mb;
    }

    /**
     * Instantiation method.
     *
     * @return the instantiated object
     * @throws TransactionException a problem occurred creating the Transaction
     */
    protected Transaction instantiate()
        throws
            TransactionException
    {
        Transaction ret = theMeshBase.createTransactionAsap();
        return ret;
    }

    /**
     * The MeshBase for which the Transaction is created.
     */
    protected MeshBase theMeshBase;
}
