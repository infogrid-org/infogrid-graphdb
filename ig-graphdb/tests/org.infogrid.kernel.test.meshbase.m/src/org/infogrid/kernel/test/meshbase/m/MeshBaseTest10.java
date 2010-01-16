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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.IllegalPropertyValueException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.ColorValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.ExtentValue;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.IntegerValue;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PointValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.TimePeriodValue;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.util.logging.Log;

/**
 * Tests that we can't assign the wrong PropertyValue subtypes.
 */
public class MeshBaseTest10
        extends
            AbstractMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public void run()
        throws
            Exception
    {
        MeshBase theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                rootContext );

        MeshObject home = theMeshBase.getHomeObject();
        Transaction tx = theMeshBase.createTransactionNow();
        home.bless( TestSubjectArea.PROPERTYTEST );
        tx.commitTransaction();

        checkEqualsOutOfSequence( home.getTypes(), new EntityType[] { TestSubjectArea.PROPERTYTEST }, "wrong types on home object" );
        
        //
        
        PropertyValue [] testValues = {
                TestSubjectArea.PROPERTYTEST_WHATABLOBDATATYPE_type.createBlobValue( "test", "text/plain" ),
                BooleanValue.TRUE,
                ColorValue.create( 0x202020 ),
                TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE_type.select( "Value3" ),
                ExtentValue.create( 1.2, 3.4 ),
                FloatValue.create( 56.78 ),
                IntegerValue.create( 99 ),
                MultiplicityValue.create( 2, 7 ),
                PointValue.create( 9.8, 7.6 ),
                StringValue.create( "some string" ),
                TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short)  4, (short)  5, (float)  6. ),
                TimeStampValue.create(  (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (float) 12. )
        };
        PropertyType [] testProperties = {
                TestSubjectArea.PROPERTYTEST_WHATABLOBDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATABOOLEANDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATACOLORDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAEXTENTDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAFLOATDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAINTEGERDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAMULTIPLICITYDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATAPOINTDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATASTRINGDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATATIMEPERIODDATATYPE,
                TestSubjectArea.PROPERTYTEST_WHATATIMESTAMPDATATYPE
        };
        checkEquals( testValues.length, testProperties.length, "inconsistency in test data" );
        
        tx = theMeshBase.createTransactionNow();
        
        for( int i=0 ; i<testValues.length ; ++i ) {
            log.info( "Now running with offset " + i );
            for( int j=0 ; j<testValues.length ; ++j ) {
                PropertyValue currentValue = testValues[ (j+i) % testValues.length ];
                log.debug( "Looking at test value " + j + ": " + currentValue );
                if( i == 0 ) {
                    // should not throw an exception
                    home.setPropertyValue( testProperties[j], currentValue );
                } else {
                    // should throw an exception
                    try {
                        home.setPropertyValue( testProperties[j], currentValue );
                        reportError( "attempting to set unexpectedly succeeded", testProperties[i].getIdentifier(), currentValue );
                    } catch( IllegalPropertyValueException ex ) {
                        // do nothing
                        log.debug( "Good, that did not work" );
                    }
                }
                checkEquals( home.getPropertyValue( testProperties[i] ), testValues[i], "wrong value found" );
            }
        }
        tx.commitTransaction();
        
        //
        
        theMeshBase.die();
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        MeshBaseTest10 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest10( args );
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
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public MeshBaseTest10(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest10.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest10.class );
}
