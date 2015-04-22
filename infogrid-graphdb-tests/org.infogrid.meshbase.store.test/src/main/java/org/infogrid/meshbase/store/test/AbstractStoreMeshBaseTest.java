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

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseIdentifierFactory;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelBaseSingleton;
import org.infogrid.store.sql.AbstractSqlStore;
import org.infogrid.store.sql.mysql.MysqlStore;
import org.infogrid.testharness.AbstractTest;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;

/**
 * Factors out common functionality of StoreMeshBaseTests.
 */
public abstract class AbstractStoreMeshBaseTest
        extends
            AbstractTest
{
    /**
     * Constructor.
     * 
     * @param testClass the actual test class being run
     */
    public AbstractStoreMeshBaseTest(
            Class testClass )
    {
        super( localFileName( testClass, "/ResourceHelper" ));

        theDataSource = new MysqlDataSource();
        theDataSource.setDatabaseName( test_DATABASE_NAME );
        
        theSqlStore = MysqlStore.create( theDataSource, test_TABLE_NAME );
    }
    
    /**
     * The root context for these tests.
     */
    protected static final Context rootContext = SimpleContext.createRoot( "root-context" );

    /**
     * The ModelBase.
     */
    protected ModelBase theModelBase = ModelBaseSingleton.getSingleton();

    /**
     * The Database connection.
     */
    protected MysqlDataSource theDataSource;

    /**
     * The AbstractSqlStore to be tested.
     */
    protected AbstractSqlStore theSqlStore;

    /**
     * Factory for MeshBaseIdentifiers.
     */
    protected MeshBaseIdentifierFactory theMeshBaseIdentifierFactory = DefaultMeshBaseIdentifierFactory.create();

    /**
     * The name of the database that we use to store test data.
     */
    public static final String test_DATABASE_NAME = "test";

    /**
     * The name of the table that we use to store test data.
     */
    public static final String test_TABLE_NAME = "SqlStoreMeshBaseTest";

    /**
     * Holds the SQL driver.
     */

    static Object theSqlDriver;
    static {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            theSqlDriver = Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }        
    }
}
