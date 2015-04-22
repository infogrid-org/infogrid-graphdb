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
 * Thrown if an IntegerValue was out of range for its IntegerDataType.
 */
public abstract class IntegerValueNotInDomainException
        extends
            NotInDomainException
{
    /**
     * Constructor.
     *
     * @param value the invalid value
     * @param type the DataType whose domain was violated
     */
    protected IntegerValueNotInDomainException(
            IntegerValue    value,
            IntegerDataType type )
    {
        super( type );

        theValue = value;
    }

    /**
     * Obtain the DataType whose domain was violated.
     *
     * @return the DataType
     */
    @Override
    public IntegerDataType getDataType()
    {
        return (IntegerDataType) super.getDataType();
    }

    /**
     * Obtain the invalid value.
     *
     * @return the value
     */
    public IntegerValue getValue()
    {
        return theValue;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theType, theValue };
    }

    /**
     * The invalid value.
     */
    protected IntegerValue theValue;
}
