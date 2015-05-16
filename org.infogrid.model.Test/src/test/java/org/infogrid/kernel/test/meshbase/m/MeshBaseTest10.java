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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.IllegalPropertyValueException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.Transaction;
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
import org.infogrid.model.primitives.CurrencyValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that we can't assign the wrong PropertyValue subtypes.
 */
public class MeshBaseTest10
        extends
            AbstractSingleMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    @Test
    public void run()
        throws
            Exception
    {
        MeshObject home = theMeshBase.getHomeObject();
        Transaction tx = theMeshBase.createTransactionNow();
        home.bless( TestSubjectArea.OPTIONALPROPERTIES );
        home.bless( TestSubjectArea.MANDATORYPROPERTIES );
        tx.commitTransaction();

        checkEqualsOutOfSequence( home.getTypes(), new EntityType[] { TestSubjectArea.OPTIONALPROPERTIES, TestSubjectArea.MANDATORYPROPERTIES }, "wrong types on home object" );
        
        //

        PropertyValue [] testValues1 = {
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEANY_type.createBlobValue( "test1", "text/plain" ),
                BooleanValue.TRUE,
                ColorValue.create( 0x202020 ),
                CurrencyValue.parseCurrencyValue( "1.23 USD" ),
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value3" ),
                ExtentValue.create( 1.2, 3.4 ),
                FloatValue.create( 56.78 ),
                IntegerValue.create( 99 ),
                MultiplicityValue.create( 2, 7 ),
                PointValue.create( 9.8, 7.6 ),
                StringValue.create( "some string" ),
                TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short)  4, (short)  5, (float)  6. ),
                TimeStampValue.create(  (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (float) 12. )
        };
        PropertyType [] testProperties1 = {
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEANY,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALBOOLEANDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALCOLORDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALCURRENCYDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALEXTENTDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALFLOATDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALINTEGERDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALMULTIPLICITYDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALPOINTDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALSTRINGDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALTIMEPERIODDATATYPE,
                TestSubjectArea.OPTIONALPROPERTIES_OPTIONALTIMESTAMPDATATYPE
        };

        runWith( theMeshBase, testProperties1, testValues1 );

        //

        PropertyValue [] testValues2 = {
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEANY_type.createBlobValue( "test", "text/plain" ),
                BooleanValue.TRUE,
                ColorValue.create( 0x202020 ),
                CurrencyValue.parseCurrencyValue( "0.01\nEUR" ),
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value3" ),
                ExtentValue.create( 1.2, 3.4 ),
                FloatValue.create( 56.78 ),
                IntegerValue.create( 99 ),
                MultiplicityValue.create( 2, 7 ),
                PointValue.create( 9.8, 7.6 ),
                StringValue.create( "some string" ),
                TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short)  4, (short)  5, (float)  6. ),
                TimeStampValue.create(  (short) 7, (short) 8, (short) 9, (short) 10, (short) 11, (float) 12. )
        };
        PropertyType [] testProperties2 = {
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEANY,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYBOOLEANDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYCOLORDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYCURRENCYDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYEXTENTDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYFLOATDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYINTEGERDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYMULTIPLICITYDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYPOINTDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYSTRINGDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYTIMEPERIODDATATYPE,
                TestSubjectArea.MANDATORYPROPERTIES_MANDATORYTIMESTAMPDATATYPE
        };

        runWith( theMeshBase, testProperties2, testValues2 );
    }

    /**
     * Run one test set.
     *
     * @param mb the MeshBase to use for the test
     * @param testProperties the PropertyTypes to test
     * @param testValues the PropertyValues corresponding to the PropertyTypes to test
     * @throws Exception all sorts of things may go wrong during a test.
     */
    protected void runWith(
            MeshBase         mb,
            PropertyType []  testProperties,
            PropertyValue [] testValues )
        throws
            Exception
    {
        checkEquals( testValues.length, testProperties.length, "inconsistency in test data" );

        MeshObject home = mb.getHomeObject();

        Transaction tx = mb.createTransactionNow();
        
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
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest10.class );
}
