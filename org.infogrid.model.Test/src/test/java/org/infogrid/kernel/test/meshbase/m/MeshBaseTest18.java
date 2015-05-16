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

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.Test.TestSubjectArea;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.util.logging.Log;
import org.junit.Test;

/**
 * Tests regular expressions on StringDataTypes.
 */
public class MeshBaseTest18
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
        MeshObject home = theMeshBase.getHomeObject();
        Transaction tx = theMeshBase.createTransactionNow();
        home.bless( TestSubjectArea.OPTIONALSTRINGREGEX );
        tx.commitTransaction();

        //

        PropertyValue [] validValues = {
                StringValue.create( "0.0.0.0" ),
                StringValue.create( "1.2.3.4" ),
                StringValue.create( "111.222.222.111" ),
                StringValue.create( "127.0.0.1" ),
                StringValue.create( "255.255.255.255" ),
        };
        PropertyValue [] invalidValues = {
                StringValue.create( "some string" ),
                StringValue.create( "1.2.3" ),
                StringValue.create( "1.2.3.4." ),
        };

        tx = theMeshBase.createTransactionNow();

        for( int i=0 ; i<validValues.length ; ++i ) {
            try {
                home.setPropertyValue( TestSubjectArea.OPTIONALSTRINGREGEX_OPTIONALSTRINGREGEXDATATYPE, validValues[i] );

            } catch( Throwable t ) {
                reportError( "Valid value " + i + " threw exception", t );
            }
        }
        for( int i=0 ; i<invalidValues.length ; ++i ) {
            try {
                home.setPropertyValue( TestSubjectArea.OPTIONALSTRINGREGEX_OPTIONALSTRINGREGEXDATATYPE, invalidValues[i] );
                reportError( "Invalid value " + i + " did not throw exception" );

            } catch( Throwable t ) {
                // that's right
            }
        }

        tx.commitTransaction();
    }

    // Our Logger
    private static Log log = Log.getLogInstance( MeshBaseTest18.class );
}
