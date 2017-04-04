package org.ncic.bioinfo.sparkseq.algorithms.data.sam;

import junit.framework.TestCase;
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import org.ncic.bioinfo.sparkseq.data.partition.SamRecordPartition;
import org.ncic.bioinfo.sparkseq.fileio.NormalFileLoader;
import scala.collection.immutable.List;

/**
 * Author: wbc
 */
public class TestReadSamTraverser extends TestCase {

    public void testReadSamTraverser() {
        String filePath = getClass().getResource("/test.sam").getFile();
        RefContigInfo refContigInfo = RefContigInfo.apply(getClass().getResource("/human_g1k_v37.dict").getFile());
        SamHeaderInfo headerInfo = SamHeaderInfo.sortedHeader(refContigInfo, null);
        headerInfo.addReadGroupInfo(ReadGroupInfo.apply("SRR504516", "sample1"));
        List<BasicSamRecord> samRecords = NormalFileLoader.loadSam(filePath, refContigInfo);

        SamRecordPartition samRecordPartition = new SamRecordPartition(1, 0, samRecords, headerInfo);
        SamContentProvider samContentProvider = new SamContentProvider(samRecordPartition);
        ReadSamTraverser traverser = new ReadSamTraverser(samContentProvider);

        assertEquals(true, traverser.hasNext());
        GATKSAMRecord record = traverser.next();
        assertEquals("SRR504516.1492_HWI-ST423_0087:3:1:19251:2214", record.getReadName());
        GATKSAMRecord record2 = traverser.next();
        record2 = traverser.next();
        assertEquals("SRR504516.519_HWI-ST423_0087:3:1:7608:2165", record2.getReadName());
        traverser.rewind();
        GATKSAMRecord record3 = traverser.next();
        assertEquals("SRR504516.1492_HWI-ST423_0087:3:1:19251:2214", record3.getReadName());
    }
}
