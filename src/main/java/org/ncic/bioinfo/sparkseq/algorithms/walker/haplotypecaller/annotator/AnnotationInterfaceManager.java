package org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator;

import org.ncic.bioinfo.sparkseq.algorithms.utils.DeprecatedToolChecks;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator.interfaces.GenotypeAnnotation;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator.interfaces.InfoFieldAnnotation;
import org.ncic.bioinfo.sparkseq.exceptions.GATKException;
import org.ncic.bioinfo.sparkseq.exceptions.UserException;
import sun.reflect.annotation.AnnotationType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Author: wbc
 */
public class AnnotationInterfaceManager {
    private static final String NULL_ANNOTATION_NAME = "none";
    private static final String NULL_ANNOTATION_GROUP_NAME = "none";
    private static final String packageName = "org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.annotator.";

    public static List<InfoFieldAnnotation> createInfoFieldAnnotations(List<String> infoFieldAnnotations) {
        List<InfoFieldAnnotation> annotations = new ArrayList<>();
        try {
            for (String name : infoFieldAnnotations) {
                annotations.add((InfoFieldAnnotation) Class.forName(packageName + name).newInstance());
            }
        } catch (Exception e) {
            throw new GATKException("Can't find annotation");
        }
        return annotations;
    }

    public static List<GenotypeAnnotation> createGenotypeAnnotations(List<String> genotypeAnnotations) {
        List<GenotypeAnnotation> annotations = new ArrayList<>();
        try {
            for (String name : genotypeAnnotations) {
                annotations.add((GenotypeAnnotation) Class.forName(packageName + name).newInstance());
            }
        } catch (Exception e) {
            throw new GATKException("Can't find annotation");
        }
        annotations.add(new DepthPerAlleleBySample());
        annotations.add(new DepthPerSampleHC());
        annotations.add(new StrandBiasBySample());
        return annotations;
    }
}
