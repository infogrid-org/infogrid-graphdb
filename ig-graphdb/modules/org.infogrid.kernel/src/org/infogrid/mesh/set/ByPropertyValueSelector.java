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
// Copyright 1998-2013 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.PropertyComparisonOperator;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.logging.Log;

/**
 * An implementation of MeshObjectSelector that accepts all MeshObjects which have a property
 * that compares successfully with a constant.
 */
public class ByPropertyValueSelector
        extends
            AbstractMeshObjectSelector
{
    private static final Log log = Log.getLogInstance( ByPropertyValueSelector.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @param propertyType the type of the property that is being compared
     * @param comparisonValue the value to which the property is being compared
     * @param operator the type of comparison to be made
     * @return the created ByPropertyValueSelector
     */
    public static ByPropertyValueSelector create(
            PropertyType               propertyType,
            PropertyValue              comparisonValue,
            PropertyComparisonOperator operator )
    {
        return new ByPropertyValueSelector( propertyType, comparisonValue, operator );
    }

    /**
     * Constructor.
     *
     * @param propertyType the type of the property that is being compared
     * @param comparisonValue the value to which the property is being compared
     * @param operator the type of comparison to be made
     */
    protected ByPropertyValueSelector(
            PropertyType               propertyType,
            PropertyValue              comparisonValue,
            PropertyComparisonOperator operator )
    {
        thePropertyType    = propertyType;
        theComparisonValue = comparisonValue;
        theOperator        = operator;
    }

    /**
     * Determine whether this MeshObject shall be selected.
     *
     * @param candidate MeshObject to test
     * @return true if this MeshObject is an instance of the specified type
     */
    public boolean accepts(
            MeshObject candidate )
    {
        if( candidate == null ) {
            throw new IllegalArgumentException();
        }

        try {
            PropertyValue value = candidate.getPropertyValue( thePropertyType );
            boolean       ret   = theOperator.compare( value, theComparisonValue );

            return ret;

        } catch( IllegalPropertyTypeException ex ) {
            log.error( ex );
        } catch( NotPermittedException ex ) {
            log.error( ex );
        }
        return false;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare against
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof ByPropertyValueSelector ) {
            ByPropertyValueSelector realOther = (ByPropertyValueSelector) other;

            if( !thePropertyType.equals( realOther.thePropertyType )) {
                return false;
            }
            if( PropertyValue.compare( theComparisonValue, realOther.theComparisonValue ) != 0 ) {
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * Hash code.
     *
     * @return hash code.
     */
    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 41 * hash + (this.thePropertyType != null ? this.thePropertyType.hashCode() : 0);
        hash = 41 * hash + (this.theComparisonValue != null ? this.theComparisonValue.hashCode() : 0);
        hash = 41 * hash + (this.theOperator != null ? this.theOperator.hashCode() : 0);
        return hash;
    }

    /**
     * The PropertyType for the comparison.
     */
    protected PropertyType thePropertyType;

    /**
     * The comparison value.
     */
    protected PropertyValue theComparisonValue;

    /**
     * The comparison operator.
     */
    protected PropertyComparisonOperator theOperator;
}
