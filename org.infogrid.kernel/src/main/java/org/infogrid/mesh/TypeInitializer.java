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

package org.infogrid.mesh;

import java.lang.reflect.Method;
import java.util.Collection;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.IsDeadException;


/**
 * Subclassable helper class to initialize a MeshObject when it is blessed with
 * a new type. This is not usually relevant to an application developer.
 */
public class TypeInitializer
{
    /**
     * Constructor.
     *
     * @param obj the MeshObject
     * @param newType the type with which the MeshObject is being newly blessed.
     */
    protected TypeInitializer(
            AbstractMeshObject obj,
            EntityType         newType )
    {
        theMeshObject = obj;
        theNewType    = newType;
    }

    /**
     * Perform the initialization.
     * 
     * @param skipSettingValuesFor collection of PropertyTypes that already have values and should not be initialized
     * @param timeUpdated the timeUpdated time stamp to use, in System.currentTimeMillis() format
     */
    public void initialize(
            Collection<PropertyType> skipSettingValuesFor,
            long                     timeUpdated )
    {
        try {
            Class<?> implClass = theMeshObject.getMeshBase().getMeshBaseLifecycleManager().getImplementationClass( theNewType );

            if( implClass != null ) {
                Method   initializer = implClass.getDeclaredMethod( "initializeDefaultValues", TypeInitializer.class, Collection.class, Long.TYPE );
                initializer.invoke( null, this, skipSettingValuesFor, timeUpdated );
            }

        } catch( Exception ex ) {
            AbstractMeshObject.log.error( ex );
        }
    }
    
    /**
     * Callback from the generated code. Subclasses may override this.
     *
     * @param propertyTypes the PropertyTypes of the MeshObject to change
     * @param propertyValues the PropertyValues to set them to
     * @param skipSettingValuesFor collection of PropertyTypes that already have values and should not be initialized
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public void setPropertyValues(
            PropertyType  []         propertyTypes,
            PropertyValue []         propertyValues,
            Collection<PropertyType> skipSettingValuesFor,
            long                     timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        if( skipSettingValuesFor != null ) {
            PropertyType  [] propertyTypes2  = new PropertyType[ propertyTypes.length ];
            PropertyValue [] propertyValues2 = new PropertyValue[ propertyValues.length ];
            int count = 0;

            for( int i=0 ; i<propertyTypes.length ; ++i ) {
                if( !skipSettingValuesFor.contains( propertyTypes[i] )) {
                    propertyTypes2[count]  = propertyTypes[i];
                    propertyValues2[count] = propertyValues[i];
                    ++count;
                }
            }
            if( count < propertyTypes2.length ) {
                propertyTypes2  = ArrayHelper.copyIntoNewArray( propertyTypes2,  0, count, PropertyType.class );
                propertyValues2 = ArrayHelper.copyIntoNewArray( propertyValues2, 0, count, PropertyValue.class );
            }
            theMeshObject.internalSetPropertyValues( propertyTypes2, propertyValues2, false, false, timeUpdated );
        } else {
            theMeshObject.internalSetPropertyValues( propertyTypes, propertyValues, false, false, timeUpdated );
        }
    }

    /**
     * Perform a cascading delete.
     */
    public void cascadingDelete()
    {
        MeshBase mb = theMeshObject.getMeshBase();
        if( mb == null ) {
            return; // this is a loop, do nothing
        }
        try {
            Class<?> implClass = mb.getMeshBaseLifecycleManager().getImplementationClass( theNewType );

            if( implClass != null ) {
                TypedMeshObjectFacade facade      = theMeshObject.getTypedFacadeFor( theNewType );
                Method                initializer = implClass.getDeclaredMethod( "cascadingDelete" );
                // will throw if not found
                initializer.invoke( facade );
            }

        } catch( NoSuchMethodException ex ) {
            // do nothing
        } catch( IsDeadException ex ) {
            // do nothing
            // this can happen when MeshObjects are killed
        } catch( Exception ex ) {
            AbstractMeshObject.log.error( ex );
        }
    }

    /**
     * The MeshObject to be initialized.
     */
    protected AbstractMeshObject theMeshObject;

    /**
     * The type with which the MeshObject is to be newly blessed.
     */
    protected EntityType theNewType;
}
