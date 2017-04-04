package org.ncic.bioinfo.sparkseq.compress;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import org.ncic.bioinfo.sparkseq.algorithms.walker.AbstractTestCase;
import org.ncic.bioinfo.sparkseq.data.basic.FastqRecord;
import org.ncic.bioinfo.sparkseq.fileio.NormalFileLoader;
import scala.collection.JavaConversions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Author: wbc
 */
public class TestCompressRate extends AbstractTestCase {

    /*public void testCompressRate() {
        List<FastqRecord> fastq1 = JavaConversions.asJavaList(NormalFileLoader.loadFastq("D:/test1.fastq"));
        List<FastqRecord> fastq2 = JavaConversions.asJavaList(NormalFileLoader.loadFastq("D:/test1.fastq"));
        System.out.println("Total reads:" + fastq1.size()*2);

        Kryo kryo = new Kryo();
        kryo.setReferences(false);

        long totalRawBase = 0;
        long totalCompressedBase = 0;
        long totalRawQual = 0;
        long totalCompressedQual = 0;
        long totalKryoBase = 0;
        long totalKryoQual = 0;
        long totalDesc = 0;

        for(FastqRecord record : fastq1) {
            byte[] sequence = record.sequence();
            byte[] quality = record.quality();

            byte[] compressSequenceBytes = BaseCompressTools.compressBase(sequence, quality);
            byte[] compressQualityBytes = QualityCompressTools.compressQual(quality);

            byte[] serialSequenceBytes = serializationObject(kryo, sequence);
            byte[] serialQualBytes = serializationObject(kryo, quality);

            totalDesc += record.descriptionLine().length();
            totalRawBase += sequence.length;
            totalCompressedBase += compressSequenceBytes.length;
            totalKryoBase += serialSequenceBytes.length;
            totalRawQual += quality.length;
            totalCompressedQual += compressQualityBytes.length;
            totalKryoQual += serialQualBytes.length;
        }

        for(FastqRecord record : fastq2) {
            byte[] sequence = record.sequence();
            byte[] quality = record.quality();

            byte[] compressSequenceBytes = BaseCompressTools.compressBase(sequence, quality);
            byte[] compressQualityBytes = QualityCompressTools.compressQual(quality);

            byte[] serialSequenceBytes = serializationObject(kryo, sequence);
            byte[] serialQualBytes = serializationObject(kryo, quality);

            totalDesc += record.descriptionLine().length();
            totalRawBase += sequence.length;
            totalCompressedBase += compressSequenceBytes.length;
            totalKryoBase += serialSequenceBytes.length;
            totalRawQual += quality.length;
            totalCompressedQual += compressQualityBytes.length;
            totalKryoQual += serialQualBytes.length;
        }

        System.out.println("Total description line:" + totalDesc);
        System.out.println("Total raw base:" + totalRawBase);
        System.out.println("Total compressed base:" + totalCompressedBase);
        System.out.println("Total serial base:" + totalKryoBase);
        System.out.println("Base compress rate:" + (float)totalCompressedBase / (float)totalRawBase);
        System.out.println("Total raw qual:" + totalRawQual);
        System.out.println("Total compressed qual:" + totalCompressedQual);
        System.out.println("Total serial qual:" + totalKryoQual);
        System.out.println("Qual compress rate:" + (float)totalCompressedQual / (float)totalRawQual);

    }

    private byte[] serializationObject(Kryo kryo, byte[] obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }*/
}
