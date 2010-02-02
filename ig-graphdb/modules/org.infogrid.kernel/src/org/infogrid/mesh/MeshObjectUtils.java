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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh;

import org.infogrid.util.ArrayHelper;

/**
 * Collection of utility methods for MeshObjects.
 */
public abstract class MeshObjectUtils
{
    /**
     * Private constructor, this class cannot be instantiated.
     */
    private MeshObjectUtils()
    {
        // noop
    }

    /**
     * Construct an array of MeshObjectIdentifiers from an array of MeshObjects.
     *
     * @param objs the MeshObjects
     * @return the MeshObjectIdentifiers of the MeshObjects
     */
    public static MeshObjectIdentifier [] meshObjectIdentifiers(
            MeshObject [] objs )
    {
        if( objs == null ) {
            return null;
        }
        MeshObjectIdentifier [] ret = new MeshObjectIdentifier[ objs.length ];
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = objs[i].getIdentifier();
        }
        return ret;
    }

//    /**
//     * Determine the intersection of two arrays of MeshObjectIdentifiers. This is useful, for example,
//     * when attempting to find the set of MeshObjects that are neighbors of two MeshObjects.
//     *
//     * @param firstSet the first set
//     * @param secondSet the second set
//     * @return the intersection
//     */
//    public static MeshObjectIdentifier [] intersectMeshObjectIdentifiers(
//            MeshObjectIdentifier [] firstSet,
//            MeshObjectIdentifier [] secondSet )
//    {
//        return intersectMeshObjectIdentifiers(
//                new MeshObjectIdentifier[][] {
//                        firstSet,
//                        secondSet
//                });
//    }
//
//    /**
//     * Determine the intersection of N arrays of MeshObjectIdentifiers. This is useful, for example,
//     * when attempting to find the set of MeshObjects that are neighbors of N MeshObjects.
//     *
//     * @param sets the set of MeshObjectIdentifier sets
//     * @return the intersection
//     */
//    public static MeshObjectIdentifier [] intersectMeshObjectIdentifiers(
//            MeshObjectIdentifier [][] sets )
//    {
//        return intersectMeshObjectIdentifiers( sets, MeshObjectIdentifier.class );
//    }
//
//    /**
//     * Determine the intersection of N arrays of MeshObjectIdentifiers. This is useful, for example,
//     * when attempting to find the set of MeshObjects that are neighbors of N MeshObjects.
//     *
//     * @param sets the set of MeshObjectIdentifier sets
//     * @param arrayComponentType subtype of MeshObjectIdentifier
//     * @return the intersection
//     * @param <T> subtype of MeshObjectIdentifier
//     */
//    public static <T extends MeshObjectIdentifier> T [] intersectMeshObjectIdentifiers(
//            T [][]   sets,
//            Class<T> arrayComponentType )
//    {
//        // find the smallest set, then eliminate
//        int smallest = 0;
//        int max      = Integer.MAX_VALUE;
//        for( int i=0 ; i<sets.length ; ++i ) {
//            if( sets[i].length < max ) {
//                max = sets[i].length;
//                smallest = i;
//            }
//        }
//
//        T [] almost = ArrayHelper.copyIntoNewArray( sets[smallest], arrayComponentType );
//        int count = max;
//        for( int i=0 ; i<max ; ++i ) {
//            for( int j=0 ; j<sets.length ; ++j ) {
//                if( j == smallest ) {
//                    continue;
//                }
//                if( !ArrayHelper.isIn( almost[i], sets[j], true )) {
//                    almost[i] = null;
//                    --max;
//                    break;
//                }
//            }
//        }
//        T [] ret;
//        if( count < max ) {
//            ret = ArrayHelper.createArray( arrayComponentType, count );
//            for( int i=max-1 ; i>=0 ; --i ) {
//                if( almost[i] != null ) {
//                    ret[--count] = almost[i];
//                }
//            }
//        } else {
//            ret = almost;
//        }
//
//        return ret;
//    }
}
