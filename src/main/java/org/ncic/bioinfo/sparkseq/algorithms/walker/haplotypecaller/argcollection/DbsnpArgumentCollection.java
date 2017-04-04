package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.argcollection;

import htsjdk.variant.variantcontext.VariantContext;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODNames;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RodBinding;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.Tags;

/**
 * Author: wbc
 */
public class DbsnpArgumentCollection {

    /**
     * A dbSNP VCF file.
     */
    public RodBinding<VariantContext> dbsnp = new RodBinding<VariantContext>(VariantContext.class,
            RODNames.DBSNP, "", "VCF", new Tags());;

}