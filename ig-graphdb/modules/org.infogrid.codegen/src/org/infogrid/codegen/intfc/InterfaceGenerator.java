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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.codegen.intfc;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.infogrid.codegen.AbstractGenerator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringifierException;

/**
  * This class is a code generator that generates the interface code
  * for the Model from a set of MeshTypes.
  */
public class InterfaceGenerator
        extends
            AbstractGenerator
{
    private static final Log log = Log.getLogInstance(InterfaceGenerator.class); // our own, private logger

    /**
     * Constructor.
     *
     * @param outputDir the directory into which the code shall be generated
     * @param commentsRepresentation the StringRepresentation to use for emitting comments
     */
    public InterfaceGenerator(
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
    protected String generateCodeForEntityType(
            EntityType theMeshType )
        throws
            IOException,
            StringifierException
    {
        if( !theMeshType.getDoGenerateInterfaceCode().value() ) {
            return null;
        }
        SubjectArea theSa = theMeshType.getSubjectArea();
        if( !theSa.getDoGenerateInterfaceCode().value() ) {
            return null;
        }
        String saName          = encodeSubjectAreaWithVersion( theSa );
        String theMeshTypeName = theMeshType.getName().value();

        log.info( "Generating Interface for " + saName + " " + theMeshType );

        PrintWriter outStream = getCodePrintWriterFor( theMeshType );

        outStream.println( legalNotice );
        outStream.println( "//" );
        outStream.println( "// This file has been generated AUTOMATICALLY. DO NOT MODIFY." );
        outStream.println( "// on " + currentDateTime() );
        outStream.println( "//" );
        outStream.println( "// DO NOT MODIFY -- re-generate!" );
        outStream.println();

        String packageName = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSa );

        outStream.println( "package " + packageName + getInterfaceSubPackageName() + ";" );
        outStream.println();

        // import
        outStream.println( "import org.infogrid.model.primitives.*;" );
        outStream.println( "import org.infogrid.mesh.*;" );
        outStream.println( "import org.infogrid.modelbase.ModelBaseSingleton;" );
        outStream.println();

        // JavaDoc comment
        outStream.println( "/**" );
        outStream.println( "  * <p>Java interface for EntityType " + theMeshType.getIdentifier().toExternalForm() + ".</p>" );
        outStream.println( "  * <p>To instantiate, use the factory methods provided by the <code>MeshBase</code>'s" );
        outStream.println( "  * <code>MeshObjectLifecycleManager</code>.</p>" );
        outStream.println( "  * <p><b>Note:</b> As an application programmer, you must never rely on any characteristic of any" );
        outStream.println( "  * class that implements this interface, only on the characteristics provided by this interface" );
        outStream.println( "  * and its supertypes.</p>" );
        outStream.println( "  *" );
        outStream.println( "  * <table>" );
        outStream.println(
                  "  *  <tr><td>Identifier:</td><td><tt>"
                + theMeshType.getIdentifier().toExternalForm()
                + "</tt></td></tr>" );
        outStream.println(
                  "  *  <tr><td>Name:</td><td><tt>"
                + PropertyValue.toStringRepresentationOrNull( theMeshType.getName(), theCommentsRepresentation, null )
                + "</tt></td></tr>" );
        outStream.println(
                  "  *  <tr><td>IsAbstract:</td><td>"
                + PropertyValue.toStringRepresentationOrNull( theMeshType.getIsAbstract(), theCommentsRepresentation, null )
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

        outStream.println( "public interface " + theMeshTypeName );

        AttributableMeshType [] supertypes = theMeshType.getDirectSupertypes();

        String sep = "        extends\n            ";
        if( supertypes.length >= 1 ) {
            String supertypeName
                    = thePackageNameTranslatorWithoutVersion.translateSubjectArea( supertypes[0].getSubjectArea() )
                    + getInterfaceSubPackageName()
                    + "."
                    + supertypes[0].getName().value();

            outStream.print( sep );
            outStream.print( supertypeName );
            sep = ",\n            ";
        } else {
            outStream.print( sep );
            outStream.print( "org.infogrid.mesh.TypedMeshObjectFacade" );
            sep = ",\n            ";
        }
        for( int i=1 ; i<supertypes.length ; ++i ) {
            String supertypeName
                    = thePackageNameTranslatorWithoutVersion.translateSubjectArea( supertypes[i].getSubjectArea() )
                    + getInterfaceSubPackageName()
                    + "."
                    + supertypes[i].getName().value();

            outStream.print( sep );
            outStream.print( supertypeName );
            sep = ",\n            ";
        }

        String [] additionalInterfaces = theMeshType.getAdditionalInterfaces();
        if( additionalInterfaces != null ) {
            for( int i=0 ; i<additionalInterfaces.length ; ++i ) {
                outStream.print( sep );
                outStream.print( additionalInterfaces[i] );
                sep = ",\n            ";
            }
        }

        outStream.println();
        outStream.println( "{" );

        outStream.println( "    /**" );
        outStream.println( "      * Subject area in which this MeshObject is declared." );
        outStream.println( "      */" );
        outStream.println( "    public static final SubjectArea _SUBJECTAREA = ModelBaseSingleton.findSubjectArea( \"" + theSa.getIdentifier().toExternalForm() + "\" );" );
        outStream.println();

        outStream.println( "    /**" );
        outStream.println( "      * This MeshType." );
        outStream.println( "      */" );
        outStream.println( "    public static final EntityType _TYPE = ModelBaseSingleton.findEntityType( \"" + theMeshType.getIdentifier().toExternalForm() + "\" );" );
        outStream.println();

        RoleType [] localRoleTypes = theMeshType.getLocalRoleTypes();
        for( int i=0 ; i<localRoleTypes.length ; ++i ) {
            RoleType theLocalRoleType = localRoleTypes[i];
            
            String srcDest = theLocalRoleType.isSource() ? "_SOURCE" : "_DESTINATION";
            
            outStream.println( "    /**" );
            outStream.println( "      * This MeshType." );
            outStream.println( "      */" );
            outStream.println( "    public static final RoleType _" + theLocalRoleType.getRelationshipType().getName().value() + srcDest
                    + " = ModelBaseSingleton.findRoleType( \"" + theLocalRoleType.getIdentifier().toExternalForm() + "\" );" );
            outStream.println();
        }
        
        PropertyType [] propertyTypes = theMeshType.getLocalPropertyTypes();
        for( int i=0 ; i<propertyTypes.length ; ++i ) {
            PropertyType thePropertyType = propertyTypes[i];
            String propertyTypeName = thePropertyType.getName().value();
            String propertyTypeType = thePropertyType.getDataType().getCorrespondingJavaClass().getName();

            // only generate setter for properties that are read-write
            if( !thePropertyType.getIsReadOnly().value() ) {
                boolean throwsIllegalValueException
                        =    ( !thePropertyType.getIsOptional().value())
                          || (  thePropertyType.getDataType().getPerformDomainCheck() );

                // JavaDoc comment
                outStream.println( "    /**" );
                outStream.println( "      * <p>Set value for property " + propertyTypeName + ".</p>" );
                outStream.println( "      *" );
                generatePropertyTypeJavaDoc( thePropertyType, outStream );
                outStream.println( "      *" );
                outStream.println("       * @param newValue the new value for this property" );
                outStream.println("       * @throws NotPermittedException thrown if this caller was not allowed to perform this operation" );
                outStream.println("       * @throws TransactionException thrown if this operation is invoked from outside of a Transaction" );
                if( throwsIllegalValueException ) {
                    outStream.println("       * @throws IllegalPropertyValueException thrown if it is attempted to assign the <code>null</code> value to this non-optional property" );
                }
                outStream.println( "      */" );
                outStream.println( "    public abstract void set" + propertyTypeName + "(" );
                outStream.println( "            " + propertyTypeType + " newValue )" );
                outStream.println( "        throws" );
                outStream.println( "            org.infogrid.mesh.NotPermittedException," );
                outStream.print(   "            org.infogrid.meshbase.transaction.TransactionException" );
                if( throwsIllegalValueException ) {
                    outStream.print( ",\n            org.infogrid.mesh.IllegalPropertyValueException" );
                }
                outStream.println( ";" );
                outStream.println();
            }

            // JavaDoc comment
            outStream.println( "    /**" );
            outStream.println( "      * <p>Obtain value for property " + propertyTypeName + ".</p>" );
            outStream.println( "      *" );
            generatePropertyTypeJavaDoc( thePropertyType, outStream );
            outStream.println( "      *" );
            outStream.println( "      * @return the current value of the property" );
            outStream.println( "      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation" );
            outStream.println( "      */" );

            outStream.println( "    public abstract " + propertyTypeType + " get" + propertyTypeName + "()" );
            outStream.println( "        throws" );
            outStream.println( "            org.infogrid.mesh.NotPermittedException;" );
            outStream.println();

            outStream.println( "    /**" );
            outStream.println( "      * Name of the " + propertyTypeName + " property." );
            outStream.println( "      */" );
            outStream.println( "    public static final String " + propertyTypeName.toUpperCase() + "_name = \"" + propertyTypeName + "\";" );
            outStream.println();

            outStream.println( "    /**" );
            outStream.println( "      * The " + propertyTypeName + " PropertyType." );
            outStream.println( "      */" );
            outStream.println( "    public static final PropertyType " + propertyTypeName.toUpperCase() + " = ModelBaseSingleton.findPropertyType( \"" + thePropertyType.getIdentifier().toExternalForm() + "\" );" );
            outStream.println( "    public static final " + thePropertyType.getDataType().getClass().getName() + " " + propertyTypeName.toUpperCase() + "_type = (" + thePropertyType.getDataType().getClass().getName() + ") " + propertyTypeName.toUpperCase() + ".getDataType();" );
            outStream.println();

            if( thePropertyType.getDataType() instanceof EnumeratedDataType ) {
                outStream.println( "    /* Values of the EnumeratedDataType. */" );
                EnumeratedDataType realDataType = (EnumeratedDataType) thePropertyType.getDataType();

                for( EnumeratedValue current : realDataType.getDomain() ) {
                    outStream.println( "    public static final EnumeratedValue " + propertyTypeName.toUpperCase() + "_type_" + current.value().toUpperCase() + " = " + propertyTypeName.toUpperCase() + "_type.select( \"" + current.value() + "\" );" );
                }
            }
        }

        PropertyType [] overridingPropertyTypes = theMeshType.getOverridingLocalPropertyTypes();
        for( int i=0 ; i<overridingPropertyTypes.length ; ++i ) {
            PropertyType thePropertyType = overridingPropertyTypes[i];

            if( ! thePropertyType.getIsReadOnly().value() ) // FIXME this may be generating even if inheritance is enough
            {   // this local override turned a read-only PropertyType into a read-write PropertyType

                String propertyTypeName = thePropertyType.getName().value();
                String propertyTypeType = thePropertyType.getDataType().getCorrespondingJavaClass().getName();

                boolean throwsIllegalValueException
                        =    ( !thePropertyType.getIsOptional().value())
                          || (  thePropertyType.getDataType().getPerformDomainCheck() );

                // JavaDoc comment
                outStream.println( "    /**" );
                outStream.println( "      * <p>Set value for PropertyType " + propertyTypeName + ".</p>" );
                outStream.println( "      *" );
                generatePropertyTypeJavaDoc( thePropertyType, outStream );
                outStream.println( "      *" );
                outStream.println("       * @param newValue the new value for this property" );
                outStream.println( "      * @throws NotPermittedException thrown if this caller was not permitted to perform this operation" );
                outStream.println("       * @throws TransactionException thrown if this operation is invoked from outside of a Transaction" );
                if( throwsIllegalValueException ) {
                    outStream.println("       * @throws IllegalPropertyValueException thrown if it is attempted to assign the <code>null</code> value to this non-optional PropertyType" );
                }
                outStream.println( "      */" );
                outStream.println( "    public abstract void set" + propertyTypeName + "(" );
                outStream.println( "            " + propertyTypeName + " newValue )" );
                outStream.println( "        throws" );
                outStream.println( "            org.infogrid.mesh.NotPermittedException," );
                outStream.print(   "            org.infogrid.meshbase.transaction.TransactionException" );
                if( throwsIllegalValueException ) {
                    outStream.print( ",\n            org.infogrid.mesh.IllegalPropertyValueException" );
                }
                outStream.println( ";" );
                outStream.println();
            }
        }

        // any code that needs to be inserted
        for( StringValue declaredMethod : theMeshType.getDeclaredMethods() ) {
            outStream.println( " // #### BEGIN DECLARED METHOD (INSERTED FROM model.xml FILE) ####" );
            outStream.println( declaredMethod.getAsString() );
            outStream.println( " // #### END DECLARED METHOD (INSERTED FROM model.xml FILE) ####" );
        }
        
        outStream.println( "}" );

        outStream.close();

        return getRelativeCodeFileNameFor( theMeshType );
    }

    /**
     * Generate code for one SubjectArea.
     *
     * @param theSa the SubjectArea for which to generate code
     * @return the fully-qualified file name where it was generated
     * @throws IOException an input/output error occurred
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    @Override
    protected String generateCodeForSubjectArea(
            SubjectArea theSa )
        throws
            IOException,
            StringifierException
    {
        if( !theSa.getDoGenerateInterfaceCode().value() ) {
            return null;
        }

        log.info( "Generating Interface for SubjectArea " + theSa.getName() );

        PrintWriter outStream = getCodePrintWriterFor( theSa );

        outStream.println( legalNotice );
        outStream.println( "//" );
        outStream.println( "// This file has been generated AUTOMATICALLY. DO NOT MODIFY." );
        outStream.println( "// on " + currentDateTime() );
        outStream.println( "//" );
        outStream.println( "// DO NOT MODIFY -- re-generate!" );
        outStream.println( "" );

        String packageName = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSa );
        String saShortName = packageName.substring( packageName.lastIndexOf( '.' )+1 );

        saShortName = Character.toUpperCase( saShortName.charAt( 0 )) + saShortName.substring( 1 );
        outStream.println( "package " + packageName + getInterfaceSubPackageName() + ";" );
        outStream.println();

        outStream.println( "import org.infogrid.model.primitives.*;" );
        outStream.println( "import org.infogrid.mesh.*;" );
        outStream.println( "import org.infogrid.modelbase.ModelBaseSingleton;" );
        outStream.println();

        // JavaDoc comment
        outStream.println( "/**" );
        outStream.println( "  * <p>Collects the MeshTypes in this Subject Area for simpler access.</p>" );
        outStream.println( "  */" );

        outStream.println( "public abstract class " + saShortName + "SubjectArea" );
        outStream.println( "{" );

        outStream.println( "    /**" );
        outStream.println( "      * The SubjectArea itself." );
        outStream.println( "      */" );
        outStream.println( "    public static final SubjectArea _SA = ModelBaseSingleton.findSubjectArea( \"" + packageName + "\" );" );
        outStream.println();

        for( CollectableMeshType type : theSa.getCollectableMeshTypes() ) {
            if( !type.getDoGenerateInterfaceCode().value() ) {
                continue;
            }
            if( !( type instanceof AttributableMeshType )) {
                continue;
            }
            
            if( type.getUserVisibleDescription() != null ) {
                outStream.println( "    /**" );
                outStream.println( "      * " + PropertyValue.toStringRepresentationOrNull( type.getUserVisibleDescription(), theCommentsRepresentation, null ));
                outStream.println( "      */" );
            }

            if( type instanceof EntityType ) {
                EntityType realType = (EntityType) type;

                String name      = realType.getName().value();
                String upperName = name.toUpperCase();
                String id   = realType.getIdentifier().toExternalForm();
                outStream.println( "    public static final EntityType " + upperName + " = " + packageName + getInterfaceSubPackageName() + "." + name + "._TYPE;");
                
                for( PropertyType propType : realType.getLocalPropertyTypes() ) {
                    String propName      = propType.getName().value();
                    String upperPropName = propName.toUpperCase();
                    String propId        = propType.getIdentifier().toExternalForm();

                    outStream.println();
                    if( propType.getUserVisibleDescription() != null ) {
                        outStream.println( "    /**" );
                        outStream.println( "      * " + PropertyValue.toStringRepresentationOrNull( propType.getUserVisibleDescription(), theCommentsRepresentation, null ));
                        outStream.println( "      */" );
                    }
                    outStream.println( "    public static final PropertyType " + upperName + "_" + upperPropName + " = " + packageName + getInterfaceSubPackageName() + "." + name + "." + propName.toUpperCase() + ";" );
                    
                    outStream.println( "    public static final " + propType.getDataType().getClass().getName() + " " + upperName + "_" + upperPropName + "_type = (" + propType.getDataType().getClass().getName() + ") " + upperName + "_" + upperPropName + ".getDataType();" );

                    if( propType.getDataType() instanceof EnumeratedDataType ) {
                        outStream.println( "    /* Values of the EnumeratedDataType. */" );
                        EnumeratedDataType realDataType = (EnumeratedDataType) propType.getDataType();

                        for( EnumeratedValue current : realDataType.getDomain() ) {
                            outStream.println( "    public static final EnumeratedValue " + upperName + "_" + upperPropName + "_type_" + current.value().toUpperCase() + " = " + upperName + "_" + upperPropName + "_type.select( \"" + current.value() + "\" );" );
                        }
                    }
                }

            } else {
                // must be a RelationshipType
                RelationshipType realType = (RelationshipType) type;

                String name      = realType.getName().value();
                String upperName = name.toUpperCase();
                String id   = realType.getIdentifier().toExternalForm();
                outStream.println( "    public static final RelationshipType " + upperName + " = ModelBaseSingleton.findRelationshipType( \"" + realType.getIdentifier().toExternalForm() + "\" );" );
            }
            outStream.println();
        }
        
        outStream.println( "}" );
        outStream.close();
        
        return getRelativeCodeFileNameFor( theSa );
    }

    /**
     * Generate the JavaDoc documentation for one SubjectArea.
     *
     * @param theSubjectArea the SubjectArea to generate documentation for
     * @return the fully-qualified file name where it was generated
     * @throws IOException thrown if an I/O error occurred during code generation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    protected String generateJavaDocForSubjectArea(
            SubjectArea theSubjectArea )
        throws
            IOException,
            StringifierException
    {
        PrintWriter w = this.getJavaDocPrintWriterFor( theSubjectArea );
        
        w.println( "<html>" );
        w.println( " <head>" );
        w.println( "  <title>Documentation for " + theSubjectArea.getIdentifier().toExternalForm() + "</title>" );
        w.println( " </head>" );
        w.println( " <body>" );
        
        w.println( PropertyValue.toStringRepresentationOrNull( theSubjectArea.getUserVisibleDescription(), theCommentsRepresentation, null ));

        w.println( " </body>" );
        w.println( "</html>" );
        
        w.close();
        
        return getRelativeJavaDocFileNameFor( theSubjectArea );
    }

    /**
      * Obtain a relative file name into which the code will be written.
      *
      * @param theMeshType the MeshType for which we generate code
      * @return the relative path of the file to which the code will be written
      * @throws IOException thrown if an I/O error occurred
      */
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

        String rawSubjectArea = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        if( theAttributableMeshType != null ) {
            String amoName = theAttributableMeshType.getName().value();

            return dirName + File.separatorChar + amoName + ".java";

        } else {
            String shortSaName = rawSubjectArea.substring( rawSubjectArea.lastIndexOf( '.' )+1 );
            shortSaName = Character.toTitleCase( shortSaName.charAt( 0 ) ) + shortSaName.substring( 1 ); // first char is uppercase
            return dirName + File.separatorChar + shortSaName + "SubjectArea.java";
        }
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
        String rawSubjectArea = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        return dirName + File.separatorChar + "package.html";
    }
}
