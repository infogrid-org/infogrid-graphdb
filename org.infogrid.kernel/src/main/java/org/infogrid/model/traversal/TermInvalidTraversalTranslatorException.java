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

package org.infogrid.model.traversal;

import org.infogrid.util.ArrayHelper;

/**
 * A term was invalid.
 */
public class TermInvalidTraversalTranslatorException
        extends
            TraversalTranslatorException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param terms the terms that were given
     * @param invalid the invalid term
     * @param msg the message
     */
    public TermInvalidTraversalTranslatorException(
            String [] terms,
            String    invalid,
            String    msg )
    {
        super( terms, msg );

        theInvalid = invalid;
    }

    /**
     * Constructor.
     *
     * @param terms the terms that were given
     * @param invalid the invalid term
     * @param cause the cause of the exception
     */
    public TermInvalidTraversalTranslatorException(
            String [] terms,
            String    invalid,
            Throwable cause )
    {
        super( terms, cause );

        theInvalid = invalid;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        String msg = getMessage();
        if( msg == null && getCause() != null ) {
            msg = getCause().getMessage();
        }
        return new Object[] {
            theTerms.length,
            theTerms,
            ArrayHelper.join( " ", theTerms ),
            theInvalid,
            msg
        };
    }

    /**
     * The invalid term.
     */
    protected String theInvalid;
}
