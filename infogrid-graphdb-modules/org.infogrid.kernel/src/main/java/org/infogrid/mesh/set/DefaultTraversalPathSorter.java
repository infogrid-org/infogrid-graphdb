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

import java.util.Arrays;
import java.util.Comparator;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.StringHelper;

/**
 * A default TraversalPathSorter implementation leveraging the Java collections API.
 */
public class DefaultTraversalPathSorter
        implements
            TraversalPathSorter
{
    /**
     * Construct one. Specify the comparison criteria like the Java collections API does.
     *
     * @param c the Comparator defining the sorting criteria
     */
    public DefaultTraversalPathSorter(
            Comparator<TraversalPath> c )
    {
        this( c, null );
    }

    /**
     * Construct one. Specify the comparison criteria like the Java collections API does.
     *
     * @param c the Comparator defining the sorting criteria
     * @param userName a user-visible name for this object
     */
    public DefaultTraversalPathSorter(
            Comparator<TraversalPath> c,
            String                    userName )
    {
        theComparator  = c;
        theUserName    = userName;
    }

    /**
     * Obtain the Comparator that determines the ordering.
     *
     * @return the Comparator that determines the ordering
     */
    public Comparator<TraversalPath> getComparator()
    {
        return theComparator;
    }

    /**
     * Pass in an array of TraversalPath in arbitrary order, and return the same set
     * in order.
     *
     * @param unorderedSet the unordered set
     * @return this a convenience return value: it's the same array as was passed in
     */
    public TraversalPath [] getOrderedInSame(
            TraversalPath [] unorderedSet )
    {
        Arrays.sort( unorderedSet, theComparator );
        return unorderedSet;
    }

    /**
     * Pass in an array of TraversalPath in arbitrary order, and return a new set
     * with the same content in order.
     *
     * @param unorderedSet the unordered set
     * @return the new array with the ordered content
     */
    public TraversalPath [] getOrderedInNew(
            TraversalPath [] unorderedSet )
    {
        TraversalPath [] unorderedSet2 = ArrayHelper.copyIntoNewArray( unorderedSet, TraversalPath.class );

        return getOrderedInSame( unorderedSet2 );
    }

    /**
     * Obtain the index where a new TraversalPath would be inserted to keep the
     * passed-in array ordered according to this sorter.
     *
     * @param newObject the new TraversalPath potentially to be inserted
     * @param orderedSet the set into which newObject could potentially be inserted
     * @return the index where newObject would be inserted
     */
    public int getIndexOfNew(
            TraversalPath    newObject,
            TraversalPath [] orderedSet )
    {
        int index = Arrays.binarySearch( orderedSet, newObject, theComparator );
        if( index >= 0 ) {
            return index;
        } else {
            return -index-1;
        }
    }

    /**
     * Obtain the index of a an existing TraversalPath in the array.
     *
     * @param objectToLookFor the TraversalPath that we look for
     * @param orderedSet the set in which we look.
     * @return the index of objectToLookFor within orderedSet, or -1 if not found
     */
    public int getIndexOf(
            TraversalPath    objectToLookFor,
            TraversalPath [] orderedSet )
    {
        int index = Arrays.binarySearch( orderedSet, objectToLookFor, theComparator );
        if( index >= 0 ) {
            return index;
        } else {
            return -1;
        }
    }

    /**
     * Obtain the user visible-name for this sorter.
     *
     * @return the user-visible name
     */
    public String getUserName()
    {
        return theUserName;
    }

    /**
     * The Comparator that determines the sort order.
     */
    protected Comparator<TraversalPath> theComparator;

    /**
     * The user-visible name for this TraversalPathSorter.
     */
    protected String theUserName;

    /**
     * An abstract superclass for the Comparators defined in this file.
     */
    public static abstract class AbstractComparator
        implements
            Comparator<TraversalPath>
    {
        /**
         * Constructor.
         *
         * @param index the index of the MeshObject in the TraversalPath that we compare. -1 indicates the last MeshObject in the TraversalPath
         */
        public AbstractComparator(
               int index )
        {
            theIndex = index;
        }

        /**
         * Helper method to find the right MeshObject given the parameters
         * passed into the compare method.
         *
         * @param o the TraversalPath, as Object
         * @return the found MeshObject
         */
        protected MeshObject getMeshObjectFrom(
                TraversalPath o )
        {
            if( theIndex == -1 ) {
                return o.getLastMeshObject();
            } else {
                return o.getMeshObjectAt( theIndex );
            }
        }

        /**
         * The index in the TrraversalPath where we compare.
         */
        protected int theIndex;
    }

    /**
     * A pre-defined Comparator class to compare by update time.
     */
    public static class UpdateTimeComparator
        extends
            AbstractComparator
    {
        /**
         * Constructor.
         *
         * @param index the index of the MeshObject in the TraversalPath that we compare. -1 indicates the last MeshObject in the TraversalPath
         */
        public UpdateTimeComparator(
               int index )
        {
            super( index );
        }

        /**
         * Comparison method.
         *
         * @param one the first TraversalPath to compare
         * @param two the second TraversalPath to compare
         * @return the comparison value (-1, 0, +1)
         */
        public int compare(
                TraversalPath one,
                TraversalPath two )
        {
            long oneValue = getMeshObjectFrom( one ).getTimeUpdated();
            long twoValue = getMeshObjectFrom( two ).getTimeUpdated();

            if( oneValue < twoValue ) {
                return -1;
            } else if( oneValue == twoValue ) {
                return 0;
            } else {
                return +1;
            }
        }
    }

    /**
     * A pre-defined Comparator class to compare by creation time.
     */
    public static class CreationTimeComparator
        extends
            AbstractComparator
    {
        /**
         * Constructor.
         *
         * @param index the index of the MeshObject in the TraversalPath that we compare. -1 indicates the last MeshObject in the TraversalPath
         */
        public CreationTimeComparator(
               int index )
        {
            super( index );
        }

        /**
         * Comparison method.
         *
         * @param one the first TraversalPath to compare
         * @param two the second TraversalPath to compare
         * @return the comparison value (-1, 0, +1)
         */
        public int compare(
                TraversalPath one,
                TraversalPath two )
        {
            long oneValue = getMeshObjectFrom( one ).getTimeCreated();
            long twoValue = getMeshObjectFrom( two ).getTimeCreated();

            if( oneValue < twoValue ) {
                return -1;
            } else if( oneValue == twoValue ) {
                return 0;
            } else {
                return +1;
            }
        }
    }

    /**
     * A pre-defined Comparator class to compare by the value of a certain PropertyType on both
     * compared objects.
     */
    public static class ByPropertyComparator
        extends
            AbstractComparator
    {
        /**
         * Constructor.
         *
         * @param index the index of the MeshObject in the TraversalPath that we compare. -1 indicates the last
         *        MeshObject in the TraversalPath
         * @param pt the PropertyType that indicates which properties we compare
         */
        public ByPropertyComparator(
               int          index,
               PropertyType pt )
        {
            super( index );

            thePropertyType = pt;
        }


        /**
         * Comparison method.
         *
         * @param one the first TraversalPath to compare
         * @param two the second TraversalPath to compare
         * @return the comparison value (-1, 0, +1)
         */
        public int compare(
                TraversalPath one,
                TraversalPath two )
        {
            try {
                PropertyValue oneValue = getMeshObjectFrom( one ).getPropertyValue( thePropertyType );
                PropertyValue twoValue = getMeshObjectFrom( two ).getPropertyValue( thePropertyType );

                return PropertyValue.compare( oneValue, twoValue );

            } catch( IllegalPropertyTypeException ex ) {
                return 0;

            } catch( NotPermittedException ex ) {
                return 0;
            }
        }

        /**
         * The PropertyType by which we compare.
         */
        protected PropertyType thePropertyType;
    }

    /**
     * A pre-defined Comparator class to compare by the the user-visibile String of two MeshObjects.
     */
    public static class ByUserVisibleStringComparator
        extends
            AbstractComparator
    {
        /**
         * Constructor.
         *
         * @param index the index of the MeshObject in the TraversalPath that we compare. -1 indicates the last
         *        MeshObject in the TraversalPath
         */
        public ByUserVisibleStringComparator(
               int index )
        {
            super( index );
        }

        /**
         * Comparison method. This follows the same comparison approach based on UserVisibleString,
         * with a fallback of Identifier, as DefaultMeshObjectSorter.BY_USER_VISIBLE_STRING.
         *
         * @param one the first TraversalPath to compare
         * @param two the second TraversalPath to compare
         * @return the comparison value (-1, 0, +1)
         */
        public int compare(
                TraversalPath one,
                TraversalPath two )
        {
            MeshObject objectOne = getMeshObjectFrom( one );
            MeshObject objectTwo = getMeshObjectFrom( two );

            String valueOne = objectOne.getUserVisibleString();
            String valueTwo = objectTwo.getUserVisibleString();

            if( valueOne == null && valueTwo == null ) {
                valueOne = objectOne.getIdentifier().toExternalForm();
                valueTwo = objectTwo.getIdentifier().toExternalForm();
            }
            int ret = StringHelper.compareTo( valueOne, valueTwo );
            return ret;
        }
    }
}

