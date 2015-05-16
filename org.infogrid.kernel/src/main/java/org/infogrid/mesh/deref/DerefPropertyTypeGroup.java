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

package org.infogrid.mesh.deref;

import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.IllegalPropertyValueException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
  * <p>A pair of MeshObject and one or more PropertyTypes,
  *    representing one or more properties of this MeshObject.</p>
  *
  * <p>Note: while the sequence in the array has no semantic meaning for the DerefPropertyTypeGroup,
  *    it is essential in getter and setting functions to determine which property
  *    has which value.</p>
  */
public final class DerefPropertyTypeGroup
        extends
            DerefPropertyTypeOrGroup
        implements
            CanBeDumped
{
    private static final Log log = Log.getLogInstance(DerefPropertyTypeGroup.class); // our own, private logger

    /**
      * Constructor.
      *
      * @param mo the MeshObject
      * @param ptGroup the PropertyTypeGroup
      */
    public DerefPropertyTypeGroup(
            MeshObject        mo,
            PropertyTypeGroup ptGroup )
    {
        super( mo );
        thePropertyTypeGroup = ptGroup;
    }

    /**
      * Obtain the PropertyTypeGroup.
      *
      * @return the PropertyTypeGroup
      */
    public PropertyTypeGroup getPropertyTypeGroup()
    {
        return thePropertyTypeGroup;
    }

    /**
      * Obtain the values of the properties contained in the PropertyTypeGroup
      * for this MeshObject, in the same sequence as getPropertyTypeGroup().getPropertyTypes().
      *
      * @return the values of the properties contained in the PropertyGroup for this MeshObject
      * @throws NotPermittedException thrown if the caller did not have sufficient access rights
      * @see #setValues
      */
    public PropertyValue [] getValues()
        throws
            NotPermittedException
    {
        PropertyType  [] pts = thePropertyTypeGroup.getContainedPropertyTypes();
        PropertyValue [] ret = new PropertyValue[ pts.length ];

        for( int i=pts.length-1 ; i>=0 ; --i ) {
            try {
                ret[i] = theMeshObject.getPropertyValue( pts[i] );

            } catch( IllegalPropertyTypeException ex ) {
                log.error( ex );
                ret[i] = null;
            }
        }
        return ret;
    }

    /**
     * Set new values for the properties contained in the PropertyTypeGroup
     * for this MeshObject, in the same sequence as getPropertyTypes().
     * 
     * @param newValues the new values for the properties on this MeshObject
     * @throws NotPermittedException thrown if the caller did not have sufficient access rights
     * @throws IllegalPropertyValueException thrown if the specified value is an illegal
     *         value for this PropertyType
     * @throws TransactionException thrown if this method is not invoked between
     *         proper Transaction boundaries
     * @see #getValues
     */
    public void setValues(
            PropertyValue [] newValues )
        throws
            NotPermittedException,
            IllegalPropertyValueException,
            TransactionException
    {
        PropertyType [] pts = thePropertyTypeGroup.getContainedPropertyTypes();

        log.assertLog( newValues.length == pts.length, "invalid length of new value for PropertyTypeGroup" );

        for( int i=pts.length-1 ; i>=0 ; --i ) {
            try {
                theMeshObject.setPropertyValue( pts[i], newValues[i] );

            } catch( IllegalPropertyTypeException ex ) {
                log.error( ex );
            }
        }
    }

    /**
     * A convenience function that sets the value of the properties contained
     * in the PropertyTypeGroup to a reasonable default. The reasonable default is obtained
     * from the PropertyType, and if not present, from the underlying DataType.
     * This is particularly useful when a default value is needed when the
     * property was previously null for this MeshObject.
     * 
     * @throws NotPermittedException thrown if the caller did not have sufficient access rights
     * @throws TransactionException thrown if this method is not invoked between
     *         proper Transaction boundaries
     */
    public void setDefaultValue()
        throws
            NotPermittedException,
            TransactionException
    {
        PropertyType [] pts = thePropertyTypeGroup.getContainedPropertyTypes();

        for( int i=pts.length-1 ; i>=0 ; --i ) {
            try {
                theMeshObject.setPropertyValue(
                        pts[i],
                        pts[i].getDataType().instantiate() );

            } catch( IllegalPropertyTypeException ex ) {
                log.error( ex );
            } catch( IllegalPropertyValueException ex ) {
                log.error( ex );
            }
        }
    }

    /**
      * Determine equality.
      *
      * @param other the object to check against
      * @return true if the two objects are equal
      */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof DerefPropertyTypeGroup ) {
            DerefPropertyTypeGroup realOther = (DerefPropertyTypeGroup) other;

            if( ! theMeshObject.equals( realOther.theMeshObject )) {
                return false;
            }
            return thePropertyTypeGroup.equals( realOther.thePropertyTypeGroup );
        }
        return false;
    }

    /**
     * Hash code.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int hash = theMeshObject.hashCode() ^ thePropertyTypeGroup.hashCode();
        return hash;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "theMeshObject",
                    "thePropertyTypeGroup"
                },
                new Object[] {
                    theMeshObject,
                    thePropertyTypeGroup
                });
    }

    /**
      * The PropertyTypeGroup.
      */
    protected PropertyTypeGroup thePropertyTypeGroup;
}
