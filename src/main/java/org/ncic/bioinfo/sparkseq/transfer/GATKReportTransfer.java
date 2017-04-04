package org.ncic.bioinfo.sparkseq.transfer;

import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReport;
import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReportTable;
import org.ncic.bioinfo.sparkseq.algorithms.utils.reports.GATKReportVersion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: wbc
 */
public class GATKReportTransfer {

    public static GATKReport lines2Report(List<String> lines) {
        Iterator<String> lineIter = lines.iterator();
        GATKReport report = new GATKReport();

        // version line
        String reportHeaderLine = lineIter.next();
        GATKReportVersion version = GATKReportVersion.fromHeader(reportHeaderLine);
        report.setVersion(version);

        // tables
        int nTables = Integer.parseInt(reportHeaderLine.split(":")[2]);
        // Read each table according ot the number of tables
        for (int i = 0; i < nTables; i++) {
            report.addTable(new GATKReportTable(lineIter, version));
        }

        return report;
    }

    public static List<String> report2Lines(GATKReport report) {
        List<String> lines = new ArrayList<>();
        // version line
        lines.add(GATKReport.GATKREPORT_HEADER_PREFIX + report.getVersion().toString()
                + GATKReport.SEPARATOR + report.getTables().size());

        for (GATKReportTable table : report.getTables()) {
            String[] tableLines = table.transIntoLines();
            for(String str : tableLines) {
                lines.add(str);
            }
            lines.add("");
        }
        return lines;
    }

}
