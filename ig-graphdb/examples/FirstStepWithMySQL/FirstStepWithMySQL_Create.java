/*
 * Simple InfoGrid example application that tags websites.
 *
 * This app creates a persistent MeshBase, which delegates to MySQL.
 * It populates the MeshBase with some MeshObjects representing the
 * tag library, some tags, and some websites.
 * To read out the graph again, run FirstStepWithMySQL_Read.
 */

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.store.StoreMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.Tagging.TaggingSubjectArea;
import org.infogrid.model.Web.WebSubjectArea;
import org.infogrid.store.Store;
import org.infogrid.store.sql.mysql.MysqlStore;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class FirstStepWithMySQL_Create {

    public static void main( String[] args ) throws Throwable {
        
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName( "test" ); // name of the database

        MysqlStore store = MysqlStore.create( ds, "test" ); // name of the single table used in this example app
        store.initializeIfNecessary();

        MeshBase mb = StoreMeshBase.create( store );

        System.err.println( "Creating the persistent StoreMeshBase (MySQL)" );

        MeshObject tagLibrary = mb.findMeshObjectByIdentifier( mb.getMeshObjectIdentifierFactory().fromExternalForm( LIBRARY_ID ));
        if( tagLibrary != null ) {
            System.err.println( "ERROR: have library object already. Did you re-run this app without deleting the database content?" );
            System.err.println( "       Run something like \"echo drop table test | mysql test\" to get rid of the data." );
            System.exit( 1 );
        }

        Transaction tx = mb.createTransactionNow();

        tagLibrary = mb.getMeshBaseLifecycleManager().createMeshObject( mb.getMeshObjectIdentifierFactory().fromExternalForm( LIBRARY_ID ));
        tagLibrary.bless( TaggingSubjectArea.TAGLIBRARY );

        MeshObject tag_funny = mb.getMeshBaseLifecycleManager().createMeshObject( TaggingSubjectArea.TAG );
        MeshObject tag_good  = mb.getMeshBaseLifecycleManager().createMeshObject( TaggingSubjectArea.TAG );

        tag_funny.setPropertyValue( TaggingSubjectArea.TAG_LABEL, StringValue.create( "funny" ));
        tag_good.setPropertyValue(  TaggingSubjectArea.TAG_LABEL, StringValue.create( "good"  ));

        tagLibrary.relateAndBless( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource(), tag_funny );
        tagLibrary.relateAndBless( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource(), tag_good );

        MeshObject cnn = mb.getMeshBaseLifecycleManager().createMeshObject(
                mb.getMeshObjectIdentifierFactory().fromExternalForm( "http://cnn.com/" ), WebSubjectArea.WEBRESOURCE );
        MeshObject onion = mb.getMeshBaseLifecycleManager().createMeshObject(
                mb.getMeshObjectIdentifierFactory().fromExternalForm( "http://theonion.com/" ), WebSubjectArea.WEBRESOURCE );
        MeshObject xkcd = mb.getMeshBaseLifecycleManager().createMeshObject(
                mb.getMeshObjectIdentifierFactory().fromExternalForm( "http://xkcd.com/" ), WebSubjectArea.WEBRESOURCE );

        tag_good.relateAndBless( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource(),  cnn );
        tag_good.relateAndBless( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource(),  xkcd );
        tag_funny.relateAndBless( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource(), onion );
        tag_funny.relateAndBless( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource(), xkcd );

        tx.commitTransaction();

        mb.die();
    }
    final static String LIBRARY_ID = "library";
}
