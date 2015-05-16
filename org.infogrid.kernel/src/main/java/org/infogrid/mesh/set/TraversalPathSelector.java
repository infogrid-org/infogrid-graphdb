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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.mesh.set;

import org.infogrid.model.traversal.TraversalPath;

/**
  * This interface allows us to indicate whether or not a certain TraversalPath
  * matches a selection criteria.
  */
public interface TraversalPathSelector
{
     /**
      * Returns true if a candidate TraversalPath shall be accepted.
      *
      * @param candidate the candidate TraversalPath
      * @return true if this candidate shall be accepted
      */
    public abstract boolean accepts(
            TraversalPath candidate );
}
