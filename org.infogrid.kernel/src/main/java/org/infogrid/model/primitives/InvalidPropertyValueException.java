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

package org.infogrid.model.primitives;

import org.infogrid.util.AbstractLocalizedRuntimeException;
import org.infogrid.util.ResourceHelper;

/**
 * Indicates that a PropertyValue was illegal.
 */
public abstract class InvalidPropertyValueException
        extends
            AbstractLocalizedRuntimeException
{
    /**
     * All subclasses have a common ResourceHelper for all inner classes.
     *
     * @return the ResourceHelper to use
     */
    @Override
    protected ResourceHelper findResourceHelperForLocalizedMessage()
    {
        return findResourceHelperForLocalizedMessageViaEnclosingClass();
    }

    /**
     * All subclasses have a common ResourceHelper for all inner classes.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationParameter()
    {
        return findParameterViaEnclosingClass( STRING_REPRESENTATION_KEY );
    }

    /**
     * All subclasses have a common ResourceHelper for all inner classes.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationLinkStartParameter()
    {
        return findParameterViaEnclosingClass( STRING_REPRESENTATION_LINK_START_KEY );
    }

    /**
     * All subclasses have a common ResourceHelper for all inner classes.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationLinkEndParameter()
    {
        return findParameterViaEnclosingClass( STRING_REPRESENTATION_LINK_END_KEY );
    }
}
