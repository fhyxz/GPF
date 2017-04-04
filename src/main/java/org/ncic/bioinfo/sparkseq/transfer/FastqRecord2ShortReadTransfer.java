package org.ncic.bioinfo.sparkseq.transfer;

import com.github.lindenb.jbwa.jni.ShortRead;
import org.ncic.bioinfo.sparkseq.compress.BaseCompressTools;
import org.ncic.bioinfo.sparkseq.compress.QualityCompressTools;
import org.ncic.bioinfo.sparkseq.data.basic.FastqPairRecord;
import org.ncic.bioinfo.sparkseq.data.basic.FastqRecord;

/**
 * Author: wbc
 */
public class FastqRecord2ShortReadTransfer {

    public static ShortRead transfer(FastqRecord record) {
        String readName = record.descriptionLine();
        byte[] seq = record.sequence();
        byte[] qual = record.quality();
        if(record.compressFlag()){
            qual = QualityCompressTools.deCompressQual(qual);
            seq = BaseCompressTools.decompressBase(seq, qual);
        }
        return new ShortRead(readName, seq, qual);
    }

    public static ShortRead transferRead1(FastqPairRecord record) {
        String readName = record.descriptionLine();
        byte[] seq = record.sequence1();
        byte[] qual = record.quality1();
        if (record.compressFlag()) {
            qual = QualityCompressTools.deCompressQual(qual);
            seq = BaseCompressTools.decompressBase(seq, qual);
        }
        return new ShortRead(readName, seq, qual);
    }

    public static ShortRead transferRead2(FastqPairRecord record) {
        String readName = record.descriptionLine();
        byte[] seq = record.sequence2();
        byte[] qual = record.quality2();
        if (record.compressFlag()) {
            qual = QualityCompressTools.deCompressQual(qual);
            seq = BaseCompressTools.decompressBase(seq, qual);
        }
        return new ShortRead(readName, seq, qual);
    }
}
