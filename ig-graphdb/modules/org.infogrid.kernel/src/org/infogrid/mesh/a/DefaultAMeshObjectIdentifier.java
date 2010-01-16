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

package org.infogrid.mesh.a;

import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.text.MeshStringRepresentationContext;
import org.infogrid.meshbase.MeshBase;
import org.infogrid.meshbase.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.util.AbstractIdentifier;
import org.infogrid.util.text.IdentifierStringifier;
import org.infogrid.util.text.StringRepresentation;
import org.infogrid.util.text.StringRepresentationContext;
import org.infogrid.util.text.StringRepresentationParameters;
import org.infogrid.util.text.StringifierException;

/**
 * Implements MeshObjectIdentifier for the "A" implementation.
 */
public class DefaultAMeshObjectIdentifier
        extends
             AbstractIdentifier
        implements
            MeshObjectIdentifier
{
    /**
     * Factory method.
     *
     * @param factory the DefaultAMeshObjectIdentifierFactory that created this identifier
     * @param localId the localId of the to-be-DefaultAMeshObjectIdentifier
     * @return the created DefaultAMeshObjectIdentifier
     */
    public static DefaultAMeshObjectIdentifier create(
            DefaultAMeshObjectIdentifierFactory factory,
            String                              localId )
    {
        if( localId == null || localId.length() == 0 ) {
            return factory.HOME_OBJECT;
        }

        return new DefaultAMeshObjectIdentifier( factory, localId );
    }

    /**
     * Private constructor, use factory method.
     * 
     * @param factory the DefaultAMeshObjectIdentifierFactory that created this identifier
     * @param localId the localId of the to-be-created DefaultAMeshObjectIdentifier
     */
    protected DefaultAMeshObjectIdentifier(
            DefaultAMeshObjectIdentifierFactory factory,
            String                              localId )
    {
        theFactory = factory;
        theLocalId = localId;
    }

    /**
     * Obtain the factory that created this identifier.
     *
     * @return the factory
     */
    public DefaultAMeshObjectIdentifierFactory getFactory()
    {
        return theFactory;
    }

    /**
     * Obtain the localId component.
     *
     * @return the localId component
     */
    public String getLocalId()
    {
        return theLocalId;
    }

    /**
     * Determine whether this MeshObjectIdentifier identifies a Home Object.
     *
     * @return true if it identifies a Home Object
     */
    public boolean identifiesHomeObject()
    {
        return toExternalForm().length() == 0;
    }

    /**
      * Determine hashCode.
      *
      * @return the hash code
      */
    @Override
    public final int hashCode()
    {
        return toExternalForm().hashCode();
    }

    /**
      * Determine equality of two objects.
      *
      * @param otherValue the object to test against
      * @return true if the objects are equal
      */
    @Override
    public final boolean equals(
            Object otherValue )
    {
        if( otherValue instanceof MeshObjectIdentifier ) {
            MeshObjectIdentifier realValue = (MeshObjectIdentifier) otherValue;
            
            if( !toExternalForm().equals( realValue.toExternalForm() )) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
      * Obtain as string representation, for debugging.
      *
      * @return string representation of this object
      */
    @Override
    public String toString()
    {
        return "&" + toExternalForm();
    }

    /**
     * Obtain an external form for this Identifier, similar to
     * <code>java.net.URL.toExternalForm()</code>.
     *
     * @return external form of this Identifier
     */
    public String toExternalForm()
    {
        if( theLocalId != null ) {
            return theLocalId;
        } else {
            return "";
        }
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
        MeshObject meshObject  = context != null ? (MeshObject) context.get( MeshStringRepresentationContext.MESHOBJECT_KEY ) : null;
        String     contextPath = context != null ? (String) context.get(  StringRepresentationContext.WEB_CONTEXT_KEY ) : null;
        MeshBase   meshBase    = meshObject != null ? meshObject.getMeshBase() : null;

        boolean isDefaultMeshBase = false;
        if( meshBase != null && context != null ) {
            isDefaultMeshBase = meshBase.equals( context.get( MeshStringRepresentationContext.DEFAULT_MESHBASE_KEY ));
        }
        boolean isHomeObject;
        if( meshBase != null ) {
            isHomeObject = meshObject == meshBase.getHomeObject();
        } else {
            isHomeObject = identifiesHomeObject();
        }

        String key;
        if( isDefaultMeshBase ) {
            if( isHomeObject ) {
                key = DEFAULT_MESH_BASE_HOME_ENTRY;
            } else {
                key = DEFAULT_MESH_BASE_ENTRY;
            }
        } else {
            if( isHomeObject ) {
                key = NON_DEFAULT_MESH_BASE_HOME_ENTRY;
            } else {
                key = NON_DEFAULT_MESH_BASE_ENTRY;
            }
        }

        String meshObjectExternalForm = IdentifierStringifier.defaultFormat( toExternalForm(), pars );
        String meshBaseExternalForm   = meshBase != null ? IdentifierStringifier.defaultFormat( meshBase.getIdentifier().toExternalForm(), pars ) : null;

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                key,
                pars,
                meshObjectExternalForm,
                contextPath,
                meshBaseExternalForm );

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
        MeshObject meshObject  = context != null ? (MeshObject) context.get( MeshStringRepresentationContext.MESHOBJECT_KEY ) : null;
        String     contextPath = context != null ? (String) context.get(  StringRepresentationContext.WEB_CONTEXT_KEY ) : null;
        MeshBase   meshBase    = meshObject != null ? meshObject.getMeshBase() : null;

        boolean isDefaultMeshBase = false;
        if( meshBase != null && context != null ) {
            isDefaultMeshBase = meshBase.equals( context.get( MeshStringRepresentationContext.DEFAULT_MESHBASE_KEY ));
        }
        boolean isHomeObject;
        if( meshBase != null ) {
            isHomeObject = meshObject == meshBase.getHomeObject();
        } else {
            isHomeObject = identifiesHomeObject();
        }

        String key;
        if( isDefaultMeshBase ) {
            if( isHomeObject ) {
                key = DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY;
            } else {
                key = DEFAULT_MESH_BASE_LINK_START_ENTRY;
            }
        } else {
            if( isHomeObject ) {
                key = NON_DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY;
            } else {
                key = NON_DEFAULT_MESH_BASE_LINK_START_ENTRY;
            }
        }
        if( target == null ) {
            target = "_self";
        }

        String meshObjectExternalForm = toExternalForm();
        String meshBaseExternalForm   = meshBase != null ? meshBase.getIdentifier().toExternalForm() : null;

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                key,
                null,
        /* 0 */ meshObjectExternalForm,
        /* 1 */ contextPath,
        /* 2 */ meshBaseExternalForm,
        /* 3 */ additionalArguments,
        /* 4 */ target,
        /* 5 */ title );

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
        MeshObject meshObject  = context != null ? (MeshObject) context.get( MeshStringRepresentationContext.MESHOBJECT_KEY ) : null;
        String     contextPath = context != null ? (String) context.get(  StringRepresentationContext.WEB_CONTEXT_KEY ) : null;
        MeshBase   meshBase    = meshObject != null ? meshObject.getMeshBase() : null;

        boolean isDefaultMeshBase = false;
        if( meshBase != null && context != null ) {
            isDefaultMeshBase = meshBase.equals( context.get( MeshStringRepresentationContext.DEFAULT_MESHBASE_KEY ));
        }
        boolean isHomeObject;
        if( meshBase != null ) {
            isHomeObject = meshObject == meshBase.getHomeObject();
        } else {
            isHomeObject = identifiesHomeObject();
        }

        String key;
        if( isDefaultMeshBase ) {
            if( isHomeObject ) {
                key = DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY;
            } else {
                key = DEFAULT_MESH_BASE_LINK_END_ENTRY;
            }
        } else {
            if( isHomeObject ) {
                key = NON_DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY;
            } else {
                key = NON_DEFAULT_MESH_BASE_LINK_END_ENTRY;
            }
        }

        String meshObjectExternalForm = toExternalForm();
        String meshBaseExternalForm   = meshBase != null ? meshBase.getIdentifier().toExternalForm() : null;

        String ret = rep.formatEntry(
                getClass(), // dispatch to the right subtype
                key,
                null,
                meshObjectExternalForm,
                contextPath,
                meshBaseExternalForm );

        return ret;
    }

    /**
     * The factory that created this identifier.
     */
    protected DefaultAMeshObjectIdentifierFactory theFactory;

    /**
     * The real value for the localId.
     */
    protected String theLocalId;

//    /**
//     * The default entry in the resouce files, prefixed by the StringRepresentation's prefix.
//     */
//    public static final String DEFAULT_ENTRY = "String";
//
//    /**
//     * The home entry in the resouce files, prefixed by the StringRepresentation's prefix.
//     */
//    public static final String HOME_DEFAULT_ENTRY = "HomeString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_ENTRY = "DefaultMeshBaseString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_HOME_ENTRY = "DefaultMeshBaseHomeString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_START_ENTRY = "DefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY = "DefaultMeshBaseHomeLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_LINK_END_ENTRY = "DefaultMeshBaseLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY = "DefaultMeshBaseHomeLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_ENTRY = "NonDefaultMeshBaseString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_HOME_ENTRY = "NonDefaultMeshBaseHomeString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_START_ENTRY = "NonDefaultMeshBaseLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_HOME_LINK_START_ENTRY = "NonDefaultMeshBaseHomeLinkStartString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_LINK_END_ENTRY = "NonDefaultMeshBaseLinkEndString";

    /**
     * Entry in the resource files, prefixed by the StringRepresentation's prefix.
     */
    public static final String NON_DEFAULT_MESH_BASE_HOME_LINK_END_ENTRY = "NonDefaultMeshBaseHomeLinkEndString";
}
