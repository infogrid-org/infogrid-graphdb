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

package org.infogrid.modelbase.externalized.xml;

import java.util.HashMap;

/**
 * Collects the token definitions for the XmlModelLoader and XmlModelExporter.
 */
abstract class XmlModelTokens
{
    /**
     * Private no-op constructor to keep this private.
     */
    private XmlModelTokens()
    {}

    /**
     * This is the public URL at which we find the model DTD.
     */
    public static final String PUBLIC_MODEL_DTD_URL = "http://infogrid.org/dtds/model.dtd";

    /**
     * The publicId of the model DTD.
     */
    public static final String PUBLIC_ID_DTD = "-//InfoGrid.org//InfoGrid Model//EN";

    /**
     * This is the relative location inside the JAR at which we find the same model DTD.
     */
    public static final String LOCAL_MODEL_DTD_PATH = "org/infogrid/modelbase/externalized/xml/model.dtd";

    /**
     * Internal helper to obtain the token code from a keyword.
     *
     * @param namespaceUri the URI of the namespace, if any
     * @param keyword the keyword from which to obtain a token code
     * @return the token code, or -1 if not found
     */
    static int getTokenFromKeyword(
            String namespaceUri,
            String keyword )
    {
        StringBuilder key = new StringBuilder();
        if( namespaceUri != null && namespaceUri.length() > 0 ) {
            key.append( namespaceUri ).append( ":" );
        }
        key.append( keyword );
        Integer almostRet = theTokenMap.get( key.toString() );
        if( almostRet != null ) {
            return almostRet.intValue();
        }
        return -1;
    }

    /**
     * Internal helper to obtain the keyword from the token code.
     *
     * @param token the token
     * @return the keyword, or null if not found
     */
    static String getKeywordFromToken(
            int token )
    {
        if( token >=0 && token < theTokenTable.length ) {
            return theTokenTable[ token ];
        }
        return null;
    }

    /**
     * Helper method to add the content to the tables.
     *
     * @param keyword the keyword for which to add the token
     * @param token the token for this keyword
     */
    private static void addToTable(
            String keyword,
            int    token )
    {
        theTokenMap.put( keyword, new Integer( token ));
        if( theTokenTable[ token ] == null ) {
           theTokenTable[ token ] = keyword;
        } else {
            throw new InternalError( "token " + token + " in table already" ); // paranoia
        }
    }

    /**
     * The tokens that go with our set of keywords, listed here and inserted into a table.
     *
     * One would really like to take a counter here, but then, we can't use the static finals as
     * labels in a switch/case statement any more.
     */
    protected static final int MODEL_TOKEN                                   =  0;
    protected static final int SUBJECT_AREA_TOKEN                            =  1;
    protected static final int DEPENDSON_TOKEN                               =  2;
    protected static final int SUBJECT_AREA_REFERENCE_TOKEN                  =  3;
    // protected static final int MODULE_REFERENCE_TOKEN                        =  4;
    protected static final int ENTITY_TYPE_TOKEN                             =  5;

    protected static final int RELATIONSHIP_TYPE_TOKEN                       =  6;
    protected static final int PROPERTY_TYPE_TOKEN                           =  7;
    protected static final int PROJECTED_PROPERTY_TYPE_TOKEN                 =  8;
    protected static final int PROPERTY_TYPE_GROUP_TOKEN                     =  9;
    protected static final int PROPERTY_TYPE_GROUP_MEMBER_TOKEN              = 10;
    protected static final int INPUT_PROPERTY_SPECIFICATION_TOKEN            = 11;
    protected static final int TRAVERSAL_SPECIFICATION_TOKEN                 = 12;
    protected static final int ALTERNATIVE_TRAVERSAL_SPECIFICATION_TOKEN     = 13;
    protected static final int SEQUENTIAL_TRAVERSAL_SPECIFICATION_TOKEN      = 14;
    protected static final int SELECTIVE_TRAVERSAL_SPECIFICATION_TOKEN       = 15;
    protected static final int STAY_RIGHT_HERE_TRAVERSAL_SPECIFICATION_TOKEN = 16;
    protected static final int ALL_NEIGHBORS_TRAVERSAL_SPECIFICATION_TOKEN   = 17;
    protected static final int MESH_OBJECT_SELECTOR_TOKEN                    = 18;
    protected static final int BY_TYPE_MESH_OBJECT_SELECTOR_TOKEN            = 19;
    protected static final int PROPERTY_TYPE_REFERENCE_TOKEN                 = 20;
    protected static final int ROLE_TYPE_REFERENCE_TOKEN                     = 21;

