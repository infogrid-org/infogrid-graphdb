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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.test.modelbase;

import java.util.Iterator;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.modelbase.externalized.xml.XmlModelExporter;
import org.infogrid.util.logging.Log;

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
    public void run()
        throws
            Exception
    {
        log.info( "Loading Subject Areas" );

        for( int i=0 ; i<theSubjectAreas.length ; ++i ) {
            theModelBase.findSubjectArea( theSubjectAreas[i], theSubjectAreaVersions[i] );
        }

        //

        if( log.isDebugEnabled() ) {
            XmlModelExporter theExporter = new XmlModelExporter();
            theExporter.exportToXML( theModelBase, System.err );
        }

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

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        ModelBaseTest2 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: {SubjectArea1.xml} ..." );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ModelBaseTest2( args );
            test.run();

        } catch( Throwable ex ) {
            log.error( ex );
            System.exit(1);
        }
        if( test != null ) {
            test.cleanup();
        }

        if( errorCount == 0 ) {
            log.info( "PASS" );
        } else {
            log.info( "FAIL (" + errorCount + " errors)" );
        }
        System.exit( errorCount );
    }

    /**
      * Constructor.
      *
      * @param args command-line arguments
      */
    public ModelBaseTest2(
            String [] args )
        throws
            Exception
    {
        super( ModelBaseTest2.class );

        // create ModelBase
        theModelBase = ModelBaseSingleton.getSingleton();

        // subject areas to load
        theSubjectAreas        = new String[ args.length ];
        theSubjectAreaVersions = new String[ args.length ];

        for( int i=0 ; i<args.length ; ++i ) {
            theSubjectAreas[i]        = args[i];
            theSubjectAreaVersions[i] = null; // FIXME?
        }
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        theModelBase.die();
    }

    /**
     * The names of the Subject Areas we want to load for this test.
     */
    protected String [] theSubjectAreas;
    protected String [] theSubjectAreaVersions;

    // Our Logger
    private static Log log = Log.getLogInstance( ModelBaseTest2.class);

    /**
     * The ModelBase to test.
     */
    protected ModelBase theModelBase;
}
