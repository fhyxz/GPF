package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.utils.QualityUtils;

/**
 * Author: wbc
 */
public class MappingQualityUnavailableFilter extends Filter{
    @Override
    public boolean filterOut(SAMRecord read){
        return read.getMappingQuality() == QualityUtils.MAPPING_QUALITY_UNAVAILABLE;
    }
}
