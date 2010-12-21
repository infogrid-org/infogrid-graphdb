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

package org.infogrid.codegen;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.infogrid.module.ModuleAdvertisement;
import org.infogrid.module.StandardModuleRunException;

/**
 * Thrown if the code generator run failed.
 */
public class CodeGeneratorRunException
        extends
            StandardModuleRunException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
      * Constructor.
      *
      * @param adv the ModuleAdvertisement whose Module we wanted to run
      * @param runClassName the name of the class whose run method we wanted to execute, or null if not known
      * @param cause the Throwable that caused this Exception
      */
    public CodeGeneratorRunException(
             ModuleAdvertisement adv,
             String              runClassName,
             Throwable           cause )
    {
        super( adv, runClassName, "main", cause );
    }

    /**
     * Obtain the message.
     *
     * @return the message
     */
    @Override
    public String getMessage()
    {
        StringBuilder buf = new StringBuilder( 100 );
        buf.append( "Runinng code generator failed for module: " );
        if( theModuleAdvertisement != null ) {
            buf.append( theModuleAdvertisement.getModuleName() );
            buf.append( ", version: " );
            if( theModuleAdvertisement.getModuleVersion() != null ) {
                buf.append( theModuleAdvertisement.getModuleVersion() );
            } else {
                buf.append( "?" );
            }
        } else {
            buf.append( "null" );
        }
        Throwable cause = getCause();
        if( cause != null ) {
            buf.append( "\nCause: " ).append( cause.getMessage() );
        }
        return buf.toString();
    }

    /**
     * Override for better error messages.
     */
    @Override
    public void printStackTrace()
    {
        // do nothing
    }

    /**
     * Override for better error messages.
     *
     * @param s the PrintStream
     */
    @Override
    public void printStackTrace(
            PrintStream s )
    {
        // do nothing
    }

    /**
     * Override for better error messages.
     *
     * @param w the PrintWriter
     */
    @Override
    public void printStackTrace(
            PrintWriter w )
    {
        // do nothing
    }
}
