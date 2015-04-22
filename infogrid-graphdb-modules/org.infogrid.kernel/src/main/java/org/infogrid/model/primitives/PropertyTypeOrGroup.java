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

package org.infogrid.model.primitives;

/**
 * A PropertyTypeOrGroup is a common, abstract supertype of both PropertyType and
 * PropertyGroupType. This interface allows application developers to treat both
 * of them in a uniform manner. For example, it is very convenient in connection with
 * the method getAllPropertyTypesOrGroupsInSequence on AttributableObjectType.
 *
 * It provides the SequenceNumber property, which defines the place of this
 * PropertyTypeOrGroup in a sequence, such as the sequence needed by a property sheet.
 */
public interface PropertyTypeOrGroup
        extends
            CollectableMeshType
{
    /**
      * Obtain the AttributableMeshType on which this PropertyTypeOrGroup is defined.
      *
      * @return the AttributableMeshType on which this PropertyTypeOrGroup is defined
      */
    public AttributableMeshType getAttributableMeshType();

    /**
     * Obtain the value of the SequenceNumber property.
     *
     * @return the value of the SequenceNumber property
     */
    public FloatValue getSequenceNumber();
}
