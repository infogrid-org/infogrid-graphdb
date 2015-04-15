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

package org.infogrid.model.traversal.xpath;

import java.util.HashMap;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.MapCursorIterator;

/**
 * Simple implementation of SimpleXpathNamespaceMap.
 */
public class SimpleXpathNamespaceMap
        implements
            XpathNamespaceMap
{
    /**
     * Factory method.
     *
     * @return the created SimpleXpathNamespaceMap
     */
    public static SimpleXpathNamespaceMap create()
    {
        return new SimpleXpathNamespaceMap();
    }

    /**
     * Private constructor, use factory method.
     */
    protected SimpleXpathNamespaceMap()
    {
        // no op
    }

    /**
     * Determine whether this XpathNamespaceMap is empty.
     *
     * @return true if it is empty
     */
    public boolean isEmpty()
    {
        return theForwardMap.isEmpty();
    }

    /**
     * Put a value into the map.
     *
     * @param prefix the prefix
     * @param sa the SubjectArea
     * @throws XpathNamespaceConflictException if there was already a mapping
     */
    public void put(
            String      prefix,
            SubjectArea sa )
        throws
            XpathNamespaceConflictException
    {
        SubjectArea oldSa = theForwardMap.put( prefix, sa );
        if( oldSa != null ) {
            throw new XpathNamespaceConflictException( prefix, sa, oldSa );
        }
        theBackwardMap.put( sa, prefix ); // no need to check for conflicts here
    }

    /**
     * Obtain the prefix for this SubjectArea. If the SubjectArea does not have a prefix, return null.
     *
     * @param sa the Subject Area
     * @return the prefix, or null
     */
    public String getPrefixFor(
            SubjectArea sa )
    {
        String ret = theBackwardMap.get( sa );
        return ret;
    }

    /**
     * Obtain the prefix for this SubjectArea. If the SubjectArea does not have a prefix, create a new one.
     *
     * @param sa the Subject Area
     * @return the prefix
     */
    public String obtainPrefixFor(
            SubjectArea sa )
    {
        String ret = theBackwardMap.get( sa );
        if( ret == null ) {
            ret = constructNewPrefix();
            theBackwardMap.put( sa, ret );
            theForwardMap.put( ret, sa );
        }
        return ret;
    }

    /**
     * Obtain the SubjectArea for this prefix.
     *
     * @param prefix the prefix
     * @return the SubjectArea, or null
     */
    public SubjectArea getSubjectAreaFor(
            String prefix )
    {
        SubjectArea ret = theForwardMap.get( prefix );
        return ret;
    }

    /**
     * Obtain an Iterator over all prefixes contained in this map.
     *
     * @return the Iterator
     */
    public CursorIterator<String> prefixIterator()
    {
        return MapCursorIterator.createForKeys( theForwardMap, String.class, SubjectArea.class );
    }

    /**
     * Helper to construct an as-yet unused prefix.
     *
     * @return the unused prefix
     */
    protected String constructNewPrefix()
    {
        // we are counting in hex, that seems easy

        for( int i=0 ; i<26*26 ; ++i ) { // gotta stop at some reasonable number

            String candidate = new String( new char[] { (char) ((i/26) + 'a' ), (char) ((i%26) + 'a' ) } );
            if( theForwardMap.get( candidate ) == null ) {
                return candidate;
            }
        }
        throw new RuntimeException( "Ran out of prefixes" );
    }

    /**
     * Maps in one direction.
     */
    protected HashMap<String,SubjectArea> theForwardMap = new HashMap<String,SubjectArea>();

    /**
     * Maps in the other direction.
     */
    protected HashMap<SubjectArea,String> theBackwardMap = new HashMap<SubjectArea,String>();
}
