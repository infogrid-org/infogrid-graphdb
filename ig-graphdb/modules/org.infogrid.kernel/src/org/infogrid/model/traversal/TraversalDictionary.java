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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;

/**
 * <p>This interface is supported by classes that know how to translate a
 * a String (representing a TraversalSpecification) into the actual
 * TraversalSpecification. Many different implementations are possible.</p>
 *
 * <p>The same term may translate into different TraversalSpecifications when
 * applied to different MeshObjects.</p>
 */
public interface TraversalDictionary
{
    /**
     * Translate the String form of a TraversalSpecification into an actual TraversalSpecification.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerm the term to be translated
     * @return the TraversalSpecification, or null if not found.
     */
    public abstract TraversalSpecification translate(
            MeshObject startObject,
            String     traversalTerm );

    /**
     * Translate an actual TraversalSpecification into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversal the TraversalSpecification
     * @return the external form
     */
    public abstract String translate(
            MeshObject             startObject,
            TraversalSpecification traversal );
}
