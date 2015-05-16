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

package org.infogrid.mesh.set.m;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.ImmutableMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.ArrayHelper;

/**
  * A simple implementation of an immutable MeshObjectSet that keeps its content in
  * memory.
  */
public class ImmutableMMeshObjectSet
        extends
            AbstractMMeshObjectSet
        implements
            ImmutableMeshObjectSet
{
    /**
     * Constructor to be used by subclasses only.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param content the content of the MeshObjectSet
     */
    protected ImmutableMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObject []        content )
    {
        super( factory );
        
        setInitialContent( content );
    }

    /**
     * Returns a MeshObjectSet obtained by traversing all the MeshObjects
     * contained in this ImmutableMMeshObjectSet for the given RoleType.
     * 
     * @param role the RoleType to traverse
     * @return the set of reached MeshObjects
     */
    public MeshObjectSet traverse(
            RoleType role )
    {
        return traverse( role, true );
    }

    /**
     * Returns a MeshObjectSet obtained by traversing all the MeshObjects
     * contained in this ImmutableMMeshObjectSet for the given RoleType.
     * 
     * @param role the RoleType to traverse
     * @param considerEquivalents if true, consider equivalents for the traversal
     * @return the set of reached MeshObjects
     */
    public MeshObjectSet traverse(
            RoleType role,
            boolean  considerEquivalents )
    {
        ArrayList<MeshObject> almostRet = new ArrayList<MeshObject>( currentContent.length * 3 ); // fudge
        if( considerEquivalents ) {

            for( int i = 0 ; i < currentContent.length ; ++i ) {
                MeshObject [] equivalents = currentContent[i].getEquivalents().getMeshObjects();

                for( int j = 0 ; j < equivalents.length ; ++j ) {
                    MeshObject [] found = equivalents[j].traverse( role ).getMeshObjects();

                    for( int k=0 ; k<found.length ; ++k ) {
                        if( ! almostRet.contains( found[k] )) {
                            almostRet.add( found[k] );
                        }
                    }
                }
            }

        } else {

            for( int i = 0 ; i < currentContent.length ; ++i ) {
                MeshObject [] found = currentContent[i].traverse( role ).getMeshObjects();

                for( int j=0 ; j<found.length ; ++j ) {
                    if( ! almostRet.contains( found[j] )) {
                        almostRet.add( found[j] );
                    }
                }
            }            
        }
        MeshObjectSet ret = theFactory.createImmutableMeshObjectSet( ArrayHelper.copyIntoNewArray( almostRet, MeshObject.class ));
        return ret;
    }

    /**
     * Returns a MeshObjectSet obtained by traversing all the MeshObjects
     * contained in this ImmutableMMeshObjectSet for the given TraversalSpecification.
     * 
     * @param theSpec the TraversalSpecification to traverse
     * @return the set of reached MeshObjects
     */
    @Override
    public MeshObjectSet traverse(
            TraversalSpecification theSpec,
            boolean                considerEquivalents )
    {
        if( theSpec instanceof RoleType ) {
            return traverse( (RoleType) theSpec, considerEquivalents );

        } else {
            return theSpec.traverse( this, considerEquivalents );
        }
    }
}
