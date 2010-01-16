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

package org.infogrid.meshbase;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.util.AbstractLocalizedRuntimeException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Indicates a violation in the multiplicity of a RoleType.
 */
public class MultiplicityException
        extends
            AbstractLocalizedRuntimeException
        implements
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Construct one.
     *
     * @param meshObject the MeshObject where we discovered the multiplicity violation
     * @param roleType the RoleType for which it happened
     */
    public MultiplicityException(
            MeshObject meshObject,
            RoleType   roleType )
    {
        theMeshObject           = meshObject;
        theMeshObjectIdentifier = meshObject.getIdentifier();
        theRoleType             = roleType;
        theRoleTypeIdentifier   = roleType.getIdentifier();
    }

    /**
     * Obtain the parameters with which the internationalized string
     * will be parameterized.
     *
     * @return the parameters with which the internationalized string will be parameterized
     */
    public Object [] getLocalizationParameters()
    {
        Object [] ret = { theMeshObjectIdentifier, theRoleTypeIdentifier };

        return ret;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "theMeshObjectIdentifier",
                    "theRoleTypeIdentifier"
                },
                new Object[] {
                    theMeshObjectIdentifier,
                    theRoleTypeIdentifier
                });
    }

    /**
     * The MeshObject for which we discovered a violation.
     */
    protected transient MeshObject theMeshObject;

    /**
     * The identifier of the MeshObject for which we discovered a violation.
     */
    protected MeshObjectIdentifier theMeshObjectIdentifier;

    /**
     * The RoleType for which we discovered a violation.
     */
    protected transient RoleType theRoleType;
    
    /**
     * The identifier of the RoleType for which we discovered a violation.
     */
    protected MeshTypeIdentifier theRoleTypeIdentifier;
}
