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

package org.infogrid.modelbase;

import java.io.IOException;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.primitives.UnknownEnumeratedValueException;
import org.infogrid.util.logging.Log;

/**
 * <p>An abstract supertype for classes that know how to load
 * a model, or part of a model.</p>
 *
 * <p>Note: subtypes of this class need to have the same constructor as this class
 * for the Class.forName()-based instantiation to work.</p>
 */

public abstract class ModelLoader
{
    /**
     * Constructor for subclasses only.
     *
     * @param modelBase the ModelBase into which the new model will be merged
     */
    protected ModelLoader(
            ModelBase modelBase )
    {
        theModelBase = modelBase;
    }

    /**
     * Instruct this ModelLoader to instantiate and check its model.
     *
     * @param theInstantiator the MeshTypeLifecycleManager whose methods shall be used to instantiate
     * @param now the time the current run was started
     * @return the SubjectAreas that were instantiated
     * @throws MeshTypeNotFoundException thrown if there was an undeclared dependency in the model
     *         that could not be resolved
     * @throws InheritanceConflictException thrown if there was a conflict in the inheritance hierarchy of
     *         the newly loaded model
     * @throws IOException thrown if reading the model failed
     * @throws UnknownEnumeratedValueException thrown if an invalid EnumeratedValue was specified
     */
    public final SubjectArea [] loadAndCheckModel(
            MeshTypeLifecycleManager theInstantiator,
            TimeStampValue           now )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException,
            UnknownEnumeratedValueException
    {
        LoaderMeshTypeLifecycleManager delegate = new LoaderMeshTypeLifecycleManager( theInstantiator );

        SubjectArea [] ret = loadModel( delegate, now );

        MeshType [] inst = delegate.getInstantiated();
        for( int i=0 ; i<inst.length ; ++i ) {
            if( inst[i] instanceof EntityType ) {
                theModelBase.checkEntityType( (EntityType) inst[i] );
            } else if( inst[i] instanceof RelationshipType ) {
                theModelBase.checkRelationshipType( (RelationshipType) inst[i] );
            } else if( inst[i] instanceof PropertyType ) {
                theModelBase.checkPropertyType( (PropertyType) inst[i] );
            } else if( inst[i] instanceof PropertyTypeGroup ) {
                theModelBase.checkPropertyTypeGroup( (PropertyTypeGroup) inst[i] );
            } else if( inst[i] instanceof SubjectArea ) {
                theModelBase.checkSubjectArea( (SubjectArea) inst[i] );
            } else {
                getLog().error( "unexpected " + inst[i] );
            }
        }
        return ret;
    }

    /**
     * Instruct this ModelLoader to instantiate its model.
     *
     * @param theInstantiator the MeshTypeLifecycleManager whose method shall be used to instantiate
     * @param now the time the current run was started
     * @return the SubjectAreas that were instantiated
     * @throws MeshTypeNotFoundException thrown if there was an undeclared dependency in the model that could not be resolved
     * @throws InheritanceConflictException thrown if there was a conflict in the inheritance hierarchy
     *          of the newly loaded model
     * @throws IOException thrown if reading the model failed
     * @throws UnknownEnumeratedValueException thrown if an invalid EnumeratedValue was specified
     */
    protected abstract SubjectArea [] loadModel(
            MeshTypeLifecycleManager theInstantiator,
            TimeStampValue           now )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException,
            UnknownEnumeratedValueException;

    /**
     * Obtain the correct logger of the subclass.
     *
     * @return the logger of the subclass
     */
    protected final Log getLog()
    {
        return Log.getLogInstance( getClass() );
    }

    /**
     * The ModelBase that we work on.
     */
    protected ModelBase theModelBase;
}
