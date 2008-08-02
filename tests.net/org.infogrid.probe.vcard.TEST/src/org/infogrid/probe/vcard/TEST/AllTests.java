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

package org.infogrid.probe.vcard.TEST;

import org.infogrid.testharness.AbstractTest;
import org.infogrid.testharness.AbstractTestGroup;
import org.infogrid.testharness.AbstractTestGroup.TestSpec;

/**
 * Tests the VCard Probe.
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
        String [] noArgs = {};
        
        TestSpec [] tests = {

                new TestSpec(
                        VCardProbeTest1.class,
                        new String [] {
                                AbstractTest.fileSystemFile( VCardProbeTest1.class, "VCardProbeTest1.vcf" )
                        } )
        };

        runTests( tests );
    }
}