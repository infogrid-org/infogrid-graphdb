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

package org.infogrid.model.primitives.text;

import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.text.MeshStringRepresentationParameters;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.util.Identifier;
import org.infogrid.util.text.IdentifierStringifier;
import org.infogrid.util.text.StringRepresentationParameters;

/**
 * A Stringifier that knows how to format Identifiers correctly as part of a URL
 * that includes a web context path already.
 */
public class WebContextAwareMeshObjectIdentifierStringifier
        extends
            IdentifierStringifier
{
    /**
     * Factory method.
     *
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     * @return the created WebContextAwareMeshObjectIdentifierStringifier
     */
    public static WebContextAwareMeshObjectIdentifierStringifier create(
            boolean assembleAsPartOfLongerId )
    {
        return new WebContextAwareMeshObjectIdentifierStringifier( true, null, null, assembleAsPartOfLongerId );
    }

    /**
     * Factory method.
     *
     * @param processColloquial if true, process the colloquial parameter (if given). If false, leave identifier as is.
     * @param prefix the prefix for the identifier, if any
     * @param postfix the postfix for the identifier, if any
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     * @return the created WebContextAwareMeshObjectIdentifierStringifier
     */
    public static WebContextAwareMeshObjectIdentifierStringifier create(
            boolean processColloquial,
            String  prefix,
            String  postfix,
            boolean assembleAsPartOfLongerId )
    {
        return new WebContextAwareMeshObjectIdentifierStringifier(
                processColloquial,
                prefix,
                postfix,
                assembleAsPartOfLongerId );
    }

    /**
     * Constructor. Use factory method.
     *
     * @param processColloquial if true, process the colloquial parameter (if given). If false, leave identifier as is.
     * @param prefix the prefix for the identifier, if any
     * @param postfix the postfix for the identifier, if any
     * @param assembleAsPartOfLongerId if true, escape properly so that the produced String can become part of a longer identifier
     */
    protected WebContextAwareMeshObjectIdentifierStringifier(
            boolean processColloquial,
            String  prefix,
            String  postfix,
            boolean assembleAsPartOfLongerId )
    {
        super( processColloquial, prefix, postfix );

        theAssembleAsPartOfLongerId = assembleAsPartOfLongerId;
    }

    /**
     * Format an Object using this Stringifier.
     *
     * @param soFar the String so far, if any
     * @param arg the Object to format, or null
     * @param pars collects parameters that may influence the String representation
     * @return the formatted String
     */
    @Override
    public String format(
            String                         soFar,
            Identifier                     arg,
            StringRepresentationParameters pars )
    {
        if( arg == null ) {
            return formatToNull( soFar, pars );
        }

        MeshObjectIdentifier realIdentifier  = (MeshObjectIdentifier) arg;
        MeshBase             defaultMeshBase = (MeshBase) pars.get(  MeshStringRepresentationParameters.DEFAULT_MESHBASE_KEY );
        String contextPath;
        if( defaultMeshBase != null ) {
            contextPath = defaultMeshBase.getIdentifier().toExternalForm();
        } else {
            contextPath = (String) pars.get( StringRepresentationParameters.WEB_ABSOLUTE_CONTEXT_KEY ); // which may or may not be null
            if( contextPath != null && !contextPath.endsWith( "/" )) {
                contextPath += "/";
            }
        }

        String ext = realIdentifier.toLocalExternalForm( contextPath, theAssembleAsPartOfLongerId );

        ext = potentiallyProcessColloquial( ext, pars );
        ext = escape( ext );

        String ret = processPrefixPostfix( ext );
        return ret;
    }

    /**
     * Overridable method to possibly escape a String first.
     *
     * @param s the String to be escaped
     * @return the escaped String
     */
    @Override
    protected String escape(
            String s )
    {
        if( theAssembleAsPartOfLongerId ) {
            String ret = s.replace( "#", "%23" );
            return ret;
        } else {
            return s;
        }
    }

    /**
     * Overridable method to possibly unescape a String first.
     *
     * @param s the String to be unescaped
     * @return the unescaped String
     */
    @Override
    protected String unescape(
            String s )
    {
        if( theAssembleAsPartOfLongerId ) {
            String ret = s.replace( "%23", "#" );
            return ret;
        } else {
            return s;
        }
    }

    /**
     * If true, escape.
     */
    protected boolean theAssembleAsPartOfLongerId;
}
