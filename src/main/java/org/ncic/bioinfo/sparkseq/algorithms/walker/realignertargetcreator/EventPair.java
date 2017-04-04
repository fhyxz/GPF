package org.ncic.bioinfo.sparkseq.algorithms.walker.realignertargetcreator;

import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;

import java.util.TreeSet;

/**
 * Author: wbc
 */
public class EventPair {
    public Event left, right;
    public TreeSet<GenomeLoc> intervals = new TreeSet<GenomeLoc>();

    public EventPair(Event left, Event right) {
        this.left = left;
        this.right = right;
    }

    public EventPair(Event left, Event right, TreeSet<GenomeLoc> set1, TreeSet<GenomeLoc> set2) {
        this.left = left;
        this.right = right;
        intervals.addAll(set1);
        intervals.addAll(set2);
    }
}