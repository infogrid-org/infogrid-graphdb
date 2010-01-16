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
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.util.NameValuePair;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Locale;

/**
 * This is a in implementation of a map between Locale and PropertyValue. It is used
 * to obtain Locale-specific PropertyValue, such as names and descriptions. It provides
 * methods to obtain the most specific PropertyValue given for a provided Locale, and
 * failing that, obtains less specific PropertyValue. For example, if the desired Locale
 * is en-US, it first attempts to find a PropertyValue for en-US, and failing that, for
 * Locale en. If none of those is found, a global default value is returned.
 */
public interface L10Map
        extends
            Serializable
{
    /**
     * Determine the number of entries in this L10Map.
     *
     * @return the number of entries in this L10Map
     */
    public int size();

    /**
     * Returns true if this map is empty.
     *
     * @return true of this map is empty
     */
    public boolean isEmpty();

    /**
     * Obtains the value to which this map maps the provided Locale. The implementation
     * must first look for the exact Locale, and return that if it is found. If a
     * value does not exist, the implementation looks for a less and less specific
     * Locale (e.g. from en-US to en). If all fails, the default value is returned.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue get(
            Locale l );

    /**
     * Obtains the value to which this map maps the provided Locale. The implementation
     * must first look for the exact Locale, and return that if it is found. If a
     * value does not exist, the implementation looks for a less and less specific
     * Locale (e.g. from en-US to en). If all fails, the default value is returned.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue get(
            String l );

    /**
     * Obtains the value for this exact Locale. If a value for this exact Locale does
     * not exist, returns null. It does not attempt to find a default value using a
     * less-specific Locale.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue getExact(
            Locale l );

    /**
     * Obtains the value for this exact Locale. If a value for this exact Locale does
     * not exist, returns null. It does not attempt to find a default value using a
     * less-specific Locale.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue getExact(
            String l );

    /**
     * Obtain the default value regardless of Locale.
     *
     * @return the default value
     */
    public PropertyValue getDefault();

    /**
     * Obtain an Iterator over all keys (Locales) known by this L10Map.
     *
     * @return an Iterator over all keys (Locales) known by this L10Map
     */
    public Iterator<String> keyIterator();

    /**
     * Obtain an Iterator over all mappings known by this L10Map.
     * JSP / JavaBeans-friendly version.
     *
     * @return an Iterator over all keys (Locales) known by this L10Map
     */
    public Iterator<NameValuePair<PropertyValue>> getPairIterator();

    /**
     * Obtain a string which is the Java-language constructor expression reflecting this value.
     * This is mainly for code-generation purposes.
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @param typeVar  name of the variable containing the DataType that goes with the to-be-created instance.
     * @return the Java-language constructor expression
     */
    public String getJavaConstructorString(
            String classLoaderVar,
            String typeVar );
}
