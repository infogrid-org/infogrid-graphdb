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
 * Comparison operators for PropertyValues.
 */
public enum PropertyComparisonOperator {
    EQUAL {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found == 0;
        }
    },

    NON_EQUAL {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found != 0;
        }
    },

    GREATER {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found > 0;
        }
    },

    SMALLER {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found < 0;
        }
    },

    GREATER_OR_EQUALS {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found >= 0;
        }
    },

    SMALLER_OR_EQUALS {
        public boolean compare(
                PropertyValue one,
                PropertyValue two )
        {
            int found = PropertyValue.compare( one, two );
            return found <= 0;
        }
    };

    /**
     * Compare.
     *
     * @param one the first value
     * @param two the second value
     * @return the result of the comparison
     */
    public abstract boolean compare(
            PropertyValue one,
            PropertyValue two );
}
