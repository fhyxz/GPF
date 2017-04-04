package org.ncic.bioinfo.sparkseq.algorithms.utils.downsampling;

/**
 * Author: wbc
 */
public class LIBSDownsamplingInfo {
    final private boolean performDownsampling;
    final private int toCoverage;

    public LIBSDownsamplingInfo(boolean performDownsampling, int toCoverage) {
        this.performDownsampling = performDownsampling;
        this.toCoverage = toCoverage;
    }

    public boolean isPerformDownsampling() {
        return performDownsampling;
    }

    public int getToCoverage() {
        return toCoverage;
    }
}
