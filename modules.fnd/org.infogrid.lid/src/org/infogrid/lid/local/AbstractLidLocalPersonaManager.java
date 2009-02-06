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

package org.infogrid.lid.local;

import org.infogrid.util.Identifier;
import org.infogrid.util.SimpleStringIdentifier;
import org.infogrid.util.http.SaneRequest;

/**
 * Factors out common functionality of LidIdentityManagers.
 */
public abstract class AbstractLidLocalPersonaManager
        implements
            LidLocalPersonaManager
{
    /**
     * Find the LidResource, or null.
     * 
     * @param request the incoming request
     * @return the found LidResource, or null
     * @throws LidLocalPersonaUnknownException thrown if the resource could not be found
     */
    public LidLocalPersona findFromRequest(
            SaneRequest request )
        throws
            LidLocalPersonaUnknownException
    {
        String          identifier = request.getAbsoluteBaseUri();
        Identifier      realId     = SimpleStringIdentifier.create( identifier );
        LidLocalPersona ret        = find( realId );
        return ret;
    }
}
