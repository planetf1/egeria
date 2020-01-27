/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import java.util.*;

/**
 * Capture the differences between classification instances.  These differences are calculated irrespective of the
 * ordering of the classification lists, and differences are based on the names of the classifications (assuming that
 * a classification with a given name can only exist once on a given entity instance).
 *
 * Note that differences in instance properties within each classification are not explicitly captured here.  If you
 * need this level of detail captured on differences, simply:
 * <ol>
 *     <li>Use the getLeft() and getRight() methods to retrieve the differing classifications from this object.</li>
 *     <li>Instantiate your own InstancePropertiesDifferences object.</li>
 *     <li>Call the check() method of the InstancePropertiesDifferences object, passing in the getProperties() result
 *          from both the left and right objects retrieved in the first step.</li>
 *     <li>The InstancePropertiesDifferences object will then contain the detailed instance properties differences
 *          between just those two differing classifications.</li>
 * </ol>
 */
public class ClassificationDifferences extends Differences
{

    /**
     * Construct a new set of differences between two instance's classifications.
     */
    public ClassificationDifferences()
    {
        super();
    }

    /**
     * Returns true if the two have any differences (are not equal), otherwise false.
     *
     * @return boolean
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences();
    }

    /**
     * Determine if there is a difference between the provided lists of classifications, and capture each of the
     * differences and similarities.  Note that this will check for differences between the classifications irrespective
     * of their ordering in each list, as this is presumably the desired behavior.
     *
     * @param left the classifications from the first instance
     * @param right the classifications from the second instance
     */
    public void check(List<Classification> left, List<Classification> right)
    {
        if (left == null && right == null)
        {
            // Do nothing, they are equal and nothing to capture
        }
        else if (left == null)
        {
            setOnlyOnOne(onlyOnRight, right);
        }
        else if (right == null)
        {
            setOnlyOnOne(onlyOnLeft, left);
        }
        else
        {
            if (left.isEmpty() && right.isEmpty())
            {
                // Do nothing...
            }
            else if (left.isEmpty())
            {
                setOnlyOnOne(onlyOnRight, right);
            }
            else if (right.isEmpty())
            {
                setOnlyOnOne(onlyOnLeft, left);
            }
            else
            {
                calculateUnorderedDifferences(left, right);
            }
        }
    }

    /**
     * Set these differing classifications on one side only.
     *
     * @param map the map into which to place the differences
     * @param classifications the classifications to add to the map
     */
    private void setOnlyOnOne(Map<String, Object> map, List<Classification> classifications)
    {
        // Note that if the provided classifications are also empty, then there are no differences to capture
        // (so if one is null and the other is empty, they will still be considered the same)
        if (!classifications.isEmpty())
        {
            for (Classification classification : classifications)
            {
                String classificationName = classification.getName();
                map.put(classificationName, classification);
            }
        }
    }

    /**
     * Calculate the differences between the two lists of classifications, neither of which is null or empty at this
     * point.  A difference in the order of the classifications will NOT be considered a difference.
     *
     * @param left the classifications from the first instance
     * @param right the classifications from the second instance
     */
    private void calculateUnorderedDifferences(List<Classification> left, List<Classification> right)
    {
        Map<String, Object> foundOnLeft = new TreeMap<>();
        // First iterate through the left side and build up a map of what is there
        for (Classification classificationLeft : left)
        {
            String classificationNameLeft = classificationLeft.getName();
            foundOnLeft.put(classificationNameLeft, classificationLeft);
        }
        // Use that map to check against what is on the right side, keeping track of any differences directly
        // in the 'onlyOnRight' and in the 'foundOnLeft' maps (removing any that are also on the right)
        for (Classification classificationRight : right)
        {
            String classificationNameRight = classificationRight.getName();
            if (foundOnLeft.containsKey(classificationNameRight))
            {
                Classification classificationLeft = (Classification) foundOnLeft.remove(classificationNameRight);
                check(classificationNameRight, classificationLeft, classificationRight);
            }
            else
            {
                onlyOnRight.put(classificationNameRight, classificationRight);
            }
        }
        // Whatever is left-over in the 'foundOnLeft' must be only on the left side
        onlyOnLeft = foundOnLeft;
    }

}
