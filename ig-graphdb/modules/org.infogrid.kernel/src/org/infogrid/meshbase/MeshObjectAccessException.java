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

package org.infogrid.meshbase;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.MeshObjectIdentifier;
import org.infogrid.util.AbstractLocalizedException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.PartialResultException;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;

/**
 * Thrown if something went wrong when trying to access a MeshObject. The underlying
 * cause may indicate what went wrong. This Exception inherits from PartialResultException,
 * which means that if we attempt to access N MeshObjects, we may have been successful
 * for some of them. To determine which were successful, this Exception carries both
 * an array of identifiers of the MeshObjects that were attempted to be accessed, and an
 * array with MeshObjects that constitutes the result, in the same sequence.
 * Because redirection is a possibility, some of the MeshObjects that were found
 * may have different MeshObjectIdentifiers than the ones that were specified.
 */
public class MeshObjectAccessException
        extends
            AbstractLocalizedException
        implements
            PartialResultException<MeshObject[]>,
            CanBeDumped
{
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param mbIdentifier the MeshBaseIdentifier in which the Exception occurred
     * @param result a partial result, available at the time the Exception occurred
     * @param failedIdentifiers the MeshObjectIdentifiers 
     * @param causes the underlying causes for the Exception, in the same order as the failedIdentifiers
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshBaseIdentifier      mbIdentifier,
            MeshObject []           result,
            MeshObjectIdentifier [] attemptedIdentifiers,
            Throwable []            causes )
    {
        this( mb, mbIdentifier, result, attemptedIdentifiers, causes, null );
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param mbIdentifier the MeshBaseIdentifier in which the Exception occurred
     * @param result a partial result, available at the time the Exception occurred
     * @param failedIdentifiers the MeshObjectIdentifiers
     * @param causes the underlying causes for the Exception, in the same order as the failedIdentifiers
     * @param cause the cause
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshBaseIdentifier      mbIdentifier,
            MeshObject []           result,
            MeshObjectIdentifier [] attemptedIdentifiers,
            Throwable []            causes,
            Throwable               cause )
    {
        super( null, cause ); // avoid construction of default message

        theMeshBase             = mb;
        theMeshBaseIdentifier   = mbIdentifier;
        theAttemptedIdentifiers = attemptedIdentifiers;
        theResult               = result;
        theCauses               = causes;

        theNumberFoundWhereExpected = 0;
        theNumberFoundSomewhereElse = 0;
        theNumberCauses             = 0;

        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( theResult[i] != null ) {
                if( theResult[i].getIdentifier().equals( theAttemptedIdentifiers[i] )) {
                    ++theNumberFoundWhereExpected;
                } else {
                    ++theNumberFoundSomewhereElse;
                }
            } else if( theCauses[i] != null ) {
                ++theNumberCauses;
            }
        }
        if( theNumberFoundWhereExpected + theNumberFoundSomewhereElse + theNumberCauses == 0) {
            throw new IllegalArgumentException( "This Exception must not be thrown unless at least something was found or Exceptions can be reported" );
        }
        if( theAttemptedIdentifiers.length != theResult.length ) {
            throw new IllegalArgumentException( "Inconsistent invocation: " + theAttemptedIdentifiers.length + " vs. " + theResult.length );
        }
        if( theAttemptedIdentifiers.length != theCauses.length ) {
            throw new IllegalArgumentException( "Inconsistent invocation: " + theAttemptedIdentifiers.length + " vs. " + theCauses.length );
        }
    }

    /**
     * Obtain the MeshBase in which the Exception occurred.
     *
     * @return the MeshBase in which the Exception occurred
     */
    public MeshBase getMeshBase()
    {
        return theMeshBase;
    }

    /**
     * Determine whether a partial result is available.
     *
     * @return true if a partial result is available
     */
    public boolean isPartialResultAvailable()
    {
        if( theNumberFoundWhereExpected > 0 ) {
            return true;
        }
        return theNumberFoundSomewhereElse > 0;
    }

    /**
     * Determine whether a complete result is available. This is true
     * if all MeshObjects could be found, but at least one of them
     * was found at a different MeshObjectIdentifier than was specified.
     *
     * @return true if the result is complete
     */
    public boolean isCompleteResultAvailable()
    {
        return theNumberFoundWhereExpected + theNumberFoundSomewhereElse == theAttemptedIdentifiers.length;
    }

    /**
     * Obtain the partial result, if any.
     *
     * @return the partial result, if any
     */
    public MeshObject [] getBestEffortResult()
    {
        return theResult;
    }

    /**
     * Determine the cause for this MeshObjectIdentifier.
     *
     * @param key the MeshObjectIdentifier
     * @return the cause, if any
     */
    public Throwable getCauseFor(
            MeshObjectIdentifier key )
    {
        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( key.equals( theAttemptedIdentifiers[i] )) {
                return theCauses[i];
            }
        }
        throw new IllegalArgumentException( "Unknown key: " + key.toExternalForm() );
    }

    /**
     * Allow subclasses to override which key to use in the Resource file for the string representation.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationParameter()
    {
        if( theAttemptedIdentifiers.length == 1 ) {
            if( theNumberFoundSomewhereElse == 1 ) {
                return SINGULAR_FOUND_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY;
            } else {
                return SINGULAR_NOT_FOUND_STRING_REPRESENTATION_KEY;
            }
        } else {
            // more than one
            if( theNumberFoundWhereExpected + theNumberFoundSomewhereElse == theAttemptedIdentifiers.length ) {
                return ALL_FOUND_MAYBE_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY;
            } else if( theNumberFoundSomewhereElse == 0 ) {
                return PLURAL_NOT_FOUND_STRING_REPRESENTATION_KEYl;
            } else {
                return SOME_FOUND_MAYBE_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY;
            }
        }
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String [] {
                    "mb",
                    "attempted",
                    "cause"
                },
                new Object[] {
                    theMeshBaseIdentifier,
                    theAttemptedIdentifiers,
                    getCause()
                });
    }

    /**
     * Obtain the parameters with which the internationalized string
     * will be parameterized.
     *
     * @return the parameters with which the internationalized string will be parameterized
     */
    @Override
    public Object [] getLocalizationParameters()
    {
        ArrayList<MeshObjectIdentifier> notFoundIdentifiers      = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );
        ArrayList<MeshObjectIdentifier> somewhereElseIdentifiers = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );
        ArrayList<MeshObjectIdentifier> foundIdentifiers         = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );

        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( theResult[i] != null ) {
                if( theResult[i].getIdentifier().equals( theAttemptedIdentifiers[i] )) {
                    foundIdentifiers.add( theAttemptedIdentifiers[i] );
                } else {
                    somewhereElseIdentifiers.add( theAttemptedIdentifiers[i] );
                }
            } else {
                notFoundIdentifiers.add( theAttemptedIdentifiers[i] );
            }
        }

        return new Object[] {
                theAttemptedIdentifiers,
                ArrayHelper.copyIntoNewArray( notFoundIdentifiers,      MeshObjectIdentifier.class ),
                ArrayHelper.copyIntoNewArray( somewhereElseIdentifiers, MeshObjectIdentifier.class ),
                ArrayHelper.copyIntoNewArray( foundIdentifiers,         MeshObjectIdentifier.class ),
                theResult
        };
    }

    /**
     * The MeshBase in which this Exception occurred.
     */
    protected transient MeshBase theMeshBase;

    /**
     * The identifier of the MeshBase in which this Exception occurred.
     */
    protected MeshBaseIdentifier theMeshBaseIdentifier;

    /**
     * The result.
     */
    protected transient MeshObject [] theResult;

    /**
     * The identifiers of the MeshObjects that were attempted to be accessed.
     */
    protected MeshObjectIdentifier [] theAttemptedIdentifiers;

    /**
     * The number of MeshObjects that were found with the MeshObjectIdentifiers that were expected.
     */
    protected int theNumberFoundWhereExpected;

    /**
     * The number of MeshObjects that were found but with different MeshObjectIdentifiers.
     */
    protected int theNumberFoundSomewhereElse;

    /**
     * The causes for not being able to find the MeshObjects, in the same sequence as theAttemptedIdentifiers.
     */
    protected Throwable [] theCauses;

    /**
     * The number of causes, i.e. non-null entries in theCauses.
     */
    protected int theNumberCauses;

    /**
     * Entry into the ResourceHelper for StringRepresentation purposes.
     */
    public static final String SINGULAR_FOUND_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY   = "SingularFoundSomewhereElseString";

    /**
     * Entry into the ResourceHelper for StringRepresentation purposes.
     */
    public static final String SINGULAR_NOT_FOUND_STRING_REPRESENTATION_KEY              = "SingularNotFoundString";

    /**
     * Entry into the ResourceHelper for StringRepresentation purposes.
     */
    public static final String ALL_FOUND_MAYBE_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY  = "AllFoundMaybeSomewhereElseString";

    /**
     * Entry into the ResourceHelper for StringRepresentation purposes.
     */
    public static final String PLURAL_NOT_FOUND_STRING_REPRESENTATION_KEYl               = "PluralNotFoundString";

    /**
     * Entry into the ResourceHelper for StringRepresentation purposes.
     */
    public static final String SOME_FOUND_MAYBE_SOMEWHERE_ELSE_STRING_REPRESENTATION_KEY = "SomeFoundMaybeSomewhereElseString";
}
