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

package org.infogrid.mesh.set.active.m;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.mesh.set.MeshObjectSorter;
import org.infogrid.mesh.set.OrderedMeshObjectSet;
import org.infogrid.mesh.set.TraversalPathSet;
import org.infogrid.mesh.set.TraversalPathSorter;
import org.infogrid.mesh.set.active.ActiveMeshObjectSet;
import org.infogrid.mesh.set.active.ActiveMeshObjectSetFactory;
import org.infogrid.mesh.set.active.ActiveTraversalPathSet;
import org.infogrid.mesh.set.active.OrderedActiveMeshObjectSet;
import org.infogrid.mesh.set.active.OrderedActiveTraversalPathSet;
import org.infogrid.mesh.set.active.TraversalActiveMeshObjectSet;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.traversal.AllNeighborsTraversalSpecification;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * A factory for in-memory ActiveMeshObjectSets.
 */
public class ActiveMMeshObjectSetFactory
        extends
            ImmutableMMeshObjectSetFactory
        implements
            ActiveMeshObjectSetFactory
{
    private static final Log log = Log.getLogInstance( ActiveMMeshObjectSetFactory.class ); // our own, private logger

    /**
     * Factory method for the factory itself.
     * 
     * @param componentClass           the Class to use to allocate arrays of MeshObjects
     * @param componentIdentifierClass the Class to use to allocate arrays of MeshObjectIdentifiers
     * @return created ActiveMMeshObjectSetFactory
     */
    public static ActiveMMeshObjectSetFactory create(
            Class<? extends MeshObject>           componentClass,
            Class<? extends MeshObjectIdentifier> componentIdentifierClass )
    {
        return new ActiveMMeshObjectSetFactory( componentClass, componentIdentifierClass );
    }

    /**
     * Constructor, for factory method and subclasses only.
     * 
     * @param componentClass           the Class to use to allocate arrays of MeshObjects
     * @param componentIdentifierClass the Class to use to allocate arrays of MeshObjectIdentifiers
     */
    protected ActiveMMeshObjectSetFactory(
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
    public OrderedActiveMMeshObjectSet obtainEmptyConstantActiveMeshObjectSet()
    {
        if( theEmptyActiveSet == null ) {
            theEmptyActiveSet = new OrderedConstantActiveMMeshObjectSet( this, new MeshObject[0], 0 );
        }
        return theEmptyActiveSet;
    }
    
    /**
     * Factory method to create a MeshObjectSet with a single specified member.
     * This method may return
     * the same instance every time it is invoked, but is not required to do so.
     * Given that it has only one member, it might as well return an ordered ActiveMeshObjectSet.
     *
     * @param member the content of the set
     * @return the created MeshObjectSet
     */
    public OrderedActiveMMeshObjectSet obtainSingleMemberConstantActiveMeshObjectSet(
            MeshObject member )
    {
        return new OrderedConstantActiveMMeshObjectSet( this, new MeshObject[] { member }, 1 );
    }

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @return the created MeshObjectSet
     */
    public ActiveMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObject []      candidates )
    {
        return createConstantActiveMeshObjectSet( candidates, null );
    }

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     * 
     * @param candidates the candidate members of the set
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ActiveMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObject []      candidates,
            MeshObjectSelector selector )
    {
        ActiveMeshObjectSet ret;
        if( selector != null ) {
            MeshObject [] content = new MeshObject[ candidates.length ];
            int count = 0;
            for( int i=0 ; i<candidates.length ; ++i ) {
                if( selector.accepts( candidates[i] )) {
                    content[count++] = candidates[i];
                }
            }
            if( count < content.length ) {
                content = ArrayHelper.copyIntoNewArray( content, 0, count, MeshObject.class );
            }
            ret = new ConstantActiveMMeshObjectSet( this, content );

        } else {
            ret = new ConstantActiveMMeshObjectSet( this, candidates );
        }
        return ret;        
    }

    /**
     * Factory method to construct a MeshObjectSet with the specified members, as long
     * as they are selected by the MeshObjectSelector.
     *
     * @param input the input MeshObjectSet
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public ConstantActiveMMeshObjectSet createConstantActiveMeshObjectSet(
            MeshObjectSet      input,
            MeshObjectSelector selector )
    {
        MeshObject [] content = new MeshObject[ input.size() ];
        int count = 0;
        for( int i=0 ; i<input.size() ; ++i ) {
            if( selector.accepts( input.get( i ) )) {
                content[count++] = input.get( i );
            }
        }
        if( count < content.length ) {
            content = ArrayHelper.copyIntoNewArray( content, 0, count, MeshObject.class );
        }

        return new ConstantActiveMMeshObjectSet( this, content );
    }

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet [] operands )
    {
        return createActiveMeshObjectSetUnification( operands, null );
    }

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets, as long as they are selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet []   operands,
            MeshObjectSelector selector )
    {
        if( operands.length < 1 ) {
            throw new IllegalArgumentException();
        }

        boolean  foundActive = false;
        // mb may be null
        for( int i=0 ; i<operands.length ; ++i ) {
            if( theMeshBase != operands[i].getMeshBase() ) {
                throw new IllegalArgumentException( "createActiveUnification cannot be called with MeshObjects in different MeshBases");
            }
            if( operands[i] instanceof ActiveMeshObjectSet && !( operands[i] instanceof ConstantActiveMMeshObjectSet )) {
                foundActive = true;
                break;
            }
        }
        if( foundActive ) {
            CompositeActiveMMeshObjectSet ret = new CompositeActiveMMeshObjectSet.Unification( this, operands, selector );
            return ret;

        } else {
            throw new IllegalArgumentException( "at least one set must be active" );
        }        
    }
    
    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     *
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createActiveMeshObjectSetUnification(
                new MeshObjectSet[] {
                        one,
                        two
                });
    }

    /**
     * Factory method to construct a MeshObjectSet with all the members of the provided
     * MeshObjectSets.
     *
     * @param one the first set to unify
     * @param two the second set to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetUnification(
            MeshObjectSet one,
            MeshObject    two )
    {
        return createActiveMeshObjectSetUnification(
                new MeshObjectSet[] {
                        one,
                        createSingleMemberImmutableMeshObjectSet( two )
                });
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects that are contained
     * in all of the provided MeshObjectSets.
     * 
     * @param operands the sets to unify
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet [] operands )
    {
        return createActiveMeshObjectSetIntersection( operands, null );
    }

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     * 
     * @param operands the sets to unify
     * @param selector determines which candidates are included
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet []   operands,
            MeshObjectSelector selector )
    {
        if( operands.length < 1 ) {
            throw new IllegalArgumentException();
        }

        boolean  foundActive = false;
        // mb may be null
        for( int i=0 ; i<operands.length ; ++i ) {
            if( theMeshBase != operands[i].getMeshBase() ) {
                throw new IllegalArgumentException( "createActiveIntersection cannot be called with MeshObjects in different MeshBases");
            }
            if( operands[i] instanceof ActiveMeshObjectSet && !( operands[i] instanceof ConstantActiveMMeshObjectSet )) {
                foundActive = true;
                break;
            }
        }

        if( foundActive ) {
            CompositeActiveMMeshObjectSet ret = new CompositeActiveMMeshObjectSet.Intersection( this, operands, selector );
            return ret;

        } else {
            throw new IllegalArgumentException( "at least one set must be active" );
        }        
    }

   /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createActiveMeshObjectSetIntersection(
                new MeshObjectSet[] {
                        one,
                        two
                });
    }

    /**
     * Factory method to construct a MeshObjectSet that conatins those MeshObjects that are
     * contained in all of the provided MeshObjectSets, as long as they are also
     * selected by the MeshObjectSelector.
     *
     * @param one the first set to intersect
     * @param two the second set to intersect
     * @return the created MeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetIntersection(
            MeshObjectSet one,
            MeshObject    two )
    {
        return createActiveMeshObjectSetIntersection(
                new MeshObjectSet[] {
                        one,
                        createSingleMemberImmutableMeshObjectSet( two )
                });
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet.
     *
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @return the created CompositeActiveMeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetMinus(
            MeshObjectSet one,
            MeshObjectSet two )
    {
        return createActiveMeshObjectSetMinus( one, two, null );
    }

    /**
     * Factory method to construct a MeshObjectSet that contains those MeshObjects from
     * a first MeshObjectSet that are not contained in a second MeshObjectSet, as long
     * as they are also selected by the MeshObjectSelector.
     *
     * @param one the first MeshObjectSet
     * @param two the second MeshObjectSet
     * @param selector the MeshObjectSelector to use, if any
     * @return the created CompositeActiveMeshObjectSet
     */
    public CompositeActiveMMeshObjectSet createActiveMeshObjectSetMinus(
            MeshObjectSet      one,
            MeshObjectSet      two,
            MeshObjectSelector selector )
    {
        throw new UnsupportedOperationException( "for now" ); // FIXME
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     *
     * @param contentInOrder the content of the OrderedMeshObjectSet, in order
     * @return the created OrderedActiveMeshObjectSet
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObject [] contentInOrder )
    {
        return new OrderedConstantActiveMMeshObjectSet( this, contentInOrder, contentInOrder.length );
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter )
    {
        return createOrderedActiveMeshObjectSet( content, sorter, OrderedMeshObjectSet.UNLIMITED );
    }

    /**
     * Factory method to create an OrderedMeshObjectSet.
     * 
     * @param content the content of the OrderedMeshObjectSet
     * @param sorter the MeshObjectSorter that determines the ordering within the OrderedMeshObjectSet
     * @param max the maximum number of MeshObjects that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max MeshObjects according to the sorter.
     */
    public OrderedActiveMeshObjectSet createOrderedActiveMeshObjectSet(
            MeshObjectSet    content,
            MeshObjectSorter sorter,
            int              max )
    {
        return new OrderedActiveMMeshObjectSet( this, content, sorter, max );        
    }

   /**
     * Factory method to construct a TraversalActiveMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public TraversalActiveMeshObjectSet createActiveMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification )
    {
        if( specification instanceof RoleType ) {
            return createSpecificActiveMeshObjectSet( startObject, (RoleType) specification );

        } else if( specification instanceof SelectiveTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startObject, (SelectiveTraversalSpecification) specification );

        } else if( specification instanceof SequentialCompoundTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startObject, (SequentialCompoundTraversalSpecification) specification );

        } else if( specification instanceof AlternativeCompoundTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startObject, (AlternativeCompoundTraversalSpecification) specification );

        } else if( specification instanceof StayRightHereTraversalSpecification ) {
            return createToSelfActiveMeshObjectSet( startObject );

        } else if( specification instanceof AllNeighborsTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startObject, (AllNeighborsTraversalSpecification) specification );

        } else {
            throw new IllegalArgumentException( "unknown or unsuitable TraversalSpecification " + specification );
        }        
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a TraversalSpecification.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public TraversalActiveMeshObjectSet createActiveMeshObjectSet(
            MeshObjectSet          startSet,
            TraversalSpecification specification )
    {
        if( specification instanceof RoleType ) {
            return createSpecificActiveMeshObjectSet( startSet, (RoleType) specification );
            
        } else if( specification instanceof SelectiveTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startSet, (SelectiveTraversalSpecification) specification );

        } else if( specification instanceof SequentialCompoundTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startSet, (SequentialCompoundTraversalSpecification) specification );

        } else if( specification instanceof AlternativeCompoundTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startSet, (AlternativeCompoundTraversalSpecification) specification );

        } else if( specification instanceof StayRightHereTraversalSpecification ) {
            return createToSelfActiveMeshObjectSet( startSet );

        } else if( specification instanceof AllNeighborsTraversalSpecification ) {
            return createSpecificActiveMeshObjectSet( startSet, (AllNeighborsTraversalSpecification) specification );

        } else {
            throw new IllegalArgumentException( "unknown or unsuitable TraversalSpecification " + specification );
        }        
    }

    /**
     * Factory method to construct a TraversalActiveMeshObjectSet as the result of
     * traversing from a MeshObject through a TraversalSpecification, and repeating that process.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created ActiveMeshObjectSet
     */
    public ActiveMeshObjectSet createTransitiveClosureActiveMeshObjectSet(
            MeshObject             startObject,
            TraversalSpecification specification )
    {
        return new TransitiveClosureTraversalActiveMMeshObjectSet( this, startObject, specification );        
    }

    /**
     * Factory method to create an ImmutableTraversalPathSet.
     * Given that it has only one member, it might as well return an ordered TraversalPathSet.
     *
     * @param singleMember the single member of the ImmutableTraversalPathSet
     * @return return the created ImmutableTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createSingleMemberConstantActiveTraversalPathSet(
            TraversalPath singleMember )
    {
        throw new UnsupportedOperationException( "for now" ); // FIXME
    }

    /**
     * Factory method to create an ActiveTraversalPathSet.
     *
     * @param content the content for the ActiveTraversalPathSet
     * @return the created ActiveTraversalPathSet
     */
    public ActiveTraversalPathSet createActiveTraversalPathSet(
            TraversalPath [] content )
    {
        throw new UnsupportedOperationException( "for now" ); // FIXME
    }

    /**
     * Factory method to create an ActiveTraversalPathSet.
     * This creates a set of TraversalPaths each with length 1.
     * The destination of each TraversalPath corresponds to the elements of the
     * given MeshObjectSet. The TraversalSpecification is passed in.
     *
     * @param spec the traversed TraversalSpecification
     * @param set used to construct the content for the ActiveTraversalPathSet
     * @return the created ActiveTraversalPathSet
     */
    public ActiveTraversalPathSet createActiveTraversalPathSet(
            TraversalSpecification spec,
            MeshObjectSet          set )
    {
        throw new UnsupportedOperationException( "for now" ); // FIXME
    }

    /**
     * Factory method.
     *
     * @param start the MeshObject from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveMTraversalPathSet createActiveTraversalPathSet(
            MeshObject             start,
            TraversalSpecification spec )
    {
        if( spec instanceof RoleType ) {
            return new TraversalActiveMTraversalPathSet.OneStepFromMeshObject( this, start, (RoleType) spec );

        } else if( spec instanceof SelectiveTraversalSpecification ) {
            SelectiveTraversalSpecification realSpec = (SelectiveTraversalSpecification) spec;
            if( realSpec.getStartSelector() == null || realSpec.getStartSelector().accepts( start )) {
                return new TraversalActiveMTraversalPathSet.SelectiveStepFromMeshObject( this, start, realSpec );
            } else {
                return new TraversalActiveMTraversalPathSet.MeshObjectEmpty( this, start, spec );
            }

        } else if( spec instanceof SequentialCompoundTraversalSpecification ) {
            SequentialCompoundTraversalSpecification realSpec = (SequentialCompoundTraversalSpecification) spec;
            return new TraversalActiveMTraversalPathSet.MultiStepFromMeshObject( this, start, realSpec );

        } else if( spec instanceof AlternativeCompoundTraversalSpecification ) {
            AlternativeCompoundTraversalSpecification realSpec = (AlternativeCompoundTraversalSpecification) spec;
            return new TraversalActiveMTraversalPathSet.ParallelStepFromMeshObject( this, start, realSpec );

        } else if( spec instanceof StayRightHereTraversalSpecification ) {
            StayRightHereTraversalSpecification realSpec = (StayRightHereTraversalSpecification) spec;
            return new TraversalActiveMTraversalPathSet.ToSelfFromMeshObject( this, start );

        } else if( spec instanceof AllNeighborsTraversalSpecification ) {
            AllNeighborsTraversalSpecification realSpec = (AllNeighborsTraversalSpecification) spec;
            return new TraversalActiveMTraversalPathSet.OneStepFromMeshObject( this, start, realSpec );

        } else {
            log.error( "unknown or unsuitable TraversalSpecification " + spec );
            return null;
        }                
    }

    /**
     * Factory method.
     *
     * @param startSet the MeshObjectSet from which we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveMTraversalPathSet createActiveTraversalPathSet(
            MeshObjectSet          startSet,
            TraversalSpecification spec )
    {
        if( spec == null ) {
            throw new IllegalArgumentException();

        } else {
            return new TraversalActiveMTraversalPathSet.MeshObjectUnifier( this, startSet, spec );
        }                
    }

    /**
     * Factory method.
     *
     * @param startSet the TraversalPathSet from whose destination MeshObject we start the traversal
     * @param spec the TraversalSpecification from the start MeshObject
     * @return the created TraversalActiveMTraversalPathSet
     */
    public TraversalActiveMTraversalPathSet createActiveTraversalPathSet(
            TraversalPathSet       startSet,
            TraversalSpecification spec )
    {
        return new TraversalActiveMTraversalPathSet.PathUnifier( this, startSet, spec );
    }
    

    /**
     * Factory method to create an OrderedActiveTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @return the created OrderedActiveTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createOrderedActiveTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter )
    {
        return createOrderedActiveTraversalPathSet( content, sorter, Integer.MAX_VALUE );
    }

    /**
     * Factory method to create an OrderedActiveTraversalPathSet.
     *
     * @param content the content of the TraversalPathSet
     * @param sorter the TraversalPathSorter that determines the ordering within the OrderedTraversalPathSet
     * @param max the maximum number of TraversalPaths that will be contained by this set. If the underlying set contains more,
     *        this set will only contain the first max TraversalPaths according to the sorter.
     * @return the created OrderedActiveTraversalPathSet
     */
    public OrderedActiveTraversalPathSet createOrderedActiveTraversalPathSet(
            TraversalPathSet    content,
            TraversalPathSorter sorter,
            int                 max )
    {
        throw new UnsupportedOperationException( "Placeholder for now" );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObject through a RoleType. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the RoleTypes to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObject      startObject,
            RoleType        specification )
    {
        return new TraversalActiveMMeshObjectSet.OneStepFromMeshObject( this, startObject, specification );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a RoleType. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the RoleTypes to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObjectSet   startSet,
            RoleType        specification )
    {
        return new TraversalActiveMMeshObjectSet.Unifier( this, startSet, specification, null );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObject through a SelectiveTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the SelectiveTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObject                      startObject,
            SelectiveTraversalSpecification specification )
    {
        if( specification.getStartSelector() == null || specification.getStartSelector().accepts( startObject )) {
            ActiveMeshObjectSet childSet = createActiveMeshObjectSet( startObject, specification.getQualifiedTraversalSpecification() );
            TraversalActiveMMeshObjectSet.SelectiveStepFromMeshObject ret
                    = new TraversalActiveMMeshObjectSet.SelectiveStepFromMeshObject( this, startObject, specification, childSet );
            return ret;

        } else {
            return createEmptyActiveMeshObjectSet( startObject, specification );
        }
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a SelectiveTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the SelectiveTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObjectSet                   startSet,
            SelectiveTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.Unifier( this, startSet, specification, specification.getStartSelector() );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObject through a SequentialCompoundTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the SequentialCompoundTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObject                               startObject,
            SequentialCompoundTraversalSpecification specification )
    {
        TraversalSpecification [] specSteps = specification.getSteps();

        TraversalActiveMeshObjectSet current = createActiveMeshObjectSet( startObject, specSteps[0] );
        for( int i=1 ; i<specSteps.length ; ++i ) {
            current = createActiveMeshObjectSet( current, specSteps[i] );
        }
        return current;
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a SequentialCompoundTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the SequentialCompoundTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObjectSet                            startSet,
            SequentialCompoundTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.Unifier( this, startSet, specification, null );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObject through a AlternativeCompoundTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the AlternativeCompoundTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObject                                startObject,
            AlternativeCompoundTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.ParallelStepFromMeshObject( this, startObject, specification, null );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet through a AlternativeCompoundTraversalSpecification. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the AlternativeCompoundTraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObjectSet                             startSet,
            AlternativeCompoundTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.Unifier( this, startSet, specification, null );
    }

    /**
     * Factory method to construct a degenerate TraversalActiveMMeshObjectSet with only the start
     * object as content.
     * 
     * @param start the MeshObject from where we start the traversal
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createToSelfActiveMeshObjectSet(
            MeshObject start )
    {
        return new TraversalActiveMMeshObjectSet.ToSelfFromMeshObject( this, start );
    }

    /**
     * Factory method to construct a degenerate TraversalActiveMMeshObjectSet with only the start
     * set as content.
     * 
     * @param startSet the MeshObjectSet from where we start the traversal
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createToSelfActiveMeshObjectSet(
            MeshObjectSet startSet )
    {
        return new TraversalActiveMMeshObjectSet.ToSelfFromMeshObjectSet( this, startSet );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObject to all its neighbors. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     *
     * @param startObject the MeshObject from where we start the traversal
     * @param specification the RoleTypes to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObject                         startObject,
            AllNeighborsTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.OneStepFromMeshObject( this, startObject, specification );
    }

    /**
     * Factory method to construct a TraversalActiveMMeshObjectSet as the result of
     * traversing from a MeshObjectSet to all its neighbors. Only forward
     * events to a TraversalActiveMMeshObjectSet's content PropertyChangeListener that
     * affect the specified PropertyTypes.
     *
     * @param startSet the MeshObjectSet from where we start the traversal
     * @param specification the RoleTypes to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createSpecificActiveMeshObjectSet(
            MeshObjectSet                      startSet,
            AllNeighborsTraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.Unifier( this, startSet, specification, null );
    }

    /**
     * Factory method to construct a degenerate TraversalActiveMMeshObjectSet that is known to be
     * empty at all times.
     * 
     * @param start the MeshObject from where we start the traversal
     * @param specification the TraversalSpecification to apply to the startObject
     * @return the created TraversalActiveMMeshObjectSet
     */
    public TraversalActiveMMeshObjectSet createEmptyActiveMeshObjectSet(
            MeshObject             start,
            TraversalSpecification specification )
    {
        return new TraversalActiveMMeshObjectSet.EmptyFromMeshObject( this, start, specification );
    }

    /**
     * Buffer for an empty MeshObjectSet.
     */
    protected OrderedConstantActiveMMeshObjectSet theEmptyActiveSet;
}
