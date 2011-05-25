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
// Copyright 1998-2011 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.model.primitives;

import org.infogrid.util.L10MapImpl;
import java.util.Map;

/**
 * Concrete subclass of L10MapImpl that constructs PropertyValues.
 */
public class L10PropertyValueMapImpl
        extends
            L10MapImpl<PropertyValue>
        implements
            L10PropertyValueMap
{
    /**
      * Construct an empty L10Map with only a default value and no Locale-specific values.
      *
      * @param defaultValue the default T returned by get for all Locales
      * @return the created L10MapImpl
      */
    public static L10PropertyValueMapImpl create(
            PropertyValue defaultValue )
    {
        return new L10PropertyValueMapImpl( defaultValue, null, null );
    }

    /**
     * Construct an L10Map from a HashMap whose keys are String representations of the Locale,
     * and whose values are the corresponding Ts. The default value of this
     * L10Map will be the T with the key null. If the T with
     * the key null is null, the default value of this L10Map will be secondDefaultValue.
     *
     * @param theMap the HashMap with the Locale keys and the corresponding T
     * @param secondDefaultValue the default value of this L10Map if none was found in theMap
     * @return the created L10MapImpl
     */
     public static L10PropertyValueMapImpl create(
            Map<String,PropertyValue> theMap,
            PropertyValue             secondDefaultValue )
    {
        return create( theMap, secondDefaultValue, new Fact<PropertyValue,L10PropertyValueMapImpl>() {
                public L10PropertyValueMapImpl create(
                        PropertyValue theDefaultValue,
                        String [][]   theKeys,
                        Object []     theValues )
                {
                    return new L10PropertyValueMapImpl( theDefaultValue, theKeys, theValues );
                }
        });
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
    public static L10PropertyValueMapImpl create(
            PropertyValue    theDefaultValue,
            String [][]      theKeys,
            PropertyValue [] theValues )
    {
        return new L10PropertyValueMapImpl( theDefaultValue, theKeys, theValues );
    }

    /**
     * Private constructor, use factory method.
     *
     * @param theDefaultValue the default value of this L10Map
     * @param theKeys the Locales in key format that have values in this L10Map
     * @param theValues the corresponding PropertyValues, in same sequence as theKeys
     */
    protected L10PropertyValueMapImpl(
            PropertyValue theDefaultValue,
            String [][]   theKeys,
            Object []     theValues )
    {
        super( theDefaultValue, theKeys, theValues );
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
        StringBuilder ret = new StringBuilder( getClass().getName() );
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
                ret.append( ((PropertyValue)values[i]).getJavaConstructorString( classLoaderVar, typeVar ));
                if( i<keys.length-1 ) {
                    ret.append( ", " );
                }
            }
            ret.append( "} " );
        }

        ret.append( DataType.CLOSE_PARENTHESIS_STRING );
        return ret.toString();
    }
}
