package org.ncic.bioinfo.sparkseq.algorithms.utils.interval;

/**
 * Author: wbc
 */
public enum IntervalMergingRule {
    ALL, // we merge both overlapping intervals and abutting intervals
    OVERLAPPING_ONLY // We merge intervals that are overlapping, but NOT ones that only abut each other
}
