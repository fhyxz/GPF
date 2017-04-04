package org.ncic.bioinfo.sparkseq.algorithms.data.sam;

import org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.FilterUtils;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.exceptions.GATKException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author: wbc
 */
public class RegionSamTraverser {

    private final ArrayList<GATKSAMRecord> samList;
    private int startIdx = 0;
    private int endIdx = 0;
    private int lastLocStart = 0;
    private int lastLocStop = 0;
    private final int readCount;

    public RegionSamTraverser(SamContentProvider samContentProvider) {
        this(samContentProvider, new FilterUtils());
    }

    public RegionSamTraverser(SamContentProvider samContentProvider, FilterUtils filterUtils) {

        samList = new ArrayList<>();
        samContentProvider.getGatksamRecords().forEach(record -> {
            if (filterUtils.filter(record)) {
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
        startIdx = 0;
        endIdx = 0;
        lastLocStart = 0;
        lastLocStop = 0;
    }

    /**
     * 强烈建议每次访问的loc必须是顺序的，这样能减少查询的次数（目前版本是线性的）
     * 选出的region是startIdx和endIdx的左闭右开区间
     *
     * @param loc
     * @return
     */
    public List<GATKSAMRecord> getOverlappedReads(GenomeLoc loc) {
        if(loc.getStart() < lastLocStart || loc.getStop() < lastLocStop) {
            throw new GATKException("Locus to traverse is not in sequence");
        }
        lastLocStart = loc.getStart();
        lastLocStop = loc.getStop();
        while (startIdx < readCount && samList.get(startIdx).getAlignmentEnd() < lastLocStart) {
            startIdx++;
        }
        while (endIdx < readCount && samList.get(endIdx).getAlignmentStart() <= lastLocStop) {
            endIdx++;
        }
        if(startIdx < readCount) {
            List<GATKSAMRecord> resultRecords = new ArrayList<>(endIdx - startIdx);
            for(int i = startIdx; i < endIdx; i ++) {
                GATKSAMRecord record = samList.get(i);
                if(record.getAlignmentEnd() >= lastLocStart)    // 需要再次判断，因为有更短的read可能会没有overlap
                resultRecords.add(samList.get(i));
            }
            return resultRecords;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
