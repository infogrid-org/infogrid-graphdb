/*
 * Simple InfoGrid example application that tags websites.
 *
 * This app creates an in-memory MeshBase, with no persistence.
 * It populates the MeshBase with some MeshObjects representing the
 * tag library, some tags, and some websites. It then traverses
 * the resulting graph and prints out what it finds.
 */

import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.m.MMeshBase;
import org.infogrid.meshbase.transaction.Transaction;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.Tagging.TaggingSubjectArea;
import org.infogrid.model.Web.WebSubjectArea;

public class FirstStep {

    public static void main( String[] args ) throws Throwable {
        MeshBase mb = MMeshBase.create();

        Transaction tx = mb.createTransactionNow();

        MeshObject tagLibrary = mb.getMeshBaseLifecycleManager().createMeshObject();
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

        // Here are some example traversals:
        
        System.err.println( "All tags in the tag library, and the sites they tag:" );
        for( MeshObject tag : tagLibrary.traverse( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource() )) {
            System.err.println( "    " + tag.getPropertyValue( TaggingSubjectArea.TAG_LABEL ));

            for( MeshObject site : tag.traverse( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource() )) {
                System.err.println( "        " + site.getIdentifier().toExternalForm() );
            }
        }

        System.err.println( "Xkcd is tagged by:" );
        for( MeshObject tag : xkcd.traverse( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getDestination() )) {
            System.err.println( "    " + tag.getPropertyValue( TaggingSubjectArea.TAG_LABEL ));
        }

        System.err.println( "All tagged sites (note there are no duplicates: set semantics)" );
        for( MeshObject site : tagLibrary.traverse( TaggingSubjectArea.TAGLIBRARY_COLLECTS_TAG.getSource() ).traverse( TaggingSubjectArea.TAG_TAGS_MESHOBJECT.getSource() )) {
            System.err.println( "    " + site.getIdentifier().toExternalForm() );
        }
    }
}
