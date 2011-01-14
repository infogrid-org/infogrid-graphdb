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

package org.infogrid.model.primitives;

import java.util.regex.Pattern;

/**
 * Thrown if a StringValue did not match the regular expression required by its StringDataType.
 */
public class DoesNotMatchRegexException
        extends
            NotInDomainException
{
    /**
     * Constructor.
     *
     * @param value the invalid value
     * @param type the DataType whose domain was violated
     */
    public DoesNotMatchRegexException(
            StringValue    value,
            StringDataType type )
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
    public StringDataType getDataType()
    {
        return (StringDataType) super.getDataType();
    }

    /**
     * Obtain the invalid value.
     *
     * @return the value
     */
    public StringValue getValue()
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
        Pattern regex = ((StringDataType)theType).getRegex();

        return new Object[] { theType, regex != null ? regex.toString() : null, theValue };
    }

    /**
     * The invalid value.
     */
    protected StringValue theValue;
}
