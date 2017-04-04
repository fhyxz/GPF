package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

/**
 * Author: wbc
 */
public class HCMappingQualityFilter extends Filter{
    public int MIN_MAPPING_QUALTY_SCORE = 20;

    @Override
    public boolean filterOut(SAMRecord read) {
        return (read.getMappingQuality() < MIN_MAPPING_QUALTY_SCORE);
    }
}
