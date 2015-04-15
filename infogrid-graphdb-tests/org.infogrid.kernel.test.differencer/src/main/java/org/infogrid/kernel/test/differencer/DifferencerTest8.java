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

package org.infogrid.kernel.test.differencer;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.IterableMeshBase;
import org.infogrid.meshbase.IterableMeshBaseDifferencer;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.ChangeSet;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.util.logging.Log;

/**
 * Tests everything together: object creation and deletion, type changes, property changes,
 * relationship graph changes, and relationship type changes.
 */
public class DifferencerTest8
        extends
            AbstractDifferencerTest
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
        long now = 1234L;

        //

        log.info( "Creating MeshObjects in MeshBase1" );
        
        MeshBaseLifecycleManager life1 = theMeshBase1.getMeshBaseLifecycleManager();

        Transaction tx1 = theMeshBase1.createTransactionNow();

        MeshObject a1_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a1" ), typeAA, now );
        MeshObject a2_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a2" ), typeAA, now );
        MeshObject a3_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a3" ), typeAA, now );
        MeshObject a4_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a4" ), typeAA, now );

        a1_mb1.setPropertyValue( typeX, StringValue.create( "a1" ));
        a2_mb1.setPropertyValue( typeX, StringValue.create( "a2" ));
        a3_mb1.setPropertyValue( typeX, StringValue.create( "a3" ));
        a4_mb1.setPropertyValue( typeX, StringValue.create( "a4" ));

        a1_mb1.relateAndBless( typeAR1A.getSource(), a2_mb1 );
        a1_mb1.relateAndBless( typeAR1A.getSource(), a3_mb1 );

        tx1.commitTransaction();

        //
        
        log.info( "Creating MeshObjects in MeshBase2" );
        
        MeshBaseLifecycleManager life2 = theMeshBase2.getMeshBaseLifecycleManager();

        Transaction tx2 = theMeshBase2.createTransactionNow();

        MeshObject a1_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a1" ), typeAA, now );
        MeshObject a2_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a2" ), typeAA, now );
        MeshObject a3_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a3" ), typeAA, now );
        // DIFFERENCE: no a4_mb2
        MeshObject a5_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a5" ), typeAA, now ); // DIFFERENCE

        a1_mb2.setPropertyValue( typeX, StringValue.create( "a1" ));
        a1_mb2.setPropertyValue( typeY, FloatValue.create( 123 )); // DIFFERENCE
        a2_mb2.setPropertyValue( typeX, StringValue.create( "a2" ));
        a3_mb2.setPropertyValue( typeX, StringValue.create( "a3" ));

        a2_mb2.relateAndBless( typeAR1A.getSource(), a1_mb2 ); // DIFFERENCE: switch source and destination
        a1_mb2.relateAndBless( typeAR1A.getSource(), a3_mb2 ); // DIFFERENCE: different external name
        a1_mb2.relateAndBless( typeAR1A.getSource(), a5_mb2 ); // DIFFERENCE: new relationship
        
        tx2.commitTransaction();

        //

        log.info( "now differencing" );

        IterableMeshBaseDifferencer diff = new IterableMeshBaseDifferencer( theMeshBase1 );

        ChangeSet theChangeSet = diff.determineChangeSet( theMeshBase2, false );
        Change [] theChanges = theChangeSet.getChanges();

        log.info( "found " + theChanges.length + " changes" );

        if( log.isInfoEnabled() ) {
            for( int i=0 ; i<theChanges.length ; ++i ) {
                Change current = theChanges[i];
                log.info( "    " + i + ": " + current );
            }
        }

        // now count

        Class [] changeTypes   = new Class[ theChanges.length ]; // this is more than we likely need
        int   [] changeNumbers = new int  [ theChanges.length ];

        for( int i=0 ; i<theChanges.length ; ++i ) {
            for( int j=0 ; j<changeTypes.length ; ++j ) {
                if( changeTypes[j] == null ) {
                    changeTypes[j] = theChanges[i].getClass();
                    changeNumbers[j] = 1;
                    break;
                }
                if( changeTypes[j] == theChanges[i].getClass() ) {
                    ++changeNumbers[j];
                    break;
                }
            }
        }
        for( int j=0 ; j<changeTypes.length ; ++j ) {
            if( changeTypes[j] == null ) {
                break;
            }
            log.info( "  Type " + changeTypes[j] + " -- " + changeNumbers[j] + " times" );
        }

        //

        log.info( "now applying changes" );

        Transaction tx = diff.getBaselineMeshBase().createTransactionNow();
        diff.applyChangeSet( theChangeSet );
        tx.commitTransaction();

        //

        ChangeSet theEmptyChangeSet = diff.determineChangeSet( theMeshBase2 );

        checkEquals( theEmptyChangeSet.getChanges().length, 0, "Change set not empty after applying differences" );

        log.info( "Empty ChangeSet is " + theEmptyChangeSet );
    }

    /**
     * Main program.
     *
     * @param args command-line arguments
     */
    public static void main(
            String [] args )
    {
        DifferencerTest8 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new DifferencerTest8( args );
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
     * @throws Exception all sorts of things may happen during a test
     */
    public DifferencerTest8(
            String [] args )
        throws
            Exception
    {
        super( DifferencerTest8.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( DifferencerTest8.class );
}
