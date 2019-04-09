package edu.cmu.cs.cs214.hw5.framework.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PData {
    private Map<String, Map<String, Map<Integer, BigDecimal>>> internals;

    PData(Map<String, Map<String, Map<Integer, BigDecimal>>> data) {
        this.internals = data;
    }

    /**
     * returns list of year-value pairs for desired state
     * @param state
     * @return
     */
    public List<STtuple> getStateData(String state){
        return null;
    }

    /**
     * returns list of state_county data in form year-value
     * @param county
     * @return
     */
    public List<STtuple> getCountyData(String state, String county){
        return null;
    }

    public List<BigDecimal> getYearData(Integer year){
        return null;
    }

    /**
     * returns avg for given state
     * @param state
     * @return
     */
    public BigDecimal getStateAvg(String state){
        return null;
    }
}


