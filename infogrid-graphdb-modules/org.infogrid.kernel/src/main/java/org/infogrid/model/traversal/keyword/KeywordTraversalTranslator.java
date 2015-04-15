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

package org.infogrid.model.traversal.keyword;

import java.util.Map;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.AbstractTraversalTranslator;
import org.infogrid.model.traversal.TermNotFoundTraversalTranslatorException;
import org.infogrid.model.traversal.TraversalSpecification;
import org.infogrid.model.traversal.TraversalTranslatorException;
import org.infogrid.model.traversal.UnknownTermTraversalTranslatorException;
import org.infogrid.model.traversal.WrongNumberTermsTraversalTranslatorException;

/**
 * A TraversalTranslator that uses static tables of keywords to describe TraversalSpecifications.
 */
public class KeywordTraversalTranslator
        extends
            AbstractTraversalTranslator
{
    /**
     * Constructor.
     * 
     * @param mb the MeshBase to use
     * @param forwardTable forward direction of the translation
     * @param backwardTable backward direction of the translation
     */
    public KeywordTraversalTranslator(
            MeshBase                           mb,
            Map<String,TraversalSpecification> forwardTable,
            Map<TraversalSpecification,String> backwardTable )
    {
        super( mb );

        theForwardTable  = forwardTable;
        theBackwardTable = backwardTable;
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
        if( traversalTerms.length != 1 ) {
            throw new WrongNumberTermsTraversalTranslatorException( traversalTerms );
        }
        TraversalSpecification ret = theForwardTable.get( traversalTerms[0] );
        if( ret == null ) {
            throw new UnknownTermTraversalTranslatorException( traversalTerms, traversalTerms[0] );
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
        String ret = theBackwardTable.get( traversal );
        if( ret == null ) {
            throw new TermNotFoundTraversalTranslatorException( traversal );
        }
        return new String[] { ret };
    }

    /**
     * <p>Check that the named value is one of the valid values,
     * and it not, set it to the default. The default is the first value in the
     * array of allowedValues.</p>
     *
     * <p>This depends on a good implementation of equals().</p>
     *
     * @param candidateValue the candidate value
     * @param allowedValues the set of allowed values
     * @return the approved value
     * @param <T> type parameter
     */    
    protected static <T> T correct(
            T    candidateValue,
            T [] allowedValues )
    {
        for( T current : allowedValues ) {
            if( candidateValue == null ) {
                if( current == null ) {
                    return current; // fine
                }
            } else if( candidateValue.equals( current )) {
                return current; // fine
            }
        }
        return allowedValues[0];
    }
    
    /**
     * The translation table in one direction.
     */
    protected Map<String,TraversalSpecification> theForwardTable;
    
    /**
     * The translation table in the other direction.
     */
    protected Map<TraversalSpecification,String> theBackwardTable;
}
