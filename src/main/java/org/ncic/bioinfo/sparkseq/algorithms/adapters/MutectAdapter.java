package org.ncic.bioinfo.sparkseq.algorithms.adapters;

import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFHeader;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.header.StandardWGSVCFHeader;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.walker.mutect.Mutect;
import org.ncic.bioinfo.sparkseq.data.basic.VcfRecord;
import org.ncic.bioinfo.sparkseq.data.common.Locus;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.partition.FastaPartition;
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition;
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition;
import org.ncic.bioinfo.sparkseq.transfer.SAMSequenceDictTransfer;
import org.ncic.bioinfo.sparkseq.transfer.VC2VcfRecordTransfer;
import scala.collection.JavaConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class MutectAdapter {
    public static List<VcfRecord> callVariants(RefContigInfo refContigInfo,
                                               SamRecordPartition tumorSamRecordPartition,
                                               SamRecordPartition normalSamRecordPartition,
                                               FastaPartition refPartition,
                                               List<VcfRecordPartition> rodPartitions,
                                               List<Locus> intervals) {
        SAMSequenceDictionary samSequenceDictionary = SAMSequenceDictTransfer.transfer(refContigInfo);
        GenomeLocParser parser = new GenomeLocParser(samSequenceDictionary);

        SamContentProvider tumorSamContentProvider = new SamContentProvider(tumorSamRecordPartition);
        SamContentProvider normalSamContentProvider = new SamContentProvider(normalSamRecordPartition);
        RefContentProvider refContentProvider = new RefContentProvider(samSequenceDictionary, refPartition);

        List<RODContentProvider> rodContentProviders = new java.util.ArrayList<>();
        rodPartitions.forEach(
                rodPartition -> rodContentProviders.add(
                        new RODContentProvider(rodPartition.key(), rodPartition, parser))
        );

        List<GenomeLoc> intervalLocus = new ArrayList<>();
        GenomeLoc traverseLocus = refContentProvider.getLocus();
        intervals.forEach(
                locus -> {
                    GenomeLoc interval = new GenomeLoc(locus.contigName(), locus.contigId(), locus.start(), locus.stop());
                    if (interval.overlapsP(traverseLocus)) {
                        intervalLocus.add(interval);
                    }
                }
        );

        Mutect mutect = new Mutect(parser, refContentProvider,
                tumorSamContentProvider, normalSamContentProvider, rodContentProviders, intervalLocus);

        List<VariantContext> finalResult = mutect.getResultVCFRecords();

        VCFHeader header = StandardWGSVCFHeader.getHeader();
        VC2VcfRecordTransfer transfer = new VC2VcfRecordTransfer(header, refContigInfo);
        List<VcfRecord> vcfRecords = new ArrayList<>(finalResult.size());
        finalResult.forEach(vc -> vcfRecords.add(transfer.transfer(vc)));
        return vcfRecords;
    }
}
