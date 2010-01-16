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

package org.infogrid.mesh.text;

import java.util.Map;
import org.infogrid.util.text.SimpleStringRepresentationContext;
import org.infogrid.util.text.StringRepresentationContext;

/**
 * Extends SimpleStringRepresentationContext for the InfoGrid kernel.
 */
public class SimpleMeshStringRepresentationContext
        extends
            SimpleStringRepresentationContext
        implements
            MeshStringRepresentationContext
{
    /**
     * Factory method.
     *
     * @param contextObjects the objects in the context
     * @return the created SimpleStringRepresentationContext
     */
    public static SimpleMeshStringRepresentationContext create(
            Map<String,Object> contextObjects )
    {
        return new SimpleMeshStringRepresentationContext( contextObjects, null );
    }

    /**
     * Factory method.
     *
     * @param contextObjects the objects in the context
     * @param delegate the StringRepresentationContext to delegate to if a context object could not be found locally
     * @return the created SimpleStringRepresentationContext
     */
    public static SimpleMeshStringRepresentationContext create(
            Map<String,Object>          contextObjects,
            StringRepresentationContext delegate )
    {
        return new SimpleMeshStringRepresentationContext( contextObjects, delegate );
    }

    /**
     * Private constructor for subclasses only, use factory method.
     *
     * @param contextObjects the objects in the context
     * @param delegate the StringRepresentationContext to which to delegate if a context object could not be found locally
     */
    protected SimpleMeshStringRepresentationContext(
            Map<String,Object>          contextObjects,
            StringRepresentationContext delegate )
    {
        super( contextObjects, delegate );
    }
}
