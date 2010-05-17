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

package org.infogrid.model.traversal;

import java.text.ParseException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.MeshObjectAccessException;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.util.ArrayHelper;

/**
 * Collects functionality common to many implementations of TraversalTranslator.
 */
public abstract class AbstractTraversalTranslator
        implements
            TraversalTranslator
{
    /**
     * Constructor for subclasses only.
     *
     * @param mb the MeshBase to use
     */
    protected AbstractTraversalTranslator(
            MeshBase mb )
    {
        theMeshBase = mb;
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
    public TraversalSpecification [] translateTraversalSpecifications(
            MeshObject startObject,
            String []  traversalTerms )
        throws
            TraversalTranslatorException
    {
        TraversalSpecification found = translateTraversalSpecification( startObject, traversalTerms );
        if( found != null ) {
            return new TraversalSpecification[] { found };
        } else {
            return null;
        }
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
    public String [] translateTraversalSpecifications(
            MeshObject                startObject,
            TraversalSpecification [] traversals )
        throws
            TraversalTranslatorException
    {
        String [][] almost = new String[ traversals.length ][];
        int count = 0;
        for( int i=0 ; i<traversals.length ; ++i ) {
            almost[i] = translateTraversalSpecification( startObject, traversals[i] );
            count += almost[i].length;
        }

        String [] ret = new String[ count ];
        for( int i=almost.length-1 ; i>=0 ; --i ) {
            for( int j=almost[i].length-1 ; j>=0 ; --j ) {
                ret[--count] = almost[i][j];
            }
        }
        return ret;
    }

    /**
     * Translate the String form of a TraversalPath into an actual TraversalPath.
     *
     * @param start the start MeshObject of the TraversalPath
     * @param traversalTerms the terms to be translated
     * @return the TraversalPath, or null if not found.
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public TraversalPath translateTraversalPath(
            MeshObject start,
            String []  traversalTerms )
        throws
            TraversalTranslatorException
    {
        String reachedString = findTermWithTag( REACHED_TAG, traversalTerms );
        if( reachedString == null ) {
            throw new TermMissingTraversalTranslatorException( REACHED_TAG );
        }
        // don't do String.split, it might ignore empty strings.

        String [] reachedComponents = mySplit( reachedString, ',' );

        TraversalSpecification spec = translateTraversalSpecification( start, traversalTerms );

        MeshObjectIdentifierFactory idFact = theMeshBase.getMeshObjectIdentifierFactory();

        try {
            TraversalPath ret;
            if( reachedComponents.length > 1 ) {
                if( !( spec instanceof SequentialCompoundTraversalSpecification )) {
                    throw new TraversalTranslatorException( "Components: " + reachedComponents.length + ", TraversalSpec: " + spec );
                }
                SequentialCompoundTraversalSpecification realSpec = (SequentialCompoundTraversalSpecification) spec;
                if( realSpec.getStepCount() != reachedComponents.length ) {
                    throw new TraversalTranslatorException( "Components: " + reachedComponents.length + ", TraversalSpec steps: " + realSpec.getStepCount() );
                }
                MeshObject [] reached = new MeshObject[ reachedComponents.length ];
                for( int i=0 ; i<reached.length ; ++i ) {
                    MeshObjectIdentifier identifier = idFact.fromExternalForm( reachedComponents[i] );
                    reached[i] = theMeshBase.accessLocally( identifier );
                    if( reached[i] == null ) {
                        throw new TraversalTranslatorException( "Cannot find MeshObject: " + identifier );
                    }
                }
                ret = TraversalPath.create( realSpec.getSteps(), reached );

            } else {
                MeshObjectIdentifier identifier = idFact.fromExternalForm( reachedComponents[0] );
                MeshObject reached = theMeshBase.accessLocally( identifier );

                ret = TraversalPath.create( spec, reached );
            }
            return ret;

        } catch( ParseException ex ) {
            throw new TraversalTranslatorException( ex );

        } catch( MeshObjectAccessException ex ) {
            throw new TraversalTranslatorException( ex );

        } catch( NotPermittedException ex ) {
            throw new TraversalTranslatorException( ex );
        }
    }

    /**
     * Helper method to split a String by a separator without ignoring empty Strings.
     *
     * @param in the String to split
     * @param s the separator
     * @return the components
     */
    protected String [] mySplit(
            String in,
            char   s )
    {
        int count = 1;
        for( int i=0 ; i<in.length() ; ++i ) {
            if( in.charAt( i ) == s ) {
                ++count;
            }
        }

        String [] ret = new String[count];
        count = 0;
        int start = 0;
        for( int i=0 ; i<in.length() ; ++i ) {
            if( in.charAt( i ) == s ) {
                ret[count++] = in.substring( start, i );
                ++i;
                start = i;
            }
        }
        ret[count++] = in.substring( start );
        return ret;
    }

    /**
     * Translate an actual TraversalPath into String form.
     *
     * @param start the start MeshObject of the TraversalPath
     * @param path the TraversalPath
     * @return the external form
     * @throws TraversalTranslatorException thrown if the translation failed
     */
    public String [] translateTraversalPath(
            MeshObject    start,
            TraversalPath path )
        throws
            TraversalTranslatorException
    {
        String [] almost = translateTraversalSpecification( start, path.getTraversalSpecification() );

        MeshObjectIdentifier [] reached = path.getMeshObjectIdentifiersInSequence();

        StringBuilder reachedString = new StringBuilder();
        reachedString.append( REACHED_TAG );
        serializeMeshObjectIdentifiers( reached, reachedString );

        String [] ret = ArrayHelper.append( almost, reachedString.toString(), String.class );
        return ret;
    }

    /**
     * Helper method to convert a sequence of reached MeshObjects into the right term.
     *
     * @param reached the reached MeshObjects,
     * @param buf the StringBuilder to write to
     */
    protected void serializeMeshObjectIdentifiers(
            MeshObjectIdentifier [] reached,
            StringBuilder           buf )
    {
        for( int i=0 ; i<reached.length ; ++i ) {
            if( i>0 ) {
                buf.append( "," );
            }
            buf.append( reached[i].toExternalForm().replace( ",", "%2C" ));
        }
    }

    /**
     * Find the term that corresponds to this tag. Strip the tag
     *
     * @param tag the tag
     * @param terms the terms
     * @return the found term, or null
     */
    protected String findTermWithTag(
            String    tag,
            String [] terms )
    {
        if( terms == null ) {
            return null;
        }
        for( int i=0 ; i<terms.length ; ++i ) {
            if( terms[i].startsWith( tag )) {
                return terms[i].substring( tag.length() );
            }
        }
        return null;
    }

    /**
     * The MeshBase to use.
     */
    protected MeshBase theMeshBase;

    /**
     * Tag in the traversalTerm that indicates a reached MeshObjectIdentifier.
     */
    public static final String REACHED_TAG = "reached:";

}
