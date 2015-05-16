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
// Copyright 1998-2015 by Johannes Ernst
// All rights reserved.
//

package org.infogrid.codegen;

import org.infogrid.model.primitives.SubjectArea;

/**
 * A PackageNameTranslator that takes the version of the package into account.
 */
public class PackageNameTranslatorWithVersion
        implements
            PackageNameTranslator
{
    /**
      * Translate into the name of the SubjectArea with appended version.
      *
      * @param theSa the SubjectArea that shall be translated into a package name
      * @return the package name of the SubjectArea
      */
    public String translateSubjectArea(
            SubjectArea theSa )
    {
        StringBuilder ret = new StringBuilder();
        ret.append( theSa.getName().value() );
        ret.append( ".V" );
        if( theSa.getVersionNumber() != null ) {
            ret.append( theSa.getVersionNumber().value() );
        }
        return ret.toString();
    }
}
