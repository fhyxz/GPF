package org.ncic.bioinfo.sparkseq.algorithms.utils.reports;

/**
 * Author: wbc
 */
public class GATKReportColumnFormat {
    public static enum Alignment { LEFT, RIGHT }
    private final int width;
    private final Alignment alignment;

    public GATKReportColumnFormat(int width, Alignment alignment) {
        this.width = width;
        this.alignment = alignment;
    }

    public int getWidth() {
        return width;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public String getNameFormat() {
        return "%-" + width + "s";
    }

    public String getValueFormat() {
        switch (alignment) {
            case LEFT:
                return "%-" + width + "s";
            case RIGHT:
                return "%" + width + "s";
            default:
                throw new UnsupportedOperationException("Unknown alignment: " + alignment);
        }
    }
}
