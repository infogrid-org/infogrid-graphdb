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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;

/**
 * Make sure Properties go away when their EntityType is unblessed.
 */
public class MeshBaseTest15
        extends
            AbstractMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public void run()
        throws
            Exception
    {
        log.info( "Instantiating MeshObject of type AA" );

        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();
        MeshObject obj = life.createMeshObject( TestSubjectArea.AA );
        obj.setPropertyValue( TestSubjectArea.A_X, StringValue.create( "Some value" ));
            
        tx.commitTransaction();
        
        PropertyType [] types = obj.getAllPropertyTypes();
        checkEquals( types.length, 4, "Wrong number of Properties" );
        
        //
        
        log.info( "Unblessing and checking" );
        
        tx = theMeshBase.createTransactionNow();

        obj.unbless( TestSubjectArea.AA );
            
        tx.commitTransaction();
        
        checkEquals( obj.getAllPropertyTypes().length, 0, "Wrong number of Properties" );
        
        //
        
        log.info( "Making sure that the values are gone, too" );
        
        for( PropertyType current : types ) {
            try {
                PropertyValue value = obj.getPropertyValue( current );
                
                reportError( "Accessed non-existing property successfully", current, value );

            } catch( IllegalPropertyTypeException ex ) {
                log.debug( "Correctly received exception", ex );
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
        MeshBaseTest15 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new MeshBaseTest15( args );
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
     * @throws Exception all sorts of things may go wrong during a test.
     */
    public MeshBaseTest15(
            String [] args )
        throws
            Exception
    {
        super( MeshBaseTest15.class );

        theMeshBase = MMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                rootContext );
    }

    /**
     * Clean up after the test.
     */
    @Override
    public void cleanup()
    {
        theMeshBase.die();
    }

    /**
     * The MeshBase for the test.
     */
    protected MeshBase theMeshBase;

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest15.class);
}
