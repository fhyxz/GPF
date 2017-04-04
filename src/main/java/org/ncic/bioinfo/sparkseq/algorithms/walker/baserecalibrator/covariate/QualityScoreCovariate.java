package org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.covariate;

import org.ncic.bioinfo.sparkseq.algorithms.utils.QualityUtils;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.ReadCovariates;
import org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator.RecalibrationArgumentCollection;

/**
 * Author: wbc
 */
public class QualityScoreCovariate implements RequiredCovariate {

    // Initialize any member variables using the command-line arguments passed to the walkers
    @Override
    public void initialize(final RecalibrationArgumentCollection RAC) {}

    @Override
    public void recordValues(final GATKSAMRecord read, final ReadCovariates values) {
        final byte[] baseQualities = read.getBaseQualities();
        final byte[] baseInsertionQualities = read.getBaseInsertionQualities();
        final byte[] baseDeletionQualities = read.getBaseDeletionQualities();

        for (int i = 0; i < baseQualities.length; i++) {
            values.addCovariate((int)baseQualities[i], (int)baseInsertionQualities[i], (int)baseDeletionQualities[i], i);
        }
    }

    // Used to get the covariate's value from input csv file during on-the-fly recalibration
    @Override
    public final Object getValue(final String str) {
        return Byte.parseByte(str);
    }

    @Override
    public String formatKey(final int key) {
        return String.format("%d", key);
    }

    @Override
    public int keyFromValue(final Object value) {
        return (value instanceof String) ? (int)Byte.parseByte((String) value) : (int)(Byte) value;
    }

    @Override
    public int maximumKeyValue() {
        return QualityUtils.MAX_SAM_QUAL_SCORE;
    }
}