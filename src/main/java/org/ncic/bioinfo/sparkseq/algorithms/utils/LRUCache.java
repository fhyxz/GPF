package org.ncic.bioinfo.sparkseq.algorithms.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Author: wbc
 */
public class LRUCache<K,V> extends LinkedHashMap<K,V> {
    private int capacity; // Maximum number of items in the cache.

    public LRUCache(int capacity) {
        super(capacity+1, 1.0f, true); // Pass 'true' for accessOrder.
        this.capacity = capacity;
    }

    protected boolean removeEldestEntry(final Map.Entry entry) {
        return (size() > this.capacity);
    }
}
