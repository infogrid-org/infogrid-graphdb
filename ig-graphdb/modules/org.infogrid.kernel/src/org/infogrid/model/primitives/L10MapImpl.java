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

package org.infogrid.model.primitives;

import org.infogrid.util.ArrayHelper;
import org.infogrid.util.NameValuePair;
import org.infogrid.util.ZeroElementCursorIterator;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>This is a memory-conservative implementation of a map between Locale and PropertyValue.</p>
 *
 * <p>This will not scale for large numbers of supported locales, but it is good for just one or
 * a handful of locales (the typical case here).</p>
 */
public class L10MapImpl
        implements
            L10Map
{
    private final static long serialVersionUID = 1L; // helps with serialization

    /**
      * Construct an empty L10Map with only a default value and no Locale-specific values.
      *
      * @param defaultValue the default PropertyValue returned by get for all Locales
      * @return the created L10MapImpl
      */
    public static L10MapImpl create(
            PropertyValue defaultValue )
    {
        return new L10MapImpl( defaultValue, null, null );
    }

    /**
     * Construct an L10Map from a HashMap whose keys are String representations of the Locale,
     * and whose values are the corresponding PropertyValue. The default value of this
     * L10Map will be the PropertyValue with the key null. If the PropertyValue with
     * the key null is null, the default value of this L10Map will be secondDefaultValue.
     *
     * @param theMap the HashMap with the Locale keys and the corresponding PropertyValue
     * @param secondDefaultValue the default value of this L10Map if none was found in theMap
     * @return the created L10MapImpl
     */
     public static L10MapImpl create(
            Map<String,PropertyValue> theMap,
            PropertyValue             secondDefaultValue )
    {
        if( theMap == null ) {
            if( secondDefaultValue == null ) {
                return null;
            } else {
                return new L10MapImpl( secondDefaultValue, null, null );
            }
        }

        // we have to pull out the default value

        int s = theMap.size();

        PropertyValue defaultValue = theMap.get( null );
        if( defaultValue != null ) {
            --s;
        } else {
            defaultValue = secondDefaultValue;
        }

        if( s == 0 && defaultValue == null ) {
            return null;
        }

        String [][]      keys   = new String[ s ][];
        PropertyValue [] values = new PropertyValue[ s ];

        Iterator<String> keyIter = theMap.keySet().iterator();
        int i=0;
        while( keyIter.hasNext() ) {

            String currentString = keyIter.next();
            if( currentString == null ) {
                continue; // skip default value
            }

            String [] myKey = parseLocaleString( currentString );

            keys[i]   = myKey;
            values[i] = theMap.get( currentString );

            ++i;
        }
        return new L10MapImpl( defaultValue, keys, values );
    }

    /**
     * This factory method is not very safe and thus should not really be invoked by the
     * application programmer. It exists because method getJavaConstructorString needs it.
     *
     * @param theDefaultValue the default value of this L10Map
     * @param theKeys the Locales in key format that have values in this L10Map
     * @param theValues the corresponding PropertyValue, in same sequence as theKeys
      * @return the created L10MapImpl
     */
    public static L10MapImpl create(
            PropertyValue    theDefaultValue,
            String [][]      theKeys,
            PropertyValue [] theValues )
    {
        return new L10MapImpl( theDefaultValue, theKeys, theValues );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param theDefaultValue the default value of this L10Map
     * @param theKeys the Locales in key format that have values in this L10Map
     * @param theValues the corresponding PropertyValues, in same sequence as theKeys
     */
    private L10MapImpl(
            PropertyValue    theDefaultValue,
            String [][]      theKeys,
            PropertyValue [] theValues )
    {
        defaultValue = theDefaultValue;
        keys         = theKeys;
        values       = theValues;
    }

    /**
     * Determine the number of entries in this L10Map.
     *
     * @return the number of entries in this L10Map
     */
    public int size()
    {
        if( keys == null ) {
            return 0;
        }
        return keys.length;
    }

    /**
     * Returns true if this map is empty.
     *
     * @return true of this map is empty
     */
    public boolean isEmpty()
    {
        return keys == null || keys.length == 0;
    }

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
            Locale l )
    {
        return get( getKey( l.getLanguage(), l.getCountry(), l.getVariant()) );
        
    }
    
    /**
     * Obtains the value to which this map maps the provided Locale. The implementation
     * must first look for the exact Locale, and return that if it is found. If a
     * value does not exist, the implementation looks for a less and less specific
     * Locale (e.g. from en-US to en). If all fails, the default value is returned.
     *
     * @param s the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue get(
            String s )
    {
        return get( parseLocaleString( s ));
    }
    
    /**
     * Implementation of the other two get methods.
     *
     * @param k String array with 1 to 3 elements, representing language, country and variant
     * @return the PropertyValue
     */
    protected PropertyValue get(
            String [] k )
    {
        if( keys == null || keys.length == 0 ) {
            return defaultValue;
        }

        // we are going through the array only once, keeping the best guess up to now
        int           bestGuessQuality = Integer.MAX_VALUE; // the number of Locale component Strings that we match; zero is best
        PropertyValue bestGuess        = defaultValue;

        for( int i=0 ; i<keys.length ; ++i ) {

            int currentQuality = compare( k, keys[i] );
            if( currentQuality < 0 ) {
                continue;
            }

            if( currentQuality < bestGuessQuality ) {
                bestGuessQuality = currentQuality;
                bestGuess        = values[i];
            }
        }
        return bestGuess;
    }

    /**
     * Obtains the value for this exact Locale. If a value for this exact Locale does
     * not exist, returns null. It does not attempt to find a default value using a
     * less-specific Locale.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue getExact(
            Locale l )
    {
        return getExact( l.toString() );
    }
    
    /**
     * Obtains the value for this exact Locale. If a value for this exact Locale does
     * not exist, returns null. It does not attempt to find a default value using a
     * less-specific Locale.
     *
     * @param l the Locale for which a value shall be determined
     * @return the PropertyValue for this Locale
     */
    public PropertyValue getExact(
            String l )
    {
        if( keys == null ) {
            return null;
        }
        String [] parsed = parseLocaleString( l );
        for( int i=0 ; i<keys.length ; ++i ) {
            if( ArrayHelper.equals( keys, parsed )) {
                return values[i];
            }
        }
        return null;
    }

    /**
     * Obtain the default value regardless of Locale.
     *
     * @return the default value
     */
    public PropertyValue getDefault()
    {
        return defaultValue;
    }

    /**
     * Add a PropertyValue for the specified Locale. Return the previously set value if any.
     *
     * @param k the Locale for which the value is set
     * @param v the new PropertyValue for this Locale
     * @return the previously set PropertyValue for this Locale, if any
     */
    public PropertyValue put(
            Locale        k,
            PropertyValue v )
    {
        String [] newKey = getKey( k.getLanguage(), k.getCountry(), k.getVariant() );

        if( keys == null ) {
            keys   = new String[][] { newKey };
            values = new PropertyValue[] { v };
            return null;
        } else {
            for( int i=0 ; i<keys.length ; ++i ) {
                if( compare( keys[i], newKey ) == 0 ) {
                    PropertyValue ret = values[i];
                    values[i]         = v;
                    return ret;
                }
            }
            keys   = ArrayHelper.append( keys,   newKey, String[].class );
            values = ArrayHelper.append( values, v,      PropertyValue.class );
            return null;
        }
    }

    /**
     * Set the default value to be returned if no Locale is given, or the Locale could not be resolved
     * any better.
     *
     * @param v the new default value
     * @return the previous default value, if any
     */
    public PropertyValue putDefault(
            PropertyValue v )
    {
        PropertyValue ret = defaultValue;
        defaultValue = v;
        return ret;
    }

    /**
     * Obtain an Iterator over all keys (Locales) known by this L10Map.
     *
     * @return an Iterator over all keys (Locales) known by this L10Map
     */
    public Iterator<String> keyIterator()
    {
        if( keys == null || keys.length == 0 ) {
            return ZeroElementCursorIterator.<String>create();
        } else {
            return new MyKeyIterator();
        }
    }

    /**
     * Obtain an Iterator over all mappings known by this L10Map.
     * JSP / JavaBeans-friendly version.
     *
     * @return an Iterator over all keys (Locales) known by this L10Map
     */
    public Iterator<NameValuePair<PropertyValue>> getPairIterator()
    {
        if( keys == null || keys.length == 0 ) {
            return ZeroElementCursorIterator.<NameValuePair<PropertyValue>>create();
        } else {
            return new MyPairIterator();
        }
    }

    /**
     * This internal helper helps us creating keys.
     *
     * @param language the language component of the Locale
     * @param country the country component of the Locale
     * @param variant the variant component of the Locale
     * @return the key appropriate for this combination of language, country and variant
     */
    private static String [] getKey(
            String language,
            String country,
            String variant )
    {
        String [] key;
        if( variant == null ) {
            if( country == null ) {
                key = new String[] { language };
            } else {
                key = new String[] { language, country };
            }
        } else {
            key = new String[] { language, country, variant };
        }
        return key;
    }

    /**
     * Helper method to compare two keys.
     *
     * @param key1 first key to compare
     * @param key2 second key to compare
     * @return 0 means is the same, positive number indicates the distance. -1 indicates no match.
     */
    private int compare(
            String [] key1,
            String [] key2 )
    {
        if( key1.length < key2.length ) {
            String [] swap = key1;
            key1 = key2;
            key2 = swap;
        }

        int length = key2.length; // max
        for( int i=0 ; i<length ; ++i ) {
            if( ! key1[i].equals( key2[i] )) {
                return -1;
            }
        }
        return key1.length - length;
    }

    /**
     * Obtain a string which is the Java-language constructor expression reflecting this value.
     * This is mainly for code-generation purposes.
     *
     * FIXME? This makes the assumption that all PropertyValues here can use the same
     * ClassLoader. I think this assumption is correct, but I better make a note here ...
     *
     * @param classLoaderVar name of a variable containing the class loader to be used to initialize this value
     * @param typeVar  name of the variable containing the DataType that goes with the to-be-created instance.
     * @return the Java-language constructor expression
     */
    public String getJavaConstructorString(
            String classLoaderVar,
            String typeVar )
    {
        StringBuffer ret = new StringBuffer( getClass().getName() );
        ret.append( DataType.CREATE_STRING );

        if( defaultValue == null ) {
            ret.append( DataType.NULL_STRING );
        } else {
            ret.append( defaultValue.getJavaConstructorString( classLoaderVar, typeVar ));
        }

        if( keys != null && keys.length > 0 ) {
            ret.append( ", new String[][] { " );
            for( int i=0 ; i<keys.length ; ++i ) {
                ret.append( "{ " );
                for( int j=0 ; j<keys[i].length ; ++j ) {
                    ret.append( "\"" );
                    ret.append( keys[i][j] );
                    ret.append( "\"" );
                    if( j<keys[i].length-1 ) {
                        ret.append( ", " );
                    }
                }
                ret.append( "}" );
                if( i<keys.length-1 ) {
                    ret.append( ", " );
                }
            }

            ret.append( "}, new PropertyValue[] { " );
            for( int i=0 ; i<keys.length ; ++i ) {
                ret.append( values[i].getJavaConstructorString( classLoaderVar, typeVar ));
                if( i<keys.length-1 ) {
                    ret.append( ", " );
                }
            }
            ret.append( "} " );
        }

        ret.append( DataType.CLOSE_PARENTHESIS_STRING );
        return ret.toString();
    }

    /**
     * Internal helper to parse a Locale String into our three Locale fields.
     *
     * @param s the string
     * @return String array, with 1 to three elements, representing language, country, variant (as many as were given)
     */
    protected static String [] parseLocaleString(
            String s )
    {
        s = s.toLowerCase();
        
        String language = null;
        String country  = null;
        String variant  = null;

        int slash1 = s.indexOf( SLASH );
        int slash2;

        if( slash1 < 0 ) {
            language = s;
            country  = null;
            variant  = null;
        } else {
            language = s.substring( 0, slash1 );
            slash2   = s.indexOf( SLASH, slash1+1 );
            if( slash2 < 0 ) {
                country = s.substring( slash1 );
                variant = null;
            } else {
                country = s.substring( slash1, slash2 );
                variant = s.substring( slash2+1 );
            }
        }
        if( variant != null ) {
            return new String[] { language, country, variant };
        } else if( country != null ) {
            return new String[] { language, country };
        } else {
            return new String[] { language };
        }
    }
            
    /**
     * The default value of this L10Map.
     */
    protected PropertyValue defaultValue;

    /**
     * The ordered set of Locales for which we have a PropertyValue (same order as values array).
     */
    protected String [][] keys = null;

    /**
     * The ordered set of PropertyValues that go with the Locales (same order as keys).
     */
    protected PropertyValue [] values = null;

    /**
     * The character which separates the Locale components in its string representation.
     */
    public static final char SLASH = '/';
    
    /**
     * Factors out common functionality of the two concrete Iterators below.
     */
    private class MyAbstractIterator
    {
        /**
         * Constructor.
         */
        public MyAbstractIterator()
        {
            goNext();
        }
        
        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext()
        {
            if( outerIndex >= keys.length ) {
                return false;
            }
            return true;
        }

        /**
         * Removes from the underlying collection the last element returned by the
         * iterator (optional operation).
         *
         * @throws UnsupportedOperationException always thrown
         */
        public void remove()
        {
            throw new UnsupportedOperationException( "remove not supported" );
        }
        
        /**
         * Helper to go to the next element.
         */
        protected final void goNext()
        {
            while( true ) {
                if( outerIndex >= keys.length ) {
                    return; // nothing else to do
                }
                if( innerIndex >= keys[outerIndex].length ) {
                    ++outerIndex;
                    innerIndex = 0;
                } else {
                    ++innerIndex;
                    return;
                }
            }
        }

        /**
         * The current index in the outer array.
         */
        protected int outerIndex = 0;
        
        /**
         * The current index in the inner array.
         */
        protected int innerIndex = 0;
    }
            
    /**
     * Iterator that iterates over all keys.
     */
    private class MyKeyIterator
            extends
                MyAbstractIterator
            implements
                Iterator<String>
    {
        /**
         * Constructor.
         */
        public MyKeyIterator()
        {
        }

        /**
         * Returns the next element in the iteration.  Calling this method
         * repeatedly until the {@link #hasNext()} method returns false will
         * return each element in the underlying collection exactly once.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException iteration has no more elements.
         */
        public String next()
        {
            if( !hasNext() ) {
                throw new NoSuchElementException();
            }
            String ret = keys[outerIndex][innerIndex];
            goNext();
            return ret;
        }
    }

    /**
     * Iterator that iterates over all key-value pairs.
     */
    private class MyPairIterator
            extends
                MyAbstractIterator
            implements
                Iterator<NameValuePair<PropertyValue>>
    {
        /**
         * Constructor.
         */
        public MyPairIterator()
        {
        }

        /**
         * Returns the next element in the iteration.  Calling this method
         * repeatedly until the {@link #hasNext()} method returns false will
         * return each element in the underlying collection exactly once.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException iteration has no more elements.
         */
        public NameValuePair<PropertyValue> next()
        {
            if( !hasNext() ) {
                throw new NoSuchElementException();
            }
            String        key = keys[outerIndex][innerIndex];
            PropertyValue val = getExact( key );

            goNext();
            return new NameValuePair<PropertyValue>( key, val );
        }
    }
}
