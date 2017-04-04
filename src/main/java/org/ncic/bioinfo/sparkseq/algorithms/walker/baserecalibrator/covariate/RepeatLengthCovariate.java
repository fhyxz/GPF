package org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.covariate;

/**
 * Author: wbc
 */
public class RepeatLengthCovariate extends RepeatCovariate {

    protected String getCovariateValueFromUnitAndLength(final byte[] repeatFromUnitAndLength, final int repeatLength) {
        return String.format("%d",repeatLength);
    }

    @Override
    public synchronized int maximumKeyValue() {
        // Synchronized so that we don't query table size while the tables are being updated
        //return repeatLookupTable.size() - 1;
        // max possible values of covariate: for repeat unit, length is up to MAX_STR_UNIT_LENGTH,
        // so we have 4^MAX_STR_UNIT_LENGTH * MAX_REPEAT_LENGTH possible values
        return (1+MAX_REPEAT_LENGTH);
    }

}
