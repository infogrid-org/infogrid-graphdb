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

package org.infogrid.meshbase.store.test;

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Rollback transaction does not leave stuff in the Store.
 */
public class StoreMeshBaseTest7
        extends
            AbstractStoreMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception thrown if an Exception occurred during the test
     */
    @Test
    public void run()
        throws
            Exception
    {
        log.info( "Deleting old database and creating new database" );

        theSqlStore.initializeHard();

        //

        log.info( "Creating MeshBase" );

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "MeshBase" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        checkEquals( theSqlStore.size(), 1, "Wrong number of entries in Store" );

        //

        MeshBaseLifecycleManager    life   = mb.getMeshBaseLifecycleManager();
        MeshObjectIdentifierFactory idfact = mb.getMeshObjectIdentifierFactory();

        log.info( "Creating Transaction and rolling it back" );

        Transaction tx = mb.createTransactionNow();

        MeshObject obj1 = life.createMeshObject( idfact.fromExternalForm( "obj1" ));
        MeshObject obj2 = life.createMeshObject( idfact.fromExternalForm( "obj2" ), TestSubjectArea.AA );
        obj2.setPropertyValue( TestSubjectArea.AA_Y, FloatValue.create( 1.2f ));

        tx.rollbackTransaction( null );

        //

        checkEquals( theSqlStore.size(), 1, "Wrong number of entries in Store after rolled-back Transaction" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest7.class );
}
