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

import org.infogrid.model.primitives.SubjectArea;

import org.infogrid.model.primitives.MeshTypeIdentifier;

/**
 * This Exception indicates that an AttributableMeshType could not be found.
 */
public class AttributableMeshTypeNotFoundException
        extends
            MeshTypeNotFoundException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param sa the SubjectArea within which an AttributableMeshObjectType could not be found
     * @param amtName the name of the AttributableMeshType that could not be found
     */
    public AttributableMeshTypeNotFoundException(
            SubjectArea sa,
            String      amtName )
    {
        super();

        theSubjectArea           = sa;
        theSubjectAreaIdentifier = sa.getIdentifier();
        theAmtName               = amtName;
    }

    /**
     * Constructor.
     *
     * @param sa the SubjectArea within which an AttributableMeshObjectType could not be found
     * @param amtName the name of the AttributableMeshType that could not be found
     * @param cause the Throwable that caused this Exception
     */
    public AttributableMeshTypeNotFoundException(
            SubjectArea sa,
            String      amtName,
            Throwable   cause )
    {
        super( cause );

        theSubjectArea     = sa;
        theSubjectAreaIdentifier = sa.getIdentifier();
        theAmtName         = amtName;
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
        almostRet.append( theSubjectAreaIdentifier );
        almostRet.append( ", Amt: " );
        almostRet.append( theAmtName );
        return almostRet.toString();
    }

    /**
     * Obtain parameters for the internationalization.
     *
     * @return the parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[]{ theSubjectAreaIdentifier, theAmtName };
    }

    /**
     * The SubjectArea in which the AttributableMeshObjectType could not be found.
     */
    protected volatile SubjectArea theSubjectArea;
    
    /**
     * The Identifier of the SubjectArea in which the AttributableMeshObjectType could not be found. This
     * is stored as MeshTypeIdentifier rather than SubjectArea in order to support serialization.
     */
    protected MeshTypeIdentifier theSubjectAreaIdentifier;

    /**
     * The name of the AttributableMeshType that could not be found.
     */
    protected String theAmtName;
}
