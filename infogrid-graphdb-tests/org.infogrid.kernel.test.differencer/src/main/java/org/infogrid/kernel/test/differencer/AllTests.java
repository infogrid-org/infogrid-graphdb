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

package org.infogrid.kernel.test.differencer;

import org.infogrid.testharness.AbstractTestGroup;

/**
 * Runs all tests in this package.
 */
public class AllTests
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
        TestSpec [] tests = {
                new TestSpec( DifferencerTest1.class ),
                new TestSpec( DifferencerTest2.class ),
                new TestSpec( DifferencerTest3.class ),
                new TestSpec( DifferencerTest4.class ),
                new TestSpec( DifferencerTest5.class ),
                new TestSpec( DifferencerTest6.class ),
                new TestSpec( DifferencerTest7.class ),
                new TestSpec( DifferencerTest8.class ),
                new TestSpec( DifferencerTest9.class ),
        };

        runTests( tests );
    }
}

