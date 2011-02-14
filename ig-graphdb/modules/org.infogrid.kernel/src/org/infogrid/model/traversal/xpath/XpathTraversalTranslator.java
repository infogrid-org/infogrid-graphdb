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

package org.infogrid.model.traversal.xpath;

import java.util.ArrayList;
import java.util.Iterator;
import org.infogrid.model.traversal.TermMissingTraversalTranslatorException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.set.ByRelatedMeshObjectSelector;
import org.infogrid.mesh.set.ByTypeMeshObjectSelector;
import org.infogrid.mesh.set.InvertingMeshObjectSelector;
import org.infogrid.mesh.set.MeshObjectSelector;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.primitives.CollectableMeshType;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.model.primitives.SubjectArea;
import org.infogrid.model.traversal.AbstractTraversalTranslator;
import org.infogrid.model.traversal.AllNeighborsTraversalSpecification;
import org.infogrid.model.traversal.AlternativeCompoundTraversalSpecification;
import org.infogrid.model.traversal.SelectiveTraversalSpecification;
import org.infogrid.model.traversal.SequentialCompoundTraversalSpecification;
import org.infogrid.model.traversal.StayRightHereTraversalSpecification;
import org.infogrid.model.traversal.TermInvalidTraversalTranslatorException;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalTranslatorException;
import org.infogrid.model.traversal.WrongNumberTermsTraversalTranslatorException;
import org.infogrid.modelbase.MeshTypeWithIdentifierNotFoundException;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.util.logging.Log;

/**
 * <p>A TraversalTranslator that interprets Strings as XPath expressions.</p>
 */
