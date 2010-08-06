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

package org.infogrid.modelbase.externalized.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.modelbase.MeshTypeLifecycleManager;
import org.infogrid.modelbase.MeshTypeNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.ModelLoader;
import org.infogrid.util.logging.Log;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
  * This class loads a model from an XML InputStream.
  */
public class XmlModelLoader
        extends
            ModelLoader
{
    private static final Log log = Log.getLogInstance(XmlModelLoader.class); // our own, private logger

    /**
     * Constructor.
     *
     * @param modelBase the ModelBase into which we load the model
     * @param s the stream that contains the meta-model in XML format
     * @param catalogClassLoader the ClassLoader from which we load the XML catalog
     * @param codeClassLoader the ClassLoader from which we load the code
     * @param errorPrefix a String with which to prefix error messages
     */
    public XmlModelLoader(
            ModelBase    modelBase,
            InputStream  s,
            ClassLoader  catalogClassLoader,
            ClassLoader  codeClassLoader,
            String       errorPrefix )
    {
        super( modelBase );

        if( s == null ) {
            throw new NullPointerException( "No stream for the Model XML file" );
        }

        theStream             = s;
        theCatalogClassLoader = catalogClassLoader;
        theCodeClassLoader    = codeClassLoader;
        theErrorPrefix        = errorPrefix;
    }

    /**
     * This is being called in order to tell this ModelLoader to instantiate its
     * model through the passed-in MeshTypeLifecycleManager.
     *
     * @param theInstantiator the MeshTypeLifecycleManager to use to instantiate the model
     * @return the instantiated SubjectAreas
     * @throws MeshTypeNotFoundException thrown if a referenced MeshType could not be resolved
     * @throws InheritanceConflictException thrown if there is a conflict in the inheritance hierarchy
     * @throws IOException thrown if an I/O error occurred
     */
    public SubjectArea [] loadModel(
            MeshTypeLifecycleManager theInstantiator )
        throws
            MeshTypeNotFoundException,
            InheritanceConflictException,
            IOException
    {
        synchronized( LOCK ) {
            if( theStream == null ) {
                throw new IOException( "no stream from XML file available" );
            }

            try {
                // initialize if we have not done so already
                if( theParser == null ) {
                    // The documentation says we are supposed to be doing this:
                    //      SAXParserFactory.newInstance();
                    // However, this "very smart" implementation tries to use the context class loader to load
                    // that class (and does not give us an option to specify a different class loader) and
                    // that won't work. So we cut through all the clutter and instantiate the thing directly:

                    SAXParserFactory fact = SAXParserFactory.newInstance(); // FIXME? new SAXParserFactoryImpl();
                    fact.setValidating( true );

                    theParser = fact.newSAXParser();
                }

                theHandler = new MyHandler( theInstantiator, theModelBase, theCatalogClassLoader, theErrorPrefix );
                theParser.parse( theStream, theHandler );
                SubjectArea [] ret = theHandler.instantiateExternalizedObjects( theCodeClassLoader );

                return ret;
                
            } catch( ParserConfigurationException ex ) {
                log.error( ex );
                return new SubjectArea[0]; // FIXME?

            } catch( SAXParseException ex ) {
                log.warn( "SAXParseException in line " + ex.getLineNumber() + ", column: " + ex.getColumnNumber(), ex );
                throw new FixedIOException( "Could not read model", ex );

            } catch( SAXException ex ) {
                log.warn( ex, ex );
                throw new FixedIOException( "Could not read model", ex );
            }
        }
    }

    /**
     * The InputStream from which we read the XML data.
     */
    protected InputStream theStream;

    /**
     * The ClassLoader from which we can find the DTD catalog, if any.
     */
    protected ClassLoader theCatalogClassLoader;

    /**
     * The ClassLoader through which the classes can be found that implement
     * the loaded meta-model.
     */
    protected ClassLoader theCodeClassLoader;

    /**
     * The error prefix to use in front of error messages (if any).
     */
    protected String theErrorPrefix;

    /**
     * Our callback handler for SAX callbacks.
     */
    protected MyHandler theHandler;

    /**
     * Static lock to avoid that we have more than one thread in this class (unlikely anyway).
     */
    protected static final Object LOCK = new Object();

    /**
     * We allocate only one XML parser etc.
     */
    protected static SAXParser theParser = null;

    /**
     * Java IOException's constructor is broken. This attempts to fix it.
     */
    public static class FixedIOException
            extends
                IOException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         * 
         * @param message error message, if any
         * @param cause underlying cause, if any
         */
        public FixedIOException(
                String    message,
                Throwable cause )
        {
            super( message );
            
            initCause( cause );
        }
    }
}
