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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.testharness.AbstractTestGroup;

/**
 * Runs all tests in this package.
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
        TestSpec [] tests = {
                new TestSpec(  EquivalenceSetComparatorTest1.class ),

                new TestSpec(  MeshBaseTest1.class ),
                new TestSpec(  MeshBaseTest2.class ),
                new TestSpec(  MeshBaseTest3.class ),
                new TestSpec(  MeshBaseTest4.class ),
                new TestSpec(  MeshBaseTest5.class ),
                new TestSpec(  MeshBaseTest6.class ),
                new TestSpec(  MeshBaseTest7.class ),
                new TestSpec(  MeshBaseTest8.class ),
                new TestSpec(  MeshBaseTest9.class ),
                new TestSpec( MeshBaseTest10.class ),
                new TestSpec( MeshBaseTest11.class ),
                new TestSpec( MeshBaseTest12.class ),
                new TestSpec( MeshBaseTest13.class ),
                new TestSpec( MeshBaseTest14.class ),
                new TestSpec( MeshBaseTest15.class ),
                new TestSpec( MeshBaseTest16.class ),
                new TestSpec( MeshBaseTest17.class ),
                new TestSpec( MeshBaseTest18.class ),

                new TestSpec( GarbageCollectionTest1.class ),

                new TestSpec( SweeperTest1.class ),
                new TestSpec( SweeperTest2.class )
        };

        runTests( tests );
    }
}

