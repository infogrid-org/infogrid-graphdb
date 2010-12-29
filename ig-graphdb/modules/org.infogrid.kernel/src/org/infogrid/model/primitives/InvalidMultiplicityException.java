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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

/**
 * Thrown if the specified MultiplicityValue is illegal.
 */
public class InvalidMultiplicityException
        extends
            InvalidPropertyValueException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param min the minimum value specified
     * @param max the maximum value specified
     */
    public InvalidMultiplicityException(
            int min,
            int max )
    {
        theMin = min;
        theMax = max;
    }
    
    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */    
    public Object [] getLocalizationParameters()
    {
        return new Object[] {
            theMin != MultiplicityValue.N ? String.valueOf( theMin ) : MultiplicityValue.N_SYMBOL,
            theMax != MultiplicityValue.N ? String.valueOf( theMax ) : MultiplicityValue.N_SYMBOL,
        };
    }
    
    /**
     * The minimum value.
     */
    protected int theMin;
    
    /**
     * The maximum value.
     */
    protected int theMax;
}
