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
 * Thrown if a MIME type of a BlobValue was not allowed in the set of MIME types
 * defined by its BlobDataType.
 */
public class MimeTypeNotInDomainException
        extends
            NotInDomainException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor, for subclasses only.
     *
     * @param type the DataType whose domain was violated
     * @param mime the disallowed MIME type
     */
    public MimeTypeNotInDomainException(
            BlobDataType type,
            String       mime )
    {
        super( type );

        theMime = mime;
    }

    /**
     * Obtain the disallowed MIME type.
     *
     * @return the MIME type
     */
    public String getMimeType()
    {
        return theMime;
    }

    /**
     * Obtain the BlobDataType whose domain was violated.
     * 
     * @return the BlobDataType
     */
    @Override
    public BlobDataType getDataType()
    {
        return (BlobDataType) theType;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        return new Object[] { theType, theMime, ((BlobDataType)theType).theMimeTypeRegexes };
    }

    /**
     * The disallowed MIME type.
     */
    protected String theMime;
}
