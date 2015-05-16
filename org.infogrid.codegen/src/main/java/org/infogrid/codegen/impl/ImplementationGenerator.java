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

package org.infogrid.codegen.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import org.infogrid.codegen.AbstractGenerator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.BreadthFirstSupertypeIterator;
import org.infogrid.util.UniqueIterator;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.SimpleStringRepresentationParameters;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
  * This class is a code generator that generates the implementation
  * of a set of MeshTypes in the model.
  */
public class ImplementationGenerator
        extends
            AbstractGenerator
{
    private static final Log log = Log.getLogInstance( ImplementationGenerator.class ); // our own, private logger

    /**
     * Constructor.
     *
     * @param outputDir the directory into which the code shall be generated
     * @param commentsRepresentation the StringRepresentation to use for emitting comments
     */
    public ImplementationGenerator(
            File                 outputDir,
            StringRepresentation commentsRepresentation )
    {
        super( outputDir, commentsRepresentation );
    }

    /**
      * Generate code for one EntityType.
      *
      * @param theMeshType the EntityType to generate code for
      * @return the fully-qualified file name where it was generated
      * @throws IOException thrown if an I/O error occurred during code generation
      * @throws StringifierException thrown if there was a problem when attempting to stringify
      */
    @Override
    protected String generateCodeForEntityType(
            EntityType theMeshType )
        throws
            IOException,
            StringifierException
    {
        if( !theMeshType.getDoGenerateImplementationCode().value() ) {
            return null;
        }

        SubjectArea theSa = theMeshType.getSubjectArea();
        if( !theSa.getDoGenerateImplementationCode().value() ) {
            return null;
        }

        // ForwardReferences need to be able to instantiate abstract MeshTypes, so do not check for that
        if( theMeshType.getIsAbstract().value() && !theMeshType.getMayBeUsedAsForwardReference().value() ) {
            return null;
        }

        String saName          = encodeSubjectAreaWithVersion( theSa );
        String theMeshTypeName = theMeshType.getName().value();

        log.info( "Generating Implementation for " + saName + " " + theMeshTypeName );

        PrintWriter outStream = getCodePrintWriterFor( theMeshType );

        // boilerplate
        outStream.println( legalNotice );
        outStream.println( "//" );
        outStream.println( "// This file has been generated AUTOMATICALLY. DO NOT MODIFY." );
        outStream.println( "// on " + currentDateTime() );
        outStream.println( "//" );
        outStream.println( "// DO NOT MODIFY -- re-generate!" );
        outStream.println( "" );

        // package
        String packageName = thePackageNameTranslatorWithVersion.translateSubjectArea( theSa );

        outStream.println( "package " + packageName + getImplementationSubPackageName() + ";" );
        outStream.println();

        // import
        generateImports( outStream, theSa );

        // class and inheritance
        generateClassAndInheritance(
                outStream,
                theMeshType,
                theMeshTypeName );

        outStream.println( "{" );

        // constructor
        generateConstructor(
                theMeshType,
                theMeshTypeName,
                saName,
                outStream );

        // PropertyTypes
        PropertyType [] allPropertyTypes = theMeshType.getAllPropertyTypes();

        for( int i=0 ; i<allPropertyTypes.length ; ++i ) {
            generateForOnePropertyType( outStream, allPropertyTypes[i] );
        }

        // we generate the local override code in the Memory implementation

        StringValue code = theMeshType.getInheritingOverrideCode();
        if( code != null ) {
            outStream.println( "// BEGIN INHERITED OVERRIDE CODE from local EntityType" );
            outStream.println();

            outStream.println( code.getAsString() );

            outStream.println();
            outStream.println( "// END INHERITED OVERRIDE CODE from local EntityType" );
            outStream.println();
        }

        Iterator<AttributableMeshType> theIter = new UniqueIterator<AttributableMeshType>(
                new BreadthFirstSupertypeIterator( theMeshType ),
                true );
        while( theIter.hasNext() ) {
            AttributableMeshType current = theIter.next();
            if( current instanceof EntityType ) {
                EntityType realCurrent = (EntityType) current;
                code = realCurrent.getInheritingOverrideCode();
                if( code != null ) {
                    outStream.println( "// BEGIN INHERITED OVERRIDE CODE from: " + current.getIdentifier().toExternalForm() );
                    outStream.println();

                    outStream.println( code.getAsString() );

                    outStream.println();
                    outStream.println( "// END INHERITED OVERRIDE CODE from: " + current.getIdentifier().toExternalForm() );
                    outStream.println();
                }
            }
        }

        // any code that needs to be inserted
        for( StringValue implementedMethod : theMeshType.getImplementedMethods() ) {
            outStream.println( " // #### BEGIN IMPLEMENTED METHOD (INSERTED FROM model.xml FILE) ####" );
            outStream.println( implementedMethod.getAsString() );
            outStream.println( " // #### END IMPLEMENTED METHOD (INSERTED FROM model.xml FILE) ####" );
        }

        // finish up
        outStream.println( "}" );

        outStream.close();

        return getRelativeCodeFileNameFor( theMeshType );
    }

    /**
      * Obtain sub-package name under which the implementation packages
      * will be generated.
      *
      * @return the sub-package name for the implementation packages
      */
    protected String getImplementationSubPackageName()
    {
        return "";
    }

    /**
      * Obtain prefix to apply to implementation class names.
      *
      * @return prefix to apply to implementation class names
      */
    protected String getClassPrefix()
    {
        return "Impl";
    }

    /**
      * This overridable method generates the import statements.
      *
      * @param outStream the streem to write to
      * @param sa the SubjectArea for which the import statements are being generated
      */
    protected void generateImports(
            PrintWriter outStream,
            SubjectArea sa )
    {
        outStream.println( "import org.infogrid.model.primitives.*;" );
        outStream.println( "import org.infogrid.mesh.*;" );
        outStream.println(
                "import "
                + thePackageNameTranslatorWithoutVersion.translateSubjectArea( sa )
                + getInterfaceSubPackageName()
                + ".*;");
        outStream.println();
    }

    /**
      * Generate the class and inheritance declaration.
      *
      * @param outStream the streem to write to
      * @param theMeshType the MeshType for which we generate code
      * @param theMeshTypeName the MeshType's name
      * @throws StringifierException thrown if there was a problem when attempting to stringify
      */
    protected void generateClassAndInheritance(
            PrintWriter outStream,
            EntityType  theMeshType,
            String      theMeshTypeName )
        throws
            StringifierException
    {
        StringRepresentationParameters pars = SimpleStringRepresentationParameters.create();

        // JavaDoc comment
        outStream.println( "/**" );
        outStream.println( "  * <p>Java implementation class for EntityType " + theMeshType.getIdentifier().toExternalForm() + ".</p>" );
        outStream.println( "  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s" );
        outStream.println( "  * <code>MeshObjectLifecycleManager</code>.</p>" );
        outStream.println( "  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any" );
        outStream.println( "  * class that implements this interface, only on the characteristics provided by this interface" );
        outStream.println( "  * and its supertypes.</p>" );
        outStream.println( "  *" );
        outStream.println( "  * <table>" );
        outStream.println(
                  "  *  <tr><td>Identifier:</td><td><tt>"
                + theMeshType.getIdentifier().toStringRepresentation( theCommentsRepresentation, pars )
                + "</tt></td></tr>" );
        outStream.println(
                  "  *  <tr><td>Name:</td><td><tt>"
                + PropertyValue.toStringRepresentationOrNull( theMeshType.getName(), theCommentsRepresentation, pars )
                + "</tt></td></tr>" );
        outStream.println(
                  "  *  <tr><td>IsAbstract:</td><td>"
                + PropertyValue.toStringRepresentationOrNull( theMeshType.getIsAbstract(), theCommentsRepresentation, pars )
                + "</td></tr>" );
        generateL10Map(
                theMeshType.getUserVisibleNameMap(),
                "  *  <tr><td>UserVisibleName:</td><td>",
                "</td></tr>",
                outStream );
        generateL10Map(
                theMeshType.getUserVisibleDescriptionMap(),
                "  *  <tr><td>UserVisibleDescription:</td><td>",
                "</td></tr>",
                outStream );
        outStream.println( "  * </table>" );
        outStream.println( "  */" );
        outStream.println();
        
        outStream.println( "public class " + getClassPrefix() + theMeshTypeName );
        outStream.println( "        extends" );
        outStream.println( "            org.infogrid.mesh.TypedMeshObjectFacadeImpl" );
        outStream.println( "        implements" );
        outStream.println( "            "
                + thePackageNameTranslatorWithoutVersion.translateSubjectArea( theMeshType.getSubjectArea())
                + getInterfaceSubPackageName()
                + "."
                + theMeshTypeName );
        outStream.println();
    }

    /**
      * Generate the constructor.
      *
      * @param theMeshType the MeshType for which we generate code
      * @param theMeshTypeName the MeshType's name
      * @param encodedSubjectArea the encoded SubjectArea
      * @param outStream the streem to write to
      */
    protected void generateConstructor(
            EntityType  theMeshType,
            String      theMeshTypeName,
            String      encodedSubjectArea,
            PrintWriter outStream )
    {
        outStream.println( "    private static final org.infogrid.util.logging.Log log = org.infogrid.util.logging.Log.getLogInstance( " + getClassPrefix() + theMeshTypeName + ".class ); // our own, private logger" );
        outStream.println();
        outStream.println( "    /**" );
        outStream.println( "      * Constructor." );
        outStream.println( "      *");
        outStream.println( "      * @param delegate the underlying MeshObject" );
        outStream.println( "      */" );
        outStream.println( "    public " + getClassPrefix() + theMeshTypeName + "(" );
        outStream.println( "            MeshObject delegate )" );
        outStream.println( "    {" );
        outStream.println( "        super( delegate );" );
        outStream.println( "    }" );
        outStream.println();
        outStream.println( "    /**" );
        outStream.println( "      * Initializes the MeshObject to its default values." );
        outStream.println( "      *");
        outStream.println( "      * @param init the TypeInitializer" );
        outStream.println( "      * @param skipSettingValuesFor collection of PropertyTypes that already have values and should not be initialized" );
        outStream.println( "      * @param timeUpdated the timeUpdated to use" );
        outStream.println( "      * @throws org.infogrid.mesh.IllegalPropertyTypeException should not be thrown -- codegenerator faulty" );
        outStream.println( "      * @throws org.infogrid.mesh.IllegalPropertyValueException should not be thrown -- codegenerator faulty" );
        outStream.println( "      * @throws org.infogrid.mesh.NotPermittedException should not be thrown -- codegenerator faulty" );
        outStream.println( "      * @throws org.infogrid.meshbase.transaction.TransactionException should not be thrown -- codegenerator faulty" );
        outStream.println( "      * @throws org.infogrid.model.primitives.UnknownEnumeratedValueException should not be thrown -- codegenerator faulty" );
        outStream.println( "      */" );
        outStream.println( "    public static void initializeDefaultValues(" );
        outStream.println( "            TypeInitializer                    init," );
        outStream.println( "            java.util.Collection<PropertyType> skipSettingValuesFor," );
        outStream.println( "            long                               timeUpdated )" );
        outStream.println( "        throws" );
        outStream.println( "            org.infogrid.mesh.IllegalPropertyTypeException," );
        outStream.println( "            org.infogrid.mesh.IllegalPropertyValueException," );
        outStream.println( "            org.infogrid.mesh.NotPermittedException," );
        outStream.println( "            org.infogrid.meshbase.transaction.TransactionException," );
        outStream.println( "            org.infogrid.model.primitives.UnknownEnumeratedValueException" );
        outStream.println( "    {" );
        
        PropertyType [] pts = theMeshType.getAllPropertyTypes();
        
        StringBuilder propTypesString  = new StringBuilder();
        StringBuilder propValuesString = new StringBuilder();
        
        for( int i=0 ; i<pts.length ; ++i ) {
            if( !pts[i].getDoGenerateImplementationCode().value() ) {
                continue;
            }

            String        propertyTypeName = pts[i].getName().value();
            PropertyValue defaultValue     = pts[i].getDefaultValue();
            StringValue   defaultValueCode = pts[i].getDefaultValueCode();

            if( defaultValue != null ) {
                propTypesString.append(  "                    " ).append( propertyTypeName.toUpperCase() );
                propTypesString.append( ",\n" );
                
                propValuesString.append( "                    " ).append( defaultValue.getJavaConstructorString(
                        getClassPrefix() + theMeshTypeName + ".class.getClassLoader()",
                        propertyTypeName.toUpperCase() + "_type" ));
                propValuesString.append( ",\n" );
            }
            // this should (?) be "else if" but because they are mutually exclusive, we can
            // do this, which helps with debugging in case they are not indeed mutually exclusive ;-)
            if( defaultValueCode != null ) {
                propTypesString.append( "                    " ).append(  propertyTypeName.toUpperCase());
                propTypesString.append( ",\n" );
                
                propValuesString.append( "                    " ).append( defaultValueCode.getAsString());
                propValuesString.append( ",\n" );
            }
        }
        if( propTypesString.length() > 0 ) {
            outStream.println( "        init.setPropertyValues(" );
            outStream.println( "                new PropertyType[] {" );
            outStream.print( propTypesString.toString() );
            outStream.println( "                }," );
            outStream.println( "                new PropertyValue[] {" );
            outStream.print( propValuesString.toString() );
            outStream.println( "                }," );
            outStream.println( "                skipSettingValuesFor," );
            outStream.println( "                timeUpdated );" );
        }
        outStream.println( "    }" );
        outStream.println();
    }

    /**
      * Generate code for one PropertyType.
      *
      * @param outStream the stream to write to
      * @param thePropertyType the PropertyType for which we generate code
      */
    protected void generateForOnePropertyType(
            PrintWriter   outStream,
            PropertyType  thePropertyType )
    {
        if( !thePropertyType.getDoGenerateImplementationCode().value() ) {
            return;
        }

        String propertyTypeName = thePropertyType.getName().value();
        String propertyTypeType = thePropertyType.getDataType().getCorrespondingJavaClass().getName();

        if( !thePropertyType.getIsReadOnly().value() ) {

            outStream.println( "    /**" );
            outStream.println( "      * Set value for property " + propertyTypeName + "." );
            outStream.println( "      *" );
            outStream.println( "      * @param newValue the new value for the property" );
            outStream.println( "      * @throws NotPermittedException thrown if this caller did not have permission to perform this operation");
            outStream.println( "      * @throws org.infogrid.meshbase.transaction.TransactionException thrown if invoked outside proper transaction boundaries");
            if(    ( !thePropertyType.getIsOptional().value())
                || ( thePropertyType.getDataType().getPerformDomainCheck() ))
            {
                outStream.println( "      * @throws IllegalPropertyValueException thrown if the PropertyValue was invalid for this Property");
            }
            outStream.println( "      */" );
            outStream.println( "    public void set" + propertyTypeName + "(" );
            outStream.println( "            " + propertyTypeType + " newValue )" );
            outStream.println( "        throws" );
            outStream.println( "            org.infogrid.mesh.NotPermittedException," );
            outStream.print(   "            org.infogrid.meshbase.transaction.TransactionException" );
            if(    ( !thePropertyType.getIsOptional().value())
                || ( thePropertyType.getDataType().getPerformDomainCheck() ))
            {
                outStream.println( "," );
                outStream.print( "            org.infogrid.mesh.IllegalPropertyValueException" );
            }
            outStream.println();
            outStream.println( "    {" );

            outStream.println( "        if( log.isTraceEnabled() ) {" );
            outStream.println( "            log.traceMethodCallEntry( this, \"set" + propertyTypeName + "\", newValue );" );
            outStream.println( "        }" );
            outStream.println();

            outStream.println( "        try {" );
            outStream.println( "            the_Delegate.setPropertyValue( " + propertyTypeName.toUpperCase() + ", newValue );" );
            outStream.println();
            outStream.println( "        } catch( IllegalPropertyValueException ex ) {" );
            outStream.println( "            log.error( ex );" );
            outStream.println( "        } catch( IllegalPropertyTypeException ex ) {" );
            outStream.println( "            log.error( ex );" );
            outStream.println( "        }" );
            outStream.println( "    }" );
            outStream.println();
        }

        outStream.println( "    /**" );
        outStream.println( "      * Obtain value for property " + propertyTypeName + "." );
        outStream.println( "      *" );
        outStream.println( "      * @return the current value of the property" );
        outStream.println( "      * @throws NotPermittedException thrown if this caller did not have permission to perform this operation");
        outStream.println( "      */" );
        outStream.println( "    public " + propertyTypeType + " get" + propertyTypeName + "()" );
        outStream.println( "        throws" );
        outStream.println( "            org.infogrid.mesh.NotPermittedException" );
        outStream.println( "    {" );
        outStream.println( "        try {" );
        outStream.println( "            return (" + propertyTypeType + ") the_Delegate.getPropertyValue( " + propertyTypeName.toUpperCase() + " );" );
        outStream.println( "        } catch( IllegalPropertyTypeException ex ) {" );
        outStream.println( "            log.error( ex );" );        
        outStream.println( "            return null;" );
        outStream.println( "        }" );
        outStream.println( "    }" );
        outStream.println();

        // inner class for projected properties
        if( thePropertyType instanceof ProjectedPropertyType ) {
            ProjectedPropertyType realPropertyType = (ProjectedPropertyType) thePropertyType;
            // FIXME reinsert support for ProjectedPropertyTypes.

//            outStream.println( "    /**" );
//            outStream.println( "      * Recalculation code for ProjectedPropertyType " + realPropertyType.getIdentifier().toExternalForm() );
//            outStream.println( "      */" );
//            outStream.println( "    protected " + propertyTypeType + " recalculate_" + propertyTypeName + "(" );
//            outStream.println( "            com.r_objects.mm.Foundation.util.InternalTraversalActiveRootEntitySet.InternalMarker toWatch," );
//            outStream.println( "            java.util.EventObject                                                                triggeringEvent," );
//            outStream.println( "            com.r_objects.mm.util.TraversalToPropertyTypeInstanceSpecification []               allTraversalSpecificationsForThisProjectedPropertyType )" );
//            outStream.println( "    {" );
//            if( realPropertyType.getProjectionCode() != null ) {
//                outStream.println( realPropertyType.getProjectionCode().getAsString() );
//            }
//            outStream.println( "    }" );
//            outStream.println();
//
//            outStream.println( "    /**" );
//            outStream.println( "     * Update code for ProjectedPropertyType " + realPropertyType.getIdentifier().toExternalForm() );
//            outStream.println( "     * This is public so we can invoke it through reflection" );
//            outStream.println( "     */" );
//            outStream.println( "    public void update_" + propertyTypeName + "(" );
//            outStream.println( "            com.r_objects.mm.Foundation.util.InternalTraversalActiveRootEntitySet.InternalMarker triggeringActiveSet," );
//            outStream.println( "            java.util.EventObject                                                                triggeringEvent," );
//            outStream.println( "            com.r_objects.mm.util.TraversalToPropertyTypeInstanceSpecification []               allTraversalSpecsForThis )" );
//            outStream.println( "    {" );
//            outStream.println( "        try {" );
//            outStream.println( "            " + propertyTypeType + " new" + propertyTypeName + " = recalculate_" + propertyTypeName + "( triggeringActiveSet, triggeringEvent, allTraversalSpecsForThis );" );
//            outStream.println( "            set" + propertyTypeName + "( false, new" + propertyTypeName + " ); // probable FIXME" );
//            outStream.println( "        } catch( org.infogrid.mesh.NotPermittedException ex ) {" );
//            outStream.println( "            log.error( ex );" );
//            if(    ( !thePropertyType.getIsOptional().value())
//                || ( thePropertyType.getDataType().getPerformDomainCheck() ))
//            {
//                outStream.println( "        } catch( IllegalValueException ex ) {" );
//                outStream.println( "            log.error( ex );" );
//            }
//            outStream.println( "        } catch( com.r_objects.modelmanagement.transaction.TransactionException ex ) {" );
//            outStream.println( "            log.error( ex );" );
//            outStream.println( "        } catch( Exception ex ) {" );
//            outStream.println( "            log.error( \"exception in recalculation code\", ex );" );
//            outStream.println( "        }" );
//            outStream.println( "    }" );
            outStream.println();
        }
    }

    /**
      * Obtain a relative file name into which the code will be written.
      *
      * @param theMeshType the MeshType for which we generate code
      * @return the relative path of the file to which the code will be written
      * @throws IOException thrown if an I/O error occurred
      */
    @Override
    protected String getRelativeCodeFileNameFor(
            MeshType theMeshType )
        throws
            IOException
    {
        SubjectArea          theSubjectArea;
        AttributableMeshType theAttributableMeshType;

        if( theMeshType instanceof AttributableMeshType ) {
            theAttributableMeshType = (AttributableMeshType) theMeshType;
            theSubjectArea          = theAttributableMeshType.getSubjectArea();

        } else if( theMeshType instanceof SubjectArea ) {
            theAttributableMeshType = null;
            theSubjectArea          = (SubjectArea) theMeshType;

        } else {
            return null;
        }

        String rawSubjectArea = thePackageNameTranslatorWithVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        if( theAttributableMeshType != null ) {
            String amoName = theAttributableMeshType.getName().value();

            return dirName + File.separatorChar + IMPL_PREFIX + amoName + ".java";

        } else {
            return dirName + File.separatorChar + "package.html";
        }
    }

    /**
      * Obtain a relative file name into which the JavaDoc will be written.
      *
      * @param theSubjectArea the MeshType for whose generated code we want to obtain the file name
      * @return the relative file name into which the code will be written
      * @throws IOException thrown if an I/O error occurred during code generation
      */
    @Override
    protected String getRelativeJavaDocFileNameFor(
            SubjectArea theSubjectArea )
        throws
            IOException
    {
        String rawSubjectArea = thePackageNameTranslatorWithVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        return dirName + File.separatorChar + "package.html";
    }

    /**
     * Generate the JavaDoc documentation for one SubjectArea.
     *
     * @param theSubjectArea the SubjectArea to generate documentation for
     * @return the fully-qualified file name where it was generated
     * @throws IOException thrown if an I/O error occurred during code generation
     */
    @Override
    protected String generateJavaDocForSubjectArea(
            SubjectArea theSubjectArea )
        throws IOException
    {
        PrintWriter w = this.getJavaDocPrintWriterFor( theSubjectArea );
        
        w.println( "<html>" );
        w.println( " <head>" );
        w.println( "  <title>Documentation for " + theSubjectArea.getIdentifier().toExternalForm() + "</title>" );
        w.println( " </head>" );
        w.println( " <body>" );
        w.println( "  <p>This package provides implementation classes for the " + theSubjectArea.getUserVisibleName().value() + ".</p> ");        
        w.println( " </body>" );
        w.println( "</html>" );
        
        w.close();
        
        return getRelativeJavaDocFileNameFor( theSubjectArea );
    }
    
    /**
     * The file prefix for implementation classes.
     */
    public static final String IMPL_PREFIX = "Impl";
}
