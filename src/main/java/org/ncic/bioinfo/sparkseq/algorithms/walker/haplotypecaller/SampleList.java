package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

/**
 * Author: wbc
 */
public interface SampleList  {

    public int sampleCount();

    public int sampleIndex(final String sample);

    public String sampleAt(final int sampleIndex);
}
