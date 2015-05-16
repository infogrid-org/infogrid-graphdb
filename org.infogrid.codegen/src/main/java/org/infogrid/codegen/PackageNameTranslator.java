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
  * This interface is supported by objects that can determines the name of
  * the Java package that corresponds with a SubjectArea, for the purposes of code generation.
  */
public interface PackageNameTranslator
{
    /**
      * Translate a SubjectArea into a package name.
      *
      * @param theSa the SubjectArea that shall be translated into a package name
      * @return the package name
      */
    public abstract String translateSubjectArea(
            SubjectArea theSa );
}
