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

package org.infogrid.modelbase.m;

import org.infogrid.model.primitives.m.MProjectedPropertyType;
import org.infogrid.model.traversal.TraversalToPropertySpecification;
import org.infogrid.modelbase.ProjectedPropertyTypePatcher;

/**
  * Utility class that patches the inputProperties field of ProjectedPropertyTypes
  * in the second pass of parsing a model.
  * In-memory implementation.
  */
public class MProjectedPropertyTypePatcher
        extends
            ProjectedPropertyTypePatcher
{
    /**
     * Constructor.
     * 
     * @param projPropertyType the ProjectedPropertyType to patch
     */
    MProjectedPropertyTypePatcher(
            MProjectedPropertyType projPropertyType )
    {
        super( projPropertyType );
    }

    /**
     * Set the input Properties for this ProjectedPropertyTypePatcher.
     * This may only be invoked once.
     *
     * @param inputProperties the input Properties to be set
     * @throws IllegalStateException thrown if invoked more than once.
     */
    @Override
    public void setInputProperties(
            TraversalToPropertySpecification [] inputProperties )
    {
        MProjectedPropertyType realProjectedPropertyType = (MProjectedPropertyType) theProjectedPropertyType;
        
        realProjectedPropertyType.setInputPropertiesDuringParsing( inputProperties );
    }
}
