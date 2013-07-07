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

package org.infogrid.meshbase.m;

import java.util.HashMap;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.mesh.set.MeshObjectSetFactory;
import org.infogrid.mesh.set.m.ImmutableMMeshObjectSetFactory;
import org.infogrid.meshbase.DefaultMeshBaseIdentifierFactory;
import org.infogrid.meshbase.MeshBaseIdentifier;
import org.infogrid.meshbase.MeshObjectIdentifierFactory;
import org.infogrid.meshbase.a.AIterableMeshBase;
import org.infogrid.meshbase.a.AMeshBaseLifecycleManager;
import org.infogrid.mesh.a.DefaultAMeshObjectIdentifierFactory;
import org.infogrid.meshbase.security.AccessManager;
import org.infogrid.modelbase.ModelBase;
import org.infogrid.modelbase.m.MModelBase;
import org.infogrid.util.CachingMap;
import org.infogrid.util.CursorIterator;
import org.infogrid.util.MCachingHashMap;
import org.infogrid.util.MapCursorIterator;
import org.infogrid.util.context.Context;
import org.infogrid.util.context.SimpleContext;
import org.infogrid.util.logging.Log;
import org.infogrid.util.text.StringRepresentationParseException;

/**
  * This MeshBase is only held in memory. It has no persistence whatsoever.
  */
public class MMeshBase
        extends
            AIterableMeshBase
{
    private static final Log log = Log.getLogInstance( MMeshBase.class ); // our own, private logger

    /**
     * Most convenient factory method.
     *
     * @return the created MMeshBase
     */
    public static MMeshBase create()
    {
        try {
            return create(
                    DefaultMeshBaseIdentifierFactory.create().fromExternalForm( "DefaultMeshBase" ),
                    MModelBase.create(),
                    null,
                    SimpleContext.createRoot( "root context" ));
        } catch( StringRepresentationParseException ex ) {
            log.error( ex );
            return null;
        }
    }

    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param modelBase the ModelBase with the type definitions we use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param c the Context in which this MeshBase will run
     * @return the created MMeshBase
     */
    public static MMeshBase create(
            MeshBaseIdentifier identifier,
            ModelBase          modelBase,
            AccessManager      accessMgr,
            Context            c )
    {
        DefaultAMeshObjectIdentifierFactory identifierFactory = DefaultAMeshObjectIdentifierFactory.create();

        return create(
                identifier,
                identifierFactory,
                modelBase,
                accessMgr,
                c );
    }

    /**
     * Factory method.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param identifierFactory the factory for MeshObjectIdentifiers appropriate for this MeshBase
     * @param modelBase the ModelBase with the type definitions we use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param c the Context in which this MeshBase will run
     * @return the created MMeshBase
     */
    public static MMeshBase create(
            MeshBaseIdentifier          identifier,
            MeshObjectIdentifierFactory identifierFactory,
            ModelBase                   modelBase,
            AccessManager               accessMgr,
            Context                     c )
    {
        MCachingHashMap<MeshObjectIdentifier,MeshObject> cache = MCachingHashMap.create();

        AMeshBaseLifecycleManager      life       = AMeshBaseLifecycleManager.create();
        ImmutableMMeshObjectSetFactory setFactory = ImmutableMMeshObjectSetFactory.create( MeshObject.class, MeshObjectIdentifier.class );

        MMeshBase ret = new MMeshBase(
                identifier,
                identifierFactory,
                setFactory,
                modelBase,
                life,
                accessMgr,
                cache,
                c );

        setFactory.setMeshBase( ret );
        ret.initializeHomeObject();

        if( log.isDebugEnabled() ) {
            log.debug( "created " + ret );
        }

        return ret;
    }

    /**
     * Constructor.
     *
     * @param identifier the MeshBaseIdentifier of this MeshBase
     * @param identifierFactory the factory for MeshObjectIdentifiers appropriate for this MeshBase
     * @param setFactory the factory for MeshObjectSets appropriate for this MeshBase
     * @param modelBase the ModelBase containing type information
     * @param life the MeshBaseLifecycleManager to use
     * @param accessMgr the AccessManager that controls access to this MeshBase
     * @param cache the CachingMap that holds the MeshObjects in this MeshBase
     * @param context the Context in which this MeshBase runs.
      */
    protected MMeshBase(
            MeshBaseIdentifier                          identifier,
            MeshObjectIdentifierFactory                 identifierFactory,
            MeshObjectSetFactory                        setFactory,
            ModelBase                                   modelBase,
            AMeshBaseLifecycleManager                   life,
            AccessManager                               accessMgr,
            CachingMap<MeshObjectIdentifier,MeshObject> cache,
            Context                                     context )
    {
        super( identifier, identifierFactory, setFactory, modelBase, life, accessMgr, cache, context );
    }


    /**
     * Returns a CursorIterator over the content of this MeshBase.
     * 
     * @return a CursorIterator.
     */
    public CursorIterator<MeshObject> iterator()
    {
        // not sure why these type casts are needed, they should not be
        MapCursorIterator.Values<MeshObjectIdentifier,MeshObject> ret = MapCursorIterator.createForValues(
                (HashMap<MeshObjectIdentifier,MeshObject>) theCache,
                MeshObjectIdentifier.class,
                MeshObject.class );
        return ret;
    }

    /**
     * Determine the number of MeshObjects in this MeshBase.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #getSize()
     */
    public int size()
    {
        return theCache.size();
    }

    /**
     * Determine the number of MeshObjects in this MeshBase. This redundant method
     * is provided to make life easier for JavaBeans-aware software.
     *
     * @return the number of MeshObjects in this MeshBase
     * @see #size()
     */
    public final int getSize()
    {
        return size();
    }
}
