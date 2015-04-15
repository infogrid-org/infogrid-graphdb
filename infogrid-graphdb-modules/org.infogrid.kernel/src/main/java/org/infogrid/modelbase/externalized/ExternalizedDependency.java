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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.modelbase.externalized;

import org.infogrid.model.primitives.StringValue;

/**
 * This is data wanting to become a dependency, during reading.
 */
public abstract class ExternalizedDependency
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setName(
            StringValue newValue ) 
    {
        theName = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public StringValue getName()
    {
        return theName;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setMinVersion(
            StringValue newValue ) 
    {
        theMinVersion = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public StringValue getMinVersion()
    {
        return theMinVersion;
    }

    /**
     * Convert to String, for user error messages.
     *
     * @return String form of this object
     */
    @Override
    public String toString()
    {
        if( theMinVersion != null ) {
            return "Dependency on module " + theName.value() + " with a minimum version of " + theMinVersion.value();
        } else {
            return "Dependency on module " + theName.value();
        }
    }

    /**
     * Name of the referenced SubjectArea.
     */
    protected StringValue theName = null;

    /**
     * Minimum version of the referenced Module.
     */
    protected StringValue theMinVersion = null;
}
