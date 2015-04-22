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

/**
 * This Exception indicates that a RelationshipType could not be found.
 */
public class RelationshipTypeNotFoundException
        extends
            AttributableMeshTypeNotFoundException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param sa the SubjectArea within which a RelationshipType could not be found
     * @param relationshipTypeName the name of the RelationshipType that could not be found
     */
    public RelationshipTypeNotFoundException(
            SubjectArea sa,
            String relationshipTypeName )
    {
        super( sa, relationshipTypeName );
    }

    /**
     * Constructor.
     *
     * @param sa the SubjectArea within which a RelationshipType could not be found
     * @param relationshipTypeName the name of the RelationshipType that could not be found
     * @param cause the Throwable that caused this Exception
     */
    public RelationshipTypeNotFoundException(
            SubjectArea sa,
            String      relationshipTypeName,
            Throwable   cause )
    {
        super( sa, relationshipTypeName, cause );
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
        almostRet.append( ", RelationshipType: " );
        almostRet.append( theAmtName );
        return almostRet.toString();
    }
}
