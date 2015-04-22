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

package org.infogrid.model.primitives;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;

/**
  * <p>This is a value-holding property of an AttributableObjectType.</p>
  *
  * <p>PropertyTypes have DataTypes that constrain the values they can hold.</p>
  *
  * <p>If a PropertyType is optional, it may have the value null. If it is not
  * optional, the null value is not allowed.</p>
  *
  * <p>They can have default values, i.e. the initial value to which they are set
  * once the enclosing AttributableObjectType is instantiated. Default values can
  * be specified either as fixed values, or as code that is to be run when the
  * enclosing AttributableObjectType is instantiated.</p>
  *
  * <p>They may be read-only, i.e. not provide means to set their value.</p>
  *
  * <p>PropertyTypes in subtypes may override PropertyTypes in supertypes, within certain limits.
  * For example, a PropertyType may override a PropertyType in a supertype by
  * specifying a different default value.</p>
  */
public interface PropertyType
        extends
            PropertyTypeOrGroup
{
    /**
      * Obtain the value of the DataType property.
      *
      * @return the value of the DataType property
      */
    public DataType getDataType();

    /**
     * Obtain the value of the DefaultValue property.
     *
     * @return the value of the DefaultValue property
     */
    public PropertyValue getDefaultValue();

    /**
     * Obtain programming language code determining the default value for this PropertyType.
     * This is in-lined by the code generator.
     *
     * @return the programming language code that determines the default value for this PropertyValue.
     */
    public StringValue getDefaultValueCode();

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
    public PropertyValue fromStringRepresentation(
            StringRepresentation           representation,
            StringRepresentationParameters pars,
            String                         s,
            String                         mimeType )
        throws
            PropertyValueParsingException;

    /**
      * Obtain the value of the IsOptional property.
      *
      * @return the value of the IsOptional property
      */
    public BooleanValue getIsOptional();

    /**
     * Obtain the value of the IsReadOnly property.
     *
     * @return the value of the IsReadOnly property
     */
    public BooleanValue getIsReadOnly();

    /**
     * Obtain the MetaAttributes that this PropertyValue overrides, if any.
     *
     * @return the MetaAttributes that this Property overrides
     */
    public PropertyType [] getOverride();

    /**
     * Obtain the ancestor PropertyType of the overriding hierarchy. If this PropertyType
     * does not override any other PropertyType, a call to this method returns this
     * PropertyType. Otherwise it is invoked recursively on the overridden
     * PropertyType, until we reach the top of the overriding hierarchy. Due to
     * topology constraints, it is known that the ancestor PropertyType is the same,
     * regardless which override path was taken in case of multiple inheritance of the
     * enclosing AttributableObjectType.
     *
     * @return the ancestor of the overriding hierarchy
     */
    public PropertyType getOverrideAncestor();

    /**
     * Determine whether this PropertyType overrides the passed-in PropertyType.
     *
     * @param other the PropertyType to test against
     * @return true if this PropertyType overrides the passed-in PropertyType
     */
    public boolean overrides(
            PropertyType other );

    /**
     * Determine whether this PropertyType is the same as, or overrides the passed-in
     * PropertyType. This is the method typically used to determine whether
     * or not an events PropertyType applies to
     * an event subscriber, which generally picks the PropertyType highest in the inheritance
     * hierarchy to compare against.
     *
     * @param other the PropertyType to test against
     * @return true if this PropertyType is the same as, or overrides the passed-in PropertyType
     */
    public boolean equalsOrOverrides(
            PropertyType other );

    /**
     * Obtain the set of PropertyTypeGuards locally defined on this PropertyType.
     *
     * @return the set of PropertyTypeGuard locally defined on this PropertyType
     */
    public PropertyTypeGuard [] getLocalPropertyTypeGuards();

    /**
     * Obtain the set of PropertyTypeGuard defined on this PropertyType either locally or by inheritance.
     *
     * @return the set of PropertyTypeGuard defined on this PropertyType either locally or by inheritance
     */
    public PropertyTypeGuard [] getAllPropertyTypeGuards();

    /**
     * Obtain the class names of the set of PropertyTypeGuard on this PropertyType.
     *
     * @return the class names of the set of PropertyTypeGuard on this PropertyType
     */
    public String [] getLocalPropertyTypeGuardClassNames();

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
    public void checkPermittedSetProperty(
            MeshObject    subject,
            PropertyValue newValue,
            MeshObject    caller )
        throws
            NotPermittedException;

    /**
     * Check whether the given caller is allowed to read the property corresponding to this PropertyType
     * on the given subject MeshObject. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject whose property is supposed to be read
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    public void checkPermittedGetProperty(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException;
}
