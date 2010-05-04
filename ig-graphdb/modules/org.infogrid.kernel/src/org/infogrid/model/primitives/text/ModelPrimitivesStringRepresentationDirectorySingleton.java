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

package org.infogrid.model.primitives.text;

import java.util.Map;
import org.infogrid.util.text.HtmlifyingDelegatingStringifier;
import org.infogrid.util.text.InvalidStringifier;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationDirectorySingleton;
import org.infogrid.util.text.Stringifier;

/**
 * Extends StringRepresentationDirectorySingleton with the appropriate additional definitions
 * useful in an InfoGrid kernel context.
 */
public class ModelPrimitivesStringRepresentationDirectorySingleton
        extends
            StringRepresentationDirectorySingleton
{
    /**
     * Private constructor, use static singleton methods.
     */
    protected ModelPrimitivesStringRepresentationDirectorySingleton()
    {
        super();
    }

    /**
     * Initialize. This needs to be called to add the local definition to the defaults set in
     * the org.infogrid.util module.
     */
    public static void initialize()
    {
        if( theIsInitialized ) {
            return;
        }
        instantiateDefaultSingleton();

        // what we have s0 far
        StringRepresentation plain      = theSingleton.get( TEXT_PLAIN_NAME );
        StringRepresentation editPlain  = theSingleton.get( EDIT_TEXT_PLAIN_NAME );
        StringRepresentation html       = theSingleton.get( TEXT_HTML_NAME );
        StringRepresentation editHtml   = theSingleton.get( EDIT_TEXT_HTML_NAME );
        StringRepresentation url        = theSingleton.get( TEXT_URL_NAME );
        StringRepresentation java       = theSingleton.get( TEXT_JAVA_NAME );
        StringRepresentation javadoc    = theSingleton.get( TEXT_JAVADOC_NAME );
        StringRepresentation javascript = theSingleton.get( TEXT_JAVASCRIPT_NAME );

        Map<String,Stringifier<? extends Object>> plainMap      = plain.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> editPlainMap  = editPlain.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> htmlMap       = html.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> editHtmlMap   = editHtml.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> urlMap        = url.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> javaMap       = java.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> javadocMap    = javadoc.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> javascriptMap = javascript.getLocalStringifierMap();

    // blob

        plainMap.put(     "mimelist",         BlobMimeOptionsStringifier.create( null,       null,                             ",",  null,        " (selected)" ));
        // editPlain:  same as plain
        // html:       same as plain
        // editHtml:   same as html
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java

        // not plain
        // editPlain:  same as plain
        editHtmlMap.put(  "mimeoptions",      BlobMimeOptionsStringifier.create( "<option>", "<option selected=\"selected\">", "\n", "</option>", "</option>" ));
        // editHtml:   same as html
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java
        
    // enum

        plainMap.put(     "enum",             EnumeratedValueStringifier.create( true ) );
        // editPlain:  same as plain
        htmlMap.put(      "enum",             HtmlifyingDelegatingStringifier.create( EnumeratedValueStringifier.create( true ) ));
        // editHtml:   same as html
        urlMap.put(       "enum",             EnumeratedValueStringifier.create( false ) );
        javaMap.put(      "enum",             EnumeratedValueStringifier.create( false ) );
        // javascript: same as java
        // javadoc:    same as java

    // enumdomain -- render the domain of an EnumeratedDataType

        plainMap.put(     "enumdomain",       EnumeratedDataTypeDomainStringifier.create( EnumeratedValueStringifier.create( true ), ", " ));
        editPlainMap.put( "enumdomain",       InvalidStringifier.create() );
        htmlMap.put(      "enumdomain",       EnumeratedDataTypeDomainStringifier.create( HtmlifyingDelegatingStringifier.create( EnumeratedValueStringifier.create( true )), "<li>", "</li><li>", "</li>" ));
        editHtmlMap.put(  "enumdomain",       InvalidStringifier.create() );
        urlMap.put(       "enumdomain",       InvalidStringifier.create() );
        javaMap.put(      "enumdomain",       EnumeratedDataTypeDomainStringifier.create( EnumeratedValueStringifier.create( false ), ", " ));
        // javascript: same as java
        // javadoc:    same as java

    // enumchoice -- render the domain of an EnumeratedValue, with the given EnumeratedValue being selected
        
        plainMap.put(     "enumchoice",       InvalidStringifier.create() );
        // editPlain:  same as plain
        // html:       same as plain
        editHtmlMap.put(  "enumchoice",       EnumeratedValueChoiceHtmlStringifier.create( "option" ));
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java

    // identifiers in a URL

        plainMap.put(     "idInWebContext",   WebContextAwareMeshObjectIdentifierStringifier.create( true ) );
        // editPlain:  same as plain
        htmlMap.put(      "idInWebContext",   HtmlifyingDelegatingStringifier.create( WebContextAwareMeshObjectIdentifierStringifier.create( true ) ));
        // editHtml:   same as html
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java

    // short identifiers

        plainMap.put(     "shortId",          WebContextAwareMeshObjectIdentifierStringifier.create( false ) );
        // editPlain:  same as plain
        htmlMap.put(      "shortId",          HtmlifyingDelegatingStringifier.create( WebContextAwareMeshObjectIdentifierStringifier.create( false ) ));
        // editHtml:   same as html
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java

    // DataType

        plainMap.put(     "type",             DataTypeStringifier.create() );
        // editPlain:  same as plain
        // editHtml:   same as html
        // url:        same as plain
        // java:       same as plain
        // javascript: same as java
        // javadoc:    same as java

    // PropertyValue raw

        plainMap.put(     "pv",               PropertyValueStringifier.create( plain ) );
        // editPlain:  same as plain
        htmlMap.put(      "pv",               PropertyValueStringifier.create( html ) );
        // editHtml:   same as html
        // url:        same as plain
        javaMap.put(      "pv",               JavaPropertyValueStringifier.create());
        javascriptMap.put("pv",               PropertyValueStringifier.create( javascript ) );
        javadocMap.put(   "pv",               PropertyValueStringifier.create( javadoc ) );
    }

    /**
     * Has this class been initialized.
     */
    protected static boolean theIsInitialized = false;
}
