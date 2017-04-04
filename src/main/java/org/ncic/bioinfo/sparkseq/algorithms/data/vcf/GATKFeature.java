package org.ncic.bioinfo.sparkseq.algorithms.data.vcf;

import htsjdk.tribble.Feature;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLoc;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.utils.HasGenomeLocation;

/**
 * Author: wbc
 */
public abstract class GATKFeature implements Feature, HasGenomeLocation {

    public GATKFeature(String name) {
        this.name = name;
    }

    String name;

    protected void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract GenomeLoc getLocation();

    // TODO: this should be a Feature
    public abstract Object getUnderlyingObject();

    /**
     * wrapping a Tribble feature in a GATK friendly interface
     */
    public static class TribbleGATKFeature extends GATKFeature {
        private final GenomeLocParser genomeLocParser;
        private final Feature feature;
        private GenomeLoc position = null;

        public TribbleGATKFeature(GenomeLocParser genomeLocParser,Feature f, String name) {
            super(name);
            this.genomeLocParser = genomeLocParser;
            feature = f;
        }
        public GenomeLoc getLocation() {
            if (position == null) position = genomeLocParser.createGenomeLoc(feature.getChr(), feature.getStart(), feature.getEnd());
            return position;
        }

        /** Return the features reference sequence name, e.g chromosome or contig */
        @Override
        public String getChr() {
            return feature.getChr();
        }

        /** Return the start position in 1-based coordinates (first base is 1) */
        @Override
        public int getStart() {
            return feature.getStart();
        }

        /**
         * Return the end position following 1-based fully closed conventions.  The length of a feature is
         * end - start + 1;
         */
        @Override
        public int getEnd() {
            return feature.getEnd();
        }

        // TODO: this should be a Feature, actually
        public Object getUnderlyingObject() {
            return feature;
        }
    }
}
