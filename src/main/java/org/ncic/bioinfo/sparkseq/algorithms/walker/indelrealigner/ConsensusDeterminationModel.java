package org.ncic.bioinfo.sparkseq.algorithms.walker.indelrealigner;

/**
 * Author: wbc
 */
public enum ConsensusDeterminationModel {
    /**
     * Uses only indels from a provided ROD of known indels.
     */
    KNOWNS_ONLY,
    /**
     * Additionally uses indels already present in the original alignments of the reads.
     */
    USE_READS,
    /**
     * Additionally uses 'Smith-Waterman' to generate alternate consenses.
     */
    USE_SW
}
