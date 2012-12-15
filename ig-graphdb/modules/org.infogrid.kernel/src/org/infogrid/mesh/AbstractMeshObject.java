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

package org.infogrid.mesh;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.infogrid.mesh.security.PropertyReadOnlyException;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.meshbase.transaction.MeshObjectBecameDeadStateEvent;
import org.infogrid.meshbase.transaction.MeshObjectNeighborAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectNeighborRemovedEvent;
import org.infogrid.meshbase.transaction.MeshObjectPropertyChangeEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectRoleRemovedEvent;
import org.infogrid.meshbase.transaction.MeshObjectTypeAddedEvent;
import org.infogrid.meshbase.transaction.MeshObjectTypeRemovedEvent;
import org.infogrid.meshbase.transaction.NotWithinTransactionBoundariesException;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.meshbase.transaction.TransactionException;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.Role;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.PropertyTypeNotFoundException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.FlexiblePropertyChangeListenerSet;
import org.infogrid.util.Identifier;
import org.infogrid.util.IsDeadException;
import org.infogrid.util.ZeroElementCursorIterator;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
 * This Collects functionality that is probably useful to all implementations
 * of {@link MeshObject}. Its use by implementations, however, is optional.
 */
public abstract class AbstractMeshObject
        implements
            MeshObject,
            CanBeDumped
{
    static final Log log = Log.getLogInstance( AbstractMeshObject.class ); // our own, private logger

    /**
     * Constructor, for subclasses only.
     * 
     * @param identifier the value for the Identifier
     * @param meshBase the MeshBase that this MeshObject belongs to
     * @param created the time this MeshObject was created
     * @param updated the time this MeshObject was last updated
     * @param read the time this MeshObject was last read
     * @param expires the time this MeshObject will expire
     */
    public AbstractMeshObject(
            MeshObjectIdentifier identifier,
            MeshBase             meshBase,
            long                 created,
            long                 updated,
            long                 read,
            long                 expires )
    {
        theIdentifier   = identifier;
        theMeshBase     = meshBase;
        
        theTimeCreated  = created;
        theTimeUpdated  = updated;
        theTimeRead     = read;
        theTimeExpires  = expires;
    }

    /**
      * Obtain the globally unique identifier of this MeshObject.
      *
      * @return the globally unique identifier of this MeshObject
      */
    public MeshObjectIdentifier getIdentifier()
    {
        return theIdentifier;
    }

    /**
     * Determine whether this object is being identified with the provided Identifier.
     * This is a useful method for those objects of type HasIdentifier that may listen
     * to multiple names.
     *
     * @param toTest the Identifier to test against
     * @return true if this HasIdentifier is being identified by the provided Identifier
     */
    public boolean isIdentifiedBy(
            Identifier toTest )
    {
        return theIdentifier.equals( toTest );
    }

    /**
     * Obtain the MeshBase that contains this MeshObject. This is immutable for the
     * lifetime of this instance.
     *
     * @return the MeshBase that contains this MeshObject.
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * Obtain the time of creation of this MeshObject. This is immutable for the
     * lifetime of the MeshObject.
     *
     * @return the time this MeshObject was created in <code>System.currentTimeMillis()</code> format
     */
    public final long getTimeCreated()
    {
        return theTimeCreated;
    }

    /**
     * Obtain the time of last update of this MeshObject. This changes automatically
     * every time the MeshObject is changed.
     *
     * @return the time this MeshObject was last updated in <code>System.currentTimeMillis()</code> format
     */
    public final long getTimeUpdated()
    {
        return theTimeUpdated;
    }

    /**
     * Obtain the time of the last reading operation of this MeshObject. This changes automatically
     * every time the MeshObject is read.
     *
     * @return the time this MeshObject was last read in <code>System.currentTimeMillis()</code> format
     */
    public final long getTimeRead()
    {
        return theTimeRead;
    }

    /**
     * Set the time when this MeshObject expires. If -1, it never does.
     *
     * @param newValue the new value, in <code>System.currentTimeMillis()</code> format
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public final void setTimeExpires(
            long newValue )
        throws
            NotPermittedException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "setTimeExpires", newValue );
        }
        checkPermittedSetTimeExpires( newValue );
        
        theTimeExpires = newValue;
    }

    /**
     * Obtain the time when this MeshObject expires. If this returns -1, it never does.
     * This may return a time before the present, indicating that the MeshObject is stale
     * and needs to be refreshed in some fashion. Being stale is different from being dead
     * (see {@link #getIsDead}).
     *
     * @return the time at which this MeshObject expires, in <code>System.currentTimeMillis()</code> format
     */
    public final long getTimeExpires()
    {
        return theTimeExpires;
    }

    /**
     * Determine whether this MeshObject is dead and should not be used any further.
     *
     * @return true if the MeshObject is dead
     */
    public final boolean getIsDead()
    {
        MeshBase mb = theMeshBase; // this allows us not to synchronize this method
        
        if( mb == null ) {
            return true; // the object itself has been deleted or purged
        }
        if( mb.isDead() ) {
            return true; // the whole MeshBase died
        }
        return false;
    }

    /**
     * Throw an IsDeadException if this MeshObject is dead and should not be used any further.
     * Does nothing as long as this MeshObject is alive.
     *
     * @throws IsDeadException thrown if this MeshObject is dead already
     */
    public final void checkAlive()
        throws
            IsDeadException
    {
        if( getIsDead() ) {
            throw new IsDeadException( this );
        }
    }

    /**
     * Determine whether this MeshObject is the home object of its MeshBase.
     * 
     * @return true if it is the home object
     */
    public final boolean isHomeObject()
    {
        MeshObject home = theMeshBase.getHomeObject();
        boolean    ret  = home == this;
        return ret;
    }

    /**
     * Obtain the value of a Property.
     *
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @return the current value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #setPropertyValue
     */
    public PropertyValue getPropertyValue(
            PropertyType thePropertyType )
        throws
            IllegalPropertyTypeException,
            NotPermittedException
    {
        checkAlive();

        if( thePropertyType == null ) {
            throw new NullPointerException( "Null PropertyType" );
        }

        EntityType requiredType = (EntityType) thePropertyType.getAttributableMeshType();
        boolean    found        = false;

        synchronized( this ) {
            
            if( theMeshTypes != null ) {
                for( AttributableMeshType type : theMeshTypes.keySet() ) {
                    if( type.equalsOrIsSupertype( requiredType )) {
                        found = true;
                        break;
                    }
                }
            }
            if( !found ) {
                throw new IllegalPropertyTypeException( this, thePropertyType );
            }
            
            checkPermittedGetProperty( thePropertyType );

            updateLastRead();

            PropertyValue ret;
            if( theProperties != null ) {
                ret = theProperties.get( thePropertyType );
            } else {
                ret = null;
            }
            return ret;
        }
    }

    /**
     * Obtain all PropertyValues for the PropertyTypes provided, in the same sequence as the provided
     * PropertyTypes. If a PropertyType does not exist on this MeshObject, or if access to one of the
     * PropertyTypes is not permitted, this will throw an exception. This is a convenience method.
     *
     * @param thePropertyTypes the PropertyTypes
     * @return the PropertyValues, in the same sequence as PropertyTypes
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public PropertyValue [] getPropertyValues(
            PropertyType [] thePropertyTypes )
        throws
            IllegalPropertyTypeException,
            NotPermittedException
    {
        checkAlive();

        if( thePropertyTypes == null ) {
            throw new NullPointerException();
        }

        synchronized( this ) {
            for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
                EntityType requiredType = (EntityType) thePropertyTypes[i].getAttributableMeshType();
                boolean    found        = false;
            
                if( theMeshTypes != null ) {
                    for( AttributableMeshType type : theMeshTypes.keySet() ) {
                        if( type.equalsOrIsSupertype( requiredType )) {
                            found = true;
                            break;
                        }
                    }
                }
                if( !found ) {
                    throw new IllegalPropertyTypeException( this, thePropertyTypes[i] );
                }
            }

            for( PropertyType currentType : thePropertyTypes ) {
                checkPermittedGetProperty( currentType );
            }

            updateLastRead();
            
            PropertyValue [] ret = new PropertyValue[ thePropertyTypes.length ];
            if( theProperties != null ) {
                for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
                    ret[i] = theProperties.get( thePropertyTypes[i] );
                }
            }
            return ret;
        }
    }
    
    /**
     * Set the value of a Property.
     * 
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @param newValue the new value of the Property
     * @return old value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #getPropertyValue
     */
    public PropertyValue setPropertyValue(
            PropertyType  thePropertyType,
            PropertyValue newValue )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        return setPropertyValue( thePropertyType, newValue, 0 );
    }

    /**
     * Set the value of a Property, and specify a time when that change happened. The caller must
     * have the appropriate rights to invoke this; typical callers do not have the rights because this
     * call is mostly intended for system-internal purposes.
     * 
     * @param thePropertyType the PropertyType that identifies the correct Property of this MeshObject
     * @param newValue the new value of the Property
     * @param timeUpdated the time at which this change occurred. -1 means "don't update" and 0 means "current time".
     * @return old value of the Property
     * @throws IllegalPropertyTypeException thrown if the PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @see #getPropertyValue
     */
    public PropertyValue setPropertyValue(
            PropertyType  thePropertyType,
            PropertyValue newValue,
            long          timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        return internalSetPropertyValues(
                new PropertyType[] { thePropertyType },
                new PropertyValue[] { newValue },
                true,
                timeUpdated )[0];
    }

    /**
     * Internal helper to implement a method. While on this level, it does not appear that factoring out
     * this method makes any sense, subclasses may appreciate it.
     *
     * @param thePropertyTypes the sequence of PropertyTypes to set
     * @param newValues the sequence of PropertyValues for the PropertyTypes
     * @param isMaster if true, check permissions
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 means "don't update" and 0 means "current time".
     * @return the old values of the Properties
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    protected PropertyValue [] internalSetPropertyValues(
            PropertyType []  thePropertyTypes,
            PropertyValue [] newValues,
            boolean          isMaster,
            long             timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        if( log.isDebugEnabled() ) {
            log.debug(
                    this
                    + ".internalSetPropertyValues( "
                    + ArrayHelper.join( ", ", "[", "]", "null", thePropertyTypes )
                    + ", "
                    + ArrayHelper.join( ", ", "[", "]", "null", newValues )
                    + ", "
                    + timeUpdated
                    + ", "
                    + isMaster
                    + " )" );
        }

        checkAlive();

        if( thePropertyTypes.length != newValues.length ) {
            throw new IllegalArgumentException( "PropertyTypes and PropertyValues must have same length" );
        }

        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            if( thePropertyTypes[i] == null ) {
                throw new NullPointerException();
            }
            if( isMaster && thePropertyTypes[i].getIsReadOnly().value() ) {
                throw new PropertyReadOnlyException( this, thePropertyTypes[i] );
            }
            DataType type = thePropertyTypes[i].getDataType();
            if( newValues[i] == null ) {
                if( !thePropertyTypes[i].getIsOptional().value() ) {
                    throw new IllegalPropertyValueException( this, thePropertyTypes[i], newValues[i] );
                }
            } else {
                try {
                    if( type.conforms( newValues[i] ) != 0 ) {
                        throw new IllegalPropertyValueException( this, thePropertyTypes[i], newValues[i] );
                    }
                } catch( ClassCastException ex ) {
                    throw new IllegalPropertyValueException( this, thePropertyTypes[i], newValues[i] );
                }
            }
        }

        checkTransaction();
        // it appears that if we do checkTransaction we don't need to do synchronized

        if( isMaster ) {
            for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
                checkPermittedSetProperty( thePropertyTypes[i], newValues[i] );
            }
        }

        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            EntityType requiredType = (EntityType) thePropertyTypes[i].getAttributableMeshType();
            boolean    found        = false;

            if( theMeshTypes != null ) {
                for( AttributableMeshType amt : theMeshTypes.keySet() ) {
                    if( amt.equalsOrIsSupertype( requiredType )) {
                        found = true;
                        break;
                    }
                }
            }
            if( !found ) {
                throw new IllegalPropertyTypeException( this, thePropertyTypes[i] );
            }
        }

        if( theProperties == null ) {
            theProperties = createProperties();
        }
        PropertyValue [] oldValues = new PropertyValue[ thePropertyTypes.length ];

        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            oldValues[i] = theProperties.put( thePropertyTypes[i], newValues[i] );
        }
        updateLastUpdated( timeUpdated, theTimeUpdated );

        for( int i=0 ; i<thePropertyTypes.length ; ++i ) {
            if( PropertyValue.compare( oldValues[i], newValues[i] ) != 0 ) {
                firePropertyChange( thePropertyTypes[i], oldValues[i], newValues[i], theMeshBase );
            }
        }

        return oldValues;
    }

    /**
     * Set the value of several Properties at the same time. The PropertyTypes identifying the Properties
     * and their new PropertyValues are given in the same sequence. This method sets either all values, or none.
     *
     * @param thePropertyTypes the PropertyTypes that identify the correct Properties of this MeshObject
     * @param thePropertyValues the new values of the Properties
     * @return old values of the Properties
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public PropertyValue [] setPropertyValues(
            PropertyType []  thePropertyTypes,
            PropertyValue [] thePropertyValues )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        return setPropertyValues( thePropertyTypes, thePropertyValues, 0 );
    }
            
    /**
     * Set the value of several Properties at the same time. The PropertyTypes identifying the Properties
     * and their new PropertyValues are given as paids in the Map. This method sets either all values, or none.

     * @param newValues Map of PropertyType to PropertyValue
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public void setPropertyValues(
            Map<PropertyType,PropertyValue> newValues )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        // FIXME Not a very smart implementation
        PropertyType  [] types = new PropertyType[ newValues.size() ];
        PropertyValue [] values = new PropertyValue[ types.length ];

        int i=0;
        for( PropertyType currentType : newValues.keySet() ) {
            types[i] = currentType;
            values[i] = newValues.get( currentType );
            ++i;
        }
        setPropertyValues( types, values );
    } 

    /**
     * Set the value of several Properties, given their PropertyTypes and PropertyValues, in the same sequence,
     * and specify a time when that change happened. This method sets either all values, or none.
     *
     * @param thePropertyTypes the PropertyTypes whose values we want to set
     * @param thePropertyValues the new values for the PropertyTypes for this MeshObject
     * @param timeUpdated the time at which this change occurred.  -1 means "don't update" and 0 means "current time".
     * @return old value of the Properties
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public PropertyValue [] setPropertyValues(
            PropertyType []  thePropertyTypes,
            PropertyValue [] thePropertyValues,
            long             timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        return internalSetPropertyValues( thePropertyTypes, thePropertyValues, true, timeUpdated );
    }

    /**
     * Set the value of several Properties, given their PropertyTypes and PropertyValues,
     * and specify a time when that change happened. This method sets either all values, or none.
     *
     * @param newValues Map of PropertyType to PropertyValue
     * @param timeUpdated the time at which this change occurred.  -1 means "don't update" and 0 means "current time".
     * @throws IllegalPropertyTypeException thrown if one PropertyType does not exist on this MeshObject
     *         because the MeshObject has not been blessed with a MeshType that provides this PropertyType
     * @throws IllegalPropertyValueException thrown if the new value is an illegal value for this Property
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     */
    public void setPropertyValues(
            Map<PropertyType,PropertyValue> newValues,
            long                            timeUpdated )
        throws
            IllegalPropertyTypeException,
            IllegalPropertyValueException,
            NotPermittedException,
            TransactionException
    {
        // FIXME Not a very smart implementation
        PropertyType  [] types = new PropertyType[ newValues.size() ];
        PropertyValue [] values = new PropertyValue[ types.length ];

        int i=0;
        for( PropertyType currentType : newValues.keySet() ) {
            types[i] = currentType;
            values[i] = newValues.get( currentType );
            ++i;
        }
        setPropertyValues( types, values, timeUpdated );
    }

    /**
     * Obtain the set of all PropertyTypes currently used with this MeshObject.
     *
     * @return the set of all PropertyTypes
     */
    public synchronized PropertyType [] getAllPropertyTypes()
    {
        checkAlive();

        // we cannot look at the properties table, because it may not contain keys for all of them
        if( theMeshTypes == null || theMeshTypes.isEmpty() ) {
            return new PropertyType[0];
        }
        ArrayList<PropertyType> almostRet = new ArrayList<PropertyType>();
        for( EntityType type : theMeshTypes.keySet() ) {
            PropertyType [] current = type.getAllPropertyTypes();
            for( int i=0 ; i<current.length ; ++i ) {
                if( !almostRet.contains( current[i] )) {
                    try {
                        checkPermittedGetProperty( current[i] );
                        almostRet.add( current[i] );

                    } catch( NotPermittedException ex ) {
                        log.info( ex );
                    }
                }
            }
        }
        PropertyType [] ret = ArrayHelper.copyIntoNewArray( almostRet, PropertyType.class );

        updateLastRead();
        return ret;
    }

    /**
     * <p>This is a convenience method to obtain the value of a property by providing the
     * name of the property. If such a property could be found, this call returns its value. If not,
     * it throws an MeshTypeNotFoundException.</p>
     *
     * <p>Warning: sometimes, a MeshObject may carry two Properties with the same (short) name.
     * It is undefined which of the PropertyValues this call will return.</p>
     *
     * @param propertyName the name of the property
     * @return the PropertyValue
     * @throws MeshTypeNotFoundException thrown if a Property by this name could not be found
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public PropertyValue getPropertyValueByName(
            String propertyName )
        throws
            MeshTypeNotFoundException,
            NotPermittedException
    {
        // getTypes() will checkAlive()
        
        for( EntityType current : getTypes() ) {
            PropertyType propertyType = current.findPropertyTypeByName( propertyName );
            if( propertyType != null ) {
                try {
                    PropertyValue ret = getPropertyValue( propertyType );
                    return ret;

                } catch( IllegalPropertyTypeException ex ) {
                    log.error( ex );
                }
            }
        }
        throw new PropertyTypeNotFoundException( null, propertyName );
    }

    /**
     * Traverse from this MeshObject to all directly related MeshObjects. Directly
     * related MeshObjects are those MeshObjects that are participating in a
     * relationship with this MeshObject.
     *
     * @return the set of MeshObjects that are directly related to this MeshObject
     */
    public final MeshObjectSet traverseToNeighborMeshObjects()
    {
        return traverseToNeighborMeshObjects( true );
    }

    /**
     * Obtain the MeshObjectIdentifier of the neighbors of this MeshObject. This is sometimes a
     * more efficient operation than to traverse to the neighbors and determine the
     * MeshObjectIdentifiers from there.
     *
     * @return the MeshObjectIdentifier of the neighbors, if any
     */
    public MeshObjectIdentifier[] getNeighborMeshObjectIdentifiers()
    {
        checkAlive();
        
        MeshObjectIdentifier [] ret = traverseToNeighborMeshObjects().asIdentifiers();
        return ret;
    }
    
    /**
     * Make this MeshObject support the provided EntityType.
     * 
     * @param type the new EntityType to be supported by this MeshObject
     * @throws EntityBlessedAlreadyException thrown if this MeshObject is blessed already with this EntityType
     * @throws IsAbstractException thrown if the EntityType is abstract and cannot be instantiated
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void bless(
            EntityType type )
        throws
            EntityBlessedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        bless( new EntityType[] { type } );
    }

    /**
     * Make this MeshObject support the provided one or more EntityTypes. As a result, the
     * MeshObject will either be blessed with all of the EntityTypes, or none.
     * 
     * @param types the new EntityTypes to be supported by this MeshObject
     * @throws EntityBlessedAlreadyException thrown if this MeshObject is blessed already with at least one of these EntityTypes
     * @throws IsAbstractException thrown if at least one of the EntityTypes is abstract and cannot be instantiated
     * @throws TransactionException thrown if invoked outside of proper transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void bless(
            EntityType [] types )
        throws
            EntityBlessedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        internalBless( types, true, true, false, 0L );
    }
    
    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param types the EntityTypes to bless the MeshObject with
     * @param isMaster if true, this is the master replica
     * @param checkIsAbstract if false, do not check whether or not the EntityTypes are abstract
     * @param forgiving if true, attempt to ignore errors
     * @param timeUpdated the value for the timeUpdated property after this operation.  -1 means "don't update" and 0 means "current time".
     * @throws EntityBlessedAlreadyException thrown if this MeshObject is blessed already with at least one of these EntityTypes
     * @throws IsAbstractException thrown if at least one of the EntityTypes is abstract and cannot be instantiated
     * @throws TransactionException thrown if invoked outside of proper transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected void internalBless(
            EntityType [] types,
            boolean       isMaster,
            boolean       checkIsAbstract,
            boolean       forgiving,
            long          timeUpdated )
        throws
            EntityBlessedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        if( log.isDebugEnabled() ) {
            log.debug(
                    this
                    + ".internalBless( "
                    + ArrayHelper.join( ", ", "[", "]", "null", types )
                    + ", "
                    + isMaster
                    + ", "
                    + checkIsAbstract
                    + ", "
                    + forgiving
                    + " )" );
        }

        checkAlive();

        for( int i=0 ; i<types.length ; ++i ) {
            if( types[i] == null ) {
                throw new IllegalArgumentException( "null EntityType at index " + i );
            }
            if( !types[i].getIsAbstract().value() ) {
                continue;
            }
            if( checkIsAbstract ) {
                throw new IsAbstractException( this, types[i] );
            }
            if( !types[i].getMayBeUsedAsForwardReference().value() ) {
                throw new IsAbstractException( this, types[i] );
            }
        }

        theMeshBase.checkTransaction();
        //  if we do checkTransaction, it does not seem we need to do synchronized

        if( isMaster ) {
            checkPermittedBless( types );
        }

        ArrayList<EntityType> oldTypes = new ArrayList<EntityType>();

        // throw Exception if the new type is already here, or a supertype of an existing one.
        // However, subtypes are allowed but need to be removed.
        if( theMeshTypes != null ) {
            EntityType toRemove = null;
            Set<EntityType> keySet = theMeshTypes.keySet();
            for( EntityType already : keySet ) {
                oldTypes.add( already );
                for( int i=0 ; i<types.length ; ++i ) {
                    if( !forgiving && already.isSubtypeOfOrEquals( types[i] )) {
                        throw new EntityBlessedAlreadyException( this, types[i] );
                    } else if( types[i].isSubtypeOfDoesNotEqual( already )) {
                        toRemove = already;
                    }
                }
            }
            if( toRemove != null ) {
                if( theMeshTypes.size() == 1 ) {
                    theMeshTypes = null;
                } else {
                    theMeshTypes.remove( toRemove );
                }
            }
        }
        if( theMeshTypes == null ) {
            theMeshTypes = createMeshTypes();
        }
        boolean doTimeUpdate = false;
        for( int i=0 ; i<types.length ; ++i ) {
            WeakReference<TypedMeshObjectFacade> already = theMeshTypes.get( types[i] );
            if( already == null ) { // otherwise we have it already
                theMeshTypes.put( types[i], null );
            }
            doTimeUpdate = true;
        }
        if( doTimeUpdate ) {
            updateLastUpdated();
        }

        fireTypesAdded(
                ArrayHelper.copyIntoNewArray( oldTypes, EntityType.class ),
                types,
                ArrayHelper.copyIntoNewArray( theMeshTypes.keySet().iterator(), EntityType.class ),
                theMeshBase );

        for( int i=0 ; i<types.length ; ++i ) {
            TypeInitializer init = createTypeInitializer( types[i] );
            init.initialize( theTimeUpdated );
        }
    }

    /**
     * Obtain a type initializer. This may be overridden by subclasses.
     *
     * @param type the EntityType for which a TypeInitializer is to be found
     * @return the TypeInitializer
     */
    public TypeInitializer createTypeInitializer(
             EntityType type )
    {
         return new TypeInitializer( this, type );
    }

    /**
     * Makes this MeshObject stop supporting the provided EntityType. This may fail with an
     * RoleTypeRequiresEntityTypeException because the RoleType of a relationship in which
     * this MeshObject participates requires this MeshObject to have the EntityType
     * that is supposed to be unblessed. To avoid this, unbless the relevant relationship(s)
     * first.
     * 
     * @param type the EntityType that the MeshObject will stop supporting
     * @throws RoleTypeRequiresEntityTypeException thrown if this MeshObject plays a role that requires the MeshObject to remain being blessed with this EntityType
     * @throws EntityNotBlessedException thrown if this MeshObject does not currently support this EntityType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unbless(
            EntityType type )
        throws
            RoleTypeRequiresEntityTypeException,
            EntityNotBlessedException,
            TransactionException,
            NotPermittedException
    {
        unbless( new EntityType[] { type } );
    }

    /**
     * Makes this MeshObject stop supporting all of the provided EntityTypes. As a result,
     * the MeshObject will either be unblessed from all of the EntityTypes, or none. 
     * This may fail with an
     * RoleTypeRequiresEntityTypeException because the RoleType of a relationship in which
     * this MeshObject participates requires this MeshObject to have the EntityType
     * that is supposed to be unblessed. To avoid this, unbless the relevant relationship(s) first.
     * 
     * @param types the EntityTypes that the MeshObject will stop supporting
     * @throws RoleTypeRequiresEntityTypeException thrown if this MeshObject plays one or more roles that requires the MeshObject to remain being blessed with at least one of the EntityTypes
     * @throws EntityNotBlessedException thrown if this MeshObject does not support at least one of the given EntityTypes
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unbless(
            EntityType [] types )
        throws
            RoleTypeRequiresEntityTypeException,
            EntityNotBlessedException,
            TransactionException,
            NotPermittedException
    {
        internalUnbless( types, true, 0 );
    }
    
    /**
     * Internal helper to implement a method. While on this level, it does not appear that
     * factoring out this method makes any sense, subclasses may appreciate it.
     * 
     * @param types the EntityTypes that the MeshObject will stop supporting
     * @param isMaster if true, this is the master replica
     * @param timeUpdated the value for the timeUpdated property after this operation. -1 means "don't update" and 0 means "current time".
     * @throws RoleTypeRequiresEntityTypeException thrown if this MeshObject plays one or more roles that requires the MeshObject to remain being blessed with at least one of the EntityTypes
     * @throws EntityNotBlessedException thrown if this MeshObject does not support at least one of the given EntityTypes
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    protected synchronized void internalUnbless(
            EntityType [] types,
            boolean       isMaster,
            long          timeUpdated )
        throws
            RoleTypeRequiresEntityTypeException,
            EntityNotBlessedException,
            TransactionException,
            NotPermittedException
    {
        if( log.isDebugEnabled() ) {
            log.debug(
                    this
                    + ".internalBless( "
                    + ArrayHelper.join( ", ", "[", "]", "null", types )
                    + ", "
                    + isMaster
                    + " )" );
        }

        checkAlive();

        if( types == null || types.length == 0 ) {
            return;
        }
        theMeshBase.checkTransaction();

        if( isMaster ) {
            checkPermittedUnbless( types );
        }

        if( theMeshTypes == null ) {
            throw new EntityNotBlessedException( this, types[0] );
        }
        Set<EntityType> keySet = theMeshTypes.keySet();
        for( int i=0 ; i<types.length ; ++i ) {
            if( !keySet.contains( types[i] )) {
                 throw new EntityNotBlessedException( this, types[i] );
            }
        }
        
        EntityType [] oldTypes       = getTypes();
        EntityType [] remainingTypes = ArrayHelper.removeIfPresent( oldTypes, types, false, EntityType.class );
        
        for( Role role : getRoles() ) {

            EntityType requiredType = role.getRoleType().getEntityType();
            if( requiredType != null ) {
                boolean found = false;
                for( int i=0 ; i<remainingTypes.length ; ++i ) {

                    if( remainingTypes[i].equalsOrIsSupertype( requiredType )) {
                        found = true;
                        break;
                    }
                }
                if( !found ) {
                    throw new RoleTypeRequiresEntityTypeException( this, requiredType, role.getRoleType(), role.getNeighborIdentifier() );
                }
            }
        }
        for( int i=0 ; i<types.length ; ++i ) {
            keySet.remove( types[i] );
        }

        // remove unnecessary properties
        HashMap<PropertyType,PropertyValue> removedProperties = null;
        if( theProperties != null ) {
            PropertyType [] remainingProperties = getAllPropertyTypes();
            removedProperties = new HashMap<PropertyType,PropertyValue>();
            for( Map.Entry<PropertyType,PropertyValue> current : theProperties.entrySet() ) {
                if( !ArrayHelper.isIn( current.getKey(), remainingProperties, false )) {
                    removedProperties.put( current.getKey(), current.getValue() );
                }
            }
            for( PropertyType current : removedProperties.keySet() ) {
                theProperties.remove( current );
            }
            if( theProperties.isEmpty() ) {
                theProperties = null;
            }
        }
        
        updateLastUpdated( timeUpdated, theTimeUpdated );

        fireTypesRemoved(
                oldTypes,
                types,
                getTypes(),
                removedProperties,
                theMeshBase );
    }

    /**
      * Obtain the EntityTypes that this MeshObject is currently blessed with.
      *
      * @return the types of this MeshObject
      */
    public synchronized EntityType [] getTypes()
    {
        checkAlive();

        updateLastRead();

        if( theMeshTypes != null ) {
            Set<EntityType> keySet = theMeshTypes.keySet();
            EntityType []   ret    = new EntityType[ keySet.size() ];

            int index = 0;
            for( EntityType current : keySet ) {
                try {
                    checkPermittedBlessedBy( current );
                    ret[ index++ ] = current;
                } catch( NotPermittedException ex ) {
                    log.info( ex );
                }
            }
            if( index < ret.length ) {
                ret = ArrayHelper.copyIntoNewArray( ret, 0, index, EntityType.class );
            }
            
            return ret;
        } else {
            return new EntityType[0];
        }
    }

    /**
     * Determine whether this MeshObject currently supports this EntityType.
     * By default, this returns true even if the MeshObject is blessed by a
     * subtype of the provided EntityType instead of the EntityType directly.
     * 
     * @param type the EntityType to look for
     * @return true if this MeshObject supports this MeshType or a subtype
     */
    public final boolean isBlessedBy(
            EntityType type )
    {
        return isBlessedBy( type, true );
    }
    
    /**
     * Determine whether this MeshObject currently supports this EntityType.
     * Specify whether or not subtypes of the provided EntityType should be considered.
     * 
     * @param type the EntityType to look for
     * @param considerSubtypes if true, return true even if only a subtype matches
     * @return true if this MeshObject supports this MeshType
     */
    public synchronized boolean isBlessedBy(
            EntityType type,
            boolean    considerSubtypes )
    {
        checkAlive();

        try {
            checkPermittedBlessedBy( type );

            updateLastRead();
            
            if( theMeshTypes != null ) {
                Set<EntityType> keySet = theMeshTypes.keySet();

                if( considerSubtypes ) {
                    for( EntityType actualBlessed : keySet ) {
                        if( actualBlessed.isSubtypeOfOrEquals( type )) {
                            return true;
                        }
                    }
                } else {
                    for( EntityType actualBlessed : keySet ) {
                        if( actualBlessed.equals( type )) {
                            return true;
                        }
                    }
                }
            }
        } catch( NotPermittedException ex ) {
            log.info( ex ); 
            // caller is not allowed to know
        }
        return false;
    }

   /**
     * Determine the specific subtypes of the provided EntityType with which this
     * MeshObject has been blessed. If this MeshObject has not been blessed with a
     * subtype of the provided EntityType, return a zero-length array. If this MeshObject
     * has not been blessed with the provided EntityType either, throw an
     * EntityNotBlessedException.
     *
     * @param type the EntityType
     * @return the subtypes, if any
     * @throws EntityNotBlessedException thrown if the MeshObject is not blessed by the EntityType
     * @see #determineSingleBlessedSubtype
     */
    public EntityType [] determineBlessedSubtypes(
            EntityType type )
        throws
            EntityNotBlessedException
    {
        checkAlive();

        try {
            checkPermittedBlessedBy( type );
        } catch( NotPermittedException ex ) {
            log.info( ex ); 
            // caller is not allowed to know
            return null;
        } finally {
            updateLastRead();
        }
        
        if( !isBlessedBy( type )) {
            throw new EntityNotBlessedException( this, type );
        }
        // This means theMeshTypes != null

        Set<EntityType> keySet = theMeshTypes.keySet();

        int        count    = 0;
        EntityType found [] = new EntityType[ keySet.size() ];

        for( EntityType actualBlessed : keySet ) {
            if( actualBlessed.isSubtypeOfDoesNotEqual( type )) {
                found[ count++ ] = actualBlessed;
            }
        }
        if( count < found.length ) {
            found = ArrayHelper.copyIntoNewArray( found, 0, count, EntityType.class );
        }
        return found;
    }

    /**
     * Convenience method to determine the single subtype of the provided EntityType with
     * which this MeshObject has been blessed. If this MeshObject has not been blessed with a
     * subtype of the provided EntityType, return <code>null</code>. If it has
     * been blessed with more than one subtype, throw an IllegalStateException. If this MeshObject
     * has not been blessed with the provided EntityType either, throw an
     * EntityNotBlessedException.
     *
     * @param type the EntityType
     * @return the subtype, if any
     * @throws EntityNotBlessedException thrown if the MeshObject is not blessed by the EntityType
     * @throws IllegalStateException thrown if the MeshObject is blessed by more than one subtype
     * @see #determineBlessedSubtypes
     */
    public EntityType determineSingleBlessedSubtype(
            EntityType type )
        throws
            EntityNotBlessedException,
            IllegalStateException
    {
        EntityType [] found = determineBlessedSubtypes( type );
        switch( found.length ) {
            case 0:
                return null;
            case 1:
                return found[0];
            default:
                throw new IllegalStateException( "More than one blessed subtype found: " + ArrayHelper.arrayToString( found ));
        }
    }

    /**
     * If the provided TypedMeshObjectFacade is a facade of this instance, get the EntityType
     * that corresponds to this TypedMeshObjectFacade.
     * 
     * @param obj the TypedMeshObjectFacade
     * @return the EntityType that corresponds to this TypedMeshObjectFacade
     * @throws IllegalArgumentException thrown if the TypedMeshObjectFacade is not a facade of this MeshObject
     */
    public synchronized EntityType getTypeFor(
            TypedMeshObjectFacade obj )
        throws
            IllegalArgumentException
    {
        checkAlive();

        // This implementation is very slow (FIXME?)
        if( obj == null ) {
            throw new NullPointerException();
        }
        if( theMeshTypes == null ) {
            return null;
        }

        updateLastRead();
        
        Iterator<EntityType> iter = theMeshTypes.keySet().iterator();
        while( iter.hasNext() ) {
            EntityType                       key   = iter.next();
            Reference<TypedMeshObjectFacade> value = theMeshTypes.get( key );
            if( value != null ) {
                TypedMeshObjectFacade candidate = value.get();
                if( candidate != null && candidate == obj ) {
                    return key;
                }
            }
        }
        return null;
    }

    /**
     * Obtain an instance of (a subclass of) TypedMeshObjectFacade that provides the type-safe interface
     * to this MeshObject for a particular EntityType. Throw NotBlessedException
     * if this MeshObject does not current support this EntityType.
     * 
     * @param type the EntityType
     * @return the TypedMeshObjectFacade for this MeshObject
     * @throws EntityNotBlessedException thrown if this MeshObject does not currently support this EntityType
     */
    public synchronized TypedMeshObjectFacade getTypedFacadeFor(
            EntityType type )
        throws
            EntityNotBlessedException
    {
        checkAlive();

        try {
            checkPermittedBlessedBy( type );

            if( theMeshTypes == null ) {
                throw new EntityNotBlessedException( this, type );
            }

            Set<EntityType> keySet = theMeshTypes.keySet();

            EntityType found = null;
            for( EntityType actualBlessed : keySet ) {
                if( actualBlessed.isSubtypeOfOrEquals( type )) {
                    found = actualBlessed;
                    break; // good enough
                }
            }
            if( found == null ) {
                throw new EntityNotBlessedException( this, type );
            }
            WeakReference<TypedMeshObjectFacade> ref = theMeshTypes.get( found );
            TypedMeshObjectFacade                ret = ( ref != null ) ? ref.get() : null;

            if( ret == null ) {
                ret = theMeshBase.getMeshBaseLifecycleManager().createTypedMeshObjectFacade( this, found );
                theMeshTypes.put( found, new WeakReference<TypedMeshObjectFacade>( ret ));
            }

            updateLastRead();

            return ret;

        } catch( NotPermittedException ex ) {
            log.info( ex );
            throw new EntityNotBlessedException( this, type ); // don't leak information, so we must say that it isn't blessed
        }
    }

    /**
     * Make a relationship of this MeshObject to another MeshObject support the provided RoleType.
     * 
     * @param thisEnd the RoleType of the RelationshipType that is instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject whose relationship to this MeshObject shall be blessed
     * @throws RoleTypeBlessedAlreadyException thrown if the relationship to the other MeshObject is blessed
     *         already with this RoleType
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws IsAbstractException thrown if the RoleType belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #relateAndBless
     * @see #unrelate
     */
    public void blessRelationship(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            RoleTypeBlessedAlreadyException,
            EntityNotBlessedException,
            NotRelatedException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        blessRelationship( new RoleType[] { thisEnd }, neighbor );
    }

    /**
     * Convenience method to relate this MeshObject to another MeshObject, and bless the new relationship
     * with the provided RoleType.
     * 
     * @param thisEnd the RoleType of the RelationshipType that is instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject to which a relationship is to be created and blessed
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws IsAbstractException thrown if the provided RoleType belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #blessRelationship
     * @see #unrelate
     */
    public final void relateAndBless(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            EntityNotBlessedException,
            RelatedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        relate( neighbor );

        try {
            blessRelationship( thisEnd, neighbor );

        } catch( NotRelatedException ex ) {
            // was just created, should not happen
            log.error( ex );
        } catch( RoleTypeBlessedAlreadyException ex ) {
            // was just created, should not happen
            log.error( ex );
        }
    }

    /**
     * Convenience method to relate this MeshObject to another MeshObject, and bless the new relationship
     * with all of the provided RoleTypes. As a result, this relationship will support either all RoleTypes or none.
     * 
     * @param thisEnds the RoleTypes of the RelationshipTypes that are to be instantiated at the end that this MeshObject is attached to
     * @param neighbor the MeshObject to which a relationship is to be created and blessed
     * @throws RelatedAlreadyException thrown to indicate that this MeshObject is already related
     *         to the otherObject
     * @throws EntityNotBlessedException thrown if this MeshObject is not blessed by a requisite EntityType
     * @throws IsAbstractException thrown if one of the provided RoleTypes belongs to an abstract RelationshipType
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     * @see #relate
     * @see #blessRelationship
     * @see #unrelate
     */
    public final void relateAndBless(
            RoleType [] thisEnds,
            MeshObject  neighbor )
        throws
            EntityNotBlessedException,
            RelatedAlreadyException,
            IsAbstractException,
            TransactionException,
            NotPermittedException
    {
        relate( neighbor );

        try {
            blessRelationship( thisEnds, neighbor );

        } catch( NotRelatedException ex ) {
            // was just created, should not happen
            log.error( ex );
        } catch( RoleTypeBlessedAlreadyException ex ) {
            // was just created, should not happen
            log.error( ex );
        }
    }

    /**
     * Make a relationship of this MeshObject to another MeshObject stop supporting the provided RoleType.
     * 
     * @param thisEnd the RoleType of the RelationshipType at the end that this MeshObject is attached to, and that shall be removed
     * @param neighbor the other MeshObject whose relationship to this MeshObject shall be unblessed
     * @throws RoleTypeNotBlessedException thrown if the relationship to the other MeshObject does not support the RoleType
     * @throws NotRelatedException thrown if this MeshObject is not currently related to otherObject
     * @throws TransactionException thrown if this method is invoked outside of proper Transaction boundaries
     * @throws NotPermittedException thrown if the caller is not authorized to perform this operation
     */
    public void unblessRelationship(
            RoleType   thisEnd,
            MeshObject neighbor )
        throws
            RoleTypeNotBlessedException,
            NotRelatedException,
            TransactionException,
            NotPermittedException
    {
        unblessRelationship( new RoleType[] { thisEnd }, neighbor );
    }

    /**
      * Traverse a TraversalSpecification from this MeshObject to obtain a set of MeshObjects.
      * This will consider all MeshObjects equivalent to this one as the start MeshObject.
      *
      * @param theTraverseSpec the TraversalSpecification to traverse
      * @return the set of MeshObjects found as a result of the traversal
      */
    public final MeshObjectSet traverse(
            TraversalSpecification theTraverseSpec )
    {
        return traverse( theTraverseSpec, true );
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in. This will return only one
     * instance of the same RoleType object, even if the MeshObject participates in this RoleType
     * multiple times with different other MeshObjects.
     * 
     * @return the RoleTypes that this MeshObject currently participates in.
     */
    public final RoleType [] getRoleTypes()
    {
        return getRoleTypes( true );
    }

    /**
     * Obtain the Roles that this MeshObject currently participates in.
     *
     * @return the Roles that this MeshObject currently participates in.
     */
    public final Role [] getRoles()
    {
        return getRoles( true );
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * specified other MeshObject.
     *
     * @param neighbor the other MeshObject
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and otherObject are not related
     */
    public final RoleType [] getRoleTypes(
            MeshObject neighbor )
        throws
            NotRelatedException
    {
        return getRoleTypes( neighbor, true );
    }

    /**
     * Obtain the RoleTypes that this MeshObject currently participates in with the
     * MeshObject with the specified MeshObjectIdentifier.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the other MeshObject
     * @return the RoleTypes that this MeshObject currently participates in.
     * @throws NotRelatedException thrown if this MeshObject and the neighbor MeshObject are not related
     */
    public RoleType [] getRoleTypes(
            MeshObjectIdentifier neighborIdentifier )
        throws
            NotRelatedException
    {
        return getRoleTypes( neighborIdentifier, true );
    }

    /**
     * Obtain the MeshTypeIdentifiers of the RoleTypes that this MeshObject plays with a
     * given neighbor MeshObject identified by its MeshObjectIdentifier.
     *
     * @param neighborIdentifier the MeshObjectIdentifier of the neighbor MeshObject
     * @return the identifiers of the RoleTypes
     * @throws NotRelatedException thrown if the specified MeshObject is not actually a neighbor
     */
    public final MeshTypeIdentifier [] getRoleTypeIdentifiers(
            MeshObjectIdentifier neighborIdentifier )
        throws
            NotRelatedException
    {
        return getRoleTypeIdentifiers( neighborIdentifier, true );
    }

    /**
     * Determine whether this MeshObject's relationship to the other MeshObject is blessed
     * with a given RoleType. Also returns false if the two MeshObjects are not related.
     *
     * @param thisEnd the RoleTypes of the RelationshipTypes at the end that this MeshObject is attached to
     * @param neighbor the other MeshObject
     * @return true if this MeshObject has a relationship to the other MeshObject  and it is blessed with the given RoleType
     */
    public final boolean isRelated(
            RoleType   thisEnd,
            MeshObject neighbor )
    {
        try {
            RoleType [] allRoleTypes = getRoleTypes( neighbor );
            if( ArrayHelper.isIn( thisEnd, allRoleTypes, false )) {
                return true;
            } else {
                return false;
            }
        } catch( NotRelatedException ex ) {
            return false;
        }
    }

    /**
     * Determine whether this MeshObject is related to another MeshObject whose MeshObjectIdentifier is given is blessed
     * with a given RoleType. Also returns false if the two MeshObjects are not related.
     *
     * @param thisEnd the RoleTypes of the RelationshipTypes at the end that this MeshObject is attached to
     * @param neighborIdentifier the MeshObjectIdentifier of the other MeshObject
     * @return true if this MeshObject is currently related to otherObject
     */
    public final boolean isRelated(
            RoleType             thisEnd,
            MeshObjectIdentifier neighborIdentifier )
    {
        try {
            RoleType [] allRoleTypes = getRoleTypes( neighborIdentifier );
            if( ArrayHelper.isIn( thisEnd, allRoleTypes, false )) {
                return true;
            } else {
                return false;
            }
        } catch( NotRelatedException ex ) {
            return false;
        }
    }

    /**
     * Obtain the Identifiers of the equivalent MeshObjects. This is sometimes more efficient than
     * traversing to the equivalents, and determining the MeshObjectIdentifiers.
     *
     * @return the MeshObjectIdentifiers of the equivalents
     */
    public MeshObjectIdentifier[] getEquivalentMeshObjectIdentifiers()
    {
        MeshObjectIdentifier [] ret = getEquivalents().asIdentifiers();
        return ret;
    }

    /**
      * Determine equality. This works even if the MeshObject is dead already.
      *
      * @param other the Object to test against
      * @return true if the two Objects are equal
      */
    @Override
    public final boolean equals(
            Object other )
    {
        if( other instanceof MeshObject ) {
            MeshObject realOther = (MeshObject) other;
            if( theIdentifier.equals( realOther.getIdentifier() )) {
                return true;
            }
        } else if( other instanceof TypedMeshObjectFacade ) {
            TypedMeshObjectFacade realOther = (TypedMeshObjectFacade) other;
            if( theIdentifier.equals(  realOther.getIdentifier() )) {
                return true;
            }
        }
        return false;
    }

    /**
      * The hash code of this MeshObject is the same as the Identifier's hashCode.
      * This works even if the MeshObject is dead already.
      *
      * @return the hash code of this MeshObject
      */
    @Override
    public final int hashCode()
    {
        return theIdentifier.hashCode();
    }

    /**
     * Delete this MeshObject. This must only be invoked by our MeshObjectLifecycleManager
     * and thus is defined down here, not higher up in the inheritance hierarchy.
     * 
     * @throws TransactionException thrown if invoked outside of proper Transaction boundaries
     */
    public abstract void delete()
        throws
            TransactionException;

    /**
     * Add a PropertyChangeListener.
     * This listener is added directly to the listener list, which prevents the
     * listener from being garbage-collected before this Object is being garbage-collected.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public synchronized void addDirectPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        checkAlive();

        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
        }
        thePropertyChangeListeners.addDirect( newListener );
    }

    /**
     * Add a PropertyChangeListener.
     * This listener is added to the listener list using a <code>java.lang.ref.SoftReference</code>,
     * which allows the listener to be garbage-collected before this Object is being garbage-collected
     * according to the semantics of Java references.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public synchronized void addWeakPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        checkAlive();

        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
        }
        thePropertyChangeListeners.addWeak( newListener );
    }

    /**
     * Add a PropertyChangeListener.
     * This listener is added to the listener list using a <code>java.lang.ref.WeakReference</code>,
     * which allows the listener to be garbage-collected before this Object is being garbage-collected
     * according to the semantics of Java references.
     *
     * @param newListener the to-be-added PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #removePropertyChangeListener
     */
    public synchronized void addSoftPropertyChangeListener(
            PropertyChangeListener newListener )
    {
        checkAlive();

        if( thePropertyChangeListeners == null ) {
            thePropertyChangeListeners = new FlexiblePropertyChangeListenerSet();
        }
        thePropertyChangeListeners.addSoft( newListener );
    }

    /**
     * Remove a PropertyChangeListener.
     *
     * @param oldListener the to-be-removed PropertyChangeListener
     * @see #addDirectPropertyChangeListener
     * @see #addWeakPropertyChangeListener
     * @see #addSoftPropertyChangeListener
     */
    public synchronized void removePropertyChangeListener(
            PropertyChangeListener oldListener )
    {
        if( thePropertyChangeListeners != null ) {
            thePropertyChangeListeners.remove( oldListener );

            if( thePropertyChangeListeners.isEmpty() ) {
                thePropertyChangeListeners = null; // cleanup to not take up unnecessary memory
            }
        } else {
            log.error( "trying to remove listener from empty list" );
        }
    }

    /**
     * Determine whether there is at least one currently subscribed PropertyChangeListener.
     *
     * @return true if there is at least one currently subscribed PropertyChangeListener.
     */
    public boolean hasPropertyChangeListener()
    {
        Iterator<PropertyChangeListener> iter = propertyChangeListenersIterator();
        return iter.hasNext();
    }

    /**
     * This method returns an Iterator over the currently subscribed PropertyChangeListeners.
     *
     * @return the Iterator over the currently subscribed PropertyChangeListeners
     */
    public Iterator<PropertyChangeListener> propertyChangeListenersIterator()
    {
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;
        if( listeners == null || listeners.isEmpty() ) {
            return ZeroElementCursorIterator.<PropertyChangeListener>create();
        }
        return listeners.iterator();
    }

    /**
      * Fire an event indicating a change of a property of this MeshObject.
      *
      * @param thePropertyType the PropertyType whose value changed
      * @param oldValue the value of the PropertyValue prior to the change
      * @param newValue the value of the PropertyValue now, after the change
      * @param mb the MeshBase to use
      */
    protected void firePropertyChange(
            PropertyType  thePropertyType,
            PropertyValue oldValue,
            PropertyValue newValue,
            MeshBase      mb )
    {
        MeshObjectPropertyChangeEvent theEvent
                = new MeshObjectPropertyChangeEvent(
                        this,
                        thePropertyType,
                        oldValue,
                        newValue,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating a change in the set of neighbors of this MeshObject.
     * 
     * @param addedRoleTypes the newly added RoleTypes, if any
     * @param oldValue the MeshObjectIdentifiers of the neighbors prior to the change
     * @param added the added MeshObjectIdentifier
     * @param newValue the MeshObjectIdentifiers of the neighbors now, after the change
     * @param mb the MeshBase to use
     */
    protected void fireNeighborAdded(
            RoleType []             addedRoleTypes,
            MeshObjectIdentifier [] oldValue,
            MeshObjectIdentifier    added,
            MeshObjectIdentifier [] newValue,
            MeshBase                mb )
    {
        if( addedRoleTypes == null ) {
            addedRoleTypes = new RoleType[0];
        }
        MeshObjectNeighborAddedEvent theEvent
                = new MeshObjectNeighborAddedEvent(
                        this,
                        addedRoleTypes,
                        oldValue,
                        added,
                        newValue,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
      * Fire an event indicating a change in the set of neighbors of this MeshObject.
      *
      * @param oldValue the MeshObjectIdentifier of the neighbors prior to the change
      * @param removed the removed Identifier
      * @param newValue the MeshObjectIdentifier of the neighbors now, after the change
      * @param mb the MeshBase to use
      */
    protected void fireNeighborRemoved(
            MeshObjectIdentifier [] oldValue,
            MeshObjectIdentifier    removed,
            MeshObjectIdentifier [] newValue,
            MeshBase                mb )
    {
        MeshObjectNeighborRemovedEvent theEvent
                = new MeshObjectNeighborRemovedEvent(
                        this,
                        oldValue,
                        removed,
                        newValue,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating that one or more MeshTypes have been added to this MeshObject.
     *
     * @param oldTypes the EntityTypes prior to the change
     * @param addedTypes the added MeshTypes
     * @param newTypes the EntityTypes now, after the change
     * @param mb the MeshBase to use
     */
    protected void fireTypesAdded(
            EntityType [] oldTypes,
            EntityType [] addedTypes,
            EntityType [] newTypes,
            MeshBase      mb )
    {
        MeshObjectTypeAddedEvent theEvent
                = new MeshObjectTypeAddedEvent(
                        this,
                        oldTypes,
                        addedTypes,
                        newTypes,
                        null,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating that one or more MeshTypes have been removed from this MeshObject.
     *
     * @param oldTypes the EntityTypes prior to the change
     * @param removedTypes the removed MeshTypes
     * @param newTypes the EntityTypes now, after the change
     * @param removedProperties the PropertyTypes and their values that were removed when removing the MeshTypes
     * @param mb the MeshBase to use
     */
    protected void fireTypesRemoved(
            EntityType []                   oldTypes,
            EntityType []                   removedTypes,
            EntityType []                   newTypes,
            Map<PropertyType,PropertyValue> removedProperties,
            MeshBase                        mb )
    {
        MeshObjectTypeRemovedEvent theEvent
                = new MeshObjectTypeRemovedEvent(
                        this,
                        oldTypes,
                        removedTypes,
                        newTypes,
                        removedProperties,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating that one or more RoleTypes were added to the relationship of this
     * MeshObject to another MeshObject.
     *
     * @param oldRoleTypes the RoleTypes prior to the change
     * @param addedRoleTypes the RoleTypes that were added
     * @param newRoleTypes the RoleTypes now, after the change
     * @param neighborIdentifier the identifier of the other side of this relationship
     * @param mb the MeshBase to use
     */
    protected void fireTypesAdded(
            RoleType []          oldRoleTypes,
            RoleType []          addedRoleTypes,
            RoleType []          newRoleTypes,
            MeshObjectIdentifier neighborIdentifier,
            MeshBase             mb )
    {
        if( oldRoleTypes == null ) {
            oldRoleTypes = new RoleType[0];
        }
        MeshObjectRoleAddedEvent theEvent
                = new MeshObjectRoleAddedEvent(
                        this,
                        oldRoleTypes,
                        addedRoleTypes,
                        newRoleTypes,
                        neighborIdentifier,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating that one or more RoleTypes wwere removed from the relationship of this
     * MeshObject to another MeshObject.
     *
     * @param oldRoleTypes the RoleTypes prior to the change
     * @param removedRoleTypes the RoleTypes that were removed
     * @param newRoleTypes the RoleTypes now, after the change
     * @param neighborIdentifier the identifier of the other side of this relationship
     * @param mb the MeshBase to use
     */
    protected void fireTypesRemoved(
            RoleType []          oldRoleTypes,
            RoleType []          removedRoleTypes,
            RoleType []          newRoleTypes,
            MeshObjectIdentifier neighborIdentifier,
            MeshBase             mb )
    {
        if( newRoleTypes == null ) {
            newRoleTypes = new RoleType[0];
        }
        MeshObjectRoleRemovedEvent theEvent
                = new MeshObjectRoleRemovedEvent(
                        this,
                        oldRoleTypes,
                        removedRoleTypes,
                        newRoleTypes,
                        neighborIdentifier,
                        theTimeUpdated );

        mb.getCurrentTransaction().addChange( theEvent );

        firePropertyChange( theEvent );
    }

    /**
     * Fire an event indicating that this MeshObject was deleted.
     * We pass in the MeshBase, because the member variable has already been zero'd.
     * 
     * @param oldMeshBase the MeshBase this MeshObject used to belong to
     * @param canonicalMeshObjectName the canonical Identifier that this MeshObject used to have
     * @param time the time at which this change occurred
     */
    protected void fireDeleted(
            MeshBase             oldMeshBase,
            MeshObjectIdentifier canonicalMeshObjectName,
            long                 time )
    {
        MeshObjectBecameDeadStateEvent theEvent
                = new MeshObjectBecameDeadStateEvent(
                        this,
                        canonicalMeshObjectName,
                        time );
        
        // Let's not insert it into the transaction any more, there is no point of having
        // both MeshObjectBecameDeadStateEvent and MeshObjectDeletedEvent
        //
        // oldMeshBase.getCurrentTransaction().addChange( theEvent );
        
        firePropertyChange( theEvent );
    }

    /**
     * This internal helper fires PropertyChangeEvents.
     *
     * @param theEvent the PropertyChangeEvent to be fired
     */
    protected final void firePropertyChange(
            PropertyChangeEvent theEvent )
    {
        FlexiblePropertyChangeListenerSet listeners = thePropertyChangeListeners;

        if( listeners != null ) {
            listeners.fireEvent( theEvent );
        }
    }

    /**
     * Helper to check that we are within the proper Transaction boundaries.
     *
     * @throws TransactionException throw if this has been invoked outside of proper Transaction boundaries
     */
    public final void checkTransaction()
        throws
            TransactionException
    {
        checkAlive();

        internalCheckTransaction( theMeshBase );
    }

    /**
     * Internal helper to check that we are within the proper Transaction boundaries.
     * We pass in the MeshBase, because this may be invokved when the member variable has been zero'd out already.
     *
     * @param mb the MeshBase
     * @throws TransactionException throw if this has been invoked outside of proper Transaction boundaries
     */
    protected final void internalCheckTransaction(
            MeshBase mb )
        throws
            TransactionException
    {
        Transaction tx = mb.getCurrentTransaction();
        if( tx == null ) {
            throw new NotWithinTransactionBoundariesException( mb );
        }

        tx.checkThreadIsAllowed();
    }

    /**
     * Update the lastUpdated property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     */
    protected void updateLastUpdated()
    {
        updateLastUpdated( 0, theTimeUpdated );
    }

    /**
     * Update the lastRead property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     */
    protected void updateLastRead()
    {
        updateLastRead( 0, theTimeRead );
    }

    /**
     * Update the lastUpdated property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     *
     * @param timeUpdated the time to set to. -1 means "don't update" and 0 means "current time".
     * @param lastTimeUpdated the time this MeshObject was updated last before
     */
    protected abstract void updateLastUpdated(
            long timeUpdated,
            long lastTimeUpdated );

    /**
     * Update the lastRead property. This does not trigger an event generation -- not necessary.
     * This may be overridden.
     *
     * @param timeRead the time to set to.  -1 means "don't update" and 0 means "current time".
     * @param lastTimeRead the time this MeshObject was read last before
     */
    protected abstract void updateLastRead(
            long timeRead,
            long lastTimeRead );
    
    /**
     * Check whether it is permitted to set this MeshObject's timeExpires to the given value.
     * Subclasses may override this.
     *
     * @param newValue the proposed new value for the timeExpires
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetTimeExpires(
            long newValue )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedSetTimeExpires( this, newValue );
        }
    }

    /**
     * Check whether it is permitted to set this MeshObject's given property to the given
     * value. Subclasses may override this.
     *
     * @param thePropertyType the PropertyType identifing the property to be modified
     * @param newValue the proposed new value for the property
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedSetProperty(
            PropertyType  thePropertyType,
            PropertyValue newValue )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedSetProperty( this, thePropertyType, newValue );
        }
    }

    /**
     * Check whether it is permitted to obtain this MeshObject's given property. Subclasses
     * may override this.
     *
     * @param thePropertyType the PropertyType identifing the property to be read
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedGetProperty(
            PropertyType thePropertyType )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedGetProperty( this, thePropertyType );
        }
    }

    /**
     * Check whether it is permitted to determine whether or not this MeshObject is blessed with
     * the given type. Subclasses may override this.
     * 
     * @param type the EntityType whose blessing we wish to check
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBlessedBy(
            EntityType type )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedBlessedBy( this, type );
        }
    }

    /**
     * Check whether it is permitted to bless this MeshObject with the given EntityTypes. Subclasses
     * may override this.
     * 
     * @param types the EntityTypes with which to bless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            EntityType [] types )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedBless( this, types );
        }
    }

    /**
     * Check whether it is permitted to unbless this MeshObject from the given EntityTypes. Subclasses
     * may override this.
     * 
     * @param types the EntityTypes from which to unbless
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            EntityType [] types )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedUnbless( this, types );
        }
    }    
    
    /**
     * Check whether it is permitted to bless the relationship to the otherObject with the
     * provided RoleTypes. Subclasses
     * may override this.
     * 
     * @param thisEnds the RoleTypes to bless the relationship with
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedBless(
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        checkAlive();

        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedBless( this, thisEnds, neighborIdentifier, neighbor );
        }
    }

    /**
     * Check whether it is permitted to unbless the relationship to the otherObject from the
     * provided RoleTypes. Subclasses may override this.
     * 
     * @param thisEnds the RoleTypes to unbless the relationship from
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedUnbless(
            RoleType []          thisEnds,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        if( theMeshBase == null ) {
            return; // this is invoked on a dead object -- no checking needed
        }
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedUnbless( this, thisEnds, neighborIdentifier, neighbor );
        }
    }    
    
    /**
     * Check whether it is permitted to traverse the given RoleType from this MeshObject to the
     * given MeshObject. Subclasses may override this.
     * 
     * @param toTraverse the RoleType to traverse
     * @param neighborIdentifier identifier of the neighbor to which this MeshObject is related
     * @param neighbor neighbor to which this MeshObject is related, if it could be resolved
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedTraversal(
            RoleType             toTraverse,
            MeshObjectIdentifier neighborIdentifier,
            MeshObject           neighbor )
        throws
            NotPermittedException
    {
        checkAlive();

        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedTraversal( this, toTraverse, neighborIdentifier, neighbor );
        }
    }

    /**
     * Check whether it is permitted to make this MeshObject equivalent to another.
     * Subclasses may override this.
     * 
     * @param newEquivalentIdentifier identifier of the potential new equivalent
     * @param newEquivalent the potential new equivalent
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedAddAsEquivalent(
            MeshObjectIdentifier newEquivalentIdentifier,
            MeshObject           newEquivalent )
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedAddAsEquivalent( this, newEquivalentIdentifier, newEquivalent );
        }
    }

    /**
     * Check whether it is permitted to remove this MeshObject from the equivalence set
     * it is currently a member of.
     * Subclasses may override this.
     * 
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedRemoveAsEquivalent()
        throws
            NotPermittedException
    {
        checkAlive();
        
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedRemoveAsEquivalent( this, getRoleTypes() );
        }
    }

    /**
     * Check whether it is permitted to delete this MeshObject. This checks both whether the
     * MeshObject itself may be deleted, and whether the relationships it participates in may
     * be deleted (which in turn depends on whether the relationships may be unblessed).
     * Subclasses may override this.
     *
     * @throws NotPermittedException thrown if it is not permitted
     */
    public void checkPermittedDelete()
        throws
            NotPermittedException
    {
        checkAlive();
        
        // we need to check for unblessing all current relationships, unrelating, and unblessing ourselves
        AccessManager accessMgr = theMeshBase.getAccessManager();
        if( accessMgr != null ) {
            accessMgr.checkPermittedDelete( this );
        }
    }

    /**
     * Internal helper to allocate the theMeshTypes property.
     *
     * @return the object to become the theMeshTypes property
     */
    protected HashMap<EntityType,WeakReference<TypedMeshObjectFacade>> createMeshTypes()
    {
        return new HashMap<EntityType,WeakReference<TypedMeshObjectFacade>>();
    }

    /**
     * Internal helper to allocate the theProperties property.
     *
     * @return the object to become the theProperties property
     */
    protected HashMap<PropertyType,PropertyValue> createProperties()
    {
        return new HashMap<PropertyType,PropertyValue>();
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
                    "theIdentifier",
                    "theTimeCreated",
                    "theTimeUpdated",
                    "theTimeRead",
                    "theTimeExpires",
                    "theProperties",
                    "theMeshTypes"
                },
                new Object[] {
                    theIdentifier,
                    theTimeCreated,
                    theTimeUpdated,
                    theTimeRead,
                    theTimeExpires,
                    theProperties,
                    theMeshTypes != null ? theMeshTypes.keySet() : null
                });
    }

    /**
     * Convert to String, for debugging.
     *
     * @return String format
     */
    @Override
    public String toString()
    {
        return super.toString() + "{ id: " + theIdentifier.toExternalForm() + " }";
    }

    /**
      * The MeshBase in which this MeshObject lives. If this is null, the MeshObject
      * is dead.
      */
    protected MeshBase theMeshBase;

    /**
      * Storage for the Identifier property.
      */
    protected final MeshObjectIdentifier theIdentifier;

    /**
     * The time this MeshObject was created.
     * This is a long in System.currentTimeMillis() format.
     */
    protected long theTimeCreated;

    /**
     * The time this MeshObject was last updated.
     * This is a long in System.currentTimeMillis() format.
     */
    protected long theTimeUpdated;

    /**
     * The time this MeshObject was last read.
     * This is a long in System.currentTimeMillis() format.
     */
    protected long theTimeRead;

    /**
     * The time this MeshObject expires.
     * This is a long in System.currentTimeMillis() format.
     * If -1, it will never expire.
     */
    protected long theTimeExpires;

    /**
     * The set of properties with their types and values. This is allocated as needed.
     */
    protected HashMap<PropertyType,PropertyValue> theProperties;

    /**
     * The set of MeshTypes with their respective facades. This is allocated as needed.
     */
    protected HashMap<EntityType,WeakReference<TypedMeshObjectFacade>> theMeshTypes;

    /**
      * The current set of PropertyChangeListeners.
      */
    protected FlexiblePropertyChangeListenerSet thePropertyChangeListeners;
}
