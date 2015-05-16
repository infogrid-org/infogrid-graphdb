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
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.modelbase.externalized.xml.XmlModelExporter;
import org.infogrid.util.logging.Log;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the MMeshBase's ability to store and retrive MeshTypes.
 */
public class ModelBaseTest1
        extends
            AbstractModelBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test
     */
    @Test
    public void run()
        throws
            Exception
    {
        log.info( "Loading Subject Areas" );

        populateModelBase();

        //

        log.info( "Iterating over content, and trying to find all those MeshTypes" );

        Iterator<MeshType> theIter = theModelBase.iterator();

        while( theIter.hasNext() ) {
            MeshType           current = theIter.next();
            MeshTypeIdentifier id      = current.getIdentifier();

            log.debug( "Looking at MeshType with id " + id.toExternalForm() );

            //

            if( current instanceof EntityType ) {
                
                EntityType  realCurrent = (EntityType) current;
                SubjectArea sa          = realCurrent.getSubjectArea();

                MeshType other = theModelBase.findMeshTypeByIdentifier( id );
                checkIdentity( current, other, "Look-up by Identifier failed for EntityType" );

                SubjectArea otherSa = theModelBase.findSubjectArea(
                        sa.getName().value(),
                        sa.getVersionNumber() != null ? sa.getVersionNumber().value() : null );
                checkIdentity( sa, otherSa, "Look-up by name failed for SubjectArea" );

                other = theModelBase.findEntityType( otherSa, realCurrent.getName().value() );
                checkIdentity( current, other, "Look-up by name failed for EntityType" );

                other = theModelBase.findEntityType(
                        sa.getName().value(),
                        sa.getVersionNumber() != null ? sa.getVersionNumber().value() : null,
                        realCurrent.getName().value() );
                checkIdentity( current, other, "Look-up direct failed for EntityType" );

            } else if( current instanceof RelationshipType ) {

                RelationshipType realCurrent = (RelationshipType) current;
                SubjectArea      sa          = realCurrent.getSubjectArea();

                MeshType other = theModelBase.findMeshTypeByIdentifier( id );
                checkIdentity( current, other, "Look-up by Identifier failed for RelationshipType" );

                SubjectArea otherSa = theModelBase.findSubjectArea(
                        sa.getName().value(),
                        sa.getVersionNumber() != null ? sa.getVersionNumber().value() : null );
                checkIdentity( sa, otherSa, "Look-up by name failed for SubjectArea" );

                other = theModelBase.findRelationshipType( otherSa, realCurrent.getName().value() );
                checkIdentity( current, other, "Look-up by name failed for RelationshipType" );

                other = theModelBase.findRelationshipType(
                        sa.getName().value(),
                        sa.getVersionNumber() != null ? sa.getVersionNumber().value() : null,
                        realCurrent.getName().value() );
                checkIdentity( current, other, "Look-up direct failed for RelationshipType" );

            } else if( current instanceof PropertyType ) {
                
                PropertyType         realCurrent = (PropertyType) current;
                SubjectArea          sa          = realCurrent.getSubjectArea();
                AttributableMeshType amo         = realCurrent.getAttributableMeshType();
                SubjectArea          amoSa       = amo.getSubjectArea();

                MeshType other = theModelBase.findMeshTypeByIdentifier( id );
                checkIdentity( current, other, "Look-up by Identifier failed for PropertyType" );

                SubjectArea otherSa = theModelBase.findSubjectArea(
                        sa.getName().value(),
                        sa.getVersionNumber() != null ? sa.getVersionNumber().value() : null );
                checkIdentity( sa, otherSa, "SubjectAreas not the same" );
                checkIdentity( sa, amoSa,   "SubjectAreas not the same" );

                AttributableMeshType otherAmo;
                if( amo instanceof EntityType ) {
                    otherAmo = theModelBase.findEntityType( sa, amo.getName().value() );
                } else {
                    otherAmo = theModelBase.findRelationshipType( sa, amo.getName().value() );
                }
                checkIdentity( amo, otherAmo, "AttributableMeshType not the same" );

                other = theModelBase.findPropertyType( amo, realCurrent.getName().value() );
                checkIdentity( current, other, "Look-up by name failed for PropertyType" );

            } else if( current instanceof SubjectArea ) {
                
                SubjectArea realCurrent = (SubjectArea) current;

                SubjectArea otherSa = theModelBase.findSubjectArea(
                        realCurrent.getName().value(),
                        realCurrent.getVersionNumber() != null ? realCurrent.getVersionNumber().value() : null );
                checkIdentity( realCurrent, otherSa, "Look-up direct failed for SubjectArea" );

            } else {
                
                log.error( "what is this: " + current );
            }
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ModelBaseTest1.class);
}
