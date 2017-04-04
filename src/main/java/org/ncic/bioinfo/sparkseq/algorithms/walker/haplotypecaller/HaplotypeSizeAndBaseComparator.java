package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller;

import org.ncic.bioinfo.sparkseq.algorithms.utils.haplotype.Haplotype;

import java.util.Comparator;

/**
 * Author: wbc
 */
public class HaplotypeSizeAndBaseComparator implements Comparator<Haplotype> {
    @Override
    public int compare(final Haplotype hap1, final Haplotype hap2) {
        if (hap1.getBases().length < hap2.getBases().length)
            return -1;
        else if (hap1.getBases().length > hap2.getBases().length)
            return 1;
        else
            return hap1.getBaseString().compareTo(hap2.getBaseString());
    }
}
