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
  * This TransactionException is thrown to indicate that a Transaction is
  * already active and thus the asked-for new Transaction cannot be created.
  */
public class TransactionActiveAlreadyException
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
    public TransactionActiveAlreadyException(
            MeshBase    trans,
            Transaction blockingTransaction )
    {
        super( trans, blockingTransaction );
    }
}
