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

import org.infogrid.module.DefaultStandardModuleActivator;
import org.infogrid.module.ModelModule;
import org.infogrid.module.Module;
import org.infogrid.module.ModuleActivationException;
import org.infogrid.module.ModuleActivator;
import org.infogrid.module.StandardModule;

/**
 * A StandardModuleActivator specifically for the purposes of the code generator. It cannot, for example,
 * attempt to load generated class files because they aren't there yet.
 */
public class CodeGeneratorStandardModuleActivator
        extends
            DefaultStandardModuleActivator
{
    /**
     * Constructor.
     *
     * @param mod the Module to activate
     */
    public CodeGeneratorStandardModuleActivator(
            StandardModule mod )
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
                    (StandardModule) dependentModule );
        } else {
            ret = new CodeGeneratorModelModuleActivator(
                    (ModelModule) dependentModule );
        }
        return ret;
    }
}
