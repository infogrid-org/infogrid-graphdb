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

package org.infogrid.model.traversal;

import org.infogrid.util.ArrayHelper;

/**
 * An unknown term was given.
 */
public class UnknownTermTraversalTranslatorException
        extends
            TraversalTranslatorException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param terms the terms that were given
     * @param unknownTerm the unknown term
     */
    public UnknownTermTraversalTranslatorException(
            String [] terms,
            String    unknownTerm )
    {
        super( terms );

        theUnknownTerm = unknownTerm;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] {
                theTerms.length,
                theTerms,
                ArrayHelper.join( " ", theTerms ),
                theUnknownTerm
        };
    }

    /**
     * The unknown term that was given.
     */
    protected String theUnknownTerm;
}
