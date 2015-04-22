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

import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.PropertyType;

/**
 * This Exception is thrown if the specified PropertyType overriding was inconsistent
 * with the model. For example, an EntityType cannot override a PropertyType in another
 * EntityType that is not one of its supertypes.
 */
public class InheritanceConflictException
        extends
            RuntimeException
{
    private static final long serialVersionUID = 1L; // helps with serialization -- FIXME, serialization needs to be supported better

    /**
     * Construct one.
     *
     * @param amt the AttributableMeshType where the problem occurred
     * @param pts the PropertyType that are in conflict
     */
    public InheritanceConflictException(
            AttributableMeshType amt,
            PropertyType []      pts )
    {
        theAmt = amt;
        thePts = pts;
    }

    /**
     * Return in string format, for debugging.
     *
     * @return this object in string format
     */
    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append( getClass().getName() );
        ret.append( ": AttributableMeshType: " );
        ret.append( theAmt );
        ret.append( ", PropertyTypes: " );
        for( int i=0 ; i<thePts.length ; ++i )
        {
            if( i>0 ) {
                ret.append( ", " );
            }
            ret.append( thePts[i].toExternalForm() );
        }
        return ret.toString();
    }

    /**
     * The AttributableMeshType where the problem occurred.
     */
    protected AttributableMeshType theAmt;

    /**
     * The conflicting PropertyTypes.
     */
    protected PropertyType [] thePts;
}
