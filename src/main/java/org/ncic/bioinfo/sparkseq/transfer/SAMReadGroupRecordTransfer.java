package org.ncic.bioinfo.sparkseq.transfer;

import htsjdk.samtools.SAMReadGroupRecord;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;

/**
 * Author: wbc
 */
public class SAMReadGroupRecordTransfer {

    public static SAMReadGroupRecord transfer(ReadGroupInfo readGroupInfo) {
        SAMReadGroupRecord record = new SAMReadGroupRecord(readGroupInfo.id());
        record.setSample(readGroupInfo.sample());
        record.setLibrary(readGroupInfo.lib());
        record.setPlatform(readGroupInfo.platform());
        record.setPlatformUnit(readGroupInfo.platformUnit());
        return record;
    }

}
