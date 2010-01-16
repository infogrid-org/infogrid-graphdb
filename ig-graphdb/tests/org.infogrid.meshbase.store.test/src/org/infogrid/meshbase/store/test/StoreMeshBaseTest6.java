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

package org.infogrid.meshbase.store.test;

import java.util.Date;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.ColorValue;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.model.primitives.ExtentValue;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.IntegerValue;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PointValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.TimePeriodValue;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.util.logging.Log;

/**
 * Reproduces a StoreMeshBase integrity problem found 2007-06-22.
 */
public class StoreMeshBaseTest6
        extends
            AbstractStoreMeshBaseTest
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
        theSqlStore.initializeHard();

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "mb" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();
        long                     now  = System.currentTimeMillis();

        //
        
        log.info( "Creating test objects" );
        
        Transaction tx = mb.createTransactionNow();

        for( int i=0 ; i<testData.length ; ++i ) {
            DataSet test = testData[i];

            MeshObjectIdentifier identifier = mb.getMeshObjectIdentifierFactory().fromExternalForm( test.theIdentifier );
            MeshObject obj = life.createMeshObject( identifier, TestSubjectArea.PROPERTYTEST );

            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATABLOBDATATYPE,         test.theBlob );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATABOOLEANDATATYPE,      test.theBoolean );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATACOLORDATATYPE,        test.theColor );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE,   test.theEnumerated );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAEXTENTDATATYPE,       test.theExtent );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAFLOATDATATYPE,        test.theFloat );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAINTEGERDATATYPE,      test.theInteger );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAMULTIPLICITYDATATYPE, test.theMultiplicity );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAPOINTDATATYPE,        test.thePoint );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATASTRINGDATATYPE,       test.theString );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATATIMEPERIODDATATYPE,   test.theTimePeriod );
            obj.setPropertyValue( TestSubjectArea.PROPERTYTEST_WHATATIMESTAMPDATATYPE,    test.theTimeStamp );
        }
        
        tx.commitTransaction();

        //
        
        log.info( "collecting garbage" );
        
        tx  = null;
        
        collectGarbage();

        //
        
        log.info( "Recovering and comparing test objects" );
        
        for( int i=0 ; i<testData.length ; ++i ) {
            DataSet test = testData[i];

            MeshObjectIdentifier identifier = mb.getMeshObjectIdentifierFactory().fromExternalForm( test.theIdentifier );
            MeshObject obj = mb.accessLocally( identifier );

            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATABLOBDATATYPE ),         test.theBlob,         "Wrong Blob value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATABOOLEANDATATYPE ),      test.theBoolean,      "Wrong Boolean value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATACOLORDATATYPE ),        test.theColor,        "Wrong Color value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE ),   test.theEnumerated,   "Wrong Enumerated value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAEXTENTDATATYPE ),       test.theExtent,       "Wrong Extent value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAFLOATDATATYPE ),        test.theFloat,        "Wrong Float value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAINTEGERDATATYPE ),      test.theInteger,      "Wrong Integer value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAMULTIPLICITYDATATYPE ), test.theMultiplicity, "Wrong Multiplicity value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATAPOINTDATATYPE ),        test.thePoint,        "Wrong Point value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATASTRINGDATATYPE ),       test.theString,       "Wrong String value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATATIMEPERIODDATATYPE ),   test.theTimePeriod,   "Wrong TimePeriod value" );
            checkEquals( obj.getPropertyValue( TestSubjectArea.PROPERTYTEST_WHATATIMESTAMPDATATYPE ),    test.theTimeStamp,    "Wrong TimeStamp value" );
        
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
        StoreMeshBaseTest6 test = null;
        try {
            if( args.length > 0 ) {
                System.err.println( "Synopsis: <no argument>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new StoreMeshBaseTest6( args );
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
     * @throws Exception anything can go wrong in a test
     */
    public StoreMeshBaseTest6(
            String [] args )
        throws
            Exception
    {
        super( StoreMeshBaseTest6.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest6.class );
    
    /**
     * Test data.
     */
    protected static DataSet [] testData;
    static {
        // This way we can catch exceptions
        try {
            testData = new DataSet[] {
                new DataSet(
                        "1",
                        BlobDataType.theAnyType.createBlobValue( "Simple String Blog", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                        BooleanValue.create( true ),
                        ColorValue.create( 123 ),
                        TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE_type.select( "Value1" ),
                        ExtentValue.create( 12.34, 56.78 ),
                        FloatValue.create( 12.34 ),
                        IntegerValue.create( 123 ),
                        MultiplicityValue.create( 0, 17),
                        PointValue.create( 12.34, 56.78 ),
                        StringValue.create( "Simple String" ),
                        TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (short) 6 ),
                        TimeStampValue.create( 1L ) ),
                new DataSet(
                        "2",
                        BlobDataType.theAnyType.createBlobValue( "An <b>important&trade;</b>&#33; HTML String", BlobValue.TEXT_HTML_MIME_TYPE ),
                        BooleanValue.create( false ),
                        ColorValue.create( 255, 255, 255, 255 ),
                        TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE_type.select( "Value2" ),
                        ExtentValue.create( -12.34, 56.78 ),
                        FloatValue.create( -12.34 ),
                        IntegerValue.create( -123 ),
                        MultiplicityValue.create( 17, 19 ),
                        PointValue.create( -12.34, -56.78 ),
                        StringValue.create( "An <b>important&trade;</b>&#33; HTML String" ),
                        TimePeriodValue.create( (short) 2999, (short) 12, (short) 31, (short) 23, (short) 59, 59.999f ),
                        TimeStampValue.create( 1L ) ),
                new DataSet(
                        "3",
                        BlobDataType.theAnyType.createBlobValue( "An <foo:bar>XML</foo:bar> String <!CDATA[with a CDATA]]> section.", BlobValue.TEXT_HTML_MIME_TYPE ),
                        null,
                        ColorValue.create( 1378 ),
                        TestSubjectArea.PROPERTYTEST_WHATAENUMERATEDDATATYPE_type.select( "Value3" ),
                        ExtentValue.create( Double.MAX_VALUE, 0 ),
                        FloatValue.create( Double.MIN_VALUE ),
                        IntegerValue.create( Integer.MAX_VALUE ),
                        MultiplicityValue.ZERO_N,
                        PointValue.create( Double.MIN_VALUE, -Double.MAX_VALUE ),
                        StringValue.create( "An <foo:bar>XML</foo:bar> String <!CDATA[with a CDATA]]> section." ),
                        null,
                        TimeStampValue.create( new Date() ) ),
            };
        } catch( Throwable t ) {
            log.error( t );
        }
    }
    
    /**
     * Captures one test case.
     */
    protected static class DataSet
    {
        /**
         * Constructor.
         * 
         * @param identifier the identifier
         * @param tBlob the BlobValue
         * @param tBoolean the BooleanValue
         * @param tColor the ColorValue
         * @param tEnumerated the EnumeratedValue
         * @param tExtent the ExtentValue
         * @param tFloat the FloatValue
         * @param tInteger the IntegerValue
         * @param tMultiplicity the MultiplicityValue
         * @param tPoint the PointValue
         * @param tString the StringValue
         * @param tTimePeriod the TimePeriodValue
         * @param tTimeStamp the TimeStampValue
         */
        public DataSet(
                String            identifier,
                BlobValue         tBlob,
                BooleanValue      tBoolean,
                ColorValue        tColor,
                EnumeratedValue   tEnumerated,
                ExtentValue       tExtent,
                FloatValue        tFloat,
                IntegerValue      tInteger,
                MultiplicityValue tMultiplicity,
                PointValue        tPoint,
                StringValue       tString,
                TimePeriodValue   tTimePeriod,
                TimeStampValue    tTimeStamp )
        {
            theIdentifier = identifier;

            theBlob         = tBlob;
            theBoolean      = tBoolean;
            theColor        = tColor;
            theEnumerated   = tEnumerated;
            theExtent       = tExtent;
            theFloat        = tFloat;
            theInteger      = tInteger;
            theMultiplicity = tMultiplicity;
            thePoint        = tPoint;
            theString       = tString;
            theTimePeriod   = tTimePeriod;
            theTimeStamp    = tTimeStamp;
        }

        String   theIdentifier;
        
        BlobValue         theBlob;
        BooleanValue      theBoolean;
        ColorValue        theColor;
        EnumeratedValue   theEnumerated;
        ExtentValue       theExtent;
        FloatValue        theFloat;
        IntegerValue      theInteger;
        MultiplicityValue theMultiplicity;
        PointValue        thePoint;
        StringValue       theString;
        TimePeriodValue   theTimePeriod;
        TimeStampValue    theTimeStamp;
    }
}
