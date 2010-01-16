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

package org.infogrid.kernel.active.test;

import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest1;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest2;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest3;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest4;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest5;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest6;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest7;
import org.infogrid.kernel.active.test.objectset.ActiveMeshObjectSetTest8;
import org.infogrid.kernel.active.test.traversalpathset.ActiveTraversalPathSetTest1;
import org.infogrid.kernel.active.test.traversalpathset.ActiveTraversalPathSetTest2;

import org.infogrid.testharness.AbstractTestGroup;

/**
 * Runs all ActiveMeshObjectSetTests and ActiveTraversalPathSetTests.
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
                new TestSpec( ActiveMeshObjectSetTest1.class ),
                new TestSpec( ActiveMeshObjectSetTest2.class ),
                new TestSpec( ActiveMeshObjectSetTest3.class ),
                new TestSpec( ActiveMeshObjectSetTest4.class ),
                new TestSpec( ActiveMeshObjectSetTest5.class ),
                new TestSpec( ActiveMeshObjectSetTest6.class ),
                // new TestSpec( ActiveMeshObjectSetTest7.class ), // FIXME, transitive closure is broken
                new TestSpec( ActiveMeshObjectSetTest8.class ),

                new TestSpec( ActiveTraversalPathSetTest1.class ),
                new TestSpec( ActiveTraversalPathSetTest2.class )
        };

        runTests( tests );
    }
}

