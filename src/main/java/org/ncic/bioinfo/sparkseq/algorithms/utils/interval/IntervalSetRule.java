package org.ncic.bioinfo.sparkseq.algorithms.utils.interval;

/**
 * Author: wbc
 */
public enum IntervalSetRule {
    /** Take the union of all intervals */
    UNION,
    /** Take the intersection of intervals (the subset that overlaps all intervals specified) */
    INTERSECTION;
}