    protected static final int NAME_TOKEN                                    = 22;
    protected static final int USERNAME_TOKEN                                = 23;
    protected static final int USERDESCRIPTION_TOKEN                         = 24;
    protected static final int SUPERTYPE_TOKEN                               = 25;
    protected static final int SYNONYM_TOKEN                                 = 26;
    protected static final int OVERRIDE_CODE_TOKEN                           = 27;
    protected static final int PROJECTION_CODE_TOKEN                         = 28;
    protected static final int IS_ABSTRACT_TOKEN                             = 29;
    protected static final int MAYBE_USED_AS_FORWARD_REFERENCE               = 30;
    protected static final int ADDITIONAL_INTERFACE                          = 31;
    protected static final int IS_OPTIONAL_TOKEN                             = 32;
    protected static final int IS_READONLY_TOKEN                             = 33;
    protected static final int IS_SIGNIFICANT_TOKEN                          = 34;
    protected static final int CODEGEN_TOKEN                                 = 35;
    protected static final int DATATYPE_TOKEN                                = 36;
    protected static final int DEFAULT_VALUE_TOKEN                           = 37;
    protected static final int SOURCE_TOKEN                                  = 38;
    protected static final int DESTINATION_TOKEN                             = 39;
    protected static final int SOURCE_DESTINATION_TOKEN                      = 40;
    protected static final int ENTITY_TOKEN                                  = 41;
    protected static final int REFINES_TOKEN                                 = 42;
    protected static final int TO_OVERRIDE_TOKEN                             = 43;
    protected static final int GUARD_TOKEN                                   = 44;
    protected static final int SEQUENCE_NUMBER_TOKEN                         = 45;
    protected static final int ICON_TOKEN                                    = 46;

    protected static final int BLOB_DATATYPE_TOKEN                           = 47;
    protected static final int BOOLEAN_DATATYPE_TOKEN                        = 48;
    protected static final int COLOR_DATATYPE_TOKEN                          = 49;
    protected static final int CURRENCY_DATATYPE_TOKEN                       = 50;
    protected static final int ENUMERATED_DATATYPE_TOKEN                     = 51;
    protected static final int EXTENT_DATATYPE_TOKEN                         = 52;
    protected static final int FLOAT_DATATYPE_TOKEN                          = 53;
    protected static final int FLOAT_MATRIX_DATATYPE_TOKEN                   = 54;
    protected static final int INTEGER_DATATYPE_TOKEN                        = 55;
    protected static final int MULTIPLICITY_DATATYPE_TOKEN                   = 56;
    protected static final int POINT_DATATYPE_TOKEN                          = 57;
    protected static final int STRING_DATATYPE_TOKEN                         = 58;
    protected static final int TIME_PERIOD_DATATYPE_TOKEN                    = 59;
    protected static final int TIME_STAMP_DATATYPE_TOKEN                     = 60;

    protected static final int MULTIPLICITY_VALUE_TOKEN                      = 61;
    protected static final int ENUM_TOKEN                                    = 62;
    protected static final int REGEX_TOKEN                                   = 63;
    protected static final int REGEX_ERROR_TOKEN                             = 64;

    protected static final int DECLARES_METHOD_TOKEN                         = 65;
    protected static final int IMPLEMENTS_METHOD_TOKEN                       = 66;

    /**
     * The table to map integers into strings.
     */
    protected static final String [] theTokenTable = new String[ IMPLEMENTS_METHOD_TOKEN+1 ];

