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

package org.infogrid.kernel.test.traversal;

import org.infogrid.mesh.set.ByTypeMeshObjectSelector;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.traversal.AllNeighborsTraversalSpecification;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalTranslator;
import org.infogrid.model.traversal.xpath.XpathTraversalTranslator;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * Tests externalization and reconstructinon of TraversalSpecifications.
 */
public class TraversalTest1
        extends
            AbstractTraversalTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        // get some interesting data
        TraversalSpecification [] roleTypesTestData = new TraversalSpecification[] {
                TestSubjectArea.AR1A.getSource(), // 0
                TestSubjectArea.AR1A.getDestination(), // 1
                AllNeighborsTraversalSpecification.create() // 2
        };
        TraversalSpecification [] stayRightHereTestData = new TraversalSpecification[] {
                StayRightHereTraversalSpecification.create() // 3
        };
        TraversalSpecification [] sequentialTestData = new TraversalSpecification[] {
                SequentialCompoundTraversalSpecification.create( // 4
                        TestSubjectArea.AR1A.getSource(),
                        TestSubjectArea.AR1A.getDestination()),
                SequentialCompoundTraversalSpecification.create( // 5
                        TestSubjectArea.AR1A.getSource(),
                        AllNeighborsTraversalSpecification.create(),
                        TestSubjectArea.AR1A.getDestination()),
                SequentialCompoundTraversalSpecification.create( // 6
                        TestSubjectArea.AR1A.getSource(),
                        SequentialCompoundTraversalSpecification.create(
                                TestSubjectArea.AR1A.getSource(),
                                TestSubjectArea.AR1A.getDestination()),
                        TestSubjectArea.AR1A.getDestination())
        };
        TraversalSpecification [] alternativeTestData = new TraversalSpecification[] {
                AlternativeCompoundTraversalSpecification.create(
                        TestSubjectArea.AR1A.getSource(),
                        TestSubjectArea.AR1A.getDestination()),
                AlternativeCompoundTraversalSpecification.create(
                        TestSubjectArea.AR1A.getSource(),
                        SequentialCompoundTraversalSpecification.create(
                                TestSubjectArea.AR1A.getSource(),
                                TestSubjectArea.AR1A.getDestination()),
                        TestSubjectArea.AR1A.getDestination())
        };
        TraversalSpecification [] selectiveTestData = new TraversalSpecification[] {
                SelectiveTraversalSpecification.create(
                        TestSubjectArea.AR1A.getSource(),
                        ByTypeMeshObjectSelector.create( TestSubjectArea.A )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.AA_Y, StringValue.create( "abc" ), PropertyComparisonOperator.GREATER )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.B_Z, TestSubjectArea.B_Z_type_VALUE1, PropertyComparisonOperator.SMALLER )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.AA_Y, IntegerValue.create( 5 ), PropertyComparisonOperator.EQUAL )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.AA_Y, FloatValue.create( -5.1 ), PropertyComparisonOperator.NON_EQUAL )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.AA_Y, CurrencyValue.create( 2, 4, "USD" ), PropertyComparisonOperator.GREATER_OR_EQUALS )),
//                SelectiveTraversalSpecification.create(
//                        TestSubjectArea.AR1A.getSource(),
//                        ByPropertyValueSelector.create( TestSubjectArea.AA_Y, FloatValue.create( 5.0 ), PropertyComparisonOperator.SMALLER_OR_EQUALS )),
        };

        //

        TraversalSpecification [] testData = ArrayHelper.append(
                new TraversalSpecification[][] {
                        roleTypesTestData,
                        stayRightHereTestData,
                        sequentialTestData,
                        alternativeTestData,
                        selectiveTestData
                },
                TraversalSpecification.class );

        TraversalTranslator translator = XpathTraversalTranslator.create( theMeshBase );

        for( int i=0 ; i<testData.length ; ++i ) {
            log.info( "Looking at: " + i );

            String [] encoded = translator.translateTraversalSpecification( null, testData[i] );

            log.debug( "Externalized: ", encoded );

            TraversalSpecification restored = translator.translateTraversalSpecification( null, encoded );

            checkEquals( testData[i], restored, "Failed to restore" );
        }
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        TraversalTest1 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new TraversalTest1( args );
            test.run();

        } catch( Throwable ex ) {
            log.error( ex );
            System.exit(1);
        }
        if( test != null ) {
            test.cleanup();
        }
        if( errorCount == 0 ) {
            log.info( "PASS" );
        } else {
            log.info( "FAIL (" + errorCount + " errors)" );
        }
        System.exit( errorCount );
    }

    /**
     * Constructor.
     *
     * @param args command-line arguments
     * @throws Exception all sorts of things may go wrong in a test
     */
    public TraversalTest1(
            String [] args )
        throws
            Exception
    {
        super( TraversalTest1.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( TraversalTest1.class );}
