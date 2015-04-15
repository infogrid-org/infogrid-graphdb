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
  * <p>Java interface for EntityType org.infogrid.model.Test/AllProperties.</p>
  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s
  * <code>MeshObjectLifecycleManager</code>.</p>
  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any
  * class that implements this interface, only on the characteristics provided by this interface
  * and its supertypes.</p>
  *
  * <table>
  *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties</tt></td></tr>
  *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
  *  <tr><td>IsAbstract:</td><td>PlainFalse</td></tr>
  *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
  *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
  * </table>
  */

public interface AllProperties
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
    public static final EntityType _TYPE = ModelBaseSingleton.findEntityType( "org.infogrid.model.Test/AllProperties" );

    /**
      * <p>Set value for property OptionalBlobDataTypeAny.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeAny</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeAny</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATATYPEANY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeAny" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEANY.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypePlainOrHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlainOrHtml</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlainOrHtml</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATATYPEPLAINORHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlainOrHtml" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEPLAINORHTML.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypePlain.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlain</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlain</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATATYPEPLAIN = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlain" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEPLAIN.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataHtml</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataHtml</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATAHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataHtml" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATAHTML.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypeImage.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeImage</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeImage</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATATYPEIMAGE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeImage" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEIMAGE.getDataType();

    /**
      * <p>Set value for property OptionalBlobDataTypeJpg.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeJpg</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeJpg</tt></td></tr>
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
    public static final PropertyType OPTIONALBLOBDATATYPEJPG = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeJpg" );
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBDATATYPEJPG.getDataType();

    /**
      * <p>Set value for property OptionalBooleanDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBooleanDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalBooleanDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALBOOLEANDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalBooleanDataType" );
    public static final org.infogrid.model.primitives.BooleanDataType OPTIONALBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) OPTIONALBOOLEANDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalColorDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalColorDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalColorDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALCOLORDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalColorDataType" );
    public static final org.infogrid.model.primitives.ColorDataType OPTIONALCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) OPTIONALCOLORDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalCurrencyDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalCurrencyDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalCurrencyDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALCURRENCYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalCurrencyDataType" );
    public static final org.infogrid.model.primitives.CurrencyDataType OPTIONALCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) OPTIONALCURRENCYDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalEnumeratedDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalEnumeratedDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALENUMERATEDDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalEnumeratedDataType" );
    public static final org.infogrid.model.primitives.EnumeratedDataType OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) OPTIONALENUMERATEDDATATYPE.getDataType();

    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE1 = OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE2 = OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue OPTIONALENUMERATEDDATATYPE_type_VALUE3 = OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );
    /**
      * <p>Set value for property OptionalExtentDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalExtentDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalExtentDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALEXTENTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalExtentDataType" );
    public static final org.infogrid.model.primitives.ExtentDataType OPTIONALEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) OPTIONALEXTENTDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalFloatDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalFloatDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalFloatDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALFLOATDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalFloatDataType" );
    public static final org.infogrid.model.primitives.FloatDataType OPTIONALFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) OPTIONALFLOATDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalIntegerDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalIntegerDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalIntegerDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALINTEGERDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalIntegerDataType" );
    public static final org.infogrid.model.primitives.IntegerDataType OPTIONALINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) OPTIONALINTEGERDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalMultiplicityDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalMultiplicityDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalMultiplicityDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALMULTIPLICITYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalMultiplicityDataType" );
    public static final org.infogrid.model.primitives.MultiplicityDataType OPTIONALMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) OPTIONALMULTIPLICITYDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalPointDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalPointDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalPointDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALPOINTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalPointDataType" );
    public static final org.infogrid.model.primitives.PointDataType OPTIONALPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) OPTIONALPOINTDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalStringDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalStringDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalStringDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALSTRINGDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalStringDataType" );
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRINGDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalStringRegexDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalStringRegexDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalStringRegexDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALSTRINGREGEXDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalStringRegexDataType" );
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRINGREGEXDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalTimePeriodDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalTimePeriodDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalTimePeriodDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALTIMEPERIODDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalTimePeriodDataType" );
    public static final org.infogrid.model.primitives.TimePeriodDataType OPTIONALTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) OPTIONALTIMEPERIODDATATYPE.getDataType();

    /**
      * <p>Set value for property OptionalTimeStampDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalTimeStampDataType</tt></td></tr>
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
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_OptionalTimeStampDataType</tt></td></tr>
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
    public static final PropertyType OPTIONALTIMESTAMPDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_OptionalTimeStampDataType" );
    public static final org.infogrid.model.primitives.TimeStampDataType OPTIONALTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) OPTIONALTIMESTAMPDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataTypeAny.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeAny</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataTypeAny(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataTypeAny.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeAny</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataTypeAny()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataTypeAny property.
      */
    public static final String MANDATORYBLOBDATATYPEANY_name = "MandatoryBlobDataTypeAny";

    /**
      * The MandatoryBlobDataTypeAny PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATATYPEANY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeAny" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATATYPEANY.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataTypePlainOrHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlainOrHtml</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataTypePlainOrHtml(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataTypePlainOrHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlainOrHtml</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataTypePlainOrHtml()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataTypePlainOrHtml property.
      */
    public static final String MANDATORYBLOBDATATYPEPLAINORHTML_name = "MandatoryBlobDataTypePlainOrHtml";

    /**
      * The MandatoryBlobDataTypePlainOrHtml PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATATYPEPLAINORHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlainOrHtml" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATATYPEPLAINORHTML.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataTypePlain.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlain</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataTypePlain(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataTypePlain.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlain</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainDontInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataTypePlain()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataTypePlain property.
      */
    public static final String MANDATORYBLOBDATATYPEPLAIN_name = "MandatoryBlobDataTypePlain";

    /**
      * The MandatoryBlobDataTypePlain PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATATYPEPLAIN = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlain" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATATYPEPLAIN.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataHtml</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataHtml(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataHtml.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataHtml</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainInterpretTextTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataHtml()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataHtml property.
      */
    public static final String MANDATORYBLOBDATAHTML_name = "MandatoryBlobDataHtml";

    /**
      * The MandatoryBlobDataHtml PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATAHTML = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataHtml" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATAHTML.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataTypeImage.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeImage</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainBinaryTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataTypeImage(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataTypeImage.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeImage</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainBinaryTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataTypeImage()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataTypeImage property.
      */
    public static final String MANDATORYBLOBDATATYPEIMAGE_name = "MandatoryBlobDataTypeImage";

    /**
      * The MandatoryBlobDataTypeImage PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATATYPEIMAGE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeImage" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATATYPEIMAGE.getDataType();

    /**
      * <p>Set value for property MandatoryBlobDataTypeJpg.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeJpg</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainBinaryTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBlobDataTypeJpg(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBlobDataTypeJpg.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeJpg</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainBinaryTextOrBinaryString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BlobValue getMandatoryBlobDataTypeJpg()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBlobDataTypeJpg property.
      */
    public static final String MANDATORYBLOBDATATYPEJPG_name = "MandatoryBlobDataTypeJpg";

    /**
      * The MandatoryBlobDataTypeJpg PropertyType.
      */
    public static final PropertyType MANDATORYBLOBDATATYPEJPG = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeJpg" );
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBDATATYPEJPG.getDataType();

    /**
      * <p>Set value for property MandatoryBooleanDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBooleanDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryBooleanDataType(
            org.infogrid.model.primitives.BooleanValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryBooleanDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryBooleanDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainTrue</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.BooleanValue getMandatoryBooleanDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryBooleanDataType property.
      */
    public static final String MANDATORYBOOLEANDATATYPE_name = "MandatoryBooleanDataType";

    /**
      * The MandatoryBooleanDataType PropertyType.
      */
    public static final PropertyType MANDATORYBOOLEANDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryBooleanDataType" );
    public static final org.infogrid.model.primitives.BooleanDataType MANDATORYBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) MANDATORYBOOLEANDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryColorDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryColorDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryColorDataType(
            org.infogrid.model.primitives.ColorValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryColorDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryColorDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.ColorValue getMandatoryColorDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryColorDataType property.
      */
    public static final String MANDATORYCOLORDATATYPE_name = "MandatoryColorDataType";

    /**
      * The MandatoryColorDataType PropertyType.
      */
    public static final PropertyType MANDATORYCOLORDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryColorDataType" );
    public static final org.infogrid.model.primitives.ColorDataType MANDATORYCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) MANDATORYCOLORDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryCurrencyDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryCurrencyDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryCurrencyDataType(
            org.infogrid.model.primitives.CurrencyValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryCurrencyDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryCurrencyDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.CurrencyValue getMandatoryCurrencyDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryCurrencyDataType property.
      */
    public static final String MANDATORYCURRENCYDATATYPE_name = "MandatoryCurrencyDataType";

    /**
      * The MandatoryCurrencyDataType PropertyType.
      */
    public static final PropertyType MANDATORYCURRENCYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryCurrencyDataType" );
    public static final org.infogrid.model.primitives.CurrencyDataType MANDATORYCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) MANDATORYCURRENCYDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryEnumeratedDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryEnumeratedDataType(
            org.infogrid.model.primitives.EnumeratedValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryEnumeratedDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryEnumeratedDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.EnumeratedValue getMandatoryEnumeratedDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryEnumeratedDataType property.
      */
    public static final String MANDATORYENUMERATEDDATATYPE_name = "MandatoryEnumeratedDataType";

    /**
      * The MandatoryEnumeratedDataType PropertyType.
      */
    public static final PropertyType MANDATORYENUMERATEDDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryEnumeratedDataType" );
    public static final org.infogrid.model.primitives.EnumeratedDataType MANDATORYENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) MANDATORYENUMERATEDDATATYPE.getDataType();

    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue MANDATORYENUMERATEDDATATYPE_type_VALUE1 = MANDATORYENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue MANDATORYENUMERATEDDATATYPE_type_VALUE2 = MANDATORYENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue MANDATORYENUMERATEDDATATYPE_type_VALUE3 = MANDATORYENUMERATEDDATATYPE_type.select( "Value3" );
    /**
      * <p>Set value for property MandatoryExtentDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryExtentDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryExtentDataType(
            org.infogrid.model.primitives.ExtentValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryExtentDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryExtentDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.ExtentValue getMandatoryExtentDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryExtentDataType property.
      */
    public static final String MANDATORYEXTENTDATATYPE_name = "MandatoryExtentDataType";

    /**
      * The MandatoryExtentDataType PropertyType.
      */
    public static final PropertyType MANDATORYEXTENTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryExtentDataType" );
    public static final org.infogrid.model.primitives.ExtentDataType MANDATORYEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) MANDATORYEXTENTDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryFloatDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryFloatDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryFloatDataType(
            org.infogrid.model.primitives.FloatValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryFloatDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryFloatDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.FloatValue getMandatoryFloatDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryFloatDataType property.
      */
    public static final String MANDATORYFLOATDATATYPE_name = "MandatoryFloatDataType";

    /**
      * The MandatoryFloatDataType PropertyType.
      */
    public static final PropertyType MANDATORYFLOATDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryFloatDataType" );
    public static final org.infogrid.model.primitives.FloatDataType MANDATORYFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) MANDATORYFLOATDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryIntegerDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryIntegerDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryIntegerDataType(
            org.infogrid.model.primitives.IntegerValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryIntegerDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryIntegerDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.IntegerValue getMandatoryIntegerDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryIntegerDataType property.
      */
    public static final String MANDATORYINTEGERDATATYPE_name = "MandatoryIntegerDataType";

    /**
      * The MandatoryIntegerDataType PropertyType.
      */
    public static final PropertyType MANDATORYINTEGERDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryIntegerDataType" );
    public static final org.infogrid.model.primitives.IntegerDataType MANDATORYINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) MANDATORYINTEGERDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryMultiplicityDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryMultiplicityDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryMultiplicityDataType(
            org.infogrid.model.primitives.MultiplicityValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryMultiplicityDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryMultiplicityDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.MultiplicityValue getMandatoryMultiplicityDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryMultiplicityDataType property.
      */
    public static final String MANDATORYMULTIPLICITYDATATYPE_name = "MandatoryMultiplicityDataType";

    /**
      * The MandatoryMultiplicityDataType PropertyType.
      */
    public static final PropertyType MANDATORYMULTIPLICITYDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryMultiplicityDataType" );
    public static final org.infogrid.model.primitives.MultiplicityDataType MANDATORYMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) MANDATORYMULTIPLICITYDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryPointDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryPointDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryPointDataType(
            org.infogrid.model.primitives.PointValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryPointDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryPointDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.PointValue getMandatoryPointDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryPointDataType property.
      */
    public static final String MANDATORYPOINTDATATYPE_name = "MandatoryPointDataType";

    /**
      * The MandatoryPointDataType PropertyType.
      */
    public static final PropertyType MANDATORYPOINTDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryPointDataType" );
    public static final org.infogrid.model.primitives.PointDataType MANDATORYPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) MANDATORYPOINTDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryStringDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryStringDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryStringDataType(
            org.infogrid.model.primitives.StringValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryStringDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryStringDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.StringValue getMandatoryStringDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryStringDataType property.
      */
    public static final String MANDATORYSTRINGDATATYPE_name = "MandatoryStringDataType";

    /**
      * The MandatoryStringDataType PropertyType.
      */
    public static final PropertyType MANDATORYSTRINGDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryStringDataType" );
    public static final org.infogrid.model.primitives.StringDataType MANDATORYSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYSTRINGDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryStringRegexDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryStringRegexDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainRegexType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryStringRegexDataType(
            org.infogrid.model.primitives.StringValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryStringRegexDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryStringRegexDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainRegexType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.StringValue getMandatoryStringRegexDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryStringRegexDataType property.
      */
    public static final String MANDATORYSTRINGREGEXDATATYPE_name = "MandatoryStringRegexDataType";

    /**
      * The MandatoryStringRegexDataType PropertyType.
      */
    public static final PropertyType MANDATORYSTRINGREGEXDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryStringRegexDataType" );
    public static final org.infogrid.model.primitives.StringDataType MANDATORYSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYSTRINGREGEXDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryTimePeriodDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryTimePeriodDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryTimePeriodDataType(
            org.infogrid.model.primitives.TimePeriodValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryTimePeriodDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryTimePeriodDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.TimePeriodValue getMandatoryTimePeriodDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryTimePeriodDataType property.
      */
    public static final String MANDATORYTIMEPERIODDATATYPE_name = "MandatoryTimePeriodDataType";

    /**
      * The MandatoryTimePeriodDataType PropertyType.
      */
    public static final PropertyType MANDATORYTIMEPERIODDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryTimePeriodDataType" );
    public static final org.infogrid.model.primitives.TimePeriodDataType MANDATORYTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) MANDATORYTIMEPERIODDATATYPE.getDataType();

    /**
      * <p>Set value for property MandatoryTimeStampDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryTimeStampDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
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
    public abstract void setMandatoryTimeStampDataType(
            org.infogrid.model.primitives.TimeStampValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property MandatoryTimeStampDataType.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/AllProperties_MandatoryTimeStampDataType</tt></td></tr>
      *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>DataType:</td><td><tt>PlainType</tt></td></tr>
      *  <tr><td>DefaultValue:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>IsOptional:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>IsReadOnly:</td><td><tt>PlainFalse</tt></td></tr>
      *  <tr><td>SequenceNumber:</td><td><tt>PlainString</tt></td></tr>
      *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
      * </table>
      *
      * @return the current value of the property
      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation
      */
    public abstract org.infogrid.model.primitives.TimeStampValue getMandatoryTimeStampDataType()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the MandatoryTimeStampDataType property.
      */
    public static final String MANDATORYTIMESTAMPDATATYPE_name = "MandatoryTimeStampDataType";

    /**
      * The MandatoryTimeStampDataType PropertyType.
      */
    public static final PropertyType MANDATORYTIMESTAMPDATATYPE = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/AllProperties_MandatoryTimeStampDataType" );
    public static final org.infogrid.model.primitives.TimeStampDataType MANDATORYTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) MANDATORYTIMESTAMPDATATYPE.getDataType();

}