public class XpathTraversalTranslator
        extends
            AbstractTraversalTranslator
{
    private static final Log log = Log.getLogInstance( XpathTraversalTranslator.class ); // our own, private logger

    /**
     * Factory method.
     *
     * @param mb the MeshBase to use
     * @return the created XpathTraversalTranslator
     */
    public static XpathTraversalTranslator create(
            MeshBase mb )
    {
        return new XpathTraversalTranslator( mb );
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase to use
     */
    protected XpathTraversalTranslator(
            MeshBase mb )
    {
        super( mb );
    }

    /**
     * Translate the String form of a TraversalSpecification into an actual TraversalSpecification.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerms the terms to be translated
     * @return the TraversalSpecification, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public TraversalSpecification translateTraversalSpecification(
            MeshObject startObject,
            String []  traversalTerms )
        throws
            TraversalTranslatorException
    {
        TraversalSpecification [] found = translateTraversalSpecifications( startObject, traversalTerms );
        if( found == null || found.length == 0 ) {
            return null;
        } else {
            return found[0]; // ignore the rest
        }
    }

    /**
     * Translate the String form of several TraversalSpecifications into the
     * actual TraversalSpecifications. The assumption here is that the traversal terms
     * describe several TraversalSpecifications.
     *
     * By default, there is only one.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerms the terms to be translated
     * @return the TraversalSpecifications, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    @Override
    public TraversalSpecification [] translateTraversalSpecifications(
            MeshObject startObject,
            String []  traversalTerms )
        throws
            TraversalTranslatorException
    {
        String [] xpathStrings = new String[ traversalTerms.length ];
        String    dictString   = null;
        int       xpathCount   = 0;

        for( int i=0 ; i<traversalTerms.length ; ++i ) {
            if( traversalTerms[i].startsWith( XPATH_TAG )) {
                xpathStrings[xpathCount++] = traversalTerms[i].substring( XPATH_TAG.length() );
            } else if( traversalTerms[i].startsWith( DICT_TAG )) {
                if( dictString != null ) {
                    throw new WrongNumberTermsTraversalTranslatorException( traversalTerms );
                } else {
                    dictString = traversalTerms[i].substring( DICT_TAG.length() );
                }
            }
        }
        if( xpathCount == 0 ) {
            throw new TermMissingTraversalTranslatorException( traversalTerms, XPATH_TAG );
        }
        // dictString is optional

        XpathNamespaceMap         dict = unserializeNamespaceMap( dictString, traversalTerms );
        TraversalSpecification [] ret  = new TraversalSpecification[ xpathCount ];
        
        for( int i=0 ; i<xpathCount ; ++i ) {
            ret[i] = unserializeTraversalSpecification( startObject, xpathStrings[i], traversalTerms, dict );
        }
        return ret;
    }

    /**
     * Translate an actual TraversalSpecification into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversal the TraversalSpecification
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public String [] translateTraversalSpecification(
            MeshObject             startObject,
            TraversalSpecification traversal )
        throws
            TraversalTranslatorException
    {
        SimpleXpathNamespaceMap dict = SimpleXpathNamespaceMap.create();

        StringBuilder xpathBuf = new StringBuilder();
        xpathBuf.append( XPATH_TAG );
        serializeTraversalSpecification( startObject, traversal, dict, xpathBuf );

        String [] ret;
        if( dict.isEmpty() ) {
            ret = new String[] {
                    xpathBuf.toString()
            };

        } else {
            StringBuilder dictBuf = new StringBuilder();
            dictBuf.append( DICT_TAG );
            serializeNamespaceMap( dict, dictBuf );
        
            ret = new String[] {
                    dictBuf.toString(),
                    xpathBuf.toString()
            };
        }
        return ret;
    }

    /**
     * Translate several actual TraversalSpecifications into String form. The assumption
     * here is that if it is performed at the same time, the String form may be more
     * compact than if done separately.
     *
     * By default, translate separately.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversals the TraversalSpecifications
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    @Override
    public String [] translateTraversalSpecifications(
            MeshObject                startObject,
            TraversalSpecification [] traversals )
        throws
            TraversalTranslatorException
    {
        SimpleXpathNamespaceMap dict = SimpleXpathNamespaceMap.create();

        StringBuilder [] xpathBufs = new StringBuilder[ traversals.length ];
        for( int i=0 ; i<traversals.length ; ++i ) {
            xpathBufs[i] = new StringBuilder();
            xpathBufs[i].append( XPATH_TAG );
            serializeTraversalSpecification( startObject, traversals[i], dict, xpathBufs[i] );
        }

        String [] ret;
        int       delta;
        if( dict.isEmpty() ) {
            ret   = new String[ traversals.length ];
            delta = 0;

        } else {
            StringBuilder dictBuf = new StringBuilder();
            dictBuf.append( DICT_TAG );
            serializeNamespaceMap( dict, dictBuf );

            ret = new String[ traversals.length+1 ];
            ret[0] = dictBuf.toString();
            delta = 1;
        }
        for( int i=0 ; i<xpathBufs.length ; ++i ) {
            ret[i+delta] = xpathBufs[i].toString();
        }
        return ret;
    }

    /**
     * Translate an actual TraversalSpecification into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversal the TraversalSpecification
     * @param dict the namespace map to use
     * @param buf to write to
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public void serializeTraversalSpecification(
            MeshObject             startObject,
            TraversalSpecification traversal,
            XpathNamespaceMap      dict,
            StringBuilder          buf )
        throws
            TraversalTranslatorException
    {
        if( traversal == null ) {
            throw new NullPointerException( "Null TraversalSpecification" );

        } else if( traversal instanceof RoleType ) {
            RoleType realTraversal = (RoleType) traversal;

            String prefix = dict.obtainPrefixFor( realTraversal.getSubjectArea() );
            buf.append( prefix );
            buf.append( COLON );
            buf.append( realTraversal.getName().value() );

        } else if( traversal instanceof AlternativeCompoundTraversalSpecification ) {
            AlternativeCompoundTraversalSpecification realTraversal = (AlternativeCompoundTraversalSpecification) traversal;
            TraversalSpecification []                 alt           = realTraversal.getAlternatives();

            for( int i=0 ; i<alt.length ; ++i ) {
                if( i>0 ) {
                    buf.append( ALTERNATIVE_SEPARATOR );
                }
                if( isCompound( alt[i] )) {
                    buf.append( PARENTHESIS_OPEN );
                }
                serializeTraversalSpecification( startObject, alt[i], dict, buf );
                if( isCompound( alt[i] )) {
                    buf.append( PARENTHESIS_CLOSE );
                }
            }

        } else if( traversal instanceof SelectiveTraversalSpecification ) {
            SelectiveTraversalSpecification realToConvert = (SelectiveTraversalSpecification) traversal;

            if( realToConvert.getStartSelector() != null ) {
                buf.append( QUALIFIER_OPEN );
                serializeMeshObjectSelector( realToConvert.getStartSelector(), dict, buf );
                buf.append( QUALIFIER_CLOSE );
            }

            if( isCompound( realToConvert.getQualifiedTraversalSpecification() )) {
                buf.append( PARENTHESIS_OPEN );
            }
            serializeTraversalSpecification( startObject, realToConvert.getQualifiedTraversalSpecification(), dict, buf );
            if( isCompound( realToConvert.getQualifiedTraversalSpecification() )) {
                buf.append( PARENTHESIS_CLOSE );
            }

            if( realToConvert.getEndSelector() != null ) {
                buf.append( QUALIFIER_OPEN );
                serializeMeshObjectSelector( realToConvert.getEndSelector(), dict, buf );
                buf.append( QUALIFIER_CLOSE );
            }

        } else if( traversal instanceof SequentialCompoundTraversalSpecification ) {
            SequentialCompoundTraversalSpecification realToConvert = (SequentialCompoundTraversalSpecification) traversal;
            TraversalSpecification []                steps         = realToConvert.getSteps();

            for( int i=0 ; i<steps.length ; ++i ) {
                if( i>0 ) {
                    buf.append( AXIS_SEPARATOR );
                }

                if( isCompound( steps[i] )) {
                    buf.append( PARENTHESIS_OPEN );
                }
                serializeTraversalSpecification( startObject, steps[i], dict, buf );
                if( isCompound( steps[i] )) {
                    buf.append( PARENTHESIS_CLOSE );
                }
            }

        } else if( traversal instanceof StayRightHereTraversalSpecification ) {
            buf.append( STAY_RIGHT_HERE );

        } else if( traversal instanceof AllNeighborsTraversalSpecification ) {
            buf.append( ALL_NEIGHBORS );

        } else {
            log.error( "Unknown type", traversal );
        }
    }

    /**
     * Helper method to determine whether a TraversalSpecification is primitive and thus does not need to put
     * serialized in parentheses.
     *
     * @param spec the TraversalSpecification
     * @return true if it is not primitive and must be put in parentheses
     */
    protected boolean isCompound(
            TraversalSpecification spec )
    {
        if( spec instanceof RoleType ) {
            return false;
        }
        if( spec instanceof AllNeighborsTraversalSpecification ) {
            return false;
        }
        if( spec instanceof StayRightHereTraversalSpecification ) {
            return false;
        }
        return true;
    }

    /**
     * Helper method to convert a MeshObjectSelector into the right term
     *
     * @param toConvert the MeshObjectSelector
     * @param dict the XpathNamespaceMap to use
     * @param buf the StringBuilder to write to
     */
    public void serializeMeshObjectSelector(
            MeshObjectSelector toConvert,
            XpathNamespaceMap  dict,
            StringBuilder      buf )
    {
        if( toConvert == null ) {
            return;
        }

        if( toConvert instanceof InvertingMeshObjectSelector ) {
            InvertingMeshObjectSelector realToConvert = (InvertingMeshObjectSelector) toConvert;
            buf.append( NOT );

            serializeMeshObjectSelector( realToConvert.getDelegate(), dict, buf );

        } else if( toConvert instanceof ByTypeMeshObjectSelector ) {
            ByTypeMeshObjectSelector realToConvert = (ByTypeMeshObjectSelector) toConvert;
            if( realToConvert.isSubtypesAllowed() ) {
                buf.append( HAS_TYPE_FUNCTION );
            } else {
                buf.append( HAS_EXACT_TYPE_FUNCTION );
            }
            buf.append( PARENTHESIS_OPEN );
            buf.append( STAY_RIGHT_HERE );
            buf.append( COMMA );

            String prefix = dict.obtainPrefixFor( realToConvert.getFilterType().getSubjectArea() );
            buf.append( prefix );
            buf.append( COLON );
            buf.append( realToConvert.getFilterType().getName().value() );
            buf.append( PARENTHESIS_CLOSE );

        } else {
            throw new IllegalArgumentException( "Cannot serialize " + toConvert );
        }
    }

    /**
     * Helper method to convert an XpathNamespaceMap into the right term.
     *
     * @param dict the XpathNamespaceMap to convert
     * @param buf the StringBuilder to write to
     */
    public void serializeNamespaceMap(
            XpathNamespaceMap dict,
            StringBuilder     buf )
    {
        Iterator<String> iter = dict.prefixIterator();
        String sep = "";
        while( iter.hasNext() ) {
            String prefix = iter.next();
            String value  = dict.getSubjectAreaFor( prefix ).toExternalForm();

            buf.append( sep );
            buf.append( prefix );
            buf.append( "=" );
            buf.append( value );

            sep = ",";
        }
    }

    /**
     * Helper method to unserialize a XpathNamespaceMap.
     *
     * @param raw the serialized String
     * @return the unserialized XPathNamespaceMap
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public XpathNamespaceMap unserializeNamespaceMap(
            String    raw,
            String [] traversalTerms )
        throws
            TraversalTranslatorException
    {
        if( raw == null ) {
            return new SimpleXpathNamespaceMap();
        }
        raw = raw.trim();
        if( raw.length() == 0 ) {
            return null;
        }
        SimpleXpathNamespaceMap ret = SimpleXpathNamespaceMap.create();

        String [] parts = raw.split( "," );
        for( int i=0 ; i<parts.length ; ++i ) {
            int equals = parts[i].indexOf( '=' );
            if( equals < 0 ) {
                throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Syntax error in serialized namespace map" );
            }
            String name  = parts[i].substring( 0, equals );
            String value = parts[i].substring( equals+1 );

            ModelBase modelBase = theMeshBase.getModelBase();

            try {
                SubjectArea sa = modelBase.findSubjectAreaByIdentifier( modelBase.getMeshTypeIdentifierFactory().fromExternalForm( value ));

                ret.put( name, sa );
            } catch( MeshTypeWithIdentifierNotFoundException ex ) {
                throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, ex );
            }
        }
        return ret;
    }

    /**
     * Helper method to unserialize a TraversalSpecification.
     *
     * @param startObject the start MeshObject for the traversal
     * @param raw the serialized String
     * @param dict the dictionary to use
     * @return the unserialized TraversalSpecification
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public TraversalSpecification unserializeTraversalSpecification(
            MeshObject        startObject,
            String            raw,
            String []         traversalTerms,
            XpathNamespaceMap dict )
        throws
            TraversalTranslatorException
    {
        ArrayList<String> components = new ArrayList<String>();

        int start            = 0;
        int parenthesisLevel = 0;
        boolean foundAxis = false;
        boolean foundAlt  = false;
        for( int i=0 ; i<raw.length() ; ++i ) {
            switch( raw.charAt( i ) ) {
                case PARENTHESIS_OPEN:
                    ++parenthesisLevel;
                    break;
                case PARENTHESIS_CLOSE:
                    --parenthesisLevel;
                    if( parenthesisLevel < 0 ) {
                        throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot close more parentheses than are open" );
                    }
                    break;
                case AXIS_SEPARATOR:
                    if( parenthesisLevel == 0 ) {
                        if( foundAlt ) {
                            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Use parenthesis to disambiguate precedence between " + AXIS_SEPARATOR + " and " + ALTERNATIVE_SEPARATOR );
                        }
                        foundAxis = true;
                        components.add( subexpression( raw, start, i ) );
                        start = i+1;
                    }
                    break;
                case ALTERNATIVE_SEPARATOR:
                    if( parenthesisLevel == 0 ) {
                        if( foundAxis ) {
                            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Use parenthesis to disambiguate precedence between " + AXIS_SEPARATOR + " and " + ALTERNATIVE_SEPARATOR );
                        }
                        foundAlt = true;
                        components.add( subexpression( raw, start, i ) );
                        start = i+1;
                    }
                    break;
                default:
                    // no op
                    break;
            }
        }
        components.add( subexpression( raw, start, raw.length() ));

        if( components.size() > 1 ) {
            TraversalSpecification [] steps = new TraversalSpecification[ components.size() ];
            for( int i=0 ; i<steps.length ; ++i ) {
                steps[i] = unserializeTraversalSpecification( startObject, components.get( i ), traversalTerms, dict );
            }
            if( foundAxis ) {
                return SequentialCompoundTraversalSpecification.create( steps );

            } else if( foundAlt ) {
                return AlternativeCompoundTraversalSpecification.create( steps );
                
            } else {
                log.error( "Unexpected in " + raw );
                return null;
            }
        }

        String    singleComponent = components.get( 0 );
        String [] triple          = parseSinglePathComponent( singleComponent, traversalTerms );

        if( triple[0] != null || triple[2] != null ) {
            MeshObjectSelector     startSelector = triple[0] != null ? qualifierToMeshObjectSelector( triple[0], traversalTerms, dict ) : null;
            MeshObjectSelector     endSelector   = triple[2] != null ? qualifierToMeshObjectSelector( triple[2], traversalTerms, dict ) : null;
            TraversalSpecification qualified     = unserializeTraversalSpecification( startObject, triple[1], traversalTerms, dict );

            return SelectiveTraversalSpecification.create(
                    startSelector,
                    qualified,
                    endSelector );
        }
        if( ALL_NEIGHBORS.equals( triple[1] )) {
            return AllNeighborsTraversalSpecification.create();
        }
        if( STAY_RIGHT_HERE.equals( triple[1] )) {
            return StayRightHereTraversalSpecification.create();
        }

        RoleType rt = (RoleType) findMeshType( triple[1], traversalTerms, dict );
        return rt;
    }

    /**
     * Helper method to clean up a sub-expression string.
     *
     * @param raw the raw String
     * @param start start index (inclusive)
     * @param end end index (exclusive)
     * @return cleaned-up version
     */
    protected static String subexpression(
            String raw,
            int    start,
            int    end )
    {
        String ret = raw.substring( start, end );
        ret = ret.trim();
        if( ret.charAt( 0 ) == PARENTHESIS_OPEN && ret.charAt( ret.length()-1 ) == PARENTHESIS_CLOSE ) {
            ret = ret.substring( 1, ret.length()-1 );
            ret = ret.trim();
        }
        return ret;
    }

    /**
     * Parse a pre-condition/node/post-condition triple.
     *
     * @param raw the String
     * @return array of 3 elements
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    protected static String[] parseSinglePathComponent(
            String    raw,
            String [] traversalTerms )
        throws
            TraversalTranslatorException
    {
        raw = raw.trim();

        String [] ret = new String[3];
        int currentSection = -1;
        int bracketLevel   = 0;
        int start          = 0;

        for( int i=0 ; i<raw.length() ; ++i ) {
            switch( raw.charAt( i )) {
                case QUALIFIER_OPEN:
                    ++bracketLevel;
                    if( currentSection == -1 ) {
                        currentSection = 0;
                    } else if( currentSection == 1 ) {
                        currentSection = 2;
                        ret[1] = raw.substring( start, i );
                        start = i+1;
                    }
                    break;
                case QUALIFIER_CLOSE:
                    if( currentSection == -1 || currentSection == 1 ) {
                        throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot close ] before open [" );
                    } else if( currentSection == 0 ) {
                        --bracketLevel;
                        if( bracketLevel < 0 ) {
                            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot close more brackets than are open" );
                        } else if( bracketLevel == 0 ) {
                            currentSection = 1;
                            ret[0] = raw.substring( start, i );
                            start = i+1;
                        }
                    } else if( currentSection == 2 ) {
                        --bracketLevel;
                        if( bracketLevel < 0 ) {
                            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot close more brackets than are open" );
                        } else if( bracketLevel == 0 ) {
                            currentSection = 3;
                            ret[2] = raw.substring( start, i );
                            start = i+1;
                            if( start < raw.length() ) {
                                throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Leftover characters at end of qualifier" );
                            }
                        }
                    }
                    break;
                default:
                    if( currentSection == -1 ) {
                        currentSection = 1;
                    }
            }
        }
        if( currentSection == 1 ) {
            ret[1] = raw.substring( start );
        }
        return ret;
    }

    /**
     * Convert a qualifier expression back into a MeshObjectSelector.
     *
     * @param raw the expression
     * @param namespaceMap the interpretation of the prefixes contained in the pseudoXpath
     * @return the MeshObjectSelector
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public static MeshObjectSelector qualifierToMeshObjectSelector(
            String            raw,
            String []         traversalTerms,
            XpathNamespaceMap namespaceMap )
        throws
            TraversalTranslatorException
    {
        raw = raw.trim();

        if( raw.startsWith( HAS_TYPE_FUNCTION + PARENTHESIS_OPEN ) && raw.charAt( raw.length()-1 ) == PARENTHESIS_CLOSE ) {
            String middle = raw.substring( HAS_TYPE_FUNCTION.length() + 1, raw.length()-1 ).trim();
            if( middle.startsWith( ".," )) {
                String     typeString = middle.substring( 2 );
                EntityType type       = (EntityType) findMeshType( typeString, traversalTerms, namespaceMap );

                return ByTypeMeshObjectSelector.create( type, true );
            }
        } else if( raw.startsWith( HAS_EXACT_TYPE_FUNCTION + PARENTHESIS_OPEN ) && raw.charAt( raw.length()-1 ) == PARENTHESIS_CLOSE ) {
            String middle = raw.substring( HAS_EXACT_TYPE_FUNCTION.length() + 1, raw.length()-1 ).trim();
            if( middle.startsWith( ".," )) {
                String     typeString = middle.substring( 2 );
                EntityType type       = (EntityType) findMeshType( typeString, traversalTerms, namespaceMap );

                return ByTypeMeshObjectSelector.create( type, false );
            }
        }
        throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Sorry, can't understand your MeshObjectSelector (this is not a very complete implementation)" );
    }

    /**
     * Helper method to find a MeshType given as String.
     *
     * @param raw the String
     * @param namespaceMap the interpretation of the prefixes contained in the pseudoXpath
     * @return the MeshType, or null
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    protected static CollectableMeshType findMeshType(
            String            raw,
            String []         traversalTerms,
            XpathNamespaceMap namespaceMap )
        throws
            TraversalTranslatorException
    {
        int colon = raw.indexOf( COLON );
        if( colon < 0 ) {
            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "No " + COLON + " in " + raw );
        }
        String prefix = raw.substring( 0, colon );
        String name   = raw.substring( colon+1 );

        SubjectArea sa = namespaceMap.getSubjectAreaFor( prefix );
        if( sa == null ) {
            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot find SubjectArea corresponding to prefix " + prefix );
        }
        CollectableMeshType ret = sa.findCollectableMeshTypeByName( StringValue.create( name ));
        if( ret == null ) {
            throw new TermInvalidTraversalTranslatorException( traversalTerms, raw, "Cannot find CollectableMeshType '" + name + "' in SubjectArea " + sa.toExternalForm() );
        }
        return ret;
    }

    /**
     * Tag in the traversalTerm that indicates an XPath.
     */
    public static final String XPATH_TAG = "xpath:";

    /**
     * Tag in the traversalTerm that indicates an XPath dictionary.
     */
    public static final String DICT_TAG = "xpath-dict:";

    /**
     * Separates function arguments.
     */
    public static final char COMMA = ',';

    /**
     * Separates prefix and name.
     */
    public static final char COLON = ':';

    /**
     * Indicates any traversal.
     */
    public static final String ALL_NEIGHBORS = "*";

    /**
     * Indicates traversal to self.
     */
    public static final String STAY_RIGHT_HERE = ".";

    /**
     * Separates the components of the Xpath.
     */
    public static final char AXIS_SEPARATOR = '/';

    /**
     * Separates the alternatives in a step.
     */
    public static final char ALTERNATIVE_SEPARATOR = '|';

    /**
     * Opens up a qualifier.
     */
    public static final char QUALIFIER_OPEN = '[';

    /**
     * Closes a qualifier.
     */
    public static final char QUALIFIER_CLOSE = ']';

    /**
     * Open parenthesis.
     */
    public static final char PARENTHESIS_OPEN = '(';

    /**
     * Close parenthesis.
     */
    public static final char PARENTHESIS_CLOSE = ')';

    /**
     * Logical inversion.
     */
    public static final char NOT = '!';

    /**
     * Has-type function.
     */
    public static final String HAS_TYPE_FUNCTION = "has-type";

    /**
     * Has-exact-type function.
     */
    public static final String HAS_EXACT_TYPE_FUNCTION = "has-exact-type";
}
