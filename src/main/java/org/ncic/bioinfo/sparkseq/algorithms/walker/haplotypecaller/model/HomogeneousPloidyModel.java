package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.model;

import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.PloidyModel;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.SampleList;

/**
 * Author: wbc
 */
public class HomogeneousPloidyModel implements PloidyModel, SampleList {

    private SampleList sampleList;

    private final int ploidy;

    /**
     * Constructs a homogeneous ploidy model given the sample list and ploidy.
     *
     * @param samples the sample list.
     * @param ploidy the common ploidy for all samples in {@code samples}.
     *
     * @throws IllegalArgumentException if {@code samples} is {@code null},
     *    or ploidy is 0 or less.
     */
    public HomogeneousPloidyModel(final SampleList samples, final int ploidy) {
        if (ploidy <= 0)
            throw new IllegalArgumentException("does not support negative ploidy");
        this.ploidy = ploidy;

        sampleList = samples;
    }

    @Override
    public int sampleCount() {
        return sampleList.sampleCount();
    }

    @Override
    public String sampleAt(final int index) {
        return sampleList.sampleAt(index);
    }

    @Override
    public int sampleIndex(final String sample) {
        return sampleList.sampleIndex(sample);
    }

    @Override
    public int samplePloidy(final int sampleIndex) {
        checkSampleIndex(sampleIndex);
        return ploidy;
    }

    private void checkSampleIndex(final int sampleIndex) {
        if (sampleIndex < 0)
            throw new IllegalArgumentException("the sample index cannot be negative: " + sampleIndex);
        if (sampleIndex >= sampleList.sampleCount())
            throw new IllegalArgumentException("the sample index is equal or larger than the sample count: " + sampleIndex + " >= " + sampleList.sampleCount());
    }

    @Override
    public boolean isHomogeneous() {
        return true;
    }

    @Override
    public int totalPloidy() {
        return ploidy * sampleList.sampleCount();
    }

}
