/*
 * Simple InfoGrid example application that tags websites.
 *
 * This app reads out the graph again created with FirstStepWithMySQL_Create.
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

public class FirstStepWithMySQL_Read {

    public static void main( String[] args ) throws Throwable {
        
        MysqlDataSource ds = new MysqlDataSource();
        ds.setDatabaseName( "test" ); // name of the database

        MysqlStore store = MysqlStore.create( ds, "test" ); // name of the single table used in this example app

        MeshBase mb = StoreMeshBase.create( store );

        System.err.println( "Reading from the persistent StoreMeshBase (MySQL)" );

        MeshObject tagLibrary = mb.findMeshObjectByIdentifier( mb.getMeshObjectIdentifierFactory().fromExternalForm( LIBRARY_ID ));
        if( tagLibrary == null ) {
            System.err.println( "Error: Cannot find the tagLibrary object. Aborting." );
            System.exit( 1 );
        }
        // Here are some example traversals:
        
        System.err.println( "All tags in the tag library, and the sites they tag:" );
        for( MeshObject tag : tagLibrary.traverse( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource() )) {
            System.err.println( "    " + tag.getPropertyValue( TaggingSubjectArea.TAG_LABEL ));

            for( MeshObject site : tag.traverse( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource() )) {
                System.err.println( "        " + site.getIdentifier().toExternalForm() );
            }
        }

        System.err.println( "All tagged sites (note there are no duplicates: set semantics)" );
        for( MeshObject site : tagLibrary.traverse( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource() ).traverse( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource() )) {
            System.err.println( "    " + site.getIdentifier().toExternalForm() );
        }
    }
    final static String LIBRARY_ID = "library";
}
