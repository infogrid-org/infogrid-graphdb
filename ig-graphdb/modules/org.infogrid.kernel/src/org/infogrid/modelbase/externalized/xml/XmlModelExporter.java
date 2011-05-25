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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.modelbase.externalized.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Locale;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.BooleanDataType;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.ColorDataType;
import org.infogrid.model.primitives.ColorValue;
import org.infogrid.model.primitives.CurrencyDataType;
import org.infogrid.model.primitives.CurrencyValue;
import org.infogrid.model.primitives.DataType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EnumeratedDataType;
import org.infogrid.model.primitives.EnumeratedValue;
import org.infogrid.model.primitives.ExtentDataType;
import org.infogrid.model.primitives.ExtentValue;
import org.infogrid.model.primitives.FloatDataType;
import org.infogrid.model.primitives.FloatValue;
import org.infogrid.model.primitives.IntegerDataType;
import org.infogrid.model.primitives.IntegerValue;
import org.infogrid.model.primitives.L10PropertyValueMap;
import org.infogrid.model.primitives.MeshType;
import org.infogrid.model.primitives.MultiplicityDataType;
import org.infogrid.model.primitives.MultiplicityValue;
import org.infogrid.model.primitives.PointDataType;
import org.infogrid.model.primitives.PointValue;
import org.infogrid.model.primitives.ProjectedPropertyType;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyTypeGroup;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringDataType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.primitives.TimePeriodDataType;
import org.infogrid.model.primitives.TimePeriodValue;
import org.infogrid.model.primitives.TimeStampDataType;
import org.infogrid.model.primitives.TimeStampValue;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalToPropertySpecification;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.module.ModuleRequirement;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.Identifier;
import org.infogrid.util.L10Map;
import org.infogrid.util.logging.Log;

/**
 * This class knows how to export the current model in a ModelBase.
 */
public class XmlModelExporter
{
    private static final Log log = Log.getLogInstance( XmlModelExporter.class ); // our own, private logger

    /**
      * Constructor.
      */
    public XmlModelExporter()
    {
        // Noop
    }

