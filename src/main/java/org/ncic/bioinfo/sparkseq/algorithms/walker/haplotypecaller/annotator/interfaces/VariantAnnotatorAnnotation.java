package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator.interfaces;

import htsjdk.variant.vcf.VCFHeaderLine;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator.*;

import java.util.List;
import java.util.Set;

/**
 * Author: wbc
 */
public abstract class VariantAnnotatorAnnotation {
    // return the INFO keys
    public abstract List<String> getKeyNames();

    // initialization method (optional for subclasses, and therefore non-abstract)
    public void initialize (AnnotatorCompatible walker, GenomeLocParser parser, Set<VCFHeaderLine> headerLines ) { }
}