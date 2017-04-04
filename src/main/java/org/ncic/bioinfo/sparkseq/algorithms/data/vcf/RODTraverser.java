package org.ncic.bioinfo.sparkseq.algorithms.data.vcf;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.exceptions.GATKException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: wbc
 */
public class RODTraverser {

    private final RODContentProvider rodContentProvider;
    private final ArrayList<GATKFeature> features;
    private final int size;
    private final String name;

    private LinkedList<GATKFeature> buffer = new LinkedList<>();
    private int lastLocStartInTraverse = -1;
    private int curIdxToAdd = 0;

    public RODTraverser(RODContentProvider rodContentProvider) {
        this.rodContentProvider = rodContentProvider;
        features = rodContentProvider.getFeatures();
        size = features.size();
        name = rodContentProvider.getName();
        init();
    }

    public void init() {
        buffer.clear();
        lastLocStartInTraverse = -1;
        curIdxToAdd = 0;
    }

    public RODRecordList getOverlap(GenomeLoc loc) {
        if (loc.getStart() < lastLocStartInTraverse) {
            throw new GATKException("Not in sequence when traverse ROD");
        }

        while (!buffer.isEmpty()) {
            GATKFeature head = buffer.getFirst();
            if (head.getLocation().isBefore(loc)) {
                buffer.removeFirst();
            } else {
                break;
            }
        }

        while (curIdxToAdd < size) {
            GATKFeature feature = features.get(curIdxToAdd);
            if(feature.getLocation().isBefore(loc)) {
                curIdxToAdd ++;
                continue;
            } else if(feature.getLocation().isPast(loc)) {
                break;
            } else if(feature.getLocation().overlapsP(loc)) {
                buffer.add(feature);
                curIdxToAdd ++;
            }
        }

        List<GATKFeature> overlappedFeatures = new ArrayList<>();
        for(GATKFeature feature : buffer) {
            if(feature.getLocation().overlapsP(loc)) {
                overlappedFeatures.add(feature);
            }
        }
        return new RODRecordListImpl(name, overlappedFeatures, loc);
    }


}
