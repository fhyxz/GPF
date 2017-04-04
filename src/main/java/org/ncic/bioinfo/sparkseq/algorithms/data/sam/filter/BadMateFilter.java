package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public class BadMateFilter extends Filter {

    public boolean filterOut(final SAMRecord rec) {
        return hasBadMate(rec);
    }

    public static boolean hasBadMate(final SAMRecord rec) {
        return (rec.getReadPairedFlag() && !rec.getMateUnmappedFlag() && !rec.getReferenceIndex().equals(rec.getMateReferenceIndex()));
    }

}