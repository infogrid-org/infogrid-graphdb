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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.text;

import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.SimpleStringRepresentationParameters;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * Stringifies the allowed MIME types of a BlobDataType.
 */
public class BlobMimeOptionsStringifier
        extends
            AbstractStringifier<BlobDataType>
{
    /**
     * Factory method.
     *
     * @param begin String to print prior to a value if the value is not selected.
     * @param beginSelected String to print prior to a value if the value is selected.
     * @param middle String to print between values
     * @param end String to print after a value if the value is not selected.
     * @param endSelected String to print after a value if the value is selected.
     * @return the created EnumeratedValueStringifier
     */
    public static BlobMimeOptionsStringifier create(
            String begin,
            String beginSelected,
            String middle,
            String end,
            String endSelected )
    {
        return new BlobMimeOptionsStringifier( begin, beginSelected, middle, end, endSelected );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param begin String to print prior to a value if the value is not selected.
     * @param beginSelected String to print prior to a value if the value is selected.
     * @param middle String to print between values
     * @param end String to print after a value if the value is not selected.
     * @param endSelected String to print after a value if the value is selected.
     */
    protected BlobMimeOptionsStringifier(
            String begin,
            String beginSelected,
            String middle,
            String end,
            String endSelected )
    {
        theBeginString         = begin;
        theBeginStringSelected = beginSelected;
        theMiddleString        = middle;
        theEndString           = end;
        theEndStringSelected   = endSelected;
    }

    /**
     * Obtain the String to print prior to a value if the value is not selected.
     *
     * @return the String, if any
     */
    public String getBeginString()
    {
        return theBeginString;
    }

    /**
     * Obtain the String to print prior to a value if the value is selected.
     *
     * @return the String, if any
     */
    public String getBeginStringSelected()
    {
        return theBeginStringSelected;
    }

    /**
     * Obtain the String to print between values.
     *
     * @return the String, if any
     */
    public String getMiddleString()
    {
        return theMiddleString;
    }

    /**
     * Obtain the String to print after a value if the value is not selected.
     *
     * @return the String, if any
     */
    public String getEndString()
    {
        return theEndString;
    }

    /**
     * Obtain the String to print after a value if the value is selected.
     *
     * @return the String, if any
     */
    public String getEndStringSelected()
    {
        return theEndStringSelected;
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     */
    public String format(
            String                         soFar,
            BlobDataType                   arg,
            StringRepresentationParameters pars )
    {
        String selectedMime;
        BlobValue selected = (BlobValue) pars.get( CURRENT_VALUE );
        if( selected != null ) {
            selectedMime = selected.getMimeType();
        } else {
            selectedMime = null;
        }

        String []     values = arg.getDefaultMimeTypes();
        StringBuilder ret    = new StringBuilder();

        String sep = null;
        for( int i=0 ; i<values.length ; ++i ) {
            String current = values[i].toString();
            if( sep != null ) {
                ret.append( sep );
            }
            if( current.equals( selectedMime )) {
                if( theBeginStringSelected != null ) {
                    ret.append( theBeginStringSelected );
                }
                ret.append( current );
                if( theEndStringSelected != null ) {
                    ret.append( theEndStringSelected );
                }
            } else {
                if( theBeginString != null ) {
                    ret.append( theBeginString );
                }
                ret.append( current );
                if( theEndString != null ) {
                    ret.append( theEndString );
                }
            }
            sep = theMiddleString;
        }

        return potentiallyShorten( ret.toString(), pars );
    }

    /**
     * Format an Object using this Stringifier. This may be null.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @return the formatted String
     * @throws ClassCastException thrown if this Stringifier could not format the provided Object
     *         because the provided Object was not of a type supported by this Stringifier
     */
    public String attemptFormat(
            String                         soFar,
            Object                         arg,
            StringRepresentationParameters pars )
        throws
            ClassCastException
    {
        if( arg instanceof BlobDataType ) {
            BlobDataType realArg = (BlobDataType) arg;

            return format( soFar, realArg, pars );

        } else if( arg instanceof PropertyType ) {
            PropertyType realArg  = (PropertyType) arg;
            BlobDataType realType = (BlobDataType) realArg.getDataType();

            return format( soFar, realType, pars );

        } else if( arg instanceof BlobValue ) {
            BlobValue    realArg  = (BlobValue) arg;
            BlobDataType realType = realArg.getDataType();

            SimpleStringRepresentationParameters realPars = SimpleStringRepresentationParameters.create( pars );
            realPars.put( CURRENT_VALUE, realArg );

            return format( soFar, realType, realPars );

        } else {
            throw new ClassCastException( "Cannot stringify " + arg );
        }
    }

    /**
     * The String to print prior to a value if the value is not selected.
     */
    protected String theBeginString;

    /**
     * The String to print prior to a value if the value is selected.
     */
    protected String theBeginStringSelected;

    /**
     * The String to print between values.
     */
    protected String theMiddleString;

    /**
     * The String to print after a value if the value is not selected.
     */
    protected String theEndString;

    /**
     * The String to print after a value if the value is selected.
     */
    protected String theEndStringSelected;

    /**
     * Key for the current BlobValue, if any.
     */
    public static final String CURRENT_VALUE = "current-value";
}
