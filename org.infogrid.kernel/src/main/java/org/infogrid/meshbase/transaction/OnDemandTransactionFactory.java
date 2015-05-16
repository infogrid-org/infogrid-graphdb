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
import org.infogrid.util.AbstractFactory;
import org.infogrid.util.FactoryException;

/**
 * Simple factory for the OnDemandTransaction.
 */
public class OnDemandTransactionFactory
        extends
            AbstractFactory<MeshBase,OnDemandTransaction,Void>
{
    /**
     * Factory method.
     *
     * @param key the key information required for object creation, if any
     * @param argument any argument-style information required for object creation, if any
     * @return the created object
     * @throws FactoryException catch-all Exception, consider its cause
     */
    public OnDemandTransaction obtainFor(
            MeshBase key,
            Void     argument )
        throws
            FactoryException
    {
        return new OnDemandTransaction( key );
    }
}