    /**
     * Export the full content of this ModelBase to an XML stream.
     *
     * @param modelBase the ModelBase whose content is being exported
     * @param theStream the OutputStream to which the data is sent
     * @throws IOException a write error occurred
     */
    public void exportToXML(
            ModelBase    modelBase,
            OutputStream theStream )
        throws
            IOException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "exportToXML", modelBase, theStream );
        }

        SubjectArea [] theSubjectAreas = ArrayHelper.copyIntoNewArray( modelBase.subjectAreaIterator(), SubjectArea.class );

        exportToXML( theSubjectAreas, theStream );
    }

    /**
     * Export one SubjectArea to an XML stream.
     *
     * @param theSubjectArea the SubjectArea that is being exported
     * @param theStream the OutputStream to which the data is sent
     * @throws IOException a write error occurred
     */
    public void exportToXML(
            SubjectArea  theSubjectArea,
            OutputStream theStream )
        throws
            IOException
    {
        exportToXML( new SubjectArea[] { theSubjectArea }, theStream );
    }

    /**
     * Export an enumerated set of SubjectAreas to an XML stream.
     *
     * @param theSubjectAreas the SubjectAreas that are being exported
     * @param theStream the OutputStream to which the data is sent
     * @throws IOException a write error occurred
     */
    public void exportToXML(
            SubjectArea [] theSubjectAreas,
            OutputStream   theStream )
        throws
            IOException
    {
        if( log.isTraceEnabled() ) {
            log.traceMethodCallEntry( this, "exportToXML", theSubjectAreas, theStream );
        }

        Writer theWriter = new OutputStreamWriter( theStream, encoding );

        writeXmlHeader( theWriter, encoding );

        doIndent( 0, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MODEL_TOKEN ));
        theWriter.write( ">\n" );

        for( int s=0 ; s<theSubjectAreas.length ; ++s ) {
            SubjectArea currentSa = theSubjectAreas[s];

            // sa keyword
            doIndent( 1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUBJECT_AREA_TOKEN ));
            theWriter.write( " " );
            theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
            theWriter.write( "=\"" );
            writeIdentifier( currentSa.getIdentifier(), theWriter );
            theWriter.write( "\">\n" );

            // sa.name keyword
            doIndent( 2, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
            theWriter.write( ">" );
            writeValue( currentSa.getName(), false, theWriter );
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
            theWriter.write( ">\n" );

            // sa.username keyword
            exportToXMLUserVisibleNameMap( 2, theWriter, currentSa );

            // sa.userdescription keyword
            exportToXMLUserVisibleDescriptionMap( 2, theWriter, currentSa );

            // sa.version keyword
            if( currentSa.getVersionNumber() != null ) {
                doIndent( 2, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.VERSION_TOKEN ));
                theWriter.write( ">" );
                writeValue( currentSa.getVersionNumber(), false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.VERSION_TOKEN ));
                theWriter.write( ">\n" );
            }

            
            if( currentSa.getSubjectAreaDependencies().length > 0 || currentSa.getModuleRequirements().length > 0 ) {

                // sa.dependson keyword
                doIndent( 2, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DEPENDSON_TOKEN ));
                theWriter.write( ">\n" );
                
                SubjectArea [] saDependencies = currentSa.getSubjectAreaDependencies();
                for( int i=0 ; i<saDependencies.length ; ++i ) {
                    // sa.dependson.sareference keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUBJECT_AREA_REFERENCE_TOKEN ));
                    theWriter.write( ">\n" );

                    // sa.dependson.sareference.name keyword
                    doIndent( 4, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">" );
                    writeValue( saDependencies[i].getName(), false, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">\n" );

                    // sa.dependson.sareference.minversion keyword
                    if( saDependencies[i].getVersionNumber() != null ) {
                        doIndent( 4, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MINVERSION_TOKEN ));
                        theWriter.write( ">" );
                        writeValue( saDependencies[i].getVersionNumber(), false, theWriter );
                        theWriter.write( "</" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MINVERSION_TOKEN ));
                        theWriter.write( ">\n" );
                    }

                    // /sa.dependson.sareference keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUBJECT_AREA_REFERENCE_TOKEN ));
                    theWriter.write( ">\n" );
                }

                ModuleRequirement [] moduleRequirements = currentSa.getModuleRequirements();
                for( int i=0 ; i<moduleRequirements.length ; ++i ) {
                    // sa.dependson.modulereference keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MODULE_REFERENCE_TOKEN ));
                    theWriter.write( ">\n" );

                    // sa.dependson.modulereference.name keyword
                    doIndent( 4, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">" );
                    theWriter.write( moduleRequirements[i].getRequiredModuleName() );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">\n" );

                    // sa.dependson.modulereference.minversion keyword
                    if( moduleRequirements[i].getRequiredModuleVersion() != null ) {
                        doIndent( 4, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MINVERSION_TOKEN ));
                        theWriter.write( ">" );
                        theWriter.write( moduleRequirements[i].getRequiredModuleVersion() );
                        theWriter.write( "</" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MINVERSION_TOKEN ));
                        theWriter.write( ">\n" );
                    }

                    // /sa.dependson.modulereference keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MODULE_REFERENCE_TOKEN ));
                    theWriter.write( ">\n" );
                }

                doIndent( 2, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DEPENDSON_TOKEN ));
                theWriter.write( ">\n" );
            }

            CollectableMeshType [] cmos = currentSa.getCollectableMeshTypes();

            for( int i=0 ; i<cmos.length ; ++i ) {
                if( cmos[i] instanceof EntityType ) {
                    EntityType currentMe = (EntityType) cmos[i];

                    // sa.me keyword
                    doIndent( 2, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TYPE_TOKEN ));
                    theWriter.write( " " );
                    theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
                    theWriter.write( "=\"" );
                    writeIdentifier( currentMe.getIdentifier(), theWriter );
                    theWriter.write( "\">\n" );

                    // sa.me.name keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">" );
                    writeValue( currentMe.getName(), false, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">\n" );

                    // me.username keyword
                    exportToXMLUserVisibleNameMap( 3, theWriter, currentMe );

                    // me.userdescription keyword
                    exportToXMLUserVisibleDescriptionMap( 3, theWriter, currentMe );

                    // sa.me.icon keyword
                    if( currentMe.getIcon() != null ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ICON_TOKEN ));
                        theWriter.write( " " );
                        theWriter.write( XmlModelTokens.PATH_KEYWORD );
                        theWriter.write( "=\"" );
                        writeValue( currentMe.getIcon(), false, theWriter );
                        theWriter.write( "\">\n" );
                    }

                    // sa.me.supertype
                    AttributableMeshType [] supertypes = currentMe.getDirectSupertypes();
                    for( int j=0 ; j<supertypes.length ; ++j ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUPERTYPE_TOKEN ));
                        theWriter.write( ">" );
                        writeIdentifier( supertypes[j].getIdentifier(), theWriter );
                        theWriter.write( "</" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUPERTYPE_TOKEN ));
                        theWriter.write( ">\n" );
                    }

                    // sa.me.overridecode keyword
                    if( currentMe.getInheritingOverrideCode() != null ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.OVERRIDE_CODE_TOKEN ));
                        theWriter.write( ">" ); // do not use line feed
                        writeValue( currentMe.getInheritingOverrideCode(), false, theWriter );
                        doIndent( 3, theWriter );
                        theWriter.write( "</" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.OVERRIDE_CODE_TOKEN ));
                        theWriter.write( ">\n" );
                    }

                    // sa.me.isabstract keyword
                    if( currentMe.getIsAbstract().value() ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.IS_ABSTRACT_TOKEN ));
                        theWriter.write( "/>\n" );
                    }

                    // sa.me.issignificant keyword
                    if( currentMe.getIsSignificant().value() ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.IS_SIGNIFICANT_TOKEN ));
                        theWriter.write( "/>\n" );
                    }

                    // sa.pt... pt,ppt,ptgroup keyword
                    PropertyType [] pts = currentMe.getLocalPropertyTypes();
                    for( int j=0 ; j<pts.length ; ++j ) {
                        exportToXMLPropertyType( 3, theWriter, pts[j] );
                    }

                    pts = currentMe.getOverridingLocalPropertyTypes();
                    for( int j=0 ; j<pts.length ; ++j ) {
                        exportToXMLPropertyType( 3, theWriter, pts[j] );
                    }

                    PropertyTypeGroup [] ptGroups = currentMe.getLocalPropertyTypeGroups();
                    for( int j=0 ; j<ptGroups.length ; ++j ) {
                        exportToXMLPropertyTypeGroup( 3, theWriter, ptGroups[j] );
                    }
                    // sa./me keyword
                    doIndent( 2, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TYPE_TOKEN ));
                    theWriter.write( ">\n" );

                } else if( cmos[i] instanceof RelationshipType ) {

                    RelationshipType currentRt = (RelationshipType) cmos[i];

                    // sa.mr keyword
                    doIndent( 2, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.RELATIONSHIP_TYPE_TOKEN ));
                    theWriter.write( " " );
                    theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
                    theWriter.write( "=\"" );
                    writeIdentifier( currentRt.getIdentifier(), theWriter );
                    theWriter.write( "\">\n" );

                    // sa.mr.name keyword
                    doIndent( 3, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">" );
                    writeValue( currentRt.getName(), false, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
                    theWriter.write( ">\n" );

                    // sa.username keyword
                    exportToXMLUserVisibleNameMap( 3, theWriter, currentRt );

                    // sa.userdescription keyword
                    exportToXMLUserVisibleDescriptionMap( 3, theWriter, currentRt );

                    // sa.mr.src / dest / srcdest keywords
                    if( currentRt.getSource().isTopSingleton() ) {
                        // top singleton
                        exportToXMLRoleTypeTop( 3, theWriter, (RoleType.TopSingleton) currentRt.getSource() );

                    } else {
                        // normal relationship type
                        exportToXMLRoleType(
                                3,
                                theWriter,
                                currentRt.getSource(),
                                XmlModelTokens.getKeywordFromToken( XmlModelTokens.SOURCE_TOKEN ));
                        exportToXMLRoleType(
                                3,
                                theWriter,
                                currentRt.getDestination(),
                                XmlModelTokens.getKeywordFromToken( XmlModelTokens.DESTINATION_TOKEN ));
                    }

                    // sa.me... pt,ppt,ptgroup keyword
                    PropertyType [] pts = currentRt.getLocalPropertyTypes();
                    for( int j=0 ; j<pts.length ; ++j ) {
                        exportToXMLPropertyType( 3, theWriter, pts[j] );
                    }
                    pts = currentRt.getOverridingLocalPropertyTypes();
                    for( int j=0 ; j<pts.length ; ++j ) {
                        exportToXMLPropertyType( 3, theWriter, pts[j] );
                    }
                    PropertyTypeGroup [] ptGroups = currentRt.getLocalPropertyTypeGroups();
                    for( int j=0 ; j<ptGroups.length ; ++j ) {
                        exportToXMLPropertyTypeGroup( 3, theWriter, ptGroups[j] );
                    }
                    // sa.mr.isabstract keyword
                    if( currentRt.getIsAbstract().value() ) {
                        doIndent( 3, theWriter );
                        theWriter.write( "<" );
                        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.IS_ABSTRACT_TOKEN ));
                        theWriter.write( "/>\n" );
                    }

                    // sa./mr keyword
                    doIndent( 2, theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.RELATIONSHIP_TYPE_TOKEN ));
                    theWriter.write( ">\n" );

                } else {
                     // nothing
                }
            }
            // /sa
            doIndent( 1, theWriter );
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SUBJECT_AREA_TOKEN ));
            theWriter.write( ">\n" );
        }

        doIndent( 0, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MODEL_TOKEN ));
        theWriter.write( ">\n" );

        theWriter.flush();
    }

    /**
     * Internal helper to write a PropertyType to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param current the PropertyType to export
     * @throws IOException a write error occurred
     */
    protected void exportToXMLPropertyType(
            int                  indent,
            Writer               theWriter,
            PropertyType         current )
        throws
            IOException
    {
        ProjectedPropertyType currentProjected
                = ( current instanceof ProjectedPropertyType )
                        ? (ProjectedPropertyType) current
                        : null;

        // pt/ppt keyword
        doIndent( indent, theWriter );
        theWriter.write( "<" );
        if( currentProjected != null ) {
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROJECTED_PROPERTY_TYPE_TOKEN ));
        } else {
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROPERTY_TYPE_TOKEN ));
        }
        theWriter.write( " " );
        theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
        theWriter.write( "=\"" );
        writeIdentifier( current.getIdentifier(), theWriter );
        theWriter.write( "\">\n" );

        // pt.name keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
        theWriter.write( ">" );
        writeValue( current.getName(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
        theWriter.write( ">\n" );

        // pt.username keyword
        exportToXMLUserVisibleNameMap( indent+1, theWriter, current );

        // pt.userdescription keyword
        exportToXMLUserVisibleDescriptionMap( indent+1, theWriter, current );

        // pt.tooverride keyword
        if( current.getOverride() != null ) {

            PropertyType [] toOverride = current.getOverride();
            for( int j=0 ; j<toOverride.length ; ++j ) {

                doIndent( indent+1, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.TO_OVERRIDE_TOKEN ));
                theWriter.write( ">" );
                writeIdentifier( toOverride[j].getIdentifier(), theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.TO_OVERRIDE_TOKEN ));
                theWriter.write( ">\n" );
            }
        }

        // pt.datatype keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DATATYPE_TOKEN ));
        theWriter.write( ">\n" );
        exportToXMLDataType( indent+2, theWriter, current.getDataType() );
        doIndent( indent+1, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DATATYPE_TOKEN ));
        theWriter.write( ">\n" );

        // pt.defaultvalue keyword
        if( current.getDefaultValue() != null ) {

            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DEFAULT_VALUE_TOKEN ));
            theWriter.write( ">" );
            writeValue( current.getDefaultValue(), true, theWriter ); // FIXME? false?
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.DEFAULT_VALUE_TOKEN ));
            theWriter.write( ">\n" );
        }

        // pt.isoptional keyword
        if( currentProjected == null && current.getIsOptional().value() ) {

            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.IS_OPTIONAL_TOKEN ));
            theWriter.write( "/>\n" );
        }

        // pt.isreadonly keyword
        if( currentProjected == null && current.getIsReadOnly().value() ) {

            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.IS_READONLY_TOKEN ));
            theWriter.write( "/>\n" );
        }

        if( currentProjected != null ) {

            TraversalToPropertySpecification [] inputProperties = currentProjected.getInputProperties();

            // ppt.inputproperties keyword
            if( inputProperties != null ) {
                for( int j=0 ; j<inputProperties.length ; ++j ) {

                    doIndent( indent+1, theWriter );
                    theWriter.write( "<" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.INPUT_PROPERTY_SPECIFICATION_TOKEN ));
                    theWriter.write( ">" );
                    writeTraversalToPropertySpecification( indent+2, inputProperties[j], theWriter );
                    theWriter.write( "</" );
                    theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.INPUT_PROPERTY_SPECIFICATION_TOKEN ));
                    theWriter.write( ">\n" );
                }
            }

            // ppt.projectioncode keyword
            if( currentProjected.getProjectionCode() != null ) {

                doIndent( indent+1, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROJECTION_CODE_TOKEN ));
                theWriter.write( ">" ); // do not use line feed
                writeValue( currentProjected.getProjectionCode(), false, theWriter );
                doIndent( indent+1, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROJECTION_CODE_TOKEN ));
                theWriter.write( ">\n" );
            }
        }

        // ppt.sequencenumber keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENCE_NUMBER_TOKEN ));
        theWriter.write( ">" );
        writeValue( current.getSequenceNumber(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENCE_NUMBER_TOKEN ));
        theWriter.write( ">\n" );

        // pt//ppt keyword
        doIndent( indent, theWriter );
        theWriter.write( "</" );
        if( currentProjected != null ) {
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROJECTED_PROPERTY_TYPE_TOKEN ));
        } else {
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROPERTY_TYPE_TOKEN ));
        }
        theWriter.write( ">\n" );
    }

    /**
     * Internal helper to write a PropertyTypeGroup to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param currentGroup the PropertyTypeGroup to export
     * @throws IOException a write error occurred
     */
    protected void exportToXMLPropertyTypeGroup(
            int                   indent,
            Writer                theWriter,
            PropertyTypeGroup     currentGroup )
        throws
            IOException
    {
        // magroup keyword
        doIndent( indent, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROPERTY_TYPE_GROUP_TOKEN ));
        theWriter.write( " " );
        theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
        theWriter.write( "=\"" );
        writeIdentifier( currentGroup.getIdentifier(), theWriter );
        theWriter.write( "\">\n" );

        // ptgroup.name keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
        theWriter.write( ">" );
        writeValue( currentGroup.getName(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.NAME_TOKEN ));
        theWriter.write( ">\n" );

        // ptgroup.username keyword
        exportToXMLUserVisibleNameMap( indent+1, theWriter, currentGroup );

        // ptgroup.userdescription keyword
        exportToXMLUserVisibleDescriptionMap( indent+1, theWriter, currentGroup );

        PropertyType [] pts = currentGroup.getContainedPropertyTypes();
        for( int j=0 ; j<pts.length ; ++j ) {

            // ptgroup.ptgroupmember keyword
            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROPERTY_TYPE_GROUP_MEMBER_TOKEN ));
            theWriter.write( " " );
            theWriter.write( XmlModelTokens.IDENTIFIER_KEYWORD );
            theWriter.write( "=\"" );
            writeIdentifier( pts[j].getIdentifier(), theWriter );
            theWriter.write( "\"/>\n" );
        }

        // ptgroup.sequencenumber keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENCE_NUMBER_TOKEN ));
        theWriter.write( ">" );
        writeValue( currentGroup.getSequenceNumber(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENCE_NUMBER_TOKEN ));
        theWriter.write( ">\n" );

        // /ptgroup keyword
        doIndent( indent, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.PROPERTY_TYPE_GROUP_TOKEN ));
        theWriter.write( ">\n" );
    }

    /**
     * Internal helper to write a singleton RoleType to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param top the RoleType.TopSingleton to export
     * @throws IOException a write error occurred
     */
    protected void exportToXMLRoleTypeTop(
            int                   indent,
            Writer                theWriter,
            RoleType.TopSingleton top )
        throws
            IOException
    {
        // srcdest keyword
        doIndent( indent, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SOURCE_DESTINATION_TOKEN ));
        theWriter.write( ">\n" );

        // e keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TOKEN ));
        theWriter.write( ">" );
        writeIdentifier( top.getEntityType().getIdentifier(), theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TOKEN ));
        theWriter.write( ">\n" );

        // MultiplicityValue keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MULTIPLICITY_VALUE_TOKEN ));
        theWriter.write( ">" );
        writeValue( top.getMultiplicity(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MULTIPLICITY_VALUE_TOKEN ));
        theWriter.write( ">\n" );

        // /srcdest keyword
        doIndent( indent, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SOURCE_DESTINATION_TOKEN ));
        theWriter.write( ">\n" );
    }

    /**
     * Internal helper to write a normal RoleType to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param role the RoleType to export
     * @param keyword the XML keyword to use for this element
     * @throws IOException a write error occurred
     */
    protected void exportToXMLRoleType(
            int                   indent,
            Writer                theWriter,
            RoleType              role,
            String                keyword )
        throws
            IOException
    {
        // src / dest keyword
        doIndent( indent, theWriter );
        theWriter.write( "<" );
        theWriter.write( keyword );
        theWriter.write( ">\n" );

        // e keyword
        if( role.getEntityType() != null ) {
            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TOKEN ));
            theWriter.write( ">" );
            writeIdentifier( role.getEntityType().getIdentifier(), theWriter );
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENTITY_TOKEN ));
            theWriter.write( ">\n" );
        }

        // refines keyword
        RoleType [] supertypes = role.getDirectSuperRoleTypes();
        for( int i=0 ; i<supertypes.length ; ++i ) {

            doIndent( indent+1, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.REFINES_TOKEN ));
            theWriter.write( ">" );
            theWriter.write( supertypes[i].getIdentifier().toExternalForm() ); // RoleType -- don't go through serializer (FIXME?)
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.REFINES_TOKEN ));
            theWriter.write( ">\n" );
        }

        // MultiplicityValue keyword
        doIndent( indent+1, theWriter );
        theWriter.write( "<" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MULTIPLICITY_VALUE_TOKEN ));
        theWriter.write( ">" );
        writeValue( role.getMultiplicity(), false, theWriter );
        theWriter.write( "</" );
        theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MULTIPLICITY_VALUE_TOKEN ));
        theWriter.write( ">\n" );

        // /src / /dest keyword
        doIndent( indent, theWriter );
        theWriter.write( "</" );
        theWriter.write( keyword );
        theWriter.write( ">\n" );
    }

    /**
     * Internal helper to write a DataType to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param type the DataType to export
     * @throws IOException a write error occurred
     */
    protected void exportToXMLDataType(
            int                  indent,
            Writer               theWriter,
            DataType             type )
        throws
            IOException
    {
        String qualifier;
        if( type instanceof BlobDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.BLOB_DATATYPE_TOKEN ));

            qualifier = determineDataTypeQualifier( BlobDataType.class, type );

            if( qualifier != null ) {
                theWriter.write( qualifier );
            }
            theWriter.write( "/>\n" );

        } else if( type instanceof BooleanDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.BOOLEAN_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof ColorDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.COLOR_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof CurrencyDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.CURRENCY_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof EnumeratedDataType ) {
            EnumeratedDataType realType = (EnumeratedDataType) type;
            EnumeratedValue [] domain   = realType.getDomain();

            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENUMERATED_DATATYPE_TOKEN ));
            theWriter.write( ">\n" );
            for( int i=0 ; i<domain.length ; ++i ) {
                doIndent( indent+1, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENUM_TOKEN ));
                theWriter.write( ">" );
                writeValue( domain[i], false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENUM_TOKEN ));
                theWriter.write( ">\n" );
            }
            doIndent( indent, theWriter );
            theWriter.write( "</" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ENUMERATED_DATATYPE_TOKEN ));
            theWriter.write( ">\n" );

        } else if( type instanceof ExtentDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.EXTENT_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof FloatDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.FLOAT_DATATYPE_TOKEN ));

            qualifier = determineDataTypeQualifier( FloatDataType.class, type );

            if( qualifier != null ) {
                theWriter.write( qualifier );
            }
            theWriter.write( "/>\n" );

        } else if( type instanceof IntegerDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.INTEGER_DATATYPE_TOKEN ));

            qualifier = determineDataTypeQualifier( IntegerDataType.class, type );

            if( qualifier != null ) {
                theWriter.write( qualifier );
            }
            theWriter.write( "/>\n" );

        } else if( type instanceof MultiplicityDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.MULTIPLICITY_DATATYPE_TOKEN ));

            qualifier = determineDataTypeQualifier( MultiplicityDataType.class, type );

            if( qualifier != null ) {
                theWriter.write( qualifier );
            }
            theWriter.write( "/>\n" );

        } else if( type instanceof PointDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.POINT_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof StringDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.STRING_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof TimePeriodDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.TIME_PERIOD_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else if( type instanceof TimeStampDataType ) {
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.TIME_STAMP_DATATYPE_TOKEN ));
            theWriter.write( "/>\n" );

        } else {
            log.error( "unknown type: " + type );
        }
    }

    /**
     * Internal helper to write a UserVisibleName map to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param mo the MeshType whose UserVisibeName map is being exported
     * @throws IOException a write error occurred
     */
    protected void exportToXMLUserVisibleNameMap(
            int      indent,
            Writer   theWriter,
            MeshType mo )
        throws
            IOException
    {
        L10PropertyValueMap theMap = mo.getUserVisibleNameMap();

        if( theMap != null ) {
            PropertyValue v = theMap.getDefault();
            if( v != null ) {
                doIndent( indent, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERNAME_TOKEN ));
                theWriter.write( ">" );
                writeValue( v, false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERNAME_TOKEN ));
                theWriter.write( ">\n" );
            }

            Iterator theIter = theMap.keyIterator();
            while( theIter.hasNext() ) {
                Locale key = (Locale) theIter.next();
                v = theMap.get( key );

                doIndent( indent, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERNAME_TOKEN ));
                theWriter.write( " " );
                theWriter.write( XmlModelTokens.LOCALE_KEYWORD );
                theWriter.write( "=\"" );
                theWriter.write( key.toString() );
                theWriter.write( "\">" );
                writeValue( v, false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERNAME_TOKEN ));
                theWriter.write( ">\n" );
            }
        }
    }

    /**
     * Internal helper to write a UserVisibleDescription map to an XML export stream.
     *
     * @param indent the indent level
     * @param theWriter the Writer we write to
     * @param mo the MeshType whose UserVisibleDescription map is being exported
     * @throws IOException a write error occurred
     */
    protected void exportToXMLUserVisibleDescriptionMap(
            int      indent,
            Writer   theWriter,
            MeshType mo )
        throws
            IOException
    {
        L10PropertyValueMap theMap = mo.getUserVisibleDescriptionMap();

        if( theMap != null ) {
            PropertyValue v = theMap.getDefault();

            if( v != null ) {
                doIndent( indent, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERDESCRIPTION_TOKEN ));
                theWriter.write( ">" );
                writeValue( v, false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERDESCRIPTION_TOKEN ));
                theWriter.write( ">\n" );
            }

            Iterator theIter = theMap.keyIterator();
            while( theIter.hasNext() ) {
                Locale key = (Locale) theIter.next();
                v = theMap.get( key );

                doIndent( indent, theWriter );
                theWriter.write( "<" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERDESCRIPTION_TOKEN ));
                theWriter.write( " " );
                theWriter.write( XmlModelTokens.LOCALE_KEYWORD );
                theWriter.write( "=\"" );
                theWriter.write( key.toString() );
                theWriter.write( "\">" );
                writeValue( v, false, theWriter );
                theWriter.write( "</" );
                theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.USERDESCRIPTION_TOKEN ));
                theWriter.write( ">\n" );
            }
        }
    }

    /**
      * Internal helper to write a TraversalSpecification to an XML export stream.
      *
      * @param indent the indent level
      * @param spec the TraversalSpecification to serialize
      * @param theWriter the Writer to serialize to
      * @throws IOException thrown if a write error occurred
      */
    protected void writeTraversalSpecification(
            int                    indent,
            TraversalSpecification spec,
            Writer                 theWriter )
        throws
            IOException
    {
        if( spec instanceof AlternativeCompoundTraversalSpecification ) {
            AlternativeCompoundTraversalSpecification realSpec   = (AlternativeCompoundTraversalSpecification) spec;
            TraversalSpecification []                 childSpecs = realSpec.getAlternatives();

            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ALTERNATIVE_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );

            for( int i=0 ; i<childSpecs.length ; ++i ) {
                writeTraversalSpecification( indent+1, childSpecs[i], theWriter );
            }
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.ALTERNATIVE_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );
            return;
        }
        if( spec instanceof SelectiveTraversalSpecification ) {
            SelectiveTraversalSpecification realSpec = (SelectiveTraversalSpecification) spec;

            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SELECTIVE_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );

            MeshObjectSelector startSelector = realSpec.getStartSelector();
            if( startSelector != null ) {
                writeMeshObjectSelector( indent+1, startSelector, theWriter );
            }

            writeTraversalSpecification( indent+1, realSpec.getQualifiedTraversalSpecification(), theWriter );

            MeshObjectSelector endSelector = realSpec.getEndSelector();
            if( endSelector != null ) {
                writeMeshObjectSelector( indent+1, endSelector, theWriter );
            }

            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SELECTIVE_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );
            return;
        }
        if( spec instanceof SequentialCompoundTraversalSpecification ) {
            SequentialCompoundTraversalSpecification realSpec   = (SequentialCompoundTraversalSpecification) spec;
            TraversalSpecification []                 childSpecs = realSpec.getSteps();

            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENTIAL_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );

            for( int i=0 ; i<childSpecs.length ; ++i ) {
                writeTraversalSpecification( indent+1, childSpecs[i], theWriter );
            }
            doIndent( indent, theWriter );
            theWriter.write( "<" );
            theWriter.write( XmlModelTokens.getKeywordFromToken( XmlModelTokens.SEQUENTIAL_TRAVERSAL_SPECIFICATION_TOKEN ));
            theWriter.write( ">\n" );
            return;
        }
        log.error( "Unknown type of TraversalSpecification: " + spec );
    }

    /**
      * Internal helper to write a TraversalToPropertySpecification to an XML stream.
      *
      * @param indent the indent level
      * @param spec the TraversalToPropertySpecification to serialize
      * @param theWriter the Writer to serialize to
      * @throws IOException thrown if a write error occurred
      */
    protected void writeTraversalToPropertySpecification(
            int                              indent,
            TraversalToPropertySpecification spec,
            Writer                           theWriter )
        throws
            IOException
    {
        log.error( "FIXME, not implemented" );
    }


    /**
      * Internal helper to write a MeshObjectSelector to an XML stream.
      *
      * @param indent the indent level
      * @param selector the MeshObjectSelector to serialize
      * @param theWriter the Writer to serialize to
      * @throws IOException thrown if a write error occurred
      */
    protected void writeMeshObjectSelector(
            int                indent,
            MeshObjectSelector selector,
            Writer             theWriter )
        throws
            IOException
    {
        log.error( "FIXME, not implemented" );
    }

    /**
      * Internal helper to write any PropertyValue to an XML stream.
      *
      * @param value the PropertyValue to serialize
      * @param writeTag if true, tag the value with the type
      * @param theWriter the Writer to serialize to
      * @throws IOException thrown if a write error occurred
      */
    public void writeValue(
            PropertyValue value,
            boolean       writeTag,
            Writer        theWriter )
        throws
            IOException
    {
        if( value == null ) {
            theWriter.write( "NULL" );
            return;
        }

        if( value instanceof BlobValue ) {
            BlobValue realValue = (BlobValue) value;
            if( realValue.delayedLoadingFrom() != null ) {
                String mt = realValue.getMimeType();
                if( writeTag ) {
                    theWriter.write( "BlobDelayed:" );
                    if( mt != null ) {
                        theWriter.write( escape( mt ) );
                    } else {
                        theWriter.write( escape( "?/?" ));
                    }
                    theWriter.write( ":" );
                }
                theWriter.write( "<![CDATA[" );
                theWriter.write( escape( realValue.delayedLoadingFrom() ));
                theWriter.write( "]]>" );
                return;
            } else {
                String mt = realValue.getMimeType();
                if( writeTag ) {
                    theWriter.write( "Blob:" );
                    if( mt != null ) {
                        theWriter.write( escape( mt ) );
                    } else {
                        theWriter.write( escape( "?/?" ));
                    }
                    theWriter.write( ":" );
                }

                if( mt != null && ! mt.startsWith( "text" )) {
                    theWriter.write("x\'");
                    theWriter.write( BlobValue.encodeHex( realValue.value() ) );
                    theWriter.write("\'");
                } else {
                    theWriter.write( "<![CDATA[" );
                    theWriter.write( escape( realValue.getAsString() ));
                    theWriter.write( "]]>" );
                }
                return;
            }
        }

        if( value instanceof BooleanValue ) {
            if( writeTag ) {
                theWriter.write( "Boolean:" );
            }
            if( ((BooleanValue)value).value() ) {
                theWriter.write( "TRUE" );
            } else {
                theWriter.write( "FALSE" );
            }
            return;
        }

        if( value instanceof ColorValue ) {
            if( writeTag ) {
                theWriter.write( "Color:" );
            }
            theWriter.write( String.valueOf( ((ColorValue)value).getRGB() ));
            return;
        }

        if( value instanceof CurrencyValue ) {
            CurrencyValue realValue = (CurrencyValue) value;
            if( writeTag ) {
                theWriter.write( "Currency:" );
            }
            theWriter.write( realValue.value() );
            return;
        }

        if( value instanceof EnumeratedValue ) {
            if( writeTag ) {
                theWriter.write( "Enumerated:" );
            }
            theWriter.write( escape( ((EnumeratedValue)value).value() ));
            return;
        }

        if( value instanceof ExtentValue ) {
            if( writeTag ) {
                theWriter.write( "Extent:" );
            }
            theWriter.write( String.valueOf( ((ExtentValue)value).getWidth() ));
            theWriter.write( ";" );
            theWriter.write( String.valueOf( ((ExtentValue)value).getHeight() ));
            return;
        }

        if( value instanceof FloatValue ) {
            if( writeTag ) {
                theWriter.write( "Float:" );
            }
            theWriter.write( String.valueOf( ((FloatValue)value).value() ));
            return;
        }

        if( value instanceof IntegerValue ) {
            if( writeTag ) {
                theWriter.write( "Integer:" );
            }
            theWriter.write( String.valueOf( ((IntegerValue)value).value() ));
            return;
        }

        if( value instanceof MultiplicityValue ) {
            MultiplicityValue theValue = (MultiplicityValue) value;
            if( writeTag ) {
                theWriter.write( "Multiplicity:" );
            }
            theWriter.write( ( theValue.getMinimum() < 0 ) ? "N" : String.valueOf( theValue.getMinimum() ) );
            theWriter.write( ':' );
            theWriter.write( ( theValue.getMaximum() < 0 ) ? "N" : String.valueOf( theValue.getMaximum() ) );
            return;
        }

        if( value instanceof PointValue ) {
            PointValue theValue = (PointValue) value;
            if( writeTag ) {
                theWriter.write( "Point:" );
            }
            theWriter.write( String.valueOf( theValue.getX() ));
            theWriter.write( ";" );
            theWriter.write( String.valueOf( theValue.getY() ));
            return;
        }

        if( value instanceof StringValue ) {
            if( writeTag ) {
                theWriter.write( "String:" );
            }
            theWriter.write( escape( ((StringValue)value).value() ));
            return;
        }

        if( value instanceof TimePeriodValue ) {
            TimePeriodValue realValue = (TimePeriodValue) value;
            if( writeTag ) {
                theWriter.write( "TimePeriod:" );
            }
            theWriter.write( String.valueOf( realValue.getYear() ));
            theWriter.write( '/' );
            theWriter.write( String.valueOf( realValue.getMonth() ));
            theWriter.write( '/' );
            theWriter.write( String.valueOf( realValue.getDay() ));
            theWriter.write( ' ' );
            theWriter.write( String.valueOf( realValue.getHour() ));
            theWriter.write( ':' );
            theWriter.write( String.valueOf( realValue.getMinute() ));
            theWriter.write( ':' );
            theWriter.write( String.valueOf( realValue.getSecond() ));
            return;
        }

        if( value instanceof TimeStampValue ) {
            TimeStampValue realValue = (TimeStampValue) value;
            if( writeTag ) {
                theWriter.write( "TimeStamp:" );
            }
            theWriter.write( realValue.getAsRfc3339String() );
            return;
        }

        theWriter.write( "?" );
    }

    /**
     * Write out an Identifier to a stream.
     * 
     * @param identifier the Identifier to write
     * @param theWriter the Writer we write to
     * @throws IOException a write error occurred
     */
    public void writeIdentifier(
            Identifier identifier,
            Writer     theWriter )
        throws
            IOException
    {
        theWriter.write( identifier.toExternalForm() );
    }

    /**
     * This writes the XML file header to a stream.
     *
     * @param theWriter the Writer we write to
     * @param encoding the encoding to use
     * @throws IOException a write error occurred
     */
    protected static void writeXmlHeader(
            Writer theWriter,
            String encoding )
        throws
            IOException
    {
        theWriter.write( "<?xml version=\"1.0\"" );
        if( encoding != null ) {
            theWriter.write( " encoding=\"" );
            theWriter.write( encoding );
            theWriter.write( "\"" );
        }
        theWriter.write( "?>\n" );
        theWriter.write( "<!DOCTYPE model" );
        theWriter.write( " PUBLIC '-//NetMesh Inc.//InfoGrid Model//EN' '" );
        theWriter.write( XmlModelTokens.PUBLIC_MODEL_DTD_URL );
        theWriter.write( "'>\n" );
        theWriter.write( "\n" );
    }

    /**
     * Helper method to determine the qualifier string to be added to a DataType which
     * indicates which instance variable to use. Returns null if default.
     *
     * @param theDataTypeClass the Class of the DataType
     * @param theDataTypeInstance the instance of the DataType
     * @return the name of the member variable in theDataTypeClass
     */
    protected String determineDataTypeQualifier(
            Class    theDataTypeClass,
            DataType theDataTypeInstance )
    {
        // we use reflection to find which instance this is

        Field [] allFields = theDataTypeClass.getFields();
        for( int i=0 ; i<allFields.length ; ++i ) {
            try {
                if( ! Modifier.isStatic( allFields[i].getModifiers() )) {
                    continue;
                }

                if( theDataTypeInstance == allFields[i].get( null )) {
                    // found
                    String fieldName = allFields[i].getName();
                    if( "theDefault".equals( fieldName )) {
                        return null;
                    } else {
                        return " " + XmlModelTokens.TYPEFIELD_KEYWORD + "=\"" + fieldName + "\"";
                    }
                }
            } catch( IllegalAccessException ex ) {
                log.error( ex );
            }
        }
        // not found
        log.error( "could not find " + theDataTypeInstance + " in class " + theDataTypeClass.getName() );
        return null;
    }

    /**
     * This helper takes arbitrary strings and escapes them in a way so that they
     * fit into XML. FIXME This is not complete at all.
     *
     * @param org the original String
     * @return the escaped String
     */
    protected static String escape(
            String org )
    {
        StringBuilder ret = new StringBuilder( org.length() + org.length()/5 ); // make a little larger

        for( int i=0 ; i<org.length() ; ++i ) {
            char c = org.charAt( i );
            switch( c ) {
                case '\'':
                case '\"':
                case '\t':
                // case '\n':
                case '\\':
                case '&':
                // case '#':
                case '<':
                // case '>':
                // case '/':
                    {
                        ret.append( "&#x" );
                        // needs to have a least four hex digits according to the XML spec
                        int digit;
                        for( int j=3 ; j>=0 ; --j ) {
                            digit = c >> (j*4);
                            digit &= 0xf;
                            ret.append( charTable[ digit ] );
                        }

                        ret.append( ";" );
                        break;
                    }
                default:
                    ret.append( c );
                    break;
            }
        }
        return new String( ret );
    }

    /**
     * Helper method to indent by n steps.
     *
     * @param n the number of steps to indent
     * @param theWriter the Writer we write to
     * @throws IOException a write error occurred
     */
    protected static void doIndent(
            int    n,
            Writer theWriter )
        throws
            IOException
    {
        if( n <= 0 ) {
            return;
        }

        int nBlanks = 2*n;
        if( nBlanks <= spaces.length ) {
            theWriter.write( spaces, 0, nBlanks );
            return;
        } else {
            theWriter.write( spaces );
            doIndent( nBlanks - spaces.length, theWriter );
        }
    }

    /**
     * The default encoding for the output.
     */
    protected static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * The encoding we use for writing.
     */
    protected String encoding = DEFAULT_ENCODING;

    /**
     * The indent space buffer.
     */
    protected static final char [] spaces = new char[ 16 ];
    static {
        for( int i=0 ; i<spaces.length ; ++ i ) {
            spaces[i] = ' ';
        }
    }

    /**
     * For conversion speed, a character table.
     */
    protected static char[] charTable = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
}
