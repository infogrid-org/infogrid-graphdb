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

import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.util.AbstractIdentifier;
import org.infogrid.util.Identifier;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.text.IdentifierStringifier;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationContext;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * Identifies a MeshBase.
 */
public class MeshBaseIdentifier
        extends
             AbstractIdentifier
        implements
            Identifier,
            CanBeDumped
{
    /**
     * Constructor.
     * 
     * @param fact the MeshBaseIdentifierFactory with which this MeshBaseIdentifier was created
     * @param canonicalForm the canonical representation of this identifier
     */
    protected MeshBaseIdentifier(
            MeshBaseIdentifierFactory fact,
            String                    canonicalForm )
    {
        theFactory       = fact;
        theCanonicalForm = canonicalForm;
    }

    /**
     * Obtain the factory with which this MeshBaseIdentifier was created.
     *
     * @return the factory
     */
    public MeshBaseIdentifierFactory getFactory()
    {
        return theFactory;
    }

    /**
     * Obtain the canonical form of this identifier.
     *
     * @return the canonical form
     */
    public String getCanonicalForm()
    {
        return theCanonicalForm;
    }

    /**
     * For consistency with the Java APIs, this method is provided.
     *
     * @return the external form
     */
    public String toExternalForm()
    {
        return getCanonicalForm();
    }

    /**
     * Obtain a String representation of this instance that can be shown to the user.
     *
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @param pars collects parameters that may influence the String representation
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentation(
            StringRepresentation           rep,
            StringRepresentationContext    context,
            StringRepresentationParameters pars )
        throws
            StringifierException
    {
        String externalForm = IdentifierStringifier.defaultFormat( toExternalForm(), pars );

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                DEFAULT_ENTRY,
                pars,
                externalForm );
        return ret;
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
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
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
        String contextPath  = context != null ? (String) context.get( StringRepresentationContext.WEB_CONTEXT_KEY ) : null;
        String externalForm = toExternalForm();

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                DEFAULT_LINK_START_ENTRY,
                null,
        /* 0 */ contextPath,
        /* 1 */ externalForm,
        /* 2 */ additionalArguments,
        /* 3 */ target,
        /* 4 */ title );

        return ret;
    }

    /**
     * Obtain the end part of a String representation of this object that acts
     * as a link/hyperlink and can be shown to the user.
     * 
     * @param rep the StringRepresentation
     * @param context the StringRepresentationContext of this object
     * @return String representation
     * @throws StringifierException thrown if there was a problem when attempting to stringify
     */
    public String toStringRepresentationLinkEnd(
            StringRepresentation        rep,
            StringRepresentationContext context )
        throws
            StringifierException
    {
        String contextPath  = context != null ? (String) context.get( StringRepresentationContext.WEB_CONTEXT_KEY ) : null;
        String externalForm = toExternalForm();

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                DEFAULT_LINK_END_ENTRY,
                null,
                contextPath,
                externalForm );
        return ret;
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
        if( !( other instanceof MeshBaseIdentifier )) {
            return false;
        }
        MeshBaseIdentifier realOther = (MeshBaseIdentifier) other;
        
        String here  = getCanonicalForm();
        String there = realOther.getCanonicalForm();
        
        boolean ret = here.equals( there );
        return ret;
    }

    /**
     * Calculate hash value.
     *
     * @return the hash value
     */
    @Override
    public int hashCode()
    {
        String canonical = getCanonicalForm();
        return canonical.hashCode();
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
                    "canonical"
                },
                new Object[] {
                    theCanonicalForm
                } );
    }

    /**
     * The canonical form.
     */
    protected String theCanonicalForm;

    /**
     * The factory with which this MeshBaseIdentifier was created.
     */
    protected MeshBaseIdentifierFactory theFactory;

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_ENTRY = "String";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_LINK_START_ENTRY = "LinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_LINK_END_ENTRY = "LinkEndString";
}
