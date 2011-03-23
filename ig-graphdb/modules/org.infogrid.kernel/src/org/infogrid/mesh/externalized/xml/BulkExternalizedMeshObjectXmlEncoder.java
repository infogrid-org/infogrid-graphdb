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

package org.infogrid.mesh.externalized.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.externalized.ParserFriendlyExternalizedMeshObject;
import org.infogrid.meshbase.BulkLoadException;
import org.infogrid.meshbase.BulkLoader;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.externalized.EncodingException;
import org.infogrid.util.logging.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses a stream of AMeshObjects.
 */
public class BulkExternalizedMeshObjectXmlEncoder
        extends
            ExternalizedMeshObjectXmlEncoder
        implements
            BulkLoader,
            BulkExternalizedMeshObjectXmlTags
{
    private static final Log log = Log.getLogInstance( BulkExternalizedMeshObjectXmlEncoder.class ); // our own, private logger

    /**
     * Constructor.
     */
    public BulkExternalizedMeshObjectXmlEncoder()
    {
        // noop
    }

    /**
     * Bulk-load data into this MeshBase.
     *
     * @param inStream the Stream from which to read the data
     * @param mb the MeshBase on whose behalf the loading is performed
     * @return the iterator over the loaded ExternalizedMeshObjects
     * @throws IOException thrown if an I/O error occurred
     * @throws BulkLoadException thrown if a loading exception occurred, for the details check the cause
     */
    public Iterator<? extends ExternalizedMeshObject> bulkLoad(
            InputStream inStream,
            MeshBase    mb )
        throws
            IOException,
            BulkLoadException
    {
        theMeshBase = mb;

        try {
            synchronized( theParser ) {
                theParser.parse( inStream, this );
            }
        
            Collection<ParserFriendlyExternalizedMeshObject> parsed = getParsedExternalizedMeshObjects();
            return parsed.iterator();

        } catch( SAXException ex ) {
            throw new BulkLoadException( ex );

        } finally {
            clearState();
        }
    }

    /**
     * Addition to parsing.
     *
     * @param namespaceURI URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @param attrs the Attributes at this element
     * @throws SAXException thrown if a parsing error occurrs
     */
    @Override
    protected void startElement2(
            String     namespaceURI,
            String     localName,
            String     qName,
            Attributes attrs )
        throws
            SAXException
    {        
        if( BULK_IMPORT_TAG.equals( qName )) {
            // noop
        } else {
            startElement3( namespaceURI, localName, qName, attrs );
        }
    }

    /**
     * Allows subclasses to add to parsing.
     *
     * @param namespaceURI URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @param attrs the Attributes at this element
     * @throws SAXException thrown if a parsing error occurrs
     */
    protected void startElement3(
            String     namespaceURI,
            String     localName,
            String     qName,
            Attributes attrs )
        throws
            SAXException
    {
        log.error( "unknown qname " + qName );
    }

    /**
     * Overrides parsing behavior of the superclass.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @throws SAXException thrown if a parsing error occurrs
     */
    @Override
    protected void endElement1(
            String namespaceURI,
            String localName,
            String qName )
        throws
            SAXException
    {
        if( MESHOBJECT_TAG.equals( qName )) {
            theParsedExternalizedMeshObjects.add( theMeshObjectBeingParsed );
            theMeshObjectBeingParsed = null;

        } else {
            super.endElement1( namespaceURI, localName, qName );
        }
    }

    /**
     * Addition to parsing.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @throws SAXException thrown if a parsing error occurrs
     */
    @Override
    protected void endElement2(
            String namespaceURI,
            String localName,
            String qName )
        throws
            SAXException
    {
        if( BULK_IMPORT_TAG.equals( qName )) {
            // noop
        } else {
            endElement3( namespaceURI, localName, qName );
        }
    }

    /**
     * Allows subclasses to add to parsing.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @throws SAXException thrown if a parsing error occurrs
     */
    protected void endElement3(
            String namespaceURI,
            String localName,
            String qName )
        throws
            SAXException
    {
        log.error( "unknown qname " + qName );
    }

    /**
     * Obtain the collection of objects that we have parsed.
     *
     * @return the collection
     */
    public final Collection<ParserFriendlyExternalizedMeshObject> getParsedExternalizedMeshObjects()
    {
        return theParsedExternalizedMeshObjects;
    }

    /**
     * Reset the parser.
     */
    @Override
    public void clearState()
    {
        theParsedExternalizedMeshObjects = new ArrayList<ParserFriendlyExternalizedMeshObject>(); // don't clear, that will interfere with returning those
        
        super.clearState();
    }

    /**
     * Bulk write the set of MeshObjects returned by an iterator.
     *
     * @param iter the iterator over the MeshObjects
     * @param out the OutputStream to write to
     * @throws IOException thrown if an I/O error occurred
     * @throws EncodingException thrown if an Encoding problem occurred
     */
    public void bulkWrite(
            Iterator<MeshObject> iter,
            OutputStream         out )
        throws
            IOException,
            EncodingException
    {
        try {
            out.write( ( "<" + BULK_IMPORT_TAG + ">\n" ).getBytes( ENCODING ));

        } catch( UnsupportedEncodingException ex ) {
            log.error( ex );
        }

        while( iter.hasNext() ) {
            MeshObject current = iter.next();
            
            encodeExternalizedMeshObject( current.asExternalized(), out );
        }

        try {
            out.write( ( "</" + BULK_IMPORT_TAG + ">" ).getBytes( ENCODING ));

        } catch( UnsupportedEncodingException ex ) {
            log.error( ex );
        }
    }

    /**
     * The set of parsed ExternalizedMeshObjects that we have found so far.
     */
    protected ArrayList<ParserFriendlyExternalizedMeshObject> theParsedExternalizedMeshObjects
            = new ArrayList<ParserFriendlyExternalizedMeshObject>();
}
