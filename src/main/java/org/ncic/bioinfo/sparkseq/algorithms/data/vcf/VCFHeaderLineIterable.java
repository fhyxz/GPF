package org.ncic.bioinfo.sparkseq.algorithms.data.vcf;

import htsjdk.samtools.util.AbstractIterator;
import htsjdk.samtools.util.CloserUtil;
import htsjdk.tribble.readers.LineIterator;
import org.ncic.bioinfo.sparkseq.data.common.VcfHeaderInfo;
import org.ncic.bioinfo.sparkseq.transfer.CollectionConverter;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Author: wbc
 */
public class VCFHeaderLineIterable extends AbstractIterator<String> implements LineIterator, Closeable {

    private final Iterator<String> vcfLinesIter;

    public VCFHeaderLineIterable(final VcfHeaderInfo vcfHeaderInfo) {
        this.vcfLinesIter = CollectionConverter.asJavaList(vcfHeaderInfo.getHeaderLines()).iterator();
    }

    public VCFHeaderLineIterable(String[] vcfHeaderLines) {
        this.vcfLinesIter = Arrays.asList(vcfHeaderLines).iterator();
    }

    @Override
    protected String advance() {
        if (!vcfLinesIter.hasNext()) {
            return null;
        } else {
            return vcfLinesIter.next();
        }
    }

    @Override
    public void close() throws IOException {
        CloserUtil.close(vcfLinesIter);
    }
}
