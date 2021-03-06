package org.ncic.bioinfo.sparkseq.algorithms.walker.baserecalibrator;

import org.ncic.bioinfo.sparkseq.algorithms.utils.MathUtils;
import org.ncic.bioinfo.sparkseq.algorithms.utils.QualityUtils;
import org.ncic.bioinfo.sparkseq.algorithms.utils.RecalUtils;
import org.ncic.bioinfo.sparkseq.algorithms.data.basic.NestedIntegerArray;
import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReportTable;

import java.util.Arrays;
import java.util.List;

/**
 * Author: wbc
 */
public class QuantizationInfo {
    private List<Byte> quantizedQuals;
    private List<Long> empiricalQualCounts;
    private int quantizationLevels;

    private QuantizationInfo(List<Byte> quantizedQuals, List<Long> empiricalQualCounts, int quantizationLevels) {
        this.quantizedQuals = quantizedQuals;
        this.empiricalQualCounts = empiricalQualCounts;
        this.quantizationLevels = quantizationLevels;
    }

    public QuantizationInfo(List<Byte> quantizedQuals, List<Long> empiricalQualCounts) {
        this(quantizedQuals, empiricalQualCounts, calculateQuantizationLevels(quantizedQuals));
    }

    public QuantizationInfo(final RecalibrationTables recalibrationTables, final int quantizationLevels) {
        final Long [] qualHistogram = new Long[QualityUtils.MAX_SAM_QUAL_SCORE +1]; // create a histogram with the empirical quality distribution
        for (int i = 0; i < qualHistogram.length; i++)
            qualHistogram[i] = 0L;

        final NestedIntegerArray<RecalDatum> qualTable = recalibrationTables.getQualityScoreTable(); // get the quality score table

        for (final RecalDatum value : qualTable.getAllValues()) {
            final RecalDatum datum = value;
            final int empiricalQual = MathUtils.fastRound(datum.getEmpiricalQuality()); // convert the empirical quality to an integer ( it is already capped by MAX_QUAL )
            qualHistogram[empiricalQual] += (long) datum.getNumObservations(); // add the number of observations for every key
        }
        empiricalQualCounts = Arrays.asList(qualHistogram); // histogram with the number of observations of the empirical qualities
        quantizeQualityScores(quantizationLevels);

        this.quantizationLevels = quantizationLevels;
    }


    public void quantizeQualityScores(int nLevels) {
        QualQuantizer quantizer = new QualQuantizer(empiricalQualCounts, nLevels, QualityUtils.MIN_USABLE_Q_SCORE); // quantize the qualities to the desired number of levels
        quantizedQuals = quantizer.getOriginalToQuantizedMap(); // map with the original to quantized qual map (using the standard number of levels in the RAC)
    }

    public void noQuantization() {
        this.quantizationLevels = QualityUtils.MAX_SAM_QUAL_SCORE;
        for (int i = 0; i < this.quantizationLevels; i++)
            quantizedQuals.set(i, (byte) i);
    }

    public List<Byte> getQuantizedQuals() {
        return quantizedQuals;
    }

    public int getQuantizationLevels() {
        return quantizationLevels;
    }

    private static int calculateQuantizationLevels(List<Byte> quantizedQuals) {
        byte lastByte = -1;
        int quantizationLevels = 0;
        for (byte q : quantizedQuals) {
            if (q != lastByte) {
                quantizationLevels++;
                lastByte = q;
            }
        }
        return quantizationLevels;
    }

    public GATKReportTable generateReportTable(boolean sortByCols) {
        GATKReportTable quantizedTable;
        if(sortByCols) {
            quantizedTable = new GATKReportTable(RecalUtils.QUANTIZED_REPORT_TABLE_TITLE, "Quality quantization map", 3, GATKReportTable.TableSortingWay.SORT_BY_COLUMN);
        }   else {
            quantizedTable = new GATKReportTable(RecalUtils.QUANTIZED_REPORT_TABLE_TITLE, "Quality quantization map", 3);
        }
        quantizedTable.addColumn(RecalUtils.QUALITY_SCORE_COLUMN_NAME);
        quantizedTable.addColumn(RecalUtils.QUANTIZED_COUNT_COLUMN_NAME);
        quantizedTable.addColumn(RecalUtils.QUANTIZED_VALUE_COLUMN_NAME);

        for (int qual = 0; qual <= QualityUtils.MAX_SAM_QUAL_SCORE; qual++) {
            quantizedTable.set(qual, RecalUtils.QUALITY_SCORE_COLUMN_NAME, qual);
            quantizedTable.set(qual, RecalUtils.QUANTIZED_COUNT_COLUMN_NAME, empiricalQualCounts.get(qual));
            quantizedTable.set(qual, RecalUtils.QUANTIZED_VALUE_COLUMN_NAME, quantizedQuals.get(qual));
        }
        return quantizedTable;
    }
}
