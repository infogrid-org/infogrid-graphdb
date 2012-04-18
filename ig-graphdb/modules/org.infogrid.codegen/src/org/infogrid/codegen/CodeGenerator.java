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

package org.infogrid.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.infogrid.codegen.impl.ImplementationGenerator;
import org.infogrid.codegen.intfc.InterfaceGenerator;
import org.infogrid.codegen.modelloader.ModelLoaderGenerator;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.text.ModelPrimitivesStringRepresentationDirectorySingleton;
import org.infogrid.module.ModelModule;
import org.infogrid.module.Module;
import org.infogrid.module.ModuleActivationException;
import org.infogrid.module.ModuleActivator;
import org.infogrid.module.ModuleAdvertisement;
import org.infogrid.module.ModuleClassLoader;
import org.infogrid.module.ModuleRegistry;
import org.infogrid.module.ModuleRequirement;
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
        ArrayList<String> subjectAreas = new ArrayList<String>();
        File              outputDir    = null;
        boolean           isOutput     = false;

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
                subjectAreas.add( args[i] );
            }
        }
        if( outputDir == null ) {
            usageAndQuit();
        }

        if( theModuleRegistry == null ) {
            usageAndQuit();
        }

        ResourceHelper.initializeLogging();
        ModelPrimitivesStringRepresentationDirectorySingleton.initialize();

        StringRepresentation commentsRepresentation
                = ModelPrimitivesStringRepresentationDirectorySingleton.getSingleton().get( StringRepresentationDirectory.TEXT_JAVADOC_NAME );
        
        CodeGenerator generator = new CodeGenerator( outputDir, commentsRepresentation );

        Iterator<String> iter = subjectAreas.iterator();
        while( iter.hasNext() ) {
            String saName    = iter.next();
            String saVersion = "1_0";

            ModuleRequirement   saRequirement = null;
            ModuleAdvertisement saCandidate   = null;

            try {
                saRequirement = ModuleRequirement.create1( saName, saVersion );
                saCandidate   = theModuleRegistry.determineSingleResolutionCandidate( saRequirement );

                ModelModule saModule = (ModelModule) theModuleRegistry.resolve( saCandidate, true );
                ((ModuleClassLoader)saModule.getClassLoader()).setReportMissingJars( false ); // does not exist at this time

                ModuleActivator activator = new CodeGeneratorModelModuleActivator( saModule );

                SubjectArea [] sas = (SubjectArea []) saModule.activateRecursively( activator );

                if( sas == null || sas.length == 0 ) {
                    log.error( "Could not obtain SubjectArea '" + saName + "', version '" + saVersion + "'" );
                    System.exit( 0 );
                }
                generator.generateForAll( sas );

            } catch( ModuleActivationException ex ) {
                throw new CodeGeneratorRunException( saCandidate, CodeGenerator.class.getName(), ex.getCause() );

            } catch( Throwable ex ) {
                throw new CodeGeneratorRunException( saCandidate, CodeGenerator.class.getName(), ex );
            }
        }
    }

    /**
     * The Module Framework's BootLoader activates this Module by calling this method.
     *
     * @param dependentModules the Modules that this Module depends on, if any
     * @param dependentContextObjects the context objects of the Modules that this Module depends on, if any, in same sequence as dependentModules
     * @param thisModule reference to the Module that is currently being initialized and to which we belong
     * @return a context object that is Module-specific, or null if none
     * @throws Exception may an Exception indicating that the Module could not be activated
     */
    public static Object activate(
            Module [] dependentModules,
            Object [] dependentContextObjects,
            Module    thisModule )
        throws
            Exception
    {
        theModuleRegistry = thisModule.getModuleRegistry();
        
        return null;
    }

    /**
     * Print usage information and quit.
     */
    private static void usageAndQuit()
    {
        System.err.println( "Usage: The code generator must be invoked from the InfoGrid Module Framework:" );
        System.err.println( "    root module: " + CodeGenerator.class.getName() );
        System.err.println( "    arguments: <subjectArea> ... -o <outputDir>" );
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
    }

    /**
     * The ModuleRegistry.
     */
    protected static ModuleRegistry theModuleRegistry;
    
    /**
     * The output directory for generated artifacts.
     */
    protected File theOutputDirectory;
    
    /**
     * The StringRepresentation for generated comments.
     */
    protected StringRepresentation theCommentsRepresentation;
}
