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

package org.infogrid.mesh.externalized.xml;

import org.infogrid.model.primitives.externalized.xml.PropertyValueXmlTags;

/**
 * XML Tags for the serialization of ExternalizedMeshObjects.
 */
public interface ExternalizedMeshObjectXmlTags
        extends
            PropertyValueXmlTags
{
    /** Tag indicating a MeshObject. */
    public static final String MESHOBJECT_TAG = "MO";

    /** Tag indicating the time the Proxy was created. */
    public static final String TIME_CREATED_TAG = "TC";

    /** Tag indicating the time the Proxy was updated. */
    public static final String TIME_UPDATED_TAG = "TU";

    /** Tag indicating the time the Proxy was read. */
    public static final String TIME_READ_TAG = "TR";

    /** Tag indicating the time the Proxy will expire. */
    public static final String TIME_EXPIRES_TAG = "TE";

    /** Tag indicating the Identifier of a MeshObject. */
    public static final String IDENTIFIER_TAG = "ID";
    
    /** Tag indicating meta, aka type information. */
    public static final String TYPE_TAG = "T";

    /** Tag indicating a PropertyType. */
    public static final String PROPERTY_TYPE_TAG = "PT";
    
    /** Tag indicating a Relationship. */
    public static final String RELATIONSHIP_TAG = "R";

    /** Tag indicating an equivalent */
    public static final String EQUIVALENT_TAG = "EQ";    
}
