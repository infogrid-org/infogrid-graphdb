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

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.ArrayHelper;

/**
 * This is data wanting to become a PropertyType, during reading. Also used as supertype
 * for ProjectedPropertyType (FIXME?).
 */
public class ExternalizedPropertyType
    extends
        ExternalizedMeshType
{
    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addToOverride(
            String newValue ) 
    {
        if( toOverride == null ) {
            toOverride = new ArrayList<String>();
        }
        toOverride.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<String> getToOverrides()
    {
        return toOverride;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setDataType(
            DataType newValue ) 
    {
        type = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public DataType getDataType()
    {
        return type;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setDefaultValue(
            PropertyValue newValue ) 
    {
        defaultValue = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public PropertyValue getDefaultValue()
    {
        return defaultValue;
    }
    
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setDefaultValueCode(
            StringValue newValue )
    {
        defaultValueCode = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public StringValue getDefaultValueCode()
    {
        return defaultValueCode;
    }
    
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIsOptional(
            BooleanValue newValue ) 
    {
        isOptional = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getIsOptional()
    {
        return isOptional;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIsReadOnly(
            BooleanValue newValue ) 
    {
        isReadOnly = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getIsReadOnly()
    {
        return isReadOnly;
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
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addLocalPropertyTypeGuardClassName(
            String newValue ) 
    {
        theLocalPropertyTypeGuardClassNames.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getLocalPropertyTypeGuardClassNames()
    {
        return ArrayHelper.copyIntoNewArray( theLocalPropertyTypeGuardClassNames, String.class );
    }

    /**
     * The list of Identifiers of PropertyTypes that this one overrides.
     */
    protected ArrayList<String> toOverride = null;

    /**
     * The DataType of this PropertyType.
     */
    protected DataType type = null;

    /**
     * DefaultValue if given.
     */
    protected PropertyValue defaultValue = null;

    /**
     * The code to initialize DefaultValue if it is to be calculated.
     */
    protected StringValue defaultValueCode = null;

    /**
     * IsOptional.
     */
    protected BooleanValue isOptional = BooleanValue.FALSE;

    /**
     * IsReadOnly.
     */
    protected BooleanValue isReadOnly = BooleanValue.FALSE;

    /**
     * SequenceNumber.
     */
    protected FloatValue sequenceNumber = FloatValue.ZERO;
    
    /**
     * Names of the classes that become the PropertyTypeGuards.
     */
    protected ArrayList<String> theLocalPropertyTypeGuardClassNames = new ArrayList<String>();
}
