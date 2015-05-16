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
 * Thrown if something went wrong when trying to access one or more MeshObjects using
 * {@link MeshBase#accessLocally}.
 *
 * This Exception inherits from PartialResultException,
 * which means that if we attempt to access N MeshObjects, we may have been successful
 * for some of them. To determine which were successful, this Exception carries both
 * an array of identifiers of the MeshObjects that were attempted to be accessed, and an
 * array with MeshObjects that constitutes the result, in the same sequence.
 * Similarly, it can carry an exception for each of the attempted identifiers, in the
 * same sequence, to indicate what went wrong for a particular MeshObject.
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
     * @param attemptedIdentifiers the MeshObjectIdentifiers that were attempted to be accessed
     * @param results a partial result, available at the time the Exception occurred
     * @param seeOthers MeshObjectIdentifiers of the MeshObjects to be accessed instead (aka redirect)
     * @param causes the underlying causes for the Exception, in the same order as the attemptedIdentifiers
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshObjectIdentifier [] attemptedIdentifiers,
            MeshObject []           results,
            MeshObjectIdentifier [] seeOthers,
            Throwable []            causes )
    {
        this( mb, mb.getIdentifier(), attemptedIdentifiers, results, seeOthers, causes, null );
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param attemptedIdentifiers the MeshObjectIdentifiers that were attempted to be accessed
     * @param results a partial result, available at the time the Exception occurred
     * @param seeOthers MeshObjectIdentifiers of the MeshObjects to be accessed instead (aka redirect)
     * @param causes the underlying causes for the Exception, in the same order as the attemptedIdentifiers
     * @param cause the underlying cause for this Exception, if there is one unrelated to the individual causes
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshObjectIdentifier [] attemptedIdentifiers,
            MeshObject []           results,
            MeshObjectIdentifier [] seeOthers,
            Throwable []            causes,
            Throwable               cause )
    {
        this( mb, mb.getIdentifier(), attemptedIdentifiers, results, seeOthers, causes, cause );
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param mbIdentifier the MeshBaseIdentifier of the MeshBase in which the Exception occurred
     * @param attemptedIdentifiers the MeshObjectIdentifiers that were attempted to be accessed
     * @param results a partial result, available at the time the Exception occurred
     * @param seeOthers MeshObjectIdentifiers of the MeshObjects to be accessed instead (aka redirect)
     * @param causes the underlying causes for the Exception, in the same order as the attemptedIdentifiers
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshBaseIdentifier      mbIdentifier,
            MeshObjectIdentifier [] attemptedIdentifiers,
            MeshObject []           results,
            MeshObjectIdentifier [] seeOthers,
            Throwable []            causes )
    {
        this( mb, mbIdentifier, attemptedIdentifiers, results, seeOthers, causes, null );
    }

    /**
     * Constructor.
     *
     * @param mb the MeshBase in which the Exception occurred
     * @param mbIdentifier the MeshBaseIdentifier of the MeshBase in which the Exception occurred
     * @param attemptedIdentifiers the MeshObjectIdentifiers that were attempted to be accessed
     * @param results a partial result, available at the time the Exception occurred
     * @param seeOthers MeshObjectIdentifiers of the MeshObjects to be accessed instead (aka redirect)
     * @param causes the underlying causes for the Exception, in the same order as the attemptedIdentifiers
     * @param cause the underlying cause for this Exception, if there is one unrelated to the individual causes
     */
    public MeshObjectAccessException(
            MeshBase                mb,
            MeshBaseIdentifier      mbIdentifier,
            MeshObjectIdentifier [] attemptedIdentifiers,
            MeshObject []           results,
            MeshObjectIdentifier [] seeOthers,
            Throwable []            causes,
            Throwable               cause )
    {
        super( null, cause ); // avoid construction of default message

        theMeshBase             = mb;
        theMeshBaseIdentifier   = mbIdentifier;
        theAttemptedIdentifiers = attemptedIdentifiers;
        theResults              = results;
        theSeeOthers            = seeOthers;
        theCauses               = causes;

        theNumberFoundWhereExpected = 0;
        theNumberFoundSomewhereElse = 0;
        theNumberCauses             = 0;

        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( theResults[i] != null ) {
                ++theNumberFoundWhereExpected;

            } else if( theSeeOthers[i] != null ) {
                ++theNumberFoundSomewhereElse;

            } else if( theCauses[i] != null ) {
                ++theNumberCauses;
            }
        }
        if( theNumberFoundWhereExpected + theNumberFoundSomewhereElse + theNumberCauses == 0 && cause == null ) {
            throw new IllegalArgumentException( "This Exception must not be thrown unless at least something was found or Exceptions can be reported" );
        }
        if( theAttemptedIdentifiers.length != theResults.length ) {
            throw new IllegalArgumentException( "Inconsistent invocation: " + theAttemptedIdentifiers.length + " vs. " + theResults.length );
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
     * Obtain the identifier of the MeshBase in which the Exception occurred.
     *
     * @return the identifier of the MeshBase in which the Exception occurred
     */
    public MeshBaseIdentifier getMeshBaseIdentifier()
    {
        return theMeshBaseIdentifier;
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
        return theResults;
    }

    /**
     * Obtain the see others, if any. (aka redirects)
     *
     * @return the see others, if any.
     */
    public MeshObjectIdentifier [] getSeeOther()
    {
        return theSeeOthers;
    }

    /**
     * Obtain the see other for a given MeshObjectIdentifier.
     *
     * @param key the MeshObjectIdentifier
     * @return the redirect value, if any
     */
    public MeshObjectIdentifier getSeeOtherFor(
            MeshObjectIdentifier key )
    {
        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( key.equals( theAttemptedIdentifiers[i] )) {
                return theSeeOthers[i];
            }
        }
        throw new IllegalArgumentException( "Unknown key: " + key.toExternalForm() );
    }

    /**
     * Obtain the attempted MeshObjectIdentifiers.
     *
     * @return the MeshObjectIdentifiers
     */
    public MeshObjectIdentifier [] getAttemptedMeshObjectIdentifiers()
    {
        return theAttemptedIdentifiers;
    }

    /**
     * Obtain the causes, if any.
     *
     * @return the causes, if any
     */
    public Throwable [] getCauses()
    {
        return theCauses;
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
                    "results (identifiers)",
                    "seeOthers",
                    "causes",
                    "cause"
                },
                new Object[] {
                    theMeshBaseIdentifier,
                    theAttemptedIdentifiers,
                    ArrayHelper.identifiersOf( theResults ),
                    theSeeOthers,
                    theCauses,
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
        ArrayList<MeshObjectIdentifier> foundIdentifiers         = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );
        ArrayList<MeshObjectIdentifier> notFoundIdentifiers      = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );
        ArrayList<MeshObjectIdentifier> somewhereElseIdentifiers = new ArrayList<MeshObjectIdentifier>( theAttemptedIdentifiers.length );

        for( int i=0 ; i<theAttemptedIdentifiers.length ; ++i ) {
            if( theResults[i] != null ) {
                foundIdentifiers.add( theAttemptedIdentifiers[i] );
            } else if( theSeeOthers[i] != null ) {
                somewhereElseIdentifiers.add( theAttemptedIdentifiers[i] );
            } else {
                notFoundIdentifiers.add( theAttemptedIdentifiers[i] );
            }
        }

        if( theAttemptedIdentifiers.length == 1 ) {
            return new Object[] {
                    theAttemptedIdentifiers[0],
                    theResults,
                    theSeeOthers,
                    theCauses,
                    foundIdentifiers.isEmpty()         ? null :         foundIdentifiers.get( 0 ),
                    somewhereElseIdentifiers.isEmpty() ? null : somewhereElseIdentifiers.get( 0 ),
                    notFoundIdentifiers.isEmpty()      ? null :      notFoundIdentifiers.get( 0 ),
                    getCause()
            };
        } else {
            return new Object[] {
                    theAttemptedIdentifiers,
                    theResults,
                    theSeeOthers,
                    theCauses,
                    ArrayHelper.copyIntoNewArray( foundIdentifiers,         MeshObjectIdentifier.class ),
                    ArrayHelper.copyIntoNewArray( somewhereElseIdentifiers, MeshObjectIdentifier.class ),
                    ArrayHelper.copyIntoNewArray( notFoundIdentifiers,      MeshObjectIdentifier.class ),
                    getCause()
            };
        }
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
     * The identifiers of the MeshObjects that were attempted to be accessed.
     */
    protected MeshObjectIdentifier [] theAttemptedIdentifiers;

    /**
     * The result of the access, in the same sequence as theAttemptedIdentifiers.
     */
    protected transient MeshObject [] theResults;

    /**
     * Alternate MeshObjectIdentifiers where the MeshObjects can be found.
     */
    protected MeshObjectIdentifier [] theSeeOthers;

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
