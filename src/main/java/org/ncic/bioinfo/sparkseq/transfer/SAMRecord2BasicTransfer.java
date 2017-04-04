package org.ncic.bioinfo.sparkseq.transfer;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.TextTagCodec;
import org.ncic.bioinfo.sparkseq.data.basic.BasicSamRecord;
import scala.collection.JavaConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class SAMRecord2BasicTransfer {
    private final static int FAKE_CONTIG_ID = 255;
    private static final String FIELD_SEPARATOR = "\t";

    private final TextTagCodec tagCodec = new TextTagCodec();

    /**
     * Write the record.
     *
     * @param alignment SAMRecord.
     */
    public BasicSamRecord transfer(final SAMRecord alignment) {
        String readName = alignment.getReadName();
        int flags = alignment.getFlags();
        int contigId = alignment.getReferenceIndex();
        if (contigId == SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX) {
            contigId = FAKE_CONTIG_ID;
        }
        String contigName = alignment.getReferenceName();
        int position = alignment.getAlignmentStart();
        int mapQ = alignment.getMappingQuality();
        String cigar = alignment.getCigarString();
        int mateContigId = alignment.getMateReferenceIndex();
        if (mateContigId == SAMRecord.NO_ALIGNMENT_REFERENCE_INDEX) {
            mateContigId = FAKE_CONTIG_ID;
        }
        String mateContigName = alignment.getMateReferenceName();
        int matePosition = alignment.getMateAlignmentStart();
        int inferredSize = alignment.getInferredInsertSize();
        byte[] sequence = alignment.getReadString().getBytes();
        byte[] quality = alignment.getBaseQualityString().getBytes();
        List<SAMRecord.SAMTagAndValue> attributes = alignment.getAttributes();
        List<String> encodedTags = new ArrayList<>(attributes.size());
        for (SAMRecord.SAMTagAndValue attribute : attributes) {
            encodedTags.add(tagCodec.encode(attribute.tag, attribute.value));
        }

        // 在这里统一不压缩，压缩操作是由scala代码中的执行引擎统一调配的。
        return BasicSamRecord.apply(false, readName, flags, contigId, contigName, position, mapQ,
                cigar, mateContigId, mateContigName, matePosition, inferredSize,
                sequence, quality, encodedTags);
    }
}
