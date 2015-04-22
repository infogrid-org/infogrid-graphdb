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

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.MeshTypeIdentifier;

/**
 * This is data wanting to become a PropertyTypeGroup, during reading.
 */
public class ExternalizedPropertyTypeGroup
    extends
        ExternalizedMeshType
{
    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addGroupMember(
            MeshTypeIdentifier newValue ) 
    {
        if( groupMembers == null ) {
            groupMembers = new ArrayList<MeshTypeIdentifier>();
        }
        groupMembers.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<MeshTypeIdentifier> getGroupMembers()
    {
        return groupMembers;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setSequenceNumber(
            FloatValue newValue ) 
    {
        sequenceNumber = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public FloatValue getSequenceNumber()
    {
        return sequenceNumber;
    }

    /**
     * The list of Identifiers of PropertyTypes that this PropertyTypeGroup contains.
     */
    protected ArrayList<MeshTypeIdentifier> groupMembers = null;

    /**
     * SequenceNumber.
     */
    protected FloatValue sequenceNumber = FloatValue.ZERO;
}
