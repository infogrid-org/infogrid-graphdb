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

package org.infogrid.kernel.test.mesh.externalized;

import com.sun.org.apache.xerces.internal.util.XMLChar;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanDataType;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.ColorDataType;
import org.infogrid.model.primitives.ColorValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.model.primitives.ExtentDataType;
import org.infogrid.model.primitives.ExtentValue;
import org.infogrid.model.primitives.FloatDataType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.IntegerDataType;
import org.infogrid.model.primitives.IntegerValue;
import org.infogrid.model.primitives.L10Map;
import org.infogrid.model.primitives.MultiplicityDataType;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PointDataType;
import org.infogrid.model.primitives.PointValue;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringDataType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.TimePeriodDataType;
import org.infogrid.model.primitives.TimePeriodValue;
import org.infogrid.model.primitives.TimeStampDataType;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.primitives.externalized.xml.PropertyValueXmlEncoder;
import org.infogrid.util.logging.Log;

/**
 * Tests PropertyValue serialization and deserialization.
 *
 * NOTE: ALL testS WITH UNIT IN THEM ARE COMMENTED OUT AS THE SERIALIZER
 * CURRENTLY DOES NOT SUPPORT UNITS!!!
 */
public class SerializerTest1
        extends
            AbstractSerializerTest
{
    /**
     * Run the test.
     * 
     * @throws Exception all sorts of things may go wrong in a test
     */
    public void run()
            throws
                Exception
    {
        for( int i=0 ; i<testData.length ; ++i ) {
            for( int j=0 ; j<testData[i].length ; ++j ) {
                log.info( "Testing " + testData[i][j].theType.getClass().getName() );

                for( int k=0 ; k<testData[i][j].theValues.length ; ++k ) {

                    PropertyValue original = testData[i][j].theValues[k];
                    PropertyValue decoded  = null;

                    byte [] encoded = null;
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        theSerializer.encodePropertyValue( original, out );
                        out.close();
                        encoded = out.toByteArray();

                        log.info( "value: \"" + original + "\", serialized: \"" + encoded + "\"" );

                        ByteArrayInputStream in = new ByteArrayInputStream( encoded );
                        
                        decoded = theSerializer.decodePropertyValue( in ); // , testData[i][j].theType, getClass().getClassLoader() );
                        if( ( testData[i][j] instanceof EnumeratedTestCase ) && ( original instanceof EnumeratedValue ) && ( decoded instanceof EnumeratedValue )) {
                            EnumeratedValue realDecoded = (EnumeratedValue) decoded;
                            decoded = ((EnumeratedDataType) testData[i][j].theType).select( realDecoded.value() );
                        }

                        checkEquals( original, decoded, "incorrect deserialization at index " + i + "/" + j + "/" + k );

                    } catch( Throwable ex ) {
                        ++errorCount;
                        reportError( "element", j, original, encoded, ( (encoded == null) ? "encoding" : "decoding" ), ex );
                        checkEquals( original, decoded, "what we received" );
                    }
                }
            }
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
        SerializerTest1 test = null;
        try {
            if( args.length > 0 ) {
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new SerializerTest1( args );
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
    public SerializerTest1(
            String [] args )
        throws
            Exception
    {
        super( SerializerTest1.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( SerializerTest1.class );

   /**
     * The test cases for BooleanValue
     */
    protected static TestCase [] booleanValueTestData = new TestCase [] {
            new TestCase(
                    BooleanDataType.theDefault,
                    new BooleanValue[] {
                         BooleanValue.FALSE,
                         BooleanValue.TRUE
                    } )
    };

   /**
     * The test cases for ColorValue
     */
    protected static TestCase [] colorValueTestData = new TestCase[] {
            new TestCase(
                    ColorDataType.theDefault,
                    new ColorValue[] {
                         ColorValue.create(  0 ),
                         ColorValue.create(  1 ),
                         ColorValue.create( -1 ),
                         ColorValue.create(  255 ),
                         ColorValue.create( -255 ),
                         ColorValue.create(  256 ),
                         ColorValue.create( -256 ),
                         ColorValue.create(  Integer.MAX_VALUE ),
                         ColorValue.create( -Integer.MAX_VALUE )
                    } )
    };

   /**
     * The test cases for EnumeratedValue
     */
    protected static TestCase [] enumeratedValueTestData = new TestCase[] {
//            new TestCase(
//                    EnumeratedDataType.theDefault,
//                    new EnumeratedValue[] {
//                        EnumeratedValue.create( null, "a", null, null ),
//                        EnumeratedValue.create( null, "abcdefgh ijklmnopqrstuv wxyz ABCDEF GHIJKLM NOPQRST UVWXYZ 1234 5678 9 0", null, null )                
//                    } )
// Took out the testing of the DataType pointer from the EnumeratedValue. FIXME?
            new EnumeratedTestCase(
                    EnumeratedDataType.create(
                            new String[] {
                                    "a",
                                    "abcdefgh ijklmnopqrstuv wxyz ABCDEF GHIJKLM NOPQRST UVWXYZ 1234 5678 9 0" },
                            new L10Map[] {
                                    null,
                                    null },
                            null,
                            EnumeratedDataType.theDefault )
            )
    };

   /**
     * The test cases for ExtentValue
     */
    protected static TestCase [] extentValueTestData = new TestCase[] {
            new TestCase(
                    ExtentDataType.theDefault,
                    new ExtentValue[] {
                         ExtentValue.create(     0.,    0. ),
                         ExtentValue.create(     1.,    0. ),
                         ExtentValue.create(     0.,    1. ),
                         ExtentValue.create(     1.,    1. ),
                         ExtentValue.create(     1.,    0. ),
                         ExtentValue.create(     0.,    1. ),
                         ExtentValue.create(     1.,    1. ),
                         ExtentValue.create(     1.,    1. ),
                         ExtentValue.create(     1.,    1. ),
                         ExtentValue.create(     0., 1000. ),
                         ExtentValue.create(  2000., 3000. ),
                         ExtentValue.create(  4000., 5000. ),
                         ExtentValue.create(  6000., 7000. ),
                         ExtentValue.create(  0.              , Double.MIN_VALUE ),
                         ExtentValue.create(  Double.MIN_VALUE, 0. ),
                         ExtentValue.create(  Double.MIN_VALUE, Double.MIN_VALUE ),
                         ExtentValue.create(  0.              , Double.MIN_VALUE ),
                         ExtentValue.create(  Double.MIN_VALUE, 0. ),
                         ExtentValue.create(  Double.MIN_VALUE, Double.MIN_VALUE ),
                         ExtentValue.create(  Double.MIN_VALUE, Double.MIN_VALUE ),
                         ExtentValue.create(  Double.MIN_VALUE, Double.MIN_VALUE ),
                         ExtentValue.create(  0.              , Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MAX_VALUE, 0. ),
                         ExtentValue.create(  Double.MAX_VALUE, Double.MAX_VALUE ),
                         ExtentValue.create(  0.              , Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MAX_VALUE, 0. ),
                         ExtentValue.create(  Double.MAX_VALUE, Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MAX_VALUE, Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MAX_VALUE, Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MIN_VALUE, Double.MAX_VALUE ),
                         ExtentValue.create(  Double.MAX_VALUE, Double.MIN_VALUE )
                    } )
    };

    /**
     * The test cases for FloatValue
     */
    protected static TestCase [] floatValueTestData = new TestCase[] {
            new TestCase(
                    FloatDataType.theDefault,
                    new FloatValue[] {
                         FloatValue.create(   0. ),
                         FloatValue.create(   1. ),
                         FloatValue.create(  -1. ),
                         FloatValue.create(  10. ),
                         FloatValue.create( -10. ),
                         FloatValue.create(  Float.MAX_VALUE ),
                         FloatValue.create( -Float.MAX_VALUE ),
                         FloatValue.create(  Float.MIN_VALUE ),
                         FloatValue.create( -Float.MIN_VALUE ),
                         FloatValue.create(   1./Float.MAX_VALUE ),
                         FloatValue.create(  -1./Float.MAX_VALUE ),
                         FloatValue.create(   1./Float.MIN_VALUE ),
                         FloatValue.create(  -1./Float.MIN_VALUE )
                         // FIXME FloatValue.create(   0., Unit.theMeterUnit ),
                         // FIXME FloatValue.create(   1., Unit.theMileUnit ),
                         // FIXME FloatValue.create(  -1., Unit.theGramUnit ),
                         // FIXME FloatValue.create(  10., Unit.theMicroampereUnit ),
                         // FIXME FloatValue.create( -10., Unit.theGigabyteUnit ),
                    } ),
        new TestCase(
                FloatDataType.thePositiveDefault,
                new FloatValue[] {
                     FloatValue.create(   0. ),
                     FloatValue.create(   1. ),
                     FloatValue.create(  10. ),
                     FloatValue.create(  Float.MAX_VALUE ),
                     FloatValue.create(   1./Float.MAX_VALUE ),
                     // FIXME FloatValue.create(   0., Unit.theMeterUnit ),
                     // FIXME FloatValue.create(   1., Unit.theMileUnit ),
                     // FIXME FloatValue.create(  10., Unit.theMicroampereUnit ),
                } )
    };

    /**
     * The test cases for IntegerValues
     */
    protected static TestCase [] integerValueTestData = new TestCase [] {
            new TestCase(
                    IntegerDataType.theDefault,
                    new IntegerValue[] {
                         IntegerValue.create( 0 ),
                         IntegerValue.create( 1 ),
                         IntegerValue.create( 10 ),
                         IntegerValue.create( Integer.MAX_VALUE ),
                         IntegerValue.create( -1 ),
                         IntegerValue.create( -10 ),
                         IntegerValue.create( Integer.MIN_VALUE )
                    } )
    };

   /**
     * The test cases for MultiplicityValue
     */
    protected static TestCase [] multiplicityValueTestData = new TestCase[] {
            new TestCase(
                    MultiplicityDataType.theDefault,
                    new MultiplicityValue[] {
                         MultiplicityValue.create( 0, 1 ),
                         MultiplicityValue.create( 0, 2 ),
                         MultiplicityValue.create( 0, 10 ),
                         MultiplicityValue.create( 0, MultiplicityValue.N ),
                         MultiplicityValue.create( 1, 1 ),
                         MultiplicityValue.create( 1, 2 ),
                         MultiplicityValue.create( 1, 10 ),
                         MultiplicityValue.create( 2, MultiplicityValue.N ),
                         MultiplicityValue.create( 2, 2 ),
                         MultiplicityValue.create( 2, 10 ),
                         MultiplicityValue.create( 2, MultiplicityValue.N ),
                         MultiplicityValue.create( 10, 10 ),
                         MultiplicityValue.create( 10, MultiplicityValue.N )
                    } )
    };

   /**
     * The test cases for PointValue
     */
    protected static TestCase [] pointValueTestData = new TestCase[] {
            new TestCase(
                    PointDataType.theDefault,
                    new PointValue[] {
                         PointValue.create(  0.   ,  0. ),
                         PointValue.create(  1.   ,  0. ),
                         PointValue.create(  0.   ,  1. ),
                         PointValue.create(  1.   ,  1. ),
                         PointValue.create( -1.   ,  0. ),
                         PointValue.create(  0.   , -1. ),
                         PointValue.create( -1.   , -1. ),
                         PointValue.create( -1.   ,  1. ),
                         PointValue.create(  1.   , -1. ),
                         PointValue.create(     0., -1000. ),
                         PointValue.create( -2000., -3000. ),
                         PointValue.create( -4000.,  5000. ),
                         PointValue.create(  6000., -7000. ),
                         PointValue.create(  0.              ,  Double.MIN_VALUE ),
                         PointValue.create(  Double.MIN_VALUE,  0. ),
                         PointValue.create(  Double.MIN_VALUE,  Double.MIN_VALUE ),
                         PointValue.create(  0.              , -Double.MIN_VALUE ),
                         PointValue.create( -Double.MIN_VALUE,  0. ),
                         PointValue.create( -Double.MIN_VALUE, -Double.MIN_VALUE ),
                         PointValue.create(  Double.MIN_VALUE, -Double.MIN_VALUE ),
                         PointValue.create( -Double.MIN_VALUE,  Double.MIN_VALUE ),
                         PointValue.create(  0.              ,  Double.MAX_VALUE ),
                         PointValue.create(  Double.MAX_VALUE,  0. ),
                         PointValue.create(  Double.MAX_VALUE,  Double.MAX_VALUE ),
                         PointValue.create(  0.              , -Double.MAX_VALUE ),
                         PointValue.create( -Double.MAX_VALUE,  0. ),
                         PointValue.create( -Double.MAX_VALUE, -Double.MAX_VALUE ),
                         PointValue.create(  Double.MAX_VALUE, -Double.MAX_VALUE ),
                         PointValue.create( -Double.MAX_VALUE,  Double.MAX_VALUE ),
                         PointValue.create(  Double.MIN_VALUE,  Double.MAX_VALUE ),
                         PointValue.create(  Double.MAX_VALUE,  Double.MIN_VALUE )
                    } )
    };

    /**
     * The test cases for StringValue
     */
    protected static StringBuilder testStringBuilder = new StringBuilder( 255 );
    static {
        for( char c = '\u0001' ; c < '\uffff' ; ++c ) {
            if( XMLChar.isValid( c )) {
                testStringBuilder.append( c ); // tests the whole nine yards ...
            }
        }
    }
    protected static String testString = new String( testStringBuilder );
    protected static TestCase [] stringValueTestData = new TestCase [] {
            new TestCase(
                    StringDataType.theDefault,
                    new StringValue[] {
                         StringValue.create( "a" ),
                         StringValue.create( "" ),
                         StringValue.create( "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ" ),
                         StringValue.create( "ab<def>ghi" ),
                         StringValue.create( "a\\b" ),
                         StringValue.create( "a\tb" ),
                         StringValue.create( "a\nb" ),
                         StringValue.create( testString ),
                         StringValue.create( testString + testString + testString + testString )
                    } )
    };

    /**
     * The test cases for BlobValue
     */
    protected static byte [] testBytesShort = new byte[ 17 ]; // one more than 16
    protected static byte [] testBytesLong = new byte[ 1 << 17 ];
    static {
        for( int i=0 ; i<testBytesShort.length ; ++i ) {
            testBytesShort[i] = (byte) ( i % 256 );
        }
        for( int i=0 ; i<testBytesLong.length ; ++i ) {
            testBytesLong[i] = (byte) ( i % 256 );
        }
    }

    protected static TestCase [] blobValueTestData = new TestCase[] {
            new TestCase(
                    BlobDataType.theAnyType,
                    new BlobValue[] {
                         BlobDataType.theAnyType.createBlobValue( "a", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( "", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( testString, BlobValue.TEXT_PLAIN_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( testString + testString + testString + testString, BlobValue.TEXT_PLAIN_MIME_TYPE ),

                         BlobDataType.theAnyType.createBlobValue( "a", BlobValue.TEXT_HTML_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( "", BlobValue.TEXT_HTML_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", BlobValue.TEXT_HTML_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( testString, BlobValue.TEXT_HTML_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( testString + testString + testString + testString, BlobValue.TEXT_HTML_MIME_TYPE ),

                         BlobDataType.theAnyType.createBlobValue( testBytesShort, BlobValue.OCTET_STREAM_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( testBytesLong, BlobValue.OCTET_STREAM_MIME_TYPE ),

                         BlobDataType.theAnyType.createBlobValue( new byte[] { (byte) 1 }, BlobValue.OCTET_STREAM_MIME_TYPE ),
                         BlobDataType.theAnyType.createBlobValue( new byte[] { (byte) 11 }, BlobValue.OCTET_STREAM_MIME_TYPE )
                    } ),
        new TestCase(
                BlobDataType.theTextAnyType,
                new BlobValue[] {
                     BlobDataType.theTextAnyType.createBlobValue( "a", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", BlobValue.TEXT_PLAIN_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( testString, BlobValue.TEXT_PLAIN_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( testString + testString + testString + testString, BlobValue.TEXT_PLAIN_MIME_TYPE ),

                     BlobDataType.theTextAnyType.createBlobValue( "a", BlobValue.TEXT_HTML_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ", BlobValue.TEXT_HTML_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( testString, BlobValue.TEXT_HTML_MIME_TYPE ),
                     BlobDataType.theTextAnyType.createBlobValue( testString + testString + testString + testString, BlobValue.TEXT_HTML_MIME_TYPE ),
                } ),
        new TestCase(
                BlobDataType.theTextHtmlType,
                new BlobValue[] {
                     BlobDataType.theTextHtmlType.createBlobValue( testString, "text/html" ),
                } ),
    };

    /**
     * The test cases for TimeStampValue.
     */
    protected static TestCase [] timeStampValueTestData = new TestCase[] {
            new TestCase(
                    TimeStampDataType.theDefault,
                    new TimeStampValue[] {
                         TimeStampValue.create( (short) 1900, (short)  1, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 1900, (short)  1, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 1900, (short)  1, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 1900, (short)  1, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 1900, (short)  1, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 1900, (short)  1, (short) 31, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 1900, (short) 12, (short) 31, (short) 23, (short) 59, 59.99876789f ),

                         TimeStampValue.create( (short) 2345, (short)  1, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 2345, (short)  1, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 2345, (short)  1, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 2345, (short)  1, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 2345, (short)  1, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 2345, (short)  1, (short) 31, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimeStampValue.create( (short) 2345, (short) 12, (short) 31, (short) 23, (short) 59, 59.99876789f )
            } )
    };

    /**
     * The test cases for TimeStampValue.
     */
    protected static TestCase [] timePeriodValueTestData = new TestCase[] {
            new TestCase(
                    TimePeriodDataType.theDefault,
                    new TimePeriodValue[] {
                         TimePeriodValue.create( (short) 1900, (short)  1, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 1900, (short)  1, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 1900, (short)  1, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 1900, (short)  1, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 1900, (short)  1, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 1900, (short)  1, (short) 31, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 1900, (short) 12, (short) 31, (short) 23, (short) 59, 59.99876789f ),

                         TimePeriodValue.create( (short) 2345, (short)  1, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 2345, (short)  1, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 2345, (short)  1, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 2345, (short)  1, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 2345, (short)  1, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 2345, (short)  1, (short) 31, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short)  1, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short)  1, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short)  1, (short) 23, (short) 59, 59.99876789f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short) 31, (short)  0, (short)  0, 0.f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short) 31, (short)  1, (short)  2, 3.4f ),
                         TimePeriodValue.create( (short) 2345, (short) 12, (short) 31, (short) 23, (short) 59, 59.99876789f )
            } )
    };
    
    /**
      * all test cases put together
      */
    protected static TestCase [] [] testData = {
            blobValueTestData,
            booleanValueTestData,
            colorValueTestData,
            enumeratedValueTestData,
            extentValueTestData,
            floatValueTestData,
            integerValueTestData,
            multiplicityValueTestData,
            pointValueTestData,
            stringValueTestData,
            timeStampValueTestData,
            timePeriodValueTestData
    };

    /**
     * The serializer that we are testing
     */
    protected PropertyValueXmlEncoder theSerializer = new PropertyValueXmlEncoder();

    /**
     * Groups together the PropertyValues we are testing with their
     * corresponding DataTypes.
     */
    public static class TestCase
    {
        /**
         * Constructor.
         * 
         * @param type the DataType to test
         * @param values the PropertyValues to be tested with this DataType.
         */
        public TestCase(
                DataType         type,
                PropertyValue [] values )
        {
            theType   = type;
            theValues = values;
        }

        /**
         * DataType involved in this set of test cases.
         */
        public DataType theType;

        /**
         * PropertyValues, each of which is a test case in this set.
         */
        public PropertyValue [] theValues;
    }

    /**
     * Special setup for EnumeratedDataTypes.
     */
    public static class EnumeratedTestCase
            extends
                TestCase
    {
        /**
         * Constructor.
         * 
         * @param type the EnumeratedDataType to test, which also defines the PropertyValues
         */
        public EnumeratedTestCase(
                EnumeratedDataType type )
        {
            super( type, type.getDomain() );
        }
    }
}
