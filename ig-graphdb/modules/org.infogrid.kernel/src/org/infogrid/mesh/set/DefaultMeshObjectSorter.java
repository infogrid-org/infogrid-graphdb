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
import org.infogrid.mesh.MeshObject;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.StringHelper;

/**
 * This is a default MeshObjectSorter implementation that uses the Java collections API.
 */
public class DefaultMeshObjectSorter
        implements
            MeshObjectSorter
{
    /**
     * Factory method.
     * 
     * @param c specify the comparison criteria as the Java collections API does
     * @return the created DefaultMeshObjectSorter
     */
    public static DefaultMeshObjectSorter create(
            Comparator<MeshObject> c )
    {
        return new DefaultMeshObjectSorter( c, null );
    }

    /**
     * Factory method.
     * 
     * @param c specify the comparison criteria as the Java collections API does
     * @param userName localized user-visible name in the current locale
     * @return the created DefaultMeshObjectSorter
     */
    public static DefaultMeshObjectSorter create(
            Comparator<MeshObject> c,
            String                 userName )
    {
        return new DefaultMeshObjectSorter( c, userName );
    }

    /**
     * Constructor for subclasses only.
     *
     * @param c specify the comparison criteria as the Java collections API does
     * @param userName localized user-visible name in the current locale
     */
    protected DefaultMeshObjectSorter(
            Comparator<MeshObject> c,
            String                 userName )
    {
        theComparator  = c;
        theUserName    = userName;
    }

    /**
     * Obtain the comparator that was specified at construction time.
     *
     * @return the Comparator that was specified at construction time
     */
    public Comparator<MeshObject> getComparator()
    {
        return theComparator;
    }

    /**
     * Pass in an array of MeshObjects in arbitrary order. Return the same set
     * in order.
     *
     * @param unorderedSet the passed-in unordered set, is modified to be ordered
     * @return this a convenience return value: it's the same array as was passed in
     * @see #getOrderedInNew
     */
    public MeshObject [] getOrderedInSame(
            MeshObject [] unorderedSet )
    {
        Arrays.sort( unorderedSet, theComparator );
        return unorderedSet;
    }

    /**
     * Pass in an array of MeshObjects in arbitrary order. Return a new set
     * with the same content in order.
     *
     * @param unorderedSet the passed-in unordered set, is not modified
     * @return the new array with the ordered content
     * @see #getOrderedInSame
     */
    public MeshObject [] getOrderedInNew(
            MeshObject [] unorderedSet )
    {
        return getOrderedInSame(
                ArrayHelper.copyIntoNewArray(
                        unorderedSet,
                        MeshObject.class ));
    }

    /**
     * Obtain the index where a new MeshObject would be inserted to keep the
     * passed-in array ordered according to this sorter. This does not actually
     * insert; it only asks a hypothetical question.
     *
     * @param newMeshObject MeshObject potentially to be inserted
     * @param orderedSet the set to potentially insert into
     * @return the index of newMeshObject if it was inserted into orderedSet. This may be any number
     *         from 0 to orderedSet.length.
     */
    public int getIndexOfNew(
            MeshObject    newMeshObject,
            MeshObject [] orderedSet )
    {
        int index = Arrays.binarySearch( orderedSet, newMeshObject, theComparator );
        if( index >= 0 ) {
            return index;
        } else {
            return -index-1;
        }
    }

    /**
     * Obtain the index of a MeshObject that's currently held in the array.
     * Return -1 if the object was not found in the set.
     *
     * @param objectToLookFor the object whose index we are looking for
     * @param orderedSet the ordered set in which we look
     * @return the index of objectToLookFor within orderedSet, or -1 if not found
     */
    public int getIndexOf(
            MeshObject    objectToLookFor,
            MeshObject [] orderedSet )
    {
        int index = Arrays.binarySearch( orderedSet, objectToLookFor, theComparator );
        if( index >= 0 ) {
            return index;
        } else {
            return -1;
        }
    }

    /**
     * Obtain the user visible name for this sorter in the current locale.
     *
     * @return the user-visible name for this sorted in the current locale
     */
    public String getUserName()
    {
        return theUserName;
    }

    /**
     * The Comparator that we are using to sort.
     */
    protected Comparator<MeshObject> theComparator;

    /**
     * The user-visible name for this MeshObjectSorter.
     */
    protected String theUserName;

    /**
     * Default instance of this class that sorts by the MeshObject's identifier. While
     * this is not necessarily a comprehensible ordering to the user, it exists on
     * all MeshObjects and will always remain the same.
     */
    public static final DefaultMeshObjectSorter BY_IDENTIFIER = new DefaultMeshObjectSorter(
            new Comparator<MeshObject>() {
                    public int compare(
                            MeshObject o1,
                            MeshObject o2 )
                    {
                        String id1 = o1.getIdentifier().toExternalForm();
                        String id2 = o2.getIdentifier().toExternalForm();

                        int ret = id1.compareTo( id2 );
                        return ret;
                    }

            },
            DefaultMeshObjectSorter.class.getName() + ".BY_IDENTIFIER" );

    /**
     * Default instance of this class that sorts by the MeshObject's user-visible String.
     * This comparison follows the same approach as DefaultTraversalPathSorter.ByUserVisibleStringComparator.
     */
    public static final DefaultMeshObjectSorter BY_USER_VISIBLE_STRING = new DefaultMeshObjectSorter(
            new Comparator<MeshObject>() {
                    public int compare(
                            MeshObject o1,
                            MeshObject o2 )
                    {
                        String valueOne = o1.getUserVisibleString();
                        String valueTwo = o2.getUserVisibleString();
 
                        if( valueOne == null && valueTwo == null ) {
                            valueOne = o1.getIdentifier().toExternalForm();
                            valueTwo = o2.getIdentifier().toExternalForm();
                        }

                        int ret = StringHelper.compareTo( valueOne, valueTwo );
                        return ret;
                    }

            },
            DefaultMeshObjectSorter.class.getName() + ".BY_USER_VISIBLE_STRING" );

    /**
     * Default instance of this class that sorts by the MeshObject's user-visible String in reverse order.
     */
    public static final DefaultMeshObjectSorter BY_REVERSE_USER_VISIBLE_STRING = new DefaultMeshObjectSorter(
            new Comparator<MeshObject>() {
                    public int compare(
                            MeshObject o1,
                            MeshObject o2 )
                    {
                        String valueOne = o1.getUserVisibleString();
                        String valueTwo = o2.getUserVisibleString();

                        if( valueOne == null && valueTwo == null ) {
                            valueOne = o1.getIdentifier().toExternalForm();
                            valueTwo = o2.getIdentifier().toExternalForm();
                        }

                        int ret = StringHelper.compareTo( valueOne, valueTwo );
                        return -ret; // notice the minus
                    }

            },
            DefaultMeshObjectSorter.class.getName() + ".BY_REVERSE_USER_VISIBLE_STRING" );
}
