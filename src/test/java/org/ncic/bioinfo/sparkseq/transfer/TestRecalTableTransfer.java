package org.ncic.bioinfo.sparkseq.transfer;

import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReport;
import org.ncic.bioinfo.sparkseq.algorithms.walker.AbstractTestCase;

import java.util.List;

/**
 * Author: wbc
 */
public class TestRecalTableTransfer extends AbstractTestCase {

    public void testRecalTableTransfer() {
        List<String> recalTableStrings = getRecalTableLines();
        GATKReport report = GATKReportTransfer.lines2Report(recalTableStrings);
    }
}
