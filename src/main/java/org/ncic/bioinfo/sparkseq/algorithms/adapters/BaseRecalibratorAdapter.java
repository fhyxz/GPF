package org.ncic.bioinfo.sparkseq.algorithms.adapters;

import htsjdk.samtools.SAMSequenceDictionary;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODNames;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReport;
import org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.BaseRecalibrator;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.partition.FastaPartition;
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition;
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition;
import org.ncic.bioinfo.sparkseq.transfer.GATKReportTransfer;
import org.ncic.bioinfo.sparkseq.transfer.SAMSequenceDictTransfer;
import scala.collection.JavaConversions;

import java.util.List;

/**
 * Author: wbc
 */
public class BaseRecalibratorAdapter {

    public static List<String> getRecalTableLines(RefContigInfo refContigInfo,
                                                  SamRecordPartition samRecordPartition,
                                                  FastaPartition refPartition,
                                                  List<VcfRecordPartition> rodPartitions) {
        SAMSequenceDictionary samSequenceDictionary = SAMSequenceDictTransfer.transfer(refContigInfo);
        GenomeLocParser parser = new GenomeLocParser(samSequenceDictionary);

        SamContentProvider samContentProvider = new SamContentProvider(samRecordPartition);
        RefContentProvider refContentProvider = new RefContentProvider(samSequenceDictionary, refPartition);

        List<RODContentProvider> rodContentProviders = new java.util.ArrayList<>();
        rodPartitions.forEach(
                rodPartition -> rodContentProviders.add(
                        new RODContentProvider(RODNames.KNOWN_ALLELES + rodPartition.key(), rodPartition, parser))
        );

        // 生成recal table
        BaseRecalibrator baseRecalibrator = new BaseRecalibrator(
                parser, refContentProvider, samContentProvider, rodContentProviders);
        baseRecalibrator.run();

        GATKReport bqsrTable = baseRecalibrator.getReport();

        return GATKReportTransfer.report2Lines(bqsrTable);
    }
}
