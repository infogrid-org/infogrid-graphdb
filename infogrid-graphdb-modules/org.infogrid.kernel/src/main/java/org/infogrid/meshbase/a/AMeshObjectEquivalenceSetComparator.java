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

package org.infogrid.meshbase.a;

import org.infogrid.mesh.MeshObjectIdentifier;

import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * The representation of equivalence sets in the "A" implementation requires the consistent
 * ordering of MeshObjects by their identifiers. This objects defines the order.
 */
public class AMeshObjectEquivalenceSetComparator
        implements
            Comparator<MeshObjectIdentifier>
{
    private static final Log log = Log.getLogInstance( AMeshObjectEquivalenceSetComparator.class ); // our own, private logger

    /**
     * Private constructor, use singleton instance instead of creating new instances.
     */
    protected AMeshObjectEquivalenceSetComparator()
    {
        // noop
    }

    /**
     * Compares its two arguments for order. 
     * 
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * 	       first argument is less than, equal to, or greater than the
     *	       second. 
     * @throws ClassCastException if the arguments' types prevent them from
     * 	       being compared by this Comparator.
     */
    public int compare(
            MeshObjectIdentifier o1,
            MeshObjectIdentifier o2 )
    {
        int ret = o1.toExternalForm().compareTo( o2.toExternalForm() );
        return ret;
    }
    
    
    /**
     * Obtain the left and right neighbors of the provided MeshObject, given the
     * equivalents, for the "A" implementation.
     *
     * @param meshObject the Identifier of the MeshObject whose left and right neighbors we seek
     * @param equivalents the Identifiers of the equivalent MeshObjects, if any
     * @return array of length 2: left equivalent and right equivalent, either of both of which may be null
     */
    public MeshObjectIdentifier[] findLeftAndRightEquivalents(
            MeshObjectIdentifier    meshObject,
            MeshObjectIdentifier [] equivalents )
    {
        MeshObjectIdentifier left  = null;
        MeshObjectIdentifier right = null;

        if( equivalents != null && equivalents.length > 0 ) {
            for( int i=0 ; i<equivalents.length ; ++i ) {
                // determine whether the current object is left or right
                int comp = compare( equivalents[i], meshObject );
                if( comp < 0 ) {
                    if( left == null ) {
                        left = equivalents[i];
                    } else if( compare( left, equivalents[i] ) < 0 ) {
                        left = equivalents[i];
                    }
                } else if( comp > 0 ) {
                    if( right == null ) {
                        right = equivalents[i];
                    } else if( compare( equivalents[i], right ) < 0 ) {
                        right = equivalents[i];
                    }
                    
                } else {
                    log.error( "MeshObject cannot be equivalent of itself: " + meshObject + " vs. " + ArrayHelper.join( ", ", equivalents ));
                }
            }
        }

        MeshObjectIdentifier [] ret;
        if( left != null || right != null ) {
            ret = (MeshObjectIdentifier[]) Array.newInstance( meshObject.getClass(), 2 );
            ret[0] = left;
            ret[1] = right;
        } else {
            ret = null;
        }

        return ret;
    }

    /**
     * The singleton instance of this class.
     */
    public static final AMeshObjectEquivalenceSetComparator SINGLETON = new AMeshObjectEquivalenceSetComparator();
}
