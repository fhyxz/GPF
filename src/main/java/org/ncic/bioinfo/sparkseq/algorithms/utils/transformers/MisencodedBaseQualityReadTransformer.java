package org.ncic.bioinfo.sparkseq.algorithms.utils.transformers;

import org.ncic.bioinfo.sparkseq.algorithms.engine.Walker;
import org.ncic.bioinfo.sparkseq.algorithms.utils.QualityUtils;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.exceptions.UserException;

import java.util.Map;

/**
 * Author: wbc
 */
public class MisencodedBaseQualityReadTransformer extends ReadTransformer {

    private static final int samplingFrequency = 1000;  // sample 1 read for every 1000 encountered
    private static final int encodingFixValue = 31;  // Illumina_64 - PHRED_33

    private boolean disabled;
    private boolean fixQuals;
    private boolean ALLOW_POTENTIALLY_MISENCODED_QUALS;
    protected static int currentReadCounter = 0;

    @Override
    public void initializeSub(Map<String, Object> args, final Walker walker) {
        fixQuals = (Boolean)args.get("fixQuals");
        ALLOW_POTENTIALLY_MISENCODED_QUALS = (Boolean) args.get("ALLOW_POTENTIALLY_MISENCODED_QUALS");
        disabled = !fixQuals && ALLOW_POTENTIALLY_MISENCODED_QUALS;
    }

    @Override
    public boolean enabled() {
        return !disabled;
    }

    @Override
    public GATKSAMRecord apply(final GATKSAMRecord read) {
        if ( fixQuals )
            return fixMisencodedQuals(read);

        checkForMisencodedQuals(read);
        return read;
    }

    protected static GATKSAMRecord fixMisencodedQuals(final GATKSAMRecord read) {
        final byte[] quals = read.getBaseQualities();
        for ( int i = 0; i < quals.length; i++ ) {
            quals[i] -= encodingFixValue;
            if ( quals[i] < 0 )
                throw new UserException.BadInput("while fixing mis-encoded base qualities we encountered a read that was correctly encoded; we cannot handle such a mixture of reads so unfortunately the BAM must be fixed with some other tool");
        }
        read.setBaseQualities(quals);
        return read;
    }

    protected static void checkForMisencodedQuals(final GATKSAMRecord read) {
        // sample reads randomly for checking
        if ( ++currentReadCounter >= samplingFrequency ) {
            currentReadCounter = 0;

            final byte[] quals = read.getBaseQualities();
            for ( final byte qual : quals ) {
                if ( qual > QualityUtils.MAX_REASONABLE_Q_SCORE )
                    throw new UserException.MisencodedBAM(read, "we encountered an extremely high quality score of " + (int)qual);
            }
        }
    }
}
