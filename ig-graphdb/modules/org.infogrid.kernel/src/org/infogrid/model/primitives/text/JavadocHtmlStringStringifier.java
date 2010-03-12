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

package org.infogrid.model.primitives.text;

import org.infogrid.util.text.AbstractDelegatingStringifier;
import org.infogrid.util.text.Stringifier;

/**
 * A HtmlifyingDelegatingStringifier that escapes star-slash (the Java end-of-comment indicator)
 * so emitted Html is safe to be used in Javadoc.
 *
 * @param <T> the type of the Objects to be stringified
 */
public class JavadocHtmlStringStringifier<T>
        extends
            AbstractDelegatingStringifier<T>
{
    /**
     * Factory method.
     *
     * @param delegate the underlying Stringifier that knows how to deal with the real type
     * @return the created JavadocHtmlStringStringifier
     * @param <T> the type of the Objects to be stringified
     */
    public static <T> JavadocHtmlStringStringifier<T> create(
            Stringifier<T> delegate )
    {
        return new JavadocHtmlStringStringifier<T>( delegate );
    }

    /**
     * Constructor. Use factory method.
     *
     * @param delegate the underlying Stringifier that knows how to deal with the real type
     */
    protected JavadocHtmlStringStringifier(
            Stringifier<T> delegate )
    {
        super( delegate );
    }

    /**
     * Overridable method to possibly escape a String first.
     *
     * @param s the String to be escaped
     * @return the escaped String
     */
    protected String escape(
            String s )
    {
        String ret = s.replaceAll( "\\*/", "&#42;/" );
        return ret;
    }

    /**
     * Overridable method to possibly unescape a String first.
     *
     * @param s the String to be unescaped
     * @return the unescaped String
     */
    protected String unescape(
            String s )
    {
        String ret = s.replaceAll( "&#42;/", "\\*/" );
        return ret;
    }
}
