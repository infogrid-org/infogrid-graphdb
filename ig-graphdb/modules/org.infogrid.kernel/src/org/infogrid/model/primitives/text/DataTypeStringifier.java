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

package org.infogrid.model.primitives.text;

import org.infogrid.model.primitives.DataType;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.AbstractStringifier;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * A Stringifier to stringify subclasses of DataType.
 */
public class DataTypeStringifier
        extends
            AbstractStringifier<DataType>
{
    private static final Log log = Log.getLogInstance( DataTypeStringifier.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @return the created DataTypeStringifier
     */
    public static DataTypeStringifier create()
    {
        return new DataTypeStringifier();
    }

    /**
     * Private constructor for subclasses only, use factory method.
     */
    protected DataTypeStringifier()
    {
        // no op
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
     * @return the formatted String
     */
    public String format(
            String                         soFar,
            DataType                       arg,
            StringRepresentationParameters pars )
    {
        String ret;
        try {
            StringRepresentation plain = ModelPrimitivesStringRepresentationDirectorySingleton.getSingleton().get( ModelPrimitivesStringRepresentationDirectorySingleton.TEXT_PLAIN_NAME );
            ret = arg.toStringRepresentation( plain, pars );

        } catch( StringifierException ex ) {
            log.error( ex );
            ret = arg.getName();
        }
        ret = potentiallyShorten( ret, pars );
        return ret;
    }

    /**
     * Format an Object using this Stringifier. This may be null.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
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
        return format( soFar, (DataType) arg, pars );
    }
}