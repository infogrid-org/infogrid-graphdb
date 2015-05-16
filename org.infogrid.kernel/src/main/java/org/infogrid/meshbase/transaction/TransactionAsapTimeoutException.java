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

package org.infogrid.meshbase.transaction;

import org.infogrid.meshbase.MeshBase;

/**
  * <p>This TransactionException is thrown to indicate that the current Thread reached a time out for
  * trying to create a Transaction "asap". While there may be genuine congestion in an application,
  * the most likely cause of this Exception is that the developer forgot to complete a previously
  * opened Transaction, in which case the next Transaction cannot be started.</p>
  * <p>For a good pattern on how to manage Transactions, please refer to the documentation
  * of {@link MeshBase}.</p>  
  */
public class TransactionAsapTimeoutException
        extends
            TransactionException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param trans the MeshBase that was affected
     * @param blockingTransaction the Transaction that blocked the new Transaction
     */
    public TransactionAsapTimeoutException(
            MeshBase    trans,
            Transaction blockingTransaction )
    {
        super( trans, blockingTransaction );
    }
}
