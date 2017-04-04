package org.ncic.bioinfo.sparkseq.transfer;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import junit.framework.TestCase;
import org.ncic.bioinfo.sparkseq.algorithms.walker.AbstractTestCase;
import org.ncic.bioinfo.sparkseq.algorithms.walker.TestRealignerTargetCreator;
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import org.ncic.bioinfo.sparkseq.fileio.NormalFileLoader;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: wbc
 */
public class TestSAMRecordTransfer extends AbstractTestCase {

    public void testSAMRecordTransfer() {

        RefContigInfo refContigInfo = RefContigInfo.apply(getClass().getResource("/human_g1k_v37.dict").getFile());
        SamHeaderInfo headerInfo = SamHeaderInfo.sortedHeader(refContigInfo, null);
        headerInfo.addReadGroupInfo(ReadGroupInfo.apply("SRR504516", "sample1"));

        SAMFileHeader header = SAMHeaderTransfer.transfer(headerInfo);
        Basic2SAMRecordTransfer basic2SAMRecordTransfer = new Basic2SAMRecordTransfer(header);
        SAMRecord2BasicTransfer samRecord2BasicTransfer = new SAMRecord2BasicTransfer();

        String realPath = TestRealignerTargetCreator.class.getResource("/realigned_reads.sam").getFile();
        List<BasicSamRecord> basicRecords = CollectionConverter.asJavaList(NormalFileLoader.loadSam(realPath, refContigInfo));
        List<SAMRecord> samRecords = basicRecords.stream()
                .map(record -> basic2SAMRecordTransfer.transfer(record))
                .collect(Collectors.toList());

        List<BasicSamRecord> basicRecords2 = samRecords.stream()
                .map(record -> samRecord2BasicTransfer.transfer(record))
                .collect(Collectors.toList());

        for(int i = 0; i < basicRecords.size(); i ++) {
            BasicSamRecord record1 = basicRecords.get(i);
            BasicSamRecord record2 = basicRecords2.get(i);

            assertEquals(record1.toString(), record2.toString());
        }
    }
}
