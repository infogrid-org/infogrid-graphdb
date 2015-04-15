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
  * <p>Java interface for EntityType org.infogrid.model.Test/OptionalProperties.</p>
  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s
  * <code>MeshObjectLifecycleManager</code>.</p>
  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any
  * class that implements this interface, only on the characteristics provided by this interface
  * and its supertypes.</p>
  *
  * <table>
  *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties</tt></td></tr>
  *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
  *  <tr><td>IsAbstract:</td><td>PlainFalse</td></tr>
  *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
  *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
  * </table>
  */

public interface OptionalProperties
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
    public static final EntityType _TYPE = ModelBaseSingleton.findEntityType( "org.infogrid.model.Test/OptionalProperties" );

    /**
      * <p>Set value for property OptionalBlobDataTypeAny.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeAny</tt></td></tr>
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
    public abstract void setOptionalBlobDataTypeAny(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataTypeAny.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeAny</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataTypeAny()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataTypeAny property.
      */
    public static final String OPTIONALBLOBDATATYPEANY_name = "OptionalBlobDataTypeAny";

    /**
      * The OptionalBlobDataTypeAny PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATATYPEANY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeAny" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEANY.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypePlainOrHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlainOrHtml</tt></td></tr>
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
    public abstract void setOptionalBlobDataTypePlainOrHtml(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataTypePlainOrHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlainOrHtml</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataTypePlainOrHtml()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataTypePlainOrHtml property.
      */
    public static final String OPTIONALBLOBDATATYPEPLAINORHTML_name = "OptionalBlobDataTypePlainOrHtml";

    /**
      * The OptionalBlobDataTypePlainOrHtml PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATATYPEPLAINORHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlainOrHtml" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEPLAINORHTML.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypePlain.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlain</tt></td></tr>
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
    public abstract void setOptionalBlobDataTypePlain(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataTypePlain.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlain</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataTypePlain()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataTypePlain property.
      */
    public static final String OPTIONALBLOBDATATYPEPLAIN_name = "OptionalBlobDataTypePlain";

    /**
      * The OptionalBlobDataTypePlain PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATATYPEPLAIN = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlain" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEPLAIN.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataHtml</tt></td></tr>
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
    public abstract void setOptionalBlobDataHtml(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataHtml</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataHtml()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataHtml property.
      */
    public static final String OPTIONALBLOBDATAHTML_name = "OptionalBlobDataHtml";

    /**
      * The OptionalBlobDataHtml PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATAHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataHtml" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATAHTML.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypeImage.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeImage</tt></td></tr>
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
    public abstract void setOptionalBlobDataTypeImage(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataTypeImage.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeImage</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataTypeImage()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataTypeImage property.
      */
    public static final String OPTIONALBLOBDATATYPEIMAGE_name = "OptionalBlobDataTypeImage";

    /**
      * The OptionalBlobDataTypeImage PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATATYPEIMAGE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeImage" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEIMAGE.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypeJpg.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeJpg</tt></td></tr>
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
    public abstract void setOptionalBlobDataTypeJpg(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalBlobDataTypeJpg.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeJpg</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getOptionalBlobDataTypeJpg()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBlobDataTypeJpg property.
      */
    public static final String OPTIONALBLOBDATATYPEJPG_name = "OptionalBlobDataTypeJpg";

    /**
      * The OptionalBlobDataTypeJpg PropertyType.
      */
    public static final PropertyType OPTIONALBLOBDATATYPEJPG = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeJpg" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEJPG.getDataType();

    /**
      * <p>Set value for property OptionalBooleanDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBooleanDataType</tt></td></tr>
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
      */
    public abstract void setOptionalBooleanDataType(
            org.infogrid.model.primitives.BooleanValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalBooleanDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalBooleanDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BooleanValue getOptionalBooleanDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalBooleanDataType property.
      */
    public static final String OPTIONALBOOLEANDATATYPE_name = "OptionalBooleanDataType";

    /**
      * The OptionalBooleanDataType PropertyType.
      */
    public static final PropertyType OPTIONALBOOLEANDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalBooleanDataType" );
    public static final org.infogrid.model.primitives.BooleanDataType OPTIONALBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) OPTIONALBOOLEANDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalColorDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalColorDataType</tt></td></tr>
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
      */
    public abstract void setOptionalColorDataType(
            org.infogrid.model.primitives.ColorValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalColorDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalColorDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.ColorValue getOptionalColorDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalColorDataType property.
      */
    public static final String OPTIONALCOLORDATATYPE_name = "OptionalColorDataType";

    /**
      * The OptionalColorDataType PropertyType.
      */
    public static final PropertyType OPTIONALCOLORDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalColorDataType" );
    public static final org.infogrid.model.primitives.ColorDataType OPTIONALCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) OPTIONALCOLORDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalCurrencyDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalCurrencyDataType</tt></td></tr>
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
      */
    public abstract void setOptionalCurrencyDataType(
            org.infogrid.model.primitives.CurrencyValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalCurrencyDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalCurrencyDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.CurrencyValue getOptionalCurrencyDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalCurrencyDataType property.
      */
    public static final String OPTIONALCURRENCYDATATYPE_name = "OptionalCurrencyDataType";

    /**
      * The OptionalCurrencyDataType PropertyType.
      */
    public static final PropertyType OPTIONALCURRENCYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalCurrencyDataType" );
    public static final org.infogrid.model.primitives.CurrencyDataType OPTIONALCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) OPTIONALCURRENCYDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalEnumeratedDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalEnumeratedDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALENUMERATEDDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalEnumeratedDataType" );
    public static final org.infogrid.model.primitives.EnumeratedDataType OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) OPTIONALENUMERATEDDATATYPE.getDataType();

    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE1 = OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE2 = OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE3 = OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );
    /**
      * <p>Set value for property OptionalExtentDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalExtentDataType</tt></td></tr>
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
      */
    public abstract void setOptionalExtentDataType(
            org.infogrid.model.primitives.ExtentValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalExtentDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalExtentDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.ExtentValue getOptionalExtentDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalExtentDataType property.
      */
    public static final String OPTIONALEXTENTDATATYPE_name = "OptionalExtentDataType";

    /**
      * The OptionalExtentDataType PropertyType.
      */
    public static final PropertyType OPTIONALEXTENTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalExtentDataType" );
    public static final org.infogrid.model.primitives.ExtentDataType OPTIONALEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) OPTIONALEXTENTDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalFloatDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalFloatDataType</tt></td></tr>
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
      */
    public abstract void setOptionalFloatDataType(
            org.infogrid.model.primitives.FloatValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalFloatDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalFloatDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.FloatValue getOptionalFloatDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalFloatDataType property.
      */
    public static final String OPTIONALFLOATDATATYPE_name = "OptionalFloatDataType";

    /**
      * The OptionalFloatDataType PropertyType.
      */
    public static final PropertyType OPTIONALFLOATDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalFloatDataType" );
    public static final org.infogrid.model.primitives.FloatDataType OPTIONALFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) OPTIONALFLOATDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalIntegerDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalIntegerDataType</tt></td></tr>
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
      */
    public abstract void setOptionalIntegerDataType(
            org.infogrid.model.primitives.IntegerValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalIntegerDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalIntegerDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.IntegerValue getOptionalIntegerDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalIntegerDataType property.
      */
    public static final String OPTIONALINTEGERDATATYPE_name = "OptionalIntegerDataType";

    /**
      * The OptionalIntegerDataType PropertyType.
      */
    public static final PropertyType OPTIONALINTEGERDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalIntegerDataType" );
    public static final org.infogrid.model.primitives.IntegerDataType OPTIONALINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) OPTIONALINTEGERDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalMultiplicityDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalMultiplicityDataType</tt></td></tr>
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
      */
    public abstract void setOptionalMultiplicityDataType(
            org.infogrid.model.primitives.MultiplicityValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalMultiplicityDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalMultiplicityDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.MultiplicityValue getOptionalMultiplicityDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalMultiplicityDataType property.
      */
    public static final String OPTIONALMULTIPLICITYDATATYPE_name = "OptionalMultiplicityDataType";

    /**
      * The OptionalMultiplicityDataType PropertyType.
      */
    public static final PropertyType OPTIONALMULTIPLICITYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalMultiplicityDataType" );
    public static final org.infogrid.model.primitives.MultiplicityDataType OPTIONALMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) OPTIONALMULTIPLICITYDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalPointDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalPointDataType</tt></td></tr>
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
      */
    public abstract void setOptionalPointDataType(
            org.infogrid.model.primitives.PointValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalPointDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalPointDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.PointValue getOptionalPointDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalPointDataType property.
      */
    public static final String OPTIONALPOINTDATATYPE_name = "OptionalPointDataType";

    /**
      * The OptionalPointDataType PropertyType.
      */
    public static final PropertyType OPTIONALPOINTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalPointDataType" );
    public static final org.infogrid.model.primitives.PointDataType OPTIONALPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) OPTIONALPOINTDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalStringDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalStringDataType</tt></td></tr>
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
      */
    public abstract void setOptionalStringDataType(
            org.infogrid.model.primitives.StringValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalStringDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalStringDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.StringValue getOptionalStringDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalStringDataType property.
      */
    public static final String OPTIONALSTRINGDATATYPE_name = "OptionalStringDataType";

    /**
      * The OptionalStringDataType PropertyType.
      */
    public static final PropertyType OPTIONALSTRINGDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalStringDataType" );
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRINGDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalStringRegexDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalStringRegexDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainRegexType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainNull</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
      * </table>
      *
       * @param newValue the new value for this property
       * @throws NotPermittedException thrown if this caller was not allowed to perform this operation
       * @throws TransactionException thrown if this operation is invoked from outside of a Transaction
       * @throws IllegalPropertyValueException thrown if it is attempted to assign the <code>null</code> value to this non-optional property
      */
    public abstract void setOptionalStringRegexDataType(
            org.infogrid.model.primitives.StringValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property OptionalStringRegexDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalStringRegexDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainRegexType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainNull</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.StringValue getOptionalStringRegexDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalStringRegexDataType property.
      */
    public static final String OPTIONALSTRINGREGEXDATATYPE_name = "OptionalStringRegexDataType";

    /**
      * The OptionalStringRegexDataType PropertyType.
      */
    public static final PropertyType OPTIONALSTRINGREGEXDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalStringRegexDataType" );
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRINGREGEXDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalTimePeriodDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalTimePeriodDataType</tt></td></tr>
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
      */
    public abstract void setOptionalTimePeriodDataType(
            org.infogrid.model.primitives.TimePeriodValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalTimePeriodDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalTimePeriodDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.TimePeriodValue getOptionalTimePeriodDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalTimePeriodDataType property.
      */
    public static final String OPTIONALTIMEPERIODDATATYPE_name = "OptionalTimePeriodDataType";

    /**
      * The OptionalTimePeriodDataType PropertyType.
      */
    public static final PropertyType OPTIONALTIMEPERIODDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalTimePeriodDataType" );
    public static final org.infogrid.model.primitives.TimePeriodDataType OPTIONALTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) OPTIONALTIMEPERIODDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalTimeStampDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalTimeStampDataType</tt></td></tr>
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
      */
    public abstract void setOptionalTimeStampDataType(
            org.infogrid.model.primitives.TimeStampValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException;

    /**
      * <p>Obtain value for property OptionalTimeStampDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/OptionalProperties_OptionalTimeStampDataType</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.TimeStampValue getOptionalTimeStampDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the OptionalTimeStampDataType property.
      */
    public static final String OPTIONALTIMESTAMPDATATYPE_name = "OptionalTimeStampDataType";

    /**
      * The OptionalTimeStampDataType PropertyType.
      */
    public static final PropertyType OPTIONALTIMESTAMPDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/OptionalProperties_OptionalTimeStampDataType" );
    public static final org.infogrid.model.primitives.TimeStampDataType OPTIONALTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) OPTIONALTIMESTAMPDATATYPE.getDataType();

}
