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

package org.infogrid.mesh.set;

import org.infogrid.mesh.MeshObject;

/**
  * This interface allows us to define selection criteria for MeshObjects. For example, it is
  * used as a parameter to subsetting methods.
  */
public interface MeshObjectSelector
{
    /**
      * Returns true if this MeshObject shall be selected.
      *
      * @param candidate the MeshObject that shall be tested
      * @return true of this MeshObject shall be selected
      */
    public abstract boolean accepts(
            MeshObject candidate );
}
