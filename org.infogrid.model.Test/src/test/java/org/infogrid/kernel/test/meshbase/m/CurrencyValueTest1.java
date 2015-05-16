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

import org.infogrid.model.primitives.CurrencyDataType;
import org.infogrid.model.primitives.CurrencyValue;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests the implementation of CurrencyValue. Not strictly a MeshBaseTest but we parked it here for lack of
 * a better place.
 */
public class CurrencyValueTest1
        extends
            AbstractTest
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
        log.info( "Creating CurrencyValues" );

        TestData [] data = {
                new TestData(
                        CurrencyValue.create( true, 0, 0, CurrencyDataType.USD ),
                        0.,
                        "$0.00" ),
                new TestData(
                        CurrencyValue.create( true, 1, 0, CurrencyDataType.USD ),
                        1.,
                        "$1.00" ),
                new TestData(
                        CurrencyValue.create( true, 0, 9, CurrencyDataType.USD ),
                        0.09,
                        "$0.09" ),
                new TestData(
                        CurrencyValue.create( true, 2, 456, CurrencyDataType.USD ),
                        6.56,
                        "$6.56" ),
                new TestData(
                        CurrencyValue.create( false, 2, 0, CurrencyDataType.USD ),
                        -2.,
                        "-$2.00" ),
                new TestData(
                        CurrencyValue.create( false, 12, 34, CurrencyDataType.USD ),
                        -12.34,
                        "-$12.34" ),
        };
        
        testConversion( data );
        testArithmetic( data );
    }
    
    /**
     * Test conversion features.
     * 
     * @param data the TestData
     */
    protected void testConversion(
            TestData [] data )
    {
        log.info( "Testing CurrencyValue conversion" );
        
        for( int i=0 ; i<data.length ; ++i ) {
            TestData current = data[i];
            
            log.debug( "Now looking at", i, current );
            
            double asDouble1 = current.getCurrency().getAsDouble();
            double asDouble2 = current.getNumeric();
            
            if( Math.abs( asDouble1 - asDouble2 ) > EPS ) {
                reportError( "Wrong numeric value", current, asDouble1, asDouble2 );
            }
            
            String asText1 = current.getCurrency().value();
            String asText2 = current.getText();
            
            checkEquals( asText1, asText2, "Not the same text representation" );
        }
    }

    /**
     * Test arithmetic features.
     * 
     * @param data the TestData
     */
    protected void testArithmetic(
            TestData [] data )
    {
        log.info( "Testing CurrencyValue arithmetic" );
        
        // We add one value after the next, and we subtract every odd one
        CurrencyValue allPlusCurrency   = CurrencyValue.create( true, 0, 0, CurrencyDataType.USD );
        CurrencyValue plusMinusCurrency = CurrencyValue.create( true, 0, 0, CurrencyDataType.USD );
        double        allPlusDouble     = 0;
        double        plusMinusDouble   = 0;

        for( int i=0 ; i<data.length ; ++i ) {
            TestData current = data[i];
            
            log.debug( "Now looking at", i, current );

            allPlusCurrency = allPlusCurrency.plus( current.getCurrency() );
            allPlusDouble   += current.getNumeric();
            
            if( i % 2 == 0 ) {
                plusMinusCurrency = plusMinusCurrency.plus( current.getCurrency() );
                plusMinusDouble   += current.getNumeric();
                
            } else {
                plusMinusCurrency = plusMinusCurrency.minus( current.getCurrency() );
                plusMinusDouble   -= current.getNumeric();
            }
            
            log.debug( "Comparing plus", allPlusCurrency, allPlusDouble );
            if( Math.abs( allPlusCurrency.getAsDouble() - allPlusDouble ) > EPS ) {
                reportError( "Plus is wrong", current, allPlusCurrency, allPlusDouble );
            }

            log.debug( "Comparing plusMinus", plusMinusCurrency, plusMinusDouble );
            if( Math.abs( plusMinusCurrency.getAsDouble() - plusMinusDouble ) > EPS ) {
                reportError( "PlusMinus is wrong", current, plusMinusCurrency, plusMinusDouble );
            }
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( CurrencyValueTest1.class );

    /**
     * The numeric error we are allowed to make before reporting an error.
     */
    protected static final double EPS = 1e-6;

    /**
     * Captures one set of test data.
     */
    public static class TestData
    {
        /**
         * Constructor.
         * 
         * @param currency the CurrencyValue to test
         * @param numeric its expected numeric representation
         * @param text its expected textual representation
         */
        public TestData(
                CurrencyValue currency,
                double        numeric,
                String        text )
        {
            theCurrency = currency;
            theNumeric  = numeric;
            theText     = text;
        }
        
        /**
         * Obtain the CurrencyValue to test.
         * 
         * @return the CurrencyValue
         */
        public CurrencyValue getCurrency()
        {
            return theCurrency;
        }
        
        /**
         * Obtain the expected numeric representation.
         * 
         * @return the numeric representation
         */
        public double getNumeric()
        {
            return theNumeric;
        }
        
        /**
         * Obtain the expected String representation
         * 
         * @return the String representation
         */
        public String getText()
        {
            return theText;
        }
        
        /**
         * Convert to String, for debugging.
         * 
         * @return String representation
         */
        @Override
        public String toString()
        {
            return "TestData( " + theCurrency + ", " + theNumeric + " , " + theText + " )";
        }
        
        /**
         * The CurrrencyValue to test.
         */
        protected CurrencyValue theCurrency;
        
        /**
         * The expected numeric representation of the CurrencyValue.
         */
        protected double theNumeric;
        
        /**
         * The expected String representation of the CurrencyValue.
         */
        protected String theText;
    }
}
