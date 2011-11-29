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

package org.infogrid.myhealth.www;

import java.util.ArrayList;
import org.infogrid.jee.viewlet.JeeViewlet;
import org.infogrid.jee.viewlet.DefaultJspViewlet;
import org.infogrid.jee.viewlet.JeeMeshObjectsToView;
import org.infogrid.jee.viewlet.blob.BlobViewlet;
import org.infogrid.jee.viewlet.bulk.BulkLoaderViewlet;
import org.infogrid.jee.viewlet.bulkexporter.BulkExporterViewlet;
import org.infogrid.jee.viewlet.log4j.Log4jConfigurationViewlet;
import org.infogrid.jee.viewlet.meshbase.AllMeshObjectsViewlet;
import org.infogrid.jee.viewlet.modelbase.AllMeshTypesViewlet;
import org.infogrid.jee.viewlet.module.ModuleDirectoryViewlet;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.Wiki.WikiSubjectArea;
import org.infogrid.model.primitives.BlobDataType;
import org.infogrid.model.primitives.BlobValue;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.viewlet.AbstractViewletFactory;
import org.infogrid.viewlet.MeshObjectsToView;
import org.infogrid.viewlet.ViewletFactoryChoice;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.logging.Log;

/**
 * ViewletFactory for the Unnamed App application's main screen.
 */
public class MainViewletFactory
        extends
            AbstractViewletFactory
{
    private static final Log log = Log.getLogInstance( MainViewletFactory.class ); // our own, private logger

    /**
     * Constructor.
     */
    public MainViewletFactory()
    {
        super( JeeViewlet.class.getName() );
    }

    /**
     * Find the ViewletFactoryChoices that apply to these MeshObjectsToView, but ignore the specified
     * viewlet type. If none are found, return an emtpy array.
     *
     * @param toView the MeshObjectsToView
     * @return the found ViewletFactoryChoices, if any
     */
    public ViewletFactoryChoice [] determineFactoryChoicesIgnoringType(
            MeshObjectsToView toView )
    {
        JeeMeshObjectsToView realToView = (JeeMeshObjectsToView) toView;

        ArrayList<ViewletFactoryChoice> ret = new ArrayList<ViewletFactoryChoice>();
        
        MeshObject subject = toView.getSubject();
        if( subject.getMeshBase().getHomeObject() == subject ) {
            ret.add( DefaultJspViewlet.choice(     realToView, "org.infogrid.myhealth.viewlet.patients.PatientsCollectionViewlet", ViewletFactoryChoice.PERFECT_MATCH_QUALITY ));
            ret.add( AllMeshObjectsViewlet.choice( realToView, ViewletFactoryChoice.GOOD_MATCH_QUALITY ));
            ret.add( AllMeshTypesViewlet.choice(   realToView, ViewletFactoryChoice.AVERAGE_MATCH_QUALITY ));
        }

        ret.add( DefaultJspViewlet.choice( realToView, "org.infogrid.jee.viewlet.propertysheet.PropertySheetViewlet", ViewletFactoryChoice.AVERAGE_MATCH_QUALITY ));
        
        return ArrayHelper.copyIntoNewArray( ret, ViewletFactoryChoice.class );
    }
}
