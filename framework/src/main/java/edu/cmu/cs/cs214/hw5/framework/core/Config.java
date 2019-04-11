package edu.cmu.cs.cs214.hw5.framework.core;
import java.util.List;
import java.util.Map;

public class Config {
    private List<Integer> timeFilter;
    private Map<String, List<String>> keyFilter;

    /**
     * Creates configuration object instance that contains list of
     * acceptable times and acceptable states/counties for data.
     * @param timeFilter acceptable times
     * @param keyFilter acceptable mappings from states->list of counties
     */
    public Config(List<Integer> timeFilter, Map<String, List<String>> keyFilter) {
        this.timeFilter = timeFilter;
        this.keyFilter = keyFilter;
    }

    /**
     * Returns acceptable times
     * @return acceptable times
     */
    List<Integer> getTimeFilter() {
        return timeFilter;
    }

    /**
     * Returns acceptable mappings from states->list of counties
     * @return acceptable mappings from states->list of counties
     */
    Map<String, List<String>> getKeyFilter() {
        return keyFilter;
    }
}
