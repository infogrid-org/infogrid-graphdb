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

package org.infogrid.codegen.modelloader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.infogrid.codegen.AbstractGenerator;
import org.infogrid.mesh.set.ByTypeMeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.L10PropertyValueMap;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.AllNeighborsTraversalSpecification;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalToPropertySpecification;
import org.infogrid.module.ModuleRequirement;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;

/**
 * This class knows how to generate Java source code classs that implement
 * ModelLoader for one or more Subject Areas.
 */
public class ModelLoaderGenerator
    extends
        AbstractGenerator
{
    private static final Log log = Log.getLogInstance( ModelLoaderGenerator.class ); // our own, private logger

    /**
     * Constructor.
     *
     * @param outputDir the directory into which the code shall be generated
     * @param commentsRepresentation the StringRepresentation to use for emitting comments
     */
    public ModelLoaderGenerator(
            File                 outputDir,
            StringRepresentation commentsRepresentation )
    {
        super( outputDir, commentsRepresentation );
    }

    /**
      * Generate code for one EntityType.
      *
      * @param theEntityType the EntityType to generate code for
      * @return the fully-qualified file name where it was generated
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected String generateCodeForEntityType(
            EntityType theEntityType )
        throws
            IOException
    {
        return null; // do nothing
    }

    /**
     * Generate for one SubjectArea.
     *
     * @param theSa the SubjectArea to generate
     * @return the fully-qualified file name where it was generated
     * @throws IOException an input/output error occurred
     */
    @Override
    protected String generateCodeForSubjectArea(
            SubjectArea theSa )
        throws
            IOException
    {
        PrintWriter outStream = getCodePrintWriterFor( theSa );

        // boilerplate
        outStream.println( legalNotice );
        outStream.println( "//" );
        outStream.println( "// This file has been generated AUTOMATICALLY. DO NOT MODIFY." );
        outStream.println( "// on " + currentDateTime() );
        outStream.println( "//" );
        outStream.println();

        // package
        String packageName = thePackageNameTranslatorWithVersion.translateSubjectArea( theSa );

        outStream.println( "package " + packageName + ";" );
        outStream.println();

        // import
        generateImports(
                outStream,
                theSa );

        // class and inheritance
        generateClassAndInheritance(
                outStream,
                theSa );

        outStream.println( "{" );

        // constructor
        generateConstructor(
                outStream,
                theSa );

        // load method
        generateLoadAll(
                outStream,
                theSa );

        // member variables
        generateMembers(
                outStream,
                theSa );

        // finish up
        outStream.println( "}" );

        outStream.close();

        return getRelativeCodeFileNameFor( theSa );
    }

    /**
     * Generate the import statements.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateImports(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "import org.infogrid.modelbase.*;" );
        outStream.println( "import org.infogrid.model.primitives.*;" );
        outStream.println( "import org.infogrid.model.traversal.TraversalSpecification;" );
        outStream.println( "import org.infogrid.model.traversal.TraversalToPropertySpecification;" );
        outStream.println( "import java.io.IOException;" );
        outStream.println();
    }

    /**
     * Generate the class and inheritance statements.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateClassAndInheritance(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "/**" );
        outStream.println( "  * This class loads the model of the " + theSa.getUserVisibleName().value() + " Subject Area." );
        outStream.println( "  */" );
        outStream.println( "public class SubjectAreaLoader" );
        outStream.println( "        extends" );
        outStream.println( "            ModelLoader" );
    }

    /**
     * Generate the constructor.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateConstructor(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "    /**" );
        outStream.println( "      * Constructor." );
        outStream.println( "      *" );
        outStream.println( "      * @param modelBase the ModelBase into which the SubjectArea will be loaded" );
        outStream.println( "      * @param loader the ClassLoader to be used for resolving resources" );
        outStream.println( "      */" );
        outStream.println( "    public SubjectAreaLoader(" );
        outStream.println( "            ModelBase   modelBase," );
        outStream.println( "            ClassLoader loader )" );
        outStream.println( "    {" );
        outStream.println( "        super( modelBase );" );
        outStream.println( "        " + LOADER_VAR + " = loader;" );
        outStream.println( "    }" );
        outStream.println();
    }

    /**
     * Generate the load method.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadAll(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "    /**" );
        outStream.println( "     * Instruct this object to instantiate its model." );
        outStream.println( "     *" );
        outStream.println( "     * @param theInstantiator the MeshTypeLifecycleManager which shall be used to instantiate" );
        outStream.println( "     * @param now time the run was started" );
        outStream.println( "     * @return the instantiated SubjectArea(s)" );
        outStream.println( "     * @throws MeshTypeNotFoundException thrown if there was an undeclared dependency in the model that could not be resolved" );
        outStream.println( "     * @throws InheritanceConflictException thrown if there was a conflict in the inheritance hierarchy of the newly loaded model" );
        outStream.println( "     * @throws IOException thrown if reading the model failed" );
        outStream.println( "     */" );
        outStream.println( "    protected SubjectArea [] loadModel(" );
        outStream.println( "            MeshTypeLifecycleManager theInstantiator," );
        outStream.println( "            TimeStampValue           now )" );
        outStream.println( "        throws" );
        outStream.println( "            MeshTypeNotFoundException," );
        outStream.println( "            InheritanceConflictException," );
        outStream.println( "            IOException," );
        outStream.println( "            org.infogrid.model.primitives.UnknownEnumeratedValueException" );
        outStream.println( "    {" );

        generateLoggingStatement( outStream );
        generateLoadSubjectArea(  outStream, theSa );

        CollectableMeshType [] cmos = theSa.getCollectableMeshTypes();

        outStream.println( "        // generateLoadOneEntity section" );
        outStream.println();

        for( int i=0 ; i<cmos.length ; ++i ) {
            if( cmos[i] instanceof EntityType ) {
                generateLoadOneEntityType( outStream, "obj" + i, (EntityType) cmos[i] );
            }
        }

        outStream.println( "        // generateLoadOneRelationshipType section" );
        outStream.println();

        for( int i=0 ; i<cmos.length ; ++i ) {
            if( cmos[i] instanceof RelationshipType ) {
                generateLoadOneRelationshipType( outStream, "obj" + i, (RelationshipType) cmos[i] );
            }
        }

        outStream.println( "        // generateFixOneEntityType section" );
        outStream.println();

        for( int i=0 ; i<cmos.length ; ++i ) {
            if( cmos[i] instanceof EntityType ) {
                generateFixOneEntityType( outStream, "obj" + i, (EntityType) cmos[i] );
            }
        }
        outStream.println( "        return new SubjectArea[] { theSa };" );
        outStream.println( "    }" );
        outStream.println();
    }

    /**
     * Generate a logging statement so we can trace what is going on.
     *
     * @param outStream the stream to write to
     * @throws IOException an input/output error occurred
     */
    protected void generateLoggingStatement(
            PrintWriter outStream )
        throws
            IOException
    {
        outStream.println( "        org.infogrid.util.logging.Log log = org.infogrid.util.logging.Log.getLogInstance( getClass() );" );
        outStream.println( "        if( log.isTraceEnabled() ) {" );
        outStream.println( "            log.traceMethodCallEntry( this, \"loadModel\" );" );
        outStream.println( "        }" );
        outStream.println();
    }

    /**
     * Generate the code for loading the SubejctArea itself.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadSubjectArea(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "        SubjectArea theSa = theInstantiator.createSubjectArea(" );
        outStream.println( "                " + getIdentifierString( theSa.getIdentifier()                 ) + ",   // IdentifierValue           theIdentifier," );
        outStream.println( "                " + getValueString( theSa.getName()                            ) + ",   // StringValue               theName," );
        outStream.println( "                " + getValueString( theSa.getVersionNumber()                   ) + ",   // StringValue               theVersion," );
        outStream.println( "                " + getValueString( theSa.getUserVisibleNameMap()              ) + ",   // L10Map                    theUserNames," );
        outStream.println( "                " + getValueString( theSa.getUserVisibleDescriptionMap()       ) + ",   // L10Map                    theUserDescriptions," );
        outStream.println( "                " + getTypeString( theSa.getSubjectAreaDependencies()          ) + ",   // SubjectArea []            theSubjectAreaDependencies," );
        outStream.println( "                " + LOADER_VAR                                                   + ",   // ClassLoader               theClassLoader," );
        outStream.println( "                " + getValueString( theSa.getDoGenerateInterfaceCode()         ) + ",   // BooleanValue              doGenerateInterfaceCode," );
        outStream.println( "                " + getValueString( theSa.getDoGenerateImplementationCode()    ) + " ); // BooleanValue              doGenerateImplementationCode )" );
        outStream.println();
    }

    /**
     * Generate the code for loading one EntityType, while not setting the InputTraversalToProperties yet.
     *
     * @param outStream the stream to write to
     * @param varName the name of the variable to which we assign this to
     * @param theEntity the EntityType that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadOneEntityType(
            PrintWriter outStream,
            String      varName,
            EntityType  theEntity )
        throws
            IOException
    {
        outStream.println( "        EntityType " + varName + " = theInstantiator.createEntityType(" );
        outStream.println( "                " + getIdentifierString( theEntity.getIdentifier()                ) + ","   ); // IdentifierValue         theIdentifier,
        outStream.println( "                " + getValueString( theEntity.getName()                           ) + ","   ); // StringValue             theName,
        outStream.println( "                " + getValueString( theEntity.getUserVisibleNameMap()             ) + ","   ); // L10Map                  theUserNames,
        outStream.println( "                " + getValueString( theEntity.getUserVisibleDescriptionMap()      ) + ","   ); // L10Map                  theUserDescriptions,
        outStream.println( "                " + getValueString( theEntity.getIcon(), BlobDataType.theJdkSupportedBitmapType.getJavaConstructorString( LOADER_VAR )) + ","   ); // BlobValue               theIcon,
        outStream.println( "                theSa," ); // SubjectArea               theSubjectArea,
        outStream.println( "                " + getTypeString(  theEntity.getDirectSupertypes()               ) + ","   ); // AttributableMeshType [] supertypes,
        outStream.println( "                " + getIdentifierStrings( theEntity.getSynonyms()                 ) + ","   ); // AttributableMeshType [] supertypes,
        outStream.println( "                " + getValueString( theEntity.getInheritingOverrideCode()         ) + ","   ); // StringValue             inheritingOverrideCode,
        outStream.println( "                " + classNameArray( theEntity.getLocalEntityTypeGuardClassNames() ) + ","   ); // String []               localEntityTypeGuardClassNames
        outStream.println( "                " + getValueString( theEntity.getDeclaredMethods()                ) + ","   ); // StringValue []          declaredMethods
        outStream.println( "                " + getValueString( theEntity.getImplementedMethods()             ) + ","   ); // StringValue []          implementedMethods
        outStream.println( "                " + classNameArray( theEntity.getAdditionalInterfaces()           ) + ","   ); // String []               additionalInterfaces,
        outStream.println( "                " + getValueString( theEntity.getIsAbstract()                     ) + ","   ); // BooleanValue            isAbstract,
        outStream.println( "                " + getValueString( theEntity.getMayBeUsedAsForwardReference()    ) + ","   ); // BooleanValue            mayBeUsedAsForwardReference,
        outStream.println( "                " + getValueString( theEntity.getIsSignificant()                  ) + ","   ); // BooleanValue            isSignificant,
        outStream.println( "                " + getValueString( theEntity.getDoGenerateInterfaceCode()        ) + ","   ); // BooleanValue            doGenerateInterfaceCode,
        outStream.println( "                " + getValueString( theEntity.getDoGenerateImplementationCode()   ) + " );" ); // BooleanValue            doGenerateImplementationCode );
        outStream.println();

        PropertyType [] mas = theEntity.getLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            generateLoadOnePropertyType( outStream, varName, varName + "_pt" + i, mas[i] );
        }
        PropertyType [] omas = theEntity.getOverridingLocalPropertyTypes();
        for( int i=0 ; i<omas.length ; ++i ) {
            generateLoadOnePropertyType( outStream, varName, varName + "_opt" + i, omas[i] );
        }
        PropertyTypeGroup [] mags = theEntity.getLocalPropertyTypeGroups();
        for( int i=0 ; i<mags.length ; ++i ) {
            generateLoadOnePropertyTypeGroup( outStream, varName, varName + "_ptg" + i, mags[i] );
        }
    }

    /**
     * Generate the code for loading one RelationshipType.
     *
     * @param outStream the stream to write to
     * @param varName the name of the variable to which we assign this to
     * @param theRelationship the RelationshipType that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadOneRelationshipType(
            PrintWriter      outStream,
            String           varName,
            RelationshipType theRelationship )
        throws
            IOException
    {
        outStream.println( "        RelationshipType " + varName + " = theInstantiator.createRelationshipType(" );
        outStream.println( "                " + getIdentifierString( theRelationship.getIdentifier()                       ) + "," ); // IdentifierValue           theIdentifier,
        outStream.println( "                " + getValueString( theRelationship.getName()                                  ) + "," ); // StringValue               theName,
        outStream.println( "                " + getValueString( theRelationship.getUserVisibleNameMap()                    ) + "," ); // L10Map                    theUserNames,
        outStream.println( "                " + getValueString( theRelationship.getUserVisibleDescriptionMap()             ) + "," ); // L10Map                    theUserDescriptions,
        outStream.println( "                theSa," ); // SubjectArea               theSubjectArea,
        outStream.println( "                " + getValueString( theRelationship.getSource().getMultiplicity()              ) + ",  " ); // MultiplicityValue         sourceMultiplicity,
        outStream.println( "                " + getValueString( theRelationship.getDestination().getMultiplicity()         ) + ",  " ); // MultiplicityValue         destinationMultiplicity,
        if( theRelationship.getSource().getEntityType() != null ) {
            outStream.println( "                theModelBase.findEntityTypeByIdentifier( " + getIdentifierString( theRelationship.getSource().getEntityType().getIdentifier() ) + " )," ); // EntityType                source,
        } else {
            outStream.println( "                null," );
        }
        if( theRelationship.getDestination().getEntityType() != null ) {
            outStream.println( "                theModelBase.findEntityTypeByIdentifier( " + getIdentifierString( theRelationship.getDestination().getEntityType().getIdentifier() ) + " )," ); // EntityType                destination,
        } else {
            outStream.println( "                null," );
        }
        if( theRelationship.getSource().getDirectSuperRoleTypes() != null ) {
            outStream.println( "                " + getTypeString(  theRelationship.getSource().getDirectSuperRoleTypes()      ) + ",  " ); // RoleType []               sourceSuperRoleTypes,
        } else {
            outStream.println( "                null," );
        }
        if( theRelationship.getDestination().getDirectSuperRoleTypes() != null ) {
        outStream.println( "                " + getTypeString(  theRelationship.getDestination().getDirectSuperRoleTypes() ) + ",  " ); // RoleType []               destinationSuperRoleTypes,
        } else {
            outStream.println( "                null," );
        }
        outStream.println( "                " + classNameArray( theRelationship.getSource().getLocalRoleTypeGuardClassNames() )     + ", " ); // String [] sourceRoleConstraintClassNames
        outStream.println( "                " + classNameArray( theRelationship.getDestination().getLocalRoleTypeGuardClassNames()) + ", " ); // String [] destinationRoleConstraintClassNames
        outStream.println( "                " + getValueString( theRelationship.getIsAbstract()                            )    + ",  " ); // BooleanValue              isAbstract,
        outStream.println( "                " + getValueString( theRelationship.getDoGenerateInterfaceCode()               )    + ",  " ); // BooleanValue              doGenerateInterfaceCode,
        outStream.println( "                " + getValueString( theRelationship.getDoGenerateImplementationCode()          )    + " );" ); // BooleanValue              doGenerateImplementationCode );
        outStream.println();

        PropertyType [] mas = theRelationship.getLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            generateLoadOnePropertyType( outStream, varName, varName + "_pt" + i, mas[i] );
        }

        PropertyType [] omas = theRelationship.getOverridingLocalPropertyTypes();
        for( int i=0 ; i<omas.length ; ++i ) {
            generateLoadOnePropertyType( outStream, varName, varName + "_opt" + i, omas[i] );
        }
        PropertyTypeGroup [] mags = theRelationship.getLocalPropertyTypeGroups();
        for( int i=0 ; i<mags.length ; ++i ) {
            generateLoadOnePropertyTypeGroup( outStream, varName, varName + "_ptg" + i, mags[i] );
        }
    }

    /**
     * Generate the code for loading one PropertyType.
     *
     * @param outStream the stream to write to
     * @param parentVarName the variable name of the parent AttributableMeshType
     * @param varName the name of the variable to which we assign this to
     * @param thePropertyType the PropertyType that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadOnePropertyType(
            PrintWriter  outStream,
            String       parentVarName,
            String       varName,
            PropertyType thePropertyType )
        throws
            IOException
    {
        // first deal with the DataType because they may need a separate declaration

        String typeVarName = varName + "_type";
        // outStream.println( "        DataType " + typeVarName + " = " + thePropertyType.getDataType().getJavaConstructorString( LOADER_VAR ) + ";" );
        outStream.println( "        " + thePropertyType.getDataType().getClass().getName() + " " + typeVarName + " = " + thePropertyType.getDataType().getJavaConstructorString( LOADER_VAR ) + ";" );

        boolean isProjected  = thePropertyType instanceof ProjectedPropertyType;
        boolean isOverriding = thePropertyType.getOverride() != null && thePropertyType.getOverride().length > 0;

        if( isProjected ) {
            if( isOverriding ) {
                outStream.println( "        ProjectedPropertyTypePatcher " + varName + " = theInstantiator.createOverridingProjectedPropertyType(" );
                outStream.println( "                " + getTypeString( thePropertyType.getOverride() ) + ",");
            } else {
                outStream.println( "        ProjectedPropertyTypePatcher " + varName + " = theInstantiator.createProjectedPropertyType(" );
            }
        } else {
            if( isOverriding ) {
                outStream.println( "        PropertyType " + varName + " = theInstantiator.createOverridingPropertyType(" );
                outStream.println( "                " + getTypeString( thePropertyType.getOverride() ) + "," );
            } else {
                outStream.println( "        PropertyType " + varName + " = theInstantiator.createPropertyType(" );
            }
        }

        outStream.println( "                " + getIdentifierString( thePropertyType.getIdentifier()              ) + "," ); // IdentifierValue           theIdentifier,
        if( !isOverriding ) {
            outStream.println( "                " + getValueString( thePropertyType.getName()                      ) + "," ); // StringValue               theName,
            outStream.println( "                " + getValueString( thePropertyType.getUserVisibleNameMap()        ) + "," ); // L10Map                    theUserNames,
        }
        outStream.println( "                " + getValueString( thePropertyType.getUserVisibleDescriptionMap() ) + "," ); // L10Map                    theUserDescriptions,
        outStream.println( "                " + parentVarName + "," );                         // AttributableMeshType    theParent,
        outStream.println( "                theSa," ); // SubjectArea               theSubjectArea,
        outStream.println( "                " + typeVarName + "," ); // DataType                  theDataType,
        outStream.println( "                " + getValueString( thePropertyType.getDefaultValue(), typeVarName ) + "," ); // PropertyValue        theDefaultValue,
        outStream.println( "                " + getValueString( thePropertyType.getDefaultValueCode()          ) + "," ); // BlobValue                 theDefaultValueCode,
        outStream.println( "                " + classNameArray( thePropertyType.getLocalPropertyTypeGuardClassNames() ) + "," ); // Guards
        if( thePropertyType instanceof ProjectedPropertyType ) {
            outStream.println( "                null," ); // TraversalToPropertyTypeInstanceSpecification [] theInputPropertyTypeInstances
            outStream.println( "                " + getValueString( ((ProjectedPropertyType)thePropertyType).getProjectionCode()) + "," ); // BlobValue                                        theProjectionCode,
        } else {
            outStream.println( "                " + getValueString( thePropertyType.getIsOptional() ) + "," ); // BooleanValue              isOptional,
            outStream.println( "                " + getValueString( thePropertyType.getIsReadOnly() ) + "," ); // BooleanValue              isReadOnly,
        }
        outStream.println( "                " + getValueString( thePropertyType.getDoGenerateInterfaceCode()      ) + "," ); // BooleanValue              doGenerateInterfaceCode,
        outStream.print(   "                " + getValueString( thePropertyType.getDoGenerateImplementationCode() )); // BooleanValue              doGenerateImplementationCode,
        if( !isOverriding ) {
            outStream.println( "," );
            outStream.print( "                " + getValueString( thePropertyType.getSequenceNumber()               )); // FloatValue                theSequenceNumber );
        }
        outStream.println( " );" );
        outStream.println();
    }

    /**
     * Generate the code for loading one PropertyTypeGroup.
     *
     * @param outStream the stream to write to
     * @param parentVarName the variable name of the parent AttributableMeshType
     * @param varName the name of the variable to which we assign this to
     * @param theGroup the PropertyTypeGroup that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateLoadOnePropertyTypeGroup(
            PrintWriter        outStream,
            String             parentVarName,
            String             varName,
            PropertyTypeGroup theGroup )
        throws
            IOException
    {
        outStream.println( "        PropertyTypeGroup " + varName + " = theInstantiator.createPropertyTypeGroup(" );
        outStream.println( "                " + getIdentifierString( theGroup.getIdentifier()              ) + ",  " ); // IdentifierValue           theIdentifier,
        outStream.println( "                " + getValueString( theGroup.getName()                         ) + ",  " ); // StringValue               theName,
        outStream.println( "                " + getValueString( theGroup.getUserVisibleNameMap()           ) + ",  " ); // L10Map                    theUserNames,
        outStream.println( "                " + getValueString( theGroup.getUserVisibleDescriptionMap()    ) + ",  " ); // L10Map                    theUserDescriptions,
        outStream.println( "                " + getTypeString(  theGroup.getAttributableMeshType()         ) + ",  " ); // AttributableMeshType    theParent,
        outStream.println( "                theSa," ); // SubjectArea               theSubjectArea,
        outStream.println( "                " + getTypeString(  theGroup.getContainedPropertyTypes()       ) + ",  " ); // PropertyType []          theMembers,
        outStream.println( "                " + getValueString( theGroup.getDoGenerateInterfaceCode()      ) + ",  " ); // BooleanValue              doGenerateInterfaceCode,
        outStream.println( "                " + getValueString( theGroup.getDoGenerateImplementationCode() ) + ",  " ); // BooleanValue              doGenerateImplementationCode,
        outStream.println( "                " + getValueString( theGroup.getSequenceNumber()               ) + " );" ); // FloatValue                theSequenceNumber );
        outStream.println();
    }

    /**
     * Generate the code for setting the InputTraversalToPropertySpecifications on an already-loaded EntityType.
     *
     * @param outStream the stream to write to
     * @param varName the name of the variable to which we assign this to
     * @param theEntity the EntityType that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateFixOneEntityType(
            PrintWriter outStream,
            String      varName,
            EntityType  theEntity )
        throws
            IOException
    {
        PropertyType [] mas = theEntity.getLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            if( mas[i] instanceof ProjectedPropertyType ) {
                generateFixOneProjectedPropertyType( outStream, varName, varName + "_pt" + i, (ProjectedPropertyType) mas[i] );
            }
        }
        mas = theEntity.getOverridingLocalPropertyTypes();
        for( int i=0 ; i<mas.length ; ++i ) {
            if( mas[i] instanceof ProjectedPropertyType ) {
                generateFixOneProjectedPropertyType( outStream, varName, varName + "_opt" + i, (ProjectedPropertyType) mas[i] );
            }
        }
    }

    /**
     * Generate the code for setting up othe MAIs on an already-loaded ProjectedPropertyType.
     *
     * @param outStream the stream to write to
     * @param parentVarName the variable name of the parent AttributableMeshType
     * @param varName the name of the variable to which we assign this to
     * @param theProjectedPropertyType the ProjectedPropertyType that we fix
     * @throws IOException an input/output error occurred
     */
    protected void generateFixOneProjectedPropertyType(
            PrintWriter           outStream,
            String                parentVarName,
            String                varName,
            ProjectedPropertyType theProjectedPropertyType )
        throws
            IOException
    {
        TraversalToPropertySpecification [] mais = theProjectedPropertyType.getInputProperties();

        outStream.println( "        " + varName + ".setInputProperties(" );
        if( mais != null ) {
            outStream.println( "                new TraversalToPropertySpecification[] {" );
            for( int i=0 ; i<mais.length ; ++i ) {
                outStream.println( "                        TraversalToPropertySpecification.create(" );
                if( mais[i].getTraversalSpecification() != null ) {
                    outStream.println( "                                " + getTypeString( mais[i].getTraversalSpecification()) + "," );
                }
                outStream.println( "                                new PropertyType[] {" );
                PropertyType [] mas = mais[i].getPropertyTypes();
                for( int j=0 ; j<mas.length ; ++j ) {
                    outStream.print( "                                        (PropertyType) theModelBase.findMeshTypeByIdentifier(" );
                    outStream.print( getIdentifierString( mas[j].getIdentifier() ));
                    outStream.print( " )" );
                    if( j < mas.length-1 ) {
                        outStream.println( "," );
                    }
                }
                outStream.println( "                                }" );
                outStream.print( "                        )" );
                if( i < mais.length-1 ) {
                    outStream.println( "," );
                }
            }
            outStream.println( "                } );" );
        } else {
            outStream.println( "                null );" );
        }

        outStream.println();
    }


    /**
     * Determine a Java language expressiong String containing an array of AttributableMeshTypes by
     * looking them up.
     *
     * @param amos the AttributableMeshTypes
     * @return Java String
     */
    protected String getTypeString(
            AttributableMeshType [] amos )
    {
        StringBuilder ret = new StringBuilder( "new AttributableMeshType[] { " );
        for( int i=0 ; i<amos.length ; ++i ) {
            ret.append( "theModelBase.findAttributableMeshTypeByIdentifier( " + getIdentifierString( amos[i].getIdentifier() ) + " )" );
            if( i < amos.length-1 ) {
                ret.append( "," );
            }
            ret.append( " " );
        }
        ret.append( "}" );
        return ret.toString();
    }

    /**
     * Determine a Java language expression String containing AttributableMeshTypes by
     * looking it up.
     *
     * @param amo the AttributableMeshType
     * @return Java String
     */
    protected String getTypeString(
            AttributableMeshType amo )
    {
        StringBuilder ret = new StringBuilder( "(AttributableMeshType) theModelBase.findMeshTypeByIdentifier( " );
        ret.append( getIdentifierString( amo.getIdentifier() ));
        ret.append( " )" );
        return ret.toString();
    }

    /**
     * Determine a Java language expression String containing an array of RoleTypes by
     * looking them up.
     *
     * @param roles the RoleTypes
     * @return Java String
     */
    protected String getTypeString(
            RoleType [] roles )
    {
        StringBuilder ret = new StringBuilder( "new RoleType[] { " );
        for( int i=0 ; i<roles.length ; ++i ) {
            ret.append( getTypeString( roles[i] ));

            if( i < roles.length-1 ) {
                ret.append( "," );
            }
            ret.append( " " );
        }
        ret.append( "}" );
        return ret.toString();
    }

    /**
     * Determine a Java language expression String containing a RoleType by
     * looking it up.
     *
     * @param role the RoleType
     * @return Java String
     */
    protected String getTypeString(
            RoleType role )
    {
        RelationshipType rel = role.getRelationshipType();

        StringBuilder ret = new StringBuilder( "((RelationshipType) theModelBase.findMeshTypeByIdentifier( " );
        ret.append( getIdentifierString( rel.getIdentifier() ));
        ret.append( " )).get" );
        if( role.isTopSingleton() || role.isSource() ) { // isSource() may throw Exception, but isTopSingleton() will prevent that
            ret.append( "Source()" );
        } else {
            ret.append( "Destination()" );
        }
        return ret.toString();
    }

    /**
     * Determine a Java language expression String containing an array of PropertyTypes by
     * looking them up.
     *
     * @param mas the PropertyTypes
     * @return Java String
     */
    protected String getTypeString(
            PropertyType [] mas )
    {
        StringBuilder ret = new StringBuilder( "new PropertyType[] { " );
        for( int i=0 ; i<mas.length ; ++i ) {
            ret.append( "theModelBase.findPropertyTypeByIdentifier( " + getIdentifierString( mas[i].getIdentifier() ) + " )" );
            if( i < mas.length-1 ) {
                ret.append( "," );
            }
            ret.append( " " );
        }
        ret.append( "}" );
        return ret.toString();
    }

    /**
     * Determine a Java language expression String containing an array of SubjectAreas by
     * looking them up.
     *
     * @param sas the SubjectAreas
     * @return Java String
     */
    protected String getTypeString(
            SubjectArea [] sas )
    {
        StringBuilder ret = new StringBuilder( "new SubjectArea[] { " );
        for( int i=0 ; i<sas.length ; ++i ) {
            ret.append( "theModelBase.findSubjectAreaByIdentifier( " + getIdentifierString( sas[i].getIdentifier() ) + " )" );
            if( i < sas.length-1 ) {
                ret.append( "," );
            }
            ret.append( " " );
        }
        ret.append( "}" );
        return ret.toString();
    }

    /**
     * Format one PropertyValue appropriately and return.
     *
     * @param val the PropertyValue, or null
     * @return String representation
     */
    protected String getValueString(
            PropertyValue val )
    {
        if( val == null ) {
            return "null";
        } else {
            return val.getJavaConstructorString( LOADER_VAR, null );
        }
    }

    /**
     * Format one PropertyValue appropriately and return.
     *
     * @param val the PropertyValue, or null
     * @param typeVar  name of the variable containing the DataType that goes with the to-be-created instance.
     * @return String representation
     */
    protected String getValueString(
            PropertyValue val,
            String        typeVar )
    {
        if( val == null ) {
            return "null";
        } else {
            return val.getJavaConstructorString( LOADER_VAR, typeVar );
        }
    }

    /**
     * Format one L10Map appropriately and return.
     *
     * @param val the L10Map, or null
     * @return String representation
     */
    protected String getValueString(
            L10PropertyValueMap val )
    {
        if( val == null ) {
            return "null";
        } else {
            return val.getJavaConstructorString( LOADER_VAR, BlobDataType.theTextAnyType.getJavaConstructorString( LOADER_VAR ) );
        }
    }

    /**
     * Format an array of StringValues appropriately and return.
     *
     * @param values the StringValues
     * @return String representation
     */
    protected String getValueString(
            StringValue [] values )
    {
        if( values == null ) {
            return "null";
        } else if( values.length == 0 ) {
            return "new StringValue[0]";
        } else {
            StringBuilder buf = new StringBuilder();
            String sep = "new StringValue[] { ";
            for( StringValue val : values ) {
                buf.append( sep );
                buf.append( val.getJavaConstructorString( null, null ));
                sep = ", ";
            }
            buf.append( " }" );
            return buf.toString();
        }
    }

    /**
     * Format an Identifier appropriately and return.
     *
     * @param value the Identifier
     * @return String representation
     */
    protected String getIdentifierString(
            MeshTypeIdentifier value )
    {
        if( value == null ) {
            return "null";
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append( "theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( \"" );
            buf.append( value.toExternalForm() );
            buf.append( "\" )" );
            return buf.toString();
        }
    }

    /**
     * Format an array of Identifier appropriately and return.
     *
     * @param value the Identifiers
     * @return String representation
     */
    protected String getIdentifierStrings(
            MeshTypeIdentifier [] value )
    {
        if( value == null ) {
            return "null";
        } else {
            StringBuilder buf = new StringBuilder();
            buf.append( "new MeshTypeIdentifier [] {\n" );

            String sep = "";
            for( MeshTypeIdentifier current : value ) {
                buf.append( sep );
                buf.append( "theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( \"" );
                buf.append( current.toExternalForm() );
                buf.append( "\" )" );
                sep = ",";
            }
            buf.append( "}" );
            return buf.toString();
        }
    }

    /**
     * Format a TraversalSpecification and return.
     *
     * @param value the TraversalSpecification
     * @return String representation
     */
    protected String getTypeString(
            TraversalSpecification value )
    {
        if( value instanceof AlternativeCompoundTraversalSpecification ) {

            AlternativeCompoundTraversalSpecification realValue = (AlternativeCompoundTraversalSpecification) value;

            StringBuilder ret = new StringBuilder( "org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification.create( new TraversalSpecification[] { " );

            TraversalSpecification [] children = realValue.getAlternatives();
            for( int i=0 ; i<children.length ; ++i ) {
                ret.append( getTypeString( children[i] ));

                if( i<children.length-1 ) {
                    ret.append( ", " );
                }
            }
            ret.append( " } )" );
            return ret.toString();

        } else if( value instanceof SelectiveTraversalSpecification ) {

            SelectiveTraversalSpecification realValue = (SelectiveTraversalSpecification) value;

            StringBuilder ret = new StringBuilder( "org.infogrid.model.traversal.SelectiveTraversalSpecification.create( " );

            if( realValue.getStartSelector() != null ) {
                ret.append( getTypeString( realValue.getStartSelector() ));
                ret.append( ", " );
            }

            ret.append( getTypeString( realValue.getQualifiedTraversalSpecification() ));
            ret.append( ", " );

            ret.append( getTypeString( realValue.getEndSelector() ));

            ret.append( " )" );
            return ret.toString();

        } else if( value instanceof SequentialCompoundTraversalSpecification ) {

            SequentialCompoundTraversalSpecification realValue = (SequentialCompoundTraversalSpecification) value;

            StringBuilder ret = new StringBuilder( "org.infogrid.model.traversal.SequentialCompoundTraversalSpecification.create( new TraversalSpecification[] { " );

            TraversalSpecification [] children = realValue.getSteps();
            for( int i=0 ; i<children.length ; ++i ) {
                ret.append( getTypeString( children[i] ));

                if( i<children.length-1 ) {
                    ret.append( ", " );
                }
            }
            ret.append( " } )" );
            return ret.toString();

        } else if( value instanceof StayRightHereTraversalSpecification ) {
            return "org.infogrid.model.traversal.StayRightHereTraversalSpecification.create()";

        } else if( value instanceof AllNeighborsTraversalSpecification ) {
            return "org.infogrid.model.traversal.AllNeighborsTraversalSpecification.create()";

        } else if( value instanceof RoleType ) {

            RoleType realValue = (RoleType) value;
            return getTypeString( realValue );

        } else {
            throw new IllegalArgumentException( "Cannot get type string for: " + String.valueOf( value ) );
        }
    }

    /**
     * Format a MeshObjectSelector and return.
     *
     * @param value the MeshObjectSelector
     * @return String representation
     */
    protected String getTypeString(
            MeshObjectSelector value )
    {
        if( value instanceof ByTypeMeshObjectSelector ) {

            ByTypeMeshObjectSelector realValue = (ByTypeMeshObjectSelector) value;

            StringBuilder buf = new StringBuilder( "new org.infogrid.mesh.set.ByTypeMeshObjectSelector( (AttributableMeshType) theModelBase.findMeshTypeByIdentifier( " );
            buf.append( getIdentifierString( realValue.getFilterType().getIdentifier() ));
            buf.append( " ), " );
            buf.append( String.valueOf( realValue.isSubtypesAllowed() ));
            buf.append( " )" );
            return buf.toString();

        } else {
            throw new IllegalArgumentException( "Cannot get type string for: " + String.valueOf( value ) );
        }
    }

    /**
     * Format a ModuleRequirement array and return/
     *
     * @param reqs array of ModuleRequirements
     * @return String representation
     */
    protected String getModuleRequirementsString(
            ModuleRequirement [] reqs )
    {
        StringBuilder ret = new StringBuilder();
        String       sep = "";

        ret.append( "new ModuleRequirement[] { " );
        for( ModuleRequirement current : reqs ) {
            ret.append( sep );
            ret.append( "ModuleRequirement.create1( \"" );
            ret.append( current.getRequiredModuleName() );
            ret.append( "\"" );
            if( current.getRequiredModuleVersion() != null ) {
                ret.append( ", \"" );
                ret.append( current.getRequiredModuleVersion() );
                ret.append( "\"" );
            }
            ret.append( " )" );
            sep = ", ";
        }
        ret.append( " }" );
        return ret.toString();
    }    
    
    /**
     * Convert an array of class names into Java source code representing a String array
     * containing the class names.
     *
     * @param classNames the class names
     * @return the Java code
     */
    protected static String classNameArray(
            String [] classNames )
    {
        if( classNames == null || classNames.length == 0 ) {
            return "new String[] {}";
        }
        StringBuilder buf = new StringBuilder();
        String sep = "new String[] { ";
        
        for( String current : classNames ) {
            buf.append( sep );
            buf.append( '"' );
            buf.append( current );
            buf.append( '"' );
            sep = ", ";
        }

        buf.append( " }" );
        return buf.toString();
    }

    /**
     * Generate the member variables.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateMembers(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "    /**" );
        outStream.println( "      * The ClassLoader to be used for resources." );
        outStream.println( "      */" );
        outStream.println( "    protected ClassLoader " + LOADER_VAR + ";" );
        outStream.println();
    }

    /**
     * Generate the JavaDoc documentation for one SubjectArea.
     *
     * @param theSubjectArea the SubjectArea to generate documentation for
     * @return the fully-qualified file name where it was generated
     * @throws IOException thrown if an I/O error occurred during code generation
     */
    protected String generateJavaDocForSubjectArea(
            SubjectArea theSubjectArea )
        throws
            IOException
    {
        return null; // do nothing
    }

    /**
      * Obtain a relative file name into which the code will be written.
      *
      * @param theMeshType theMeshType for which we generate code
      * @return the relative path of the file to which the code will be written
      * @throws IOException thrown if an I/O error occurred
      */
    protected String getRelativeCodeFileNameFor(
            MeshType theMeshType )
        throws
            IOException
    {
        if( !( theMeshType instanceof SubjectArea )) {
            throw new IllegalArgumentException( "not a SubjectArea: " + theMeshType );
        }

        SubjectArea theSubjectArea = (SubjectArea) theMeshType;

        String rawSubjectArea = thePackageNameTranslatorWithVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        return dirName + File.separatorChar + "SubjectAreaLoader.java";
    }

    /**
      * Obtain a relative file name into which the JavaDoc will be written.
      *
      * @param theSubjectArea the MeshType for whose generated code we want to obtain the file name
      * @return the relative file name into which the code will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    protected String getRelativeJavaDocFileNameFor(
            SubjectArea theSubjectArea )
        throws
            IOException
    {
        return null; // do nothing
    }

    /**
     * Name of the generated member variable holding the ClassLoader.
     */
    private static final String LOADER_VAR = "theLoader";
}
