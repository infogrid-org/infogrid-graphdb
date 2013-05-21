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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.meshbase.store.test;

import org.infogrid.testharness.AbstractTestGroup;

/**
 * Tests the Store implementation of MeshBase.
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
        String subArg1 = "123";

        TestSpec [] tests = {
                new TestSpec( StoreMeshBaseTest1.class, subArg1 ),
                new TestSpec( StoreMeshBaseTest2.class, subArg1 ),
                new TestSpec( StoreMeshBaseTest3.class ),
                new TestSpec( StoreMeshBaseTest4.class, "10000" ),
                new TestSpec( StoreMeshBaseTest5.class ),
                new TestSpec( StoreMeshBaseTest6.class ),
                new TestSpec( StoreMeshBaseTest7.class ),
                new TestSpec( StoreMeshBaseTest8.class ),
                new TestSpec( StoreMeshBaseTest9.class ),

                new TestSpec( StoreBulkLoaderTest1.class ),

                new TestSpec( StoreSweeperTest1.class ),
                new TestSpec( StoreSweeperTest2.class ),

                new TestSpec( ModelChangeTest1.class ), // removed PropertyType

                new TestSpec( ModelChangeTest2.class ), // added mandatory PropertyType
                new TestSpec( ModelChangeTest3.class ), // PropertyType turned read-only
                new TestSpec( ModelChangeTest4.class ), // supertype added
                new TestSpec( ModelChangeTest5.class ), // supertype removed

                // FIXME: to be written
//                new TestSpec( ModelChangeTest6.class ), // mandatory RelationshipType added
//                new TestSpec( ModelChangeTest7.class ), // RelationshipType removed
//                new TestSpec( ModelChangeTest8.class ), // moved RelationshipType to other destination
//                new TestSpec( ModelChangeTest9.class ), // EntityType became abstract
//                new TestSpec( ModelChangeTest10.class ), // RelationshipType became abstract
//                new TestSpec( ModelChangeTest11.class ), // RelationshipType had smaller maximum multiplicity
        };

        runTests( tests );
    }
}
