package org.ncic.bioinfo.sparkseq.algorithms;

import htsjdk.samtools.SAMSequenceDictionary;
import org.ncic.bioinfo.sparkseq.algorithms.adapters.IndelRealignAdapter;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.walker.AbstractTestCase;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import org.ncic.bioinfo.sparkseq.data.partition.FastaPartition;
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition;
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition;
import org.ncic.bioinfo.sparkseq.debug.Dumper;
import org.ncic.bioinfo.sparkseq.transfer.SAMSequenceDictTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class TestDebug extends AbstractTestCase {

    /*public void testDebug() {
        RefContigInfo refContigInfo = RefContigInfo.apply(getClass().getResource("/human_g1k_v37.dict").getFile());
        SamHeaderInfo headerInfo = SamHeaderInfo.sortedHeader(refContigInfo, null);
        headerInfo.addReadGroupInfo(ReadGroupInfo.apply("SRR504516", "sample1"));

        SamRecordPartition samRecordPartition = Dumper.dedumpSamRecordPartition("test_result/0_sam_dedupedSam", refContigInfo);
        FastaPartition fastaPartition = Dumper.deDumpRefPartition("test_result/0_ref");
        VcfRecordPartition indel1000G = Dumper.deDumpRODPartitions("test_result/0_rod_1000G_phase1.indels", refContigInfo);
        VcfRecordPartition dbsnp = Dumper.deDumpRODPartitions("test_result/0_rod_dbsnp", refContigInfo);
        VcfRecordPartition millsIndel = Dumper.deDumpRODPartitions("test_result/0_rod_Mills_and_1000G_gold_standard.indels", refContigInfo);

        List<VcfRecordPartition> rods = new ArrayList<>();
        rods.add(indel1000G);
        rods.add(millsIndel);
        rods.add(dbsnp);
        IndelRealignAdapter.realign(refContigInfo, samRecordPartition, fastaPartition, rods);
    }*/
}
