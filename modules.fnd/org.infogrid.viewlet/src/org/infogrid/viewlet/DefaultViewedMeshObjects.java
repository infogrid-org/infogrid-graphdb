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

package org.infogrid.viewlet;

import java.util.Map;
import org.infogrid.mesh.MeshObject;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.model.traversal.TraversalSpecification;

/**
 * This is the default implementation of ViewedMeshObjects. This is useful for
 * a wide variety of Viewlets.
 */
public class DefaultViewedMeshObjects
        extends
            AbstractViewedMeshObjects
{
    /**
     * Constructor, specifying a default TraversalSpecification.
     *
     * @param mb the MeshBase from which the viewed MeshObjects are taken
     * @param traversal the default TraversalSpecification
     */
    public DefaultViewedMeshObjects(
            MeshBase               mb,
            TraversalSpecification traversal )
    {
        super( mb );
        
        theDefaultTraversalSpecification = traversal;
        theTraversalSpecification        = traversal;
    }

    /**
     * Constructor. Initializes to empty content.
     *
     * @param mb the MeshBase from which the viewed MeshObjects are taken
     */
    public DefaultViewedMeshObjects(
            MeshBase mb )
    {
        super( mb );
    }
    
    /**
     * Through this method, the Viewlet that this object belongs to updates this object.
     *
     * @param subject the new subject of the Viewlet
     * @param subjectParameters the parameters of the newly selected subject, if any
     * @param viewletParameters the parameters of the Viewlet, if any
     * @param traversal the TraversalSpecification currently in effect on the Viewlet, if any
     */
    @Override
    public void update(
            MeshObject             subject,
            Map<String,Object[]>   subjectParameters,
            Map<String,Object[]>   viewletParameters,
            TraversalSpecification traversal )
    {
        super.update( subject, subjectParameters, viewletParameters, traversal );

        if( theTraversalSpecification == null ) {
            // insert default
            theTraversalSpecification = theDefaultTraversalSpecification;
        }
    }

    /**
     * The default TraversalSpecification, to be used if no other TraversalSpecification
     * has been given.
     */
    protected TraversalSpecification theDefaultTraversalSpecification;
}
