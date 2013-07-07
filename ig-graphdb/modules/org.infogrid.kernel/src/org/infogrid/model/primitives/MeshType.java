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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import java.io.Serializable;
import org.infogrid.util.HasIdentifier;

/**
  * The root of the inheritance hierarchy for the type objects. All classes whose
  * instances represent model elements inherit from here.
  */
public interface MeshType
    extends
        HasIdentifier,
        Serializable
{
    /**
      * Obtain the Identifier of this MeshType.
      *
      * @return the Identifier
      */
    public MeshTypeIdentifier getIdentifier();

    /**
      * Obtain the value of the Name property.
      *
      * @return value of the Name property
      */
    public StringValue getName();

    /**
     * Obtain a localized, user-visible name of this MeshType in the current locale.
     *
     * @return value of the UserVisibleName property
     */
    public StringValue getUserVisibleName();

    /**
     * Obtain the localized map of user-visible names.
     *
     * @return the localized map of user-visible names
     */
    public L10PropertyValueMap getUserVisibleNameMap();

    /**
     * Obtain a localized, user-visible description text for this MeshType in the current locale.
     *
     * @return the value of the UserVisibleDescription property
     */
    public BlobValue getUserVisibleDescription();

    /**
     * Obtain the localized map of user-visible descriptions.
     *
     * @return the localized map of user-visible descriptions
     */
    public L10PropertyValueMap getUserVisibleDescriptionMap();

    /**
     * Obtain the value of the Icon property.
     *
     * @return the value of the Icon property
     */
    public BlobValue getIcon();

    /**
     * Obtain the value of the DoGenerateInterfaceCode property.
     *
     * @return the value of the DoGenerateInterfaceCode property
     */
    public BooleanValue getDoGenerateInterfaceCode();

    /**
     * Obtain the value of the DoGenerateImplementationCode property.
     *
     * @return the value of the DoGenerateImplementationCode property
     */
    public BooleanValue getDoGenerateImplementationCode();

    /**
     * Obtain an external form identifying this MeshType. This is analogous
     * to the URL.toExternalForm() method in the JDK.
     *
     * @return the external form for this MeshType
     */
    public String toExternalForm();
};
