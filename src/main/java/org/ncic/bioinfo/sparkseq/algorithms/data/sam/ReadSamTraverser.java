package org.ncic.bioinfo.sparkseq.algorithms.data.sam;

import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.FilterUtils;
import org.ncic.bioinfo.sparkseq.exceptions.GATKException;

import java.util.ArrayList;

/**
 * Author: wbc
 */
public class ReadSamTraverser {

    private final ArrayList<GATKSAMRecord> samList;
    private int index = 0;
    private final int readCount;

    public ReadSamTraverser(SamContentProvider samContentProvider) {
        this(samContentProvider, new FilterUtils());
    }

    public ReadSamTraverser(SamContentProvider samContentProvider, FilterUtils filterUtils) {

        samList = new ArrayList<>();
        samContentProvider.getGatksamRecords().forEach(record -> {
            if(filterUtils.filter(record)) {
                samList.add(record);
            }
        });

        readCount = samList.size();
        rewind();
    }

    /**
     * 将遍历器调整到初始状态，从头开始遍历
     */
    public void rewind() {
        index = 0;
    }

    public boolean hasNext() {
        return index < readCount;
    }

    public GATKSAMRecord next() {
        if (!hasNext()) {
            throw new GATKException("Out of bound when traverse sam content provider");
        }
        return samList.get(index++);
    }
}
