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

package org.infogrid.model.primitives.m;

import java.util.ArrayList;
import org.infogrid.mesh.MeshObject;
import org.infogrid.mesh.NotPermittedException;
import org.infogrid.model.primitives.AttributableMeshType;
import org.infogrid.model.primitives.BooleanValue;
import org.infogrid.model.primitives.EntityType;
import org.infogrid.model.primitives.EntityTypeGuard;
import org.infogrid.model.primitives.MeshTypeIdentifier;
import org.infogrid.model.primitives.RelationshipType;
import org.infogrid.model.primitives.RoleType;
import org.infogrid.model.primitives.StringValue;
import org.infogrid.modelbase.InheritanceConflictException;
import org.infogrid.util.ArrayHelper;
import org.infogrid.util.Identifier;
import org.infogrid.util.logging.CanBeDumped;
import org.infogrid.util.logging.Dumper;
import org.infogrid.util.logging.Log;

/**
  * A MeshType that does not depend on the existance of others, that can participate in
  * inheritance relationships, and may be attributed.
  * In-memory implementation.
  */
public class MEntityType
        extends
            MAttributableMeshType
        implements
            EntityType,
            CanBeDumped
{
    private static final Log  log              = Log.getLogInstance( MEntityType.class ); // our own, private logger
    private static final long serialVersionUID = 1L; // helps with serialization

    /**
     * Constructor.
     * 
     * @param identifier the Identifier of the to-be-created object
     */
    public MEntityType(
            MeshTypeIdentifier identifier )
    {
        super( identifier );
    }

    /**
     * Determine whether this object is being identified with the provided Identifier.
     * This is a useful method for those objects of type HasIdentifier that may listen
     * to multiple names.
     *
     * @param toTest the Identifier to test against
     * @return true if this HasIdentifier is being identified by the provided Identifier
     */
    @Override
    public boolean isIdentifiedBy(
            Identifier toTest )
    {
        if( super.isIdentifiedBy( toTest )) {
            return true;
        }
        for( int i=0 ; i<theSynonyms.length ; ++i ) {
            if( theSynonyms[i].equals( toTest )) {
                return true;
            }
        }
        return false;
    }

    /**
      * Set the value of the IsSignificant property.
      *
      * @param newValue the new value of the property
      * @see #getIsSignificant
      */
    public final void setIsSignificant(
            BooleanValue newValue )
    {
        this.theIsSignificant = newValue;
    }

    /**
      * Obtain the value of the IsSignificant property.
      *
      * @return the value of the IsSignificant property
      * @see #setIsSignificant
      */
    @Override
    public final BooleanValue getIsSignificant()
    {
        return theIsSignificant;
    }

    /**
     * Set the value of the MaybeUsedAsForwardReference property.
     *
     * @param newValue the new value of the property
     * @see #getMaybeUsedAsForwardReference
     */
    public final void setMaybeUsedAsForwardReference(
            BooleanValue newValue )
    {
        this.theMaybeUsedAsForwardReference = newValue;
    }

    /**
     * Obtain the value of the MaybeUsedAsForwardReference property.
     *
     * @return the value of the MaybeUsedAsForwardReference property
     * @see #setMaybeUsedAsForwardReference
     */
    public final BooleanValue getMaybeUsedAsForwardReference()
    {
        return theMaybeUsedAsForwardReference;
    }

    /**
      * Obtain all local RoleTypes in which this EntityType participates in the
      * working model. Local RoleTypes are those which identify this EntityType
      * as the source or destination of a RelationshipType, rather than one of its
      * supertypes.
      *
      * By definition, not all local RoleTypes of a EntityType can be known at
      * any point in time; so only the currently known ones are returned.
      *
      * @return the local RoleTypes in which this EntityType participates in the working model
      */
    @Override
    public final RoleType [] getLocalRoleTypes()
    {
        return theRoles;
    }

    /**
      * Obtain all RoleTypes in which this EntityType participates in the
      * working model. This includes all RoleTypes which identify this EntityType,
      * or one of its supertypes as the source or destination of a RelationshipType.
      *
      * By definition, not all local RoleTypes of a EntityType can be known at
      * any point in time; so only the currently known ones are returned.
      *
      * @return all RoleTypes in which this EntityType or one of its supertypes
      *         participates in the working model
      * @see #getAllConcreteRoleTypes
      */
    @Override
    public final RoleType [] getAllRoleTypes()
    {
        return getAllRoleTypesInternal();
    }

    /**
      * Obtain all RoleTypes in which this EntityType participates in the
      * working model, and that belong to RelationshipTypes which are not abstract.
      * This includes all RoleTypes which identify this EntityType, or one of its supertypes
      * as the source or destination of a RelationshipType.
      *
      * By definition, not all local RoleTypes of a EntityType can be known at
      * any point in time; so only the currently known ones are returned.
      *
      * @return all RoleTypes in which this EntityType or one of its supertypes
      *         participates in the working model,
      *         and which belong to RelationshipTypes which are not abstract
      * @see #getAllRoleTypes
      */
    @Override
    public final RoleType [] getAllConcreteRoleTypes()
    {
        return getAllConcreteRoleTypesInternal();
    }

    /**
      * Determine the set of RelationshipTypes that are subtypes of the specified RelationshipType,
      * that have this EntityType or one of its supertypes as a source or destination,
      * and that are "closest" in the inheritance hierarchy.
      *
      * @param supertypeRelationshipType the supertype RelationshipType
      * @return the set of RelationshipTypes
      */
    @Override
    public final RelationshipType [] obtainMostConcreteSubtypeRelated(
            RelationshipType supertypeRelationshipType )
    {
        // make sure this relationship-type applies to us
        if(    ! equalsOrIsSupertype( supertypeRelationshipType.getSource().getEntityType() )
            && ! equalsOrIsSupertype( supertypeRelationshipType.getDestination().getEntityType() ))
        {
            return new RelationshipType[ 0 ];
        }

        // look which local relationship types match

        ArrayList<MRelationshipType> almostRet = new ArrayList<MRelationshipType>();

        for( int i=0 ; i<theRoles.length ; ++i ) {
            RelationshipType current = theRoles[i].getRelationshipType();
            if( current.equalsOrIsSupertype( supertypeRelationshipType )) {
                almostRet.add( (MRelationshipType) theRoles[i].getRelationshipType() );
            }
        }

        if( ! almostRet.isEmpty() ) {
            return ArrayHelper.copyIntoNewArray( almostRet, MRelationshipType.class );
        }

        AttributableMeshType [] supertypes = super.getDirectSupertypes();
        for( int i=0 ; i<supertypes.length ; ++i ) {
            if( supertypes[i] instanceof EntityType ) {
                RelationshipType [] temp
                        = ((EntityType) supertypes[i]).obtainMostConcreteSubtypeRelated( supertypeRelationshipType );
                for( int j=0 ; j<temp.length ; ++j ) {
                    almostRet.add( (MRelationshipType) temp[j] );
                }
            }
        }
        return ArrayHelper.copyIntoNewArray( almostRet, MRelationshipType.class );
    }

    /**
     * Set the code that we insert upon code-generating. This code simply gets added
     * to the code for the class, overriding whatever it wants to override.
     *
     * @param newValue the code to be inserted
     * @see #getInheritingOverrideCode
     */
    public final void setInheritingOverrideCode(
            StringValue newValue )
    {
        theInheritingOverrideCode = newValue;
    }

    /**
     * Obtain code that the code generator inserts into the generated code for this
     * EntityType, and all of its subtypes.
     *
     * @return text in Java that will be in-lined into the generated code
     * @see #setInheritingOverrideCode
     */
    @Override
    public final StringValue getInheritingOverrideCode()
    {
        return theInheritingOverrideCode;
    }

    /**
     * Dump this object.
     *
     * @param d the Dumper to dump to
     */
    @Override
    public void dump(
            Dumper d )
    {
        d.dump( this,
                new String[] {
                    "theIdentifier",
                },
                new Object[] {
                    getIdentifier()
                });
    }

    /**
      * Internal helper to determine all RoleTypes related to this EntityType, inherited or local.
      *
      * @return all RoleTypes related to this EntityType, inherited or local
      */
    protected final RoleType [] getAllRoleTypesInternal()
    {
        ArrayList<MRoleType> ret = new ArrayList<MRoleType>();
        if( theRoles != null ) {
            for( int i=0 ; i<theRoles.length ; ++i ) {
                ret.add( theRoles[i] );
            }
        }

        AttributableMeshType [] supertypes = super.getDirectSupertypes();
        for( int i=0 ; i<supertypes.length ; ++i ) {
            if( supertypes[i] instanceof EntityType ) { // don't want MeshObjectType
                RoleType [] temp = ((MEntityType)supertypes[i]).getAllRoleTypesInternal();
                for( int j=0 ; j<temp.length ; ++j ) {
                    ret.add( (MRoleType) temp[j] );
                }
            }
        }
        return ArrayHelper.copyIntoNewArray( ret, MRoleType.class );
    }

    /**
      * Internal helper to determine all RoleTypes related to this EntityType, inherited or local,
      * that are associated with concrete RelationshipTypes.
      *
      * @return all RoleTypes related to this EntityType, inherited or local, that are associated
      * with concrete RelationshipTypes
      */
    protected final RoleType [] getAllConcreteRoleTypesInternal()
    {
        ArrayList<MRoleType> ret = new ArrayList<MRoleType>();
        if( theRoles != null ) {
            for( int i=0 ; i<theRoles.length ; ++i ) {
                ret.add( theRoles[i] );
            }
        }

        AttributableMeshType [] supertypes = super.getDirectSupertypes();
        for( int i=0 ; i<supertypes.length ; ++i ) {
            if( supertypes[i] instanceof EntityType ) { // don't want MeshObject
                RoleType [] temp = ((MEntityType)supertypes[i]).getAllRoleTypesInternal();
                for( int j=0 ; j<temp.length ; ++j ) {
                    AttributableMeshType currentMetaRel = (AttributableMeshType) temp[j].getRelationshipType();
                    if( ! currentMetaRel.getIsAbstract().value() ) {
                        ret.add( (MRoleType) temp[j] );
                    }
                }
            }
        }

        return ArrayHelper.copyIntoNewArray( ret, MRoleType.class );
    }

    /**
     * Enable a RoleType to add itself to this EntityType.
     *
     * @param theNewRoleType the RoleType to be added to this EntityType
     */
    final void addLocalRoleType(
            MRoleType theNewRoleType )
    {
        theRoles = ArrayHelper.append( theRoles, theNewRoleType, MRoleType.class );
    }

    /**
     * Set the identifiers by which this EntityType is also known.
     *
     * @param newValue the synonym identifiers
     */
    public void setSynonyms(
            MeshTypeIdentifier [] newValue )
    {
        theSynonyms = newValue;
    }

    /**
     * Obtain the identifiers by which this EntityType is also known.
     *
     * @return the identifiers by which this EntityType is also known
     */
    @Override
    public final MeshTypeIdentifier [] getSynonyms()
    {
        return theSynonyms;
    }

    /**
     * Set the set of method declarations that this EntityType makes in addition.
     *
     * @param newValue the method declarations
     */
    public final void setDeclaredMethods(
            StringValue [] newValue )
    {
        theDeclaredMethods = newValue;
    }

    /**
     * Obtain the set of method declarations that this EntityType makes in addition.
     *
     * @return Java method declarations
     */
    @Override
    public final StringValue [] getDeclaredMethods()
    {
        return theDeclaredMethods;
    }

    /**
     * Set the set of method implementations that this EntityType makes in addition.
     *
     * @param newValue the method declarations
     */
    public final void setImplementedMethods(
            StringValue [] newValue )
    {
        theImplementedMethods = newValue;
    }
    
    /**
     * Obtain the set of method implementations that this EntityType makes in addition.
     *
     * @return Java method implementations
     */
    @Override
    public final StringValue [] getImplementedMethods()
    {
        return theImplementedMethods;
    }

    /**
     * Set the set of additional interfaces that this EntityType declares.
     *
     * @param newValue the additional interfaces
     */
    public void setAdditionalInterfaces(
            String [] newValue )
    {
        theAdditionalInterfaces = newValue;
    }

    /**
     * Obtain the set of interfaces that this EntityType declares in addition.
     *
     * @return Java interface names
     */
    @Override
    public String [] getAdditionalInterfaces()
    {
        return theAdditionalInterfaces;
    }

    /**
     * Obtain the set of EntityTypeGuards locally defined on this EntityType.
     *
     * @return the set of EntityTypeGuards locally defined on this EntityType
     */
    @Override
    public EntityTypeGuard [] getLocalEntityTypeGuards()
    {
        if( theLocalEntityTypeGuards == null ) {
            
            ClassLoader loader      = getSubjectArea().getClassLoader();
            theLocalEntityTypeGuards = new EntityTypeGuard[ theLocalEntityTypeGuardClassNames.length ];

            for( int i=0 ; i<theLocalEntityTypeGuards.length ; ++i ) {
                try {
                    Class<?> clazz = Class.forName( theLocalEntityTypeGuardClassNames[i], true, loader );
                    theLocalEntityTypeGuards[i] = (EntityTypeGuard) clazz.newInstance();
                
                } catch( ClassNotFoundException ex ) {
                    log.error( ex );
                } catch( IllegalAccessException ex ) {
                    log.error( ex );
                } catch( InstantiationException ex ) {
                    log.error( ex.getCause() );
                }
            }
        }
        return theLocalEntityTypeGuards;
        
    }

    /**
     * Obtain the set of EntityTypeGuards defined on this EntityType either locally or by inheritance.
     *
     * @return the set of EntityTypeGuards defined on this EntityType either locally or by inheritance
     */
    @Override
    public EntityTypeGuard [] getAllEntityTypeGuards()
    {
        if( theAllEntityTypeGuards == null ) {
            try {
                ArrayList<EntityTypeGuard> almostRet = internalGetAllEntityTypeGuards(); // can throw InheritanceConflictException

                theAllEntityTypeGuards = ArrayHelper.copyIntoNewArray(
                        almostRet,
                        EntityTypeGuard.class );
            } finally {
                if( theAllEntityTypeGuards == null ) {
                    theAllEntityTypeGuards = new EntityTypeGuard[0];
                }
            }
        }
        return theAllEntityTypeGuards;
        
    }

    /**
     * Obtain transitive closure of EntityTypeGuard.
     *
     * @return an ArrayList containing a transitive closure of EntityTypeGuard
     * @throws InheritanceConflictException thrown if the an inheritance conflict was found
     */
    protected final ArrayList<EntityTypeGuard> internalGetAllEntityTypeGuards()
            throws
                InheritanceConflictException
    {
        ArrayList<EntityTypeGuard> ret = new ArrayList<EntityTypeGuard>( 20 ); // fudge factor
        for( EntityTypeGuard current : getLocalEntityTypeGuards() ) {
            ret.add( current );
        }

        AttributableMeshType [] supertypes = getDirectSupertypes();
        
        for( int i=0 ; i<supertypes.length ; ++i ) {
            if( supertypes[i] instanceof EntityType ) {
                EntityTypeGuard [] inherited = ((EntityType)supertypes[i]).getAllEntityTypeGuards();

                for( int j=0 ; j<inherited.length ; ++j ) {
                
                    if( !ret.contains( inherited[j] )) {
                        ret.add( inherited[j] );
                    }
                }
            }
        }

        return ret;
    }
    
    /**
     * Obtain the class names of the set of EntityTypeGuards on this EntityType.
     *
     * @return the class names of the set of EntityTypeGuards on this EntityType
     */
    @Override
    public String [] getLocalEntityTypeGuardClassNames()
    {
        return theLocalEntityTypeGuardClassNames;
    }

    /**
     * Set the class names of the set of EntityTypeGuards locally defined on this EntityType.
     *
     * @param newValue the class names of the set of EntityTypeGuards locally defined on this EntityType.
     */
    public void setLocalEntityTypeGuardClassNames(
            String [] newValue )
    {
        theLocalEntityTypeGuardClassNames = newValue;
    }
    
    /**
     * Determine whether this EntityType may be used as a ForwardReference.
     *
     * @return true if this may be used as a ForwardReference
     */
    @Override
    public BooleanValue getMayBeUsedAsForwardReference()
    {
        return theMayBeUsedAsForwardReference;
    }
    
    
    /**
     * Set whether this EntityType may be used as a ForwardReference.
     *
     * @param newValue true if this may be used as ForwardReference
     */
    public void setMayBeUsedAsForwardReference(
            BooleanValue newValue )
    {
        theMayBeUsedAsForwardReference = newValue;
    }

    /**
     * Check whether the given caller is allowed to bless an existing MeshObject
     * with this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be blessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedBless(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        for( EntityTypeGuard current : getAllEntityTypeGuards() ) {
            current.checkPermittedBless( this, subject, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to unbless an existing MeshObject
     * from this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be unblessed
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedUnbless(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        for( EntityTypeGuard current : getAllEntityTypeGuards() ) {
            current.checkPermittedUnbless( this, subject, caller );
        }
    }

    /**
     * Check whether the given caller is allowed to determine whether a given MeshObject
     * has been blessed with this EntityType. This returns silently if the caller is permitted
     * to do this, and throws a NotPermittedException if not.
     *
     * @param subject the MeshObject that is supposed to be checked
     * @param caller the MeshObject representing the caller
     * @throws NotPermittedException thrown if this caller is not permitted to do this 
     */
    @Override
    public void checkPermittedBlessedBy(
            MeshObject    subject,
            MeshObject    caller )
        throws
            NotPermittedException
    {
        for( EntityTypeGuard current : getAllEntityTypeGuards() ) {
            current.checkPermittedBlessedBy( this, subject, caller );
        }
    }

    /**
     * The RoleTypes that this EntityType plays.
     */
    private MRoleType [] theRoles = new MRoleType[0];

    /**
     * The value of the IsSignificant property.
     */
    private BooleanValue theIsSignificant;

    /**
     * The value of the MaybeUsedAsForwardReference property.
     */
    private BooleanValue theMaybeUsedAsForwardReference;

    /**
     * The inheriting override code that we insert upon code generation. This is a transient variable
     * because we don't need to carry all this code around beyond code generation.
     */
    private transient StringValue theInheritingOverrideCode;

    /**
     * The set of EntityTypeGuards on this EntityType (not a supertype), expressed as the set of class names.
     */
    private String [] theLocalEntityTypeGuardClassNames;

    /**
     * The identifiers by which this EntityType is also known.
     */
    private MeshTypeIdentifier [] theSynonyms;

    /**
     * The declared methods code.
     */
    private transient StringValue [] theDeclaredMethods;
    
    /**
     * The implemented methods code.
     */
    private transient StringValue [] theImplementedMethods;

    /**
     * The additional interfaces.
     */
    private transient String [] theAdditionalInterfaces;

    /**
     * The set of EntityTypeGuards on this EntityType (not a supertype). This is allocated when needed.
     */
    private EntityTypeGuard [] theLocalEntityTypeGuards;

    /**
     * The set of EntityTypeGuards on this EntityType (including its supertypes). This is allocated when needed.
     */
    private EntityTypeGuard [] theAllEntityTypeGuards;
    
    /**
     * May this EntityType be used as a ForwardReference.
     */
    private BooleanValue theMayBeUsedAsForwardReference;
}
