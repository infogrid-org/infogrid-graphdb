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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.ArrayHelper;

/**
 * Factors out common behaviors of many MeshObjectSetFactories.
 */
public abstract class AbstractMeshObjectSetFactory
        implements
            MeshObjectSetFactory
{
    /**
     * Constructor for subclasses only.
     * 
     * @param componentClass           the Class to use to allocate arrays of MeshObjects
     * @param componentIdentifierClass the Class to use to allocate arrays of MeshObjectIdentifiers
     */
    protected AbstractMeshObjectSetFactory(
            Class<? extends MeshObject>           componentClass,
            Class<? extends MeshObjectIdentifier> componentIdentifierClass )
    {
        theComponentClass           = componentClass;
        theComponentIdentifierClass = componentIdentifierClass;
        theMeshBase                 = null; // not initialized
    }
    
    /**
     * Set the MeshBase on whose behalf this factory works.
     * 
     * @param newValue the new value
     */
    public void setMeshBase(
            MeshBase newValue )
    {
        if( theMeshBase != null ) {
            throw new IllegalStateException( "Already have MeshBase, cannot reset after it has been set" );
        }
        theMeshBase = newValue;
    }

    /**
     * Factory method to construct a MeshObjectSet with the specified members.
     * 
     * @param members the content of the set
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObject [] members )
    {
        return createImmutableMeshObjectSet( members, null );
    }

    /**
     * Factory method to construct a MeshObjectSet with the members of another
     * MeshObjectSet, as long as they are selected by the MeshObjectSelector.
     * 
     * @param input the input MeshObjectSet
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ImmutableMeshObjectSet createImmutableMeshObjectSet(
            MeshObjectSet      input,
            MeshObjectSelector selector )
    {
        MeshObject [] inputContent = input.getMeshObjects();
        MeshObject [] content;
        if( selector != null ) {
            content = ArrayHelper.createArray( theComponentClass, inputContent.length );
            int count = 0;
            for( int i=0 ; i<inputContent.length ; ++i ) {
                if( selector.accepts( inputContent[i] )) {
                    content[ count++ ] = inputContent[i];
                }
            }
            if( count < content.length ) {
                content = ArrayHelper.copyIntoNewArray( content, 0, count, theComponentClass );
            }
        } else {
            content = inputContent;
        }

        ImmutableMeshObjectSet ret = createImmutableMeshObjectSet( content );
        return ret;
    }

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet [] operands )
    {
        return createImmutableMeshObjectSetUnification( operands, null );
    }

    /**
     * Convenience factory method to construct a unification of two MeshObjectSets.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createImmutableMeshObjectSetUnification( new MeshObjectSet[] { one, two } );
    }

    /**
     * Convenience factory method to construct a unification of a MeshObjectSet and
     * a second single-element MeshObjectSet.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObject    two )
    {
        // FIXME this can be optimized I guess
        return createImmutableMeshObjectSetUnification( new MeshObjectSet[] { one, createSingleMemberImmutableMeshObjectSet( two ) } );
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects that are contained
     * in all of the provided MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet [] operands )
    {
        return createImmutableMeshObjectSetIntersection( operands, null );
    }

    /**
     * Convenience factory method to construct an intersection of two MeshObjectSets.
     * 
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createImmutableMeshObjectSetIntersection( new MeshObjectSet[] { one, two } );        
    }

    /**
     * Convenience factory method to construct an intersection of two MeshObjectSets.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObject    two )
    {
        return createImmutableMeshObjectSetIntersection(
                one,
                createSingleMemberImmutableMeshObjectSet( two ));
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     * 
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createImmutableMeshObjectSetMinus( one, two, null );
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     *
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @return the created CompositeImmutableMeshObjectSet
     */
    public CompositeImmutableMeshObjectSet createImmutableMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObject    two )
    {
        return createImmutableMeshObjectSetMinus(
                one,
                createSingleMemberImmutableMeshObjectSet( two ));
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     */
    public OrderedImmutableMeshObjectSet createOrderedImmutableMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter )
    {
        return createOrderedImmutableMeshObjectSet( content, sorter, OrderedMeshObjectSet.UNLIMITED );
    }

    /**
     * Obtain the MeshBase on which this MeshObjectFactory operates.
     * 
     * @return the MeshBase
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }
    
    /**
     * Helper method for implementation and subclasses: create array with the merged
     * content contained in all the sets and without duplicates.
     *
     * @param inputSets the set of input MeshObjectSets to be unified
     * @param selector selector for the resulting MeshObjects
     * @return the MeshObjects that are contained in the inputSets, but without duplicates
     */
    protected MeshObject [] unify(
            MeshObjectSet []   inputSets,
            MeshObjectSelector selector )
    {
        int count = 0;
        for( int i=0 ; i<inputSets.length ; ++i ) {
            count += inputSets[i].size();
        }

        MeshObject [] objs = ArrayHelper.createArray( theComponentClass, count );

        count = 0;
        for( int i=0 ; i<inputSets.length ; ++i ) {
            MeshObject [] candidates = inputSets[i].getMeshObjects();

            for( int j=0 ; j<candidates.length ; ++j ) {
                if( !ArrayHelper.isIn( candidates[j], objs, 0, count, false )) {
                    if( selector == null || selector.accepts( candidates[j] )) {
                        objs[ count++ ] = candidates[j];
                    }
                }
            }
        }
        if( count < objs.length ) {
            objs = ArrayHelper.copyIntoNewArray( objs, 0, count, MeshObject.class );
        }

        return objs;
    }

    /**
     * Helper method for implementation and subclasses: create array with the content
     * contained in everyone of the sets and without duplicates.
     *
     * @param inputSets the set of input MeshObjectSets to be intersected
     * @param selector selector for the resulting MeshObjects
     * @return the MeshObjects that are contained in all of the inputSets
     */
    protected MeshObject [] intersect(
            MeshObjectSet []   inputSets,
            MeshObjectSelector selector )
    {
        MeshObject [] ret = ArrayHelper.copyIntoNewArray(
                inputSets[0].getMeshObjects(),
                theComponentClass ); // shorten later, this is max

        int takenOut = 0;
        for( int i=1; i<inputSets.length ; ++i ) {
            MeshObject [] thisContent = inputSets[i].getMeshObjects();
            for( int j=0 ; j<ret.length ; ++j ) {
                MeshObject testObject = ret[j];
                if( testObject == null ) {
                    continue; // was removed previously
                }
                if( !ArrayHelper.isIn( testObject, thisContent, false )) {
                    if( selector == null || !selector.accepts( testObject )) { // note this is !accepts
                        ret[j] = null;
                        ++takenOut;
                    }
                }
            }
        }
        if( takenOut > 0 ) {
            MeshObject [] old = ret;

            ret = ArrayHelper.createArray( theComponentClass, old.length - takenOut );

            for( int i=0, j=0 ; i<old.length ; ++i ) {
                if( old[i] != null ) {
                    ret[j++] = old[i];
                }
            }
        }
        return ret;
    }

    /**
     * Convenience method to return an array of MeshObjects as an
     * array of the canonical Identifiers of the member MeshObjects.
     *
     * @param array the MeshObjects 
     * @return the array of IdentifierValues representing the Identifiers
     */
    public MeshObjectIdentifier[] asIdentifiers(
            MeshObject [] array )
    {
        MeshObjectIdentifier [] ret = ArrayHelper.createArray( theComponentIdentifierClass, array.length );
        for( int i=0 ; i<ret.length ; ++i ) {
            ret[i] = array[i].getIdentifier();
        }
        return ret;
    }
    
    /**
     * The MeshBase to which this MeshObjectSetFactory belongs.
     */
    protected MeshBase theMeshBase;
    
    /**
     * The Class to use to allocate arrays of MeshObject.
     */
    protected Class<? extends MeshObject> theComponentClass;
    
    /**
     * The Class to use to allocate arrays of MeshObjectIdentifier.
     */
    protected Class<? extends MeshObjectIdentifier> theComponentIdentifierClass;
}
