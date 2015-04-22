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

package org.infogrid.model.primitives;

/**
 * Thrown if a non-existing EnumeratedValue within an EnumeratedDataType is selected.
 */
public abstract class UnknownEnumeratedValueException
        extends
            NotInDomainException
{
    /**
     * Private constructor for subclasses only.
     *
     * @param type the EnumeratedDataType in which it could not be found
     */
    protected UnknownEnumeratedValueException(
            EnumeratedDataType type )
    {
        super( type );
    }

    /**
     * Allow subclasses to override which key to use in the Resource file for the string representation.
     *
     * @return the key
     */
    @Override
    protected String findStringRepresentationParameter()
    {
        return findParameterViaEnclosingClass( STRING_REPRESENTATION_KEY );
    }

    /**
     * Subclass that indicates the key could not be found.
     */
    public static class Key
            extends
                UnknownEnumeratedValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param type the EnumeratedDataType in which it could not be found
         * @param key the key that could not be found
         */
        public Key(
                EnumeratedDataType type,
                String             key )
        {
            super( type );

            theKey = key;
        }

        /**
         * Obtain the key that was not found.
         *
         * @return the key
         */
        public String getKey()
        {
            return theKey;
        }

        /**
         * Obtain resource parameters for the internationalization.
         *
         * @return the resource parameters
         */
        @Override
        public Object [] getLocalizationParameters()
        {
            return new Object[] { theType, theKey };
        }

        /**
         * The key that was not found.
         */
        protected String theKey;
    }

    /**
     * Subclass that indicates that the user-visible name could not be found.
     */
    public static class UserVisible
            extends
                UnknownEnumeratedValueException
    {
        private static final long serialVersionUID = 1L; // helps with serialization

        /**
         * Constructor.
         *
         * @param type the EnumeratedDataType in which it could not be found
         * @param userVisible the user-visible name that could not be found
         */
        public UserVisible(
                EnumeratedDataType type,
                String             userVisible )
        {
            super( type );

            theUserVisibleName = userVisible;
        }

        /**
         * Obtain the user-visible name that was not found.
         *
         * @return the user-visible name
         */
        public String getUserVisibleName()
        {
            return theUserVisibleName;
        }

        /**
         * Obtain resource parameters for the internationalization.
         *
         * @return the resource parameters
         */
        @Override
        public Object [] getLocalizationParameters()
        {
            return new Object[] { theType, theUserVisibleName };
        }

        /**
         * The user-visible name that was not found.
         */
        protected String theUserVisibleName;
    }
}
