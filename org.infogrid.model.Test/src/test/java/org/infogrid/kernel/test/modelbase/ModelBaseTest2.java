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

import java.util.Iterator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests that overridden PropertyTypes in multiple inheritance
 * cases only show up once in subtypes.
 */
public class ModelBaseTest2
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

        log.info( "Iterating over content, and checking that no AttributableMeshType has more than one PropertyType with the same ancestor" );

        Iterator<MeshType> theIter = theModelBase.iterator();
        while( theIter.hasNext() ) {
            MeshType current = theIter.next();

            if( !( current instanceof AttributableMeshType )) {
                continue;
            }

            AttributableMeshType realCurrent = (AttributableMeshType) current;

            log.info(
                    "looking at "
                    + ( realCurrent instanceof EntityType ? "EntityType" : "RelationshipType" )
                    + ": "
                    + realCurrent.getIdentifier() );

            PropertyType [] allPts = realCurrent.getAllPropertyTypes();
            for( int i=0 ; i<allPts.length ; ++i ) {
                PropertyType iPt         = allPts[i];
                PropertyType ancestorIPt = iPt.getOverrideAncestor();

                for( int j=0 ; j<i ; ++j ) {
                    PropertyType jPt = allPts[j];

                    if( iPt.getIdentifier().equals( jPt.getIdentifier() )) {
                        reportError( "found the same PropertyType twice", iPt, jPt );
                    }
                    PropertyType ancestorJPt = jPt.getOverrideAncestor();

                    if( ancestorIPt.getIdentifier().equals( ancestorJPt.getIdentifier() )) {
                        reportError( "two PropertyTypes have same ancestor", iPt, jPt, ancestorIPt );
                    }
                }
            }
        }
    }

    // Our Logger
    private static Log log = Log.getLogInstance( ModelBaseTest2.class);
}
