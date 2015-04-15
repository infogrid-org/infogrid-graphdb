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

package org.infogrid.model.traversal.xpath;

import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.TraversalTranslatorException;
import org.infogrid.util.ArrayHelper;

/**
 * Thrown if there was a namespace conflict.
 */
public class XpathNamespaceConflictException
        extends
            TraversalTranslatorException
{
    private static final long serialVersionUID = 1L; //helps with serialization

    /**
     * Constructor.
     *
     * @param prefix the prefix
     * @param oldValue the old value
     * @param newValue the new value
     */
    public XpathNamespaceConflictException(
            String      prefix,
            SubjectArea oldValue,
            SubjectArea newValue )
    {
        super( null );

        thePrefix = prefix;
        theOldValue = oldValue;
        theNewValue = newValue;
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
            thePrefix,
            theOldValue,
            theNewValue };
    }

    /**
     * The prefix with a conflict.
     */
    protected String thePrefix;

    /**
     * The old definition of the prefix.
     */
    protected SubjectArea theOldValue;

    /**
     * The new definition of the prefix.
     */
    protected SubjectArea theNewValue;
}
