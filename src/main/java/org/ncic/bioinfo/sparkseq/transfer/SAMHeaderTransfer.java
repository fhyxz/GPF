package org.ncic.bioinfo.sparkseq.transfer;

import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMReadGroupRecord;
import htsjdk.samtools.SAMSequenceRecord;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: wbc
 */
public class SAMHeaderTransfer {

    public static SAMFileHeader transfer(SamHeaderInfo samHeaderInfo){
        SAMFileHeader header = new SAMFileHeader();
        // read groups
        List<ReadGroupInfo> readGroupInfoList = CollectionConverter.asJavaList(samHeaderInfo.getReadGroupInfos());
        List<SAMReadGroupRecord> samReadGroupRecords = readGroupInfoList.stream()
                .map(SAMReadGroupRecordTransfer::transfer)
                .collect(Collectors.toList());
        header.setReadGroups(samReadGroupRecords);
        // Sequence records
        // TODO 获取id的方法不通用 fix me
        int contigCount = samHeaderInfo.getRefContigInfo().getContigIds().size();
        for(int id = 0; id < contigCount; id ++) {
            header.addSequence(new SAMSequenceRecord(
                    samHeaderInfo.getRefContigInfo().getName(id),
                    samHeaderInfo.getRefContigInfo().getLength(id)));
        }
        if(samHeaderInfo.sorted()) {
            header.setAttribute("SO", "coordinate");
        }
        return header;
    }
}
