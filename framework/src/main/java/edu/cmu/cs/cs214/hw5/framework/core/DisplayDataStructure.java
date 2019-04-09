package edu.cmu.cs.cs214.hw5.framework.core;

import java.util.List;
import java.util.Map;

public interface DisplayDataStructure {

    /**
     * Given configuration object with elements to filter, if any,
     * return a tabular structure (list of columns) containing calculated
     * data
     * @param obj configuration object with elements to filter, if any,
     * @return tabular structure (list of columns) calculated data
     */
    PData processFilterData(Config obj);

    /**
     * Provides states and plugin can filter
     * @return mapping of states and counties in source data
     */
    Map<String, List<String>> getAvailableKeys();

    /**
     * Provides times in which data was collected by source
     * @return list of times (year) of data recordings
     */
    List<Integer> getTimeRanges();
}
