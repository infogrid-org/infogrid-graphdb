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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.m;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import org.infogrid.mesh.MeshObject;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyTypeOrGroup;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.util.ArrayHelper;

/**
  * A MeshObjectType that can have PropertyTypes and participate in inheritance.
  * In-memory implementation.
  */
public abstract class MAttributableMeshType
        extends
            MCollectableMeshType
        implements
            AttributableMeshType
{
    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MAttributableMeshType(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
     * This overrides the getIcon() method in order to provide a default icon from our supertype.
     *
     * @return the Icon
     */
    @Override
    public final BlobValue getIcon()
    {
        if( theIcon == null ) {
            for( int i=0 ; i<theSupertypes.length ; ++i ) {
                theIcon = theSupertypes[i].getIcon();
                if( theIcon != null ) {
                    break;
                }
            }
        }
        return theIcon;
    }

    /**
      * Obtain the locally defined PropertyTypes for this AttributableMeshType.
      * This does not return locally overridden PropertyTypes.
      *
      * @return the locally defined PropertyTypes for this AttributableMeshType
      */
    @Override
    public final PropertyType [] getLocalPropertyTypes()
    {
        return thePropertyTypes;
    }

    /**
     * Obtain the inherited, but locally overridden PropertyTypes.
     *
     * @return the inherited, but locally overridden PropertyTypes
     */
    @Override
    public final PropertyType [] getOverridingLocalPropertyTypes()
    {
        return theOverridingPropertyTypes;
    }

    /**
      * Obtain the inherited PropertyTypes for this AttributableMeshType.
      *
      * @return the inherited PropertyTypes for this AttributableMeshType
      */
    @Override
    public final PropertyType [] getInheritedPropertyTypes()
    {
        if( inheritedPropertyTypes == null ) {
            try {
                ArrayList<MPropertyType> almostRet = internalGetInheritedPropertyTypes(); // can throw InheritanceConflictException

                inheritedPropertyTypes = ArrayHelper.copyIntoNewArray(
                        almostRet,
                        MPropertyType.class );
            } finally {
                if( inheritedPropertyTypes == null ) {
                    inheritedPropertyTypes = new MPropertyType[0];
                }
            }
        }
        return inheritedPropertyTypes;
    }

    /**
      * Obtain the transitive closure of all local and inherited PropertyTypes of this
      * AttributableMeshType and its supertypes without
      * duplicates. This will return locally overridden PropertyTypes and skip those in
      * supertypes that have been overridden.
      *
      * @return all local and inherited PropertyTypes
      */
    @Override
    public final PropertyType [] getAllPropertyTypes()
    {
        if( allPropertyTypes == null ) {
            try {
                ArrayList<MPropertyType> almostRet = internalGetAllPropertyTypes(); // can throw InheritanceConflictException

                allPropertyTypes = ArrayHelper.copyIntoNewArray(
                        almostRet,
                        MPropertyType.class );
            } finally {
                if( allPropertyTypes == null ) {
                    allPropertyTypes = new MPropertyType[0];
                }
            }
        }
        return allPropertyTypes;
    }

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypes of this
     * AttributableMeshType and its supertypes that are
     * ProjectedPropertyTypes without duplicates. This will return locally overridden
     * PropertyTypes and skip those in supertypes that have been overridden.
     *
     * @return all local and inherited PropertyTypes that are ProjectedPropertyTypes
     */
    @Override
    public final ProjectedPropertyType [] getAllProjectedPropertyTypes()
    {
        PropertyType [] pts = getAllPropertyTypes();

        ArrayList<MProjectedPropertyType> almostRet = new ArrayList<MProjectedPropertyType>( pts.length );
        for( int i=0 ; i<pts.length ; ++i ) {
            if( pts[i] instanceof MProjectedPropertyType ) {
                almostRet.add( (MProjectedPropertyType) pts[i] );
            }
        }
        return ArrayHelper.copyIntoNewArray( almostRet, MProjectedPropertyType.class );
    }

    /**
      * Add a PropertyType.
      *
      * @param attr the PropertyType to add
      */
    public final void addPropertyType(
            PropertyType attr )
    {
        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            if( thePropertyTypes[i].equals( attr )) {
                throw new IllegalArgumentException( attr.toExternalForm() );
            }
        }
        if( attr.getOverride().length != 0 ) {
            throw new IllegalArgumentException( attr.toExternalForm() );
        }

        thePropertyTypes = ArrayHelper.append(
                thePropertyTypes,
                (MPropertyType) attr,
                MPropertyType.class );

        zeroCaches();
    }

    /**
      * Add an overriding PropertyType.
      *
      * @param attr the overriding PropertyType to add
      * @throws InheritanceConflictException thrown if provided PropertyType is not defined in a supertype of
      *          this AttributableMeshType
      */
    public final void addOverridingPropertyType(
            PropertyType attr )
        throws
            InheritanceConflictException
    {
        for( int i=0 ; i<theOverridingPropertyTypes.length ; ++i ) {
            if( theOverridingPropertyTypes[i].equals( attr )) {
                throw new IllegalArgumentException( attr.toExternalForm() );
            }
        }

        PropertyType [] overrides = attr.getOverride();
        if( overrides.length == 0 ) {
            throw new IllegalArgumentException( attr.toExternalForm() );
        }

        // make sure we are not overriding someone else's PropertyType, and that
        // we are not skipping overriding PropertyTypes in supertypes "in between"

        for( int i=0 ; i<overrides.length ; ++i ) {
            boolean found = false;
            for( int j=0 ; j<theSupertypes.length ; ++j ) {
                if( hasNonOverridden( theSupertypes[j], overrides[i] )) {
                    found = true;
                    break;
                }
            }
            if( !found ) {
                throw new InheritanceConflictException( this, new PropertyType[] { attr, overrides[i] } );
            }
        }

        theOverridingPropertyTypes = ArrayHelper.append(
                theOverridingPropertyTypes,
                (MPropertyType) attr,
                MPropertyType.class );
        zeroCaches();
    }

    /**
     * Internal helper to determine whether this AttributableMeshType has
     * this PropertyType locally, or inherited directly (without override).
     *
     * @param amo the AttributableMeshType
     * @param attr the PropertyType to be tested
     * @return true if attr is defined locally in amo, or inherited without override, or overridden locally
     */
    protected static boolean hasNonOverridden(
            AttributableMeshType amo,
            PropertyType               attr )
    {
        PropertyType [] mas;

        // check local PropertyTypes
        mas = amo.getLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            if( mas[i] == attr ) {
                return true;
            }
        }

        // check local overridden PropertyTypes
        mas = amo.getOverridingLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            if( mas[i] == attr ) {
                return true;
            }
        }

        // check supertypes
        AttributableMeshType [] supertypes = amo.getDirectSupertypes();
        for( int i=0 ; i<supertypes.length ; ++i ) {
            if( hasNonOverridden( supertypes[i], attr )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtain the locally defined PropertyTypeGroups for this AttributableMeshTypes.
     *
     * @return the locally defined PropertyTypeGroups for this AttributableMeshTypes
     */
    @Override
    public final PropertyTypeGroup [] getLocalPropertyTypeGroups()
    {
        return thePropertyTypeGroups;
    }

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypeGroup of
     * this AttributableMeshTypes and all of its supertypes without duplicates.
     *
     * @return all local and inherited PropertyTypeGroups
     */
    @Override
    public final PropertyTypeGroup [] getAllPropertyTypeGroups()
    {
        ArrayList<MPropertyTypeGroup> almostRet = internalGetAllPropertyTypeGroups();

        // FIXME? need to deal with duplicates in case of multiple inheritance?

        return ArrayHelper.copyIntoNewArray(
                almostRet,
                MPropertyTypeGroup.class );
    }

    /**
     * Add a local PropertyTypeGroup.
     *
     * @param mag the PropertyTypeGroup to be added
     */
    public final void addPropertyTypeGroup(
            MPropertyTypeGroup mag )
    {
        thePropertyTypeGroups = ArrayHelper.append(
                thePropertyTypeGroups,
                mag,
                MPropertyTypeGroup.class );
    }

    /**
     * Obtain the transitive closure of all local and inherited PropertyTypeOrGroups
     * of this AttributableMeshType and all of its supertypes without duplicates.
     * Do not include those PropertyTypes that are part of returned PropertyTypeGroups.
     * Return them ordered by their SequenceNumber property.
     *
     * @return all local and inherited PropertyTypeOrGroups in sequence
     */
    @Override
    public final PropertyTypeOrGroup [] getAllPropertyTypeOrGroupsInSequence()
    {
        // determine which PropertyTypes are not in groups
        PropertyType      [] all    = getAllPropertyTypes();
        PropertyTypeGroup [] groups = getAllPropertyTypeGroups();

        int remaining = all.length;
        for( int i=0 ; i<groups.length ; ++i ) {
            PropertyType [] thisGroup = groups[i].getContainedPropertyTypes();
            for( int j=0 ; j<thisGroup.length ; ++j ) {
                for( int k=0 ; k<all.length ; ++k ) {
                    if( all[k] != null && all[k].equalsOrOverrides( thisGroup[j] )) {
                    // if( thisGroup[j].equals( all[k] ))
                        all[k] = null;
                        --remaining;
                        break;
                    }
                }
            }
        }

        // put the return array together
        PropertyTypeOrGroup [] almostRet = new PropertyTypeOrGroup[ remaining + groups.length ];
        for( int i=0 ; i<groups.length ; ++i ) {
            almostRet[i+remaining] = groups[i];
        }

        for( int i=all.length-1 ; i>=0 ; --i ) {
            if( all[i] != null ) {
                almostRet[ --remaining ] = all[i];
            }
        }

        // sort before returning
        PropertyTypeOrGroup [] ret = ArrayHelper.sort(
                almostRet,
                PROPERTY_TYPE_COMPARATOR );

        return ret;
    }

    /**
      * Set the direct supertypes of this AttributableMeshType.
      *
      * @param supertypes the direct supertypes of this AttributableMeshType
      * @see #getDirectSupertypes
      */
    public final void setDirectSupertypes(
            MAttributableMeshType [] supertypes )
    {
        if( this.theSupertypes.length != 0 ) {
            throw new IllegalArgumentException();
        }

        this.theSupertypes = supertypes;

        zeroCaches();
    }

    /**
      * Obtain the direct supertypes for this AttributableMeshType.
      *
      * @return the direct supertypes of this AttributableMeshType
      * @see #setDirectSupertypes
      */
    @Override
    public final AttributableMeshType [] getDirectSupertypes()
    {
        return theSupertypes;
    }

    /**
     * Obtain the transitive closure of all supertypes.
     *
     * @return transitive closure of all supertypes of this AttributableMeshType
     */
    @Override
    public final AttributableMeshType [] getAllSupertypes()
    {
        if( allSupertypes == null ) {
            ArrayList<MAttributableMeshType> almostRet = internalGetAllSupertypes();

            allSupertypes = ArrayHelper.copyIntoNewArray(
                    almostRet,
                    MAttributableMeshType.class );
        }
        return allSupertypes;
    }

    /**
     * Obtain the paths to this AttributableMeshType down the inheritance hierarchy
     * from its ultimate root AttributableMeshType(s).
     * If this AttributableMeshType does not inherit from another, this will
     * return an empty set. If it (or a supertype) employs multiple inheritance,
     * this will return a set with more than one member. Each members of the
     * set at the steps taken from the root of the inheritance hierarchy down to
     * here (but not including this AttributableMeshType).
     *
     * @return the paths from the root(s) of the inheritance hierarchy
     */
    @Override
    public AttributableMeshType [][] getPathsFromInheritanceRoots()
    {
        if( theSupertypes.length == 0 ) {
            return new AttributableMeshType[0][];
        }

        ArrayList<AttributableMeshType[]> almostRet = new ArrayList<AttributableMeshType[]>();
        
        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            AttributableMeshType [][] found = theSupertypes[i].getPathsFromInheritanceRoots();

            if( found.length > 0 ) {
                for( int j=0 ; j<found.length ; ++j ) {
                    AttributableMeshType [] prepended = ArrayHelper.append( theSupertypes[i], found[j], AttributableMeshType.class );
                    almostRet.add( prepended );
                }
            } else {
                almostRet.add( new AttributableMeshType[] { theSupertypes[i] } );
            }
        }
        
        AttributableMeshType [][] ret = ArrayHelper.copyIntoNewArray( almostRet, AttributableMeshType[].class );
        return ret;
    }
    
    /**
      * Obtain the direct subtypes for this AttributableMeshType in the working model.
      * By definition, not all subtypes of the AttributableMeshType can be known at
      * any point in time; so only the currently known ones are returned.
      *
      * @return the direct subtypes of this AttributableMeshType
      * @see #addDirectSubtype
      */
    @Override
    public final AttributableMeshType [] getDirectSubtypes()
    {
        return theSubtypes;
    }

    /**
      * Add subtype to this AttributableMeshType if not already there.
      *
      * @param subtype the new subtype to be added
      * @see #getDirectSubtypes
      */
    public final void addDirectSubtype(
            AttributableMeshType subtype )
    {
        for( int i=0 ; i<theSubtypes.length ; ++i ) {
            if( theSubtypes[i].equals( subtype )) {
                return;
            }
        }

        theSubtypes = ArrayHelper.append(
                theSubtypes,
                (MAttributableMeshType) subtype,
                MAttributableMeshType.class );

        zeroCaches();
    }

    /**
      * Obtain the transitive closure of all subtypes of this AttributableMeshType
      * in the working model.
      * By definition, not all subtypes of the AttributableMeshType can be known at
      * any point in time; so only the currently known ones are returned.
      *
      * @return the transitive closure of all known subtypes of this AttributableMeshType
      */
    @Override
     public final AttributableMeshType [] getAllSubtypes()
     {
         ArrayList<MAttributableMeshType> almostRet = new ArrayList<MAttributableMeshType>();
         for( int i=0 ; i<theSubtypes.length ; ++i ) {
             AttributableMeshType [] temp = theSubtypes[i].getAllSubtypes();
             for( int j=0 ; j<temp.length ; ++j ) {
                 if( ! almostRet.contains( temp[j] )) {
                     almostRet.add( (MAttributableMeshType) temp[j] );
                 }
             }
             almostRet.add( theSubtypes[i] );
         }
         return ArrayHelper.copyIntoNewArray( almostRet, MAttributableMeshType.class );
     }

     /**
       * Obtain the transitive closure of all subtypes of this AttributableMeshType
       * in the working model, and return those that are instantiable (as opposed
       * to those that are abstract).
       * By definition, not all subtypes of the AttributableMeshType can be known at
       * any point in time; so only the currently known ones are returned.
       *
       * @return the transitive closure of all known subtypes of this AttributableMeshType
       */
    @Override
    public final AttributableMeshType [] getAllConcreteSubtypes()
    {
        AttributableMeshType [] allSubtypes = getAllSubtypes();
        int count = 0;
        for( int i=0 ; i<allSubtypes.length ; ++i ) {
            if( ! allSubtypes[i].getIsAbstract().value() ) {
                ++count;
            }
        }
        AttributableMeshType [] ret = new AttributableMeshType[ count ];
        for( int i=allSubtypes.length-1 ; i>=0 ; --i )
        {
            if( ! allSubtypes[i].getIsAbstract().value() ) {
                ret[--count] = allSubtypes[i];
            }
        }
        return ret;
    }

    /**
      * Determine whether the passed-in AttributableMeshType equals or is
      * a supertype of this AttributableMeshType.
      *
      * @param other the AttributableMeshType to test against
      * @return true of the passed-in AttributableMeshType equals or is a supertype of this
      *         AttributableMeshType
      */
    @Override
    public final boolean equalsOrIsSupertype(
            AttributableMeshType other )
    {
        return getDistanceToSupertype( other ) >= 0;
    }

    /**
      * Determine the distance from this AttributableMeshType to the
      * passed-in (supertype) AttributableMeshType in number of "inheritance steps".
      *
      * @param other the AttributableMeshType to test against
      * @return the number of steps between this AttributableMeshType and other. -1 if
      *         there is no sub-typing relationships between the two
      */
    @Override
    public final int getDistanceToSupertype(
            AttributableMeshType other )
    {
        if( this == other || equals( other ) ) {
            return 0;
        }

        int bestMatch = Integer.MAX_VALUE;
        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            int current = theSupertypes[i].getDistanceToSupertype( other );
            if( current >= 0 && current < bestMatch ) {
                bestMatch = current;
            }
        }
        if( bestMatch != Integer.MAX_VALUE ) {
            return bestMatch + 1; // one more than zero
        } else {
            return -1;
        }
    }

    /**
      * Determine the distance from this AttributableMeshType to the
      * passed-in (supertype) AttributableMeshType in number of "inheritance steps".
      *
      * This method works on the name of the (class representing instances of the)
      * supertype, rather than its AttributableMeshType.
      *
      * @param className the class name corresponding to the AttributableMeshType to test against
      * @return the number of steps between this AttributableMeshType and other. -1 if
      *         there is no sub-typing relationships between the two
      */
    @Override
    public final int getDistanceToSupertype(
            String className )
    {
        // FIXME this is rather inefficient.

        String myName = getSubjectArea().getName().value() + "." + getName().value();
        if( myName.equals( className )) {
            return 0;
        }

        int bestDistance = Integer.MAX_VALUE;
        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            int distance = theSupertypes[i].getDistanceToSupertype( className );
            if( distance != -1 && distance < bestDistance ) {
                bestDistance = distance;
            }
        }
        if( bestDistance == Integer.MAX_VALUE ) {
            return -1;
        } else {
            return bestDistance+1;
        }
    }

    /**
     * Determine whether this AttributableMeshType is a subtype of the passed-in AttributableMeshType.
     *
     * @param other the AttributableMeshType to test against
     * @return true if this AttributableMeshType is a subtype of other
     */
    @Override
    public final boolean isSubtypeOfOrEquals(
            AttributableMeshType other )
    {
        return getDistanceToSupertype( other ) >=0;
    }

    /**
     * Determine whether this AttributableMeshType is a subtype of the passed-in
     * AttributableMeshType. This returns fakse if they are the same.
     *
     * @param other the AttributableMeshType to test against
     * @return true if this AttributableMeshType is a subtype of other
     * @see #isSubtypeOfOrEquals
     */
    @Override
    public final boolean isSubtypeOfDoesNotEqual(
            AttributableMeshType other )
    {
        return getDistanceToSupertype( other ) >0;
    }

    /**
     * Determine whether the passed-in MeshObject is an instance of this AttributableMeshType
     * or one of its subtypes. This is modeled after a similar method on java.lang.Class.
     *
     * @param candidate RootObject the RootObject to test against
     * @return true if candidate is an instance of this AttributableMeshType, or one of its subtypes
     */
    @Override
    public final boolean isInstance(
            MeshObject candidate )
    {
        AttributableMeshType [] other = candidate.getTypes();
        for( int i=0 ; i<other.length ; ++i ) {
            if( other[i].equalsOrIsSupertype( this )) {
                return true;
            }
        }
        return false;
    }

    /**
      * Set the IsAbstract property.
      *
      * @param newValue the new value for the property
      * @see #getIsAbstract
      */
    public final void setIsAbstract(
            BooleanValue newValue )
    {
        this.theIsAbstract = newValue;
    }

    /**
      * Obtain the value of the IsAbstract property.
      *
      * @return the value of the IsAbstract property
      * @see #setIsAbstract
      */
    @Override
    public final BooleanValue getIsAbstract()
    {
        return theIsAbstract;
    }

    /**
      * Find one of this AttributableMeshType's local or inherited PropertyTypes
      * by its Name property. This will return the most concrete overridden PropertyType.
      *
      * @param name value of the Name property of the PropertyType
      * @return the found PropertyType, or null if not found.
      */
    @Override
    public final PropertyType findPropertyTypeByName(
            String name )
    {
        PropertyType [] all = getAllPropertyTypes();

        for( int i=0 ; i<all.length ; ++i ) {
            if( name.equals( all[i].getName().getAsString())) {
                return all[i];
            }
        }

        return null;
    }

    /**
     * Obtain a ClassLoader that can load the appropriate Java classes to instantiate this
     * AttributableMeshType.
     *
     * @return the ClassLoader that can load the appropriate Java classes to instantiate this
     *         AttributableMeshType
     */
    @Override
    public final ClassLoader getClassLoader()
    {
        return getSubjectArea().getClassLoader();
    }

    /**
      * Obtain transitive closure of PropertyTypes, with duplicates but
      * without overridden PropertyTypes in supertypes.
      *
      * @return an ArrayList containing a transitive closure of PropertyTypes
      * @throws InheritanceConflictException an inheritance problem was encountered when traversing
      */
    protected final ArrayList<MPropertyType> internalGetAllPropertyTypes()
            throws
                InheritanceConflictException
    {
        ArrayList<MPropertyType> ret = new ArrayList<MPropertyType>( 20 ); // fudge factor
        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            ret.add( thePropertyTypes[i] );
        }
        for( int i=0 ; i<theOverridingPropertyTypes.length ; ++i ) {
            ret.add( theOverridingPropertyTypes[i] );
        }

        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            PropertyType [] inherited = theSupertypes[i].getAllPropertyTypes();

            for( int j=0 ; j<inherited.length ; ++j ) {
                PropertyType overrideRet = null;

                Iterator<MPropertyType> theIter = ret.iterator();
                while( theIter.hasNext() ) {
                    PropertyType currentRet = theIter.next();
                    if( currentRet.getOverrideAncestor() == inherited[j].getOverrideAncestor() ) {
                        overrideRet = currentRet;
                        break;
                    }
                }
                if( overrideRet == null ) {
                    // no override
                    ret.add( (MPropertyType) inherited[j] );
                } else if( overrideRet == inherited[j] ) {
                    // same from two sides, skip
                } else if( overrideRet.overrides( inherited[j] )) {
                   // correct override, skip
                } else if( inherited[j].overrides( overrideRet )) {
                    // correct override -- remove old, put new, unless it's a locally overridden thing in which
                    // case we have an error
                    if( ArrayHelper.isIn( overrideRet, theOverridingPropertyTypes, false )) {
                        throw new InheritanceConflictException( this, new PropertyType[] { overrideRet } );
                    }

                    ret.remove( overrideRet );
                    ret.add( (MPropertyType) inherited[j] );
                } else {
                    // incorrect override, error
                    throw new InheritanceConflictException( this, new PropertyType[] { overrideRet, inherited[j] } );
                }
            }
        }

        return ret;
    }

    /**
      * Obtain transitive closure of PropertyTypeGroups, with duplicates.
      *
      * @return an ArrayList containing the transitive closure of PropertyTypeGroups
      */
    protected final ArrayList<MPropertyTypeGroup> internalGetAllPropertyTypeGroups()
    {
        ArrayList<MPropertyTypeGroup> ret = new ArrayList<MPropertyTypeGroup>( 20 ); // fudge factor
        for( int i=0 ; i<thePropertyTypeGroups.length ; ++i ) {
            ret.add( thePropertyTypeGroups[i] );
        }

        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            PropertyTypeGroup [] inherited = theSupertypes[i].getAllPropertyTypeGroups();

            for( int j=0 ; j<inherited.length ; ++j ) {
                if( !ret.contains( inherited[j] )) {
                    ret.add( (MPropertyTypeGroup) inherited[j] );
                }
            }
        }
        return ret;
    }

    /**
      * Obtain transitive closure of inherited (but not local) PropertyTypes, with duplicates but
      * without overridden PropertyTypes in supertypes.
      *
      * @return an ArrayList containing a transitive closure of PropertyTypes
      * @throws InheritanceConflictException an inheritance problem was encountered when traversing
      */
    protected final ArrayList<MPropertyType> internalGetInheritedPropertyTypes()
    {
        ArrayList<MPropertyType> ret = new ArrayList<MPropertyType>( 20 ); // fudge factor

        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            PropertyType [] inherited = theSupertypes[i].getAllPropertyTypes();

            for( int j=0 ; j<inherited.length ; ++j ) {
                PropertyType overrideRet = null;

                Iterator<MPropertyType> theIter = ret.iterator();
                while( theIter.hasNext() ) {
                    PropertyType currentRet = theIter.next();
                    if( currentRet.getOverrideAncestor() == inherited[j].getOverrideAncestor() ) {
                        overrideRet = currentRet;
                        break;
                    }
                }
                if( overrideRet == null ) {
                    // no override
                    ret.add( (MPropertyType) inherited[j] );
                } else if( overrideRet == inherited[j] ) {
                    // same from two sides, skip
                } else if( overrideRet.overrides( inherited[j] )) {
                   // correct override, skip
                } else if( inherited[j].overrides( overrideRet )) {
                    // correct override -- remove old, put new, unless it's a locally overridden thing in which
                    // case we have an error
                    if( ArrayHelper.isIn( overrideRet, theOverridingPropertyTypes, false )) {
                        throw new InheritanceConflictException( this, new PropertyType[] { overrideRet } );
                    }

                    ret.remove( overrideRet );
                    ret.add( (MPropertyType) inherited[j] );
                } else {
                    // incorrect override, error
                    throw new InheritanceConflictException( this, new PropertyType[] { overrideRet, inherited[j] } );
                }
            }
        }

        return ret;        
    }

    /**
     * Obtain transitive closure of all supertypes.
     *
     * @return an ArrayList containing all supertypes.
     */
    protected final ArrayList<MAttributableMeshType> internalGetAllSupertypes()
    {
        ArrayList<MAttributableMeshType> ret = new ArrayList<MAttributableMeshType>( 20 ); // fudge factor

        for( int i=0 ; i<theSupertypes.length ; ++i ) {
            AttributableMeshType [] inherited = theSupertypes[i].getAllSupertypes();

            for( int j=0 ; j<inherited.length ; ++j ) {
                if( !ret.contains( inherited[j] )) {
                    ret.add( (MAttributableMeshType) inherited[j] );
                }
            }
            if( !ret.contains( theSupertypes[i] )) {
                ret.add( (MAttributableMeshType) theSupertypes[i] );
            }
        }
        return ret;
    }

    /**
     * This internal helper zeros out cached values. It is invoked in case the inheritance
     * hierarchy changes, such as when loading additional SubjectAreas at run-time..
     */
    protected final void zeroCaches()
    {
        allPropertyTypes = null;
        allSupertypes    = null;

        for( int i=0 ; i<theSubtypes.length ; ++i ) {
            theSubtypes[i].zeroCaches();
        }
    }

    /**
      * The PropertyTypes locally defined in this AttributableMeshType.
      */
    private MPropertyType [] thePropertyTypes = new MPropertyType[0];

    /**
     * The PropertyTypes overriding inherited PropertyTypes for this AttributableMeshType.
     */
    private MPropertyType [] theOverridingPropertyTypes = new MPropertyType[0];

    /**
     * The PropertyTypeGroups locally defined in this AttributableMeshType.
     */
    private MPropertyTypeGroup [] thePropertyTypeGroups = new MPropertyTypeGroup[0];

    /**
      * The set of direct supertypes of this AttributableMeshType.
      */
    private AttributableMeshType [] theSupertypes = new MAttributableMeshType[0];

    /**
     * The set of direct subtypes of this AttributableMeshType in the working model.
      */
    private MAttributableMeshType [] theSubtypes = new MAttributableMeshType[0];

    /**
     * This caches the set of all inherited PropertyTypes.
     * This goes with method zeroCaches().
     */
    private transient MPropertyType [] inheritedPropertyTypes = null;

    /**
     * This caches the set of all PropertyTypes, local and inherited. If we don't cache
     * this, we'll have to recalculate every time we determine the set of all
     * (local and inherited) PropertyTypes and that takes a while.
     * This goes with method zeroCaches().
     */
    private transient MPropertyType [] allPropertyTypes = null;

    /**
     * This caches the set of all supertypes. If we don't cache this,
     * we'll have to recalculate every time we determine the set of all (direct and
     * indirect) supertypes and that takes a while. This goes with method zeroCaches().
     */
    private transient AttributableMeshType [] allSupertypes = null;

    /**
      * The value of the IsAbstract property.
      */
    private BooleanValue theIsAbstract;

    /**
     * This is a Comparator that allows us to order PropertyTypes / PropertyTypeGroups
     * by their SequenceNumber properties.
     */
    private static Comparator<PropertyTypeOrGroup> PROPERTY_TYPE_COMPARATOR = new Comparator<PropertyTypeOrGroup>() {
            /**
             * Compare the two Objects.
             *
             * @param o1 the first Object
             * @param o2 the second Object
             * @returns integer indicating the comparison result
             */
            @Override
            public int compare(
                    PropertyTypeOrGroup o1,
                    PropertyTypeOrGroup o2 )
            {
                double v1 = o1.getSequenceNumber().value();
                double v2 = o2.getSequenceNumber().value();

                if( v1 > v2 ) {
                    return -1;
                } else if( v1 < v2 ) {
                    return 1;
                } else {
                    return 0;
                }
            }
    };
}
