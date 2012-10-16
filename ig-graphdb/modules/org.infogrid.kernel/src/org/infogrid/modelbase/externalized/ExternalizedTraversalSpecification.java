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
// Copyright 1998-2012 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.modelbase.externalized;

import java.util.ArrayList;
import org.infogrid.util.StringHelper;

/**
 * This is data wanting to become a TraversalSpecification, during reading.
 * Inner classes are the concrete subtypes.
 */
public abstract class ExternalizedTraversalSpecification
{
    /**
     * This wants to become a SequentialCompoundTraversalSpecification.
     */
    public static class Sequential
        extends
            ExternalizedTraversalSpecification
    {
        /**
         * Add to the property.
         *
         * @param newValue the new value
         */
        public void addStep(
                ExternalizedTraversalSpecification newValue ) 
        {
            steps.add( newValue );
        }

        /**
         * Get property.
         *
         * @return the value
         */
        public ArrayList<ExternalizedTraversalSpecification> getSteps()
        {
            return steps;
        }

        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
            return "Sequential TraversalSpecification with steps " + StringHelper.join( steps.iterator() );
        }

        /**
         * The steps, as ExternalizedTraversalSpecification.
         */
        protected ArrayList<ExternalizedTraversalSpecification> steps = new ArrayList<ExternalizedTraversalSpecification>();
    }

    /**
     * This wants to become a SelectiveTraversalSpecification.
     */
    public static class Selective
        extends
            ExternalizedTraversalSpecification
    {
        /**
         * Set property.
         *
         * @param newValue the new value
         */
        public void setTraversalSpecification(
                ExternalizedTraversalSpecification newValue ) 
        {
            travSpec = newValue;
        }

        /**
         * Get property.
         *
         * @return the value
         */
        public ExternalizedTraversalSpecification getTraversalSpecification()
        {
            return travSpec;
        }

        /**
         * Set property.
         *
         * @param newValue the new value
         */
        public void setStartSelector(
                ExternalizedMeshObjectSelector newValue ) 
        {
            startSelector = newValue;
        }

        /**
         * Get property.
         *
         * @return the value
         */
        public ExternalizedMeshObjectSelector getStartSelector()
        {
            return startSelector;
        }

        /**
         * Set property.
         *
         * @param newValue the new value
         */
        public void setEndSelector(
                ExternalizedMeshObjectSelector newValue ) 
        {
            endSelector = newValue;
        }

        /**
         * Get property.
         *
         * @return the value
         */
        public ExternalizedMeshObjectSelector getEndSelector()
        {
            return endSelector;
        }

        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
            StringBuilder buf = new StringBuilder();
            buf.append( "Selective TraversalSpecification of " );
            buf.append( travSpec );
            if( startSelector != null ) {
                buf.append( ", with start " );
                buf.append( startSelector );
            }
            if( endSelector != null ) {
                buf.append( ", with end " );
                buf.append( endSelector );
            }
            return buf.toString();
        }

        /**
         * The qualified ExternalizedTraversalSpecification.
         */
        protected ExternalizedTraversalSpecification travSpec;

        /**
         * The start MeshObjectSelector.
         */
        protected ExternalizedMeshObjectSelector startSelector;

        /**
         * The end MeshObjectSelector.
         */
        protected ExternalizedMeshObjectSelector endSelector;
    }

    /**
     * This wants to refer to a RoleType.
     */
    public static class Role
        extends
            ExternalizedTraversalSpecification
    {
        /**
         * Set property.
         *
         * @param newValue the new value
         */
        public void setIdentifierString(
                String newValue ) 
        {
            identifierString = newValue;
        }

        /**
         * Get property.
         *
         * @return the value
         */
        public String getIdentifierString()
        {
            return identifierString;
        }

        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
             return "Role TraversalSpecification: " + identifierString;
        }

        /**
         * Identifier of the RoleType in string form -- inefficient to convert it to an IdentifierValue.
         */
        protected String identifierString;
    }

    /**
     * This wants to refer to "StayRightHereTraversalSpecification".
     */
    public static class StayRightHere
        extends
            ExternalizedTraversalSpecification
    {
        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
             return "Stay-right-here TraversalSpecification";
        }
    }

    /**
     * This wants to refer to "AllNeighborsTraversalSpecification".
     */
    public static class AllNeighbors
        extends
            ExternalizedTraversalSpecification
    {
        /**
         * Convert to String, for user error messages.
         *
         * @return String form of this object
         */
        @Override
        public String toString()
        {
             return "All-neighbors TraversalSpecification";
        }
    }

}
