package org.ncic.bioinfo.sparkseq.algorithms.walker;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMSequenceDictionary;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.GATKSAMRecord;
import org.ncic.bioinfo.sparkseq.algorithms.data.sam.SamContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.data.vcf.RODContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.utils.GenomeLocParser;
import org.ncic.bioinfo.sparkseq.algorithms.data.reference.RefContentProvider;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegionFinder;
import org.ncic.bioinfo.sparkseq.algorithms.walker.haplotypecaller.ActiveRegionMapData;
import org.ncic.bioinfo.sparkseq.data.common.ReadGroupInfo;
import org.ncic.bioinfo.sparkseq.data.common.RefContigInfo;
import org.ncic.bioinfo.sparkseq.data.common.SamHeaderInfo;
import org.ncic.bioinfo.sparkseq.transfer.SAMHeaderTransfer;
import org.ncic.bioinfo.sparkseq.transfer.SAMSequenceDictTransfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wbc
 */
public class TestActiveRegionFinder extends AbstractTestCase {

    public void testActiveRegionFinder() {
        RefContigInfo refContigInfo = RefContigInfo.apply(getClass().getResource("/human_g1k_v37.dict").getFile());
        SamHeaderInfo headerInfo = SamHeaderInfo.sortedHeader(refContigInfo, null);
        headerInfo.addReadGroupInfo(ReadGroupInfo.apply("SRR504516", "sample1"));

        SAMSequenceDictionary samSequenceDictionary = SAMSequenceDictTransfer.transfer(refContigInfo);
        GenomeLocParser parser = new GenomeLocParser(samSequenceDictionary);

        List<SAMRecord> recaledReads = getRecaledReads(headerInfo);
        List<GATKSAMRecord> realignedGATKRecords = new ArrayList<>();
        for (SAMRecord record : recaledReads) {
            realignedGATKRecords.add(new GATKSAMRecord(record));
        }
        SamContentProvider samContentProvider = new SamContentProvider(realignedGATKRecords, SAMHeaderTransfer.transfer(headerInfo));

        RefContentProvider refContentProvider = getRefContentProvider(samSequenceDictionary);

        java.util.List<RODContentProvider> rodContentProviders = new java.util.ArrayList<>();

        ActiveRegionFinder activeRegionFinder = new ActiveRegionFinder(parser, refContentProvider, samContentProvider, rodContentProviders, true);

        activeRegionFinder.run();

        List<ActiveRegionMapData> activeRegionMapDataList = activeRegionFinder.getResultActiveRegions();

        List<ActiveRegionInfo> standardActiveRegionInfo = getStandardActiveRegionInfo();
        assertEquals(standardActiveRegionInfo.size(), activeRegionMapDataList.size());
        for(int i = 0; i < standardActiveRegionInfo.size(); i ++) {
            ActiveRegionMapData mapData = activeRegionMapDataList.get(i);
            ActiveRegionInfo info = standardActiveRegionInfo.get(i);
            assertEquals(info.start, mapData.activeRegion.getLocation().getStart());
            assertEquals(info.end, mapData.activeRegion.getLocation().getStop());
            assertEquals(info.readCount, mapData.activeRegion.getReads().size());
        }
    }

    public static List<ActiveRegionInfo> getStandardActiveRegionInfo() {
        List<ActiveRegionInfo> result = new ArrayList<>();
        String realPath = TestRealignerTargetCreator.class.getResource("/activeRegions.txt").getFile();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(realPath)))) {
            String line = reader.readLine();
            while(line != null) {
                String[] strs = line.split(" ");
                String[] locusStrs = strs[0].split("-");
                int start = Integer.parseInt(locusStrs[0]);
                int end = Integer.parseInt(locusStrs[1]);
                int numOfReads = Integer.parseInt(strs[1]);
                result.add(new ActiveRegionInfo(start, end, numOfReads));
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static class ActiveRegionInfo {
        int start;
        int end;
        int readCount;

        ActiveRegionInfo(int start, int end, int readCount) {
            this.start = start;
            this.end = end;
            this.readCount = readCount;
        }
    }
}
