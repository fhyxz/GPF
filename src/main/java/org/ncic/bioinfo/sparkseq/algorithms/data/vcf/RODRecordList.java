package org.ncic.bioinfo.sparkseq.algorithms.data.vcf;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.HasGenomeLocation;

import java.util.List;

/**
 * Author: wbc
 */
public interface RODRecordList extends List<GATKFeature>, Comparable<RODRecordList>, HasGenomeLocation {
    GenomeLoc getLocation();

    String getName();
}
