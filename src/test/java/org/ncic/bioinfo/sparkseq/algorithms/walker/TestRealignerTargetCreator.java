package org.ncic.bioinfo.sparkseq.algorithms.walker;

import htsjdk.samtools.SAMSequenceDictionary;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODNames;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.walker.realignertargetcreator.RealignerTargetCreator;
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition;
import org.ncic.bioinfo.sparkseq.data.partition.VcfRecordPartition;
import org.ncic.bioinfo.sparkseq.fileio.NormalFileLoader;
import org.ncic.bioinfo.sparkseq.transfer.SAMSequenceDictTransfer;
import scala.collection.immutable.List;

/**
 * Author: wbc
 */
public class TestRealignerTargetCreator extends AbstractTestCase {

    public void testTargetCreator() {
        RefContigInfo refContigInfo = RefContigInfo.apply(getClass().getResource("/human_g1k_v37.dict").getFile());
        SamHeaderInfo headerInfo = SamHeaderInfo.sortedHeader(refContigInfo, null);
        headerInfo.addReadGroupInfo(ReadGroupInfo.apply("SRR504516", "sample1"));

        SAMSequenceDictionary samSequenceDictionary = SAMSequenceDictTransfer.transfer(refContigInfo);
        GenomeLocParser parser = new GenomeLocParser(samSequenceDictionary);

        String filePath = getClass().getResource("/intervaled.sam").getFile();
        List<BasicSamRecord> samRecords = NormalFileLoader.loadSam(filePath, refContigInfo);
        SamRecordPartition samRecordPartition = new SamRecordPartition(1, 0, samRecords, headerInfo);
        SamContentProvider samContentProvider = new SamContentProvider(samRecordPartition);

        RefContentProvider refContentProvider = getRefContentProvider(samSequenceDictionary);

        VcfRecordPartition vcfRecordPartition = loadVcfPartition("/mills_indel.vcf", "MILLS_INDEL", refContigInfo);
        RODContentProvider rodContentProvider = new RODContentProvider(RODNames.KNOWN_ALLELES, vcfRecordPartition, parser);

        java.util.List<RODContentProvider> rodContentProviders = new java.util.ArrayList<>();
        rodContentProviders.add(rodContentProvider);

        RealignerTargetCreator realignerTargetCreator = new RealignerTargetCreator(
                parser, refContentProvider, samContentProvider, rodContentProviders);

        realignerTargetCreator.run();

        java.util.List<GenomeLoc> result = realignerTargetCreator.getTargetIntervals();

        // 标准答案
        java.util.List<GenomeLoc> standardResult = getRealignerTargetInterval(refContigInfo);
        assertEquals(result.size(), standardResult.size());
        for (int i = 0; i < result.size(); i++) {
            GenomeLoc res1 = result.get(i);
            GenomeLoc res2 = standardResult.get(i);
            assertEquals(res1.getContig(), res2.getContig());
            assertEquals(res1.getStart(), res2.getStart());
            assertEquals(res1.getStop(), res2.getStop());
        }
    }

}
