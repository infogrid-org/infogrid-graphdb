//
//
//

package org.infogrid.codegen.moduleinit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.infogrid.codegen.AbstractGenerator;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.util.text.StringRepresentation;

/**
 * This class knows how to generate Java source code class that can be used
 * as the ModuleInit class for a SubjectArea Module.
 */
public class ModuleInitGenerator
    extends
        AbstractGenerator
{
    /**
     * Constructor.
     *
     * @param outputDir the directory into which the code shall be generated
     * @param commentsRepresentation the StringRepresentation to use for emitting comments
     */
    public ModuleInitGenerator(
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
        String packageName = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSa );

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

        generateActivate(
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
        String packageName = thePackageNameTranslatorWithVersion.translateSubjectArea( theSa );

        outStream.println( "import java.io.IOException;" );
        outStream.println( "import " + packageName + ".SubjectAreaLoader;" );
        outStream.println( "import org.infogrid.model.primitives.TimeStampValue;" );
        outStream.println( "import org.infogrid.modelbase.MeshTypeNotFoundException;" );
        outStream.println( "import org.infogrid.modelbase.ModelBase;" );
        outStream.println( "import org.infogrid.modelbase.ModelBaseSingleton;" );
        outStream.println( "import org.diet4j.core.Module;" );
        outStream.println( "import org.diet4j.core.ModuleActivationException;" );
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
        outStream.println( "  * Activates the module by loading the Subject Area." );
        outStream.println( "  */" );
        outStream.println( "public class ModuleInit" );
    }

    /**
     * Generate the activate method.
     *
     * @param outStream the stream to write to
     * @param theSa the SubjectArea that we generate
     * @throws IOException an input/output error occurred
     */
    protected void generateActivate(
            PrintWriter outStream,
            SubjectArea theSa )
        throws
            IOException
    {
        outStream.println( "    /**" );
        outStream.println( "     * Activate this Module." );
        outStream.println( "     *" );
        outStream.println( "     * @param thisModule the current Module" );
        outStream.println( "     * @return the Subject Area as context object" );
        outStream.println( "     * @throws ModuleActivationException thrown if the Module could not be activated" );
        outStream.println( "     */" );
        outStream.println( "    public static Object moduleActivate(" );
        outStream.println( "            Module thisModule )" );
        outStream.println( "        throws" );
        outStream.println( "            ModuleActivationException" );
        outStream.println( "    {" );
        outStream.println( "        try {" );
        outStream.println( "            ModelBase mb = ModelBaseSingleton.getSingleton();" );
        outStream.println();
        outStream.println( "            SubjectAreaLoader saLoader = new SubjectAreaLoader( mb, thisModule.getClassLoader() );" );
        outStream.println();
        outStream.println( "            return saLoader.loadAndCheckModel( mb.getMeshTypeLifecycleManager(), TimeStampValue.now());" );
        outStream.println();
        outStream.println( "        } catch( MeshTypeNotFoundException | IOException ex ) {" );
        outStream.println( "            throw new ModuleActivationException( thisModule.getModuleMeta(), ex );" );
        outStream.println( "       }" );
        outStream.println( "    }" );
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

        String rawSubjectArea = thePackageNameTranslatorWithoutVersion.translateSubjectArea( theSubjectArea );
        String dirName        = rawSubjectArea.replace( '.', File.separatorChar );

        return dirName + File.separatorChar + "ModuleInit.java";
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
}
