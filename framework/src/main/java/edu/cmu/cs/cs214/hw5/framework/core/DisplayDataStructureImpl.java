package edu.cmu.cs.cs214.hw5.framework.core;
import java.math.BigDecimal;
import java.util.*;

class DisplayDataStructureImpl implements DisplayDataStructure {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> currentData;
    private Map<String, List<String>> stringKeys;
    private List<Integer> timeRange;
    private String dataLabel;

    /**
     * Sets up internal datastructures
     * @param tree the heirarchy of membership built by the framework
     */
    DisplayDataStructureImpl(Map<String, Map<String, Map<Integer, BigDecimal>>> tree, String dLabel){
        this.currentData = new HashMap<String, Map<String, Map<Integer, BigDecimal>>>(tree);
        this.dataLabel = dLabel;

        //State -> County mapping
        this.stringKeys = new HashMap<>();
        for(String state : this.currentData.keySet()){
            this.stringKeys.put(state, new ArrayList<>());
            for(String county : this.currentData.get(state).keySet()) {
                this.stringKeys.get(state).add(county);
            }
        }

        //All available years
        Set<Integer> intset = new HashSet<>();
        for(String state : this.currentData.keySet()) {
            for(String county : this.currentData.get(state).keySet()) {
                for(Integer year : this.currentData.get(state).get(county).keySet()){
                    intset.add(year);
                }
            }
        }
        this.timeRange = new ArrayList<>(intset);
    }

    /**
     * Given configuration object with elements to filter, if any,
     * return a tabular structure (list of columns) containing calculated
     * data
     * @param config configuration object with elements we want to keep, if null we
     *               keep everything
     * @return PData datastructure containing filtered data and API to extract stuff from it
     */
    public PData processFilterData(Config config) {
        if(config == null) {
            return new PData(new TreeMap<>(this.currentData));
        }
        Map<String,List<String>> scToKeep = new HashMap<>();
        List<Integer> timesToKeep = new ArrayList<>();
        scToKeep = config.getKeyFilter();
        timesToKeep = config.getTimeFilter();

        Map<String, Map<String, Map<Integer, BigDecimal>>> mapCopy = new HashMap<String, Map<String, Map<Integer, BigDecimal>>>();

        for(String state : scToKeep.keySet()){
            if(mapCopy.containsKey(state)) {continue;}
            for(String county : scToKeep.get(state)) {
                if(mapCopy.get(state) == null) {
                    mapCopy.put(state, new TreeMap<>());
                }
                mapCopy.get(state).put(county, new TreeMap<>(this.currentData.get(state).get(county)));
            }
        }
        for(String state : mapCopy.keySet()) {
            Set<String> counties = new HashSet(mapCopy.get(state).keySet());
            for(String county : counties) {
                Set<Integer> years = new HashSet<Integer>(mapCopy.get(state).get(county).keySet());
                for(Integer year : years){
                    if(!timesToKeep.contains(year)){
                        mapCopy.get(state).get(county).remove(year);
                    }
                }
                if(mapCopy.get(state).get(county).keySet().size() == 0) {
                    mapCopy.get(state).remove(county);
                }
            }
        }
        return new PData(mapCopy);
    }

    /**
     * Provides states and plugin can filter
     * @return mapping of states and counties in source data
     */
    public Map<String, List<String>> getAvailableKeys() {
        return this.stringKeys;
    }

    /**
     * Provides times in which data was collected by source
     * @return list of times (year) of data recordings
     */
    public List<Integer> getTimeRanges() {
        Collections.sort(this.timeRange);
        return this.timeRange;
    }

    /**
     * Returns the description of the numeric data
     * @return string represnting description
     */
    public String getValueDescription() {
        return this.dataLabel;
    }
}
