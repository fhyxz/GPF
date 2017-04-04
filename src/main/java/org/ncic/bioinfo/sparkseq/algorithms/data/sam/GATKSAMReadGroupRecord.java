package org.ncic.bioinfo.sparkseq.algorithms.data.sam;

import htsjdk.samtools.SAMReadGroupRecord;
import org.ncic.bioinfo.sparkseq.algorithms.utils.NGSPlatform;

/**
 * Author: wbc
 */
public class GATKSAMReadGroupRecord extends SAMReadGroupRecord {
    // the SAMReadGroupRecord data we're caching
    private String mSample = null;
    private String mPlatform = null;
    private NGSPlatform mNGSPlatform = null;

    // because some values can be null, we don't want to duplicate effort
    private boolean retrievedSample = false;
    private boolean retrievedPlatform = false;
    private boolean retrievedNGSPlatform = false;

    public GATKSAMReadGroupRecord(final String id) {
        super(id);
    }

    public GATKSAMReadGroupRecord(SAMReadGroupRecord record) {
        super(record.getReadGroupId(), record);
    }

    /**
     * Get the NGSPlatform enum telling us the platform of this read group
     * <p>
     * This function call is caching, so subsequent calls to it are free, while
     * the first time it's called there's a bit of work to resolve the enum
     *
     * @return an NGSPlatform enum value
     */
    public NGSPlatform getNGSPlatform() {
        if (!retrievedNGSPlatform) {
            mNGSPlatform = NGSPlatform.fromReadGroupPL(getPlatform());
            retrievedNGSPlatform = true;
        }

        return mNGSPlatform;
    }

    @Override
    public String toString() {
        return "GATKSAMReadGroupRecord @RG:" + getReadGroupId();
    }

    ///////////////////////////////////////////////////////////////////////////////
    // *** The following methods are overloaded to cache the appropriate data ***//
    ///////////////////////////////////////////////////////////////////////////////

    @Override
    public String getSample() {
        if (!retrievedSample) {
            mSample = super.getSample();
            retrievedSample = true;
        }
        return mSample;
    }

    @Override
    public void setSample(String s) {
        super.setSample(s);
        mSample = s;
        retrievedSample = true;
    }

    @Override
    public String getPlatform() {
        if (!retrievedPlatform) {
            mPlatform = super.getPlatform();
            retrievedPlatform = true;
        }
        return mPlatform;
    }

    @Override
    public void setPlatform(String s) {
        super.setPlatform(s);
        mPlatform = s;
        retrievedPlatform = true;
        retrievedNGSPlatform = false; // recalculate the NGSPlatform
    }

}
