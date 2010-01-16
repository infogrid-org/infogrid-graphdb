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
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.AbstractMeshObjectSetFactory;
import org.infogrid.mesh.set.CompositeImmutableMeshObjectSet;
import org.infogrid.mesh.set.ImmutableMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.OrderedImmutableMeshObjectSet;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayHelper;

/**
 * An MeshObjectSetFactory that creates in-memory, immutable MeshObjectSets.
 * The method setMeshBase() must be called after the constructor.
 */
public class ImmutableMMeshObjectSetFactory
        extends
            AbstractMeshObjectSetFactory
{
    /**
     * Factory method for the factory itself.
     * 
     * @param componentClass           the Class to use to allocate arrays of MeshObjects
     * @param componentIdentifierClass the Class to use to allocate arrays of MeshObjectIdentifiers
     * @return the created ImmutableMMeshObjectSetFactory
     */
    public static ImmutableMMeshObjectSetFactory create(
            Class<? extends MeshObject>           componentClass,
            Class<? extends MeshObjectIdentifier> componentIdentifierClass )
    {
        return new ImmutableMMeshObjectSetFactory( componentClass, componentIdentifierClass );
    }

    /**
     * Constructor.
     * 
     * @param componentClass           the Class to use to allocate arrays of MeshObjects
     * @param componentIdentifierClass the Class to use to allocate arrays of MeshObjectIdentifiers
     */
    protected ImmutableMMeshObjectSetFactory(
            Class<? extends MeshObject>           componentClass,
            Class<? extends MeshObjectIdentifier> componentIdentifierClass )
    {
        super( componentClass, componentIdentifierClass );
    }

    /**
     * Factory method to create an empty MeshObjectSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * 
     * @return the empty MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet obtainEmptyImmutableMeshObjectSet()
    {
        if( theEmptySet == null ) {
            theEmptySet = new OrderedImmutableMMeshObjectSet( this, new MeshObject[0], 0 );
        }
        return theEmptySet;
    }
    
    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     * @throws IllegalArgumentException thrown if the array of MeshObjects contained dead objects, duplicates, null pointers etc.
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject []      candidates,
            MeshObjectSelector selector )
    {
        // check for duplicates first
        for( int i=0 ; i<candidates.length ; ++i ) {
            if( candidates[i] == null ) {
                throw new IllegalArgumentException( "Cannot add a null object to a MeshObjectSet" );
            }
            if( candidates[i].getIsDead() ) {
                throw new IllegalArgumentException( "Cannot add a dead object to a MeshObjectSet: " + candidates[i] );
            }
            for( int j=0 ; j<i ; ++j ) {
                if( candidates[i] == candidates[j] ) {
                    throw new IllegalArgumentException( "Cannot create a MeshObjectSet with duplicate members: " + candidates[i] );
                }
            }
        }
        
        MeshObject [] content;
        
        if( selector != null ) {
            int count = 0;
            content = ArrayHelper.createArray( theComponentClass, candidates.length );
            for( int i=0 ; i<candidates.length ; ++i ) {
                if( selector.accepts( candidates[i] )) {
                    content[count++] = candidates[i];
                }
            }
            if( count < content.length ) {
                content = ArrayHelper.copyIntoNewArray( content, 0, count, theComponentClass );
            }
 
        } else {
            content = ArrayHelper.copyIntoNewArray( candidates, theComponentClass );
        }
        
        ImmutableMeshObjectSet ret = new ImmutableMMeshObjectSet( this, content );
        
        return ret;
    }
    
    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets, as long as they are selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet []   operands,
            MeshObjectSelector selector )
    {
        if( operands.length < 1 ) {
            throw new IllegalArgumentException();
        }
        for( int i=0 ; i<operands.length ; ++i ) {
            if( theMeshBase != operands[i].getMeshBase() ) {
                throw new IllegalArgumentException( "cannot create unification of MeshObjectSets in different MeshBases" );
            }
        }

        MeshObject [] content = unify( operands, selector );
    
        return new CompositeImmutableMMeshObjectSet.Unification( this, content, operands );        
    }

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector )
    {
        if( operands.length < 1 ) {
            throw new IllegalArgumentException();
        }
        for( int i=0 ; i<operands.length ; ++i ) {
            if( theMeshBase != operands[i].getMeshBase() ) {
                throw new IllegalArgumentException( "cannot create intersection of MeshObjectSets in different MeshBases" );
            }
        }

        MeshObject [] content = intersect( operands, selector );
    
        return new CompositeImmutableMMeshObjectSet.Intersection( this, content, operands );
    }
    
    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet, as long
     * as they are also selected by the MeshObjectSelector.
     * 
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet      one,
            MeshObjectSet      two,
            MeshObjectSelector selector )
    {
        if( theMeshBase != one.getMeshBase() ) {
            throw new IllegalArgumentException( "cannot calculate SimpleMeshObjectSet.minus of MeshObjects in different MeshBases" );
        }
        if( theMeshBase != two.getMeshBase() ) {
            throw new IllegalArgumentException( "cannot calculate SimpleMeshObjectSet.minus of MeshObjects in different MeshBases" );
        }
        
        MeshObject [] oneContent = one.getMeshObjects();
        MeshObject [] result     = ArrayHelper.createArray( theComponentClass, oneContent.length );

        int count = 0;
        for( int i=0 ; i<oneContent.length ; ++i ) {
            if( !two.contains( oneContent[i] )) {
                if( selector == null || selector.accepts( oneContent[i] )) {
                    result[ count++ ] = oneContent[i];
                }
            }
        }
        if( count < result.length ) {
            result = ArrayHelper.copyIntoNewArray( result, 0, count, theComponentClass );
        }
        return new CompositeImmutableMMeshObjectSet.Minus( this, result, new MeshObjectSet[] { one, two } );        
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     *
     * @param contentInOrder the content of the OrderedMeshObjectSet, in order
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObject [] contentInOrder )
    {
        return new OrderedImmutableMMeshObjectSet(
                this,
                contentInOrder,
                OrderedMeshObjectSet.UNLIMITED );
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter,
            int              max )
    {
        // I was thinking of sorting only when the content is actually requested, but
        // it's unlikely the user will request an ordered set and then not use it, so that

        return new OrderedImmutableMMeshObjectSet(
                this,
                sorter.getOrderedInNew( content.getMeshObjects() ),
                max );
    }

    /**
     * Factory method.
     *
     * @param content the content for the ImmutableMTraversalPathSet
     * @return the created ImmutableMTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            TraversalPath [] content )
    {
        return new ImmutableMTraversalPathSet( this, content );
    }

    /**
     * Factory method. This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet.
     *
     * @param set used to construct the content for the ImmutableMTraversalPathSet
     * @return the created ImmutableMTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            MeshObjectSet set )
    {
        if( theMeshBase != set.getMeshBase() ) {
            throw new IllegalArgumentException();
        }

        MeshObject    [] res     = set.getMeshObjects();
        TraversalPath [] content = new TraversalPath[ res.length ];
        for( int i=0 ; i<content.length ; ++i ) {
            content[i] = TraversalPath.create( null, res[i] );
        }

        return new ImmutableMTraversalPathSet( this, content );
    }

    /**
     * Buffer for an empty MeshObjectSet.
     */
    protected OrderedImmutableMeshObjectSet theEmptySet;
}
