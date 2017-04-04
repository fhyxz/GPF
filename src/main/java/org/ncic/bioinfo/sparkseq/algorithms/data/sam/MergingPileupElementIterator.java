package org.ncic.bioinfo.sparkseq.algorithms.data.sam;

import htsjdk.samtools.util.PeekableIterator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Author: wbc
 */
class MergingPileupElementIterator<PE extends PileupElement> implements Iterator<PE> {
    private final PriorityQueue<PeekableIterator<PE>> perSampleIterators;

    public MergingPileupElementIterator(PerSamplePileupElementTracker<PE> tracker) {
        perSampleIterators = new PriorityQueue<PeekableIterator<PE>>(Math.max(1,tracker.getSamples().size()),new PileupElementIteratorComparator());
        for(final String sample: tracker.getSamples()) {
            PileupElementTracker<PE> trackerPerSample = tracker.getElements(sample);
            if(trackerPerSample.size() != 0)
                perSampleIterators.add(new PeekableIterator<PE>(trackerPerSample.iterator()));
        }
    }

    public boolean hasNext() {
        return !perSampleIterators.isEmpty();
    }

    public PE next() {
        PeekableIterator<PE> currentIterator = perSampleIterators.remove();
        PE current = currentIterator.next();
        if(currentIterator.hasNext())
            perSampleIterators.add(currentIterator);
        return current;
    }

    public void remove() {
        throw new UnsupportedOperationException("Cannot remove from a merging iterator.");
    }

    /**
     * Compares two peekable iterators consisting of pileup elements.
     */
    private class PileupElementIteratorComparator implements Comparator<PeekableIterator<PE>> {
        public int compare(PeekableIterator<PE> lhs, PeekableIterator<PE> rhs) {
            return rhs.peek().getOffset() - lhs.peek().getOffset();
        }
    }
}
