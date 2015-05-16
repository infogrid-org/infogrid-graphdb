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

package org.infogrid.model.traversal.xpath;

import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.util.CursorIterator;

/**
 * A namespace map for Xpaths.
 */
public interface XpathNamespaceMap
{
    /**
     * Determine whether this XpathNamespaceMap is empty.
     *
     * @return true if it is empty
     */
    public boolean isEmpty();

    /**
     * Obtain the prefix for this SubjectArea. If the SubjectArea does not have a prefix, return null.
     *
     * @param sa the Subject Area
     * @return the prefix, or null
     */
    public String getPrefixFor(
            SubjectArea sa );

    /**
     * Obtain the prefix for this SubjectArea. If the SubjectArea does not have a prefix, create a new one.
     *
     * @param sa the Subject Area
     * @return the prefix
     */
    public String obtainPrefixFor(
            SubjectArea sa );

    /**
     * Obtain the SubjectArea for this prefix.
     *
     * @param prefix the prefix
     * @return the SubjectArea, or null
     */
    public SubjectArea getSubjectAreaFor(
            String prefix );

    /**
     * Obtain an Iterator over all prefixes contained in this map.
     *
     * @return the Iterator
     */
    public CursorIterator<String> prefixIterator();
}
