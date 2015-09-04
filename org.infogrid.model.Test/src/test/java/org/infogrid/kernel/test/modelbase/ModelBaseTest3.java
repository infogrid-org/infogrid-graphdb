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

package org.infogrid.kernel.test.modelbase;

import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.L10PropertyValueMapImpl;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.StringDataType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.modelbase.MeshTypeLifecycleManager;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.util.logging.Log;

/**
 * Instantiates a test model.
 */
public class ModelBaseTest3
        extends
            AbstractModelBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test
     */
    public void run()
        throws
            Exception
    {
        MeshTypeLifecycleManager life = theModelBase.getMeshTypeLifecycleManager();

        ClassLoader loader = getClass().getClassLoader();

        //

        log.info( "Creating test Model" );

        SubjectArea sa = life.createSubjectArea(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "Test.Sa/sa"),
                StringValue.create( "TestSa" ),
                L10PropertyValueMapImpl.create( StringValue.create( "Test SA" ) ),
                null,
                new SubjectArea[0],
                loader,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkSubjectArea( sa );

        EntityType a = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/A" ),
                StringValue.create( "A" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is a" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[0],
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        PropertyType a_name = life.createPropertyType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/A_Name" ),
                StringValue.create( "Name" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is a name" ) ),
                null,
                a,
                sa,
                StringDataType.theDefault,
                null,
                null,
                new String[0],
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                FloatValue.create( 123.0 ) );
        theModelBase.checkPropertyType( a_name );
        theModelBase.checkEntityType( a );

        EntityType b = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/B" ),
                StringValue.create( "B" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is b name" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { a },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        PropertyType b_name = life.createOverridingPropertyType(
                new PropertyType[] { a_name },
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/B_Name" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is b name" ) ),
                b,
                sa,
                StringDataType.theDefault,
                null,
                null,
                new String[0],
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkPropertyType( b_name );
        theModelBase.checkEntityType( b );

        EntityType c = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/C" ),
                StringValue.create( "C" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is c" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { a },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        PropertyType c_name = life.createOverridingPropertyType(
                new PropertyType[] { a_name },
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/C_Name" ),
                null,
                c,
                sa,
                StringDataType.theDefault,
                null,
                null,
                new String[0],
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkPropertyType( c_name );
        theModelBase.checkEntityType( c );

        EntityType d = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/D" ),
                StringValue.create( "D" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is d" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { a },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkEntityType( d );

        // we don't do an override on D

        EntityType e = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/E" ),
                StringValue.create( "E" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is e" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { d },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        PropertyType e_name = life.createOverridingPropertyType(
                new PropertyType[] { a_name },
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/E_Name" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is e name" ) ),
                e,
                sa,
                StringDataType.theDefault,
                null,
                null,
                new String[0],
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkPropertyType( e_name );
        theModelBase.checkEntityType( e );

        boolean exceptionThrown = false;
        try {
            EntityType f = life.createEntityType(
                    theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/F" ),
                    StringValue.create( "F" ),
                    L10PropertyValueMapImpl.create( StringValue.create( "This is f" ) ),
                    null,
                    null,
                    sa,
                    new AttributableMeshType[] { b, c },
                    new MeshTypeIdentifier[0],
                    null,
                    new String[0],
                    new StringValue[0],
                    new StringValue[0],
                    new String[0],
                    BooleanValue.FALSE,
                    BooleanValue.FALSE,
                    BooleanValue.TRUE,
                    BooleanValue.TRUE,
                    BooleanValue.TRUE );
            theModelBase.checkEntityType( f );
        } catch( InheritanceConflictException ex ) {
            exceptionThrown = true;
        }
        checkCondition( exceptionThrown, "exception was not thrown when creating a conflict on meta-entity F" );

        exceptionThrown = false;
        try {
            EntityType g = life.createEntityType(
                    theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/G" ),
                    StringValue.create( "G" ),
                    L10PropertyValueMapImpl.create( StringValue.create( "This is g" ) ),
                    null,
                    null,
                    sa,
                    new AttributableMeshType[] { c, e },
                    new MeshTypeIdentifier[0],
                    null,
                    new String[0],
                    new StringValue[0],
                    new StringValue[0],
                    new String[0],
                    BooleanValue.FALSE,
                    BooleanValue.FALSE,
                    BooleanValue.TRUE,
                    BooleanValue.TRUE,
                    BooleanValue.TRUE );
            theModelBase.checkEntityType( g );
        } catch( InheritanceConflictException ex ) {
            exceptionThrown = true;
        }
        checkCondition( exceptionThrown, "exception was not thrown when creating a conflict on meta-entity G" );

        EntityType h = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/H" ),
                StringValue.create( "H" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is h" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { c, d },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        PropertyType h_name = life.createOverridingPropertyType(
                new PropertyType[] { c_name },
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/H_Name" ),
                null,
                h,
                sa,
                StringDataType.theDefault,
                null,
                null,
                new String[0],
                BooleanValue.TRUE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );
        theModelBase.checkPropertyType( h_name );
        theModelBase.checkEntityType( h );

        EntityType i = life.createEntityType(
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/I" ),
                StringValue.create( "I" ),
                L10PropertyValueMapImpl.create( StringValue.create( "This is i" ) ),
                null,
                null,
                sa,
                new AttributableMeshType[] { c },
                new MeshTypeIdentifier[0],
                null,
                new String[0],
                new StringValue[0],
                new StringValue[0],
                new String[0],
                BooleanValue.FALSE,
                BooleanValue.FALSE,
                BooleanValue.TRUE,
                BooleanValue.TRUE,
                BooleanValue.TRUE );

        exceptionThrown = false;
        try {
            PropertyType i_name = life.createOverridingPropertyType(
                    new PropertyType[] { a_name },
                theModelBase.getMeshTypeIdentifierFactory().fromExternalForm( "pre.fix/I_Name" ),
                    null,
                    i,
                    sa,
                    StringDataType.theDefault,
                    null,
                    null,
                    new String[0],
                    BooleanValue.TRUE,
                    BooleanValue.FALSE,
                    BooleanValue.TRUE,
                    BooleanValue.TRUE );
            theModelBase.checkPropertyType( i_name );
            theModelBase.checkEntityType( i );
        } catch( InheritanceConflictException ex ) {
            exceptionThrown = true;
        }
        checkCondition( exceptionThrown, "exception was not thrown when creating a conflict on meta-entity I" );

        //

        log.info( "checking" );

        EntityType [] all = new EntityType[] { a, b, c, e, h }; // don't check d, it doesn't have a local override
        for( int k=0 ; k<all.length ; ++k ) {
            log.debug( "now looking at EntityType " + all[k] );

            PropertyType [] allPts       = all[k].getAllPropertyTypes();
            PropertyType [] localPts     = all[k].getLocalPropertyTypes();
            PropertyType [] localOverPts = all[k].getOverridingLocalPropertyTypes();

            checkEquals( allPts.length, 1, "incorrect number of all PropertyTypes" );
            checkEquals( localPts.length + localOverPts.length, 1, "incorrect number of local PropertyTypes" );
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ModelBaseTest3.class);
}
