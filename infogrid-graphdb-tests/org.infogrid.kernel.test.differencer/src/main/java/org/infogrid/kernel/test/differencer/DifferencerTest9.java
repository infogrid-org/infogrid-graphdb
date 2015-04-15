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
import org.infogrid.meshbase.IterableMeshBaseDifferencer;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.Change;
import org.infogrid.meshbase.transaction.ChangeSet;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.util.logging.Log;

/**
 * Tests the differencer with respect to EquivalenceSets.
 */
public class DifferencerTest9
        extends
            AbstractDifferencerTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    public void run()
        throws
            Exception
    {
        long now = 1122334455L;

        //

        log.info( "Creating MeshObjects in MeshBase1" );
        
        MeshBaseLifecycleManager life1 = theMeshBase1.getMeshBaseLifecycleManager();

        Transaction tx1 = theMeshBase1.createTransactionNow();

        MeshObject a1_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a1" ), typeAA, now );
        MeshObject a2_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a2" ), typeAA, now );
        MeshObject a3_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a3" ), typeAA, now );
        MeshObject a4_mb1 = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "a4" ), typeAA, now );

        MeshObject b_mb1  = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "b"  ), typeAA, now );
        MeshObject c_mb1  = createMeshObject( life1, theMeshBase1.getMeshObjectIdentifierFactory().fromExternalForm( "c"  ), typeAA, now );

        a1_mb1.relateAndBless( typeAR1A.getSource(), b_mb1 );
        a2_mb1.relateAndBless( typeAR1A.getSource(), c_mb1 );
        
        a1_mb1.addAsEquivalent( a2_mb1 );
        a2_mb1.addAsEquivalent( a3_mb1 );
        // no a4
        
        tx1.commitTransaction();

        //
        
        log.info( "Creating MeshObjects in MeshBase2" );
        
        MeshBaseLifecycleManager life2 = theMeshBase2.getMeshBaseLifecycleManager();

        Transaction tx2 = theMeshBase2.createTransactionNow();
        
        MeshObject a1_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a1" ), typeAA, now );
        MeshObject a2_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a2" ), typeAA, now );
        MeshObject a3_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a3" ), typeAA, now );
        MeshObject a4_mb2 = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "a4" ), typeAA, now );

        MeshObject b_mb2  = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "b"  ), typeAA, now );
        MeshObject c_mb2  = createMeshObject( life2, theMeshBase2.getMeshObjectIdentifierFactory().fromExternalForm( "c"  ), typeAA, now );

        a1_mb2.relateAndBless( typeAR1A.getSource(), b_mb2 );
        a2_mb2.relateAndBless( typeAR1A.getSource(), c_mb2 );
        
        a1_mb2.addAsEquivalent( a3_mb2 ); // different structure, but same semantics
        a3_mb2.addAsEquivalent( a4_mb2 );
        // new a4
        // and a2 is missing
        
        tx2.commitTransaction();

        //

        log.info( "now differencing" );

        IterableMeshBaseDifferencer diff = new IterableMeshBaseDifferencer( theMeshBase1 );

        ChangeSet theChangeSet = diff.determineChangeSet( theMeshBase2, false );
        Change [] theChanges = theChangeSet.getChanges();

        checkCondition( theChangeSet.size() > 0, "no changes found" );

        if( log.isInfoEnabled() ) {
            log.info( "found " + theChanges.length + " changes" );
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
        DifferencerTest9 test = null;
        try {
            if( args.length < 0 ) { // well, not quite possible but to stay with the general outline
                System.err.println( "Synopsis: <no arguments>" );
                System.err.println( "aborting ..." );
                System.exit( 1 );
            }

            test = new DifferencerTest9( args );
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
     * @throws Exception all sorts of things may go wrong in a test
     */
    public DifferencerTest9(
            String [] args )
        throws
            Exception
    {
        super( DifferencerTest9.class );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( DifferencerTest9.class );}
