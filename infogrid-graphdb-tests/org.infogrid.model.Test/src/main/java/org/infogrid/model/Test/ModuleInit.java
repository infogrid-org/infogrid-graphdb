/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.infogrid.model.Test;

import java.io.IOException;
import org.infogrid.model.Test.V.SubjectAreaLoader;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.module.Module;
import org.infogrid.module.ModuleActivationException;
/**
 *
 * @author buildmaster
 */
public class ModuleInit
{
    public static Object activate( 
            Module[] dependentModules,
            Object[] contextObjects,
            Module   theModule )
        throws
            ModuleActivationException
    {
        try {
            ModelBase mb = ModelBaseSingleton.getSingleton();
            
            SubjectAreaLoader saLoader = new SubjectAreaLoader( mb, theModule.getClassLoader() );

            return saLoader.loadAndCheckModel( mb.getMeshTypeLifecycleManager(), TimeStampValue.now());

        } catch( MeshTypeNotFoundException | IOException ex ) {
            throw new ModuleActivationException( theModule.getModuleMeta(), ex );
        }
    }
}
