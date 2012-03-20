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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.codegen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.infogrid.module.ModelModule;
import org.infogrid.module.ModelModuleActivator;
import org.infogrid.module.Module;
import org.infogrid.module.ModuleActivationException;
import org.infogrid.module.ModuleActivator;
import org.infogrid.module.ModuleErrorHandler;
import org.infogrid.module.StandardModule;

/**
 * A ModelModuleActivator specifically for the purposes of the code generator. It cannot, for example,
 * attempt to load generated class files because they aren't there yet.
 */
public class CodeGeneratorModelModuleActivator
        extends
            ModelModuleActivator
{
    /**
     * Constructor.
     *
     * @param mod the ModelModule that we activate
     */
    public CodeGeneratorModelModuleActivator(
            ModelModule mod )
    {
        super( mod );
    }

    /**
     * Obtain a ModuleActivator that is responsible for activating a dependent Module.
     *
     * @param dependentModule the dependent Module to activate
     * @return the ModuleActivator for the dependent Module
     * @throws ModuleActivationException thrown if the dependent Module could not be activated
     */
    @Override
    public ModuleActivator dependentModuleActivator(
            Module dependentModule )
        throws
            ModuleActivationException
    {
        ModuleActivator ret;
        if( dependentModule instanceof StandardModule ) {
            ret = new CodeGeneratorStandardModuleActivator(
                    (StandardModule) dependentModule);
        } else {
            ret = new CodeGeneratorModelModuleActivator(
                    (ModelModule) dependentModule );
        }
        return ret;
    }

    /**
     * Activate a Module.
     *
     * @param dependentModules the Modules that this Module depends on, if any
     * @param dependentContextObjects the context objects of the Modules that this Module depends on, if any, in same sequence as dependentModules
     * @return a context object that is Module-specific, or null if none
     * @throws ModuleActivationException throws if the Module could not be activated
     */
    public Object activate(
            Module [] dependentModules,
            Object [] dependentContextObjects )
        throws
            ModuleActivationException
    {
        Exception ex = null;

        try {
            ModuleErrorHandler.informModuleActivateStart( theModule );

            File adFile    = theModule.getModuleAdvertisement().getAdvertisementFile();
            File modelFile = new File( adFile.getCanonicalPath().replaceAll( "\\.adv$", ".xml" ));

            Object ret = activateModelUsingXmlModel(
                    dependentModules,
                    dependentContextObjects,
                    new BufferedInputStream( new FileInputStream( modelFile )),
                    theModule.getClassLoader() );

            ModuleErrorHandler.informModuleActivateSucceeded( theModule );
            return ret;

        } catch( Exception ex2 ) {
            ex = ex2;
            ModuleErrorHandler.informModuleActivateFailed( theModule, ex );
        }
        
        if( ex instanceof ModuleActivationException ) {
            throw ((ModuleActivationException)ex);
        } else {
            throw new ModuleActivationException( theModule.getModuleAdvertisement(), ex );
        }
    }
}
