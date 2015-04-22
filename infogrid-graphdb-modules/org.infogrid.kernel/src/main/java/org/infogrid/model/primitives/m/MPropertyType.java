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

package org.infogrid.model.primitives.m;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGuard;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.PropertyValueParsingException;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;

/**
  * Implementation of a value-holding property of an AttributableMeshObjectType.
  * In-memory implementation.
  */
public class MPropertyType
        extends
            MPropertyTypeOrGroup
        implements
            PropertyType,
            CanBeDumped
{
    private static final Log  log              = Log.getLogInstance( MPropertyType.class ); // our own, private logger
    private static final long serialVersionUID = 1L; // helps with serialization
    
    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MPropertyType(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
      * Set a value for the DataType property.
      *
      * @param newValue the new value for the property
      * @see #getDataType
      */
    public final void setDataType(
            DataType newValue )
    {
        this.theDataType = newValue;
    }

    /**
      * Obtain the value of the DataType property.
      *
      * @return the value of the DataType property
      * @see #setDataType
      */
    @Override
    public final DataType getDataType()
    {
        return theDataType;
    }

    /**
      * Set a value for the DefaultValue property.
      *
      * @param newValue the new value for the property
      * @see #getDefaultValue
      */
    public final void setDefaultValue(
            PropertyValue newValue )
    {
        this.theDefaultValue = newValue;
    }

    /**
     * Obtain the value of the DefaultValue property.
     *
     * @return the value of the DefaultValue property
     * @see #setDefaultValue
     */
    @Override
    public final PropertyValue getDefaultValue()
    {
        return theDefaultValue;
    }

    /**
      * Set a value for the DefaultValueCode property.
      *
      * @param newValue the new value for the property
      * @see #getDefaultValueCode
      */
    public final void setDefaultValueCode(
            StringValue newValue )
    {
        this.theDefaultValueCode = newValue;
    }

    /**
     * Obtain programming language code determining the default value for this PropertyType.
     * This is in-lined by the code generator.
     *
     * @return the programming language code that determines the default value for this PropertyType.
     * @see #setDefaultValueCode
     */
    @Override
    public final StringValue getDefaultValueCode()
    {
        return theDefaultValueCode;
    }

    /**
      * Set a value for the IsOptional property.
      *
      * @param newValue the new value for the property
      * @see #getIsOptional
      */
    public final void setIsOptional(
            BooleanValue newValue )
    {
        this.theIsOptional = newValue;
    }

    /**
      * Obtain the value of the IsOptional property.
      *
      * @return the value of the IsOptional property
      * @see #setIsOptional
      */
    @Override
    public final BooleanValue getIsOptional()
    {
        return theIsOptional;
    }

    /**
      * Set a value for the IsReadOnly property.
      *
      * @param newValue the new value for the property
      * @see #getIsReadOnly
      */
    public final void setIsReadOnly(
            BooleanValue newValue )
    {
        this.theIsReadOnly = newValue;
    }

    /**
     * Obtain the value of the IsReadOnly property.
     *
     * @return the value of the IsReadOnly property
     * @see #setIsReadOnly
     */
    @Override
    public final BooleanValue getIsReadOnly()
    {
        return this.theIsReadOnly;
    }

    /**
     * Set the PropertyTypes that we override (if any).
     *
     * @param newValue the PropertyTypes that we override
     * @see #getOverride
     */
    public final void setOverride(
            PropertyType [] newValue )
    {
        theOverride = newValue;
    }

    /**
     * Obtain the PropertyTypes that this PropertyType overrides, if any.
     *
     * @return the PropertyTypes that this PropertyType overrides
     * @see #setOverride
     */
    @Override
    public final PropertyType [] getOverride()
    {
        return theOverride;
    }

    /**
     * Obtain the ancestor PropertyType of the overriding hierarchy. If this PropertyType
     * does not override any other PropertyType, a call to this method returns this
     * PropertyType. Otherwise it is invoked recursively on the overridden
     * PropertyTypes, until we reach the top of the overriding hierarchy. Due to
     * topology constraints, it is known that the ancestor PropertyType is the same,
     * regardless which override path was taken in case of multiple inheritance of the
     * enclosing AttributableMeshObjectType.
     *
     * @return the ancestor of the overriding hierarchy
     */
    @Override
    public final PropertyType getOverrideAncestor()
    {
        if( theOverride.length == 0 ) {
            return this;
        } else {
            return theOverride[0].getOverrideAncestor(); // we can just pick one, they all lead to the same one
        }
    }

    /**
     * Determine whether this PropertyType overrides the passed-in PropertyType.
     *
     * @param other the PropertyType to test against
     * @return true if this PropertyType overrides the passed-in PropertyType
     */
    @Override
    public final boolean overrides(
            PropertyType other )
    {
        for( int i=0 ; i<theOverride.length ; ++i ) {
            if( theOverride[i].equals( other )) {
                return true;
            }
        }
        for( int i=0 ; i<theOverride.length ; ++i ) {
            if( theOverride[i].overrides( other )) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether this PropertyType is the same as, or overrides the passed-in
     * PropertyType. This is the method typically used to determine whether
     * or not a MeshObjectPropertyChangeEvent's PropertyType applies to
     * an event subscriber, which generally picks the PropertyType highest in the inheritance
     * hierarchy to compare against.
     *
     * @param other the PropertyType to test against
     * @return true if this PropertyType is the same as, or overrides the passed-in PropertyType
     */
    @Override
    public final boolean equalsOrOverrides(
            PropertyType other )
    {
        if( equals( other )) {
            return true;
        }
        return overrides( other );
    }

    /**
     * Obtain the set of PropertyTypeGuards locally defined on this PropertyType.
     *
     * @return the set of PropertyTypeGuards locally defined on this PropertyType
     */
    @Override
    public synchronized PropertyTypeGuard [] getLocalPropertyTypeGuards()
    {
        if( theLocalPropertyTypeGuards == null ) {
            
            ClassLoader loader         = getAttributableMeshType().getSubjectArea().getClassLoader();
            theLocalPropertyTypeGuards = new PropertyTypeGuard[ theLocalPropertyTypeGuardClassNames.length ];

            for( int i=0 ; i<theLocalPropertyTypeGuards.length ; ++i ) {
                try {
                    Class<?> clazz = Class.forName( theLocalPropertyTypeGuardClassNames[i], true, loader );
                    theLocalPropertyTypeGuards[i] = (PropertyTypeGuard) clazz.newInstance();
                
                } catch( ClassNotFoundException ex ) {
                    log.error( ex );
                } catch( IllegalAccessException ex ) {
                    log.error( ex );
                } catch( InstantiationException ex ) {
                    log.error( ex.getCause() );
                }
            }
        }
        return theLocalPropertyTypeGuards;
    }

    /**
     * Obtain the set of PropertyTypeGuards applicable on this PropertyType. These include
     * both the locally defined PropertyTypeGuards and the inherited ones.
     *
     * @return the set of applicable PropertyTypeGuards
     */
    @Override
    public synchronized PropertyTypeGuard [] getAllPropertyTypeGuards()
    {
        if( theAllPropertyTypeGuards == null ) {
            try {
                ArrayList<PropertyTypeGuard> almostRet = internalGetAllPropertyTypeGuards(); // can throw InheritanceConflictException

                theAllPropertyTypeGuards = ArrayHelper.copyIntoNewArray(
                        almostRet,
                        PropertyTypeGuard.class );
            } finally {
                if( theAllPropertyTypeGuards == null ) {
                    theAllPropertyTypeGuards = new PropertyTypeGuard[0];
                }
            }
        }
        return theAllPropertyTypeGuards;
    }

    /**
     * Obtain transitive closure of PropertyTypeGuards.
     *
     * @return an ArrayList containing a transitive closure of PropertyTypeGuards
     * @throws InheritanceConflictException thrown if a conflict is detected
     */
    protected final ArrayList<PropertyTypeGuard> internalGetAllPropertyTypeGuards()
            throws
                InheritanceConflictException
    {
        ArrayList<PropertyTypeGuard> ret = new ArrayList<PropertyTypeGuard>( 20 ); // fudge factor
        for( PropertyTypeGuard current : getLocalPropertyTypeGuards() ) {
            ret.add( current );
        }

        for( int i=0 ; i<theOverride.length ; ++i ) {
            PropertyTypeGuard [] inherited = theOverride[i].getAllPropertyTypeGuards();

            for( int j=0 ; j<inherited.length ; ++j ) {
                
                if( !ret.contains( inherited[j] )) {
                    ret.add( inherited[j] );
                }
            }
        }

        return ret;
    }
    
    /**
     * Obtain the class names of the set of PropertyTypeGuards defined locally on this PropertyType.
     *
     * @return the class names of the set of PropertyTypeGuards defined locally on this PropertyType
     */
    @Override
    public String [] getLocalPropertyTypeGuardClassNames()
    {
        return theLocalPropertyTypeGuardClassNames;
    }

    /**
     * Set the class names of the set of PropertyTypeGuards defined locally on this PropertyType.
     *
     * @param newValue the class names of the set of PropertyTypeGuards defined locally on this PropertyType
     */
    public void setLocalPropertyTypeGuardClassNames(
            String [] newValue )
    {
        theLocalPropertyTypeGuardClassNames = newValue;
    }

    /**
     * Check whether the given caller is allowed to updated the property corresponding to this PropertyType to the given
     * new value on the given subject MeshObject. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject on which the property is supposed to be set
     * @param newValue the new value for the property
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedSetProperty(
            MeshObject    subject,
            PropertyValue newValue,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        for( PropertyTypeGuard current : getAllPropertyTypeGuards() ) {
            current.checkPermittedSetProperty( subject, this, newValue, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to read the property corresponding to this PropertyType
     * on the given subject MeshObject. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject whose property is supposed to be read
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedGetProperty(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        for( PropertyTypeGuard current : getAllPropertyTypeGuards() ) {
            current.checkPermittedGetProperty( subject, this, caller );
        }
    }

    /**
     * Obtain a PropertyValue that corresponds to this PropertyType, based on the String representation
     * of the PropertyValue.
     *
     * @param representation the StringRepresentation in which the String s is given
     * @param pars collects parameters that may influence the String representation. Always provided.
     * @param s the String
     * @param mimeType the MIME type of the representation, if known
     * @return the PropertyValue
     * @throws PropertyValueParsingException thrown if the String representation could not be parsed successfully
     */
    @Override
    public PropertyValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException
    {
        return theDataType.fromStringRepresentation( representation, pars, s, mimeType );
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "theIdentifier"
                },
                new Object[] {
                    getIdentifier()
                });
    }

    /**
      * The value for the DataType property.
      */
    private DataType theDataType;

    /**
      * Our default value.
      */
    private PropertyValue theDefaultValue;

    /**
     * The code to calculate the default value.
     */
    private StringValue theDefaultValueCode;

    /**
     * The value for the IsOptional property.
     */
    private BooleanValue theIsOptional;

    /**
     * The value for the IsReadOnly property.
     */
    private BooleanValue theIsReadOnly;

    /**
     * The PropertyTypes that this PropertyType overrides.
     */
    private PropertyType [] theOverride = new PropertyType[0];

    /**
     * The set of PropertyTypeGuards on this PropertyType (not a supertype), expressed as the set of class names.
     */
    private String [] theLocalPropertyTypeGuardClassNames;

    /**
     * The set of PropertyTypeGuards on this PropertyType (not a supertype). This is allocated when needed.
     */
    private PropertyTypeGuard [] theLocalPropertyTypeGuards;

    /**
     * The set of PropertyTypeGuards on this PropertyType (including its supertypes). This is allocated when needed.
     */
    private PropertyTypeGuard [] theAllPropertyTypeGuards;
}
