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

/**
 * Thrown if a value was not in the domain specified by its DataType.
 */
public abstract class NotInDomainException
        extends
            InvalidPropertyValueException
{
    /**
     * Constructor, for subclasses only.
     *
     * @param type the DataType whose domain was violated
     */
    protected NotInDomainException(
            DataType type )
    {
        theType = type;
    }

    /**
     * Obtain the DataType whose domain was violated.
     *
     * @return the DataType
     */
    public DataType getDataType()
    {
        return theType;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theType };
    }

    /**
     * The DataType whose domain was violated
     */
    protected DataType theType;
}
