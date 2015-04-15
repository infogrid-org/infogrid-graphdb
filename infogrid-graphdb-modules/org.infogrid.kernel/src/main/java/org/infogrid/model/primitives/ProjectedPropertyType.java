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

package org.infogrid.model.primitives;

import org.infogrid.model.traversal.TraversalToPropertySpecification;

/**
 * A ProjectedPropertyType is a PropertyType whose value is automatically calculated rather
 * than set.
 */
public interface ProjectedPropertyType
        extends
            PropertyType
{
    /**
     * Obtain the local TraversalToPropertySpecification that we are watching.
     *
     * @return the local TraversalToPropertySpecification that we are watching
     */
    public TraversalToPropertySpecification [] getInputProperties();

    /**
     * Obtain the projection code for this ProjectedPropertyType that is in-lined by the code generator.
     *
     * @return the Java text containing the projection code for this ProjectedPropertyType
     */
    public StringValue getProjectionCode();
}
