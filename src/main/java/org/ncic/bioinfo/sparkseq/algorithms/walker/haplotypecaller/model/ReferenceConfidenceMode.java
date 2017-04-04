package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.model;

/**
 * Author: wbc
 */
public enum ReferenceConfidenceMode {

    /**
     * Regular calling without emitting reference confidence calls.
     */
    NONE,

    /**
     * Reference model emitted site by site.
     */
    BP_RESOLUTION,

    /**
     * Reference model emitted with condensed non-variant blocks, i.e. the GVCF format.
     */
    GVCF;
}