    /**
     * The map to map strings into integers.
     */
    protected static final HashMap<String,Integer> theTokenMap = new HashMap<String,Integer>();
    static {
        addToTable( "model",                               MODEL_TOKEN );
        addToTable( "subjectarea",                         SUBJECT_AREA_TOKEN );
        addToTable( "dependson",                           DEPENDSON_TOKEN );
        addToTable( "subjectareareference",                SUBJECT_AREA_REFERENCE_TOKEN );
        // addToTable( "modulereference",                     MODULE_REFERENCE_TOKEN );
        addToTable( "entitytype",                          ENTITY_TYPE_TOKEN );
        addToTable( "relationshiptype",                    RELATIONSHIP_TYPE_TOKEN );
        addToTable( "propertytype",                        PROPERTY_TYPE_TOKEN );
        addToTable( "projectedpropertytype",               PROJECTED_PROPERTY_TYPE_TOKEN );
        addToTable( "propertytypegroup",                   PROPERTY_TYPE_GROUP_TOKEN );
        addToTable( "propertytypegroupmember",             PROPERTY_TYPE_GROUP_MEMBER_TOKEN );
        addToTable( "inputpropertyspecification",          INPUT_PROPERTY_SPECIFICATION_TOKEN );
        addToTable( "traversalspecification",              TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "alternativetraversalspecification",   ALTERNATIVE_TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "sequentialtraversalspecification",    SEQUENTIAL_TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "selectivetraversalspecification",     SELECTIVE_TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "stayrightheretraversalspecification", STAY_RIGHT_HERE_TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "allneighborstraversalspecification",  ALL_NEIGHBORS_TRAVERSAL_SPECIFICATION_TOKEN );
        addToTable( "meshobjectselector",                  MESH_OBJECT_SELECTOR_TOKEN );
        addToTable( "bytypemeshobjectselector",            BY_TYPE_MESH_OBJECT_SELECTOR_TOKEN );
        addToTable( "propertytypereference",               PROPERTY_TYPE_REFERENCE_TOKEN );
        addToTable( "roletypereference",                   ROLE_TYPE_REFERENCE_TOKEN );

        addToTable( "name",                        NAME_TOKEN );
        addToTable( "username",                    USERNAME_TOKEN );
        addToTable( "userdescription",             USERDESCRIPTION_TOKEN );

        addToTable( "supertype",                   SUPERTYPE_TOKEN );
        addToTable( "synonym",                     SYNONYM_TOKEN );
        addToTable( "overridecode",                OVERRIDE_CODE_TOKEN );
        addToTable( "projectioncode",              PROJECTION_CODE_TOKEN );
        addToTable( "isabstract",                  IS_ABSTRACT_TOKEN );
        addToTable( "maybeusedasforwardreference", MAYBE_USED_AS_FORWARD_REFERENCE );
        addToTable( "additionalinterface",         ADDITIONAL_INTERFACE );
        addToTable( "isoptional",                  IS_OPTIONAL_TOKEN );
        addToTable( "isreadonly",                  IS_READONLY_TOKEN );
        addToTable( "issignificant",               IS_SIGNIFICANT_TOKEN );
        addToTable( "codegen",                     CODEGEN_TOKEN );
        addToTable( "datatype",                    DATATYPE_TOKEN );
        addToTable( "defaultvalue",                DEFAULT_VALUE_TOKEN );
        addToTable( "src",                         SOURCE_TOKEN );
        addToTable( "dest",                        DESTINATION_TOKEN );
        addToTable( "srcdest",                     SOURCE_DESTINATION_TOKEN );
        addToTable( "e",                           ENTITY_TOKEN );
        addToTable( "refines",                     REFINES_TOKEN );
        addToTable( "tooverride",                  TO_OVERRIDE_TOKEN );
        addToTable( "guard",                       GUARD_TOKEN );
        addToTable( "sequencenumber",              SEQUENCE_NUMBER_TOKEN );
        addToTable( "icon",                        ICON_TOKEN );

        addToTable( "BlobDataType",             BLOB_DATATYPE_TOKEN );
        addToTable( "BooleanDataType",          BOOLEAN_DATATYPE_TOKEN );
        addToTable( "ColorDataType",            COLOR_DATATYPE_TOKEN );
        addToTable( "CurrencyDataType",         CURRENCY_DATATYPE_TOKEN );
        addToTable( "EnumeratedDataType",       ENUMERATED_DATATYPE_TOKEN );
        addToTable( "ExtentDataType",           EXTENT_DATATYPE_TOKEN );
        addToTable( "FloatDataType",            FLOAT_DATATYPE_TOKEN );
        addToTable( "FloatMatrixDataType",      FLOAT_MATRIX_DATATYPE_TOKEN );
        addToTable( "IntegerDataType",          INTEGER_DATATYPE_TOKEN );
        addToTable( "MultiplicityDataType",     MULTIPLICITY_DATATYPE_TOKEN );
        addToTable( "PointDataType",            POINT_DATATYPE_TOKEN );
        // addToTable( "ReferenceDataType",        REFERENCE_DATATYPE_TOKEN );
        addToTable( "StringDataType",           STRING_DATATYPE_TOKEN );
        addToTable( "TimePeriodDataType",       TIME_PERIOD_DATATYPE_TOKEN );
        addToTable( "TimeStampDataType",        TIME_STAMP_DATATYPE_TOKEN );

        addToTable( "MultiplicityValue",        MULTIPLICITY_VALUE_TOKEN );
        addToTable( "enum",                     ENUM_TOKEN );
        addToTable( "regex",                    REGEX_TOKEN );
        addToTable( "regexerror",               REGEX_ERROR_TOKEN );
        
        addToTable( "declaresMethod",           DECLARES_METHOD_TOKEN );
        addToTable( "implementsMethod",         IMPLEMENTS_METHOD_TOKEN );
    }

    /**
     * This is the list of keywords for attribute names.
     */
    static final String TYPEFIELD_KEYWORD       = "typefield";
    static final String IDENTIFIER_KEYWORD      = "ID";
    static final String IMPLEMENTATION_KEYWORD  = "implementation";
    static final String INTERFACE_KEYWORD       = "interface";
    static final String LOCALE_KEYWORD          = "locale";
    static final String PATH_KEYWORD            = "path";
    static final String CODE_KEYWORD            = "code";
    static final String SUBTYPE_ALLOWED_KEYWORD = "subtypeallowed";
}
