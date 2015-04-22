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

package org.infogrid.kernel.test.modelbase;

import org.infogrid.testharness.AbstractTestGroup;
import org.infogrid.util.logging.Log;

/**
 * Runs the tests in this package.
 */
public abstract class AllTests
        extends
            AbstractTestGroup
{
    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        String subArg = "org.infogrid.model.Test";
        
        TestSpec [] tests = {
                new TestSpec( ModelBaseTest1.class, subArg ),
                new TestSpec( ModelBaseTest2.class, subArg ),
                new TestSpec( ModelBaseTest3.class, subArg )
        };
        // do not run ModelBaseTest4 -- it's not suitable for automatic testing

        runTests( tests );

    }

    // Our Logger
    private static Log log = Log.getLogInstance( AllTests.class );
}

