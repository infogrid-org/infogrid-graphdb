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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

/**
 * <p>A PropertyTypeGroup is a group of PropertyTypes, defined by the same AttributableMeshObjectType
 * (or its ancestors in the inheritance hierarchy) that logically belong together.</p>
 *
 * <p>This can be used by things like property sheets, for example, to show related PropertyTypes together.</p>
 */
public interface PropertyTypeGroup
        extends
            PropertyTypeOrGroup
{
    /**
     * Obtain the PropertyTypes that are members of this PropertyTypeGroup.
     *
     * @return the PropertyTypes that are members of this PropertyTypeGroup
     */
    public PropertyType [] getContainedPropertyTypes();
}
