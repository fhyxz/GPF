package org.ncic.bioinfo.sparkseq.algorithms.utils.smithwaterman;

import htsjdk.samtools.Cigar;

/**
 * Author: wbc
 */
public interface SmithWaterman {

    /**
     * Get the cigar string for the alignment of this SmithWaterman class
     * @return a non-null cigar
     */
    public Cigar getCigar();

    /**
     * Get the starting position of the read sequence in the reference sequence
     * @return a positive integer >= 0
     */
    public int getAlignmentStart2wrt1();
}
