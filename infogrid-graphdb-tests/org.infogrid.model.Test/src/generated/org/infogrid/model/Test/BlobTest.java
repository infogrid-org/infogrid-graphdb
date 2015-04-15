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
  * <p>Java interface for EntityType org.infogrid.model.Test/BlobTest.</p>
  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s
  * <code>MeshObjectLifecycleManager</code>.</p>
  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any
  * class that implements this interface, only on the characteristics provided by this interface
  * and its supertypes.</p>
  *
  * <table>
  *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest</tt></td></tr>
  *  <tr><td>Name:</td><td><tt>PlainString</tt></td></tr>
  *  <tr><td>IsAbstract:</td><td>PlainFalse</td></tr>
  *  <tr><td>UserVisibleName:</td><td><table><tr><td>default locale:</td><td>PlainString</td></tr></table></td></tr>
  *  <tr><td>UserVisibleDescription:</td><td><table><tr><td>default locale:</td><td>PlainInterpretTextTextOrBinaryString</td></tr></table></td></tr>
  * </table>
  */

public interface BlobTest
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
    public static final EntityType _TYPE = ModelBaseSingleton.findEntityType( "org.infogrid.model.Test/BlobTest" );

    /**
      * <p>Set value for property AnyOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_AnyOptional</tt></td></tr>
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
    public abstract void setAnyOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property AnyOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_AnyOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getAnyOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the AnyOptional property.
      */
    public static final String ANYOPTIONAL_name = "AnyOptional";

    /**
      * The AnyOptional PropertyType.
      */
    public static final PropertyType ANYOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_AnyOptional" );
    public static final org.infogrid.model.primitives.BlobDataType ANYOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) ANYOPTIONAL.getDataType();

    /**
      * <p>Set value for property AnyMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_AnyMandatory</tt></td></tr>
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
    public abstract void setAnyMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property AnyMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_AnyMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getAnyMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the AnyMandatory property.
      */
    public static final String ANYMANDATORY_name = "AnyMandatory";

    /**
      * The AnyMandatory PropertyType.
      */
    public static final PropertyType ANYMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_AnyMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType ANYMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) ANYMANDATORY.getDataType();

    /**
      * <p>Set value for property TextPlainOrHtmlOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOrHtmlOptional</tt></td></tr>
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
    public abstract void setTextPlainOrHtmlOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextPlainOrHtmlOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOrHtmlOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextPlainOrHtmlOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextPlainOrHtmlOptional property.
      */
    public static final String TEXTPLAINORHTMLOPTIONAL_name = "TextPlainOrHtmlOptional";

    /**
      * The TextPlainOrHtmlOptional PropertyType.
      */
    public static final PropertyType TEXTPLAINORHTMLOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextPlainOrHtmlOptional" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTPLAINORHTMLOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) TEXTPLAINORHTMLOPTIONAL.getDataType();

    /**
      * <p>Set value for property TextPlainOrHtmlMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOrHtmlMandatory</tt></td></tr>
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
    public abstract void setTextPlainOrHtmlMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextPlainOrHtmlMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOrHtmlMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextPlainOrHtmlMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextPlainOrHtmlMandatory property.
      */
    public static final String TEXTPLAINORHTMLMANDATORY_name = "TextPlainOrHtmlMandatory";

    /**
      * The TextPlainOrHtmlMandatory PropertyType.
      */
    public static final PropertyType TEXTPLAINORHTMLMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextPlainOrHtmlMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTPLAINORHTMLMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) TEXTPLAINORHTMLMANDATORY.getDataType();

    /**
      * <p>Set value for property TextPlainOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOptional</tt></td></tr>
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
    public abstract void setTextPlainOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextPlainOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextPlainOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextPlainOptional property.
      */
    public static final String TEXTPLAINOPTIONAL_name = "TextPlainOptional";

    /**
      * The TextPlainOptional PropertyType.
      */
    public static final PropertyType TEXTPLAINOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextPlainOptional" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTPLAINOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) TEXTPLAINOPTIONAL.getDataType();

    /**
      * <p>Set value for property TextPlainMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainMandatory</tt></td></tr>
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
    public abstract void setTextPlainMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextPlainMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextPlainMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextPlainMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextPlainMandatory property.
      */
    public static final String TEXTPLAINMANDATORY_name = "TextPlainMandatory";

    /**
      * The TextPlainMandatory PropertyType.
      */
    public static final PropertyType TEXTPLAINMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextPlainMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTPLAINMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) TEXTPLAINMANDATORY.getDataType();

    /**
      * <p>Set value for property TextHtmlOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextHtmlOptional</tt></td></tr>
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
    public abstract void setTextHtmlOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextHtmlOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextHtmlOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextHtmlOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextHtmlOptional property.
      */
    public static final String TEXTHTMLOPTIONAL_name = "TextHtmlOptional";

    /**
      * The TextHtmlOptional PropertyType.
      */
    public static final PropertyType TEXTHTMLOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextHtmlOptional" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTHTMLOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) TEXTHTMLOPTIONAL.getDataType();

    /**
      * <p>Set value for property TextHtmlMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextHtmlMandatory</tt></td></tr>
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
    public abstract void setTextHtmlMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property TextHtmlMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_TextHtmlMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getTextHtmlMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the TextHtmlMandatory property.
      */
    public static final String TEXTHTMLMANDATORY_name = "TextHtmlMandatory";

    /**
      * The TextHtmlMandatory PropertyType.
      */
    public static final PropertyType TEXTHTMLMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_TextHtmlMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType TEXTHTMLMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) TEXTHTMLMANDATORY.getDataType();

    /**
      * <p>Set value for property GifOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_GifOptional</tt></td></tr>
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
    public abstract void setGifOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property GifOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_GifOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getGifOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the GifOptional property.
      */
    public static final String GIFOPTIONAL_name = "GifOptional";

    /**
      * The GifOptional PropertyType.
      */
    public static final PropertyType GIFOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_GifOptional" );
    public static final org.infogrid.model.primitives.BlobDataType GIFOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) GIFOPTIONAL.getDataType();

    /**
      * <p>Set value for property GifMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_GifMandatory</tt></td></tr>
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
    public abstract void setGifMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property GifMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_GifMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getGifMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the GifMandatory property.
      */
    public static final String GIFMANDATORY_name = "GifMandatory";

    /**
      * The GifMandatory PropertyType.
      */
    public static final PropertyType GIFMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_GifMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType GIFMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) GIFMANDATORY.getDataType();

    /**
      * <p>Set value for property JpgOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_JpgOptional</tt></td></tr>
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
    public abstract void setJpgOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property JpgOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_JpgOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getJpgOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the JpgOptional property.
      */
    public static final String JPGOPTIONAL_name = "JpgOptional";

    /**
      * The JpgOptional PropertyType.
      */
    public static final PropertyType JPGOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_JpgOptional" );
    public static final org.infogrid.model.primitives.BlobDataType JPGOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) JPGOPTIONAL.getDataType();

    /**
      * <p>Set value for property JpgMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_JpgMandatory</tt></td></tr>
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
    public abstract void setJpgMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property JpgMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_JpgMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getJpgMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the JpgMandatory property.
      */
    public static final String JPGMANDATORY_name = "JpgMandatory";

    /**
      * The JpgMandatory PropertyType.
      */
    public static final PropertyType JPGMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_JpgMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType JPGMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) JPGMANDATORY.getDataType();

    /**
      * <p>Set value for property PngOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_PngOptional</tt></td></tr>
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
    public abstract void setPngOptional(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property PngOptional.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_PngOptional</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getPngOptional()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the PngOptional property.
      */
    public static final String PNGOPTIONAL_name = "PngOptional";

    /**
      * The PngOptional PropertyType.
      */
    public static final PropertyType PNGOPTIONAL = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_PngOptional" );
    public static final org.infogrid.model.primitives.BlobDataType PNGOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) PNGOPTIONAL.getDataType();

    /**
      * <p>Set value for property PngMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_PngMandatory</tt></td></tr>
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
    public abstract void setPngMandatory(
            org.infogrid.model.primitives.BlobValue newValue )
        throws
            org.infogrid.mesh.NotPermittedException,
            org.infogrid.meshbase.transaction.TransactionException,
            org.infogrid.mesh.IllegalPropertyValueException;

    /**
      * <p>Obtain value for property PngMandatory.</p>
      *
      * <table>
      *  <tr><td>Identifier:</td><td><tt>org.infogrid.model.Test/BlobTest_PngMandatory</tt></td></tr>
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
    public abstract org.infogrid.model.primitives.BlobValue getPngMandatory()
        throws
            org.infogrid.mesh.NotPermittedException;

    /**
      * Name of the PngMandatory property.
      */
    public static final String PNGMANDATORY_name = "PngMandatory";

    /**
      * The PngMandatory PropertyType.
      */
    public static final PropertyType PNGMANDATORY = ModelBaseSingleton.findPropertyType( "org.infogrid.model.Test/BlobTest_PngMandatory" );
    public static final org.infogrid.model.primitives.BlobDataType PNGMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) PNGMANDATORY.getDataType();

}
