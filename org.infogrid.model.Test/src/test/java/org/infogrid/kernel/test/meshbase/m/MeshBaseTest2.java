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

import java.lang.ref.WeakReference;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.IsAbstractException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotBlessedException;
import org.infogrid.mesh.TypedMeshObjectFacade;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshBaseLifecycleManager;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.B;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * This tests the blessing of MeshObjects, unblessing, and Facade-based access. This does not test
 * relationships between MeshObjects.
 */
public class MeshBaseTest2
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
        log.info( "Trying to instantiate abstract MeshType" );
        
        MeshBaseLifecycleManager life = theMeshBase.getMeshBaseLifecycleManager();

        Transaction tx = theMeshBase.createTransactionNow();
        try {
            MeshObject obj = life.createMeshObject( TestSubjectArea.A );
            
            reportError( "Should not be able to instantiate abstract type" );
        } catch( IsAbstractException ex ) {
            // this is good
        }
        tx.commitTransaction();
        
        //
        
        log.info( "Creating MeshObject" );
        
        life = theMeshBase.getMeshBaseLifecycleManager();

        tx = theMeshBase.createTransactionNow();

        MeshObject obj = life.createMeshObject( TestSubjectArea.AA );
        
        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.AA }, "not blessed with the same type" );

        tx.commitTransaction();
        
        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.AA }, "not blessed with the same type" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.A_X  ), null,                       "Property X not initialized to null" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create( 12.34 ), "Property Y not initialized to correct value" );
        
        //
        
        log.info( "Blessing MeshObject" );
        
        tx = theMeshBase.createTransactionAsap();
        
        obj.bless( TestSubjectArea.B );

        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.AA, TestSubjectArea.B }, "not blessed with the same type" );
        
        tx.commitTransaction();

        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.AA, TestSubjectArea.B }, "not blessed with the same type" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.A_X  ), null, "Property X wrong" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.AA_Y ), FloatValue.create( 12.34 ), "Property Y wrong" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.B_Z  ), TestSubjectArea.B_Z_type.select( "Value2" ), "Property Z not initialized to correct value" );
        checkEquals( obj.getPropertyValue( TestSubjectArea.B_U  ), null, "Property U not initialized to null" );

        // 
        
        log.info( "Unblessing MeshObject" );
        
        tx = theMeshBase.createTransactionAsap();

        obj.unbless( TestSubjectArea.AA );
        
        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.B }, "not blessed with the same type" );

        tx.commitTransaction();
        
        checkEqualsOutOfSequence( obj.getTypes(), new EntityType[] { TestSubjectArea.B }, "not blessed with the same type" );

        //
        
        log.info( "Making sure we cannot unbless what isn't blessed" );
        
        tx = theMeshBase.createTransactionAsap();

        try {
            obj.unbless( TestSubjectArea.AA );
            
            reportError( "Unblessing unblessed object should not have worked" );
        } catch( NotBlessedException ex ) {
            
        }
        tx.commitTransaction();

        //
        
        log.info( "Making sure we cannot access properties of the unblessed type" );
        
        try {
            PropertyValue v = obj.getPropertyValue( TestSubjectArea.AA_Y );
            reportError( "Accessing property of a non-blessed object should throw exception" );

        } catch( IllegalPropertyTypeException ex ) {
            // good
        }

        tx = theMeshBase.createTransactionAsap();
        try {
            obj.setPropertyValue( TestSubjectArea.A_X, StringValue.create( "wrong value" ) );
            reportError( "Accessing property of a non-blessed object should throw exception" );

        } catch( IllegalPropertyTypeException ex ) {
            // good
        }
        tx.commitTransaction();
        
        //
        
        log.info( "Checking making of Facades" );
        
        TypedMeshObjectFacade facadeB = obj.getTypedFacadeFor( TestSubjectArea.B );
        
        checkObject( facadeB, "facade is null" );
        checkEquals( facadeB.get_Delegate(), obj, "facade does not point back to MeshObject" );
        
        TypedMeshObjectFacade facadeBB = obj.getTypedFacadeFor( TestSubjectArea.B );
        
        checkEquals( facadeB, facadeBB, "Facade factory method not working" );
        
        //
        
        log.info( "Accessing object via facade" );

        tx = theMeshBase.createTransactionAsap();

        B b = (B) facadeB;
        b.setU( StringValue.create( "Name 1" ));
        b.setU( StringValue.create( "Name 2" ));
        
        tx.commitTransaction();

        checkEquals( "Name 2", b.getU().value(), "wrong name value" );
        checkEquals( "Name 2", ((StringValue)obj.getPropertyValue( TestSubjectArea.B_U )).value(), "wrong name value" );

        tx = theMeshBase.createTransactionAsap();

        obj.setPropertyValue( TestSubjectArea.B_U, StringValue.create( "Name 3" ));

        tx.commitTransaction();

        checkEquals( "Name 3", b.getU().value(), "wrong name value" );
        checkEquals( "Name 3", ((StringValue)obj.getPropertyValue( TestSubjectArea.B_U )).value(), "wrong name value" );

        //
        
        WeakReference<TypedMeshObjectFacade> facadeBRef = new WeakReference<TypedMeshObjectFacade>( facadeB );
        facadeB  = null;
        facadeBB = null;
        b        = null;

        super.collectGarbage();
        
        checkCondition( facadeBRef.get() == null, "facade has not been garbage-collected" );
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest2.class);
}
