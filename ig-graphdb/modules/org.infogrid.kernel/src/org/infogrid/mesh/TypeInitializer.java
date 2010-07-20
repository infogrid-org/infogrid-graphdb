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

package org.infogrid.mesh;

import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;

import org.infogrid.meshbase.transaction.TransactionException;

import java.lang.reflect.Method;

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
     * @param type the type with which the MeshObject is being newly blessed.
     */
    protected TypeInitializer(
            AbstractMeshObject obj,
            EntityType         type )
    {
        theMeshObject = obj;
        theType       = type;
    }

    /**
     * Perform the initialization.
     * 
     * @param timeUpdated the timeUpdated time stamp to use, in System.currentTimeMillis() format
     */
    public void initialize(
            long timeUpdated )
    {
        try {
            Class<?> implClass = theMeshObject.getMeshBase().getMeshBaseLifecycleManager().getImplementationClass( theType );

            if( implClass != null ) {
                Method   initializer = implClass.getDeclaredMethod( "initializeDefaultValues", TypeInitializer.class, Long.TYPE );
                initializer.invoke( null, this, timeUpdated );
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
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 indicates "don't change"
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public void setPropertyValues(
            PropertyType  [] propertyTypes,
            PropertyValue [] propertyValues,
            long             timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        theMeshObject.internalSetPropertyValues( propertyTypes, propertyValues, false, timeUpdated );
    }

    /**
     * Perform a cascading delete.
     */
    public void cascadingDelete()
    {
        try {
            Class<?> implClass = theMeshObject.getMeshBase().getMeshBaseLifecycleManager().getImplementationClass( theType );

            if( implClass != null ) {
                TypedMeshObjectFacade facade      = theMeshObject.getTypedFacadeFor( theType );
                Method                initializer = implClass.getDeclaredMethod( "cascadingDelete" );
                // will throw if not found
                initializer.invoke( facade );
            }

        } catch( NoSuchMethodException ex ) {
            // do nothing
        } catch( Exception ex ) {
            AbstractMeshObject.log.error( ex );
        }
    }

    /**
     * The MeshObject to be initialized.
     */
    protected AbstractMeshObject theMeshObject;

    /**
     * The type with which the MeshObject is to be initialized.
     */
    protected EntityType theType;
}