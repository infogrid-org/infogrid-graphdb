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
  * This TransactionException is thrown if a (potentially) modifying operation
  * is invoked outside of Transaction boundaries.
  */
public class NotWithinTransactionBoundariesException
        extends
            TransactionException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param trans the MeshBase that was affected
     */
    public NotWithinTransactionBoundariesException(
            MeshBase trans )
    {
        super( trans, null );
    }
}
