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

import java.util.Comparator;
import org.infogrid.mesh.IllegalPropertyTypeException;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.PropertyType;
import org.infogrid.model.primitives.PropertyValue;
import org.infogrid.util.InverseComparator;
import org.infogrid.util.logging.Log;

/**
 * Sorts by the value of a Property available on all MeshObjects in the MeshObjectSet.
 * This is a convenience class.
 */
public class ByPropertyValueSorter
        extends
            DefaultMeshObjectSorter
{
    private static final Log log = Log.getLogInstance( ByPropertyValueSorter.class ); // our own, private logger

    /**
     * Factory method.
     * 
     * @param property the PropertyType by whose values we sort
     * @return the created ByPropertyValueSorter
     */
    public static ByPropertyValueSorter create(
            PropertyType property )
    {
        Comparator<MeshObject> c = new PropertyValueComparator( property );
        return new ByPropertyValueSorter( c, null );
    }

    /**
     * Factory method.
     * 
     * @param property the PropertyType by whose values we sort
     * @param userName localized user-visible name in the current locale
     * @return the created ByPropertyValueSorter
     */
    public static ByPropertyValueSorter create(
            PropertyType property,
            String       userName )
    {
        Comparator<MeshObject> c = new PropertyValueComparator( property );
        return new ByPropertyValueSorter( c, userName );
    }

    /**
     * Factory method.
     *
     * @param property the PropertyType by whose values we sort
     * @param inverse if true, sort in reverse order
     * @return the created ByPropertyValueSorter
     */
    public static ByPropertyValueSorter create(
            PropertyType property,
            boolean      inverse )
    {
        Comparator<MeshObject> c = new PropertyValueComparator( property );
        if( inverse ) {
            c = new InverseComparator<MeshObject>( c );
        }
        return new ByPropertyValueSorter( c, null );
    }

    /**
     * Factory method.
     *
     * @param property the PropertyType by whose values we sort
     * @param inverse if true, sort in reverse order
     * @param userName localized user-visible name in the current locale
     * @return the created ByPropertyValueSorter
     */
    public static ByPropertyValueSorter create(
            PropertyType property,
            boolean      inverse,
            String       userName )
    {
        Comparator<MeshObject> c = new PropertyValueComparator( property );
        if( inverse ) {
            c = new InverseComparator<MeshObject>( c );
        }
        return new ByPropertyValueSorter( c, userName );
    }

    /**
     * Constructor for subclasses only.
     *
     * @param c specify the comparison criteria as the Java collections API does
     * @param userName localized user-visible name in the current locale
     */
    protected ByPropertyValueSorter(
            Comparator<MeshObject> c,
            String                 userName )
    {
        super( c, userName );
    }

    /**
     * The underlying Comparator of property values.
     */
    static class PropertyValueComparator
            implements
                Comparator<MeshObject>
    {
        /**
         * Constructor.
         * 
         * @param property the PropertyType that selects the property to compare
         */
        public PropertyValueComparator(
                PropertyType property )
        {
            thePropertyType = property;
        }
        
        /**
         * Comparison method.
         * 
         * @param o1 the first object
         * @param o2 the second object
         * @return comparison value
         */
        public int compare(
                MeshObject o1,
                MeshObject o2 )
        {
            PropertyValue o1Value = null; // better have a default
            PropertyValue o2Value = null; // better have a default
            
            try {
                o1Value = o1.getPropertyValue( thePropertyType );
            } catch( IllegalPropertyTypeException ex ) {
                log.error( ex );
            } catch( NotPermittedException ex ) {
                log.error( ex );
            }

            try {
                o2Value = o2.getPropertyValue( thePropertyType );
            } catch( IllegalPropertyTypeException ex ) {
                log.error( ex );
            } catch( NotPermittedException ex ) {
                log.error( ex );
            }
            
            int ret = PropertyValue.compare( o1Value, o2Value );
            return ret;
        }
        
        /**
         * The PropertyType that selects the property to compare.
         */
        protected PropertyType thePropertyType;
    }
}
