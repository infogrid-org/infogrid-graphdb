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

package org.infogrid.model.traversal;

import org.infogrid.mesh.MeshObject;

/**
 * <p>This interface is supported by classes that know how to translate one or more
 * Strings (representing a TraversalSpecification, or several TraversalSpecifications) into the actual
 * TraversalSpecification(s). Many different implementations are possible.</p>
 *
 * <p>Note: there is no requirement that the same set of terms translate into the same
 * TraversalSpecification(s) when applied to different MeshObjects.</p>
 */
public interface TraversalTranslator
{
    /**
     * Translate the String form of a single TraversalSpecification into an
     * actual TraversalSpecification. The assumption here is that the traversal terms
     * describe a single TraversalSpecification.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerms the terms to be translated
     * @return the TraversalSpecification, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract TraversalSpecification translateTraversalSpecification(
            MeshObject startObject,
            String []  traversalTerms )
        throws
            TraversalTranslatorException;

    /**
     * Translate the String form of several TraversalSpecifications into the
     * actual TraversalSpecifications. The assumption here is that the traversal terms
     * describe several TraversalSpecifications.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerms the terms to be translated
     * @return the TraversalSpecifications, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract TraversalSpecification [] translateTraversalSpecifications(
            MeshObject startObject,
            String []  traversalTerms )
        throws
            TraversalTranslatorException;

    /**
     * Translate an actual TraversalSpecification into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversal the TraversalSpecification
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract String [] translateTraversalSpecification(
            MeshObject             startObject,
            TraversalSpecification traversal )
        throws
            TraversalTranslatorException;

    /**
     * Translate several actual TraversalSpecifications into String form. The assumption
     * here is that if it is performed at the same time, the String form may be more
     * compact than if done separately.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversals the TraversalSpecifications
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract String [] translateTraversalSpecifications(
            MeshObject                startObject,
            TraversalSpecification [] traversals )
        throws
            TraversalTranslatorException;

    /**
     * Translate the String form of a TraversalPath into an actual TraversalPath.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerms the terms to be translated
     * @return the TraversalPath, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract TraversalPath translateTraversalPath(
            MeshObject  startObject,
            String []   traversalTerms )
        throws
            TraversalTranslatorException;

    /**
     * Translate an actual TraversalPath into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param path the TraversalPath
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public abstract String [] translateTraversalPath(
            MeshObject    startObject,
            TraversalPath path )
        throws
            TraversalTranslatorException;
}
