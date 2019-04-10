package edu.cmu.cs.cs214.hw5.framework.core;
import java.math.BigDecimal;
import java.util.*;

class DisplayDataStructureImpl implements DisplayDataStructure {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> currentData;
    private Map<String, List<String>> stringKeys;
    private List<Integer> timeRange;

    /**
     * Sets up internal datastructures
     * @param tree the heirarchy of membership built by the framework
     */
    public DisplayDataStructureImpl(Map<String, Map<String, Map<Integer, BigDecimal>>> tree){
        this.currentData = new HashMap<String, Map<String, Map<Integer, BigDecimal>>>(tree);

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
     * @param config configuration object with elements to filter, if any,
     * @return tabular structure (list of columns) calculated data
     */
    public PData processFilterData(Config config) {
        Map<String,List<String>> scToRemove = config.getKeyFilter();
        List<Integer> timeToRemove = config.getTimeFilter();



        Map<String, Map<String, Map<Integer, BigDecimal>>> mapCopy = new HashMap<String, Map<String, Map<Integer, BigDecimal>>>(this.currentData);

        for(String state : scToRemove.keySet()){
            if(!mapCopy.containsKey(state)) {continue;}
            for(String county : scToRemove.get(state)) {
                mapCopy.get(state).remove(county);
            }
            //To remove a state, plugin has to remove all counties within it. Then we delete appropiately
            //once we detect that
            if(mapCopy.values().size() == 0){
                mapCopy.remove(state);
            }
        }
        for(String state : mapCopy.keySet()) {
            for(String county : mapCopy.get(state).keySet()) {
                for(Integer year : timeToRemove){
                    mapCopy.get(state).get(county).remove(year);
                }
            }
        }

        //At this point we have removed all the req
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
}
