package org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter;

import htsjdk.samtools.SAMRecord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: wbc
 */
public class FilterUtils {

    static final String PACKAGE_NAME = "org.ncic.bioinfo.sparkseq.algorithms.data.sam.filter.";
    public static final String DUPLICATE_READ_FILTER = PACKAGE_NAME + "DuplicateReadFilter";
    public static final String FAILS_VENDOR_QUALITY_CHECK_FILTER = PACKAGE_NAME + "FailsVendorQualityCheckFilter";
    public static final String HC_MAPPING_QUALITY_FILTER = PACKAGE_NAME + "HCMappingQualityFilter";
    public static final String MAPPING_QUALITY_UNAVAILABLE_FILTER = PACKAGE_NAME + "MappingQualityUnavailableFilter";
    public static final String NOT_PRIMARY_ALIGNMENT_FILTER = PACKAGE_NAME + "NotPrimaryAlignmentFilter";
    public static final String UNMAPPED_READ_FILTER = PACKAGE_NAME + "UnmappedReadFilter";

    List<Filter> filters = new ArrayList<>();
    Set<String> filterNames = new HashSet<>();

    public FilterUtils() {

    }

    public FilterUtils(String[] filterNames) {
        super();
        addFilter(filterNames);
    }

    public void addFilter(Filter filter) {
        if (!filterNames.contains(filter.getClass().getSimpleName())) {
            filters.add(filter);
        }
    }

    public void addFilter(String filterName) {
        try {
            Filter filter = (Filter) (Class.forName(filterName).newInstance());
            addFilter(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFilter(String[] filterNames) {
        for (String filterName : filterNames) {
            addFilter(filterName);
        }
    }

    /**
     * Judge if a read pass all the filters. It should not be added into a pileup if return false
     *
     * @param read read to check
     * @return if it shouldn't pass all the filter, false is returned
     */
    public boolean filter(SAMRecord read) {
        for (Filter f : filters) {
            if (f.filterOut(read))
                return false;
        }
        return true;
    }
}
