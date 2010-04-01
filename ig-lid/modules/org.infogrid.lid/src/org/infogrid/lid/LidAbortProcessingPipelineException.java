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

package org.infogrid.lid;

import javax.servlet.RequestDispatcher;
import org.infogrid.util.AbstractLocalizedException;

/**
 * Thrown by stages of the LID pipeline during processing of an incoming request, for the sole purpose
 * of interrupting the regular control flow in the LidProcessingPipeline.
 */
public class LidAbortProcessingPipelineException
        extends
            AbstractLocalizedException
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param source the LidProcessingPipelineStage that threw this exception
     */
    public LidAbortProcessingPipelineException(
            LidProcessingPipelineStage source )
    {
        this( source, null, null, null );
    }

    /**
     * Constructor.
     *
     * @param source the LidProcessingPipelineStage that threw this exception
     * @param message the message, if any
     */
    public LidAbortProcessingPipelineException(
            LidProcessingPipelineStage source,
            String                     message )
    {
        this( source, null, message, null );
    }

    /**
     * Constructor.
     *
     * @param source the LidProcessingPipelineStage that threw this exception
     * @param message the message, if any
     * @param cause the underlying cause
     */
    public LidAbortProcessingPipelineException(
            LidProcessingPipelineStage source,
            String                     message,
            Throwable                  cause )
    {
        this( source, null, message, cause );
    }

    /**
     * Constructor.
     *
     * @param source the LidProcessingPipelineStage that threw this exception
     * @param cause the underlying cause
     */
    public LidAbortProcessingPipelineException(
            LidProcessingPipelineStage source,
            Throwable                  cause )
    {
        this( source, null, null, cause );
    }

    /**
     * Constructor.
     *
     * @param source the LidProcessingPipelineStage that threw this exception
     * @param dispatcherToRun the RequestDispatcher to run as a result of this Exception, if any
     * @param message the message, if any
     * @param cause the underlying cause
     */
    public LidAbortProcessingPipelineException(
            LidProcessingPipelineStage source,
            RequestDispatcher          dispatcherToRun,
            String                     message,
            Throwable                  cause )
    {
        super( message, cause );
        
        theSource          = source;
        theDispatcherToRun = dispatcherToRun;
    }

    /**
     * Obtain resource parameters for the internationalization.
     *
     * @return the resource parameters
     */
    public Object [] getLocalizationParameters()
    {
        return new Object[0];
    }

    /**
     * Obtain the RequestDispatcher to execute in order to produce the result.
     * 
     * @return the RequestDispatcher
     */
    public RequestDispatcher getRequestDispatcher()
    {
        return theDispatcherToRun;
    }

    /**
     * The LidProcessingPipelineStage that threw this exception.
     */
    protected transient LidProcessingPipelineStage theSource;
            
    /**
     * The RequestDispatcher to run, if any.
     */
    protected transient RequestDispatcher theDispatcherToRun;
}