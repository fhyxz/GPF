package org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.covariate;

import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMReadGroupRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.ReadCovariates;
import org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.RecalibrationArgumentCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: wbc
 */
public class ReadGroupCovariate implements RequiredCovariate {

    private final HashMap<String, Integer> readGroupLookupTable = new HashMap<String, Integer>();
    private final HashMap<Integer, String> readGroupReverseLookupTable = new HashMap<Integer, String>();
    private int nextId = 0;
    private String forceReadGroup;

    // Initialize any member variables using the command-line arguments passed to the walkers
    @Override
    public void initialize(final RecalibrationArgumentCollection RAC) {
        forceReadGroup = RAC.FORCE_READGROUP;
    }

    @Override
    public void recordValues(final GATKSAMRecord read, final ReadCovariates values) {
        final String readGroupId = readGroupValueFromRG(read.getReadGroup());
        final int key = keyForReadGroup(readGroupId);

        final int l = read.getReadLength();
        for (int i = 0; i < l; i++)
            values.addCovariate(key, key, key, i);
    }

    @Override
    public final Object getValue(final String str) {
        return str;
    }

    @Override
    public synchronized String formatKey(final int key) {
        // This method is synchronized so that we don't attempt to do a get()
        // from the reverse lookup table while that table is being updated
        return readGroupReverseLookupTable.get(key);
    }

    @Override
    public int keyFromValue(final Object value) {
        return keyForReadGroup((String) value);
    }

    /**
     * Get the mapping from read group names to integer key values for all read groups in this covariate
     * @return a set of mappings from read group names -> integer key values
     */
    public Set<Map.Entry<String, Integer>> getKeyMap() {
        return readGroupLookupTable.entrySet();
    }

    private int keyForReadGroup(final String readGroupId) {
        // Rather than synchronize this entire method (which would be VERY expensive for walkers like the BQSR),
        // synchronize only the table updates.

        // Before entering the synchronized block, check to see if this read group is not in our tables.
        // If it's not, either we will have to insert it, OR another thread will insert it first.
        // This preliminary check avoids doing any synchronization most of the time.
        if ( ! readGroupLookupTable.containsKey(readGroupId) ) {

            synchronized ( this ) {

                // Now we need to make sure the key is STILL not there, since another thread may have come along
                // and inserted it while we were waiting to enter this synchronized block!
                if ( ! readGroupLookupTable.containsKey(readGroupId) ) {
                    readGroupLookupTable.put(readGroupId, nextId);
                    readGroupReverseLookupTable.put(nextId, readGroupId);
                    nextId++;
                }
            }
        }

        return readGroupLookupTable.get(readGroupId);
    }

    @Override
    public synchronized int maximumKeyValue() {
        // Synchronized so that we don't query table size while the tables are being updated
        return readGroupLookupTable.size() - 1;
    }

    /**
     * If the sample has a PU tag annotation, return that. If not, return the read group id.
     *
     * @param rg the read group record
     * @return platform unit or readgroup id
     */
    private String readGroupValueFromRG(final GATKSAMReadGroupRecord rg) {
        if ( forceReadGroup != null )
            return forceReadGroup;

        final String platformUnit = rg.getPlatformUnit();
        return platformUnit == null ? rg.getId() : platformUnit;
    }

}