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

import org.infogrid.util.logging.Dumper;

/**
 * <p>We abuse the Exception class slightly to have a convenient method of
 *    storing the place and time where a Transaction was created. This is
 *    often invaluable during debugging.</p>
 * <p>Please do not throw this Exception.</p>
 */
public class TransactionConstructionMarker
        extends
            Exception
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     */
    TransactionConstructionMarker()
    {
        now = System.currentTimeMillis();
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
                    "now",
                    "stacktrace"
                },
                new Object[] {
                    now,
                    getStackTrace()
                } );
    }

    /**
     * The time at which the Transaction was created.
     */
    protected long now;
}
