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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.text;

import org.infogrid.util.text.HtmlStringStringifier;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * A HtmlStringStringifier that escapes star-slash (the Java end-of-comment indicator)
 * so emitted Html is safe to be used in Javadoc.
 */
public class JavadocHtmlStringStringifier
        extends
            HtmlStringStringifier
{
    /**
     * Factory method.
     *
     * @return the created JavadocHtmlStringStringifier
     */
    public static JavadocHtmlStringStringifier create()
    {
        return new JavadocHtmlStringStringifier();
    }

    /**
     * No-op constructor. Use factory method.
     */
    protected JavadocHtmlStringStringifier()
    {
        // no op
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
     * @return the formatted String
     */
    @Override
    public String format(
            String                         soFar,
            String                         arg,
            StringRepresentationParameters pars )
    {
        String raw = super.format( soFar, arg, pars );

        String ret = raw.replaceAll( "\\*/", "&#42;/" );
        return ret;
    }
}
