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
 * that lies within the boundaries of an interval.
 */
public class ByPropertyValueIntervalSelector
        extends
            AbstractMeshObjectSelector
{
    private static final Log log = Log.getLogInstance( ByPropertyValueIntervalSelector.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @param propertyType the type of the property that is being compared
     * @param lowerValue the lower bound of the interval
     * @param upperValue the lower bound of the interval
     * @param lowerMayEqual if true, the value may equal the lower bound to be selected
     * @param upperMayEqual if true, the value may equal the upper bound to be selected
     */
    public static ByPropertyValueIntervalSelector create(
            PropertyType  propertyType,
            PropertyValue lowerValue,
            PropertyValue upperValue )
    {
        return new ByPropertyValueIntervalSelector(
                propertyType,
                lowerValue,
                upperValue,
                true,
                false );
    }

    /**
     * Factory method.
     *
     * @param propertyType the type of the property that is being compared
     * @param lowerValue the lower bound of the interval
     * @param upperValue the lower bound of the interval
     * @param lowerMayEqual if true, the value may equal the lower bound to be selected
     * @param upperMayEqual if true, the value may equal the upper bound to be selected
     */
    public static ByPropertyValueIntervalSelector create(
            PropertyType  propertyType,
            PropertyValue lowerValue,
            PropertyValue upperValue,
            boolean       lowerMayEqual,
            boolean       upperMayEqual )
    {
        return new ByPropertyValueIntervalSelector(
                propertyType,
                lowerValue,
                upperValue,
                lowerMayEqual,
                upperMayEqual );
    }

    /**
     * Constructor.
     *
     * @param propertyType the type of the property that is being compared
     * @param lowerValue the lower bound of the interval
     * @param upperValue the lower bound of the interval
     * @param lowerMayEqual if true, the value may equal the lower bound to be selected
     * @param upperMayEqual if true, the value may equal the upper bound to be selected
     */
    protected ByPropertyValueIntervalSelector(
            PropertyType  propertyType,
            PropertyValue lowerValue,
            PropertyValue upperValue,
            boolean       lowerMayEqual,
            boolean       upperMayEqual )
    {
        thePropertyType  = propertyType;
        theLowerValue    = lowerValue;
        theUpperValue    = upperValue;
        theLowerMayEqual = lowerMayEqual;
        theUpperMayEqual = upperMayEqual;
    }

    /**
     * Determine whether this MeshObject shall be selected.
     *
     * @param candidate MeshObject to test
     * @return true if this MeshObject is an instance of the specified type
     */
    @Override
    public boolean accepts(
            MeshObject candidate )
    {
        if( candidate == null ) {
            throw new IllegalArgumentException();
        }

        try {
            PropertyValue value = candidate.getPropertyValue( thePropertyType );
            if( value == null ) {
                return false;
            }

            if( theLowerMayEqual ) {
                if( theLowerValue.compareTo( value ) > 0 ) {
                    return false;
                }
            } else {
                if( theLowerValue.compareTo( value ) >= 0 ) {
                    return false;
                }
            }
            if( theUpperMayEqual ) {
                if( value.compareTo( theUpperValue ) > 0 ) {
                    return false;
                }
            } else {
                if( value.compareTo( theUpperValue ) >= 0 ) {
                    return false;
                }
            }
            return true;

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
        if( other instanceof ByPropertyValueIntervalSelector ) {
            ByPropertyValueIntervalSelector realOther = (ByPropertyValueIntervalSelector) other;

            if( !thePropertyType.equals( realOther.thePropertyType )) {
                return false;
            }
            if( !theLowerValue.equals( realOther.theLowerValue )) {
                return false;
            }
            if( !theUpperValue.equals( realOther.theUpperValue )) {
                return false;
            }
            if( theLowerMayEqual != realOther.theLowerMayEqual ) {
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
        hash = 41 * hash + (this.theLowerValue != null ? this.theLowerValue.hashCode() : 0);
        hash = 41 * hash + (this.theUpperValue != null ? this.theUpperValue.hashCode() : 0);
        hash = 41 * hash + (this.theLowerMayEqual ? 1 : 0);
        hash = 41 * hash + (this.theUpperMayEqual ? 1 : 0);
        return hash;
    }

    /**
     * The PropertyType for the comparison.
     */
    protected PropertyType thePropertyType;

    /**
     * The lower bound of the interval.
     */
    protected PropertyValue theLowerValue;

    /**
     * The upper bound of the interval.
     */
    protected PropertyValue theUpperValue;

    /**
     * If true, the property may be equal to the lower bound to select.
     */
    protected boolean theLowerMayEqual;
    
    /**
     * If true, the property may be equal to the upper bound to select.
     */
    protected boolean theUpperMayEqual;
}
