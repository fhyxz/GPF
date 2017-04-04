package org.ncic.bioinfo.sparkseq.compress;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Author: wbc
 */
public class TestKryo extends TestCase {

    public void testKyro() {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        //byte[] test = new byte[10];
        //kryo.register(test.getClass(), new JavaSerializer());

        byte[] sequence = "TGGGATGAGAGCATGAGAAGGTGGAGCTAAGGTGGGAGACCGTCTACCCCCGACCCTGTGTGGTGCACTGACCGTGACTCTCTGCACCTTCTCGTGGGGGA".getBytes();
        byte[] quality = "DCDE8EFFFFBEEEEFGGGGEFEGGGFGGGFF6FF/>@A?EEEE=DCAC5C@@@B=8B64A########################################".getBytes();

        byte[] serialSequence = serializationObject(kryo, sequence);
        byte[] serialQual = serializationObject(kryo, quality);
        System.out.println(sequence.length + " " + serialSequence.length);
        System.out.println(quality.length + " " + serialQual.length);
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
    }
}
