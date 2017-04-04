package org.ncic.bioinfo.sparkseq.algorithms.adapters;

import junit.framework.TestCase;
import org.ncic.bioinfo.sparkseq.algorithms.walker.TestRealignerTargetCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class TestBQSRGather extends TestCase {

    public void testBQSRGather() {
        List<List<String>> bqsrSources = new ArrayList<>();
        bqsrSources.add(getRecalTableLines("/bqsrtables/0_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/1_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/2_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/3_bqsr.table"));

        BQSRTableGather bqsrTableGather = new BQSRTableGather();

        List<String> mergedTableLines = bqsrTableGather.gatherBQSRTables(bqsrSources);
        List<String> standardTableLines = getRecalTableLines("/bqsrtables/merged.table");
        for (int i = 0; i < mergedTableLines.size(); i++) {
            assertEquals(mergedTableLines.get(i), standardTableLines.get(i));
        }
    }

    public void testParallelBQSRGather() {
        List<List<String>> bqsrSources = new ArrayList<>();
        bqsrSources.add(getRecalTableLines("/bqsrtables/0_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/1_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/2_bqsr.table"));
        bqsrSources.add(getRecalTableLines("/bqsrtables/3_bqsr.table"));

        BQSRTableGather bqsrTableGather = new BQSRTableGather();

        List<String> mergedTableLines = bqsrTableGather.gatherBQSRTablesInParallel(bqsrSources, 12, Integer.MAX_VALUE);
        List<String> standardTableLines = getRecalTableLines("/bqsrtables/merged.table");
        for (int i = 0; i < mergedTableLines.size(); i++) {
            assertEquals(mergedTableLines.get(i), standardTableLines.get(i));
        }
    }

    private static List<String> getRecalTableLines(String filePath) {
        java.util.List<String> res = new ArrayList<>();
        String realignedSamPath = TestRealignerTargetCreator.class.getResource(filePath).getFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(realignedSamPath)))) {
            String line = reader.readLine();
            while (line != null) {
                res.add(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }
}
