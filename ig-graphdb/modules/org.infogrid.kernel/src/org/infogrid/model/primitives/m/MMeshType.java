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

package org.infogrid.model.primitives.m;

import java.util.Locale;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.L10PropertyValueMap;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.Identifier;

/**
  * The root of the type inheritance hierarchy.
  * In-memory implementation.
  */
public abstract class MMeshType
        implements
            MeshType,
            Comparable<MeshType>
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MMeshType(
            MeshTypeIdentifier identifier )
    {
        this.theIdentifier = identifier;
    }

    /**
      * Obtain the value of the Identifier property.
      *
      * @return value of the Identifier property
      */
    public final MeshTypeIdentifier getIdentifier()
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
      * Set the Name property.
      *
      * @param newValue the new value for the property
      * @see #getName
      */
    public final void setName(
            StringValue newValue )
    {
        this.theName = newValue;
    }

    /**
      * Obtain the value of the Name property.
      *
      * @return value of the Name property
      * @see #setName
      */
    public final StringValue getName()
    {
        return theName;
    }

    /**
     * Obtain a localized, user-visible name of this MeshType in the current locale.
     * This is non-final so subclasses can easily override it with something more specific.
     *
     * @return value of the UserVisibleName property
     * @see #setUserVisibleNameMap
     */
    public StringValue getUserVisibleName()
    {
        if( theUserName == null && theUserNameMap != null ) {
            theUserName = (StringValue) theUserNameMap.get( Locale.getDefault() );
        }
        return theUserName;
    }

    /**
     * Set the localized map of user-visible names.
     *
     * @param newValue the new value for the map
     * @see #getUserVisibleNameMap
     */
    public final void setUserVisibleNameMap(
            L10PropertyValueMap newValue )
    {
        theUserNameMap = newValue;
    }

    /**
     * Obtain the localized map of user-visible names.
     *
     * @return the localized map of user-visible names
     * @see #setUserVisibleNameMap
     */
    public final L10PropertyValueMap getUserVisibleNameMap()
    {
        return theUserNameMap;
    }

    /**
     * Obtain a localized, user-visible description text for this MeshType in the current locale.
     *
     * @return the value of the UserVisibleDescription property
     * @see #setUserVisibleDescriptionMap
     */
    public final BlobValue getUserVisibleDescription()
    {
        if( theUserDescription == null && theUserDescriptionMap != null ) {
            theUserDescription = (BlobValue) theUserDescriptionMap.get( Locale.getDefault() );
        }

        return theUserDescription;
    }

    /**
     * Set the localized map of user-visible descriptions.
     *
     * @param newValue the new value for the map
     * @see #getUserVisibleDescriptionMap
     */
    public final void setUserVisibleDescriptionMap(
            L10PropertyValueMap newValue )
    {
        theUserDescriptionMap = newValue;
    }

    /**
     * Obtain the localized map of user-visible descriptions.
     *
     * @return the localized map of user-visible descriptions
     * @see #setUserVisibleDescriptionMap
     */
    public final L10PropertyValueMap getUserVisibleDescriptionMap()
    {
        return theUserDescriptionMap;
    }

    /**
      * Set the Icon property.
      *
      * @param newValue the new value for the property
      * @see #getIcon
      */
    public final void setIcon(
            BlobValue newValue )
    {
        this.theIcon = newValue;
    }

    /**
     * Obtain the value of the Icon property.
     *
     * @return the value of the Icon property
     * @see #setIcon
     */
    public BlobValue getIcon()
    {
        return theIcon;
    }

    /**
      * Set the DoGenerateInterfaceCode property.
      *
      * @param newValue the new value for the property
      * @see #getDoGenerateInterfaceCode
      */
    public final void setDoGenerateInterfaceCode(
            BooleanValue newValue )
    {
        theDoGenerateInterfaceCode = newValue;
    }

    /**
     * Obtain the value of the DoGenerateInterfaceCode property.
     *
     * @return the value of the DoGenerateInterfaceCode property
     * @see #setDoGenerateInterfaceCode
     */
    public final BooleanValue getDoGenerateInterfaceCode()
    {
        return theDoGenerateInterfaceCode;
    }

    /**
      * Set the DoGenerateImplementationCode property.
      *
      * @param newValue the new value for the property
      * @see #getDoGenerateImplementationCode
      */
    public final void setDoGenerateImplementationCode(
            BooleanValue newValue )
    {
        theDoGenerateImplementationCode = newValue;
    }

    /**
     * Obtain the value of the DoGenerateImplementationCode property.
     *
     * @return the value of the DoGenerateImplementationCode property
     * @see #setDoGenerateImplementationCode
     */
    public final BooleanValue getDoGenerateImplementationCode()
    {
        return theDoGenerateImplementationCode;
    }

    /**
     * Obtain an external form identifying this MeshObjectType. This is analogous
     * to the URL.toExternalForm() method in the JDK.
     *
     * @return the external form for this MeshObjectType
     */
    public final String toExternalForm()
    {
        return theIdentifier.toExternalForm();
    }

    /**
     * Implement Comparable using Identifier comparison. This gives consistent ordering
     * in all cases.
     * 
     * @param other the Object to compare against
     */
    @Override
    public int compareTo(
            MeshType other )
    {
        return theIdentifier.getExternalForm().compareTo( other.getIdentifier().getExternalForm() );
    }
    
    /**
      * Determine equality.
      *
      * @param other the Object to compare against
      * @return true if the Objects are equal
      */
    @Override
    public final boolean equals(
            Object other )
    {
        if( other instanceof MMeshType ) {
            MMeshType realOther = (MMeshType) other;
            return theIdentifier.equals( realOther.getIdentifier() );
        }
        return false;
    }

    /**
     * Determine hash code. The hash code is the same as the hash code of our
     * Identifier.
     *
     * @return the hash code
     */
    @Override
    public final int hashCode()
    {
        return theIdentifier.hashCode();
    }

    /**
     * Convert to String, for debugging.
     *
     * @return String
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append( super.toString() );
        buf.append( "{ identifier: " );
        buf.append( theIdentifier.toExternalForm() );
        buf.append( " }" );
        return buf.toString();
    }

    /**
      * Value of the Identifier property.
      */
    private MeshTypeIdentifier theIdentifier;

    /**
      * Value of the Name property.
      */
    private StringValue theName;

    /**
     * Our user-visible Name -- allocated as needed.
     * This is protected, not private, so our subclasses can easily reassign it to something more specific.
     */
    protected transient StringValue theUserName;

    /**
     * Our user-visible Description -- allocated as needed.
     */
    private transient BlobValue theUserDescription;

    /**
     * The Icon. This is protected instead of private so subclasses can easily modify it.
     */
    protected BlobValue theIcon;

    /**
     * The flag indicating whether we need to generate interface code from this MeshObjectType.
     */
    private BooleanValue theDoGenerateInterfaceCode;

    /**
     * The flag indicating whether we need to generate implementation code from this MeshObjectType.
     */
    private BooleanValue theDoGenerateImplementationCode;

    /**
     * The map of localized user names.
     */
    private L10PropertyValueMap theUserNameMap;

    /**
     * The map of localized user descriptions.
     */
    private L10PropertyValueMap theUserDescriptionMap;
};
