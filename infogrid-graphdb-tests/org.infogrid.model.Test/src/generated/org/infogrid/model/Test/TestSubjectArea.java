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
  * <p>Collects the MeshTypes in this Subject Area for simpler access.</p>
  */
public abstract class TestSubjectArea
{
    /**
      * The SubjectArea itself.
      */
    public static final SubjectArea _SA = ModelBaseSingleton.findSubjectArea( "org.infogrid.model.Test" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType A = org.infogrid.model.Test.A._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType A_X = org.infogrid.model.Test.A.X;
    public static final org.infogrid.model.primitives.StringDataType A_X_type = (org.infogrid.model.primitives.StringDataType) A_X.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType A_XX = org.infogrid.model.Test.A.XX;
    public static final org.infogrid.model.primitives.BlobDataType A_XX_type = (org.infogrid.model.primitives.BlobDataType) A_XX.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType A_READONLY = org.infogrid.model.Test.A.READONLY;
    public static final org.infogrid.model.primitives.BooleanDataType A_READONLY_type = (org.infogrid.model.primitives.BooleanDataType) A_READONLY.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType AA = org.infogrid.model.Test.AA._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType AA_Y = org.infogrid.model.Test.AA.Y;
    public static final org.infogrid.model.primitives.FloatDataType AA_Y_type = (org.infogrid.model.primitives.FloatDataType) AA_Y.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType B = org.infogrid.model.Test.B._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType B_Z = org.infogrid.model.Test.B.Z;
    public static final org.infogrid.model.primitives.EnumeratedDataType B_Z_type = (org.infogrid.model.primitives.EnumeratedDataType) B_Z.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue B_Z_type_VALUE1 = B_Z_type.select( "Value1" );
    public static final EnumeratedValue B_Z_type_VALUE2 = B_Z_type.select( "Value2" );
    public static final EnumeratedValue B_Z_type_VALUE3 = B_Z_type.select( "Value3" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType B_U = org.infogrid.model.Test.B.U;
    public static final org.infogrid.model.primitives.StringDataType B_U_type = (org.infogrid.model.primitives.StringDataType) B_U.getDataType();

    public static final EntityType C = org.infogrid.model.Test.C._TYPE;

    public static final EntityType D = org.infogrid.model.Test.D._TYPE;

    public static final EntityType SIMPLE1 = org.infogrid.model.Test.Simple1._TYPE;

    public static final EntityType SIMPLE2 = org.infogrid.model.Test.Simple2._TYPE;

    public static final EntityType SIMPLE3 = org.infogrid.model.Test.Simple3._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType OPTIONALPROPERTIES = org.infogrid.model.Test.OptionalProperties._TYPE;

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEANY = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATATYPEANY;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEANY.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAIN = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEPLAIN.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATAHTML = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATAHTML.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEIMAGE = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEIMAGE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEJPG = org.infogrid.model.Test.OptionalProperties.OPTIONALBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALPROPERTIES_OPTIONALBLOBDATATYPEJPG.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALBOOLEANDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType OPTIONALPROPERTIES_OPTIONALBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) OPTIONALPROPERTIES_OPTIONALBOOLEANDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALCOLORDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType OPTIONALPROPERTIES_OPTIONALCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) OPTIONALPROPERTIES_OPTIONALCOLORDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALCURRENCYDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType OPTIONALPROPERTIES_OPTIONALCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) OPTIONALPROPERTIES_OPTIONALCURRENCYDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE1 = OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE2 = OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE3 = OPTIONALPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALEXTENTDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType OPTIONALPROPERTIES_OPTIONALEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) OPTIONALPROPERTIES_OPTIONALEXTENTDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALFLOATDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType OPTIONALPROPERTIES_OPTIONALFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) OPTIONALPROPERTIES_OPTIONALFLOATDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALINTEGERDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType OPTIONALPROPERTIES_OPTIONALINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) OPTIONALPROPERTIES_OPTIONALINTEGERDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALMULTIPLICITYDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType OPTIONALPROPERTIES_OPTIONALMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) OPTIONALPROPERTIES_OPTIONALMULTIPLICITYDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALPOINTDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType OPTIONALPROPERTIES_OPTIONALPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) OPTIONALPROPERTIES_OPTIONALPOINTDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALSTRINGDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType OPTIONALPROPERTIES_OPTIONALSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALPROPERTIES_OPTIONALSTRINGDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType OPTIONALPROPERTIES_OPTIONALSTRINGREGEXDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType OPTIONALPROPERTIES_OPTIONALSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALPROPERTIES_OPTIONALSTRINGREGEXDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALTIMEPERIODDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType OPTIONALPROPERTIES_OPTIONALTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) OPTIONALPROPERTIES_OPTIONALTIMEPERIODDATATYPE.getDataType();

    public static final PropertyType OPTIONALPROPERTIES_OPTIONALTIMESTAMPDATATYPE = org.infogrid.model.Test.OptionalProperties.OPTIONALTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType OPTIONALPROPERTIES_OPTIONALTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) OPTIONALPROPERTIES_OPTIONALTIMESTAMPDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType MANDATORYPROPERTIES = org.infogrid.model.Test.MandatoryProperties._TYPE;

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEANY = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATATYPEANY;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEANY.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAIN = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEPLAIN.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATAHTML = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATAHTML.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEIMAGE = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEIMAGE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEJPG = org.infogrid.model.Test.MandatoryProperties.MANDATORYBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYPROPERTIES_MANDATORYBLOBDATATYPEJPG.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYBOOLEANDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType MANDATORYPROPERTIES_MANDATORYBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) MANDATORYPROPERTIES_MANDATORYBOOLEANDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYCOLORDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType MANDATORYPROPERTIES_MANDATORYCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) MANDATORYPROPERTIES_MANDATORYCOLORDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYCURRENCYDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType MANDATORYPROPERTIES_MANDATORYCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) MANDATORYPROPERTIES_MANDATORYCURRENCYDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE1 = MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE2 = MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE3 = MANDATORYPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value3" );

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYEXTENTDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType MANDATORYPROPERTIES_MANDATORYEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) MANDATORYPROPERTIES_MANDATORYEXTENTDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYFLOATDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType MANDATORYPROPERTIES_MANDATORYFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) MANDATORYPROPERTIES_MANDATORYFLOATDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYINTEGERDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType MANDATORYPROPERTIES_MANDATORYINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) MANDATORYPROPERTIES_MANDATORYINTEGERDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYMULTIPLICITYDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType MANDATORYPROPERTIES_MANDATORYMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) MANDATORYPROPERTIES_MANDATORYMULTIPLICITYDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYPOINTDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType MANDATORYPROPERTIES_MANDATORYPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) MANDATORYPROPERTIES_MANDATORYPOINTDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYSTRINGDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType MANDATORYPROPERTIES_MANDATORYSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYPROPERTIES_MANDATORYSTRINGDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType MANDATORYPROPERTIES_MANDATORYSTRINGREGEXDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType MANDATORYPROPERTIES_MANDATORYSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYPROPERTIES_MANDATORYSTRINGREGEXDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYTIMEPERIODDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType MANDATORYPROPERTIES_MANDATORYTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) MANDATORYPROPERTIES_MANDATORYTIMEPERIODDATATYPE.getDataType();

    public static final PropertyType MANDATORYPROPERTIES_MANDATORYTIMESTAMPDATATYPE = org.infogrid.model.Test.MandatoryProperties.MANDATORYTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType MANDATORYPROPERTIES_MANDATORYTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) MANDATORYPROPERTIES_MANDATORYTIMESTAMPDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType ALLPROPERTIES = org.infogrid.model.Test.AllProperties._TYPE;

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATATYPEANY = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATATYPEANY;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATATYPEANY.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAINORHTML.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAIN = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATATYPEPLAIN.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATAHTML = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATAHTML.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATATYPEIMAGE = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATATYPEIMAGE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBLOBDATATYPEJPG = org.infogrid.model.Test.AllProperties.OPTIONALBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_OPTIONALBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_OPTIONALBLOBDATATYPEJPG.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALBOOLEANDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType ALLPROPERTIES_OPTIONALBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) ALLPROPERTIES_OPTIONALBOOLEANDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALCOLORDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType ALLPROPERTIES_OPTIONALCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) ALLPROPERTIES_OPTIONALCOLORDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALCURRENCYDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType ALLPROPERTIES_OPTIONALCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) ALLPROPERTIES_OPTIONALCURRENCYDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE1 = ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE2 = ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type_VALUE3 = ALLPROPERTIES_OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );

    public static final PropertyType ALLPROPERTIES_OPTIONALEXTENTDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType ALLPROPERTIES_OPTIONALEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) ALLPROPERTIES_OPTIONALEXTENTDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALFLOATDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType ALLPROPERTIES_OPTIONALFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) ALLPROPERTIES_OPTIONALFLOATDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALINTEGERDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType ALLPROPERTIES_OPTIONALINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) ALLPROPERTIES_OPTIONALINTEGERDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALMULTIPLICITYDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType ALLPROPERTIES_OPTIONALMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) ALLPROPERTIES_OPTIONALMULTIPLICITYDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALPOINTDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType ALLPROPERTIES_OPTIONALPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) ALLPROPERTIES_OPTIONALPOINTDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALSTRINGDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType ALLPROPERTIES_OPTIONALSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) ALLPROPERTIES_OPTIONALSTRINGDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType ALLPROPERTIES_OPTIONALSTRINGREGEXDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType ALLPROPERTIES_OPTIONALSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) ALLPROPERTIES_OPTIONALSTRINGREGEXDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALTIMEPERIODDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType ALLPROPERTIES_OPTIONALTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) ALLPROPERTIES_OPTIONALTIMEPERIODDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_OPTIONALTIMESTAMPDATATYPE = org.infogrid.model.Test.AllProperties.OPTIONALTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType ALLPROPERTIES_OPTIONALTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) ALLPROPERTIES_OPTIONALTIMESTAMPDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATATYPEANY = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATATYPEANY;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATATYPEANY.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAINORHTML.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAIN = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATATYPEPLAIN.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATAHTML = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATAHTML.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATATYPEIMAGE = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATATYPEIMAGE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBLOBDATATYPEJPG = org.infogrid.model.Test.AllProperties.MANDATORYBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType ALLPROPERTIES_MANDATORYBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) ALLPROPERTIES_MANDATORYBLOBDATATYPEJPG.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYBOOLEANDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType ALLPROPERTIES_MANDATORYBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) ALLPROPERTIES_MANDATORYBOOLEANDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYCOLORDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType ALLPROPERTIES_MANDATORYCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) ALLPROPERTIES_MANDATORYCOLORDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYCURRENCYDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType ALLPROPERTIES_MANDATORYCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) ALLPROPERTIES_MANDATORYCURRENCYDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE1 = ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE2 = ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type_VALUE3 = ALLPROPERTIES_MANDATORYENUMERATEDDATATYPE_type.select( "Value3" );

    public static final PropertyType ALLPROPERTIES_MANDATORYEXTENTDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType ALLPROPERTIES_MANDATORYEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) ALLPROPERTIES_MANDATORYEXTENTDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYFLOATDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType ALLPROPERTIES_MANDATORYFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) ALLPROPERTIES_MANDATORYFLOATDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYINTEGERDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType ALLPROPERTIES_MANDATORYINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) ALLPROPERTIES_MANDATORYINTEGERDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYMULTIPLICITYDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType ALLPROPERTIES_MANDATORYMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) ALLPROPERTIES_MANDATORYMULTIPLICITYDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYPOINTDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType ALLPROPERTIES_MANDATORYPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) ALLPROPERTIES_MANDATORYPOINTDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYSTRINGDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType ALLPROPERTIES_MANDATORYSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) ALLPROPERTIES_MANDATORYSTRINGDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType ALLPROPERTIES_MANDATORYSTRINGREGEXDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType ALLPROPERTIES_MANDATORYSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) ALLPROPERTIES_MANDATORYSTRINGREGEXDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYTIMEPERIODDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType ALLPROPERTIES_MANDATORYTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) ALLPROPERTIES_MANDATORYTIMEPERIODDATATYPE.getDataType();

    public static final PropertyType ALLPROPERTIES_MANDATORYTIMESTAMPDATATYPE = org.infogrid.model.Test.AllProperties.MANDATORYTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType ALLPROPERTIES_MANDATORYTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) ALLPROPERTIES_MANDATORYTIMESTAMPDATATYPE.getDataType();

    public static final EntityType OPTIONALBLOBANY = org.infogrid.model.Test.OptionalBlobAny._TYPE;

    public static final PropertyType OPTIONALBLOBANY_OPTIONALBLOBDATATYPEANY = org.infogrid.model.Test.OptionalBlobAny.OPTIONALBLOBDATATYPEANY;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBANY_OPTIONALBLOBDATATYPEANY_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBANY_OPTIONALBLOBDATATYPEANY.getDataType();

    public static final EntityType OPTIONALBLOBPLAINORHTML = org.infogrid.model.Test.OptionalBlobPlainOrHtml._TYPE;

    public static final PropertyType OPTIONALBLOBPLAINORHTML_OPTIONALBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.OptionalBlobPlainOrHtml.OPTIONALBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBPLAINORHTML_OPTIONALBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBPLAINORHTML_OPTIONALBLOBDATATYPEPLAINORHTML.getDataType();

    public static final EntityType OPTIONALBLOBPLAIN = org.infogrid.model.Test.OptionalBlobPlain._TYPE;

    public static final PropertyType OPTIONALBLOBPLAIN_OPTIONALBLOBDATATYPEPLAIN = org.infogrid.model.Test.OptionalBlobPlain.OPTIONALBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBPLAIN_OPTIONALBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBPLAIN_OPTIONALBLOBDATATYPEPLAIN.getDataType();

    public static final EntityType OPTIONALBLOBHTML = org.infogrid.model.Test.OptionalBlobHtml._TYPE;

    public static final PropertyType OPTIONALBLOBHTML_OPTIONALBLOBDATAHTML = org.infogrid.model.Test.OptionalBlobHtml.OPTIONALBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBHTML_OPTIONALBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBHTML_OPTIONALBLOBDATAHTML.getDataType();

    public static final EntityType OPTIONALBLOBIMAGE = org.infogrid.model.Test.OptionalBlobImage._TYPE;

    public static final PropertyType OPTIONALBLOBIMAGE_OPTIONALBLOBDATATYPEIMAGE = org.infogrid.model.Test.OptionalBlobImage.OPTIONALBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBIMAGE_OPTIONALBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBIMAGE_OPTIONALBLOBDATATYPEIMAGE.getDataType();

    public static final EntityType OPTIONALBLOBJPG = org.infogrid.model.Test.OptionalBlobJpg._TYPE;

    public static final PropertyType OPTIONALBLOBJPG_OPTIONALBLOBDATATYPEJPG = org.infogrid.model.Test.OptionalBlobJpg.OPTIONALBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType OPTIONALBLOBJPG_OPTIONALBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) OPTIONALBLOBJPG_OPTIONALBLOBDATATYPEJPG.getDataType();

    public static final EntityType OPTIONALBOOLEAN = org.infogrid.model.Test.OptionalBoolean._TYPE;

    public static final PropertyType OPTIONALBOOLEAN_OPTIONALBOOLEANDATATYPE = org.infogrid.model.Test.OptionalBoolean.OPTIONALBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType OPTIONALBOOLEAN_OPTIONALBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) OPTIONALBOOLEAN_OPTIONALBOOLEANDATATYPE.getDataType();

    public static final EntityType OPTIONALCOLOR = org.infogrid.model.Test.OptionalColor._TYPE;

    public static final PropertyType OPTIONALCOLOR_OPTIONALCOLORDATATYPE = org.infogrid.model.Test.OptionalColor.OPTIONALCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType OPTIONALCOLOR_OPTIONALCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) OPTIONALCOLOR_OPTIONALCOLORDATATYPE.getDataType();

    public static final EntityType OPTIONALCURRENCY = org.infogrid.model.Test.OptionalCurrency._TYPE;

    public static final PropertyType OPTIONALCURRENCY_OPTIONALCURRENCYDATATYPE = org.infogrid.model.Test.OptionalCurrency.OPTIONALCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType OPTIONALCURRENCY_OPTIONALCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) OPTIONALCURRENCY_OPTIONALCURRENCYDATATYPE.getDataType();

    public static final EntityType OPTIONALENUMERATED = org.infogrid.model.Test.OptionalEnumerated._TYPE;

    public static final PropertyType OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE = org.infogrid.model.Test.OptionalEnumerated.OPTIONALENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type_VALUE1 = OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type_VALUE2 = OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type_VALUE3 = OPTIONALENUMERATED_OPTIONALENUMERATEDDATATYPE_type.select( "Value3" );

    public static final EntityType OPTIONALEXTENT = org.infogrid.model.Test.OptionalExtent._TYPE;

    public static final PropertyType OPTIONALEXTENT_OPTIONALEXTENTDATATYPE = org.infogrid.model.Test.OptionalExtent.OPTIONALEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType OPTIONALEXTENT_OPTIONALEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) OPTIONALEXTENT_OPTIONALEXTENTDATATYPE.getDataType();

    public static final EntityType OPTIONALFLOAT = org.infogrid.model.Test.OptionalFloat._TYPE;

    public static final PropertyType OPTIONALFLOAT_OPTIONALFLOATDATATYPE = org.infogrid.model.Test.OptionalFloat.OPTIONALFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType OPTIONALFLOAT_OPTIONALFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) OPTIONALFLOAT_OPTIONALFLOATDATATYPE.getDataType();

    public static final EntityType OPTIONALINTEGER = org.infogrid.model.Test.OptionalInteger._TYPE;

    public static final PropertyType OPTIONALINTEGER_OPTIONALINTEGERDATATYPE = org.infogrid.model.Test.OptionalInteger.OPTIONALINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType OPTIONALINTEGER_OPTIONALINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) OPTIONALINTEGER_OPTIONALINTEGERDATATYPE.getDataType();

    public static final EntityType OPTIONALMULTIPLICITY = org.infogrid.model.Test.OptionalMultiplicity._TYPE;

    public static final PropertyType OPTIONALMULTIPLICITY_OPTIONALMULTIPLICITYDATATYPE = org.infogrid.model.Test.OptionalMultiplicity.OPTIONALMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType OPTIONALMULTIPLICITY_OPTIONALMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) OPTIONALMULTIPLICITY_OPTIONALMULTIPLICITYDATATYPE.getDataType();

    public static final EntityType OPTIONALPOINT = org.infogrid.model.Test.OptionalPoint._TYPE;

    public static final PropertyType OPTIONALPOINT_OPTIONALPOINTDATATYPE = org.infogrid.model.Test.OptionalPoint.OPTIONALPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType OPTIONALPOINT_OPTIONALPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) OPTIONALPOINT_OPTIONALPOINTDATATYPE.getDataType();

    public static final EntityType OPTIONALSTRING = org.infogrid.model.Test.OptionalString._TYPE;

    public static final PropertyType OPTIONALSTRING_OPTIONALSTRINGDATATYPE = org.infogrid.model.Test.OptionalString.OPTIONALSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRING_OPTIONALSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRING_OPTIONALSTRINGDATATYPE.getDataType();

    public static final EntityType OPTIONALSTRINGREGEX = org.infogrid.model.Test.OptionalStringRegex._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType OPTIONALSTRINGREGEX_OPTIONALSTRINGREGEXDATATYPE = org.infogrid.model.Test.OptionalStringRegex.OPTIONALSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType OPTIONALSTRINGREGEX_OPTIONALSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) OPTIONALSTRINGREGEX_OPTIONALSTRINGREGEXDATATYPE.getDataType();

    public static final EntityType OPTIONALTIMEPERIOD = org.infogrid.model.Test.OptionalTimePeriod._TYPE;

    public static final PropertyType OPTIONALTIMEPERIOD_OPTIONALTIMEPERIODDATATYPE = org.infogrid.model.Test.OptionalTimePeriod.OPTIONALTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType OPTIONALTIMEPERIOD_OPTIONALTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) OPTIONALTIMEPERIOD_OPTIONALTIMEPERIODDATATYPE.getDataType();

    public static final EntityType OPTIONALTIMESTAMP = org.infogrid.model.Test.OptionalTimeStamp._TYPE;

    public static final PropertyType OPTIONALTIMESTAMP_OPTIONALTIMESTAMPDATATYPE = org.infogrid.model.Test.OptionalTimeStamp.OPTIONALTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType OPTIONALTIMESTAMP_OPTIONALTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) OPTIONALTIMESTAMP_OPTIONALTIMESTAMPDATATYPE.getDataType();

    public static final EntityType MANDATORYBLOBANY = org.infogrid.model.Test.MandatoryBlobAny._TYPE;

    public static final PropertyType MANDATORYBLOBANY_MANDATORYBLOBDATATYPE = org.infogrid.model.Test.MandatoryBlobAny.MANDATORYBLOBDATATYPE;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBANY_MANDATORYBLOBDATATYPE_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBANY_MANDATORYBLOBDATATYPE.getDataType();

    public static final EntityType MANDATORYBLOBPLAINORHTML = org.infogrid.model.Test.MandatoryBlobPlainOrHtml._TYPE;

    public static final PropertyType MANDATORYBLOBPLAINORHTML_MANDATORYBLOBDATATYPEPLAINORHTML = org.infogrid.model.Test.MandatoryBlobPlainOrHtml.MANDATORYBLOBDATATYPEPLAINORHTML;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBPLAINORHTML_MANDATORYBLOBDATATYPEPLAINORHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBPLAINORHTML_MANDATORYBLOBDATATYPEPLAINORHTML.getDataType();

    public static final EntityType MANDATORYBLOBPLAIN = org.infogrid.model.Test.MandatoryBlobPlain._TYPE;

    public static final PropertyType MANDATORYBLOBPLAIN_MANDATORYBLOBDATATYPEPLAIN = org.infogrid.model.Test.MandatoryBlobPlain.MANDATORYBLOBDATATYPEPLAIN;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBPLAIN_MANDATORYBLOBDATATYPEPLAIN_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBPLAIN_MANDATORYBLOBDATATYPEPLAIN.getDataType();

    public static final EntityType MANDATORYBLOBHTML = org.infogrid.model.Test.MandatoryBlobHtml._TYPE;

    public static final PropertyType MANDATORYBLOBHTML_MANDATORYBLOBDATAHTML = org.infogrid.model.Test.MandatoryBlobHtml.MANDATORYBLOBDATAHTML;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBHTML_MANDATORYBLOBDATAHTML_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBHTML_MANDATORYBLOBDATAHTML.getDataType();

    public static final EntityType MANDATORYBLOBIMAGE = org.infogrid.model.Test.MandatoryBlobImage._TYPE;

    public static final PropertyType MANDATORYBLOBIMAGE_MANDATORYBLOBDATATYPEIMAGE = org.infogrid.model.Test.MandatoryBlobImage.MANDATORYBLOBDATATYPEIMAGE;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBIMAGE_MANDATORYBLOBDATATYPEIMAGE_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBIMAGE_MANDATORYBLOBDATATYPEIMAGE.getDataType();

    public static final EntityType MANDATORYBLOBJPG = org.infogrid.model.Test.MandatoryBlobJpg._TYPE;

    public static final PropertyType MANDATORYBLOBJPG_MANDATORYBLOBDATATYPEJPG = org.infogrid.model.Test.MandatoryBlobJpg.MANDATORYBLOBDATATYPEJPG;
    public static final org.infogrid.model.primitives.BlobDataType MANDATORYBLOBJPG_MANDATORYBLOBDATATYPEJPG_type = (org.infogrid.model.primitives.BlobDataType) MANDATORYBLOBJPG_MANDATORYBLOBDATATYPEJPG.getDataType();

    public static final EntityType MANDATORYBOOLEAN = org.infogrid.model.Test.MandatoryBoolean._TYPE;

    public static final PropertyType MANDATORYBOOLEAN_MANDATORYBOOLEANDATATYPE = org.infogrid.model.Test.MandatoryBoolean.MANDATORYBOOLEANDATATYPE;
    public static final org.infogrid.model.primitives.BooleanDataType MANDATORYBOOLEAN_MANDATORYBOOLEANDATATYPE_type = (org.infogrid.model.primitives.BooleanDataType) MANDATORYBOOLEAN_MANDATORYBOOLEANDATATYPE.getDataType();

    public static final EntityType MANDATORYCOLOR = org.infogrid.model.Test.MandatoryColor._TYPE;

    public static final PropertyType MANDATORYCOLOR_MANDATORYCOLORDATATYPE = org.infogrid.model.Test.MandatoryColor.MANDATORYCOLORDATATYPE;
    public static final org.infogrid.model.primitives.ColorDataType MANDATORYCOLOR_MANDATORYCOLORDATATYPE_type = (org.infogrid.model.primitives.ColorDataType) MANDATORYCOLOR_MANDATORYCOLORDATATYPE.getDataType();

    public static final EntityType MANDATORYCURRENCY = org.infogrid.model.Test.MandatoryCurrency._TYPE;

    public static final PropertyType MANDATORYCURRENCY_MANDATORYCURRENCYDATATYPE = org.infogrid.model.Test.MandatoryCurrency.MANDATORYCURRENCYDATATYPE;
    public static final org.infogrid.model.primitives.CurrencyDataType MANDATORYCURRENCY_MANDATORYCURRENCYDATATYPE_type = (org.infogrid.model.primitives.CurrencyDataType) MANDATORYCURRENCY_MANDATORYCURRENCYDATATYPE.getDataType();

    public static final EntityType MANDATORYENUMERATED = org.infogrid.model.Test.MandatoryEnumerated._TYPE;

    public static final PropertyType MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE = org.infogrid.model.Test.MandatoryEnumerated.MANDATORYENUMERATEDDATATYPE;
    public static final org.infogrid.model.primitives.EnumeratedDataType MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type = (org.infogrid.model.primitives.EnumeratedDataType) MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE.getDataType();
    /* Values of the EnumeratedDataType. */
    public static final EnumeratedValue MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type_VALUE1 = MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type.select( "Value1" );
    public static final EnumeratedValue MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type_VALUE2 = MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type.select( "Value2" );
    public static final EnumeratedValue MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type_VALUE3 = MANDATORYENUMERATED_MANDATORYENUMERATEDDATATYPE_type.select( "Value3" );

    public static final EntityType MANDATORYEXTENT = org.infogrid.model.Test.MandatoryExtent._TYPE;

    public static final PropertyType MANDATORYEXTENT_MANDATORYEXTENTDATATYPE = org.infogrid.model.Test.MandatoryExtent.MANDATORYEXTENTDATATYPE;
    public static final org.infogrid.model.primitives.ExtentDataType MANDATORYEXTENT_MANDATORYEXTENTDATATYPE_type = (org.infogrid.model.primitives.ExtentDataType) MANDATORYEXTENT_MANDATORYEXTENTDATATYPE.getDataType();

    public static final EntityType MANDATORYFLOAT = org.infogrid.model.Test.MandatoryFloat._TYPE;

    public static final PropertyType MANDATORYFLOAT_MANDATORYFLOATDATATYPE = org.infogrid.model.Test.MandatoryFloat.MANDATORYFLOATDATATYPE;
    public static final org.infogrid.model.primitives.FloatDataType MANDATORYFLOAT_MANDATORYFLOATDATATYPE_type = (org.infogrid.model.primitives.FloatDataType) MANDATORYFLOAT_MANDATORYFLOATDATATYPE.getDataType();

    public static final EntityType MANDATORYINTEGER = org.infogrid.model.Test.MandatoryInteger._TYPE;

    public static final PropertyType MANDATORYINTEGER_MANDATORYINTEGERDATATYPE = org.infogrid.model.Test.MandatoryInteger.MANDATORYINTEGERDATATYPE;
    public static final org.infogrid.model.primitives.IntegerDataType MANDATORYINTEGER_MANDATORYINTEGERDATATYPE_type = (org.infogrid.model.primitives.IntegerDataType) MANDATORYINTEGER_MANDATORYINTEGERDATATYPE.getDataType();

    public static final EntityType MANDATORYMULTIPLICITY = org.infogrid.model.Test.MandatoryMultiplicity._TYPE;

    public static final PropertyType MANDATORYMULTIPLICITY_MANDATORYMULTIPLICITYDATATYPE = org.infogrid.model.Test.MandatoryMultiplicity.MANDATORYMULTIPLICITYDATATYPE;
    public static final org.infogrid.model.primitives.MultiplicityDataType MANDATORYMULTIPLICITY_MANDATORYMULTIPLICITYDATATYPE_type = (org.infogrid.model.primitives.MultiplicityDataType) MANDATORYMULTIPLICITY_MANDATORYMULTIPLICITYDATATYPE.getDataType();

    public static final EntityType MANDATORYPOINT = org.infogrid.model.Test.MandatoryPoint._TYPE;

    public static final PropertyType MANDATORYPOINT_MANDATORYPOINTDATATYPE = org.infogrid.model.Test.MandatoryPoint.MANDATORYPOINTDATATYPE;
    public static final org.infogrid.model.primitives.PointDataType MANDATORYPOINT_MANDATORYPOINTDATATYPE_type = (org.infogrid.model.primitives.PointDataType) MANDATORYPOINT_MANDATORYPOINTDATATYPE.getDataType();

    public static final EntityType MANDATORYSTRING = org.infogrid.model.Test.MandatoryString._TYPE;

    public static final PropertyType MANDATORYSTRING_MANDATORYSTRINGDATATYPE = org.infogrid.model.Test.MandatoryString.MANDATORYSTRINGDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType MANDATORYSTRING_MANDATORYSTRINGDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYSTRING_MANDATORYSTRINGDATATYPE.getDataType();

    public static final EntityType MANDATORYSTRINGREGEX = org.infogrid.model.Test.MandatoryStringRegex._TYPE;

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final PropertyType MANDATORYSTRINGREGEX_MANDATORYSTRINGREGEXDATATYPE = org.infogrid.model.Test.MandatoryStringRegex.MANDATORYSTRINGREGEXDATATYPE;
    public static final org.infogrid.model.primitives.StringDataType MANDATORYSTRINGREGEX_MANDATORYSTRINGREGEXDATATYPE_type = (org.infogrid.model.primitives.StringDataType) MANDATORYSTRINGREGEX_MANDATORYSTRINGREGEXDATATYPE.getDataType();

    public static final EntityType MANDATORYTIMEPERIOD = org.infogrid.model.Test.MandatoryTimePeriod._TYPE;

    public static final PropertyType MANDATORYTIMEPERIOD_MANDATORYTIMEPERIODDATATYPE = org.infogrid.model.Test.MandatoryTimePeriod.MANDATORYTIMEPERIODDATATYPE;
    public static final org.infogrid.model.primitives.TimePeriodDataType MANDATORYTIMEPERIOD_MANDATORYTIMEPERIODDATATYPE_type = (org.infogrid.model.primitives.TimePeriodDataType) MANDATORYTIMEPERIOD_MANDATORYTIMEPERIODDATATYPE.getDataType();

    public static final EntityType MANDATORYTIMESTAMP = org.infogrid.model.Test.MandatoryTimeStamp._TYPE;

    public static final PropertyType MANDATORYTIMESTAMP_MANDATORYTIMESTAMPDATATYPE = org.infogrid.model.Test.MandatoryTimeStamp.MANDATORYTIMESTAMPDATATYPE;
    public static final org.infogrid.model.primitives.TimeStampDataType MANDATORYTIMESTAMP_MANDATORYTIMESTAMPDATATYPE_type = (org.infogrid.model.primitives.TimeStampDataType) MANDATORYTIMESTAMP_MANDATORYTIMESTAMPDATATYPE.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType SIMPLEBLOBTEST = org.infogrid.model.Test.SimpleBlobTest._TYPE;

    public static final PropertyType SIMPLEBLOBTEST_ANYOPTIONAL = org.infogrid.model.Test.SimpleBlobTest.ANYOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType SIMPLEBLOBTEST_ANYOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) SIMPLEBLOBTEST_ANYOPTIONAL.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final EntityType BLOBTEST = org.infogrid.model.Test.BlobTest._TYPE;

    public static final PropertyType BLOBTEST_ANYOPTIONAL = org.infogrid.model.Test.BlobTest.ANYOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_ANYOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_ANYOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_ANYMANDATORY = org.infogrid.model.Test.BlobTest.ANYMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_ANYMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_ANYMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_TEXTPLAINORHTMLOPTIONAL = org.infogrid.model.Test.BlobTest.TEXTPLAINORHTMLOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTPLAINORHTMLOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTPLAINORHTMLOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_TEXTPLAINORHTMLMANDATORY = org.infogrid.model.Test.BlobTest.TEXTPLAINORHTMLMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTPLAINORHTMLMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTPLAINORHTMLMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_TEXTPLAINOPTIONAL = org.infogrid.model.Test.BlobTest.TEXTPLAINOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTPLAINOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTPLAINOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_TEXTPLAINMANDATORY = org.infogrid.model.Test.BlobTest.TEXTPLAINMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTPLAINMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTPLAINMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_TEXTHTMLOPTIONAL = org.infogrid.model.Test.BlobTest.TEXTHTMLOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTHTMLOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTHTMLOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_TEXTHTMLMANDATORY = org.infogrid.model.Test.BlobTest.TEXTHTMLMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_TEXTHTMLMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_TEXTHTMLMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_GIFOPTIONAL = org.infogrid.model.Test.BlobTest.GIFOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_GIFOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_GIFOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_GIFMANDATORY = org.infogrid.model.Test.BlobTest.GIFMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_GIFMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_GIFMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_JPGOPTIONAL = org.infogrid.model.Test.BlobTest.JPGOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_JPGOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_JPGOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_JPGMANDATORY = org.infogrid.model.Test.BlobTest.JPGMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_JPGMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_JPGMANDATORY.getDataType();

    public static final PropertyType BLOBTEST_PNGOPTIONAL = org.infogrid.model.Test.BlobTest.PNGOPTIONAL;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_PNGOPTIONAL_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_PNGOPTIONAL.getDataType();

    public static final PropertyType BLOBTEST_PNGMANDATORY = org.infogrid.model.Test.BlobTest.PNGMANDATORY;
    public static final org.infogrid.model.primitives.BlobDataType BLOBTEST_PNGMANDATORY_type = (org.infogrid.model.primitives.BlobDataType) BLOBTEST_PNGMANDATORY.getDataType();

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType R = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/R" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType RR = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/RR" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType S = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/S" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType AR1A = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/AR1A" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType AR2A = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/AR2A" );

    /**
      * PlainInterpretTextTextOrBinaryString
      */
    public static final RelationshipType ARANY = ModelBaseSingleton.findRelationshipType( "org.infogrid.model.Test/ARAny" );

}
