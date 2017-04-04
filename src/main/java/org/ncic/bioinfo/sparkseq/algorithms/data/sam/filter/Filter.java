package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public abstract class Filter {
    public abstract boolean filterOut(SAMRecord read);

}
