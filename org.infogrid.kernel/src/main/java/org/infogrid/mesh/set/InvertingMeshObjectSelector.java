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

import org.infogrid.mesh.MeshObject;

/**
 * A MeshObjectSelector that selects the opposite of what a delegate MeshObjectSelector
 * would select.
 */
public class InvertingMeshObjectSelector
        implements
            MeshObjectSelector
{
    /**
     * Factory method.
     *
     * @param delegate the delegate
     * @return the created InvertingMeshObjectSelector
     */
    public static InvertingMeshObjectSelector create(
            MeshObjectSelector delegate )
    {
        return new InvertingMeshObjectSelector( delegate );
    }

    /**
     * Constructor.
     *
     * @param delegate the delegate
     */
    protected InvertingMeshObjectSelector(
            MeshObjectSelector delegate )
    {
        theDelegate = delegate;
    }
    
    /**
      * Returns true if this MeshObject shall be selected.
      *
      * @param candidate the MeshObject that shall be tested
      * @return true of this MeshObject shall be selected
      */
    public boolean accepts(
            MeshObject candidate )
    {
        boolean ret = theDelegate.accepts( candidate );
        return !ret;
    }

    /**
     * Obtain the delegate MeshObjectSelector.
     *
     * @return the delegate MeshObjectSelector
     */
    public MeshObjectSelector getDelegate()
    {
        return theDelegate;
    }

    /**
     * Determine equality.
     *
     * @param other the Object to compare against
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( other instanceof InvertingMeshObjectSelector ) {
            InvertingMeshObjectSelector realOther = (InvertingMeshObjectSelector) other;

            if( !theDelegate.equals( realOther.theDelegate )) {
                return false;
            }

            return true;
        }
        return false;
    }

    /**
     * Hash code.
     *
     * @return hash code.
     */
    @Override
    public int hashCode()
    {
        return theDelegate.hashCode();
    }

    /**
     * The delegate MeshObjectSelector.
     */
    protected MeshObjectSelector theDelegate;
}
