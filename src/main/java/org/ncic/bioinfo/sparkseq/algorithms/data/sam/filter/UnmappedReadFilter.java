package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public class UnmappedReadFilter extends Filter{

    @Override
    public boolean filterOut(SAMRecord read) {
        return read.getReadUnmappedFlag() || read.getAlignmentStart() == SAMRecord.NO_ALIGNMENT_START;
    }
}
