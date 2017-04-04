package org.ncic.bioinfo.sparkseq.transfer;

import scala.collection.mutable.ListBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wbc on 3/16/17.
 */
public class CollectionConverter {

    public static <T> List<T> asJavaList(scala.collection.Iterable<T> raw) {
        scala.collection.Iterator<T> iter = raw.iterator();
        List<T> result = new ArrayList<T>(raw.size());
        while (iter.hasNext()) {
            T next = iter.next();
            result.add(next);
        }
        return result;

    }

}
