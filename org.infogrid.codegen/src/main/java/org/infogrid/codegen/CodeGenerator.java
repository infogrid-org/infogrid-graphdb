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

package org.infogrid.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.infogrid.codegen.impl.ImplementationGenerator;
import org.infogrid.codegen.intfc.InterfaceGenerator;
import org.infogrid.codegen.modelloader.ModelLoaderGenerator;
import org.infogrid.codegen.moduleinit.ModuleInitGenerator;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.primitives.text.ModelPrimitivesStringRepresentationDirectorySingleton;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.module.ModuleClassLoader;
import org.infogrid.util.ResourceHelper;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationDirectory;
import org.infogrid.util.text.StringifierException;

/**
 * The InfoGrid code generator.
 */
public class CodeGenerator
{
    private static final Log log = Log.getLogInstance( CodeGenerator.class );

    /**
     * Main program for the code generator.
     *
     * @param args command-line arguments
     * @throws Throwable things may go wrong
     */
    public static void main(
            String [] args )
        throws
            Throwable
    {
        ArrayList<String> modelFiles = new ArrayList<>();
        File              outputDir  = null;
        boolean           isOutput   = false;

        for( int i=0 ; i<args.length ; ++i ) {
            if( "-o".equals( args[i] )) {
                isOutput = true;
            } else if( isOutput ) {
                isOutput = false;
                if( outputDir != null ) {
                    usageAndQuit();
                } else {
                    outputDir = new File( args[i] );
                    if( !outputDir.exists() || !outputDir.canRead() ) {
                        outputDir.mkdirs();
                    }
                    if( !outputDir.exists() || !outputDir.canRead() ) {
                        usageAndQuit();
                    }
                }
            } else {
                modelFiles.add( args[i] );
            }
        }
        if( outputDir == null ) {
            usageAndQuit();
        }

        ClassLoader cl = CodeGenerator.class.getClassLoader();
        if( !( cl instanceof ModuleClassLoader ) || ((ModuleClassLoader)cl).getModuleRegistry() == null ) {
            usageAndQuit();
        }

        ResourceHelper.initializeLogging();
        ModelPrimitivesStringRepresentationDirectorySingleton.initialize();

        StringRepresentation commentsRepresentation
                = ModelPrimitivesStringRepresentationDirectorySingleton.getSingleton().get( StringRepresentationDirectory.TEXT_JAVADOC_NAME );
        
        CodeGenerator  generator = new CodeGenerator( outputDir, commentsRepresentation );
        TimeStampValue now       = TimeStampValue.now();

        Iterator<String> iter = modelFiles.iterator();
        while( iter.hasNext() ) {
            File modelFile = new File( iter.next());
            if( ! modelFile.canRead() ) {
                throw new CodeGeneratorRunException( "Cannot read model file " + modelFile.getAbsolutePath() );
            }
            SubjectArea [] sas = ModelBaseSingleton.loadModel(
                    modelFile.getAbsolutePath(),
                    new FileInputStream( modelFile ),
                    CodeGenerator.class.getClassLoader(),
                    now );

            if( sas == null || sas.length == 0 ) {
                throw new CodeGeneratorRunException( "Could not load model from " + modelFile.getAbsolutePath() );
            }
            generator.generateForAll( sas );
        }
    }

    /**
     * Print usage information and quit.
     */
    private static void usageAndQuit()
    {
        System.err.println( "Usage: The code generator must be invoked from the InfoGrid Module Framework:" );
        System.err.println( "    root module: " + CodeGenerator.class.getName() );
        System.err.println( "    arguments: <model.xml file> ... -o <outputDir>" );
        System.exit( 1 );
    }

    /**
     * Constructor.
     *
     * @param outputDir the output directory
     * @param commentsRepresentation the StringRepresentation for generated comments
     */
    public CodeGenerator(
            File                 outputDir,
            StringRepresentation commentsRepresentation )
    {
        theOutputDirectory        = outputDir;
        theCommentsRepresentation = commentsRepresentation;
    }
    
    /**
     * Generate the code for one SubjectArea.
     *
     * @param sas the SubjectArea
     * @throws IOException thrown if an I/O error occurred
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public void generateForAll(
            SubjectArea [] sas )
        throws
            IOException,
            StringifierException
    {
        InterfaceGenerator theInterfaceGenerator = new InterfaceGenerator( theOutputDirectory, theCommentsRepresentation );
        theInterfaceGenerator.generateForAll( sas );

        ImplementationGenerator theImplementationGenerator = new ImplementationGenerator( theOutputDirectory, theCommentsRepresentation );
        theImplementationGenerator.generateForAll( sas );

        ModelLoaderGenerator theLoaderGenerator = new ModelLoaderGenerator( theOutputDirectory, theCommentsRepresentation );
        theLoaderGenerator.generateForAll( sas );

        ModuleInitGenerator theModuleInitGenerator = new ModuleInitGenerator( theOutputDirectory, theCommentsRepresentation );
        theModuleInitGenerator.generateForAll( sas );
    }

    /**
     * The output directory for generated artifacts.
     */
    protected File theOutputDirectory;
    
    /**
     * The StringRepresentation for generated comments.
     */
    protected StringRepresentation theCommentsRepresentation;
}
