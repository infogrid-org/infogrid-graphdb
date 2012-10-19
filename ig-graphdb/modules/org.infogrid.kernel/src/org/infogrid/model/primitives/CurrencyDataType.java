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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.io.ObjectStreamException;
import java.text.ParseException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParseException;
import org.infogrid.util.text.StringifierException;

/**
  * This is a DataType for PropertyValue capturing money.
  */
public class CurrencyDataType
        extends DataType
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * This is the default instance of this class. It represents
      * a currency data type with no restriction.
      */
    public static final CurrencyDataType theDefault
            = new CurrencyDataType( null );

    /**
     * Private constructor, use factory method instead.
     *
     * @param superType the DataType that we refine, if any
     */
    private CurrencyDataType(
            DataType superType )
    {
        super( superType );
    }

    /**
      * Test for equality.
      *
      * @param other object to test against
      * @return true if the two objects are equal
      */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(
            Object other )
    {
        return this == other;
    }

    /**
     * Determine hash code. Make editor happy that otherwise indicates a warning.
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * Determine whether this PropertyValue conforms to the constraints of this instance of DataType.
     *
     * @param value the candidate PropertyValue
     * @return 0 if the candidate PropertyValue conforms to this type. Non-zero values depend
     *         on the DataType; generally constructed by analogy with the return value of strcmp.
     * @throws ClassCastException if this PropertyValue has the wrong type (e.g.
     *         the PropertyValue is a StringValue, and the DataType an IntegerDataType)
     */
    public int conforms(
            PropertyValue value )
        throws
            ClassCastException
    {
        CurrencyValue realValue = (CurrencyValue) value; // may throw

        return 0;
    }

    /**
      * Obtain the Java class that can hold values of this data type.
      *
      * @return the Java class that can hold values of this data type
      */
    public Class<CurrencyValue> getCorrespondingJavaClass()
    {
        return CurrencyValue.class;
    }

    /**
     * Obtain the default value of this DataType.
     *
     * @return the default value of this DataType
     */
    public CurrencyValue getDefaultValue()
    {
        return CurrencyValue.create( 1, 0, USD );
    }

    /**
     * Correctly deserialize a static instance.
     *
     * @return the static instance if appropriate
     * @throws ObjectStreamException thrown if reading from the stream failed
     */
    public Object readResolve()
        throws
            ObjectStreamException
    {
        if( this.equals( theDefault )) {
            return theDefault;
        } else {
            return this;
        }
    }

    /**
     * Obtain a value expression in the Java language that invokes the constructor
     * of factory method of the underlying concrete class, thereby creating or
     * reusing an instance of the underlying concrete class that is identical
     * to the instance on which this method was invoked.
     *
     * This is used primarily for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @return the Java language expression
     */
    public String getJavaConstructorString(
            String classLoaderVar )
    {
        final String className = getClass().getName();

        if( this == theDefault ) {
            return className + DEFAULT_STRING;

        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        return rep.formatEntry(
                CurrencyValue.class,
                DEFAULT_ENTRY,
                pars,
                this,
                theSupertype );
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     *
     * @param representation the StringRepresentation in which the String s is given
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    public CurrencyValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException
    {
        try {
            Object [] found = representation.parseEntry( CurrencyValue.class, StringRepresentation.DEFAULT_ENTRY, pars, s, this );

            CurrencyValue ret;
            switch( found.length ) {
                case 1:
                case 2:
                    ret = (CurrencyValue) found[0];
                    break;

                default:
                    throw new PropertyValueParsingException( this, representation, s );
            }

            return ret;

        } catch( StringRepresentationParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex.getFormatString(), ex );

        } catch( ParseException ex ) {
            throw new PropertyValueParsingException( this, representation, s, null, ex );

        } catch( ClassCastException ex ) {
            throw new PropertyValueParsingException( this, representation, s, ex );
        }
    }

    /**
     * Given a Unit as an ISO code, find the pre-defined constant.
     *
     * @param s the ISO code
     * @return the constant, or null if not found
     */
    public static Unit findUnitForCode(
            String s )
    {
        s = s.toUpperCase();
        for( int i=0 ; i<ALL_CURRENCIES.length ; ++i ) {
            if( s.equals( ALL_CURRENCIES[i].theCode )) {
                return ALL_CURRENCIES[i];
            }
        }
        return null;
    }

    /**
     * Given a Unit as a symbol, find the pre-defined constant.
     *
     */
    public static Unit findUnitForSymbol(
            String s )
    {
        for( int i=0 ; i<ALL_CURRENCIES.length ; ++i ) {
            if( s.equals( ALL_CURRENCIES[i].theSymbol )) {
                return ALL_CURRENCIES[i];
            }
        }
        return null;
    }

    /**
     * Represents a currency unit, such as US dollar.
     */
    public static class Unit
    {
        /**
         * Private constructor, for use of this class only. Most currencies have
         * two decimal places for the fraction, so this is what this constructor does.
         *
         * @param code the ISO code for this Unit
         */
        private Unit(
                String code )
        {
            this( code, null, 2 );
        }

        /**
         * Private constructor, for use of this class only. Most currencies have
         * @param symbol the commonly used symbol for this Unit
         * two decimal places for the fraction, so this is what this constructor does.
         *
         * @param code the ISO code for this Unit
         */
        private Unit(
                String code,
                String symbol )
        {
            this( code, symbol, 2 );
        }

        /**
         * Private constructor, for use of this class only.
         *
         * @param code the ISO code for this Unit
         * @param fractionPlaces the number of decimal places for the fraction
         */
        private Unit(
                String code,
                int    fractionPlaces )
        {
            this( code, null, fractionPlaces );
        }

        /**
         * Private constructor, for use of this class only.
         *
         * @param code the ISO code for this Unit
         * @param symbol the commonly used symbol for this Unit
         * @param fractionPlaces the number of decimal places for the fraction
         */
        private Unit(
                String code,
                String symbol,
                int    fractionPlaces )
        {
            theCode           = code;
            theSymbol         = symbol;
            theFractionPlaces = fractionPlaces;
        }

        /**
         * Obtain the ISO 4217 code for this unit.
         *
         * @return the code for this unit
         */
        public String getIsoCode()
        {
            return theCode;
        }

        /**
         * Obtain the commonly used symbol for this unit.
         *
         * @return the symbol
         */
        public String getSymbol()
        {
            return theSymbol;
        }

        /**
         * Obtain the number of places of the fractional part.
         *
         * @return the number of decimal places
         */
        public int getFractionPlaces()
        {
            return theFractionPlaces;
        }

        /**
         * Obtain the factor by which the whole part needs to be multiplied to be equivalent to the fractional part.
         *
         * @return the factor
         */
        public int getFractionMultiplier()
        {
            int ret = 1;
            for( int i=0 ; i<theFractionPlaces ; ++i ) {
                ret *= 10;
            }
            return ret;
        }

        /**
         * Format a whole and a fraction suitably for this Unit.
         *
         * @param whole the whole
         * @param fraction the fraction
         * @return formatted String
         */
        public String format(
                long whole,
                int  fraction )
        {
            StringBuilder buf = new StringBuilder();

            if( theSymbol != null ) {
                buf.append( theSymbol );
            }

            if( whole > 0 ) {
                buf.append( whole );
            } else {
                buf.append( "0" );
            }
            if( theFractionPlaces > 0 ) {
                buf.append( "." );
                String tmp = String.valueOf( fraction );
                for( int i=theFractionPlaces-tmp.length() ; i>0 ; --i ) {
                    buf.append( "0" );
                }
                buf.append( tmp );
            }
            if( theSymbol == null ) {
                buf.append( ' ' );
                buf.append( theCode );
            }
            return buf.toString();
        }

        /**
         * Given an amount as a String, return the appropriate CurrencyValue.
         *
         * @param s the String
         * @return the CurrencyValue
         */
        public CurrencyValue createCurrencyValue(
                String s )
        {
            long wholes;
            int  fractions;

            int    period = s.indexOf( '.' );
            String remainder;

            if( period > 0 ) {
                wholes = Long.parseLong( s.substring( 0, period ));
                remainder = s.substring( period+1 );
            } else if( period == 0 ) {
                wholes = 0L;
                remainder = s.substring( 1 );
            } else {
                wholes    = Long.parseLong( s );
                remainder = null;
            }

            if( remainder != null ) {
                fractions = Integer.parseInt( remainder );
                for( int i=theFractionPlaces-remainder.length() ; i>0 ; --i ) {
                    fractions *= 10;
                }
            } else {
                fractions = 0;
            }
            return CurrencyValue.create( wholes, fractions, this );
        }

        /**
         * Convert to String form, for debugging.
         *
         * @return String form
         */
        @Override
        public String toString()
        {
            return "Currency unit " + theCode;
        }

        /**
         * The ISO code for this Unit.
         */
        protected String theCode;

        /**
         * The commonly used symbol for this Unit.
         */
        protected String theSymbol;

        /**
         * The number of decimal places for the fraction.
         */
        protected int theFractionPlaces;
    }

    // The known currencies, from http://en.wikipedia.org/wiki/ISO_4217
    public static final Unit AED = new Unit( "AED" );
    public static final Unit AFN = new Unit( "AFN" );
    public static final Unit ALL = new Unit( "ALL" );
    public static final Unit AMD = new Unit( "AMD" );
    public static final Unit ANG = new Unit( "ANG" );
    public static final Unit AOA = new Unit( "AOA" );
    public static final Unit ARS = new Unit( "ARS" );
    public static final Unit AUD = new Unit( "AUD" );
    public static final Unit AWG = new Unit( "AWG" );
    public static final Unit AZN = new Unit( "AZN" );
    public static final Unit BAM = new Unit( "BAM" );
    public static final Unit BBD = new Unit( "BBD" );
    public static final Unit BDT = new Unit( "BDT" );
    public static final Unit BGN = new Unit( "BGN" );
    public static final Unit BHD = new Unit( "BHD", 3 );
    public static final Unit BIF = new Unit( "BIF", 0 );
    public static final Unit BMD = new Unit( "BMD" );
    public static final Unit BND = new Unit( "BND" );
    public static final Unit BOB = new Unit( "BOB" );
    public static final Unit BOV = new Unit( "BOV" );
    public static final Unit BRL = new Unit( "BRL" );
    public static final Unit BSD = new Unit( "BSD" );
    public static final Unit BTN = new Unit( "BTN" );
    public static final Unit BWP = new Unit( "BWP" );
    public static final Unit BYR = new Unit( "BYR", 0 );
    public static final Unit BZD = new Unit( "BZD" );
    public static final Unit CAD = new Unit( "CAD" );
    public static final Unit CDF = new Unit( "CDF" );
    public static final Unit CHE = new Unit( "CHE" );
    public static final Unit CHF = new Unit( "CHF" );
    public static final Unit CHW = new Unit( "CHW" );
    public static final Unit CLF = new Unit( "CLF", 0 );
    public static final Unit CLP = new Unit( "CLP", 0 );
    public static final Unit CNY = new Unit( "CNY", 1 );
    public static final Unit COP = new Unit( "COP", 0 );
    public static final Unit COU = new Unit( "COU" );
    public static final Unit CRC = new Unit( "CRC" );
    public static final Unit CUC = new Unit( "CUC" );
    public static final Unit CUP = new Unit( "CUP" );
    public static final Unit CVE = new Unit( "CVE", 0 );
    public static final Unit CZK = new Unit( "CZK" );
    public static final Unit DJF = new Unit( "DJF", 0 );
    public static final Unit DKK = new Unit( "DKK" );
    public static final Unit DOP = new Unit( "DOP" );
    public static final Unit DZD = new Unit( "DZD" );
    public static final Unit EEK = new Unit( "EEK" );
    public static final Unit EGP = new Unit( "EGP" );
    public static final Unit ERN = new Unit( "ERN" );
    public static final Unit ETB = new Unit( "ETB" );
    public static final Unit EUR = new Unit( "EUR", "\u20AC" );
    public static final Unit FJD = new Unit( "FJD" );
    public static final Unit FKP = new Unit( "FKP" );
    public static final Unit GBP = new Unit( "GBP" );
    public static final Unit GEL = new Unit( "GEL" );
    public static final Unit GHS = new Unit( "GHS" );
    public static final Unit GIP = new Unit( "GIP" );
    public static final Unit GMD = new Unit( "GMD" );
    public static final Unit GNF = new Unit( "GNF", 0 );
    public static final Unit GTQ = new Unit( "GTQ" );
    public static final Unit GYD = new Unit( "GYD" );
    public static final Unit HKD = new Unit( "HKD" );
    public static final Unit HNL = new Unit( "HNL" );
    public static final Unit HRK = new Unit( "HRK" );
    public static final Unit HTG = new Unit( "HTG" );
    public static final Unit HUF = new Unit( "HUF" );
    public static final Unit IDR = new Unit( "IDR", 0 );
    public static final Unit ILS = new Unit( "ILS" );
    public static final Unit INR = new Unit( "INR" );
    public static final Unit IQD = new Unit( "IQD", 0 );
    public static final Unit IRR = new Unit( "IRR", 0 );
    public static final Unit ISK = new Unit( "ISK", 0 );
    public static final Unit JMD = new Unit( "JMD" );
    public static final Unit JOD = new Unit( "JOD", 3 );
    public static final Unit JPY = new Unit( "JPY", 0 );
    public static final Unit KES = new Unit( "KES" );
    public static final Unit KGS = new Unit( "KGS" );
    public static final Unit KHR = new Unit( "KHR", 0 );
    public static final Unit KMF = new Unit( "KMF", 0 );
    public static final Unit KPW = new Unit( "KPW", 0 );
    public static final Unit KRW = new Unit( "KRW", 0 );
    public static final Unit KWD = new Unit( "KWD", 3 );
    public static final Unit KYD = new Unit( "KYD" );
    public static final Unit KZT = new Unit( "KZT" );
    public static final Unit LAK = new Unit( "LAK", 0 );
    public static final Unit LBP = new Unit( "LBP", 0 );
    public static final Unit LKR = new Unit( "LKR" );
    public static final Unit LRD = new Unit( "LRD" );
    public static final Unit LSL = new Unit( "LSL" );
    public static final Unit LTL = new Unit( "LTL" );
    public static final Unit LVL = new Unit( "LVL" );
    public static final Unit LYD = new Unit( "LYD", 3 );
    public static final Unit MAD = new Unit( "MAD" );
    public static final Unit MDL = new Unit( "MDL" );
    public static final Unit MGA = new Unit( "MGA", 1 ); // note: not 10th but 5th -- not sure how to represent this
    public static final Unit MKD = new Unit( "MKD" );
    public static final Unit MMK = new Unit( "MMK", 0 );
    public static final Unit MNT = new Unit( "MNT" );
    public static final Unit MOP = new Unit( "MOP", 1 );
    public static final Unit MRO = new Unit( "MRO", 1 ); // note: not 10th but 5th -- not sure how to represent this
    public static final Unit MUR = new Unit( "MUR" );
    public static final Unit MVR = new Unit( "MVR" );
    public static final Unit MWK = new Unit( "MWK" );
    public static final Unit MXN = new Unit( "MXN" );
    public static final Unit MXV = new Unit( "MXV" );
    public static final Unit MYR = new Unit( "MYR" );
    public static final Unit MZN = new Unit( "MZN" );
    public static final Unit NAD = new Unit( "NAD" );
    public static final Unit NGN = new Unit( "NGN" );
    public static final Unit NIO = new Unit( "NIO" );
    public static final Unit NOK = new Unit( "NOK" );
    public static final Unit NPR = new Unit( "NPR" );
    public static final Unit NZD = new Unit( "NZD" );
    public static final Unit OMR = new Unit( "OMR", 3 );
    public static final Unit PAB = new Unit( "PAB" );
    public static final Unit PEN = new Unit( "PEN" );
    public static final Unit PGK = new Unit( "PGK" );
    public static final Unit PHP = new Unit( "PHP" );
    public static final Unit PKR = new Unit( "PKR" );
    public static final Unit PLN = new Unit( "PLN" );
    public static final Unit PYG = new Unit( "PYG", 0 );
    public static final Unit QAR = new Unit( "QAR" );
    public static final Unit RON = new Unit( "RON" );
    public static final Unit RSD = new Unit( "RSD" );
    public static final Unit RUB = new Unit( "RUB" );
    public static final Unit RWF = new Unit( "RWF", 0 );
    public static final Unit SAR = new Unit( "SAR" );
    public static final Unit SBD = new Unit( "SBD" );
    public static final Unit SCR = new Unit( "SCR" );
    public static final Unit SDG = new Unit( "SDG" );
    public static final Unit SEK = new Unit( "SEK" );
    public static final Unit SGD = new Unit( "SGD" );
    public static final Unit SHP = new Unit( "SHP" );
    public static final Unit SLL = new Unit( "SLL", 0 );
    public static final Unit SOS = new Unit( "SOS" );
    public static final Unit SRD = new Unit( "SRD" );
    public static final Unit STD = new Unit( "STD", 0 );
    public static final Unit SYP = new Unit( "SYP" );
    public static final Unit SZL = new Unit( "SZL" );
    public static final Unit THB = new Unit( "THB" );
    public static final Unit TJS = new Unit( "TJS" );
    public static final Unit TMT = new Unit( "TMT" );
    public static final Unit TND = new Unit( "TND", 3 );
    public static final Unit TOP = new Unit( "TOP" );
    public static final Unit TRY = new Unit( "TRY" );
    public static final Unit TTD = new Unit( "TTD" );
    public static final Unit TWD = new Unit( "TWD", 1 );
    public static final Unit TZS = new Unit( "TZS" );
    public static final Unit UAH = new Unit( "UAH" );
    public static final Unit UGX = new Unit( "UGX", 0 );
    public static final Unit USD = new Unit( "USD", "$" );
    public static final Unit USN = new Unit( "USN" );
    public static final Unit USS = new Unit( "USS" );
    public static final Unit UYU = new Unit( "UYU" );
    public static final Unit UZS = new Unit( "UZS" );
    public static final Unit VEF = new Unit( "VEF" );
    public static final Unit VND = new Unit( "VND", 0 );
    public static final Unit VUV = new Unit( "VUV", 0 );
    public static final Unit WST = new Unit( "WST" );
    public static final Unit XAF = new Unit( "XAF", 0 );
    public static final Unit XAG = new Unit( "XAG", 0 );
    public static final Unit XAU = new Unit( "XAU", 0 );
    public static final Unit XBA = new Unit( "XBA", 0 );
    public static final Unit XBB = new Unit( "XBB", 0 );
    public static final Unit XBC = new Unit( "XBC", 0 );
    public static final Unit XBD = new Unit( "XBD", 0 );
    public static final Unit XCD = new Unit( "XCD" );
    public static final Unit XDR = new Unit( "XDR", 0 );
    public static final Unit XFU = new Unit( "XFU", 0 );
    public static final Unit XOF = new Unit( "XOF", 0 );
    public static final Unit XPD = new Unit( "XPD", 0 );
    public static final Unit XPF = new Unit( "XPF", 0 );
    public static final Unit XPT = new Unit( "XPT", 0 );
    public static final Unit XTS = new Unit( "XTS", 0 );
    public static final Unit XXX = new Unit( "XXX", 0 );
    public static final Unit YER = new Unit( "YER", 0 );
    public static final Unit ZAR = new Unit( "ZAR" );
    public static final Unit ZMK = new Unit( "ZMK", 0 );
    public static final Unit ZWL = new Unit( "ZWL" );

    /**
     * Set of known currencies.
     */
    public static final Unit [] ALL_CURRENCIES = {
            AED,
            AFN,
            ALL,
            AMD,
            ANG,
            AOA,
            ARS,
            AUD,
            AWG,
            AZN,
            BAM,
            BBD,
            BDT,
            BGN,
            BHD,
            BIF,
            BMD,
            BND,
            BOB,
            BOV,
            BRL,
            BSD,
            BTN,
            BWP,
            BYR,
            BZD,
            CAD,
            CDF,
            CHE,
            CHF,
            CHW,
            CLF,
            CLP,
            CNY,
            COP,
            COU,
            CRC,
            CUC,
            CUP,
            CVE,
            CZK,
            DJF,
            DKK,
            DOP,
            DZD,
            EEK,
            EGP,
            ERN,
            ETB,
            EUR,
            FJD,
            FKP,
            GBP,
            GEL,
            GHS,
            GIP,
            GMD,
            GNF,
            GTQ,
            GYD,
            HKD,
            HNL,
            HRK,
            HTG,
            HUF,
            IDR,
            ILS,
            INR,
            IQD,
            IRR,
            ISK,
            JMD,
            JOD,
            JPY,
            KES,
            KGS,
            KHR,
            KMF,
            KPW,
            KRW,
            KWD,
            KYD,
            KZT,
            LAK,
            LBP,
            LKR,
            LRD,
            LSL,
            LTL,
            LVL,
            LYD,
            MAD,
            MDL,
            MGA,
            MKD,
            MMK,
            MNT,
            MOP,
            MRO,
            MUR,
            MVR,
            MWK,
            MXN,
            MXV,
            MYR,
            MZN,
            NAD,
            NGN,
            NIO,
            NOK,
            NPR,
            NZD,
            OMR,
            PAB,
            PEN,
            PGK,
            PHP,
            PKR,
            PLN,
            PYG,
            QAR,
            RON,
            RSD,
            RUB,
            RWF,
            SAR,
            SBD,
            SCR,
            SDG,
            SEK,
            SGD,
            SHP,
            SLL,
            SOS,
            SRD,
            STD,
            SYP,
            SZL,
            THB,
            TJS,
            TMT,
            TND,
            TOP,
            TRY,
            TTD,
            TWD,
            TZS,
            UAH,
            UGX,
            USD,
            USN,
            USS,
            UYU,
            UZS,
            VEF,
            VND,
            VUV,
            WST,
            XAF,
            XAG,
            XAU,
            XBA,
            XBB,
            XBC,
            XBD,
            XCD,
            XDR,
            XFU,
            XOF,
            XPD,
            XPF,
            XPT,
            XTS,
            XXX,
            YER,
            ZAR,
            ZMK,
            ZWL
    };
}
