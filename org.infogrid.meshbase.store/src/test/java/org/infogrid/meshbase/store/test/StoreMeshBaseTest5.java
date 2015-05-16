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
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSet;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Reproduces a StoreMeshBase integrity problem found 2007-06-22.
 */
public class StoreMeshBaseTest5
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
        theSqlStore.initializeHard();

        StoreMeshBase mb = StoreMeshBase.create(
                theMeshBaseIdentifierFactory.fromExternalForm( "mb" ),
                theModelBase,
                null,
                theSqlStore,
                rootContext );

        MeshBaseLifecycleManager life = mb.getMeshBaseLifecycleManager();

        //
        
        log.info( "Creating a few objects" );
        
        Transaction tx = mb.createTransactionNow();
        
        MeshObjectIdentifier extName = mb.getMeshObjectIdentifierFactory().fromExternalForm( "wsItEtOFGML7KyXCQ0slH6w+Jc9Tw5tY9+kc0TTlz8U=" );
        MeshObject obj = life.createMeshObject( extName, TestSubjectArea.AA );
        
        mb.getHomeObject().relateAndBless( TestSubjectArea.ARANY.getDestination(), obj );
        
        tx.commitTransaction();

        //
        
        log.info( "collecting garbage" );
        
        tx = null;
        obj = null;
        
        collectGarbage();
        
        //
        
        log.info( "checking everything's still there" );
        
        MeshObjectSet objs = mb.getHomeObject().traverseToNeighborMeshObjects();
        checkEquals( objs.size(), 1, "wrong number neighbors found" );

        RoleType [] rts = mb.getHomeObject().getRoleTypes( objs.getSingleMember() );
        checkEquals( rts.length, 1, "Wrong number of roles" );
        checkEquals( rts[0], TestSubjectArea.ARANY.getDestination(), "Wrong role" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( StoreMeshBaseTest5.class);
}
