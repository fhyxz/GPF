package org.ncic.bioinfo.sparkseq.transfer;

import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.SAMSequenceRecord;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import scala.collection.JavaConversions;

import java.util.List;

/**
 * Author: wbc
 */
public class SAMSequenceDictTransfer {

    public static SAMSequenceDictionary transfer(RefContigInfo refContigInfo) {
        SAMSequenceDictionary dictionary = new SAMSequenceDictionary();
        List<Integer> ids = CollectionConverter.asJavaList(refContigInfo.getContigIdsInteger());
        ids.forEach(id -> {
            String name = refContigInfo.getName(id);
            int length = refContigInfo.getLength(name);
            dictionary.addSequence(new SAMSequenceRecord(name, length));
        });

        return dictionary;
    }
}
