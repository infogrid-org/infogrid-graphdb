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

package org.infogrid.modelbase.externalized;

import java.util.HashMap;
import java.util.Iterator;
import org.xml.sax.Attributes;

/**
 * This contains a list of XML node attributes. We can't store the XML Attributes object directly,
 * because it is mutable (apparently, after a lot of debugging).
 */
public class ExternalizedAttributes
{
    /**
      * Constructor.
      *
      * @param raw theAttributes object from which we take our content
      */
    public ExternalizedAttributes(
            Attributes raw )
    {
        int i = raw.getLength();
        if( i>0 ) {
            // optimization
            theMap = new HashMap<String,String>( i );

            for( --i ; i>=0 ; --i ) {
                theMap.put( raw.getQName( i ), raw.getValue( i ));
            }
        }
    }

    /**
     * Obtain the value of an attribute with this name.
     *
     * @param keyword the name of the attribute
     * @return the value of the attribute, or null if not present
     */
    public String getValue(
            String keyword )
    {
        if( theMap != null ) {
            return theMap.get( keyword );
        }
        return null;
    }

    /**
     * Convert to String form, for debugging.
     *
     * @return String form of this instance
     */
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder( 100 ); // fudge
        buf.append( super.toString() );
        buf.append( "{" );
        if( theMap != null ) {
            Iterator<String> theIter = theMap.keySet().iterator();
            while( theIter.hasNext() ) {
                String currentKey = theIter.next();

                buf.append( " " );
                buf.append( currentKey );
                buf.append( "=" );
                buf.append( theMap.get( currentKey ));
            }
        } else {
            buf.append( " empty" );
        }
        buf.append( " }" );
        return buf.toString();
    }

    /**
     * Internally, we use a HashMap.
     */
    protected HashMap<String,String> theMap;
}
