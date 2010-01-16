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

package org.infogrid.mesh.set;

import org.infogrid.meshbase.MeshObjectsNotFoundException;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.traversal.TraversalPath;
import org.infogrid.util.logging.Log;

/**
 * A simple implementation of TraversalPathSelector that accepts all TraversalPaths
 * whose first and/or last MeshObject matches a certain type (or subtype).
 */
public class ByTypeTraversalPathSelector
        implements
            TraversalPathSelector
{
    private static final Log log = Log.getLogInstance( ByTypeTraversalPathSelector.class ); // our own, private logger

    /**
     * Factory method.
     * 
     * @param firstType the type we are looking for in the first MeshObject of the path -- null matches all
     * @param firstSubtypeAllowed  if true, we could also be an instance of a subtype of firstType
     * @param lastType the type we are looking for in the last MeshObject of the path -- null matches all
     * @param lastSubtypeAllowed  if true, we could also be an instance of a subtype of lastType
     * @return the created ByTypeTraversalPathSelector
     */
    public static ByTypeTraversalPathSelector create(
            AttributableMeshType firstType,
            boolean              firstSubtypeAllowed,
            AttributableMeshType lastType,
            boolean              lastSubtypeAllowed )
    {
        return new ByTypeTraversalPathSelector( firstType, firstSubtypeAllowed, lastType, lastSubtypeAllowed );
    }

    /**
     * Construct one with the types that we are looking for.
     *
     * @param firstType the type we are looking for in the first MeshObject of the path -- null matches all
     * @param firstSubtypeAllowed  if true, we could also be an instance of a subtype of firstType
     * @param lastType the type we are looking for in the last MeshObject of the path -- null matches all
     * @param lastSubtypeAllowed  if true, we could also be an instance of a subtype of lastType
     */
    protected ByTypeTraversalPathSelector(
            AttributableMeshType firstType,
            boolean              firstSubtypeAllowed,
            AttributableMeshType lastType,
            boolean              lastSubtypeAllowed )
    {
        theFirstType           = firstType;
        theFirstSubtypeAllowed = firstSubtypeAllowed;
        theLastType            = lastType;
        theLastSubtypeAllowed  = lastSubtypeAllowed;
    }

    /**
     * Returns true if a candidate TraversalPath shall be accepted.
     *
     * @param candidate the candidate TraversalPath
     * @return true if this candidate shall be accepted
     */
    public boolean accepts(
            TraversalPath candidate )
    {
        if( candidate == null ) {
            throw new IllegalArgumentException();
        }

        AttributableMeshType [] candidateFirstTypes;
        try {
            candidateFirstTypes = candidate.getFirstMeshObject().getTypes();

        } catch( MeshObjectsNotFoundException ex ) {
            log.error( ex );
            return false;
        }
        if( theFirstType != null ) {
            boolean found = false;
            if( theFirstSubtypeAllowed ) {
                for( int i=0 ; i<candidateFirstTypes.length ; ++i ) {
                    if( candidateFirstTypes[i].isSubtypeOfOrEquals( theFirstType )) {
                        found = true;
                        break;
                    }
                }
            } else {
                for( int i=0 ; i<candidateFirstTypes.length ; ++i ) {
                    if( candidateFirstTypes[i].equals( theFirstType )) {
                        found = true;
                        break;
                    }
                }
            }
            if( !found ) {
                return false;
            }
        }

        AttributableMeshType [] candidateLastTypes = candidate.getLastMeshObject().getTypes();
        if( theLastType != null ) {
            boolean found = false;
            if( theLastSubtypeAllowed ) {
                for( int i=0 ; i<candidateLastTypes.length ; ++i ) {
                    if( candidateLastTypes[i].isSubtypeOfOrEquals( theLastType )) {
                        found = true;
                        break;
                    }
                }
            } else {
                for( int i=0 ; i<candidateLastTypes.length ; ++i ) {
                    if( candidateLastTypes[i].equals( theLastType )) {
                        found = true;
                        break;
                    }
                }
            }
            if( !found ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Serialize into external form.
     *
     * @return external form
     * @throws UnsupportedOperationException always thrown
     */
    public String toExternalForm()
    {
        throw new UnsupportedOperationException( "FIXME" );
    }

    /**
     * The AttributableMeshType against which we compare our first MeshObject.
     */
    protected AttributableMeshType theFirstType;

    /**
     * The AttributableMeshType against which we compare our last MeshObject.
     */
    protected AttributableMeshType theLastType;

    /**
     * Do we allow a subtype instance or only instances of the exact type for the first MeshObject.
     */
    protected boolean theFirstSubtypeAllowed;

    /**
     * Do we allow a subtype instance or only instances of the exact type for the last MeshObject.
     */
    protected boolean theLastSubtypeAllowed;
}
