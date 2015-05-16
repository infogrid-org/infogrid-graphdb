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

package org.infogrid.modelbase;

import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.traversal.TraversalToPropertySpecification;

/**
 * <p>Instances of a subclass of this class are returned from the MeshTypeLifecycleManager's
 * {@link MeshTypeLifecycleManager#createProjectedPropertyType} method. They hold the created ProjectedProperty, but
 * also allow the invoker of the createProjectedPropertyType method to, in a second step,
 * set the input Properties. This allows us to prevent others to update the input Properties
 * after the ProjectedProperty has been created. Unfortunately this two-step approach
 * is necessary for the instantiation of ProjectedProperties.</p>
 *
 * <p>Subclasses are provided by specific implementations of the interfaces in
 * the org.infogrid.model.primitives.</p>
 */
public abstract class ProjectedPropertyTypePatcher
{
    /**
     * Constructor for subclasses only.
     *
     * @param projPropType the ProjectedPropertyType that was created
     */
    protected ProjectedPropertyTypePatcher(
            ProjectedPropertyType projPropType )
    {
        theProjectedPropertyType = projPropType;
    }
    
    /**
     * Obtain the created ProjectedPropertyType.
     *
     * @return the created ProjectedPropertyType
     */
    public final ProjectedPropertyType getProjectedPropertyType()
    {
        return theProjectedPropertyType;
    }

    /**
     * Set the input TraversalToPropertySpecification for this ProjectedPropertyTypePatcher.
     * This may only be invoked once.
     *
     * @param inputProperties the input Properties to be set
     * @throws IllegalStateException thrown if invoked more than once.
     */
    public abstract void setInputProperties(
            TraversalToPropertySpecification [] inputProperties );

    /**
     * The created ProjectedPropertyType.
     */
    protected ProjectedPropertyType theProjectedPropertyType;
}
