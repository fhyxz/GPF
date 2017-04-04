package org.ncic.bioinfo.sparkseq.algorithms.data.basic;

import java.util.HashMap;

/**
 * Author: wbc
 */
public class DefaultHashMap<K,V> extends HashMap<K,V> {

    public void setDefaultValue(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    protected V defaultValue;
    public DefaultHashMap(V defaultValue) {
        this.defaultValue = defaultValue;
    }
    @Override
    public V get(Object k) {
        V v = super.get(k);
        return ((v == null) && !this.containsKey(k)) ? this.defaultValue : v;
    }

}
