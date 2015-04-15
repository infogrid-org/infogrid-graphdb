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
// on Wed, 2015-04-15 20:21:35 +0000
//

package org.infogrid.model.Test.V;

import org.infogrid.modelbase.*;
import org.infogrid.model.primitives.*;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalToPropertySpecification;
import org.infogrid.module.ModuleException;
import org.infogrid.module.ModuleRequirement;
import java.io.IOException;

/**
  * This class loads the model of the Test Subject Area Subject Area.
  */
public class SubjectAreaLoader
        extends
            ModelLoader
{
    /**
      * Constructor.
      *
      * @param modelBase the ModelBase into which the SubjectArea will be loaded
      * @param loader the ClassLoader to be used for resolving resources
      */
    public SubjectAreaLoader(
            ModelBase   modelBase,
            ClassLoader loader )
    {
        super( modelBase );
        theLoader = loader;
    }

    /**
     * Instruct this object to instantiate its model.
     *
     * @param theInstantiator the MeshTypeLifecycleManager which shall be used to instantiate
     * @param now time the run was started
     * @return the instantiated SubjectArea(s)
     * @throws MeshTypeNotFoundException thrown if there was an undeclared dependency in the model that could not be resolved
     * @throws InheritanceConflictException thrown if there was a conflict in the inheritance hierarchy of the newly loaded model
     * @throws IOException thrown if reading the model failed
     */
    protected SubjectArea [] loadModel(
            MeshTypeLifecycleManager theInstantiator,
            TimeStampValue           now )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException,
            org.infogrid.model.primitives.UnknownEnumeratedValueException
    {
        org.infogrid.util.logging.Log log = org.infogrid.util.logging.Log.getLogInstance( getClass() );
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "loadModel" );
        }

        SubjectArea theSa = theInstantiator.createSubjectArea(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test" ),   // IdentifierValue           theIdentifier,
                org.infogrid.model.primitives.StringValue.create( "org.infogrid.model.Test" ),   // StringValue               theName,
                null,   // StringValue               theVersion,
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Test Subject Area" ) ),   // L10Map                    theUserNames,
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "The Test Subject Area is only used to test InfoGrid code." , "text/html" ) ),   // L10Map                    theUserDescriptions,
                new SubjectArea[] { },   // SubjectArea []            theSubjectAreaDependencies,
                theLoader,   // ClassLoader               theClassLoader,
                org.infogrid.model.primitives.BooleanValue.TRUE,   // BooleanValue              doGenerateInterfaceCode,
                org.infogrid.model.primitives.BooleanValue.TRUE ); // BooleanValue              doGenerateImplementationCode )

        // generateLoadOneEntity section

        EntityType obj0 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ),
                org.infogrid.model.primitives.StringValue.create( "A" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The A EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This EntityType is a test EntityType." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.StringDataType obj0_pt0_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj0_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A_X" ),
                org.infogrid.model.primitives.StringValue.create( "X" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The X PropertyType of the A EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj0,
                theSa,
                obj0_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj0_pt1_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj0_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A_XX" ),
                org.infogrid.model.primitives.StringValue.create( "XX" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The XX PropertyType of the A EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj0,
                theSa,
                obj0_pt1_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BooleanDataType obj0_pt2_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj0_pt2 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A_ReadOnly" ),
                org.infogrid.model.primitives.StringValue.create( "ReadOnly" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The ReadOnly PropertyType of the A EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj0,
                theSa,
                obj0_pt2_type,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj1 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AA" ),
                org.infogrid.model.primitives.StringValue.create( "AA" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The AA EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This EntityType is a test EntityType." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { theModelBase.findAttributableMeshTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ) },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.FloatDataType obj1_pt0_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj1_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AA_Y" ),
                org.infogrid.model.primitives.StringValue.create( "Y" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The Y PropertyType of the A EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj1,
                theSa,
                obj1_pt0_type,
                org.infogrid.model.primitives.FloatValue.create( 12.34 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj2 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/B" ),
                org.infogrid.model.primitives.StringValue.create( "B" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The B EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This EntityType is a test EntityType." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.EnumeratedDataType obj2_pt0_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj2_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/B_Z" ),
                org.infogrid.model.primitives.StringValue.create( "Z" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The Z PropertyType of the B EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj2,
                theSa,
                obj2_pt0_type,
                obj2_pt0_type.select( "Value2" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj2_pt1_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj2_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A_U" ),
                org.infogrid.model.primitives.StringValue.create( "U" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The U PropertyType of the B EntityType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "A property." , "text/html" ) ),
                obj2,
                theSa,
                obj2_pt1_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj3 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/C" ),
                org.infogrid.model.primitives.StringValue.create( "C" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "C" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        EntityType obj4 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/D" ),
                org.infogrid.model.primitives.StringValue.create( "D" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "D" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { theModelBase.findAttributableMeshTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/C" ) ) },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        EntityType obj5 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/Simple1" ),
                org.infogrid.model.primitives.StringValue.create( "Simple1" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Simple1" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        EntityType obj6 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/Simple2" ),
                org.infogrid.model.primitives.StringValue.create( "Simple2" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Simple2" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        EntityType obj7 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/Simple3" ),
                org.infogrid.model.primitives.StringValue.create( "Simple3" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Simple3" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        EntityType obj8 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalProperties" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalProperties" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "Holds properties of all optional InfoGrid PropertyTypes." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj8_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj8_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ) ),
                null,
                obj8,
                theSa,
                obj8_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj8_pt1_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj8_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ) ),
                null,
                obj8,
                theSa,
                obj8_pt1_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj8_pt2_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj8_pt2 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ) ),
                null,
                obj8,
                theSa,
                obj8_pt2_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj8_pt3_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj8_pt3 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ) ),
                null,
                obj8,
                theSa,
                obj8_pt3_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj8_pt4_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj8_pt4 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ) ),
                null,
                obj8,
                theSa,
                obj8_pt4_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj8_pt5_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj8_pt5 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ) ),
                null,
                obj8,
                theSa,
                obj8_pt5_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BooleanDataType obj8_pt6_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj8_pt6 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt6_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ColorDataType obj8_pt7_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj8_pt7 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt7_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.CurrencyDataType obj8_pt8_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj8_pt8 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt8_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.EnumeratedDataType obj8_pt9_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj8_pt9 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt9_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ExtentDataType obj8_pt10_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj8_pt10 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt10_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.FloatDataType obj8_pt11_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj8_pt11 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt11_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.IntegerDataType obj8_pt12_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj8_pt12 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt12_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.MultiplicityDataType obj8_pt13_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj8_pt13 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt13_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.PointDataType obj8_pt14_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj8_pt14 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt14_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj8_pt15_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj8_pt15 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt15_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj8_pt16_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj8_pt16 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj8,
                theSa,
                obj8_pt16_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimePeriodDataType obj8_pt17_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj8_pt17 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt17_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimeStampDataType obj8_pt18_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj8_pt18 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalProperties_OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ) ),
                null,
                obj8,
                theSa,
                obj8_pt18_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj9 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryProperties" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryProperties" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "Holds properties of all mandatory InfoGrid PropertyTypes." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj9_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj9_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataTypeAny" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeAny" ) ),
                null,
                obj9,
                theSa,
                obj9_pt0_type,
                obj9_pt0_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj9_pt1_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj9_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ) ),
                null,
                obj9,
                theSa,
                obj9_pt1_type,
                obj9_pt1_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj9_pt2_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj9_pt2 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ) ),
                null,
                obj9,
                theSa,
                obj9_pt2_type,
                obj9_pt2_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj9_pt3_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj9_pt3 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ) ),
                null,
                obj9,
                theSa,
                obj9_pt3_type,
                obj9_pt3_type.createBlobValue( "DEFAULT VALUE" , "text/html" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj9_pt4_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj9_pt4 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ) ),
                null,
                obj9,
                theSa,
                obj9_pt4_type,
                obj9_pt4_type.createBlobValue( "DEFAULT VALUE" , "image/gif" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj9_pt5_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj9_pt5 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ) ),
                null,
                obj9,
                theSa,
                obj9_pt5_type,
                obj9_pt5_type.createBlobValue( "DEFAULT VALUE" , "image/jpeg" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BooleanDataType obj9_pt6_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj9_pt6 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt6_type,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ColorDataType obj9_pt7_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj9_pt7 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt7_type,
                org.infogrid.model.primitives.ColorValue.create( 123 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.CurrencyDataType obj9_pt8_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj9_pt8 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt8_type,
                org.infogrid.model.primitives.CurrencyValue.create( true, 1, 0, "USD" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.EnumeratedDataType obj9_pt9_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj9_pt9 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt9_type,
                obj9_pt9_type.select( "Value2" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ExtentDataType obj9_pt10_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj9_pt10 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt10_type,
                org.infogrid.model.primitives.ExtentValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.FloatDataType obj9_pt11_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj9_pt11 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt11_type,
                org.infogrid.model.primitives.FloatValue.create( 1.2 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.IntegerDataType obj9_pt12_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj9_pt12 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt12_type,
                org.infogrid.model.primitives.IntegerValue.create( 1L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.MultiplicityDataType obj9_pt13_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj9_pt13 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt13_type,
                org.infogrid.model.primitives.MultiplicityValue.create( 1, org.infogrid.model.primitives.MultiplicityValue.N ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.PointDataType obj9_pt14_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj9_pt14 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt14_type,
                org.infogrid.model.primitives.PointValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj9_pt15_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj9_pt15 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt15_type,
                org.infogrid.model.primitives.StringValue.create( "DEFAULT" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj9_pt16_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "12.34.56.78" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj9_pt16 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj9,
                theSa,
                obj9_pt16_type,
                org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimePeriodDataType obj9_pt17_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj9_pt17 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt17_type,
                org.infogrid.model.primitives.TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (float) 6.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimeStampDataType obj9_pt18_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj9_pt18 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryProperties_MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ) ),
                null,
                obj9,
                theSa,
                obj9_pt18_type,
                org.infogrid.model.primitives.TimeStampValue.create( 978307199999L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj10 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties" ),
                org.infogrid.model.primitives.StringValue.create( "AllProperties" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "AllProperties" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "Holds properties of all InfoGrid PropertyTypes." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj10_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj10_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ) ),
                null,
                obj10,
                theSa,
                obj10_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt1_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj10_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ) ),
                null,
                obj10,
                theSa,
                obj10_pt1_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt2_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj10_pt2 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ) ),
                null,
                obj10,
                theSa,
                obj10_pt2_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt3_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj10_pt3 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ) ),
                null,
                obj10,
                theSa,
                obj10_pt3_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt4_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj10_pt4 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ) ),
                null,
                obj10,
                theSa,
                obj10_pt4_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt5_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj10_pt5 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ) ),
                null,
                obj10,
                theSa,
                obj10_pt5_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BooleanDataType obj10_pt6_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj10_pt6 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt6_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ColorDataType obj10_pt7_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj10_pt7 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt7_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.CurrencyDataType obj10_pt8_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj10_pt8 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt8_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.EnumeratedDataType obj10_pt9_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj10_pt9 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt9_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ExtentDataType obj10_pt10_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj10_pt10 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt10_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.FloatDataType obj10_pt11_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj10_pt11 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt11_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.IntegerDataType obj10_pt12_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj10_pt12 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt12_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.MultiplicityDataType obj10_pt13_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj10_pt13 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt13_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.PointDataType obj10_pt14_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj10_pt14 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt14_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj10_pt15_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj10_pt15 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt15_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj10_pt16_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj10_pt16 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj10,
                theSa,
                obj10_pt16_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimePeriodDataType obj10_pt17_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj10_pt17 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt17_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimeStampDataType obj10_pt18_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj10_pt18 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt18_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt19_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj10_pt19 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeAny" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeAny" ) ),
                null,
                obj10,
                theSa,
                obj10_pt19_type,
                obj10_pt19_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt20_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj10_pt20 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ) ),
                null,
                obj10,
                theSa,
                obj10_pt20_type,
                obj10_pt20_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt21_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj10_pt21 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ) ),
                null,
                obj10,
                theSa,
                obj10_pt21_type,
                obj10_pt21_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt22_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj10_pt22 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ) ),
                null,
                obj10,
                theSa,
                obj10_pt22_type,
                obj10_pt22_type.createBlobValue( "DEFAULT VALUE" , "text/html" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt23_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj10_pt23 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ) ),
                null,
                obj10,
                theSa,
                obj10_pt23_type,
                obj10_pt23_type.createBlobValue( "DEFAULT VALUE" , "image/gif" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj10_pt24_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj10_pt24 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ) ),
                null,
                obj10,
                theSa,
                obj10_pt24_type,
                obj10_pt24_type.createBlobValue( "DEFAULT VALUE" , "image/jpeg" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BooleanDataType obj10_pt25_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj10_pt25 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt25_type,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ColorDataType obj10_pt26_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj10_pt26 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt26_type,
                org.infogrid.model.primitives.ColorValue.create( 123 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.CurrencyDataType obj10_pt27_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj10_pt27 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt27_type,
                org.infogrid.model.primitives.CurrencyValue.create( true, 1, 0, "USD" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.EnumeratedDataType obj10_pt28_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj10_pt28 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt28_type,
                obj10_pt28_type.select( "Value2" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.ExtentDataType obj10_pt29_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj10_pt29 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt29_type,
                org.infogrid.model.primitives.ExtentValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.FloatDataType obj10_pt30_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj10_pt30 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt30_type,
                org.infogrid.model.primitives.FloatValue.create( 1.2 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.IntegerDataType obj10_pt31_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj10_pt31 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt31_type,
                org.infogrid.model.primitives.IntegerValue.create( 1L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.MultiplicityDataType obj10_pt32_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj10_pt32 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt32_type,
                org.infogrid.model.primitives.MultiplicityValue.create( 1, org.infogrid.model.primitives.MultiplicityValue.N ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.PointDataType obj10_pt33_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj10_pt33 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt33_type,
                org.infogrid.model.primitives.PointValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj10_pt34_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj10_pt34 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt34_type,
                org.infogrid.model.primitives.StringValue.create( "DEFAULT" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.StringDataType obj10_pt35_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "12.34.56.78" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj10_pt35 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj10,
                theSa,
                obj10_pt35_type,
                org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimePeriodDataType obj10_pt36_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj10_pt36 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt36_type,
                org.infogrid.model.primitives.TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (float) 6.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.TimeStampDataType obj10_pt37_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj10_pt37 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AllProperties_MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ) ),
                null,
                obj10,
                theSa,
                obj10_pt37_type,
                org.infogrid.model.primitives.TimeStampValue.create( 978307199999L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj11 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobAny" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobAny" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj11_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj11_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobAny_OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeAny" ) ),
                null,
                obj11,
                theSa,
                obj11_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj12 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobPlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobPlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobPlainOrHtml" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj12_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj12_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobPlainOrHtml_OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlainOrHtml" ) ),
                null,
                obj12,
                theSa,
                obj12_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj13 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobPlain" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobPlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobPlain" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj13_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj13_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobPlain_OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypePlain" ) ),
                null,
                obj13,
                theSa,
                obj13_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj14 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobHtml" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj14_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj14_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobHtml_OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataHtml" ) ),
                null,
                obj14,
                theSa,
                obj14_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj15 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobImage" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobImage" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj15_pt0_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj15_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobImage_OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeImage" ) ),
                null,
                obj15,
                theSa,
                obj15_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj16 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobJpg" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobJpg" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj16_pt0_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj16_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBlobJpg_OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBlobDataTypeJpg" ) ),
                null,
                obj16,
                theSa,
                obj16_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj17 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBoolean" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBoolean" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBoolean" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BooleanDataType obj17_pt0_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj17_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalBoolean_OptionalBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalBooleanDataType" ) ),
                null,
                obj17,
                theSa,
                obj17_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj18 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalColor" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalColor" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalColor" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.ColorDataType obj18_pt0_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj18_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalColor_OptionalColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalColorDataType" ) ),
                null,
                obj18,
                theSa,
                obj18_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj19 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalCurrency" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalCurrency" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalCurrency" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.CurrencyDataType obj19_pt0_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj19_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalCurrency_OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalCurrencyDataType" ) ),
                null,
                obj19,
                theSa,
                obj19_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj20 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalEnumerated" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalEnumerated" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalEnumerated" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.EnumeratedDataType obj20_pt0_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj20_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalEnumerated_OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalEnumeratedDataType" ) ),
                null,
                obj20,
                theSa,
                obj20_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj21 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalExtent" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalExtent" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalExtent" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.ExtentDataType obj21_pt0_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj21_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalExtent_OptionalExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalExtentDataType" ) ),
                null,
                obj21,
                theSa,
                obj21_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj22 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalFloat" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalFloat" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalFloat" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.FloatDataType obj22_pt0_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj22_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalFloat_OptionalFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalFloatDataType" ) ),
                null,
                obj22,
                theSa,
                obj22_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj23 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalInteger" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalInteger" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalInteger" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.IntegerDataType obj23_pt0_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj23_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalInteger_OptionalIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalIntegerDataType" ) ),
                null,
                obj23,
                theSa,
                obj23_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj24 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalMultiplicity" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicity" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicity" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.MultiplicityDataType obj24_pt0_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj24_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalMultiplicity_OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalMultiplicityDataType" ) ),
                null,
                obj24,
                theSa,
                obj24_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj25 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalPoint" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalPoint" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalPoint" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.PointDataType obj25_pt0_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj25_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalPoint_OptionalPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalPointDataType" ) ),
                null,
                obj25,
                theSa,
                obj25_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj26 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalString" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalString" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalString" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.StringDataType obj26_pt0_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj26_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalString_OptionalStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringDataType" ) ),
                null,
                obj26,
                theSa,
                obj26_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj27 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalStringRegex" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringRegex" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringRegex" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.StringDataType obj27_pt0_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj27_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalString_OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj27,
                theSa,
                obj27_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj28 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalTimePeriod" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriod" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriod" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.TimePeriodDataType obj28_pt0_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj28_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalTimePeriod_OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimePeriodDataType" ) ),
                null,
                obj28,
                theSa,
                obj28_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj29 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalTimeStamp" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimeStamp" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimeStamp" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.TimeStampDataType obj29_pt0_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj29_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/OptionalTimeStamp_OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "OptionalTimeStampDataType" ) ),
                null,
                obj29,
                theSa,
                obj29_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj30 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobAny" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobAny" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj30_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj30_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobAny_MandatoryBlobDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataType" ) ),
                null,
                obj30,
                theSa,
                obj30_pt0_type,
                obj30_pt0_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj31 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobPlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobPlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobPlainOrHtml" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj31_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj31_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobPlainOrHtml_MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlainOrHtml" ) ),
                null,
                obj31,
                theSa,
                obj31_pt0_type,
                obj31_pt0_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj32 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobPlain" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobPlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobPlain" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj32_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj32_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobPlain_MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypePlain" ) ),
                null,
                obj32,
                theSa,
                obj32_pt0_type,
                obj32_pt0_type.createBlobValue( "DEFAULT VALUE" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj33 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobHtml" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj33_pt0_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj33_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobHtml_MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataHtml" ) ),
                null,
                obj33,
                theSa,
                obj33_pt0_type,
                obj33_pt0_type.createBlobValue( "DEFAULT VALUE" , "text/html" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj34 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobImage" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobImage" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj34_pt0_type = org.infogrid.model.primitives.BlobDataType.theJdkSupportedBitmapType;
        PropertyType obj34_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobImage_MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeImage" ) ),
                null,
                obj34,
                theSa,
                obj34_pt0_type,
                obj34_pt0_type.createBlobValue( "DEFAULT VALUE" , "image/gif" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj35 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobJpg" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobJpg" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj35_pt0_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj35_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBlobJpg_MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBlobDataTypeJpg" ) ),
                null,
                obj35,
                theSa,
                obj35_pt0_type,
                obj35_pt0_type.createBlobValue( "DEFAULT VALUE" , "image/jpeg" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj36 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBoolean" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBoolean" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBoolean" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BooleanDataType obj36_pt0_type = org.infogrid.model.primitives.BooleanDataType.theDefault;
        PropertyType obj36_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryBoolean_MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryBooleanDataType" ) ),
                null,
                obj36,
                theSa,
                obj36_pt0_type,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj37 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryColor" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryColor" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryColor" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.ColorDataType obj37_pt0_type = org.infogrid.model.primitives.ColorDataType.theDefault;
        PropertyType obj37_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryColor_MandatoryColorDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryColorDataType" ) ),
                null,
                obj37,
                theSa,
                obj37_pt0_type,
                org.infogrid.model.primitives.ColorValue.create( 123 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj38 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryCurrency" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryCurrency" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryCurrency" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.CurrencyDataType obj38_pt0_type = org.infogrid.model.primitives.CurrencyDataType.theDefault;
        PropertyType obj38_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryCurrency_MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryCurrencyDataType" ) ),
                null,
                obj38,
                theSa,
                obj38_pt0_type,
                org.infogrid.model.primitives.CurrencyValue.create( true, 0, 1, "EUR" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj39 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryEnumerated" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryEnumerated" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryEnumerated" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.EnumeratedDataType obj39_pt0_type = org.infogrid.model.primitives.EnumeratedDataType.create( new String[] { "Value1", "Value2", "Value3" }, new org.infogrid.model.primitives.L10PropertyValueMap[] { org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "First value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Second value of Z" ) ), org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "Third value of Z" ) ) }, new org.infogrid.model.primitives.L10PropertyValueMap[] { null, null, null }, org.infogrid.model.primitives.EnumeratedDataType.theDefault );
        PropertyType obj39_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryEnumerated_MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryEnumeratedDataType" ) ),
                null,
                obj39,
                theSa,
                obj39_pt0_type,
                obj39_pt0_type.select( "Value2" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj40 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryExtent" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryExtent" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryExtent" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.ExtentDataType obj40_pt0_type = org.infogrid.model.primitives.ExtentDataType.theDefault;
        PropertyType obj40_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryExtent_MandatoryExtentDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryExtentDataType" ) ),
                null,
                obj40,
                theSa,
                obj40_pt0_type,
                org.infogrid.model.primitives.ExtentValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj41 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryFloat" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryFloat" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryFloat" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.FloatDataType obj41_pt0_type = org.infogrid.model.primitives.FloatDataType.theDefault;
        PropertyType obj41_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryFloat_MandatoryFloatDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryFloatDataType" ) ),
                null,
                obj41,
                theSa,
                obj41_pt0_type,
                org.infogrid.model.primitives.FloatValue.create( 1.2 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj42 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryInteger" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryInteger" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryInteger" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.IntegerDataType obj42_pt0_type = org.infogrid.model.primitives.IntegerDataType.theDefault;
        PropertyType obj42_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryInteger_MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryIntegerDataType" ) ),
                null,
                obj42,
                theSa,
                obj42_pt0_type,
                org.infogrid.model.primitives.IntegerValue.create( 1L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj43 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryMultiplicity" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicity" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicity" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.MultiplicityDataType obj43_pt0_type = org.infogrid.model.primitives.MultiplicityDataType.theDefault;
        PropertyType obj43_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryMultiplicity_MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryMultiplicityDataType" ) ),
                null,
                obj43,
                theSa,
                obj43_pt0_type,
                org.infogrid.model.primitives.MultiplicityValue.create( 1, org.infogrid.model.primitives.MultiplicityValue.N ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj44 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryPoint" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryPoint" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryPoint" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.PointDataType obj44_pt0_type = org.infogrid.model.primitives.PointDataType.theDefault;
        PropertyType obj44_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryPoint_MandatoryPointDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryPointDataType" ) ),
                null,
                obj44,
                theSa,
                obj44_pt0_type,
                org.infogrid.model.primitives.PointValue.create( 1.0, 2.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj45 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryString" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryString" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryString" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.StringDataType obj45_pt0_type = org.infogrid.model.primitives.StringDataType.theDefault;
        PropertyType obj45_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryString_MandatoryStringDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringDataType" ) ),
                null,
                obj45,
                theSa,
                obj45_pt0_type,
                org.infogrid.model.primitives.StringValue.create( "DEFAULT" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj46 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryStringRegex" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegex" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegex" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.StringDataType obj46_pt0_type = org.infogrid.model.primitives.StringDataType.create( java.util.regex.Pattern.compile( "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}" ), org.infogrid.model.primitives.StringValue.create( "12.34.56.78" ), org.infogrid.util.L10StringMapImpl.create( "An IP address looks like this: xxx.xxx.xxx.xxx, where xxx is a number between 0 and 255.") );
        PropertyType obj46_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryStringRegex_MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryStringRegexDataType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "IP address xxx.xxx.xxx.xxx" , "text/html" ) ),
                obj46,
                theSa,
                obj46_pt0_type,
                org.infogrid.model.primitives.StringValue.create( "127.0.0.1" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj47 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryTimePeriod" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriod" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriod" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.TimePeriodDataType obj47_pt0_type = org.infogrid.model.primitives.TimePeriodDataType.theDefault;
        PropertyType obj47_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryTimePeriod_MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimePeriodDataType" ) ),
                null,
                obj47,
                theSa,
                obj47_pt0_type,
                org.infogrid.model.primitives.TimePeriodValue.create( (short) 1, (short) 2, (short) 3, (short) 4, (short) 5, (float) 6.0 ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj48 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryTimeStamp" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStamp" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStamp" ) ),
                null,
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.TimeStampDataType obj48_pt0_type = org.infogrid.model.primitives.TimeStampDataType.theDefault;
        PropertyType obj48_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/MandatoryTimeStamp_MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "MandatoryTimeStampDataType" ) ),
                null,
                obj48,
                theSa,
                obj48_pt0_type,
                org.infogrid.model.primitives.TimeStampValue.create( 978307199999L ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj49 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/SimpleBlobTest" ),
                org.infogrid.model.primitives.StringValue.create( "SimpleBlobTest" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "SimpleBlobTest" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "Holds a single BlobValue. For GUI testing." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj49_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj49_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/SimpleBlobTest_AnyOptional" ),
                org.infogrid.model.primitives.StringValue.create( "AnyOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "AnyOptional" ) ),
                null,
                obj49,
                theSa,
                obj49_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        EntityType obj50 = theInstantiator.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest" ),
                org.infogrid.model.primitives.StringValue.create( "BlobTest" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "BlobTest" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "Holds properties of a range of different BlobDataTypes." , "text/html" ) ),
                null,
                theSa,
                new AttributableMeshType[] { },
                new MeshTypeIdentifier [] {
},
                null,
                new String[] {},
                new StringValue[0],
                new StringValue[0],
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE );

        org.infogrid.model.primitives.BlobDataType obj50_pt0_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj50_pt0 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_AnyOptional" ),
                org.infogrid.model.primitives.StringValue.create( "AnyOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "AnyOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt0_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt1_type = org.infogrid.model.primitives.BlobDataType.theAnyType;
        PropertyType obj50_pt1 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_AnyMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "AnyMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "AnyMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt1_type,
                obj50_pt1_type.createBlobValue( "Default value for AnyMandatory" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt2_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj50_pt2 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextPlainOrHtmlOptional" ),
                org.infogrid.model.primitives.StringValue.create( "TextPlainOrHtmlOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextPlainOrHtmlOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt2_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt3_type = org.infogrid.model.primitives.BlobDataType.theTextAnyType;
        PropertyType obj50_pt3 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextPlainOrHtmlMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "TextPlainOrHtmlMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextPlainOrHtmlMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt3_type,
                obj50_pt3_type.createBlobValue( "Default value for TextPlainOrHtmlMandatory" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt4_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj50_pt4 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextPlainOptional" ),
                org.infogrid.model.primitives.StringValue.create( "TextPlainOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextPlainOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt4_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt5_type = org.infogrid.model.primitives.BlobDataType.theTextPlainType;
        PropertyType obj50_pt5 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextPlainMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "TextPlainMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextPlainMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt5_type,
                obj50_pt5_type.createBlobValue( "Default value for TextPlainMandatory" , "text/plain" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt6_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj50_pt6 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextHtmlOptional" ),
                org.infogrid.model.primitives.StringValue.create( "TextHtmlOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextHtmlOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt6_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt7_type = org.infogrid.model.primitives.BlobDataType.theTextHtmlType;
        PropertyType obj50_pt7 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_TextHtmlMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "TextHtmlMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "TextHtmlMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt7_type,
                obj50_pt7_type.createBlobValue( "Default value for TextHtmlMandatory" , "text/html" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt8_type = org.infogrid.model.primitives.BlobDataType.theGifType;
        PropertyType obj50_pt8 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_GifOptional" ),
                org.infogrid.model.primitives.StringValue.create( "GifOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "GifOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt8_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt9_type = org.infogrid.model.primitives.BlobDataType.theGifType;
        PropertyType obj50_pt9 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_GifMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "GifMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "GifMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt9_type,
                obj50_pt9_type.createBlobValue( "FIXME" , "image/gif" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt10_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj50_pt10 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_JpgOptional" ),
                org.infogrid.model.primitives.StringValue.create( "JpgOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "JpgOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt10_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt11_type = org.infogrid.model.primitives.BlobDataType.theJpgType;
        PropertyType obj50_pt11 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_JpgMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "JpgMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "JpgMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt11_type,
                obj50_pt11_type.createBlobValue( "FIXME" , "image/jpeg" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt12_type = org.infogrid.model.primitives.BlobDataType.thePngType;
        PropertyType obj50_pt12 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_PngOptional" ),
                org.infogrid.model.primitives.StringValue.create( "PngOptional" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "PngOptional" ) ),
                null,
                obj50,
                theSa,
                obj50_pt12_type,
                null,
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        org.infogrid.model.primitives.BlobDataType obj50_pt13_type = org.infogrid.model.primitives.BlobDataType.thePngType;
        PropertyType obj50_pt13 = theInstantiator.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/BlobTest_PngMandatory" ),
                org.infogrid.model.primitives.StringValue.create( "PngMandatory" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "PngMandatory" ) ),
                null,
                obj50,
                theSa,
                obj50_pt13_type,
                obj50_pt13_type.createBlobValue( "FIXME" , "image/png" ),
                null,
                new String[] {},
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.FALSE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.BooleanValue.TRUE,
                org.infogrid.model.primitives.FloatValue.create( 0.0 ) );

        // generateLoadOneRelationshipType section

        RelationshipType obj51 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/R" ),
                org.infogrid.model.primitives.StringValue.create( "R" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The R RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, 1 ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/B" ) ),
                new RoleType[] { },  
                new RoleType[] { },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        RelationshipType obj54 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/RR" ),
                org.infogrid.model.primitives.StringValue.create( "RR" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The RR RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, 1 ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AA" ) ),
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/B" ) ),
                new RoleType[] { ((RelationshipType) theModelBase.findMeshTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/R" ) )).getSource() },  
                new RoleType[] { ((RelationshipType) theModelBase.findMeshTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/R" ) )).getDestination() },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        RelationshipType obj57 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/S" ),
                org.infogrid.model.primitives.StringValue.create( "S" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The S RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, 1 ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/B" ) ),
                new RoleType[] { },  
                new RoleType[] { },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        RelationshipType obj60 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AR1A" ),
                org.infogrid.model.primitives.StringValue.create( "AR1A" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The AR1A RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                new RoleType[] { },  
                new RoleType[] { },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        RelationshipType obj63 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/AR2A" ),
                org.infogrid.model.primitives.StringValue.create( "AR2A" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The AR2A RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                new RoleType[] { },  
                new RoleType[] { },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        RelationshipType obj66 = theInstantiator.createRelationshipType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/ARAny" ),
                org.infogrid.model.primitives.StringValue.create( "ARAny" ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.StringValue.create( "The ARAny RelationshipType" ) ),
                org.infogrid.model.primitives.L10PropertyValueMapImpl.create( org.infogrid.model.primitives.BlobDataType.theTextAnyType.createBlobValue( "This RelationshipType is a test RelationshipType." , "text/html" ) ),
                theSa,
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                org.infogrid.model.primitives.MultiplicityValue.create( 0, org.infogrid.model.primitives.MultiplicityValue.N ),  
                theModelBase.findEntityTypeByIdentifier( theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "org.infogrid.model.Test/A" ) ),
                null,
                new RoleType[] { },  
                new RoleType[] { },  
                new String[] {}, 
                new String[] {}, 
                org.infogrid.model.primitives.BooleanValue.FALSE,  
                org.infogrid.model.primitives.BooleanValue.TRUE,  
                org.infogrid.model.primitives.BooleanValue.TRUE );

        // generateFixOneEntityType section

        return new SubjectArea[] { theSa };
    }

    /**
      * The ClassLoader to be used for resources.
      */
    protected ClassLoader theLoader;

}
