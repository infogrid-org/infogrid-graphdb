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
// Copyright 1998-2010 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.externalized.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.externalized.ExternalizedMeshObject;
import org.infogrid.mesh.externalized.ExternalizedMeshObjectEncoder;
import org.infogrid.mesh.externalized.ParserFriendlyExternalizedMeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.externalized.DecodingException;
import org.infogrid.model.primitives.externalized.EncodingException;
import org.infogrid.model.primitives.externalized.xml.PropertyValueXmlEncoder;
import org.infogrid.util.XmlUtils;
import org.infogrid.util.logging.Log;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Utility methods to encode/decode an ExternalizedMeshObject to/from XML. Implements the
 * SAX interface.
 */
public class ExternalizedMeshObjectXmlEncoder
        extends
            PropertyValueXmlEncoder
        implements
            ExternalizedMeshObjectEncoder,
            ExternalizedMeshObjectXmlTags
{
    private static final Log log = Log.getLogInstance( ExternalizedMeshObjectXmlEncoder.class ); // our own, private logger

    /**
     * Constructor.
     */
    public ExternalizedMeshObjectXmlEncoder()
    {
        // no op
    }

    /**
     * Serialize an ExternalizedMeshObject to an OutputStream.
     * 
     * @param obj the input ExternalizedMeshObject
     * @param out the OutputStream to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     * @throws IOException thrown if an I/O error occurred
     */
    public void encodeExternalizedMeshObject(
            ExternalizedMeshObject obj,
            OutputStream           out )
        throws
            EncodingException,
            IOException
    {
        StringBuilder buf = new StringBuilder();

        appendExternalizedMeshObject( obj, buf );

        OutputStreamWriter w = new OutputStreamWriter( out, ENCODING );
        w.write( buf.toString() );
        w.flush();
    }

    /**
     * Serialize an ExternalizedMeshObject to a StringBuilder.
     * 
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    public void appendExternalizedMeshObject(
            ExternalizedMeshObject obj,
            StringBuilder          buf )
        throws
            EncodingException
    {
        appendExternalizedMeshObject( obj, MESHOBJECT_TAG, buf );
    }

    /**
     * Serialize an ExternalizedMeshObject to a StringBuilder with an alternate XML top-level tag.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param meshObjectTagName the XML top-level tag to use for this ExternalizedMeshObject
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    public void appendExternalizedMeshObject(
            ExternalizedMeshObject obj,
            String                 meshObjectTagName,
            StringBuilder          buf  )
        throws
            EncodingException
    {
        encodeOpeningTag( obj, meshObjectTagName, buf  );

        encodeExternalizedMeshObjectTypes(       obj, buf );
        encodeExternalizedMeshObjectProperties(  obj, buf );
        encodeExternalizedMeshObjectNeighbors(   obj, buf );
        encodeExternalizedMeshObjectEquivalents( obj, buf );
        
        appendExternalizedMeshObjectEncodingHook( obj, buf  );

        buf.append( "</" ).append( meshObjectTagName ).append( ">\n" );
    }

    /**
     * Serialize the opening tag, to make it easy for subclasses to add to the attributes list.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param meshObjectTagName the XML top-level tag to use for this ExternalizedMeshObject
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    protected void encodeOpeningTag(
            ExternalizedMeshObject obj,
            String                 meshObjectTagName,
            StringBuilder          buf )
        throws
            EncodingException
    {
        buf.append( "<" );
        buf.append( meshObjectTagName );
        buf.append( " " );
        buf.append( IDENTIFIER_TAG );
        buf.append( "=\"" );
        appendIdentifier( obj.getIdentifier(), buf );
        buf.append( "\" " );
        buf.append( TIME_CREATED_TAG );
        buf.append( "=\"" );
        appendLong( obj.getTimeCreated(), buf );
        buf.append( "\" " );
        buf.append( TIME_UPDATED_TAG );
        buf.append( "=\"" );
        appendLong( obj.getTimeUpdated(), buf );
        buf.append( "\" " );
        buf.append( TIME_READ_TAG );
        buf.append( "=\"" );
        appendLong( obj.getTimeRead(), buf );
        buf.append( "\" " );
        buf.append( TIME_EXPIRES_TAG );
        buf.append( "=\"" );
        appendLong( obj.getTimeExpires(), buf );
        buf.append( "\">\n" );
    }

    /**
     * Serialize the types section.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    protected void encodeExternalizedMeshObjectTypes(
            ExternalizedMeshObject obj,
            StringBuilder          buf  )
        throws
            EncodingException
    {
        MeshTypeIdentifier [] allTypes = obj.getExternalTypeIdentifiers();
        if( allTypes != null ) {
            for( int i=0 ; i<allTypes.length ; ++i ) {
                buf.append( " " );
                buf.append( "<" );
                buf.append( TYPE_TAG );
                buf.append( ">" );
                appendIdentifier( allTypes[i], buf );
                buf.append( "</" );
                buf.append( TYPE_TAG );
                buf.append( ">\n" );
            }
        }
    }

    /**
     * Serialize the properties section.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    protected void encodeExternalizedMeshObjectProperties(
            ExternalizedMeshObject obj,
            StringBuilder          buf  )
        throws
            EncodingException
    {
        MeshTypeIdentifier [] allPropertyTypes  = obj.getPropertyTypes();
        PropertyValue []      allPropertyValues = obj.getPropertyValues();
        if( allPropertyTypes != null ) {
            for( int i=0 ; i<allPropertyTypes.length ; ++i ) {
                PropertyValue value        = allPropertyValues[i];

                buf.append( " " );
                buf.append( "<" );
                buf.append( PROPERTY_TYPE_TAG );
                buf.append( " " );
                buf.append( TYPE_TAG );
                buf.append( "=\"" );
                appendIdentifier( allPropertyTypes[i], buf );
                buf.append( "\">" );
                appendPropertyValue( value, buf );
                buf.append( "</" );
                buf.append( PROPERTY_TYPE_TAG );
                buf.append( ">\n" );
            }
        }
    }

    /**
     * Serialize the neighbors section.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    protected void encodeExternalizedMeshObjectNeighbors(
            ExternalizedMeshObject obj,
            StringBuilder          buf  )
        throws
            EncodingException
    {
        MeshObjectIdentifier [] neighbors = obj.getNeighbors();
        if( neighbors != null ) {
            for( int i=0 ; i<neighbors.length ; ++i ) {
                MeshObjectIdentifier  currentNeighbor  = neighbors[i];
                MeshTypeIdentifier [] currentRoleTypes = obj.getRoleTypesFor( currentNeighbor );
                buf.append( " <" );
                buf.append( RELATIONSHIP_TAG );
                buf.append( " " );
                buf.append( IDENTIFIER_TAG );
                buf.append( "=\"" );
                appendIdentifier( currentNeighbor, buf );
                buf.append( "\"" );
                if( currentRoleTypes == null || currentRoleTypes.length == 0 ) {
                    buf.append( "/>\n" );
                } else {
                    buf.append( ">\n" );
                    for( int j=0 ; j<currentRoleTypes.length ; ++j ) {
                        buf.append( "  <" );
                        buf.append( TYPE_TAG );
                        buf.append( ">" );
                        appendIdentifier( currentRoleTypes[j], buf );
                        buf.append( "</" );
                        buf.append( TYPE_TAG );
                        buf.append( ">\n" );
                    }
                    buf.append( " </" );
                    buf.append( RELATIONSHIP_TAG );
                    buf.append( ">\n" );
                }
            }
        }
    }

    /**
     * Serialize the equivalents section.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     * @throws EncodingException thrown if a problem occurred during encoding
     */
    protected void encodeExternalizedMeshObjectEquivalents(
            ExternalizedMeshObject obj,
            StringBuilder          buf  )
        throws
            EncodingException
    {
        // If we have this, we have to write even null values, because otherwise we can't distinguish right from left
        MeshObjectIdentifier [] equivalentsNames = obj.getEquivalents();
        if( equivalentsNames != null ) {
            for( int i=0 ; i<equivalentsNames.length ; ++i ) {
                buf.append( " <" );
                buf.append( EQUIVALENT_TAG );
                buf.append( ">" );
                appendIdentifier( equivalentsNames[i], buf );
                buf.append( "</" );
                buf.append( EQUIVALENT_TAG );
                buf.append( ">\n" );
            }
        }
    }

    /**
     * Hook to enable subclasses to add to the encoding of an ExternalizedMeshObject.
     *
     * @param obj the ExternalizedMeshObject to encode
     * @param buf the StringBuilder to which to append the ExternalizedMeshObject
     */
    protected void appendExternalizedMeshObjectEncodingHook(
            ExternalizedMeshObject obj,
            StringBuilder          buf )
    {
        // noop on this level
    }
    
    /**
     * Deserialize a ExternalizedMeshObject from a stream.
     * 
     * @param contentAsStream the byte [] stream in which the ExternalizedProxy is encoded
     * @param mb the NetMeshBase on whose behalf the decoding is performed
     * @return return the just-instantiated ExternalizedMeshObject
     * @throws DecodingException thrown if a problem occurred during decoding
     * @throws IOException thrown if an I/O error occurred
     */
    public synchronized ExternalizedMeshObject decodeExternalizedMeshObject(
            InputStream contentAsStream,
            MeshBase    mb )
        throws
            DecodingException,
            IOException
    {
        theMeshBase = mb;
        
        try {
            theParser.parse( contentAsStream, this );
            return theMeshObjectBeingParsed;

        } catch( SAXException ex ) {
            throw new DecodingException( ex );

        } finally {
            clearState();
        }
    }

    /**
     * Invoked when no previous start-element parsing rule has matched. Allows subclasses to add to parsing.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @param attrs the Attributes at this element
     * @throws SAXException thrown if a parsing error occurrs
     */
    @Override
    protected void startElement1(
            String     namespaceURI,
            String     localName,
            String     qName,
            Attributes attrs )
        throws
            SAXException
    {
        if( MESHOBJECT_TAG.equals( qName )) {
            theMeshObjectBeingParsed = theMeshBase.getMeshBaseLifecycleManager().createParserFriendlyExternalizedMeshObject();

            String identifier      = attrs.getValue( IDENTIFIER_TAG );
            String timeCreated     = attrs.getValue( TIME_CREATED_TAG );
            String timeUpdated     = attrs.getValue( TIME_UPDATED_TAG );
            String timeRead        = attrs.getValue( TIME_READ_TAG );
            String timeExpires     = attrs.getValue( TIME_EXPIRES_TAG );

            if( identifier != null ) {
                try {
                    theMeshObjectBeingParsed.setIdentifier(
                            theMeshBase.getMeshObjectIdentifierFactory().guessFromExternalForm( XmlUtils.descape( identifier )));
                } catch( ParseException ex ) {
                    error( ex );
                }
            }
            if( timeCreated != null && timeCreated.length() > 0 ) {
                theMeshObjectBeingParsed.setTimeCreated( Long.parseLong( timeCreated ));
            }
            if( timeUpdated != null && timeUpdated.length() > 0 ) {
                theMeshObjectBeingParsed.setTimeUpdated( Long.parseLong( timeUpdated ));
            }
            if( timeRead != null && timeRead.length() > 0 ) {
                theMeshObjectBeingParsed.setTimeRead( Long.parseLong( timeRead ));
            }
            if( timeExpires != null && timeExpires.length() > 0 ) {
                theMeshObjectBeingParsed.setTimeExpires( Long.parseLong( timeExpires ));
            } 
        } else if( TYPE_TAG.equals( qName )) {
            // no op
        } else if( PROPERTY_TYPE_TAG.equals( qName )) {
            String type = attrs.getValue( TYPE_TAG );

            if( type != null && type.length() > 0 ) {
                theMeshObjectBeingParsed.addPropertyType( theMeshBase.getModelBase().getMeshTypeIdentifierFactory().fromExternalForm( XmlUtils.descape( type )));
            } else {
                log.error( "empty '" + TYPE_TAG + "' on '" + PROPERTY_TYPE_TAG + "'" );
            }
        } else if( RELATIONSHIP_TAG.equals( qName )) {
            String identifier  = attrs.getValue( IDENTIFIER_TAG );
            long   updated     = parseLong( attrs, TIME_UPDATED_TAG, -1L );

            if( identifier != null ) {
                try {
                    theHasTypesBeingParsed = createRelationship(
                            theMeshObjectBeingParsed.getIdentifier(),
                            theMeshBase.getMeshObjectIdentifierFactory().guessFromExternalForm( XmlUtils.descape( identifier )),
                            updated );
                } catch( ParseException ex ) {
                    error( ex );
                }
            } else {
                log.error( "empty '" + IDENTIFIER_TAG + "' on '" + RELATIONSHIP_TAG + "'" );
            }

        } else if( EQUIVALENT_TAG.equals( qName )) {
            // no op
        } else {
            startElement2( namespaceURI, localName, qName, attrs );
        }
    }

    /**
     * Factors out the creation of Relationship instances, so subclasses can override it.
     * 
     * @param identifier the MeshObjectIdentifier on this side of the relationship
     * @param neighborIdentifier the MeshObjectIdentifier on the other side of the relationship
     * @param timeUpdated the time it was last updated
     * @return the created Relationship object
     */
    protected ParserFriendlyExternalizedMeshObject.Relationship createRelationship(
            MeshObjectIdentifier identifier,
            MeshObjectIdentifier neighborIdentifier,
            long                 timeUpdated )
    {
        return new ParserFriendlyExternalizedMeshObject.Relationship( identifier, neighborIdentifier, timeUpdated );
    }

    /**
     * Invoked when no previous start-element parsing rule has matched. Allows subclasses to add to parsing.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @param attrs the Attributes at this element
     * @throws SAXException thrown if a parsing error occurrs
     */
    protected void startElement2(
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
     * Invoked when no previous end-element parsing rule has matched. Allows subclasses to add to parsing.
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
            // no op        
        } else if( TYPE_TAG.equals( qName )) {
            // first the "inner" element if present
            if( theHasTypesBeingParsed != null ) {
                theHasTypesBeingParsed.addType(
                        theMeshBase.getModelBase().getMeshTypeIdentifierFactory().fromExternalForm( theCharacters.toString() ) );
            } else if( theMeshObjectBeingParsed != null ) {
                theMeshObjectBeingParsed.addMeshType(
                        theMeshBase.getModelBase().getMeshTypeIdentifierFactory().fromExternalForm( theCharacters.toString() ) );
            } else {
                log.error( "neither found" );
            }
        
        } else if( PROPERTY_TYPE_TAG.equals( qName )) {
            theMeshObjectBeingParsed.addPropertyValue( thePropertyValue );

        } else if( RELATIONSHIP_TAG.equals( qName )) {
            theMeshObjectBeingParsed.addRelationship( (ParserFriendlyExternalizedMeshObject.Relationship) theHasTypesBeingParsed );
            theHasTypesBeingParsed = null;

        } else if( EQUIVALENT_TAG.equals( qName )) {
            if( theCharacters != null ) {
                try {
                    theMeshObjectBeingParsed.addEquivalent(
                            theMeshBase.getMeshObjectIdentifierFactory().guessFromExternalForm( theCharacters.toString() ));
                } catch( ParseException ex ) {
                    error( ex );
                }
            }
            
        } else {
            endElement2( namespaceURI, localName, qName );
        }
    }

    /**
     * Invoked when no previous end-element parsing rule has matched. Allows subclasses to add to parsing.
     *
     * @param namespaceURI the URI of the namespace
     * @param localName the local name
     * @param qName the qName
     * @throws SAXException thrown if a parsing error occurrs
     */
    protected void endElement2(
            String namespaceURI,
            String localName,
            String qName )
        throws
            SAXException
    {
        log.error( "unknown qname " + qName );
    }

    /**
     * Reset the parser.
     */
    @Override
    public void clearState()
    {
        theMeshObjectBeingParsed = null;
        
        super.clearState();
    }

    /**
     * The MeshBase on whose behalf we work.
     */
    protected MeshBase theMeshBase;

    /**
     * The ExternalizedMeshObject that is currently being parsed, if any.
     */
    protected ParserFriendlyExternalizedMeshObject theMeshObjectBeingParsed = null;
    
    /**
     * The Relationship that is currently being parsed, if any.
     */
    protected ParserFriendlyExternalizedMeshObject.HasTypes theHasTypesBeingParsed = null;
}
