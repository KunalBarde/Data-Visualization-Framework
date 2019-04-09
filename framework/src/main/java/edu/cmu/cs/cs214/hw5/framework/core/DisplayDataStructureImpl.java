package edu.cmu.cs.cs214.hw5.framework.core;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

class DisplayDataStructureImpl implements DisplayDataStructure {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> currentData;

    public DisplayDataStructureImpl(Map<String, Map<String, Map<Integer, BigDecimal>>> tree){
        this.currentData = tree;
    }

    /**
     * Given configuration object with elements to filter, if any,
     * return a tabular structure (list of columns) containing calculated
     * data
     * @param config configuration object with elements to filter, if any,
     * @return tabular structure (list of columns) calculated data
     */
    public List<Column> processFilterData(Config config) {
        Map<String,List<String>> scToRemove = config.getKeyFilter();
        List<Integer> timeToRemove = config.getTimeFilter();

        for(String state : scToRemove.keySet()){
            if(!this.currentData.containsKey(state)) {continue;}
            for(String county : scToRemove.get(state)) {
                this.currentData.get(state).remove(county);
            }
            //To remove a state, plugin has to remove all counties within it. Then we delete appropiately
            //once we detect that
            if(this.currentData.values().size() == 0){
                this.currentData.remove(state);
            }
        }
        for(String state : this.currentData.keySet()) {
            for(String county : this.currentData.get(state).keySet()) {
                for(Integer year : timeToRemove){
                    this.currentData.get(state).get(county).remove(year);
                }
            }
        }
        return null;
    }

    /**
     * Provides states and plugin can filter
     * @return mapping of states and counties in source data
     */
    public Map<String, List<String>> getAvailableKeys() {return null;}

    /**
     * Provides times in which data was collected by source
     * @return list of times (year) of data recordings
     */
    public List<Integer> getTimeRanges() {return null;}
}
