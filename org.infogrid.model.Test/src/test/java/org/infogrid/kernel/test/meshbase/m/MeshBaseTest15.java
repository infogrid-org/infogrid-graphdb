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

package org.infogrid.kernel.test.meshbase.m;

import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Make sure Properties go away when their EntityType is unblessed.
 */
public class MeshBaseTest15
        extends
            AbstractSingleMeshBaseTest
{
    /**
     * Run the test.
     *
     * @throws Exception all sorts of things may go wrong during a test.
     */
    @Test
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

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest15.class);
}
