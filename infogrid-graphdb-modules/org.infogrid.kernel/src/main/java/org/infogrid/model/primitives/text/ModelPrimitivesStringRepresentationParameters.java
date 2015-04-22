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

package org.infogrid.model.primitives.text;

import org.infogrid.util.text.StringRepresentationParameters;

/**
 * Collects parameters that may influence the formatting of a String using StringRepresentation.
 */
public interface ModelPrimitivesStringRepresentationParameters
    extends
        StringRepresentationParameters
{
    /**
     * The key into this object that identifies the MeshObject to which the rendered PropertyValue belongs,
     * if any.
     */
    public final String MESH_OBJECT = "meshObject";

    /**
     * The key into this object that identifies the PropertyType of which the rendered PropertyValue is
     * an instance, if any.
     */
    public final String PROPERTY_TYPE = "propertyType";

    /**
     * The key into this object hat identifies a Boolean which indicates whether a Property is allowed to
     * be set to null from a JSP page.
     */
    public final String ALLOW_NULL = "allow-null";

    /**
     * The key into this object that identifies the TimeZone in which the rendered PropertyValue shall be
     * shown, if any.
     */
    public final String TIME_ZONE = "tz";

    /**
     * The key into this object that identifies the default value to use for this property instead of
     * the one given in the model.
     */
    public final String DEFAULT_VALUE = "defaultValue";
}
