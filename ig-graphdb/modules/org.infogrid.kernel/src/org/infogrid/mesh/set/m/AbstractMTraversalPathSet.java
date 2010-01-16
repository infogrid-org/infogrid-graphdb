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
import org.infogrid.mesh.set.AbstractTraversalPathSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.WrongMeshBaseException;
import org.infogrid.model.traversal.TraversalPath;

/**
 * Factors out common functionality of in-memory TraversalPathSet implementations.
 */
public abstract class AbstractMTraversalPathSet
        extends
            AbstractTraversalPathSet
{
    /**
     * Private constructor for subtypes only.
     *
     * @param factory the MeshObjectSetFactory that created this TraversalPathSet
     */
    protected AbstractMTraversalPathSet(
            MeshObjectSetFactory factory )
    {
        super( factory );
    }

    /**
     * Set the initial content of the set, for subclasses only.
     *
     * @param initialContent the initial content of the set
     */
    protected void setInitialContent(
            TraversalPath [] initialContent )
    {
        MeshBase base = theFactory.getMeshBase();

        for( TraversalPath current : initialContent ) {
            // do something smart even if setMeshBase has not been called yet
            if( base == null ) {
                base = current.getMeshBase();
            } else if( base != current.getMeshBase() ) {
                throw new WrongMeshBaseException( base, current.getMeshBase() );
            }
        }
        currentContent = initialContent;
    }

    /**
     * Obtain the content of this set.
     *
     * @return the content of this set
     */
    public final TraversalPath [] getTraversalPaths()
    {
        return currentContent;
    }

    /**
     * The content of this set.
     */
    protected TraversalPath [] currentContent;
}
