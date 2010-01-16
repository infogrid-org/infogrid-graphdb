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

package org.infogrid.modelbase.m;

import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.util.AbstractIdentifier;
import org.infogrid.util.text.IdentifierStringifier;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationContext;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * MeshTypeIdentifier implementation for MModelBase.
 */
public class MMeshTypeIdentifier
        extends
             AbstractIdentifier
        implements
             MeshTypeIdentifier
{
    /**
     * Factory method.
     *
     * @param s the String
     * @return the created MMeshTypeIdentifier
     */
    public static MMeshTypeIdentifier create(
            String s )
    {
        if( s.indexOf( '#' ) >= 0 ) {
            throw new IllegalArgumentException( "Hashes in MeshTypeIdentifiers are no longer allowed: " + s );
        }

        return new MMeshTypeIdentifier( s );
    }

    /**
     * Constructor.
     *
     * @param s the String
     */
    protected MMeshTypeIdentifier(
            String s )
    {
        theString = s;
    }
    
    /**
     * Convert to external form.
     *
     * @return the external form
     */
    public String toExternalForm()
    {
        return theString;
    }

    /**
     * Create a derived MeshTypeIdentifier.
     *
     * @param suffix the suffix to append
     * @return the created MeshTypeIdentifier
     */
    public MeshTypeIdentifier createDerivedMeshTypeIdentifier(
            String suffix )
    {
        return new MMeshTypeIdentifier( theString + suffix );
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @param pars collects parameters that may influence the String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return String representation
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationContext    context,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String extForm = IdentifierStringifier.defaultFormat( toExternalForm(), pars );
        return rep.formatEntry( getClass(), DEFAULT_ENTRY, pars, extForm );
    }

    /**
     * Obtain the start part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     *
     * @param additionalArguments additional arguments for URLs, if any
     * @param target the HTML target, if any
     * @param title title of the HTML link, if any
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return String representation
     */
    public String toStringRepresentationLinkStart(
            String                      additionalArguments,
            String                      target,
            String                      title,
            StringRepresentation        rep,
            StringRepresentationContext context )
        throws
            StringifierException
    {
        return "";
    }

    /**
     * Obtain the end part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     * 
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     * @return String representation
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation        rep,
            StringRepresentationContext context )
        throws
            StringifierException
    {
        return "";
    }

    /**
     * Convert to String. For debugging.
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return toExternalForm();
    }

    /**
     * Default hashCode implementation.
     *
     * @return hashCode
     */
    @Override
    public int hashCode()
    {
        return toExternalForm().hashCode();
    }

    /**
     * Default equals implementation.
     *
     * @param other the Object to compare with
     * @return true if the Objects equal each other
     */
    @Override
    public boolean equals(
            Object other )
    {
        if( !( other instanceof MeshTypeIdentifier )) {
            return false;
        }
        
        MeshTypeIdentifier realOther = (MeshTypeIdentifier) other;
        
        boolean ret = toExternalForm().equals( realOther.toExternalForm() );
        return ret;
    }
    
    /**
     * The underlying String.
     */
    protected String theString;

    /**
     * The default entry in the resouce files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_ENTRY = "String";
}
