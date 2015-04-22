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

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.util.ArrayHelper;

/**
 * This is data wanting to become a EntityType, during reading.
 */
public class ExternalizedEntityType
        extends
            ExternalizedAttributableMeshType
{
    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setRelativeIconPath(
            String newValue ) 
    {
        theRelativeIconPath = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String getRelativeIconPath()
    {
        return theRelativeIconPath;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIconMimeType(
            String newValue ) 
    {
        theIconMimeType = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String getIconMimeType()
    {
        return theIconMimeType;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addSuperType(
            MeshTypeIdentifier newValue ) 
    {
        theSuperTypes.add( newValue );
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public ArrayList<MeshTypeIdentifier> getSuperTypes()
    {
        return theSuperTypes;
    }

    /**
     * Add to the property.
     *
     * @param newValue the new value
     */
    public void addSynonym(
            MeshTypeIdentifier newValue )
    {
        theSynonyms.add( newValue );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public MeshTypeIdentifier [] getSynonyms()
    {
        return ArrayHelper.copyIntoNewArray( theSynonyms, MeshTypeIdentifier.class );
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setRawOverrideCode(
            String newValue ) 
    {
        theRawOverrideCode = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public String getRawOverrideCode()
    {
        return theRawOverrideCode;
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setIsSignificant(
            BooleanValue newValue ) 
    {
        isSignificant = newValue;
    }
    
    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getIsSignificant()
    {
        return isSignificant;
    }

    /**
     * Add to property.
     *
     * @param newValue the new value
     */
    public void addLocalEntityTypeGuardClassName(
            String newValue ) 
    {
        theLocalEntityTypeGuardClassNames.add( newValue );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getLocalEntityTypeGuardClassNames()
    {
        return ArrayHelper.copyIntoNewArray( theLocalEntityTypeGuardClassNames, String.class );
    }

    /**
     * Add a declared method.
     *
     * @param newValue the new value
     */
    public void addDeclaredMethod(
            String newValue )
    {
        theDeclaredMethods.add( newValue );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getDeclaredMethods()
    {
        return ArrayHelper.copyIntoNewArray( theDeclaredMethods, String.class );
    }

    /**
     * Add an implemented method.
     *
     * @param newValue the new value
     */
    public void addImplementedMethod(
            String newValue )
    {
        theImplementedMethods.add( newValue );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getImplementedMethods()
    {
        return ArrayHelper.copyIntoNewArray( theImplementedMethods, String.class );
    }

    /**
     * Set property.
     *
     * @param newValue the new value
     */
    public void setMayBeUsedAsForwardReference(
            BooleanValue newValue )
    {
        theMayBeUsedAsForwardReference = newValue;
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public BooleanValue getMayBeUsedAsForwardReference()
    {
        return theMayBeUsedAsForwardReference;
    }

    /**
     * Add an additional interface.
     * 
     * @param toAdd the fully-qualified interface name to add
     */
    public void addAdditionalInterface(
            String toAdd )
    {
        theAdditionalInterfaces.add( toAdd );
    }

    /**
     * Get property.
     *
     * @return the value
     */
    public String [] getAdditionalInterfaces()
    {
        return ArrayHelper.copyIntoNewArray( theAdditionalInterfaces, String.class );
    }

    /**
      * Relative icon path, if any.
      */
    protected String theRelativeIconPath = null;

    /**
     * Mime type of the icon, if any.
     */
    protected String theIconMimeType = null;

    /**
     * List of Identifiers identifying the supertypes.
     */
    protected ArrayList<MeshTypeIdentifier> theSuperTypes = new ArrayList<MeshTypeIdentifier>();

    /**
     * List of Strings containing the synonyms.
     */
    protected ArrayList<MeshTypeIdentifier> theSynonyms = new ArrayList<MeshTypeIdentifier>();
    
    /**
      * OverrideCode in raw form.
      */
    protected String theRawOverrideCode = null;

    /**
      * IsSignificant.
      */
    protected BooleanValue isSignificant = BooleanValue.FALSE;
    
    /**
     * MayBeUsedAsForwardReference.
     */
    protected BooleanValue theMayBeUsedAsForwardReference = BooleanValue.FALSE;

    /**
     * Names of the classes that become the EntityTypeGuards.
     */
    protected ArrayList<String> theLocalEntityTypeGuardClassNames = new ArrayList<String>();
    
    /**
     * Declared methods code.
     */
    protected ArrayList<String> theDeclaredMethods = new ArrayList<String>();
    
    /**
     * Implemented methods code.
     */
    protected ArrayList<String> theImplementedMethods = new ArrayList<String>();

    /**
     * Additional interfaces.
     */
    protected ArrayList<String> theAdditionalInterfaces = new ArrayList<String>();
}
