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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set.m;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.ImmutableMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.ArrayHelper;

/**
 * <p>This is an ImmutableMeshObjectSet whose elements are obtained by repeatedly
 * traversing a TraversalSpecification. All MeshObjects reached by that repeated
 * traversal are members of this set. The start MeshObject is also a member of this set.</p>
 *
 * <p>To instantiate, use factory methods.</p>
 *
 * <p>Note: This may potentially forward duplicate PropertyChangeEvents.</p>
 */
public class TransitiveClosureImmutableMMeshObjectSet
        extends
            AbstractMMeshObjectSet
        implements
            ImmutableMeshObjectSet
{
    /**
     * Constructor.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param root the root MeshObject from where we attempt to traverse
     * @param spec the TraversalSpecification to use
     */
    protected TransitiveClosureImmutableMMeshObjectSet(
            MeshObjectSetFactory   factory,
            MeshObject             root,
            TraversalSpecification spec )
    {
        super( factory );

        ArrayList<MeshObject> found = determineContent( root, spec );

        MeshObject [] content = ArrayHelper.copyIntoNewArray( found, MeshObject.class );
        setInitialContent( content );
    }

    /**
     * Construct the content of the set.
     *
     * @param root the root MeshObject from where we attempt to traverse
     * @param spec the TraversalSpecification to use
     * @return the content
     */
    protected ArrayList<MeshObject> determineContent(
            MeshObject             root,
            TraversalSpecification spec )
    {
        ArrayList<MeshObject> found     = new ArrayList<MeshObject>();
        ArrayList<MeshObject> toProcess = new ArrayList<MeshObject>();
        found.add( root );
        toProcess.add( root );

        while( !toProcess.isEmpty() ) {
            // remove from the end, that makes things more efficient
            MeshObject current = toProcess.remove( toProcess.size()-1 );

            MeshObject [] newlyFound = current.traverse( spec ).getMeshObjects();
            for( int i=0 ; i<newlyFound.length ; ++i ) {
                if( !found.contains( newlyFound[i] )) {
                    found.add( newlyFound[i] );
                    toProcess.add( newlyFound[i] );
                }
            }
        }
        return found;
    }
}
