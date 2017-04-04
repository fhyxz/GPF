package org.ncic.bioinfo.sparkseq.algorithms.utils;

import org.ncic.bioinfo.sparkseq.algorithms.data.sam.AlignmentContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.PileupElement;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.ReadBackedPileup;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.ReadBackedPileupImpl;
import org.ncic.bioinfo.sparkseq.exceptions.ReviewedGATKException;
import org.ncic.bioinfo.sparkseq.exceptions.UserException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: wbc
 */
public class AlignmentContextUtils {

    // Definitions:
    //   COMPLETE = full alignment context
    //   FORWARD  = reads on forward strand
    //   REVERSE  = reads on forward strand
    //
    public enum ReadOrientation { COMPLETE, FORWARD, REVERSE }

    private AlignmentContextUtils() {
        // cannot be instantiated
    }

    /**
     * Returns a potentially derived subcontext containing only forward, reverse, or in fact all reads
     * in alignment context context.
     *
     * @param context
     * @param type
     * @return
     */
    public static AlignmentContext stratify(AlignmentContext context, ReadOrientation type) {
        switch(type) {
            case COMPLETE:
                return context;
            case FORWARD:
                return new AlignmentContext(context.getLocation(),context.getBasePileup().getPositiveStrandPileup());
            case REVERSE:
                return new AlignmentContext(context.getLocation(),context.getBasePileup().getNegativeStrandPileup());
            default:
                throw new ReviewedGATKException("Unable to get alignment context for type = " + type);
        }
    }

    public static Map<String, AlignmentContext> splitContextBySampleName(AlignmentContext context) {
        return splitContextBySampleName(context, null);
    }

    /**
     * Splits the given AlignmentContext into a StratifiedAlignmentContext per sample, but referencd by sample name instead
     * of sample object.
     *
     * @param context                the original pileup
     *
     * @return a Map of sample name to StratifiedAlignmentContext
     *
     **/
    public static Map<String, AlignmentContext> splitContextBySampleName(AlignmentContext context, String assumedSingleSample) {
        GenomeLoc loc = context.getLocation();
        HashMap<String, AlignmentContext> contexts = new HashMap<String, AlignmentContext>();

        for(String sample: context.getBasePileup().getSamples()) {
            ReadBackedPileup pileupBySample = context.getBasePileup().getPileupForSample(sample);

            // Don't add empty pileups to the split context.
            if(pileupBySample.getNumberOfElements() == 0)
                continue;

            if(sample != null)
                contexts.put(sample, new AlignmentContext(loc, pileupBySample));
            else {
                if(assumedSingleSample == null) {
                    throw new UserException.ReadMissingReadGroup(pileupBySample.iterator().next().getRead());
                }
                contexts.put(assumedSingleSample,new AlignmentContext(loc, pileupBySample));
            }
        }

        return contexts;
    }

    public static Map<String, AlignmentContext> splitContextBySampleName(ReadBackedPileup pileup) {
        return splitContextBySampleName(new AlignmentContext(pileup.getLocation(), pileup));
    }


    public static AlignmentContext joinContexts(Collection<AlignmentContext> contexts) {
        // validation
        GenomeLoc loc = contexts.iterator().next().getLocation();
        for(AlignmentContext context: contexts) {
            if(!loc.equals(context.getLocation()))
                throw new ReviewedGATKException("Illegal attempt to join contexts from different genomic locations");
        }

        List<PileupElement> pe = new ArrayList<PileupElement>();
        for(AlignmentContext context: contexts) {
            for(PileupElement pileupElement: context.getBasePileup())
                pe.add(pileupElement);
        }
        return new AlignmentContext(loc, new ReadBackedPileupImpl(loc,pe));
    }
}