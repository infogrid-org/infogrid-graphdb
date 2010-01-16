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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.kernel.test.modelbase;

import java.io.FileOutputStream;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.modelbase.externalized.xml.XmlModelExporter;
import org.infogrid.util.logging.Log;

/**
 * Tests loading SubjectAreas from files and saving their content to files.
 */
public class ModelBaseTest4
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

        log.info( "Exporting to file" );

        FileOutputStream theStream = new FileOutputStream( theExportFile );

        XmlModelExporter theExporter = new XmlModelExporter();
        theExporter.exportToXML( theModelBase, theStream );

        //
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        ModelBaseTest4 test = null;
        try {
            if( args.length < 2 || ! "-o".equals( args[ args.length-2 ] ))
            {
                System.err.println( "Synopsis: {SubjectArea1.xml} -o <exportfile>..." );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new ModelBaseTest4( args );
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
    public ModelBaseTest4(
            String [] args )
        throws
            Exception
    {
        super( ModelBaseTest4.class );

        // create ModelBase
        theModelBase = ModelBaseSingleton.getSingleton();

        // subject areas to load
        theSubjectAreas        = new String[ args.length-2 ];
        theSubjectAreaVersions = new String[ args.length-2 ];

        for( int i=0 ; i<args.length-2 ; ++i ) {
            theSubjectAreas[i]        = args[i];
            theSubjectAreaVersions[i] = null; // FIXME?
        }

        theExportFile = args[ args.length-1 ];
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
    private static Log log = Log.getLogInstance( ModelBaseTest4.class);

    /**
     * The file to which we export the Model.
     */
    protected String theExportFile;

    /**
     * The ModelBase to test.
     */
    protected ModelBase theModelBase;
}
