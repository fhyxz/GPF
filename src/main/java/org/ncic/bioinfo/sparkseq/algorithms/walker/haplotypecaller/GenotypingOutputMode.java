package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

/**
 * Author: wbc
 */
public enum GenotypingOutputMode {

    /**
     * The genotyper will choose the most likely alternate allele
     */
    DISCOVERY,

    /**
     * Only the alleles passed by the user should be considered.
     */
    GENOTYPE_GIVEN_ALLELES
}
