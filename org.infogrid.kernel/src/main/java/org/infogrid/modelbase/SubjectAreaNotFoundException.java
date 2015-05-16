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

package org.infogrid.modelbase;

/**
 * This Exception indicates that a SubjectArea could not be found.
 */
public class SubjectAreaNotFoundException
        extends
            MeshTypeNotFoundException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param saName the fully-qualified name of the SubjectArea
     * @param saVersion the name of the SubjectArea
     */
    public SubjectAreaNotFoundException(
            String saName,
            String saVersion )
    {
        theSubjectAreaName    = saName;
        theSubjectAreaVersion = saVersion;
    }

    /**
     * Constructor.
     *
     * @param saName the fully-qualified name of the SubjectArea
     * @param saVersion the name of the SubjectArea
     * @param cause the Throwable that caused this Exception
     */
    public SubjectAreaNotFoundException(
            String    saName,
            String    saVersion,
            Throwable cause )
    {
        super( cause );

        theSubjectAreaName    = saName;
        theSubjectAreaVersion = saVersion;
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[]{ theSubjectAreaName, theSubjectAreaVersion };
    }

    /**
     * Convert object into string representation, mostly for debugging.
     *
     * @return string representation of this object
     */
    @Override
    public String toString()
    {
        StringBuilder almostRet = new StringBuilder();
        almostRet.append( super.toString() );
        almostRet.append( "SubjectArea: " );
        almostRet.append( theSubjectAreaName );
        almostRet.append( ", Version: " );
        almostRet.append( theSubjectAreaVersion );
        return almostRet.toString();
    }

    /**
     * The fully-qualified name of the SubjectArea that we weren't able to find.
     */
    protected String theSubjectAreaName;

    /**
     * The version of the SubjectArea that we weren't able to find.
     */
    protected String theSubjectAreaVersion;
}
