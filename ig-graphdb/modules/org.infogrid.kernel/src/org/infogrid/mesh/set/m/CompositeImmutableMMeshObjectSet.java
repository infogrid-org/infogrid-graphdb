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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.CompositeImmutableMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSet;

import org.infogrid.mesh.set.MeshObjectSetFactory;

/**
 * <p>This MeshObjectSet is calculated as a composition of other
 *    MeshObjectSets. The type of composition can be specified by
 *    choosing an appropriate factory method.</p>
 *
 * <p>CompositeImmutableMeshObjectSets are immutable, even if some of
 *    their arguments may be not.</p>
 */
public abstract class CompositeImmutableMMeshObjectSet
        extends
            ImmutableMMeshObjectSet
        implements
            CompositeImmutableMeshObjectSet
{
    /**
     * Private constructor, use factory methods.
     *
     * @param factory the MeshObjectSetFactory that created this MeshObjectSet
     * @param content the content of this set
     * @param operands the operands that were used to construct the content
     */
    protected CompositeImmutableMMeshObjectSet(
            MeshObjectSetFactory factory,
            MeshObject    []     content,
            MeshObjectSet []     operands )
    {
        super( factory, content );

        theOperands = operands;
    }

    /**
     * Obtain the operands that were used to construct this set.
     *
     * @return the operands that were used to construct this set
     */
    public MeshObjectSet [] getOperands()
    {
        return theOperands;
    }

    /**
     * The operands of this set.
     */
    protected MeshObjectSet [] theOperands;

    /**
     * An immutable set that is constructed as a unification of the content of other sets.
     */
    public static class Unification
            extends
                CompositeImmutableMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param content the content of this set
         * @param operands the operands that were used to construct the content
         */
        protected Unification(
                MeshObjectSetFactory factory,
                MeshObject    []     content,
                MeshObjectSet []     operands )
        {
            super( factory, content, operands );
        }
    }

    /**
     * An immutable set that is constructed as an intersection of the content of other sets.
     */
    public static class Intersection
            extends
                CompositeImmutableMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param content the content of this set
         * @param operands the operands that were used to construct the content
         */
        protected Intersection(
                MeshObjectSetFactory factory,
                MeshObject    []     content,
                MeshObjectSet []     operands )
        {
            super( factory, content, operands );
        }
    }

    /**
     * An immutable set that is constructed as a set subtraction.
     */
    public static class Minus
            extends
                CompositeImmutableMMeshObjectSet
    {
        /**
         * Private constructor, use factory methods.
         *
         * @param factory the MeshObjectSetFactory that created this MeshObjectSet
         * @param content the content of this set
         * @param operands the operands that were used to construct the content
         */
        protected Minus(
                MeshObjectSetFactory factory,
                MeshObject    []     content,
                MeshObjectSet []     operands )
        {
            super( factory, content, operands );
        }
    }
}
