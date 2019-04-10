package edu.cmu.cs.cs214.hw5.framework.core;
import java.util.List;
import java.util.Map;

public class Config {
    private List<Integer> timeFilter;
    private Map<String, List<String>> keyFilter;

    public Config(List<Integer> timeFilter, Map<String, List<String>> keyFilter) {
        this.timeFilter = timeFilter;
        this.keyFilter = keyFilter;
    }

    List<Integer> getTimeFilter() {
        return timeFilter;
    }

    Map<String, List<String>> getKeyFilter() {
        return keyFilter;
    }
}
