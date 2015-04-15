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

//
// This file has been generated AUTOMATICALLY. DO NOT MODIFY.
// on Wed, 2015-04-15 20:21:34 +0000
//
// DO NOT MODIFY -- re-generate!

package org.infogrid.model.Test;

import org.infogrid.model.primitives.*;
import org.infogrid.mesh.*;
import org.infogrid.modelbase.ModelBaseSingleton;

/**
  * <p>Java interface for EntityType org.infogrid.model.Test/OptionalEnumerated.</p>
  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s
  * <code>MeshObjectLifecycleManager</code>.</p>
  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any
  * class that implements this interface, only on the characteristics provided by this interface
  * and its supertypes.</p>
  *
  * <table>
  *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalEnumerated</tt></td></tr>
  *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
  *  <tr><td>IsAbstract:</td><td>PlainFalse</td></tr>
  *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
  * </table>
  */

public interface OptionalEnumerated
        extends
            org.infogrid.mesh.TypedMeshObjectFacade
{
    /**
      * Subject area in which this MeshObject is declared.
      */
    public static final SubjectArea _SUBJECTAREA = ModelBaseSingleton.findSubjectArea( "org.infogrid.model.Test" );

    /**
      * This MeshType.
      */
    public static final EntityType _TYPE = ModelBaseSingleton.findEntityType( "org.infogrid.model.Test/OptionalEnumerated" );

    /**
      * <p>Set value for property OptionalEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalEnumerated_OptionalEnumeratedDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainNull</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
       * @param newValue the new value for this property
       * @throws NotPermittedException thrown if this caller was not allowed to perform this operation
       * @throws TransactionException thrown if this operation is invoked from outside of a Transaction
       * @throws IllegalPropertyValueException thrown if it is attempted to assign the <code>null</code> value to this non-optional property
      */
    public abstract void setOptionalEnumeratedDataType(
            org.infogrid.model.primitives.EnumeratedValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalEnumerated_OptionalEnumeratedDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainNull</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.EnumeratedValue getOptionalEnumeratedDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalEnumeratedDataType property.
      */
    public static final String OPTIONALENUMERATEDDATATYPE_name = "OptionalEnumeratedDataType";

    /**
      * The OptionalEnumeratedDataType PropertyType.
      */
    public static final PropertyType OPTIONALENUMERATEDDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalEnumerated_OptionalEnumeratedDataType" );
    public static final org.infogrid.model.primitives.EnumeratedDataType OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) OPTIONALENUMERATEDDATATYPE.getDataType();

    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE1 = OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE2 = OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE3 = OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );
}
