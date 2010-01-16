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

package org.infogrid.model.traversal;

import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.modelbase.ModelBase;

/**
 * Factors out functionality that is common to many types of TraversalDictionary.
 */
public abstract class AbstractTraversalDictionary
    implements
        TraversalDictionary
{
    /**
     * Constructor.
     * 
     * @param modelBase the ModelBase to use
     */
    protected AbstractTraversalDictionary(
            ModelBase modelBase )
    {
        theModelBase = modelBase;
    }
    
    /**
     * Translate the String form of a TraversalSpecification into an actual TraversalSpecification.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversalTerm the term to be translated
     * @return the TraversalSpecification, or null if not found.
     */
    public TraversalSpecification translate(
            MeshObject startObject,
            String     traversalTerm )
    {
        TraversalSpecification ret = theForwardTable.get( traversalTerm );
        return ret;
    }

    /**
     * Translate an actual TraversalSpecification into String form.
     *
     * @param startObject the start MeshObject for the traversal
     * @param traversal the TraversalSpecification
     * @return the external form
     */
    public String translate(
            MeshObject             startObject,
            TraversalSpecification traversal )
    {
        String ret = theBackwardTable.get( traversal );
        return ret;
    }

    /**
     * Enable subclasses to insert a translation entry into our tables.
     * 
     * @param term the term to be added
     * @param specification the TraversalSpecification to be added
     */
    protected void addTranslationEntry(
            String                 term,
            TraversalSpecification specification )
    {
        theForwardTable.put( term, specification );
        theBackwardTable.put( specification, term );
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
     * The ModelBase to use.
     */
    protected ModelBase theModelBase;
    
    /**
     * The translation table in one direction.
     */
    protected HashMap<String,TraversalSpecification> theForwardTable = new HashMap<String,TraversalSpecification>();
    
    /**
     * The translation table in the other direction.
     */
    protected HashMap<TraversalSpecification,String> theBackwardTable = new HashMap<TraversalSpecification,String>();
}
