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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.transaction;

/**
  * This interface is implemented by objects that wish to listen to Transaction-related events.
  */
public interface TransactionListener
{
    /**
      * Indicates that a Transaction has been started.
      *
      * @param tx the Transaction that was started
      */
    public abstract void transactionStarted(
            Transaction tx );

    /**
      * Indicates that a Transaction has been committed.
      *
      * @param tx the Transaction that was committed
      */
    public abstract void transactionCommitted(
            Transaction tx );

    /**
     * Indicates that a Transaction has been rolled back.
     *
     * @param tx the Transaction that was rolled back
     */
    public abstract void transactionRolledback(
            Transaction tx );
};
