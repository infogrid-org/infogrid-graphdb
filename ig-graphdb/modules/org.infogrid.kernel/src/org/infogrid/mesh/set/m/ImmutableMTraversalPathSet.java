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

package org.infogrid.mesh.set.m;

import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.ImmutableTraversalPathSet;
import org.infogrid.model.traversal.TraversalPath;

/**
 * A simple, immutable TraversalPathSet implementation.
 */
public class ImmutableMTraversalPathSet
        extends
            AbstractMTraversalPathSet
        implements
            ImmutableTraversalPathSet
{
    /**
     * Private constructor, use factory method.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     * @param content the content for the ImmutableMTraversalPathSet
     */
    protected ImmutableMTraversalPathSet(
            MeshObjectSetFactory factory,
            TraversalPath []     content )
    {
        super( factory );

        super.setInitialContent( content );
    }
}
