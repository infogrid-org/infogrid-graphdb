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
// Copyright 1998-2009 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives.text;

import java.util.HashMap;
import java.util.Map;
import org.infogrid.util.text.SimpleStringRepresentation;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationDirectory;
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

        StringRepresentation plain     = theSingleton.get( TEXT_PLAIN_NAME );
        StringRepresentation editPlain = theSingleton.get( EDIT_TEXT_PLAIN_NAME );
        StringRepresentation html      = theSingleton.get( TEXT_HTML_NAME );
        StringRepresentation editHtml  = theSingleton.get( EDIT_TEXT_HTML_NAME );

        Map<String,Stringifier<? extends Object>> plainMap     = plain.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> editPlainMap = editPlain.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> htmlMap      = html.getLocalStringifierMap();
        Map<String,Stringifier<? extends Object>> editHtmlMap  = editHtml.getLocalStringifierMap();

        Map<String,Stringifier<? extends Object>> javadocMap = new HashMap<String,Stringifier<? extends Object>>();
        Map<String,Stringifier<? extends Object>> javaMap    = new HashMap<String,Stringifier<? extends Object>>();

    // old tags for new StringRepresentations

        // int
        // javadoc: same as html
        // java: same as plain

        // int2
        // javadoc: same as html
        // java: same as plain

        // int4
        // javadoc: same as html
        // java: same as plain

        // float
        // javadoc: same as html
        javaMap.put(      "float",            JavaFloatStringifier.create() );

        // double
        // javadoc: same as html
        javaMap.put(      "double",           JavaDoubleStringifier.create() );

        // string
        javadocMap.put(   "string",           JavadocHtmlStringStringifier.create() );
        javaMap.put(      "string",           JavaStringStringifier.create() );

        // stacktrace
        // javadoc: same as html
        // java: same as plain

        // urlappend
        // javadoc: same as html
        // java: same as plain

        // list
        // javadoc: same as html
        // java: FIXME?

        // new tags for all

        // blob
        editHtmlMap.put(  "mimeoptions",      BlobMimeOptionsHtmlStringifier.create( "<option>", "<option selected=\"selected\">", "\n", "</option>", "</option>" ));
        editHtmlMap.put(  "mimelist",         BlobMimeOptionsHtmlStringifier.create( null, null, ",", null, " (selected)" ));
        
        // enum
        plainMap.put(     "enum",             EnumeratedValueStringifier.create( true ) );
        editPlainMap.put( "enum",             EnumeratedValueStringifier.create( true ) );
        htmlMap.put(      "enum",             EnumeratedValueStringifier.create( true ) );
        editHtmlMap.put(  "enum",             EnumeratedValueStringifier.create( true ) );

        // enumdomain -- render the domain of an EnumeratedDataType
        plainMap.put(     "enumdomain",       EnumeratedDataTypeDomainStringifier.create( EnumeratedValueStringifier.create( true ), ", " ));
        // does not exist in plain / edit
        htmlMap.put(      "enumdomain",       EnumeratedDataTypeDomainStringifier.create( EnumeratedValueStringifier.create( true ), "<li>", "</li><li>", "</li>" ));
        // does not exist in html / edit

        // enumchoice -- render the domain of an EnumeratedValue, with the given EnumeratedValue being selected
        // does not exist in plain / non-edit
        // does not exist in plain / edit
        // does not exist in html / non-edit
        editHtmlMap.put(  "enumchoice",       EnumeratedValueChoiceHtmlStringifier.create( "option" ));

        // multiplicity
        plainMap.put(     "multiplicity",     MultiplicityValueStringStringifier.create() );
        editPlainMap.put( "multiplicity",     MultiplicityValueStringStringifier.create() );
        // html: same as plain
        // javadoc: same as html
        javaMap.put(      "multiplicity",     JavaPropertyValueStringifier.create());

        // esacpe hash
        plainMap.put(     "escapehashstring", EscapeHashHtmlStringStringifier.create() );
        editPlainMap.put( "escapehashstring", EscapeHashHtmlStringStringifier.create() );
        // html: same as plain
        // javadoc: same as html
        // java: same as plain

        // DataType
        plainMap.put(     "type",             DataTypeStringifier.create() );
        editPlainMap.put( "type",             DataTypeStringifier.create() );
        // html: same as plain
        // javadoc: same as html
        // java: same as plain

        // PropertyValue raw
        plainMap.put(     "pv",               PropertyValueStringifier.create( plain, null ) );
        editPlainMap.put( "pv",               PropertyValueStringifier.create( plain, null ) );
        // html: same as plain
        // javadoc: same as html
        javaMap.put(      "pv",               JavaPropertyValueStringifier.create());


        SimpleStringRepresentation javadoc = SimpleStringRepresentation.create(
                theSingleton,
                StringRepresentationDirectory.TEXT_JAVADOC_NAME,
                javadocMap,
                html );
        SimpleStringRepresentation java = SimpleStringRepresentation.create(
                theSingleton,
                StringRepresentationDirectory.TEXT_JAVA_NAME,
                javaMap,
                plain );

        // after the fact due to dependency

        theSingleton.put( javadoc.getName(), javadoc  );
        theSingleton.put(    java.getName(), java  );
    }

    /**
     * Has this class been initialized.
     */
    protected static boolean theIsInitialized = false;
}
