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

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.AbstractMeshObjectSetFactory;
import org.infogrid.mesh.set.CompositeImmutableMeshObjectSet;
import org.infogrid.mesh.set.ImmutableMeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.OrderedImmutableMeshObjectSet;
import org.infogrid.mesh.set.OrderedImmutableTraversalPathSet;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.mesh.set.TraversalPathSorter;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.model.traversal.TraversalSpecification;
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
     * {@inheritDoc}
     */
    @Override
    public ImmutableMMeshObjectSet createImmutableMeshObjectSet(
            MeshObject [] members )
    {
        return (ImmutableMMeshObjectSet) super.createImmutableMeshObjectSet( members );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmutableMMeshObjectSet createImmutableMeshObjectSet(
            MeshObjectSet      input,
            MeshObjectSelector selector )
    {
        return (ImmutableMMeshObjectSet) super.createImmutableMeshObjectSet( input, selector );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet [] operands )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetUnification( operands );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetUnification( one, two );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObject    two )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetUnification( one, two );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet [] operands )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetIntersection( operands );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetIntersection( one, two );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObject    two )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetIntersection( one, two );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompositeImmutableMMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return (CompositeImmutableMMeshObjectSet) super.createImmutableMeshObjectSetMinus( one, two );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderedImmutableMMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter )
    {
        return (OrderedImmutableMMeshObjectSet) super.createOrderedImmutableMeshObjectSet( content, sorter );
    }

    /**
     * Factory method to create an empty MeshObjectSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * 
     * @return the empty MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet obtainEmptyImmutableMeshObjectSet()
    {
        if( theEmptyMeshObjectSet == null ) {
            theEmptyMeshObjectSet = new OrderedImmutableMMeshObjectSet( this, new MeshObject[0], 0 );
        }
        return theEmptyMeshObjectSet;
    }
    
    /**
     * Factory method to construct a MeshObjectSet with the single specified member.
     * Given that it has only one member, we might as well return an OrderedMeshObjectSet.
     *
     * @param member the content of the set
     * @return the created MeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createSingleMemberImmutableMeshObjectSet(
            MeshObject member )
    {
        return new OrderedImmutableMMeshObjectSet( this, new MeshObject[] { member }, 1 );
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
        return createOrderedImmutableMeshObjectSet( contentInOrder, (MeshObjectSelector) null );
    }

    /**
     * Factory method to create an OrderedMeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidatesInOrder the candidate content of the OrderedMeshObjectSet, in order
     * @param selector determines which candidates are included
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObject []      candidatesInOrder,
            MeshObjectSelector selector )
    {
        // check for duplicates first
        for( int i=0 ; i<candidatesInOrder.length ; ++i ) {
            if( candidatesInOrder[i] == null ) {
                throw new IllegalArgumentException( "Cannot add a null object to a MeshObjectSet" );
            }
            if( candidatesInOrder[i].getIsDead() ) {
                throw new IllegalArgumentException( "Cannot add a dead object to a MeshObjectSet: " + candidatesInOrder[i] );
            }
            for( int j=0 ; j<i ; ++j ) {
                if( candidatesInOrder[i] == candidatesInOrder[j] ) {
                    throw new IllegalArgumentException( "Cannot create a MeshObjectSet with duplicate members: " + candidatesInOrder[i] );
                }
            }
        }
        
        MeshObject [] content;
        
        if( selector != null ) {
            int count = 0;
            content = ArrayHelper.createArray( theComponentClass, candidatesInOrder.length );
            for( int i=0 ; i<candidatesInOrder.length ; ++i ) {
                if( selector.accepts( candidatesInOrder[i] )) {
                    content[count++] = candidatesInOrder[i];
                }
            }
            if( count < content.length ) {
                content = ArrayHelper.copyIntoNewArray( content, 0, count, theComponentClass );
            }
 
        } else {
            content = ArrayHelper.copyIntoNewArray( candidatesInOrder, theComponentClass );
        }
        
        OrderedImmutableMMeshObjectSet ret = new OrderedImmutableMMeshObjectSet( this, content, OrderedMeshObjectSet.UNLIMITED );
        
        return ret;
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet, in any order
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @return the created OrderedImmutableMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObject []    content,
            MeshObjectSorter sorter )
    {
        return new OrderedImmutableMMeshObjectSet(
                this,
                sorter.getOrderedInNew( content),
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
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification.
     *
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification )
    {
        MeshObjectSet ret = startObject.traverse( specification );

        if( ret.getFactory() != this ) {
            return (ImmutableMeshObjectSet) ret;
        } else {
            // happens if two MeshObjectSetFactories are in use
            return createImmutableMeshObjectSet( ret.getMeshObjects() );
        }
    }

    /**
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a TraversalSpecification.
     *
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ImmutableMMeshObjectSet createImmutableMeshObjectSet(
            MeshObjectSet          startSet,
            TraversalSpecification specification )
    {
        MeshObjectSet ret = startSet.traverse( specification );

        if( ret.getFactory() != this ) {
            return (ImmutableMMeshObjectSet) ret;
        } else {
            // happens if two MeshObjectSetFactories are in use
            return createImmutableMeshObjectSet( ret.getMeshObjects() );
        }
    }

    /**
     * Factory method to construct a ImmutableMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification, and repeating that process.
     *
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public TransitiveClosureImmutableMMeshObjectSet createTransitiveClosureImmutableMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification )
    {
        return new TransitiveClosureImmutableMMeshObjectSet( this, startObject, specification );
    }

    /**
     * Factory method to create an empty TraversalPathSet. This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * Given that it is empty, we might as well return an OrderedTraversalPathSet.
     *
     * @return the empty TraversalPathSet
     */
    public OrderedImmutableTraversalPathSet obtainEmptyImmutableTraversalPathSet()
    {
        if( theEmptyTraversalPathSet == null ) {
            theEmptyTraversalPathSet = new OrderedImmutableMTraversalPathSet( this, new TraversalPath[0], 0 );
        }
        return theEmptyTraversalPathSet;
    }

    /**
     * Factory method.
     *
     * @param singleMember the single member of the ImmutableMTraversalPathSet
     * @return return the created ImmutableMTraversalPathSet
     */
    public OrderedImmutableMTraversalPathSet createSingleMemberImmutableTraversalPathSet(
            TraversalPath singleMember )
    {
        return new OrderedImmutableMTraversalPathSet( this, new TraversalPath[] { singleMember }, 1 );
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
     * Factory method to create an ImmutableTraversalPathSet.
     * This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet. The TraversalSpecification is passed in.
     *
     * @param spec the traversed TraversalSpecification
     * @param set used to construct the content for the ImmutableTraversalPathSet
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            TraversalSpecification spec,
            MeshObjectSet          set )
    {
        if( theMeshBase != set.getMeshBase() ) {
            throw new IllegalArgumentException();
        }

        MeshObject    [] res     = set.getMeshObjects();
        TraversalPath [] content = new TraversalPath[ res.length ];
        for( int i=0 ; i<content.length ; ++i ) {
            content[i] = TraversalPath.create( spec, res[i] );
        }

        return new ImmutableMTraversalPathSet( this, content );
    }

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param start the MeshObject from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            MeshObject             start,
            TraversalSpecification spec )
    {
        return createImmutableTraversalPathSet( spec, start.traverse( spec ));
    }

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param startSet the MeshObjectSet from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            MeshObjectSet          startSet,
            TraversalSpecification spec )
    {
        return createImmutableTraversalPathSet( spec, startSet.traverse( spec ));
    }

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     *
     * @param startSet the TraversalPathSet from whose destination MeshObject we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created ImmutableTraversalPathSet
     */
    public ImmutableMTraversalPathSet createImmutableTraversalPathSet(
            TraversalPathSet       startSet,
            TraversalSpecification spec )
    {
        TraversalPath [] oldPaths = startSet.getTraversalPaths();
        MeshObjectSet [] newSets  = new MeshObjectSet[ oldPaths.length ];

        int count = 0;
        for( int i=0 ; i<oldPaths.length ; ++i ) {
            newSets[i] = oldPaths[i].getLastMeshObject().traverse( spec );

            count += newSets[i].size();
        }

        TraversalPath [] newPaths = new TraversalPath[ count ];
        int count2 = 0;
        for( int i=0 ; i<oldPaths.length ; ++i ) {
            for( int j=0 ; j<newSets[i].size() ; ++j ) {
                newPaths[ count2++ ] = TraversalPath.create( newPaths[i], spec, newSets[i].get( j ));
            }
        }
        return createImmutableTraversalPathSet( newPaths );
    }

    /**
     * Factory method to create an OrderedImmutableTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @return the created OrderedImmutableTraversalPathSet
     */
    public OrderedImmutableMTraversalPathSet createOrderedImmutableTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter )
    {
        return createOrderedImmutableTraversalPathSet( content, sorter, Integer.MAX_VALUE );
    }

    /**
     * Factory method to create an OrderedImmutableTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @param max the maximum number of TraversalPaths that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max TraversalPaths according to the sorter.
     * @return the created OrderedImmutableTraversalPathSet
     */
    public OrderedImmutableMTraversalPathSet createOrderedImmutableTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter,
            int                 max )
    {
        TraversalPath [] sorted = sorter.getOrderedInNew( content.getTraversalPaths() );

        return new OrderedImmutableMTraversalPathSet( this, sorted, max );
    }

    /**
     * Buffer for an empty MeshObjectSet.
     */
    protected OrderedImmutableMeshObjectSet theEmptyMeshObjectSet;

    /**
     * Buffer for an empty TraversalPathSet.
     */
    protected OrderedImmutableTraversalPathSet theEmptyTraversalPathSet;
}
